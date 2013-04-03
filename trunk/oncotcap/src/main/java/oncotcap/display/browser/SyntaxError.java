package oncotcap.display.browser;
import oncotcap.engine.*;
import oncotcap.util.*;

public class SyntaxError extends Object {
		String errorString = null;
		String theString = null;
		PreprocessedCodeBundle ppcb = null;
		public SyntaxError(PreprocessedCodeBundle cb, String theString,
											 String errorString) {
				ppcb = cb;
				this.theString = theString;
				this.errorString = errorString;
		}

		public String toString() {
				return "Syntax error in code bundle " + ppcb.toString() 
						+ " ===>> " + theString;
		}
}

