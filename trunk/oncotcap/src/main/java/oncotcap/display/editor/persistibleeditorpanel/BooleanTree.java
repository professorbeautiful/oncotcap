package oncotcap.display.editor.persistibleeditorpanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import javax.swing.event.TreeSelectionListener; 
import java.util.*;
import java.io.*;
import java.lang.reflect.Array;
import oncotcap.util.*;
import oncotcap.datalayer.Persistible;
import oncotcap.datalayer.SaveListener;
import oncotcap.datalayer.SaveEvent;

import oncotcap.display.browser.OncBrowser;
import oncotcap.display.browser.GenericTreeNode;
import oncotcap.display.browser.GenericTreeNodeRenderer;
import oncotcap.display.browser.GenericTreeSelectionListener;

import oncotcap.display.common.DragDropLabel;
import oncotcap.display.common.Droppable;
import oncotcap.display.common.OncTreeNode;
import oncotcap.display.editor.EditorFrame;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.parameter.*;

import javax.swing.tree.*;

public class BooleanTree extends JTree
		implements  DragGestureListener,
								SaveListener {
		
	protected DefaultTreeModel model;
	private boolean keywordsOnly = false;
	private Vector deleteListeners = new Vector();

		GenericTreeNodeRenderer nodeRenderer = null;
		GenericTreeSelectionListener genericTreeListener = null;

	public BooleanTree(DefaultTreeModel treeModel) {
			super(treeModel);
			model = treeModel;
			init();
		}
		private void init() {
				setRowHeight(0);
				// Update only one tree instance
				nodeRenderer = new GenericTreeNodeRenderer();
				// BooleanTreeNodeEditor nodeEditor = 
// 						new BooleanTreeNodeEditor(this, nodeRenderer);
				setCellRenderer(nodeRenderer);
				//setCellEditor(cellEditor);
				BooleanTreeTransferHandler transferHandler = 
						new BooleanTreeTransferHandler();
				setTransferHandler(transferHandler);		
				transferHandler.setTargetComponent(this);
				//setDragEnabled(true);
				addKeyListener(new TreeKeyListener());
				//Listen for when the selection changes.
				genericTreeListener 
						= new GenericTreeSelectionListener();
				genericTreeListener.allowDoubleClickEditing(true);
				addTreeSelectionListener(genericTreeListener);
				addMouseListener(genericTreeListener);
				setRowHeight(0);
				setEditable(false);

		}
		public GenericTreeSelectionListener getSelectionListener() {
				TreeSelectionListener[] listeners = getTreeSelectionListeners();
				if ( listeners != null && listeners.length > 0 &&
						 listeners[0] instanceof GenericTreeSelectionListener) {
						// listen for its double clicks 
						return((GenericTreeSelectionListener)listeners[0]);
				}
				return null;
		}
		public void addNodeDeleteListener(NodeDeleteListener listener)
		{
			if(! deleteListeners.contains(listener))
				deleteListeners.add(listener);
		}
		public void removeNodeDeleteListener(NodeDeleteListener listener)
		{
			if(deleteListeners.contains(listener))
				deleteListeners.remove(listener);
		}
		private void fireNodeDeleteListeners(OncTreeNode node)
		{
			Iterator it = deleteListeners.iterator();
			while(it.hasNext()) {
					NodeDeleteListener listener = (NodeDeleteListener)it.next();
					listener.nodeDeleted(node);
			}
		}

		public boolean insertIntoTree(OncTreeNode targetNode, OncTreeNode newChild)
		{
			Object target = targetNode.getUserObject();
			Object pkgObj = newChild.getUserObject();
			//System.out.println("target " + target + " leaf " + pkgObj);
			try {										
			if ( pkgObj instanceof OntologyObjectName ) {
					if ( isOperator(target) || isRootNode(target) ){
							return(insertIntoTree(targetNode, 
																		newChild, 
																		targetNode.getChildCount()));
					}
			}
			if(pkgObj instanceof Droppable)
			{
					// 	System.out.println("target " + target
// 																			 + " pkgObj " + pkgObj);
				
					Droppable pkg = (Droppable) pkgObj;
					OncTreeNode tNode;
	

				//dropping a valid leaf into an operator
				if(isOperator(target) && isLeafType(pkg))
				{
					return(insertIntoTree(targetNode, newChild, targetNode.getChildCount()));
				}
				else if(isRootNode(target) && isLeafType(pkg))
				{
						//if there is an operator under the root already, add the
						//dropped node to that operator
						// 						try {
						if(targetNode.getChildCount() > 0 && 
							 isOperator(((OncTreeNode)targetNode.getFirstChild()).getUserObject()))
								{
										return(insertIntoTree((OncTreeNode) targetNode.getFirstChild(), newChild));
								}
						else {
								return(insertIntoTree(targetNode, newChild,
																			targetNode.getChildCount()));
						}
						// 						}catch(Exception e) {
						// 								e.printStackTrace();
						// 								return false;
						// 						}
				}	
				//dropping a leaf into a leaf..add the dropped leaf to the the
				//target's operator
				else if(isLeafType(target) && isLeafType(pkg))
				{
					OncTreeNode parent = (OncTreeNode) targetNode.getParent();
					int dropIdx = parent.getIndex(targetNode) + 1;
					return(insertIntoTree(parent, newChild, dropIdx));
				}
				//dropping a leaf into the root node

				//dropping an operator on the root node
				else if(isRootNode(target) && isOperator(pkg))
				{
					//if there's nothing there add the operator to the root node
					if(targetNode.getChildCount() == 0)
					{
						return(insertIntoTree(targetNode, newChild, 0));
					}
					//if there's already an operator under the root, add this
					//one too- NO, actually do nothing- wes 7/18/2003
					else if(isOperator(((OncTreeNode)targetNode.getFirstChild()).getUserObject()))
					{
//						return(insertIntoTree(targetNode, newChild, targetNode.getChildCount()));
						return(false);
					} 
					//if there's a leaf, add the leaf to this operator and this
					//operator to the root
					else if(isLeafType(((OncTreeNode)targetNode.getFirstChild()).getUserObject()))
					{
						Vector expandedNodes = getCurrentExpandedNodes();
						OncTreeNode moveChild = (OncTreeNode) targetNode.getFirstChild();
						targetNode.remove(moveChild);
						targetNode.insert(newChild, targetNode.getChildCount());
						newChild.insert(moveChild, newChild.getChildCount());
						((DefaultTreeModel) getModel()).nodeStructureChanged(targetNode);
						expandedNodes.add(moveChild);
						expandAllNodes(expandedNodes);
						return(true);
					}
					return(false);
				}
				//dropping an operator onto a leaf
				else if(isLeafType(target) && isOperator(pkg))
				{
					//add the operator that is being dropped to the leafs
					//parent and move the leaf under this operator
					OncTreeNode upperParent = (OncTreeNode) targetNode.getParent();
					
					//only do this if the parent leaf isn't the same type of
					//operator that is being added.
					if(!upperParent.getUserObject().equals(pkg))
					{
						Vector expandedNodes = getCurrentExpandedNodes();
//						OncTreeNode newChild = new OncTreeNode((Persistible) pkg);
						upperParent.remove(targetNode);
						if ( newChild instanceof Persistible && newChild.getUserObject() 
								 instanceof Persistible) {
								((Persistible)newChild.getUserObject()).addSaveListener(this);
						}
						upperParent.insert(newChild, upperParent.getChildCount());
						newChild.insert(targetNode, newChild.getChildCount());
						((DefaultTreeModel) getModel()).nodeStructureChanged(upperParent);
						expandedNodes.add(targetNode);
						expandAllNodes(expandedNodes);
						return(true);
					}
					return(false);
				}
				//dropping an operator onto an operator
				else if(isOperator(target) && isOperator(pkg))
				{
					//if a like operator isn't being added, add it (don't
					//add an AND to an AND, for example.) 
					if(!target.equals(pkg))
					{
						return(insertIntoTree(targetNode, newChild, targetNode.getChildCount()));
					}
					return(false);
				}
			} //end if(pkgObj instanceof Droppable)
												 } catch (Exception e) {
														 e.printStackTrace();
												 }
			return(false);
		} //end insertIntoTree(node, node)

		public boolean isLeafType(Object obj)
		{
			if(!keywordsOnly)
				return((obj instanceof EnumLevel 
								|| obj instanceof Persistible
								|| obj instanceof oncotcap.datalayer.SearchText) 
							 && ! isOperator(obj) && ! isRootNode(obj));
			else
				return(obj instanceof Keyword);
		}
		public boolean isOperator(Object obj)
		{
			return(obj instanceof TcapLogicalOperator);
		}
		public boolean isRootNode(Object obj)
		{
				return(obj.equals(((OncTreeNode)getModel().getRoot()).getUserObject()));
		}
		public TreeNode getRootNode() {
				return (OncTreeNode)getModel().getRoot();
		}
		private boolean insertIntoTree(OncTreeNode parent, OncTreeNode child, int index)
		{
				// System.out.println("insertIntoTree " + parent + " " + child + 
// 													 " " + index);
			Object parentObj = parent.getUserObject();
			Object childObj = child.getUserObject();
			
			//if the parent node is an operator, check to see how many
			//arguments this operator allows before adding another...
			if(isOperator(parentObj))
			{
				int maxArgs = ((TcapLogicalOperator) parentObj).getMaxArguments();
				int nChildren = parent.getChildCount(); 
				if( maxArgs > 0 && nChildren >= maxArgs)
				{
					return(false);
				}
			}
			
			Vector expandedNodes = getCurrentExpandedNodes();
		
			((DefaultTreeModel) getModel()).insertNodeInto(child, parent, index);
			if ( child instanceof Persistible && child.getUserObject() 
					 instanceof Persistible) 
					((Persistible)child.getUserObject()).addSaveListener(this);
			((DefaultTreeModel) getModel()).nodeStructureChanged(parent);
			TreeNode oldParent = child.getParent();
			((DefaultTreeModel) getModel()).nodeStructureChanged(oldParent);
			expandedNodes.add(child);
			expandAllNodes(expandedNodes);
			return(true);
		}

		private void expandAllNodes(Vector nodes)
		{
			Iterator it = nodes.iterator();
			while(it.hasNext())
			{
				Object node = it.next();
				if(node instanceof OncTreeNode)
					expandToNode((OncTreeNode) node);
				else
					System.out.println("NOT TreeNode!!");
			}
		}
		private void expandToNode(OncTreeNode node)
		{
			TreePath pathToExpand = new TreePath(((DefaultTreeModel) getModel()).getPathToRoot(node));
			expandPath(pathToExpand);
		}
		private Vector getCurrentExpandedNodes()
		{
			Vector allNodes = new Vector();
			int nNodes = getRowCount();
			for(int i = 0; i < nNodes; i++)
			{
				allNodes.add(getPathForRow(i).getLastPathComponent());
			}
			return(allNodes);
		}
		private OncTreeNode copyNode(OncTreeNode node)
		{
			OncTreeNode newNode = new OncTreeNode(node.getUserObject(), true);
			for (int n = 0; n < node.getChildCount(); n++)
			{
				OncTreeNode child = copyNode((OncTreeNode) node.getChildAt(n));
				newNode.insert(child, n);
			}
			return(newNode);
		}

		public void dragGestureRecognized(DragGestureEvent dge) {
		}
		public void dragEnter(DropTargetDragEvent dtde) {}
		public void dragExit(DropTargetEvent dte) {}
		public void dragOver(DropTargetDragEvent dtde) {}

		public void objectSaved(SaveEvent e) {
				((DefaultTreeModel) getModel()).nodeStructureChanged
						((TreeNode)((DefaultTreeModel) getModel()).getRoot());
				refresh();
		}

		public void objectDeleted(SaveEvent e) {
				// Do nothing the data source will refresh the tree
				//e.getSavedObject().removeSaveListener(this);
		}
		public void refresh() {
				((DefaultTreeModel)getModel()).reload();
				expandAll();
		}

		public void expandAll() {
				expandAll(this, true);
		}
		// If expand is true, expands all nodes in the tree.
    // Otherwise, collapses all nodes in the tree.
    public void expandAll(JTree tree, boolean expand) {
        TreeNode root = (TreeNode)tree.getModel().getRoot();
				
        // Traverse tree from root
        expandAll(tree, new TreePath(root), expand);
    }
		
    private void expandAll(JTree tree, TreePath parent, boolean expand) {
				TreeNode root = (TreeNode)tree.getModel().getRoot();
        // Traverse children
        TreeNode node = (TreeNode)parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration e=node.children(); e.hasMoreElements(); ) {
                TreeNode n = (TreeNode)e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path, expand);
            }
        }
				
        // Expansion or collapse must be done bottom-up
				if (expand) {
            tree.expandPath(parent);
        } else {
						// Do not collapse teh level directly below the root
						if (parent.getPathCount() != 1)
								tree.collapsePath(parent);
        }
				//resetSelection();
    }

	class TreeKeyListener implements KeyListener
	{
			// This is overriding the default input map delete key handling 
			// in removeNode
		public void keyPressed(KeyEvent e)
		{
			if(e.getKeyCode() == KeyEvent.VK_DELETE)
			{
				TreePath path = getSelectionPath();

				if(path != null)
				{
					OncTreeNode node = (OncTreeNode) path.getLastPathComponent();
					if(node != null && ! isRootNode(node))
					{
							try {
									System.out.println("removeNodeFromParent(node) "  +
																		 node);
									((DefaultTreeModel) getModel()).removeNodeFromParent(node);
									
							}
							catch ( Exception ex) {
									System.out.println("Reminder: Check delete key on filter." );
							}
						fireNodeDeleteListeners(node);
					}
				}
				refresh();
				
			}
		}
		public void keyReleased(KeyEvent e) {}
		public void keyTyped(KeyEvent e) {}



	} //end of class TreeKeyListener


}

