import java.util.List;

public class DataStructDefinitionNode extends DeclarationNode {
	private String typeName;
    private List<DeclarationNode> declarations; // varDcl, dataStructDcl, arrayDcl
    
    public DataStructDefinitionNode(String typeName, List<DeclarationNode> declarations, int lineNumber, int colNumber) {
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