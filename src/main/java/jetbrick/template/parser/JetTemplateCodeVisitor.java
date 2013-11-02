package jetbrick.template.parser;

import java.lang.reflect.*;
import java.util.*;
import javax.lang.model.SourceVersion;
import jetbrick.template.JetEngine;
import jetbrick.template.parser.code.*;
import jetbrick.template.parser.grammer.*;
import jetbrick.template.parser.grammer.JetTemplateParser.*;
import jetbrick.template.parser.support.*;
import jetbrick.template.resource.Resource;
import jetbrick.template.runtime.JetContext;
import jetbrick.template.utils.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Visitor 模式访问器，用来生成 Java 代码
public class JetTemplateCodeVisitor extends AbstractParseTreeVisitor<Code> implements JetTemplateParserVisitor<Code> {
    private static final Logger log = LoggerFactory.getLogger(JetTemplateCodeVisitor.class);

    private final JetEngine engine;
    private final JetTemplateParser parser;
    private final VariableResolver resolver;
    private final Resource resource;
    private final String encoding;
    private final boolean trimDirectiveLine;

    private SymbolScope scope; // 当前的作用域
    private SymbolScope contextScope; // 全局context变量
    private BlockCode textBlockCode; // 缓存全局文本内容
    private BlockCode contextBlockCode; // 缓存全局context变量
    private Map<String, String> textCodeCache; // 缓存文本内容
    private Deque<String[]> forStack; // 维护嵌套 #for 的堆栈， 可以识别是否在嵌入在 #for 里面, 内部是否使用了 for.index
    private int uuid = 1;

    public JetTemplateCodeVisitor(JetEngine engine, VariableResolver resolver, JetTemplateParser parser, Resource resource) {
        this.engine = engine;
        this.parser = parser;
        this.resolver = resolver;
        this.resource = resource;
        this.encoding = engine.getConfig().getOutputEncoding();
        this.trimDirectiveLine = engine.getConfig().isTrimDirectiveLine();
        this.scope = new SymbolScope(null);
        this.textCodeCache = new HashMap<String, String>(32);
        this.forStack = new ArrayDeque<String[]>(8);
    }

    @Override
    public Code visitTemplate(TemplateContext ctx) {
        BlockCode code = scope.createBlockCode(128);
        if (resource.getPackageName() != null) {
            code.addLine("package " + resource.getPackageName() + ";");
            code.addLine("");
        }
        code.addLine("import java.util.*;");
        code.addLine("import jetbrick.template.runtime.*;");
        code.addLine("");

        code.addLine("public class " + resource.getClassName() + " extends JetPage {");
        scope = scope.push();
        textBlockCode = scope.createBlockCode(32); // 在当前作用域中建立 textBlockCode

        // add render() method
        code.addLine("");
        code.addLine("  @Override");
        code.addLine("  public void render(JetContext context) throws Throwable {");
        scope = scope.push();
        scope.define("context", TypedKlass.JetContext);
        code.addLine("    JetWriter $out = context.getWriter();");

        contextScope = scope; // 全局context变量
        contextBlockCode = scope.createBlockCode(8); // 在当前作用域中建立 contextBlockCode
        Code blockCode = ctx.block().accept(this);

        // add context variable definition
        code.addChild(contextBlockCode);
        code.addChild(blockCode);

        code.addLine("    $out.flush();");
        scope = scope.pop(); // exit render() method
        code.addLine("  }");

        // add getName() method
        code.addLine("");
        code.addLine("  @Override");
        code.addLine("  public String getName() {");
        code.addLine("    return \"" + StringEscapeUtils.escapeJava(resource.getName()) + "\";");
        code.addLine("  }");

        // add text fields definition
        code.addLine("");
        code.addLine("  private static final String $ENC = \"" + encoding + "\";");
        code.addChild(textBlockCode);

        scope = scope.pop(); // exit class
        code.addLine("}");

        return code;
    }

    @Override
    public Code visitBlock(BlockContext ctx) {
        BlockCode code = scope.createBlockCode(32);

        int size = ctx.children.size();
        for (int i = 0; i < size; i++) {
            ParseTree node = ctx.children.get(i);
            Code c = node.accept(this);

            if (node instanceof TextContext) {
                // 文本节点
                TextCode textCode = (TextCode) c;
                if (trimDirectiveLine) {
                    // trim 指令两边的空白内容
                    ParseTree prev = i > 0 ? ctx.children.get(i - 1) : null;
                    ParseTree next = (i < size - 1) ? ctx.children.get(i + 1) : null;

                    boolean trimLeft;
                    if (prev == null) {
                        trimLeft = !(ctx.getParent() instanceof TemplateContext);
                    } else {
                        trimLeft = (prev instanceof DirectiveContext);
                    }

                    boolean trimRight;
                    if (next == null) {
                        trimRight = !(ctx.getParent() instanceof TemplateContext);
                    } else {
                        trimRight = (next instanceof DirectiveContext);
                    }

                    textCode.trim(trimLeft, trimRight);
                }

                if (!textCode.isEmpty()) {
                    // 如果有相同内容的Text，则从缓存中读取
                    String source = textCodeCache.get(textCode.getText());
                    if (source == null) {
                        source = c.getSource();
                        textCodeCache.put(textCode.getText(), source);
                        // add text into field
                        textBlockCode.addLine(textCode.getTextValueFieldSource());
                        textBlockCode.addLine(textCode.getTextBytesFieldSource());
                    }
                    code.addLine(source);
                }
            } else {
                code.addChild(c);
            }
        }
        return code;
    }

    @Override
    public Code visitText(TextContext ctx) {
        Token token = ((TerminalNode) ctx.getChild(0)).getSymbol();
        String text = token.getText();
        switch (token.getType()) {
        case JetTemplateParser.TEXT_CDATA:
            text = text.substring(3, text.length() - 3);
            break;
        case JetTemplateParser.TEXT_ESCAPED_CHAR:
            text = text.substring(1);
            break;
        }

        String id = getUid("txt");
        return new TextCode(id, text);
    }

    @Override
    public Code visitValue(ValueContext ctx) {
        Code code = ctx.expression().accept(this);
        String source = code.getSource();

        Token token = ((TerminalNode) ctx.getChild(0)).getSymbol();
        if (token.getType() == JetTemplateParser.VALUE_ESCAPED_OPEN) {
            source = "JetUtils.asEscapeHtml(" + source + ")";
        }

        if ("null".equals(source)) {
            // 防止编译出错 (也可以生成一个空行)
            source = "(Object)null";
        }
        return scope.createLineCode("$out.print(" + source + "); // line: " + ctx.getStart().getLine());
    }

