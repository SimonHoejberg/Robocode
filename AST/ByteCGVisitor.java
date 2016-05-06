import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import exceptions.NotImplementedException;
import nodes.*;
import nodes.EqualityExprNode.EqualityType;
import nodes.RobotDeclarationNode.RobotDeclarationType;


public class ByteCGVisitor extends ASTVisitor<String>{
	private String code;
	private String roboname;
	private String initString = "";
	private int eqCounter = 0;
	private int andCounter = 0;
	private int orCounter = 0;
	private int forCounter = 0;
	private int ifCounter = 0;
	private int relCounter = 0;
	private int unCounter = 0;
	private int whileCounter = 0;
	private String currentLabel;
	private boolean isInit;
	private String header = "";
	
	@Override
	public String visit(AdditiveExprNode node) {
		String res = visit(node.getLeftChild());
		res += visit(node.getRightChild());
		
		switch (node.getType()) {
			case add:
				res += "dadd\n";
				break;
			case sub:
				res += "dsub\n";
				break;
			default:
				throw new NotImplementedException();
		}
		
		return res;
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
		return node.getBool().booleanValue() ? "1" : "0";
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
		// TODO Auto-generated method stub
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
		String res = visit(node.getLeftChild());
		res += visit(node.getRightChild());
		EqualityType eqType = node.getType();
		switch((String) node.getLeftChild().getNodeType()) {
			case "num":
				res += "dcmpg\n";
				res += "ifne\t";
				res += "EQ" + eqCounter + "\n";
				res += eqType == EqualityType.equal ? "iconst_0\n" : "iconst_1\n";
				res += "goto\t";
				res += "EQ" + (eqCounter + 1) + "\n";
				res += "EQ" + eqCounter + ": ";
				res += eqType == EqualityType.equal ? "iconst_0\n" : "iconst_1\n";
				res += "EQ" + (eqCounter + 1) + ": ";
				eqCounter += 2;
				break;
			case "bool":
				res += "if_icmpne\t";
				res += "EQ" + eqCounter + "\n";
				res += eqType == EqualityType.equal ? "iconst_1\n" : "iconst_0\n";
				res += "goto\t";
				res += "EQ" + (eqCounter + 1) + "\n";
				res += "EQ" + eqCounter + ": ";
				res += eqType == EqualityType.equal ? "iconst_0\n" : "iconst_1\n";
				res += "EQ" + (eqCounter + 1) + ": ";
				eqCounter += 2;
				break;
			case "text":
				// TODO 
				break;
			default:
				throw new NotImplementedException();
		}
		return res;
	}

	@Override
	public String visit(EventDeclarationNode node) {
		String res = "";
		if(code.contains(".method public "+getEventMethodName(node.getParam().getType())+"("+getEventParam(node.getParam().getType()))){
			String temp = AddEventMethod(node);
			int event = code.indexOf(".method public "+getEventMethodName(node.getParam().getType())+"("+getEventParam(node.getParam().getType()), 0);
			int firstP = code.indexOf(')',event);
			int lastP = code.indexOf(".end",firstP);

			String firstString = code.substring(0, lastP-1);
			String payloadString = "invokevirtual "+roboname+"pk/"+roboname+"/"+"\n";
			String endStrng = code.substring(lastP-1);
			code= firstString+"\n"+payloadString+endStrng;
			res = temp;
		}
		else{
			res+=".method public "+getEventMethodName(node.getParam().getType())+"("+getEventParam(node.getParam().getType())+";)V\n";
			res+="invokevirtual " + roboname + "pk/"+roboname+"/"+"\n";
			res+=".end method\n\n";
			res+=AddEventMethod(node);
		}
		
		return res;
	}
	
	private String getEventName(EventDeclarationNode node){
		if(getEventMethodName(node.getParam().getType()).equals(node.getIdent())){
			return "_"+node.getIdent();
		}
		else {
			return node.getIdent();
		}
	}
	
