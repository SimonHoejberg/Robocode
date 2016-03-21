import java.util.List;

public class FuncDeclarationNode extends DeclarationNode {

	List<TypeNode> returnTypes;

    String ident;

    List<VarNode> paramList;

    List<StatementNode> statements;

    public FuncDeclarationNode(List<TypeNode> returnTypes, String ident, List<VarNode> paramList,
			List<StatementNode> statements) {
    	this.returnTypes = returnTypes;
    	this.ident = ident;
    	this.paramList = paramList;
    	this.statements = statements;
	}
}
