// Generated from Hello.g4 by ANTLR 4.5.2
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link HelloParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface HelloVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link HelloParser#prog}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProg(HelloParser.ProgContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#dcls}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDcls(HelloParser.DclsContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#robonameAssign}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRobonameAssign(HelloParser.RobonameAssignContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#initBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInitBlock(HelloParser.InitBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#behaviorBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBehaviorBlock(HelloParser.BehaviorBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(HelloParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#dataStructDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataStructDef(HelloParser.DataStructDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#dataStructDcl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataStructDcl(HelloParser.DataStructDclContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#arrayDcl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayDcl(HelloParser.ArrayDclContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#eventDcl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEventDcl(HelloParser.EventDclContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#funcDcl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncDcl(HelloParser.FuncDclContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#paramList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParamList(HelloParser.ParamListContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#param}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam(HelloParser.ParamContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#eventParam}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEventParam(HelloParser.EventParamContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#funcCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncCall(HelloParser.FuncCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#argList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgList(HelloParser.ArgListContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#varDcl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDcl(HelloParser.VarDclContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#baseIdent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBaseIdent(HelloParser.BaseIdentContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#generalIdent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeneralIdent(HelloParser.GeneralIdentContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#assign}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssign(HelloParser.AssignContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#basicAssignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBasicAssignment(HelloParser.BasicAssignmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#assignmentOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentOp(HelloParser.AssignmentOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr(HelloParser.ExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#stmts}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStmts(HelloParser.StmtsContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#ifStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStmt(HelloParser.IfStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#iterStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIterStmt(HelloParser.IterStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#returnStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnStmt(HelloParser.ReturnStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#logicalORExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalORExpr(HelloParser.LogicalORExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#logicalANDExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalANDExpr(HelloParser.LogicalANDExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#equalityExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqualityExpr(HelloParser.EqualityExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#relationalExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelationalExpr(HelloParser.RelationalExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#additiveExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdditiveExpr(HelloParser.AdditiveExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#multExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultExpr(HelloParser.MultExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#unaryExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryExpr(HelloParser.UnaryExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#primaryExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryExpr(HelloParser.PrimaryExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#typeList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeList(HelloParser.TypeListContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(HelloParser.TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#eventType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEventType(HelloParser.EventTypeContext ctx);
}