import java.util.List;

public class RobotDeclarationNode extends DeclarationNode {

	 public enum RobotDeclarationType { 

	        name, initialization, behavior

	 }

    private RobotDeclarationType type;

    private String name;

    private List<StatementNode> statements;

    public RobotDeclarationNode(RobotDeclarationType type, String name) {
    	this.type = type;
    	this.name = name;
    }
    
    public RobotDeclarationNode(RobotDeclarationType type, List<StatementNode> statements) {
    	this.type = type;
    	this.statements = statements;
    }
    
    public RobotDeclarationType getType() {
    	return type;
    }
    
    public String getName() {
    	return name;
    }
    
    public List<StatementNode> getStatements() {
    	return statements;
    }
}
