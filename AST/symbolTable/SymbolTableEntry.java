package symbolTable;

public abstract class SymbolTableEntry {
	private int scope;
	
	public SymbolTableEntry(int scope) {
		this.scope = scope;
	}
	
	public int getScope() {
		return scope;
	}
}
