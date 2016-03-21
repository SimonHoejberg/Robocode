public class ArrayDeclarationNode extends DeclarationNode {

    String type;			// String to accommodate user-defined structs
    
    String ident;
    ExpressionNode size;
    
    public ArrayDeclarationNode(String type, String ident, ExpressionNode size) {
    	this.type = type;
    	this.ident = ident;
    	this.size = size;
    }
}