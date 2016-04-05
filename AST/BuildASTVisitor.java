import java.util.*;

import org.antlr.v4.runtime.tree.ParseTree;


public class BuildASTVisitor extends HelloBaseVisitor<AbstractNode> {
	public AbstractNode visitProg(HelloParser.ProgContext context) {
		return visit(context.dcls());
	}
	
	public AbstractNode visitDcls(HelloParser.DclsContext context) {
		List<DeclarationNode> declarations = new ArrayList<DeclarationNode>();
		for (ParseTree child : nullSafe(context.children)) {
			DeclarationNode dcl = (DeclarationNode) visit(child);
			declarations.add(dcl);
		}
		return new ProgramNode(declarations);
	}
	
	public AbstractNode visitRobonameAssign(HelloParser.RobonameAssignContext context) {
		return new RobotDeclarationNode(RobotDeclarationNode.RobotDeclarationType.name, context.TextLit().getText());
	}
	
	public AbstractNode visitInitBlock(HelloParser.InitBlockContext context) {
		List<StatementNode> statements = new ArrayList<StatementNode>();
		
		for (ParseTree child : nullSafe(context.block().stmts().children)) {
			StatementNode stmt = (StatementNode) visit(child);
			statements.add(stmt);		
		}
		
		return new RobotDeclarationNode(RobotDeclarationNode.RobotDeclarationType.initialization, statements);
	}
	
	public AbstractNode visitBehaviorBlock(HelloParser.BehaviorBlockContext context) {
		List<StatementNode> statements = new ArrayList<StatementNode>();
		
		for (ParseTree child : nullSafe(context.block().stmts().children)) {
			StatementNode stmt = (StatementNode) visit(child);
			statements.add(stmt);		
		}
		
		return new RobotDeclarationNode(RobotDeclarationNode.RobotDeclarationType.behavior, statements);
	}
	
	public AbstractNode visitEventDcl(HelloParser.EventDclContext context) {
		String ident = context.Ident().getText();
		VarNode param = new VarNode(context.eventParam().eventType().getText(), context.eventParam().Ident().getText());
		
		List<StatementNode> statements = new ArrayList<StatementNode>();
		for (ParseTree child : nullSafe(context.block().stmts().children)) {
			StatementNode stmt = (StatementNode) visit(child);
			statements.add(stmt);		
		}
		
		return new EventDeclarationNode(ident, param, statements);	
	}
	
	public AbstractNode visitFuncDcl(HelloParser.FuncDclContext context) {
		// List of return types
		List<TypeNode> typeList = new ArrayList<TypeNode>();
		if (context.typeList().getRuleContext() instanceof HelloParser.GeneralTypeListContext)
			for (ParseTree child : nullSafe(context.typeList().children)){
				if (child instanceof HelloParser.GeneralTypeContext)
				{
					TypeNode type = new TypeNode(child.getText());
					typeList.add(type);
				}
			}
		else
			typeList.add(new TypeNode("void"));
		
		String ident = context.Ident().getText();
		
		List<VarNode> paramList = new ArrayList<VarNode>();
		for (HelloParser.ParamContext child : nullSafe(context.paramList().param())) {
			VarNode param = new VarNode(child.type().getText(), child.Ident().getText());
			paramList.add(param);
		}

		List<StatementNode> statements = new ArrayList<StatementNode>();
		for (ParseTree child : nullSafe(context.block().stmts().children)) {
			StatementNode stmt = (StatementNode) visit(child);
			statements.add(stmt);
		}
		
		return new FuncDeclarationNode(typeList, ident, paramList, statements);	
	}
	
	public AbstractNode visitVarDcl(HelloParser.VarDclContext context) {
		VarNode variable = new VarNode(	context.type().getText(),
										context.basicAssignment().Ident().getText());
		return new VarDeclarationNode(	context.start.getLine(),
										context.start.getCharPositionInLine(),
										variable,
										(ExpressionNode) visit(context.basicAssignment().expr()));
	}
	
