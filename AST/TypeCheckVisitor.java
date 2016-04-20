import java.util.*;

import exceptions.*;
import nodes.*;
import nodes.AssignmentNode.AssignmentType;
import symbolTable.*;
import symbolTable.STSubprogramEntry.SubprogramType;

public class TypeCheckVisitor extends ASTVisitor<Object> {
	SymbolTable symbolTable;
	List<TypeCheckError> errors;
	private STStructDefEntry currentStructDef;
	private STSubprogramEntry currentFuncDcl;
	private String currentFuncDclName;
	final Object 	NUM = "num".intern(),
					TEXT = "text".intern(),
					BOOL = "bool".intern(),
					VOID = "void".intern();
	
	
	public TypeCheckVisitor() {
		symbolTable = new SymbolTable();
		errors = new ArrayList<TypeCheckError>();
	}
	
	public List<TypeCheckError> getErrorList(){
		return errors;
	}
	
	@Override
	public Object visit(AdditiveExprNode node) {
		Object leftType = visit(node.getLeftChild());
		Object rightType = visit(node.getRightChild());
		if (leftType == NUM && rightType == NUM) {
			node.setNodeType(NUM);
			return NUM;
		}
		
		errors.add(new TypeCheckError(node, "The operator " + node.getType() + " is undefined for the argument type(s) " + leftType + ", " + rightType));
		return NUM;
	}

