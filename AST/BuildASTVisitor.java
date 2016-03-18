import RobotDeclarationNode.RobotDeclarationType;



public class BuildASTVisitor extends HelloBaseVisitor<AbstractNode> {
	public AbstractNode visitCompileUnit(HelloParser.ProgContext currentNode) {
		return visit(currentNode.dcls());
	}
	
	public AbstractNode visitDcls(HelloParser.DclsContext currentNode) {
		AbstractNode node = new ProgramNode();
		for (int i = 0; i < currentNode.children.size(); ++i) {
			((ProgramNode) node).addDeclaration(visit(currentNode.getChild(i)));
		}
		return node;
	}
	
	public AbstractNode visitRobonameAssign(HelloParser.RobonameAssignContext currentNode) {currentNode
		return new RobotDeclarationNode(RobotDeclarationType.name, );
	}
}
