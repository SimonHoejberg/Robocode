import java.util.List;


public class IfNode extends StatementNode {

    public enum IfType{

        If, IfElse, ElseIf

    }
    List<StatementNode> ifBlockStatements;

    IfType type;

    List<StatementNode> elseBlockStatements;

    IfNode next;

}