    @Override
    public Code visitDirective(DirectiveContext ctx) {
        return ctx.getChild(0).accept(this);
    }

    @Override
    public Code visitDefine_directive(Define_directiveContext ctx) {
        List<Define_expressionContext> define_expression_list = ctx.define_expression();
        BlockCode code = scope.createBlockCode(define_expression_list.size());

        for (Define_expressionContext node : define_expression_list) {
            Code c = node.accept(this);
            if (c != null) {
                code.addChild(c);
            }
        }
        return code;
    }

    @Override
    public Code visitDefine_expression(Define_expressionContext ctx) {
        SegmentCode code = (SegmentCode) ctx.type().accept(this);
        String name = assert_java_identifier(ctx.IDENTIFIER(), true);

        if (!scope.define(name, code.getTypedKlass())) {
            throw reportError("Duplicate local variable " + name, ctx.IDENTIFIER());
        }

        String typeName = code.getTypedKlass().asBoxedTypedKlass().getSource();
        return scope.createLineCode(typeName + " " + name + " = (" + typeName + ") context.get(\"" + name + "\"); // line: " + ctx.getStart().getLine());
    }

    @Override
    public Code visitSet_directive(Set_directiveContext ctx) {
        List<Set_expressionContext> set_expression_list = ctx.set_expression();
        BlockCode code = scope.createBlockCode(set_expression_list.size());

        for (Set_expressionContext node : set_expression_list) {
            Code c = node.accept(this);
            if (c != null) {
                code.addChild(c);
            }
        }
        return code;
    }

    @Override
    public Code visitSet_expression(Set_expressionContext ctx) {
        String name = assert_java_identifier(ctx.IDENTIFIER(), true);
        SegmentCode code = (SegmentCode) ctx.expression().accept(this);

        TypedKlass lhs = null; // 变量类型
        TypeContext type = ctx.type();
        if (type != null) {
            // 定义一个变量
            SegmentCode c = (SegmentCode) type.accept(this);
            lhs = c.getTypedKlass();
            if (!scope.define(name, lhs)) {
                throw reportError("Duplicate local variable " + name, ctx.IDENTIFIER());
            }
        } else {
            // 直接赋值，如果变量没有定义，则先定义
            lhs = scope.resolve(name);
            if (lhs == null) {
                lhs = code.getTypedKlass();
                scope.define(name, lhs);
            }
        }

        // 进行赋值语句类型检查
        if (!ClassUtils.isAssignable(lhs.getKlass(), code.getKlass())) {
            throw reportError("Type mismatch: cannot convert from " + code.getTypedKlass().getSource() + " to " + lhs.getSource(), ctx);
        }

        String source = lhs.getSource() + " " + name + " = (" + lhs.getSource() + ") " + code.getSource() + "; // line: " + ctx.getStart().getLine();
        return scope.createLineCode(source);
    }

    @Override
    public Code visitPut_directive(Put_directiveContext ctx) {
        List<ExpressionContext> expression_list = ctx.expression();
        if (expression_list.size() != 2) {
            throw reportError("Only two arguments can be accepted for #put directive", ctx);
        }

        SegmentCode name = (SegmentCode) expression_list.get(0).accept(this);
        SegmentCode value = (SegmentCode) expression_list.get(1).accept(this);

        if (!String.class.equals(name.getKlass())) {
            throw reportError("The first parameter type is not String.class for #put directive", ctx);
        }

        return scope.createLineCode("context.put(" + name.getSource() + ", " + value.getSource() + "); // line: " + ctx.getStart().getLine());
    }

    @Override
    public Code visitIf_directive(If_directiveContext ctx) {
        BlockCode code = scope.createBlockCode(16);

        SegmentCode expr_code = (SegmentCode) ctx.expression().accept(this);
        code.addLine("if (" + get_if_expression_source(expr_code) + ") { // line: " + ctx.getStart().getLine());
        scope = scope.push();

        Code block_code = ctx.block().accept(this);
        code.addChild(block_code);
        scope = scope.pop();
        code.addLine("}");

        // elseif ...
        List<Elseif_directiveContext> elseif_directive_list = ctx.elseif_directive();
        for (Elseif_directiveContext elseif_directive : elseif_directive_list) {
            Code c = elseif_directive.accept(this);
            code.addChild(c);
        }

        // else ...
        Else_directiveContext else_directive = ctx.else_directive();
        if (else_directive != null) {
            Code c = else_directive.accept(this);
            code.addChild(c);
        }

        return code;
    }

    @Override
    public Code visitElseif_directive(Elseif_directiveContext ctx) {
        BlockCode code = scope.createBlockCode(16);

        SegmentCode expr_code = (SegmentCode) ctx.expression().accept(this);
        code.addLine("else if (" + get_if_expression_source(expr_code) + ") { // line: " + ctx.getStart().getLine());
        scope = scope.push();

        Code block_code = ctx.block().accept(this);
        code.addChild(block_code);
        scope = scope.pop();
        code.addLine("}");

        return code;
    }

    @Override
    public Code visitElse_directive(Else_directiveContext ctx) {
        BlockCode code = scope.createBlockCode(16);

        if (ctx.getParent() instanceof If_directiveContext) {
            code.addLine("else { // line: " + ctx.getStart().getLine());
        }

        scope = scope.push();
        Code block_code = ctx.block().accept(this);
        code.addChild(block_code);
        scope = scope.pop();

        if (ctx.getParent() instanceof If_directiveContext) {
            code.addLine("}");
        }

        return code;
    }

    @Override
    public Code visitFor_directive(For_directiveContext ctx) {
        BlockCode code = scope.createBlockCode(16);
        String id_for = getUid("for");
        String id_it = getUid("it");

        scope = scope.push();
        // 注意：for循环变量的作用域要放在 for 内部， 防止出现变量重定义错误
        ForExpressionCode for_expr_code = (ForExpressionCode) ctx.for_expression().accept(this);
        // for block
        forStack.push(new String[] { id_for, "false" });
        Code for_block_code = ctx.block().accept(this);
        scope = scope.pop();

        // for-else
        Else_directiveContext else_directive = ctx.else_directive();
        Code for_else_block = (else_directive == null) ? null : else_directive.accept(this);

        // 生成代码
        String[] for_status = forStack.pop();
        boolean need_for_status = for_else_block != null || "true".equals(for_status[1]);
        if (need_for_status) {
            code.addLine("JetForStatus " + id_for + " = new JetForStatus();");
        }
        code.addLine("Iterator<?> " + id_it + " = JetUtils.asIterator(" + for_expr_code.getSource() + ");");
        code.addLine("while (" + id_it + ".hasNext()) { // line: " + ctx.getStart().getLine());

        // class item = (class) it.next() ...
        String typeName = for_expr_code.getKlassName();
        code.addLine("  " + typeName + " " + for_expr_code.getName() + " = (" + typeName + ") " + id_it + ".next();");
        if (need_for_status) {
            code.addLine("  " + id_for + ".inc();"); // 在 #for 的内部变量，用于计数
        }
        code.addChild(for_block_code);
        code.addLine("}");

        // for else ...
        if (for_else_block != null) {
            code.addLine("if (" + id_for + ".empty()) { // line: " + ctx.getStart().getLine());
            code.addChild(for_else_block);
            code.addLine("}");
        }

        return code;
    }

