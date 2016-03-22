
public abstract class ASTVisitor<T> {
	public abstract T visit(AdditiveExprNode node);
	public abstract T visit(ArrayDeclarationNode node);
	public abstract T visit(AssignmentNode node);
	public abstract T visit(BaseIdentNode node);
	public abstract T visit(BoolLiteralNode node);
	public abstract T visit(CallStatementNode node);
	public abstract T visit(DataStructDeclarationNode node);
	public abstract T visit(DataStructDefinitionNode node);
	public abstract T visit(DeclarationNode node);
	public abstract T visit(EqualityExprNode node);
	public abstract T visit(EventDeclarationNode node);
	public abstract T visit(ExpressionNode node);
	public abstract T visit(FuncCallNode node);
	public abstract T visit(FuncDeclarationNode node);
	public abstract T visit(GeneralIdentNode node);
	public abstract T visit(IfNode node);
	public abstract T visit(IterationNode node);
	public abstract T visit(LogicalANDExprNode node);
	public abstract T visit(LogicalORExprNode node);
	public abstract T visit(MultExprNode node);
	public abstract T visit(NumLiteralNode node);
	public abstract T visit(ParenthesesNode node);
	public abstract T visit(PrimaryExprNode node);
	public abstract T visit(ProgramNode node);
	public abstract T visit(RelationExprNode node);
	public abstract T visit(ReturnNode node);
	public abstract T visit(RobotDeclarationNode node);
	public abstract T visit(StatementNode node);
	public abstract T visit(TextLiteralNode node);
	public abstract T visit(TypeNode node);
	public abstract T visit(UnaryExprNode node);
	public abstract T visit(VarDeclarationNode node);
	public abstract T visit(VarNode node);
	
	public T visit(AbstractNode node) throws InstanceNotFoundException {
		if (node instanceof AdditiveExprNode){
			return visit((AdditiveExprNode)node);
		}
		if (node instanceof ArrayDeclarationNode){
			return visit ((ArrayDeclarationNode) node);
		}
		if (node instanceof AssignmentNode){
			return visit ((AssignmentNode) node);
		}
		if (node instanceof BaseIdentNode){
			return visit ((BaseIdentNode) node);
		}
		if (node instanceof BoolLiteralNode){
			return visit ((BoolLiteralNode) node);
		}
		if (node instanceof CallStatementNode){
			return visit ((CallStatementNode) node);
		}
		if (node instanceof DataStructDeclarationNode){
			return visit ((DataStructDeclarationNode) node);
		}
		if (node instanceof DataStructDefinitionNode){
			return visit ((DataStructDefinitionNode) node);
		}
		if (node instanceof DeclarationNode){
			return visit ((DeclarationNode) node);
		}
		if (node instanceof EqualityExprNode){
			return visit ((EqualityExprNode) node);
		}
		if (node instanceof EventDeclarationNode){
			return visit ((EventDeclarationNode) node);
		}
		if (node instanceof ExpressionNode){
			return visit ((ExpressionNode) node);
		}
		if (node instanceof FuncCallNode){
			return visit ((FuncCallNode) node);
		}
		if (node instanceof FuncDeclarationNode){
			return visit ((FuncDeclarationNode) node);
		}
		if (node instanceof GeneralIdentNode){
			return visit ((GeneralIdentNode) node);
		}
		if (node instanceof IfNode){
			return visit ((IfNode) node);
		}
		if (node instanceof IterationNode){
			return visit ((IterationNode) node);
		}
		if (node instanceof LogicalANDExprNode){
			return visit ((LogicalANDExprNode) node);
		}
		if (node instanceof LogicalORExprNode){
			return visit ((LogicalORExprNode) node);
		}
		if (node instanceof MultExprNode){
			return visit ((MultExprNode) node);
		}
		if (node instanceof NumLiteralNode){
			return visit ((NumLiteralNode) node);
		}
		if (node instanceof ParenthesesNode){
			return visit ((ParenthesesNode) node);
		}
		if (node instanceof PrimaryExprNode){
			return visit ((PrimaryExprNode) node);
		}
		if (node instanceof ProgramNode){
			return visit ((ProgramNode) node);
		}
		if (node instanceof RelationExprNode){
			return visit ((RelationExprNode) node);
		}
		if (node instanceof ReturnNode){
			return visit ((ReturnNode) node);
		}
		if (node instanceof RobotDeclarationNode){
			return visit ((RobotDeclarationNode) node);
		}
		if (node instanceof StatementNode){
			return visit ((StatementNode) node);
		}
		if (node instanceof TextLiteralNode){
			return visit ((TextLiteralNode) node);
		}
		if (node instanceof TypeNode) {
			return visit ((TypeNode) node);
		}
		if (node instanceof UnaryExprNode){
			return visit ((UnaryExprNode) node);
		}
		if (node instanceof VarDeclarationNode){
			return visit ((VarDeclarationNode) node);
		}
		if (node instanceof VarNode){
			return visit ((VarNode) node);
		}
		throw new InstanceNotFoundException();
		
		
	}
}
