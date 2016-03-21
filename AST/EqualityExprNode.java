public class EqualityExprNode extends ExpressionNode {
	public enum EqualityType {
        equal, notEqual
   }

   EqualityType type;
   ExpressionNode leftChild;    // RelationExpr
   ExpressionNode rightChild;    // EqualityExpr
   
   public EqualityExprNode(EqualityType type, ExpressionNode leftChild, ExpressionNode rightChild) {
		this.type = type;
		this.leftChild = leftChild;
		this.rightChild = rightChild;
	}
}
