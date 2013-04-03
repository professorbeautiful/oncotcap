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

import java.awt.*;
import java.awt.event.*;

import java.awt.datatransfer.*;
import java.awt.dnd.*;

import java.util.*;
import java.lang.reflect.Array;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;


import oncotcap.datalayer.*;
import oncotcap.util.*;
import oncotcap.datalayer.persistible.Keyword;
import oncotcap.display.common.Droppable;
import oncotcap.display.common.Popupable;

import edu.stanford.smi.protege.model.*;

public class GenericTree extends JTree 	
implements DropTargetListener, 
DragGestureListener,
SaveListener,
AdjustmentListener,
ContainerListener
{
		private String name = "tree name not set";

		Icon KEYWORD_ICON = 
				oncotcap.util.OncoTcapIcons.getImageIcon("k-icon.jpg");

		Icon ENUM_ICON = 
				oncotcap.util.OncoTcapIcons.getImageIcon("e-icon.jpg");

		Icon INFOSOURCE_ICON =
				oncotcap.util.OncoTcapIcons.getImageIcon("is-icon.jpg");
		Vector<TreePath>  expandedNodes = new Vector<TreePath>();
		TreePath lastSelectedPath = null;
		GenericTreeNode lastSelectedNode = null;
		Object lastSelectedObject = null;
		Icon QUOTENUGGET_ICON =
				oncotcap.util.OncoTcapIcons.getImageIcon("qn-icon.jpg");		

		DropTarget dropTarget = null;
		DragGestureRecognizer dragSource = null;

		Toolkit kit = Toolkit.getDefaultToolkit();
		final Clipboard clipboard =
				kit.getSystemClipboard();

		Vector prunedNodes = new Vector();
		Hashtable popupHashtable = new Hashtable();
		Hashtable treeNodeHashtable = new Hashtable();
		Hashtable treeParentHashtable = null;
		Hashtable treeChildrenHashtable = null;
		GenericRootNodeObject rootNodeObject = 
				new GenericRootNodeObject(null);
		GenericTreeNode rootNode = new GenericTreeNode(rootNodeObject);
		JPopupMenu popup = null;
		OntologyTree ontologyTree  = null;
		int topRow = -1;
		int bottomRow = -1;

    private static String ROOT = String.valueOf(-1);

		boolean allowDoubleClickEdit = false;
		boolean expandState = true;
		protected GenericTreeSelectionListener genericTreeListener = null;
		// set up comparator
		BrowserNodeComparator comparator = new BrowserNodeComparator();
		BrowserNodeVersionComparator versionComparator = 
				new BrowserNodeVersionComparator();
		public static Vector genericTrees = new Vector();
		private Hashtable treeHashtable = null;

		public GenericTree(Hashtable treeHashtable) {
				// This is a temp constructor to build a treemodel 
				// no ui 
				super();
				setModel(getTreeModel(treeHashtable));
		}
		public GenericTree(Hashtable treeHashtable, boolean rootNodeVisible) {
				super();
				setModel(getTreeModel(treeHashtable));
				init(rootNodeVisible);
				genericTrees.add(this);
		}

		public GenericTree(TreeModel treeModel, boolean rootNodeVisible) {
				super(treeModel);
				init(rootNodeVisible);
				expandAll(this, true);
				genericTrees.add(this);
		}
		public Hashtable getTreeNodeHashtable() {
				return treeParentHashtable;
		}
		public void setRootNodeLabel(JLabel label) {
				rootNode.setUserObject(label);
		}
		public void setRootNodeString(String str) {
				rootNode.setUserObject(str);
		}
		public void updateTree(Hashtable treeHashtable) {
				TreePath lastPath = getSelectionPath();
				if ( lastPath != null ) {
						// get user object so it can be reset when tree is updated
					//System.out.println("GenericTree.updateTree: lastpath = " + lastPath);
					//System.out.println("GenericTree.updateTree: lastSelectedPath = " + lastSelectedPath);
				}
				// remove the nodes from the current tree from the 
				// related persistibles
				removeNodesFromPersistibles();
				setModel(getTreeModel(treeHashtable));
				// If the item that was last selected is still in 
				// the tree then reselect it otherwise clear selection
				TreePath path = matchPath(lastPath);
				//System.out.println("GenericTree.updateTree: "
				//		+ " matchPath returns " + path); 
				if ( path != null ) {
						setSelectionPath(path);
						//System.out.println("GenericTree.updateTree: "
						//	+ "nodeIsInTree returns "
						//		+ nodeIsInTree((GenericTreeNode)path.getLastPathComponent()));
				}
				else {
						clearSelection();
				}
				refresh();
		}

		public boolean nodeIsInTree(GenericTreeNode node) {
				DefaultTreeModel model = (DefaultTreeModel)getModel();
				int index = model.getIndexOfChild(model.getRoot(), node);
				if ( index > -1 )
						return true;
				else
						return false;
		}

		public void sort(GenericTreeNode node) {
			
			if ( getOntologyTree() != null  
					&& getOntologyTree().getSortMode() == OntologyTree.SORT_BY_ALPHA){
				sort(node, comparator);
			}
			else 
				sort(node, versionComparator);
			
		}
		
		public void sort(GenericTreeNode node, Comparator comparator) {
			// sort the roots children
			Object[] objs = new Object[node.getChildCount()];
			Enumeration children = node.children();
			for (int i=0;children.hasMoreElements();i++) {
				
				GenericTreeNode child = 
					(GenericTreeNode) children.nextElement();
				objs[i] = child;
			}
			Arrays.sort(objs, comparator);
			node.removeAllChildren();
			
			// insert newly ordered children
			for (int i=0;i<objs.length;i++) {
				GenericTreeNode orderedNode = 
					(GenericTreeNode) objs[i];
				node.add(orderedNode);
				if (!orderedNode.isLeaf()) {
					sort(orderedNode);
				}
			}
		}

		public void refresh(Object savedObject) {
 		  	//System.out.println("Refreshing tree " + savedObject);
			if ( savedObject instanceof TreeUserObject) {
				Vector treeNodes = ((TreeUserObject)savedObject).getTreeNodes();
				Iterator i = treeNodes.iterator();
				while ( i.hasNext() ) {
					GenericTreeNode treeNode = (GenericTreeNode)i.next();
					((DefaultTreeModel)getModel()).nodeChanged(treeNode);
				}
			}
			//sort(rootNode);
			repaint();
		}
		public void refresh() {
	 	  	//lastSelectedObject);
	 	  	//oncotcap.util.ForceStackTrace.showStackTrace();
	 	  	saveExpandedNodeState();
	 	  	//TODO: save state separately for each node.
	 	  	sort(rootNode);
	 	  	DefaultTreeModel theModel = (DefaultTreeModel)getModel();
	 	  	theModel.reload(); // this will collapse nodes
	 	  	expandAll(this, true);
	 	  	resetSelection();
	 	  	repaint();
	 	  	System.out.println("Refreshing tree " + getName() + " with count " + getRowCount());

		}

		private void saveExpandedNodeState() {
				// Get a depth first version of tree 
				GenericTreeNode rootNode = (GenericTreeNode)getModel().getRoot();
				Enumeration expandedNodesEnumeration = 
						getExpandedDescendants(new TreePath(rootNode.getPath()));
				expandedNodes.clear();
				while ( expandedNodesEnumeration != null && expandedNodesEnumeration.hasMoreElements() ) {
						expandedNodes.add((TreePath)expandedNodesEnumeration.nextElement());
				}
				//System.out.println("SaveExpandNodeState : " + new TreePath(rootNode.getPath()) + " " 
				//									 + expandedNodes.size());
				// Put it in a vector
				
		// 		int cnt = 0;
// 				while ( expandedNodes != null && expandedNodes.hasMoreElements() ) {
// 						cnt++;
// 				}
// 				System.out.println("Set ExpandNodes : " + getName() + " " + cnt);
				
		}

		private void expandNodes() {
				if ( expandedNodes == null )
						return;
				int cnt = 0;
				for (TreePath treePath : expandedNodes){
						setExpandedState(treePath, true);
						cnt++;
				}
				// if ( cnt < 5 ) {
// 							System.out.println("ExpandNodes : " + getName() + " " +
// 																 expandedNodes);
// 				}		 
// 				System.out.println("ExpandNodes : " + getName() + " " + cnt);
		}

		private void init(boolean rootNodeVisible) {
			// DId this cause a problem before???? 
			//may not want to share maps just add actions to maps
//			setInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT,
//				    OncBrowser.getDefaultInputMap());
//			setInputMap(JComponent.WHEN_FOCUSED,
//				    OncBrowser.getDefaultInputMap());
//			setActionMap(OncBrowser.getDefaultActionMap());
				// Register the listener with the component
				addContainerListener(this);
				setRootVisible(rootNodeVisible);
				setShowsRootHandles(true);
				setToggleClickCount(3);
				setRowHeight(25);
				setEditable(false);
				// Update only one tree instance
				GenericTreeNodeRenderer nodeRenderer = new GenericTreeNodeRenderer();
				setCellRenderer(nodeRenderer);				
				getSelectionModel().setSelectionMode
						(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
// 						(TreeSelectionModel.SINGLE_TREE_SELECTION);
				
				//Listen for when the selection changes.
				genericTreeListener 
						= new GenericTreeSelectionListener();
				addTreeSelectionListener(genericTreeListener);
				addMouseListener(genericTreeListener);

				// Enable dropping
				GenericTreeTransferHandler th = new GenericTreeTransferHandler(this);
				setTransferHandler(th);		
				refresh();
		}
		
		public JPopupMenu getPopup(Class treeObjectType) {
				return (JPopupMenu)popupHashtable.get(treeObjectType);
		}
		public JPopupMenu getPopup(Object treeObject) {
				JPopupMenu popup = null;
				if ( treeObject instanceof Popupable)
						popup = ((Popupable)treeObject).getPopupMenu();
				else
						popup = (JPopupMenu)popupHashtable.get(treeObject);
				return popup;
		}

		public void addPopup(Class treeObjectType, JPopupMenu popup) {
				popupHashtable.put(treeObjectType, popup);
		}

		public  DefaultTreeModel getTreeModel(Hashtable treeHashtable) {
				rootNode.removeAllChildren();
				this.treeParentHashtable = treeHashtable;
				this.treeChildrenHashtable = invertHashtable(treeHashtable);
				constructTree(treeChildrenHashtable);
				DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
				return treeModel;
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

		private Hashtable invertHashtable(Hashtable h) {
				Hashtable newHash = new Hashtable();
				//System.out.println("Starting hashtable " + h);
				for (Enumeration e = h.keys() ; 
						 e.hasMoreElements() ;) {
						Object key = e.nextElement();
						Vector parents = (Vector)h.get(key);
						if ( parents != null ) {
								Iterator i = parents.iterator();
								while ( i.hasNext() ) {
										addVectorValue(newHash, i.next(), key);
								}
						}
				}				
				//System.out.println("Starting hashtable " + h);
	
				return newHash;
		}
		
	private void addVectorValue(Hashtable hashTable,
															Object key, Object value) {
				if ( key != null && value != null ) {
						// Allow a node to have more than one value
						Vector values  = 
								(Vector)hashTable.get(key);
						if ( values == null ) 
								values = new Vector();
						// If  the key had a value that was the root remove it 
						// before adding non root values
					// 	if ( values.contains(ROOT) ) // can't be root an leaf
// 								values.remove(ROOT);
						values.addElement(value);
						hashTable.put(key, values);
				}
		}

		public void constructTree(Hashtable treeChildrenHashtable) {
				treeNodeHashtable.clear(); // empty out 
				GenericTreeNode node = null;
				GenericTreeNode parentNode = null;
				// Assume the hashtable contains key=parent and value=children
				Vector children = (Vector)treeChildrenHashtable.get(ROOT);
				createTreeNodes(rootNode, children);
		}	

		private void createTreeNodes( GenericTreeNode parent, 
																	Vector children) {
				GenericTreeNode newNode = null;
				Vector childNodes = new Vector();
				if ( children == null ) 
						return;
				// For each child
				Iterator i = children.iterator();
				while (i.hasNext() ) {
						Object childObj = i.next();
						if ( isObjectAlreadyOnNode(parent, childObj)
								 ||  isObjectInNodePath(parent, childObj) )
								continue;
						newNode = newTreeNode(childObj);
						// add child node to child node
						try {
								parent.add(newNode);
						}	catch (IllegalArgumentException iae) {
								// NOt adding node it is already an ancestor
								System.out.println
										("Error: Trying to add a node that is already "
										 + " an ancestor " );
								//iae.printStackTrace();
						}
						childNodes.add(newNode);
				}
				// Now create the next level of nodes
				//System.out.println("ChildNodes " + childNodes);
				createTreeNodes(childNodes);
		}

		private void createTreeNodes(Vector forNodes) {
				//System.out.println("Creating nodes for " + forNodes);
				if ( forNodes.size() <= 0 ) 
						return;
				GenericTreeNode newNode = null;
				Vector childNodes = new Vector();
				// For each node get child nodes
				Iterator ii = forNodes.iterator();
				while (ii.hasNext()) {
						GenericTreeNode parent = (GenericTreeNode)ii.next();
						Vector children = 
								(Vector)treeChildrenHashtable.get(parent.getUserObject());
						// For each child
						if (children == null)
								continue;
						Iterator i = children.iterator();
						while (i.hasNext() ) {
								Object childObj = i.next();
								if ( isObjectAlreadyOnNode(parent, childObj)
										 ||  isObjectInNodePath(parent, childObj) )
										continue;
								
								newNode = newTreeNode(childObj);
								// add child node to child node
								try {
										parent.add(newNode);
								}	catch (IllegalArgumentException iae) {
										// NOt adding node it is already an ancestor
										System.out.println
												("Error: Trying to add a node that is already "
												 + " an ancestor " );
										//iae.printStackTrace();
								}
								childNodes.add(newNode);
						} // while there are children
				}// while there are parents
				createTreeNodes(childNodes);
		}


		public void allowDoubleClickEditing(boolean allow) {
				allowDoubleClickEdit = allow;
				genericTreeListener.allowDoubleClickEditing
						(allowDoubleClickEdit);
		}

		public boolean allowsDoubleClickEditing() {
				return allowDoubleClickEdit;
		}
		public void selectRowLikeUser(int row) {
				// Select it as if the user did it so it will fire the change method
				setSelectionRow(row);
				TreePath treePath = getSelectionPath();
				if ( treePath != null ) {
						GenericTreeNode node = (GenericTreeNode)treePath.getLastPathComponent();
						fireTreePanelEvent(new TreePanelEvent(node.getUserObject(), this));
// 						System.out.println("firing value changed " + treePath);
					  fireValueChanged(new TreeSelectionEvent(this, treePath,true,null,treePath));
				}
		}

		// This i believe is a temporary method I am using until all items
		// are converted to persistible it will look up an instance in a 
		// hashtable of guid/instances to create tree nodes
		// Once I have persistibles the defaultmutabletreenode will be accessible 


		public GenericTree getTree() {
				return this;
		}

		/** Keep the horizontal scroll bar from shifting when
				a node is selected */
		public void scrollRectToVisible(Rectangle aRect) {
				aRect.x = 0;
				super.scrollRectToVisible(aRect);
		}


		   // Add the event registration and notification code to a class.
    
		// This methods allows classes to register for TreePanelEvents
		public void addTreePanelListener
				(TreePanelListener listener) {
				// 				System.out.println("Adding listener : " + listener);
				listenerList.add(TreePanelListener.class, listener);
		}
    
		// This methods allows classes to unregister for TreePanelEvents
		public void removeTreePanelListener
				(TreePanelListener listener) {
				listenerList.remove(TreePanelListener.class, listener);
		}
    
		// This private class is used to fire TreePanelEvents
		public void fireTreePanelEvent(TreePanelEvent evt) {
				Object[] listeners = listenerList.getListenerList();
				// Each listener occupies two elements 
				// - the first is the listener class
				// and the second is the listener instance
				for (int i=0; i<listeners.length; i+=2) {
						if (listeners[i]==TreePanelListener.class) {
								//								System.out.println("CHANGED " + listeners[i+1] + " " + evt);
								((TreePanelListener)listeners[i+1]).changed(evt);
						}
				}
		}



		private GenericTreeNode newTreeNode(Object userObject) {
				if ( userObject == null ) 
						return null;
				if (userObject instanceof Persistible) {
						//System.out.println("addSaveListener to tree for " 
						//  + node.getUserObject());
						((Persistible)userObject).addSaveListener(this);
				}
				GenericTreeNode treeNode = new GenericTreeNode(userObject);
				return treeNode;		
		}
		
		public void removeNode(Object userObject) {
				TreePath treePath = getSelectionPath();
				TreePath previousNodePath = getSelectionPath();
				int currentRow = -1;
				// Get the node directly above curremtly selected node even if they
				// are not related
				if ( treePath != null ) {
						currentRow = getRowForPath(treePath);
						if ( currentRow > 1 ) {
								previousNodePath = getPathForRow(currentRow -1);
						}
						else 
								previousNodePath = null;
				}
						
				GenericTreeNode toNode = null;
				GenericTreeNode newNode = null;
				if ( userObject instanceof TreeUserObject ) {
						// get all the nodes related to this persistible
						Vector nodes = ((TreeUserObject)userObject).getTreeNodes();
						Object [] nodeArray = nodes.toArray();
						for ( int i = 0; i < Array.getLength(nodeArray); i++) {
								GenericTreeNode node = (GenericTreeNode)nodeArray[i];;
								((TreeUserObject)userObject).removeTreeNode(node);	
								node.removeFromParent();
						}						
				}
				// Now select the row immediately above the orginally selected row
				if ( currentRow > 0 && previousNodePath != null) {
						setSelectionPath(previousNodePath);
				}
				else {   // If no where to go
						clearSelection();
						fireTreePanelEvent(new TreePanelEvent(rootNode.getUserObject(), 
																									this));						
				}
		}
		
		// Selected items is a list of persistibles
		public void addNode(Object userObject, boolean toRoot, 
												Vector selectedItems) {
				if ( toRoot || selectedItems == null ) {
						addNode(userObject, true);
						return;
				}
				GenericTreeNode newNode = null;
				Iterator i = selectedItems.iterator();
				while (i.hasNext()) {
						Object obj = i.next();
						if ( obj instanceof TreeUserObject ) {
								// Get all the nodes that represent this object
								newNode = addNodeToTreeUserObject((TreeUserObject)obj, 
																				  userObject);
						}
						else if ( obj instanceof GenericTreeNode ) {
										TreePath treePath = getPath((GenericTreeNode)obj);
										newNode = addNode(userObject, toRoot, treePath);
						}
						else if ( obj instanceof TreePath ) {
								newNode = addNode(userObject, toRoot, (TreePath)obj);
						}
				}
				selectNode(newNode);
		}

		public void selectNode(GenericTreeNode node ) {
				// pick a path to be selected
				TreePath path = getPath(node);
				//System.out.println("selectNode " + path + " node " + node);
				setLastSelectedPath(path);
				setLastSelectedNode(node);
				setSelectionPath(path);
	// 	06.07.04		System.out.println("selectNode selectionPath " + getSelectionPath());
// 				oncotcap.util.ForceStackTrace.showStackTrace();
				refresh();
		}

		private GenericTreeNode addNodeToTreeUserObject(TreeUserObject obj, 
																				 Object userObject) {
				GenericTreeNode newNode = null;
				GenericTreeNode toNode = null;

				Vector userObjectNodes = obj.getTreeNodes();
				Iterator i = userObjectNodes.iterator();
				while (i.hasNext()) {
						toNode = (GenericTreeNode)i.next();
						if ( !isObjectAlreadyOnNode(toNode, userObject)) {
								newNode = newTreeNode(userObject);
								toNode.add(newNode);
						}
				}
				return newNode;
		}

		// THis is a temporary FIX 
		public void pruneNodes(Color color) {
				// Get all tree nodes that are of the color specified
				Vector pruneNodes = new Vector();
				for (Enumeration e=rootNode.depthFirstEnumeration(); 
						 e.hasMoreElements(); ) {
						GenericTreeNode n = (GenericTreeNode)e.nextElement();		
						
						if ( n.getUserObject() instanceof AbstractPersistible) {
								if ( ((AbstractPersistible)n.getUserObject()).getForeground().equals(color) ) {
								pruneNodes.addElement(n);
								}
						}
				}
				// remove those nodes 
				removePrunedNodes(pruneNodes);
				refresh();
		}

		public void pruneBranches(Vector userObjectTypes) {
				// Get all tree nodes that are of the type userObjectTypes
				Vector allKids = CollectionHelper.makeVector(rootNode);
				Vector connectedKids = CollectionHelper.makeVector(rootNode);
				for (Enumeration e=rootNode.depthFirstEnumeration(); 
						 e.hasMoreElements(); ) {
						GenericTreeNode n = (GenericTreeNode)e.nextElement();						
						// Build a list of all tree nodes
						allKids.addElement(n);
						if ( isOfType(n, userObjectTypes) ) {
								// Get paths for the selected tree nodes
								TreeNode[] treeNodes = n.getPath();
								if ( treeNodes != null ) {
										for ( int i = 0; i < Array.getLength(treeNodes); i++) {
												// Build a list of all nodes included in the paths
												connectedKids.addElement(treeNodes[i]);
										}
								}
						}
				}
// 				System.out.println("connectedKids " + connectedKids);
				// find the set of nodes not in the list of included nodes
				Vector pruneNodes = new Vector(CollectionHelper.not(allKids, connectedKids));

				// remove those nodes 
				removePrunedNodes(pruneNodes);
				refresh();
		}
		public void pruneNodesFromRoot(Vector userObjectTypes) {
				// Loop thru all root nodes and make sure they are of the 
				// correct type
				Vector kids = new Vector();
				if (rootNode.getChildCount() >= 0) {
						for (Enumeration e=rootNode.children(); e.hasMoreElements(); ) {
								kids.addElement(e.nextElement());
            }
						Iterator i = kids.iterator();
						while ( i.hasNext() ) {
								GenericTreeNode n = (GenericTreeNode)i.next();
								if ( !isOfType(n, userObjectTypes) ) {
										n.removeFromParent();
								}
						}
        }
		}

		public void pruneNodes(Vector userObjectTypes) {
				// Loop through each node in the tree and if it is 
				// an instance of one of the given userObjectTypes 
				// remove the node from the tree and reparent the children
				// Do not even check nodes connected from the root they cannot 
				// be removed
				//System.out.println("Pruning " + userObjectTypes);
				prunedNodes.clear();
				pruneNode(rootNode, userObjectTypes);
				removePrunedNodes(prunedNodes);
				//System.out.println("refresh after pruning");
				refresh();
		}

		public void pruneNode(GenericTreeNode node, 
													 Vector userObjectTypes) {
				boolean reparentChildren = false;
				if ( isOfType(node, userObjectTypes) )  {
						reparentChildren = true;
				}
				if (node.getChildCount() >= 0) {
						for (Enumeration e=node.children(); e.hasMoreElements(); ) {
								GenericTreeNode n = (GenericTreeNode)e.nextElement();
								pruneNode(n, userObjectTypes);
            }
        }
				if ( reparentChildren ) {
						reparentChildren(node);
						prunedNodes.addElement(node); //.removeFromParent();
				}
    }
		
		public void removePrunedNodes(Vector prunedNodes) {
				Iterator i = prunedNodes.iterator();
				while ( i.hasNext() ) {
						((GenericTreeNode)i.next()).removeFromParent();
				}
		}

		private void reparentChildren(GenericTreeNode node) {
				// Get parent node 
				GenericTreeNode parent = (GenericTreeNode)node.getParent();
		// 		System.out.println("reparenting " +  node.getChildCount() 
// 													 + " " + parent);
				Vector kids = new Vector();
				if (node.getChildCount() >= 0) {
						// Get the children
						for (Enumeration e=node.children(); e.hasMoreElements(); ) {
								GenericTreeNode n = (GenericTreeNode)e.nextElement();
								kids.addElement(n);
            }
						// Now reparent kids - unfortunately can't manipulate the 
						// parents while looping thru ths kids 
						Iterator i = kids.iterator();
						while ( i.hasNext() ) {
						// 		System.out.println("reparenting " +  node.getChildCount() 
// 																	 + " " + parent + " "
// 																	 + i);
								parent.add((DefaultMutableTreeNode)i.next());
						}
        }
		}
		private boolean isOfType(GenericTreeNode node, 
														 Vector userObjectTypes) {
				Object obj = node.getUserObject();
				Iterator i = userObjectTypes.iterator();
				while (i.hasNext()) {
						Class cls = (Class)i.next();
						if ( cls.isInstance(obj) ) {
// 								System.out.println("isoftype " + cls + " " + obj);
								return true;
						}
				}
				return false;
		}
		public void addNode(Object userObject, boolean toRoot) {
				TreePath treePath = getSelectionPath();
				GenericTreeNode newNode = null;
				newNode = addNode(userObject, toRoot, treePath);
				selectNode(newNode);
		}
		public GenericTreeNode addNode(Object userObject, boolean toRoot, 
												TreePath treePath) {
				GenericTreeNode toNode = null;
				GenericTreeNode newNode = null;
				if ( userObject instanceof Persistible ) 
						((Persistible)userObject).addSaveListener(this);
				if ( treePath == null || toRoot) {
						if ( !isObjectAlreadyOnNode(rootNode, userObject)) {
								// Add node to the root
								newNode = newTreeNode(userObject);
								rootNode.add(newNode);
						}
				}
				else {
						//for each tree node instance for the selected object
						//create a tree node from the user object just added
						// If there is an element selected create objects that are 
						// directly related to it
						toNode = 
								(GenericTreeNode)treePath.getLastPathComponent();	
						Object obj = toNode.getUserObject();
						if ( obj instanceof TreeUserObject ) {
								newNode = addNodeToTreeUserObject((TreeUserObject)obj, 
																				userObject);
						}
						else {
								if ( !isObjectAlreadyOnNode(toNode, userObject)) {
										toNode.add(newTreeNode(userObject));
								}
						}
						//System.out.println("Adding node to generic tree " + userObject + " toNode " 
						//						 + toNode);
				}
				return newNode;
		}
		
		private boolean isObjectInNodePath(GenericTreeNode node, 
																			 Object userObject) {
				if ( !(userObject instanceof TreeUserObject) )
						return false;
				Vector treeNodes = ((TreeUserObject)userObject).getTreeNodes();
				if ( treeNodes == null )
						return false;
				Iterator i = treeNodes.iterator();
				GenericTreeNode existingNode = null;
				while (i.hasNext()) {
						// for each tree node see if it is in the path of the current node 
						// don't want to make any loops
						existingNode = (GenericTreeNode)i.next();
						if ( node.isNodeAncestor(existingNode) 
								 || node.isNodeChild(existingNode) ) {
								// if ( existingNode.getUserObject() instanceof Persistible 
// 										 && node.getUserObject() instanceof Persistible ) 
// 										System.out.println
// 												("In hierarchy already existing - node " 
// 												 + existingNode.getUserObject().getClass() + " " 
// 												 + ((Persistible)existingNode.getUserObject()).getGUID()
// 												 + " - " 
// 												 + node.getUserObject().getClass() + " " 
// 												 + ((Persistible)node.getUserObject()).getGUID()); 
								return true;
						}
				}
				return false;
		}

		private boolean isObjectAlreadyOnNode(GenericTreeNode node, 
																					Object userObject) {
				// See if the user object is already a treenode direct descendant of this node
				for (Enumeration e=node.children(); e.hasMoreElements(); ) {
						GenericTreeNode child = (GenericTreeNode)e.nextElement();
						if ( child.getUserObject() == userObject )
								return true;
				}
				return false;
				/*//another way is this is the shorter way
				do this later
				treeNodes = userObject.getTreeNodes();	
				
				//for each treeNOde compare parent with the node
				*/
		}
		

		public boolean  moveNode(GenericTreeNode node, GenericTreeNode toNode) {
				if (node == toNode || node.getUserObject() == toNode.getUserObject() 
						|| node == rootNode)
						return false;
				if ( toNode.isNodeAncestor(node) ) {
						// Trying to make a node a child of its child
						return false;
				}
				try { 
				if ( node.getUserObject() instanceof Keyword ) {
						//						System.out.println("Move Node " + node 
						//								 + " TO " + toNode);
						Keyword parentKeyword = null;
						Keyword newParentKeyword = null;
						Keyword keyword = (Keyword)node.getUserObject();
						GenericTreeNode parent = (GenericTreeNode)node.getParent();
						//						System.out.println("parent " + parent);

						if ( parent.getUserObject() instanceof Keyword ){ 
										parentKeyword = (Keyword)parent.getUserObject();
										//										System.out.println("Keyword " + keyword);
										//System.out.println("parentKeyword " + parentKeyword);
										// IN FUTURE use link but need to be able to specify
										// direction (slot/attr)
										// Remove the current parent from the moving keyword
										Vector parents = keyword.getParents();
										parents.remove(parentKeyword);
										// Remove moving keyword from current parent
										Vector children = parentKeyword.getChildren();
										children.remove(keyword);
										parentKeyword.update();
								}
						if ( toNode.getUserObject() instanceof Keyword) {
								Object newParentObj = toNode.getUserObject();
		
								if ( newParentObj instanceof Keyword ){ 
										
										//System.out.println("newParentKeyword " + newParentObj);
										// Add child to new parent
										((Keyword)newParentObj).addChild(keyword);
										// Add new parent to child
										keyword.setPar(((Keyword)newParentObj));
										keyword.update();
										((Keyword)newParentObj).update();
								}
								toNode.add(node);
								refresh();
								return true;
						}
						else if ( toNode == rootNode ) {
								toNode.add(node);
								refresh();
								return true;
						}
						// Make sure the list of nodes this user object is attached to is updated
						keyword.removeTreeNode(node);
				}
				}catch ( Exception e ) {
						e.printStackTrace();
				}
		
				return false;
		}


		public boolean  copyNode(GenericTreeNode node, GenericTreeNode toNode) {
				if (node == toNode || node.getUserObject() == toNode.getUserObject() 
						|| node == rootNode )
						return false;
				if ( toNode.isNodeAncestor(node) ) {
						// Trying to make a node a child of its child
						return false;
				}
				if ( toNode == rootNode) {
						// System.out.println("Trying to copy to root node");
						// Only allow this to work with OncMergeBrowser for the
						// time being
						// System.out.println(" What data source is the root node " + 
						// 	 getOntologyTree().getDataSource());
						// System.out.println(" What data source is the copyNode node " + 
						// ((AbstractPersistible)node.getUserObject()).getDataSource());

						//OncMerge mode - two data sources
						if ( oncotcap.Oncotcap.getDataSourceMode() == false ) { 
								return OncMergeBrowser.mergeNode(node, this);
						}
						else
								return false;
				}
				if ( node instanceof Droppable ) {
						boolean didItDrop = ((Droppable)node).dropOn(toNode);
						if ( didItDrop == true ) {
								GenericTreeNode newNode = newTreeNode(node.getUserObject());
								toNode.add(newNode);
								OncBrowser.refresh();
								return true;
						}
				}
			
				return false;
		}
		public void setLastSelectedObject(Object obj) {
				lastSelectedObject = obj;
		}
		public void setLastSelectedPath(TreePath path) {
				//System.out.println("setLastSelectedPath " + path);

				lastSelectedPath = path;
				setLastSelectedNode((GenericTreeNode)path.getLastPathComponent());
		}
		public TreePath getLastSelectedPath() {
				return lastSelectedPath;
		}
		public void setLastSelectedNode(GenericTreeNode node) {
				//System.out.println("setLastSelectedNode " + node);
				lastSelectedNode = node;
		}
		public GenericTreeNode getLastSelectedNode() {
				return lastSelectedNode;
		}

		private Enumeration getDepthFirstNodes() {
				return null;
		}
		public GenericTreeNode findUserObject(Object userObj) {
				// Get a depth first version of tree 
				GenericTreeNode rootNode = (GenericTreeNode)getModel().getRoot();
				GenericTreeNode matchNode = null;
				for (Enumeration e = rootNode.depthFirstEnumeration() ; 
						 e.hasMoreElements() ;) {
						matchNode = (GenericTreeNode)e.nextElement();	
						if ( matchNode.getUserObject().equals(userObj) ) 
								return matchNode;
						else matchNode = null;
				}
				return matchNode;
		}

// 		public void setSelectedNode(JTree tree, TreeNode node) {
//         TreePath path = 
// 						new TreePath(getPathToRoot(node));
//         scrollPathToVisible(path);
//         setSelectionPath(path);
//     }
		
		private Object[] getPathToRoot(GenericTreeNode node) {
				if (node == null) 
						return null;
				 TreePath nodePath = getPath(node);
				 Vector pathV = new Vector();
				 for ( int i = 0; i < nodePath.getPathCount(); i++) {
						 Object pathComponent = nodePath.getPathComponent(i);
// 						 System.out.println("pathComponent: " + pathComponent
// 																+ " " + pathComponent.getClass());
						 pathV.addElement(getPath((GenericTreeNode)pathComponent));
				 }
				 return pathV.toArray();
    }

		private TreePath matchPath(TreePath treePath) {
				Vector guidPath = new Vector();
				Vector userObjectPath = new Vector();
				//Object 	selectedNode =
				// 										(GenericTreeNode)treePath.getLastPathComponent();
				//System.out.println("selectedNode " + selectedNode);
				if ( treePath == null ) 
						return null;
				return matchNode(rootNode, 
									treePath, 
									1);
       //  if (rootNode.getChildCount() >= 0) {
//             for (Enumeration e=rootNode.children(); e.hasMoreElements(); ) {
//                 GenericTreeNode n = (GenericTreeNode)e.nextElement();
//                 removeNodeFromPersistibles(n);
//             }
//         }

				/* Object lastPathComponent = ((GenericTreeNode)treePath.getLastPathComponent()).getUserObject();
				//System.out.println("lastPathComponent " + lastPathComponent);
				if ( lastPathComponent instanceof AbstractPersistible) {
						GUID guid = ((AbstractPersistible)lastPathComponent).getGUID();
						// Get the most current user object
						Persistible currentObject 
								= AbstractPersistible.getPersistible(guid);

						Vector treeNodes = 
								((AbstractPersistible)currentObject).getTreeNodes();
						Iterator i = treeNodes.iterator();
						while ( i.hasNext() ) {
								GenericTreeNode node = (GenericTreeNode)i.next();
								if ( pathMatches(node, treePath) ) {
										System.out.println("matched a path");
										return new TreePath(node.getPath());
								}
						}
				}
								
				return null;
				*/

		}


		private TreePath matchNode(GenericTreeNode node, 
													 TreePath path, 
													 int pathPosition) {
				// System.out.println( "MATCH NODE "
// 														+ node + " " + path + " " +pathPosition);
				if ( path.getPathCount() < pathPosition) {
						// node cannot be matched
						return null;
				}
						
				GenericTreeNode nodeToMatch = 
						(GenericTreeNode)path.getPathComponent(pathPosition);
	
        if (node.getChildCount() >= 0) {
            for (Enumeration e=node.children(); e.hasMoreElements(); ) {
                GenericTreeNode n = (GenericTreeNode)e.nextElement();
								Object child = n.getUserObject();
								Object objectToMatch = nodeToMatch.getUserObject();
								// If the child matches the node in the path
								if ( child instanceof Persistible 
										 && objectToMatch instanceof Persistible 
										 && child.equals(objectToMatch)) {
										if ( pathPosition+1 == path.getPathCount() ) {
												//System.out.println("The path that matches " + 
												//								 new TreePath(n.getPath()));
												return new TreePath(n.getPath());
										}
										else {
												pathPosition = pathPosition+1;
												return matchNode(n, path, pathPosition); 
										}
								}
            }
        }
				return null;
						
    }
		
		private boolean pathMatches(GenericTreeNode node, TreePath path) {
				// Work your way backward thru the hierarchy and 
				// if it matches to the root them you have a match
				boolean match = false;
				int i = 1;
				GenericTreeNode parent = (GenericTreeNode)node.getParent();
				while ( parent != null && 
								(parent.getUserObject() instanceof AbstractPersistible) ) {
						if ( parent.getUserObject() == 
								 ((GenericTreeNode)path.getPathComponent(i)).getUserObject() ) {
								match = true;
								i++;
								parent = (GenericTreeNode)parent.getParent();
						}
						else 
								return false;
				}

				return match;

		}

		public void resetSelection(TreePath[] treePaths){
				if ( treePaths != null ) {
						for ( int i = 0; i < Array.getLength(treePaths); i++) {
								TreePath path = matchPath(treePaths[i]);
								if ( path != null ) {
										setSelectionPath(path);
										selectNode((GenericTreeNode)path.getLastPathComponent());
								}
						}
						

				}
		}

		public void resetSelection(){
		//06.07.04 			System.out.println("resetSelection lastselectedpath " + 
// 				 								 lastSelectedPath + 
// 				 								 " getSelectionPath " + getSelectionPath());
				// Build an array of paths
				 Object[] path = null; 
				 Object root = getModel().getRoot();
				 if ( lastSelectedPath == null )
						 return;
				 path = lastSelectedPath.getPath();
				 TreePath treePath = new TreePath(path);
				 TreePath path1 = null; 
				 try {
						 path1 = matchPath(treePath);
				 } catch ( Exception e) {
					 System.out.println("GenericTree.resetSelection: problem resetting" );
					 e.printStackTrace();
				 }
				 if ( path1 == null ) 
					clearSelection();
				 else 
					 setSelectionPath(path1);
				 //System.out.println("getSelectionPath " + getSelectionPath());
// 				 System.out.println("What is the viewport dimensions " + getParent().getHeight());
// 				 System.out.println("What is the path bounds " 
// 														+ getPathBounds(treePath));
// 				 System.out.println("What is row height " + getRowHeight());
				 scrollPathToVisible(treePath, 5);

		}

		private void scrollPathToVisible(TreePath treePath  , 
																		 int rowsFromBottom) {
				int row = getRowForPath(treePath);
				int rowCount = getActualVisibleRowCount();
				int scrollToRow = row;
				if ( topRow < row ) 
						scrollToRow = row+(int)(rowCount/2);
				else 
						scrollToRow = row - (int)(rowCount/2);
				if ( scrollToRow >= getRowCount() )
						scrollToRow = getRowCount()-1;
				else if ( scrollToRow < 0) 
						scrollToRow = 0;
 				scrollRowToVisible(scrollToRow);
		}
		public int getActualVisibleRowCount() {
				Rectangle visibleRect = getVisibleRect();
				// upper corner
				topRow = getClosestRowForLocation((int)visibleRect.getX(), (int)visibleRect.getY());
				bottomRow = 
						getClosestRowForLocation((int)(visibleRect.getX()+visibleRect.getWidth()), 
															(int)(visibleRect.getY()+visibleRect.getHeight()));
				return bottomRow - topRow;
				// System.out.println("VisibleRect " +
				// 													 getVisibleRect()
				// 													 + " rowBounds " + getRowBounds(row));
				// 		Rectangle          bounds = getPathBounds(path);
				
				// 				if(bounds != null) {
				// 						scrollRectToVisible(bounds);
				// 						if (accessibleContext != null) {
				// 								((AccessibleJTree)accessibleContext).fireVisibleDataPropertyChange();
				// 						}
				// 				}
		}
		private boolean isRowVisible(int row) {
				Rectangle visibleRectangle = getVisibleRect();
				Rectangle rowBounds = getRowBounds(row);
// 				System.out.println("VisibleRect " +
// 													 getVisibleRect()
// 													 + " rowBounds " + getRowBounds(row));
				return visibleRectangle.contains(rowBounds.getX(),
																				 rowBounds.getY(),
																				 5.0,
																				 rowBounds.getHeight());
		}
		public GenericTreeNode getSelectedNode() {
				Vector selected = getSelectedNodes();
				if ( selected.size() > 0 )
						return (GenericTreeNode)selected.firstElement();
				else
						return null;
		}
		public Vector getSelected() {
				TreePath[] treePaths = getSelectionPaths();
				Vector selected = new Vector();
				// Convert paths into an vector of user objects
				if (treePaths == null ) 
						return selected;
				for ( int i = 0; i < Array.getLength(treePaths); i++) {
						GenericTreeNode node =
								(GenericTreeNode)treePaths[i].getLastPathComponent();
						selected.addElement(node.getUserObject());
				}
				return selected;
		}
		public Vector getSelectedNodes() {
				TreePath[] treePaths = getSelectionPaths();
				Vector selected = new Vector();
				// Convert paths into an vector of user objects
				if (treePaths == null ) 
						return selected;
				for ( int i = 0; i < Array.getLength(treePaths); i++) {
						GenericTreeNode node =
								(GenericTreeNode)treePaths[i].getLastPathComponent();
						selected.addElement(node);
				}
				return selected;
		}
		public Vector getSelectedParents() {
				TreePath[] treePaths = getSelectionPaths();
				Vector selected = new Vector();
				// Convert paths into an vector of user objects
				if (treePaths == null ) 
						return selected;
				for ( int i = 0; i < Array.getLength(treePaths); i++) {
						int pathCount  = treePaths[i].getPathCount();
						if ( pathCount > 1 ) {
								GenericTreeNode node =
										(GenericTreeNode)treePaths[i].getPathComponent(pathCount-2);
								selected.addElement(node.getUserObject());
						}
						else 
								selected.addElement(null);		
				}
				return selected;
		}
		public Object getLastSelectedUserObject() {
				TreePath treePath = getSelectionPath();
				//System.out.println("getLastSelectedUserObject " + treePath);
				if (treePath == null) {
						// create an object equivalent to the root node type
						return null;
				}
				else {
						GenericTreeNode node =
								(GenericTreeNode)treePath.getLastPathComponent();
						return node.getUserObject();
				}
		}

	
		private void removeNodesFromPersistibles() {
				// Visit each node 
				removeNodeFromPersistibles(rootNode);
		}

		private void removeNodeFromPersistibles(GenericTreeNode node) {
				//System.out.println("removing node from persistible " + node);
				// If node user object is a TreeUserObject - remove node
				TreeUserObject userObject = null;
				if ( node.getUserObject() instanceof TreeUserObject ) {
						userObject = (TreeUserObject)node.getUserObject();
						// node is visited exactly once
						userObject.removeTreeNode(node);
				}
        if (node.getChildCount() >= 0) {
            for (Enumeration e=node.children(); e.hasMoreElements(); ) {
                GenericTreeNode n = (GenericTreeNode)e.nextElement();
                removeNodeFromPersistibles(n);
            }
        }
    }
	
		public boolean getExpandState() { 
				return expandState;
		}
		public void setExpandState(boolean state) {
				expandState = state;
				expandAll(state);
		}

		public void expandAll() {
				expandAll(getTree(), true);
		}
		public void expandAll(boolean state) {
				expandAll(getTree(), state);
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

		public void collapseAll() {
				expandAll(getTree(), false);
		}



		public void removeNode(TreeNode node) {

		}

		public void hideType() {
				// Get list of objects of type and mark nodes isVisibleNode(false)
				// Get a list of paths starting with nodes directly beneath root node
				// Go through each path and skip nodes until you get a visible and 
				// that nodes parent to current node then make current node the 
				// the node that was just rest and recursively continue
		}




		// Returns a TreePath containing the specified node.
    public TreePath getPath(GenericTreeNode node) {
        java.util.List list = new ArrayList();
    
        // Add all nodes to list
        while (node != null) {
            list.add(node);
            node = (GenericTreeNode)node.getParent();
        }
        Collections.reverse(list);
				if ( list.size() > 0 ) {
						//Convert array of nodes to TreePath
						return new TreePath(list.toArray());
				}
				else
						return null;
    }
		
		public GenericTreeNode findString(String searchWord) {
				GenericTreeNode matchNode = findString(this, searchWord);
				if (matchNode == null) // deselect everything so next search will wrap
						clearSelection();
						
				return matchNode;
		}

		public GenericTreeNode getRootNode() {
				return (GenericTreeNode)getModel().getRoot();
		}

    // Find a string in  tree
    public GenericTreeNode findString(JTree tree, String searchWord) {
//         GenericTreeNode root = (GenericTreeNode)tree.getModel().getRoot();
				TreePath currentPath = getSelectionPath();
				GenericTreeNode currentNode = null;
				if ( currentPath == null )
						currentNode = (GenericTreeNode)tree.getModel().getRoot();
				else {
						currentNode = getNextNode(currentPath);
				}
        return findString(currentNode, searchWord);
    }

		public GenericTreeNode findString(GenericTreeNode node, 
																			String searchWord) {
				// Step thru the tree starting at node
				while ( node != null) {
						String listWord = node.getUserObject().toString();
						if ( listWord!= null && 
								 listWord.toLowerCase().indexOf(searchWord.toLowerCase()) > -1 ) {
								// match found
								setSelectionPath(getPath(node));
								// System.out.println("find string getSelecteionPath " + listWord
// 																	 + " " + searchWord
// 																	 + "selectionPath " + getSelectionPath());
								scrollPathToVisible(getSelectionPath(), 5);
								return node;
						}
						else {
								node = getNextNode(getPath(node));
						} 
				}
				return null;
		}
		// Drop listener
		public void dragGestureRecognized(DragGestureEvent dge) {
				//System.out.println("drag gesture recognized " + getSelectionPath());
		}
		public void dragEnter(DropTargetDragEvent dtde) {}
		public void dragExit(DropTargetEvent dte) {}
		public void dragOver(DropTargetDragEvent dtde) {}
		public void drop(DropTargetDropEvent dtde) {
 				//System.out.println("Dropping it");
				Point pt = dtde.getLocation();
				TreePath dropPath = this.getClosestPathForLocation(pt.x, pt.y);
				Transferable t = clipboard.getContents(clipboard);
				try {
										// 	System.out.println("Drop " 
// 						 															 + t
// 						 															 + " at " + dropPath);
						DataFlavor flavors[] = t.getTransferDataFlavors();
						JFrame filterTreeFrame = new JFrame("Add object to filter tree (Not implemented)");
						filterTreeFrame.getContentPane().add
								(new JLabel
								 ("Code to determine if node is droppable at locatin not implemented yet"));
						filterTreeFrame.setSize(400,200);
						filterTreeFrame.setVisible(true);
						
						// for ( int i = 0; i < Array.getLength(flavors); i++) {
						// 								System.out.println(flavors[i]);								
						// 						}

				} catch(Exception ufe) {
						ufe.printStackTrace();
				}
				
		}
		public void dropActionChanged(DropTargetDragEvent dtde) {}

		public void objectSaved(SaveEvent e) {
			// 	if ( !(e.getSavedObject() instanceof oncotcap.datalayer.persistible.StatementTemplate) 
// 						 )  {
// 						refresh();
// 				}
// 				else if ( (e.getSavedObject() instanceof 
// 									 oncotcap.datalayer.persistible.StatementTemplate) &&
// 									isFocusOwner() ) 
				refresh();
				//refresh(e.getSavedObject());
		}

		public void objectDeleted(SaveEvent e) {
				// Reset selection
				//System.out.println("object deleted " + e);
		}
		
		private GenericTreeNode getNextNode(TreePath path) {
				GenericTreeNode nextNode = null;
				int currentRow = getRowForPath(path);
				TreePath nextPath = getPathForRow(currentRow+1);
				if ( nextPath != null ) {
						nextNode = 
								(GenericTreeNode)nextPath.getLastPathComponent();
				}
				return nextNode;
		}

		public void adjustmentValueChanged(AdjustmentEvent e) {
				repaint();
		}
		public void setOntologyTree(OntologyTree ot) {
				ontologyTree = ot;
		}
		public OntologyTree getOntologyTree() {
				// if this tree is on an ontologytree
				return ontologyTree;
		}
		public void setName(String name) {
				this.name = name;
		}
		public String getName() {
				return this.name;
		}
		public String toString() {
				return this.name;
		}

		public static GenericTree getTreeForNode(DefaultMutableTreeNode node) {
				Iterator i = genericTrees.iterator();
				while ( i.hasNext() ) {
						GenericTree tree = (GenericTree)i.next();
						if ( tree.getRootNode().isNodeRelated(node) )
								return tree;
				}
				return null;
		}
		
		// implement container listener
		public void componentAdded(ContainerEvent e){
				//System.out.println("Component added");
		}
		public void componentRemoved(ContainerEvent e){
				//System.out.println("Component removed");
		}
}



// 				// Convert the treePath into a vector of guids -skip root
// 				// Convert the tree path into an ordered list of user objects
// 				if ( treePath == null ) 
// 						return null;
// 				for ( int i = 0; i < Array.getLength(treePath.getPath()); i++) {
// 						Object userObject = 
// 								((GenericTreeNode)treePath.getPathComponent(i)).getUserObject();
// 						userObjectPath.addElement(userObject);
// 						if ( userObject instanceof AbstractPersistible) {
// 								System.out.println("matchPath userObject " + userObject
// 																	 + " guid " 
// 																	 + ((AbstractPersistible)userObject).getGUID()
// 																	 + " tree nodes " 
// 																	 + ((AbstractPersistible)userObject).getTreeNodes());
// 								guidPath.addElement
// 										( ((AbstractPersistible)userObject).getGUID());
// 						}
// 				}
// 				System.out.println("guidPath " + guidPath);
// 				GenericTreeNode selectedNode = null;