	private String AddEventMethod(EventDeclarationNode node){
		String rest = ".method private " + getEventName(node) +"("+getEventParam(node.getParam().getType())+";)V\n";
		List<StatementNode> stms = node.getStatements();
		for(StatementNode stm : stms)
			rest+=visit(stm);
		rest+=".end method\n\n";
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
		String res;
		Object assign = node.assign;
		
		if (assign instanceof CallStatementNode)
			res = visit((CallStatementNode) assign);
		else if (assign instanceof VarDeclarationNode)
			res = visit((VarDeclarationNode) assign);
		else if (assign instanceof AssignmentNode)
			res = visit((AssignmentNode) assign);
		else
			throw new NotImplementedException();
		
		int predicateNum = forCounter++;
		int endNum = forCounter++;
		
		res += "FOR" + predicateNum + ": ";
		res += visit((ExpressionNode) node.predicate);
		res += "ifne\t";
		res += "FOR" + endNum + "\n";
		
		// Visit body
		List<StatementNode> stms = node.getStatements();
		for(StatementNode stm : stms)
			res+=visit(stm);
		
		Object update = node.update;
		if (update instanceof CallStatementNode)
			res += visit((CallStatementNode) update);
		else if (update instanceof AssignmentNode)
			res += visit((AssignmentNode) update);
		else
			throw new NotImplementedException();
		
		res += "goto\t";
		res += "FOR" + predicateNum + "\n";
		res += "FOR" + endNum + ": ";
		
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
		if(node.getReturnTypes().size()!= 1 && node.getReturnTypes().size() != 0){
			res = ".method public "+node.getIdent()+"(";
			List<VarNode> params = node.getParamList();

			for(VarNode param : params) {
				res += convertType(param.getType());
			}
			res += ")a";
		}
			
		else{
			res = ".method public "+node.getIdent()+"(";
			List<VarNode> params = node.getParamList();


			for(VarNode param : params) {
				res += convertType(param.getType());
			}
			res += ")"+convertReturnType(node.getReturnTypes().get(0).getType());
		}
		res += "\n";
		
		List<StatementNode> stms = node.getStatements();
		for(StatementNode stm : stms )
			res += visit(stm);
		res += "return\n";
		res += ".end method\n";		
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
		int notNum = ifCounter++;
		
		res = visit(node.getExpression());
		res += "ifne\t";
		res += "IF" + notNum + "\n";
		
		List<StatementNode> stms = node.getIfBlockStatements();
		for(StatementNode stm : stms)
			res += visit(stm);

		int trueNum;
		switch (node.getClass().getName()) {
		case "nodes.IfNode":
			res += "IF" + notNum + ": ";
			break;
		case "nodes.IfElseNode":
			trueNum = ifCounter++;
			res += "goto\t";
			res += "IF" + trueNum + "\n";
			
			res += "IF" + notNum + ": ";
			List<StatementNode> elseStms = ((IfElseNode) node).getElseBlockStatements();
			for(StatementNode stm : elseStms)
				res += visit(stm);
			res += "IF" + trueNum + ": ";
			
			break;
		case "nodes.ElseIfNode":
			trueNum = ifCounter++;
			res += "goto\t";
			res += "IF" + trueNum + "\n";
			
			res += "IF" + notNum + ": ";
			
			res += visit(((ElseIfNode) node).getNext());

			res += "IF" + trueNum + ": ";
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
		String res = "";
		
		int endNum = andCounter++;
		res += visit(node.getLeftChild());
		res += "ifne\t";
		res += "AND" + endNum + "\n";
		res += visit(node.getRightChild());
		res += "ifne\t";
		res += "AND" + endNum + "\n";
		res += "iconst_1\n";
		res += "AND" + endNum + ": ";
		
		return res;
	}

	@Override
	public String visit(LogicalORExprNode node) {
		String res = "";
		
		int eqNum = orCounter++;
		int endNum = orCounter++;
		res += visit(node.getLeftChild());
		res += "ifeq\t";
		res += "OR" + eqNum + "\n";
		
		res += visit(node.getRightChild());
		res += "ifeq\t";
		res += "OR" + eqNum + "\n";
		
		res += "iconst_0\n";
		res += "goto\t";
		res += "OR" + endNum + "\n";
		
		res += "OR" + eqNum + ": "; 
		res += "iconst_1\n";
		
		res += "OR" + endNum + ": ";
		
		return res;
	}

	@Override
	public String visit(MultExprNode node) {
		String res = visit(node.getLeftChild());
		res += visit(node.getRightChild());
		switch (node.getType()) {
			case mult:
				res += "dmul\n";
				break;
			case div:
				res += "ddiv\n";
				break;
			case mod:
				res += "drem\n";
				break;
			default:
				throw new NotImplementedException();
		}
		
		return res;
	}

	@Override
	public String visit(NumLiteralNode node) {
		return String.valueOf(node.getValue());
	}

	@Override
	public String visit(ParenthesesNode node) {
		return visit(node.getChild());
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
				if (type == RobotDeclarationType.name) {
					roboname = robodcl.getName();
					roboname = roboname.substring(1, roboname.length()-1);
				}
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
			 Files.newOutputStream(Paths.get((roboname+"pk/"+roboname + ".j")), CREATE, TRUNCATE_EXISTING))) {
			
			header =".class "+roboname+"pk"+"/"+roboname+"\n";
			header +=".super robocode/Robot\n";
			header +="\n";
			initString +=".method public <init>()V\n";
			initString +="aload_0 \n";
			initString += "invokespecial robocode/Robot/<init>()V \n";
			initString += "return \n";
			code +="\n";
			code +=".method public run()V\n";
			
			if (init != null)
				code += visit(init);
			
			code +="WHILE:\n";
			// Robot behavior
			if (behavior != null)
				code += visit(behavior);
			
			code +="goto WHILE\n";
			code +="return \n";
			code +=".end method\n\n";
			
			String temp;
			// Declarations
			for(DeclarationNode dcl : declarations){
			    temp = visit(dcl);
			    code += temp;
			}			
			
			initString +=".end method\n";
			out.write(header.getBytes());
			out.write(initString.getBytes());
			out.write(code.getBytes());
			out.flush();
			out.close();
			try{
			Process ps = Runtime.getRuntime().exec("java -jar jasmin.jar "+roboname+"pk/"+roboname+".j");
			try {
				ps.waitFor();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    InputStream in = ps.getInputStream();
		    InputStream err = ps.getErrorStream();

		    byte b[]=new byte[in.available()];
		    in.read(b,0,b.length);
		    System.out.println(new String(b));

		    byte c[]=new byte[err.available()];
		    err.read(c,0,c.length);
		    System.out.println(new String(c));
			}catch(IOException ex){
				ex.printStackTrace();
			}
		}
		catch (IOException ex) {
			System.out.println("Failed to write target file \"" + roboname + ".j");
		}
		
		return null;
	}

