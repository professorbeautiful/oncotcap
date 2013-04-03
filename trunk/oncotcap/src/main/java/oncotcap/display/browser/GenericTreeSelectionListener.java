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
package oncotcap.display.browser;

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

public class GenericTreeSelectionListener extends MouseAdapter
		implements TreeSelectionListener {

		/** Used to make sure the paths are unique, will contain all the paths
     * in <code>selection</code>.
     */
    private Vector                       selectedPaths;
	private DragDropLabel dragLabel = new DragDropLabel();
	private boolean doubleClickEdit = false;

	static Dimension defaultSize = null;
	// Create the listener list
	protected EventListenerList listenerList =
			new EventListenerList();		
	// Create the listener list
	protected EventListenerList singleClickListenerList =
			new EventListenerList();		

	public GenericTreeSelectionListener() {
		selectedPaths = new Vector();
	}

	public void allowDoubleClickEditing(boolean allowDoubleClickEditing) {
		doubleClickEdit = allowDoubleClickEditing;
	}

    public void valueChanged(TreeSelectionEvent e) {
		JTree tree = (JTree)e.getSource();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                           ((JTree)e.getSource()).getLastSelectedPathComponent();

// 				System.out.println("valueChanged getLastSelectedPathComponent " + node);
				// Get all the paths that have been add to the selection list
		TreePath paths [] = e.getPaths();
				
				// Loop through each path and see if it was already selected if so deselect
				// it
				// Update the selected Paths list as you go
		int i = 0;
		for ( i = 0;i < Array.getLength(paths); i++) {
			if ( selectedPaths.contains(paths[i]) )
					selectPath(paths[i], false);
			else
					selectPath(paths[i], true);
		// 	System.out.println("tree.setLastSelectedPath(paths[i] " 
// 															 + paths[i]);
			if ( tree instanceof GenericTree) {
					((GenericTree)tree).setLastSelectedPath(paths[i]);
			}
		}
        if (node == null) return;
			tree.repaint(); // fix up html jlabel rendering font problem
        Object nodeInfo = node.getUserObject();
		if ( tree instanceof GenericTree)
				((GenericTree)tree).setLastSelectedObject(nodeInfo);
		if ( tree instanceof GenericTree ) {
				((GenericTree)tree).fireTreePanelEvent
						(new TreePanelEvent(nodeInfo, (GenericTree)tree));
		}
    }

	public void mousePressed(MouseEvent e)
	{
// 			System.out.println("mousePressed" + e.getButton()
// 													 + " and modifier " + e.getModifiers());
			JTree tree = (JTree) e.getSource();
			TreePath treePath = 
					((JTree)e.getSource()).getPathForLocation(e.getX(), e.getY());
			Object obj = null;
			if ( treePath != null )
					obj = treePath.getLastPathComponent();
// 				if ( e.getButton() == MouseEvent.BUTTON1 
// 						 && (e.getModifiers() == KeyEvent.VK_ALT ||  
// 																		e.getModifiers() == KeyEvent.VK_CONTROL) )
// 						return;
			if ( e.isPopupTrigger() || e.getButton() == MouseEvent.BUTTON3) {
					if ( tree instanceof GenericTree && obj != null) {
							Class cls = 
									((DefaultMutableTreeNode)obj).getUserObject().getClass();
							Object inst = 
									((DefaultMutableTreeNode)obj).getUserObject();
							System.out.println("showing popup " 
																 + cls);
							if ( oncotcap.Oncotcap.getDataSourceMode() == false){
									OncMergeBrowser.showPopup(tree, e.getX(), e.getY());
							}
							else {
									JPopupMenu popup = 
											((GenericTree)tree).getPopup(inst);
									if ( popup != null && popup instanceof OncPopupMenu){
											System.out.println("showing popup");
											((OncPopupMenu)popup).setCurrentObject(inst);
											popup.show(tree, e.getX(), e.getY() );
									}
							}
					}
					return;
			}
			// DOUBLE CLICK
			if (e.getClickCount() == 2 && doubleClickEdit == true) {
						fireDoubleClickEvent(new DoubleClickEvent(tree, e));
			}
			else if (e.getClickCount() == 2 && doubleClickEdit == false ) {
					// Double click and double click editing is not allowed 
					// Do whatever the select and return is for that object
					System.out.println("Do Double Click Action " + e.getSource());
					fireDoubleClickEvent(new DoubleClickEvent(tree, e));

			}
			else {
					if ( tree.getSelectionPath() == null || treePath == null)
							return;
					// otherwise determine drag target / targets based on 
					// how many items 
					// are selected
					setDragTarget(e, tree);
					fireSingleClickEvent(new SingleClickEvent(tree, e));

			}
	}
	
	private void setDragTarget(MouseEvent e, JTree tree) {
			// Get all the currently selected nodes 
			TreePath[] paths = tree.getSelectionPaths();
			//System.out.println("PATHS " + paths.length);
			if ( paths.length > 1 ) {
					// MULTIPLE NODES
					Vector selectedTreeNodes = new Vector();
					for (int j=0; j < paths.length; j++) {
							selectedTreeNodes.add(paths[j].getLastPathComponent()); 
					}
					TransferableList selectedNodes = 
							new TransferableList(selectedTreeNodes);						
					setDragTarget(tree, selectedNodes, e);
			}
			else if ( paths.length == 1 ){
					// SINGLE NODE
					DefaultMutableTreeNode selectedNode =
							(DefaultMutableTreeNode)paths[0].getLastPathComponent(); 
					setDragTarget(tree, selectedNode, e);
			}
	}
	private void setDragTarget(JTree tree, Object dragTarget, MouseEvent e) {
			try { 
					if (dragTarget instanceof Droppable) {
							dragLabel.setTarget((Droppable) dragTarget);
							TransferHandler handler = tree.getTransferHandler();
							if(handler != null) {
									// Determine if this is a copy or a move
									int transferAction = TransferHandler.COPY;
									if ( handler instanceof BooleanTreeTransferHandler ) 
											transferAction = TransferHandler.MOVE;
									else if ( e.getButton() == MouseEvent.BUTTON1 
														&& (e.getModifiers() == KeyEvent.VK_ALT )){
											System.out.println("setDragTarget " + dragTarget);
											transferAction = TransferHandler.MOVE;
									}

									handler.exportAsDrag(dragLabel, e, transferAction);
							}
					}
			}		
			catch ( Exception ee) {
					ee.printStackTrace();
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


   // Add the event registration and notification code to a class.
    
		// This methods allows classes to register for DoubleClickEvents
		public void addDoubleClickListener
				(DoubleClickListener listener) {
				listenerList.add(DoubleClickListener.class, listener);
		}
    
		// This methods allows classes to unregister for DoubleClickEvents
		public void removeDoubleClickListener
				(DoubleClickListener listener) {
				listenerList.remove(DoubleClickListener.class, listener);
		}
		// This private class is used to fire OntologyPanelEvents
		private void fireDoubleClickEvent(DoubleClickEvent evt) {

				Object[] listeners = listenerList.getListenerList();
				// Each listener occupies two elements 
				// - the first is the listener class
				// and the second is the listener instance
				for (int i=0; i<listeners.length; i+=2) {
						if (listeners[i]==DoubleClickListener.class) {
								((DoubleClickListener)listeners[i+1]).doubleClicked(evt);
						}
				}
		}

		// This methods allows classes to register for SingleClickEvents
		public void addSingleClickListener
				(SingleClickListener listener) {
				singleClickListenerList.add(SingleClickListener.class, listener);
		}
    
		// This methods allows classes to unregister for SingleClickEvents
		public void removeSingleClickListener
				(SingleClickListener listener) {
				singleClickListenerList.remove(SingleClickListener.class, listener);
		}
		// This private class is used to fire OntologyPanelEvents
		private void fireSingleClickEvent(SingleClickEvent evt) {

				Object[] listeners = singleClickListenerList.getListenerList();
				// Each listener occupies two elements 
				// - the first is the listener class
				// and the second is the listener instance
				for (int i=0; i<listeners.length; i+=2) {
						if (listeners[i]==SingleClickListener.class) {
								((SingleClickListener)listeners[i+1]).singleClicked(evt);
						}
				}
		}

}

