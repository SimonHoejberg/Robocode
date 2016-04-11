package nodes;
import java.util.List;


public class ForNode extends IterationNode {
	
	public Object assign;
	public Object predicate;
	public Object update;
	public ForNode(int lineNumber, int colNumber, Object assing, Object predicate, Object update, List<StatementNode> statements) {
		super(lineNumber, colNumber);
		this.assign = assing;
		this.predicate = predicate;
		this.update = update;
		this.statements = statements;
	}
}
