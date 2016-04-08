package nodes;
public class LogicalORExprNode extends ExpressionNode {
   private ExpressionNode leftChild;    // LogicalANDExpr
   private ExpressionNode rightChild;    // LogicalORExpr
   
   public LogicalORExprNode(int lineNumber, int colNumber, ExpressionNode leftChild, ExpressionNode rightChild) {
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