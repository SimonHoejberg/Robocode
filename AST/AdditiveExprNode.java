public class AdditiveExprNode extends ExpressionNode {
    public enum AdditionType {
        add, sub
    }

    AdditionType type;
    ExpressionNode leftChild;    // MultExpr
    ExpressionNode rightChild;    // AdditiveExpr
}