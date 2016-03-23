public class ArrayDeclarationNode extends DeclarationNode {

    private String type;			// String to accommodate user-defined structs
    
    private String ident;
    private ExpressionNode size;
    
    public ArrayDeclarationNode(String type, String ident, ExpressionNode size) {
    	this.type = type;
    	this.ident = ident;
    	this.size = size;
    }
    
    public String getType() {
    	return type;
    }
    
    public String getIdent() {
    	return ident;
    }
    
    public ExpressionNode getSize() {
    	return size;
    }
}