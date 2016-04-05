import java.util.List;


public class ElseIfNode extends IfNode {

    private IfNode next;
	
	public ElseIfNode(int lineNumber, int colNumber, ExpressionNode expr, List<StatementNode> ifBlockStatements, IfNode next) {
		super(lineNumber, colNumber);
		this.expr = expr;
    	this.ifBlockStatements = ifBlockStatements;
    	this.next = next;
    }
    
    public IfNode getNext() {
    	return next;
    }
}
