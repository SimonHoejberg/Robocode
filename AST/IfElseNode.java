import java.util.List;


public class IfElseNode extends IfNode {
	
    private List<StatementNode> elseBlockStatements;
	
	public IfElseNode(ExpressionNode expr, List<StatementNode> ifBlockStatements, List<StatementNode> elseBlockStatements) {
    	this.expr = expr;
    	this.ifBlockStatements = ifBlockStatements;
    	this.elseBlockStatements = elseBlockStatements;
    }
    
    public List<StatementNode> getElseBlockStatements() {
    	return elseBlockStatements;
    }
}
