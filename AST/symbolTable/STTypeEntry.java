package symbolTable;

public class STTypeEntry extends SymbolTableEntry {
	private Object type;
	
	public STTypeEntry(Object type, boolean used) {
		this.type = type;
		this.used = used;
	}
	
	public Object getType() {
		return type;
	}
}
