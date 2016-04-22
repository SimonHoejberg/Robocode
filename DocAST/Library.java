import java.util.List;

public class Library {
	
	private String name;
	private List<Method> methods;
	
	public Library(List<Method> methods) {
		this.methods = methods;
	}
	
	public String getName() {
		return name;
	}
	
	public List<Method> getMethods() {
		return methods;
	}
	
}
