import java.util.List;

import exceptions.NotImplementedException;

public class MathCommands {
	
	public static String parseCommand(String ident, List<String> args) {
		switch(ident) {
			case "getRandomNumber":
				return "(Math.random() * " + args.get(0) + ")";
			case "getRandomNumberBetween":
				return "(Math.random() * (" + args.get(1) + " - " + args.get(0) + ") + " + args.get(0) + ")";
			case "pow":
				return "Math.pow(" + args.get(0) + ", " + args.get(1) + ")";
			case "sqrt":
				return "Math.sqrt(" + args.get(0) + ")";
			case "floor":
				return "Math.floor(" + args.get(0) + ")";
			case "ceil":
				return "Math.ceil(" + args.get(0) + ")";
			default:
				throw new NotImplementedException();
		}
	}
}
