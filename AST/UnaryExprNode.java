class UnaryExprNode extends ExpressionNode {
	ExpressionNode child;    // UnaryExpr
	
	public UnaryExprNode(ExpressionNode child) {
		this.child = child;
	}
}
