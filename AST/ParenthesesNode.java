public class ParenthesesNode extends PrimaryExprNode {
	private ExpressionNode child;

	public ParenthesesNode(ExpressionNode child) {
		this.child = child;
	}
	
	public ExpressionNode getChild() {
		return child;
	}
}
