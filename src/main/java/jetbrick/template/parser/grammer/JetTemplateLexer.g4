lexer grammar JetTemplateLexer;

@header {
package jetbrick.template.parser.grammer;
}

// *******************************************************************
// ------- DEFAULT mode for Plain Text -------------------------------
COMMENT_LINE            : '#--' .*? '--#'                 -> skip ;
COMMENT_BLOCK           : '##' ~[\r\n]* NEWLINE           -> skip ;
fragment NEWLINE        : ('\r'? '\n' | EOF)              ;

TEXT_PLAIN              : ~('$'|'#'|'\\')+                ;
TEXT_CDATA              : '#[[' .*? ']]#'                 ;
TEXT_ESCAPED_CHAR       : ('\\#'|'\\$'|'\\\\')            ;
TEXT_SINGLE_CHAR        : ('#'|'$'|'\\')                  ;

VALUE_OPEN              : '${'                            -> pushMode(INSIDE) ;
VALUE_ESCAPED_OPEN      : '$!{'                           -> pushMode(INSIDE) ;

DIRECTIVE_OPEN_DEFINE   : '#define'   ARGUMENT_START      -> pushMode(INSIDE) ;
DIRECTIVE_OPEN_SET      : '#set'      ARGUMENT_START      -> pushMode(INSIDE) ;
DIRECTIVE_OPEN_PUT      : '#put'      ARGUMENT_START      -> pushMode(INSIDE) ;
DIRECTIVE_OPEN_IF       : '#if'       ARGUMENT_START      -> pushMode(INSIDE) ;
DIRECTIVE_OPEN_ELSEIF   : '#elseif'   ARGUMENT_START      -> pushMode(INSIDE) ;
DIRECTIVE_OPEN_FOR      : '#for'      ARGUMENT_START      -> pushMode(INSIDE) ;
DIRECTIVE_OPEN_BREAK    : '#break'    ARGUMENT_START      -> pushMode(INSIDE) ;
DIRECTIVE_OPEN_CONTINUE : '#continue' ARGUMENT_START      -> pushMode(INSIDE) ;
DIRECTIVE_OPEN_STOP     : '#stop'     ARGUMENT_START      -> pushMode(INSIDE) ;
DIRECTIVE_OPEN_INCLUDE  : '#include'  ARGUMENT_START      -> pushMode(INSIDE) ;
DIRECTIVE_OPEN_DEBUG    : '#debug'    ARGUMENT_START      -> pushMode(INSIDE) ;

DIRECTIVE_DEFINE        : '#define'                       ;
DIRECTIVE_SET           : '#set'                          ;
DIRECTIVE_PUT           : '#put'                          ;
DIRECTIVE_IF            : '#if'                           ;
DIRECTIVE_ELSEIF        : '#elseif'                       ;
DIRECTIVE_FOR           : '#for'                          ;
DIRECTIVE_INCLUDE       : '#include'                      ;
DIRECTIVE_BREAK         : '#break'                        ;
DIRECTIVE_CONTINUE      : '#continue'                     ;
DIRECTIVE_STOP          : '#stop'                         ;
DIRECTIVE_DEBUG         : '#debug'                        ;

DIRECTIVE_ELSE          : '#else'     ARGUMENT_EMPTY?     ;
DIRECTIVE_END           : '#end'      ARGUMENT_EMPTY?     ;

fragment ARGUMENT_START :  [ \t]* '(';
fragment ARGUMENT_EMPTY :  [ \t]* '()';

// *******************************************************************
// -------- INSIDE mode for directive --------------------------------
mode INSIDE;

WHITESPACE              : [ \t\r\n]+                       -> skip ;

LEFT_PARENTHESE         : '('                              -> pushMode(INSIDE) ;
RIGHT_PARENTHESE        : ')'                              -> popMode ;
LEFT_BRACKET            : '['                              ;
RIGHT_BRACKET           : ']'                              ;
LEFT_BRACE              : '{'                              -> pushMode(INSIDE) ;
RIGHT_BRACE             : '}'                              -> popMode ;

OP_ASSIGNMENT           : '='                              ;

OP_DOT_INVOCATION       : '.'                              ;
OP_DOT_INVOCATION_SAFE  : '?.'                             ;

OP_EQUALITY_EQ          : '=='                             ;
OP_EQUALITY_NE          : '!='                             ;

OP_RELATIONAL_GT        : '>'                              ;
OP_RELATIONAL_LT        : '<'                              ;
OP_RELATIONAL_GE        : '>='                             ;
OP_RELATIONAL_LE        : '<='                             ;

OP_CONDITIONAL_AND      : '&&'                             ;
OP_CONDITIONAL_OR       : '||'                             ;
OP_CONDITIONAL_NOT      : '!'                              ;

OP_MATH_PLUS            : '+'                              ;
OP_MATH_MINUS           : '-'                              ;
OP_MATH_MULTIPLICATION  : '*'                              ;
OP_MATH_DIVISION        : '/'                              ;
OP_MATH_REMAINDER       : '%'                              ;
OP_MATH_INCREMENT       : '++'                             ;
OP_MATH_DECREMENT       : '--'                             ;

OP_BITWISE_AND          : '&'                              ;
OP_BITWISE_OR           : '|'                              ;
OP_BITWISE_NOT          : '~'                              ;
OP_BITWISE_XOR          : '^'                              ;
OP_BITWISE_SHL          : '<<'                             ;

// Following operators are conflict with Java Generic Type definition.
// Sample: List<List<String>>
//OP_BITWISE_SHR        : '>>'                           ;
//OP_BITWISE_SHR_2      : '>>>'                          ;

OP_INSTANCEOF           : 'instanceof' | 'is'              ;
OP_NEW                  : 'new'                            ;
OP_CONDITIONAL_TERNARY  : '?'                              ;

COMMA                   : ','                              ;
COLON                   : ':'                              ;

KEYWORD_TRUE            : 'true'                           ;
KEYWORD_FALSE           : 'false'                          ;
KEYWORD_NULL            : 'null'                           ;

IDENTIFIER              : [_a-zA-Z$][_a-zA-Z$0-9]*         ;

INTEGER                 : INT [lLfFdD]?                    ;
INTEGER_HEX             : '0x' HEX+ [lL]?                  ;
FLOATING_POINT          : INT ('.' INT)? EXP? [fFdD]?      ;
fragment INT            : '0' | [1-9] [0-9]*               ;
fragment EXP            : [Ee] [+\-]? INT                  ;

STRING_DOUBLE           : '"'  (ESC|.)*? '"'               ;
STRING_SINGLE           : '\'' (ESC|.)*? '\''              ;
fragment ESC            : '\\' ([btnfr"'\\]|UNICODE)       ;
fragment UNICODE        : 'u' HEX HEX HEX HEX              ;
fragment HEX            : [0-9a-fA-F]                      ;

