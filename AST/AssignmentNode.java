public class AssignmentNode extends StatementNode {

    public enum AssignmentType {

        basic, add, sub, mult, div, mod

    }


    GeneralIdentNode generalIdent;
    AssignmentType type;
    ExpressionNode expr;
    
    public AssignmentNode(GeneralIdentNode generalIdent, AssignmentType type, ExpressionNode expr) {
    	this.generalIdent = generalIdent;
    	this.type = type;
    	this.expr = expr;
    }

}
