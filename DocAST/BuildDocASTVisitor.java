import java.util.ArrayList;
import java.util.List;

public class BuildDocASTVisitor extends RoboDocBaseVisitor<Object> {
	public Object visit(RoboDocParser.ProgContext context) {
		return visit(context.dcls());
	}
	
	public Object visit(RoboDocParser.DclsContext context) {
		List<Method> methods = new ArrayList<Method>();
		List<RoboDocParser.MethodContext> methodCtx = context.method();
		for (RoboDocParser.MethodContext method : methodCtx){
			methods.add((Method) visit(method));
		}
		
		return methods;
	}
	
	public Object visit(RoboDocParser.MethodContext context) {
		List<MethodParam> params = new ArrayList<MethodParam>();
		List<RoboDocParser.ParamsContext> arguments = context.params();
		for (RoboDocParser.ParamsContext arg : arguments) {
			params.add((MethodParam) visit(arg));
		}
		
		return new Method(context.type().getText().intern(),
							context.Ident().getText(),
							params,
							context.description().getText());
	}
	
	public Object visit(RoboDocParser.ParamsContext context) {
		return new MethodParam(context.type().toString().intern(), context.Ident().toString());
	}
}
