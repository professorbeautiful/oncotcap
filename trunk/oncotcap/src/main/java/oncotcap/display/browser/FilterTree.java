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
import oncotcap.display.browser.*;
import oncotcap.util.*;
import java.awt.dnd.*;

public class FilterTree extends AbstractAction {
	
		GenericTree tree = null;
		Object source = null;
		public FilterTree() {
				super(null);
				
		}
		public void actionPerformed(ActionEvent e) {
				// Show OntologyTree (panel)
				JFrame f = new JFrame();
				OntologyTree p = new OntologyTree();
				f.getContentPane().add(p);
				f.setSize(400,200);
				f.setVisible(true);
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

}
