public class DataStructDeclarationNode extends DeclarationNode {

    private String type;			// String to accommodate user-defined structs
    
    private String ident;
    
    public DataStructDeclarationNode(int lineNumber, int colNumber, String type, String ident) {
    	super(lineNumber, colNumber);
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