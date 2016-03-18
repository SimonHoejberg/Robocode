import java.util.List;


public class FuncDeclarationNode extends DeclarationNode {

    List<String> returnTypes;

    String ident;

    List<VarNode> params;
  // Maybe use ParamNode instead?

    List<StatementNode> statements;

}
