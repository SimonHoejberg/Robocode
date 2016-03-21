public class MultExprNode extends ExpressionNode {
    public enum MultiplicationType {
        mult, div, mod
    }

    MultiplicationType type;
    ExpressionNode leftChild;    // UnaryExpr
    ExpressionNode rightChild;    // MultExpr
}