package symbolTable;

import java.util.List;

public class STSubprogramEntry extends SymbolTableEntry {
	public enum SubprogramType {
		func, event;
	}
	
	SubprogramType type;
	List<Object> returnTypes;
	SymbolTable params;
	
	public STSubprogramEntry(int scope, SubprogramType type, List<Object> returnTypes, SymbolTable params) {
		super(scope);
		this.type = type;
		this.returnTypes = returnTypes;
		this.params = params;
	}
	
	public SubprogramType getType() {
		return type;
	}
	
	public List<Object> getReturnTypes() {
		return returnTypes;
	}
	
	public SymbolTable getParameters() {
		return params;
	}
}
