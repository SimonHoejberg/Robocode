import java.util.List;
import nodes.VarNode;

public class Method {
	private Object type;
	private String ident;
	private List<VarNode> params;
	private String description;
	
	public Method (Object type, String ident, List<VarNode> params, String description) {
		this.type = type;
		this.ident = ident;
		this.params = params;
		this.description = description;
	}
	
	public Object getType() {
		return type;
	}
	
	public String getIdent() {
		return ident;
	}
	
	public List<VarNode> getParameters() {
		return params;
	}
	
	public String getDescription() {
		return description;
	}
}
