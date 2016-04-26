import java.io.BufferedOutputStream;
import static java.nio.file.StandardOpenOption.*;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import exceptions.NotImplementedException;
import nodes.*;
import nodes.RobotDeclarationNode.RobotDeclarationType;

public class JavaCGVisitor extends ASTVisitor<String> {
	private int indentationLevel;
	private final String LANG_NAME = "BTR";
	private String imports, header, dcls;
	private String extClassHead;
	private String roboname;
	private boolean initializingRobot;
	private boolean usesColors, usesMath, usesArrays;
	
	public JavaCGVisitor() {
		indentationLevel = 0;
	}
	
	private String getIndentation() {
		String res = "";
		for (int i = 0; i < indentationLevel; ++i)
			res += "    ";
		
		return res;
	}
	
	@Override
	public String visit(AdditiveExprNode node) {
		return 	visit(node.getLeftChild())
				+ node.getType().toString()
				+ visit(node.getRightChild());
	}

	@Override
	public String visit(ArrayDeclarationNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(AssignmentNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(BaseIdentNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(BoolLiteralNode node) {
		return node.getBool().toString();
	}

	@Override
	public String visit(CallStatementNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(DataStructDeclarationNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(DataStructDefinitionNode node) {
		String typeName = node.getTypeName();
		String contents;
		
		// FIXME import list if arrays are used
		
		contents = "public class " + typeName + "() {";
		
		List<Object> dcls = node.getDeclarations();
		for (Object dcl : dcls) {
			contents += "    ";
			//contents += visit(node.getDeclarations().get(i));
		}
		
		contents += "}";
		
		try (OutputStream out = new BufferedOutputStream(
				 Files.newOutputStream(Paths.get((typeName + ".java")), CREATE, TRUNCATE_EXISTING))) {
			out.write(contents.getBytes());
		}
		catch (IOException ex) {
			System.out.println("Failed to write target file \"" + roboname + ".java");
		}
		
		return null;
	}

	@Override
	public String visit(DeclarationNode node) {
		String res;
		if (node instanceof RobotDeclarationNode)
			return "";
		else if (node instanceof EventDeclarationNode)
			res = visit((EventDeclarationNode) node);
		else if (node instanceof FuncDeclarationNode)
			res = visit((FuncDeclarationNode) node);
		else if (node instanceof VarDeclarationNode)
			res = visit((VarDeclarationNode) node);
		else if (node instanceof DataStructDefinitionNode)
			res = visit((DataStructDefinitionNode) node);
		else if (node instanceof DataStructDeclarationNode)
			res = visit((DataStructDeclarationNode) node);
		else if (node instanceof ArrayDeclarationNode)
			res = visit((ArrayDeclarationNode) node);
		else
			throw new NotImplementedException();
		res += "\n";
		return res;
	}

	@Override
	public String visit(EqualityExprNode node) {
		return visit(node.getLeftChild()) + node.getType().toString() + visit(node.getRightChild());
	}

	@Override
	public String visit(EventDeclarationNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(ExpressionNode node) {
		String res;
		if (node instanceof LogicalORExprNode)
			res = visit((LogicalORExprNode) node);
		else if (node instanceof LogicalANDExprNode)
			res = visit((LogicalANDExprNode) node);
		else if (node instanceof EqualityExprNode)
			res = visit((EqualityExprNode) node);
		else if (node instanceof RelationExprNode)
			res = visit((RelationExprNode) node);
		else if (node instanceof AdditiveExprNode)
			res = visit((AdditiveExprNode) node);
		else if (node instanceof MultExprNode)
			res = visit((MultExprNode) node);
		else if (node instanceof UnaryExprNode)
			res = visit((UnaryExprNode) node);
		else if (node instanceof PrimaryExprNode)
			res = visit((PrimaryExprNode) node);
		else
			throw new NotImplementedException();
		return res;
	}

	@Override
	public String visit(ForNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(FuncCallNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(FuncDeclarationNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(GeneralIdentNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(IfNode node) {
		String res;
		res = "if (";
		res += visit(node.getExpression());
		res += ") {";
		
		indentationLevel++;
		
		for (int i = 0; i < node.getIfBlockStatements().size(); ++i)
			res += visit(node.getIfBlockStatements().get(i));
		
		indentationLevel--;
		
		res += getIndentation() + "}";
		
		switch (node.getClass().getName()) {
			case "nodes.IfNode":				
				break;
			case "nodes.IfElseNode":
				res += "\n";
				res += getIndentation() + "else {";
				
				indentationLevel++;
				
				for (int i = 0; i < ((IfElseNode) node).getElseBlockStatements().size(); ++i)
					res += visit(((IfElseNode) node).getElseBlockStatements().get(i));
				
				indentationLevel--;
				
				res += getIndentation() + "}";
				
				break;
			case "nodes.ElseIfNode":
				res += "\n";
				res += getIndentation() + "else ";
				res += visit(((ElseIfNode) node).getNext());
				break;
			default:
				throw new NotImplementedException();
		}
		
		return res;
	}

	@Override
	public String visit(IterationNode node) {
		String res = "";
		switch (node.getClass().getName()) {
			case "nodes.WhileNode":	
				res += "while (";
				res += visit(node.getExpressions().get(0));
				res += ") {";	
				break;
			case "nodes.ForNode":
				res += "for (";
				if(((ForNode)node).assign instanceof AssignmentNode){
					res += visit((AssignmentNode)((ForNode)node).assign);
				}
				else if(((ForNode)node).assign instanceof VarDeclarationNode){
					res += visit((VarDeclarationNode)((ForNode)node).assign);
				}
				else if(((ForNode)node).assign instanceof ExpressionNode){
					res += visit((ExpressionNode)((ForNode)node).assign);
				}
				res += "; ";
				res += visit((ExpressionNode)((ForNode)node).predicate);
				res += "; ";
				if(((ForNode)node).update instanceof AssignmentNode){
					res += visit((AssignmentNode)((ForNode)node).update);
				}
				else if(((ForNode)node).update instanceof ExpressionNode){
					res += visit((ExpressionNode)((ForNode)node).update);
				}
				res += ") {";	
				break;
			default:
				throw new NotImplementedException();
		}
			
		indentationLevel++;
		
		for (int i = 0; i < node.getStatements().size(); ++i)
			res += visit(node.getStatements().get(i));
			
		indentationLevel--;
		
		res += getIndentation() + "}";
		
		return res;
	}

	@Override
	public String visit(LogicalANDExprNode node) {
		return 	visit(node.getLeftChild())
				+ " && "
				+ visit(node.getRightChild());
	}

	@Override
	public String visit(LogicalORExprNode node) {
		return 	visit(node.getLeftChild())
				+ " || "
				+ visit(node.getRightChild());
	}

	@Override
	public String visit(MultExprNode node) {
		return  visit(node.getLeftChild())
				+ node.getType().toString()
				+ visit(node.getRightChild());
	}

	@Override
	public String visit(NumLiteralNode node) {
		return String.valueOf(node.getValue());
	}

	@Override
	public String visit(ParenthesesNode node) {
		return 	"("
				+ visit(node.getChild())
				+ ")";
	}

	@Override
	public String visit(PrimaryExprNode node) {
		String res;
		if (node instanceof GeneralIdentNode)
			res = visit((GeneralIdentNode) node);
		else if (node instanceof TextLiteralNode)
			res = visit((TextLiteralNode) node);
		else if (node instanceof NumLiteralNode)
			res = visit((NumLiteralNode) node);
		else if (node instanceof BoolLiteralNode)
			res = visit((BoolLiteralNode) node);
		else if (node instanceof ParenthesesNode)
			res = visit((ParenthesesNode) node);
		else
			throw new NotImplementedException();
		return res;
	}

	@Override
	public String visit(ProgramNode node) {
		// Fetch robot declarations
		RobotDeclarationNode init = null;
		RobotDeclarationNode behavior = null;
		
		List<DeclarationNode> declarations = node.getDeclarations();
		for(DeclarationNode dcl : declarations)
			if (dcl instanceof RobotDeclarationNode) {
				RobotDeclarationNode robodcl = (RobotDeclarationNode) dcl;
				RobotDeclarationType type = robodcl.getType();
				if (type == RobotDeclarationType.name) {
					roboname = robodcl.getName();
					roboname = roboname.substring(1, roboname.length()-1);
					break;
				}
				else if (type == RobotDeclarationType.initialization)
					init = robodcl;
				else if (type == RobotDeclarationType.behavior)
					behavior = robodcl;
			}
		try (OutputStream out = new BufferedOutputStream(
			 Files.newOutputStream(Paths.get((roboname + ".java")), CREATE, TRUNCATE_EXISTING))) {
			
			// Flags for use in code generation
			initializingRobot = false;
			usesColors = false;
			usesMath = false;
			
			// FIXME package?
			
			
			// Class declaration
			header = "/**\n * " + roboname + " - a robot created with " + LANG_NAME + "\n */\n";
			
			header += "public class " + roboname + " extends robocode.Robot {\n\n";
			
			indentationLevel++;
			
			// Robot initialization
			dcls = "\n";
			dcls += getIndentation() + "/**\n";
			dcls += getIndentation() + "* run: TheMachine's default behavior\n";
			dcls += getIndentation() + "*/\n";
			dcls += getIndentation() + "public void run() {\n";
			
			indentationLevel++;
			
			if (init != null)
				dcls += visit(init);
			
			dcls += getIndentation() + "// Robot main loop\n";
			dcls += getIndentation() + "while(true) {\n";
			
			indentationLevel++;
			
			// Robot behavior
			if (behavior != null)
				dcls += visit(behavior);
			
			indentationLevel--;
			
			dcls += getIndentation() + "}\n";
			
			indentationLevel--;
			
			dcls += getIndentation() + "}\n\n";
			
			// Declarations
			for(DeclarationNode dcl : declarations)
			    dcls += visit(dcl);
			
			indentationLevel--;
			
			dcls += "}";
			
			// Imports
			imports = "import robocode.*;\n";		// FIXME need proper path
			imports += "import java.awt.*;\n";
			imports += "\n";
			
			out.write(imports.getBytes());
			out.write(header.getBytes());
			out.write(dcls.getBytes());
		}
		catch (IOException ex) {
			System.out.println("Failed to write target file \"" + roboname + ".java");
		}
		
		return null;
	}

	@Override
	public String visit(RelationExprNode node) {
		return 	visit(node.getLeftChild())
				+ node.getType().toString()
				+ visit(node.getRightChild());
	}

	@Override
	public String visit(ReturnNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(RobotDeclarationNode node) {
		String res = "";
		switch (node.getType()) {
			case initialization:
				initializingRobot = true;
				for (int i = 0; i < node.getStatements().size(); ++i)
					res += visit(node.getStatements().get(i));
				initializingRobot = false;
				break;
			case behavior:
				for (int i = 0; i < node.getStatements().size(); ++i)
					res += visit(node.getStatements().get(i));
				break;
			default:
				throw new NotImplementedException();
		}
		
		return res;
	}

	@Override
	public String visit(StatementNode node) {
		String res = getIndentation();
		if (node instanceof VarDeclarationNode)
			res += visit((VarDeclarationNode) node);
		else if (node instanceof ArrayDeclarationNode)
			res += visit((ArrayDeclarationNode) node);
		else if (node instanceof DataStructDeclarationNode)
			res += visit((DataStructDeclarationNode) node);
		else if (node instanceof AssignmentNode)
			res += visit((AssignmentNode) node);
		else if (node instanceof IfNode)
			res += visit((IfNode) node);
		else if(node instanceof CallStatementNode)
			res += visit((CallStatementNode)node);
		else if (node instanceof IterationNode)
			res += visit((IterationNode) node);
		else if (node instanceof ReturnNode)
			res += visit((ReturnNode) node);
		else
			throw new NotImplementedException();
		res += "\n";
		return res;
	}

	@Override
	public String visit(TextLiteralNode node) {
		return node.getText();
	}

	@Override
	public String visit(TypeNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(UnaryExprNode node) {
		return 	dcls += "-"
				+ visit(node.getChild());
	}

	@Override
	public String visit(VarDeclarationNode node) {
		String res = "";
		String exprRes = visit(node.getExpression());
		
		List<VarNode> input = node.getVariable();
		for(VarNode var : input) {
			res += visit(var);
			res += " = ";
			res += exprRes;
			res += ";";
		}
		return res;
	}

	@Override
	public String visit(VarNode node) {
		String res = convertType(node.getType()) + " " + node.getIdent();
		if (initializingRobot)
			header += "    " + res + ";\n";
		return res;
	}

	@Override
	public String visit(WhileNode node) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private String convertType(String input) {
		switch (input) {
			case "num":
				return "double";
			case "text":
				return "String";
			case "bool":
				return "boolean";
			default:
				return input;
		}
	}
}
