class UnaryExprNode extends ExpressionNode {
	private ExpressionNode child;    // UnaryExpr
	
	public UnaryExprNode(int lineNumber, int colNumber, ExpressionNode child) {
		super(lineNumber, colNumber);
		this.child = child;
	}
	
	public ExpressionNode getChild() {
		return child;
	}
}
