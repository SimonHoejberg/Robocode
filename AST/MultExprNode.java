public class MultExprNode extends ExpressionNode {
	public enum MultiplicationType {
        mult, div, mod;
		
		public String toString() {
			switch (this) {
				case mult:
					return " * ";
				case div:
					return " / ";
				case mod:
					return " % ";
				default:
					throw new NotImplementedException();
			}
		}
    }

    private MultiplicationType type;
    private ExpressionNode leftChild;    // UnaryExpr
    private ExpressionNode rightChild;    // MultExpr
    
    public MultExprNode(MultiplicationType type, ExpressionNode leftChild, ExpressionNode rightChild) {
		this.type = type;
		this.leftChild = leftChild;
		this.rightChild = rightChild;
	}
    
    public MultiplicationType getType() {
    	return type;
    }
    
    public ExpressionNode getLeftChild() {
		return leftChild;
	}
	
	public ExpressionNode getRightChild() {
		return rightChild;
	}
}