	public AbstractNode visitDataStructDef(HelloParser.DataStructDefContext context) {
		String typeName = context.Ident().getText();
		
		final int PRE_DCLS_TOKENS = 3;
		final int POST_DCLS_TOKENS = 1;
		List<DeclarationNode> declarations = new ArrayList<DeclarationNode>();
		for (int i = PRE_DCLS_TOKENS; i < context.getChildCount()-POST_DCLS_TOKENS; ++i) {
			DeclarationNode declaration = (DeclarationNode) visit(context.getChild(i));
			declarations.add(declaration);
		}
		
		return new DataStructDefinitionNode(typeName, declarations);
	}
	
	public AbstractNode visitDataStructDcl(HelloParser.DataStructDclContext context) {
		return new DataStructDeclarationNode(context.Ident(0).getText(), context.Ident(1).getText());
	}
	
	public AbstractNode visitBasicArrayDcl(HelloParser.BasicArrayDclContext context) {
		return new ArrayDeclarationNode(context.type().getText(), context.Ident().getText(), (ExpressionNode) visit(context.expr()));
	}
	
	public AbstractNode visitStructArrayDcl(HelloParser.StructArrayDclContext context) {
		return new ArrayDeclarationNode(context.dataStructDcl().Ident(0).getText(), context.dataStructDcl().Ident(1).getText(), (ExpressionNode) visit(context.expr()));
	}
	
	public AbstractNode visitAssign(HelloParser.AssignContext context) {
		AssignmentNode.AssignmentType type;
		switch (context.assignmentOp().op.getType()) {
			case HelloLexer.OP_ASSIGN:
				type = AssignmentNode.AssignmentType.basic;
				break;
			case HelloLexer.OP_ADD_ASSIGN:
				type = AssignmentNode.AssignmentType.add;
				break;
			case HelloLexer.OP_SUB_ASSIGN:
				type = AssignmentNode.AssignmentType.sub;
				break;
			case HelloLexer.OP_MUL_ASSIGN:
				type = AssignmentNode.AssignmentType.mult;
				break;
			case HelloLexer.OP_DIV_ASSIGN:
				type = AssignmentNode.AssignmentType.div;
				break;
			case HelloLexer.OP_MOD_ASSIGN:
				type = AssignmentNode.AssignmentType.mod;
				break;
			default:
				throw new NotImplementedException();
		}
		
		return new AssignmentNode(	(GeneralIdentNode) visit(context.generalIdent()),
									type,
									(ExpressionNode) visit(context.expr()));
	}
	
	public AbstractNode visitCallStmt(HelloParser.CallStmtContext context) {
		GeneralIdentNode generalIdent;
		if (context.generalIdent() != null)
			generalIdent = (GeneralIdentNode) visit(context.generalIdent());
		else
			generalIdent = new GeneralIdentNode(new ArrayList<BaseIdentNode>());
		generalIdent.addIdent((BaseIdentNode) visit(context.funcCall()));
		return new CallStatementNode(generalIdent);
	}
	
	public AbstractNode visitIfThenStmt(HelloParser.IfThenStmtContext context) {
		ExpressionNode expr = (ExpressionNode) visit(context.expr());
		
		// Statements in "if" block
		List<StatementNode> ifBlockStatements = new ArrayList<StatementNode>();
		for (ParseTree child : nullSafe(context.block().stmts().children)) {
			StatementNode stmt = (StatementNode) visit(child);
			ifBlockStatements.add(stmt);
		}
		
		return new IfNode(expr, ifBlockStatements);
	}
	
