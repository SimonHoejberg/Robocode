public class AdditiveExprNode extends ExpressionNode {
	public enum AdditionType {
        add, sub;
        
		@Override
        public String toString() {
        	switch (this) {
        		case add:
        			return " + ";
        		case sub:
        			return " - ";
        		default:
        			throw new NotImplementedException();
        	}
        }
    }

    private AdditionType type;
    private ExpressionNode leftChild;    // MultExpr
    private ExpressionNode rightChild;    // AdditiveExpr
    
    public AdditiveExprNode(int lineNumber, int colNumber, AdditionType type, ExpressionNode leftChild, ExpressionNode rightChild) {
    	super(lineNumber, colNumber);
    	this.type = type;
		this.leftChild = leftChild;
		this.rightChild = rightChild;
	}
    
    public AdditionType getType() {
    	return type;
    }
    
    public ExpressionNode getLeftChild() {
    	return leftChild;
    }
    
    public ExpressionNode getRightChild() {
    	return rightChild;
    }
}