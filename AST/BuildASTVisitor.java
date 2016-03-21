import java.util.*;

public class BuildASTVisitor extends HelloBaseVisitor<AbstractNode> {
	public AbstractNode visitProg(HelloParser.ProgContext context) {
		return visit(context.dcls());
	}
	
	public AbstractNode visitDcls(HelloParser.DclsContext context) {
		List<DeclarationNode> declarations = new ArrayList<DeclarationNode>();
		for (int i = 0; i < context.getChildCount(); ++i) {
			DeclarationNode dcl = (DeclarationNode) visit(context.getChild(i));
			declarations.add(dcl);		
		}
		return new ProgramNode(declarations);
	}
	
	public AbstractNode visitRobonameAssign(HelloParser.RobonameAssignContext context) {
		return new RobotDeclarationNode(RobotDeclarationNode.RobotDeclarationType.name, context.TextLit().getText());
	}
	
	public AbstractNode visitInitBlock(HelloParser.InitBlockContext context) {
		List<StatementNode> statements = new ArrayList<StatementNode>();
		
		for (int i = 0; i < context.block().stmts().getChildCount(); ++i) {
			StatementNode stmt = (StatementNode) visit(context.block().stmts().getChild(i));
			statements.add(stmt);		
		}
		
		return new RobotDeclarationNode(RobotDeclarationNode.RobotDeclarationType.initialization, statements);
	}
	
	public AbstractNode visitBehaviorBlock(HelloParser.BehaviorBlockContext context) {
		List<StatementNode> statements = new ArrayList<StatementNode>();
		
		for (int i = 0; i < context.block().stmts().getChildCount(); ++i) {
			StatementNode stmt = (StatementNode) visit(context.block().stmts().getChild(i));
			statements.add(stmt);		
		}
		
		return new RobotDeclarationNode(RobotDeclarationNode.RobotDeclarationType.behavior, statements);
	}
	
	public AbstractNode visitEventDcl(HelloParser.EventDclContext context) {
		String ident = context.Ident().getText();
		VarNode param = new VarNode(context.eventParam().eventType().getText(), context.eventParam().Ident().getText());
		
		List<StatementNode> statements = new ArrayList<StatementNode>();
		for (int i = 0; i < context.block().stmts().getChildCount(); ++i) {
			StatementNode stmt = (StatementNode) visit(context.block().stmts().getChild(i));
			statements.add(stmt);		
		}
		
		return new EventDeclarationNode(ident, param, statements);	
	}
	
	public AbstractNode visitFuncDcl(HelloParser.FuncDclContext context) {
		// List of return types
		List<TypeNode> typeList = new ArrayList<TypeNode>();
		for (int i = 0; i < context.typeList().getChildCount(); ++i) {
			TypeNode type = new TypeNode(context.typeList().getChild(i).getText());
			typeList.add(type);
		}
		
		String ident = context.Ident().getText();
		
		List<VarNode> paramList = new ArrayList<VarNode>();
		for (int i = 0; i < context.paramList().param().size(); ++i) {
			VarNode param = new VarNode(context.paramList().param(i).type().getText(), context.paramList().param(i).Ident().getText());
			paramList.add(param);
		}

		List<StatementNode> statements = new ArrayList<StatementNode>();
		for (int i = 0; i < context.block().stmts().getChildCount(); ++i) {
			StatementNode stmt = (StatementNode) visit(context.block().stmts().getChild(i));
			statements.add(stmt);		
		}
		
		return new FuncDeclarationNode(typeList, ident, paramList, statements);	
	}
	
	public AbstractNode visitVarDcl(HelloParser.VarDclContext context) {
		return new VarDeclarationNode(	context.type().getText(),
										context.basicAssignment().Ident().getText(),
										(ExpressionNode) visit(context.basicAssignment().expr()));
	}
	
