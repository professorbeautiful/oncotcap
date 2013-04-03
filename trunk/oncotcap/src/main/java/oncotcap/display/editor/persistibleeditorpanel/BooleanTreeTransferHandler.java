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
import oncotcap.datalayer.SearchText;
import oncotcap.display.browser.GenericTreeNode;
import oncotcap.display.browser.GenericTreeNodeRenderer;
import oncotcap.display.common.DragDropLabel;
import oncotcap.display.common.Droppable;
import oncotcap.display.common.OncTreeNode;
import oncotcap.display.common.GenericTextInputPanel;
import oncotcap.display.editor.EditorFrame;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.parameter.*;

import javax.swing.tree.*;

public class BooleanTreeTransferHandler extends TransferHandler 
		implements Transferable {
		public static DataFlavor NODE_FLAVOR = 
				new DataFlavor(OncTreeNode.class, "TreePath");
		private static final DataFlavor nodeFlavor [] = { NODE_FLAVOR };
		private OncTreeNode oldNode = null;
		private JComponent targetComponent = null;

		
		public int getSourceActions(JComponent c)
		{
				return(COPY_OR_MOVE);
		}

		public boolean importData(JComponent c, Transferable t) {
				DataFlavor[] flv = t.getTransferDataFlavors();
// 				for ( int i = 0; i < flv.length; i++) {
// 						System.out.println("FLAVS "+ flv[i]);
// 				}
// 				System.out.println("Component and Transferable BTTH" + 
// 													 c + " " 
// 													 + t
// 													 + " FLAVORS " + t.getTransferDataFlavors()
// 													 + " t.isDataFlavorSupported(Droppable.searchText) " 
// 													 + t.isDataFlavorSupported(Droppable.searchText));
				try	{
						if ( !(c instanceof BooleanTree) ) {
								System.out.println("This BooleanTreeTransferHandler "
																	 + "is attached to wrong type of component " 
																	 + c);
								return false;
						}
						
						BooleanTree booleanTree = (BooleanTree)c;
						OncTreeNode dropTarget = 
								(OncTreeNode) booleanTree.getSelectionPath().getLastPathComponent();
						
						if ( t.isDataFlavorSupported(Droppable.genericTreeNode) ) {
								GenericTreeNode genericTreeNode = 
										(GenericTreeNode)t.getTransferData(Droppable.genericTreeNode);
								//System.out.println("genericTreeNode " + genericTreeNode );
								OncTreeNode newOncTreeNode = 
										new OncTreeNode(genericTreeNode.getUserObject() );
								// If this is an enum level need to find out what 
								// enum it is connected to
								boolean didItDrop = booleanTree.insertIntoTree(dropTarget, 
																															 newOncTreeNode);
								// 		System.out.println("dropped a class " + didItDrop 
								// 																 + " >> " + dropTarget + " >> " 
								// 																 + newOncTreeNode );	
								return didItDrop;
								
						}
						else if(t.isDataFlavorSupported(Droppable.oncTreeNode))
								{
										//System.out.println("btth oncTreeNode " );
										OncTreeNode str = (OncTreeNode)	t.getTransferData
												(Droppable.oncTreeNode);
										if ( str != dropTarget) {
												return(booleanTree.insertIntoTree(dropTarget, str));
										}
										return false;
								}
						else if(t.isDataFlavorSupported(Droppable.droppableData))
								{
										//System.out.println("droppabledata " );
										OncTreeNode str = 
												new OncTreeNode((Droppable) t.getTransferData
																				(Droppable.droppableData));
										return(booleanTree.insertIntoTree(dropTarget, str));
								}
					
						else if (t.isDataFlavorSupported
										 (Droppable.ontologyObjectName)) {
								// Drop a class on the filter tree 
								OncTreeNode str = 
										new OncTreeNode(t.getTransferData
																		(Droppable.ontologyObjectName));
								boolean didItDrop = booleanTree.insertIntoTree(dropTarget, str);
								
								return(didItDrop);
						}
						else if( t.isDataFlavorSupported(Droppable.searchText))
								{
										//System.out.println(" Droppable.searchText " );
										SearchText searchText = 
												(SearchText)t.getTransferData(Droppable.searchText);
										getMoreInfo(searchText);
										OncTreeNode droppedNode = 
												new OncTreeNode(searchText);
										return(booleanTree.insertIntoTree(dropTarget, droppedNode));
								}
						else if( t.isDataFlavorSupported(NODE_FLAVOR))
								{
										OncTreeNode droppedNode = 
												(OncTreeNode) t.getTransferData(NODE_FLAVOR);
									
										OncTreeNode newNode = (OncTreeNode) droppedNode.clone();
										return(booleanTree.insertIntoTree(dropTarget, newNode));
								}
						else {
 								//System.out.println("None of the above");
								return(false);
						}
				}
				catch(UnsupportedFlavorException e){
						System.out.println("Unsupported Flavor"); 
						e.printStackTrace();
						return(false);
				}
				catch(IOException e){
						System.out.println("IO Exception"); 
						e.printStackTrace();
						return(false);
				}
		}
		
		private void getMoreInfo(SearchText text) {
				GenericTextInputPanel textPanel = 
						new GenericTextInputPanel((Frame)oncotcap.display.browser.OncBrowser.getOncBrowser(), 
																	"Please enter search string", 
																	"Input search string");
				text.setText(textPanel.getValidatedText());
		}

		public void exportAsDrag(JComponent comp, InputEvent e, int action)
		{
				// if ( !(comp instanceof BooleanTree) ) {
// 						System.out.println("This BooleanTreeTransferHandler "
// 															 + "is attached to wrong type of component " 
// 															 + comp);
// 						return;
// 				}
// 				System.out.println("Is this making it here");
				super.exportAsDrag(comp, e, action); 
		}
		public void exportToClipboard(JComponent comp, Clipboard clip, int action)
		{
				if ( !(comp instanceof BooleanTree) ) {
						System.out.println("This BooleanTreeTransferHandler "
															 + "is attached to wrong type of component " 
															 + comp);
						return;
				}

				super.exportToClipboard((BooleanTree)comp, clip, action);
		}

		
		public Transferable createTransferable(JComponent c)
		{
// 			System.out.println("createTransferable BTTH : " + c);
			if(c instanceof JTree)
			{
				return(this);
			}
			else
				return(null);
		}
		protected void exportDone(JComponent c, Transferable t, int action)
		{
	// 			System.out.println("exportDone " + action + " oldNode " + oldNode 
// 													 + " COMPONENT" + c + " -- " + t);
			if(action == MOVE && c instanceof JTree && oldNode != null)
			{
				DefaultTreeModel model = (DefaultTreeModel) ((JTree) c).getModel();
				if(! isRootNode(oldNode))
					model.removeNodeFromParent(oldNode);
			}
		}
		private boolean isRootNode(Object obj)
		{
			return(OncFilter.rootFilterObj.equals(obj));
		}
		public boolean canImport(JComponent c, DataFlavor[] flavors)
		{
				for(int n = 0; n<flavors.length; n++) {

						if(flavors[n].equals(Droppable.droppableData)
							 || flavors[n].equals(NODE_FLAVOR)
							 || flavors[n].equals(Droppable.searchText)
							 || flavors[n].equals(Droppable.ontologyObjectName)) {
								return(true);
						}
				}						
				
				return(false);
		}
		public DataFlavor [] getTransferDataFlavors()
		{
// 				System.out.println("	getTransferDataFlavors BTTH");
			return(nodeFlavor);
		}
		public boolean isDataFlavorSupported( DataFlavor flavor)
		{
			return( flavor.equals(NODE_FLAVOR));
		}
		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
		{
			if (isDataFlavorSupported(flavor))
				return(oldNode);
			else
				throw(new UnsupportedFlavorException(flavor));
		}

		public void setTargetComponent(JComponent c) {
				this.targetComponent = c;
		}

		public JComponent getTargetComponent() {
				return targetComponent;
		}


	}
