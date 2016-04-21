import java.util.*;

import nodes.*;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import exceptions.*;


public class BuildASTVisitor extends HelloBaseVisitor<AbstractNode> {
	public AbstractNode visitProg(HelloParser.ProgContext context) {
		return visit(context.dcls());
	}
	
	public AbstractNode visitDcls(HelloParser.DclsContext context) {
		List<ParseTree> input = context.children;
		List<DeclarationNode> declarations = CreateList(input, DeclarationNode.class);
		
		return new ProgramNode(context.start.getLine(),
							   context.start.getCharPositionInLine(),
							   declarations);
	}
	
	public AbstractNode visitRobonameAssign(HelloParser.RobonameAssignContext context) {
		return new RobotDeclarationNode(context.start.getLine(),
										context.start.getCharPositionInLine(),
										RobotDeclarationNode.RobotDeclarationType.name,
										context.TextLit().getText());
	}
	
	public AbstractNode visitInitBlock(HelloParser.InitBlockContext context) {
		
		List<ParseTree> input = context.block().stmts().children;
		List<StatementNode> statements = CreateList(input, StatementNode.class);
		
		return new RobotDeclarationNode(context.start.getLine(),
										context.start.getCharPositionInLine(),
										RobotDeclarationNode.RobotDeclarationType.initialization, 
										statements);
	}
	
	public AbstractNode visitBehaviorBlock(HelloParser.BehaviorBlockContext context) {
		
		List<ParseTree> input = context.block().stmts().children;
		List<StatementNode> statements = CreateList(input, StatementNode.class);
		
		return new RobotDeclarationNode(context.start.getLine(),
										context.start.getCharPositionInLine(),
										RobotDeclarationNode.RobotDeclarationType.behavior,
										statements);
	}
	
	public AbstractNode visitEventDcl(HelloParser.EventDclContext context) {
		String ident = context.Ident().getText();
		VarNode param = new VarNode(context.eventParam().start.getLine(),
									context.eventParam().start.getCharPositionInLine(),
									context.eventParam().eventType().getText(),
									context.eventParam().Ident().getText());
		
		List<ParseTree> input = context.block().stmts().children;
		List<StatementNode> statements = CreateList(input, StatementNode.class);
		
		return new EventDeclarationNode(context.start.getLine(),
										context.start.getCharPositionInLine(),
										ident,
										param,
										statements);	
	}
	
