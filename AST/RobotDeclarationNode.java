import java.util.List;

public class RobotDeclarationNode extends DeclarationNode {

	 public enum RobotDeclarationType { 

	        name, initialization, behavior

	 }

    RobotDeclarationType type;

    String name;

    List<StatementNode> statements;

    public RobotDeclarationNode(RobotDeclarationType type, String name) {
    	this.type = type;
    	this.name = name;
    }
    
    public RobotDeclarationNode(RobotDeclarationType type, List<StatementNode> statements) {
    	this.type = type;
    	this.statements = statements;
    }
}
