package symbolTable;

import java.util.List;
import nodes.TypeNode;
import nodes.VarNode;

public class STSubprogramEntry extends SymbolTableEntry {
	public enum SubprogramType {
		func, event;
	}
	
	SubprogramType type;
	List<TypeNode> returnTypes;
	List<VarNode> params;
	
	public STSubprogramEntry(SubprogramType type, List<TypeNode> returnTypes, List<VarNode> params) {
		this.type = type;
		this.returnTypes = returnTypes;
		this.params = params;
		used = false;
	}
	
	public SubprogramType getType() {
		return type;
	}
	
	public List<TypeNode> getReturnTypes() {
		return returnTypes;
	}
	
	public List<VarNode> getParameters() {
		return params;
	}
}
