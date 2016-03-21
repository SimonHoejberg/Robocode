import java.util.List;

public class EventDeclarationNode extends DeclarationNode {
	
    String ident;

    VarNode param; // Maybe use ParamNode instead?

    List<StatementNode> statements;
    
    public EventDeclarationNode(String ident, VarNode param, List<StatementNode> statements) {
    	this.ident = ident;
    	this.param = param;
    	this.statements = statements;
    }

}
