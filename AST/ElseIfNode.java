import java.util.List;


public class ElseIfNode extends IfNode {

    private IfNode next;
	
	public ElseIfNode(ExpressionNode expr, List<StatementNode> ifBlockStatements, IfNode next) {
    	this.expr = expr;
    	this.ifBlockStatements = ifBlockStatements;
    	this.next = next;
    }
    
    public IfNode getNext() {
    	return next;
    }
}
