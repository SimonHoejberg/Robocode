public class VarNode extends AbstractNode {
	private String type;

    private String ident;

    public VarNode(String type, String ident) {
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
