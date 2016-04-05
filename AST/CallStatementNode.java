public class CallStatementNode extends StatementNode {
    private GeneralIdentNode ident;
    
    public CallStatementNode(GeneralIdentNode ident, int lineNumber, int colNumber) {
    	super(lineNumber, colNumber);
    	this.ident = ident;
    }
    
    public GeneralIdentNode getIdent() {
    	return ident;
    }
}
