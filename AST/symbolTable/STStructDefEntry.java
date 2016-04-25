package symbolTable;

public class STStructDefEntry extends SymbolTableEntry {

	private boolean isClass;
	private SymbolTable vars;
	
	public STStructDefEntry(boolean isClass, SymbolTable vars) {
		this.isClass = isClass;
		this.vars = vars;
	}
	
	public SymbolTable getVariables() {
		return vars;
	}
	
	public boolean getIsClass() {
		return isClass;
	}
}