	public AbstractNode visitFuncDcl(HelloParser.FuncDclContext context) {
		// List of return types
		List<TypeNode> typeList = new ArrayList<TypeNode>();
		if (context.typeList().getRuleContext() instanceof HelloParser.GeneralTypeListContext)
			for (ParseTree child : nullSafe(context.typeList().children)){
				if (child instanceof TerminalNode)		// Skip commas
					continue;
				TypeNode type;
				//System.out.println(child.getText());
				if (child instanceof HelloParser.TypeGeneralTypeContext)
				{
					type = new TypeNode(((HelloParser.GeneralTypeContext) child).start.getLine(),
										((HelloParser.GeneralTypeContext) child).start.getCharPositionInLine(),
										child.getText());
					
				}
				else if (child.getChild(0) instanceof HelloParser.StructTypeContext) {
					type = new TypeNode(((HelloParser.GeneralTypeContext) child).start.getLine(),
							((HelloParser.GeneralTypeContext) child).start.getCharPositionInLine(),
							((HelloParser.StructTypeContext) child.getChild(0)).Ident().getText());
				}
				else if (child.getChild(0) instanceof HelloParser.ArrayTypeContext) {
					type = new TypeNode(((HelloParser.GeneralTypeContext) child).start.getLine(),
										((HelloParser.GeneralTypeContext) child).start.getCharPositionInLine(),
										((HelloParser.ArrayTypeContext) child.getChild(0)).type().getText() + "[]");
				}
				else if (child.getChild(0) instanceof HelloParser.StructArrayTypeContext) {
					type = new TypeNode(((HelloParser.GeneralTypeContext) child).start.getLine(),
										((HelloParser.GeneralTypeContext) child).start.getCharPositionInLine(),
										((HelloParser.StructArrayTypeContext) child.getChild(0)).Ident().getText() + "[]");
				}
				else
					throw new NotImplementedException();
				
				typeList.add(type);
			}
		else
			typeList.add(new TypeNode(context.typeList().start.getLine(),
									  context.typeList().start.getCharPositionInLine(),
									  "void"));
		
		String ident = context.Ident().getText();
		
		List<VarNode> paramList = new ArrayList<VarNode>();
		for (HelloParser.ParamContext child : nullSafe(context.paramList().param())) {
			VarNode param;
			if (child.generalType().getRuleContext() instanceof HelloParser.TypeGeneralTypeContext)
				param = new VarNode(child.start.getLine(),
									child.start.getCharPositionInLine(),
									child.generalType().getChild(0).getText(),
									child.Ident().getText());
			else {
				HelloParser.ComplexTypeContext ct = ((HelloParser.ComplexTypeContext) child.generalType().getChild(0));
				if (ct.getRuleContext() instanceof HelloParser.StructTypeContext)
					param = new VarNode(child.start.getLine(),
										child.start.getCharPositionInLine(),
										((HelloParser.StructTypeContext) ct).Ident().getText(),
										child.Ident().getText());
				else if (ct.getRuleContext() instanceof HelloParser.ArrayTypeContext)
					param = new VarNode(child.start.getLine(),
										child.start.getCharPositionInLine(),
										((HelloParser.ArrayTypeContext) ct).type().getText() + "[]",
										child.Ident().getText());
				else if (ct.getRuleContext() instanceof HelloParser.StructArrayTypeContext)
					param = new VarNode(child.start.getLine(),
										child.start.getCharPositionInLine(),
										((HelloParser.StructArrayTypeContext) ct).Ident().getText() + "[]",
										child.Ident().getText());
				else
					throw new NotImplementedException();
			}
			paramList.add(param);
		}
		List<ParseTree> input = context.block().stmts().children;
		List<StatementNode> statements = CreateList(input, StatementNode.class);
		
		return new FuncDeclarationNode(context.start.getLine(),
									   context.start.getCharPositionInLine(),
									   typeList,
									   ident,
									   paramList,
									   statements);	
	}
	
	public AbstractNode visitVar(HelloParser.VarContext context){
		return new VarNode(context.start.getLine(),
				   		   context.start.getCharPositionInLine(),
				           context.type().getText(),
				           context.Ident().getText());
	}
	
	public AbstractNode visitVarDcl(HelloParser.VarDclContext context) {
		List<HelloParser.VarContext> input = context.var();
		List<VarNode> variable = CreateList(input, VarNode.class);
		ExpressionNode expr = (ExpressionNode) visit(context.expr());
		return new VarDeclarationNode(context.start.getLine(),
									  context.start.getCharPositionInLine(),
									  variable,expr);
	}
	
	public AbstractNode visitDataStructDef(HelloParser.DataStructDefContext context) {
		String typeName = context.Ident().getText();
		List<DeclarationNode> declarations = new ArrayList<DeclarationNode>();
		for(ParseTree o : context.children){
			if(o instanceof ParserRuleContext){
				DeclarationNode declaration = (DeclarationNode) visit(o);
				declarations.add(declaration);
			}
		}
		
		return new DataStructDefinitionNode(context.start.getLine(),
											context.start.getCharPositionInLine(),
											typeName,
											declarations);
	}
	
	public AbstractNode visitDataStructDcl(HelloParser.DataStructDclContext context) {
			return new DataStructDeclarationNode(context.start.getLine(),
					 context.start.getCharPositionInLine(),
					 context.Ident(0).getText(), context.Ident(1).getText());
	}
	
	public AbstractNode visitBasicArrayDcl(HelloParser.BasicArrayDclContext context) {
		return new ArrayDeclarationNode(context.start.getLine(),
										context.start.getCharPositionInLine(),
										context.type().getText(), context.Ident().getText(), (ExpressionNode) visit(context.expr()));
	}
	
