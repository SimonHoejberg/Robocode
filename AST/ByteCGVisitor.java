import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

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
			 Files.newOutputStream(Paths.get((roboname+"/"+roboname + ".j")), CREATE, TRUNCATE_EXISTING))) {
			
			
			out.write(code.getBytes());
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(StatementNode node) {
		// TODO Auto-generated method stub
		return null;
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
