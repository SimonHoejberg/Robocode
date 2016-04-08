package nodes;
public class ParenthesesNode extends PrimaryExprNode {
	private ExpressionNode child;

	public ParenthesesNode(int lineNumber, int colNumber, ExpressionNode child) {
		super(lineNumber, colNumber);
		this.child = child;
	}
	
	public ExpressionNode getChild() {
		return child;
	}
}
