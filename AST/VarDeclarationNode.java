public class VarDeclarationNode extends DeclarationNode {

    String type;
    String ident;
    ExpressionNode expr;	// The expression that the variable is assigned
    
    public VarDeclarationNode(String type, String ident, ExpressionNode expr) {
    	this.type = type;
    	this.ident = ident;
    	this.expr = expr;
    }
}
