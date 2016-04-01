package symbolTable;

public class STTypeEntry extends SymbolTableEntry {
	private Object type;
	
	public STTypeEntry(int scope, Object type) {
		super(scope);
		this.type = type;
	}
	
	public Object getType() {
		return type;
	}
}
