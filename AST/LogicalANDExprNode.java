public class LogicalANDExprNode extends ExpressionNode {  
	private ExpressionNode leftChild;    // EqualityExpr
	private ExpressionNode rightChild;    // LogicalANDExpr
	
	public LogicalANDExprNode(ExpressionNode leftChild, ExpressionNode rightChild) {
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
