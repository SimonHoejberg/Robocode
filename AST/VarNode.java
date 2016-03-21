public class VarNode extends AbstractNode {
	String type;

    String ident;

    public VarNode(String type, String ident) {
		this.type = type;
		this.ident = ident;
	}
}
