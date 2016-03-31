import java.io.FileReader;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
public class Hello 
{
    public static void main( String[] args) throws Exception 
    {

        ANTLRInputStream input = new ANTLRInputStream(new FileReader("theMachine"));

        HelloLexer lexer = new HelloLexer(input);

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        HelloParser parser = new HelloParser(tokens);
        SymbolTable t = new SymbolTable();
        
        try {
        	HelloParser.ProgContext cst = parser.prog();
        	ProgramNode ast = (ProgramNode) new BuildASTVisitor().visitProg(cst);
        	new PrettyPrintVisitor().visit(ast);
        }
        catch (Exception ex) {
        	System.out.println(ex.getMessage());
        }
        
        //ParseTree tree = parser.prog(); // begin parsing at rule 'r'
        //System.out.println(tree.toStringTree(parser)); // print LISP-style tree
    }
}
