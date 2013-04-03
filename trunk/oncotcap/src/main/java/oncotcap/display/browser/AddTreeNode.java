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
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.ListDialog;
import oncotcap.display.editor.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.util.*;
import java.awt.dnd.*;


/// Inner class
public class AddTreeNode extends OncAbstractAction {

		static public  final int  NONE = 0;
		static public  final int CONNECTED_CLASSES = 1;
		static public  final int KEYWORD_ONLY = 2;
		private String actionName = null;
		private boolean isNew = false;
		private boolean isModal = false;
		private String createClassName = null;
		GenericTree tree = null;
		Object source = null;
		boolean link = false;
		int typeOfClasses = NONE;
		OntologyButtonPanel ontologyButtonPanel = null;
		private String addLinkPanelMsg = 
				"Select Class Type to create new linked item OR Select an existing instance to link";
		
		public AddTreeNode(String actionName) {
				super(actionName, null);
				this.actionName = actionName;
				// Set an accelerator key; this value is used by menu items
				putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control F2"));
		}
		public AddTreeNode(String actionName, boolean link) {
				super(actionName, null);
				this.actionName = actionName;
				this.link = link;
				// Set an accelerator key; this value is used by menu items
				putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control F2"));
		}
		public AddTreeNode(String actionName, boolean link, 
											 int typeOfClasses) {
				super(actionName, null);
				this.actionName = actionName;
				this.link = link;
				this.typeOfClasses = typeOfClasses;
				// Set an accelerator key; this value is used by menu items
				putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control F2"));

		}
		public void isModal(boolean m) {
				isModal = m;
				//System.out.println("isModal " + isModal);

		}

