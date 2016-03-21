import java.util.List;

public class FuncCallNode extends BaseIdentNode  {
	List<VarNode> arguments;
	
	public FuncCallNode(String ident, List<VarNode> arguments) {
		this.ident = ident;
		this.arguments = arguments;
	}
	
	public FuncCallNode(String ident, List<VarNode> arguments, ExpressionNode index) {
		this.ident = ident;
		this.arguments = arguments;
		this.index = index;
	}
}