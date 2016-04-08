package nodes;
import java.util.List;


public class IfNode extends StatementNode {
    
    protected ExpressionNode expr;
    
    protected List<StatementNode> ifBlockStatements;
    
    public IfNode(int lineNumber, int colNumber) {
    	super(lineNumber, colNumber);
    }
    
    public IfNode(int lineNumber, int colNumber, ExpressionNode expr, List<StatementNode> ifBlockStatements) {
    	super(lineNumber, colNumber);
    	this.expr = expr;
 		this.ifBlockStatements = ifBlockStatements;
 	}
    
    public ExpressionNode getExpression() {
    	return expr;
    }
    
    public List<StatementNode> getIfBlockStatements() {
    	return ifBlockStatements;
    }

}