	public AbstractNode visitIfElseStmt(HelloParser.IfElseStmtContext context) {
		ExpressionNode expr = (ExpressionNode) visit(context.expr());
		
		// Statements in "if" block
		List<StatementNode> ifBlockStatements = new ArrayList<StatementNode>();
		for (int i = 0; i < context.ifblock.stmts().getChildCount(); ++i) 
		for (ParseTree child : nullSafe(context.ifblock.stmts().children)){
			StatementNode stmt = (StatementNode) visit(child);
			ifBlockStatements.add(stmt);
		}
		
		// Statements in "else" block
		List<StatementNode> elseBlockStatements = new ArrayList<StatementNode>();
		for (ParseTree child : nullSafe(context.elseblock.stmts().children)) {
			StatementNode stmt = (StatementNode) visit(child);
			elseBlockStatements.add(stmt);
		}
			
		return new IfElseNode(expr, ifBlockStatements, elseBlockStatements);
	}

	public AbstractNode visitElseIfStmt(HelloParser.ElseIfStmtContext context) {
		ExpressionNode expr = (ExpressionNode) visit(context.expr());
		
		// Statements in "if" block
		List<StatementNode> ifBlockStatements = new ArrayList<StatementNode>();
		for (ParseTree child : nullSafe(context.block().stmts().children)) {
			StatementNode stmt = (StatementNode) visit(child);
			ifBlockStatements.add(stmt);
		}
		
		IfNode next = (IfNode) visit(context.ifStmt());
		return new ElseIfNode(expr, ifBlockStatements, next);
	}
	
	public AbstractNode visitWhileStmt(HelloParser.WhileStmtContext context) {	
		// Statements in block
		List<StatementNode> blockStatements = new ArrayList<StatementNode>();
		for (ParseTree child : nullSafe(context.block().stmts().children)) {
			StatementNode stmt = (StatementNode) visit(child);
			blockStatements.add(stmt);
		}
		
		List<ExpressionNode> expressions = new ArrayList<ExpressionNode>();	
		expressions.add((ExpressionNode) visit(context.expr()));
		return new WhileNode(expressions, blockStatements);		
	}
	
	public AbstractNode visitForAssignStmt(HelloParser.ForAssignStmtContext context) {
		// Statements in block
		List<StatementNode> blockStatements = new ArrayList<StatementNode>();
		for (ParseTree child : nullSafe(context.block().stmts().children)) {
			StatementNode stmt = (StatementNode) visit(child);
			blockStatements.add(stmt);
		}
		
		List<ExpressionNode> expressions = new ArrayList<ExpressionNode>();
		AssignmentNode assignment = new AssignmentNode(	(GeneralIdentNode) visit(context.basicAssignment().Ident()),
														AssignmentNode.AssignmentType.basic,
														(ExpressionNode) visit(context.basicAssignment().expr()));
		expressions.add((ExpressionNode) visit(context.second));
		expressions.add((ExpressionNode) visit(context.third));
		return new ForWithAssignmentNode(expressions, blockStatements, assignment);
	}
	
	public AbstractNode visitForDclStmt(HelloParser.ForDclStmtContext context) {
		// Statements in block
		List<StatementNode> blockStatements = new ArrayList<StatementNode>();
		for (ParseTree child : nullSafe(context.block().stmts().children)) {
			StatementNode stmt = (StatementNode) visit(child);
			blockStatements.add(stmt);
		}
		
		List<ExpressionNode> expressions = new ArrayList<ExpressionNode>();
		VarDeclarationNode varDcl = (VarDeclarationNode) visit(context.varDcl());
		expressions.add((ExpressionNode) visit(context.second));
		expressions.add((ExpressionNode) visit(context.third));
		return new ForWithDclNode(expressions, blockStatements, varDcl);
	}

	public AbstractNode visitForStmt(HelloParser.ForStmtContext context) {
		// Statements in block
		List<StatementNode> blockStatements = new ArrayList<StatementNode>();
		for (ParseTree child : nullSafe(context.block().stmts().children)) {
			StatementNode stmt = (StatementNode) visit(child);
			blockStatements.add(stmt);
		}
		
		List<ExpressionNode> expressions = new ArrayList<ExpressionNode>();
		expressions.add((ExpressionNode) visit(context.first));
		expressions.add((ExpressionNode) visit(context.second));
		expressions.add((ExpressionNode) visit(context.third));
		return new ForNode(expressions, blockStatements);
	}
	
