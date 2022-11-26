package parser;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.NumberUtil;
import core.HorScriptLexer;
import core.HorScriptParser.*;
import core.HorScriptParserBaseVisitor;
import domain.DataModel;
import domain.ListModel;
import domain.ObjectModel;
import domain.ValueModel;
import exception.VisitorException;
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

    // 作用域
    private Scope scope;
    // 方法
    private final Map<String, Function> functions;
    private static final ReturnValue returnValue = new ReturnValue();

    public HorScriptVisitor(Scope scope, Map<String, Function> functions) {
        this.scope = scope;
        this.functions = new HashMap<>(functions);
    }

    // 语句块
    // blockSet    : ( statement | functionDecl )* ( RETURN expr (SEM)? )?;
    @Override
    public ValueModel visitBlockSet(BlockSetContext ctx) {
        scope = new Scope(scope, false); // create new local scope
        for (FunctionDeclContext fdx : ctx.functionDecl()) {
            this.visit(fdx);
        }
        for (StatementContext sx : ctx.statement()) {
            this.visit(sx);
        }
        AnyObjectContext ex;
        if ((ex = ctx.anyObject()) != null) {
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
        LambdaDefContext lambdaDefContext = ctx.anyObject().lambdaDef();
        if (lambdaDefContext != null) {
            Function function = (Function) this.visit(lambdaDefContext).asOv();
            String id = ctx.IDENTIFIER().getText() + function.getParams().size();
            // 函数已经定义抛出异常
            if (functions.get(id) != null) {
                throw newParseException(ctx.start, "函数不可以重新定义");
            }
            functions.put(id, function);
            return ValueModel.VOID;
        }
        String id = ctx.IDENTIFIER().getText();
        scope.assign(id, this.visit(ctx.anyObject()));
        return ValueModel.VOID;
    }

    // 无前缀赋值
    @Override
    public ValueModel visitNoAssignment(NoAssignmentContext ctx) {

        LambdaDefContext lambdaDefContext = ctx.anyObject().lambdaDef();
        if (lambdaDefContext != null) {
            Function function = (Function) this.visit(lambdaDefContext).asOv();
            String id = ctx.IDENTIFIER().getText() + function.getParams().size();
            // 函数已经定义抛出异常
            if (functions.get(id) != null) {
                throw newParseException(ctx.start, "函数不可以重新定义");
            }
            functions.put(id, function);
            return ValueModel.VOID;
        }
        ValueModel newVal = this.visit(ctx.anyObject());
        if (ctx.indexes() != null) {
            ValueModel val = scope.resolve(ctx.IDENTIFIER().getText());
            List<ExprContext> exps = ctx.indexes().expr();
            setAtIndex(ctx, exps, val, newVal);
        } else {
            String id = ctx.IDENTIFIER().getText();
            scope.assign(id, newVal);
        }
        return ValueModel.VOID;
    }

    // Lambda 函数声明 (a,b) => {}
    @Override
    public ValueModel visitLambdaDef(LambdaDefContext ctx) {
        // 参数列表
        List<TerminalNode> params = ctx.idList() != null ? ctx.idList().IDENTIFIER() : new ArrayList<>();
        // 语句块
        ParseTree block = ctx.blockSet();
        Function function = new Function(scope, params, block);
        return new ValueModel(function);
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

    // 函数调用 functionCall: IDENTIFIER LBT exprList? RBT functionCallResult?  // xx()
    @Override
    public ValueModel visitIdentifierFunctionCall(IdentifierFunctionCallContext ctx) {
        List<ExprContext> params = ctx.exprList() != null ? ctx.exprList().expr() : new ArrayList<>();
        String id = ctx.IDENTIFIER().getText() + params.size();
        Function function;
        if ((function = functions.get(id)) != null) {
            List<ValueModel> args = new ArrayList<>(params.size());
            for (ExprContext param : params) {
                args.add(this.visit(param));
            }
            ValueModel val = function.invoke(args, functions);
            if (val.isString() || val.isList() || val.isObjectModel()) {
                // funcCallResult_route1
                FunctionCallResultContext functionCallResultContext = ctx.functionCallResult();
                if (functionCallResultContext != null) {
                    ValueModel visit = this.visit(functionCallResultContext);
                    List<ExprContext> exps = Convert.convert(new TypeReference<List<ExprContext>>() {}, visit.asOv());
                    val = resolveIndexes(val, exps);
                    return val;
                }
            }
            return function.invoke(args, functions);
        }
        throw newParseException(ctx.start, ctx.getText());
    }

    @Override
    public ValueModel visitFuncCallResult_route1(FuncCallResult_route1Context ctx) {
        ctx.accept(this);
        return super.visitFuncCallResult_route1(ctx);
    }

    @Override
    public ValueModel visitFuncCallResult_route2(FuncCallResult_route2Context ctx) {
        if (ctx.indexes() != null) {
            List<ExprContext> exps = ctx.indexes().expr();
            return new ValueModel(exps);
        }

        return super.visitFuncCallResult_route2(ctx);
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

//    @Override
//    public ValueModel visitNameExprRoute(NameExprRouteContext ctx) {
//        List<RouteNameContext> routeNameContexts = ctx.routeNameSet().routeName();
////        List<RouteNameContext> restRoutNames =routeNameContexts.subList(1,routeNameContexts.size());
//        String id = routeNameContexts.get(0).IDENTIFIER().getText();
//        Map<String, DataModel> val = scope.resolve(id).asObjectModel().asOv();
//        BeanPath resolver = new BeanPath("b.c");
//        Object result = resolver.get(val);//ID为1
//        System.out.println(result);
////        for (RouteNameContext routeNameContext: restRoutNames) {
////            if (!val.fieldNames().contains(routeNameContext.IDENTIFIER().getText())) {
////                System.out.println("错误");
////            }
////            if (val.get(routeNameContext.IDENTIFIER().getText()).isObject()) {
////                System.out.println(val.get(routeNameContext.IDENTIFIER().getText()));
////            }
////
////        }
//        return ValueModel.VOID;
//    }

    @Override
    public ValueModel visitObjectValue(ObjectValueContext ctx) {
        ObjectModel objectModel = new ObjectModel();
        for (ObjectKeyValueContext objectKeyValueContext : ctx.objectKeyValue()) {
            String key = objectKeyValueContext.IDENTIFIER().getText();
            AnyObjectContext value = objectKeyValueContext.anyObject();
            ObjectValueContext objectValueContext = objectKeyValueContext.anyObject().objectValue();
            if (objectValueContext != null) {
                ValueModel valueModel = visitObjectValue(objectValueContext);
                objectModel.put(key, valueModel);
            }
            objectModel.put(key, this.visit(value));
        }
        return new ValueModel(objectModel);
    }

    // listValue  : LSBT anyObject? (COMMA anyObject)* RSBT;
    @Override
    public ValueModel visitListValue(ListValueContext ctx) {
        ListModel listModel = new ListModel();
        if (ctx.anyObject() != null) {
            for (AnyObjectContext ex : ctx.anyObject()) {
                listModel.add(this.visit(ex));
            }
        }
        return new ValueModel(listModel);
    }

    // listValue indexes?                        #listExprRoute // 列表路由
    @Override
    public ValueModel visitListRoute(ListRouteContext ctx) {
        ValueModel val = this.visit(ctx.listValue());
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
        if (this.visit(ctx.ifStat().expr()).asBoolean()) {
            return this.visit(ctx.ifStat().blockSet());
        }
        // else if ...
        for (int i = 0; i < ctx.elseIfStat().size(); i++) {
            if (this.visit(ctx.elseIfStat(i).expr()).asBoolean()) {
                return this.visit(ctx.elseIfStat(i).blockSet());
            }
        }
        // else ...
        if (ctx.elseStat() != null) {
            return this.visit(ctx.elseStat().blockSet());
        }
        return ValueModel.VOID;
    }

    //forStatement for循环
    @Override
    public ValueModel visitForStatement(ForStatementContext ctx) {
        ValueModel lhs, rhs;
        if (ctx.anyObject(1) == null) {
            rhs = this.visit(ctx.anyObject(0));
            lhs = new ValueModel(0);
        } else {
            lhs = this.visit(ctx.anyObject(0));
            rhs = this.visit(ctx.anyObject(1));
        }

        switch (ctx.op.getType()) {
            case HorScriptLexer.IN:
                try {
                    if (rhs.isList()) {
                        for (DataModel val : rhs.asList().asOv()) {
                            scope.assign(ctx.IDENTIFIER().getText(), new ValueModel(val.asT()));
                            ValueModel returnValue = this.visit(ctx.blockSet());
                            if (returnValue != ValueModel.VOID) {
                                return returnValue;
                            }
                        }
                    }
                } catch (NullPointerException npe) {
                    throw newParseException(ctx.start, "错误的可迭代对象: " + ctx.getText());

                }
                return ValueModel.VOID;
            case HorScriptLexer.TO:
                int start = lhs.asInt();
                int stop = rhs.asInt();
                for (int i = start; i <= stop; i++) {
                    scope.assign(ctx.IDENTIFIER().getText(), new ValueModel(i));
                    ValueModel returnValue = this.visit(ctx.blockSet());
                    if (returnValue != ValueModel.VOID) {
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
        while (this.visit(ctx.expr()).asBoolean()) {
            ValueModel returnValue = this.visit(ctx.blockSet());
            if (returnValue != ValueModel.VOID) {
                return returnValue;
            }
        }
        return ValueModel.VOID;
    }

    //whileStatement do while判断循环
    @Override
    public ValueModel visitDoWhileStatement(DoWhileStatementContext ctx) {
        try {
            do {
                ValueModel returnValue = this.visit(ctx.blockSet());
                if (returnValue != ValueModel.VOID) {
                    return returnValue;
                }
            } while (this.visit(ctx.expr()).asBoolean());
        } catch (NullPointerException e) {
            throw newParseException(ctx.start, "空指针异常: " + ctx.expr().getText());
        }
        return ValueModel.VOID;
    }


    // 系统函数打印
    @Override
    public ValueModel visitPrintFunctionCall(PrintFunctionCallContext ctx) {
        if (ctx.exprList() != null) {
            System.out.print(printStr(ctx.exprList().expr()));
        } else System.out.println();
        return ValueModel.VOID;
    }

    @Override
    public ValueModel visitPrintlnFunctionCall(PrintlnFunctionCallContext ctx) {
        if (ctx.exprList() != null) {
            System.out.println(printStr(ctx.exprList().expr()));
        } else System.out.println();
        return ValueModel.VOID;
    }

    private String printStr(List<ExprContext> ctx) {
        StringBuilder sBuffer = new StringBuilder();
        // 参数列表
        for (ExprContext ex : ctx) {
            ValueModel content = this.visit(ex);
            if (content.isBoolean()) {
                content.setValue(content.asBoolean() ? '真' : '假');
            }
            if (content.isNull()) {
                content.setValue('空');
            }
            sBuffer.append(content.asString());
        }
        return sBuffer.toString();
    }

    // 字符串类型
    @Override
    public ValueModel visitStringValue(StringValueContext ctx) {
        String text = fixString(ctx.STRING());
        return new ValueModel(text);
    }

    // STRING indexes?                 // 字符串 or 字符串[0]
    @Override
    public ValueModel visitStringRoute(StringRouteContext ctx) {
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


    // LBT expr RBT                                   #privilegeExpr  // 优先级
    @Override
    public ValueModel visitPrivilegeExpr(PrivilegeExprContext ctx) {
        return this.visit(ctx.expr());
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
                    throw newParseException(ctx.start, "类型错误: " + ctx.getText());
                }
                return vm.setValue(NumberUtil.mul("-1", vm.asString()));
            case HorScriptLexer.NOT:
                if (vm.isNumber()) {
                    vm.setValue(vm.asInt() > 0);
                }
                if (!vm.isBoolean()) {
                    throw newParseException(ctx.start, "类型错误: " + ctx.getText());
                }
                return vm.setValue(!vm.asBoolean());
        }
        return ValueModel.VOID;
    }

    // expr op=( MUL | DIV | MOD ) expr                    #dyadicExpr_A // 二元运算 优先级1st  * / %
    @Override
    public ValueModel visitDyadicExpr_A(DyadicExpr_AContext ctx) {
        try {
            switch (ctx.op.getType()) {
                case HorScriptLexer.MUL:
                    return mul(ctx);
                case HorScriptLexer.DIV:
                    return div(ctx);
                case HorScriptLexer.MOD:
                    return mod(ctx);
            }
        } catch (NullPointerException e) {
            throw newParseException(ctx.start, "空指针异常: " + ctx.start.getText());
        }
        return ValueModel.VOID;
    }

    // expr op=( PLUS | MINUS) expr          #dyadicExpr_B   // 二元运算优先级 2st + -
    @Override
    public ValueModel visitDyadicExpr_B(DyadicExpr_BContext ctx) {
        try {
            switch (ctx.op.getType()) {
                case HorScriptLexer.PLUS:
                    return plus(ctx);
                case HorScriptLexer.MINUS:
                    return minus(ctx);
            }
        } catch (NullPointerException e) {
            throw newParseException(ctx.start, "空指针异常: " + ctx.start.getText());
        }
        return ValueModel.VOID;
    }

    // expr op=( AND | OR | XOR | L_SHIFT | R_SHIFT | R_SHIFT2) expr #dyadicExpr_C   // 二元运算优先级
    @Override
    public ValueModel visitDyadicExpr_C(DyadicExpr_CContext ctx) {
        try {
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
        } catch (NullPointerException e) {
            throw newParseException(ctx.start, "空指针异常: " + ctx.start.getText());
        }
        return ValueModel.VOID;
    }


    // expr op=( GTEquals | LTEquals | GT | LT ) expr             #dyadicExpr_D // 4st '>=' | '<=' | '>' | '<'
    @Override
    public ValueModel visitDyadicExpr_D(DyadicExpr_DContext ctx) {
        try {
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
        } catch (NullPointerException e) {
            throw newParseException(ctx.start, "空指针异常: " + ctx.start.getText());
        }
        return ValueModel.VOID;
    }


    //  expr op=( EQ | NE ) expr                                   #dyadicExpr_E // 5st '=='  '!='
    @Override
    public ValueModel visitDyadicExpr_E(DyadicExpr_EContext ctx) {
        try {
            switch (ctx.op.getType()) {
                case HorScriptLexer.EQ:
                    return eq(ctx);
                case HorScriptLexer.NE:
                    return neq(ctx);
            }
        } catch (NullPointerException e) {
            throw newParseException(ctx.start, "空指针异常: " + ctx.start.getText());
        }
        return ValueModel.VOID;
    }

    // expr op=( SC_OR | SC_AND) expr                             #dyadicExpr_F  // 6st || &&
    @Override
    public ValueModel visitDyadicExpr_F(DyadicExpr_FContext ctx) {
        try {
            switch (ctx.op.getType()) {
                case HorScriptLexer.SC_OR:
                    return sc_or(ctx);
                case HorScriptLexer.SC_AND:
                    return sc_and(ctx);
            }
        } catch (NullPointerException e) {
            throw newParseException(ctx.start, "空指针异常: " + ctx.start.getText());
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
            for (DataModel val : rhs.asList().asOv()) {
                if (val.equals(lhs)) {
                    return new ValueModel(true);
                }
            }
            return new ValueModel(false);
        }
        throw newParseException(ctx.start, "类型错误: " + ctx.getText() + " 解决方案：应为列表类型");

    }

    private ValueModel plus(DyadicExpr_BContext ctx) {
        ValueModel lhs = this.visit(ctx.expr(0));
        ValueModel rhs = this.visit(ctx.expr(1));

        ValueModel res = new ValueModel();

        // number + number
        if (lhs.isNumber() && rhs.isNumber()) {
            return res.setValue(NumberUtil.add(lhs.asString(), rhs.asString()));
        }

        // list + any
        if (lhs.isList()) {
            ListModel list = lhs.asList();
            list.add(rhs);
            return res.setValue(list);
        }
        // string + any
        return res.setValue(lhs.asString() + rhs.asString());

    }

    private ValueModel minus(DyadicExpr_BContext ctx) {
        ValueModel lhs = this.visit(ctx.expr(0));
        ValueModel rhs = this.visit(ctx.expr(1));

        ValueModel res = new ValueModel();

        // number - number
        if (lhs.isNumber() && rhs.isNumber()) {
            return res.setValue(NumberUtil.sub(lhs.asString(), rhs.asString()));
        }

        // list - any
        if (lhs.isList()) {
            ListModel list = lhs.asList();
            list.remove(rhs);
            return res.setValue(list);
        }

        throw newParseException(ctx.start, "非法表达式: " + ctx.getText());
    }

    private ValueModel mul(DyadicExpr_AContext ctx) {
        ValueModel lhs = this.visit(ctx.expr(0));
        ValueModel rhs = this.visit(ctx.expr(1));

        ValueModel vm = new ValueModel();

        // number * number
        if (lhs.isNumber() && rhs.isNumber()) {
            return vm.setValue(NumberUtil.mul(lhs.asString(), rhs.asString()));
        }

        // string * number
        if (lhs.isString() && rhs.isNumber()) {
            StringBuilder str = new StringBuilder();
            int stop = rhs.asDouble().intValue();
            for (int i = 0; i < stop; i++) {
                str.append(lhs.asString());
            }
            return vm.setValue(str.toString());
        }

        // list * number
        if (lhs.isList() && rhs.isNumber()) {
            ListModel total = new ListModel();
            int stop = rhs.asDouble().intValue();
            for (int i = 0; i < stop; i++) {
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
            return new ValueModel(NumberUtil.div(lhs.asString(), rhs.asString()));
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
        if (lhs.isString() && rhs.isString()) {
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
        if (lhs.isString() && rhs.isString()) {
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
        if (lhs.isString() && rhs.isString()) {
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
        if (lhs.isString() && rhs.isString()) {
            return new ValueModel(lhs.asString().compareTo(rhs.asString()) < 0);
        }

        throw newParseException(ctx.start, "非法表达式: " + ctx.getText());
    }

    // !=
    private ValueModel neq(DyadicExpr_EContext ctx) {
        ValueModel lhs = this.visit(ctx.expr(0));
        ValueModel rhs = this.visit(ctx.expr(1));

        return new ValueModel(!lhs.equals(rhs));

    }

    // ==
    private ValueModel eq(DyadicExpr_EContext ctx) {
        ValueModel lhs = this.visit(ctx.expr(0));
        ValueModel rhs = this.visit(ctx.expr(1));

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

        if (!lhs.isBoolean() || !rhs.isBoolean()) {
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
        } else {
            lhs.setValue(true);
        }

        if (rhs.isNumber()) {
            rhs.setValue(rhs.asLong() != 0);
        }

        if (!lhs.isBoolean() || !rhs.isBoolean()) {
            throw newParseException(ctx.start, "非法表达式: " + ctx.getText());
        }

        return new ValueModel(lhs.asBoolean() && rhs.asBoolean());
    }

    private ValueModel resolveIndexes(ValueModel val, List<ExprContext> indexes) {
        for (ExprContext ec : indexes) {
            ValueModel idx = this.visit(ec);
            int i = idx.asInt();
            if (!idx.isNumber() || (!val.isList() && !val.isString())) {
                throw newParseException(ec.start, "非法表达式: " + ec.getText());
            }
            if (val.isObjectModel()) {
                val = new ValueModel(val.asObjectModel().get(idx.asString()));
                return val;
            }
            if (val.isString()) {
                if (val.asString().length() - 1 < i) {
                    throw newParseException(ec.start, "字符串索引超出范围：" + i + " 解决方案：索引 <= " + (i - 1));
                }
                val = new ValueModel(val.asString().substring(i, i + 1));
            } else {
                val = val.asList().getValue(i);
            }
        }
        return val;
    }

    private void setAtIndex(ParserRuleContext ctx, List<ExprContext> indexes, ValueModel val, ValueModel newVal) {
        if (!val.isList()) {
            throw newParseException(ctx.start, "类型错误");
        }
        for (int i = 0; i < indexes.size() - 1; i++) {
            ValueModel idx = this.visit(indexes.get(i));
            if (!idx.isNumber()) {
                throw newParseException(ctx.start, "类型错误");
            }
            val = val.asList().getValue(idx.asDouble().intValue());
        }
        ValueModel idx = this.visit(indexes.get(indexes.size() - 1));
        if (!idx.isNumber()) {
            throw newParseException(ctx.start, "类型错误");
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
