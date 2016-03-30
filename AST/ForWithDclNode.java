import java.util.List;


public class ForWithDclNode extends IterationNode {

    private VarDeclarationNode varDcl;
	
	public ForWithDclNode(List<ExpressionNode> expressions, List<StatementNode> statements, VarDeclarationNode varDcl) {
		this.expressions = expressions;
		this.statements = statements;
		this.varDcl = varDcl;
	}
	
	public VarDeclarationNode getVarDeclaration() {
		return varDcl;
	}
}
