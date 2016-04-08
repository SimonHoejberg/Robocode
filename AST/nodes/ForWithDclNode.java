package nodes;
import java.util.List;


public class ForWithDclNode extends IterationNode {

    private VarDeclarationNode varDcl;
	
	public ForWithDclNode(int lineNumber, int colNumber, List<ExpressionNode> expressions, List<StatementNode> statements, VarDeclarationNode varDcl) {
		super(lineNumber, colNumber);
		this.expressions = expressions;
		this.statements = statements;
		this.varDcl = varDcl;
	}
	
	public VarDeclarationNode getVarDeclaration() {
		return varDcl;
	}
}
