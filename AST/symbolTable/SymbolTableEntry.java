package symbolTable;

import nodes.AbstractNode;

public abstract class SymbolTableEntry {
	private int scope;
	private AbstractNode node;
	protected boolean used;
	
	public SymbolTableEntry() {
		used = false;
	}
	
	public void setScope(int scope) {
		this.scope = scope;
	}
	
	public int getScope() {
		return scope;
	}
	
	public void setNode(AbstractNode node) {
		this.node = node;
	}
	
	public AbstractNode getNode() {
		return node;
	}
	
	public boolean getUsed() {
		return used;
	}
	
	public void useSymbol() {
		used = true;
	}
}
