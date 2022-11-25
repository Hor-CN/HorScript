// Generated from java-escape by ANTLR 4.11.1
package core;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link HorScriptParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface HorScriptParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link HorScriptParser#rootInstSet}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRootInstSet(HorScriptParser.RootInstSetContext ctx);
	/**
	 * Visit a parse tree produced by {@link HorScriptParser#importInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportInst(HorScriptParser.ImportInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link HorScriptParser#blockSet}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockSet(HorScriptParser.BlockSetContext ctx);
	/**
	 * Visit a parse tree produced by {@link HorScriptParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(HorScriptParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code nullValue}
	 * labeled alternative in {@link HorScriptParser#primitiveValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullValue(HorScriptParser.NullValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code booleanValue}
	 * labeled alternative in {@link HorScriptParser#primitiveValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanValue(HorScriptParser.BooleanValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code numberValue}
	 * labeled alternative in {@link HorScriptParser#primitiveValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumberValue(HorScriptParser.NumberValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code stringValue}
	 * labeled alternative in {@link HorScriptParser#primitiveValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringValue(HorScriptParser.StringValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link HorScriptParser#anyObject}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnyObject(HorScriptParser.AnyObjectContext ctx);
	/**
	 * Visit a parse tree produced by {@link HorScriptParser#objectValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectValue(HorScriptParser.ObjectValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link HorScriptParser#objectKeyValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectKeyValue(HorScriptParser.ObjectKeyValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link HorScriptParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment(HorScriptParser.AssignmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link HorScriptParser#noAssignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNoAssignment(HorScriptParser.NoAssignmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link HorScriptParser#idList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdList(HorScriptParser.IdListContext ctx);
	/**
	 * Visit a parse tree produced by {@link HorScriptParser#exprList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprList(HorScriptParser.ExprListContext ctx);
	/**
	 * Visit a parse tree produced by {@link HorScriptParser#listValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListValue(HorScriptParser.ListValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code identifierExpr}
	 * labeled alternative in {@link HorScriptParser#routerMapping}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierExpr(HorScriptParser.IdentifierExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code nameExprRoute}
	 * labeled alternative in {@link HorScriptParser#routerMapping}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNameExprRoute(HorScriptParser.NameExprRouteContext ctx);
	/**
	 * Visit a parse tree produced by the {@code listRoute}
	 * labeled alternative in {@link HorScriptParser#routerMapping}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListRoute(HorScriptParser.ListRouteContext ctx);
	/**
	 * Visit a parse tree produced by the {@code stringRoute}
	 * labeled alternative in {@link HorScriptParser#routerMapping}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringRoute(HorScriptParser.StringRouteContext ctx);
	/**
	 * Visit a parse tree produced by {@link HorScriptParser#routeNameSet}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRouteNameSet(HorScriptParser.RouteNameSetContext ctx);
	/**
	 * Visit a parse tree produced by {@link HorScriptParser#routeName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRouteName(HorScriptParser.RouteNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link HorScriptParser#indexes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexes(HorScriptParser.IndexesContext ctx);
	/**
	 * Visit a parse tree produced by {@link HorScriptParser#ifStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStatement(HorScriptParser.IfStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link HorScriptParser#ifStat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStat(HorScriptParser.IfStatContext ctx);
	/**
	 * Visit a parse tree produced by {@link HorScriptParser#elseIfStat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseIfStat(HorScriptParser.ElseIfStatContext ctx);
	/**
	 * Visit a parse tree produced by {@link HorScriptParser#elseStat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseStat(HorScriptParser.ElseStatContext ctx);
	/**
	 * Visit a parse tree produced by {@link HorScriptParser#forStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForStatement(HorScriptParser.ForStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link HorScriptParser#whileStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStatement(HorScriptParser.WhileStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link HorScriptParser#doWhileStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDoWhileStatement(HorScriptParser.DoWhileStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link HorScriptParser#lambdaDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLambdaDef(HorScriptParser.LambdaDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link HorScriptParser#functionDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionDecl(HorScriptParser.FunctionDeclContext ctx);
	/**
	 * Visit a parse tree produced by the {@code identifierFunctionCall}
	 * labeled alternative in {@link HorScriptParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierFunctionCall(HorScriptParser.IdentifierFunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code funcCallResult_route1}
	 * labeled alternative in {@link HorScriptParser#functionCallResult}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncCallResult_route1(HorScriptParser.FuncCallResult_route1Context ctx);
	/**
	 * Visit a parse tree produced by the {@code funcCallResult_route2}
	 * labeled alternative in {@link HorScriptParser#functionCallResult}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncCallResult_route2(HorScriptParser.FuncCallResult_route2Context ctx);
	/**
	 * Visit a parse tree produced by the {@code funcCallResult_call}
	 * labeled alternative in {@link HorScriptParser#functionCallResult}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncCallResult_call(HorScriptParser.FuncCallResult_callContext ctx);
	/**
	 * Visit a parse tree produced by the {@code printFunctionCall}
	 * labeled alternative in {@link HorScriptParser#systemFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrintFunctionCall(HorScriptParser.PrintFunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code printlnFunctionCall}
	 * labeled alternative in {@link HorScriptParser#systemFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrintlnFunctionCall(HorScriptParser.PrintlnFunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assertFunctionCall}
	 * labeled alternative in {@link HorScriptParser#systemFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssertFunctionCall(HorScriptParser.AssertFunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code sizeFunctionCall}
	 * labeled alternative in {@link HorScriptParser#systemFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSizeFunctionCall(HorScriptParser.SizeFunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code inputFunctionCall}
	 * labeled alternative in {@link HorScriptParser#systemFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInputFunctionCall(HorScriptParser.InputFunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dyadicExpr_F}
	 * labeled alternative in {@link HorScriptParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDyadicExpr_F(HorScriptParser.DyadicExpr_FContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dyadicExpr_D}
	 * labeled alternative in {@link HorScriptParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDyadicExpr_D(HorScriptParser.DyadicExpr_DContext ctx);
	/**
	 * Visit a parse tree produced by the {@code unaryExpr}
	 * labeled alternative in {@link HorScriptParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryExpr(HorScriptParser.UnaryExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dyadicExpr_E}
	 * labeled alternative in {@link HorScriptParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDyadicExpr_E(HorScriptParser.DyadicExpr_EContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dyadicExpr_B}
	 * labeled alternative in {@link HorScriptParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDyadicExpr_B(HorScriptParser.DyadicExpr_BContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ternaryExpr}
	 * labeled alternative in {@link HorScriptParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTernaryExpr(HorScriptParser.TernaryExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dyadicExpr_C}
	 * labeled alternative in {@link HorScriptParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDyadicExpr_C(HorScriptParser.DyadicExpr_CContext ctx);
	/**
	 * Visit a parse tree produced by the {@code inExpr}
	 * labeled alternative in {@link HorScriptParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInExpr(HorScriptParser.InExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dyadicExpr_A}
	 * labeled alternative in {@link HorScriptParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDyadicExpr_A(HorScriptParser.DyadicExpr_AContext ctx);
	/**
	 * Visit a parse tree produced by the {@code atomExpr}
	 * labeled alternative in {@link HorScriptParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtomExpr(HorScriptParser.AtomExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code privilegeExpr}
	 * labeled alternative in {@link HorScriptParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrivilegeExpr(HorScriptParser.PrivilegeExprContext ctx);
}