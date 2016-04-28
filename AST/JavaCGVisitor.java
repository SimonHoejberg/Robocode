import java.io.BufferedOutputStream;
import java.io.File;
import static java.nio.file.StandardOpenOption.*;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.List;

import exceptions.NotImplementedException;
import nodes.*;
import nodes.RobotDeclarationNode.RobotDeclarationType;

public class JavaCGVisitor extends ASTVisitor<String> {
	private int indentationLevel;
	private final String LANG_NAME = "BTR", STRUCT_INDENTATION = "    ";
	private String imports, header, dcls;
	private String structHeader, constructorParams;
	private String roboname;
	private boolean initializingRobot, creatingStructClass;
	private boolean usesColors, usesMath, usesArrays;
	
	private Hashtable<String, String> structConstructors;
	
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
		String res = convertType("ArrayList<" + node.getType())+ "> " + node.getIdent();
		
		String ident = node.getIdent();
		String type = node.getType();
		
		ExpressionNode size = node.getSize();
		boolean sizeless = size == null;
		String exprRes = "";
		if (!sizeless)
			exprRes = visit(size);
		
		if (creatingStructClass) {
			structHeader += STRUCT_INDENTATION + "public " + res + ";\n";
			constructorParams += res + ", ";
			return ident;
		}
		else if (initializingRobot) {
			header += "    private " + res + ";\n";
			res = ident + " = new ArrayList<" + type + ">();\n";
			if (!sizeless) {
				res += getIndentation() + "for (int _i = 0; _i < " + exprRes + "; ++_i)\n"; // _i is used since it is not a valid variable name in the language, thus no conflicts can occur
				indentationLevel++;
				res += getIndentation() + ident + ".add(" + getDefaultOfType(type) + ")";
				indentationLevel--;
			}
		}
		return res;
	}

	@Override
	public String visit(AssignmentNode node) {
		String res = "";
		String exprRes = visit(node.getExpression());
		
		
		List<AbstractNode> input = node.getVariables();
		int inputSize = input.size();
		
		for (int i = 0; i < inputSize; ++i) {
			AbstractNode current = input.get(i);
			if (current instanceof VarNode)
				res += visit((VarNode) current);
			else if (current instanceof ArrayDeclarationNode)
				res += visit((ArrayDeclarationNode) current);
			else if (current instanceof DataStructDeclarationNode)
				res += visit((DataStructDeclarationNode) current);
			else if (current instanceof GeneralIdentNode)
				res += visit((GeneralIdentNode) current);
			else 
				throw new NotImplementedException();
			res += node.getType().toJavaSyntax();
			res += exprRes;
			if (i < inputSize-1) {
				res += ";\n";
				res += getIndentation();
			}
		}
		
		switch (node.getType()) {
			case basic:
				
				break;
			case add:
				
				break;
			case sub:
				
				break;
			case mult:
				
				break;
			case div:
				
				break;
			case mod:
				
				break;
			default:
				throw new NotImplementedException();
		}
		
		return res;
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
		
		structHeader = "public class " + typeName + "() {\n";
		
		creatingStructClass = true;
		
		constructorParams = "";
		contents = "";
		List<Object> dcls = node.getDeclarations();
		for (Object dcl : dcls) {
			contents += STRUCT_INDENTATION + STRUCT_INDENTATION;
			if (dcl instanceof DeclarationNode)
				contents += visit((DeclarationNode) dcl);
			else if (dcl instanceof AssignmentNode)
				contents += visit((AssignmentNode) dcl);
		}
		
		contents += STRUCT_INDENTATION + "}\n}";
		
		constructorParams = constructorParams.substring(0, constructorParams.length()-2);
		
		try (OutputStream out = new BufferedOutputStream(
				 Files.newOutputStream(Paths.get((roboname + "/" + typeName + ".java")), CREATE, TRUNCATE_EXISTING))) {
			out.write(structHeader.getBytes());
			out.write(("\n    public " + typeName + "(" + constructorParams + ") {\n").getBytes());
			out.write(contents.getBytes());
		}
		catch (IOException ex) {
			System.out.println("Failed to write file \"" + roboname + "/" + typeName + ".java\"");
		}
		
		creatingStructClass = false;
		
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
		String res = "";
		if(dcls.contains(getEventMethodName(node.getParam().getType())+"("+node.getParam().getType())){
			String temp = AddEventMethod(node);
			int event = dcls.indexOf(getEventMethodName(node.getParam().getType()), 0);
			int lastP = dcls.indexOf('{',event);
			String first = dcls.substring(0, lastP+1);
			indentationLevel++;
			String payload =getIndentation()+getEventName(node)+"("+node.getParam().getIdent()+");";
			indentationLevel--;
			String end = dcls.substring(lastP+1);
			dcls= first+"\n"+payload+end+"\n";
			res = temp;
		}
		else{
			res+=getIndentation()+"@Override\n";
			res+=getIndentation()+"public void "+getEventMethodName(node.getParam().getType())+"("+node.getParam().getType()+" "+node.getParam().getIdent()+"){\n";
			indentationLevel++;
			res+=getIndentation()+getEventName(node)+"("+node.getParam().getIdent()+");\n";
			indentationLevel--;
			res+=getIndentation()+"}\n";
			res+=AddEventMethod(node);
		}
		return res;
	}
	
	private String getEventName(EventDeclarationNode node){
		if(getEventMethodName(node.getParam().getType()).equals(node.getIdent())){
			return node.getIdent().substring(2);
		}
		else{
			return node.getIdent();
		}
	}
	
	private String AddEventMethod(EventDeclarationNode node){
		String res = getIndentation()+"private void " + getEventName(node) +"("+node.getParam().getType()+ " " +node.getParam().getIdent()+"){\n";
		List<StatementNode> stms = node.getStatements();
		indentationLevel++; 
		for(StatementNode stm : stms)
			res+=visit(stm);
		indentationLevel--;
		res+=getIndentation()+"}\n";
		return res;
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
		String res = "for(";
		Object assign = node.assign;
		
		if (assign instanceof ExpressionNode)
			res+=visit((ExpressionNode) assign);
		else if (assign instanceof VarDeclarationNode)
			res+=visit((VarDeclarationNode) assign);
		else if (assign instanceof AssignmentNode)
			res+=visit((AssignmentNode) assign);
		else
			throw new NotImplementedException();

		res+=" ; "+ visit((ExpressionNode) node.predicate);
		Object update = node.update;
		if (update instanceof ExpressionNode)
			res+=visit((ExpressionNode) update);
		else if (update instanceof AssignmentNode)
			res+=visit((AssignmentNode) update);
		else
			throw new NotImplementedException();
		res+="){\n";
		indentationLevel++;
		List<StatementNode> stms = node.getStatements();
		for(StatementNode stm : stms)
			res+=visit(stm);
		indentationLevel--;
		res+=getIndentation() + "}";
		return res;
	}

	@Override
	public String visit(FuncCallNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(FuncDeclarationNode node) {
		String res = "";
		if(node.getReturnTypes().size()!= 1)
			res = getIndentation()+ "public Object " +node.getIdent() +"(";
		else
			res = getIndentation()+ "public "+ convertType(node.getReturnTypes().get(0).getType())+ " " +node.getIdent() +"(";
		List<VarNode> params = node.getParamList();
		for(VarNode param : params){
			res+=convertType(param.getType())+" " +param.getIdent()+", ";
		}
		res.substring(0, 2);
		res+="){\n";
		indentationLevel++;
		List<StatementNode> stms = node.getStatements();
		for(StatementNode stm : stms )
			res += visit(stm);
		indentationLevel--;
		res+= getIndentation()+"}\n";
		return res;
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
		res += "){\n";
		List<StatementNode> stms = node.getIfBlockStatements();
		indentationLevel++;
		for(StatementNode stm : stms)
			res += visit(stm);		
		indentationLevel--;
		
		res += getIndentation() + "}";
		
		switch (node.getClass().getName()) {
			case "nodes.IfNode":				
				break;
			case "nodes.IfElseNode":
				res += "\n";
				res += getIndentation() + "else {";
				List<StatementNode> elseStms = ((IfElseNode) node).getElseBlockStatements();
				indentationLevel++;
				for(StatementNode stm : elseStms)
					res += visit(stm);
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
		if(node instanceof WhileNode){
			res = visit((WhileNode)node);
		}
		else if(node instanceof ForNode){
			res = visit((ForNode)node);
		}
		else{
			throw new NotImplementedException();
		}
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
				}
				else if (type == RobotDeclarationType.initialization)
					init = robodcl;
				else if (type == RobotDeclarationType.behavior)
					behavior = robodcl;
			}
		
		// Create directory for output files
		File dir = new File(roboname);

		// if the directory does not exist, create it
		if (!dir.exists()) {
		    System.out.println("Creating directory " + roboname + ".");
		    boolean result = false;

		    try{
		        dir.mkdir();
		        result = true;
		    } 
		    catch(SecurityException se){
		        //handle it
		    }        
		    if(result) {    
		        System.out.println("Directory successfully created.");  
		    }
		}
		
		// Start creation of file class
		try (OutputStream out = new BufferedOutputStream(
			 Files.newOutputStream(Paths.get((roboname+"/"+roboname + ".java")), CREATE, TRUNCATE_EXISTING))) {
			
			// Flags for use in code generation
			initializingRobot = false;
			creatingStructClass = false;
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
			
			String temp;
			// Declarations
			for(DeclarationNode dcl : declarations){
			    temp = visit(dcl);
			    dcls += temp;
			}
			
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
		res += ";\n";
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
		int inputSize = input.size();
		for (int i = 0; i < inputSize; ++i) {
			VarNode var = input.get(i);
			if (creatingStructClass) {
				String ident = visit(var);
				res += "this.";
				res += ident;
				res += " = ";
				res += ident;
				if (i < inputSize-1) {
					res += ";\n";
					res += STRUCT_INDENTATION + STRUCT_INDENTATION;			
				}
			}
			else {
				res += visit(var);
				res += " = ";
				res += exprRes;
				if (i < inputSize-1) {
					res += ";\n";
					res += getIndentation();
				}
			}
		}
		return res;
	}

	@Override
	public String visit(VarNode node) {
		String res = convertType(node.getType()) + " " + node.getIdent();
		if (creatingStructClass) {
			structHeader += STRUCT_INDENTATION + "public " + res + ";\n";
			constructorParams += res + ", ";
			return node.getIdent();
		}
		else if (initializingRobot) {
			header += "    private " + res + ";\n";
			return node.getIdent();
		}
		return res;
	}

	@Override
	public String visit(WhileNode node) {
		String res = "while("+visit(node.getExpressions().get(0))+"){\n";
		List<StatementNode> input = node.getStatements();
		indentationLevel++;
		for(StatementNode stm : input){
			res += visit(stm);
		}
		indentationLevel--;
		res+= getIndentation() + "}";
		return res;
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
	
	private String getDefaultOfType(String input) {
		switch (input) {
			case "num":
				return "0.0";
			case "text":
				return "";
			case "bool":
				return "false";
			default:
				return "new " + input + "()"; //FIXME structConstructors.get(input);
		}
	}
	
	private String getEventMethodName(String input){
		switch (input) {
		case "BulletHitEvent":
			return "onBulletHit";
		case "BulletHitBulletEvent":
			return "onBulletHitBullet";
		case "BulletMissedEvent":
			return "onBulletMissed";
		case "DeathEvent":
			return "onDeath";
		case "HitByBulletEvent":
			return "onHitByBullet";
		case "HitRobotEvent":
			return "onHitRobot";
		case "HitWallEvent":
			return "onHitWall";
		case "RobotDeathEvent":
			return "onRobotDeath";
		case "ScannedRobotEvent":
			return "onScannedRobot";
		case "StatusEvent":
			return "onStatus";
		case "WinEvent":
			return "onWin";
		case "BattleEndedEvent":
			return "onBattleEnded";
		case "RoundEndedEvent":
			return "onRoundEnded";
		default:
			throw new NotImplementedException();
		}
	}
}
