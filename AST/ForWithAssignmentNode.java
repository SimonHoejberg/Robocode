import java.util.List;


public class ForWithAssignmentNode extends IterationNode {
	
    private AssignmentNode assignment;
	
	public ForWithAssignmentNode(List<ExpressionNode> expressions, List<StatementNode> statements, AssignmentNode assignment) {
		this.expressions = expressions;
		this.statements = statements;
		this.assignment = assignment;
	}
	
	public AssignmentNode getAssignment() {
		return assignment;
	}
}
