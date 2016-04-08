package nodes;
import exceptions.NotImplementedException;

public class UnaryExprNode extends ExpressionNode {
	
	public enum UnaryType {
		negation, not;
		
		public String toString() {
			switch(this) {
				case negation:
					return "-";
				case not:
					return "!";
				default:
					throw new NotImplementedException();
			}
		}
	}
	
	private UnaryType type;
	private ExpressionNode child;
	
	public UnaryExprNode(int lineNumber, int colNumber, UnaryType type, ExpressionNode child) {
		super(lineNumber, colNumber);
		this.type = type;
		this.child = child;
	}
	
	public UnaryType getType() {
		return type;
	}
	
	public ExpressionNode getChild() {
		return child;
	}
}