	public AbstractNode visitStructArrayDcl(HelloParser.StructArrayDclContext context) {
		return new ArrayDeclarationNode(context.start.getLine(),
										context.start.getCharPositionInLine(),
										context.dataStructDcl().Ident(0).getText(), context.dataStructDcl().Ident(1).getText(), (ExpressionNode) visit(context.expr()));
	}
	
	public AbstractNode visitBasicSizelessDcl(HelloParser.BasicSizelessDclContext context) {
		return new ArrayDeclarationNode(context.start.getLine(),
										context.start.getCharPositionInLine(),
										context.type().getText(), context.Ident().getText(), null);
	}
	
	public AbstractNode visitStructSizelessDcl(HelloParser.StructSizelessDclContext context) {
		return new ArrayDeclarationNode(context.start.getLine(),
										context.start.getCharPositionInLine(),
										context.dataStructDcl().Ident(0).getText(), context.dataStructDcl().Ident(1).getText(), null);
	}
	
	public AbstractNode visitAssignHelp(HelloParser.AssignHelpContext context){
		if(context.generalIdent() != null)
			return visit(context.generalIdent());
		else if(context.var() != null)
			return visit(context.var());
		else if(context.dataStructDcl() != null)
			return visit(context.dataStructDcl());
		else if(context.arrayDcl() != null)
			return visit(context.arrayDcl());
		else if(context.sizelessArrayDcl() != null)
			return visit(context.sizelessArrayDcl());
		else
			throw new NullPointerException("Both generalIdent, Var and dataStructDcl are null");
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
		List<AbstractNode> output = new ArrayList<AbstractNode>();
		List<HelloParser.AssignHelpContext> input = context.assignHelp();
		for(HelloParser.AssignHelpContext o : input){
			output.add(visit(o));
		}
		return new AssignmentNode(context.start.getLine(),
								  context.start.getCharPositionInLine(),
								  output,
								  type,
								  (ExpressionNode) visit(context.expr()));
	}
	
	public AbstractNode visitCallStmt(HelloParser.CallStmtContext context) {
		GeneralIdentNode generalIdent;
		if (context.generalIdent() != null)
			generalIdent = (GeneralIdentNode) visit(context.generalIdent());
		else
			generalIdent = new GeneralIdentNode(context.start.getLine(),
												context.start.getCharPositionInLine(),
												new ArrayList<BaseIdentNode>());
		generalIdent.addIdent((BaseIdentNode) visit(context.funcCall()));
		return new CallStatementNode(context.start.getLine(),
									 context.start.getCharPositionInLine(),
									 generalIdent);
	}
	
	public AbstractNode visitIfThenStmt(HelloParser.IfThenStmtContext context) {
		ExpressionNode expr = (ExpressionNode) visit(context.expr());
		
		// Statements in "if" block
		List<ParseTree> input = context.block().stmts().children;
		List<StatementNode> ifBlockStatements = CreateList(input, StatementNode.class);
		
		return new IfNode(context.start.getLine(),
						  context.start.getCharPositionInLine(),
						  expr, ifBlockStatements);
	}
	
	public AbstractNode visitIfElseStmt(HelloParser.IfElseStmtContext context) {
		ExpressionNode expr = (ExpressionNode) visit(context.expr());
		
		// Statements in "if" block
		List<ParseTree> input = context.ifblock.stmts().children;
		List<StatementNode> ifBlockStatements = CreateList(input, StatementNode.class);
		
		// Statements in "else" block
		List<StatementNode> elseBlockStatements = new ArrayList<StatementNode>();
		for (ParseTree child : nullSafe(context.elseblock.stmts().children)) {
			StatementNode stmt = (StatementNode) visit(child);
			elseBlockStatements.add(stmt);
		}
			
		return new IfElseNode(context.start.getLine(),
							  context.start.getCharPositionInLine(),
							  expr, ifBlockStatements, elseBlockStatements);
	}

