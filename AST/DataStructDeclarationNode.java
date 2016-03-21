public class DataStructDeclarationNode extends DeclarationNode {

    String type;			// String to accommodate user-defined structs
    
    String ident;
    
    public DataStructDeclarationNode(String type, String ident) {
    	this.type = type;
    	this.ident = ident;
    }
}