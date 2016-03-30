import java.util.List;


public class WhileNode extends IterationNode {
	
	public WhileNode(List<ExpressionNode> expressions, List<StatementNode> statements) {
		this.expressions = expressions;
		this.statements = statements;
	}
}
