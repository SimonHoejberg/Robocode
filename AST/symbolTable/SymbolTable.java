package symbolTable;

import java.lang.reflect.Array;
import java.util.*;

public class SymbolTable implements SymbolTableInterface {
	private int currentScope;
	private Hashtable<String, Stack<SymbolTableEntry>> table;
	private List<List<String>> scopeEntries;
	
	
	public SymbolTable() {
		currentScope = 0;
		table = new Hashtable<String, Stack<SymbolTableEntry>>();
		scopeEntries = new ArrayList<List<String>>();
	}
	
	public void openScope() {
		currentScope++;
	}
	
	public void closeScope() {
		// Remove entries in current scope
		for (String ident : scopeEntries.get(currentScope)) {
			Stack<SymbolTableEntry> stack = table.get(ident);
			stack.pop();
			// If stack is empty, remove it from the table
			if (stack.peek() == null)
				table.remove(stack);
		}
		
		// Remove scope entries
		scopeEntries.remove(currentScope);
		
		currentScope--;
	}
	
	public void enterSymbol(String ident, SymbolTableEntry entry) {
		// Add entry to scopeEntries
		// Add list if none exists
		if (scopeEntries.get(currentScope) == null)
			scopeEntries.add(new ArrayList<String>());
		// Add ident to list
		scopeEntries.get(currentScope).add(ident);
		// Add entry to symbol table
		// Add stack if none exists
		if (!table.contains(ident))
			table.put(ident, new Stack<SymbolTableEntry>());
		// Create SymbolTableEntry, push to stack
		table.get(ident).push(entry);
	}
	
	public SymbolTableEntry retrieveSymbol(String ident) throws Exception {
		// Retrieve the symbol
		if (table.contains(ident)) {
			Stack<SymbolTableEntry> stack = table.get(ident);
			if (stack.peek() != null)
				return table.get(ident).peek();
		}
		
		// If symbol could not be found, throw exception
		throw new Exception(ident + " cannot be resolved to a type");
	}
	
	public boolean declaredLocally(String ident) throws Exception {
		// Retrieve symbol
		SymbolTableEntry entry = retrieveSymbol(ident);
		
		// If symbol is in current scope, return true
		return entry.getScope() == currentScope;
	}
}