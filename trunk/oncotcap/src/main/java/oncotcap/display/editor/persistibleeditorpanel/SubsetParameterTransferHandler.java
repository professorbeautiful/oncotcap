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
import oncotcap.display.browser.OncBrowser;
import oncotcap.display.browser.GenericTreeNodeRenderer;
import oncotcap.display.common.DragDropLabel;
import oncotcap.display.common.Droppable;
import oncotcap.display.common.OncTreeNode;
import oncotcap.display.editor.EditorFrame;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.parameter.*;

import javax.swing.tree.*;

class SubsetParameterTransferHandler extends BooleanTreeTransferHandler
		implements Transferable {
		public static DataFlavor NODE_FLAVOR = 
				new DataFlavor(OncTreeNode.class, "TreePath");
		private static final DataFlavor nodeFlavor [] = { NODE_FLAVOR };
		private BooleanTree booleanTree = null;

		public boolean importData(JComponent c, Transferable t) {
				try	{
						if ( !(c instanceof BooleanTree) ) {
								System.out.println("This BooleanTreeTransferHandler "
																	 + "is attached to wrong type of component " 
																	 + c);
								return false;
						}
						
						booleanTree = (BooleanTree)c;
						OncTreeNode dropTarget = 
								(OncTreeNode) booleanTree.getSelectionPath().getLastPathComponent();
					// 	System.out.println("dropTarget " + dropTarget.getClass()
// 															 + " SubsetParameterEditorPanel " + 
// 															 getEditor(c)
// 															 + " root node " + booleanTree.getRootNode().getClass());

						boolean persistMode = ((EditorPanel) getEditor(c)).getSaveToDataSourceOnCreate();
						if ( t.isDataFlavorSupported(Droppable.genericTreeNode) &&
								 t.getTransferData(Droppable.genericTreeNode)  != null ) {
								if ( booleanTree.isRootNode(dropTarget.getUserObject())
										 && !(getEditor(c) instanceof InstantiationParameterEditor)
										 && !(getEditor(c) instanceof AssignmentParameterEditor)
										 ) {
										JOptionPane.showMessageDialog
												((JFrame)null, 
												 "<html>"
												 + "Please provide an operator to build an expression. "
												 + "<br>Please remove a node before adding another"
												 + "</html>");
										return false;
								}
								GenericTreeNode genericTreeNode = (GenericTreeNode)t.getTransferData(Droppable.genericTreeNode);
						
							// 	System.out.println("genericTreeNode " 
// 																	 + genericTreeNode.getUserObject().getClass()  + " " 
// 																	 + genericTreeNode.getClass() );

								if ( genericTreeNode.getUserObject() instanceof BooleanExpression )
								{
										BooleanExpression droppedExpr =
												(BooleanExpression)genericTreeNode.getUserObject();
										//if ( allowOne && booleanTree.getNodeCount() < 1 ) {
										// Create a boolean expression using the keyword
										BooleanExpression bool = new BooleanExpression(persistMode);
										Keyword keyword = (Keyword)droppedExpr.getLeftHandSide();
										bool.setRightHandSide(droppedExpr.getRightHandSide());
										// System.out.println("boolean expression " 
										// 																			 + bool );	
										EnumDefinition enumDefinition = 
												new EnumDefinition(keyword.toString(), persistMode);
										enumDefinition.setKeyword(keyword);
											// System.out.println("Enum Definition " 
// 																													  + enumDefinition );	
										enumDefinition.setLevelList
												((EnumLevelList)droppedExpr.getRightHandSide());
										bool.setLeftHandSide(enumDefinition);
										if ( getEditor(c) 
															instanceof AssignmentParameterEditor) {
												bool.setExpressionType
														(DeclareEnumPicker.ASSIGNMENT);
										}
										else if ( getEditor(c) 
															instanceof InstantiationParameterEditor) {
												bool.setExpressionType
														(DeclareEnumPicker.INSTANTIATION);
										}
										else if ( getEditor(c) instanceof SubsetParameterEditorPanel) {
												bool.setExpressionType
														(DeclareEnumPicker.BOOLEAN_EXPRESSION);
										}
										// System.out.println("boolean expression "  
// 																			 + bool );	
										OncTreeNode newOncTreeNode = 
												new OncTreeNode(bool, (persistMode ? Persistible.DIRTY : Persistible.DO_NOT_SAVE));
								// 		System.out.println("newOncTreeNode " 
// 																			 + newOncTreeNode );	
										getInfo(bool);
									// 	System.out.println("going to drop a class " 
// 																			 + " >> " + dropTarget + " >> " 
// 																			 + newOncTreeNode );	
										boolean didItDrop = booleanTree.insertIntoTree(dropTarget, 
																																			 newOncTreeNode);
								// 		System.out.println("dropped a class " + didItDrop 
// 																			 + " >> " + dropTarget + " >> " 
// 																					 + newOncTreeNode );	
										if ( didItDrop == true ) {
												bool.addSaveListener(booleanTree);
												Container subsetPanel = 
														booleanTree.getParent().getParent().getParent
														().getParent().getParent();
												if ( subsetPanel instanceof SubsetParameterEditorPanel)
														enumDefinition.setParametersContainingMe
																((Parameter)((SubsetParameterEditorPanel)subsetPanel).param);
										}

										return didItDrop;
								}
								else if ( genericTreeNode.getUserObject() 
													instanceof Keyword ) {
										 Keyword keyword =
												(Keyword)genericTreeNode.getUserObject();
										//if ( allowOne && booleanTree.getNodeCount() < 1 ) {
										// Create a boolean expression using the keyword
										BooleanExpression bool = new BooleanExpression(persistMode);
										System.out.println("boolean expression " 
										 																			 + bool );	
										EnumDefinition enumDefinition = 
												new EnumDefinition(keyword.toString(), persistMode);
										enumDefinition.setKeyword(keyword);
										System.out.println("Enum Definition " 
					 						  + enumDefinition );	
									
										
										if ( getEditor(c) 
															instanceof AssignmentParameterEditor) {
												bool.setExpressionType
														(DeclareEnumPicker.ASSIGNMENT);
										}
										else if ( getEditor(c) 
															instanceof InstantiationParameterEditor) {
												bool.setExpressionType
														(DeclareEnumPicker.INSTANTIATION);
										}
										else if ( getEditor(c) instanceof SubsetParameterEditorPanel) {
												bool.setExpressionType
														(DeclareEnumPicker.BOOLEAN_EXPRESSION);
										}
										System.out.println("boolean expression "  
 																			 + bool );	
										OncTreeNode newOncTreeNode = 
												new OncTreeNode(bool, (persistMode ? Persistible.DIRTY : Persistible.DO_NOT_SAVE));
										bool.setLeftHandSide(enumDefinition);
	
										getInfo(bool);
									// 	System.out.println("going to drop a class " 
// 																			 + " >> " + dropTarget + " >> " 
// 																			 + newOncTreeNode );	

										boolean didItDrop = booleanTree.insertIntoTree
												(dropTarget, 
												 newOncTreeNode);
											return didItDrop;
								}

								return false;
								
						}
						if (t.isDataFlavorSupported(Droppable.oncTreeNode)){
								System.out.println("oncTreeNode");
								OncTreeNode dropNode = 
										(OncTreeNode)t.getTransferData(Droppable.oncTreeNode);
								if ( dropNode != dropTarget) {
										return(booleanTree.insertIntoTree(dropTarget, dropNode));
								}
								// Don't drop a node on itself - ignore
								return false;
						}
						if(t.isDataFlavorSupported(Droppable.droppableData))
								{
								System.out.println("droppabledata");
										OncTreeNode str = 
												new OncTreeNode((Droppable) t.getTransferData(Droppable.droppableData), 
																				(persistMode ? Persistible.DIRTY : Persistible.DO_NOT_SAVE));
										//	System.out.println("droppable " + str);
										return(booleanTree.insertIntoTree(dropTarget, str));
								}
						else if (t.isDataFlavorSupported
										 (Droppable.ontologyObjectName)) {
								System.out.println("ONTOLOGY OBJECT NAME");
								// Drop a class on the filter tree 
								OncTreeNode str = new OncTreeNode(t.getTransferData (Droppable.ontologyObjectName), (persistMode ? Persistible.DIRTY : Persistible.DO_NOT_SAVE));
								boolean didItDrop = booleanTree.insertIntoTree(dropTarget, str);
							// 	System.out.println("dropped a class " + didItDrop 
// 																	 + " >> " + dropTarget + " >> " + str);
								
								return(didItDrop);
						}
						else if( t.isDataFlavorSupported(NODE_FLAVOR))
								{
										OncTreeNode droppedNode = 
												(OncTreeNode) t.getTransferData(NODE_FLAVOR);
								System.out.println("NODE_FLAVOR");
								OncTreeNode newNode = null;
								try {
										newNode = (OncTreeNode) droppedNode.clone();
								} catch(Exception eee ) {
										eee.printStackTrace();
								}
								System.out.println("NODE_FLAVOR");
									boolean didItDrop = 
											booleanTree.insertIntoTree(dropTarget, newNode);
									System.out.println("didItDrop " + didItDrop);
									return(didItDrop);
								}
						else {
								System.out.println("None of the above");
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
						return(false);
				}
		}
// 		public Transferable createTransferable(JComponent c)
// 		{
// 				System.out.println("createTransferable SPTH : " + c);
// 				if(c instanceof JTree) {
// 						TreePath path = ((JTree)c).getSelectionPath();
// 						DefaultMutableTreeNode selectedNode = 
// 								(DefaultMutableTreeNode)path.getLastPathComponent();
// 						System.out.println("create transferable " + selectedNode.getClass());

// 						if ( selectedNode instanceof Transferable) {
// 								return((Transferable)selectedNode);
// 						}
// 						else
// 								return null;
// 				}
// 				else
// 						return(null);
// 		}
		private Object getEditor(JComponent c) {
				return c.getParent().getParent().getParent().getParent().getParent();
		}

		private void getInfo(BooleanExpression expr) {
			// 	System.out.println("FOCUS " + OncBrowser.getOncBrowser().getFocusOwner() + " -- " 
// 													 + OncBrowser.getOncBrowser().getMostRecentFocusOwner());
				EditorPanel editorPanel =
						EditorFrame.showEditor((Editable)expr, 
																	 null,  0, 0, booleanTree);
				expr.addSaveListener(editorPanel);
				
				
		}

}