    @Override
    public Code visitFor_expression(For_expressionContext ctx) {
        String name = ctx.IDENTIFIER().getText();
        SegmentCode code = (SegmentCode) ctx.expression().accept(this);

        TypedKlass resultKlass = null;
        TypeContext type = ctx.type();
        if (type != null) {
            // 手动定义返回变量的类型
            SegmentCode c = (SegmentCode) type.accept(this);
            resultKlass = c.getTypedKlass();
        } else {
            // 根据 expression 来进行自动类型推导
            Class<?> rhsKlass = code.getKlass();
            if (rhsKlass.isArray()) {
                resultKlass = TypedKlass.create(rhsKlass.getComponentType(), code.getTypeArgs());
            } else if (Map.class.isAssignableFrom(rhsKlass)) {
                resultKlass = TypedKlass.create(Map.Entry.class, code.getTypeArgs());
            } else if (Collection.class.isAssignableFrom(rhsKlass)) {
                if (code.getTypeArgs() != null && code.getTypeArgs().length == 1) {
                    resultKlass = code.getTypeArgs()[0];
                }
            }
        }

        if (resultKlass == null) {
            resultKlass = TypedKlass.Object;
        }

        // 必须是 Boxed 对象，因为需要强制类型转换 from iterator.next()
        resultKlass = resultKlass.asBoxedTypedKlass();

        if (!scope.define(name, resultKlass)) {
            throw reportError("Duplicate local variable " + name, ctx.IDENTIFIER());
        }

        return new ForExpressionCode(resultKlass, name, code.getSource());
    }

    @Override
    public Code visitBreak_directive(Break_directiveContext ctx) {
        assert_inside_of_for_directive(ctx, "#break");

        ExpressionContext expression = ctx.expression();
        String source;
        if (expression != null) {
            SegmentCode c = (SegmentCode) expression.accept(this);
            source = get_if_expression_source(c);
        } else {
            source = "true";
        }
        source = "if (" + source + ") break; // line: " + ctx.getStart().getLine();
        return scope.createLineCode(source);
    }

    @Override
    public Code visitContinue_directive(Continue_directiveContext ctx) {
        assert_inside_of_for_directive(ctx, "#continue");

        ExpressionContext expression = ctx.expression();
        String source;
        if (expression != null) {
            SegmentCode c = (SegmentCode) expression.accept(this);
            source = get_if_expression_source(c);
        } else {
            source = "true";
        }
        source = "if (" + source + ") continue; // line: " + ctx.getStart().getLine();
        return scope.createLineCode(source);
    }

    @Override
    public Code visitStop_directive(Stop_directiveContext ctx) {
        ExpressionContext expression = ctx.expression();
        String source;
        if (expression != null) {
            SegmentCode c = (SegmentCode) expression.accept(this);
            source = get_if_expression_source(c);
        } else {
            source = "true";
        }
        source = "if (" + source + ") return; // line: " + ctx.getStart().getLine();
        return scope.createLineCode(source);
    }

    @Override
    public Code visitInclude_directive(Include_directiveContext ctx) {
        Expression_listContext expression_list = ctx.expression_list();
        SegmentListCode childrenCode = (SegmentListCode) expression_list.accept(this);
        if (childrenCode.size() > 3) {
            throw reportError("The arguments do not matched with #include directive.", ctx);
        }

        // argument 1: file
        SegmentCode fileCode = childrenCode.getChild(0);
        ExpressionContext fileExpression = expression_list.expression(0);
        if (!String.class.equals(fileCode.getKlass())) {
            throw reportError("Type mismatch: the first argument cannot convert from " + fileCode.getKlassName() + " to String", fileExpression);
        }

        // argument 2: isPlainText
        SegmentCode isPlainTextCode = null;
        if (childrenCode.size() > 1) {
            isPlainTextCode = childrenCode.getChild(1);
            if (!(Boolean.TYPE.equals(isPlainTextCode.getKlass()) || Boolean.class.equals(isPlainTextCode.getKlass()))) {
                throw reportError("Type mismatch: the second argument cannot convert from " + isPlainTextCode.getKlassName() + " to Boolean", expression_list.expression(1));
            }
        }

        // argument 3: encoding
        SegmentCode encodingCode = null;
        if (childrenCode.size() > 2) {
            encodingCode = childrenCode.getChild(2);
            if (!(String.class.equals(encodingCode.getKlass()))) {
                throw reportError("Type mismatch: the third argument cannot convert from " + encodingCode.getKlassName() + " to String", expression_list.expression(2));
            }
        }

        // 如果 file 是常量，那么进行 file.exists() 校验
        if (fileExpression instanceof Expr_constantContext) {
            String file = fileCode.getSource();
            file = file.substring(1, file.length() - 1);
            file = StringEscapeUtils.unescapeJava(file);
            file = PathUtils.relativePath(resource.getName(), file);
            if (engine.getResource(file) == null) {
                throw reportError("FileNotFoundException: " + file, fileExpression);
            }
        }

        // 生成代码
        StringBuilder source = new StringBuilder();
        source.append("JetUtils.asInclude(context, getName(), ");
        source.append(fileCode.getSource());
        source.append(", ");
        source.append((isPlainTextCode != null) ? isPlainTextCode.getSource() : "false");
        source.append(", ");
        source.append((encodingCode != null) ? encodingCode.getSource() : "null");
        source.append("); // line: ");
        source.append(ctx.getStart().getLine());
        return scope.createLineCode(source.toString());
    }

    @Override
    public Code visitDebug_directive(Debug_directiveContext ctx) {
        Expression_listContext expression_list = ctx.expression_list();
        SegmentListCode childrenCode = (SegmentListCode) expression_list.accept(this);
        if (childrenCode.size() == 0) {
            throw reportError("Missing arguments for #debug directive.", ctx);
        }
        SegmentCode formatCode = childrenCode.getChild(0);
        if (!String.class.equals(formatCode.getKlass())) {
            throw reportError("Type mismatch: the first argument cannot convert from " + formatCode.getKlassName() + " to String", expression_list.expression(0));
        }

        // 生成代码
        StringBuilder source = new StringBuilder();
        source.append("JetUtils.debug(");
        source.append(childrenCode.getSource());
        source.append("); // line: ");
        source.append(ctx.getStart().getLine());
        return scope.createLineCode(source.toString());
    }