	@Override
	public Object visit(ArrayDeclarationNode node) {
		
		// Check if the symbol has already been defined in this scope
		boolean local;
		try {
			local = symbolTable.declaredLocally(node.getIdent());
		}
		catch (Exception ex) {
			local = false;
		}
		
		if (local) {
			errors.add(new TypeCheckError(node, "Duplicate local variable " + node.getIdent()));
			return VOID;
		}
		
		Object varType = node.getType().intern();
		Object rhsType = visit(node.getSize());
		
		// Add variable to symbol table
		if(currentStructDef == null)
			symbolTable.enterSymbol(node.getIdent(), new STArrayEntry(varType));		// new STArrayEntry(((String) varType + "[]").intern())
		else	
			currentStructDef.getVariables().enterSymbol(node.getIdent(), new STArrayEntry(varType)); 	// new STArrayEntry(((String) varType + "[]").intern())
		if (rhsType == NUM) {
			node.setNodeType(VOID);
			return VOID;
		}
				
		errors.add(new TypeCheckError(node, "Array size must be of type num"));
		return VOID;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visit(AssignmentNode node) {
		// Symbol table lookup
		List<Object> lhs = new ArrayList<Object>();
		List<AbstractNode> input = node.getVariables();
		boolean hasVarDcl = false;
		String dcl = "";
		Object bs;
		for(AbstractNode n : input){
			if(n instanceof GeneralIdentNode){
					bs = visit((GeneralIdentNode)n); 
					if(bs instanceof List<?>)
						lhs.addAll((List<Object>)bs);
					else
						lhs.add(bs);
			}	
			else if(n instanceof VarNode){
				lhs.add(visit((VarNode)n));
				dcl += ((VarNode) n).getType().intern() + " " + ((VarNode) n).getIdent();
				dcl += ", ";
				hasVarDcl = true;
			}
		}
		if(hasVarDcl && !node.getType().equals(AssignmentType.basic))
			errors.add(new TypeCheckError(node, "Can not use the operator "+node.getType().toString()+" when declaring variables "+ dcl.substring(0, dcl.length()-2)));
		
		List<Object> rhs = new ArrayList<Object>();
		Object temp = visit(node.getExpression());
		if(temp instanceof List<?>)
			rhs.addAll((List<Object>)temp);
		else
			rhs.add(temp);
		
		int lhsSize = lhs.size();
		int rhsSize = rhs.size();
		if(lhsSize == rhsSize){
			for (int i = 0; i < lhsSize; ++i) {
				if (lhs.get(i) == rhs.get(i))
					continue;
				else {
					errors.add(new TypeCheckError(node, "Cannot assign variable of type " + lhs.get(i) + " a value of type " + rhs.get(i)));
					return VOID;
				}
			}
		} else {
			errors.add(new TypeCheckError(node, "Lefthand side is of size "+ lhsSize +" and righthand side of size "+ rhsSize));
		}
		node.setNodeType(VOID);
		return VOID;
	}
	
	
	@Override
	public Object visit(BaseIdentNode node) {
		try {
			SymbolTableEntry entry;
			if (currentStructDef == null) {
				entry = symbolTable.retrieveSymbol(node.getIdent());
			}
			else
				entry = currentStructDef.getVariables().retrieveSymbol(node.getIdent());
			
			Object type;
			if (entry instanceof STStructEntry) {
				type = ((STStructEntry) entry).getType();
				currentStructDef = (STStructDefEntry) symbolTable.retrieveSymbol((String) type);	// FIXME Exception handling? Shouldn't be required
				node.setNodeType(type);
				return type;
			}
			else if (entry instanceof STArrayEntry) {
				STArrayEntry arrayEntry = (STArrayEntry) entry;
				//System.out.println(((STArrayEntry) entry).getType());
				type = arrayEntry.getType();
				if (type == NUM || type == TEXT || type == BOOL) {
					node.setNodeType(type);
					return type;
				}
				else {
					currentStructDef = (STStructDefEntry) symbolTable.retrieveSymbol((String) type);	// FIXME Exception handling? Shouldn't be required
					node.setNodeType(type);																// FIXME Maybe add [] to type since technically it's an array?
					return type;
				}
			}
			else if (entry instanceof STStructDefEntry || entry instanceof STSubprogramEntry) {
				// FIXME Error
				return VOID;
			}
			else if (entry instanceof STTypeEntry) {
				type = ((STTypeEntry) entry).getType();
				node.setNodeType(type);
				return type;
			}
			else {
				throw new NotImplementedException();
			}
		}
		catch (Exception ex) {
			errors.add(new TypeCheckError(node, node.getIdent() + " cannot be resolved")); //FIXME Errors
			return VOID;
		}
	}

	@Override
	public Object visit(BoolLiteralNode node) {
		node.setNodeType(BOOL);
		return BOOL;
	}
	
	@Override
	public Object visit(CallStatementNode node) {
		return visit((GeneralIdentNode) node.getIdent());
	}

	@Override
	public Object visit(DataStructDeclarationNode node) {
		// Check if the symbol has already been defined in this scope
		boolean local;
				
		local = symbolTable.declaredLocally(node.getIdent());
				
		if (local) {
			errors.add(new TypeCheckError(node, "Duplicate local variable " + node.getIdent()));
			return VOID;
		}
		
		// Look up type
		boolean defExists;
		
		try {
			Stack<SymbolTableEntry> entries = symbolTable.retrieveSymbolStack(node.getType());
			defExists = false;
			for (SymbolTableEntry entry : entries) {
				// Check if all entries are not of type structdef
				if (entry instanceof STStructDefEntry) {
					defExists = true;
					break;
				}
			}
		}
		catch (Exception ex) {
			defExists = false;
		}
		
		if (!defExists) {
			errors.add(new TypeCheckError(node, node.getType() + " cannot be resolved to a type"));
			return VOID;
		}
		
		// Add variable to symbol table
		if(currentStructDef == null)
			symbolTable.enterSymbol(node.getIdent(), new STStructEntry(node.getType().intern()));
		else
			currentStructDef.getVariables().enterSymbol(node.getIdent(), new STStructEntry(node.getType().intern()));
		node.setNodeType(VOID);
		return VOID;
	}
	
	@Override
	public Object visit(DataStructDefinitionNode node) {
		// Check if the symbol has already been defined in this scope
		boolean local;
				
		local = symbolTable.declaredLocally(node.getTypeName());
				
		if (local) {
			errors.add(new TypeCheckError(node, "Duplicate struct definition " + node.getTypeName()));
			return VOID;
		}
		
		// Add variable to symbol table
		STStructDefEntry def = new STStructDefEntry(new SymbolTable());
		currentStructDef = def;
		List<DeclarationNode> input = node.getDeclarations();
		for(DeclarationNode d : input)
			visit(d);
		currentStructDef = null;
		symbolTable.enterSymbol(node.getTypeName().intern(), def);
		node.setNodeType(VOID);
		return VOID;
	}

	@Override
	public Object visit(DeclarationNode node) {
		if (node instanceof RobotDeclarationNode)
			return visit((RobotDeclarationNode) node);
		else if (node instanceof EventDeclarationNode)
			return visit((EventDeclarationNode) node);
		else if (node instanceof FuncDeclarationNode)
			return visit((FuncDeclarationNode) node);
		else if (node instanceof VarDeclarationNode)
			return visit((VarDeclarationNode) node);
		else if (node instanceof DataStructDefinitionNode)
			return visit((DataStructDefinitionNode) node);
		else if (node instanceof DataStructDeclarationNode)
			return visit((DataStructDeclarationNode) node);
		else if (node instanceof ArrayDeclarationNode)
			return visit((ArrayDeclarationNode) node);
		throw new NotImplementedException();
	}

	@Override
	public Object visit(EqualityExprNode node) {
		Object leftType = visit(node.getLeftChild());
		Object rightType = visit(node.getRightChild());
		if(leftType == NUM && rightType == NUM || leftType == TEXT && rightType == TEXT ){
			node.setNodeType(BOOL);
			return BOOL;
		}
		errors.add(new TypeCheckError(node, "The operator " + node.getNodeType() + " is undefined for the argument type(s) " + leftType + ", " + rightType));
		return BOOL;
	}

	@Override
	public Object visit(EventDeclarationNode node) {
		boolean local;
		
		local = symbolTable.declaredLocally(node.getIdent());
				
		if (local) {
			errors.add(new TypeCheckError(node, "Duplicate event " + node.getIdent()));
			return VOID;
		}
		
		// Add variable to symbol table
		List<VarNode> eventParams = new ArrayList<VarNode>();
		eventParams.add(node.getParam());
		symbolTable.enterSymbol(node.getIdent(), new STSubprogramEntry(SubprogramType.event,new ArrayList<TypeNode>(),eventParams));
		symbolTable.openScope();
		symbolTable.enterSymbol(node.getParam().getIdent(), new STTypeEntry(node.getParam().getType().intern()));
		List<StatementNode> input = node.getStatements();
		for(StatementNode i : input)
			visit(i);
		
		symbolTable.closeScope();
		node.setNodeType(VOID);
		return VOID;
	}

	@Override
	public Object visit(ExpressionNode node) {
		if (node instanceof LogicalORExprNode)
			return visit((LogicalORExprNode) node);
		else if (node instanceof LogicalANDExprNode)
			return visit((LogicalANDExprNode) node);
		else if (node instanceof EqualityExprNode)
			return visit((EqualityExprNode) node);
		else if (node instanceof RelationExprNode)
			return visit((RelationExprNode) node);
		else if (node instanceof AdditiveExprNode)
			return visit((AdditiveExprNode) node);
		else if (node instanceof MultExprNode)
			return visit((MultExprNode) node);
		else if (node instanceof UnaryExprNode)
			return visit((UnaryExprNode) node);
		else if (node instanceof PrimaryExprNode)
			return visit((PrimaryExprNode) node);
		throw new NotImplementedException();
	}

	@Override
	public Object visit(ForNode node) {
		Object assign = node.assign;
		
		if (assign instanceof ExpressionNode)
			visit((ExpressionNode) assign);
		else if (assign instanceof VarDeclarationNode)
			visit((VarDeclarationNode) assign);
		else if (assign instanceof AssignmentNode)
			visit((AssignmentNode) assign);
		else
			throw new NotImplementedException();

		Object exprType = visit((ExpressionNode) node.predicate);
		if (exprType != BOOL)
			errors.add(new TypeCheckError(node, "Type mismatch: cannot convert from " + exprType + " to boolean"));
		else
			node.setNodeType(VOID);
		
		Object update = node.update;
		if (update instanceof ExpressionNode)
			visit((ExpressionNode) update);
		else if (update instanceof AssignmentNode)
			visit((AssignmentNode) update);
		else
			throw new NotImplementedException();
		
		List<StatementNode> stms = node.getStatements();
		for(StatementNode stm : stms){
			visit(stm);
		}
		return VOID;
	}
	
	@Override
	public Object visit(FuncCallNode node) {
		List<Object> type = new ArrayList<Object>();
		try {
			// TODO class method calls
			SymbolTableEntry entry = symbolTable.retrieveSymbol(node.getIdent());
			if (entry instanceof STSubprogramEntry) {
				STSubprogramEntry subprogram = (STSubprogramEntry) entry;
				List<ExpressionNode> arguments = node.getArguments();
				List<VarNode> params = subprogram.getParameters();
				
				int argSize = arguments.size();
				int paramSize = params.size();
				
				if (argSize == paramSize) {
					for (int i = 0; i < argSize; ++i) {
						if (visit(arguments.get(i)) == params.get(i).getType().intern())
							continue;
						
						errors.add(new TypeCheckError(arguments.get(i), "Type mismatch: cannot convert from " + arguments.get(i).getNodeType() + " to " + params.get(i).getType()));
						return VOID;
					}
					List<TypeNode> returnParams = subprogram.getReturnTypes();
					for (TypeNode param : returnParams)
						type.add(param.getType().intern());
					node.setNodeType(type);
					return type;
				}
				else {
					String argString = "";
					for (int i = 0; i < argSize; ++i) {
						ExpressionNode arg = arguments.get(i);
						argString += visit(arg);
						if (i+1 < argSize)
							argString += ", ";
					}
					String paramString = "";
					for (int i = 0; i < paramSize; ++i) {
						VarNode param = params.get(i);
						paramString += param.getType();
						if (i+1 < paramSize)
							paramString += ", ";
					}
					if (argSize == 0)
						errors.add(new TypeCheckError(node, "No arguments passed, expected " + paramString));
					else
						errors.add(new TypeCheckError(node, "Invalid argument(s) " + argString + ", expected " + paramString));
					return VOID;
				}
			}
			else {
				errors.add(new TypeCheckError(node, "The method " + node.getIdent() + " is undefined"));
				return VOID;
			}
		}
		catch (Exception ex) {
			errors.add(new TypeCheckError(node, "The method " + node.getIdent() + " is undefined for the type " + currentStructDef));	// TODO this is not relevant yet
			return VOID;
		}
	}

	@Override
	public Object visit(FuncDeclarationNode node) {
		boolean local;
		local = symbolTable.declaredLocally(node.getIdent());
				
		if (local) {
			errors.add(new TypeCheckError(node, "Duplicate function " + node.getIdent()));
			return VOID;
		}
		
		// Add FuncDcl to symbol table
		List<VarNode> params = node.getParamList();
		currentFuncDcl = new STSubprogramEntry(SubprogramType.func,node.getReturnTypes(),params);
		currentFuncDclName = node.getIdent();
		symbolTable.enterSymbol(currentFuncDclName, currentFuncDcl); 
		symbolTable.openScope();
		for(VarNode param : params)
			symbolTable.enterSymbol(param.getIdent(), new STTypeEntry(param.getType().intern()));
		List<StatementNode> input = node.getStatements();
		for(StatementNode i : input)
			visit(i);
		
		symbolTable.closeScope();
		currentFuncDcl = null;
		currentFuncDclName = null;
		node.setNodeType(VOID);
		return VOID;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object visit(GeneralIdentNode node) {
		List<BaseIdentNode> idents = node.getIdents();
		Object type;
		for (int i = 0; i < idents.size(); ++i) {
			BaseIdentNode ident = idents.get(i);
			
			if (ident instanceof FuncCallNode){
				Object temp = visit((FuncCallNode) ident);
				type = temp;
			}
			else
				type = visit(ident);
			
			// TODO "The primitive type " + type + " of " + ident + " does not have a field " + ident2;
			// "Cannot invoke " + methodName + " on the primitive type " + type;
			
			// Set currentStruct to null before returning
			
			if (i+1 < idents.size()) {
				if (type == NUM || type == BOOL || type == TEXT || type == VOID) {
					currentStructDef = null;
					errors.add(new TypeCheckError(ident, "The primitive type " + type + " of " + ident.getIdent() + " does not have a field " + idents.get(i+1).getIdent()));
					return VOID;
				}
			} 
			else {
				currentStructDef = null;
				node.setNodeType(type);
				return type;
			}
		}
		throw new RuntimeException("GeneralIdentNode " + node + " is empty");
	}

	@Override
	public Object visit(IfNode node) {
		
		Object exprType = visit(node.getExpression());
		if (exprType != BOOL)
			errors.add(new TypeCheckError(node, "Type mismatch: cannot convert from " + exprType + " to boolean"));
		else
			node.setNodeType(VOID);
		
		List<StatementNode> stmts = node.getIfBlockStatements();
		for (StatementNode stmt : stmts)
			visit(stmt);
		
		switch (node.getClass().getName()) {
			case "nodes.IfNode":
				return VOID;
			case "nodes.IfElseNode":
				stmts = ((IfElseNode) node).getElseBlockStatements();
				for (StatementNode stmt : stmts)
					visit(stmt);
				return VOID;
			case "nodes.ElseIfNode":
				visit(((ElseIfNode) node).getNext());
				return VOID;
			default:
				throw new NotImplementedException();
		}	
	}

	@Override
	public Object visit(IterationNode node) {
		Object nodeType = node.getNodeType();
		if(nodeType instanceof ForNode){
			visit((ForNode)nodeType);
		}
		else if(nodeType instanceof WhileNode){
			visit((WhileNode)nodeType);
		}
		return VOID;
	}

	@Override
	public Object visit(LogicalANDExprNode node) {
		Object leftType = visit(node.getLeftChild());
		Object rightType = visit(node.getRightChild());
		if(leftType == BOOL && rightType == BOOL){
			node.setNodeType(BOOL);
			return BOOL;
		}
		errors.add(new TypeCheckError(node, "The operator " + node.getNodeType() + " is undefined for the argument type(s) " + leftType + ", " + rightType));
		return BOOL;
	}

	@Override
	public Object visit(LogicalORExprNode node) {
		Object leftType = visit(node.getLeftChild());
		Object rightType = visit(node.getRightChild());
		if(leftType == BOOL && rightType == BOOL){
			node.setNodeType(BOOL);
			return BOOL;
		}
		errors.add(new TypeCheckError(node, "The operator " + node.getNodeType() + " is undefined for the argument type(s) " + leftType + ", " + rightType));
		return BOOL;
	}

	@Override
	public Object visit(MultExprNode node) {
		Object leftType = visit(node.getLeftChild());
		Object rightType = visit(node.getRightChild());
		if (leftType == NUM && rightType == NUM)  {
			node.setNodeType(NUM);
			return NUM;
		}
		
		errors.add(new TypeCheckError(node, "The operator " + node.getType() + " is undefined for the argument type(s) " + leftType + ", " + rightType));
		return NUM;
	}

	@Override
	public Object visit(NumLiteralNode node) {
		node.setNodeType(NUM);
		return NUM;
	}

	@Override
	public Object visit(ParenthesesNode node) {
		Object type = visit(node.getChild());
		node.setNodeType(type);
		return type;
	}

	@Override
	public Object visit(PrimaryExprNode node) {
		Object type;
		switch (node.getClass().getName()) {
			case "nodes.GeneralIdentNode":
				type = visit ((GeneralIdentNode) node);
				node.setNodeType(type);
				return type;
			case "nodes.TextLiteralNode":
				return visit ((TextLiteralNode) node);
			case "nodes.NumLiteralNode":
				return visit ((NumLiteralNode) node);
			case "nodes.BoolLiteralNode":
				return visit((BoolLiteralNode) node);
			case "nodes.ParenthesesNode":
				type = visit(((ParenthesesNode) node).getChild());
				node.setNodeType(type);
				return type;
			default:
				throw new NotImplementedException();
		}
	}

	@Override
	public Object visit(ProgramNode node) {		
		System.out.println("Typechecking program: ");
		List<DeclarationNode> declarations = node.getDeclarations();
		for(int i = 0; i < declarations.size(); ++i)
		    visit(declarations.get(i));
		
		node.setNodeType(VOID);
		return VOID;
	}

	@Override
	public Object visit(RelationExprNode node) {
		Object leftType = visit(node.getLeftChild());
		Object rightType = visit(node.getRightChild());
		if(leftType == NUM && rightType == NUM){
			node.setNodeType(BOOL);
			return BOOL;
		}
		errors.add(new TypeCheckError(node, "The operator " + node.getNodeType() + " is undefined for the argument type(s) " + leftType + ", " + rightType));
		return BOOL;
	}
	
	@Override
	public Object visit(ReturnNode node) {
		// Does it return the correct values for the func?
		STSubprogramEntry entry = currentFuncDcl;
		
		// TODO check if actually part of func
		
		// Get return parameters
		List<TypeNode> returnParams = entry.getReturnTypes();
		
		// Evaluate expressions to get return types and compare them
		List<ExpressionNode> returnTypes = node.getExpressions();
		int returnTypeSize = returnTypes.size();
		int returnParamsSize = returnParams.size();
		if(returnParamsSize == returnTypeSize){
			for (int i = 0; i < returnTypeSize; ++i) {
				Object current = visit(returnTypes.get(i));

				if (current == returnParams.get(i).getType().intern())
					continue;
				else {
					errors.add(new TypeCheckError(returnTypes.get(i), "Type mismatch: cannot convert from " + current + " to " + returnParams.get(i).getType()));
					return VOID;
				}
			}
		}
		else if(returnTypeSize > returnParamsSize){
			returnTypeErrorListCreater(returnParamsSize, returnTypeSize, returnParams, node,"many");
		}
		else if(returnTypeSize < returnParamsSize){
			returnTypeErrorListCreater(returnParamsSize, returnTypeSize, returnParams, node,"few");
		}
		node.setNodeType(VOID);
		return VOID;
	}
	
	private void returnTypeErrorListCreater(int returnParamsSize, int returnTypeSize, List<TypeNode> returnParams, ReturnNode node, String word){
		String paramString = "";
		for (int j = 0; j < returnParamsSize; ++j) {
			paramString += returnParams.get(j).getType();
			if (j+1 < returnParamsSize)
				paramString += ", ";
		}
		errors.add(new TypeCheckError(node, "Method "+ currentFuncDclName + " returns too "+ word +" arguments(" + returnTypeSize + "). Expected " + paramString));
	
	}
	

	@Override
	public Object visit(RobotDeclarationNode node) {
		switch (node.getType()) {
			case initialization:
				for (int i = 0; i < node.getStatements().size(); ++i)
					visit(node.getStatements().get(i));
				node.setNodeType(VOID);
				return VOID;
			case behavior:
				symbolTable.openScope();
				for (int i = 0; i < node.getStatements().size(); ++i)
					visit(node.getStatements().get(i));
				symbolTable.closeScope();
			case name:
				node.setNodeType(VOID);
				return VOID;
			default:
				throw new NotImplementedException();
		}
	}

	@Override
	public Object visit(StatementNode node) {
		if (node instanceof VarDeclarationNode)
			return visit((VarDeclarationNode) node);
		else if (node instanceof ArrayDeclarationNode)
			return visit((ArrayDeclarationNode) node);
		else if (node instanceof DataStructDeclarationNode)
			return visit((DataStructDeclarationNode) node);
		else if (node instanceof AssignmentNode)
			return visit((AssignmentNode) node);
		else if (node instanceof CallStatementNode)
			return visit((CallStatementNode) node);
		else if (node instanceof IfNode)
			return visit((IfNode) node);
		else if (node instanceof IterationNode)
			return visit((IterationNode) node);
		else if (node instanceof ReturnNode)
			return visit((ReturnNode) node);
		throw new NotImplementedException();
	}

	@Override
	public Object visit(TextLiteralNode node) {
		node.setNodeType(TEXT);
		return TEXT;
	}

	@Override
	public Object visit(TypeNode node) {
		// Add to func's list of return types?
		Object type = node.getType().intern();
		node.setNodeType(type);
		return type;
	}

	@Override
	public Object visit(UnaryExprNode node) {
		Object child = visit(node.getChild());
		if (node.getType() == UnaryExprNode.UnaryType.negation) {
			if (child == NUM)
				node.setNodeType(NUM);
			else
				errors.add(new TypeCheckError(node, "The operator - is undefined for the argument type(s) " + child));
			return NUM;
		}
		else if (node.getType() == UnaryExprNode.UnaryType.not) {
			if (child == BOOL)
				node.setNodeType(BOOL);
			else
				errors.add(new TypeCheckError(node, "The operator ! is undefined for the argument type(s) " + child));
			return BOOL;
		}
		
		
		return NUM;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visit(VarDeclarationNode node) {
		List<VarNode> input = node.getVariable();
		
		// Evaluate Expression
		List<Object> rhsType = new ArrayList<Object>();
		Object temp = visit(node.getExpression());
		if (temp instanceof List<?>)
			rhsType.addAll((List<Object>) temp);
		else
			rhsType.add(temp);
		
		int inputSize = input.size();
		int rhsSize = rhsType.size();
		if (inputSize == rhsSize) {
			for(int i = 0; i < inputSize; ++i) {
				VarNode var = input.get(i);
				// Check if the symbol has already been defined in this scope
				boolean local;
				try {
					local = symbolTable.declaredLocally(var.getIdent());
				}
				catch (Exception ex) {
					local = false;
				}
			
				if (local) {
					errors.add(new TypeCheckError(node, "Duplicate local variable " + var.getIdent()));
					return VOID;
				}
				
				Object varType = var.getType().intern();
				
				// Add variable to symbol table
				if(currentStructDef == null)
					symbolTable.enterSymbol(var.getIdent(), new STTypeEntry(varType));
				else
					currentStructDef.getVariables().enterSymbol(var.getIdent(), new STTypeEntry(varType));
				if (varType == rhsType.get(i))	// TODO support multiple return
					continue;
				
				errors.add(new TypeCheckError(node, "Cannot assign variable of type " + varType + " a value of type " + rhsType.get(i)));
				return VOID;
			}
		} else {
			errors.add(new TypeCheckError(node, "Lefthand side is of size "+ inputSize +" and righthand side of size "+ rhsSize));
		}
		node.setNodeType(VOID);
		return VOID;
		
	}

	@Override
	public Object visit(VarNode node) {
		return node.getType().intern();
	}
	
	@Override
	public Object visit(WhileNode node) {
		List<ExpressionNode> input = node.getExpressions();
		Object exprType = visit(input.get(0));
		if (exprType != BOOL)
			errors.add(new TypeCheckError(node, "Type mismatch: cannot convert from " + exprType + " to boolean"));
		else
			node.setNodeType(VOID);
		
		List<StatementNode> stms = node.getStatements();
		for(StatementNode stm : stms){
			visit(stm);
		}
		return VOID;
	}

}
