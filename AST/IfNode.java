import java.util.List;


public class IfNode extends StatementNode {

    public enum IfType{

        If, IfElse, ElseIf

    }
    
    private IfType type;
    
    private ExpressionNode expr;
    
    private List<StatementNode> ifBlockStatements;

    private List<StatementNode> elseBlockStatements;

    private IfNode next;
    
 // If Constructor
 	public IfNode(ExpressionNode expr, List<StatementNode> ifBlockStatements) {
 		this.type = IfType.If;
 		this.expr = expr;
 		this.ifBlockStatements = ifBlockStatements;
 	}
    
    // If Else Constructor
    public IfNode(ExpressionNode expr, List<StatementNode> ifBlockStatements, List<StatementNode> elseBlockStatements) {
    	this.type = IfType.IfElse;
    	this.expr = expr;
    	this.ifBlockStatements = ifBlockStatements;
    	this.elseBlockStatements = elseBlockStatements;
    }

 // Else If Constructor
    public IfNode(ExpressionNode expr, List<StatementNode> ifBlockStatements, IfNode next) {
    	this.type = IfType.ElseIf;
    	this.expr = expr;
    	this.ifBlockStatements = ifBlockStatements;
    	this.next = next;
    }
    
    public IfType getType() {
    	return type;
    }
    
    public ExpressionNode getExpression() {
    	return expr;
    }
    
    public List<StatementNode> getIfBlockStatements() {
    	return ifBlockStatements;
    }
    
    public List<StatementNode> getElseBlockStatements() {
    	return elseBlockStatements;
    }
    
    public IfNode getNext() {
    	return next;
    }

}