		public void actionPerformed(ActionEvent e) {
				setSource(getBrowserActionSource(e));
				setTree(getBrowserActionTree(getSource()));
				setOntologyButtonPanel(getBrowserActionOBP(getSource()));
				isNew = false;
				Class editableClass = null;
				Class newObjectClass = null;
				boolean showEditor = true;
				
				// OncScrollList and GenericTree  and OncScrollTable
				Vector selectedItems = null;
				Vector addTypes = null;
				// Determimne what you are linking to 
				//				System.out.println(" Add Node " + e);
				if ( link && source instanceof OncScrollList) {
						Collection coll = ((OncScrollList)source).getLinkTos();
						//						System.out.println(" coll is " + coll);
						if ( coll != null ) 
								selectedItems = new Vector(coll);
						else 
								selectedItems = new Vector();
						
				}
				else if ( link && source instanceof OntologyTree) {
						selectedItems = tree.getSelected(); 
						//OncBrowser.getTree().getSelected();
				}
				else if ( link && source instanceof OncScrollTable) {
						Collection coll = ((OncScrollTable)source).getLinkTos();
						if ( coll != null ) 
								selectedItems = new Vector(coll);
						else 
								selectedItems = new Vector();
				}
				
				if ( source instanceof OntologyTree ) {  
						// FUTURE put an interface on 
								// OncScrollList and GenericTree and oncscrolltable
								// JTree is the selection source
								if ( link &&  typeOfClasses == CONNECTED_CLASSES) { 
										addTypes = 	 tree.getSelected(); 
								if ( selectedItems.size() <= 0 || 
										 selectedItems.firstElement() instanceof String) {
										JOptionPane.showMessageDialog
												(OncBrowser.getOncBrowser(), 
												 "<html>"
												 + "Please select a tree node before adding leaf node"
												 + "</html>");
								}
						}
						else if ( link && typeOfClasses == KEYWORD_ONLY) { 
								addTypes = new Vector();
								addTypes.addElement(Keyword.class);
						}
						else {
								addTypes = new Vector();
								addTypes.addElement(OntologyMap.getRootNodeClass(ontologyButtonPanel));
						}
				}
				else if (source instanceof OncScrollList) {
						Object obj = ((OncScrollList)source).getListType();
						if (obj != null ) {
								addTypes = new Vector();
								addTypes.addElement(obj);
						}
				}
				else if (source instanceof OncScrollTable) {
						Object obj = ((OncScrollTable)source).getListType();
						if (obj != null ) {
								addTypes = new Vector();
								addTypes.addElement(obj);
						}
				}
				// ask the user what type of object they want to create
				// If there is only one possible type ( i.e. no subtypes skip this)
				Object editableObj = null;
				Object linkableObj = null;

				if ( addTypes != null && addTypes.size() > 0) {
						linkableObj = showCreatableTypes(addTypes.firstElement(), link,
																						 typeOfClasses, selectedItems);	
						if ( linkableObj == null )  {// nothing to edit 
								return; // user must have cancelled out
						}
						if ( linkableObj instanceof Class ) {
								// Create a new object
								// Do not allow creation of top level class
								// where subclasses exist
								editableClass = (Class)linkableObj;
								editableObj = createNewInstance(editableClass, selectedItems);
								if ( editableObj instanceof StatementBundle 
										 && ( selectedItems != null && selectedItems.size() > 0 
													&& !(selectedItems.firstElement() 
													instanceof StatementTemplate)) )
										showEditor = false;
						}
						else if ( linkableObj instanceof Persistible ) {
								// User selected an existing object to add
								editableObj = linkableObj;
								showEditor = false;
						}
				}
				if ( editableObj instanceof AbstractPersistible ) {
						((AbstractPersistible)editableObj).update();
						if ( link ) {
								linkNewInstance(selectedItems, 
																(AbstractPersistible)editableObj);
						}
						// Attach any global keywords set
						if ( GlobalKeywordsPanel.getKeywords() != null  && !(editableObj instanceof Keyword) )
								linkNewInstance(new Vector(GlobalKeywordsPanel.getKeywords()),
																(AbstractPersistible)editableObj);
				}
				
				Editable editableInstance = null;
				if ( editableObj instanceof String ) {
						// Make a label
						String label = new String("Please enter string value");
						if ( source instanceof OncScrollList) {
								label = ((OncScrollList)source).getLabel();
								String inputValue = 
										JOptionPane.showInputDialog(label); 
								if ( inputValue != null )
										((OncScrollList)source).addValue(inputValue);
						}
						else 
								System.out.println("How did you get to add a string");
				}
				else if ( editableObj instanceof Editable) {
						editableInstance = (Editable)editableObj;
						showEditor(editableInstance, showEditor);
						if ( editableInstance instanceof TreeBrowserNode) {
								if ( getTree() != null ) {
										getTree().addNode(editableInstance, 
																			!link, selectedItems);
								}
						}
						
						// Add it to the source if it is a scrollList
						if ( source instanceof OncScrollList ) {
								//((Persistible)editableInstance).addSaveListener
								//((SaveListener)source);
								((OncScrollList)source).addValue(editableInstance);
								// What tree was the current selected item in
								// Reset selected node to the newly created object
								
						}
						// Add it to the source if it is a scrollList
						else if ( source instanceof OncScrollTable ) {
								OncScrollTable oncTable = (OncScrollTable)source;
								((Persistible)editableInstance).addSaveListener
										((SaveListener)oncTable.getTableModel());
								
						}
				}
				OncBrowser.refresh();
				
		}

		private static void showEditor(Editable editableInstance, 
																	 boolean showEditor) {
				if ( showEditor ) 
					 	EditorFrame.showEditor(editableInstance, 
																	 null);
		}

