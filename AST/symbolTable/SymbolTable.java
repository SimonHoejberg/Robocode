package symbolTable;

import java.util.*;

public class SymbolTable implements SymbolTableInterface {
	private int currentScope;
	private Hashtable<String, Stack<SymbolTableEntry>> table;
	private List<List<String>> scopeEntries;
	private List<SymbolTableEntry> unusedSymbols;
	
	
	public SymbolTable() {
		currentScope = 0;
		table = new Hashtable<String, Stack<SymbolTableEntry>>();
		scopeEntries = new ArrayList<List<String>>();
		scopeEntries.add(new ArrayList<String>());
		unusedSymbols = new ArrayList<SymbolTableEntry>();
	}
	
	public void openScope() {
		currentScope++;
		scopeEntries.add(new ArrayList<String>());
	}
	
	public void closeScope() {
		// Remove entries in current scope
		for (String ident : scopeEntries.get(currentScope)) {
			Stack<SymbolTableEntry> stack = table.get(ident);
			SymbolTableEntry entry = stack.pop();
			// If the symbol was never used, add to list of unused symbols
			if (!entry.getUsed())
				unusedSymbols.add(entry);
			// If stack is empty, remove it from the table
			if (stack.isEmpty())
				table.remove(ident);
		}
		
		// Remove scope entries
		scopeEntries.remove(currentScope);
		
		currentScope--;
	}
	
	public void enterSymbol(String ident, SymbolTableEntry entry) {
		// Add entry to scopeEntries
		// Add ident to list
		scopeEntries.get(currentScope).add(ident);
		// Add entry to symbol table
		// Add stack if none exists
		if (!table.containsKey(ident))
			table.put(ident, new Stack<SymbolTableEntry>());

		// Add scope to SymbolTableEntry, push to stack
		entry.setScope(currentScope);
		table.get(ident).push(entry);
	}
	
	public SymbolTableEntry retrieveSymbol(String ident) throws Exception {
		// Retrieve the symbol
		if (table.containsKey(ident)) {
			Stack<SymbolTableEntry> stack = table.get(ident);
			if (!stack.isEmpty())		// FIXME Possibly unnecessary?
				return table.get(ident).peek();
		}
		
		// If symbol could not be found, throw exception
		throw new Exception(ident + " cannot be resolved to a type");
	}
	
	public Stack<SymbolTableEntry> retrieveSymbolStack(String ident) throws Exception {
		// Retrieve the symbol
		if (table.containsKey(ident)) {
			Stack<SymbolTableEntry> stack = table.get(ident);
			return stack;
		}
		
		// If symbol could not be found, throw exception
		throw new Exception(ident + " cannot be resolved to a type");
	}
		
	public boolean declaredLocally(String ident) {
		// Retrieve symbol
		try {
			SymbolTableEntry entry = retrieveSymbol(ident);
			
			// If symbol is in current scope, return true
			return entry.getScope() == currentScope;
		}
		catch (Exception ex) {
			return false;
		}		
	}
	
	public boolean declaredInCurrentSubprogram(String ident) {
		// Retrieve symbol
		try {
			SymbolTableEntry entry = retrieveSymbol(ident);
			
			// If symbol declared in the current subprogram, return true (scope 0 is global)
			return entry.getScope() > 0;
		}
		catch (Exception ex) {
			return false;
		}		
	}
	
	public void checkGlobalVarUsage() {
		// Chheck entries in global scope
		for (String ident : scopeEntries.get(0)) {
			Stack<SymbolTableEntry> stack = table.get(ident);
			SymbolTableEntry entry = stack.peek();
			// If the symbol was never used, add to list of unused symbols
			if (!entry.getUsed())
				unusedSymbols.add(entry);
		}
	}
	
	public List<SymbolTableEntry> getUnusedSymbols() {
		return unusedSymbols;
	}
}