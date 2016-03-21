public class RelationExprNode extends ExpressionNode {
    public enum RelationType {
        lessThan, greaterThan, lessThanOrEqual, greaterThanOrEqual
    }

    RelationType type;
    ExpressionNode leftChild;    // RelationExpr
    ExpressionNode rightChild;    // AdditiveExpr
}