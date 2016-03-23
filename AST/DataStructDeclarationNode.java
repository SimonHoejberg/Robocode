public class DataStructDeclarationNode extends DeclarationNode {

    private String type;			// String to accommodate user-defined structs
    
    private String ident;
    
    public DataStructDeclarationNode(String type, String ident) {
    	this.type = type;
    	this.ident = ident;
    }
    
    public String getType() {
    	return type;
    }
    
    public String getIdent() {
    	return ident;
    }
}