import java.util.ArrayList;
import java.util.List;

public class ProgramNode extends AbstractNode {
	private List<DeclarationNode> declarations;
	public ProgramNode(List<DeclarationNode> declarations) {
		this.declarations = declarations;
	}
	
	public List<DeclarationNode> getDeclarations() {
		return declarations;
	}
	
	//public void addDeclaration(DeclarationNode node) {
	//	declarations.add(node);
	//}
}