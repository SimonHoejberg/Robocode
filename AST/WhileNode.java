import java.util.List;


public class WhileNode extends IterationNode {
	
	public WhileNode(int lineNumber, int colNumber, List<ExpressionNode> expressions, List<StatementNode> statements) {
		super(lineNumber, colNumber);
		this.expressions = expressions;
		this.statements = statements;
	}
}
