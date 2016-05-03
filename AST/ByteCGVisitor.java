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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(EqualityExprNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(EventDeclarationNode node) {
		// TODO Auto-generated method stub
		return null;
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
			
			code =".class "+roboname+"pk"+"/"+roboname+"\n";
			code +=".super robocode/Robot";
			code +="\n\n\n";
			code +=".method public run()V\n";
			
			if (init != null)
				code += visit(init);
			
			code +="WHILE:\n";
			// Robot behavior
			if (behavior != null)
				code += visit(behavior);
			
			code +="goto WHILE\n";
			code +="return \n";
			code +=".end method\n";
			
			String temp;
			// Declarations
			for(DeclarationNode dcl : declarations){
			    temp = visit(dcl);
			    code += temp;
			}			
			
			out.write(code.getBytes());
			out.flush();
			out.close();
//			Process ps = Runtime.getRuntime().exec("java -jar jasmin.jar "+roboname+"pk/"+roboname+".j");
//			try {
//				ps.waitFor();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		    InputStream in = ps.getInputStream();
//		    InputStream err = ps.getErrorStream();
//
//		    byte b[]=new byte[in.available()];
//		    in.read(b,0,b.length);
//		    System.out.println(new String(b));
//
//		    byte c[]=new byte[err.available()];
//		    err.read(c,0,c.length);
//		    System.out.println(new String(c));
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
				for (StatementNode stm : input)
					res += visit(stm);
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(VarNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(WhileNode node) {
		// TODO Auto-generated method stub
		return null;
	}

}
