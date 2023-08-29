package cn.itbk.horscript.parser;

import cn.itbk.horscript.domain.*;
import java.io.File;

import cn.itbk.horscript.natives.HorNatives;
import cn.itbk.horscript.utils.OperatorUtil;
import cn.itbk.horscript.utils.StringUtil;
import cn.itbk.horscript.utils.ListUtil;
import java.util.List;
import cn.itbk.horscript.core.HorScriptLexer;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.ArrayDeque;
import cn.itbk.horscript.core.HorScriptParser.*;
import cn.itbk.horscript.exception.VisitorException;
import org.antlr.v4.runtime.Token;
import cn.itbk.horscript.core.HorScriptParserBaseVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

public class HorScriptVisitor extends HorScriptParserBaseVisitor<ValueModel> {

    /**
     * 作用域
     */
    private Scope scope;
    /**
     * 返回值
     */
    private static final ReturnModel RETURN_MODEL = new ReturnModel();

    public HorScriptVisitor(Scope scope) {
        this.scope = scope;
    }

    /** 导入
     * @param ctx the parse tree
     * @return ValueModel
     */
    @Override
    public ValueModel visitImportInst(ImportInstContext ctx) {
        // 别名
        VariableModel var = new VariableModel(ctx.IDENTIFIER().getText());
        // 导入模块名
        String importPath = StringUtil.sub(ctx.STRING().getText(),1,-1);

        // 使用 @ 符号将可导入内置模块
        // @      if (ctx.ROU() != null) { // 导入 @'网络模块' 为 网络;
        /*         导入 @'系统模块' 为 系统;
         *          赋值 time = 系统.当前时间()
         */
        //            System.out.println("内部包模块");
        //        }

        if (ctx.ROU() != null) { // 导入 @'网络模块' 为 网络;
            ValueModel importNative =  HorNatives.getInstance().importNative(importPath);
            if (importNative != null){
                scope.globalAssign(var, importNative);
            } else {
                throw parseException(ctx.start, importPath + " 无法找到模块");
            }

            return ValueModel.VOID;
        }

        // 其他导入HS脚本文件
        File importFile = new File(importPath);
        if (importFile.isAbsolute()) {
            if (!importFile.isFile()) {
                throw parseException(ctx.start, importPath + " 无法找到模块");
            }
        }

        File parent = new File(ctx.start.getTokenSource().getSourceName()).getParentFile();
        importFile = new File(parent,importPath);
        try {
            HorScript horScript = new HorScript();
            horScript._parserCode(importFile);
        }catch (ReturnModel returnModel) {
            scope.globalAssign(var, returnModel.value);
        }
        return ValueModel.VOID;
    }

    /** 导出
     * @param ctx the parse tree
     * @return ValueModel
     */
    @Override
    public ValueModel visitExportInst(ExportInstContext ctx) {
        ObjectModel objectModel = new ObjectModel();

        if (ctx.exportElements().isEmpty()) {
            throw RETURN_MODEL;
        }
        ExportElementsContext exportElementsContext = ctx.exportElements();
        List<ExportElementContext> exportElementContexts = exportElementsContext.exportElement();
        // 遍历导出
        for (ExportElementContext exportElementContext : exportElementContexts) {
            // 属性和对象
            if (exportElementContext.implicitParameter() == null) {
                VariableModel var = new VariableModel(exportElementContext.IDENTIFIER().getText());
                ValueModel content = scope.resolve(var);
                if (content == null) {
                    content = ValueModel.NULL;
                }
                if (content.isObjectModel()) {
                    // 设置为 对象 类型，默认 值 类型
                    var.setType(ModelType.Object);
                }
                objectModel.put(var,content);

            }else {
                // 函数
                List<AnyObjectContext> params = this.visit(exportElementContext.implicitParameter()).asImplicitParameter();
                VariableModel var = new VariableModel(exportElementContext.IDENTIFIER().getText());
                // 设置为 函数 类型
                var.setType(ModelType.function);
                var.setId(params.size());
                ValueModel content = scope.resolve(var);
                if (content == null) {
                    content = ValueModel.NULL;
                }
                objectModel.put(var,content);
            }
        }
        RETURN_MODEL.value = new ValueModel(objectModel);
        throw RETURN_MODEL;
    }

    /** 语句块
     * blockSet    : ( statement )* ( RETURN expr (SEM)? )?;
     * @param ctx the parse tree
     */
    @Override
    public ValueModel visitBlockSet(BlockSetContext ctx) {
        ArrayDeque<StatementContext> Stack = new ArrayDeque<>();
        ArrayDeque<StatementContext> Stack1 = new ArrayDeque<>();
        ArrayDeque<StatementContext> Stack2 = new ArrayDeque<>();

        // 函数声明首先被提升，然后提升变量，其他不提升
        for (StatementContext sx : ctx.statement()) {
            // 函数声明首先提升
            if (sx.functionDecl() != null) {
                Stack.add(sx);
            }
            // 提升无前缀变量声明
            else if (sx.noAssignment() != null) {
                Stack1.add(sx);
            }
            // 其他不提升
            else {
                Stack2.add(sx);
            }
        }

        Stack.addAll(Stack1);
        Stack.addAll(Stack2);
        Stack1.clear();
        Stack2.clear();

        for (StatementContext statementContext : Stack) {
            this.visit(statementContext);
        }
        //如果是局部作用域则继续创建下层作用域，否则使用全局作用域
        if (scope.isLocalScope()) {
            scope = new Scope(scope, true);
        }

        // 返回内容时
        AnyObjectContext ex;
        if ((ex = ctx.anyObject()) != null) {
            RETURN_MODEL.value = this.visit(ex);
            // 当返回内容时作用域上移一层，也就是将当前的局部作用域摧毁。
            scope = scope.parent();
            // 销毁运行函数的解析器
            throw RETURN_MODEL;
        }

        return ValueModel.VOID;
    }

