package nodes;
public class VarNode extends AbstractNode {
	private String type;

    private String ident;

    public VarNode(int lineNumber, int colNumber, String type, String ident) {
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
    
    public boolean isArray() {
    	return type.endsWith("[]");
    }
}
