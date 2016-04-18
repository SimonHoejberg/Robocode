package nodes;

import java.util.List;

public class VarDeclarationNode extends DeclarationNode {

    private List<VarNode> variable;
    private ExpressionNode expr;	// The expression that the variable is assigned
    
    public VarDeclarationNode(int lineNumber, int colNumber, List<VarNode> variable2, ExpressionNode expr) {
    	super(lineNumber, colNumber);
    	this.variable = variable2;
    	this.expr = expr;
    }
    
    public List<VarNode> getVariable() {
    	return variable;
    }
    
    public ExpressionNode getExpression() {
    	return expr;
    }
}
