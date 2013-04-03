package oncotcap.display.browser;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;
import java.lang.reflect.Array;
import javax.swing.event.*;

import oncotcap.datalayer.Persistible;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.display.editor.EditorFrame;


public class ShowEditorAction extends OncAbstractAction {
		GenericTree tree = null;
		Object source = null;

		public ShowEditorAction(String actionName) {
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
			try {
				setSource(getBrowserActionSource(e));
				setTree(getBrowserActionTree(getSource()));
				//get selected item
				if ( e.getSource() instanceof GenericTree 
						 && getTree() != null && 
						 !getTree().allowsDoubleClickEditing())
						return;
				Object obj = tree.getLastSelectedPathComponent();
				Object userObject = null;
				if ( obj instanceof DefaultMutableTreeNode ) 
						userObject = ((DefaultMutableTreeNode)obj).getUserObject();
				// System.out.println("userObject " + userObject);
				Class cls = 
						((DefaultMutableTreeNode)obj).getUserObject().getClass();

				if ( userObject instanceof Editable) {
						if ( oncotcap.Oncotcap.getDataSourceMode() == false){
								OncMergeBrowser.showUserObjects(userObject);
						}
						else {
								EditorPanel editorPanel =
										EditorFrame.showEditor((Editable)userObject, 
																					 null,
																					 50, 50, tree);
								if ( userObject instanceof Persistible ) {
										((Persistible)userObject).addSaveListener(editorPanel);
										
								}
						}
				}
				else {
						JOptionPane.showMessageDialog
								(null, 
								 "There is no editor for the selected object.");
				}
			}
			catch (NullPointerException npe){
				System.err.println("ShowEditorAction.actionPerformed: Null Pointer Exception");
			}
		}
}
