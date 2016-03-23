import java.util.List;

public class GeneralIdentNode extends PrimaryExprNode {
    private List<BaseIdentNode> idents;
    
    public GeneralIdentNode(List<BaseIdentNode> idents) {
    	this.idents = idents;
    }
    
    public void addIdent(BaseIdentNode ident) {
    	idents.add(ident);
    }
    
    public List<BaseIdentNode> getIdents() {
    	return idents;
    }
    
}
