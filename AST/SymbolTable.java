import java.util.Hashtable;


public class SymbolTable {
	
	Hashtable<String, Object> symbolTable;
	
	public SymbolTable(){
		symbolTable = new Hashtable<String, Object>();
	}
	
	public void addEntry(String ident, String type) {
		symbolTable.put(ident, type);
	}
	
	
	public void addScope(){
		
	}
}
