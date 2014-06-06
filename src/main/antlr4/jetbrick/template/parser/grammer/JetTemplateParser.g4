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
parser grammar JetTemplateParser;

options {
    tokenVocab = JetTemplateLexer; // use tokens from JetTemplateLexer.g4
}

/*
@header {
package jetbrick.template.parser.grammer;
}
*/

// -------- rule ---------------------------------------
template    :   block EOF
            ;

block       :   (text | value | directive)*
            ;

text        :   TEXT_PLAIN
            |   TEXT_CDATA
            |   TEXT_SINGLE_CHAR
            |   TEXT_ESCAPED_CHAR
            |   TEXT_DIRECTIVE_LIKE
            ;

value       :   VALUE_OPEN         expression '}'
            |   VALUE_ESCAPED_OPEN expression '}'
            ;

directive   :   define_directive
            |   set_directive
            |   put_directive
            |   if_directive
            |   for_directive
            |   break_directive
            |   continue_directive
            |   stop_directive
            |   include_directive
            |   tag_directive
            |   macro_directive
            |   invalid_directive
            ;

define_directive
            :   DIRECTIVE_OPEN_DEFINE define_expression_list ')'
            ;
define_expression_list
            :   define_expression (',' define_expression)*
            ;
define_expression
            :   type IDENTIFIER
            ;

set_directive
            :   DIRECTIVE_OPEN_SET set_expression (',' set_expression)* ')'
            ;
set_expression
            :   type? IDENTIFIER '=' expression
            ;

put_directive
            :   DIRECTIVE_OPEN_PUT expression (',' expression)* ')'
            ;

if_directive
            :   DIRECTIVE_OPEN_IF expression ')' block elseif_directive* else_directive? DIRECTIVE_END
            ;
elseif_directive
            :   DIRECTIVE_OPEN_ELSEIF expression ')' block
            ;
else_directive
            :   DIRECTIVE_ELSE block
            ;

for_directive
            :   DIRECTIVE_OPEN_FOR for_expression ')' block else_directive? DIRECTIVE_END
            ;
for_expression
            :   type? IDENTIFIER ':' expression
            ;

break_directive
            :   DIRECTIVE_OPEN_BREAK expression? ')'
            |   DIRECTIVE_BREAK
            ;
continue_directive
            :   DIRECTIVE_OPEN_CONTINUE expression? ')'
            |   DIRECTIVE_CONTINUE
            ;
stop_directive
            :   DIRECTIVE_OPEN_STOP expression? ')'
            |   DIRECTIVE_STOP
            ;

include_directive
            :   DIRECTIVE_OPEN_INCLUDE expression_list ')'
            ;

tag_directive
            :   DIRECTIVE_OPEN_TAG expression_list? ')' block DIRECTIVE_END
            ;

macro_directive
            :   DIRECTIVE_OPEN_MACRO define_expression_list? ')' block DIRECTIVE_END
            ;

invalid_directive
            :   DIRECTIVE_DEFINE
            |   DIRECTIVE_SET
            |   DIRECTIVE_PUT
            |   DIRECTIVE_IF
            |   DIRECTIVE_ELSEIF
            |   DIRECTIVE_FOR
            |   DIRECTIVE_INCLUDE
            |   DIRECTIVE_TAG
            |   DIRECTIVE_MACRO
            ;

expression  :   '(' expression ')'                                           # expr_group
            |   constant                                                     # expr_constant
            |   IDENTIFIER                                                   # expr_identifier
            |   '[' expression_list? ']'                                     # expr_array_list
            |   '{' hash_map_entry_list? '}'                                 # expr_hash_map
            |   expression ('.'|'?.') IDENTIFIER                             # expr_field_access
            |   expression ('.'|'?.') IDENTIFIER '(' expression_list? ')'    # expr_method_invocation
            |   IDENTIFIER '(' expression_list? ')'                          # expr_function_call
            |   static_type_name '.' IDENTIFIER                              # expr_static_field_access
            |   static_type_name '.' IDENTIFIER  '(' expression_list? ')'    # expr_static_method_invocation
            |   expression ('?')? '[' expression ']'                         # expr_array_get
            |   expression ('++'|'--')                                       # expr_math_unary_suffix
            |   ('+' |'-' )     expression                                   # expr_math_unary_prefix
            |   ('++'|'--')     expression                                   # expr_math_unary_prefix
            |   '~'             expression                                   # expr_math_unary_prefix
            |   '!'             expression                                   # expr_compare_not
            |   '(' type ')'    expression                                   # expr_class_cast
            |   'new' type '('  expression_list? ')'                         # expr_new_object
            |   'new' type ('[' expression ']')+                             # expr_new_array
            |   expression ('*'|'/'|'%')  expression                         # expr_math_binary_basic
            |   expression ('+'|'-')      expression                         # expr_math_binary_basic
            |   expression ('<<'|'>' '>'|'>' '>' '>') expression             # expr_math_binary_shift
            |   expression ('>='|'<='|'>'|'<') expression                    # expr_compare_relational
            |   expression OP_INSTANCEOF type                                # expr_instanceof
            |   expression ('=='|'!=') expression                            # expr_compare_equality
            |   expression '&'  expression                                   # expr_math_binary_bitwise
            |   <assoc=right> expression '^' expression                      # expr_math_binary_bitwise
            |   expression '|'  expression                                   # expr_math_binary_bitwise
            |   expression '&&' expression                                   # expr_compare_condition
            |   expression '||' expression                                   # expr_compare_condition
            |   <assoc=right> expression '?' expression ':' expression       # expr_conditional_ternary
            ;

constant    :   STRING_DOUBLE
            |   STRING_SINGLE
            |   INTEGER
            |   INTEGER_HEX
            |   FLOATING_POINT
            |   KEYWORD_TRUE
            |   KEYWORD_FALSE
            |   KEYWORD_NULL
            ;

expression_list
            :   expression (',' expression)*
            ;

hash_map_entry_list
            :   expression ':' expression (',' expression ':' expression)*
            ;

static_type_name
            : '@' IDENTIFIER
            | '@' '(' IDENTIFIER ('.' IDENTIFIER)* ')'
            ;

type        :   IDENTIFIER ('.' IDENTIFIER)* type_arguments? type_array_suffix*
            ;

type_array_suffix
            :   '[' ']'
            ;

type_arguments
            :   '<' type_list '>'
            ;
type_list   :   type_name (',' type_name)*
            ;

type_name   :   type | '?'
            ;



