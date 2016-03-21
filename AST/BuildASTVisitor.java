import java.util.*;

public class BuildASTVisitor extends HelloBaseVisitor<AbstractNode> {
	public AbstractNode visitCompileUnit(HelloParser.ProgContext context) {
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
		return new DataStructDeclarationNode(context.Ident(0).getText(), context.Ident(0).getText());
	}
	
	public AbstractNode visitArrayDcl(HelloParser.ArrayDclContext context) {
		if (context.getRuleIndex() == 0)
			return new ArrayDeclarationNode(context.type().getText(), context.Ident().getText(), (ExpressionNode) visit(context.expr()));
		else 
			return new ArrayDeclarationNode(context.dataStructDcl().Ident(0).getText(), context.dataStructDcl().Ident(1).getText(), (ExpressionNode) visit(context.expr()));
	}
	
	public AbstractNode visitAssign(HelloParser.AssignContext context) {
		return new AssignmentNode(	(GeneralIdentNode) visit(context.generalIdent()),
									AssignmentNode.AssignmentType.values()[context.assignmentOp().getRuleIndex()],
									(ExpressionNode) visit(context.expr()));
	}
	
	public AbstractNode visitCallStmt(HelloParser.CallStmtContext context) {
		GeneralIdentNode generalIdent = (GeneralIdentNode) visit(context.generalIdent());
		generalIdent.addIdent((BaseIdentNode) visit(context.funcCall()));
		return new CallStatementNode(generalIdent);
	}
	
	public AbstractNode visitIfStmt(HelloParser.IfStmtContext context) {
		
		IfNode.IfType type = IfNode.IfType.values()[context.getRuleIndex()];
		
		ExpressionNode expr = (ExpressionNode) visit(context.expr());
		
		// Statements in "if" block
		List<StatementNode> ifBlockStatements = new ArrayList<StatementNode>();
		for (int i = 0; i < context.block(0).stmts().getChildCount(); ++i) {
			StatementNode stmt = (StatementNode) visit(context.block(0).stmts().getChild(i));
			ifBlockStatements.add(stmt);
		}
		
		if (type == IfNode.IfType.IfElse) {
			// Statements in "else" block
			List<StatementNode> elseBlockStatements = new ArrayList<StatementNode>();
			for (int i = 0; i < context.block(1).stmts().getChildCount(); ++i) {
				StatementNode stmt = (StatementNode) visit(context.block(1).stmts().getChild(i));
				elseBlockStatements.add(stmt);
			}
			
			return new IfNode(expr, ifBlockStatements, elseBlockStatements);
		}
		else if (type == IfNode.IfType.ElseIf) {
			IfNode next = (IfNode) visit(context.ifStmt());
			return new IfNode(expr, ifBlockStatements, next);
		}
		
		return new IfNode(expr, ifBlockStatements);
	}
	
	public AbstractNode visitIterStmt(HelloParser.IterStmtContext context) {
		
		IterationNode.IterationType type = IterationNode.IterationType.For;
		switch (context.getRuleIndex()) {
			case 0:
				type = IterationNode.IterationType.While;
				break;
			case 1:
				if (!context.basicAssignment().isEmpty())			// For testing, change later
					type = IterationNode.IterationType.ForWithAssignment;
				else if (!context.varDcl().isEmpty())
					type = IterationNode.IterationType.ForWithDcl;
				break;
			case 2:
				type = IterationNode.IterationType.For;
				break;
			default:
				break;
		}		
		
		// Statements in block
		List<StatementNode> blockStatements = new ArrayList<StatementNode>();
		for (int i = 0; i < context.block().stmts().getChildCount(); ++i) {
			StatementNode stmt = (StatementNode) visit(context.block().stmts().getChild(i));
			blockStatements.add(stmt);
		}
		
		List<ExpressionNode> expressions = new ArrayList<ExpressionNode>();
		switch (type) {
			case While:	
				expressions.add((ExpressionNode) visit(context.expr(0)));
				return new IterationNode(type, expressions, blockStatements);
			case ForWithAssignment:
				AssignmentNode assignment = new AssignmentNode(	(GeneralIdentNode) visit(context.basicAssignment().Ident()),
																AssignmentNode.AssignmentType.basic,
																(ExpressionNode) visit(context.basicAssignment().expr()));
				expressions.add((ExpressionNode) visit(context.expr(0)));
				expressions.add((ExpressionNode) visit(context.expr(1)));
				return new IterationNode(expressions, blockStatements, assignment);
			case ForWithDcl:
				VarDeclarationNode varDcl = (VarDeclarationNode) visit(context.varDcl());
				expressions.add((ExpressionNode) visit(context.expr(0)));
				expressions.add((ExpressionNode) visit(context.expr(1)));
				return new IterationNode(expressions, blockStatements, varDcl);
			default:
				expressions.add((ExpressionNode) visit(context.expr(0)));
				expressions.add((ExpressionNode) visit(context.expr(1)));
				expressions.add((ExpressionNode) visit(context.expr(2)));
				return new IterationNode(type, expressions, blockStatements);
		}
		
	}
	
