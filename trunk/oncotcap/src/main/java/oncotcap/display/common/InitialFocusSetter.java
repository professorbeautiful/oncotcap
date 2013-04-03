package oncotcap.display.common;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.*;
import java.lang.reflect.*;

public class InitialFocusSetter {
		public static void setInitialFocus(Window w, Component c) {
				w.addWindowListener(new FocusSetter(c));
		}
    
		public static class FocusSetter extends WindowAdapter {
				Component initComp;
				FocusSetter(Component c) {
						initComp = c;
				}
				public void windowOpened(WindowEvent e) {
						initComp.requestFocus();
						
						// Since this listener is no longer needed, remove it
						e.getWindow().removeWindowListener(this);
				}
		}
}
