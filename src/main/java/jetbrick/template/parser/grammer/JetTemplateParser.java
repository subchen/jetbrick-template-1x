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
package jetbrick.template.parser.grammer;

import java.util.List;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

@SuppressWarnings({ "all", "warnings", "unchecked", "unused", "cast" })
public class JetTemplateParser extends Parser {
    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache = new PredictionContextCache();
    public static final int OP_BITWISE_AND = 57, OP_RELATIONAL_GT = 43, DIRECTIVE_OPEN_CONTINUE = 16, OP_INSTANCEOF = 62, OP_BITWISE_OR = 58, VALUE_ESCAPED_OPEN = 8, DIRECTIVE_OPEN_PUT = 11,
            OP_EQUALITY_EQ = 41, LEFT_BRACKET = 34, VALUE_OPEN = 7, DIRECTIVE_INCLUDE = 25, OP_EQUALITY_NE = 42, OP_BITWISE_XOR = 60, TEXT_ESCAPED_CHAR = 5, DIRECTIVE_OPEN_INCLUDE = 18,
            DIRECTIVE_SET = 20, DIRECTIVE_OPEN_BREAK = 15, TEXT_PLAIN = 3, TEXT_CDATA = 4, OP_MATH_DECREMENT = 56, DIRECTIVE_OPEN_STOP = 17, OP_RELATIONAL_LE = 46, RIGHT_PARENTHESE = 33,
            OP_ASSIGNMENT = 38, COMMA = 65, IDENTIFIER = 70, RIGHT_BRACKET = 35, OP_MATH_MULTIPLICATION = 52, DIRECTIVE_END = 30, OP_CONDITIONAL_AND = 47, DIRECTIVE_PUT = 21, KEYWORD_NULL = 69,
            DIRECTIVE_ELSEIF = 23, DIRECTIVE_DEFINE = 19, TEXT_SINGLE_CHAR = 6, DIRECTIVE_OPEN_FOR = 14, OP_RELATIONAL_GE = 45, INTEGER = 71, DIRECTIVE_STOP = 28, RIGHT_BRACE = 37,
            LEFT_PARENTHESE = 32, OP_BITWISE_SHL = 61, DIRECTIVE_IF = 22, OP_CONDITIONAL_TERNARY = 64, COMMENT_BLOCK = 2, DIRECTIVE_ELSE = 29, OP_MATH_INCREMENT = 55, INTEGER_HEX = 72,
            KEYWORD_FALSE = 68, DIRECTIVE_BREAK = 26, OP_MATH_PLUS = 50, WHITESPACE = 31, OP_NEW = 63, OP_MATH_REMAINDER = 54, OP_DOT_INVOCATION = 39, DIRECTIVE_FOR = 24, COMMENT_LINE = 1,
            OP_MATH_DIVISION = 53, FLOATING_POINT = 73, DIRECTIVE_OPEN_SET = 10, OP_DOT_INVOCATION_SAFE = 40, COLON = 66, OP_CONDITIONAL_NOT = 49, KEYWORD_TRUE = 67, OP_MATH_MINUS = 51,
            OP_RELATIONAL_LT = 44, LEFT_BRACE = 36, DIRECTIVE_OPEN_IF = 12, DIRECTIVE_OPEN_ELSEIF = 13, DIRECTIVE_CONTINUE = 27, STRING_DOUBLE = 74, STRING_SINGLE = 75, DIRECTIVE_OPEN_DEFINE = 9,
            OP_BITWISE_NOT = 59, OP_CONDITIONAL_OR = 48;
    public static final String[] tokenNames = { "<INVALID>", "COMMENT_LINE", "COMMENT_BLOCK", "TEXT_PLAIN", "TEXT_CDATA", "TEXT_ESCAPED_CHAR", "TEXT_SINGLE_CHAR", "'${'", "'$!{'",
            "DIRECTIVE_OPEN_DEFINE", "DIRECTIVE_OPEN_SET", "DIRECTIVE_OPEN_PUT", "DIRECTIVE_OPEN_IF", "DIRECTIVE_OPEN_ELSEIF", "DIRECTIVE_OPEN_FOR", "DIRECTIVE_OPEN_BREAK", "DIRECTIVE_OPEN_CONTINUE",
            "DIRECTIVE_OPEN_STOP", "DIRECTIVE_OPEN_INCLUDE", "'#define'", "'#set'", "'#put'", "'#if'", "'#elseif'", "'#for'", "'#include'", "'#break'", "'#continue'", "'#stop'", "DIRECTIVE_ELSE",
            "DIRECTIVE_END", "WHITESPACE", "'('", "')'", "'['", "']'", "'{'", "'}'", "'='", "'.'", "'?.'", "'=='", "'!='", "'>'", "'<'", "'>='", "'<='", "'&&'", "'||'", "'!'", "'+'", "'-'", "'*'",
            "'/'", "'%'", "'++'", "'--'", "'&'", "'|'", "'~'", "'^'", "'<<'", "OP_INSTANCEOF", "'new'", "'?'", "','", "':'", "'true'", "'false'", "'null'", "IDENTIFIER", "INTEGER", "INTEGER_HEX",
            "FLOATING_POINT", "STRING_DOUBLE", "STRING_SINGLE" };
    public static final int RULE_template = 0, RULE_block = 1, RULE_text = 2, RULE_value = 3, RULE_directive = 4, RULE_define_directive = 5, RULE_define_expression = 6, RULE_set_directive = 7,
            RULE_set_expression = 8, RULE_put_directive = 9, RULE_if_directive = 10, RULE_elseif_directive = 11, RULE_else_directive = 12, RULE_for_directive = 13, RULE_for_expression = 14,
            RULE_break_directive = 15, RULE_continue_directive = 16, RULE_stop_directive = 17, RULE_include_directive = 18, RULE_invalid_directive = 19, RULE_expression = 20, RULE_constant = 21,
            RULE_expression_list = 22, RULE_hash_map_entry_list = 23, RULE_type = 24, RULE_type_array_suffix = 25, RULE_type_arguments = 26, RULE_type_list = 27, RULE_type_name = 28;
    public static final String[] ruleNames = { "template", "block", "text", "value", "directive", "define_directive", "define_expression", "set_directive", "set_expression", "put_directive",
            "if_directive", "elseif_directive", "else_directive", "for_directive", "for_expression", "break_directive", "continue_directive", "stop_directive", "include_directive",
            "invalid_directive", "expression", "constant", "expression_list", "hash_map_entry_list", "type", "type_array_suffix", "type_arguments", "type_list", "type_name" };

    @Override
    public String getGrammarFileName() {
        return "JetTemplateParser.g4";
    }

    @Override
    public String[] getTokenNames() {
        return tokenNames;
    }

    @Override
    public String[] getRuleNames() {
        return ruleNames;
    }

    @Override
    public ATN getATN() {
        return _ATN;
    }

    public JetTemplateParser(TokenStream input) {
        super(input);
        _interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
    }

    public static class TemplateContext extends ParserRuleContext {
        public BlockContext block() {
            return getRuleContext(BlockContext.class, 0);
        }

