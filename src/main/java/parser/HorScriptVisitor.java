package parser;

import cn.hutool.core.util.NumberUtil;
import domain.ValueModel;
import exception.VisitorException;
import core.HorScriptLexer;
import core.HorScriptParser.*;
import core.HorScriptParserBaseVisitor;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HorScriptVisitor extends HorScriptParserBaseVisitor<ValueModel> {

    private Scope scope;
    private Map<String, Function> functions;

    private static ReturnValue returnValue = new ReturnValue();

    public HorScriptVisitor(Scope scope, Map<String, Function> functions) {
        this.scope = scope;
        this.functions = new HashMap<>(functions);
    }

    // visitFunctionDecl 函数声明
    @Override
    public ValueModel visitFunctionDecl(FunctionDeclContext ctx) {
        List<TerminalNode> params = ctx.idList() != null ? ctx.idList().IDENTIFIER() : new ArrayList<>();
        ParseTree block = ctx.blockSet();
        String id = ctx.IDENTIFIER().getText() + params.size();
        // 函数已经定义抛出异常
        if (functions.get(id) != null) {
            throw newParseException(ctx.start, "函数不可以重新定义");
        }
        functions.put(id, new Function(scope, params, block));
        return ValueModel.VOID;
    }

    // 函数调用 functionCall: IDENTIFIER LBT exprList? RBT #identifierFunctionCall; // xx()
    @Override
    public ValueModel visitIdentifierFunctionCall(IdentifierFunctionCallContext ctx) {
        List<ExprContext> params = ctx.exprList() != null ? ctx.exprList().expr() : new ArrayList<>();
        String id = ctx.IDENTIFIER().getText() + params.size();
        Function function;
        if ((function = functions.get(id)) != null) {
            List<ValueModel> args = new ArrayList<>(params.size());
            for (ExprContext param: params) {
                args.add(this.visit(param));
            }
            return function.invoke(args, functions);
        }
        throw newParseException(ctx.start,ctx.getText());
    }

    // 语句块
    // blockSet    : ( statement | functionDecl )* ( RETURN expr (SEM)? )?;
    @Override
    public ValueModel visitBlockSet(BlockSetContext ctx) {
        scope = new Scope(scope, false); // create new local scope
        for (FunctionDeclContext fdx: ctx.functionDecl()) {
            this.visit(fdx);
        }
        for (StatementContext sx: ctx.statement()) {
            this.visit(sx);
        }
        ExprContext ex;
        if ((ex = ctx.expr()) != null) {
            returnValue.value = this.visit(ex);
            scope = scope.parent();
            throw returnValue;
        }
        scope = scope.parent();
        return ValueModel.VOID;
    }

    // assignment SEM? // 赋值 xx = xx;
    @Override
    public ValueModel visitAssignment(AssignmentContext ctx) {
        PrimitiveValueContext primitiveValueContext = ctx.anyObject().primitiveValue();
        ExprContext exprContext = ctx.anyObject().expr();
        ValueModel vm = new ValueModel();
        if (primitiveValueContext != null) {
            vm.setValue(this.visit(primitiveValueContext).asOv());
        }
        if (exprContext !=null) {
            vm.setValue(this.visit(exprContext).asOv());
        }
        String id = ctx.IDENTIFIER().getText();
        scope.assign(id, vm);
        return ValueModel.VOID;
    }

    @Override
    public ValueModel visitNoAssignment(NoAssignmentContext ctx) {
        PrimitiveValueContext primitiveValueContext = ctx.anyObject().primitiveValue();
        ExprContext exprContext = ctx.anyObject().expr();
        ValueModel newVal = new ValueModel();
        if (primitiveValueContext != null) {
            newVal.setValue(this.visit(primitiveValueContext).asOv());
        }
        if (exprContext !=null) {
            newVal.setValue(this.visit(exprContext).asOv());
        }
        if (ctx.indexes() != null) {
            ValueModel val = scope.resolve(ctx.IDENTIFIER().getText());
            List<ExprContext> exps = ctx.indexes().expr();
            setAtIndex(ctx, exps, val, newVal);
        }else {
            String id = ctx.IDENTIFIER().getText();
            scope.assign(id, newVal);
        }
        return ValueModel.VOID;
    }

    // IDENTIFIER indexes?                                        #identifierExpr
    @Override
    public ValueModel visitIdentifierExpr(IdentifierExprContext ctx) {
        String id = ctx.IDENTIFIER().getText();
        ValueModel val = scope.resolve(id);
        if (ctx.indexes() != null) {
            List<ExprContext> exps = ctx.indexes().expr();
            val = resolveIndexes(val, exps);
        }
        return val;
    }

    // 系统函数打印
    @Override
    public ValueModel visitPrintFunctionCall(PrintFunctionCallContext ctx) {
        if (ctx.exprList() != null) {
            StringBuffer sBuffer = new StringBuffer();
            // 参数列表
            for (ExprContext ex : ctx.exprList().expr()) {
                ValueModel content = this.visit(ex);
                if (content.isBoolean()) {
                    content.setValue(content.asBoolean() ? '真' : '假');
                }
                if (content.isNull()) {
                    content.setValue('空');
                }
                sBuffer.append(content.asString());
            }
            System.out.println(sBuffer);
        }
        System.out.println();
        return ValueModel.VOID;
    }


    // STRING indexes?                 #stringValue    // 字符串 or 字符串[0]
    @Override
    public ValueModel visitStringValue(StringValueContext ctx) {
        String text = fixString(ctx.STRING());
        ValueModel val = new ValueModel(text);
        if (ctx.indexes() != null) {
            List<ExprContext> exps = ctx.indexes().expr();
            val = resolveIndexes(val, exps);
        }
        return val;
    }

    // NUMBER                          #numberValue    // 数值类型
    @Override
    public ValueModel visitNumberValue(NumberValueContext ctx) {
        ValueModel vm = new ValueModel();
        TerminalNode intNode = ctx.INTEGER_NUM();
        TerminalNode decimalNode = ctx.DECIMAL_NUM();
        String radixNumber = null;
        if (intNode != null) {
            radixNumber = intNode.getText();
        }
        if (radixNumber != null) {
            Long bigInt = new Long(radixNumber);
            return vm.setValue(bigInt);
        } else {
            BigDecimal bigDec = new BigDecimal(decimalNode.getText());
            int precisionLength = bigDec.precision();
            if (precisionLength < 8 && !Float.isInfinite(bigDec.floatValue())) {
                return vm.setValue(bigDec.floatValue());
            }
            if (precisionLength < 16 && !Double.isInfinite(bigDec.doubleValue())) {
                return vm.setValue(bigDec.doubleValue());
            }
            return vm.setValue(bigDec);
        }
    }

    //  ( TRUE | FALSE)                 #booleanValue   // boolean 类型
    @Override
    public ValueModel visitBooleanValue(BooleanValueContext ctx) {
        boolean boolValue = ctx.TRUE() != null;
        return new ValueModel(boolValue);
    }

    // null
    @Override
    public ValueModel visitNullValue(NullValueContext ctx) {
        return ValueModel.NULL;
    }

    // list: '[' exprList? ']'
    @Override
    public ValueModel visitList(ListContext ctx) {
        List<ValueModel> list = new ArrayList<>();
        if (ctx.exprList() != null) {
            for(ExprContext ex: ctx.exprList().expr()) {
                list.add(this.visit(ex));
            }
        }
        return new ValueModel(list);
    }

    // list indexes?                   #listValue      // 列表类型
    @Override
    public ValueModel visitListValue(ListValueContext ctx) {
        ValueModel val = this.visit(ctx.list());
        if (ctx.indexes() != null) {
            List<ExprContext> exps = ctx.indexes().expr();
            val = resolveIndexes(val, exps);
        }
        return val;
    }


    // 如果 否则如果 否则
    @Override
    public ValueModel visitIfStatement(IfStatementContext ctx) {
        // if ...
        if(this.visit(ctx.ifStat().expr()).asBoolean()) {
            return this.visit(ctx.ifStat().blockSet());
        }
        // else if ...
        for(int i = 0; i < ctx.elseIfStat().size(); i++) {
            if(this.visit(ctx.elseIfStat(i).expr()).asBoolean()) {
                return this.visit(ctx.elseIfStat(i).blockSet());
            }
        }
        // else ...
        if(ctx.elseStat() != null) {
            return this.visit(ctx.elseStat().blockSet());
        }
        return ValueModel.VOID;
    }

    //forStatement for循环
    @Override
    public ValueModel visitForStatement(ForStatementContext ctx) {
        ValueModel lhs,rhs;
        if (ctx.expr(1) == null) {
            rhs  = this.visit(ctx.expr(0));
            lhs = new ValueModel(0);
        }else {
            lhs = this.visit(ctx.expr(0));
            rhs = this.visit(ctx.expr(1));
        }

        switch (ctx.op.getType()) {
            case HorScriptLexer.IN:
                try {
                    if(rhs.isList()) {
                        for(ValueModel val : rhs.asList()) {
                            scope.assign(ctx.IDENTIFIER().getText(), new ValueModel(val.asT()));
                            ValueModel returnValue = this.visit(ctx.blockSet());
                            if(returnValue != ValueModel.VOID) {
                                return returnValue;
                            }
                        }
                    }
                }
                catch (NullPointerException npe) {
                    throw newParseException(ctx.start, "错误的可迭代对象: " + ctx.getText());

                }
                return ValueModel.VOID;
            case HorScriptLexer.TO:
                int start = lhs.asInt();
                int stop = rhs.asInt();
                for(int i = start; i <= stop; i++) {
                    scope.assign(ctx.IDENTIFIER().getText(), new ValueModel(i));
                    ValueModel returnValue = this.visit(ctx.blockSet());
                    if(returnValue != ValueModel.VOID) {
                        return returnValue;
                    }
                }
                return ValueModel.VOID;
            default:
                throw newParseException(ctx.start, "未知运算符类型: " + ctx.op.getType());
        }

    }

    //whileStatement 判断循环
    @Override
    public ValueModel visitWhileStatement(WhileStatementContext ctx) {
        while( this.visit(ctx.expr()).asBoolean() ) {
            ValueModel returnValue = this.visit(ctx.blockSet());
            if (returnValue == ValueModel.VOID) {
                return returnValue;
            }
        }
        return ValueModel.VOID;
    }

    // LBT expr RBT indexes?                                      #exprExpr  // 优先级
    @Override
    public ValueModel visitExprExpr(ExprExprContext ctx) {
        ValueModel val = this.visit(ctx.expr());
        if (ctx.indexes() != null) {
            List<ExprContext> exps = ctx.indexes().expr();
            val = resolveIndexes(val, exps);
        }
        return val;
    }



    // prefix=( PLUS | MINUS | NOT) expr                          #unaryExpr // 一元运算 + - !
    @Override
    public ValueModel visitUnaryExpr(UnaryExprContext ctx) {
        ValueModel vm = this.visit(ctx.expr());
        switch (ctx.prefix.getType()) {
            case HorScriptLexer.PLUS:
                return vm;
            case HorScriptLexer.MINUS:
                if (!vm.isNumber()) {
                    throw newParseException(ctx.start,"类型错误: " + ctx.getText());
                }
                return vm.setValue(NumberUtil.mul("-1",vm.asString()));
            case HorScriptLexer.NOT:
                if (vm.isNumber()) {
                    vm.setValue(vm.asInt() > 0);
                }
                if(!vm.isBoolean()) {
                    throw newParseException(ctx.start,"类型错误: " + ctx.getText());
                }
                return vm.setValue(!vm.asBoolean());
        }
        return ValueModel.VOID;
    }

    // expr op=( MUL | DIV | MOD ) expr                    #dyadicExpr_A // 二元运算 优先级1st  * / %
    @Override
    public ValueModel visitDyadicExpr_A(DyadicExpr_AContext ctx) {
        switch (ctx.op.getType()) {
            case HorScriptLexer.MUL:
                return mul(ctx);
            case HorScriptLexer.DIV:
                return div(ctx);
            case HorScriptLexer.MOD:
                return mod(ctx);
        }
        return ValueModel.VOID;
    }




    // expr op=( PLUS | MINUS) expr          #dyadicExpr_B   // 二元运算优先级 2st + -
    @Override
    public ValueModel visitDyadicExpr_B(DyadicExpr_BContext ctx) {
        switch (ctx.op.getType()) {
            case HorScriptLexer.PLUS:
                return plus(ctx);
            case HorScriptLexer.MINUS:
                return minus(ctx);
        }
        return ValueModel.VOID;
    }


    // expr op=( AND | OR | XOR | L_SHIFT | R_SHIFT | R_SHIFT2) expr #dyadicExpr_C   // 二元运算优先级
    @Override
    public ValueModel visitDyadicExpr_C(DyadicExpr_CContext ctx) {
        switch (ctx.op.getType()) {
            case HorScriptLexer.AND:
                return and(ctx);
            case HorScriptLexer.OR:
                return or(ctx);
            case HorScriptLexer.XOR:
                return xor(ctx);
            case HorScriptLexer.LSHIFT:
                return l_shift(ctx);
            case HorScriptLexer.RSHIFT:
                return r_shift(ctx);
            case HorScriptLexer.RSHIFT2:
                return r_shift2(ctx);
        }
        return ValueModel.VOID;
    }


    // expr op=( GTEquals | LTEquals | GT | LT ) expr             #dyadicExpr_D // 4st '>=' | '<=' | '>' | '<'
    @Override
    public ValueModel visitDyadicExpr_D(DyadicExpr_DContext ctx) {
        switch (ctx.op.getType()) {
            case HorScriptLexer.GTEquals:
                return gt_equals(ctx);
            case HorScriptLexer.LTEquals:
                return lt_equals(ctx);
            case HorScriptLexer.GT:
                return gt(ctx);
            case HorScriptLexer.LT:
                return lt(ctx);
        }
        return ValueModel.VOID;
    }


    //  expr op=( EQ | NE ) expr                                   #dyadicExpr_E // 5st '=='  '!='
    @Override
    public ValueModel visitDyadicExpr_E(DyadicExpr_EContext ctx) {
        switch (ctx.op.getType()) {
            case HorScriptLexer.EQ:
                return eq(ctx);
            case HorScriptLexer.NE:
                return neq(ctx);
        }
        return ValueModel.VOID;
    }

    // expr op=( SC_OR | SC_AND) expr                             #dyadicExpr_F  // 6st || &&
    @Override
    public ValueModel visitDyadicExpr_F(DyadicExpr_FContext ctx) {
        switch (ctx.op.getType()) {
            case HorScriptLexer.SC_OR:
                return sc_or(ctx);
            case HorScriptLexer.SC_AND:
                return sc_and(ctx);
        }
        return ValueModel.VOID;
    }


    // expr QMark expr COLON expr                                 #ternaryExpr    // 三元运算
    @Override
    public ValueModel visitTernaryExpr(TernaryExprContext ctx) {
        ValueModel condition = this.visit(ctx.expr(0));
        if (condition.asBoolean()) {
            return this.visit(ctx.expr(1));
        } else {
            return this.visit(ctx.expr(2));
        }
    }


    // expr IN expr                                               #inExpr // 在 true false
    @Override
    public ValueModel visitInExpr(InExprContext ctx) {
        ValueModel lhs = this.visit(ctx.expr(0));
        ValueModel rhs = this.visit(ctx.expr(1));

        if (rhs.isList()) {
            for(ValueModel val: rhs.asList()) {
                if (val.equals(lhs)) {
                    return new ValueModel(true);
                }
            }
            return new ValueModel(false);
        }
        throw newParseException(ctx.start,"类型错误: " + ctx.getText() + " 解决方案：应为列表类型");

    }

    private ValueModel plus(DyadicExpr_BContext ctx) {
        ValueModel lhs = this.visit(ctx.expr(0));
        ValueModel rhs = this.visit(ctx.expr(1));
        if (lhs.isNull() || rhs.isNull()) {
            throw newParseException(ctx.start, "空指针异常");
        }
        ValueModel res = new ValueModel();

        // number + number
        if (lhs.isNumber() && rhs.isNumber()) {
            return res.setValue(NumberUtil.add(lhs.asString(), rhs.asString()));
        }

        // list + any
        if (lhs.isList()) {
            List<ValueModel> list = lhs.asList();
            list.add(rhs);
            return res.setValue(list);
        }
        // string + any
        return res.setValue(lhs.asString() + rhs.asString());
    }

    private ValueModel minus(DyadicExpr_BContext ctx) {
        ValueModel lhs = this.visit(ctx.expr(0));
        ValueModel rhs = this.visit(ctx.expr(1));
        if (lhs.isNull() || rhs.isNull()) {
            throw newParseException(ctx.start, "空指针异常");
        }
        ValueModel res = new ValueModel();

        // number - number
        if (lhs.isNumber() && rhs.isNumber()) {
            return res.setValue(NumberUtil.sub(lhs.asString(), rhs.asString()));
        }

        // list + any
        if (lhs.isList()) {
            List<ValueModel> list = lhs.asList();
            list.remove(rhs);
            return res.setValue(list);
        }
        throw newParseException(ctx.start, "非法表达式: " + ctx.getText());
    }

    private ValueModel mul(DyadicExpr_AContext ctx) {
        ValueModel lhs = this.visit(ctx.expr(0));
        ValueModel rhs = this.visit(ctx.expr(1));
        if (lhs.isNull() || rhs.isNull()) {
            throw newParseException(ctx.start, "空指针异常");
        }
        ValueModel vm = new ValueModel();

        // number * number
        if(lhs.isNumber() && rhs.isNumber()) {
            return vm.setValue(NumberUtil.mul(lhs.asString(),rhs.asString()));
        }

        // string * number
        if(lhs.isString() && rhs.isNumber()) {
            StringBuilder str = new StringBuilder();
            int stop = rhs.asDouble().intValue();
            for(int i = 0; i < stop; i++) {
                str.append(lhs.asString());
            }
            return vm.setValue(str.toString());
        }

        // list * number
        if(lhs.isList() && rhs.isNumber()) {
            List<ValueModel> total = new ArrayList<>();
            int stop = rhs.asDouble().intValue();
            for(int i = 0; i < stop; i++) {
                total.addAll(lhs.asList());
            }
            return vm.setValue(total);
        }
        throw newParseException(ctx.start, "非法表达式: " + ctx.getText());
    }


    private ValueModel div(DyadicExpr_AContext ctx) {
        ValueModel lhs = this.visit(ctx.expr(0));
        ValueModel rhs = this.visit(ctx.expr(1));

        if (lhs.isNumber() && rhs.isNumber()) {
            return new ValueModel(NumberUtil.div(lhs.asString(),rhs.asString()));
        }
        throw newParseException(ctx.start, "非法表达式: " + ctx.getText());
    }

    private ValueModel mod(DyadicExpr_AContext ctx) {
        ValueModel lhs = this.visit(ctx.expr(0));
        ValueModel rhs = this.visit(ctx.expr(1));
        if (lhs.isNumber() && rhs.isNumber()) {
            return new ValueModel(lhs.asLong() % rhs.asLong());
        }
        throw newParseException(ctx.start, "非法表达式: " + ctx.getText());
    }

    // &
    private ValueModel and(DyadicExpr_CContext ctx) {
        ValueModel lhs = this.visit(ctx.expr(0));
        ValueModel rhs = this.visit(ctx.expr(1));
        if (lhs.isNumber() && rhs.isNumber()) {
            return new ValueModel(lhs.asLong() & rhs.asLong());
        }
        throw newParseException(ctx.start, "非法表达式: " + ctx.getText());
    }

    // |
    private ValueModel or(DyadicExpr_CContext ctx) {
        ValueModel lhs = this.visit(ctx.expr(0));
        ValueModel rhs = this.visit(ctx.expr(1));
        if (lhs.isNumber() && rhs.isNumber()) {
            return new ValueModel(lhs.asLong() | rhs.asLong());
        }
        throw newParseException(ctx.start, "非法表达式: " + ctx.getText());
    }

    // ^
    private ValueModel xor(DyadicExpr_CContext ctx) {
        ValueModel lhs = this.visit(ctx.expr(0));
        ValueModel rhs = this.visit(ctx.expr(1));
        if (lhs.isNumber() && rhs.isNumber()) {
            return new ValueModel(lhs.asLong() ^ rhs.asLong());
        }
        throw newParseException(ctx.start, "非法表达式: " + ctx.getText());
    }


    // <<
    private ValueModel l_shift(DyadicExpr_CContext ctx) {
        ValueModel lhs = this.visit(ctx.expr(0));
        ValueModel rhs = this.visit(ctx.expr(1));
        if (lhs.isNumber() && rhs.isNumber()) {
            return new ValueModel(lhs.asLong() << rhs.asLong());
        }
        throw newParseException(ctx.start, "非法表达式: " + ctx.getText());
    }


    // >>
    private ValueModel r_shift(DyadicExpr_CContext ctx) {
        ValueModel lhs = this.visit(ctx.expr(0));
        ValueModel rhs = this.visit(ctx.expr(1));
        if (lhs.isNumber() && rhs.isNumber()) {
            return new ValueModel(lhs.asLong() >> rhs.asLong());
        }
        throw newParseException(ctx.start, "非法表达式: " + ctx.getText());
    }

    private ValueModel r_shift2(DyadicExpr_CContext ctx) {
        ValueModel lhs = this.visit(ctx.expr(0));
        ValueModel rhs = this.visit(ctx.expr(1));
        if (lhs.isNumber() && rhs.isNumber()) {
            return new ValueModel(lhs.asLong() >>> rhs.asLong());
        }
        throw newParseException(ctx.start, "非法表达式: " + ctx.getText());
    }


    // >=
    private ValueModel gt_equals(DyadicExpr_DContext ctx) {
        ValueModel lhs = this.visit(ctx.expr(0));
        ValueModel rhs = this.visit(ctx.expr(1));
        if (lhs.isNumber() && rhs.isNumber()) {
            return new ValueModel(lhs.asDouble() >= rhs.asDouble());
        }
        if(lhs.isString() && rhs.isString()) {
            return new ValueModel(lhs.asString().compareTo(rhs.asString()) >= 0);
        }
        throw newParseException(ctx.start, "非法表达式: " + ctx.getText());
    }

    // <=
    private ValueModel lt_equals(DyadicExpr_DContext ctx) {
        ValueModel lhs = this.visit(ctx.expr(0));
        ValueModel rhs = this.visit(ctx.expr(1));
        if (lhs.isNumber() && rhs.isNumber()) {
            return new ValueModel(lhs.asDouble() <= rhs.asDouble());
        }
        if(lhs.isString() && rhs.isString()) {
            return new ValueModel(lhs.asString().compareTo(rhs.asString()) <= 0);
        }
        throw newParseException(ctx.start, "非法表达式: " + ctx.getText());
    }


    // >
    private ValueModel gt(DyadicExpr_DContext ctx) {
        ValueModel lhs = this.visit(ctx.expr(0));
        ValueModel rhs = this.visit(ctx.expr(1));
        if (lhs.isNumber() && rhs.isNumber()) {
            return new ValueModel(lhs.asDouble() > rhs.asDouble());
        }
        if(lhs.isString() && rhs.isString()) {
            return new ValueModel(lhs.asString().compareTo(rhs.asString()) > 0);
        }
        throw newParseException(ctx.start, "非法表达式: " + ctx.getText());
    }

    // <
    private ValueModel lt(DyadicExpr_DContext ctx) {
        ValueModel lhs = this.visit(ctx.expr(0));
        ValueModel rhs = this.visit(ctx.expr(1));
        if (lhs.isNumber() && rhs.isNumber()) {
            return new ValueModel(lhs.asDouble() < rhs.asDouble());
        }
        if(lhs.isString() && rhs.isString()) {
            return new ValueModel(lhs.asString().compareTo(rhs.asString()) < 0);
        }
        throw newParseException(ctx.start, "非法表达式: " + ctx.getText());
    }


    // !=
    private ValueModel neq(DyadicExpr_EContext ctx) {
        ValueModel lhs = this.visit(ctx.expr(0));
        ValueModel rhs = this.visit(ctx.expr(1));
        if (lhs.isNull() || rhs.isNull()) {
            throw newParseException(ctx.start, "空指针异常");
        }
        return new ValueModel(!lhs.equals(rhs));
    }

    // ==
    private ValueModel eq(DyadicExpr_EContext ctx) {
        ValueModel lhs = this.visit(ctx.expr(0));
        ValueModel rhs = this.visit(ctx.expr(1));
        if (lhs.isNull() || rhs.isNull()) {
            throw newParseException(ctx.start, "空指针异常");
        }
        return new ValueModel(lhs.equals(rhs));
    }



    // ||
    private ValueModel sc_or(DyadicExpr_FContext ctx) {
        ValueModel lhs = this.visit(ctx.expr(0));
        ValueModel rhs = this.visit(ctx.expr(1));

        if (lhs.isNumber() && lhs.asLong() != 0) {
            return new ValueModel(true);
        }

        if (rhs.isNumber()) {
            rhs.setValue(false);
        }

        if(!lhs.isBoolean() || !rhs.isBoolean()) {
            throw newParseException(ctx.start, "非法表达式: " + ctx.getText());
        }
        return new ValueModel(lhs.asBoolean() || rhs.asBoolean());
    }
    // &&
    private ValueModel sc_and(DyadicExpr_FContext ctx) {
        ValueModel lhs = this.visit(ctx.expr(0));
        ValueModel rhs = this.visit(ctx.expr(1));
        if (lhs.isNumber() && lhs.asLong() == 0) {
            return new ValueModel(false);
        }else {
            lhs.setValue(true);
        }

        if (rhs.isNumber()) {
            rhs.setValue(rhs.asLong() != 0);
        }

        if(!lhs.isBoolean() || !rhs.isBoolean()) {
            throw newParseException(ctx.start, "非法表达式: " + ctx.getText());
        }
        return new ValueModel(lhs.asBoolean() && rhs.asBoolean());
    }

    private ValueModel resolveIndexes(ValueModel val, List<ExprContext> indexes) {
        for (ExprContext ec : indexes) {
            ValueModel idx = this.visit(ec);
            if (!idx.isNumber() || (!val.isList() && !val.isString())) {
                throw newParseException(ec.start, "非法表达式: " + ec.getText());
            }
            int i = idx.asInt();
            if (val.isString()) {
                if (val.asString().length() - 1 < i) {
                    throw newParseException(ec.start, "字符串索引超出范围：" + i + " 解决方案：索引 <= " + (i - 1));
                }
                val = new ValueModel(val.asString().substring(i, i + 1));
            } else {
                val = val.asList().get(i);
            }
        }
        return val;
    }

    private void setAtIndex(ParserRuleContext ctx, List<ExprContext> indexes, ValueModel val, ValueModel newVal) {
        if (!val.isList()) {
            throw newParseException(ctx.start,"类型错误");
        }
        for (int i = 0; i < indexes.size() - 1; i++) {
            ValueModel idx = this.visit(indexes.get(i));
            if (!idx.isNumber()) {
                throw newParseException(ctx.start,"类型错误");
            }
            val = val.asList().get(idx.asDouble().intValue());
        }
        ValueModel idx = this.visit(indexes.get(indexes.size() - 1));
        if (!idx.isNumber()) {
            throw newParseException(ctx.start,"类型错误");
        }
        val.asList().set(idx.asDouble().intValue(), newVal);
    }

    private VisitorException newParseException(Token token, String errorMessage) {
        return new VisitorException(token.getLine(), token.getStartIndex(), errorMessage);
    }

    /* 获取String */
    private String fixString(TerminalNode stringNode) {
        String nodeText = stringNode.getText();
        return nodeText.substring(1, nodeText.length() - 1);
    }

}
