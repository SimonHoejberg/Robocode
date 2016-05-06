import java.util.BitSet;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
public class BtrErrorListner implements ANTLRErrorListener {

	private String errors = "";
	
	public String getErrors(){
		return errors;
	}

	@Override
	public void reportAmbiguity(Parser arg0, DFA arg1, int arg2, int arg3,
			boolean arg4, BitSet arg5, ATNConfigSet arg6) {
		
		
	}

	@Override
	public void reportAttemptingFullContext(Parser arg0, DFA arg1, int arg2,
			int arg3, BitSet arg4, ATNConfigSet arg5) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reportContextSensitivity(Parser arg0, DFA arg1, int arg2,
			int arg3, int arg4, ATNConfigSet arg5) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void syntaxError(Recognizer<?, ?> arg0, Object arg1, int arg2,
			int arg3, String arg4, RecognitionException arg5) {
		errors += "line "+ arg2 +":"+ arg3 +" "+ arg4 +"\n";
		
	}

}