    @Override
    public Code visitInvalid_directive(Invalid_directiveContext ctx) {
        throw reportError("Missing arguments for " + ctx.getText() + " directive.", ctx);
    }

    @Override
    public Code visitExpr_group(Expr_groupContext ctx) {
        SegmentCode code = (SegmentCode) ctx.expression().accept(this);
        String source = "(" + code.getSource() + ")";
        return new SegmentCode(code.getTypedKlass(), source);
    }

    @Override
    public Code visitExpr_constant(Expr_constantContext ctx) {
        return ctx.getChild(0).accept(this);
    }

    @Override
    public Code visitExpr_array_list(Expr_array_listContext ctx) {
        String source = "Collections.EMPTY_LIST";
        Expression_listContext expression_list = ctx.expression_list();
        if (expression_list != null) {
            Code code = expression_list.accept(this);
            source = "Arrays.asList(" + code.getSource() + ")";
        }
        return new SegmentCode(List.class, source);
    }

    @Override
    public Code visitExpr_hash_map(Expr_hash_mapContext ctx) {
        String source = "Collections.EMPTY_MAP";
        Hash_map_entry_listContext hash_map_entry_list = ctx.hash_map_entry_list();
        if (hash_map_entry_list != null) {
            Code code = hash_map_entry_list.accept(this);
            source = "JetUtils.asMap(" + code.getSource() + ")";
        }
        return new SegmentCode(Map.class, source);
    }

    @Override
    public Code visitHash_map_entry_list(Hash_map_entry_listContext ctx) {
        List<ExpressionContext> expression_list = ctx.expression();
        SegmentListCode code = new SegmentListCode(expression_list.size());
        for (ExpressionContext expression : expression_list) {
            code.addChild((SegmentCode) expression.accept(this));
        }
        return code;
    }

    @Override
    public Code visitExpr_identifier(Expr_identifierContext ctx) {
        String name = assert_java_identifier(ctx.IDENTIFIER(), false);

        // 特殊处理 for 变量
        if ("for".equals(name)) {
            assert_inside_of_for_directive(ctx, "Local variable \"for\"");
            // 强制映射成 JetForStatus $for
            String[] forStatus = forStack.peek();
            forStatus[1] = "true"; // 存在 for 变量，则变成 true
            name = forStatus[0]; // 取出 forStatus 的原始变量名
            return new SegmentCode(TypedKlass.JetForStatus, name);
        }

        // 找到变量的类型
        TypedKlass resultKlass = scope.resolve(name);
        if (resultKlass == null) {
            // 没有定义过，则查找全局定义
            resultKlass = resolver.resolveVariable(name);
            if (contextScope.define(name, resultKlass)) {
                if (resultKlass == TypedKlass.Object) {
                    log.warn("line " + ctx.getStart().getLine() + ": Implicit definition for context variable: " + resultKlass.getSource() + " " + name);
                }
                // 注意：不光要在Global作用域定义，也要在当前的作用域定义，防止后面重定义
                scope.define(name, resultKlass);

                String klass = resultKlass.asBoxedTypedKlass().getSource();
                contextBlockCode.addLine(klass + " " + name + " = (" + klass + ") context.get(\"" + name + "\");");
            }
        }

        return new SegmentCode(resultKlass, name);
    }

    @Override
    public Code visitExpr_field_access(Expr_field_accessContext ctx) {
        ExpressionContext expression = get_not_null_constantContext(ctx.expression());
        SegmentCode code = (SegmentCode) expression.accept(this);
        String name = ctx.IDENTIFIER().getText();

        // 进行类型推导，找到方法的返回类型
        code = code.asBoxedSegmentCode();
        Class<?> beanClass = code.getKlass();
        Member member = resolver.resolveProperty(beanClass, name);
        if (member == null) {
            // reportError
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            StringBuilder err = new StringBuilder(128);
            err.append("The method ");
            err.append("get" + name);
            err.append("() or ");
            err.append("is" + name);
            err.append("() is undefined for the type ");
            err.append(beanClass.getName());
            err.append('.');
            throw reportError(err.toString(), ctx.IDENTIFIER());
        }

        // 生成code
        StringBuilder source = new StringBuilder();
        TypedKlass resultKlass = null;

        String op = ctx.getChild(1).getText();
        if (member instanceof Method) {
            Method method = (Method) member;
            if (method.getParameterTypes().length == 0) {
                // getXXX() or isXXX()
                if ("?.".equals(op)) { // 安全调用，防止 NullPointException
                    source.append("((");
                    source.append(code.getSource());
                    source.append("==null)?null:");
                    source.append(code.getSource());
                    source.append('.');
                    source.append(method.getName());
                    source.append("())");
                } else {
                    source.append(code.getSource());
                    source.append('.');
                    source.append(method.getName());
                    source.append("()");
                }
            } else {
                // get(String)
                if ("?.".equals(op)) { // 安全调用，防止 NullPointException
                    source.append("((");
                    source.append(code.getSource());
                    source.append("==null)?null:");
                    source.append(code.getSource());
                    source.append(".get(");
                    source.append(name);
                    source.append("))");
                } else {
                    source.append(code.getSource());
                    source.append(".get(");
                    source.append(name);
                    source.append(')');
                }
            }
            resultKlass = TypedKlassUtils.getMethodReturnTypedKlass(method);
        } else {
            Field field = (Field) member;
            if ("?.".equals(op)) { // 安全调用，防止 NullPointException
                source.append("((");
                source.append(code.getSource());
                source.append("==null)?null:");
                source.append(code.getSource());
                source.append('.');
                source.append(field.getName());
                source.append(')');
            } else {
                source.append(code.getSource());
                source.append('.');
                source.append(field.getName());
            }
            resultKlass = TypedKlassUtils.getFieldTypedKlass(field);
        }

        return new SegmentCode(resultKlass, source.toString());
    }

