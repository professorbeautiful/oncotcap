package oncotcap.display.browser;
import java.util.*;

import java.awt.event.*;
import javax.swing.event.*;

public class TreePanelEvent extends EventObject {
		GenericTree tree = null;
		public TreePanelEvent(Object source, GenericTree tree) {
				super(source);
				this.tree = tree;
		}
		public GenericTree getTree() {
				return tree;
		}
}
