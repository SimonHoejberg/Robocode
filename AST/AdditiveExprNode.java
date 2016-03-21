public class AdditiveExprNode extends ExpressionNode {
	public enum AdditionType {
        add, sub
    }

    AdditionType type;
    ExpressionNode leftChild;    // MultExpr
    ExpressionNode rightChild;    // AdditiveExpr
    
    public AdditiveExprNode(AdditionType type, ExpressionNode leftChild, ExpressionNode rightChild) {
		this.type = type;
		this.leftChild = leftChild;
		this.rightChild = rightChild;
	}
}