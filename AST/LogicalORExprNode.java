public class LogicalORExprNode extends ExpressionNode {
   private ExpressionNode leftChild;    // LogicalANDExpr
   private ExpressionNode rightChild;    // LogicalORExpr
   
   public LogicalORExprNode(ExpressionNode leftChild, ExpressionNode rightChild) {
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