    /**
     * 赋值
     * assignment SEM? // 赋值 xx = xx;
     * @param ctx the parse tree
     */
    @Override
    public ValueModel visitAssignment(AssignmentContext ctx) {
        // 获取表达式值
        ValueModel visit = this.visit(ctx.anyObject());
        // 获取变量名
        String id = ctx.IDENTIFIER().getText();
        // 创建变量 k = v
        VariableModel variableModel = new VariableModel(id);

        // 如果是 lambda 函数 加入 functions
        if (visit.isFunction()) {
            variableModel.setType(ModelType.function);
            variableModel.setId(visit.asFunction().getParams().size());
        }
        // 如果是 object 加入 objects
        if (visit.isObjectModel()) {
            // objects.put(id, visit.asObjectModel());
            variableModel.setType(ModelType.Object);
        }
        scope.localAssign(variableModel, visit);
        return ValueModel.VOID;
    }

    /**
     * 无前缀赋值 全局赋值
     * @param ctx the parse tree
     */
    @Override
    public ValueModel visitNoAssignment(NoAssignmentContext ctx) {
        // 获取表达式值
        ValueModel visit = this.visit(ctx.anyObject());
        // 获取变量名
        String id = ctx.IDENTIFIER().getText();
        // 创建变量 k = v
        VariableModel variableModel = new VariableModel(id);

        // 如果是 lambda 函数 加入 functions
        if (visit.isFunction()) {
            variableModel.setType(ModelType.function);
            variableModel.setId(visit.asFunction().getParams().size());
        }
        // 如果是 object 加入 objects
        if (visit.isObjectModel()) {
            // objects.put(id, visit.asObjectModel());
            variableModel.setType(ModelType.Object);
        }
        // 如果是列表
        if (ctx.indexes() != null) {
            ValueModel val = scope.resolve(variableModel);
            List<ExprContext> exps = ctx.indexes().expr();
            setAtIndex(ctx, exps, val, visit);
        } else {
            // 如果在局部作用域中存在那么修改局部的作用域
            if (scope.isLocalScope()) {
                scope.localAssign(variableModel, visit);
            } else {
                // 声明变量
                scope.assign(variableModel, visit);
            }
        }
        return ValueModel.VOID;
    }


    /**
     * 多变量声明
     * @param ctx the parse tree
     */
    @Override
    public ValueModel visitMultivariable(MultivariableContext ctx) {
        // 遍历多变量
        for (VariableContext variableContext : ctx.variable()) {
            // 创建变量
            VariableModel variableModel = new VariableModel(variableContext.IDENTIFIER().getText());

            ValueModel visit = this.visit(variableContext.anyObject());
            if (visit != null) {
                // 如果在局部作用域中存在那么修改局部的作用域
                if (scope.isLocalScope()) {
                    scope.localAssign(variableModel, visit);
                } else {
                    // 声明变量
                    scope.assign(variableModel, visit);
                }
            }
        }
        return ValueModel.VOID;
    }

    /**
     * 显式参数在函数定义时列出 (a,b,c)
     * @param ctx the parse tree
     */
    @Override
    public ValueModel visitExplicitParameter(ExplicitParameterContext ctx) {
        // 创建显式参数参数列表
        return new ValueModel(ctx.idList() != null ? ctx.idList().IDENTIFIER() : new ArrayList<>());
    }

    /**
     * 隐式参数在函数调用时传递给函数真正的值。 12,23
     * @param ctx the parse tree
     */
    @Override
    public ValueModel visitImplicitParameter(ImplicitParameterContext ctx) {
        // 创建隐式参数参数列表
        return new ValueModel(ctx.exprList() != null ? ctx.exprList().anyObject() : new ArrayList<>());
    }

    /**
     * Lambda 函数声明 (a,b) => {}
     * @param ctx the parse tree
     */
    @Override
    public ValueModel visitLambdaDef(LambdaDefContext ctx) {
        // 参数列表
        List<TerminalNode> params = this.visit(ctx.explicitParameter()).asExplicitParameter();
        // 语句块
        ParseTree block = ctx.blockSet();
        // 创建函数
        Function function = new Function(scope, params, block);
        return new ValueModel(function);
    }

    /**
     * 函数声明
     * @param ctx the parse tree
     */
    @Override
    public ValueModel visitFunctionDecl(FunctionDeclContext ctx) {
        // 参数列表
        List<TerminalNode> params = this.visit(ctx.explicitParameter()).asExplicitParameter();
        // 语句块
        ParseTree block = ctx.blockSet();
        // 函数名
        String id = ctx.IDENTIFIER().getText();
        // 创建变量
        VariableModel variableModel = new VariableModel(id);
        variableModel.setId(params.size());
        // 创建函数
        Function function = new Function(scope, params, block);
        // 设置函数名
        function.setIdentifier(id);
        // 设置变量类型为函数
        variableModel.setType(ModelType.function);
        scope.localAssign(variableModel, new ValueModel(function));
        return ValueModel.VOID;
    }

