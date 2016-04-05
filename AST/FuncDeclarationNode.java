import java.util.List;

public class FuncDeclarationNode extends DeclarationNode {

	private List<TypeNode> returnTypes;

    private String ident;

    private List<VarNode> paramList;

    private List<StatementNode> statements;

    public FuncDeclarationNode(int lineNumber, int colNumber, List<TypeNode> returnTypes, String ident, List<VarNode> paramList,
			List<StatementNode> statements) {
    	super(lineNumber, colNumber);
    	this.returnTypes = returnTypes;
    	this.ident = ident;
    	this.paramList = paramList;
    	this.statements = statements;
	}
    
    public List<TypeNode> getReturnTypes() {
    	return returnTypes;
    }
    
    public String getIdent() {
    	return ident;
    }
    
    public List<VarNode> getParamList() {
    	return paramList;
    }
    
    public List<StatementNode> getStatements() {
    	return statements;
    }
}
