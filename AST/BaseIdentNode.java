public class BaseIdentNode extends AbstractNode {
	protected String ident;
    protected ExpressionNode index; // expression node for array index - if any
    
    public BaseIdentNode(int lineNumber, int colNumber) {
    	super(lineNumber, colNumber);
    }
    
    public BaseIdentNode(String ident, int lineNumber, int colNumber) {
    	super(lineNumber, colNumber);
    	this.ident = ident;
	}
    
    public BaseIdentNode(String ident, ExpressionNode index, int lineNumber, int colNumber) {
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