    /**
     * 函数调用
     * @param ctx the parse tree
     */
    @Override
    public ValueModel visitIdentifierFunctionCall(IdentifierFunctionCallContext ctx) {
        // 获取函数名
        VariableModel variableModel = new VariableModel(ctx.IDENTIFIER().getText());
        variableModel.setType(ModelType.function);
        ValueModel valueModel = scope.resolve(variableModel);


        // 函数是否有参数并调用
//        if (ctx.implicitParameter() != null) {
            // 遍历参数
            for (ImplicitParameterContext implicitParameterContext : ctx.implicitParameter()) {
                // 获取参数值列表
                List<AnyObjectContext> params = this.visit(implicitParameterContext).asImplicitParameter();
                List<ValueModel> args = new ArrayList<>(params.size());
                for (AnyObjectContext param : params) {
                    args.add(this.visit(param));
                }
                variableModel.setId(args.size());
                valueModel = scope.resolve(variableModel);

                // 是否存在
                if (valueModel == null) {
                    throw parseException(ctx.start, ctx.IDENTIFIER() + " 不是一个函数");
                }
                // 内置模块函数
                if (valueModel.isNativesFunction()) {
                    return new ValueModel(valueModel.asNativesFunction().invoke(args));
                }

                // 确保为函数
                if (valueModel.isFunction()) {
                    valueModel = valueModel.asFunction().invoke(args);
                }
            }
//        }
        return valueModel;
    }

    /**
     * 路由名
     * IDENTIFIER indexes? implicitParameter?
     * @param ctx the parse tree
     */
    @Override
    public ValueModel visitRouteName(RouteNameContext ctx) {
        // 获取变量名
        VariableModel variableModel = new VariableModel(ctx.IDENTIFIER().getText());
        // 获取变量
        ValueModel valueModel = scope.resolve(variableModel);
        // 有索引时
        if (ctx.indexes() != null) {
            valueModel = resolveIndexes(valueModel, ctx.indexes());
        }
        // 为函数
//        if (valueModel.isFunction() || valueModel.isNativesFunction()) {
            // 函数
            if (ctx.implicitParameter() != null) {
                // 遍历参数
                for (ImplicitParameterContext implicitParameterContext : ctx.implicitParameter()) {
                    // 获取参数值列表
                    List<AnyObjectContext> params = this.visit(implicitParameterContext).asImplicitParameter();
                    List<ValueModel> args = new ArrayList<>(params.size());
                    for (AnyObjectContext param : params) {
                        args.add(this.visit(param));
                    }
                    variableModel.setId(args.size());
                    variableModel.setType(ModelType.function);
                    valueModel = scope.resolve(variableModel);
                    // 内置模块函数
                    if (valueModel.isNativesFunction()) {
                        return new ValueModel(valueModel.asNativesFunction().invoke(args));
                    }else if (valueModel.isFunction()) {
                        // 调用
                        valueModel = valueModel.asFunction().invoke(args);
                    } else {
                        throw parseException(ctx.start, ctx.IDENTIFIER() + " 不是一个函数");
                    }
                }
            }
//        }
        return valueModel;
    }


    /**
     * 访问表达式路由（访问符 -> 子元素）
     * a.b.c
     * @param ctx the parse tree
     */
    @Override
    public ValueModel visitIdentifierExprRoute(IdentifierExprRouteContext ctx) {

        // 不存在访问子元素时
        if (ctx.routeNameSet().DOT().isEmpty()) {
            // 直接访问
            return this.visit(ctx.routeNameSet().routeName().get(0));
        }

        ValueModel valueModel = null;
        // 遍历访问的子元素
        for (RouteNameContext routeNameContext : ctx.routeNameSet().routeName()) {
            // 初始化最前的元素
            if (valueModel == null) {
                valueModel = this.visit(routeNameContext);
            } else {
                // 如果此元素为对象类型时继续进入，否则无法继续访问子元素
                if (valueModel.isObjectModel()) {
                    // 获取对象中的某个元素
                    VariableModel var = new VariableModel(routeNameContext.IDENTIFIER().getText());

                    // 为函数调用
                    if (routeNameContext.implicitParameter() != null) {
                        // 遍历参数列表
                        for (ImplicitParameterContext implicitParameterContext : routeNameContext.implicitParameter()) {
                            List<AnyObjectContext> params = this.visit(implicitParameterContext).asImplicitParameter();
                            List<ValueModel> args = new ArrayList<>(params.size());
                            for (AnyObjectContext param : params) {
                                args.add(this.visit(param));
                            }
                            var.setId(args.size());
                            var.setType(ModelType.function);
                            valueModel = valueModel.asObjectModel().getValue(var);
                            if (valueModel == null) {
                                throw parseException(ctx.start, ctx.getText());
                            }
                            // 内置模块函数
                            if (valueModel.isNativesFunction()) {
                                return new ValueModel(valueModel.asNativesFunction().invoke(args));
                            }else if (valueModel.isFunction()) {
                                // 如果是函数则调用并赋值返回值，如果在下次循环中不是函数则报错
                                valueModel = valueModel.asFunction().invoke(args);
                            } else {
                                throw parseException(ctx.start, ctx.getText() + " 不是一个函数");
                            }
                        }
                    }
                    if (valueModel.isObjectModel()) {
                        valueModel = valueModel.asObjectModel().getValue(var);
                    }
                } else {
                    throw parseException(ctx.start, routeNameContext.IDENTIFIER() + " 无法读取此未知对象");
                }
            }
        }

        return valueModel;
    }

    /**
     * 对象类型
     * @param ctx the parse tree
     */
    @Override
    public ValueModel visitObjectValue(ObjectValueContext ctx) {
        // 创建一个空对象
        ObjectModel objectModel = new ObjectModel();
        // 遍历键值添加进对象中
        for (ObjectKeyValueContext objectKeyValueContext : ctx.objectKeyValue()) {
            String key = objectKeyValueContext.IDENTIFIER().getText();
            VariableModel variableModel = new VariableModel(key);
            variableModel.setType(ModelType.Object);
            ValueModel value = this.visit(objectKeyValueContext.anyObject());
            objectModel.put(variableModel, value);
        }
        return new ValueModel(objectModel);
    }