    @Override
    public Code visitExpr_method_invocation(Expr_method_invocationContext ctx) {
        // 查找方法的参数类型
        Class<?> parameterTypes[] = ArrayUtils.EMPTY_CLASS_ARRAY;
        SegmentListCode expr_list_code = null;
        Expression_listContext expression_list = ctx.expression_list();
        if (expression_list != null) {
            expr_list_code = (SegmentListCode) expression_list.accept(this);
            parameterTypes = new Class<?>[expr_list_code.size()];
            for (int i = 0; i < expr_list_code.size(); i++) {
                parameterTypes[i] = expr_list_code.getChild(i).getKlass();
            }
        }

        // 查找方法
        ExpressionContext expression = get_not_null_constantContext(ctx.expression());
        SegmentCode code = (SegmentCode) expression.accept(this);
        code = code.asBoxedSegmentCode();
        Class<?> beanClass = code.getKlass();
        String name = ctx.IDENTIFIER().getText();
        Method method1 = resolver.resolveMethod(beanClass, name, parameterTypes);
        Method method2 = (method1 != null) ? null : resolver.resolveToolMethod(beanClass, name, parameterTypes);
        if (method1 == null && method2 == null) {
            // reportError
            StringBuilder err = new StringBuilder(128);
            err.append("The method ");
            err.append(name);
            err.append('(');
            for (int i = 0; i < parameterTypes.length; i++) {
                if (i > 0) err.append(',');
                err.append(parameterTypes[i].getName());
            }
            err.append(") is undefined for the type ");
            err.append(beanClass.getName());
            err.append('.');
            throw reportError(err.toString(), ctx.IDENTIFIER());
        }

        // 生成code
        StringBuilder source = new StringBuilder();
        String op = ctx.getChild(1).getText();
        if (method2 != null) {
            // tool method
            source.append(ClassUtils.getShortClassName(method2.getDeclaringClass()));
            source.append('.');
            source.append(name);
            source.append('(');
            source.append(code.getSource());
            if (expr_list_code != null) {
                source.append(',');
            }
        } else {
            if ("?.".equals(op)) { // 安全调用，防止 NullPointException
                source.append('(');
                source.append(code.getSource());
                source.append("==null)?null:");
                source.append(code.getSource());
                source.append('.');
                source.append(name);
                source.append('(');
            } else {
                source.append(code.getSource());
                source.append('.');
                source.append(name);
                source.append('(');
            }
        }
        if (expr_list_code != null) {
            source.append(expr_list_code.getSource());
        }
        source.append(')');

        if ("?.".equals(op)) { // 为了安全起见，用()包起来
            source.insert(0, '(').append(')');
        }

        // 得到方法的返回类型
        Method method = (method1 == null) ? method2 : method1;
        TypedKlass typedKlass = TypedKlassUtils.getMethodReturnTypedKlass(method);
        return new SegmentCode(typedKlass, source.toString());
    }

    @Override
    public Code visitExpr_function_call(Expr_function_callContext ctx) {
        // 查找方法的参数类型
        Class<?> parameterTypes[] = ArrayUtils.EMPTY_CLASS_ARRAY;
        Expression_listContext expression_list = ctx.expression_list();
        SegmentListCode expr_list_code = null;
        if (expression_list != null) {
            expr_list_code = (SegmentListCode) expression_list.accept(this);
            parameterTypes = new Class<?>[expr_list_code.size()];
            for (int i = 0; i < expr_list_code.size(); i++) {
                parameterTypes[i] = expr_list_code.getChild(i).getKlass();
            }
        }
        // 查找方法
        String name = ctx.IDENTIFIER().getText();
        Method method = resolver.resolveFunction(name, parameterTypes);
        if (method == null) {
            throw reportError("Undefined function " + name + "(...).", ctx.IDENTIFIER());
        }

        // 生成code
        StringBuilder source = new StringBuilder();
        source.append(ClassUtils.getShortClassName(method.getDeclaringClass()));
        source.append('.');
        source.append(name);
        source.append('(');
        if (expr_list_code != null) {
            source.append(expr_list_code.getSource());
        }
        source.append(')');

        TypedKlass typedKlass = TypedKlassUtils.getMethodReturnTypedKlass(method);
        return new SegmentCode(typedKlass, source.toString());
    }

    @Override
    public Code visitExpr_array_get(Expr_array_getContext ctx) {
        ExpressionContext lhs_expression = get_not_null_constantContext(ctx.expression(0));
        ExpressionContext rhs_expression = get_not_null_constantContext(ctx.expression(1));
        SegmentCode lhs = (SegmentCode) lhs_expression.accept(this);
        SegmentCode rhs = (SegmentCode) rhs_expression.accept(this);
        Class<?> lhsKlass = lhs.getKlass();
        if (lhsKlass.isArray()) {
            if (!ClassUtils.isAssignable(Integer.TYPE, rhs.getKlass())) {
                throw reportError("Type mismatch: cannot convert from " + rhs.getKlassName() + " to int.", rhs_expression);
            }
            String source = lhs.getSource() + "[" + rhs.getSource() + "]";
            return new SegmentCode(lhsKlass.getComponentType(), lhs.getTypeArgs(), source);
        } else {
            TypedKlass resultKlass = null;

            // try to List.get(index) or Map.get(name) or JetContext.get(name)
            if (List.class.isAssignableFrom(lhsKlass)) {
                if (!ClassUtils.isAssignable(Integer.TYPE, rhs.getKlass())) {
                    throw reportError("The method get(int) in the type List is not applicable for the arguments (" + rhs.getKlassName() + ")", rhs_expression);
                }
                // 取出可能的List泛型
                if (lhs.getTypeArgs() != null && lhs.getTypeArgs().length == 1) {
                    resultKlass = lhs.getTypeArgs()[0];
                }
            } else if (Map.class.isAssignableFrom(lhsKlass)) {
                // 取出可能的Map泛型
                if (lhs.getTypeArgs() != null && lhs.getTypeArgs().length == 2) {
                    resultKlass = lhs.getTypeArgs()[1]; // value 对应的泛型
                }
            } else if (JetContext.class.isAssignableFrom(lhsKlass)) {
                if (!String.class.equals(rhs.getKlass())) {
                    throw reportError("The method get(String) in the type JetContext is not applicable for the arguments (" + rhs.getKlassName() + ")", rhs_expression);
                }
                resultKlass = TypedKlass.Object;
            } else {
                throw reportError("Operator [] is not applicable for the object (" + lhs.getKlassName() + ").", ctx);
            }

            if (resultKlass == null) {
                resultKlass = TypedKlass.Object;
            }

            String source = lhs.getSource() + ".get(" + rhs.getSource() + ")";
            return new SegmentCode(resultKlass, source);
        }
    }

