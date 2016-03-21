public class BaseIdentNode extends AbstractNode {
	String ident;
    ExpressionNode index; // expression node for array index - if any
    
    public BaseIdentNode() {
    	
    }
    
    public BaseIdentNode(String ident) {
		this.ident = ident;
	}
    
    public BaseIdentNode(String ident, ExpressionNode index) {
		this.ident = ident;
		this.index = index;
	}
}