package nodes;
import java.util.List;


public class IfElseNode extends IfNode {
	
    private List<StatementNode> elseBlockStatements;
	
	public IfElseNode(int lineNumber, int colNumber, ExpressionNode expr, List<StatementNode> ifBlockStatements, List<StatementNode> elseBlockStatements) {
		super(lineNumber, colNumber);
		this.expr = expr;
    	this.ifBlockStatements = ifBlockStatements;
    	this.elseBlockStatements = elseBlockStatements;
    }
    
    public List<StatementNode> getElseBlockStatements() {
    	return elseBlockStatements;
    }
}
