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
import nodes.RobotDeclarationNode.RobotDeclarationType;


public class ByteCGVisitor extends ASTVisitor<String>{
	private String code;
	private String roboname;
	private int whileCounter = 0;
	private String currentLabel;
	private boolean isInit;
	private String header = "";
	
	@Override
	public String visit(AdditiveExprNode node) {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
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
			String payloadString = ";Invoke help plz replace me\n";
			String endStrng = code.substring(lastP-1);
			code= firstString+"\n"+payloadString+endStrng;
			res = temp;
		}
		else{
			res+=".method public "+getEventMethodName(node.getParam().getType())+"("+getEventParam(node.getParam().getType())+";)V\n";
			res+=";Invoke help plz replace me\n";
			res+=".end method\n\n";
			res+=AddEventMethod(node);
		}
		
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
		String rest = ".method private " + getEventName(node) +"("+getEventParam(node.getParam().getType())+";)V\n";
		List<StatementNode> stms = node.getStatements();
		for(StatementNode stm : stms)
			rest+=visit(stm);
		rest+=".end method\n\n";
		return rest;
	}

	@Override
	public String visit(ExpressionNode node) {
		// TODO Auto-generated method stub
		return null;
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
		String res = "";
		if(node.getReturnTypes().size()!= 1 && node.getReturnTypes().size() != 0){
			res = ".method public "+node.getIdent()+"(";
			List<VarNode> params = node.getParamList();

			for(VarNode param : params) {
				res += convertType(param.getType())+";";
			}
			res += ")a";
		}
			
		else{
			res = ".method public "+node.getIdent()+"(";
			List<VarNode> params = node.getParamList();


			for(VarNode param : params) {
				res += convertType(param.getType())+";";
			}
			res += ")"+convertReturnType(node.getReturnTypes().get(0).getType());
		}
		res += "\n";
		
		List<StatementNode> stms = node.getStatements();
		for(StatementNode stm : stms )
			res += visit(stm);
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(IterationNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(LogicalANDExprNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(LogicalORExprNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(MultExprNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(NumLiteralNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(ParenthesesNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(PrimaryExprNode node) {
		// TODO Auto-generated method stub
		return null;
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
			header +=".method public <init>()V\n";
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
			
			header +=".end method\n";
			out.write(header.getBytes());
			out.write(code.getBytes());
			out.flush();
			out.close();
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
		}
		catch (IOException ex) {
			System.out.println("Failed to write target file \"" + roboname + ".j");
		}
		
		return null;
	}

	@Override
	public String visit(RelationExprNode node) {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(TypeNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(UnaryExprNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(VarDeclarationNode node) {
		String res = "";
		for (VarNode var : node.getVariable()) {
			res += visit(var);
		}
		return res;
	}

	@Override
	public String visit(VarNode node) {
		// TODO Auto-generated method stub
		return null;
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
			return "robocode.BulletHit";
		case "BulletHitBulletEvent":
			return "robocode.BulletHitBullet";
		case "BulletMissedEvent":
			return "robocode.BulletMissed";
		case "DeathEvent":
			return "robocode.Death";
		case "HitByBulletEvent":
			return "robocode.HitByBullet";
		case "HitRobotEvent":
			return "robocode.HitRobot";
		case "HitWallEvent":
			return "robocode.HitWall";
		case "RobotDeathEvent":
			return "robocode.RobotDeath";
		case "ScannedRobotEvent":
			return "robocode.ScannedRobot";
		case "StatusEvent":
			return "robocode.Status";
		case "WinEvent":
			return "robocode.Win";
		case "BattleEndedEvent":
			return "robocode.BattleEnded";
		case "RoundEndedEvent":
			return "robocode.RoundEnded";
		default:
			throw new NotImplementedException();
		}
	}

}
