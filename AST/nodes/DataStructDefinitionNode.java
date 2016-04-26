package nodes;
import java.util.List;

public class DataStructDefinitionNode extends DeclarationNode {
	private String typeName;
    private List<Object> declarations; // varDcl, dataStructDcl, arrayDcl
    
    public DataStructDefinitionNode(int lineNumber, int colNumber, String typeName, List<Object> declarations) {
    	super(lineNumber, colNumber);
    	this.typeName = typeName;
		this.declarations = declarations;
	}
    
    public String getTypeName() {
    	return typeName;
    }
    
    public List<Object> getDeclarations() {
    	return declarations;
    }
}