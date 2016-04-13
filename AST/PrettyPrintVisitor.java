import java.util.*;

import com.sun.javafx.binding.SelectBinding.AsInteger;

import exceptions.*;
import nodes.*;

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
		visit(node.getLeftChild());
		System.out.print(node.getType().toString());
		visit(node.getRightChild());
		
		return null;
	}

	@Override
	public Void visit(ArrayDeclarationNode node) {
		System.out.print(node.getType() + " " + node.getIdent() + "[");
		visit(node.getSize());
		System.out.print("]");
		
		return null;
	}

	@Override
	public Void visit(AssignmentNode node) {
		visit(node.getGeneralIdent());
		System.out.print(node.getType().toString());
		visit(node.getExpression());
		
		return null;
	}

	@Override
	public Void visit(BaseIdentNode node) {
		
		if (node instanceof FuncCallNode)
			visit((FuncCallNode) node);
		else
			System.out.print(node.getIdent());
		
		if(node.getIndex() != null) {
			System.out.print("[");
			visit(node.getIndex());
			System.out.print("]");
		}
		
		return null;
	}

	@Override
	public Void visit(BoolLiteralNode node) {
		System.out.print(node.getBool());
		
		return null;
	}

	@Override
	public Void visit(CallStatementNode node) {
		visit(node.getIdent());
		
		return null;
	}

	@Override
	public Void visit(DataStructDeclarationNode node) {
		System.out.print(node.getType() + " " + node.getIdent());
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
		System.out.println();
		return null;
	}

	@Override
	public Void visit(EqualityExprNode node) {
		visit(node.getLeftChild());
		System.out.print(node.getType().toString());
		visit(node.getRightChild());
		
		return null;
	}

	@Override
	public Void visit(EventDeclarationNode node) {
		System.out.print("event " + node.getIdent() + "(");
		visit(node.getParam());
		System.out.println(") {");
		
		indentationLevel++;
		
		for (int i = 0; i < node.getStatements().size(); ++i)
			visit(node.getStatements().get(i));
		
		indentationLevel--;
		
		System.out.println("}");
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
	public Void visit(ForNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(FuncCallNode node) {
		System.out.print(node.getIdent() + "(");
		
		for (int i = 0; i < node.getArguments().size(); ++i) {
			visit(node.getArguments().get(i));
			if (i+1 < node.getArguments().size())
				System.out.print(", ");
		}
		
		System.out.print(")");
		
		return null;
	}

	@Override
	public Void visit(FuncDeclarationNode node) {
		System.out.print("func ");
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
		
		System.out.println("}");
		return null;
	}

	@Override
	public Void visit(GeneralIdentNode node) {
		for (int i = 0; i < node.getIdents().size(); ++i) {
			visit(node.getIdents().get(i));
			if (i+1 < node.getIdents().size())
				System.out.print('.');			
		}
		
		return null;
	}

	@Override
	public Void visit(IfNode node) {
		System.out.print("if (");
		visit(node.getExpression());
		System.out.println(") {");
		
		indentationLevel++;
		
		for (int i = 0; i < node.getIfBlockStatements().size(); ++i)
			visit(node.getIfBlockStatements().get(i));
		
		indentationLevel--;
		
		addIndentation();
		System.out.print("}");
		
		switch (node.getClass().getName()) {
			case "nodes.IfNode":				
				break;
			case "nodes.IfElseNode":
				System.out.println();
				addIndentation();
				System.out.println("else {");
				
				indentationLevel++;
				
				for (int i = 0; i < ((IfElseNode) node).getElseBlockStatements().size(); ++i)
					visit(((IfElseNode) node).getElseBlockStatements().get(i));
				
				indentationLevel--;
				
				addIndentation();
				System.out.print("}");
				
				break;
			case "nodes.ElseIfNode":
				System.out.println();
				addIndentation();
				System.out.print("else ");
				visit(((ElseIfNode) node).getNext());
				break;
			default:
				throw new NotImplementedException();
		}
		
		return null;
	}

	@Override
	public Void visit(IterationNode node) {
		switch (node.getClass().getName()) {
			case "nodes.WhileNode":	
				System.out.print("while (");
				visit(node.getExpressions().get(0));
				System.out.println(") {");	
				break;
			case "nodes.ForNode":
				System.out.print("for (");
				if(((ForNode)node).assign instanceof AssignmentNode){
					visit((AssignmentNode)((ForNode)node).assign);
				}
				else if(((ForNode)node).assign instanceof VarDeclarationNode){
					visit((VarDeclarationNode)((ForNode)node).assign);
				}
				else if(((ForNode)node).assign instanceof ExpressionNode){
					visit((ExpressionNode)((ForNode)node).assign);
				}
				System.out.print("; ");
				visit((ExpressionNode)((ForNode)node).predicate);
				System.out.print("; ");
				if(((ForNode)node).update instanceof AssignmentNode){
					visit((AssignmentNode)((ForNode)node).update);
				}
				else if(((ForNode)node).update instanceof ExpressionNode){
					visit((ExpressionNode)((ForNode)node).update);
				}
				System.out.println(") {");	
				break;
			default:
				throw new NotImplementedException();
		}
			
		indentationLevel++;
		
		for (int i = 0; i < node.getStatements().size(); ++i)
			visit(node.getStatements().get(i));
			
		indentationLevel--;
		
		addIndentation();
		System.out.print("}");
		
		return null;
	}

	@Override
	public Void visit(LogicalANDExprNode node) {
		visit(node.getLeftChild());
		System.out.print(" & ");
		visit(node.getRightChild());
		
		return null;
	}

	@Override
	public Void visit(LogicalORExprNode node) {
		visit(node.getLeftChild());
		System.out.print(" | ");
		visit(node.getRightChild());
		
		return null;
	}

	@Override
	public Void visit(MultExprNode node) {
		visit(node.getLeftChild());
		System.out.print(node.getType().toString());
		visit(node.getRightChild());
		
		return null;
	}

	@Override
	public Void visit(NumLiteralNode node) {
		System.out.print(node.getValue());
		
		return null;
	}

	@Override
	public Void visit(ParenthesesNode node) {
		System.out.print("(");
		visit(node.getChild());
		System.out.print(")");
		
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
		visit(node.getLeftChild());
		System.out.print(node.getType().toString());
		visit(node.getRightChild());
		
		return null;
	}

	@Override
	public Void visit(ReturnNode node) {
		System.out.print("return");
		if (node.getExpressions() != null) {
			System.out.print(" ");
			for (int i = 0; i < node.getExpressions().size(); ++i) {
				visit(node.getExpressions().get(i));
				if (i+1 < node.getExpressions().size())
					System.out.print(", ");
			}
		}
		
		return null;
	}

	@Override
	public Void visit(RobotDeclarationNode node) {
		switch (node.getType()) {
			case name:
				System.out.println("roboname := " + node.getName());
				break;
			case initialization:
				System.out.println("robot initialization() {");
				
				indentationLevel++;
				
				for (int i = 0; i < node.getStatements().size(); ++i)
					visit(node.getStatements().get(i));
				
				indentationLevel--;
				
				System.out.println("}");
				break;
			case behavior:
				System.out.println("robot behavior() {");
				
				indentationLevel++;
				
				for (int i = 0; i < node.getStatements().size(); ++i)
					visit(node.getStatements().get(i));
				
				indentationLevel--;
				
				System.out.println("}");
				break;
			default:
				throw new NotImplementedException();
		}
		
		return null;
	}

	@Override
	public Void visit(StatementNode node) {
		addIndentation();
		if (node instanceof VarDeclarationNode)
			visit((VarDeclarationNode) node);
		else if (node instanceof ArrayDeclarationNode)
			visit((ArrayDeclarationNode) node);
		else if (node instanceof DataStructDeclarationNode)
			visit((DataStructDeclarationNode) node);
		else if (node instanceof AssignmentNode)
			visit((AssignmentNode) node);
		else if (node instanceof CallStatementNode)
			visit((CallStatementNode) node);
		else if (node instanceof IfNode)
			visit((IfNode) node);
		else if (node instanceof IterationNode)
			visit((IterationNode) node);
		else if (node instanceof ReturnNode)
			visit((ReturnNode) node);
		System.out.println();
		return null;
	}

	@Override
	public Void visit(TextLiteralNode node) {
		System.out.print(node.getText());
		
		return null;
	}

	@Override
	public Void visit(TypeNode node) {
		System.out.print(node.getType());
		return null;
	}

	@Override
	public Void visit(UnaryExprNode node) {
		System.out.print("-");
		visit(node.getChild());
		
		return null;
	}

	@Override
	public Void visit(VarDeclarationNode node) {
		visit(node.getVariable());
		System.out.print(" := ");
		visit(node.getExpression());
		return null;
	}

	@Override
	public Void visit(VarNode node) {
		System.out.print(node.getType() + " " + node.getIdent());
		return null;
	}

	@Override
	public Void visit(WhileNode node) {
		// TODO Auto-generated method stub
		return null;
	}
}
