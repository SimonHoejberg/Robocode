import nodes.AbstractNode;


public class TypeCheckWarning extends TypeCheckProblem {
	
	public TypeCheckWarning(AbstractNode node, String msg) {
		super(node, msg);
	}
}
