package symbolTable;

import java.util.Arrays;
import java.util.List;

public class STSubprogramEntry extends SymbolTableEntry {
	public enum SubprogramType {
		func, event;
	}
	
	SubprogramType type;
	List<Object> returnTypes;
	SymbolTable params;
	
	public STSubprogramEntry(SubprogramType type, Object[] returnTypes, SymbolTable params) {
		this.type = type;
		this.returnTypes = Arrays.asList(returnTypes);
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