    /**
     * 列表类型
     * listValue
     * @param ctx the parse tree
     */
    @Override
    public ValueModel visitListValue(ListValueContext ctx) {
        // 创建一个空列表
        ListModel listModel = new ListModel();
        // 值不为空时
        if (ctx.anyObject() != null) {
            // 遍历添加解析值
            for (AnyObjectContext ex : ctx.anyObject()) {
                listModel.add(this.visit(ex));
            }
        }
        return new ValueModel(listModel);
    }

    /**
     * 列表索引切片
     * listValue indexes?                        #listExprRoute // 列表路由
     * @param ctx the parse tree
     */
    @Override
    public ValueModel visitListRoute(ListRouteContext ctx) {
        // 获取列表
        ValueModel val = this.visit(ctx.listValue());
        // 当索引不为空时
        if (ctx.indexes() != null) {
            // 根据索引取得值
            val = resolveIndexes(val, ctx.indexes());
        }
        return val;
    }

    /**
     * 字符串类型
     * @param ctx the parse tree
     */
    @Override
    public ValueModel visitStringValue(StringValueContext ctx) {
        String text = fixString(ctx.STRING());
        return new ValueModel(text);
    }

    /**
     * 字符串索引切片
     * STRING indexes? 字符串 or 字符串[0]
     * @param ctx the parse tree
     */
    @Override
    public ValueModel visitStringRoute(StringRouteContext ctx) {
        // 获取字符串值
        String text = fixString(ctx.STRING());
        ValueModel val = new ValueModel(text);
        if (ctx.indexes() != null) {
            val = resolveIndexes(val, ctx.indexes());
        }
        return val;
    }

    /**
     * 数值类型
     * NUMBER    #numberValue    // 数值类型
     * @param ctx the parse tree
     */
    @Override
    public ValueModel visitNumberValue(NumberValueContext ctx) {
        // 创建空值模型
        ValueModel vm = new ValueModel();
        // 整型
        TerminalNode intNode = ctx.INTEGER_NUM();
        // 浮点型
        TerminalNode decimalNode = ctx.DECIMAL_NUM();

        String radixNumber = null;
        if (intNode != null) {
            radixNumber = intNode.getText();
        }
        if (radixNumber != null) {
            // 转换为Long
            if (radixNumber.length() < 2147483647) {
                return new ValueModel(new Integer(radixNumber));
            }else {
                return new ValueModel(new Long(radixNumber));
            }
        } else {
            // 小数转换为BigDecimal
            BigDecimal bigDec = new BigDecimal(decimalNode.getText());
            int precisionLength = bigDec.precision();
            // 8位以下 使用 float
            if (precisionLength < 8 && !Float.isInfinite(bigDec.floatValue())) {
                return vm.setValue(bigDec.floatValue());
            }
            // 8位以上16位以下 使用 double
            if (precisionLength < 16 && !Double.isInfinite(bigDec.doubleValue())) {
                return vm.setValue(bigDec.doubleValue());
            }
            return vm.setValue(bigDec);
        }
    }

    /**
     * 布尔类型
     *   ( TRUE | FALSE)     #booleanValue   // boolean 类型
     * @param ctx the parse tree
     */
    @Override
    public ValueModel visitBooleanValue(BooleanValueContext ctx) {
        boolean boolValue = ctx.TRUE() != null;
        return new ValueModel(boolValue);
    }

    /**
     * 空类型
     * @param ctx the parse tree
     */
    @Override
    public ValueModel visitNullValue(NullValueContext ctx) {
        return ValueModel.NULL;
    }

    /**
     * 如果 否则如果 否则
     * @param ctx the parse tree
     */
    // 如果 否则如果 否则
    @Override
    public ValueModel visitIfStatement(IfStatementContext ctx) {
        // 如果 ...
        if (this.visit(ctx.ifStat().expr()).asBoolean()) {
            return this.visit(ctx.ifStat().blockSet());
        }
        // 否则如果 ...
        for (int i = 0; i < ctx.elseIfStat().size(); i++) {
            if (this.visit(ctx.elseIfStat(i).expr()).asBoolean()) {
                return this.visit(ctx.elseIfStat(i).blockSet());
            }
        }
        // 否则 ...
        if (ctx.elseStat() != null) {
            return this.visit(ctx.elseStat().blockSet());
        }
        return ValueModel.VOID;
    }

    /**
     * forStatement for循环 类似Python
     * 循环( ? 到(to)｜在(in) ？) {}
     * 用于可迭代序列
     * @param ctx the parse tree
     */
    @Override
    public ValueModel visitForStatement(ForStatementContext ctx) {
        // 获取左右两个对象
        ValueModel lhs, rhs;
        if (ctx.anyObject(1) == null) {
            rhs = this.visit(ctx.anyObject(0));
            lhs = new ValueModel(0);
        } else {
            lhs = this.visit(ctx.anyObject(0));
            rhs = this.visit(ctx.anyObject(1));
        }

        // 判断运算符
        switch (ctx.op.getType()) {
                // 在
            case HorScriptLexer.IN:
                try {
                    if (rhs.isList()) {
                        for (DataModel val : rhs.asList().asOv()) {
                            scope.assign(new VariableModel(ctx.IDENTIFIER().getText()), new ValueModel(val.asT()));
                            ValueModel returnValue = this.visit(ctx.blockSet());
                            if (returnValue != ValueModel.VOID) {
                                return returnValue;
                            }
                        }
                    }
                } catch (NullPointerException npe) {
                    throw parseException(ctx.start, "错误的可迭代对象: " + ctx.getText());

                }
                return ValueModel.VOID;
                // 到
            case HorScriptLexer.TO:
                int start = lhs.asInt();
                int stop = rhs.asInt();
                for (int i = start; i <= stop; i++) {
                    scope.assign(new VariableModel(ctx.IDENTIFIER().getText()), new ValueModel(i));
                    ValueModel returnValue = this.visit(ctx.blockSet());
                    if (returnValue != ValueModel.VOID) {
                        return returnValue;
                    }
                }
                return ValueModel.VOID;
            default:
                throw parseException(ctx.start, "未知运算符类型: " + ctx.op.getType());
        }

    }

