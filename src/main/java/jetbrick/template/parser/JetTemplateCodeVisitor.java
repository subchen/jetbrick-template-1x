/**
 * jetbrick-template
 * http://subchen.github.io/jetbrick-template/
 *
 * Copyright 2010-2013 Guoqiang Chen. All rights reserved.
 * Email: subchen@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jetbrick.template.parser;

import java.lang.reflect.*;
import java.util.*;
import javax.lang.model.SourceVersion;
import jetbrick.template.*;
import jetbrick.template.parser.code.*;
import jetbrick.template.parser.grammer.*;
import jetbrick.template.parser.grammer.JetTemplateParser.BlockContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Break_directiveContext;
import jetbrick.template.parser.grammer.JetTemplateParser.ConstantContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Continue_directiveContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Define_directiveContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Define_expressionContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Define_expression_listContext;
import jetbrick.template.parser.grammer.JetTemplateParser.DirectiveContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Else_directiveContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Elseif_directiveContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Expr_array_getContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Expr_array_listContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Expr_class_castContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Expr_compare_conditionContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Expr_compare_equalityContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Expr_compare_notContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Expr_compare_relationalContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Expr_conditional_ternaryContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Expr_constantContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Expr_field_accessContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Expr_function_callContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Expr_groupContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Expr_hash_mapContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Expr_identifierContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Expr_instanceofContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Expr_math_binary_basicContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Expr_math_binary_bitwiseContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Expr_math_binary_shiftContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Expr_math_unary_prefixContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Expr_math_unary_suffixContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Expr_method_invocationContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Expr_new_arrayContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Expr_new_objectContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Expr_static_field_accessContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Expr_static_method_invocationContext;
import jetbrick.template.parser.grammer.JetTemplateParser.ExpressionContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Expression_listContext;
import jetbrick.template.parser.grammer.JetTemplateParser.For_directiveContext;
import jetbrick.template.parser.grammer.JetTemplateParser.For_expressionContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Hash_map_entry_listContext;
import jetbrick.template.parser.grammer.JetTemplateParser.If_directiveContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Include_directiveContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Invalid_directiveContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Macro_directiveContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Put_directiveContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Set_directiveContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Set_expressionContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Static_type_nameContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Stop_directiveContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Tag_directiveContext;
import jetbrick.template.parser.grammer.JetTemplateParser.TemplateContext;
import jetbrick.template.parser.grammer.JetTemplateParser.TextContext;
import jetbrick.template.parser.grammer.JetTemplateParser.TypeContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Type_argumentsContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Type_array_suffixContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Type_listContext;
import jetbrick.template.parser.grammer.JetTemplateParser.Type_nameContext;
import jetbrick.template.parser.grammer.JetTemplateParser.ValueContext;
import jetbrick.template.parser.support.*;
import jetbrick.template.resource.Resource;
import jetbrick.template.runtime.JetTagContext;
import jetbrick.template.utils.PathUtils;
import jetbrick.template.utils.StringEscapeUtils;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.*;

// Visitor 模式访问器，用来生成 Java 代码
public class JetTemplateCodeVisitor extends AbstractParseTreeVisitor<Code> implements JetTemplateParserVisitor<Code> {
    private final JetEngine engine;
    private final Resource resource;
    private final JetTemplateParser parser;
    private final VariableResolver resolver;
    private final JetSecurityManager securityManager;
    private final boolean trimDirectiveLine;
    private final boolean trimDirectiveComments;
    private final String commentsPrefix;
    private final String commentsSuffix;

    private TemplateClassCode tcc; //
    private ScopeCode scopeCode; // 当前作用域对应的 Code
    private Map<String, MacroCode> macroMap; // 宏定义
    private Map<String, TextCode> textCache; // 文本内容缓存(可以减少冗余 Text)
    private Deque<String> forStack; // 维护嵌套 #for 的堆栈，可以识别是否在嵌入在 #for 里面, 内部存储当前 for 的真实变量名
    private int uuid = 1; // 计数器

    public JetTemplateCodeVisitor(JetEngine engine, VariableResolver resolver, JetSecurityManager securityManager, JetTemplateParser parser, Resource resource) {
        this.engine = engine;
        this.parser = parser;
        this.resolver = resolver;
        this.securityManager = securityManager;
        this.resource = resource;
        this.trimDirectiveLine = engine.getConfig().isTrimDirectiveLine();
        this.trimDirectiveComments = engine.getConfig().isTrimDirectiveComments();
        this.commentsPrefix = engine.getConfig().getTrimDirectiveCommentsPrefix();
        this.commentsSuffix = engine.getConfig().getTrimDirectiveCommentsSuffix();

        this.textCache = new HashMap<String, TextCode>(32);
        this.forStack = new ArrayDeque<String>(8);

        // 专门处理是否存在未完成的解析
        Token token = parser.getCurrentToken();
        if (token.getType() != Token.EOF) {
            throw reportError("Invalid " + token.getText() + " directive in here.", token);
        }
    }

    @Override
    public Code visitTemplate(TemplateContext ctx) {
        tcc = new TemplateClassCode();
        tcc.setPackageName(resource.getPackageName());
        tcc.setClassName(resource.getClassName());
        tcc.setTemplateName(resource.getName());
        tcc.setEncoding(engine.getConfig().getOutputEncoding());

        scopeCode = tcc.getMethodCode();
        scopeCode.define(Code.CONTEXT_NAME, TypedKlass.JetContext);
        scopeCode.setBodyCode(ctx.block().accept(this));
        return tcc;
    }

    @Override
    public Code visitBlock(BlockContext ctx) {
        int size = ctx.getChildCount();
        BlockCode code = scopeCode.createBlockCode(size);
        if (size == 0) return code;

        for (int i = 0; i < size; i++) {
            ParseTree node = ctx.children.get(i);
            Code c = node.accept(this);

            if (node instanceof TextContext) {
                // 文本节点
                TextCode textCode = (TextCode) c;

                if (trimDirectiveLine || trimDirectiveComments) {
                    ParseTree prev = (i > 0) ? ctx.children.get(i - 1) : null;
                    ParseTree next = (i < size - 1) ? ctx.children.get(i + 1) : null;

                    boolean trimLeft;
                    boolean keepLeftNewLine = false;
                    if (prev == null) {
                        trimLeft = !(ctx.getParent() instanceof TemplateContext);
                    } else {
                        trimLeft = prev instanceof DirectiveContext;
                        if (trimLeft) {
                            // inline directive, 对于一个内联的 #if, #for 指令，后面有要求保留一个 NewLine
                            // @see https://github.com/subchen/jetbrick-template/issues/25
                            ParserRuleContext directive = (ParserRuleContext) ((DirectiveContext) prev).getChild(0);
                            if (directive instanceof If_directiveContext || directive instanceof For_directiveContext) {
                                if (directive.getStart().getLine() == directive.getStop().getLine()) {
                                    keepLeftNewLine = true; // 保留一个 NewLine
                                }
                            }
                        }
                    }

                    boolean trimRight;
                    if (next == null) {
                        trimRight = !(ctx.getParent() instanceof TemplateContext);
                    } else {
                        trimRight = (next instanceof DirectiveContext);
                    }

                    // trim 指令两边的注释
                    if (trimDirectiveComments) {
                        textCode.trimComments(trimLeft, trimRight, commentsPrefix, commentsSuffix);
                    }
                    // trim 指令两边的空白内容
                    if (trimDirectiveLine) {
                        textCode.trimEmptyLine(trimLeft, trimRight, keepLeftNewLine);
                    }

                    // trim 掉 #tag 和 #macro 指令最后一个多余的 '\n'
                    if (next == null) {
                        if (ctx.getParent() instanceof Tag_directiveContext || ctx.getParent() instanceof Macro_directiveContext) {
                            textCode.trimLastNewLine();
                        }
                    }
                }

                if (!textCode.isEmpty()) {
                    // 如果有相同内容的Text，则从缓存中读取
                    TextCode old = textCache.get(textCode.getText());
                    if (old == null) {
                        old = textCode;
                        textCache.put(textCode.getText(), textCode);
                        // add text into field
                        tcc.addField(textCode.getId(), textCode.getText());
                    }
                    code.addLine(old.toString());
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
        String source = code.toString();

        // 如果返回值是 void，那么不需要 print 语句。
        if (code instanceof SegmentCode) {
            Class<?> klass = ((SegmentCode) code).getKlass();
            if (Void.TYPE.equals(klass)) {
                return scopeCode.createLineCode(source + "; // line: " + ctx.getStart().getLine());
            }
        }

        Token token = ((TerminalNode) ctx.getChild(0)).getSymbol();
        if (token.getType() == JetTemplateParser.VALUE_ESCAPED_OPEN) {
            source = "JetUtils.asEscapeHtml(" + source + ")";
        }

        if ("null".equals(source)) {
            // 防止编译出错 (也可以生成一个空行)
            source = "(Object)null";
        }
        return scopeCode.createLineCode("$out.print(" + source + "); // line: " + ctx.getStart().getLine());
    }

    @Override
    public Code visitDirective(DirectiveContext ctx) {
        return ctx.getChild(0).accept(this);
    }

    @Override
    public Code visitDefine_directive(Define_directiveContext ctx) {
        SegmentListCode define_expression_list = (SegmentListCode) ctx.define_expression_list().accept(this);
        BlockCode code = scopeCode.createBlockCode(define_expression_list.size());

        for (SegmentCode node : define_expression_list.getChildren()) {
            DefineExpressionCode c = (DefineExpressionCode) node;
            String name = c.getName();

            if (!scopeCode.define(name, c.getTypedKlass())) {
                throw reportError("Duplicate local variable " + name, c.getNode());
            }

            String typeName = c.getTypedKlass().asBoxedTypedKlass().toString();
            code.addLine(typeName + " " + name + " = (" + typeName + ") " + Code.CONTEXT_NAME + ".get(\"" + name + "\"); // line: " + c.getNode().getStart().getLine());
        }
        return code;
    }

    @Override
    public Code visitDefine_expression_list(Define_expression_listContext ctx) {
        List<Define_expressionContext> define_expression_list = ctx.define_expression();
        SegmentListCode code = new SegmentListCode(define_expression_list.size());

        for (Define_expressionContext define_expression : define_expression_list) {
            code.addChild((SegmentCode) define_expression.accept(this));
        }
        return code;
    }

    @Override
    public Code visitDefine_expression(Define_expressionContext ctx) {
        SegmentCode code = (SegmentCode) ctx.type().accept(this);
        String name = assert_java_identifier(ctx.IDENTIFIER(), true);
        return new DefineExpressionCode(code.getTypedKlass(), name, ctx);
    }

    @Override
    public Code visitSet_directive(Set_directiveContext ctx) {
        List<Set_expressionContext> set_expression_list = ctx.set_expression();
        BlockCode code = scopeCode.createBlockCode(set_expression_list.size());

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

        boolean defining = false; // 是否同时定义一个变量

        TypedKlass lhs = null; // 变量类型
        TypeContext type = ctx.type();
        if (type != null) {
            defining = true;
            // 定义一个变量
            SegmentCode c = (SegmentCode) type.accept(this);
            lhs = c.getTypedKlass();
            if (!scopeCode.define(name, lhs)) {
                throw reportError("Duplicate local variable " + name, ctx.IDENTIFIER());
            }
        } else {
            // 直接赋值，如果变量没有定义，则先定义
            lhs = scopeCode.resolve(name, false);
            defining = (lhs == null);
            if (defining) {
                lhs = code.getTypedKlass();
                scopeCode.define(name, lhs);
            }
        }

        // 进行赋值语句类型检查
        if (!ClassUtils.isAssignable(lhs.getKlass(), code.getKlass())) { // 是否支持正常赋值
            if (!ClassUtils.isAssignable(code.getKlass(), lhs.getKlass())) { // 是否支持强制类型转换
                throw reportError("Type mismatch: cannot convert from " + code.getTypedKlass().toString() + " to " + lhs.toString(), ctx);
            }
        }

        BlockCode c = scopeCode.createBlockCode(2);
        String source = name + " = (" + lhs.getSource() + ") " + code.toString() + "; // line: " + ctx.getStart().getLine();
        if (defining) {
            source = lhs.getSource() + " " + source;
        }
        c.addLine(source);
        c.addLine(Code.CONTEXT_NAME + ".put(\"" + name + "\", " + name + ");");
        return c;
    }

    @Override
    public Code visitPut_directive(Put_directiveContext ctx) {
        List<ExpressionContext> expression_list = ctx.expression();
        int size = expression_list.size();
        if (size == 0 || size % 2 == 1) {
            throw reportError("Mismatched arguments count for #put directive", ctx);
        }

        BlockCode code = scopeCode.createBlockCode(size / 2);
        for (int i = 0; i < size; i += 2) {
            SegmentCode name = (SegmentCode) expression_list.get(i).accept(this);
            SegmentCode value = (SegmentCode) expression_list.get(i + 1).accept(this);
            if (!String.class.equals(name.getKlass())) {
                throw reportError("The argument type can't cast to String.class for #put directive", name.getNode());
            }
            assert_not_void_expression(value);
            code.addLine(Code.CONTEXT_NAME + ".putAsParents(" + name.toString() + ", " + value.toString() + "); // line: " + ctx.getStart().getLine());
        }
        return code;
    }

    @Override
    public Code visitIf_directive(If_directiveContext ctx) {
        BlockCode code = scopeCode.createBlockCode(16);

        SegmentCode expr_code = (SegmentCode) ctx.expression().accept(this);
        code.addLine("if (" + get_if_expression_source(expr_code) + ") { // line: " + ctx.getStart().getLine());
        scopeCode = scopeCode.push();
        code.addChild(ctx.block().accept(this));
        scopeCode = scopeCode.pop();
        code.addLine("}");

        // elseif ...
        List<Elseif_directiveContext> elseif_directive_list = ctx.elseif_directive();
        for (Elseif_directiveContext elseif_directive : elseif_directive_list) {
            code.addChild(elseif_directive.accept(this));
        }

        // else ...
        Else_directiveContext else_directive = ctx.else_directive();
        if (else_directive != null) {
            code.addChild(else_directive.accept(this));
        }

        return code;
    }

    @Override
    public Code visitElseif_directive(Elseif_directiveContext ctx) {
        BlockCode code = scopeCode.createBlockCode(16);

        SegmentCode expr_code = (SegmentCode) ctx.expression().accept(this);
        code.addLine("else if (" + get_if_expression_source(expr_code) + ") { // line: " + ctx.getStart().getLine());
        scopeCode = scopeCode.push();
        code.addChild(ctx.block().accept(this));
        scopeCode = scopeCode.pop();
        code.addLine("}");

        return code;
    }

    @Override
    public Code visitElse_directive(Else_directiveContext ctx) {
        BlockCode code = scopeCode.createBlockCode(16);

        if (ctx.getParent() instanceof If_directiveContext) {
            code.addLine("else { // line: " + ctx.getStart().getLine());
        }

        scopeCode = scopeCode.push();
        code.addChild(ctx.block().accept(this));
        scopeCode = scopeCode.pop();

        if (ctx.getParent() instanceof If_directiveContext) {
            code.addLine("}");
        }

        return code;
    }

    @Override
    public Code visitFor_directive(For_directiveContext ctx) {
        BlockCode code = scopeCode.createBlockCode(16);
        String id_for = getUid("for");

        scopeCode = scopeCode.push();
        // 注意：for循环变量的作用域要放在 for 内部， 防止出现变量重定义错误
        ForExpressionCode for_expr_code = (ForExpressionCode) ctx.for_expression().accept(this);
        // for block
        forStack.push(id_for);
        Code for_block_code = ctx.block().accept(this);
        forStack.pop();
        scopeCode = scopeCode.pop();

        // for-else
        Else_directiveContext else_directive = ctx.else_directive();
        Code for_else_block = (else_directive == null) ? null : else_directive.accept(this);

        // 生成代码
        code.addLine("JetForIterator " + id_for + " = new JetForIterator(" + for_expr_code.toString() + ");");
        code.addLine("while (" + id_for + ".hasNext()) { // line: " + ctx.getStart().getLine());

        // class item = (class) it.next() ...
        String typeName = for_expr_code.getKlassName();
        code.addLine("  " + typeName + " " + for_expr_code.getName() + " = (" + typeName + ") " + id_for + ".next();");
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
        assert_not_void_expression(code);

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

        if (!scopeCode.define(name, resultKlass)) {
            throw reportError("Duplicate local variable " + name, ctx.IDENTIFIER());
        }

        return new ForExpressionCode(resultKlass, name, code.toString(), ctx);
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
        return scopeCode.createLineCode(source);
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
        return scopeCode.createLineCode(source);
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
        return scopeCode.createLineCode(source);
    }

    @Override
    public Code visitInclude_directive(Include_directiveContext ctx) {
        Expression_listContext expression_list = ctx.expression_list();
        SegmentListCode childrenCode = (SegmentListCode) expression_list.accept(this);
        if (childrenCode.size() > 2) {
            throw reportError("Arguments mismatch for #include directive.", ctx);
        }

        // argument 1: file
        SegmentCode fileCode = childrenCode.getChild(0);
        ExpressionContext fileExpression = expression_list.expression(0);
        if (!String.class.equals(fileCode.getKlass())) {
            throw reportError("Type mismatch: the first argument cannot convert from " + fileCode.getKlassName() + " to String", fileExpression);
        }

        // argument 2: parameters
        SegmentCode parametersCode = null;
        if (childrenCode.size() > 1) {
            parametersCode = childrenCode.getChild(1);
            if (!(Map.class.equals(parametersCode.getKlass()))) {
                throw reportError("Type mismatch: the second argument cannot convert from " + parametersCode.getKlassName() + " to Map", expression_list.expression(1));
            }
        }

        // 如果 file 是常量，那么进行 file.exists() 校验
        if (fileExpression instanceof Expr_constantContext) {
            String file = fileCode.toString();
            file = file.substring(1, file.length() - 1);
            file = StringEscapeUtils.unescapeJava(file);
            file = PathUtils.getAbsolutionName(resource.getName(), file);
            if (!engine.lookupResource(file)) {
                throw reportError("FileNotFoundException: " + file, fileExpression);
            }
        }

        // 生成代码
        StringBuilder source = new StringBuilder();
        source.append("JetUtils.asInclude($ctx, ");
        source.append(fileCode.toString());
        source.append(", (Map<String, Object>)");
        source.append((parametersCode != null) ? parametersCode.toString() : "null");
        source.append("); // line: ");
        source.append(ctx.getStart().getLine());
        return scopeCode.createLineCode(source.toString());
    }

    @Override
    public Code visitTag_directive(Tag_directiveContext ctx) {
        String text = ctx.getChild(0).getText();
        String name = text.substring(5, text.length() - 1).trim();

        TagCode tagCode = scopeCode.createTagCode();
        tagCode.setTagId(getUid("tag"));
        scopeCode = tagCode.getMethodCode();
        scopeCode.define(Code.CONTEXT_NAME, TypedKlass.JetContext);
        scopeCode.setBodyCode(ctx.block().accept(this)); // add body content
        scopeCode = scopeCode.pop();

        // finding tag function
        Expression_listContext expression_list = ctx.expression_list();
        SegmentListCode segmentListCode = (expression_list == null) ? SegmentListCode.EMPTY : (SegmentListCode) expression_list.accept(this);
        Class<?>[] parameterTypes = segmentListCode.getParameterTypes(JetTagContext.class);
        Method method = resolver.resolveTagMethod(name, parameterTypes);
        if (method == null) {
            throw reportError("Undefined tag definition: " + getMethodSignature(name, parameterTypes), ctx);
        }

        tagCode.setMethod(method);
        tagCode.setExpressionListCode(segmentListCode);
        return tagCode;
    }

    @Override
    public Code visitMacro_directive(Macro_directiveContext ctx) {
        String text = ctx.getChild(0).getText();
        String name = text.substring(7, text.length() - 1).trim();

        MacroCode macroCode = scopeCode.createMacroCode();
        macroCode.setName(name);

        scopeCode = macroCode.getMethodCode();
        scopeCode.define(Code.CONTEXT_NAME, TypedKlass.JetContext);

        // 处理参数
        Define_expression_listContext define_expression_list = ctx.define_expression_list();
        if (define_expression_list != null) {
            SegmentListCode define_list_code = (SegmentListCode) define_expression_list.accept(this);
            macroCode.setDefineListCode(define_list_code);

            // 设置参数 Context
            for (SegmentCode node : define_list_code.getChildren()) {
                DefineExpressionCode c = (DefineExpressionCode) node;
                scopeCode.define(c.getName(), c.getTypedKlass());
            }
        }

        scopeCode.setBodyCode(ctx.block().accept(this)); // add body content
        scopeCode = scopeCode.pop();

        if (macroMap == null) {
            macroMap = new HashMap<String, MacroCode>(8);
        }
        MacroCode old = macroMap.put(name, macroCode);
        if (old != null) {
            throw reportError("Duplicated macro defination " + name, ctx);
        }

        tcc.addMacro(macroCode);
        return Code.EMPTY;
    }

    @Override
    public Code visitInvalid_directive(Invalid_directiveContext ctx) {
        throw reportError("Missing arguments for " + ctx.getText() + " directive.", ctx);
    }

    @Override
    public Code visitExpr_group(Expr_groupContext ctx) {
        SegmentCode code = (SegmentCode) ctx.expression().accept(this);
        String source = "(" + code.toString() + ")";
        return new SegmentCode(code.getTypedKlass(), source, ctx);
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
            source = "Arrays.asList(" + code.toString() + ")";
        }
        return new SegmentCode(List.class, source, ctx);
    }

    @Override
    public Code visitExpr_hash_map(Expr_hash_mapContext ctx) {
        String source = "Collections.EMPTY_MAP";
        Hash_map_entry_listContext hash_map_entry_list = ctx.hash_map_entry_list();
        if (hash_map_entry_list != null) {
            Code code = hash_map_entry_list.accept(this);
            source = "JetUtils.asMap(" + code.toString() + ")";
        }
        return new SegmentCode(Map.class, source, ctx);
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
            String forStatus = forStack.peek(); // 取出 forStatus 的实际变量名
            return new SegmentCode(TypedKlass.JetForStatus, forStatus, ctx);
        }

        // 找到变量的类型
        TypedKlass resultKlass = scopeCode.resolve(name, false);
        if (resultKlass == null) {
            // 没有定义过，继续向上深度查找
            resultKlass = scopeCode.resolve(name, true);

            // 没有定义过，则查找全局定义
            if (resultKlass == null) {
                resultKlass = resolver.resolveVariable(name);
            }
            if (scopeCode.define(name, resultKlass, true)) {
                if (resultKlass == TypedKlass.Object) {
                    //removed unsed warning in 1.2.0
                    //log.warn("line " + ctx.getStart().getLine() + ": Implicit definition for context variable: " + resultKlass.toString() + " " + name);
                }
            }
        }

        return new SegmentCode(resultKlass, name, ctx);
    }

    @Override
    public Code visitExpr_field_access(Expr_field_accessContext ctx) {
        SegmentCode code = (SegmentCode) ctx.expression().accept(this);
        String name = ctx.IDENTIFIER().getText();

        assert_not_null_constantContext(code.getNode());

        // 进行类型推导，找到方法的返回类型
        code = code.asBoxedSegmentCode();
        Class<?> beanClass = code.getKlass();
        Member member = null;

        if ((!beanClass.isArray()) || (!"length".equals(name))) { // not array.length
            member = resolver.resolveProperty(beanClass, name);
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
                if (Object.class.equals(beanClass)) {
                    err.append("\n advise: ");
                    if (code.getNode() instanceof Expr_identifierContext) {
                        err.append("Please use #define(type ");
                        err.append(code.getNode().getText());
                        err.append(") to define variable type.");
                    } else {
                        err.append("Please use #set(type xxx = ");
                        err.append(code.getNode().getText());
                        err.append(") to define expression type.");
                    }
                }
                throw reportError(err.toString(), ctx.IDENTIFIER());
            }
        }

        boolean isSafeCall = "?.".equals(ctx.getChild(1).getText());

        // 生成code
        StringBuilder sb = new StringBuilder(64);
        TypedKlass resultKlass = null;
        if (member instanceof Method) {
            Method method = (Method) member;
            if (securityManager != null) {
                securityManager.checkMemberAccess(method);
            }
            resultKlass = TypedKlassUtils.getMethodReturnTypedKlass(method);
            if (method.getParameterTypes().length == 0) {
                // getXXX() or isXXX()
                if (isSafeCall) { // 安全调用，防止 NullPointException
                    sb.append("((");
                    sb.append(code.toString());
                    sb.append("==null)?null:");
                    sb.append(code.toString());
                    sb.append('.');
                    sb.append(method.getName());
                    sb.append("())");
                } else {
                    sb.append(code.toString());
                    sb.append('.');
                    sb.append(method.getName());
                    sb.append("()");
                }
            } else {
                // get(String)
                if (isSafeCall) { // 安全调用，防止 NullPointException
                    sb.append("((");
                    sb.append(code.toString());
                    sb.append("==null)?null:");
                    sb.append(code.toString());
                    sb.append(".get(\"");
                    sb.append(name);
                    sb.append("\"))");
                } else {
                    sb.append(code.toString());
                    sb.append(".get(\"");
                    sb.append(name);
                    sb.append("\")");
                }
            }
        } else {
            if (member instanceof Field) {
                if (securityManager != null) {
                    securityManager.checkMemberAccess((Field) member);
                }
                resultKlass = TypedKlassUtils.getFieldTypedKlass((Field) member);
            } else {
                // array.length
                resultKlass = TypedKlass.create(Integer.TYPE);
            }
            if (isSafeCall) { // 安全调用，防止 NullPointException
                sb.append("((");
                sb.append(code.toString());
                sb.append("==null)?null:");
                sb.append(code.toString());
                sb.append('.');
                sb.append(name);
                sb.append(')');
            } else {
                sb.append(code.toString());
                sb.append('.');
                sb.append(name);
            }
        }

        if (isSafeCall) {
            resultKlass = resultKlass.asBoxedTypedKlass();
        }
        return new SegmentCode(resultKlass, sb.toString(), ctx);
    }

    @Override
    public Code visitExpr_method_invocation(Expr_method_invocationContext ctx) {
        // 处理参数
        Expression_listContext expression_list = ctx.expression_list();
        SegmentListCode segmentListCode = (expression_list == null) ? SegmentListCode.EMPTY : (SegmentListCode) expression_list.accept(this);
        Class<?>[] parameterTypes = segmentListCode.getParameterTypes();

        // 查找方法
        SegmentCode code = (SegmentCode) ctx.expression().accept(this);
        assert_not_null_constantContext(code.getNode());

        code = code.asBoxedSegmentCode();
        Class<?> beanClass = code.getKlass();
        String name = ctx.IDENTIFIER().getText();
        Method bean_method = resolver.resolveMethod(beanClass, name, parameterTypes);
        Method tool_method = (bean_method != null) ? null : resolver.resolveToolMethod(beanClass, name, parameterTypes);
        boolean tool_advanced = false;
        if (tool_method == null) {
            tool_method = resolver.resolveToolMethod_advanced(beanClass, name, parameterTypes);
            tool_advanced = true;
        }
        if (bean_method == null && tool_method == null) {
            // reportError
            StringBuilder err = new StringBuilder(128);
            err.append("The method ").append(getMethodSignature(name, parameterTypes));
            err.append(" is undefined for the type ");
            err.append(beanClass.getName());
            err.append('.');
            if (Object.class.equals(beanClass)) {
                err.append("\n advise: ");
                if (code.getNode() instanceof Expr_identifierContext) {
                    err.append("Please use #define(type ");
                    err.append(code.getNode().getText());
                    err.append(") to define variable type.");
                } else {
                    err.append("Please use #set(type xxx = ");
                    err.append(code.getNode().getText());
                    err.append(") to define expression type.");
                }
            }
            throw reportError(err.toString(), ctx.IDENTIFIER());
        }

        boolean isSafeCall = "?.".equals(ctx.getChild(1).getText());

        // 生成code
        StringBuilder sb = new StringBuilder(64);
        if (tool_method != null) {
            // tool method
            sb.append(ClassUtils.getShortClassName(tool_method.getDeclaringClass()));
            sb.append('.');
            sb.append(name);
            sb.append('(');
            sb.append(code.toString());
            if (tool_advanced) {
                sb.append(",$ctx");
            }
            if (segmentListCode != null && segmentListCode.size() > 0) {
                sb.append(',');
            }
        } else {
            if (isSafeCall) { // 安全调用，防止 NullPointException
                sb.append('(');
                sb.append(code.toString());
                sb.append("==null)?null:");
                sb.append(code.toString());
                sb.append('.');
                sb.append(name);
                sb.append('(');
            } else {
                sb.append(code.toString());
                sb.append('.');
                sb.append(name);
                sb.append('(');
            }
        }
        if (segmentListCode != null && segmentListCode.size() > 0) {
            sb.append(segmentListCode.toString());
        }
        sb.append(')');

        if (isSafeCall) { // 为了安全起见，用()包起来
            sb.insert(0, '(').append(')');
        }

        // 得到方法的返回类型
        Method method = (bean_method == null) ? tool_method : bean_method;
        if (securityManager != null) {
            securityManager.checkMemberAccess(method);
        }

        TypedKlass resultKlass = TypedKlassUtils.getMethodReturnTypedKlass(method);
        if (isSafeCall) {
            resultKlass = resultKlass.asBoxedTypedKlass();
        }
        return new SegmentCode(resultKlass, sb.toString(), ctx);
    }

    @Override
    public Code visitExpr_function_call(Expr_function_callContext ctx) {
        // 处理参数
        Expression_listContext expression_list = ctx.expression_list();
        SegmentListCode segmentListCode = (expression_list == null) ? SegmentListCode.EMPTY : (SegmentListCode) expression_list.accept(this);
        Class<?>[] parameterTypes = segmentListCode.getParameterTypes();

        // 查找方法
        String name = ctx.IDENTIFIER().getText();

        // 优先查找 macro
        MacroCode macroCode = null;
        if (macroMap != null) {
            macroCode = macroMap.get(name);
            if (macroCode != null) {
                // macro 参数匹配
                SegmentListCode defineListCode = macroCode.getDefineListCode();
                int size = (defineListCode == null) ? 0 : defineListCode.size();
                if (parameterTypes.length != size) {
                    throw reportError("Arguments mismatch for #macro " + getMethodSignature(name, parameterTypes) + ".", ctx.IDENTIFIER());
                }
                for (int i = 0; i < parameterTypes.length; i++) {
                    if (!ClassUtils.isAssignable(parameterTypes[i], defineListCode.getChild(i).getKlass())) {
                        throw reportError("Arguments mismatch for #macro " + getMethodSignature(name, parameterTypes) + ".", ctx.IDENTIFIER());
                    }
                }

                // 生成 macro 调用 code
                StringBuilder sb = new StringBuilder(64);
                sb.append("$macro_").append(name);
                sb.append("($ctx");
                if (segmentListCode != null && segmentListCode.size() > 0) {
                    sb.append(',').append(segmentListCode.toString());
                }
                sb.append(')');
                return new SegmentCode(TypedKlass.VOID, sb.toString(), ctx);
            }
        }

        // 查找扩展方法
        boolean advanced = false;
        Method method = resolver.resolveFunction(name, parameterTypes);
        if (method == null) {
            method = resolver.resolveFunction_advanced(name, parameterTypes);
            advanced = true;
        }
        if (method == null) {
            throw reportError("Undefined function or arguments mismatch: " + getMethodSignature(name, parameterTypes) + ".", ctx.IDENTIFIER());
        }
        if (securityManager != null) {
            securityManager.checkMemberAccess(method);
        }

        // 生成code
        StringBuilder sb = new StringBuilder(64);
        sb.append(ClassUtils.getShortClassName(method.getDeclaringClass()));
        sb.append('.');
        sb.append(name);
        sb.append('(');
        if (advanced) {
            sb.append("$ctx");
        }
        if (segmentListCode != null && segmentListCode.size() > 0) {
            if (advanced) sb.append(',');
            sb.append(segmentListCode.toString());
        }
        sb.append(')');

        TypedKlass typedKlass = TypedKlassUtils.getMethodReturnTypedKlass(method);
        return new SegmentCode(typedKlass, sb.toString(), ctx);
    }

    @Override
    public Code visitExpr_static_field_access(Expr_static_field_accessContext ctx) {
        SegmentCode static_type_name_code = (SegmentCode) ctx.static_type_name().accept(this);
        String typeName = static_type_name_code.toString();
        String name = ctx.IDENTIFIER().getText();

        Class<?> beanClass = resolver.resolveClass(typeName);
        if (beanClass == null) {
            throw reportError("java.lang.ClassNotFoundException: " + typeName, static_type_name_code.getNode());
        }
        Field field = resolver.resolveStaticField(beanClass, name);
        if (field == null) {
            throw reportError(name + " is not a static field for type " + beanClass.getName(), ctx.IDENTIFIER());
        }

        if (securityManager != null) {
            securityManager.checkMemberAccess(field);
        }

        String source = typeName + '.' + name;
        TypedKlass resultKlass = TypedKlassUtils.getFieldTypedKlass(field);
        return new SegmentCode(resultKlass, source, ctx);
    }

    @Override
    public Code visitExpr_static_method_invocation(Expr_static_method_invocationContext ctx) {
        SegmentCode static_type_name_code = (SegmentCode) ctx.static_type_name().accept(this);
        String typeName = static_type_name_code.toString();
        String name = ctx.IDENTIFIER().getText();

        // 获取静态 Class
        Class<?> beanClass = resolver.resolveClass(typeName);
        if (beanClass == null) {
            throw reportError("java.lang.ClassNotFoundException: " + typeName, static_type_name_code.getNode());
        }

        // 处理参数
        Expression_listContext expression_list = ctx.expression_list();
        SegmentListCode segmentListCode = (expression_list == null) ? SegmentListCode.EMPTY : (SegmentListCode) expression_list.accept(this);
        Class<?>[] parameterTypes = segmentListCode.getParameterTypes();

        Method method = resolver.resolveStaticMethod(beanClass, name, parameterTypes);
        if (method == null) {
            throw reportError("The static method " + getMethodSignature(name, parameterTypes) + " is undefined for the type " + beanClass.getName(), ctx.IDENTIFIER());
        }

        if (securityManager != null) {
            securityManager.checkMemberAccess(method);
        }

        // 生成代码
        StringBuilder sb = new StringBuilder();
        sb.append(ClassUtils.getShortClassName(method.getDeclaringClass()));
        sb.append('.');
        sb.append(name);
        sb.append('(');
        sb.append(segmentListCode.toString());
        sb.append(')');

        TypedKlass resultKlass = TypedKlassUtils.getMethodReturnTypedKlass(method);
        return new SegmentCode(resultKlass, sb.toString(), ctx);
    }

    @Override
    public Code visitStatic_type_name(Static_type_nameContext ctx) {
        List<TerminalNode> name_list = ctx.IDENTIFIER();
        StringBuilder sb = new StringBuilder();
        for (TerminalNode node : name_list) {
            if (sb.length() > 0) {
                sb.append('.');
            }
            sb.append(node.getText());
        }
        return new SegmentCode(TypedKlass.NULL, sb.toString(), ctx);
    }

    @Override
    public Code visitExpr_array_get(Expr_array_getContext ctx) {
        SegmentCode lhs = (SegmentCode) ctx.expression(0).accept(this);
        SegmentCode rhs = (SegmentCode) ctx.expression(1).accept(this);

        assert_not_null_constantContext(lhs.getNode());
        assert_not_null_constantContext(rhs.getNode());

        boolean isSafeCall = "?".equals(ctx.getChild(1).getText());

        Class<?> lhsKlass = lhs.getKlass();
        if (lhsKlass.isArray()) {
            if (!ClassUtils.isAssignable(Integer.TYPE, rhs.getKlass())) {
                throw reportError("Type mismatch: cannot convert from " + rhs.getKlassName() + " to int.", lhs.getNode());
            }
            StringBuilder sb = new StringBuilder();
            if (isSafeCall) {
                sb.append('(');
                sb.append(lhs.toString());
                sb.append("==null?null:");
                sb.append(lhs.toString());
                sb.append('[');
                sb.append(rhs.toString());
                sb.append("])");
            } else {
                sb.append(lhs.toString());
                sb.append('[');
                sb.append(rhs.toString());
                sb.append(']');
            }

            TypedKlass resultKlass = TypedKlass.create(lhsKlass.getComponentType(), lhs.getTypeArgs());
            if (isSafeCall) {
                resultKlass = resultKlass.asBoxedTypedKlass();
            }
            return new SegmentCode(resultKlass, sb.toString(), ctx);
        } else {
            TypedKlass resultKlass = null;

            // try to List.get(index) or Map.get(name) or JetContext.get(name)
            if (List.class.isAssignableFrom(lhsKlass)) {
                if (!ClassUtils.isAssignable(Integer.TYPE, rhs.getKlass())) {
                    throw reportError("The method get(int) in the type List is not applicable for the arguments (" + rhs.getKlassName() + ")", rhs.getNode());
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
                    throw reportError("The method get(String) in the type JetContext is not applicable for the arguments (" + rhs.getKlassName() + ")", rhs.getNode());
                }
                resultKlass = TypedKlass.Object;
            } else {
                throw reportError("Operator [] is not applicable for the object (" + lhs.getKlassName() + ").", ctx);
            }

            if (resultKlass == null) {
                resultKlass = TypedKlass.Object;
            }

            StringBuilder sb = new StringBuilder();
            if (isSafeCall) {
                sb.append('(');
                sb.append(lhs.toString());
                sb.append("==null?null:");
                sb.append(lhs.toString());
                sb.append(".get(");
                sb.append(rhs.toString());
                sb.append("))");
            } else {
                sb.append(lhs.toString());
                sb.append(".get(");
                sb.append(rhs.toString());
                sb.append(')');
            }

            if (isSafeCall) {
                resultKlass = resultKlass.asBoxedTypedKlass();
            }
            return new SegmentCode(resultKlass, sb.toString(), ctx);
        }
    }

    @Override
    public Code visitExpr_new_object(Expr_new_objectContext ctx) {
        SegmentCode code = (SegmentCode) ctx.type().accept(this);

        Expression_listContext expression_list = ctx.expression_list();
        SegmentListCode segmentListCode = (expression_list == null) ? SegmentListCode.EMPTY : (SegmentListCode) expression_list.accept(this);
        Class<?>[] parameterTypes = segmentListCode.getParameterTypes();

        // 查找对应的构造函数
        Class<?> beanClass = code.getKlass();
        Constructor<?> constructor = resolver.resolveConstructor(beanClass, parameterTypes);
        if (constructor == null) {
            // reportError
            StringBuilder err = new StringBuilder(128);
            err.append("The constructor ");
            err.append(getMethodSignature(beanClass.getSimpleName(), parameterTypes));
            err.append(" is undefined for the type ");
            err.append(beanClass.getName());
            err.append('.');
            throw reportError(err.toString(), ctx.type());
        }

        // 生成代码
        StringBuilder source = new StringBuilder(32);
        source.append("(new ").append(code.toString()).append('(');
        if (segmentListCode != null && segmentListCode.size() > 0) {
            source.append(segmentListCode.toString());
        }
        source.append("))");

        return new SegmentCode(code.getTypedKlass(), source.toString(), ctx);
    }

    @Override
    public Code visitExpr_new_array(Expr_new_arrayContext ctx) {
        SegmentCode code = (SegmentCode) ctx.type().accept(this);
        if (code.getKlass().isArray()) {
            throw reportError("Cannot specify an array dimension after an empty dimension", ctx.type());
        }

        StringBuilder typeSource = new StringBuilder(code.toString());

        // 生成代码
        StringBuilder source = new StringBuilder(32);
        source.append("(new ").append(code.toString());
        for (ExpressionContext expression : ctx.expression()) {
            SegmentCode c = (SegmentCode) expression.accept(this);
            if (!ClassUtils.isAssignable(Integer.TYPE, c.getKlass())) {
                throw reportError("Type mismatch: cannot convert from " + c.getKlassName() + " to int.", expression);
            }
            source.append('[').append(c.toString()).append(']');
            typeSource.append("[]");
        }
        source.append(')');

        TypedKlass resultKlass = resolver.resolveTypedKlass(typeSource.toString());
        return new SegmentCode(resultKlass, source.toString(), ctx);
    }

    @Override
    public Code visitExpr_class_cast(Expr_class_castContext ctx) {
        SegmentCode code = (SegmentCode) ctx.type().accept(this);
        Code expr_code = ctx.expression().accept(this);
        String source = "((" + code.toString() + ")" + expr_code.toString() + ")";
        return new SegmentCode(code.getTypedKlass(), source, ctx);
    }

    @Override
    public Code visitExpr_instanceof(Expr_instanceofContext ctx) {
        SegmentCode lhs = (SegmentCode) ctx.expression().accept(this);
        SegmentCode rhs = (SegmentCode) ctx.type().accept(this);

        if (!ClassUtils.isAssignable(lhs.getKlass(), rhs.getKlass()) && !ClassUtils.isAssignable(lhs.getKlass(), rhs.getKlass())) {
            throw reportError("Incompatible conditional operand types " + lhs.getKlassName() + " and " + rhs.getKlassName(), ctx.getChild(1));
        }
        String source = "(" + lhs.toString() + " instanceof " + rhs.toString() + ")";
        return new SegmentCode(Boolean.TYPE, source, ctx);
    }

    @Override
    public Code visitExpr_math_unary_suffix(Expr_math_unary_suffixContext ctx) {
        ExpressionContext expression = ctx.expression();
        SegmentCode code = (SegmentCode) expression.accept(this);
        String op = ctx.getChild(1).getText();

        assert_not_null_constantContext(expression);

        // ++, --
        if (expression.getChildCount() == 1 && expression.getChild(0) instanceof ConstantContext) {
            throw reportError("Invalid argument to operation " + op + ", required: variable, found Value", expression);
        }

        // 类型检查
        Class<?> resultKlass = PromotionUtils.get_unary_inc_dec(code.getKlass(), op);
        if (resultKlass == null) {
            throw reportError("The UnaryOperator \"" + op + "\" is not applicable for the operand " + code.getKlassName(), ctx.getChild(1));
        }

        String source = "(" + code.toString() + op + ")";
        return new SegmentCode(code.getTypedKlass(), source, ctx);
    }

    @Override
    public Code visitExpr_math_unary_prefix(Expr_math_unary_prefixContext ctx) {
        ExpressionContext expression = ctx.expression();
        SegmentCode code = (SegmentCode) expression.accept(this);
        String op = ctx.getChild(0).getText();

        assert_not_null_constantContext(expression);

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

        String source = "(" + op + code.toString() + ")";
        return new SegmentCode(code.getTypedKlass(), source, ctx);
    }

    @Override
    public Code visitExpr_math_binary_basic(Expr_math_binary_basicContext ctx) {
        SegmentCode lhs = (SegmentCode) ctx.expression(0).accept(this);
        SegmentCode rhs = (SegmentCode) ctx.expression(1).accept(this);
        String op = ctx.getChild(1).getText();

        // 类型检查
        Class<?> resultKlass = PromotionUtils.get_binary_basic(lhs.getKlass(), rhs.getKlass(), op);
        if (resultKlass == null) {
            throw reportError("The BinaryOperator \"" + op + "\" is not applicable for the operands " + lhs.getKlassName() + " and " + rhs.getKlassName(), ctx.getChild(1));
        }

        String source = "(" + lhs.toString() + op + rhs.toString() + ")";
        return new SegmentCode(resultKlass, source, ctx);
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
            throw reportError("The BinaryOperator \"" + op + "\" is not applicable for the operands " + lhs.getKlassName() + " and " + rhs.getKlassName(), ctx.getChild(1));
        }

        String source = "(" + lhs.toString() + op + rhs.toString() + ")";
        return new SegmentCode(resultKlass, source, ctx);
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

        String source = "(" + lhs.toString() + op + rhs.toString() + ")";
        return new SegmentCode(resultKlass, source, ctx);
    }

    @Override
    public Code visitExpr_compare_not(Expr_compare_notContext ctx) {
        SegmentCode code = (SegmentCode) ctx.expression().accept(this);
        String source = "(!" + get_if_expression_source(code) + ")";
        return new SegmentCode(Boolean.TYPE, source, ctx);
    }

    @Override
    public Code visitExpr_compare_equality(Expr_compare_equalityContext ctx) {
        SegmentCode lhs = (SegmentCode) ctx.expression(0).accept(this);
        SegmentCode rhs = (SegmentCode) ctx.expression(1).accept(this);
        TerminalNode op = (TerminalNode) ctx.getChild(1);

        assert_not_void_expression(lhs);
        assert_not_void_expression(rhs);

        StringBuilder source = new StringBuilder(32);
        source.append("==".equals(op.getText()) ? "JetUtils.asEquals(" : "JetUtils.asNotEquals(");
        source.append(lhs.toString());
        source.append(',');
        source.append(rhs.toString());
        source.append(')');
        return new SegmentCode(Boolean.TYPE, source.toString(), ctx);
    }

    @Override
    public Code visitExpr_compare_relational(Expr_compare_relationalContext ctx) {
        SegmentCode lhs = (SegmentCode) ctx.expression(0).accept(this);
        SegmentCode rhs = (SegmentCode) ctx.expression(1).accept(this);
        TerminalNode op = (TerminalNode) ctx.getChild(1);

        assert_not_void_expression(lhs);
        assert_not_void_expression(rhs);
        assert_not_null_constantContext(lhs.getNode());
        assert_not_null_constantContext(rhs.getNode());

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
        source.append(lhs.toString());
        source.append(',');
        source.append(rhs.toString());
        source.append(')');
        source.append(suffix);
        source.append(')');
        return new SegmentCode(Boolean.TYPE, source.toString(), ctx);
    }

    @Override
    public Code visitExpr_compare_condition(Expr_compare_conditionContext ctx) {
        SegmentCode lhs = (SegmentCode) ctx.expression(0).accept(this);
        SegmentCode rhs = (SegmentCode) ctx.expression(1).accept(this);
        String op = ctx.getChild(1).getText();

        assert_not_void_expression(lhs);
        assert_not_void_expression(rhs);

        String source = "(" + get_if_expression_source(lhs) + op + get_if_expression_source(rhs) + ")";
        return new SegmentCode(Boolean.TYPE, source, ctx);
    }

    @Override
    public Code visitExpr_conditional_ternary(Expr_conditional_ternaryContext ctx) {
        SegmentCode condition = (SegmentCode) ctx.expression(0).accept(this);
        SegmentCode lhs = (SegmentCode) ctx.expression(1).accept(this);
        SegmentCode rhs = (SegmentCode) ctx.expression(2).accept(this);
        String source = "(" + get_if_expression_source(condition) + "?" + lhs.toString() + ":" + rhs.toString() + ")";

        TypedKlass klass = PromotionUtils.getResultClassForConditionalOperator(lhs.getTypedKlass(), rhs.getTypedKlass());
        return new SegmentCode(klass, source, ctx);
    }

    @Override
    public Code visitConstant(ConstantContext ctx) {
        Token token = ((TerminalNode) ctx.getChild(0)).getSymbol();
        String text = token.getText();
        switch (token.getType()) {
        case JetTemplateParser.STRING_DOUBLE:
            return new SegmentCode(String.class, text, ctx);
        case JetTemplateParser.STRING_SINGLE:
            text = StringEscapeUtils.asCanonicalJavaString(text);
            return new SegmentCode(String.class, text, ctx);
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
            return new SegmentCode(klass, text, ctx);
        case JetTemplateParser.KEYWORD_TRUE:
            return new SegmentCode(Boolean.TYPE, text, ctx);
        case JetTemplateParser.KEYWORD_FALSE:
            return new SegmentCode(Boolean.TYPE, text, ctx);
        case JetTemplateParser.KEYWORD_NULL:
            return new SegmentCode(TypedKlass.NULL, text, ctx);
        default:
            throw reportError("Unexpected token type :" + token.getType(), ctx);
        }
    }

    @Override
    public Code visitExpression_list(Expression_listContext ctx) {
        List<ExpressionContext> expression_list = ctx.expression();
        SegmentListCode code = new SegmentListCode(expression_list.size());

        for (ExpressionContext expression : expression_list) {
            SegmentCode c = (SegmentCode) expression.accept(this);
            assert_not_void_expression(c);
            code.addChild(c);
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

        if (securityManager != null) {
            securityManager.checkMemberAccess(klass);
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
            array_suffix = array_suffix + code.toString();
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
        return new SegmentCode(typedKlass, typedKlass.toString(), ctx);
    }

    @Override
    public Code visitType_array_suffix(Type_array_suffixContext ctx) {
        return new SegmentCode((TypedKlass) null, "[]", ctx);
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
        return new SegmentCode(TypedKlass.WildcharTypedKlass, "?", ctx);
    }

    // -----------------------------------------------------------
    // 变量必须是合法的 java 变量
    private String assert_java_identifier(ParseTree node, boolean isDefining) {
        String name = node.getText();

        if ("for".equals(name)) {
            if (isDefining) {
                throw reportError("Syntax error on token \"" + name + "\" is not a valid identifier.", node);
            }
            return name;
        }
        if (Code.CONTEXT_NAME.equals(name)) {
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

    // 检测 void 类型
    private void assert_not_void_expression(SegmentCode code) {
        if (Void.TYPE.equals(code.getKlass()) || Void.class.equals(code.getKlass())) {
            throw reportError("Unexpected void type in here.", code.getNode());
        }
    }

    // 检测非 null 常量
    private void assert_not_null_constantContext(ParserRuleContext node) {
        if (node.getStart().getType() == JetTemplateParser.KEYWORD_NULL) {
            throw reportError("Unexpected token: invalid keyword null in here.", node);
        }
    }

    // 确保返回的代码类型必须是 boolean 类型的
    private String get_if_expression_source(SegmentCode code) {
        if (Boolean.TYPE.equals(code.getKlass())) {
            return code.toString();
        } else {
            assert_not_void_expression(code);
            return "JetUtils.asBoolean(" + code.toString() + ")";
        }
    }

    // -----------------------------------------------------------
    // 创建一个全局唯一的变量名称
    private String getUid(String prefix) {
        return "$" + prefix + "_" + (uuid++);
    }

    private RuntimeException reportError(String message, Object node) {
        if (node instanceof ParserRuleContext) {
            parser.notifyErrorListeners(((ParserRuleContext) node).getStart(), message, null);
        } else if (node instanceof TerminalNode) {
            parser.notifyErrorListeners(((TerminalNode) node).getSymbol(), message, null);
        } else if (node instanceof Token) {
            parser.notifyErrorListeners((Token) node, message, null);
        }
        return new SyntaxErrorException(message);
    }

    // 等到一个方法描述字符串
    private String getMethodSignature(String name, Class<?>[] parameterTypes) {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append('(');
        for (int i = 0; i < parameterTypes.length; i++) {
            if (i > 0) {
                sb.append(',');
            }
            Class<?> type = parameterTypes[i];
            sb.append(type == null ? "<null>" : type.getSimpleName());
        }
        sb.append(')');
        return sb.toString();
    }
}
