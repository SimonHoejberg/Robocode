import java.util.List;

public class ProgramNode extends AbstractNode {
	private List<DeclarationNode> declarations;
	
	public ProgramNode(int lineNumber, int colNumber, List<DeclarationNode> declarations) {
		super(lineNumber, colNumber);
		this.declarations = declarations;
	}
	
	public List<DeclarationNode> getDeclarations() {
		return declarations;
	}
	
	//public void addDeclaration(DeclarationNode node) {
	//	declarations.add(node);
	//}
}