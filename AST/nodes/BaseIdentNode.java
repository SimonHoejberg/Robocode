package nodes;
public class BaseIdentNode extends AbstractNode {
	protected String ident;
    protected ExpressionNode index; // expression node for array index - if any
    
    public BaseIdentNode(int lineNumber, int colNumber) {
    	super(lineNumber, colNumber);
    }
    
    public BaseIdentNode(int lineNumber, int colNumber, String ident) {
    	super(lineNumber, colNumber);
    	this.ident = ident;
	}
    
    public BaseIdentNode(int lineNumber, int colNumber, String ident, ExpressionNode index) {
    	super(lineNumber, colNumber);
    	this.ident = ident;
		this.index = index;
	}
    
    public String getIdent() {
    	return ident;
    }
    
    public ExpressionNode getIndex() {
    	return index;
    }
}