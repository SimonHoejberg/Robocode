
public class DocTypeConverter {
	
	public static String Convert(Object type){
		switch (type.toString()) {
		case "int":
		case "double":
		case "long":
			return "num".intern();
		case "string":
			return "text".intern();
		case "boolean":
			return "bool".intern();
		default:
			return type.toString().intern();
		}
	}

}
