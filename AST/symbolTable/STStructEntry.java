package symbolTable;

public class STStructEntry extends SymbolTableEntry {

	SymbolTable vars;
	
	public STStructEntry(int scope, SymbolTable vars) {
		super(scope);
		this.vars = vars;
	}
	
	public SymbolTable getVariables() {
		return vars;
	}

}
