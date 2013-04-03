package oncotcap.display.editor.persistibleeditorpanel;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;
import java.lang.reflect.Array;
import javax.swing.event.*;

import oncotcap.display.browser.*;
import oncotcap.display.common.*;
import oncotcap.datalayer.Persistible;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.display.editor.EditorFrame;

import oncotcap.datalayer.persistible.*;

public class ShowLevelListAction extends OncAbstractAction {
	

		GenericTree tree = null;
		Object source = null;

		public ShowLevelListAction(String actionName) {
				super(actionName);
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

		public void actionPerformed(ActionEvent e) {
				Object source = getBrowserActionSource(e);
				System.out.println("getBrowserActionSource " + e);
				//get selected item
				if ( source instanceof GenericTree ) {
						Object obj = ((GenericTree)source).getLastSelectedPathComponent();
						Object userObject = null;
						if ( obj != null && obj instanceof DefaultMutableTreeNode &&
								 ((DefaultMutableTreeNode)obj).getUserObject() instanceof Keyword) {
								showLevelLists(
													(Keyword)((DefaultMutableTreeNode)obj).getUserObject());
						}
				}
				else if ( source instanceof Keyword ) {
						showLevelLists((Keyword)source);
				}
				else {
						JOptionPane.showMessageDialog
								(null, 
								 "Please select a Keyword to associate with new Level List.");
				}
				
		}
		protected void showLevelLists(Keyword keyword) {
				// Get the editor panel and set ending keywboard to
				// control thekeyword list in the level list editor
				Vector levelLists = keyword.getAssociatedLevelLists();
				Iterator i = levelLists.iterator();
				OncScrollList levelListPanel = 
						new OncScrollList(levelLists, "Level Lists", false, true);
				JFrame levelListFrame = new JFrame("Available Level Lists for " + 
																					 keyword);
				levelListFrame.add(levelListPanel);
				levelListFrame.setSize(400, 400);
				levelListFrame.setVisible(true);
				
		}



}
