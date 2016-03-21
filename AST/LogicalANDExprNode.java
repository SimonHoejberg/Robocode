public class LogicalANDExprNode extends ExpressionNode {  
	ExpressionNode leftChild;    // EqualityExpr
	ExpressionNode rightChild;    // LogicalANDExpr
	
	public LogicalANDExprNode(ExpressionNode leftChild, ExpressionNode rightChild) {
		this.leftChild = leftChild;
		this.rightChild = rightChild;
	}
}
