public class ParenthesesNode extends PrimaryExprNode {
	ExpressionNode child;

	public ParenthesesNode(ExpressionNode child) {
		this.child = child;
	}
}
