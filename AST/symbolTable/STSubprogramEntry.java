package symbolTable;

import java.util.Arrays;
import java.util.List;
import nodes.TypeNode;

public class STSubprogramEntry extends SymbolTableEntry {
	public enum SubprogramType {
		func, event;
	}
	
	SubprogramType type;
	List<TypeNode> returnTypes;
	SymbolTable params;
	
	public STSubprogramEntry(SubprogramType type, List<TypeNode> returnTypes, SymbolTable params) {
		this.type = type;
		this.returnTypes = returnTypes;
		this.params = params;
	}
	
	public SubprogramType getType() {
		return type;
	}
	
	public List<TypeNode> getReturnTypes() {
		return returnTypes;
	}
	
	public SymbolTable getParameters() {
		return params;
	}
}
