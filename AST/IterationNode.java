import java.util.*;

public abstract class IterationNode extends StatementNode {

    protected List<ExpressionNode> expressions;

    protected List<StatementNode> statements; 	// Block
	
	public List<ExpressionNode> getExpressions() {
		return expressions;
	}
    
	public List<StatementNode> getStatements() {
		return statements;
	}

}
