public class LogicalORExprNode extends ExpressionNode {
   ExpressionNode leftChild;    // LogicalANDExpr
   ExpressionNode rightChild;    // LogicalORExpr
   
   public LogicalORExprNode(ExpressionNode leftChild, ExpressionNode rightChild) {
	   this.leftChild = leftChild;
	   this.rightChild = rightChild;
   }
}