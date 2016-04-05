package symbolTable;

public class STStructEntry extends SymbolTableEntry {

	SymbolTable vars;
	
	public STStructEntry(SymbolTable vars) {
		this.vars = vars;
	}
	
	public SymbolTable getVariables() {
		return vars;
	}

}
