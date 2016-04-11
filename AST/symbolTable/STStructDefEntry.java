package symbolTable;

public class STStructDefEntry extends SymbolTableEntry {

	SymbolTable vars;
	
	public STStructDefEntry(SymbolTable vars) {
		this.vars = vars;
	}
	
	public SymbolTable getVariables() {
		return vars;
	}

}
