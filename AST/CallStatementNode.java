public class CallStatementNode extends StatementNode {
    private GeneralIdentNode ident;
    
    public CallStatementNode(GeneralIdentNode ident) {
    	this.ident = ident;
    }
    
    public GeneralIdentNode getIdent() {
    	return ident;
    }
}