	public AbstractNode visitRetValStmt(HelloParser.RetValStmtContext context) {
		return new ReturnNode((ExpressionNode) visit(context.expr()));			
	}
	
	public AbstractNode visitRetVoidStmt(HelloParser.RetVoidStmtContext context) {
		return new ReturnNode();
	}
	
	public AbstractNode visitExpr(HelloParser.ExprContext context) {
		return visit(context.logicalORExpr());
	}
	
	public AbstractNode visitEmptyLogORExpr(HelloParser.EmptyLogORExprContext context) {
		return visit(context.logicalANDExpr());
	}
	
	public AbstractNode visitLogORExpr(HelloParser.LogORExprContext context) {
		return new LogicalORExprNode(	(ExpressionNode) visit(context.logicalANDExpr()),
										(ExpressionNode) visit(context.logicalORExpr()));

	}
	
	public AbstractNode visitEmptyLogANDExpr(HelloParser.EmptyLogANDExprContext context) {
		return visit(context.equalityExpr());
	}
	
	public AbstractNode visitLogANDExpr(HelloParser.LogANDExprContext context) {
		return new LogicalANDExprNode(	(ExpressionNode) visit(context.equalityExpr()),
										(ExpressionNode) visit(context.logicalANDExpr()));
	}
		
	public AbstractNode visitEmptyEqualExpr(HelloParser.EmptyEqualExprContext context) {
		return visit(context.relationalExpr());
	}
	
	public AbstractNode visitEqualExpr(HelloParser.EqualExprContext context) {
		EqualityExprNode.EqualityType type;
		switch (context.op.getType()) {
			case HelloLexer.OP_EQ:
				type = EqualityExprNode.EqualityType.equal;
				break;
			case HelloLexer.OP_NEQ:
				type = EqualityExprNode.EqualityType.notEqual;
				break;
			default:
				throw new NotImplementedException();			
		}
		return new EqualityExprNode(	type,
										(ExpressionNode) visit(context.relationalExpr()),
										(ExpressionNode) visit(context.equalityExpr()));
	}
	
	public AbstractNode visitEmptyRelExpr(HelloParser.EmptyRelExprContext context) {
		return visit(context.additiveExpr());
	}
	
	public AbstractNode visitRelExpr(HelloParser.RelExprContext context) {
		RelationExprNode.RelationType type;
		switch (context.op.getType()) {
			case HelloLexer.OP_LT:
				type = RelationExprNode.RelationType.lessThan;
				break;
			case HelloLexer.OP_GT:
				type = RelationExprNode.RelationType.greaterThan;
				break;
			case HelloLexer.OP_LTE:
				type = RelationExprNode.RelationType.lessThanOrEqual;
				break;
			case HelloLexer.OP_GTE:
				type = RelationExprNode.RelationType.greaterThanOrEqual;
				break;
			default:
				throw new NotImplementedException();
		}
		return new RelationExprNode(	type,
										(ExpressionNode) visit(context.additiveExpr()),
										(ExpressionNode) visit(context.relationalExpr()));
	}
	
	public AbstractNode visitEmptyAddExpr(HelloParser.EmptyAddExprContext context) {
		return visit(context.multiplicationExpr());
	}
	
	public AbstractNode visitAddExpr(HelloParser.AddExprContext context) {
		AdditiveExprNode.AdditionType type;
		switch (context.op.getType()) {
			case HelloLexer.OP_ADD:
				type = AdditiveExprNode.AdditionType.add;
				break;
			case HelloLexer.OP_SUB:
				type = AdditiveExprNode.AdditionType.sub;
				break;
			default:
				throw new NotImplementedException();
		}
		return new AdditiveExprNode(	type,
										(ExpressionNode) visit(context.multiplicationExpr()),
										(ExpressionNode) visit(context.additiveExpr()));

	}
	
	public AbstractNode visitEmptyMultExpr(HelloParser.EmptyMultExprContext context) {
		return visit(context.unaryExpr());
	}
	
