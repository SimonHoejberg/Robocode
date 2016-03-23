import java.util.*;

class IterationNode extends StatementNode {

	public enum IterationType{

        While, ForWithAssignment, ForWithDcl, For

    }

    private IterationType type;

    private List<ExpressionNode> expressions;

    private AssignmentNode assignment;

    private VarDeclarationNode varDcl;

    private List<StatementNode> statements; 	// Block
    
    public IterationNode(IterationType type, List<ExpressionNode> expressions, List<StatementNode> statements) {
		this.type = type;
		
		this.expressions = expressions;
		this.statements = statements;
	}

	public IterationNode(List<ExpressionNode> expressions, List<StatementNode> statements, AssignmentNode assignment) {
		type = IterationType.ForWithAssignment;
		
		this.expressions = expressions;
		this.statements = statements;
		this.assignment = assignment;
	}

	public IterationNode(List<ExpressionNode> expressions, List<StatementNode> statements, VarDeclarationNode varDcl) {
		type = IterationType.ForWithDcl;
		
		this.expressions = expressions;
		this.statements = statements;
		this.varDcl = varDcl;
	}
    
	public IterationType getType() {
		return type;
	}
	
	public List<ExpressionNode> getExpressions() {
		return expressions;
	}
	
	public AssignmentNode getAssignment() {
		return assignment;
	}
	
	public VarDeclarationNode getVarDeclaration() {
		return varDcl;
	}
    
	public List<StatementNode> getStatements() {
		return statements;
	}

}
