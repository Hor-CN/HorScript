// Define a grammar called Hello
parser grammar HorScriptParser;
options { tokenVocab = HorScriptLexer; }
/* 语句 & 命令 */

/* 入口 */
rootInstSet : importInst* blockSet EOF;

/* import 指令 */
importInst  : IMPORT ROU? STRING AS IDENTIFIER SEM?; // 导入 @'xxx.cn.itbk.horscript.core' as xx;

/* 语句块 */
blockSet    : ( statement | functionDecl )* ( RETURN expr (SEM)? )?;

statement
 : assignment SEM? // 赋值 xx = xx;
 | noAssignment SEM? // xx[] = xx;
 | functionCall SEM? // 函数调用
 | systemFunction SEM? // 系统函数
 | ifStatement
 | forStatement
 | whileStatement
 ;

// 参数列表
idList     : IDENTIFIER ( COMMA IDENTIFIER )*; // a,b,c
exprList   : expr ( COMMA expr )*;
/* 索引 */
indexes    : ( LSBT expr RSBT )+; // [1][]

/* 基本类型 */
primitiveValue : NULL                            #nullValue      // 空值
               | ( TRUE | FALSE)                 #booleanValue   // 布尔类型
               | ( INTEGER_NUM | DECIMAL_NUM )   #numberValue    // 数值类型
               | STRING indexes?                 #stringValue    // 字符类型
               | list indexes?                   #listValue      // 列表类型
//               | object indexes?                 #objectValue    // 对象
               ;

anyObject :  primitiveValue | objectValue | lambdaDef | functionCall | expr;

/* 对象结构 */
objectValue     : OCBR objectKeyValue? ( COMMA objectKeyValue)* CCBR;
objectKeyValue  : IDENTIFIER ( COLON anyObject)?;

/* 列表结构 */
list       : LSBT exprList? RSBT ;

/* 表达式 */
expr:  (primitiveValue | functionCall indexes? )                 #atomExpr
    | LBT expr RBT indexes?                                      #exprExpr  // 优先级
    | prefix=( PLUS | MINUS | NOT) expr                          #unaryExpr // 一元运算 + - !
    | expr op=( MUL | DIV | MOD ) expr                           #dyadicExpr_A // 二元运算 优先级1st  * /  %
    | expr op=( PLUS | MINUS) expr                               #dyadicExpr_B   // 二元运算优先级 2st + -
    | expr op=( AND | OR | XOR | LSHIFT | RSHIFT | RSHIFT2) expr #dyadicExpr_C   // 二元运算优先级 3st
    | expr op=( GTEquals | LTEquals | GT | LT ) expr             #dyadicExpr_D // 4st '>=' | '<=' | '>' | '<'
    | expr op=( EQ | NE ) expr                                   #dyadicExpr_E // 5st '=='  '!='
    | expr op=( SC_OR | SC_AND) expr                             #dyadicExpr_F  // 6st || &&
    | expr QMark expr COLON expr                                 #ternaryExpr    // 三元运算
    | expr IN expr                                               #inExpr // 是否在xx true false
    | IDENTIFIER indexes?                                        #identifierExpr
    ;

/* 赋值 */
assignment : VAR IDENTIFIER ASS anyObject;
noAssignment: IDENTIFIER indexes? ASS anyObject;

/* 判断 */
ifStatement: ifStat elseIfStat* elseStat?;
ifStat    : IF LBT expr RBT OCBR blockSet CCBR;
elseIfStat: ELSE IF LBT expr RBT OCBR blockSet CCBR;
elseStat: ELSE OCBR blockSet CCBR;

/* 循环 */
forStatement: FOR LBT IDENTIFIER (ASS expr)? op=(TO|IN) expr RBT OCBR blockSet CCBR;

/* 判断循环 */
whileStatement: WHILE LBT expr RBT OCBR blockSet CCBR;

/* lambda函数声明 */
lambdaDef       : LBT (IDENTIFIER (COMMA IDENTIFIER)*)? RBT LAMBDA OCBR blockSet CCBR;

/* 函数 */
functionDecl: DEF IDENTIFIER LBT idList? RBT OCBR blockSet CCBR; // 函数 xx() {}
/* 函数调用 */
functionCall: IDENTIFIER LBT exprList? RBT #identifierFunctionCall; // xx()
/* 内置函数 */
systemFunction: PRINT LBT exprList RBT     #printFunctionCall
              | ASSERT LBT expr RBT    #assertFunctionCall
              | SIZE LBT anyObject RBT      #sizeFunctionCall
              | INPUT LBT STRING? RBT     #inputFunctionCall
              ;