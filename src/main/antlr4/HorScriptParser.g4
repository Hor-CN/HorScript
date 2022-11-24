// Define a grammar called Hello
parser grammar HorScriptParser;
options { tokenVocab = HorScriptLexer; }

/* 入口 */
rootInstSet : importInst* blockSet EOF;

/* import 指令 */
importInst  : IMPORT ROU? STRING AS IDENTIFIER SEM?; // 导入 @'xxx.cn.itbk.horscript.core' as xx;

/* 语句块 */
blockSet    : ( statement | functionDecl )* ( RETURN expr (SEM)? )?;

statement
 : assignment SEM?
 | noAssignment SEM?
 | functionCall SEM?
 | systemFunction SEM?
 | ifStatement
 | forStatement
 | whileStatement
 | doWhileStatement SEM?
 ;

/* 基本类型 */
primitiveValue : NULL                            #nullValue      // 空值
               | ( TRUE | FALSE)                 #booleanValue   // 布尔类型
               | ( INTEGER_NUM | DECIMAL_NUM )   #numberValue    // 数值类型
               | STRING                          #stringValue    // 字符串类型
               ;

// 任意对象
anyObject :  primitiveValue | listValue | objectValue | lambdaDef | functionCall | routerMapping | expr;

/* 对象结构 */
objectValue     : OCBR objectKeyValue? ( COMMA objectKeyValue)* CCBR;
objectKeyValue  : IDENTIFIER ( COLON anyObject)?;

/* 赋值 */
assignment : VAR IDENTIFIER ASS anyObject;
noAssignment: IDENTIFIER indexes? ASS anyObject;

// 函数参数列表
idList     : IDENTIFIER ( COMMA IDENTIFIER )*; // a,b,c
exprList   : expr ( COMMA expr )*;

/* 列表结构 */
listValue  : LSBT exprList? RSBT;

/* 路由映射 */
routerMapping : IDENTIFIER indexes?                       #identifierExpr
//              | routeNameSet                              #nameExprRoute // 表达式（访问符 -> 子元素）
              | listValue indexes?                        #listRoute     // 列表路由
              | STRING indexes?                           #stringRoute   // 字符串路由
              ;

/* 路由的规则 */
routeNameSet    : routeName (DOT routeName)* ;
/* 路由名 */
routeName       : IDENTIFIER indexes? ;
/* 索引 */
indexes    : ( LSBT expr RSBT )+; // [1][]

/* 表达式 */
expr:  (primitiveValue | functionCall indexes? | routerMapping)  #atomExpr
    | LBT expr RBT                                               #privilegeExpr    // 优先
    | prefix=( PLUS | MINUS | NOT) expr                          #unaryExpr // 一元运算 + - !
    | expr op=( MUL | DIV | MOD ) expr                           #dyadicExpr_A // 二元运算 优先级1st  * /  %
    | expr op=( PLUS | MINUS) expr                               #dyadicExpr_B   // 二元运算优先级 2st + -
    | expr op=( AND | OR | XOR | LSHIFT | RSHIFT | RSHIFT2) expr #dyadicExpr_C   // 二元运算优先级 3st
    | expr op=( GTEquals | LTEquals | GT | LT ) expr             #dyadicExpr_D // 4st '>=' | '<=' | '>' | '<'
    | expr op=( EQ | NE ) expr                                   #dyadicExpr_E // 5st '=='  '!='
    | expr op=( SC_OR | SC_AND) expr                             #dyadicExpr_F  // 6st || &&
    | expr QMark expr COLON expr                                 #ternaryExpr    // 三元运算
    | expr IN expr                                               #inExpr // 是否在xx true false
    ;

/* 判断 */
ifStatement: ifStat elseIfStat* elseStat?;
ifStat    : IF LBT expr RBT OCBR blockSet CCBR;
elseIfStat: ELSE IF LBT expr RBT OCBR blockSet CCBR;
elseStat: ELSE OCBR blockSet CCBR;

/* 循环 */
forStatement: FOR LBT IDENTIFIER (ASS anyObject)? op=(TO|IN) anyObject RBT OCBR blockSet CCBR;
/* 判断循环 */
whileStatement: WHILE LBT expr RBT OCBR blockSet CCBR;
doWhileStatement: DO OCBR blockSet CCBR WHILE LBT expr RBT;

/* lambda函数声明 */
lambdaDef       : LBT idList? RBT LAMBDA OCBR blockSet CCBR;
/* 函数 */
functionDecl: DEF IDENTIFIER LBT idList? RBT OCBR blockSet CCBR; // 函数 xx() {}
/* 函数调用 */
functionCall: IDENTIFIER LBT exprList? RBT #identifierFunctionCall; // xx()
/* 内置函数 */
systemFunction: PRINT LBT exprList? RBT     #printFunctionCall
              | PRINTLN LBT exprList? RBT   #printlnFunctionCall
              | ASSERT LBT expr RBT        #assertFunctionCall
              | SIZE LBT anyObject RBT     #sizeFunctionCall
              | INPUT LBT STRING? RBT      #inputFunctionCall
              ;