class ReturnNode extends StatementNode {

	ExpressionNode expr;
	
	public ReturnNode() {
		
	}
	
    public ReturnNode(ExpressionNode expr) {
		this.expr = expr;
	}

}
