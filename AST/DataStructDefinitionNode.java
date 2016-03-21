import java.util.List;

public class DataStructDefinitionNode extends DeclarationNode {
	String typeName;
    List<DeclarationNode> declarations; // varDcl, dataStructDcl, arrayDcl
    
    public DataStructDefinitionNode(String typeName, List<DeclarationNode> declarations) {
		this.typeName = typeName;
		this.declarations = declarations;
	}

}