	public AbstractNode visitElseIfStmt(HelloParser.ElseIfStmtContext context) {
		ExpressionNode expr = (ExpressionNode) visit(context.expr());
		
		// Statements in "if" block
		List<ParseTree> input = context.block().stmts().children;
		List<StatementNode> ifBlockStatements = CreateList(input, StatementNode.class);
		
		IfNode next = (IfNode) visit(context.ifStmt());
		return new ElseIfNode(context.start.getLine(),
							  context.start.getCharPositionInLine(),
							  expr, ifBlockStatements, next);
	}
	
	public AbstractNode visitWhileStmt(HelloParser.WhileStmtContext context) {	
		// Statements in block
		List<ParseTree> input = context.block().stmts().children;
		List<StatementNode> blockStatements = CreateList(input, StatementNode.class);
		
		List<ExpressionNode> expressions = new ArrayList<ExpressionNode>();	
		expressions.add((ExpressionNode) visit(context.expr()));
		return new WhileNode(context.start.getLine(),
							 context.start.getCharPositionInLine(),
 							 expressions, blockStatements);		
	}

	public AbstractNode visitForStmt(HelloParser.ForStmtContext context) {
		// Statements in block
		List<ParseTree> input = context.block().stmts().children;
		List<StatementNode> blockStatements = CreateList(input, StatementNode.class);
		
		return new ForNode(context.start.getLine(),
						   context.start.getCharPositionInLine(),
						   visit(context.getChild(2)),visit(context.getChild(4)),visit(context.getChild(6)), blockStatements);
	}
	
	public AbstractNode visitRetValStmt(HelloParser.RetValStmtContext context) {
		List<HelloParser.ExprContext> input = context.expr();
		List<ExpressionNode> expressions = CreateList(input, ExpressionNode.class);

		return new ReturnNode(context.start.getLine(),
							  context.start.getCharPositionInLine(),
							  expressions);
	}
	
	public AbstractNode visitRetVoidStmt(HelloParser.RetVoidStmtContext context) {
		return new ReturnNode(context.start.getLine(),
							  context.start.getCharPositionInLine());
	}
	
	public AbstractNode visitExpr(HelloParser.ExprContext context) {
		return visit(context.logicalORExpr());
	}
	
	public AbstractNode visitEmptyLogORExpr(HelloParser.EmptyLogORExprContext context) {
		return visit(context.logicalANDExpr());
	}
	
	public AbstractNode visitLogORExpr(HelloParser.LogORExprContext context) {
		return new LogicalORExprNode(context.start.getLine(),
									 context.start.getCharPositionInLine(),
									 (ExpressionNode) visit(context.logicalANDExpr()),
									 (ExpressionNode) visit(context.logicalORExpr()));

	}
	
	public AbstractNode visitEmptyLogANDExpr(HelloParser.EmptyLogANDExprContext context) {
		return visit(context.equalityExpr());
	}
	
