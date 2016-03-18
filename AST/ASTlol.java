public abstract class AbstractNode {



}



public class StatementNode extends AbstractNode {



}



public abstract class DeclarationNode extends StatementNode {



}



public class RobotDeclarationNode extends DeclarationNode {

    public enum RobotDeclarationType { 

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
  // Maybe use ParamNode instead?

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

    String type;

    String ident;

}



public class AssignmentNode extends StatementNode {

    public enum AssignmentType {

        basic, add, sub, mult, div, mod

    }


    String generalIdent;

    AssignmentType type;

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


public class CallStatementNode extends StatementNode {
    GeneralIdentNode ident;
}


public class ExpressionNode extends AbstractNode {
    
    
}

public class LogicalORExprNode extends ExpressionNode {
   ExpressionNode leftChild;    // LogicalANDExpr
   ExpressionNode rightChild;    // LogicalORExpr
}

public class LogicalANDExprNode extends ExpressionNode {
   ExpressionNode leftChild;    // EqualityExpr
   ExpressionNode rightChild;    // LogicalANDExpr
}

public class EqualityExprNode extends ExpressionNode {
   public enum EqualityType {

        equal, notEqual
   }

   EqualityType type;
   ExpressionNode leftChild;    // RelationExpr
   ExpressionNode rightChild;    // EqualityExpr
}

public class RelationExprNode extends ExpressionNode {
    public enum RelationType {

        lessThan, greaterThan, lessThanOrEqual, greaterThanOrEqual
    }

    RelationType type;
    ExpressionNode leftChild;    // RelationExpr
    ExpressionNode rightChild;    // AdditiveExpr
}

public class AdditiveExprNode extends ExpressionNode {
    public enum AdditionType {

        add, sub
    }

    AdditionType type;
    ExpressionNode leftChild;    // MultExpr
    ExpressionNode rightChild;    // AdditiveExpr
}


public class MultExprNode extends ExpressionNode {
    public enum MultiplicationType {

        mult, div, mod
    }

    MultiplicationType type;
    ExpressionNode leftChild;    // UnaryExpr
    ExpressionNode rightChild;    // MultExpr
}


public class UnaryExprNode extends ExpressionNode {
    ExpressionNode child;    // UnaryExpr
}

public class PrimaryExprNode extends ExpressionNode {
    
}

public class GeneralIdentNode extends PrimaryExprNode {
    List<BaseIdentNode> idents;
}

public class TextLiteralNode extends PrimaryExprNode {
    String text;
}

public class NumLiteralNode extends PrimaryExprNode {
    double value;
}

public class BoolLiteralNode extends PrimaryExprNode {
    Boolean bool;
}

public class ParanthesisNode extends PrimaryExprNode {
    ExpressionNode child;

}

public class BaseIdentNode extends AbstractNode {
    ExpressionNode index; // expression node for array index - if any
    String ident;
}

public class FuncCallNode extends BaseIdentNode  {
    List<ExpressionNode> arguments;
}



//public class ParamNode extends DeclarationNode {

//    String ident;

//    String type;

//}