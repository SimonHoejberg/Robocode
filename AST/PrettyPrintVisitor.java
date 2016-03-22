import java.util.*;

public class PrettyPrintVisitor extends ASTVisitor<Void> {
	private int indentationLevel;
	
	public PrettyPrintVisitor() {
		indentationLevel = 0;
	}
	
	private void addIndentation() {
		for (int i = 0; i < indentationLevel; ++i)
			System.out.print("    ");
	}
	
	@Override
	public Void visit(AdditiveExprNode node) {
		visit(node.getRightChild());
		System.out.print(node.getType().toString());
		visit(node.getLeftChild());
		System.out.println();
		return null;
	}

	@Override
	public Void visit(ArrayDeclarationNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(AssignmentNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(BaseIdentNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(BoolLiteralNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(CallStatementNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(DataStructDeclarationNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(DataStructDefinitionNode node) {
		System.out.println("container " + node.getTypeName() + " {");
		
		indentationLevel++;
		
		for (int i = 0; i < node.getDeclarations().size(); ++i) {
			addIndentation();
			visit(node.getDeclarations().get(i));
		}
		
		indentationLevel--;
		
		System.out.println("}");
		
		return null;
	}

	@Override
	public Void visit(DeclarationNode node) {
		if (node instanceof RobotDeclarationNode)
			visit((RobotDeclarationNode) node);
		else if (node instanceof EventDeclarationNode)
			visit((EventDeclarationNode) node);
		else if (node instanceof FuncDeclarationNode)
			visit((FuncDeclarationNode) node);
		else if (node instanceof VarDeclarationNode)
			visit((VarDeclarationNode) node);
		else if (node instanceof DataStructDefinitionNode)
			visit((DataStructDefinitionNode) node);
		else if (node instanceof DataStructDeclarationNode)
			visit((DataStructDeclarationNode) node);
		else if (node instanceof ArrayDeclarationNode)
			visit((ArrayDeclarationNode) node);
		return null;
	}

	@Override
	public Void visit(EqualityExprNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(EventDeclarationNode node) {
		System.out.print("\nevent " + node.getIdent() + "(");
		visit(node.getParam());
		System.out.println(") {");
		
		indentationLevel++;
		
		for (int i = 0; i < node.getStatements().size(); ++i)
			visit(node.getStatements().get(i));
		
		indentationLevel--;
		
		System.out.println("}\n");
		return null;
	}

	@Override
	public Void visit(ExpressionNode node) {
		if (node instanceof LogicalORExprNode)
			visit((LogicalORExprNode) node);
		else if (node instanceof LogicalANDExprNode)
			visit((LogicalANDExprNode) node);
		else if (node instanceof EqualityExprNode)
			visit((EqualityExprNode) node);
		else if (node instanceof RelationExprNode)
			visit((RelationExprNode) node);
		else if (node instanceof AdditiveExprNode)
			visit((AdditiveExprNode) node);
		else if (node instanceof MultExprNode)
			visit((MultExprNode) node);
		else if (node instanceof UnaryExprNode)
			visit((UnaryExprNode) node);
		else if (node instanceof PrimaryExprNode)
			visit((PrimaryExprNode) node);
		return null;
	}

	@Override
	public Void visit(FuncCallNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(FuncDeclarationNode node) {
		System.out.print("\nfunc ");
		for (int i = 0; i < node.getReturnTypes().size(); ++i) {
			visit(node.getReturnTypes().get(i));
			if (i+1 < node.getReturnTypes().size())
				System.out.print(", ");
		}
		System.out.print(" " + node.getIdent() + "(");
		for (int i = 0; i < node.getParamList().size(); ++i) {
			visit(node.getParamList().get(i));
			if (i+1 < node.getParamList().size())
				System.out.print(", ");
		}
		System.out.println(") {");
		
		indentationLevel++;
		
		for (int i = 0; i < node.getStatements().size(); ++i)
			visit(node.getStatements().get(i));
		
		indentationLevel--;
		
		System.out.println("}\n");
		return null;
	}

	@Override
	public Void visit(GeneralIdentNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IfNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IterationNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(LogicalANDExprNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(LogicalORExprNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(MultExprNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(NumLiteralNode node) {
		System.out.print(node.getValue());
		return null;
	}

	@Override
	public Void visit(ParenthesesNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(PrimaryExprNode node) {
		if (node instanceof GeneralIdentNode)
			visit((GeneralIdentNode) node);
		else if (node instanceof TextLiteralNode)
			visit((TextLiteralNode) node);
		else if (node instanceof NumLiteralNode)
			visit((NumLiteralNode) node);
		else if (node instanceof BoolLiteralNode)
			visit((BoolLiteralNode) node);
		else if (node instanceof ParenthesesNode)
			visit((ParenthesesNode) node);
		return null;
	}

	@Override
	public Void visit(ProgramNode node) {
		System.out.println("Printing program: ");
		List<DeclarationNode> declarations = node.getDeclarations();
		for(int i = 0; i < declarations.size(); ++i)
		    visit(declarations.get(i));
		
		return null;
	}

	@Override
	public Void visit(RelationExprNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ReturnNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(RobotDeclarationNode node) {
		switch (node.getType()) {
			case name:
				System.out.println("\nroboname := " + node.getName() + '\n');
				break;
			case initialization:
				System.out.println("\nrobot initialization() {");
				
				indentationLevel++;
				
				for (int i = 0; i < node.getStatements().size(); ++i)
					visit(node.getStatements().get(i));
				
				indentationLevel--;
				
				System.out.println("}\n");
				break;
			case behavior:
				System.out.println("\nrobot behavior() {");
				
				indentationLevel++;
				
				for (int i = 0; i < node.getStatements().size(); ++i)
					visit(node.getStatements().get(i));
				
				indentationLevel--;
				
				System.out.println("}\n");
				break;
			default:
				System.out.print("PrettyPrinter error: feature not yet implemented!");
				break;
		}
		
		return null;
	}

	@Override
	public Void visit(StatementNode node) {
		addIndentation();
		System.out.println("STATEMENT");
		return null;
	}

	@Override
	public Void visit(TextLiteralNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(TypeNode node) {
		System.out.print(node.getType());
		return null;
	}

	@Override
	public Void visit(UnaryExprNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(VarDeclarationNode node) {
		visit(node.getVariable());
		System.out.print(" := ");
		visit(node.getExpression());
		System.out.println();
		return null;
	}

	@Override
	public Void visit(VarNode node) {
		System.out.print(node.getType() + " " + node.getIdent());
		return null;
	}
}