	@Override
	public String visit(RelationExprNode node) {
		String res = "dcmpg\n";
		
		int trueNum = relCounter++;
		int endNum = relCounter++;
		
		switch (node.getType()) {
			case lessThan:
				res += "iflt\t";
				break;
			case lessThanOrEqual:
				res += "ifle\t";
				break;
			case greaterThan:
				res += "ifgt\t";
				break;
			case greaterThanOrEqual:
				res += "ifge\t";
				break;
			default:
				throw new NotImplementedException();
		}
		res += "REL" + trueNum + "\n";
		
		res += "iconst_0\n";
		res += "goto\t";
		res += "REL" + endNum + "\n";
		
		res += "REL" + trueNum + ": ";
		res += "iconst_1\n";
		
		res += "REL" + endNum + ": ";
		
		return res;
	}

	@Override
	public String visit(ReturnNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(RobotDeclarationNode node) {
		String res = "";
		List<StatementNode> input = node.getStatements();
		switch (node.getType()) {
			case initialization:
				isInit = true;
				for (StatementNode stm : input)
					res += visit(stm);
				isInit = false;
				break;
			case behavior:
				for (StatementNode stm : input)
					res += visit(stm);
				break;
			default:
				throw new NotImplementedException();
		}
		
		return res;
	}

	@Override
	public String visit(StatementNode node) {
		String res = "";
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
		return node.getText();
	}

	@Override
	public String visit(TypeNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(UnaryExprNode node) {
		String res;
		
		res = visit(node.getChild());
		
		switch (node.getType()) {
			case negation:
				res += "dneg\n";
				break;
			case not:
				int trueNum = unCounter++;
				int endNum = unCounter++;
				res += "ifeq\t";
				res += "UN" + trueNum + "\n";
				
				res += "iconst_1";
				res += "goto\t";
				res += "UN" + endNum + "\n";
				
				res += "UN" + trueNum + ": ";
				res += "iconst_0\n";
				
				res += "UN" + endNum + ": ";
				break;
			default:
				throw new NotImplementedException();
		}
		
		return res;
	}

	@Override
	public String visit(VarDeclarationNode node) {
		String res = "";
		for (VarNode var : node.getVariable()) {
			if (isInit) {
				header += visit(var) + visit(node.getExpression()) + "\n";
			}
			else {
				res += visit(var) + visit(node.getExpression()) + "\n";
			}
		}
		return res;
	}

	@Override
	public String visit(VarNode node) {
		String res = node.getIdent() + " " + convertType(node.getType());
		if (isInit)
		return ".field private " + res + " = ";
	
		return res;
	}

	@Override
	public String visit(WhileNode node) {
		String res = "While#"+whileCounter+"\n";
		currentLabel ="Done#"+whileCounter;
		List<ExpressionNode> exprs = node.getExpressions();
		for(ExpressionNode expr : exprs)
			res += visit(expr);
		List<StatementNode> stms = node.getStatements();
		for(StatementNode stm : stms)
			res += visit(stm);
		res += "goto While#"+whileCounter+"\n";
		res += currentLabel+"\n";
		currentLabel = null;
		return res;
	}
	
	private String convertReturnType(String input) {
		switch (input) {
			case "num":
				return "D";
			case "text":
				return "a";
			case "bool":
				return "Z";
			case "void":
				return "V";
			default:
				return "a";
		}
	}
	
	private String convertType(String input) {
		switch (input) {
			case "num":
				return "D";
			case "text":
				return "Ljava/lang/String";
			case "bool":
				return "Z";
			default:
				return "Ljava/lang/Object";
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
	
	private String getEventParam(String input){
		switch (input) {
		case "BulletHitEvent":
			return "Lrobocode/BulletHitEvent";
		case "BulletHitBulletEvent":
			return "Lrobocode/BulletHitBulletEvent";
		case "BulletMissedEvent":
			return "Lrobocode/BulletMissedEvent";
		case "DeathEvent":
			return "Lrobocode/DeathEvent";
		case "HitByBulletEvent":
			return "Lrobocode/HitByBulletEvent";
		case "HitRobotEvent":
			return "Lrobocode/HitRobotEvent";
		case "HitWallEvent":
			return "Lrobocode/HitWallEvent";
		case "RobotDeathEvent":
			return "Lrobocode/RobotDeathEvent";
		case "ScannedRobotEvent":
			return "Lrobocode/ScannedRobotEvent";
		case "StatusEvent":
			return "Lrobocode/StatusEvent";
		case "WinEvent":
			return "Lrobocode/WinEvent";
		case "BattleEndedEvent":
			return "Lrobocode/BattleEndedEvent";
		case "RoundEndedEvent":
			return "Lrobocode/RoundEndedEvent";
		default:
			throw new NotImplementedException();
		}
	}

}
