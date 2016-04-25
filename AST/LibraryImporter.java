import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import nodes.TypeNode;
import nodes.VarNode;
import symbolTable.STStructDefEntry;
import symbolTable.STSubprogramEntry;
import symbolTable.SymbolTable;
import symbolTable.STSubprogramEntry.SubprogramType;
import symbolTable.STTypeEntry;

public class LibraryImporter {

	ANTLRInputStream docInput;
	RoboDocLexer docLexer;
	CommonTokenStream docTokens;
	RoboDocParser docParser;
	RoboDocParser.ProgContext docCst;
	Library ast;
	
	public LibraryImporter() {
	
	}
	
	public void importLibraries(SymbolTable st, String libname, boolean isObject) throws IOException {		
		ANTLRInputStream docInput = new ANTLRInputStream(new FileReader("lib/" + libname));
		RoboDocLexer docLexer = new RoboDocLexer(docInput);
    	CommonTokenStream docTokens = new CommonTokenStream(docLexer);
    	RoboDocParser docParser = new RoboDocParser(docTokens);
    	RoboDocParser.ProgContext docCst = docParser.prog();
    	Library lib = (Library) new BuildDocASTVisitor().visit(docCst);
    	
    	SymbolTable symbolTable;
    	if (isObject) {
    		symbolTable = new SymbolTable();
    		st.enterSymbol(libname, new STStructDefEntry(true, symbolTable));
    		if (libname.equals("Color")) {	// FIXME Might be better to add this functionality to the doc parser
    			symbolTable.enterSymbol("r", new STTypeEntry("num".intern()));
    			symbolTable.enterSymbol("g", new STTypeEntry("num".intern()));
    			symbolTable.enterSymbol("b", new STTypeEntry("num".intern()));
    		}
    	}
    	else
    		symbolTable = st;
    	
    	List<Method> methods = lib.getMethods();
    	for (Method method : methods) {
    		boolean local;
    		local = symbolTable.declaredLocally(method.getIdent());
    				
    		if (local) 
    			continue;
    			//throw new RuntimeException("Conflicting method definitions from library imports");
    		
    		// Check if event - events are checked by parser
    		List<MethodParam> javaParams = method.getParameters();
    		if (javaParams.size() != 0) {
    			if (javaParams.get(0).getType().toString().contains("Event"))
    				continue;
    		}
    		
    		// Type conversion
    		List<VarNode> params = new ArrayList<VarNode>();
    		
    		for (MethodParam javaParam : javaParams) {
    			params.add(new VarNode(0, 0, convertType(javaParam.getType()), javaParam.getIdent()));
    		}
    		
    		// Add FuncDcl to symbol table
    		List<TypeNode> returnTypes = new ArrayList<TypeNode>();
    		returnTypes.add(new TypeNode(0,0, convertType(method.getType())));
    		symbolTable.enterSymbol(method.getIdent(), new STSubprogramEntry(SubprogramType.func,returnTypes,params)); 
    	}
	}
	
	private String convertType(Object type) {
		switch ((String) type) {
			case "int":
			case "float":
			case "double":
			case "long":
			case "short":
				return "num";
			case "String":
				return "text";
			case "boolean":
				return "bool";
			default:
				return (String) type;
		}
	}
}
