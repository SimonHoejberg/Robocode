public class TextLiteralNode extends PrimaryExprNode {
	private String text;
	
    public TextLiteralNode(String text) {
		this.text = text;
	}
    
    public String getText() {
    	return text;
    }
}