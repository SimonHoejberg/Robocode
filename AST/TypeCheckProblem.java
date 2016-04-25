import nodes.AbstractNode;


public class TypeCheckProblem {

	protected String msg;
	protected AbstractNode node;
	
	public TypeCheckProblem(AbstractNode node, String msg) {
		this.node = node;
		this.msg = msg;
	}
	
	public String getMessage() {
		return msg;
	}
	
	public AbstractNode getNode() {
		return node;
	}
	
	public String toString() {
		return node.getLineNumber() + ":" + (node.getColumnNumber()+1) + " " + msg;
	}
}
