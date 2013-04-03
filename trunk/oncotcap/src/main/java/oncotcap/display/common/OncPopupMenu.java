package oncotcap.display.common;

import javax.swing.*;

public class OncPopupMenu extends JPopupMenu {
		Object currentObject = null;
		Object context = null;
		public OncPopupMenu() {
			super();
		}
		public OncPopupMenu(String title) {
			super(title);
		}
		public void setCurrentObject(Object obj) {
				currentObject = obj;
		}
		public Object getCurrentObject() {
				return currentObject;
		}
		public Object getContext() {
			return context;
		}
		public void setContext(Object context) {
			this.context = context;
		}
}
