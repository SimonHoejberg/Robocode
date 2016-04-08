package nodes;
public class BoolLiteralNode extends PrimaryExprNode {
	private Boolean bool;
	
	public BoolLiteralNode(int lineNumber, int colNumber, boolean bool) {
		super(lineNumber, colNumber);
		this.bool = bool;
	}
	
	public Boolean getBool() {
		return bool;
	}
}