package oncotcap.display.browser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.util.*;
import java.io.*;
import java.lang.reflect.Array;
		
import oncotcap.util.*;
import oncotcap.datalayer.OncoTCapDataSource;
import oncotcap.datalayer.Persistible;
import oncotcap.datalayer.AbstractPersistible;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;

import javax.swing.tree.*;

public class GenericTreeTransferHandler extends TransferHandler 
			{
		private GenericTreeNode oldNode = null;
		private GenericTree tree = null;
		public static DataFlavor NODE_FLAVOR = 
				new DataFlavor(GenericTreeNode.class, "Generic Tree Node");
		private static final DataFlavor nodeFlavor [] = { NODE_FLAVOR };

					private int pasteAction = COPY; // default action

		public GenericTreeTransferHandler(GenericTree tree) {
				this.tree = tree;
		}
		public int getSourceActions(JComponent c)
		{
			return(COPY_OR_MOVE);
		}

		public boolean importData(JComponent c, Transferable t)
		{
				System.out.println("importData " + c + 
													 " - " + tree); 
				// If any of the pastes are successful return successful
				// otherwise return false
				boolean status = false;
				try
						{
								// Accomadate to many
								TreePath []  selectedPaths = tree.getSelectionPaths();
								if (selectedPaths == null ) {
										// select the root node
										selectedPaths = (TreePath [])Array.newInstance(TreePath.class, 1);
										selectedPaths[0] = 
												new TreePath(((DefaultMutableTreeNode)tree.getModel().getRoot()).getPath());
								}
								
								for ( int i = 0; i < Array.getLength(selectedPaths); i++) {
										GenericTreeNode dropTarget = 
												(GenericTreeNode)selectedPaths[i].getLastPathComponent();
									// 	System.out.println("Transferrable " + t.getTransferDataFlavors());
// 										System.out.println("Component " + c );
// 										System.out.println("pasteAction " + pasteAction + " " 
// 																			 + Array.getLength(selectedPaths) + "(" + 
// 																			 i + ") " + selectedPaths[i]);
										if (t.isDataFlavorSupported(Droppable.transferableList)) {
												Vector transferableList = 
														(Vector)t.getTransferData
														(Droppable.transferableList);
												Iterator ii = transferableList.iterator();
												boolean tStatus = false;
												// Make sure to put a try catch on inner most block so 
												// it doesn't bail when one of the drops is 
												// unsuccessful
												while ( ii.hasNext() ) {
														Object transferObj = ii.next();
														if ( transferObj instanceof GenericTreeNode) {
																// Copy/Move item
																if ( pasteAction == MOVE ) {
																		//System.out.println("Moving");
																		tStatus = 
																				((GenericTree)c).moveNode
																				((GenericTreeNode)transferObj, dropTarget);
																}
																else {
																		//System.out.println("Copying");
																	
																		tStatus = ((GenericTree)c).copyNode
																				((GenericTreeNode)transferObj, dropTarget);
																}
														}
												}
										}
										else if ( t.isDataFlavorSupported(Droppable.genericTreeNode) ) {
												Object transferableData = 
														t.getTransferData(Droppable.genericTreeNode);
												boolean tStatus = false;
												if ( c instanceof GenericTree ) {
														// If not dragged from within this tree add 
														// a new node for the object or copy to an 
														// existing node with the same guid
														GenericTreeNode transferNode = 
																(GenericTreeNode)transferableData;
														OntologyTree ot1 = ((GenericTree)c).getOntologyTree();
														OntologyTree ot2 = null;
														// Find out where this node came from 
														GenericTree gt = 
																GenericTree.getTreeForNode(transferNode);
														if ( gt != null ) 
																ot2 = gt.getOntologyTree();
														// Determine if this is from an ontology tree
														if ( ot1 != null && ot2 != null 
																 && ot1 != ot2 ) {
																System.out.println(" ot1 " + 
																									 ot1.getName());
																System.out.println(" ot2 " + 
																									 ot2.getName());
																Object obj = 
																		transferNode.getUserObject();
																GUID guid = ((Persistible)obj).getGUID();
																//see if this object exists in in both data sources
																OncoTCapDataSource pds1 = ot1.getDataSource();
																OncoTCapDataSource pds2 = ot2.getDataSource();
																Persistible p1 = 
																		AbstractPersistible.getPersistible(guid,
																																			 pds1);
																Persistible p2 = 
																		AbstractPersistible.getPersistible(guid,
																																			 pds2);
																// if ( p1 != null ) 
// 																		System.out.println("p1 " + p1.hashCode() );
// 																if ( p2 != null )
// 																		System.out.println("p2 " + p2.hashCode());
														}													
												}
												// is this a copy/ paste or a cut/paste
												//javax.swing.text.DefaultEditorKit.pasteAction
												if ( pasteAction == MOVE ) {
														tStatus = 
																((GenericTree)c).moveNode
																((GenericTreeNode)transferableData, dropTarget);
												}
												else {
														tStatus = ((GenericTree)c).copyNode
																((GenericTreeNode)transferableData, dropTarget);
												}
												if ( tStatus == true ) 
														status = true;
										}
										
								}
								return(status);
						}
				catch(UnsupportedFlavorException e) {
						System.out.println("Unsupported Flavor"); return(false);}
				catch(IOException e){
						System.out.println("IO Exception"); return(false);}
		}

		public void exportAsDrag(JComponent comp, InputEvent e, int action)
		{
			super.exportAsDrag(comp, e, action); 
		}
		public void exportToClipboard(JComponent comp, Clipboard clip, int action)
		{
				// IS COMP same as TREE
				if ( comp == tree ) 
						System.out.println( "COMP " + comp + "tree "  + tree);	
				pasteAction = action;
				super.exportToClipboard(tree, clip, action);

		}


		public Transferable createTransferable(JComponent c)
		{
				if(c instanceof JTree) {
						TreePath[] paths = ((JTree)c).getSelectionPaths();
						// If copy/move pasting multiple items
						if ( paths != null && paths.length > 1 ) {
								Vector nodeList  = new Vector();
								for ( int i = 0; i < paths.length; i++) {
										System.out.println("creating transferables " 
																			 + i + ": " + paths[i]);
										DefaultMutableTreeNode selectedNode = 
												(DefaultMutableTreeNode)paths[i].getLastPathComponent();
										if ( selectedNode instanceof GenericTreeNode) {
												// depending on whether its a move or copy 
												// copy the nodes and attach the user object or 
												// actually put the existing object on the 
												// transferable list and once its parent is changed
 												nodeList.add(selectedNode);
										} 
								}
								return new TransferableList(nodeList);
						}
						else {
								TreePath path = ((JTree)c).getSelectionPath();
								DefaultMutableTreeNode selectedNode = 
										(DefaultMutableTreeNode)path.getLastPathComponent();
								if ( selectedNode instanceof Transferable) {
										return((Transferable)selectedNode);
								}
								else
										return null;
						}
				}
			else
				return(null);
		}

		protected void exportDone(JComponent c, Transferable t, int action)
		{
				System.out.println("action is " + action);
				if ( action == MOVE) {
				}
				else if ( action == COPY ) {
				}
				pasteAction = action; //TODO: may not need this statement anymore
		}

		public boolean canImport(JComponent c, DataFlavor[] flavors)
		{
			for(int n = 0; n<flavors.length; n++)
				if ( flavors[n].equals(Droppable.droppableData) 
					 || flavors[n].equals(NODE_FLAVOR)
						 || flavors[n].equals(Droppable.transferableList) )
					return(true);

			return(false);
		}
		public DataFlavor [] getTransferDataFlavors()
		{
			return(nodeFlavor);
		}
		public boolean isDataFlavorSupported( DataFlavor flavor)
		{
					System.out.println("flavor isDataFlavorSupported " + flavor);

			return( flavor.equals(NODE_FLAVOR));
		}
	
	}  
