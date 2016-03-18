import java.util.List;


public class StructDefinitionNode extends DeclarationNode {

    String typeName;
    List<VarDeclarationNode> declarations; // varDcl, dataStructDcl, arrayDcl

}