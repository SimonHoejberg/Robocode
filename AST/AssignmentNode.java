public class AssignmentNode extends StatementNode {

    public enum AssignmentType {

        basic, add, sub, mult, div, mod;
    	
    	public String toString() {
    		switch (this) {
    			case basic:
    				return " := ";
    			case add:
    				return " +:= ";
    			case sub:
    				return " -:= ";
    			case mult:
    				return " *:= ";
    			case div:
    				return " /:= ";
    			case mod:
    				return " %:= ";
    			default:
    				throw new NotImplementedException();
    		}
    	}

    }


    private GeneralIdentNode generalIdent;
    private AssignmentType type;
    private ExpressionNode expr;
    
    public AssignmentNode(int lineNumber, int colNumber, GeneralIdentNode generalIdent, AssignmentType type, ExpressionNode expr) {
    	super(lineNumber, colNumber);
    	this.generalIdent = generalIdent;
    	this.type = type;
    	this.expr = expr;
    }
    
    public GeneralIdentNode getGeneralIdent() {
    	return generalIdent;
    }
    
    public AssignmentType getType() {
    	return type;
    }
    
    public ExpressionNode getExpression() {
    	return expr;
    }
}
