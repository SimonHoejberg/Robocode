import nodes.AbstractNode;


public class TypeCheckError extends TypeCheckProblem {
	
	public TypeCheckError(AbstractNode node, String msg) {
		super(node, msg);
	}
}