    @Override
    public Code visitExpr_new_object(Expr_new_objectContext ctx) {
        SegmentCode code = (SegmentCode) ctx.type().accept(this);

        SegmentListCode expr_list_code = null;
        Expression_listContext expression_list = ctx.expression_list();
        if (expression_list != null) {
            expr_list_code = (SegmentListCode) expression_list.accept(this);
        }

        // 查找对应的构造函数
        Class<?> beanClass = code.getKlass();
        Class<?> parameterTypes[] = ArrayUtils.EMPTY_CLASS_ARRAY;
        if (expr_list_code != null) {
            parameterTypes = new Class<?>[expr_list_code.size()];
            for (int i = 0; i < expr_list_code.size(); i++) {
                parameterTypes[i] = expr_list_code.getChild(i).getKlass();
            }
        }
        Constructor<?> constructor = resolver.resolveConstructor(beanClass, parameterTypes);
        if (constructor == null) {
            // reportError
            StringBuilder err = new StringBuilder(128);
            err.append("The constructor ");
            err.append('(');
            for (int i = 0; i < parameterTypes.length; i++) {
                if (i > 0) err.append(',');
                err.append(parameterTypes[i].getName());
            }
            err.append(") is undefined for the type ");
            err.append(beanClass.getName());
            err.append('.');
            throw reportError(err.toString(), ctx.type());
        }

        // 生成代码
        StringBuilder source = new StringBuilder(32);
        source.append("(new ").append(code.getSource()).append('(');
        if (expr_list_code != null) {
            source.append(expr_list_code.getSource());
        }
        source.append("))");

        return new SegmentCode(code.getTypedKlass(), source.toString());
    }

    @Override
    public Code visitExpr_new_array(Expr_new_arrayContext ctx) {
        SegmentCode code = (SegmentCode) ctx.type().accept(this);
        if (code.getKlass().isArray()) {
            throw reportError("Cannot specify an array dimension after an empty dimension", ctx.type());
        }

        // 生成代码
        StringBuilder source = new StringBuilder(32);
        source.append("(new ").append(code.getSource());
        for (ExpressionContext expression : ctx.expression()) {
            SegmentCode c = (SegmentCode) expression.accept(this);
            if (!ClassUtils.isAssignable(Integer.TYPE, c.getKlass())) {
                throw reportError("Type mismatch: cannot convert from " + c.getKlassName() + " to int.", expression);
            }
            source.append('[').append(c.getSource()).append(']');
        }
        source.append(')');
        return new SegmentCode(code.getTypedKlass(), source.toString());
    }

    @Override
    public Code visitExpr_class_cast(Expr_class_castContext ctx) {
        SegmentCode code = (SegmentCode) ctx.type().accept(this);
        Code expr_code = ctx.expression().accept(this);
        String source = "((" + code.getSource() + ")" + expr_code.getSource() + ")";
        return new SegmentCode(code.getTypedKlass(), source);
    }

    @Override
    public Code visitExpr_instanceof(Expr_instanceofContext ctx) {
        SegmentCode lhs = (SegmentCode) ctx.expression().accept(this);
        SegmentCode rhs = (SegmentCode) ctx.type().accept(this);

        if (!ClassUtils.isAssignable(lhs.getKlass(), rhs.getKlass()) && !ClassUtils.isAssignable(lhs.getKlass(), rhs.getKlass())) {
            throw reportError("Incompatible conditional operand types " + lhs.getKlassName() + " and " + rhs.getKlassName(), ctx.getChild(1));
        }
        String source = "(" + lhs.getSource() + " instanceof " + rhs.getSource() + ")";
        return new SegmentCode(Boolean.TYPE, source);
    }

    @Override
    public Code visitExpr_math_unary_suffix(Expr_math_unary_suffixContext ctx) {
        ExpressionContext expression = get_not_null_constantContext(ctx.expression());
        SegmentCode code = (SegmentCode) expression.accept(this);
        String op = ctx.getChild(1).getText();

        // ++, --
        if (expression.getChildCount() == 1 && expression.getChild(0) instanceof ConstantContext) {
            throw reportError("Invalid argument to operation " + op + ", required: variable, found Value", expression);
        }

        // 类型检查
        Class<?> resultKlass = PromotionUtils.get_unary_inc_dec(code.getKlass(), op);
        if (resultKlass == null) {
            throw reportError("The UnaryOperator \"" + op + "\" is not applicable for the operand " + code.getKlassName(), ctx.getChild(1));
        }

        String source = "(" + code.getSource() + op + ")";
        return new SegmentCode(code.getTypedKlass(), source);
    }

    @Override
    public Code visitExpr_math_unary_prefix(Expr_math_unary_prefixContext ctx) {
        ExpressionContext expression = get_not_null_constantContext(ctx.expression());
        SegmentCode code = (SegmentCode) expression.accept(this);
        String op = ctx.getChild(0).getText();

        // 类型检查
        Class<?> resultKlass;
        if (op.length() == 1) {
            // +, -, ~
            resultKlass = PromotionUtils.get_unary_basic(code.getKlass(), op);
        } else {
            // ++, --
            if (expression.getChildCount() == 1 && expression.getChild(0) instanceof ConstantContext) {
                throw reportError("Invalid argument to operation " + op + ", required: variable, found Value", expression);
            }
            resultKlass = PromotionUtils.get_unary_inc_dec(code.getKlass(), op);
        }
        if (resultKlass == null) {
            throw reportError("The UnaryOperator \"" + op + "\" is not applicable for the operand " + code.getKlassName(), ctx.getChild(0));
        }

        String source = "(" + op + code.getSource() + ")";
        return new SegmentCode(code.getTypedKlass(), source);
    }

    @Override
    public Code visitExpr_math_binary_basic(Expr_math_binary_basicContext ctx) {
        SegmentCode lhs = (SegmentCode) ctx.expression(0).accept(this);
        SegmentCode rhs = (SegmentCode) ctx.expression(1).accept(this);
        String op = ctx.getChild(1).getText();

        // 类型检查
        Class<?> resultKlass = PromotionUtils.get_binary_basic(lhs.getKlass(), rhs.getKlass(), op);
        if (resultKlass == null) {
            throw reportError("The BinaryOperator \"" + op + "\" is not applicable for the operands " + lhs.getKlassName() + " and " + rhs.getKlassName(), ctx);
        }

        String source = "(" + lhs.getSource() + op + rhs.getSource() + ")";
        return new SegmentCode(resultKlass, source);
    }

    @Override
    public Code visitExpr_math_binary_shift(Expr_math_binary_shiftContext ctx) {
        SegmentCode lhs = (SegmentCode) ctx.expression(0).accept(this);
        SegmentCode rhs = (SegmentCode) ctx.expression(1).accept(this);

        // Combined '>' '>' => '>>'
        String op = "";
        for (int i = 1; i < ctx.getChildCount() - 1; i++) {
            ParseTree node = ctx.getChild(i);
            if (node instanceof TerminalNode) {
                op = op + node.getText();
            }
        }

        // 类型检查
        Class<?> resultKlass = PromotionUtils.get_binary_shift(lhs.getKlass(), rhs.getKlass(), op);
        if (resultKlass == null) {
            throw reportError("The BinaryOperator \"" + op + "\" is not applicable for the operands " + lhs.getKlassName() + " and " + rhs.getKlassName(), ctx);
        }

        String source = "(" + lhs.getSource() + op + rhs.getSource() + ")";
        return new SegmentCode(resultKlass, source);
    }

