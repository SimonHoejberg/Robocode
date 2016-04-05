import java.util.List;

public class RobotDeclarationNode extends DeclarationNode {

	 public enum RobotDeclarationType { 

	        name, initialization, behavior

	 }

    private RobotDeclarationType type;

    private String name;

    private List<StatementNode> statements;

    public RobotDeclarationNode(int lineNumber, int colNumber, RobotDeclarationType type, String name) {
    	super(lineNumber, colNumber);
    	this.type = type;
    	this.name = name;
    }
    
    public RobotDeclarationNode(int lineNumber, int colNumber, RobotDeclarationType type, List<StatementNode> statements) {
    	super(lineNumber, colNumber);
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
