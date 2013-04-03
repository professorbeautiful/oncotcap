package oncotcap.display.browser;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import javax.swing.*;
import java.lang.reflect.Array;
import javax.swing.event.*;
import oncotcap.display.common.OncFindPanel;

public class FindAction extends OncAbstractAction {
		OncFindPanel p = null;
		GenericTree tree = null;
		JDialog f = null;
		static ImageIcon findIcon = oncotcap.util.OncoTcapIcons.getImageIcon
				("find-small.jpg");

		public FindAction(String actionName) {
				super(actionName, OncBrowserConstants.findIcon);
		}
		public void setTree(GenericTree tree) {
				this.tree = tree;
		}
		public GenericTree getTree() {
				return this.tree;
		}
		public void setModal(boolean b) {
				f.setModal(b);
		}

		public void actionPerformed(ActionEvent e) {
				p = new OncFindPanel();
				f = new JDialog();
				if ( getBrowserActionSource(e) instanceof OntologyTree)
						p.setSearchComponent(((OntologyTree)getBrowserActionSource(e)).getTree());
				else if ( getBrowserActionSource(e) instanceof OncBrowser)
					p.setSearchComponent(((OncBrowser)getBrowserActionSource(e))
							.ontologyTreeWithFocus());
				else
						p.setSearchComponent((Component)(getBrowserActionSource(e)));
				if ( getBrowserActionSource(e) instanceof OntologyTree) 
						setModal(((OntologyTree)getBrowserActionSource(e)).getModal());
				f.getContentPane().add(p);
				//f.setIconImage(findIcon);
				f.setTitle("Find item in " + getBrowserActionSource(e).toString());
				f.setSize(400,100);
				Component parentPanel = p.getSearchComponent().getParent().getParent().getParent().getParent().getParent().getParent();
				f.setLocation(parentPanel.getLocationOnScreen());
				f.setVisible(true);
		}
				
}