    @Override
    public Code visitExpr_math_binary_bitwise(Expr_math_binary_bitwiseContext ctx) {
        SegmentCode lhs = (SegmentCode) ctx.expression(0).accept(this);
        SegmentCode rhs = (SegmentCode) ctx.expression(1).accept(this);
        String op = ctx.getChild(1).getText();

        // 类型检查
        Class<?> resultKlass = PromotionUtils.get_binary_bitwise(lhs.getKlass(), rhs.getKlass(), op);
        if (resultKlass == null) {
            throw reportError("The BinaryOperator \"" + op + "\" is not applicable for the operands " + lhs.getKlassName() + " and " + rhs.getKlassName(), ctx);
        }

        String source = "(" + lhs.getSource() + op + rhs.getSource() + ")";
        return new SegmentCode(resultKlass, source);
    }

    @Override
    public Code visitExpr_compare_not(Expr_compare_notContext ctx) {
        SegmentCode code = (SegmentCode) ctx.expression().accept(this);
        String source = "(!" + get_if_expression_source(code) + ")";
        return new SegmentCode(Boolean.TYPE, source);
    }

    @Override
    public Code visitExpr_compare_equality(Expr_compare_equalityContext ctx) {
        ExpressionContext lhs_expression = get_not_null_constantContext(ctx.expression(0));
        ExpressionContext rhs_expression = get_not_null_constantContext(ctx.expression(1));
        SegmentCode lhs = (SegmentCode) lhs_expression.accept(this);
        SegmentCode rhs = (SegmentCode) rhs_expression.accept(this);
        TerminalNode op = (TerminalNode) ctx.getChild(1);

        StringBuilder source = new StringBuilder(32);
        source.append("==".equals(op.getText()) ? "JetUtils.asEquals(" : "JetUtils.asNotEquals(");
        source.append(lhs.getSource());
        source.append(',');
        source.append(rhs.getSource());
        source.append(')');
        return new SegmentCode(Boolean.TYPE, source.toString());
    }

    @Override
    public Code visitExpr_compare_relational(Expr_compare_relationalContext ctx) {
        ExpressionContext lhs_expression = get_not_null_constantContext(ctx.expression(0));
        ExpressionContext rhs_expression = get_not_null_constantContext(ctx.expression(1));
        SegmentCode lhs = (SegmentCode) lhs_expression.accept(this);
        SegmentCode rhs = (SegmentCode) rhs_expression.accept(this);
        TerminalNode op = (TerminalNode) ctx.getChild(1);

        Class<?> c1 = lhs.getKlass();
        Class<?> c2 = rhs.getKlass();

        // 类型校验
        boolean pass = true;
        if (NumberClassUtils.isNumbericClass(c1)) {
            pass = NumberClassUtils.isNumbericClass(c2);
        } else if (NumberClassUtils.isNumbericClass(c2)) {
            pass = false;
        } else {
            pass = c1.isAssignableFrom(c2) || c2.isAssignableFrom(c1);
        }
        if (pass == false) {
            throw reportError("The operator " + op.getText() + " is undefined for the argument type(s) " + lhs.getKlassName() + ", " + rhs.getKlassName(), op);
        }

        String suffix = "";
        switch (op.getSymbol().getType()) {
        case JetTemplateParser.OP_RELATIONAL_GT:
            suffix = ">0";
            break;
        case JetTemplateParser.OP_RELATIONAL_LT:
            suffix = "<0";
            break;
        case JetTemplateParser.OP_RELATIONAL_GE:
            suffix = ">=0";
            break;
        case JetTemplateParser.OP_RELATIONAL_LE:
            suffix = "<=0";
            break;
        default:
            throw reportError("Unexpected operator :" + op.getText(), ctx);
        }
        StringBuilder source = new StringBuilder(32);
        source.append("(JetUtils.asCompareWith(");
        source.append(lhs.getSource());
        source.append(',');
        source.append(rhs.getSource());
        source.append(')');
        source.append(suffix);
        source.append(')');
        return new SegmentCode(Boolean.TYPE, source.toString());
    }

    @Override
    public Code visitExpr_compare_condition(Expr_compare_conditionContext ctx) {
        ExpressionContext lhs_expression = get_not_null_constantContext(ctx.expression(0));
        ExpressionContext rhs_expression = get_not_null_constantContext(ctx.expression(1));
        SegmentCode lhs = (SegmentCode) lhs_expression.accept(this);
        SegmentCode rhs = (SegmentCode) rhs_expression.accept(this);
        String op = ctx.getChild(1).getText();
        String source = "(" + get_if_expression_source(lhs) + op + get_if_expression_source(rhs) + ")";
        return new SegmentCode(Boolean.TYPE, source);
    }

    @Override
    public Code visitExpr_conditional_ternary(Expr_conditional_ternaryContext ctx) {
        SegmentCode lhs = (SegmentCode) ctx.expression(0).accept(this);
        SegmentCode rhs1 = (SegmentCode) ctx.expression(1).accept(this);
        SegmentCode rhs2 = (SegmentCode) ctx.expression(2).accept(this);
        String source = "(" + get_if_expression_source(lhs) + "?" + rhs1.getSource() + ":" + rhs2.getSource() + ")";

        TypedKlass klass = PromotionUtils.getResultClassForConditionalOperator(rhs1.getTypedKlass(), rhs2.getTypedKlass());
        return new SegmentCode(klass, source);
    }