        public TemplateContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_template;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitTemplate(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public final TemplateContext template() throws RecognitionException {
        TemplateContext _localctx = new TemplateContext(_ctx, getState());
        enterRule(_localctx, 0, RULE_template);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(58);
                block();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class BlockContext extends ParserRuleContext {
        public List<TextContext> text() {
            return getRuleContexts(TextContext.class);
        }

        public ValueContext value(int i) {
            return getRuleContext(ValueContext.class, i);
        }

        public DirectiveContext directive(int i) {
            return getRuleContext(DirectiveContext.class, i);
        }

        public List<ValueContext> value() {
            return getRuleContexts(ValueContext.class);
        }

        public TextContext text(int i) {
            return getRuleContext(TextContext.class, i);
        }

        public List<DirectiveContext> directive() {
            return getRuleContexts(DirectiveContext.class);
        }

        public BlockContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_block;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitBlock(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public final BlockContext block() throws RecognitionException {
        BlockContext _localctx = new BlockContext(_ctx, getState());
        enterRule(_localctx, 2, RULE_block);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(65);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TEXT_PLAIN) | (1L << TEXT_CDATA) | (1L << TEXT_ESCAPED_CHAR) | (1L << TEXT_SINGLE_CHAR) | (1L << VALUE_OPEN)
                        | (1L << VALUE_ESCAPED_OPEN) | (1L << DIRECTIVE_OPEN_DEFINE) | (1L << DIRECTIVE_OPEN_SET) | (1L << DIRECTIVE_OPEN_PUT) | (1L << DIRECTIVE_OPEN_IF) | (1L << DIRECTIVE_OPEN_FOR)
                        | (1L << DIRECTIVE_OPEN_BREAK) | (1L << DIRECTIVE_OPEN_CONTINUE) | (1L << DIRECTIVE_OPEN_STOP) | (1L << DIRECTIVE_OPEN_INCLUDE) | (1L << DIRECTIVE_DEFINE)
                        | (1L << DIRECTIVE_SET) | (1L << DIRECTIVE_PUT) | (1L << DIRECTIVE_IF) | (1L << DIRECTIVE_ELSEIF) | (1L << DIRECTIVE_FOR) | (1L << DIRECTIVE_INCLUDE) | (1L << DIRECTIVE_BREAK)
                        | (1L << DIRECTIVE_CONTINUE) | (1L << DIRECTIVE_STOP))) != 0)) {
                    {
                        setState(63);
                        switch (_input.LA(1)) {
                        case TEXT_PLAIN:
                        case TEXT_CDATA:
                        case TEXT_ESCAPED_CHAR:
                        case TEXT_SINGLE_CHAR: {
                            setState(60);
                            text();
                        }
                            break;
                        case VALUE_OPEN:
                        case VALUE_ESCAPED_OPEN: {
                            setState(61);
                            value();
                        }
                            break;
                        case DIRECTIVE_OPEN_DEFINE:
                        case DIRECTIVE_OPEN_SET:
                        case DIRECTIVE_OPEN_PUT:
                        case DIRECTIVE_OPEN_IF:
                        case DIRECTIVE_OPEN_FOR:
                        case DIRECTIVE_OPEN_BREAK:
                        case DIRECTIVE_OPEN_CONTINUE:
                        case DIRECTIVE_OPEN_STOP:
                        case DIRECTIVE_OPEN_INCLUDE:
                        case DIRECTIVE_DEFINE:
                        case DIRECTIVE_SET:
                        case DIRECTIVE_PUT:
                        case DIRECTIVE_IF:
                        case DIRECTIVE_ELSEIF:
                        case DIRECTIVE_FOR:
                        case DIRECTIVE_INCLUDE:
                        case DIRECTIVE_BREAK:
                        case DIRECTIVE_CONTINUE:
                        case DIRECTIVE_STOP: {
                            setState(62);
                            directive();
                        }
                            break;
                        default:
                            throw new NoViableAltException(this);
                        }
                    }
                    setState(67);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class TextContext extends ParserRuleContext {
        public TerminalNode TEXT_CDATA() {
            return getToken(JetTemplateParser.TEXT_CDATA, 0);
        }

        public TerminalNode TEXT_SINGLE_CHAR() {
            return getToken(JetTemplateParser.TEXT_SINGLE_CHAR, 0);
        }

        public TerminalNode TEXT_ESCAPED_CHAR() {
            return getToken(JetTemplateParser.TEXT_ESCAPED_CHAR, 0);
        }

        public TerminalNode TEXT_PLAIN() {
            return getToken(JetTemplateParser.TEXT_PLAIN, 0);
        }

        public TextContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_text;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitText(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public final TextContext text() throws RecognitionException {
        TextContext _localctx = new TextContext(_ctx, getState());
        enterRule(_localctx, 4, RULE_text);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(68);
                _la = _input.LA(1);
                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TEXT_PLAIN) | (1L << TEXT_CDATA) | (1L << TEXT_ESCAPED_CHAR) | (1L << TEXT_SINGLE_CHAR))) != 0))) {
                    _errHandler.recoverInline(this);
                }
                consume();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ValueContext extends ParserRuleContext {
        public TerminalNode VALUE_OPEN() {
            return getToken(JetTemplateParser.VALUE_OPEN, 0);
        }

        public TerminalNode VALUE_ESCAPED_OPEN() {
            return getToken(JetTemplateParser.VALUE_ESCAPED_OPEN, 0);
        }

        public ExpressionContext expression() {
            return getRuleContext(ExpressionContext.class, 0);
        }

        public ValueContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_value;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitValue(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public final ValueContext value() throws RecognitionException {
        ValueContext _localctx = new ValueContext(_ctx, getState());
        enterRule(_localctx, 6, RULE_value);
        try {
            setState(78);
            switch (_input.LA(1)) {
            case VALUE_OPEN:
                enterOuterAlt(_localctx, 1);
                {
                    setState(70);
                    match(VALUE_OPEN);
                    setState(71);
                    expression(0);
                    setState(72);
                    match(RIGHT_BRACE);
                }
                break;
            case VALUE_ESCAPED_OPEN:
                enterOuterAlt(_localctx, 2);
                {
                    setState(74);
                    match(VALUE_ESCAPED_OPEN);
                    setState(75);
                    expression(0);
                    setState(76);
                    match(RIGHT_BRACE);
                }
                break;
            default:
                throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class DirectiveContext extends ParserRuleContext {
        public Include_directiveContext include_directive() {
            return getRuleContext(Include_directiveContext.class, 0);
        }

        public Stop_directiveContext stop_directive() {
            return getRuleContext(Stop_directiveContext.class, 0);
        }

        public Continue_directiveContext continue_directive() {
            return getRuleContext(Continue_directiveContext.class, 0);
        }

        public Invalid_directiveContext invalid_directive() {
            return getRuleContext(Invalid_directiveContext.class, 0);
        }

        public Define_directiveContext define_directive() {
            return getRuleContext(Define_directiveContext.class, 0);
        }

        public If_directiveContext if_directive() {
            return getRuleContext(If_directiveContext.class, 0);
        }

        public Break_directiveContext break_directive() {
            return getRuleContext(Break_directiveContext.class, 0);
        }

        public Set_directiveContext set_directive() {
            return getRuleContext(Set_directiveContext.class, 0);
        }

        public For_directiveContext for_directive() {
            return getRuleContext(For_directiveContext.class, 0);
        }

        public Put_directiveContext put_directive() {
            return getRuleContext(Put_directiveContext.class, 0);
        }

        public DirectiveContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_directive;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitDirective(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public final DirectiveContext directive() throws RecognitionException {
        DirectiveContext _localctx = new DirectiveContext(_ctx, getState());
        enterRule(_localctx, 8, RULE_directive);
        try {
            setState(90);
            switch (_input.LA(1)) {
            case DIRECTIVE_OPEN_DEFINE:
                enterOuterAlt(_localctx, 1);
                {
                    setState(80);
                    define_directive();
                }
                break;
            case DIRECTIVE_OPEN_SET:
                enterOuterAlt(_localctx, 2);
                {
                    setState(81);
                    set_directive();
                }
                break;
            case DIRECTIVE_OPEN_PUT:
                enterOuterAlt(_localctx, 3);
                {
                    setState(82);
                    put_directive();
                }
                break;
            case DIRECTIVE_OPEN_IF:
                enterOuterAlt(_localctx, 4);
                {
                    setState(83);
                    if_directive();
                }
                break;
            case DIRECTIVE_OPEN_FOR:
                enterOuterAlt(_localctx, 5);
                {
                    setState(84);
                    for_directive();
                }
                break;
            case DIRECTIVE_OPEN_BREAK:
            case DIRECTIVE_BREAK:
                enterOuterAlt(_localctx, 6);
                {
                    setState(85);
                    break_directive();
                }
                break;
            case DIRECTIVE_OPEN_CONTINUE:
            case DIRECTIVE_CONTINUE:
                enterOuterAlt(_localctx, 7);
                {
                    setState(86);
                    continue_directive();
                }
                break;
            case DIRECTIVE_OPEN_STOP:
            case DIRECTIVE_STOP:
                enterOuterAlt(_localctx, 8);
                {
                    setState(87);
                    stop_directive();
                }
                break;
            case DIRECTIVE_OPEN_INCLUDE:
                enterOuterAlt(_localctx, 9);
                {
                    setState(88);
                    include_directive();
                }
                break;
            case DIRECTIVE_DEFINE:
            case DIRECTIVE_SET:
            case DIRECTIVE_PUT:
            case DIRECTIVE_IF:
            case DIRECTIVE_ELSEIF:
            case DIRECTIVE_FOR:
            case DIRECTIVE_INCLUDE:
                enterOuterAlt(_localctx, 10);
                {
                    setState(89);
                    invalid_directive();
                }
                break;
            default:
                throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Define_directiveContext extends ParserRuleContext {
        public Define_expressionContext define_expression(int i) {
            return getRuleContext(Define_expressionContext.class, i);
        }

        public List<Define_expressionContext> define_expression() {
            return getRuleContexts(Define_expressionContext.class);
        }

        public TerminalNode DIRECTIVE_OPEN_DEFINE() {
            return getToken(JetTemplateParser.DIRECTIVE_OPEN_DEFINE, 0);
        }

        public Define_directiveContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_define_directive;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitDefine_directive(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public final Define_directiveContext define_directive() throws RecognitionException {
        Define_directiveContext _localctx = new Define_directiveContext(_ctx, getState());
        enterRule(_localctx, 10, RULE_define_directive);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(92);
                match(DIRECTIVE_OPEN_DEFINE);
                setState(93);
                define_expression();
                setState(98);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == COMMA) {
                    {
                        {
                            setState(94);
                            match(COMMA);
                            setState(95);
                            define_expression();
                        }
                    }
                    setState(100);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(101);
                match(RIGHT_PARENTHESE);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Define_expressionContext extends ParserRuleContext {
        public TerminalNode IDENTIFIER() {
            return getToken(JetTemplateParser.IDENTIFIER, 0);
        }

        public TypeContext type() {
            return getRuleContext(TypeContext.class, 0);
        }

        public Define_expressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_define_expression;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitDefine_expression(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public final Define_expressionContext define_expression() throws RecognitionException {
        Define_expressionContext _localctx = new Define_expressionContext(_ctx, getState());
        enterRule(_localctx, 12, RULE_define_expression);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(103);
                type();
                setState(104);
                match(IDENTIFIER);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Set_directiveContext extends ParserRuleContext {
        public Set_expressionContext set_expression(int i) {
            return getRuleContext(Set_expressionContext.class, i);
        }

        public TerminalNode DIRECTIVE_OPEN_SET() {
            return getToken(JetTemplateParser.DIRECTIVE_OPEN_SET, 0);
        }

        public List<Set_expressionContext> set_expression() {
            return getRuleContexts(Set_expressionContext.class);
        }

        public Set_directiveContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_set_directive;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitSet_directive(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public final Set_directiveContext set_directive() throws RecognitionException {
        Set_directiveContext _localctx = new Set_directiveContext(_ctx, getState());
        enterRule(_localctx, 14, RULE_set_directive);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(106);
                match(DIRECTIVE_OPEN_SET);
                setState(107);
                set_expression();
                setState(112);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == COMMA) {
                    {
                        {
                            setState(108);
                            match(COMMA);
                            setState(109);
                            set_expression();
                        }
                    }
                    setState(114);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(115);
                match(RIGHT_PARENTHESE);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Set_expressionContext extends ParserRuleContext {
        public TerminalNode IDENTIFIER() {
            return getToken(JetTemplateParser.IDENTIFIER, 0);
        }

        public TypeContext type() {
            return getRuleContext(TypeContext.class, 0);
        }

        public ExpressionContext expression() {
            return getRuleContext(ExpressionContext.class, 0);
        }

        public Set_expressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_set_expression;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitSet_expression(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public final Set_expressionContext set_expression() throws RecognitionException {
        Set_expressionContext _localctx = new Set_expressionContext(_ctx, getState());
        enterRule(_localctx, 16, RULE_set_expression);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(118);
                switch (getInterpreter().adaptivePredict(_input, 6, _ctx)) {
                case 1: {
                    setState(117);
                    type();
                }
                    break;
                }
                setState(120);
                match(IDENTIFIER);
                setState(121);
                match(OP_ASSIGNMENT);
                setState(122);
                expression(0);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Put_directiveContext extends ParserRuleContext {
        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class, i);
        }

        public TerminalNode DIRECTIVE_OPEN_PUT() {
            return getToken(JetTemplateParser.DIRECTIVE_OPEN_PUT, 0);
        }

        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }

        public Put_directiveContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_put_directive;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitPut_directive(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public final Put_directiveContext put_directive() throws RecognitionException {
        Put_directiveContext _localctx = new Put_directiveContext(_ctx, getState());
        enterRule(_localctx, 18, RULE_put_directive);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(124);
                match(DIRECTIVE_OPEN_PUT);
                setState(125);
                expression(0);
                setState(130);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == COMMA) {
                    {
                        {
                            setState(126);
                            match(COMMA);
                            setState(127);
                            expression(0);
                        }
                    }
                    setState(132);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(133);
                match(RIGHT_PARENTHESE);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class If_directiveContext extends ParserRuleContext {
        public TerminalNode DIRECTIVE_END() {
            return getToken(JetTemplateParser.DIRECTIVE_END, 0);
        }

        public Else_directiveContext else_directive() {
            return getRuleContext(Else_directiveContext.class, 0);
        }

        public List<Elseif_directiveContext> elseif_directive() {
            return getRuleContexts(Elseif_directiveContext.class);
        }

        public TerminalNode DIRECTIVE_OPEN_IF() {
            return getToken(JetTemplateParser.DIRECTIVE_OPEN_IF, 0);
        }

        public ExpressionContext expression() {
            return getRuleContext(ExpressionContext.class, 0);
        }

        public BlockContext block() {
            return getRuleContext(BlockContext.class, 0);
        }

        public Elseif_directiveContext elseif_directive(int i) {
            return getRuleContext(Elseif_directiveContext.class, i);
        }

        public If_directiveContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_if_directive;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitIf_directive(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public final If_directiveContext if_directive() throws RecognitionException {
        If_directiveContext _localctx = new If_directiveContext(_ctx, getState());
        enterRule(_localctx, 20, RULE_if_directive);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(135);
                match(DIRECTIVE_OPEN_IF);
                setState(136);
                expression(0);
                setState(137);
                match(RIGHT_PARENTHESE);
                setState(138);
                block();
                setState(142);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == DIRECTIVE_OPEN_ELSEIF) {
                    {
                        {
                            setState(139);
                            elseif_directive();
                        }
                    }
                    setState(144);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(146);
                _la = _input.LA(1);
                if (_la == DIRECTIVE_ELSE) {
                    {
                        setState(145);
                        else_directive();
                    }
                }

                setState(148);
                match(DIRECTIVE_END);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Elseif_directiveContext extends ParserRuleContext {
        public ExpressionContext expression() {
            return getRuleContext(ExpressionContext.class, 0);
        }

        public BlockContext block() {
            return getRuleContext(BlockContext.class, 0);
        }

        public TerminalNode DIRECTIVE_OPEN_ELSEIF() {
            return getToken(JetTemplateParser.DIRECTIVE_OPEN_ELSEIF, 0);
        }

        public Elseif_directiveContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_elseif_directive;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitElseif_directive(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public final Elseif_directiveContext elseif_directive() throws RecognitionException {
        Elseif_directiveContext _localctx = new Elseif_directiveContext(_ctx, getState());
        enterRule(_localctx, 22, RULE_elseif_directive);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(150);
                match(DIRECTIVE_OPEN_ELSEIF);
                setState(151);
                expression(0);
                setState(152);
                match(RIGHT_PARENTHESE);
                setState(153);
                block();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Else_directiveContext extends ParserRuleContext {
        public BlockContext block() {
            return getRuleContext(BlockContext.class, 0);
        }

        public TerminalNode DIRECTIVE_ELSE() {
            return getToken(JetTemplateParser.DIRECTIVE_ELSE, 0);
        }

        public Else_directiveContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_else_directive;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitElse_directive(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public final Else_directiveContext else_directive() throws RecognitionException {
        Else_directiveContext _localctx = new Else_directiveContext(_ctx, getState());
        enterRule(_localctx, 24, RULE_else_directive);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(155);
                match(DIRECTIVE_ELSE);
                setState(156);
                block();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class For_directiveContext extends ParserRuleContext {
        public TerminalNode DIRECTIVE_OPEN_FOR() {
            return getToken(JetTemplateParser.DIRECTIVE_OPEN_FOR, 0);
        }

        public TerminalNode DIRECTIVE_END() {
            return getToken(JetTemplateParser.DIRECTIVE_END, 0);
        }

        public Else_directiveContext else_directive() {
            return getRuleContext(Else_directiveContext.class, 0);
        }

        public BlockContext block() {
            return getRuleContext(BlockContext.class, 0);
        }

        public For_expressionContext for_expression() {
            return getRuleContext(For_expressionContext.class, 0);
        }

        public For_directiveContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_for_directive;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitFor_directive(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public final For_directiveContext for_directive() throws RecognitionException {
        For_directiveContext _localctx = new For_directiveContext(_ctx, getState());
        enterRule(_localctx, 26, RULE_for_directive);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(158);
                match(DIRECTIVE_OPEN_FOR);
                setState(159);
                for_expression();
                setState(160);
                match(RIGHT_PARENTHESE);
                setState(161);
                block();
                setState(163);
                _la = _input.LA(1);
                if (_la == DIRECTIVE_ELSE) {
                    {
                        setState(162);
                        else_directive();
                    }
                }

                setState(165);
                match(DIRECTIVE_END);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class For_expressionContext extends ParserRuleContext {
        public TerminalNode IDENTIFIER() {
            return getToken(JetTemplateParser.IDENTIFIER, 0);
        }

        public TypeContext type() {
            return getRuleContext(TypeContext.class, 0);
        }

        public ExpressionContext expression() {
            return getRuleContext(ExpressionContext.class, 0);
        }

        public For_expressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_for_expression;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitFor_expression(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public final For_expressionContext for_expression() throws RecognitionException {
        For_expressionContext _localctx = new For_expressionContext(_ctx, getState());
        enterRule(_localctx, 28, RULE_for_expression);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(168);
                switch (getInterpreter().adaptivePredict(_input, 11, _ctx)) {
                case 1: {
                    setState(167);
                    type();
                }
                    break;
                }
                setState(170);
                match(IDENTIFIER);
                setState(171);
                match(COLON);
                setState(172);
                expression(0);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Break_directiveContext extends ParserRuleContext {
        public TerminalNode DIRECTIVE_BREAK() {
            return getToken(JetTemplateParser.DIRECTIVE_BREAK, 0);
        }

        public ExpressionContext expression() {
            return getRuleContext(ExpressionContext.class, 0);
        }

        public TerminalNode DIRECTIVE_OPEN_BREAK() {
            return getToken(JetTemplateParser.DIRECTIVE_OPEN_BREAK, 0);
        }

        public Break_directiveContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_break_directive;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitBreak_directive(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public final Break_directiveContext break_directive() throws RecognitionException {
        Break_directiveContext _localctx = new Break_directiveContext(_ctx, getState());
        enterRule(_localctx, 30, RULE_break_directive);
        int _la;
        try {
            setState(180);
            switch (_input.LA(1)) {
            case DIRECTIVE_OPEN_BREAK:
                enterOuterAlt(_localctx, 1);
                {
                    setState(174);
                    match(DIRECTIVE_OPEN_BREAK);
                    setState(176);
                    _la = _input.LA(1);
                    if (((((_la - 32)) & ~0x3f) == 0 && ((1L << (_la - 32)) & ((1L << (LEFT_PARENTHESE - 32)) | (1L << (LEFT_BRACKET - 32)) | (1L << (LEFT_BRACE - 32))
                            | (1L << (OP_CONDITIONAL_NOT - 32)) | (1L << (OP_MATH_PLUS - 32)) | (1L << (OP_MATH_MINUS - 32)) | (1L << (OP_MATH_INCREMENT - 32)) | (1L << (OP_MATH_DECREMENT - 32))
                            | (1L << (OP_BITWISE_NOT - 32)) | (1L << (OP_NEW - 32)) | (1L << (KEYWORD_TRUE - 32)) | (1L << (KEYWORD_FALSE - 32)) | (1L << (KEYWORD_NULL - 32))
                            | (1L << (IDENTIFIER - 32)) | (1L << (INTEGER - 32)) | (1L << (INTEGER_HEX - 32)) | (1L << (FLOATING_POINT - 32)) | (1L << (STRING_DOUBLE - 32)) | (1L << (STRING_SINGLE - 32)))) != 0)) {
                        {
                            setState(175);
                            expression(0);
                        }
                    }

                    setState(178);
                    match(RIGHT_PARENTHESE);
                }
                break;
            case DIRECTIVE_BREAK:
                enterOuterAlt(_localctx, 2);
                {
                    setState(179);
                    match(DIRECTIVE_BREAK);
                }
                break;
            default:
                throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Continue_directiveContext extends ParserRuleContext {
        public TerminalNode DIRECTIVE_CONTINUE() {
            return getToken(JetTemplateParser.DIRECTIVE_CONTINUE, 0);
        }

        public ExpressionContext expression() {
            return getRuleContext(ExpressionContext.class, 0);
        }

        public TerminalNode DIRECTIVE_OPEN_CONTINUE() {
            return getToken(JetTemplateParser.DIRECTIVE_OPEN_CONTINUE, 0);
        }

        public Continue_directiveContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_continue_directive;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitContinue_directive(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public final Continue_directiveContext continue_directive() throws RecognitionException {
        Continue_directiveContext _localctx = new Continue_directiveContext(_ctx, getState());
        enterRule(_localctx, 32, RULE_continue_directive);
        int _la;
        try {
            setState(188);
            switch (_input.LA(1)) {
            case DIRECTIVE_OPEN_CONTINUE:
                enterOuterAlt(_localctx, 1);
                {
                    setState(182);
                    match(DIRECTIVE_OPEN_CONTINUE);
                    setState(184);
                    _la = _input.LA(1);
                    if (((((_la - 32)) & ~0x3f) == 0 && ((1L << (_la - 32)) & ((1L << (LEFT_PARENTHESE - 32)) | (1L << (LEFT_BRACKET - 32)) | (1L << (LEFT_BRACE - 32))
                            | (1L << (OP_CONDITIONAL_NOT - 32)) | (1L << (OP_MATH_PLUS - 32)) | (1L << (OP_MATH_MINUS - 32)) | (1L << (OP_MATH_INCREMENT - 32)) | (1L << (OP_MATH_DECREMENT - 32))
                            | (1L << (OP_BITWISE_NOT - 32)) | (1L << (OP_NEW - 32)) | (1L << (KEYWORD_TRUE - 32)) | (1L << (KEYWORD_FALSE - 32)) | (1L << (KEYWORD_NULL - 32))
                            | (1L << (IDENTIFIER - 32)) | (1L << (INTEGER - 32)) | (1L << (INTEGER_HEX - 32)) | (1L << (FLOATING_POINT - 32)) | (1L << (STRING_DOUBLE - 32)) | (1L << (STRING_SINGLE - 32)))) != 0)) {
                        {
                            setState(183);
                            expression(0);
                        }
                    }

                    setState(186);
                    match(RIGHT_PARENTHESE);
                }
                break;
            case DIRECTIVE_CONTINUE:
                enterOuterAlt(_localctx, 2);
                {
                    setState(187);
                    match(DIRECTIVE_CONTINUE);
                }
                break;
            default:
                throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Stop_directiveContext extends ParserRuleContext {
        public TerminalNode DIRECTIVE_STOP() {
            return getToken(JetTemplateParser.DIRECTIVE_STOP, 0);
        }

        public TerminalNode DIRECTIVE_OPEN_STOP() {
            return getToken(JetTemplateParser.DIRECTIVE_OPEN_STOP, 0);
        }

        public ExpressionContext expression() {
            return getRuleContext(ExpressionContext.class, 0);
        }

        public Stop_directiveContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_stop_directive;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitStop_directive(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public final Stop_directiveContext stop_directive() throws RecognitionException {
        Stop_directiveContext _localctx = new Stop_directiveContext(_ctx, getState());
        enterRule(_localctx, 34, RULE_stop_directive);
        int _la;
        try {
            setState(196);
            switch (_input.LA(1)) {
            case DIRECTIVE_OPEN_STOP:
                enterOuterAlt(_localctx, 1);
                {
                    setState(190);
                    match(DIRECTIVE_OPEN_STOP);
                    setState(192);
                    _la = _input.LA(1);
                    if (((((_la - 32)) & ~0x3f) == 0 && ((1L << (_la - 32)) & ((1L << (LEFT_PARENTHESE - 32)) | (1L << (LEFT_BRACKET - 32)) | (1L << (LEFT_BRACE - 32))
                            | (1L << (OP_CONDITIONAL_NOT - 32)) | (1L << (OP_MATH_PLUS - 32)) | (1L << (OP_MATH_MINUS - 32)) | (1L << (OP_MATH_INCREMENT - 32)) | (1L << (OP_MATH_DECREMENT - 32))
                            | (1L << (OP_BITWISE_NOT - 32)) | (1L << (OP_NEW - 32)) | (1L << (KEYWORD_TRUE - 32)) | (1L << (KEYWORD_FALSE - 32)) | (1L << (KEYWORD_NULL - 32))
                            | (1L << (IDENTIFIER - 32)) | (1L << (INTEGER - 32)) | (1L << (INTEGER_HEX - 32)) | (1L << (FLOATING_POINT - 32)) | (1L << (STRING_DOUBLE - 32)) | (1L << (STRING_SINGLE - 32)))) != 0)) {
                        {
                            setState(191);
                            expression(0);
                        }
                    }

                    setState(194);
                    match(RIGHT_PARENTHESE);
                }
                break;
            case DIRECTIVE_STOP:
                enterOuterAlt(_localctx, 2);
                {
                    setState(195);
                    match(DIRECTIVE_STOP);
                }
                break;
            default:
                throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Include_directiveContext extends ParserRuleContext {
        public TerminalNode DIRECTIVE_OPEN_INCLUDE() {
            return getToken(JetTemplateParser.DIRECTIVE_OPEN_INCLUDE, 0);
        }

        public Expression_listContext expression_list() {
            return getRuleContext(Expression_listContext.class, 0);
        }

        public Include_directiveContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_include_directive;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitInclude_directive(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public final Include_directiveContext include_directive() throws RecognitionException {
        Include_directiveContext _localctx = new Include_directiveContext(_ctx, getState());
        enterRule(_localctx, 36, RULE_include_directive);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(198);
                match(DIRECTIVE_OPEN_INCLUDE);
                setState(199);
                expression_list();
                setState(200);
                match(RIGHT_PARENTHESE);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Invalid_directiveContext extends ParserRuleContext {
        public TerminalNode DIRECTIVE_IF() {
            return getToken(JetTemplateParser.DIRECTIVE_IF, 0);
        }

        public TerminalNode DIRECTIVE_INCLUDE() {
            return getToken(JetTemplateParser.DIRECTIVE_INCLUDE, 0);
        }

        public TerminalNode DIRECTIVE_SET() {
            return getToken(JetTemplateParser.DIRECTIVE_SET, 0);
        }

        public TerminalNode DIRECTIVE_DEFINE() {
            return getToken(JetTemplateParser.DIRECTIVE_DEFINE, 0);
        }

        public TerminalNode DIRECTIVE_ELSEIF() {
            return getToken(JetTemplateParser.DIRECTIVE_ELSEIF, 0);
        }

        public TerminalNode DIRECTIVE_PUT() {
            return getToken(JetTemplateParser.DIRECTIVE_PUT, 0);
        }

        public TerminalNode DIRECTIVE_FOR() {
            return getToken(JetTemplateParser.DIRECTIVE_FOR, 0);
        }

        public Invalid_directiveContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_invalid_directive;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitInvalid_directive(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public final Invalid_directiveContext invalid_directive() throws RecognitionException {
        Invalid_directiveContext _localctx = new Invalid_directiveContext(_ctx, getState());
        enterRule(_localctx, 38, RULE_invalid_directive);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(202);
                _la = _input.LA(1);
                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << DIRECTIVE_DEFINE) | (1L << DIRECTIVE_SET) | (1L << DIRECTIVE_PUT) | (1L << DIRECTIVE_IF) | (1L << DIRECTIVE_ELSEIF)
                        | (1L << DIRECTIVE_FOR) | (1L << DIRECTIVE_INCLUDE))) != 0))) {
                    _errHandler.recoverInline(this);
                }
                consume();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ExpressionContext extends ParserRuleContext {
        public int _p;

        public ExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public ExpressionContext(ParserRuleContext parent, int invokingState, int _p) {
            super(parent, invokingState);
            this._p = _p;
        }

        @Override
        public int getRuleIndex() {
            return RULE_expression;
        }

        public ExpressionContext() {
        }

        public void copyFrom(ExpressionContext ctx) {
            super.copyFrom(ctx);
            this._p = ctx._p;
        }
    }

    public static class Expr_compare_notContext extends ExpressionContext {
        public ExpressionContext expression() {
            return getRuleContext(ExpressionContext.class, 0);
        }

        public Expr_compare_notContext(ExpressionContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitExpr_compare_not(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class Expr_identifierContext extends ExpressionContext {
        public TerminalNode IDENTIFIER() {
            return getToken(JetTemplateParser.IDENTIFIER, 0);
        }

        public Expr_identifierContext(ExpressionContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitExpr_identifier(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class Expr_math_unary_suffixContext extends ExpressionContext {
        public ExpressionContext expression() {
            return getRuleContext(ExpressionContext.class, 0);
        }

        public Expr_math_unary_suffixContext(ExpressionContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitExpr_math_unary_suffix(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class Expr_array_getContext extends ExpressionContext {
        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class, i);
        }

        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }

        public Expr_array_getContext(ExpressionContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitExpr_array_get(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class Expr_conditional_ternaryContext extends ExpressionContext {
        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class, i);
        }

        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }

        public Expr_conditional_ternaryContext(ExpressionContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitExpr_conditional_ternary(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class Expr_array_listContext extends ExpressionContext {
        public Expression_listContext expression_list() {
            return getRuleContext(Expression_listContext.class, 0);
        }

        public Expr_array_listContext(ExpressionContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitExpr_array_list(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class Expr_hash_mapContext extends ExpressionContext {
        public Hash_map_entry_listContext hash_map_entry_list() {
            return getRuleContext(Hash_map_entry_listContext.class, 0);
        }

        public Expr_hash_mapContext(ExpressionContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitExpr_hash_map(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class Expr_field_accessContext extends ExpressionContext {
        public TerminalNode IDENTIFIER() {
            return getToken(JetTemplateParser.IDENTIFIER, 0);
        }

        public ExpressionContext expression() {
            return getRuleContext(ExpressionContext.class, 0);
        }

        public Expr_field_accessContext(ExpressionContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitExpr_field_access(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class Expr_compare_conditionContext extends ExpressionContext {
        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class, i);
        }

        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }

        public Expr_compare_conditionContext(ExpressionContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitExpr_compare_condition(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class Expr_instanceofContext extends ExpressionContext {
        public TypeContext type() {
            return getRuleContext(TypeContext.class, 0);
        }

        public ExpressionContext expression() {
            return getRuleContext(ExpressionContext.class, 0);
        }

        public TerminalNode OP_INSTANCEOF() {
            return getToken(JetTemplateParser.OP_INSTANCEOF, 0);
        }

        public Expr_instanceofContext(ExpressionContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitExpr_instanceof(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class Expr_compare_relationalContext extends ExpressionContext {
        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class, i);
        }

        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }

        public Expr_compare_relationalContext(ExpressionContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitExpr_compare_relational(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class Expr_function_callContext extends ExpressionContext {
        public TerminalNode IDENTIFIER() {
            return getToken(JetTemplateParser.IDENTIFIER, 0);
        }

        public Expression_listContext expression_list() {
            return getRuleContext(Expression_listContext.class, 0);
        }

        public Expr_function_callContext(ExpressionContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitExpr_function_call(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class Expr_method_invocationContext extends ExpressionContext {
        public TerminalNode IDENTIFIER() {
            return getToken(JetTemplateParser.IDENTIFIER, 0);
        }

        public ExpressionContext expression() {
            return getRuleContext(ExpressionContext.class, 0);
        }

        public Expression_listContext expression_list() {
            return getRuleContext(Expression_listContext.class, 0);
        }

        public Expr_method_invocationContext(ExpressionContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitExpr_method_invocation(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class Expr_new_arrayContext extends ExpressionContext {
        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class, i);
        }

        public TypeContext type() {
            return getRuleContext(TypeContext.class, 0);
        }

        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }

        public Expr_new_arrayContext(ExpressionContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitExpr_new_array(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class Expr_math_binary_shiftContext extends ExpressionContext {
        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class, i);
        }

        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }

        public Expr_math_binary_shiftContext(ExpressionContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitExpr_math_binary_shift(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class Expr_compare_equalityContext extends ExpressionContext {
        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class, i);
        }

        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }

        public Expr_compare_equalityContext(ExpressionContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitExpr_compare_equality(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class Expr_class_castContext extends ExpressionContext {
        public TypeContext type() {
            return getRuleContext(TypeContext.class, 0);
        }

        public ExpressionContext expression() {
            return getRuleContext(ExpressionContext.class, 0);
        }

        public Expr_class_castContext(ExpressionContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitExpr_class_cast(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class Expr_math_binary_bitwiseContext extends ExpressionContext {
        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class, i);
        }

        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }

        public Expr_math_binary_bitwiseContext(ExpressionContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitExpr_math_binary_bitwise(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class Expr_math_binary_basicContext extends ExpressionContext {
        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class, i);
        }

        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }

        public Expr_math_binary_basicContext(ExpressionContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitExpr_math_binary_basic(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class Expr_math_unary_prefixContext extends ExpressionContext {
        public ExpressionContext expression() {
            return getRuleContext(ExpressionContext.class, 0);
        }

        public Expr_math_unary_prefixContext(ExpressionContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitExpr_math_unary_prefix(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class Expr_groupContext extends ExpressionContext {
        public ExpressionContext expression() {
            return getRuleContext(ExpressionContext.class, 0);
        }

        public Expr_groupContext(ExpressionContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitExpr_group(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class Expr_constantContext extends ExpressionContext {
        public ConstantContext constant() {
            return getRuleContext(ConstantContext.class, 0);
        }

        public Expr_constantContext(ExpressionContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitExpr_constant(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public static class Expr_new_objectContext extends ExpressionContext {
        public TypeContext type() {
            return getRuleContext(TypeContext.class, 0);
        }

        public Expression_listContext expression_list() {
            return getRuleContext(Expression_listContext.class, 0);
        }

        public Expr_new_objectContext(ExpressionContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitExpr_new_object(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public final ExpressionContext expression(int _p) throws RecognitionException {
        ParserRuleContext _parentctx = _ctx;
        int _parentState = getState();
        ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState, _p);
        ExpressionContext _prevctx = _localctx;
        int _startState = 40;
        enterRecursionRule(_localctx, RULE_expression);
        int _la;
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(261);
                switch (getInterpreter().adaptivePredict(_input, 24, _ctx)) {
                case 1: {
                    _localctx = new Expr_math_unary_prefixContext(_localctx);
                    _ctx = _localctx;
                    _prevctx = _localctx;

                    setState(207);
                    switch (_input.LA(1)) {
                    case OP_MATH_PLUS: {
                        setState(205);
                        match(OP_MATH_PLUS);
                    }
                        break;
                    case OP_MATH_MINUS: {
                        setState(206);
                        match(OP_MATH_MINUS);
                    }
                        break;
                    default:
                        throw new NoViableAltException(this);
                    }
                    setState(209);
                    expression(19);
                }
                    break;

                case 2: {
                    _localctx = new Expr_math_unary_prefixContext(_localctx);
                    _ctx = _localctx;
                    _prevctx = _localctx;
                    setState(210);
                    _la = _input.LA(1);
                    if (!(_la == OP_MATH_INCREMENT || _la == OP_MATH_DECREMENT)) {
                        _errHandler.recoverInline(this);
                    }
                    consume();
                    setState(211);
                    expression(18);
                }
                    break;

                case 3: {
                    _localctx = new Expr_math_unary_prefixContext(_localctx);
                    _ctx = _localctx;
                    _prevctx = _localctx;
                    setState(212);
                    match(OP_BITWISE_NOT);
                    setState(213);
                    expression(17);
                }
                    break;

                case 4: {
                    _localctx = new Expr_compare_notContext(_localctx);
                    _ctx = _localctx;
                    _prevctx = _localctx;
                    setState(214);
                    match(OP_CONDITIONAL_NOT);
                    setState(215);
                    expression(16);
                }
                    break;

                case 5: {
                    _localctx = new Expr_class_castContext(_localctx);
                    _ctx = _localctx;
                    _prevctx = _localctx;
                    setState(216);
                    match(LEFT_PARENTHESE);
                    setState(217);
                    type();
                    setState(218);
                    match(RIGHT_PARENTHESE);
                    setState(219);
                    expression(15);
                }
                    break;

                case 6: {
                    _localctx = new Expr_groupContext(_localctx);
                    _ctx = _localctx;
                    _prevctx = _localctx;
                    setState(221);
                    match(LEFT_PARENTHESE);
                    setState(222);
                    expression(0);
                    setState(223);
                    match(RIGHT_PARENTHESE);
                }
                    break;

                case 7: {
                    _localctx = new Expr_constantContext(_localctx);
                    _ctx = _localctx;
                    _prevctx = _localctx;
                    setState(225);
                    constant();
                }
                    break;

                case 8: {
                    _localctx = new Expr_identifierContext(_localctx);
                    _ctx = _localctx;
                    _prevctx = _localctx;
                    setState(226);
                    match(IDENTIFIER);
                }
                    break;

                case 9: {
                    _localctx = new Expr_array_listContext(_localctx);
                    _ctx = _localctx;
                    _prevctx = _localctx;
                    setState(227);
                    match(LEFT_BRACKET);
                    setState(229);
                    _la = _input.LA(1);
                    if (((((_la - 32)) & ~0x3f) == 0 && ((1L << (_la - 32)) & ((1L << (LEFT_PARENTHESE - 32)) | (1L << (LEFT_BRACKET - 32)) | (1L << (LEFT_BRACE - 32))
                            | (1L << (OP_CONDITIONAL_NOT - 32)) | (1L << (OP_MATH_PLUS - 32)) | (1L << (OP_MATH_MINUS - 32)) | (1L << (OP_MATH_INCREMENT - 32)) | (1L << (OP_MATH_DECREMENT - 32))
                            | (1L << (OP_BITWISE_NOT - 32)) | (1L << (OP_NEW - 32)) | (1L << (KEYWORD_TRUE - 32)) | (1L << (KEYWORD_FALSE - 32)) | (1L << (KEYWORD_NULL - 32))
                            | (1L << (IDENTIFIER - 32)) | (1L << (INTEGER - 32)) | (1L << (INTEGER_HEX - 32)) | (1L << (FLOATING_POINT - 32)) | (1L << (STRING_DOUBLE - 32)) | (1L << (STRING_SINGLE - 32)))) != 0)) {
                        {
                            setState(228);
                            expression_list();
                        }
                    }

                    setState(231);
                    match(RIGHT_BRACKET);
                }
                    break;

                case 10: {
                    _localctx = new Expr_hash_mapContext(_localctx);
                    _ctx = _localctx;
                    _prevctx = _localctx;
                    setState(232);
                    match(LEFT_BRACE);
                    setState(234);
                    _la = _input.LA(1);
                    if (((((_la - 32)) & ~0x3f) == 0 && ((1L << (_la - 32)) & ((1L << (LEFT_PARENTHESE - 32)) | (1L << (LEFT_BRACKET - 32)) | (1L << (LEFT_BRACE - 32))
                            | (1L << (OP_CONDITIONAL_NOT - 32)) | (1L << (OP_MATH_PLUS - 32)) | (1L << (OP_MATH_MINUS - 32)) | (1L << (OP_MATH_INCREMENT - 32)) | (1L << (OP_MATH_DECREMENT - 32))
                            | (1L << (OP_BITWISE_NOT - 32)) | (1L << (OP_NEW - 32)) | (1L << (KEYWORD_TRUE - 32)) | (1L << (KEYWORD_FALSE - 32)) | (1L << (KEYWORD_NULL - 32))
                            | (1L << (IDENTIFIER - 32)) | (1L << (INTEGER - 32)) | (1L << (INTEGER_HEX - 32)) | (1L << (FLOATING_POINT - 32)) | (1L << (STRING_DOUBLE - 32)) | (1L << (STRING_SINGLE - 32)))) != 0)) {
                        {
                            setState(233);
                            hash_map_entry_list();
                        }
                    }

                    setState(236);
                    match(RIGHT_BRACE);
                }
                    break;

                case 11: {
                    _localctx = new Expr_function_callContext(_localctx);
                    _ctx = _localctx;
                    _prevctx = _localctx;
                    setState(237);
                    match(IDENTIFIER);
                    setState(238);
                    match(LEFT_PARENTHESE);
                    setState(240);
                    _la = _input.LA(1);
                    if (((((_la - 32)) & ~0x3f) == 0 && ((1L << (_la - 32)) & ((1L << (LEFT_PARENTHESE - 32)) | (1L << (LEFT_BRACKET - 32)) | (1L << (LEFT_BRACE - 32))
                            | (1L << (OP_CONDITIONAL_NOT - 32)) | (1L << (OP_MATH_PLUS - 32)) | (1L << (OP_MATH_MINUS - 32)) | (1L << (OP_MATH_INCREMENT - 32)) | (1L << (OP_MATH_DECREMENT - 32))
                            | (1L << (OP_BITWISE_NOT - 32)) | (1L << (OP_NEW - 32)) | (1L << (KEYWORD_TRUE - 32)) | (1L << (KEYWORD_FALSE - 32)) | (1L << (KEYWORD_NULL - 32))
                            | (1L << (IDENTIFIER - 32)) | (1L << (INTEGER - 32)) | (1L << (INTEGER_HEX - 32)) | (1L << (FLOATING_POINT - 32)) | (1L << (STRING_DOUBLE - 32)) | (1L << (STRING_SINGLE - 32)))) != 0)) {
                        {
                            setState(239);
                            expression_list();
                        }
                    }

                    setState(242);
                    match(RIGHT_PARENTHESE);
                }
                    break;

                case 12: {
                    _localctx = new Expr_new_objectContext(_localctx);
                    _ctx = _localctx;
                    _prevctx = _localctx;
                    setState(243);
                    match(OP_NEW);
                    setState(244);
                    type();
                    setState(245);
                    match(LEFT_PARENTHESE);
                    setState(247);
                    _la = _input.LA(1);
                    if (((((_la - 32)) & ~0x3f) == 0 && ((1L << (_la - 32)) & ((1L << (LEFT_PARENTHESE - 32)) | (1L << (LEFT_BRACKET - 32)) | (1L << (LEFT_BRACE - 32))
                            | (1L << (OP_CONDITIONAL_NOT - 32)) | (1L << (OP_MATH_PLUS - 32)) | (1L << (OP_MATH_MINUS - 32)) | (1L << (OP_MATH_INCREMENT - 32)) | (1L << (OP_MATH_DECREMENT - 32))
                            | (1L << (OP_BITWISE_NOT - 32)) | (1L << (OP_NEW - 32)) | (1L << (KEYWORD_TRUE - 32)) | (1L << (KEYWORD_FALSE - 32)) | (1L << (KEYWORD_NULL - 32))
                            | (1L << (IDENTIFIER - 32)) | (1L << (INTEGER - 32)) | (1L << (INTEGER_HEX - 32)) | (1L << (FLOATING_POINT - 32)) | (1L << (STRING_DOUBLE - 32)) | (1L << (STRING_SINGLE - 32)))) != 0)) {
                        {
                            setState(246);
                            expression_list();
                        }
                    }

                    setState(249);
                    match(RIGHT_PARENTHESE);
                }
                    break;

                case 13: {
                    _localctx = new Expr_new_arrayContext(_localctx);
                    _ctx = _localctx;
                    _prevctx = _localctx;
                    setState(251);
                    match(OP_NEW);
                    setState(252);
                    type();
                    setState(257);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 23, _ctx);
                    do {
                        switch (_alt) {
                        case 1: {
                            {
                                setState(253);
                                match(LEFT_BRACKET);
                                setState(254);
                                expression(0);
                                setState(255);
                                match(RIGHT_BRACKET);
                            }
                        }
                            break;
                        default:
                            throw new NoViableAltException(this);
                        }
                        setState(259);
                        _errHandler.sync(this);
                        _alt = getInterpreter().adaptivePredict(_input, 23, _ctx);
                    } while (_alt != 2 && _alt != -1);
                }
                    break;
                }
                _ctx.stop = _input.LT(-1);
                setState(329);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 28, _ctx);
                while (_alt != 2 && _alt != -1) {
                    if (_alt == 1) {
                        if (_parseListeners != null) triggerExitRuleEvent();
                        _prevctx = _localctx;
                        {
                            setState(327);
                            switch (getInterpreter().adaptivePredict(_input, 27, _ctx)) {
                            case 1: {
                                _localctx = new Expr_math_binary_basicContext(new ExpressionContext(_parentctx, _parentState, _p));
                                pushNewRecursionContext(_localctx, _startState, RULE_expression);
                                setState(263);
                                if (!(12 >= _localctx._p)) throw new FailedPredicateException(this, "12 >= $_p");
                                setState(264);
                                _la = _input.LA(1);
                                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << OP_MATH_MULTIPLICATION) | (1L << OP_MATH_DIVISION) | (1L << OP_MATH_REMAINDER))) != 0))) {
                                    _errHandler.recoverInline(this);
                                }
                                consume();
                                setState(265);
                                expression(13);
                            }
                                break;

                            case 2: {
                                _localctx = new Expr_math_binary_basicContext(new ExpressionContext(_parentctx, _parentState, _p));
                                pushNewRecursionContext(_localctx, _startState, RULE_expression);
                                setState(266);
                                if (!(11 >= _localctx._p)) throw new FailedPredicateException(this, "11 >= $_p");
                                setState(267);
                                _la = _input.LA(1);
                                if (!(_la == OP_MATH_PLUS || _la == OP_MATH_MINUS)) {
                                    _errHandler.recoverInline(this);
                                }
                                consume();
                                setState(268);
                                expression(12);
                            }
                                break;

                            case 3: {
                                _localctx = new Expr_math_binary_shiftContext(new ExpressionContext(_parentctx, _parentState, _p));
                                pushNewRecursionContext(_localctx, _startState, RULE_expression);
                                setState(269);
                                if (!(10 >= _localctx._p)) throw new FailedPredicateException(this, "10 >= $_p");
                                setState(276);
                                switch (getInterpreter().adaptivePredict(_input, 25, _ctx)) {
                                case 1: {
                                    setState(270);
                                    match(OP_BITWISE_SHL);
                                }
                                    break;

                                case 2: {
                                    setState(271);
                                    match(OP_RELATIONAL_GT);
                                    setState(272);
                                    match(OP_RELATIONAL_GT);
                                }
                                    break;

                                case 3: {
                                    setState(273);
                                    match(OP_RELATIONAL_GT);
                                    setState(274);
                                    match(OP_RELATIONAL_GT);
                                    setState(275);
                                    match(OP_RELATIONAL_GT);
                                }
                                    break;
                                }
                                setState(278);
                                expression(11);
                            }
                                break;

                            case 4: {
                                _localctx = new Expr_compare_relationalContext(new ExpressionContext(_parentctx, _parentState, _p));
                                pushNewRecursionContext(_localctx, _startState, RULE_expression);
                                setState(279);
                                if (!(9 >= _localctx._p)) throw new FailedPredicateException(this, "9 >= $_p");
                                setState(280);
                                _la = _input.LA(1);
                                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << OP_RELATIONAL_GT) | (1L << OP_RELATIONAL_LT) | (1L << OP_RELATIONAL_GE) | (1L << OP_RELATIONAL_LE))) != 0))) {
                                    _errHandler.recoverInline(this);
                                }
                                consume();
                                setState(281);
                                expression(10);
                            }
                                break;

                            case 5: {
                                _localctx = new Expr_compare_equalityContext(new ExpressionContext(_parentctx, _parentState, _p));
                                pushNewRecursionContext(_localctx, _startState, RULE_expression);
                                setState(282);
                                if (!(7 >= _localctx._p)) throw new FailedPredicateException(this, "7 >= $_p");
                                setState(283);
                                _la = _input.LA(1);
                                if (!(_la == OP_EQUALITY_EQ || _la == OP_EQUALITY_NE)) {
                                    _errHandler.recoverInline(this);
                                }
                                consume();
                                setState(284);
                                expression(8);
                            }
                                break;

                            case 6: {
                                _localctx = new Expr_math_binary_bitwiseContext(new ExpressionContext(_parentctx, _parentState, _p));
                                pushNewRecursionContext(_localctx, _startState, RULE_expression);
                                setState(285);
                                if (!(6 >= _localctx._p)) throw new FailedPredicateException(this, "6 >= $_p");
                                setState(286);
                                match(OP_BITWISE_AND);
                                setState(287);
                                expression(7);
                            }
                                break;

                            case 7: {
                                _localctx = new Expr_math_binary_bitwiseContext(new ExpressionContext(_parentctx, _parentState, _p));
                                pushNewRecursionContext(_localctx, _startState, RULE_expression);
                                setState(288);
                                if (!(5 >= _localctx._p)) throw new FailedPredicateException(this, "5 >= $_p");
                                setState(289);
                                match(OP_BITWISE_XOR);
                                setState(290);
                                expression(5);
                            }
                                break;

                            case 8: {
                                _localctx = new Expr_math_binary_bitwiseContext(new ExpressionContext(_parentctx, _parentState, _p));
                                pushNewRecursionContext(_localctx, _startState, RULE_expression);
                                setState(291);
                                if (!(4 >= _localctx._p)) throw new FailedPredicateException(this, "4 >= $_p");
                                setState(292);
                                match(OP_BITWISE_OR);
                                setState(293);
                                expression(5);
                            }
                                break;

                            case 9: {
                                _localctx = new Expr_compare_conditionContext(new ExpressionContext(_parentctx, _parentState, _p));
                                pushNewRecursionContext(_localctx, _startState, RULE_expression);
                                setState(294);
                                if (!(3 >= _localctx._p)) throw new FailedPredicateException(this, "3 >= $_p");
                                setState(295);
                                match(OP_CONDITIONAL_AND);
                                setState(296);
                                expression(4);
                            }
                                break;

                            case 10: {
                                _localctx = new Expr_compare_conditionContext(new ExpressionContext(_parentctx, _parentState, _p));
                                pushNewRecursionContext(_localctx, _startState, RULE_expression);
                                setState(297);
                                if (!(2 >= _localctx._p)) throw new FailedPredicateException(this, "2 >= $_p");
                                setState(298);
                                match(OP_CONDITIONAL_OR);
                                setState(299);
                                expression(3);
                            }
                                break;

                            case 11: {
                                _localctx = new Expr_conditional_ternaryContext(new ExpressionContext(_parentctx, _parentState, _p));
                                pushNewRecursionContext(_localctx, _startState, RULE_expression);
                                setState(300);
                                if (!(1 >= _localctx._p)) throw new FailedPredicateException(this, "1 >= $_p");
                                setState(301);
                                match(OP_CONDITIONAL_TERNARY);
                                setState(302);
                                expression(0);
                                setState(303);
                                match(COLON);
                                setState(304);
                                expression(1);
                            }
                                break;

                            case 12: {
                                _localctx = new Expr_field_accessContext(new ExpressionContext(_parentctx, _parentState, _p));
                                pushNewRecursionContext(_localctx, _startState, RULE_expression);
                                setState(306);
                                if (!(24 >= _localctx._p)) throw new FailedPredicateException(this, "24 >= $_p");
                                setState(307);
                                _la = _input.LA(1);
                                if (!(_la == OP_DOT_INVOCATION || _la == OP_DOT_INVOCATION_SAFE)) {
                                    _errHandler.recoverInline(this);
                                }
                                consume();
                                setState(308);
                                match(IDENTIFIER);
                            }
                                break;

                            case 13: {
                                _localctx = new Expr_method_invocationContext(new ExpressionContext(_parentctx, _parentState, _p));
                                pushNewRecursionContext(_localctx, _startState, RULE_expression);
                                setState(309);
                                if (!(23 >= _localctx._p)) throw new FailedPredicateException(this, "23 >= $_p");
                                setState(310);
                                _la = _input.LA(1);
                                if (!(_la == OP_DOT_INVOCATION || _la == OP_DOT_INVOCATION_SAFE)) {
                                    _errHandler.recoverInline(this);
                                }
                                consume();
                                setState(311);
                                match(IDENTIFIER);
                                setState(312);
                                match(LEFT_PARENTHESE);
                                setState(314);
                                _la = _input.LA(1);
                                if (((((_la - 32)) & ~0x3f) == 0 && ((1L << (_la - 32)) & ((1L << (LEFT_PARENTHESE - 32)) | (1L << (LEFT_BRACKET - 32)) | (1L << (LEFT_BRACE - 32))
                                        | (1L << (OP_CONDITIONAL_NOT - 32)) | (1L << (OP_MATH_PLUS - 32)) | (1L << (OP_MATH_MINUS - 32)) | (1L << (OP_MATH_INCREMENT - 32))
                                        | (1L << (OP_MATH_DECREMENT - 32)) | (1L << (OP_BITWISE_NOT - 32)) | (1L << (OP_NEW - 32)) | (1L << (KEYWORD_TRUE - 32)) | (1L << (KEYWORD_FALSE - 32))
                                        | (1L << (KEYWORD_NULL - 32)) | (1L << (IDENTIFIER - 32)) | (1L << (INTEGER - 32)) | (1L << (INTEGER_HEX - 32)) | (1L << (FLOATING_POINT - 32))
                                        | (1L << (STRING_DOUBLE - 32)) | (1L << (STRING_SINGLE - 32)))) != 0)) {
                                    {
                                        setState(313);
                                        expression_list();
                                    }
                                }

                                setState(316);
                                match(RIGHT_PARENTHESE);
                            }
                                break;

                            case 14: {
                                _localctx = new Expr_array_getContext(new ExpressionContext(_parentctx, _parentState, _p));
                                pushNewRecursionContext(_localctx, _startState, RULE_expression);
                                setState(317);
                                if (!(21 >= _localctx._p)) throw new FailedPredicateException(this, "21 >= $_p");
                                setState(318);
                                match(LEFT_BRACKET);
                                setState(319);
                                expression(0);
                                setState(320);
                                match(RIGHT_BRACKET);
                            }
                                break;

                            case 15: {
                                _localctx = new Expr_math_unary_suffixContext(new ExpressionContext(_parentctx, _parentState, _p));
                                pushNewRecursionContext(_localctx, _startState, RULE_expression);
                                setState(322);
                                if (!(20 >= _localctx._p)) throw new FailedPredicateException(this, "20 >= $_p");
                                setState(323);
                                _la = _input.LA(1);
                                if (!(_la == OP_MATH_INCREMENT || _la == OP_MATH_DECREMENT)) {
                                    _errHandler.recoverInline(this);
                                }
                                consume();
                            }
                                break;

                            case 16: {
                                _localctx = new Expr_instanceofContext(new ExpressionContext(_parentctx, _parentState, _p));
                                pushNewRecursionContext(_localctx, _startState, RULE_expression);
                                setState(324);
                                if (!(8 >= _localctx._p)) throw new FailedPredicateException(this, "8 >= $_p");
                                setState(325);
                                match(OP_INSTANCEOF);
                                setState(326);
                                type();
                            }
                                break;
                            }
                        }
                    }
                    setState(331);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 28, _ctx);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            unrollRecursionContexts(_parentctx);
        }
        return _localctx;
    }

    public static class ConstantContext extends ParserRuleContext {
        public TerminalNode KEYWORD_NULL() {
            return getToken(JetTemplateParser.KEYWORD_NULL, 0);
        }

        public TerminalNode KEYWORD_FALSE() {
            return getToken(JetTemplateParser.KEYWORD_FALSE, 0);
        }

        public TerminalNode FLOATING_POINT() {
            return getToken(JetTemplateParser.FLOATING_POINT, 0);
        }

        public TerminalNode STRING_DOUBLE() {
            return getToken(JetTemplateParser.STRING_DOUBLE, 0);
        }

        public TerminalNode INTEGER_HEX() {
            return getToken(JetTemplateParser.INTEGER_HEX, 0);
        }

        public TerminalNode INTEGER() {
            return getToken(JetTemplateParser.INTEGER, 0);
        }

        public TerminalNode KEYWORD_TRUE() {
            return getToken(JetTemplateParser.KEYWORD_TRUE, 0);
        }

        public TerminalNode STRING_SINGLE() {
            return getToken(JetTemplateParser.STRING_SINGLE, 0);
        }

        public ConstantContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_constant;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitConstant(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public final ConstantContext constant() throws RecognitionException {
        ConstantContext _localctx = new ConstantContext(_ctx, getState());
        enterRule(_localctx, 42, RULE_constant);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(332);
                _la = _input.LA(1);
                if (!(((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & ((1L << (KEYWORD_TRUE - 67)) | (1L << (KEYWORD_FALSE - 67)) | (1L << (KEYWORD_NULL - 67)) | (1L << (INTEGER - 67))
                        | (1L << (INTEGER_HEX - 67)) | (1L << (FLOATING_POINT - 67)) | (1L << (STRING_DOUBLE - 67)) | (1L << (STRING_SINGLE - 67)))) != 0))) {
                    _errHandler.recoverInline(this);
                }
                consume();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Expression_listContext extends ParserRuleContext {
        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class, i);
        }

        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }

        public Expression_listContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_expression_list;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitExpression_list(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public final Expression_listContext expression_list() throws RecognitionException {
        Expression_listContext _localctx = new Expression_listContext(_ctx, getState());
        enterRule(_localctx, 44, RULE_expression_list);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(334);
                expression(0);
                setState(339);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == COMMA) {
                    {
                        {
                            setState(335);
                            match(COMMA);
                            setState(336);
                            expression(0);
                        }
                    }
                    setState(341);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Hash_map_entry_listContext extends ParserRuleContext {
        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class, i);
        }

        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }

        public Hash_map_entry_listContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_hash_map_entry_list;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitHash_map_entry_list(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public final Hash_map_entry_listContext hash_map_entry_list() throws RecognitionException {
        Hash_map_entry_listContext _localctx = new Hash_map_entry_listContext(_ctx, getState());
        enterRule(_localctx, 46, RULE_hash_map_entry_list);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(342);
                expression(0);
                setState(343);
                match(COLON);
                setState(344);
                expression(0);
                setState(352);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == COMMA) {
                    {
                        {
                            setState(345);
                            match(COMMA);
                            setState(346);
                            expression(0);
                            setState(347);
                            match(COLON);
                            setState(348);
                            expression(0);
                        }
                    }
                    setState(354);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class TypeContext extends ParserRuleContext {
        public Type_array_suffixContext type_array_suffix(int i) {
            return getRuleContext(Type_array_suffixContext.class, i);
        }

        public Type_argumentsContext type_arguments() {
            return getRuleContext(Type_argumentsContext.class, 0);
        }

        public TerminalNode IDENTIFIER(int i) {
            return getToken(JetTemplateParser.IDENTIFIER, i);
        }

        public List<TerminalNode> IDENTIFIER() {
            return getTokens(JetTemplateParser.IDENTIFIER);
        }

        public List<Type_array_suffixContext> type_array_suffix() {
            return getRuleContexts(Type_array_suffixContext.class);
        }

        public TypeContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_type;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitType(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public final TypeContext type() throws RecognitionException {
        TypeContext _localctx = new TypeContext(_ctx, getState());
        enterRule(_localctx, 48, RULE_type);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(355);
                match(IDENTIFIER);
                setState(360);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 31, _ctx);
                while (_alt != 2 && _alt != -1) {
                    if (_alt == 1) {
                        {
                            {
                                setState(356);
                                match(OP_DOT_INVOCATION);
                                setState(357);
                                match(IDENTIFIER);
                            }
                        }
                    }
                    setState(362);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 31, _ctx);
                }
                setState(364);
                switch (getInterpreter().adaptivePredict(_input, 32, _ctx)) {
                case 1: {
                    setState(363);
                    type_arguments();
                }
                    break;
                }
                setState(369);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 33, _ctx);
                while (_alt != 2 && _alt != -1) {
                    if (_alt == 1) {
                        {
                            {
                                setState(366);
                                type_array_suffix();
                            }
                        }
                    }
                    setState(371);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 33, _ctx);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Type_array_suffixContext extends ParserRuleContext {
        public Type_array_suffixContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_type_array_suffix;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitType_array_suffix(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public final Type_array_suffixContext type_array_suffix() throws RecognitionException {
        Type_array_suffixContext _localctx = new Type_array_suffixContext(_ctx, getState());
        enterRule(_localctx, 50, RULE_type_array_suffix);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(372);
                match(LEFT_BRACKET);
                setState(373);
                match(RIGHT_BRACKET);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Type_argumentsContext extends ParserRuleContext {
        public Type_listContext type_list() {
            return getRuleContext(Type_listContext.class, 0);
        }

        public Type_argumentsContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_type_arguments;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitType_arguments(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public final Type_argumentsContext type_arguments() throws RecognitionException {
        Type_argumentsContext _localctx = new Type_argumentsContext(_ctx, getState());
        enterRule(_localctx, 52, RULE_type_arguments);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(375);
                match(OP_RELATIONAL_LT);
                setState(376);
                type_list();
                setState(377);
                match(OP_RELATIONAL_GT);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Type_listContext extends ParserRuleContext {
        public List<Type_nameContext> type_name() {
            return getRuleContexts(Type_nameContext.class);
        }

        public Type_nameContext type_name(int i) {
            return getRuleContext(Type_nameContext.class, i);
        }

        public Type_listContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_type_list;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitType_list(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public final Type_listContext type_list() throws RecognitionException {
        Type_listContext _localctx = new Type_listContext(_ctx, getState());
        enterRule(_localctx, 54, RULE_type_list);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(379);
                type_name();
                setState(384);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == COMMA) {
                    {
                        {
                            setState(380);
                            match(COMMA);
                            setState(381);
                            type_name();
                        }
                    }
                    setState(386);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Type_nameContext extends ParserRuleContext {
        public TypeContext type() {
            return getRuleContext(TypeContext.class, 0);
        }

        public Type_nameContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_type_name;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof JetTemplateParserVisitor)
                return ((JetTemplateParserVisitor<? extends T>) visitor).visitType_name(this);
            else
                return visitor.visitChildren(this);
        }
    }

    public final Type_nameContext type_name() throws RecognitionException {
        Type_nameContext _localctx = new Type_nameContext(_ctx, getState());
        enterRule(_localctx, 56, RULE_type_name);
        try {
            setState(389);
            switch (_input.LA(1)) {
            case IDENTIFIER:
                enterOuterAlt(_localctx, 1);
                {
                    setState(387);
                    type();
                }
                break;
            case OP_CONDITIONAL_TERNARY:
                enterOuterAlt(_localctx, 2);
                {
                    setState(388);
                    match(OP_CONDITIONAL_TERNARY);
                }
                break;
            default:
                throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
        switch (ruleIndex) {
        case 20:
            return expression_sempred((ExpressionContext) _localctx, predIndex);
        }
        return true;
    }

    private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
        switch (predIndex) {
        case 0:
            return 12 >= _localctx._p;

        case 1:
            return 11 >= _localctx._p;

        case 2:
            return 10 >= _localctx._p;

        case 3:
            return 9 >= _localctx._p;

        case 4:
            return 7 >= _localctx._p;

        case 5:
            return 6 >= _localctx._p;

        case 6:
            return 5 >= _localctx._p;

        case 7:
            return 4 >= _localctx._p;

        case 8:
            return 3 >= _localctx._p;

        case 9:
            return 2 >= _localctx._p;

        case 10:
            return 1 >= _localctx._p;

        case 11:
            return 24 >= _localctx._p;

        case 12:
            return 23 >= _localctx._p;

        case 13:
            return 21 >= _localctx._p;

        case 14:
            return 20 >= _localctx._p;

        case 15:
            return 8 >= _localctx._p;
        }
        return true;
    }

    public static final String _serializedATN = "\3\uacf5\uee8c\u4f5d\u8b0d\u4a45\u78bd\u1b2f\u3378\3M\u018a\4\2\t\2\4" + "\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"
            + "\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22" + "\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"
            + "\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\3\2\3\2\3\3\3\3\3\3" + "\7\3B\n\3\f\3\16\3E\13\3\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\5\5Q"
            + "\n\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\5\6]\n\6\3\7\3\7\3\7\3\7" + "\7\7c\n\7\f\7\16\7f\13\7\3\7\3\7\3\b\3\b\3\b\3\t\3\t\3\t\3\t\7\tq\n\t"
            + "\f\t\16\tt\13\t\3\t\3\t\3\n\5\ny\n\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3" + "\13\7\13\u0083\n\13\f\13\16\13\u0086\13\13\3\13\3\13\3\f\3\f\3\f\3\f\3"
            + "\f\7\f\u008f\n\f\f\f\16\f\u0092\13\f\3\f\5\f\u0095\n\f\3\f\3\f\3\r\3\r" + "\3\r\3\r\3\r\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\5\17\u00a6\n\17\3"
            + "\17\3\17\3\20\5\20\u00ab\n\20\3\20\3\20\3\20\3\20\3\21\3\21\5\21\u00b3" + "\n\21\3\21\3\21\5\21\u00b7\n\21\3\22\3\22\5\22\u00bb\n\22\3\22\3\22\5"
            + "\22\u00bf\n\22\3\23\3\23\5\23\u00c3\n\23\3\23\3\23\5\23\u00c7\n\23\3\24" + "\3\24\3\24\3\24\3\25\3\25\3\26\3\26\3\26\5\26\u00d2\n\26\3\26\3\26\3\26"
            + "\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26" + "\3\26\3\26\3\26\5\26\u00e8\n\26\3\26\3\26\3\26\5\26\u00ed\n\26\3\26\3"
            + "\26\3\26\3\26\5\26\u00f3\n\26\3\26\3\26\3\26\3\26\3\26\5\26\u00fa\n\26" + "\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\6\26\u0104\n\26\r\26\16\26\u0105"
            + "\5\26\u0108\n\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26" + "\3\26\3\26\5\26\u0117\n\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26"
            + "\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26" + "\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\5\26"
            + "\u013d\n\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\7\26" + "\u014a\n\26\f\26\16\26\u014d\13\26\3\27\3\27\3\30\3\30\3\30\7\30\u0154"
            + "\n\30\f\30\16\30\u0157\13\30\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\7" + "\31\u0161\n\31\f\31\16\31\u0164\13\31\3\32\3\32\3\32\7\32\u0169\n\32\f"
            + "\32\16\32\u016c\13\32\3\32\5\32\u016f\n\32\3\32\7\32\u0172\n\32\f\32\16" + "\32\u0175\13\32\3\33\3\33\3\33\3\34\3\34\3\34\3\34\3\35\3\35\3\35\7\35"
            + "\u0181\n\35\f\35\16\35\u0184\13\35\3\36\3\36\5\36\u0188\n\36\3\36\2\37" + "\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:\2\13\3"
            + "\2\5\b\3\2\25\33\3\29:\3\2\668\3\2\64\65\3\2-\60\3\2+,\3\2)*\4\2EGIM\u01b3" + "\2<\3\2\2\2\4C\3\2\2\2\6F\3\2\2\2\bP\3\2\2\2\n\\\3\2\2\2\f^\3\2\2\2\16"
            + "i\3\2\2\2\20l\3\2\2\2\22x\3\2\2\2\24~\3\2\2\2\26\u0089\3\2\2\2\30\u0098" + "\3\2\2\2\32\u009d\3\2\2\2\34\u00a0\3\2\2\2\36\u00aa\3\2\2\2 \u00b6\3\2"
            + "\2\2\"\u00be\3\2\2\2$\u00c6\3\2\2\2&\u00c8\3\2\2\2(\u00cc\3\2\2\2*\u0107" + "\3\2\2\2,\u014e\3\2\2\2.\u0150\3\2\2\2\60\u0158\3\2\2\2\62\u0165\3\2\2"
            + "\2\64\u0176\3\2\2\2\66\u0179\3\2\2\28\u017d\3\2\2\2:\u0187\3\2\2\2<=\5" + "\4\3\2=\3\3\2\2\2>B\5\6\4\2?B\5\b\5\2@B\5\n\6\2A>\3\2\2\2A?\3\2\2\2A@"
            + "\3\2\2\2BE\3\2\2\2CA\3\2\2\2CD\3\2\2\2D\5\3\2\2\2EC\3\2\2\2FG\t\2\2\2" + "G\7\3\2\2\2HI\7\t\2\2IJ\5*\26\2JK\7\'\2\2KQ\3\2\2\2LM\7\n\2\2MN\5*\26"
            + "\2NO\7\'\2\2OQ\3\2\2\2PH\3\2\2\2PL\3\2\2\2Q\t\3\2\2\2R]\5\f\7\2S]\5\20" + "\t\2T]\5\24\13\2U]\5\26\f\2V]\5\34\17\2W]\5 \21\2X]\5\"\22\2Y]\5$\23\2"
            + "Z]\5&\24\2[]\5(\25\2\\R\3\2\2\2\\S\3\2\2\2\\T\3\2\2\2\\U\3\2\2\2\\V\3" + "\2\2\2\\W\3\2\2\2\\X\3\2\2\2\\Y\3\2\2\2\\Z\3\2\2\2\\[\3\2\2\2]\13\3\2"
            + "\2\2^_\7\13\2\2_d\5\16\b\2`a\7C\2\2ac\5\16\b\2b`\3\2\2\2cf\3\2\2\2db\3" + "\2\2\2de\3\2\2\2eg\3\2\2\2fd\3\2\2\2gh\7#\2\2h\r\3\2\2\2ij\5\62\32\2j"
            + "k\7H\2\2k\17\3\2\2\2lm\7\f\2\2mr\5\22\n\2no\7C\2\2oq\5\22\n\2pn\3\2\2" + "\2qt\3\2\2\2rp\3\2\2\2rs\3\2\2\2su\3\2\2\2tr\3\2\2\2uv\7#\2\2v\21\3\2"
            + "\2\2wy\5\62\32\2xw\3\2\2\2xy\3\2\2\2yz\3\2\2\2z{\7H\2\2{|\7(\2\2|}\5*" + "\26\2}\23\3\2\2\2~\177\7\r\2\2\177\u0084\5*\26\2\u0080\u0081\7C\2\2\u0081"
            + "\u0083\5*\26\2\u0082\u0080\3\2\2\2\u0083\u0086\3\2\2\2\u0084\u0082\3\2" + "\2\2\u0084\u0085\3\2\2\2\u0085\u0087\3\2\2\2\u0086\u0084\3\2\2\2\u0087"
            + "\u0088\7#\2\2\u0088\25\3\2\2\2\u0089\u008a\7\16\2\2\u008a\u008b\5*\26" + "\2\u008b\u008c\7#\2\2\u008c\u0090\5\4\3\2\u008d\u008f\5\30\r\2\u008e\u008d"
            + "\3\2\2\2\u008f\u0092\3\2\2\2\u0090\u008e\3\2\2\2\u0090\u0091\3\2\2\2\u0091" + "\u0094\3\2\2\2\u0092\u0090\3\2\2\2\u0093\u0095\5\32\16\2\u0094\u0093\3"
            + "\2\2\2\u0094\u0095\3\2\2\2\u0095\u0096\3\2\2\2\u0096\u0097\7 \2\2\u0097" + "\27\3\2\2\2\u0098\u0099\7\17\2\2\u0099\u009a\5*\26\2\u009a\u009b\7#\2"
            + "\2\u009b\u009c\5\4\3\2\u009c\31\3\2\2\2\u009d\u009e\7\37\2\2\u009e\u009f" + "\5\4\3\2\u009f\33\3\2\2\2\u00a0\u00a1\7\20\2\2\u00a1\u00a2\5\36\20\2\u00a2"
            + "\u00a3\7#\2\2\u00a3\u00a5\5\4\3\2\u00a4\u00a6\5\32\16\2\u00a5\u00a4\3" + "\2\2\2\u00a5\u00a6\3\2\2\2\u00a6\u00a7\3\2\2\2\u00a7\u00a8\7 \2\2\u00a8"
            + "\35\3\2\2\2\u00a9\u00ab\5\62\32\2\u00aa\u00a9\3\2\2\2\u00aa\u00ab\3\2" + "\2\2\u00ab\u00ac\3\2\2\2\u00ac\u00ad\7H\2\2\u00ad\u00ae\7D\2\2\u00ae\u00af"
            + "\5*\26\2\u00af\37\3\2\2\2\u00b0\u00b2\7\21\2\2\u00b1\u00b3\5*\26\2\u00b2" + "\u00b1\3\2\2\2\u00b2\u00b3\3\2\2\2\u00b3\u00b4\3\2\2\2\u00b4\u00b7\7#"
            + "\2\2\u00b5\u00b7\7\34\2\2\u00b6\u00b0\3\2\2\2\u00b6\u00b5\3\2\2\2\u00b7" + "!\3\2\2\2\u00b8\u00ba\7\22\2\2\u00b9\u00bb\5*\26\2\u00ba\u00b9\3\2\2\2"
            + "\u00ba\u00bb\3\2\2\2\u00bb\u00bc\3\2\2\2\u00bc\u00bf\7#\2\2\u00bd\u00bf" + "\7\35\2\2\u00be\u00b8\3\2\2\2\u00be\u00bd\3\2\2\2\u00bf#\3\2\2\2\u00c0"
            + "\u00c2\7\23\2\2\u00c1\u00c3\5*\26\2\u00c2\u00c1\3\2\2\2\u00c2\u00c3\3" + "\2\2\2\u00c3\u00c4\3\2\2\2\u00c4\u00c7\7#\2\2\u00c5\u00c7\7\36\2\2\u00c6"
            + "\u00c0\3\2\2\2\u00c6\u00c5\3\2\2\2\u00c7%\3\2\2\2\u00c8\u00c9\7\24\2\2" + "\u00c9\u00ca\5.\30\2\u00ca\u00cb\7#\2\2\u00cb\'\3\2\2\2\u00cc\u00cd\t"
            + "\3\2\2\u00cd)\3\2\2\2\u00ce\u00d1\b\26\1\2\u00cf\u00d2\7\64\2\2\u00d0" + "\u00d2\7\65\2\2\u00d1\u00cf\3\2\2\2\u00d1\u00d0\3\2\2\2\u00d2\u00d3\3"
            + "\2\2\2\u00d3\u0108\5*\26\2\u00d4\u00d5\t\4\2\2\u00d5\u0108\5*\26\2\u00d6" + "\u00d7\7=\2\2\u00d7\u0108\5*\26\2\u00d8\u00d9\7\63\2\2\u00d9\u0108\5*"
            + "\26\2\u00da\u00db\7\"\2\2\u00db\u00dc\5\62\32\2\u00dc\u00dd\7#\2\2\u00dd" + "\u00de\5*\26\2\u00de\u0108\3\2\2\2\u00df\u00e0\7\"\2\2\u00e0\u00e1\5*"
            + "\26\2\u00e1\u00e2\7#\2\2\u00e2\u0108\3\2\2\2\u00e3\u0108\5,\27\2\u00e4" + "\u0108\7H\2\2\u00e5\u00e7\7$\2\2\u00e6\u00e8\5.\30\2\u00e7\u00e6\3\2\2"
            + "\2\u00e7\u00e8\3\2\2\2\u00e8\u00e9\3\2\2\2\u00e9\u0108\7%\2\2\u00ea\u00ec" + "\7&\2\2\u00eb\u00ed\5\60\31\2\u00ec\u00eb\3\2\2\2\u00ec\u00ed\3\2\2\2"
            + "\u00ed\u00ee\3\2\2\2\u00ee\u0108\7\'\2\2\u00ef\u00f0\7H\2\2\u00f0\u00f2" + "\7\"\2\2\u00f1\u00f3\5.\30\2\u00f2\u00f1\3\2\2\2\u00f2\u00f3\3\2\2\2\u00f3"
            + "\u00f4\3\2\2\2\u00f4\u0108\7#\2\2\u00f5\u00f6\7A\2\2\u00f6\u00f7\5\62" + "\32\2\u00f7\u00f9\7\"\2\2\u00f8\u00fa\5.\30\2\u00f9\u00f8\3\2\2\2\u00f9"
            + "\u00fa\3\2\2\2\u00fa\u00fb\3\2\2\2\u00fb\u00fc\7#\2\2\u00fc\u0108\3\2" + "\2\2\u00fd\u00fe\7A\2\2\u00fe\u0103\5\62\32\2\u00ff\u0100\7$\2\2\u0100"
            + "\u0101\5*\26\2\u0101\u0102\7%\2\2\u0102\u0104\3\2\2\2\u0103\u00ff\3\2" + "\2\2\u0104\u0105\3\2\2\2\u0105\u0103\3\2\2\2\u0105\u0106\3\2\2\2\u0106"
            + "\u0108\3\2\2\2\u0107\u00ce\3\2\2\2\u0107\u00d4\3\2\2\2\u0107\u00d6\3\2" + "\2\2\u0107\u00d8\3\2\2\2\u0107\u00da\3\2\2\2\u0107\u00df\3\2\2\2\u0107"
            + "\u00e3\3\2\2\2\u0107\u00e4\3\2\2\2\u0107\u00e5\3\2\2\2\u0107\u00ea\3\2" + "\2\2\u0107\u00ef\3\2\2\2\u0107\u00f5\3\2\2\2\u0107\u00fd\3\2\2\2\u0108"
            + "\u014b\3\2\2\2\u0109\u010a\6\26\2\3\u010a\u010b\t\5\2\2\u010b\u014a\5" + "*\26\2\u010c\u010d\6\26\3\3\u010d\u010e\t\6\2\2\u010e\u014a\5*\26\2\u010f"
            + "\u0116\6\26\4\3\u0110\u0117\7?\2\2\u0111\u0112\7-\2\2\u0112\u0117\7-\2" + "\2\u0113\u0114\7-\2\2\u0114\u0115\7-\2\2\u0115\u0117\7-\2\2\u0116\u0110"
            + "\3\2\2\2\u0116\u0111\3\2\2\2\u0116\u0113\3\2\2\2\u0117\u0118\3\2\2\2\u0118" + "\u014a\5*\26\2\u0119\u011a\6\26\5\3\u011a\u011b\t\7\2\2\u011b\u014a\5"
            + "*\26\2\u011c\u011d\6\26\6\3\u011d\u011e\t\b\2\2\u011e\u014a\5*\26\2\u011f" + "\u0120\6\26\7\3\u0120\u0121\7;\2\2\u0121\u014a\5*\26\2\u0122\u0123\6\26"
            + "\b\3\u0123\u0124\7>\2\2\u0124\u014a\5*\26\2\u0125\u0126\6\26\t\3\u0126" + "\u0127\7<\2\2\u0127\u014a\5*\26\2\u0128\u0129\6\26\n\3\u0129\u012a\7\61"
            + "\2\2\u012a\u014a\5*\26\2\u012b\u012c\6\26\13\3\u012c\u012d\7\62\2\2\u012d" + "\u014a\5*\26\2\u012e\u012f\6\26\f\3\u012f\u0130\7B\2\2\u0130\u0131\5*"
            + "\26\2\u0131\u0132\7D\2\2\u0132\u0133\5*\26\2\u0133\u014a\3\2\2\2\u0134" + "\u0135\6\26\r\3\u0135\u0136\t\t\2\2\u0136\u014a\7H\2\2\u0137\u0138\6\26"
            + "\16\3\u0138\u0139\t\t\2\2\u0139\u013a\7H\2\2\u013a\u013c\7\"\2\2\u013b" + "\u013d\5.\30\2\u013c\u013b\3\2\2\2\u013c\u013d\3\2\2\2\u013d\u013e\3\2"
            + "\2\2\u013e\u014a\7#\2\2\u013f\u0140\6\26\17\3\u0140\u0141\7$\2\2\u0141" + "\u0142\5*\26\2\u0142\u0143\7%\2\2\u0143\u014a\3\2\2\2\u0144\u0145\6\26"
            + "\20\3\u0145\u014a\t\4\2\2\u0146\u0147\6\26\21\3\u0147\u0148\7@\2\2\u0148" + "\u014a\5\62\32\2\u0149\u0109\3\2\2\2\u0149\u010c\3\2\2\2\u0149\u010f\3"
            + "\2\2\2\u0149\u0119\3\2\2\2\u0149\u011c\3\2\2\2\u0149\u011f\3\2\2\2\u0149" + "\u0122\3\2\2\2\u0149\u0125\3\2\2\2\u0149\u0128\3\2\2\2\u0149\u012b\3\2"
            + "\2\2\u0149\u012e\3\2\2\2\u0149\u0134\3\2\2\2\u0149\u0137\3\2\2\2\u0149" + "\u013f\3\2\2\2\u0149\u0144\3\2\2\2\u0149\u0146\3\2\2\2\u014a\u014d\3\2"
            + "\2\2\u014b\u0149\3\2\2\2\u014b\u014c\3\2\2\2\u014c+\3\2\2\2\u014d\u014b" + "\3\2\2\2\u014e\u014f\t\n\2\2\u014f-\3\2\2\2\u0150\u0155\5*\26\2\u0151"
            + "\u0152\7C\2\2\u0152\u0154\5*\26\2\u0153\u0151\3\2\2\2\u0154\u0157\3\2" + "\2\2\u0155\u0153\3\2\2\2\u0155\u0156\3\2\2\2\u0156/\3\2\2\2\u0157\u0155"
            + "\3\2\2\2\u0158\u0159\5*\26\2\u0159\u015a\7D\2\2\u015a\u0162\5*\26\2\u015b" + "\u015c\7C\2\2\u015c\u015d\5*\26\2\u015d\u015e\7D\2\2\u015e\u015f\5*\26"
            + "\2\u015f\u0161\3\2\2\2\u0160\u015b\3\2\2\2\u0161\u0164\3\2\2\2\u0162\u0160" + "\3\2\2\2\u0162\u0163\3\2\2\2\u0163\61\3\2\2\2\u0164\u0162\3\2\2\2\u0165"
            + "\u016a\7H\2\2\u0166\u0167\7)\2\2\u0167\u0169\7H\2\2\u0168\u0166\3\2\2" + "\2\u0169\u016c\3\2\2\2\u016a\u0168\3\2\2\2\u016a\u016b\3\2\2\2\u016b\u016e"
            + "\3\2\2\2\u016c\u016a\3\2\2\2\u016d\u016f\5\66\34\2\u016e\u016d\3\2\2\2" + "\u016e\u016f\3\2\2\2\u016f\u0173\3\2\2\2\u0170\u0172\5\64\33\2\u0171\u0170"
            + "\3\2\2\2\u0172\u0175\3\2\2\2\u0173\u0171\3\2\2\2\u0173\u0174\3\2\2\2\u0174" + "\63\3\2\2\2\u0175\u0173\3\2\2\2\u0176\u0177\7$\2\2\u0177\u0178\7%\2\2"
            + "\u0178\65\3\2\2\2\u0179\u017a\7.\2\2\u017a\u017b\58\35\2\u017b\u017c\7" + "-\2\2\u017c\67\3\2\2\2\u017d\u0182\5:\36\2\u017e\u017f\7C\2\2\u017f\u0181"
            + "\5:\36\2\u0180\u017e\3\2\2\2\u0181\u0184\3\2\2\2\u0182\u0180\3\2\2\2\u0182" + "\u0183\3\2\2\2\u01839\3\2\2\2\u0184\u0182\3\2\2\2\u0185\u0188\5\62\32"
            + "\2\u0186\u0188\7B\2\2\u0187\u0185\3\2\2\2\u0187\u0186\3\2\2\2\u0188;\3" + "\2\2\2&ACP\\drx\u0084\u0090\u0094\u00a5\u00aa\u00b2\u00b6\u00ba\u00be"
            + "\u00c2\u00c6\u00d1\u00e7\u00ec\u00f2\u00f9\u0105\u0107\u0116\u013c\u0149" + "\u014b\u0155\u0162\u016a\u016e\u0173\u0182\u0187";
    public static final ATN _ATN = ATNSimulator.deserialize(_serializedATN.toCharArray());
    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}
