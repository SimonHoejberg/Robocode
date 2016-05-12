import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import static java.nio.file.StandardOpenOption.*;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import exceptions.NotImplementedException;
import nodes.*;
import nodes.RobotDeclarationNode.RobotDeclarationType;

public class JavaCGVisitor extends ASTVisitor<String> {
	private int indentationLevel;
	private int currentInputSize, currentListParam;
	private final String LANG_NAME = "BTR", STRUCT_INDENTATION = "    ";
	private String imports, header, dcls;
	private String structHeader, copyConstructor, constructorParams, defaultInstantiation;
	private String roboname;
	private String robopackage;
	private boolean initializingRobot, creatingStructClass, assigning;
	private boolean outputListSetInScope;
	private boolean lastBaseIdent, lastIdentIsArrayEntry;
	private String lastIdentIndex;
	private boolean usesColors, usesMath, usesArrays;
	private boolean structUsesArrays;
	private boolean generateJava;
	private boolean compileBTR = false;
	private Gui gui;

	private Hashtable<String, String> structInstantiations;

	// FIXME Operators need to behave like they should, 2.0 == 2.0 could return false, for (int i = 0; i < 2.0; ++i) would have unpredictable behavior

	public JavaCGVisitor() {
		indentationLevel = 0;
	}

	public void SetGenerateJava(boolean java){
		generateJava = java;
		compileBTR = true;
	}

	public void SetGuiPointer(Gui gui){
		this.gui = gui;
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
		String ident = node.getIdent();
		String type = node.getType();
		String actualType = (String) node.getNodeType();

		usesArrays = true;
		if (creatingStructClass) {
			structUsesArrays = true;
			
			// Add to copy constructor
			copyConstructor += STRUCT_INDENTATION + STRUCT_INDENTATION;
			if (actualType.equals("num[]") || actualType.equals("text[]") || actualType.equals("bool[]")) {
				String convertedType = convertTypeForList(actualType);
				copyConstructor += "this." + ident + " = new " + convertedType + "(other." + ident + ");\n";
			}
			else {
				actualType = actualType.substring(0, actualType.length()-2);
				copyConstructor += "this." + ident + " = new ArrayList<" + convertTypeForList(actualType) + ">();\n";
				copyConstructor += STRUCT_INDENTATION + STRUCT_INDENTATION;
				
				copyConstructor += "for (" + actualType + " t : other." + ident + ")\n";
				copyConstructor += STRUCT_INDENTATION + STRUCT_INDENTATION + STRUCT_INDENTATION;
				copyConstructor += "this." + ident + ".add(new " + actualType + "(t));\n";
			}
		}
		
		String res = "";
		String dcl = "ArrayList<" + convertTypeForList(type)+ "> " + ident;

		ExpressionNode size = node.getSize();
		boolean sizeless = size == null;
		String exprRes = "";
		if (!sizeless)
			exprRes = visit(size);

		if (creatingStructClass) {
			structHeader += STRUCT_INDENTATION + "public " + dcl + ";\n";

			// Add default array for constructor if not part of assignment
			if (!assigning) {
				header += "    private ArrayList<" + convertTypeForList(type)+ "> _" + ident + ";\n";
				if (!sizeless) {
					dcls += getIndentation() + "_" + ident + " = new ArrayList<" + convertTypeForList(type) + ">();\n";
					dcls += getIndentation() + "for (int _i = 0; _i < " + exprRes + "; ++_i)\n"; // _i is used since it is not a valid variable name in the language, thus no conflicts can occur
					indentationLevel++;
					dcls += getIndentation() + "_" + ident + ".add(" + getDefaultOfType(type) + ");\n";
					indentationLevel--;
				}
				defaultInstantiation += "_" + ident;
			}

			if (!assigning) {
				constructorParams += dcl + ", ";
				return "this." + ident + " = " + ident + ";";
			}
		}
		else if (initializingRobot) {
			header += "    private " + dcl + ";\n";
			res = ident + " = new ArrayList<" + convertTypeForList(type) + ">();\n";
		}
		else {
			res = dcl + " = new ArrayList<" + convertTypeForList(type) + ">();\n";
		}

		if (assigning) {
			if (creatingStructClass)
				return ident;
			return dcl;
		}
		else {
			if (!sizeless) {
				res += getIndentation() + "for (int _i = 0; _i < " + exprRes + "; ++_i)\n"; // _i is used since it is not a valid variable name in the language, thus no conflicts can occur
				indentationLevel++;
				res += getIndentation() + ident + ".add(" + getDefaultOfType(type) + ")";
				indentationLevel--;
			}
			return res + ";";
		}
	}

