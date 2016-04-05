public class BoolLiteralNode extends PrimaryExprNode {
	private Boolean bool;
	
	public BoolLiteralNode(boolean bool, int lineNumber, int colNumber) {
		super(lineNumber, colNumber);
		this.bool = bool;
	}
	
	public Boolean getBool() {
		return bool;
	}
}