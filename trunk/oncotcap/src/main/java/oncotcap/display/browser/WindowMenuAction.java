package oncotcap.display.browser;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.lang.reflect.*;
import javax.swing.ImageIcon;

import oncotcap.util.*;

public class WindowMenuAction extends AbstractAction {
		Window window = null;
		String myName = null;
		public WindowMenuAction(String name, ImageIcon icon) {
				super(name, icon);
				myName = name;
		}
		public void setWindow(Window win) {
				window = win;
		}
		public Window getWindow() {
				return window;
		}
		public String toString() {
				return "My Name Is " + myName;
		}
		public void actionPerformed(ActionEvent e) {
				// Show the window
				if (window instanceof JFrame 
						&& ((JFrame)window).getExtendedState() == Frame.ICONIFIED) {
						((JFrame)window).setExtendedState(Frame.NORMAL);
				}
				window.setVisible(true);
				window.show();
				window.toFront();
				window.repaint();
		}
		
}
