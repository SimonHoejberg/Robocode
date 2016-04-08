package nodes;
public class LogicalANDExprNode extends ExpressionNode {  
	private ExpressionNode leftChild;    // EqualityExpr
	private ExpressionNode rightChild;    // LogicalANDExpr
	
	public LogicalANDExprNode(int lineNumber, int colNumber, ExpressionNode leftChild, ExpressionNode rightChild) {
		super(lineNumber, colNumber);
		this.leftChild = leftChild;
		this.rightChild = rightChild;
	}
	
	public ExpressionNode getLeftChild() {
		return leftChild;
	}
	
	public ExpressionNode getRightChild() {
		return rightChild;
	}
}
