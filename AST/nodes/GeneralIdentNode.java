package nodes;
import java.util.List;

public class GeneralIdentNode extends PrimaryExprNode {
    private List<BaseIdentNode> idents;
    
    public GeneralIdentNode(int lineNumber, int colNumber, List<BaseIdentNode> idents) {
    	super(lineNumber, colNumber);
    	this.idents = idents;
    }
    
    public void addIdent(BaseIdentNode ident) {
    	idents.add(ident);
    }
    
    public List<BaseIdentNode> getIdents() {
    	return idents;
    }
    
}
