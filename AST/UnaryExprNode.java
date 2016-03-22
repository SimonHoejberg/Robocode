class UnaryExprNode extends ExpressionNode {
	private ExpressionNode child;    // UnaryExpr
	
	public UnaryExprNode(ExpressionNode child) {
		this.child = child;
	}
	
	public ExpressionNode getChild() {
		return child;
	}
}
