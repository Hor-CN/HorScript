lexer grammar HorScriptLexer;

/* key words 关键字*/
VAR     : '赋值';
IMPORT  : '导入';
AS      : '为';
ASSERT  : '断言';
DEF     : '函数';
RETURN  : '返回';
IF      : '如果';
ELSE    : '否则';
FOR     : '循环';
WHILE   : '判断循环';
TO      : '到';
IN      : '在';
NULL    : '空';
PRINT   : '打印';
INPUT   : '输入';
SIZE    : '长度';
TRUE    : '真';
FALSE   : '假';


/* arithmetic operators 算数运算 */
PLUS    : '+';      // 加法
MINUS   : '-';      // 减法
MUL     : '*';      // 乘法
DIV     : '/';      // 除法
MOD     : '%';      // 取摸
LBT     : '(';      // 优先级
RBT     : ')';      // 优先级
AND     : '&';      // 按位于运算
OR      : '|';      // 按位或运算
NOT     : '!';      // 按位取反
XOR     : '^';      // 异或
LSHIFT  : '<<';     // 左位移
RSHIFT  : '>>';     // 有符号右位移
RSHIFT2 : '>>>';    // 无符号右位移

/* logic operators 逻辑运算 */
GT      : '>';      // 大于
LT      : '<';      // 小于
GTEquals: '>=';     // 大于等于
LTEquals: '<=';     // 小于等于
EQ      : '==';     // 等于
NE      : '!=';     // 不等于
SC_OR   : '||';     // 逻辑或
SC_AND  : '&&';     // 逻辑与

/* assist words 连接符在某些特定场景下使用 */
ROU     : '@';
COMMA   : ',';      // 参数\分割项
COLON   : ':';      //
ASS     : '=';      // 赋值
DOT     : '.';      //
LSBT    : '[';      // 数组 or 下标
RSBT    : ']';      // 数组 or 下标
OCBR    : '{';      // 表示为一个对象
CCBR    : '}';      // 表示为一个对象
QMark   : '?';      // ?
SEM     : ';';      // ;
LAMBDA  : '->';     // ->
/* 字符串 */
STRING
 : ["] ( ~["\r\n\\] | '\\' ~[\r\n] )* ["]
 | ['] ( ~['\r\n\\] | '\\' ~[\r\n] )* [']
 ;

/* 数字 */
INTEGER_NUM     : INT;                  // 整型
DECIMAL_NUM     : INT ( '.' DIGIT* )?;  // 浮点数

fragment INT : [1-9] DIGIT* | '0';
fragment DIGIT: [0-9];


/* 标识符 */
IDENTIFIER      : [a-zA-Z_] [a-zA-Z_0-9]*;

/* skip spaces */
Space   : [ \t\n\r\u000C] -> skip; // (空格\水平制表符\换行\回车\换页)
Comment : ( '//' ~[\r\n]* | '/*' .*? '*/' ) -> skip; // 注释
EOL     : [\n\r\f]; // 结束
