package oncotcap.display.common;

import java.awt.event.*;

public class DefaultFocusListener implements InputMethodListener {
		public void inputMethodTextChanged(InputMethodEvent event){
				//System.out.println("Text changed" + event);
		}
		public void caretPositionChanged(InputMethodEvent event){
				//System.out.println("Caret position changed" + event);
		}
}
