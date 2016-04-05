import java.util.List;

public class EventDeclarationNode extends DeclarationNode {
	
    private String ident;

    private VarNode param; // Maybe use ParamNode instead?

    private List<StatementNode> statements;
    
    public EventDeclarationNode(String ident, VarNode param, List<StatementNode> statements, int lineNumber, int colNumber) {
    	super(lineNumber, colNumber);
    	this.ident = ident;
    	this.param = param;
    	this.statements = statements;
    }

    public String getIdent() {
    	return ident;
    }
    
    public VarNode getParam() {
    	return param;
    }
    
    public List<StatementNode> getStatements() {
    	return statements;
    }
}
