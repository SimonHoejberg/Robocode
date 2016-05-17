package symbolTable;

public class STTypeEntry extends SymbolTableEntry {
	private Object type;
	
	public STTypeEntry(Object type) {
		this.type = type;
		used = false;
	}
	
	public Object getType() {
		return type;
	}
}
