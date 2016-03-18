
public class AssignmentNode extends StatementNode {

    public enum AssignmentType {

        basic, add, sub, mult, div, mod

    }


    String generalIdent;

    AssignmentType type;

    ExpressionNode expr;

}
