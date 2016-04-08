package nodes;
import java.util.List;

public class DataStructDefinitionNode extends DeclarationNode {
	private String typeName;
    private List<DeclarationNode> declarations; // varDcl, dataStructDcl, arrayDcl
    
    public DataStructDefinitionNode(int lineNumber, int colNumber, String typeName, List<DeclarationNode> declarations) {
    	super(lineNumber, colNumber);
    	this.typeName = typeName;
		this.declarations = declarations;
	}
    
    public String getTypeName() {
    	return typeName;
    }
    
    public List<DeclarationNode> getDeclarations() {
    	return declarations;
    }
}