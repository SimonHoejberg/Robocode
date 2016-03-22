class ReturnNode extends StatementNode {

	private ExpressionNode expr;
	
	public ReturnNode() {
		
	}
	
    public ReturnNode(ExpressionNode expr) {
		this.expr = expr;
	}
    
    public ExpressionNode getExpression() {
    	return expr;
    }

}
