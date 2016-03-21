import java.util.*;

class IterationNode extends StatementNode {

	public enum IterationType{

        While, ForWithAssignment, ForWithDcl, For

    }

    IterationType type;

    List<ExpressionNode> expressions;

    AssignmentNode assignment;

    VarDeclarationNode varDcl;

    List<StatementNode> statements; 	// Block
    
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
    
    

}
