import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;

import nodes.VarNode;

public class BuildDocASTVisitor extends RoboDocBaseVisitor<Object> {
	public Object visit(RoboDocParser.ProgContext context) {
		return visit(context.dcls());
	}
	
	public Object visit(RoboDocParser.DclsContext context) {
		List<Method> methods = new ArrayList<Method>();
		
		for (ParseTree method : context.children)
			methods.add((Method) visit(method));
		
		Library lib = new Library((String) visit(context.libname()), methods);
		
		return lib;
	}
	
	public Object visit(RoboDocParser.LibnameContext context) {
		return context.Text().getText();
	}
	
	public Object visit(RoboDocParser.MethodContext context) {
		List<VarNode> params = new ArrayList<VarNode>();
		List<RoboDocParser.ParamContext> arguments = context.param();
		for (RoboDocParser.ParamContext arg : arguments) {
			params.add((VarNode) visit(arg));
		}
		
		return new Method(context.type().getText().intern(),
							context.Ident().getText(),
							params,
							context.description().getText());
	}
	
	public Object visit(RoboDocParser.ParamContext context) {
		return new MethodParam(context.type().getText().intern(), context.Ident().getText());
	}
}
