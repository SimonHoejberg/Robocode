public abstract class AbstractNode {

}

public abstract class DeclarationNode extends AbstractNode {

}

public class RobotDeclarationNode extends DeclarationNode {
  public enum RobotDeclarationType{
    name, initialization, behavior
  }
  RobotDeclarationType type;
  String name;
  List<StatementNode> statements;
}

public class StatementNode extends AbstractNode {

}

public class EventDeclarationNode extends DeclarationNode {
  String ident;
  VarNode param; // Maybe use ParamNode instead?
  List<StatementNode> statements;
}

public class FuncDeclarationNode extends DeclarationNode {
  List<String> returnTypes;
  String ident;
  List<VarNode> params;
  List<StatementNode> statements;
}

public class VarDeclarationNode extends DeclarationNode {
  String type;
  String ident;
}

public class VarNode extends AbstractNode {
  //String type;
  String ident;
}

//public class ParamNode extends DeclarationNode {
//  String ident;
//  String type;
//}
