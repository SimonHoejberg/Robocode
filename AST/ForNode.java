import java.util.List;


public class ForNode extends IterationNode {
	
	public ForNode(List<ExpressionNode> expressions, List<StatementNode> statements) {
		this.expressions = expressions;
		this.statements = statements;
	}
}
