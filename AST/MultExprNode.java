public class MultExprNode extends ExpressionNode {
	public enum MultiplicationType {
        mult, div, mod
    }

    MultiplicationType type;
    ExpressionNode leftChild;    // UnaryExpr
    ExpressionNode rightChild;    // MultExpr
    
    public MultExprNode(MultiplicationType type, ExpressionNode leftChild, ExpressionNode rightChild) {
		this.type = type;
		this.leftChild = leftChild;
		this.rightChild = rightChild;
	}
}