import java.io.FileReader;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
public class Hello2 
{
    public static void main( String[] args) throws Exception 
    {

        ANTLRInputStream input = new ANTLRInputStream(new FileReader("input"));

        Hello2Lexer lexer = new Hello2Lexer(input);

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        Hello2Parser parser = new Hello2Parser(tokens);
        ParseTree tree = parser.prog(); // begin parsing   at rule 'r'
        System.out.println(tree.toStringTree(parser)); // print LISP-style tree
    }
}