	@Override
	public String visit(AssignmentNode node) {
		String res = "";
		String exprRes = visit(node.getExpression());
		String generalIdent = "";

		boolean useSetter = false;

		assigning = true;

		List<AbstractNode> input = node.getVariables();
		int inputSize = input.size();
		currentInputSize = inputSize;

		if (inputSize > 1 && !creatingStructClass) {
			if (!outputListSetInScope) {
				res = "java.util.List<Object> ";
				outputListSetInScope = true;
			}
			res += "_output = " + exprRes + ";\n" + STRUCT_INDENTATION + STRUCT_INDENTATION;
		}

		String listName = "_output";
		if (creatingStructClass && currentListParam > 1)
			listName += currentListParam;
		for (int i = 0; i < inputSize; ++i) {
			AbstractNode current = input.get(i);
			String ident = "";
			if (current instanceof VarNode)
				res += visit((VarNode) current);
			else if (current instanceof ArrayDeclarationNode) {
				res += visit((ArrayDeclarationNode) current);
				ident = ((ArrayDeclarationNode) current).getIdent();
			}
			else if (current instanceof DataStructDeclarationNode)
				res += visit((DataStructDeclarationNode) current);
			else if (current instanceof GeneralIdentNode) {
				generalIdent = visit((GeneralIdentNode) current);
				res += generalIdent;
				useSetter = lastIdentIsArrayEntry;
			}
			else 
				throw new NotImplementedException();
			if (!useSetter)
				res += node.getType().toJavaSyntax();

			if (inputSize > 1) {
				String type = convertTypeForList((String) current.getNodeType());
				String getter = "(" + type + ") " + listName + ".get(" + i + ")";
				if (type.equals("num") || type.equals("bool") || type.equals("text"))
					res += getter;
				else if (type.equals("num[]") || type.equals("bool[]") || type.equals("text[]"))
					res += "new ArrayList<" + type + ">(" + getter + ")";
				else if (type.endsWith("[]")) {
					String actualType = type.substring(0, type.length()-2);
					res += "new ArrayList<" + convertTypeForList(actualType) + ">();\n";
					res += getIndentation();
					
					res += "for (" + actualType + " t : " + getter + ")\n";
					++indentationLevel;
					res += getIndentation();
					res += ident + ".add(new " + actualType + "(t))";
					--indentationLevel;
				}
				else
					res += "new " + type + "(" + getter + ")";
			}
			else if (useSetter) {
				switch(node.getType()) {
				case basic:
					res += ".set(" + lastIdentIndex + ", " + exprRes + ")";
					break;
				case add:
					res += ".set(" + lastIdentIndex + ", " + generalIdent + ".get(" + lastIdentIndex + ")" + " + " + exprRes + ")";
					break;
				case sub:
					res += ".set(" + lastIdentIndex + ", " + generalIdent + ".get(" + lastIdentIndex + ")" + " - " + exprRes + ")";
					break;
				case mult:
					res += ".set(" + lastIdentIndex + ", " + generalIdent + ".get(" + lastIdentIndex + ")" + " * " + exprRes + ")";
					break;
				case div:
					res += ".set(" + lastIdentIndex + ", " + generalIdent + ".get(" + lastIdentIndex + ")" + " / " + exprRes + ")";
					break;
				case mod:
					res += ".set(" + lastIdentIndex + ", " + generalIdent + ".get(" + lastIdentIndex + ")" + " % " + exprRes + ")";
					break;
				default:
					throw new NotImplementedException();
				}
			}	
			else {
				String type = convertTypeForList((String) current.getNodeType());
				if (type.equals("num") || type.equals("bool") || type.equals("text"))
					res += exprRes;
				else if (type.equals("num[]") || type.equals("bool[]") || type.equals("text[]"))
					res += "new ArrayList<" + type + ">(" + exprRes + ")";
				else if (type.endsWith("[]")) {
					String actualType = type.substring(0, type.length()-2);
					res += "new ArrayList<" + convertTypeForList(actualType) + ">();\n";
					res += getIndentation();
					
					res += "for (" + actualType + " t : " + exprRes + ")\n";
					++indentationLevel;
					res += getIndentation();
					res += ident + ".add(new " + actualType + "(t))";
					--indentationLevel;
				}
				else 
					res += "new " + type + "(" + exprRes + ")";
			}

			if (i < inputSize-1) {
				res += ";\n";
				if (!creatingStructClass)
					res += getIndentation();
				else
					res += STRUCT_INDENTATION + STRUCT_INDENTATION;
			}
		}

		assigning = false;

		if (creatingStructClass) {
			defaultInstantiation += exprRes;
			if (inputSize > 1) {
				constructorParams += "java.util.List<Object> " + listName + ", ";
				currentListParam++;
			}
		}

		res += ";";
		if (creatingStructClass)
			res += "\n";
		return res;
	}

