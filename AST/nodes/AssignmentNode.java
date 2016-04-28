package nodes;
import java.util.List;

import exceptions.NotImplementedException;

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
    	
    	public String toJavaSyntax() {
    		switch (this) {
			case basic:
				return " = ";
			case add:
				return " += ";
			case sub:
				return " -= ";
			case mult:
				return " *= ";
			case div:
				return " /= ";
			case mod:
				return " %= ";
			default:
				throw new NotImplementedException();
		}
    	}

    }


    private List<AbstractNode> vars;
    private AssignmentType type;
    private ExpressionNode expr;
    
    public AssignmentNode(int lineNumber, int colNumber, List<AbstractNode> output, AssignmentType type, ExpressionNode expr) {
    	super(lineNumber, colNumber);
    	this.vars = output;
    	this.type = type;
    	this.expr = expr;
    }
    
    public List<AbstractNode> getVariables() {
    	return vars;
    }
    
    public AssignmentType getType() {
    	return type;
    }
    
    public ExpressionNode getExpression() {
    	return expr;
    }
}
