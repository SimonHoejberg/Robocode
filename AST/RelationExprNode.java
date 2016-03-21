public class RelationExprNode extends ExpressionNode {
	public enum RelationType {
        lessThan, greaterThan, lessThanOrEqual, greaterThanOrEqual
    }

    RelationType type;
    ExpressionNode leftChild;    // RelationExpr
    ExpressionNode rightChild;    // AdditiveExpr
    
    public RelationExprNode(RelationType type, ExpressionNode leftChild, ExpressionNode rightChild) {
		this.type = type;
		this.leftChild = leftChild;
		this.rightChild = rightChild;
	}
}