		private Object createNewInstance (Class editableClass, 
																			Vector selectedItems) {
		
				Object editableObj = null;
				try {
						// Special case for Statement Bundles
						if ( editableClass.getName().equals
								 ("oncotcap.datalayer.persistible.StatementBundle") ) {
								if ( selectedItems != null && selectedItems.size() > 0 
										 && selectedItems.firstElement() 
										 instanceof StatementTemplate) {
										// User selected a statement template to create a 
										// SB from 
										editableObj = 
												new StatementBundle
												((StatementTemplate)selectedItems.firstElement());
								}
								else {
										editableObj = instantiateStatementBundle(selectedItems);
								}
						}
						else {
								editableObj = editableClass.newInstance();
								isNew = true;
						}
				}catch(InstantiationException ie) {
						ie.printStackTrace();
				}
				catch(IllegalAccessException iae) {
						iae.printStackTrace();
				}
				return editableObj;
		}

		
		private void linkNewInstance(Vector selectedItems, 
																 AbstractPersistible newInstance) {
				// Link this new instance to the tree selected object if a 
				// direct link can be made -- separate to its own method later
				//				System.out.println("linkNewInstance selectedItems" + selectedItems);
												
				if ( selectedItems != null ) {
						Iterator i = selectedItems.iterator();
						Vector parents = null;
						while ( i.hasNext() ) {
								Object obj = i.next();
								if ( (obj instanceof Persistible) == false )
										continue;
								Persistible pers = (Persistible)obj;
								if ( newInstance instanceof Persistible) {
										if ( newInstance.getClass() == pers.getClass()) {
												// THis object can link to another object of the same
												// type this will occur most often in Keywords
												// Do something special so it links through the 
												// proper field
												if ( pers instanceof Keyword && 
														 newInstance instanceof Keyword ) {
														Keyword keyword = (Keyword)pers;
														Keyword newKeyword = (Keyword)newInstance;
														keyword.addChild(newKeyword);
														Collection par = newKeyword.getParents();
														if ( par == null ) 
																parents = CollectionHelper.makeVector(pers);
														else {
																parents = new Vector(par);
																parents.addElement(pers);
														}
														keyword.update();
														// See is inverse linking is causing issues
														((Keyword)newInstance).setParents
																(parents);
														newKeyword.update();
												}
										}
										else {
												// Link forward and backward if possible
											// 	System.out.println("linkNewInstance " 
// 																					 + newInstance 
// 																					 + "  to pers " + pers);
												// Only link forward for now - 
												// to see if having inverse links is causing a problem
												// STs and SBs and code bundles have a one way link
												// two way links are bad for them
												newInstance.link(pers);
												pers.link(newInstance);
												newInstance.update();
												pers.update();
										}
								}
						}
				}

		}

		public static  Object instantiateStatementBundle(Vector selectedItems) {
				Object selectedObject = null;
				Object selectedItem = null;
				if ( selectedItems != null && selectedItems.size() > 0 )
						selectedItem = selectedItems.firstElement();
				Hashtable statementTemplate = new Hashtable();
				statementTemplate.put(StatementTemplate.class,
															CollectionHelper.makeVector(String.valueOf(-1)));
															
				AddLinkPanel addLinkPanel = 
						new AddLinkPanel(statementTemplate, 
														 "Add a Statement Bundle (create a new SB from a new ST or create a SB from an existing ST)",
														 selectedItem);
				addLinkPanel.setShowKeywords(true);
				addLinkPanel.setSize(new Dimension(600, 700)); // modal dialog
				addLinkPanel.selectRowLikeUser(0);
				addLinkPanel.show();
				selectedObject = addLinkPanel.getValue();
				addLinkPanel.dispose();
				
				StatementTemplate st = null;
				StatementBundle sb = null;
				if ( selectedObject instanceof Class ) {
						// Show statement Template editor for a new one
						//System.out.println("where is the ST editor " );
						st = new StatementTemplate();
						sb = new StatementBundle(st);
						sb.update();  //this update is needed because the st 
                                //method getStatementBundlesBasedOnMe fails
                                //without it
						showEditor(st, true );
						showEditor(sb, true );
						EditorFrame stEditor = EditorFrame.getEditor(st);
						stEditor.setVisible(true);
						stEditor.toFront();
				}
				else {
						st = (StatementTemplate)selectedObject;
						sb = new StatementBundle(st);
						if ( sb != null  && st != null) 
								showEditor(sb, true);
						else
								sb = null;
				}

				// create a statement bundle from the selected 
				// statement template instance 
				return sb;
						
		}

