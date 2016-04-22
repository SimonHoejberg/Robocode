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
			methods.add((Method) visit((RoboDocParser.MethodContext) method));
		}
		
		Library lib = new Library(methods);
		
		return lib;
	}
	
	@SuppressWarnings("unchecked")
	public Object visit(RoboDocParser.MethodContext context) {
		List<MethodParam> params;
		if (context.params() == null)
			params = new ArrayList<MethodParam>();
		else
			params = (List<MethodParam>) visit(context.params());
		
		return new Method(context.type().getText().intern(),
				context.Ident().getText(),
				params,
				context.description().Text().getText());
	}
	
	public Object visit(RoboDocParser.ParamsContext context) {
		int paramCount = context.type().size();
		List<MethodParam> params = new ArrayList<MethodParam>();
		for (int i = 0; i < paramCount; ++i)
			params.add(new MethodParam(context.type().get(i).getText().intern(), context.Ident().get(i).getText()));

		return params;
	}
}