	@Override
	public String visit(BaseIdentNode node) {
		String res;
		if (node instanceof FuncCallNode)
			res = visit((FuncCallNode) node);
		else
			res = node.getIdent();
		ExpressionNode index = node.getIndex();
		if (node.getIndex() != null) {
			if (lastBaseIdent && assigning) {
				lastIdentIsArrayEntry = true;
				lastIdentIndex = castIndex(index);
			}
			else
				res += ".get(" + castIndex(index) + ")";
		}
		return res;
	}

	@Override
	public String visit(BoolLiteralNode node) {
		return node.getBool().toString();
	}

	@Override
	public String visit(CallStatementNode node) {
		return visit(node.getIdent())+";";
	}

	@Override
	public String visit(DataStructDeclarationNode node) {
		String res = "";
		String ident = node.getIdent();
		String type = node.getType();

		String dcl = type + " " + ident;

		if (creatingStructClass) {
			structHeader += STRUCT_INDENTATION + "public " + dcl + ";\n";

			// Add to copy constructor
			copyConstructor += STRUCT_INDENTATION + STRUCT_INDENTATION;
			copyConstructor += "this." + node.getIdent() + " = new " + type + "(other." + node.getIdent() + ");\n";
			
			// Add to default instantiation
			if (!assigning) {
				constructorParams += dcl + ", ";
				defaultInstantiation += structInstantiations.get(type);
			}

			return "this." + ident + " = " + ident + ";";
		}
		else if (initializingRobot) {
			header += "    private " + dcl + ";\n";
		}
		else {

		}

		if (assigning) {
			return dcl;
		}

		// Instantiate with default constructor
		res = ident + " = " + structInstantiations.get(ident);

		return res;
	}

