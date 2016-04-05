import java.util.List;

public class FuncCallNode extends BaseIdentNode  {
	private List<ExpressionNode> arguments;
	
	public FuncCallNode(int lineNumber, int colNumber, String ident, List<ExpressionNode> arguments) {
		super(lineNumber, colNumber);
		this.ident = ident;
		this.arguments = arguments;
	}
	
	public FuncCallNode(int lineNumber, int colNumber, String ident, List<ExpressionNode> arguments, ExpressionNode index) {
		super(lineNumber, colNumber);
		this.ident = ident;
		this.arguments = arguments;
		this.index = index;
	}
	
	public List<ExpressionNode> getArguments() {
		return arguments;
	}
}