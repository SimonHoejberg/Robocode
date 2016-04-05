import java.util.List;

import symbolTable.STTypeEntry;
import symbolTable.SymbolTable;

public class TypeCheckVisitor extends ASTVisitor<Object> {

	SymbolTable symbolTable;
	final Object 	NUM = "num".intern(),
					TEXT = "text".intern(),
					BOOL = "bool".intern(),
					VOID = "void".intern();
	
	
	public TypeCheckVisitor() {
		symbolTable = new SymbolTable();
	}
	
	@Override
	public Object visit(AdditiveExprNode node) {
		Object leftType = visit(node.getLeftChild());
		Object rightType = visit(node.getRightChild());
		if (leftType == NUM && rightType == NUM)
			return NUM;
		
		throw new RuntimeException("The operator" + node.getType() + "is undefined for the argument type(s) " + leftType + ", " + rightType);
	}

	@Override
	public Object visit(ArrayDeclarationNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(AssignmentNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(BaseIdentNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(BoolLiteralNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(CallStatementNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(DataStructDeclarationNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(DataStructDefinitionNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(DeclarationNode node) {
		if (node instanceof RobotDeclarationNode)
			return visit((RobotDeclarationNode) node);
		else if (node instanceof EventDeclarationNode)
			return visit((EventDeclarationNode) node);
		else if (node instanceof FuncDeclarationNode)
			return visit((FuncDeclarationNode) node);
		else if (node instanceof VarDeclarationNode)
			return visit((VarDeclarationNode) node);
		else if (node instanceof DataStructDefinitionNode)
			return visit((DataStructDefinitionNode) node);
		else if (node instanceof DataStructDeclarationNode)
			return visit((DataStructDeclarationNode) node);
		else if (node instanceof ArrayDeclarationNode)
			return visit((ArrayDeclarationNode) node);
		throw new NotImplementedException();
	}

	@Override
	public Object visit(EqualityExprNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(EventDeclarationNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ExpressionNode node) {
		if (node instanceof LogicalORExprNode)
			return visit((LogicalORExprNode) node);
		else if (node instanceof LogicalANDExprNode)
			return visit((LogicalANDExprNode) node);
		else if (node instanceof EqualityExprNode)
			return visit((EqualityExprNode) node);
		else if (node instanceof RelationExprNode)
			return visit((RelationExprNode) node);
		else if (node instanceof AdditiveExprNode)
			return visit((AdditiveExprNode) node);
		else if (node instanceof MultExprNode)
			return visit((MultExprNode) node);
		else if (node instanceof UnaryExprNode)
			return visit((UnaryExprNode) node);
		else if (node instanceof PrimaryExprNode)
			return visit((PrimaryExprNode) node);
		throw new NotImplementedException();
	}

	@Override
	public Object visit(FuncCallNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(FuncDeclarationNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(GeneralIdentNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(IfNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(IterationNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(LogicalANDExprNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(LogicalORExprNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(MultExprNode node) {
		Object leftType = visit(node.getLeftChild());
		Object rightType = visit(node.getRightChild());
		if (leftType == NUM && rightType == NUM)
			return NUM;
		
		throw new RuntimeException("The operator" + node.getType() + "is undefined for the argument type(s) " + leftType + ", " + rightType);
	}

	@Override
	public Object visit(NumLiteralNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ParenthesesNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(PrimaryExprNode node) {
		switch (node.getClass().getName()) {
			case "GeneralIdentNode":
				// Need to do symbol table lookup here
				break;
			case "TextLiteralNode":
				return TEXT;
			case "NumLiteralNode":
				return NUM;
			case "BoolLiteralNode":
				return BOOL;
			case "ParenthesesNode":
				return visit(((ParenthesesNode) node).getChild());
			default:
				throw new NotImplementedException();
		}
		
		throw new RuntimeException("The operator + is undefined for the argument type(s) ");
	}

	@Override
	public Object visit(ProgramNode node) {		
		System.out.println("Typechecking program: ");
		List<DeclarationNode> declarations = node.getDeclarations();
		for(int i = 0; i < declarations.size(); ++i)
		    visit(declarations.get(i));
		
		return VOID;
	}

	@Override
	public Object visit(RelationExprNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ReturnNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(RobotDeclarationNode node) {
		switch (node.getType()) {
			case initialization:
			case behavior:
				symbolTable.openScope();
				for (int i = 0; i < node.getStatements().size(); ++i)
					visit(node.getStatements().get(i));
				symbolTable.closeScope();
			case name:
				return VOID;
			default:
				throw new NotImplementedException();
		}
	}

	@Override
	public Object visit(StatementNode node) {
		if (node instanceof VarDeclarationNode)
			return visit((VarDeclarationNode) node);
		else if (node instanceof AssignmentNode)
			return visit((AssignmentNode) node);
		else if (node instanceof CallStatementNode)
			return visit((CallStatementNode) node);
		else if (node instanceof IfNode)
			return visit((IfNode) node);
		else if (node instanceof IterationNode)
			return visit((IterationNode) node);
		else if (node instanceof ReturnNode)
			return visit((ReturnNode) node);
		throw new NotImplementedException();
	}

	@Override
	public Object visit(TextLiteralNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(TypeNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(UnaryExprNode node) {
		Object child = visit(node.getChild());
		if (child == NUM)
			return NUM;
		
		throw new RuntimeException("The operator - is undefined for the argument type(s) " + child);
	}

	@Override
	public Object visit(VarDeclarationNode node) {
		// Check if the symbol has already been defined in this scope
		VarNode var = node.getVariable();
		boolean local = false;
		try {
			local = symbolTable.declaredLocally(var.getIdent());
		}
		catch (Exception ex) { }
		
		if (local)
			throw new RuntimeException(node.getLineNumber() + ":" + (node.getColumnNumber()+1) + " Duplicate local variable " + var.getIdent());
		
		// Add variable to symbol table
		symbolTable.enterSymbol(var.getIdent(), new STTypeEntry(var.getType().intern()));
		
		Object varType = node.getVariable().getType().intern();
		Object rhsType = visit(node.getExpression());
		if (varType == rhsType)
			return VOID;
		
		throw new RuntimeException("Cannot assign variable of type " + varType + " a value of type " + rhsType);
	}

	@Override
	public Object visit(VarNode node) {
		// TODO Auto-generated method stub
		return null;
	}

}