	@Override
	public String visit(DataStructDefinitionNode node) {
		String typeName = node.getTypeName();
		String contents;

		structUsesArrays = false;

		String structImports = robopackage;
		structHeader = "public class " + typeName + " {\n";
		
		copyConstructor = STRUCT_INDENTATION + "public " + typeName + "(" + typeName + " other) {\n";

		creatingStructClass = true;
		currentListParam = 1;

		constructorParams = "";
		defaultInstantiation = "new " + node.getTypeName() + "(";
		contents = "";
		List<Object> dcls = node.getDeclarations();
		int dclsSize = dcls.size();
		for (int i = 0; i < dclsSize; ++i) {
			Object dcl = dcls.get(i);
			contents += STRUCT_INDENTATION + STRUCT_INDENTATION;
			if (dcl instanceof DeclarationNode)
				contents += visit((DeclarationNode) dcl);
			else if (dcl instanceof AssignmentNode)
				contents += visit((AssignmentNode) dcl);
			if (i < dclsSize-1)
				defaultInstantiation += ", ";
		}

		contents += STRUCT_INDENTATION + "}\n\n";
		copyConstructor += STRUCT_INDENTATION + "}\n}";
		
		if (structUsesArrays) {
			structImports += "import java.util.List;\n";
			structImports += "import java.util.ArrayList;\n";
			structImports += "\n";
		}
		
		constructorParams = constructorParams.substring(0, constructorParams.length()-2);
		defaultInstantiation += ")";


		structInstantiations.put(typeName, defaultInstantiation);

		try (OutputStream out = new BufferedOutputStream(
				Files.newOutputStream(Paths.get((roboname + "pk" + "/" + typeName + ".java")), CREATE, TRUNCATE_EXISTING))) {
			out.write(structImports.getBytes());
			out.write(structHeader.getBytes());
			out.write(("\n    public " + typeName + "(" + constructorParams + ") {\n").getBytes());
			out.write(contents.getBytes());
			out.write(copyConstructor.getBytes());
		}
		catch (IOException ex) {
			System.out.println("Failed to write file \"" + roboname + "pk" + "/" + typeName + ".java\"");
		}

		creatingStructClass = false;

		return "";
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
			return "";
		else if (node instanceof DataStructDeclarationNode)
			res = visit((DataStructDeclarationNode) node);
		else if (node instanceof ArrayDeclarationNode) {
			res = visit((ArrayDeclarationNode) node);
		}
		else
			throw new NotImplementedException();
		res += "\n";
		return res;
	}

	@Override
	public String visit(EqualityExprNode node) {
		return visit(node.getLeftChild()) + node.getType().toJavaSyntax() + visit(node.getRightChild());
	}

	@Override
	public String visit(EventDeclarationNode node) {
		String res = "";
		if(dcls.contains("public void "+getEventMethodName(node.getParam().getType())+"("+node.getParam().getType())){
			String temp = AddEventMethod(node);
			int event = dcls.indexOf("public void "+getEventMethodName(node.getParam().getType())+"("+node.getParam().getType(), 0);
			int firstP = dcls.indexOf('{',event);
			int lastP = dcls.indexOf('}',firstP);
			int last = firstP;
			while(true){
				int tempPlace = dcls.indexOf(';',last+1);
				if(tempPlace != -1 && tempPlace <= lastP){
					last = tempPlace;
				}
				else
					break;
			}
			String firstString = dcls.substring(0, last+1);
			indentationLevel++;
			String payloadString =getIndentation()+getEventName(node)+"("+node.getParam().getIdent()+");";
			indentationLevel--;
			String endStrng = dcls.substring(last+1);
			dcls= firstString+"\n"+payloadString+endStrng;
			res = temp;
		}
		else{
			res+=getIndentation()+"@Override\n";
			res+=getIndentation()+"public void "+getEventMethodName(node.getParam().getType())+"("+node.getParam().getType()+" "+node.getParam().getIdent()+"){\n";
			indentationLevel++;
			res+=getIndentation()+getEventName(node)+"("+node.getParam().getIdent()+");\n";
			indentationLevel--;
			res+=getIndentation()+"}\n\n";
			res+=AddEventMethod(node);
		}
		outputListSetInScope = false;

		return res;
	}

	private String getEventName(EventDeclarationNode node){
		if(getEventMethodName(node.getParam().getType()).equals(node.getIdent())){
			return "_"+node.getIdent();
		}
		else{
			return node.getIdent();
		}
	}

