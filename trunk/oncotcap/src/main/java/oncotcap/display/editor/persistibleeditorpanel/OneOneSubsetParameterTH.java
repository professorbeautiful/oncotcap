package oncotcap.display.editor.persistibleeditorpanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.util.*;
import java.io.*;
import java.lang.reflect.Array;
import oncotcap.util.*;
import oncotcap.datalayer.Persistible;
import oncotcap.display.browser.GenericTreeNode;
import oncotcap.display.browser.GenericTreeNodeRenderer;
import oncotcap.display.common.DragDropLabel;
import oncotcap.display.common.Droppable;
import oncotcap.display.common.OncTreeNode;
import oncotcap.display.editor.EditorFrame;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.parameter.*;

import javax.swing.tree.*;

class OneOneSubsetParameterTH extends SubsetParameterTransferHandler {
		public boolean importData(JComponent c, Transferable t) {
			// 	System.out.println("Component and Transferable " + 
// 													 c + " " 
// 													 + t);
				if ( !(c instanceof BooleanTree) ) {
						System.out.println("This BooleanTreeTransferHandler "
															 + "is attached to wrong type of component " 
															 + c);
						return false;
				}
				
				BooleanTree booleanTree = (BooleanTree)c;
				OncTreeNode dropTarget = 
						(OncTreeNode) booleanTree.getSelectionPath().getLastPathComponent();

				if (t.isDataFlavorSupported(Droppable.genericTreeNode) ) {
						// Only allow one item in the tree
						if ( containsExpression(booleanTree) ) {
								JOptionPane.showMessageDialog
										((JFrame)null, 
										 "<html>"
										 + "This picker allows only one parameter. "
										 + "<br>Please remove a node before adding another"
										 + "</html>");
								return false;
						}
						return super.importData(c,t);
				}
				else if (t.isDataFlavorSupported(Droppable.oncTreeNode)){
						try {
								OncTreeNode dropNode = 
										(OncTreeNode)t.getTransferData(Droppable.oncTreeNode);
								if ( dropNode == dropTarget) {
										// This isn't a really drag & drop ignore this action
										// Don't drop a node on itself - ignore
										System.out.println("dropNode " + dropNode
																			 + " dropTarget " + dropTarget);
										return false;
								}
						}
						catch ( Exception e) { 
								e.printStackTrace();
						}
				}
				return false;
		}
		
		private boolean containsExpression(JTree booleanTree) {
				DefaultTreeModel treeModel = (DefaultTreeModel)booleanTree.getModel();
				DefaultMutableTreeNode rootNode = 
						(DefaultMutableTreeNode)treeModel.getRoot();
				if (rootNode.getChildCount() >= 0) {
						for (Enumeration e=rootNode.children(); e.hasMoreElements(); ) {
								DefaultMutableTreeNode node = 
										(DefaultMutableTreeNode)e.nextElement();
								if ( node.getUserObject() instanceof BooleanExpression)
										return true;
								else if ( containsExpression(node) ) 
										return true;
            }
        }
				return false;
		}
		private boolean containsExpression(DefaultMutableTreeNode oldNode) {
				if (oldNode.getChildCount() >= 0) {
						for (Enumeration e=oldNode.children(); e.hasMoreElements(); ) {
								DefaultMutableTreeNode node = 
										(DefaultMutableTreeNode)e.nextElement();
								if ( node.getUserObject() instanceof BooleanExpression)
										return true;
								if ( containsExpression(node) ) 
										return true;
            }
        }
				return false;
		}
	}
