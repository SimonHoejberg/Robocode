public class CallStatementNode extends StatementNode {
    private GeneralIdentNode ident;
    
    public CallStatementNode(int lineNumber, int colNumber, GeneralIdentNode ident) {
    	super(lineNumber, colNumber);
    	this.ident = ident;
    }
    
    public GeneralIdentNode getIdent() {
    	return ident;
    }
}
