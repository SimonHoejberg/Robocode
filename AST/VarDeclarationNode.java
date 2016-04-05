public class VarDeclarationNode extends DeclarationNode {

    private VarNode variable;
    private ExpressionNode expr;	// The expression that the variable is assigned
    
    public VarDeclarationNode(int lineNum, int colNum, VarNode variable, ExpressionNode expr) {
    	this.lineNumber = lineNum;
    	this.colNumber = colNum;
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
