import java.util.List;

public class FuncCallNode extends BaseIdentNode  {
	List<ExpressionNode> arguments;
	
	public FuncCallNode(String ident, List<ExpressionNode> arguments) {
		this.ident = ident;
		this.arguments = arguments;
	}
	
	public FuncCallNode(String ident, List<ExpressionNode> arguments, ExpressionNode index) {
		this.ident = ident;
		this.arguments = arguments;
		this.index = index;
	}
}