	public AbstractNode visitMultExpr(HelloParser.MultExprContext context) {
		MultExprNode.MultiplicationType type;
		switch (context.op.getType()) {
			case HelloLexer.OP_MUL:
				type = MultExprNode.MultiplicationType.mult;
				break;
			case HelloLexer.OP_DIV:
				type = MultExprNode.MultiplicationType.div;
				break;
			case HelloLexer.OP_MOD:
				type = MultExprNode.MultiplicationType.mod;
				break;
			default:
				throw new NotImplementedException();
		}
		return new MultExprNode(	type,
									(ExpressionNode) visit(context.unaryExpr()),
									(ExpressionNode) visit(context.multiplicationExpr()));
	}
	
	public AbstractNode visitEmptyUnExpr(HelloParser.EmptyUnExprContext context) {
		return visit(context.primaryExpr());
	}
	
	public AbstractNode visitUnExpr(HelloParser.UnExprContext context) {
		return new UnaryExprNode((ExpressionNode) visit(context.unaryExpr()));
	}
	
	public AbstractNode visitGeneralPrimary(HelloParser.GeneralPrimaryContext context) {
		return visit(context.generalIdent());
	}
	
	public AbstractNode visitTextLitPrimary(HelloParser.TextLitPrimaryContext context) {
		return new TextLiteralNode(context.TextLit().getText());
	}
	
	public AbstractNode visitNumLitPrimary(HelloParser.NumLitPrimaryContext context) {
		return new NumLiteralNode(Double.parseDouble(context.NumLit().getText()));
	}
	
	public AbstractNode visitBoolLitPrimary(HelloParser.BoolLitPrimaryContext context) {
		return new BoolLiteralNode(Boolean.parseBoolean(context.BoolLit().getText()));
	}
	
	public AbstractNode visitParenPrimary(HelloParser.ParenPrimaryContext context) {
		return new ParenthesesNode((ExpressionNode) visit(context.expr()));
	}
	
	public AbstractNode visitGeneralIdent(HelloParser.GeneralIdentContext context) {
		List<BaseIdentNode> idents = new ArrayList<BaseIdentNode>();
		for (HelloParser.BaseIdentContext child : nullSafe(context.baseIdent())) {
			BaseIdentNode ident = (BaseIdentNode) visit(child);
			idents.add(ident);
		}
		return new GeneralIdentNode(idents);
	}
	
	public AbstractNode visitFuncBaseIdent(HelloParser.FuncBaseIdentContext context) {
		List<ExpressionNode> arguments = new ArrayList<ExpressionNode>();
		for (HelloParser.ExprContext child : nullSafe(context.funcCall().argList().expr())) {
			ExpressionNode arg = (ExpressionNode) visit(child);
			arguments.add(arg);
		}
		
		if (context.expr() == null)
			return new FuncCallNode(context.funcCall().Ident().getText(), arguments);
					
		return new FuncCallNode(	context.funcCall().Ident().getText(),
									arguments,
									(ExpressionNode) visit(context.expr()));				
	}
	
	public AbstractNode visitIdentBaseIdent(HelloParser.IdentBaseIdentContext context) {
			if (context.expr() == null)
				return new BaseIdentNode(context.Ident().getText());
			return new BaseIdentNode(	context.Ident().getText(),
										(ExpressionNode) visit(context.expr()));
	}
	
	public AbstractNode visitFuncCall(HelloParser.FuncCallContext context) {
		List<ExpressionNode> arguments = new ArrayList<ExpressionNode>();
		for (HelloParser.ExprContext child : nullSafe(context.argList().expr())) {
			ExpressionNode arg = (ExpressionNode) visit(child);
			arguments.add(arg);
		}
		
		return new FuncCallNode(context.Ident().getText(), arguments);
	}
	
    private static <T> Collection<T> nullSafe(Collection<T> c) {
        return (c == null) ? Collections.<T>emptyList() : c;
    }
}
