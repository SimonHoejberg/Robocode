public abstract class AbstractNode {

}

public class StatementNode extends AbstractNode {

}

public abstract class DeclarationNode extends StatementNode {

}

public class RobotDeclarationNode extends DeclarationNode {
  public enum RobotDeclarationType{
    name, initialization, behavior
  }
  RobotDeclarationType type;
  String name;
  List<StatementNode> statements;
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
  String type;		// String to accommodate user-defined structs
  String ident;
  int size;			// Size for arrays
}

public class StructDefinitionNode extends DeclarationNode {
	String typeName;
	List<VarDeclarationNode> declarations; // varDcl, dataStructDcl, arrayDcl
}

public class VarNode extends AbstractNode {
  //String type;
  String ident;
}

public class AssignmentNode extends StatementNode {
	public enum AssignmentType{
	    basic, add, sub, mult, div, mod
	}
	
	String generalIdent;
	AssignmentType type;
	ExpressionNode expr;
}

public class ExpressionNode extends AbstractNode {
	
	ExpressionNode expr;
}

public class IfNode extends StatementNode {
	public enum IfType{
	    If, IfElse, ElseIf
	}
	List<StatementNode> ifBlockStatements;
	IfType type;
	List<StatementNode> elseBlockStatements;
	IfNode next;
}

public class IterationNode extends StatementNode {
	public enum IterationType{
	    While, ForWithAssignment, ForWithDcl, For
	}
	IterationType type;
	List<ExpressionNode> expressions;
	AssignmentNode assignment;
	VarDeclarationNode varDcl;
	List<StatementNode> statements; 	// Block
}

public class ReturnNode extends StatementNode {
	ExpressionNode expr;
}

//public class ParamNode extends DeclarationNode {
//  String ident;
//  String type;
//}
