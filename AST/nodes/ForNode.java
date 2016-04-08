package nodes;
import java.util.List;


public class ForNode extends IterationNode {
	
	public ForNode(int lineNumber, int colNumber, List<ExpressionNode> expressions, List<StatementNode> statements) {
		super(lineNumber, colNumber);
		this.expressions = expressions;
		this.statements = statements;
	}
}
