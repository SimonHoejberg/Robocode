package symbolTable;

public abstract class SymbolTableEntry {
	private int scope;
	
	public void setScope(int scope) {
		this.scope = scope;
	}
	
	public int getScope() {
		return scope;
	}
}
