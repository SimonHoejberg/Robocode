import java.io.FileReader;
import java.util.List;

import nodes.ProgramNode;

import org.antlr.v4.runtime.*;
public class Hello 
{
    public static void main( String[] args) throws Exception 
    {

        ANTLRInputStream input = new ANTLRInputStream(new FileReader("theMachine"));

        HelloLexer lexer = new HelloLexer(input);

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        HelloParser parser = new HelloParser(tokens);
        
        //try {
        	HelloParser.ProgContext cst = parser.prog();
        	ProgramNode ast = (ProgramNode) new BuildASTVisitor().visitProg(cst);
        	new PrettyPrintVisitor().visit(ast);
        	TypeCheckVisitor vis = new TypeCheckVisitor();
        	vis.visit(ast);
        	
        	List<TypeCheckError> errors = vis.getErrorList(); 
        	for(TypeCheckError error : errors)
        		System.out.println("Error "+ error.msg + " on " + error.node.getLineNumber() +":" +error.node.getColumnNumber());
        /*}
        catch (Exception ex) {
        	System.out.println(ex.getMessage());
        }*/
        
        
        //ParseTree tree = parser.prog(); // begin parsing at rule 'r'
        //System.out.println(tree.toStringTree(parser)); // print LISP-style tree
    }
}
