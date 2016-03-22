public class NumLiteralNode extends PrimaryExprNode {
	private double value;
	
	public NumLiteralNode(double value) {
		this.value = value;
	}
	
	public double getValue() {
		return value;
	}
}