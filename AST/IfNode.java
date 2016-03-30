import java.util.List;


public class IfNode extends StatementNode {
    
    protected ExpressionNode expr;
    
    protected List<StatementNode> ifBlockStatements;
    
    public IfNode() {   	
    }
    
    public IfNode(ExpressionNode expr, List<StatementNode> ifBlockStatements) {
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
