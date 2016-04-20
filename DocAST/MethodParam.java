
public class MethodParam {
	private Object type;

    private String ident;

    public MethodParam(Object type, String ident) {
    	this.type = type;
		this.ident = ident;
	}
    
    public Object getType() {
    	return type;
    }
    
    public String getIdent() {
    	return ident;
    }
}
