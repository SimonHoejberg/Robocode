import java.util.List;

import AdditiveExprNode.AdditionType;
import AssignmentNode.AssignmentType;
import EqualityExprNode.EqualityType;
import IfNode.IfType;
import IterationNode.IterationType;
import MultExprNode.MultiplicationType;
import RelationExprNode.RelationType;
import RobotDeclarationNode.RobotDeclarationType;


abstract class AbstractNode {



}



class StatementNode extends AbstractNode {



}



abstract class DeclarationNode extends StatementNode {



}



class RobotDeclarationNode extends DeclarationNode {

    public enum RobotDeclarationType { 

        name, initialization, behavior

    }

    RobotDeclarationType type;

    String name;

    List<StatementNode> statements;

}



class EventDeclarationNode extends DeclarationNode {

    String ident;

    VarNode param; // Maybe use ParamNode instead?

    List<StatementNode> statements;

}



class FuncDeclarationNode extends DeclarationNode {

    List<String> returnTypes;

    String ident;

    List<VarNode> params;
  // Maybe use ParamNode instead?

    List<StatementNode> statements;

}



class VarDeclarationNode extends DeclarationNode {

    String type;		// String to accommodate user-defined structs

    String ident;
    int size;			// Size for arrays

}



class StructDefinitionNode extends DeclarationNode {

    String typeName;
    List<VarDeclarationNode> declarations; // varDcl, dataStructDcl, arrayDcl

}



class VarNode extends AbstractNode {

    String type;

    String ident;

}



class AssignmentNode extends StatementNode {

    public enum AssignmentType {

        basic, add, sub, mult, div, mod

    }


    String generalIdent;

    AssignmentType type;

    ExpressionNode expr;

}



class IfNode extends StatementNode {

    public enum IfType{

        If, IfElse, ElseIf

    }
    List<StatementNode> ifBlockStatements;

    IfType type;

    List<StatementNode> elseBlockStatements;

    IfNode next;

}



class IterationNode extends StatementNode {

    public enum IterationType{

        While, ForWithAssignment, ForWithDcl, For

    }

    IterationType type;

    List<ExpressionNode> expressions;

    AssignmentNode assignment;

    VarDeclarationNode varDcl;

    List<StatementNode> statements; 	// Block

}



class ReturnNode extends StatementNode {

    ExpressionNode expr;

}


class CallStatementNode extends StatementNode {
    GeneralIdentNode ident;
}


class ExpressionNode extends AbstractNode {
    
    
}


class LogicalORExprNode extends ExpressionNode {
   ExpressionNode leftChild;    // LogicalANDExpr
   ExpressionNode rightChild;    // LogicalORExpr
}

class LogicalANDExprNode extends ExpressionNode {
   ExpressionNode leftChild;    // EqualityExpr
   ExpressionNode rightChild;    // LogicalANDExpr
}

class EqualityExprNode extends ExpressionNode {
   public enum EqualityType {

        equal, notEqual
   }

   EqualityType type;
   ExpressionNode leftChild;    // RelationExpr
   ExpressionNode rightChild;    // EqualityExpr
}

class RelationExprNode extends ExpressionNode {
    public enum RelationType {

        lessThan, greaterThan, lessThanOrEqual, greaterThanOrEqual
    }

    RelationType type;
    ExpressionNode leftChild;    // RelationExpr
    ExpressionNode rightChild;    // AdditiveExpr
}

class AdditiveExprNode extends ExpressionNode {
    public enum AdditionType {

        add, sub
    }

    AdditionType type;
    ExpressionNode leftChild;    // MultExpr
    ExpressionNode rightChild;    // AdditiveExpr
}



class MultExprNode extends ExpressionNode {
    public enum MultiplicationType {

        mult, div, mod
    }

    MultiplicationType type;
    ExpressionNode leftChild;    // UnaryExpr
    ExpressionNode rightChild;    // MultExpr
}


class UnaryExprNode extends ExpressionNode {
    ExpressionNode child;    // UnaryExpr
}

class PrimaryExprNode extends ExpressionNode {
    
}

class GeneralIdentNode extends PrimaryExprNode {
    List<BaseIdentNode> idents;
}

class TextLiteralNode extends PrimaryExprNode {
    String text;
}

class NumLiteralNode extends PrimaryExprNode {
    double value;
}

class BoolLiteralNode extends PrimaryExprNode {
    Boolean bool;
}

class ParanthesisNode extends PrimaryExprNode {
    ExpressionNode child;

}

class BaseIdentNode extends AbstractNode {
    ExpressionNode index; // expression node for array index - if any
    String ident;
}

class FuncCallNode extends BaseIdentNode  {
    List<ExpressionNode> arguments;
}




//public class ParamNode extends DeclarationNode {

//    String ident;

//    String type;

//}