	public AbstractNode visitDataStructDef(HelloParser.DataStructDefContext context) {
		String typeName = context.Ident().getText();
		
		List<DeclarationNode> declarations = new ArrayList<DeclarationNode>();
		for (int i = 0; i < context.getChildCount(); ++i) {
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
		return new AssignmentNode(	(GeneralIdentNode) visit(context.generalIdent()),
									AssignmentNode.AssignmentType.values()[context.assignmentOp().op.getType()],
									(ExpressionNode) visit(context.expr()));
	}
	
	public AbstractNode visitCallStmt(HelloParser.CallStmtContext context) {
		GeneralIdentNode generalIdent = (GeneralIdentNode) visit(context.generalIdent());
		generalIdent.addIdent((BaseIdentNode) visit(context.funcCall()));
		return new CallStatementNode(generalIdent);
	}
	
	public AbstractNode visitIfThenStmt(HelloParser.IfThenStmtContext context) {
		ExpressionNode expr = (ExpressionNode) visit(context.expr());
		
		// Statements in "if" block
		List<StatementNode> ifBlockStatements = new ArrayList<StatementNode>();
		for (int i = 0; i < context.block().stmts().getChildCount(); ++i) {
			StatementNode stmt = (StatementNode) visit(context.block().stmts().getChild(i));
			ifBlockStatements.add(stmt);
		}
		
		return new IfNode(expr, ifBlockStatements);
	}
	
	public AbstractNode visitIfElseStmt(HelloParser.IfElseStmtContext context) {
		ExpressionNode expr = (ExpressionNode) visit(context.expr());
		
		// Statements in "if" block
		List<StatementNode> ifBlockStatements = new ArrayList<StatementNode>();
		for (int i = 0; i < context.ifblock.stmts().getChildCount(); ++i) {
			StatementNode stmt = (StatementNode) visit(context.ifblock.stmts().getChild(i));
			ifBlockStatements.add(stmt);
		}
		
		// Statements in "else" block
		List<StatementNode> elseBlockStatements = new ArrayList<StatementNode>();
		for (int i = 0; i < context.elseblock.stmts().getChildCount(); ++i) {
			StatementNode stmt = (StatementNode) visit(context.elseblock.stmts().getChild(i));
			elseBlockStatements.add(stmt);
		}
			
		return new IfNode(expr, ifBlockStatements, elseBlockStatements);
	}

	public AbstractNode visitElseIfStmt(HelloParser.ElseIfStmtContext context) {
		ExpressionNode expr = (ExpressionNode) visit(context.expr());
		
		// Statements in "if" block
		List<StatementNode> ifBlockStatements = new ArrayList<StatementNode>();
		for (int i = 0; i < context.block().stmts().getChildCount(); ++i) {
			StatementNode stmt = (StatementNode) visit(context.block().stmts().getChild(i));
			ifBlockStatements.add(stmt);
		}
		
		IfNode next = (IfNode) visit(context.ifStmt());
		return new IfNode(expr, ifBlockStatements, next);
	}
	
	public AbstractNode visitWhileStmt(HelloParser.WhileStmtContext context) {	
		// Statements in block
		List<StatementNode> blockStatements = new ArrayList<StatementNode>();
		for (int i = 0; i < context.block().stmts().getChildCount(); ++i) {
			StatementNode stmt = (StatementNode) visit(context.block().stmts().getChild(i));
			blockStatements.add(stmt);
		}
		
		List<ExpressionNode> expressions = new ArrayList<ExpressionNode>();	
		expressions.add((ExpressionNode) visit(context.expr()));
		return new IterationNode(IterationNode.IterationType.While, expressions, blockStatements);		
	}
	
	public AbstractNode visitForAssignStmt(HelloParser.ForAssignStmtContext context) {
		// Statements in block
		List<StatementNode> blockStatements = new ArrayList<StatementNode>();
		for (int i = 0; i < context.block().stmts().getChildCount(); ++i) {
			StatementNode stmt = (StatementNode) visit(context.block().stmts().getChild(i));
			blockStatements.add(stmt);
		}
		
		List<ExpressionNode> expressions = new ArrayList<ExpressionNode>();
		AssignmentNode assignment = new AssignmentNode(	(GeneralIdentNode) visit(context.basicAssignment().Ident()),
														AssignmentNode.AssignmentType.basic,
														(ExpressionNode) visit(context.basicAssignment().expr()));
		expressions.add((ExpressionNode) visit(context.second));
		expressions.add((ExpressionNode) visit(context.third));
		return new IterationNode(expressions, blockStatements, assignment);
	}
	
	public AbstractNode visitForDclStmt(HelloParser.ForDclStmtContext context) {
		// Statements in block
		List<StatementNode> blockStatements = new ArrayList<StatementNode>();
		for (int i = 0; i < context.block().stmts().getChildCount(); ++i) {
			StatementNode stmt = (StatementNode) visit(context.block().stmts().getChild(i));
			blockStatements.add(stmt);
		}
		
		List<ExpressionNode> expressions = new ArrayList<ExpressionNode>();
		VarDeclarationNode varDcl = (VarDeclarationNode) visit(context.varDcl());
		expressions.add((ExpressionNode) visit(context.second));
		expressions.add((ExpressionNode) visit(context.third));
		return new IterationNode(expressions, blockStatements, varDcl);
	}

	public AbstractNode visitForStmt(HelloParser.ForStmtContext context) {
		// Statements in block
		List<StatementNode> blockStatements = new ArrayList<StatementNode>();
		for (int i = 0; i < context.block().stmts().getChildCount(); ++i) {
			StatementNode stmt = (StatementNode) visit(context.block().stmts().getChild(i));
			blockStatements.add(stmt);
		}
		
		List<ExpressionNode> expressions = new ArrayList<ExpressionNode>();
		expressions.add((ExpressionNode) visit(context.first));
		expressions.add((ExpressionNode) visit(context.second));
		expressions.add((ExpressionNode) visit(context.third));
		return new IterationNode(IterationNode.IterationType.For, expressions, blockStatements);
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
				// throw new NotImplementedException();
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
				// throw new NotImplementedException();
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
				// throw new NotImplementedException();
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
				// throw new NotImplementedException();
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
		for (int i = 0; i < context.baseIdent().size(); ++i) {
			BaseIdentNode ident = (BaseIdentNode) visit(context.baseIdent(i));
			idents.add(ident);
		}
		return new GeneralIdentNode(idents);
	}
	
	public AbstractNode visitFuncBaseIdent(HelloParser.FuncBaseIdentContext context) {
		List<VarNode> arguments = new ArrayList<VarNode>();
		for (int i = 0; i < context.funcCall().argList().expr().size(); ++i) {
			VarNode arg = (VarNode) visit(context.funcCall().argList().expr(i));
			arguments.add(arg);
		}
		
		if (context.expr().isEmpty())
			return new FuncCallNode(context.funcCall().Ident().getText(), arguments);
					
		return new FuncCallNode(	context.funcCall().Ident().getText(),
									arguments,
									(ExpressionNode) visit(context.expr()));				
	}
	
	public AbstractNode visitIdentBaseIdent(HelloParser.IdentBaseIdentContext context) {
			if (context.expr().isEmpty())
				return new BaseIdentNode(context.Ident().getText());
			return new BaseIdentNode(	context.Ident().getText(),
										(ExpressionNode) visit(context.expr()));
	}
	
	public AbstractNode visitFuncCall(HelloParser.FuncCallContext context) {
		List<VarNode> arguments = new ArrayList<VarNode>();
		for (int i = 0; i < context.argList().expr().size(); ++i) {
			VarNode arg = (VarNode) visit(context.argList().expr(i));
			arguments.add(arg);
		}
		
		return new FuncCallNode(context.Ident().getText(), arguments);
	}
}
