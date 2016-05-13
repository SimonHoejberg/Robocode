import java.util.*;

import nodes.*;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import exceptions.*;


public class BuildASTVisitor extends BTRBaseVisitor<AbstractNode> {
	public AbstractNode visitProg(BTRParser.ProgContext context) {
		return visit(context.dcls());
	}
	
	public AbstractNode visitDcls(BTRParser.DclsContext context) {
		List<ParseTree> input = context.children;
		List<DeclarationNode> declarations = CreateList(input, DeclarationNode.class);
		
		return new ProgramNode(context.start.getLine(),
							   context.start.getCharPositionInLine(),
							   declarations);
	}
	
	public AbstractNode visitRobonameAssign(BTRParser.RobonameAssignContext context) {
		String text = context.TextLit().getText();
		text = text.substring(1, text.length()-1);
		return new RobotDeclarationNode(context.start.getLine(),
										context.start.getCharPositionInLine(),
										RobotDeclarationNode.RobotDeclarationType.name,
										text);
	}
	
	public AbstractNode visitInitBlock(BTRParser.InitBlockContext context) {
		
		List<ParseTree> input = context.block().stmts().children;
		List<StatementNode> statements = CreateList(input, StatementNode.class);
		
		return new RobotDeclarationNode(context.start.getLine(),
										context.start.getCharPositionInLine(),
										RobotDeclarationNode.RobotDeclarationType.initialization, 
										statements);
	}
	
	public AbstractNode visitBehaviorBlock(BTRParser.BehaviorBlockContext context) {
		
		List<ParseTree> input = context.block().stmts().children;
		List<StatementNode> statements = CreateList(input, StatementNode.class);
		
		return new RobotDeclarationNode(context.start.getLine(),
										context.start.getCharPositionInLine(),
										RobotDeclarationNode.RobotDeclarationType.behavior,
										statements);
	}
	
