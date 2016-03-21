import java.util.List;

public class GeneralIdentNode extends PrimaryExprNode {
    List<BaseIdentNode> idents;
    
    public GeneralIdentNode(List<BaseIdentNode> idents) {
    	this.idents = idents;
    }
    
    public void addIdent(BaseIdentNode ident) {
    	idents.add(ident);
    }
    
}
