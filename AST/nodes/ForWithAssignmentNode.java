package nodes;
import java.util.List;


public class ForWithAssignmentNode extends IterationNode {
	
    private AssignmentNode assignment;
	
	public ForWithAssignmentNode(int lineNumber, int colNumber, List<ExpressionNode> expressions, List<StatementNode> statements, AssignmentNode assignment) {
		super(lineNumber, colNumber);
		this.expressions = expressions;
		this.statements = statements;
		this.assignment = assignment;
	}
	
	public AssignmentNode getAssignment() {
		return assignment;
	}
}
