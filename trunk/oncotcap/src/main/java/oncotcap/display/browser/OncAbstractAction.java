package oncotcap.display.browser;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import javax.swing.*;
import java.lang.reflect.Array;

import javax.swing.event.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import oncotcap.datalayer.persistible.CodeBundle;

import oncotcap.display.common.*;
import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.ListDialog;
import oncotcap.display.editor.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.util.*;
import java.awt.dnd.*;


public abstract class OncAbstractAction extends AbstractAction {
		public OncAbstractAction(String actionName, Icon icon ) {
					super(actionName, icon);
		}
		public OncAbstractAction(String actionName ) {
					super(actionName);
		}
		public Object getBrowserActionSource(ActionEvent e) {
				//System.out.println("GET BROWSER ACTION SOURCE " + e);
				if ( e.getSource() instanceof GenericTree )
						return ((GenericTree)e.getSource()).getOntologyTree();
				else if (e.getSource() instanceof OntologyToolBar)
						return ((OntologyToolBar)e.getSource()).getOntologyTree();
				else if ( e.getSource() instanceof OncBrowserButton)
						return ((OncBrowserButton)e.getSource()).getParentComponent();
			 	else if ( e.getSource() instanceof JMenuItem) {
						// Find what object this menu is attached to 
						Component comp = 
								ComponentHelper.getParentOfType(((JMenuItem)e.getSource()), 
																								OncPopupMenu.class);
						if ( comp != null ) 
								return ((OncPopupMenu)comp).getCurrentObject();
				}
				return e.getSource();
		}

		public GenericTree getBrowserActionTree(Object obj) {
				if (obj instanceof OntologyTree)
						return ((OntologyTree)obj).getTree();
				else
						return null;
		}

		public OntologyButtonPanel getBrowserActionOBP(Object obj) {
				if (obj instanceof OntologyTree) 
						return ((OntologyTree)obj).getOntologyButtonPanel();
				else 
						return null;
		}

}
