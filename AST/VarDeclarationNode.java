public class VarDeclarationNode extends DeclarationNode {

    private VarNode variable;
    private ExpressionNode expr;	// The expression that the variable is assigned
    
    public VarDeclarationNode(VarNode variable, ExpressionNode expr) {
    	this.variable = variable;
    	this.expr = expr;
    }
    
    public VarNode getVariable() {
    	return variable;
    }
    
    public ExpressionNode getExpression() {
    	return expr;
    }
}