		private Object showCreatableTypes(Object selectedUserObject, 
																		 boolean link,
																		 int typeOfClasses,
																		 Object linkToObject) {
				// Ask the user object what types of objects it has direct links to
				// Build a tree with the type of object and the subclasses
				// to eliminate the two step process necessary to create a linked item
				// Only put elements that are currently visible leaf nodes in the tree
				// Also prune any abstract classes
				Hashtable treeHashtable = new Hashtable();
				String selectedOntologyName = null;
				Class superClass = null;

				// HIde other KB available types because there is 
				// not enough support yet 
				if ( selectedUserObject == KnowledgeNugget.class)
						return QuoteNugget.class;
				else if ( selectedUserObject == InformationSource.class )
						return Article.class;
								
				if (typeOfClasses == NONE &&
						selectedUserObject instanceof Class) {
						selectedOntologyName = 
								OntologyMap.getClassKey((Class)selectedUserObject);
				}
				else if ( typeOfClasses == KEYWORD_ONLY ) {
						selectedOntologyName = "Keyword";
						return oncotcap.datalayer.persistible.Keyword.class;
				}
				else {
						selectedOntologyName = 
								OntologyMap.getClassKey(selectedUserObject.getClass());
				}
				//System.out.println("selectedUserObject " + selectedUserObject);
				//System.out.println("selectedOntologyName  " + selectedOntologyName);
				Vector availableClasses = null;
				// If this class is not a browser node  just get the subclasses
				if ( selectedOntologyName == null ) {
						if ( selectedUserObject instanceof String ) {
								treeHashtable.put(String.class, CollectionHelper.makeVector(String.valueOf(-1)));
						}
						else {
								getSubClassTree((Class)selectedUserObject, treeHashtable);
						}
				}
				else {
						if ( typeOfClasses == CONNECTED_CLASSES) {
								if ( getCreateClassName() == null ) {
										availableClasses = 
												new Vector(OntologyMap.getValidDirectConnectedTypes
																	 (selectedOntologyName));
								}
								else {
										availableClasses = 
												CollectionHelper.makeVector(createClassName);
								}
						}
						else {
								availableClasses = new Vector();
								availableClasses.addElement(selectedOntologyName);
						}
						
						Iterator i = availableClasses.iterator();
						while ( i.hasNext() ) {
								String availableClass = (String)i.next();
								// Get all the possible types of object that can be created 
// 								System.out.println("availableClass " + availableClass);
								if ( availableClass != null ) { 
										if ((typeOfClasses == CONNECTED_CLASSES &&
										 OntologyMap.isVisibleNode(availableClass, 
																							 ontologyButtonPanel))
												|| typeOfClasses == NONE ) {
												superClass = 
														OntologyMap.getClassValue(availableClass);	
// 												System.out.println(">> superClass " + superClass);
											// 	System.out.println(" selectedOntologyName " + selectedOntologyName);
// 												System.out.println(" link " + link);
// 												System.out.println(" typeOfClasses" + typeOfClasses);
												// My brain is fried I cannot invert this stupid statement
												if ( !("StatementBundle".equals(selectedOntologyName) 
																			&& superClass == StatementTemplate.class) ) {
														getSubClassTree(superClass, treeHashtable);
												}
												else {
														System.out.println("See there how did I get here");
												}
										}
								}						
						}
				}
				
				// If there arent any linkable types tell user
				Object selectedObject = null;
				if ( treeHashtable.isEmpty() ) {
						JOptionPane.showMessageDialog
								(OncBrowser.getOncBrowser(), 
								 "<html>There aren't any visible linkable types."
								 + "<BR>"
								 + "Please select leaf nodes on Tree Controller"
								 + "<BR> Panel</html>");
						// Modal dialog with OK button
					// 	String message = "Line1\nLine2";
// 						JOptionPane.showMessageDialog(frame, message);

						return null;
				}
				else {
						// If creating a SB from a ST and that is the only option 
						// i.e. CB is not a visisle leaf - just show the SB
						if ( selectedOntologyName != null && 
								 selectedOntologyName.equals("StatementTemplate") 
								 && treeHashtable.size() == 1 
								 && treeHashtable.get(StatementBundle.class) != null ) {
								selectedObject = StatementBundle.class;
						}
						else if (treeHashtable.size() == 1  
										 && typeOfClasses == NONE && 
										 (selectedUserObject == String.class
											|| source instanceof OntologyTree) ) {
								//&& (superClass != null && superClass == String.class )) {
								// just return the editableClass type no need to 
								// bother the user with a pick list
								Enumeration enumer = treeHashtable.keys();
								selectedObject = enumer.nextElement();
								return (Class)selectedObject;
						}
						else {
								Object linkTo = null;
								if ( linkToObject instanceof Vector &&
										 linkToObject != null && ((Vector)linkToObject).size() > 0 ) 
										linkTo = ((Vector)linkToObject).firstElement();
								else 
										linkTo = linkToObject;
								AddLinkPanel addLinkPanel = 
										new AddLinkPanel(treeHashtable,
																		 "Add " + selectedOntologyName, 
																		 linkTo);
								// If creating leaves in a statement template turn off
								// the ability to display instances of the SB
								if ( selectedOntologyName != null && 
										 selectedOntologyName.equals("StatementTemplate") ) {
										addLinkPanel.setNoShowClasses
												(CollectionHelper.makeVector(StatementBundle.class));
								}
								addLinkPanel.setSize(new Dimension(500, 300)); // modal dialog
								addLinkPanel.selectRowLikeUser(0);
								if ( typeOfClasses == NONE  && source instanceof OntologyTree ) {
										// Adding a root collapse the instances split pane
										addLinkPanel.closeRightPanel();
										addLinkPanel.setShowInstances(false);
										addLinkPanelMsg = "Select Class Type to create";
								}
								if ( selectedUserObject == AssessmentItem.class){
									addLinkPanel.closeRightPanel();
									addLinkPanel.setShowInstances(false);
								}
									
								addLinkPanel.show();
								selectedObject = addLinkPanel.getValue();
								addLinkPanel.dispose();
						}
						return selectedObject;
				}

		}

