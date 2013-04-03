package oncotcap.display.editor.persistibleeditorpanel;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import javax.swing.*;
import javax.swing.tree.*;

import oncotcap.display.browser.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.editor.EditorFrame;


@SuppressWarnings("serial")
public class AddLevelListAction extends OncAbstractAction {
		GenericTree tree = null;
		Object source = null;

		public AddLevelListAction(String actionName) {
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
				//System.out.println("getBrowserActionSource " + e);
				//get selected item
				if ( source instanceof GenericTree ) {
						Object obj = ((GenericTree)source).getLastSelectedPathComponent();
						if ( obj != null && obj instanceof DefaultMutableTreeNode &&
								 ((DefaultMutableTreeNode)obj).getUserObject() instanceof Keyword) {
								showLevelListEditor(
													(Keyword)((DefaultMutableTreeNode)obj).getUserObject());
						}
				}
				else if ( source instanceof Keyword ) {
						showLevelListEditor((Keyword)source);
				}
				else {
						JOptionPane.showMessageDialog
								(null, 
								 "Please select a Keyword to associate with new Level List.");
				}
		}
		protected void showLevelListEditor(Keyword keyword) {
				// Get the editor panel and set ending keyword to
				// control the keyword list in the level list editor
				EnumLevelList ll = new EnumLevelList();
				ll.setKeyword(keyword);
				LevelListEditorPanel llep = 
						(LevelListEditorPanel)ll.getEditorPanelWithInstance();
				llep.setEndingKeyword(keyword);
								
				EditorFrame.showEditor((Editable)ll,
							 new Dimension(400,600));
				//ll.addSaveListener(this);
		}

}