    /**
     * 判断循环
     * whileStatement
     * @param ctx the parse tree
     */
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

    /**
     * 开始 {}判断循环()
     * whileStatement do while判断循环
     * @param ctx the parse tree
     */
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
            throw parseException(ctx.start, "空指针异常: " + ctx.expr().getText());
        }
        return ValueModel.VOID;
    }


    /**
     * 系统函数打印
     * @param ctx the parse tree
     */
    @Override
    public ValueModel visitPrintFunctionCall(PrintFunctionCallContext ctx) {
        if (ctx.exprList() != null) {
            System.out.print(printStr(ctx.exprList().anyObject()));
        } else System.out.println();
        return ValueModel.VOID;
    }

    /**
     * 系统函数打印换行
     * @param ctx the parse tree
     */
    @Override
    public ValueModel visitPrintlnFunctionCall(PrintlnFunctionCallContext ctx) {
        if (ctx.exprList() != null) {
            System.out.println(printStr(ctx.exprList().anyObject()));
        } else System.out.println();
        return ValueModel.VOID;
    }

    private String printStr(List<AnyObjectContext> ctx) {
        StringBuilder sBuffer = new StringBuilder();
        // 参数列表
        for (AnyObjectContext ex : ctx) {
            try {
                ValueModel content = this.visit(ex);
                if (content.isNull()) {
                    content.setValue('空');
                }
                if (content.isBoolean()) {
                    content.setValue(content.asBoolean() ? '真' : '假');
                }
                sBuffer.append(content.asString());
            } catch (NullPointerException e) {
                throw parseException(ex.start, "未定义：" + ex.getText());
            }
        }
        return sBuffer.toString();
    }

    /**
     * size 长度方法
     * @param ctx the parse tree
     */
    @Override
    public ValueModel visitSizeFunctionCall(SizeFunctionCallContext ctx) {
        ValueModel value = this.visit(ctx.anyObject());

        if (value.isString() || value.isNumber()) {
            return new ValueModel(value.asString().length());
        }

        if (value.isList()) {
            return new ValueModel(value.asList().size());
        }
        if (value.isFunction()) {
            return new ValueModel(value.asFunction().getParams().size());
        }
        if (value.isBoolean()) {
            return new ValueModel(1);
        }

        throw parseException(ctx.start, ctx.getText());
    }

    /**
     * 断言
     * @param ctx the parse tree
     */
    @Override
    public ValueModel visitAssertFunctionCall(AssertFunctionCallContext ctx) {

        ValueModel value = this.visit(ctx.expr());

        if (!value.isBoolean()) {
            throw parseException(ctx.start, ctx.getText());
        }

        if (!value.asBoolean()) {
            throw parseException(ctx.start, "断言失败: " + ctx.expr().getText());
        }

        return ValueModel.VOID;
    }



    /**
     * 前自增自减
     * @param ctx the parse tree
     */
    @Override
    public ValueModel visitPreSelfExpr(PreSelfExprContext ctx) {
        // 获取变量
        VariableModel variableModel = new VariableModel(ctx.IDENTIFIER().getText());
        ValueModel valueModel = scope.resolve(variableModel);

        if (!valueModel.isNumber()) {
            throw parseException(ctx.start, "类型错误: " + ctx.getText());
        }
        switch (ctx.prefix.getType()) {
            case HorScriptLexer.INCREMENT: {
                return valueModel.setValue(OperatorUtil.add(valueModel.asNumber(), 1));
            }
            case HorScriptLexer.DECREMENT: {
                return valueModel.setValue(OperatorUtil.subtract(valueModel.asNumber(),1));
            }

        }
        return ValueModel.VOID;
    }

    /**
     * 后自增自减
     * @param ctx the parse tree
     */
    @Override
    public ValueModel visitPostSelfExpr(PostSelfExprContext ctx) {
        // 获取变量
        VariableModel variableModel = new VariableModel(ctx.IDENTIFIER().getText());
        ValueModel valueModel = scope.resolve(variableModel);

        if (!valueModel.isNumber()) {
            throw parseException(ctx.start, "类型错误: " + ctx.getText());
        }
        switch (ctx.postfix.getType()) {
            case HorScriptLexer.INCREMENT: {
                Number oldValue = valueModel.asNumber();
                valueModel.setValue(OperatorUtil.add(valueModel.asNumber(), 1));
                return new ValueModel(oldValue);
            }
            case HorScriptLexer.DECREMENT: {
                Number oldValue = valueModel.asNumber();
                valueModel.setValue(OperatorUtil.subtract(valueModel.asNumber(),1));
                return new ValueModel(oldValue);
            }

        }
        return ValueModel.VOID;
    }

    /**
     * LBT expr RBT        #privilegeExpr  // 优先级
     * @param ctx the parse tree
     */
    @Override
    public ValueModel visitPrivilegeExpr(PrivilegeExprContext ctx) {
        return this.visit(ctx.expr());
    }


    /** 一元运算 + - !
     * prefix=( PLUS | MINUS | NOT) expr      #unaryExpr
     * @param ctx the parse tree
     */
    @Override
    public ValueModel visitUnaryExpr(UnaryExprContext ctx) {
        ValueModel vm = this.visit(ctx.expr());
        switch (ctx.prefix.getType()) {
            case HorScriptLexer.PLUS:
                return vm;
            case HorScriptLexer.MINUS:
                if (!vm.isNumber()) {
                    throw parseException(ctx.start, "类型错误: " + ctx.getText());
                }
                return vm.setValue(OperatorUtil.negate(vm.asNumber()));
            case HorScriptLexer.NOT:
                if (vm.isNumber()) {
                    vm.setValue(vm.asInt() > 0);
                }
                if (!vm.isBoolean()) {
                    throw parseException(ctx.start, "类型错误: " + ctx.getText());
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
            throw parseException(ctx.start, "空指针异常: " + ctx.start.getText());
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
            throw parseException(ctx.start, "空指针异常: " + ctx.start.getText());
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
            throw parseException(ctx.start, "空指针异常: " + ctx.start.getText());
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
            throw parseException(ctx.start, "空指针异常: " + ctx.start.getText());
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
            throw parseException(ctx.start, "空指针异常: " + ctx.start.getText());
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
            throw parseException(ctx.start, "空指针异常: " + ctx.start.getText());
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
        throw parseException(ctx.start, "类型错误: " + ctx.getText() + " 解决方案：应为列表类型");

    }

    /**
     * 算数运算 + plus
     */
    private ValueModel plus(DyadicExpr_BContext ctx) {
        ValueModel lhs = this.visit(ctx.expr(0));
        ValueModel rhs = this.visit(ctx.expr(1));

        ValueModel res = new ValueModel();

        // number + number
        if (lhs.isNumber() && rhs.isNumber()) {
            return res.setValue(OperatorUtil.add(lhs.asNumber(),rhs.asNumber()));
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

    /**
     * 减法
     */
    private ValueModel minus(DyadicExpr_BContext ctx) {
        ValueModel lhs = this.visit(ctx.expr(0));
        ValueModel rhs = this.visit(ctx.expr(1));

        ValueModel res = new ValueModel();

        // number - number
        if (lhs.isNumber() && rhs.isNumber()) {
            return res.setValue(OperatorUtil.subtract(lhs.asNumber(),rhs.asNumber()));
        }

        // list - any
        if (lhs.isList()) {
            ListModel list = lhs.asList();
            list.remove(rhs);
            return res.setValue(list);
        }

        throw parseException(ctx.start, "非法表达式: " + ctx.getText());
    }

    /**
     * 乘法
     */
    private ValueModel mul(DyadicExpr_AContext ctx) {
        ValueModel lhs = this.visit(ctx.expr(0));
        ValueModel rhs = this.visit(ctx.expr(1));

        ValueModel vm = new ValueModel();

        // number * number
        if (lhs.isNumber() && rhs.isNumber()) {
            return vm.setValue(OperatorUtil.multiply(lhs.asNumber(),rhs.asNumber()));
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

        throw parseException(ctx.start, "非法表达式: " + ctx.getText());
    }

    /**
     * 算数运算 /
     */
    private ValueModel div(DyadicExpr_AContext ctx) {
        // 获取表达式左值
        ValueModel lhs = this.visit(ctx.expr(0));
        // 获取表达式右值
        ValueModel rhs = this.visit(ctx.expr(1));

        if (lhs.isNumber() && rhs.isNumber()) {
            return new ValueModel(OperatorUtil.divide(lhs.asNumber(),rhs.asNumber(),10,null));
        }

        throw parseException(ctx.start, "非法表达式: " + ctx.getText());
    }

    /**
     * 算数运算 %
     */
    private ValueModel mod(DyadicExpr_AContext ctx) {
        // 获取表达式左值
        ValueModel lhs = this.visit(ctx.expr(0));
        // 获取表达式右值
        ValueModel rhs = this.visit(ctx.expr(1));

        if (lhs.isNumber() && rhs.isNumber()) {

            return new ValueModel(OperatorUtil.mod(lhs.asNumber(),rhs.asNumber()));
        }

        throw parseException(ctx.start, "非法表达式: " + ctx.getText());
    }

    /**
     * 算数运算 &
     */
    private ValueModel and(DyadicExpr_CContext ctx) {
        // 获取表达式左值
        ValueModel lhs = this.visit(ctx.expr(0));
        // 获取表达式右值
        ValueModel rhs = this.visit(ctx.expr(1));

        if (lhs.isNumber() && rhs.isNumber()) {
            return new ValueModel(OperatorUtil.and(lhs.asNumber(),rhs.asNumber()));
        }

        throw parseException(ctx.start, "非法表达式: " + ctx.getText());
    }

    /**
     * 算数运算 |
     */
    private ValueModel or(DyadicExpr_CContext ctx) {
        // 获取表达式左值
        ValueModel lhs = this.visit(ctx.expr(0));
        // 获取表达式右值
        ValueModel rhs = this.visit(ctx.expr(1));

        if (lhs.isNumber() && rhs.isNumber()) {
            return new ValueModel(OperatorUtil.or(lhs.asNumber(),rhs.asNumber()));
        }

        throw parseException(ctx.start, "非法表达式: " + ctx.getText());
    }


    /**
     * 算数运算 ^
     */
    private ValueModel xor(DyadicExpr_CContext ctx) {
        // 获取表达式左值
        ValueModel lhs = this.visit(ctx.expr(0));
        // 获取表达式右值
        ValueModel rhs = this.visit(ctx.expr(1));

        if (lhs.isNumber() && rhs.isNumber()) {
            return new ValueModel(OperatorUtil.xor(lhs.asNumber(),lhs.asNumber()));
        }
        throw parseException(ctx.start, "非法表达式: " + ctx.getText());
    }

    /**
     * 算数运算 <<
     */
    private ValueModel l_shift(DyadicExpr_CContext ctx) {
        // 获取表达式左值
        ValueModel lhs = this.visit(ctx.expr(0));
        // 获取表达式右值
        ValueModel rhs = this.visit(ctx.expr(1));

        if (lhs.isNumber() && rhs.isNumber()) {
            return new ValueModel(OperatorUtil.shiftLeft(lhs.asNumber(),rhs.asNumber()));
        }

        throw parseException(ctx.start, "非法表达式: " + ctx.getText());
    }

    /**
     * 算数运算 >>
     */
    private ValueModel r_shift(DyadicExpr_CContext ctx) {
        // 获取表达式左值
        ValueModel lhs = this.visit(ctx.expr(0));
        // 获取表达式右值
        ValueModel rhs = this.visit(ctx.expr(1));

        if (lhs.isNumber() && rhs.isNumber()) {
            return new ValueModel(OperatorUtil.shiftRight(lhs.asNumber(),rhs.asNumber()));
        }

        throw parseException(ctx.start, "非法表达式: " + ctx.getText());
    }

    /**
     * 算数运算 >>>
     */
    private ValueModel r_shift2(DyadicExpr_CContext ctx) {
        // 获取表达式左值
        ValueModel lhs = this.visit(ctx.expr(0));
        // 获取表达式右值
        ValueModel rhs = this.visit(ctx.expr(1));

        if (lhs.isNumber() && rhs.isNumber()) {
            return new ValueModel(OperatorUtil.shiftRightWithUnsigned(lhs.asNumber(),rhs.asNumber()));
        }

        throw parseException(ctx.start, "非法表达式: " + ctx.getText());
    }

    /**
     * 逻辑运算 >=
     */
    private ValueModel gt_equals(DyadicExpr_DContext ctx) {
        // 获取表达式左值
        ValueModel lhs = this.visit(ctx.expr(0));
        // 获取表达式右值
        ValueModel rhs = this.visit(ctx.expr(1));

        if (lhs.isNumber() && rhs.isNumber()) {
            return new ValueModel(OperatorUtil.gteq(lhs.asNumber(),rhs.asNumber()));
        }
        if (lhs.isString() && rhs.isString()) {
            return new ValueModel(lhs.asString().compareTo(rhs.asString()) >= 0);
        }

        throw parseException(ctx.start, "非法表达式: " + ctx.getText());
    }

    /**
     * 逻辑运算 <=
     */
    private ValueModel lt_equals(DyadicExpr_DContext ctx) {
        // 获取表达式左值
        ValueModel lhs = this.visit(ctx.expr(0));
        // 获取表达式右值
        ValueModel rhs = this.visit(ctx.expr(1));

        if (lhs.isNumber() && rhs.isNumber()) {
            return new ValueModel(OperatorUtil.lteq(lhs.asNumber(),rhs.asNumber()));
        }
        if (lhs.isString() && rhs.isString()) {
            return new ValueModel(lhs.asString().compareTo(rhs.asString()) <= 0);
        }
        throw parseException(ctx.start, "非法表达式: " + ctx.getText());

    }

    /**
     * 逻辑运算 >
     */
    private ValueModel gt(DyadicExpr_DContext ctx) {
        // 获取表达式左值
        ValueModel lhs = this.visit(ctx.expr(0));
        // 获取表达式右值
        ValueModel rhs = this.visit(ctx.expr(1));

        if (lhs.isNumber() && rhs.isNumber()) {
            return new ValueModel(OperatorUtil.gt(lhs.asNumber(),rhs.asNumber()));
        }
        if (lhs.isString() && rhs.isString()) {
            return new ValueModel(lhs.asString().compareTo(rhs.asString()) > 0);
        }

        throw parseException(ctx.start, "非法表达式: " + ctx.getText());
    }

    /**
     * 逻辑运算 <
     */
    private ValueModel lt(DyadicExpr_DContext ctx) {
        // 获取表达式左值
        ValueModel lhs = this.visit(ctx.expr(0));
        // 获取表达式右值
        ValueModel rhs = this.visit(ctx.expr(1));

        if (lhs.isNumber() && rhs.isNumber()) {
            return new ValueModel(OperatorUtil.lt(lhs.asNumber(),rhs.asNumber()));
        }
        if (lhs.isString() && rhs.isString()) {
            return new ValueModel(lhs.asString().compareTo(rhs.asString()) < 0);
        }

        throw parseException(ctx.start, "非法表达式: " + ctx.getText());
    }

    /**
     * 逻辑运算 !=
     */
    private ValueModel neq(DyadicExpr_EContext ctx) {
        // 获取表达式左值
        ValueModel lhs = this.visit(ctx.expr(0));
        // 获取表达式右值
        ValueModel rhs = this.visit(ctx.expr(1));

        return new ValueModel(!lhs.equals(rhs));

    }

    /**
     * 逻辑运算 ==
     */
    private ValueModel eq(DyadicExpr_EContext ctx) {
        // 获取表达式左值
        ValueModel lhs = this.visit(ctx.expr(0));
        // 获取表达式右值
        ValueModel rhs = this.visit(ctx.expr(1));

        return new ValueModel(lhs.equals(rhs));

    }

    /**
     * 逻辑运算 ||
     */
    private ValueModel sc_or(DyadicExpr_FContext ctx) {
        // 获取表达式左值
        ValueModel lhs = this.visit(ctx.expr(0));
        // 获取表达式右值
        ValueModel rhs = this.visit(ctx.expr(1));

        if (lhs.isNumber() && lhs.asLong() != 0) {
            return new ValueModel(true);
        }

        if (rhs.isNumber()) {
            rhs.setValue(false);
        }

        if (!lhs.isBoolean() || !rhs.isBoolean()) {
            throw parseException(ctx.start, "非法表达式: " + ctx.getText());
        }

        return new ValueModel(lhs.asBoolean() || rhs.asBoolean());
    }

    /**
     * 逻辑运算 &&
     */
    private ValueModel sc_and(DyadicExpr_FContext ctx) {
        // 获取表达式左值
        ValueModel lhs = this.visit(ctx.expr(0));
        // 获取表达式右值
        ValueModel rhs = this.visit(ctx.expr(1));

        // 进行 && 运算 为 0 为假 否则为真
        if (lhs.isNumber() && lhs.asLong() == 0) {
            return new ValueModel(false);
        } else {
            lhs.setValue(true);
        }

        // 数值类型可直接转换
        if (rhs.isNumber()) {
            rhs.setValue(rhs.asLong() != 0);
        }

        // 如果不是布尔类型 报错
        if (!lhs.isBoolean() || !rhs.isBoolean()) {
            throw parseException(ctx.start, "非法表达式: " + ctx.getText());
        }

        return new ValueModel(lhs.asBoolean() && rhs.asBoolean());
    }

    /**
     * 索引切片
     */
    private ValueModel resolveIndexes(ValueModel val, IndexesContext indexes) {
        // 类型不符合 或者 使用切片并start为空
        if (!val.isList() && !val.isString() || (indexes.slice().size() == 0 && indexes.expr().size() == 0)) {
            throw parseException(indexes.start, "非法表达式: " + indexes.getText());
        }
        Integer start = null;
        Integer end = null;
        int step = 0;
        if (indexes.expr().size() == 1) {
            ExprContext ec = indexes.expr().get(0);
            ValueModel idx = this.visit(ec);
            if (!idx.isNumber()) {
                throw parseException(ec.start, "非法表达式: " + ec.getText());
            }
            start = idx.asInt();
        }

        // 有切片时
        if (indexes.slice().size() == 1) {
            SliceContext sliceContext = indexes.slice(0);
            if (sliceContext.expr().size() != 0) {
                //[start:end]
                if (sliceContext.COLON().size() == 1) {
                    if (sliceContext.expr().size() == 1) {
                        ExprContext ec = sliceContext.expr().get(0);
                        ValueModel idx = this.visit(ec);
                        if (!idx.isNumber()) {
                            throw parseException(ec.start, "非法表达式: " + ec.getText());
                        }
                        end = idx.asInt();
                    }
                }else {
                    //[start:end:step]
                    int colonStopIndex = sliceContext.COLON(1).getSymbol().getStopIndex();
                    if (sliceContext.expr().size() == 1 && colonStopIndex > sliceContext.expr().get(0).start.getStartIndex()) {
                        ExprContext ec = sliceContext.expr().get(0);
                        ValueModel idx = this.visit(ec);
                        if (!idx.isNumber()) {
                            throw parseException(ec.start, "非法表达式: " + ec.getText());
                        }
                        end = idx.asInt();
                    } else if (sliceContext.expr().size() == 1) {
                        ExprContext ec = sliceContext.expr().get(0);
                        ValueModel idx = this.visit(ec);
                        if (!idx.isNumber()) {
                            throw parseException(ec.start, "非法表达式: " + ec.getText());
                        }
                        step = idx.asInt();
                    }else {
                        ExprContext ecEnd = sliceContext.expr().get(0);
                        ExprContext ecStep = sliceContext.expr().get(1);
                        ValueModel idEnd = this.visit(ecEnd);
                        ValueModel idStep = this.visit(ecStep);

                        if (!idEnd.isNumber()) {
                            throw parseException(ecEnd.start, "非法表达式: " + ecEnd.getText());
                        }
                        if (!idStep.isNumber()) {
                            throw parseException(ecStep.start, "非法表达式: " + ecStep.getText());
                        }
                        end = idEnd.asInt();
                        step = idStep.asInt();
                    }
                }
            }
        }else {
            if (start != null) {
                end = start + 1;
            }
        }

        // 字符串索引
        if (val.isString()) {
            if (end != null) {
                if (val.asString().length() - 1 < end) {
                    throw parseException(indexes.start, "字符串索引越界：" + end);
                }
            }
            val = new ValueModel(StringUtil.sub(val.asString(),start,end,step));
        }

        // 列表
        if (val.isList()) {
            if (end != null) {
                if (val.asList().size() - 1 < end) {
                    throw parseException(indexes.start, "列表索引越界：" + end);
                }
            }

            val = new ValueModel(ListUtil.sub(val.asList().asOv(),start,end,step));
        }

        return val;

    }

    /**
     * 列表元素赋值
     */
    private void setAtIndex(ParserRuleContext ctx, List<ExprContext> indexes, ValueModel val, ValueModel newVal) {
        if (!val.isList()) {
            throw parseException(ctx.start, "类型错误");
        }
        for (int i = 0; i < indexes.size() - 1; i++) {
            ValueModel idx = this.visit(indexes.get(i));
            if (!idx.isNumber()) {
                throw parseException(ctx.start, "类型错误");
            }
            val = val.asList().getValue(idx.asDouble().intValue());
        }
        ValueModel idx = this.visit(indexes.get(indexes.size() - 1));
        if (!idx.isNumber()) {
            throw parseException(ctx.start, "类型错误");
        }
        val.asList().set(idx.asDouble().intValue(), newVal);
    }

    private VisitorException parseException(Token token, String errorMessage) {
        return new VisitorException(token.getLine(), token.getStartIndex(), errorMessage);
    }

    /**
     *  获取 String
     */
    private String fixString(TerminalNode stringNode) {
        String nodeText = stringNode.getText();
        return nodeText.substring(1, nodeText.length() - 1);
    }

}