	public AbstractNode visitEventDcl(BTRParser.EventDclContext context) {
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
	
	public AbstractNode visitFuncDcl(BTRParser.FuncDclContext context) {
		// List of return types
		List<TypeNode> typeList = new ArrayList<TypeNode>();
		if (context.typeList().getRuleContext() instanceof BTRParser.GeneralTypeListContext)
			for (ParseTree child : nullSafe(context.typeList().children)){
				if (child instanceof TerminalNode)		// Skip commas
					continue;
				TypeNode type;
				if (child instanceof BTRParser.TypeGeneralTypeContext)
				{
					type = new TypeNode(((BTRParser.GeneralTypeContext) child).start.getLine(),
										((BTRParser.GeneralTypeContext) child).start.getCharPositionInLine(),
										TypeConvert(child.getText()));
					
				}
				else if (child.getChild(0) instanceof BTRParser.StructTypeContext) {
					type = new TypeNode(((BTRParser.GeneralTypeContext) child).start.getLine(),
							((BTRParser.GeneralTypeContext) child).start.getCharPositionInLine(),
							TypeConvert(((BTRParser.StructTypeContext) child.getChild(0)).Ident().getText()));
				}
				else if (child.getChild(0) instanceof BTRParser.ArrayTypeContext) {
					type = new TypeNode(((BTRParser.GeneralTypeContext) child).start.getLine(),
										((BTRParser.GeneralTypeContext) child).start.getCharPositionInLine(),
										TypeConvert(((BTRParser.ArrayTypeContext) child.getChild(0)).type().getText()) + "[]");
				}
				else if (child.getChild(0) instanceof BTRParser.StructArrayTypeContext) {
					type = new TypeNode(((BTRParser.GeneralTypeContext) child).start.getLine(),
										((BTRParser.GeneralTypeContext) child).start.getCharPositionInLine(),
										TypeConvert(((BTRParser.StructArrayTypeContext) child.getChild(0)).Ident().getText()) + "[]");
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
		for (BTRParser.ParamContext child : nullSafe(context.paramList().param())) {
			VarNode param;
			if (child.generalType().getRuleContext() instanceof BTRParser.TypeGeneralTypeContext)
				param = new VarNode(child.start.getLine(),
									child.start.getCharPositionInLine(),
									TypeConvert(child.generalType().getChild(0).getText()),
									child.Ident().getText());
			else {
				BTRParser.ComplexTypeContext ct = ((BTRParser.ComplexTypeContext) child.generalType().getChild(0));
				if (ct.getRuleContext() instanceof BTRParser.StructTypeContext)
					param = new VarNode(child.start.getLine(),
										child.start.getCharPositionInLine(),
										TypeConvert(((BTRParser.StructTypeContext) ct).Ident().getText()),
										child.Ident().getText());
				else if (ct.getRuleContext() instanceof BTRParser.ArrayTypeContext)
					param = new VarNode(child.start.getLine(),
										child.start.getCharPositionInLine(),
										TypeConvert(((BTRParser.ArrayTypeContext) ct).type().getText()) + "[]",
										child.Ident().getText());
				else if (ct.getRuleContext() instanceof BTRParser.StructArrayTypeContext)
					param = new VarNode(child.start.getLine(),
										child.start.getCharPositionInLine(),
										TypeConvert(((BTRParser.StructArrayTypeContext) ct).Ident().getText()) + "[]",
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
	
	public AbstractNode visitVar(BTRParser.VarContext context){
		return new VarNode(context.start.getLine(),
				   		   context.start.getCharPositionInLine(),
				   		TypeConvert(context.type().getText()),
				           context.Ident().getText());
	}
	
	public AbstractNode visitVarDcl(BTRParser.VarDclContext context) {
		List<BTRParser.VarContext> input = context.var();
		List<VarNode> variable = CreateList(input, VarNode.class);
		ExpressionNode expr = (ExpressionNode) visit(context.expr());
		return new VarDeclarationNode(context.start.getLine(),
									  context.start.getCharPositionInLine(),
									  variable,expr);
	}
	
	public AbstractNode visitDataStructDef(BTRParser.DataStructDefContext context) {
		String typeName = context.Ident().getText();
		List<Object> declarations = new ArrayList<Object>();
		for(ParseTree o : context.children){
			if(o instanceof ParserRuleContext){
				declarations.add(visit(o));
			}
		}
		
		return new DataStructDefinitionNode(context.start.getLine(),
											context.start.getCharPositionInLine(),
											typeName,
											declarations);
	}
	
	public AbstractNode visitDataStructDcl(BTRParser.DataStructDclContext context) {
			return new DataStructDeclarationNode(context.start.getLine(),
					 context.start.getCharPositionInLine(),
					 context.Ident(0).getText(), context.Ident(1).getText());
	}
	
	public AbstractNode visitBasicArrayDcl(BTRParser.BasicArrayDclContext context) {
		return new ArrayDeclarationNode(context.start.getLine(),
										context.start.getCharPositionInLine(),
										TypeConvert(context.type().getText()), context.Ident().getText(), (ExpressionNode) visit(context.expr()));
	}
	
	public AbstractNode visitStructArrayDcl(BTRParser.StructArrayDclContext context) {
		return new ArrayDeclarationNode(context.start.getLine(),
										context.start.getCharPositionInLine(),
										context.dataStructDcl().Ident(0).getText(), context.dataStructDcl().Ident(1).getText(), (ExpressionNode) visit(context.expr()));
	}
	
	public AbstractNode visitBasicSizelessDcl(BTRParser.BasicSizelessDclContext context) {
		return new ArrayDeclarationNode(context.start.getLine(),
										context.start.getCharPositionInLine(),
										TypeConvert(context.type().getText()), context.Ident().getText(), null);
	}
	
	public AbstractNode visitStructSizelessDcl(BTRParser.StructSizelessDclContext context) {
		return new ArrayDeclarationNode(context.start.getLine(),
										context.start.getCharPositionInLine(),
										context.dataStructDcl().Ident(0).getText(), context.dataStructDcl().Ident(1).getText(), null);
	}
	
	public AbstractNode visitAssignHelp(BTRParser.AssignHelpContext context){
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
	
	public AbstractNode visitAssign(BTRParser.AssignContext context) {
		AssignmentNode.AssignmentType type;
		switch (context.assignmentOp().op.getType()) {
			case BTRLexer.OP_ASSIGN:
				type = AssignmentNode.AssignmentType.basic;
				break;
			case BTRLexer.OP_ADD_ASSIGN:
				type = AssignmentNode.AssignmentType.add;
				break;
			case BTRLexer.OP_SUB_ASSIGN:
				type = AssignmentNode.AssignmentType.sub;
				break;
			case BTRLexer.OP_MUL_ASSIGN:
				type = AssignmentNode.AssignmentType.mult;
				break;
			case BTRLexer.OP_DIV_ASSIGN:
				type = AssignmentNode.AssignmentType.div;
				break;
			case BTRLexer.OP_MOD_ASSIGN:
				type = AssignmentNode.AssignmentType.mod;
				break;
			default:
				throw new NotImplementedException();
		}
		List<AbstractNode> output = new ArrayList<AbstractNode>();
		List<BTRParser.AssignHelpContext> input = context.assignHelp();
		for(BTRParser.AssignHelpContext o : input){
			output.add(visit(o));
		}
		return new AssignmentNode(context.start.getLine(),
								  context.start.getCharPositionInLine(),
								  output,
								  type,
								  (ExpressionNode) visit(context.expr()));
	}
	
	public AbstractNode visitCallStmt(BTRParser.CallStmtContext context) {
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
	
	public AbstractNode visitIfThenStmt(BTRParser.IfThenStmtContext context) {
		ExpressionNode expr = (ExpressionNode) visit(context.expr());
		
		// Statements in "if" block
		List<ParseTree> input = context.block().stmts().children;
		List<StatementNode> ifBlockStatements = CreateList(input, StatementNode.class);
		
		return new IfNode(context.start.getLine(),
						  context.start.getCharPositionInLine(),
						  expr, ifBlockStatements);
	}
	
	public AbstractNode visitIfElseStmt(BTRParser.IfElseStmtContext context) {
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

	public AbstractNode visitElseIfStmt(BTRParser.ElseIfStmtContext context) {
		ExpressionNode expr = (ExpressionNode) visit(context.expr());
		
		// Statements in "if" block
		List<ParseTree> input = context.block().stmts().children;
		List<StatementNode> ifBlockStatements = CreateList(input, StatementNode.class);
		
		IfNode next = (IfNode) visit(context.ifStmt());
		return new ElseIfNode(context.start.getLine(),
							  context.start.getCharPositionInLine(),
							  expr, ifBlockStatements, next);
	}
	
	public AbstractNode visitWhileStmt(BTRParser.WhileStmtContext context) {	
		// Statements in block
		List<ParseTree> input = context.block().stmts().children;
		List<StatementNode> blockStatements = CreateList(input, StatementNode.class);
		
		List<ExpressionNode> expressions = new ArrayList<ExpressionNode>();	
		expressions.add((ExpressionNode) visit(context.expr()));
		return new WhileNode(context.start.getLine(),
							 context.start.getCharPositionInLine(),
 							 expressions, blockStatements);		
	}

	public AbstractNode visitForStmt(BTRParser.ForStmtContext context) {
		// Statements in block
		List<ParseTree> input = context.block().stmts().children;
		List<StatementNode> blockStatements = CreateList(input, StatementNode.class);
		
		return new ForNode(context.start.getLine(),
						   context.start.getCharPositionInLine(),
						   visit(context.getChild(2)),visit(context.getChild(4)),visit(context.getChild(6)), blockStatements);
	}
	
	public AbstractNode visitRetValStmt(BTRParser.RetValStmtContext context) {
		List<BTRParser.ExprContext> input = context.expr();
		List<ExpressionNode> expressions = CreateList(input, ExpressionNode.class);

		return new ReturnNode(context.start.getLine(),
							  context.start.getCharPositionInLine(),
							  expressions);
	}
	
	public AbstractNode visitRetVoidStmt(BTRParser.RetVoidStmtContext context) {
		return new ReturnNode(context.start.getLine(),
							  context.start.getCharPositionInLine());
	}
	
	public AbstractNode visitExpr(BTRParser.ExprContext context) {
		return visit(context.logicalORExpr());
	}
	
	public AbstractNode visitEmptyLogORExpr(BTRParser.EmptyLogORExprContext context) {
		return visit(context.logicalANDExpr());
	}
	
	public AbstractNode visitLogORExpr(BTRParser.LogORExprContext context) {
		return new LogicalORExprNode(context.start.getLine(),
									 context.start.getCharPositionInLine(),
									 (ExpressionNode) visit(context.logicalANDExpr()),
									 (ExpressionNode) visit(context.logicalORExpr()));

	}
	
	public AbstractNode visitEmptyLogANDExpr(BTRParser.EmptyLogANDExprContext context) {
		return visit(context.equalityExpr());
	}
	
	public AbstractNode visitLogANDExpr(BTRParser.LogANDExprContext context) {
		return new LogicalANDExprNode(context.start.getLine(),
									  context.start.getCharPositionInLine(),
									  (ExpressionNode) visit(context.equalityExpr()),
									  (ExpressionNode) visit(context.logicalANDExpr()));
	}
		
	public AbstractNode visitEmptyEqualExpr(BTRParser.EmptyEqualExprContext context) {
		return visit(context.relationalExpr());
	}
	
	public AbstractNode visitEqualExpr(BTRParser.EqualExprContext context) {
		EqualityExprNode.EqualityType type;
		switch (context.op.getType()) {
			case BTRLexer.OP_EQ:
				type = EqualityExprNode.EqualityType.equal;
				break;
			case BTRLexer.OP_NEQ:
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
	
	public AbstractNode visitEmptyRelExpr(BTRParser.EmptyRelExprContext context) {
		return visit(context.additiveExpr());
	}
	
	public AbstractNode visitRelExpr(BTRParser.RelExprContext context) {
		RelationExprNode.RelationType type;
		switch (context.op.getType()) {
			case BTRLexer.OP_LT:
				type = RelationExprNode.RelationType.lessThan;
				break;
			case BTRLexer.OP_GT:
				type = RelationExprNode.RelationType.greaterThan;
				break;
			case BTRLexer.OP_LTE:
				type = RelationExprNode.RelationType.lessThanOrEqual;
				break;
			case BTRLexer.OP_GTE:
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
	
	public AbstractNode visitEmptyAddExpr(BTRParser.EmptyAddExprContext context) {
		return visit(context.multiplicationExpr());
	}
	
	public AbstractNode visitAddExpr(BTRParser.AddExprContext context) {
		AdditiveExprNode.AdditionType type;
		switch (context.op.getType()) {
			case BTRLexer.OP_ADD:
				type = AdditiveExprNode.AdditionType.add;
				break;
			case BTRLexer.OP_SUB:
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
	
	public AbstractNode visitEmptyMultExpr(BTRParser.EmptyMultExprContext context) {
		return visit(context.unaryExpr());
	}
	
	public AbstractNode visitMultExpr(BTRParser.MultExprContext context) {
		MultExprNode.MultiplicationType type;
		switch (context.op.getType()) {
			case BTRLexer.OP_MUL:
				type = MultExprNode.MultiplicationType.mult;
				break;
			case BTRLexer.OP_DIV:
				type = MultExprNode.MultiplicationType.div;
				break;
			case BTRLexer.OP_MOD:
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
	
	public AbstractNode visitEmptyUnExpr(BTRParser.EmptyUnExprContext context) {
		return visit(context.primaryExpr());
	}
	
	public AbstractNode visitNegUnExpr(BTRParser.NegUnExprContext context) {
		return new UnaryExprNode(context.start.getLine(),
								 context.start.getCharPositionInLine(),
								 UnaryExprNode.UnaryType.negation,
								 (ExpressionNode) visit(context.unaryExpr()));
	}
	
	public AbstractNode visitNotUnExpr(BTRParser.NotUnExprContext context) {
		return new UnaryExprNode(context.start.getLine(),
								 context.start.getCharPositionInLine(),
								 UnaryExprNode.UnaryType.not,
								 (ExpressionNode) visit(context.unaryExpr()));
	}
	
	public AbstractNode visitGeneralPrimary(BTRParser.GeneralPrimaryContext context) {
		return visit(context.generalIdent());
	}
	
	public AbstractNode visitTextLitPrimary(BTRParser.TextLitPrimaryContext context) {
		String text = context.TextLit().getText();
		text = text.substring(1, text.length()-1);
		return new TextLiteralNode(context.start.getLine(),
								   context.start.getCharPositionInLine(),
								   text);
	}
	
	public AbstractNode visitNumLitPrimary(BTRParser.NumLitPrimaryContext context) {
		return new NumLiteralNode(context.start.getLine(),
								  context.start.getCharPositionInLine(),
								  Double.parseDouble(context.NumLit().getText()));
	}
	
	public AbstractNode visitBoolLitPrimary(BTRParser.BoolLitPrimaryContext context) {
		return new BoolLiteralNode(context.start.getLine(),
								   context.start.getCharPositionInLine(),
								   Boolean.parseBoolean(context.BoolLit().getText()));
	}
	
	public AbstractNode visitParenPrimary(BTRParser.ParenPrimaryContext context) {
		return new ParenthesesNode(context.start.getLine(),
								   context.start.getCharPositionInLine(),
								   (ExpressionNode) visit(context.expr()));
	}
	
	public AbstractNode visitGeneralIdent(BTRParser.GeneralIdentContext context) {
		List<BTRParser.BaseIdentContext> input = context.baseIdent();
		List<BaseIdentNode> idents = CreateList(input, BaseIdentNode.class);

		return new GeneralIdentNode(context.start.getLine(),
									context.start.getCharPositionInLine(),
									idents);
	}
	
	public AbstractNode visitFuncBaseIdent(BTRParser.FuncBaseIdentContext context) {
		List<BTRParser.ExprContext> input = context.funcCall().argList().expr();
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
	
	public AbstractNode visitIdentBaseIdent(BTRParser.IdentBaseIdentContext context) {
			if (context.expr() == null)
				return new BaseIdentNode(context.start.getLine(),
										 context.start.getCharPositionInLine(),
										 context.Ident().getText());
			return new BaseIdentNode(context.start.getLine(),
									 context.start.getCharPositionInLine(),
									 context.Ident().getText(),
									 (ExpressionNode) visit(context.expr()));
	}
	
	public AbstractNode visitFuncCall(BTRParser.FuncCallContext context) {
		List<BTRParser.ExprContext> input = context.argList().expr();
		List<ExpressionNode> arguments = CreateList(input,ExpressionNode.class);
		
		return new FuncCallNode(context.start.getLine(),
								context.start.getCharPositionInLine(),
								context.Ident().getText(), arguments);
	}
	
    private static <T> Collection<T> nullSafe(Collection<T> c) {
        return (c == null) ? Collections.<T>emptyList() : c;
    }
    
    private String TypeConvert(String type){
    	switch (type) {
		case "boolean":
			return "bool";
		case "number":
			return "num";
		default:
			return type;
		}
    }
   
    private <T1, T2> List<T2> CreateList(Collection<T1> list,Class<T2> type){
    	List<T2> result = new ArrayList<T2>();
		for (T1 child : nullSafe(list)) {
			result.add(type.cast(visit((ParseTree) child)));
		}
    	return result;
    }
}
