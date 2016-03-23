public class EqualityExprNode extends ExpressionNode {
	public enum EqualityType {
        equal, notEqual;
        
        public String toString() {
        	switch (this) {
        		case equal:
        			return " = ";
        		case notEqual:
        			return " != ";
        		default:
        			throw new NotImplementedException();
        	}
        }
   }

   private EqualityType type;
   private ExpressionNode leftChild;    // RelationExpr
   private ExpressionNode rightChild;    // EqualityExpr
   
   public EqualityExprNode(EqualityType type, ExpressionNode leftChild, ExpressionNode rightChild) {
		this.type = type;
		this.leftChild = leftChild;
		this.rightChild = rightChild;
	}
   
   public EqualityType getType() {
	   return type;
   }
   
   public ExpressionNode getLeftChild() {
	   return leftChild;
   }
   
   public ExpressionNode getRightChild() {
	   return rightChild;
   }
}
