/*
 * Educational Resource for Tumor Heterogeneity
 *             Oncology Thinking Cap
 *
 * Copyright (c) 2003  University of Pittsburgh
 * All rights reserved.
 *
 *  SourceSafe Info:
 *               $Header: $
 *               Revision: $Revision$
 *               Author: $Author$
 *
 * Code Review History:
 *     (mm.dd.yyyy initials)
 *
 * Test History:
 *     (mm.dd.yyyy initials)
 */
package oncotcap.display.editor.persistibleeditorpanel;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.*;


import java.lang.reflect.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.util.*;
import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.storage.clips.*;

public class BooleanTreeSelectionListener extends MouseAdapter
		implements TreeSelectionListener {

		/** Used to make sure the paths are unique, will contain all the paths
     * in <code>selection</code>.
     */
    private Vector                       selectedPaths;
		private DragDropLabel dragLabel = new DragDropLabel();
		private boolean doubleClickEdit = false;

		static Dimension defaultSize = new Dimension(700,650);
		

		public BooleanTreeSelectionListener() {
				selectedPaths = new Vector();
		}


		public void allowDoubleClickEditing(boolean allowDoubleClickEditing) {
				doubleClickEdit = allowDoubleClickEditing;
		}

    public void valueChanged(TreeSelectionEvent e) {
				BooleanTree tree = (BooleanTree)e.getSource();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                           ((JTree)e.getSource()).getLastSelectedPathComponent();
// 				System.out.println("valueChanged getLastSelectedPathComponent " + node);
				// Get all the paths that have been add to the selection list
				TreePath paths [] = e.getPaths();
				
				// Loop through each path and see if it was already selected if so deselect
				// it
				// Update the selected Paths list as you go
				// int i = 0;
// 				for ( i = 0;i < Array.getLength(paths); i++) {
// 						if ( selectedPaths.contains(paths[i]) )
// 								selectPath(paths[i], false);
// 						else
// 								selectPath(paths[i], true);
// 					// 	System.out.println("tree.setLastSelectedPath(paths[i] " 
// // 															 + paths[i]);
// 						tree.setLastSelectedPath(paths[i]);

// 				}
     //    if (node == null) return;

//         Object nodeInfo = node.getUserObject();
// //  				System.out.println("New node selected: " + nodeInfo);
// 				tree.fireTreePanelEvent(new TreePanelEvent(nodeInfo, tree));
    }

		public void mousePressed(MouseEvent e)
		{
// 				System.out.println("mousePressed " + e.getButton());
				BooleanTree tree = (BooleanTree) e.getSource();
			
			// 	System.out.println("WHAT IS SELECTED " + tree.getSelectionPath());
// 				System.out.println("WHERE IS SELECTED " + e.getX() + " " + e.getY());
// 				System.out.println("WHAT AT LOCATION " + 
// 													 tree.getPathForLocation(e.getX(), e.getY()));
				TreePath selectionTreePath = tree.getPathForLocation(e.getX(), e.getY());
				if ( selectionTreePath == null )
						return;
				tree.setSelectionPath( selectionTreePath );
				OncTreeNode dragTarget = 
						(OncTreeNode) selectionTreePath.getLastPathComponent();
				if (dragTarget != null 
						&& dragTarget.getUserObject() instanceof Droppable) {
						//System.out.println("THIS IS DRAGGABLE"  );
						dragLabel.setTarget((Droppable) dragTarget.getUserObject());
						TransferHandler handler = tree.getTransferHandler();
						if(handler != null)
								handler.exportAsDrag(tree, e, TransferHandler.COPY);
				}		
				if ( e.isPopupTrigger() || e.getButton() == 3) {
						// tree.getPopup().show( (JComponent)e.getSource(), 
// 																	e.getX(), e.getY() );
				}
				if (e.getClickCount() == 2 && doubleClickEdit == true) {
						// select item
						TreePath treePath = 
								((JTree)e.getSource()).getPathForLocation(e.getX(), e.getY());
						Object obj = treePath.getLastPathComponent();
// 						System.out.println("lastPathComponent " + obj + obj.getClass());
// 						System.out.println("tree " + tree);
						Object userObject = null;
						if ( obj instanceof OncTreeNode ) 
								userObject = ((OncTreeNode)obj).getUserObject();
//  						System.out.println("userObject " + userObject);
						if ( userObject instanceof Editable) {
 								EditorPanel editorPanel =
										EditorFrame.showEditor((Editable)userObject, defaultSize);
								if ( userObject instanceof Persistible) 
										((Persistible)userObject).addSaveListener(editorPanel);
						}
						else {
								JOptionPane.showConfirmDialog
										(null, 
										 "There is no editor for the selected object.", 
										 "information",
										 JOptionPane.OK_OPTION, 
										 JOptionPane.INFORMATION_MESSAGE);
						}
				}
				else {
						if ( tree.getSelectionPath() == null)
								return;
						/// /OncTreeNode dragTarget = 
// 								(OncTreeNode)tree.getSelectionPath().getLastPathComponent();
						Object dragObj = dragTarget.getUserObject();
// 						System.out.println("dragTarget " + dragObj);

							
				}
		}

		public void mouseClicked(MouseEvent e) {
				// 				System.out.println("mouseClicked");
		}

		public void mouseReleased( MouseEvent e ) {
		} 

		// Returns a TreePath containing the specified node.
    public TreePath getPath(TreeNode node) {
        java.util.List list = new ArrayList();
    
        // Add all nodes to list
        while (node != null) {
            list.add(node);
            node = node.getParent();
        }
        Collections.reverse(list);
    
        // Convert array of nodes to TreePath
        return new TreePath(list.toArray());
    }

		private void selectPath(TreePath path, boolean setSelected) {
// 				System.out.println("selectPath " + path + " " + setSelected);
				if ( setSelected == true ) 
						selectedPaths.addElement(path);
				else
						selectedPaths.removeElement(path);
				
		}
}

