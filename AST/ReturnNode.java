import java.util.List;

class ReturnNode extends StatementNode {

	private List<ExpressionNode> expressions;
	
	public ReturnNode(int lineNumber, int colNumber) {
		super(lineNumber, colNumber);
	}
	
    public ReturnNode(int lineNumber, int colNumber, List<ExpressionNode> expressions) {
    	super(lineNumber, colNumber);
		this.expressions = expressions;
	}
    
    public List<ExpressionNode> getExpressions() {
    	return expressions;
    }

}
