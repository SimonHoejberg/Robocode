
public class SymbolTableEntry {
	private String type;
	
	public SymbolTableEntry(String ident, String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
}
