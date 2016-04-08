package nodes;
import org.antlr.v4.runtime.tree.ParseTree;

public abstract class AbstractNode {
	protected Object nodeType;
	protected int lineNumber, colNumber;
	
	public AbstractNode(int lineNumber, int colNumber) {
		this.lineNumber = lineNumber;
		this.colNumber = colNumber;
	}
	
	public Object getNodeType() {
		return nodeType;
	}
	
	public void setNodeType(Object nodeType) {
		this.nodeType = nodeType;
	}
	
	public int getLineNumber() {
		return lineNumber;
	}
	
	public int getColumnNumber() {
		return colNumber;
	}
}