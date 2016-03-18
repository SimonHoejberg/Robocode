import java.util.List;


public class EventDeclarationNode extends DeclarationNode {

    String ident;

    VarNode param; // Maybe use ParamNode instead?

    List<StatementNode> statements;

}
