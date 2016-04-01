package symbolTable;

public interface SymbolTableInterface {
	public abstract void openScope(); //Push a new symbol table
	
	public abstract void closeScope(); //Pop the top of the stack
	
	public abstract void enterSymbol(String ident, SymbolTableEntry entry);
	
	public abstract SymbolTableEntry retrieveSymbol(String ident) throws Exception;
	
	public abstract boolean declaredLocally(String ident) throws Exception;
}
