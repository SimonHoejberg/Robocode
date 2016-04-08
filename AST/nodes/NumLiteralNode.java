package nodes;
public class NumLiteralNode extends PrimaryExprNode {
	private double value;
	
	public NumLiteralNode(int lineNumber, int colNumber, double value) {
		super(lineNumber, colNumber);
		this.value = value;
	}
	
	public double getValue() {
		return value;
	}
}