		private Hashtable getSubClassTree(Class superClass, 
																			Hashtable treeHashtable) {
// 				System.out.println(" superClass " + superClass);
				
				Vector subclasses =  null;
				try {
						if ( superClass.isPrimitive() == false 
								 && superClass != String.class )  
						subclasses = 
								new Vector(oncotcap.Oncotcap.getDataSource().getSubclasses
													 (superClass));
				} catch( Exception e) {
						e.printStackTrace();
				}
				if ( subclasses != null && subclasses.size() > 0) {
						
						Object parent = String.valueOf(-1);
						if ( superClass == Interpretation.class){
							treeHashtable.put(superClass, CollectionHelper.makeVector(parent));
							parent = superClass;
						}
						Iterator ii = subclasses.iterator();
						while ( ii.hasNext() ) {
								Class subClass = (Class)ii.next();
								treeHashtable.put(subClass, CollectionHelper.makeVector(parent));
						}
				}
				else 
					treeHashtable.put(superClass, 
							CollectionHelper.makeVector(String.valueOf(-1)));

				return treeHashtable;
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

		public void setCreateClassName(String name) {
				this.createClassName = name;
		}
		public String getCreateClassName() {
				return 	this.createClassName ;
		}

		public void setSourceParent(Object source) {
				this.source = source;
		}
		public Object getSourceParent() {
				return this.source;
		}
		public void setOntologyButtonPanel(OntologyButtonPanel ontologyButtonPanel) {
				this.ontologyButtonPanel = ontologyButtonPanel;
		}
		public OntologyButtonPanel getOntologyButtonPanel() {
				return ontologyButtonPanel;
		}


}
