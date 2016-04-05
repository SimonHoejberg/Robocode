public class TextLiteralNode extends PrimaryExprNode {
	private String text;
	
    public TextLiteralNode(int lineNumber, int colNumber, String text) {
    	super(lineNumber, colNumber);
    	this.text = text;
	}
    
    public String getText() {
    	return text;
    }
}