    @Override
    public Code visitConstant(ConstantContext ctx) {
        Token token = ((TerminalNode) ctx.getChild(0)).getSymbol();
        String text = token.getText();
        switch (token.getType()) {
        case JetTemplateParser.STRING_DOUBLE:
            return new SegmentCode(String.class, text);
        case JetTemplateParser.STRING_SINGLE:
            text = StringEscapeUtils.asCanonicalJavaString(text);
            return new SegmentCode(String.class, text);
        case JetTemplateParser.INTEGER:
        case JetTemplateParser.INTEGER_HEX:
        case JetTemplateParser.FLOATING_POINT:
            Class<?> klass;
            if (text.endsWith("l") || text.endsWith("L")) {
                klass = Long.TYPE;
            } else if (text.endsWith("f") || text.endsWith("F")) {
                klass = Float.TYPE;
            } else if (text.endsWith("d") || text.endsWith("D")) {
                klass = Double.TYPE;
            } else if (token.getType() == JetTemplateParser.FLOATING_POINT) {
                klass = Double.TYPE; // 浮点数默认是double
            } else {
                klass = Integer.TYPE;
            }
            return new SegmentCode(klass, text);
        case JetTemplateParser.KEYWORD_TRUE:
            return new SegmentCode(Boolean.TYPE, text);
        case JetTemplateParser.KEYWORD_FALSE:
            return new SegmentCode(Boolean.TYPE, text);
        case JetTemplateParser.KEYWORD_NULL:
            return new SegmentCode(TypedKlass.NULL, text);
        default:
            throw reportError("Unexpected token type :" + token.getType(), ctx);
        }
    }

    @Override
    public Code visitExpression_list(Expression_listContext ctx) {
        List<ExpressionContext> expression_list = ctx.expression();
        SegmentListCode code = new SegmentListCode(expression_list.size());

        for (ExpressionContext expression : expression_list) {
            Code c = expression.accept(this);
            code.addChild((SegmentCode) c);
        }
        return code;
    }

    @Override
    public Code visitType(TypeContext ctx) {
        StringBuilder name = new StringBuilder();
        for (TerminalNode node : ctx.IDENTIFIER()) {
            if (name.length() > 0) {
                name.append('.');
            }
            name.append(node.getText());
        }

        // 查找 klass
        Class<?> klass = resolver.resolveClass(name.toString());
        if (klass == null) {
            throw reportError("java.lang.ClassNotFoundException: " + name.toString(), ctx);
        }

        // 查找泛型类型 typeArgs
        TypedKlass[] typeArgs = TypedKlass.EMPTY_TYPE_ARGS;
        Type_argumentsContext type_arguments = ctx.type_arguments();
        if (type_arguments != null) {
            SegmentListCode c = (SegmentListCode) type_arguments.accept(this);
            typeArgs = new TypedKlass[c.size()];
            for (int i = 0; i < typeArgs.length; i++) {
                typeArgs[i] = c.getChild(i).getTypedKlass();
            }
        }

        // 如果是数组类型，则把 klass 转成数组
        String array_suffix = "";
        List<Type_array_suffixContext> type_array_suffix = ctx.type_array_suffix();
        for (Type_array_suffixContext c : type_array_suffix) {
            Code code = c.accept(this);
            array_suffix = array_suffix + code.getSource();
        }

        if (array_suffix.length() > 0) {
            // 转换成 Array Class, 重新 resolve
            String klassName = name.toString() + array_suffix;
            klass = resolver.resolveClass(klassName);
            if (klass == null) {
                throw reportError("java.lang.ClassNotFoundException: " + klassName, ctx);
            }
        }

        // 返回带有的泛型信息的 Class
        TypedKlass typedKlass = TypedKlass.create(klass, typeArgs);
        return new SegmentCode(typedKlass, typedKlass.getSource());
    }

    @Override
    public Code visitType_array_suffix(Type_array_suffixContext ctx) {
        return new SegmentCode((TypedKlass) null, "[]");
    }

    @Override
    public Code visitType_arguments(Type_argumentsContext ctx) {
        return ctx.type_list().accept(this);
    }

    @Override
    public Code visitType_list(Type_listContext ctx) {
        List<Type_nameContext> type_name_list = ctx.type_name();
        SegmentListCode code = new SegmentListCode(type_name_list.size());

        for (Type_nameContext type_name : type_name_list) {
            Code c = type_name.accept(this);
            code.addChild((SegmentCode) c);
        }
        return code;
    }

    @Override
    public Code visitType_name(Type_nameContext ctx) {
        TypeContext type = ctx.type();
        if (type != null) {
            return type.accept(this);
        }

        // List<?>
        return new SegmentCode(TypedKlass.WildcharTypedKlass, "?");
    }

    // -----------------------------------------------------------
    // 变量必须是合法的 java 变量
    private String assert_java_identifier(TerminalNode node, boolean isDefining) {
        String name = node.getText();

        if ("for".equals(name)) {
            if (isDefining) {
                throw reportError("Syntax error on token \"" + name + "\" is not a valid identifier.", node);
            }
            return name;
        }
        if ("context".equals(name)) {
            if (isDefining) {
                throw reportError("Duplicate local variable \"" + name + "\" is a reserved identifier.", node);
            }
            return name;
        }

        if (SourceVersion.isKeyword(name)) {
            throw reportError("Syntax error on token \"" + name + "\", It is not a valid identifier in Java.", node);
        }
        if (name.startsWith("$")) {
            throw reportError("Local variable \"" + name + "\" can't start with '$', it is a reserved identifier.", node);
        }

        return name;
    }

    // 检验 ctx 必须在 #for 里面, 但不能在 for-else 里面
    private void assert_inside_of_for_directive(ParserRuleContext ctx, String name) {
        // 还有一种方法，直接看 forStack 是否为空就可以了
        ParserRuleContext p = ctx.getParent();
        while (p != null) {
            if (p instanceof For_directiveContext) {
                return;
            }
            if (p instanceof Else_directiveContext) {
                // 跳过可能的  for-else 的情况, 继续向上查找
                // 当然如果时候 if-else 的情况, 也可以跳过这个 #if，没有问题
                p = p.getParent();
            }
            p = p.getParent();
        }
        throw reportError(name + " cannot be used outside of a #for directive", ctx);
    }

    private ExpressionContext get_not_null_constantContext(ExpressionContext node) {
        if (node instanceof Expr_constantContext && node.getStart().getType() == JetTemplateParser.KEYWORD_NULL) {
            throw reportError("Unexpected token: invalid keyword null in here.", node);
        }
        return node;
    }

    // 确保返回的代码类型必须是 boolean 类型的
    private String get_if_expression_source(SegmentCode code) {
        if (Boolean.TYPE.equals(code.getKlass())) {
            return code.getSource();
        } else {
            return "JetUtils.asBoolean(" + code.getSource() + ")";
        }
    }

    // -----------------------------------------------------------
    // 创建一个全局唯一的变量名称
    private String getUid(String prefix) {
        return "$" + prefix + "_" + (uuid++);
    }

    private RuntimeException reportError(String message, ParseTree node) {
        if (node instanceof ParserRuleContext) {
            parser.notifyErrorListeners(((ParserRuleContext) node).getStart(), message, null);
        } else if (node instanceof TerminalNode) {
            parser.notifyErrorListeners(((TerminalNode) node).getSymbol(), message, null);
        }
        return new RuntimeException(message);
    }
}
