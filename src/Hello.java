import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import nodes.ProgramNode;

import org.antlr.v4.runtime.*;
public class Hello 
{
	
    public static void main( String[] args) throws Exception
    {        
        ANTLRInputStream input = new ANTLRInputStream(new FileReader("TheMachine.btr"));

		HelloLexer lexer = new HelloLexer(input);

		CommonTokenStream tokens = new CommonTokenStream(lexer);

		HelloParser parser = new HelloParser(tokens);

		HelloParser.ProgContext cst = parser.prog();
		ProgramNode ast = (ProgramNode) new BuildASTVisitor().visitProg(cst);
		new PrettyPrintVisitor().visit(ast);
		TypeCheckVisitor typeCheckVis = new TypeCheckVisitor();
		typeCheckVis.addFuncDcls(ast);
		typeCheckVis.visit(ast);

		List<TypeCheckProblem> problems = typeCheckVis.getProblems(); 
		int errors = typeCheckVis.getNumberOfErrors();
		int warnings = typeCheckVis.getNumberOfWarnings();
		if(errors!=0 && warnings !=0)
			System.out.println(errors +" Errors, " + warnings +" Warnings");
		else if(errors !=0 )
			System.out.println(errors +" Errors");
		else if(warnings !=0)
			System.out.println(warnings +" Warnings");
		TypeCheckError TError = null;
		TypeCheckWarning TWarn = null;
		if(errors!=0 || warnings !=0)
			for(Object error : problems){
				if(error instanceof TypeCheckError){
					TError = (TypeCheckError) error;
					System.out.println(TError.node.getLineNumber() +":" +TError.node.getColumnNumber() + "\tError:\t\t"+ TError.msg);
				}
				else if(error instanceof TypeCheckWarning){
					TWarn = (TypeCheckWarning) error;
					System.out.println(TWarn.node.getLineNumber() +":" +TWarn.node.getColumnNumber() + "\tWarning:\t"+ TWarn.msg);
				}
			}

		if (errors == 0) {
			JavaCGVisitor javaCGVis = new JavaCGVisitor();
			javaCGVis.visit(ast);
			ByteCGVisitor byteCGVis = new ByteCGVisitor();
			byteCGVis.visit(ast);
		}


		//ParseTree tree = parser.prog(); // begin parsing at rule 'r'
		//System.out.println(tree.toStringTree(parser)); // print LISP-style tree    
	}
	private Gui gui;
	public void SetGuiPointer(Gui gui){
		this.gui = gui;
	}

	public void Start(String file,boolean Java) throws FileNotFoundException, IOException{
		ANTLRInputStream input = new ANTLRInputStream(new FileReader(file));

		HelloLexer lexer = new HelloLexer(input);

		CommonTokenStream tokens = new CommonTokenStream(lexer);

		HelloParser parser = new HelloParser(tokens);
		BtrErrorListner btrError = new BtrErrorListner();
		parser.addErrorListener(btrError);
		HelloParser.ProgContext cst = parser.prog();
		ProgramNode ast = (ProgramNode) new BuildASTVisitor().visitProg(cst);
		if(btrError.getErrors().equals("")){
			TypeCheckVisitor typeCheckVis = new TypeCheckVisitor();
			typeCheckVis.addFuncDcls(ast);
			typeCheckVis.visit(ast);

			List<TypeCheckProblem> problems = typeCheckVis.getProblems(); 
			int errors = typeCheckVis.getNumberOfErrors();
			int warnings = typeCheckVis.getNumberOfWarnings();
			String errorStream ="";
			if(errors!=0 && warnings !=0)
				errorStream += errors +" Errors, " + warnings +" Warnings\n";
			else if(errors !=0 )
				errorStream +=errors +" Errors\n";
			else if(warnings !=0)
				errorStream +=warnings +" Warnings\n";
			TypeCheckError TError = null;
			TypeCheckWarning TWarn = null;
			if(errors!=0 || warnings !=0){
				for(Object error : problems){
					if(error instanceof TypeCheckError){
						TError = (TypeCheckError) error;
						errorStream +=TError.node.getLineNumber() +":" +TError.node.getColumnNumber() + "\tError:\t\t"+ TError.msg+"\n";
					}
					else if(error instanceof TypeCheckWarning){
						TWarn = (TypeCheckWarning) error;
						errorStream +=TWarn.node.getLineNumber() +":" +TWarn.node.getColumnNumber() + "\tWarning:\t"+ TWarn.msg+"\n";
					}
				}
				gui.ShowConsole(errorStream);
				if(errors != 0)
					System.exit(0);
			}


			if (errors == 0) {
				JavaCGVisitor javaCGVis = new JavaCGVisitor();
				javaCGVis.SetGenerateJava(Java);
				javaCGVis.SetGuiPointer(gui);
				javaCGVis.visit(ast);
			} 
		}
		else{
			gui.ShowConsole(btrError.getErrors());
			System.exit(0);
		}
	}
}