	public AbstractNode visitReturnStmt(HelloParser.ReturnStmtContext context) {
		if(context.getRuleIndex() == 0)
			return new ReturnNode((ExpressionNode) visit(context.expr()));
		else
			return new ReturnNode();
	}
	
	public AbstractNode visitExpr(HelloParser.ExprContext context) {
		return visit(context.logicalORExpr());
	}
	
	public AbstractNode visitLogicalORExpr(HelloParser.LogicalORExprContext context) {
		if (context.getRuleIndex() == 0) 
			return visit(context.logicalANDExpr());
		else
			return new LogicalORExprNode(	(ExpressionNode) visit(context.logicalANDExpr()),
											(ExpressionNode) visit(context.logicalORExpr()));
	}
	
	public AbstractNode visitLogicalANDExpr(HelloParser.LogicalANDExprContext context) {
		if (context.getRuleIndex() == 0) 
			return visit(context.equalityExpr());
		else
			return new LogicalANDExprNode(	(ExpressionNode) visit(context.equalityExpr()),
											(ExpressionNode) visit(context.logicalANDExpr()));
	}
	
	public AbstractNode visitEqualityExpr(HelloParser.EqualityExprContext context) {
		if (context.getRuleIndex() == 0) 
			return visit(context.relationalExpr());
		else {
			return new EqualityExprNode(	// Insert equality type
											(ExpressionNode) visit(context.relationalExpr()),
											(ExpressionNode) visit(context.equalityExpr()));
		}
	}
	
	public AbstractNode visitRelationalExpr(HelloParser.RelationalExprContext context) {
		if (context.getRuleIndex() == 0) 
			return visit(context.additiveExpr());
		else {
			return new RelationExprNode(	// Insert relation type
											(ExpressionNode) visit(context.additiveExpr()),
											(ExpressionNode) visit(context.relationalExpr()));
		}
	}
	
	public AbstractNode visitAdditiveExpr(HelloParser.AdditiveExprContext context) {
		if (context.getRuleIndex() == 0) 
			return visit(context.multExpr());
		else {
			return new AdditiveExprNode(	// Insert addition type
											(ExpressionNode) visit(context.multExpr()),
											(ExpressionNode) visit(context.additiveExpr()));
		}
	}
	
	public AbstractNode visitMultExpr(HelloParser.MultExprContext context) {
		if (context.getRuleIndex() == 0) 
			return visit(context.unaryExpr());
		else {
			return new MultExprNode(	// Insert multiplication type
										(ExpressionNode) visit(context.unaryExpr()),
										(ExpressionNode) visit(context.multExpr()));
		}
	}
	
	public AbstractNode visitUnaryExpr(HelloParser.UnaryExprContext context) {
		if (context.getRuleIndex() == 0) 
			return visit(context.primaryExpr());
		else {
			return new UnaryExprNode((ExpressionNode) visit(context.unaryExpr()));
		}
	}
	
	public AbstractNode visitPrimaryExpr(HelloParser.PrimaryExprContext context) {
		switch (context.getRuleIndex()) {
			case 0:
				return visit(context.generalIdent());
			case 1:
				return new TextLiteralNode(context.TextLit().getText());
			case 2:
				return new NumLiteralNode(Double.parseDouble(context.NumLit().getText()));
			case 3:
				return new BoolLiteralNode(Boolean.parseBoolean(context.BoolLit().getText()));
			case 4:
				return new ParenthesesNode((ExpressionNode) visit(context.expr()));
			default:
				return new TextLiteralNode(context.TextLit().getText()); // ERROR
				// ERROR
				// ERROR - should be exception
		}
	}
	
	public AbstractNode visitGeneralIdent(HelloParser.GeneralIdentContext context) {
		List<BaseIdentNode> idents = new ArrayList<BaseIdentNode>();
		for (int i = 0; i < context.baseIdent().size(); ++i) {
			BaseIdentNode ident = (BaseIdentNode) visit(context.baseIdent(i));
			idents.add(ident);
		}
		return new GeneralIdentNode(idents);
	}
	
	public AbstractNode visitBaseIdent(HelloParser.BaseIdentContext context) {
		if (context.funcCall().isEmpty()) {
			if (context.expr().isEmpty())
				return new BaseIdentNode(context.Ident().getText());
			return new BaseIdentNode(	context.Ident().getText(),
										(ExpressionNode) visit(context.expr()));
		}
		
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
	
	public AbstractNode visitFuncCall(HelloParser.FuncCallContext context) {
		List<VarNode> arguments = new ArrayList<VarNode>();
		for (int i = 0; i < context.argList().expr().size(); ++i) {
			VarNode arg = (VarNode) visit(context.argList().expr(i));
			arguments.add(arg);
		}
		
		return new FuncCallNode(context.Ident().getText(), arguments);
	}
}
