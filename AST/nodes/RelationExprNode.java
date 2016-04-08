package nodes;
import exceptions.NotImplementedException;

public class RelationExprNode extends ExpressionNode {
	public enum RelationType {
        lessThan, greaterThan, lessThanOrEqual, greaterThanOrEqual;
        
        public String toString() {
        	switch (this) {
	        	case lessThan:
	        		return " < ";
	        	case greaterThan:
	        		return " > ";
	        	case lessThanOrEqual:
	        		return " <= ";
	        	case greaterThanOrEqual:
	        		return " >= ";
	        	default: 
	        		throw new NotImplementedException();	
        	}
        }
    }

    private RelationType type;
    private ExpressionNode leftChild;    // RelationExpr
    private ExpressionNode rightChild;    // AdditiveExpr
    
    public RelationExprNode(int lineNumber, int colNumber, RelationType type, ExpressionNode leftChild, ExpressionNode rightChild) {
    	super(lineNumber, colNumber);
    	this.type = type;
		this.leftChild = leftChild;
		this.rightChild = rightChild;
	}
    
    public RelationType getType() {
    	return type;
    }
    
    public ExpressionNode getLeftChild() {
		return leftChild;
	}
	
	public ExpressionNode getRightChild() {
		return rightChild;
	}
}