	private String AddEventMethod(EventDeclarationNode node){
		String rest = getIndentation()+"private void " + getEventName(node) +"("+node.getParam().getType()+ " " +node.getParam().getIdent()+"){\n";
		List<StatementNode> stms = node.getStatements();
		indentationLevel++; 
		for(StatementNode stm : stms)
			rest+=visit(stm);
		indentationLevel--;
		rest+=getIndentation()+"}\n";
		return rest;
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

		if (assign instanceof CallStatementNode)
			res+=visit((CallStatementNode) assign);
		else if (assign instanceof VarDeclarationNode)
			res+=visit((VarDeclarationNode) assign).replace(";", "; ");
		else if (assign instanceof AssignmentNode)
			res+=visit((AssignmentNode) assign).replace(";", "; ");
		else
			throw new NotImplementedException();

		res+=visit((ExpressionNode) node.predicate)+"; ";
		Object update = node.update;
		if (update instanceof CallStatementNode)
			res+=visit((CallStatementNode) update);
		else if (update instanceof AssignmentNode)
			res+=visit((AssignmentNode) update).replace(";","");
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
		String res = node.getIdent();

		res += "(";
		List<ExpressionNode> args = node.getArguments();
		int argsSize = args.size();
		for (int i = 0; i < argsSize; ++i) {
			ExpressionNode arg = args.get(i);
			res += visit(arg);
			if (i < argsSize-1)
				res += ", ";
		}
		res += ")";

		return res;
	}

	@Override
	public String visit(FuncDeclarationNode node) {
		String res = "";
		if(node.getReturnTypes().size()!= 1 && node.getReturnTypes().size() != 0)
			res = getIndentation()+ "public java.util.List<Object> " +node.getIdent() +"(";
		else
			res = getIndentation()+ "public " + convertTypeForList(visit(node.getReturnTypes().get(0)))+ " " +node.getIdent() +"(";
		List<VarNode> params = node.getParamList();

		for(VarNode param : params) {
			res += convertType(param.getType())+" " +param.getIdent()+", ";
		}
		if(node.getParamList().size()!=0){
			res = res.substring(0, res.length()-2);
		}
		res+="){\n";
		indentationLevel++;
		List<StatementNode> stms = node.getStatements();
		for(StatementNode stm : stms )
			res += visit(stm);
		indentationLevel--;
		res+= getIndentation()+"}\n";

		outputListSetInScope = false;

		return res;
	}

