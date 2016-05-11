import java.util.List;

import exceptions.NotImplementedException;

public class OutputCommands {
	
	public static String parseCommand(String ident, List<String> args) {
		switch(ident) {
			case "print":
				return "out.print(\"" + args.get(0) + "\")";
			case "printLine":
				return "out.println(\"" + args.get(0) + "\")";
			default:
				throw new NotImplementedException();
		}
	}
}
