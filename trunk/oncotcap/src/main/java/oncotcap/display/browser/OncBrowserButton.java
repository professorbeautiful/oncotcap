package oncotcap.display.browser;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import javax.swing.*;
import java.lang.reflect.Array;
import javax.swing.event.*;

public class OncBrowserButton extends JButton {
		private Object parentComponent = null;
		public OncBrowserButton(ImageIcon icon) {
					super(icon);
		}
		public OncBrowserButton(Action action) {
					super(action);
		}
		public OncBrowserButton(Object par,
														Action action) {
					super(action);
					setParentComponent(par);
		}
		public OncBrowserButton(Object par) {
					super();
					setParentComponent(par);
		}
		public void setParentComponent(Object par) {
				parentComponent = par;
		}

		public Object getParentComponent() {
				return parentComponent;
		}
		
}