	@Override
	public String visit(GeneralIdentNode node) {
		String res = "";
		lastBaseIdent = false;
		lastIdentIsArrayEntry = false;
		boolean prevWasColor = false;

		List<BaseIdentNode> idents = node.getIdents();
		int identsSize = idents.size();
		String identRes = "";
		String prev;
		for (int i = 0; i < identsSize; ++i) {
			if (i == identsSize-1)
				lastBaseIdent = true;
			BaseIdentNode ident = idents.get(i);
			prev = identRes;
			identRes = visit(ident);
			
			// Class methods
			if (prev.equals("Color") && identRes.endsWith("()")) {
				identRes = identRes.substring(0, identRes.length()-2);
				usesColors = true;
			}
			
			if (prev.equals("Math")) {
				// Visit arguments
				FuncCallNode fc = (FuncCallNode) ident;
				List<ExpressionNode> argExprs = fc.getArguments();
				List<String> args = new ArrayList<String>();
				for (ExpressionNode expr : argExprs)
					args.add(visit(expr));
				
				identRes = MathCommands.parseCommand(fc.getIdent(), args);
				res = "";
			}
			else if (prev.equals("Output")) {
				// Visit arguments
				FuncCallNode fc = (FuncCallNode) ident;
				List<ExpressionNode> argExprs = fc.getArguments();
				List<String> args = new ArrayList<String>();
				for (ExpressionNode expr : argExprs)
					args.add(visit(expr));
				
				identRes = OutputCommands.parseCommand(fc.getIdent(), args);
				res = "";
			}
			
			res += identRes;
			if (i < identsSize-1)
				res += ".";
		}

		return res;
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
			res += getIndentation() + "else {\n";
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

		outputListSetInScope = false;

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

		outputListSetInScope = false;

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
		// Fetch robot declarations and struct definitions
		RobotDeclarationNode init = null;
		RobotDeclarationNode behavior = null;
		List<DataStructDefinitionNode> defs = new ArrayList<DataStructDefinitionNode>();

		List<DeclarationNode> declarations = node.getDeclarations();
		for(DeclarationNode dcl : declarations) {
			if (dcl instanceof RobotDeclarationNode) {
				RobotDeclarationNode robodcl = (RobotDeclarationNode) dcl;
				RobotDeclarationType type = robodcl.getType();
				if (type == RobotDeclarationType.name)
					roboname = robodcl.getName();
				else if (type == RobotDeclarationType.initialization)
					init = robodcl;
				else if (type == RobotDeclarationType.behavior)
					behavior = robodcl;
			}
			else if (dcl instanceof DataStructDefinitionNode)
				defs.add((DataStructDefinitionNode) dcl);
		}

		// Create directory for output files
		File dir = new File(roboname+"pk");

		// if the directory does not exist, create it
		if (!dir.exists()) {
			System.out.println("Creating directory " + roboname + "pk.");
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
				Files.newOutputStream(Paths.get((roboname+"pk/"+roboname + ".java")), CREATE, TRUNCATE_EXISTING))) {

			// Flags for use in code generation
			initializingRobot = false;
			creatingStructClass = false;
			structInstantiations = new Hashtable<String, String>();
			assigning = false;

			outputListSetInScope = false;

			usesColors = false;
			usesMath = false;
			usesArrays = false;

			robopackage = "package " + roboname + "pk;\n\n"; // FIXME roboname substring to avoid package name being too long

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

			// Struct definitions
			for (DataStructDefinitionNode def : defs)
				visit(def);	// Array initializations will be added to robot initialization

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

			// Package
			imports = robopackage;

			// Imports
			imports += "import robocode.*;\n";
			if (usesArrays) {
				imports += "import java.util.List;\n";
				imports += "import java.util.ArrayList;\n";
			}
			if (usesColors)
				imports += "import java.awt.Color;\n";
			imports += "\n";
			out.write(imports.getBytes());
			out.write(header.getBytes());
			out.write(dcls.getBytes());
			out.flush();
			out.close();
			if(compileBTR){
				String javaHome = System.getenv("JAVA_HOME");
				if(javaHome!=null){
					String roboHome = System.getenv("ROBOCODE_HOME");
					if(roboHome !=null){
						try{
							File error = new File(roboname+"pk/log.txt");
							ProcessBuilder command = new ProcessBuilder(javaHome+"\\bin\\javac","-cp",roboHome+"\\libs\\robocode.jar",roboname+"pk/"+roboname+".java");
							command.redirectError(error);
							Process ps = command.start();
							ps.waitFor();
							if(ps.exitValue()!=0){
								BufferedReader reader = new BufferedReader(new FileReader(error));
								String errorString = "";
								String line = null;
								while((line = reader.readLine())!=null)
									errorString += line+"\n";
								reader.close();
								System.out.println(errorString);
								error.delete();
								gui.DisplayError(errorString);
								System.exit(0);
							}
							error.delete();
						}
						catch(IOException ex){
							gui.DisplayError("Paths may not point to the right directory");
							System.exit(0);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(!generateJava){
							File f = new File(roboname+"pk/"+roboname+".java");
							f.delete();
						}
					}
					else{
						gui.DisplayError("Missing ROBOCODE_HOME variable");
					}
				}
				else{
					gui.DisplayError("Missing JAVA_HOME variable");
				}
			}

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

		List<ExpressionNode> returnExprs = node.getExpressions();
		int returnSize = returnExprs.size();

		if (returnSize == 1) {
			return "return " + visit(node.getExpressions().get(0)) + ";";
		}
		else {
			usesArrays = true;
			String res = "java.util.List<Object> _returnVals = new ArrayList<Object>();\n";

			// FIXME Object copying
			for (ExpressionNode expr : returnExprs) {
				res += getIndentation() + "_returnVals.add(" + visit(expr) + ");\n";
			}

			res += getIndentation() + "return _returnVals;";

			return res;
		}
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

		outputListSetInScope = false;

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
		res+="\n";
		return res;
	}


	@Override
	public String visit(TextLiteralNode node) {
		return "\"" + node.getText() + "\"";
	}

	@Override
	public String visit(TypeNode node) {
		return node.getType();
	}

	@Override
	public String visit(UnaryExprNode node) {
		return node.getType().toString() + visit(node.getChild());
	}

	@Override
	public String visit(VarDeclarationNode node) {
		String res = "";
		String exprRes = visit(node.getExpression());

		List<VarNode> input = node.getVariable();
		int inputSize = input.size();

		if (inputSize > 1 && !creatingStructClass) {
			if (!outputListSetInScope) {
				res = "java.util.List<Object> ";
				outputListSetInScope = true;
			}
			res += "_output = " + exprRes + ";\n" + STRUCT_INDENTATION + STRUCT_INDENTATION;
		}

		String listName = "_output";
		if (currentListParam > 1)
			listName += currentListParam;
		for (int i = 0; i < inputSize; ++i) {
			VarNode var = input.get(i);
			if (creatingStructClass) {				
				if (inputSize == 1)
					constructorParams += convertType(var.getType()) + " " + var.getIdent() + ", ";

				String ident = visit(var);

				if (assigning) {	// FIXME might not be necessary in the end
					if (currentInputSize > 1) {
						res += ident;
						res += " = ";
						res += "(" + convertTypeForList(var.getType()) + ") ";
						res += listName;
						res += ".get(" + i + ")";
					}
				}
				else if (inputSize > 1) {
					res += ident;
					res += " = ";
					res += "(" + convertTypeForList(var.getType()) + ") ";
					res += listName;
					res += ".get(" + i + ")";
				}
				else {
					res += "this.";
					res += ident;
					res += " = ";
					res += ident;
				}
				if (i < inputSize-1) {
					res += ";\n";
					res += STRUCT_INDENTATION + STRUCT_INDENTATION;			
				}
			}
			else {
				res += visit(var);
				res += " = ";
				if (inputSize > 1)
					res += "(" + convertTypeForList(var.getType()) + ") _output.get(" + i + ")";
				else
					res += exprRes;
				if (i < inputSize-1) {
					res += ";\n";
					res += getIndentation();
				}
			}
		}

		if (creatingStructClass) {
			defaultInstantiation += exprRes;
			if (inputSize > 1) {
				constructorParams += "java.util.List<Object> " + listName + ", ";
				currentListParam++;
			}
		}

		return res+";";
	}

	@Override
	public String visit(VarNode node) {
		String res = convertType(node.getType()) + " " + node.getIdent();
		if (creatingStructClass) {
			// Add to copy constructor
			String type = node.getType();
			String ident = node.getIdent();
			if (type.equals("num") || type.equals("text") || type.equals("bool")) {
				copyConstructor += STRUCT_INDENTATION + STRUCT_INDENTATION;
				copyConstructor += "this." + ident + " = other." + ident + ";\n";
			}
			
			structHeader += STRUCT_INDENTATION + "public " + res + ";\n";
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
			return "Double";
		case "text":
			return "String";
		case "bool":
			return "Boolean";
		default:
			return input;
		}
	}

	private String convertTypeForList(String input) {
		switch (input) {
		case "num":
			return "Double";
		case "text":
			return "String";
		case "bool":
			return "Boolean";
		case "num[]":
			return "ArrayList<Double>";
		case "text[]":
			return "ArrayList<String>";
		case "bool[]":
			return "ArrayList<Boolean>";
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
			return structInstantiations.get(input);
		}
	}

	private String castIndex(ExpressionNode index) {
		if (index instanceof NumLiteralNode) // FIXME An index of 1.5 should probably issue an error rather than being cast to 1
			return 	Integer.toString(((int) Double.parseDouble((visit(index)))));
		else 
			return "(int) (" + visit(index) + ")";
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
