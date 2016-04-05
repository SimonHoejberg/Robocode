class ReturnNode extends StatementNode {

	private ExpressionNode expr;
	
	public ReturnNode(int lineNumber, int colNumber) {
		super(lineNumber, colNumber);
	}
	
    public ReturnNode(int lineNumber, int colNumber, ExpressionNode expr) {
    	super(lineNumber, colNumber);
		this.expr = expr;
	}
    
    public ExpressionNode getExpression() {
    	return expr;
    }

}
