public class BoolLiteralNode extends PrimaryExprNode {
	private Boolean bool;
	
	public BoolLiteralNode(boolean bool) {
		this.bool = bool;
	}
	
	public Boolean getBool() {
		return bool;
	}
}