	public AbstractNode visitLogANDExpr(HelloParser.LogANDExprContext context) {
		return new LogicalANDExprNode(context.start.getLine(),
									  context.start.getCharPositionInLine(),
									  (ExpressionNode) visit(context.equalityExpr()),
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
		return new EqualityExprNode(context.start.getLine(),
									context.start.getCharPositionInLine(),
									type,
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
		return new RelationExprNode(context.start.getLine(),
									context.start.getCharPositionInLine(),
									type,
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
		return new AdditiveExprNode(context.start.getLine(),
									context.start.getCharPositionInLine(),
									type,
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
		return new MultExprNode(context.start.getLine(),
								context.start.getCharPositionInLine(),
								type,
								(ExpressionNode) visit(context.unaryExpr()),
								(ExpressionNode) visit(context.multiplicationExpr()));
	}
	
	public AbstractNode visitEmptyUnExpr(HelloParser.EmptyUnExprContext context) {
		return visit(context.primaryExpr());
	}
	
	public AbstractNode visitNegUnExpr(HelloParser.NegUnExprContext context) {
		return new UnaryExprNode(context.start.getLine(),
								 context.start.getCharPositionInLine(),
								 UnaryExprNode.UnaryType.negation,
								 (ExpressionNode) visit(context.unaryExpr()));
	}
	
	public AbstractNode visitNotUnExpr(HelloParser.NotUnExprContext context) {
		return new UnaryExprNode(context.start.getLine(),
								 context.start.getCharPositionInLine(),
								 UnaryExprNode.UnaryType.not,
								 (ExpressionNode) visit(context.unaryExpr()));
	}
	
	public AbstractNode visitGeneralPrimary(HelloParser.GeneralPrimaryContext context) {
		return visit(context.generalIdent());
	}
	
	public AbstractNode visitTextLitPrimary(HelloParser.TextLitPrimaryContext context) {
		return new TextLiteralNode(context.start.getLine(),
								   context.start.getCharPositionInLine(),
								   context.TextLit().getText());
	}
	
	public AbstractNode visitNumLitPrimary(HelloParser.NumLitPrimaryContext context) {
		return new NumLiteralNode(context.start.getLine(),
								  context.start.getCharPositionInLine(),
								  Double.parseDouble(context.NumLit().getText()));
	}
	
	public AbstractNode visitBoolLitPrimary(HelloParser.BoolLitPrimaryContext context) {
		return new BoolLiteralNode(context.start.getLine(),
								   context.start.getCharPositionInLine(),
								   Boolean.parseBoolean(context.BoolLit().getText()));
	}
	
	public AbstractNode visitParenPrimary(HelloParser.ParenPrimaryContext context) {
		return new ParenthesesNode(context.start.getLine(),
								   context.start.getCharPositionInLine(),
								   (ExpressionNode) visit(context.expr()));
	}
	
	public AbstractNode visitGeneralIdent(HelloParser.GeneralIdentContext context) {
		List<HelloParser.BaseIdentContext> input = context.baseIdent();
		List<BaseIdentNode> idents = CreateList(input, BaseIdentNode.class);

		return new GeneralIdentNode(context.start.getLine(),
									context.start.getCharPositionInLine(),
									idents);
	}
	
	public AbstractNode visitFuncBaseIdent(HelloParser.FuncBaseIdentContext context) {
		List<HelloParser.ExprContext> input = context.funcCall().argList().expr();
		List<ExpressionNode> arguments = CreateList(input, ExpressionNode.class);
		
		if (context.expr() == null)
			return new FuncCallNode(context.start.getLine(),
									context.start.getCharPositionInLine(),
									context.funcCall().Ident().getText(), arguments);
					
		return new FuncCallNode(context.start.getLine(),
								context.start.getCharPositionInLine(),
								context.funcCall().Ident().getText(),
								arguments,
								(ExpressionNode) visit(context.expr()));				
	}
	
	public AbstractNode visitIdentBaseIdent(HelloParser.IdentBaseIdentContext context) {
			if (context.expr() == null)
				return new BaseIdentNode(context.start.getLine(),
										 context.start.getCharPositionInLine(),
										 context.Ident().getText());
			return new BaseIdentNode(context.start.getLine(),
									 context.start.getCharPositionInLine(),
									 context.Ident().getText(),
									 (ExpressionNode) visit(context.expr()));
	}
	
	public AbstractNode visitFuncCall(HelloParser.FuncCallContext context) {
		List<HelloParser.ExprContext> input = context.argList().expr();
		List<ExpressionNode> arguments = CreateList(input,ExpressionNode.class);
		
		return new FuncCallNode(context.start.getLine(),
								context.start.getCharPositionInLine(),
								context.Ident().getText(), arguments);
	}
	
    private static <T> Collection<T> nullSafe(Collection<T> c) {
        return (c == null) ? Collections.<T>emptyList() : c;
    }
   
    private <T1, T2> List<T2> CreateList(Collection<T1> list,Class<T2> type){
    	List<T2> result = new ArrayList<T2>();
		for (T1 child : nullSafe(list)) {
			result.add(type.cast(visit((ParseTree) child)));
		}
    	return result;
    }
}
