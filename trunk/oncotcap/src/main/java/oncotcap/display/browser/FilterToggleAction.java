package oncotcap.display.browser;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import javax.swing.*;
import java.lang.reflect.Array;
import javax.swing.event.*;

import oncotcap.display.browser.OntologyMap;
import oncotcap.display.common.*;
import oncotcap.util.*;


/// Inner class
public class FilterToggleAction extends AbstractAction {

		GenericTree tree = null;
		Object source = null;
		OntologyButtonPanel ontologyButtonPanel = null;

		public FilterToggleAction() {
			
		}

		public FilterToggleAction(String actionName) {
				super(actionName, null);
				// Set an accelerator key; this value is used by menu items
				putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control F2"));
		}
	
		public void actionPerformed(ActionEvent e) {
					OncBrowser.refresh();
		}

		public void setTree(GenericTree tree) {
				this.tree = tree;
		}
		public GenericTree getTree() {
				return this.tree;
		}
		public void setSource(Object source) {
				this.source = source;
		}
		public Object getSource() {
				return this.source;
		}

		public void setSourceParent(Object source) {
				this.source = source;
		}
		public Object getSourceParent() {
				return this.source;
		}
		public void setOntologyButtonPanel(OntologyButtonPanel ontologyButtonPanel) {
				this.ontologyButtonPanel = ontologyButtonPanel;
		}
		public OntologyButtonPanel getOntologyButtonPanel() {
				return ontologyButtonPanel;
		}


}
