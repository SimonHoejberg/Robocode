public class EqualityExprNode extends ExpressionNode {
   public enum EqualityType {
        equal, notEqual
   }

   EqualityType type;
   ExpressionNode leftChild;    // RelationExpr
   ExpressionNode rightChild;    // EqualityExpr
}
