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
import oncotcap.display.editor.persistibleeditorpanel.BooleanTree;
import oncotcap.util.*;
import java.awt.dnd.*;

public class RemoveNode extends OncAbstractAction {
		public static final int REMOVE_FROM_DISPLAY = 1;
		public static final int DELETE_PERSISTIBLE = 2;
		public static final int CANCEL = 0;
		int deleteStatus = REMOVE_FROM_DISPLAY;
		int deleteMode = DELETE_PERSISTIBLE;
			 
		GenericTree tree = null;
		Object source = null;
		public RemoveNode() {
				super("Remove");
				
		}
		public void actionPerformed(ActionEvent e) {
				setSource(getBrowserActionSource(e));
				setTree(getBrowserActionTree(getSource()));
				if ( getSource() instanceof OncScrollList ) {
						//setDeleteStatus(getSource());
						OncScrollList list = (OncScrollList)getSource();
						deleteMode = list.getDeleteMode();
						askQuestion();
						// Get selected elements
						JList jlist = list.getList();
						Object[] selectedValues = jlist.getSelectedValues();
						deleteItems(selectedValues);
				}
				else if ( getSource() instanceof OntologyTree ) {
						askQuestion();
						//setDeleteStatus(getSource());
						Vector selectedItems = null;
						selectedItems = tree.getSelected();
						deleteItems(selectedItems.toArray());
				}
			// 	else if ( getSource() instanceof FilterEditorPanel ) {
// 						Vector selectedItems = null;
// 						selectedItems = 
// 								((FilterEditorPanel)getSource).getTree().getSelected();
// 						deleteItems(selectedItems.toArray());
// 				}
				else if ( getSource() instanceof OncScrollTable) {
						askQuestion();
						//setDeleteStatus(getSource());
						Vector selectedValues = 
								((OncScrollTable)getSource()).getSelectedItems();
						deleteItems(selectedValues.toArray());
						((OncScrollTable)getSource()).deleteSelected();
				}
				// If the source is the FilterEditorPanel - 
				// ignore it and let the boolean tree handle
				OncBrowser.refresh();
		}

		public void setDeleteMode(int mode) {
				deleteMode = mode;
		}
		private void askQuestion() {
				//System.out.println("What is delet MOde " + deleteMode);
				if ( deleteMode == DELETE_PERSISTIBLE ) 
						setDeleteStatus();
				else 
						deleteStatus = REMOVE_FROM_DISPLAY;
		}
		
		private void setDeleteStatus() {
				//oncotcap.util.ForceStackTrace.showStackTrace();
				//deleteStatus = REMOVE_FROM_DISPLAY;
				int deleteAnswer = 1;
				deleteAnswer = JOptionPane.showConfirmDialog
						((JFrame)null, 
						 "Are you sure you want to delete.", 
						 "Confirmation",
						 JOptionPane.YES_NO_OPTION, 
						 JOptionPane.INFORMATION_MESSAGE);
				if ( deleteAnswer == JOptionPane.NO_OPTION ||
						 deleteAnswer == JOptionPane.CLOSED_OPTION)
						deleteStatus = CANCEL;
				else
						deleteStatus = DELETE_PERSISTIBLE;
		}

		private void deleteItems(Object[] selectedItems) {
				for ( int i = 0; i < Array.getLength(selectedItems); i++) {
						if ( selectedItems[i] instanceof Persistible ) {
								if ( deleteStatus == DELETE_PERSISTIBLE )
										((Persistible)selectedItems[i]).delete();
								if ( getTree() != null ) 
										getTree().removeNode((Persistible)selectedItems[i]);
								if ( getSource() instanceof OncScrollList) {
										OncScrollList list = (OncScrollList)getSource();
										list.removeValue(selectedItems[i]);
								}
										
						}
				}
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
