package nodes;

public class TypeNode extends AbstractNode {
	private String type;

	public TypeNode(int lineNumber, int colNumber, String type) {
		super(lineNumber, colNumber);
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
}
