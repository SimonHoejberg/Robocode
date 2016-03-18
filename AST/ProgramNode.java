import java.util.ArrayList;
import java.util.List;


public class ProgramNode extends AbstractNode {
	private List<AbstractNode> declarations;
	public ProgramNode() {
		declarations = new ArrayList<AbstractNode>();
	}
	
	public List<AbstractNode> getDeclarations() {
		return declarations;
	}
	
	public void addDeclaration(AbstractNode node) {
		declarations.add(node);
	}
}