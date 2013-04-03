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
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.awt.dnd.*;
import javax.swing.*;
import javax.swing.event.*;
import java.lang.reflect.Array;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.tree.TreePath;

import oncotcap.display.common.*;
import oncotcap.datalayer.*;
import oncotcap.display.browser.OntologyMap;
import oncotcap.datalayer.persistible.*;
import oncotcap.util.*;
import oncotcap.display.editor.persistibleeditorpanel.Editable;
import oncotcap.display.editor.persistibleeditorpanel.EditorPanel;
import oncotcap.display.editor.EditorFrame;

public class AddLinkPanel extends JDialog
		implements TreePanelListener,
							 DoubleClickListener  {
		public static final Keyword unattachedToKeyword = 
				(Keyword)oncotcap.Oncotcap.getDataSource().find
				(new GUID("888eaada00000036000000ff55a9f2f9"));
		Hashtable emptyHashtable = new Hashtable();
		Vector emptyVector = new Vector();
		GenericTree classTree = new GenericTree(emptyHashtable, false);
		OntologyTree ot1 = null;
		Object selectedObject = null;
		Object selectedLinkToObject = null;
		Vector noShowClasses = null;
		boolean showKeywords = false;
		JSplitPane selectPane  = null;
		JSplitPane instanceSplitPane = null;
		JPanel instancePanel = null; 
		boolean cancelled = false;
		boolean showInstances = true;
		private JButton okButton;
		private JButton cancelButton = new JButton("Cancel");
		private JButton newButton;
		private int rootOntologyType = -1;
		private Class baseClass;
		JPanel catalogedPanel = null;
		private AddLinkEnterAction enterAction = new AddLinkEnterAction();
		public AddLinkPanel(Class cls, String title) {
				super((JFrame)null, title, true);  
				//System.out.println("add link panel parent : " + cls);
				baseClass = cls;
				Hashtable classes = new Hashtable();
				classes.put(cls,
										CollectionHelper.makeVector(String.valueOf(-1)));
				classTree.updateTree(classes);
				init();
		}

		public AddLinkPanel(Hashtable classes, String title, 
												Object selectedLinkToObject ) {
				super((JFrame)null, title, true);  
				this.selectedLinkToObject = selectedLinkToObject;
				//super(OncBrowser.getOncBrowser(), "Add linked item", true); 

				Enumeration classEnums = classes.keys();
				if(classEnums.hasMoreElements()) {
						// Make sure base class is the super class of all classes
						// in the hashtable
						Class currentClass = (Class)classEnums.nextElement();
						if ( baseClass == null )
								baseClass = currentClass;
						else if ( baseClass.isAssignableFrom(currentClass)) 
								baseClass = currentClass;
				}
				setTitle("Add a " + StringHelper.className(baseClass.getName()));
				classTree.updateTree(classes);
				init();
		}
		
		public  Object getValue() {
				if ( !cancelled ) 
						return selectedObject;
				else 
						return null;
		}

		private boolean isSB = false;
		private boolean isST = false;
		private Box ot1Panel;
		private void init() {
				if(baseClass != null) {
 					if(baseClass.equals(StatementTemplate.class))
					{
						isST = true;
						okButton = new JButton("Add SB based on selected ST");
						newButton = new JButton("Create SB from new ST");
					}
					else if(baseClass.equals(StatementBundle.class))
					{
						isSB = true;
						if ( selectedLinkToObject instanceof Encoding )
								okButton = new JButton("Add the existing selected SB");
						else {
								okButton = new JButton("Add a copy of the selected SB");
						}
						newButton = new JButton("Add a New SB");
					}
					else
						okButton = new JButton("Done");
				}
				
				getContentPane().setLayout(new BorderLayout());
				JPanel mainPanel = new JPanel(new BorderLayout());
				JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

				ButtonActionListener actionListener = new ButtonActionListener();
				okButton.addActionListener(actionListener);
				cancelButton.addActionListener(actionListener);
				if(isSB || isST)
				{
					newButton.addActionListener(actionListener);
					buttonPanel.add(newButton);
				}
				buttonPanel.add(okButton);
				buttonPanel.add(cancelButton);
				JPanel classPanel = new JPanel(new BorderLayout());
				JScrollPane classScrollPane = new JScrollPane();
				classScrollPane.setViewportView(classTree);
				classPanel.add(classScrollPane, BorderLayout.CENTER);
		
				ot1 = initOntologyTree("Existing instances ");
				ot1.getTree().allowDoubleClickEditing(false);
				addDoubleClickListener();
				catalogedPanel = new JPanel(new BorderLayout());
				catalogedPanel.add
						(new JLabel("Existing instances of " 
												+ StringHelper.className(baseClass.getName())),
												BorderLayout.NORTH);

				catalogedPanel.add(ot1, BorderLayout.CENTER);
				selectPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
				selectPane.setTopComponent(classPanel);
				//selectPane.setBottomComponent(instancePanel);
				selectPane.setBottomComponent(catalogedPanel);
				selectPane.setDividerLocation(200);
				selectPane.setOneTouchExpandable(true);
				selectPane.setDividerSize(10);
				mainPanel.add(selectPane, BorderLayout.CENTER);
				getContentPane().add(buttonPanel, BorderLayout.SOUTH);
				getContentPane().add(selectPane, BorderLayout.CENTER);
				ot1.getTree().addTreePanelListener(this);
				classTree.addTreePanelListener(this);

		}

		public void addLinkPanelListener(TreePanelListener listener) {
				ot1.getTree().addTreePanelListener(listener);
				classTree.addTreePanelListener(listener);
		}

		public void selectRowLikeUser(int row) {
				ot1.getTree().clearSelection();
				classTree.selectRowLikeUser(row);

		}

		public void setNoShowClasses(Vector noShow) {
				noShowClasses = noShow;
		}
		public void setShowInstances(boolean show) {
				showInstances = show;
		}

		public void showAllPanes() {
				if( ! (isSB || isST))
				{
					selectPane.setOneTouchExpandable(true);
					selectPane.setDividerLocation(200);
				}
				else
				{
					selectPane.setOneTouchExpandable(false);
					selectPane.setDividerLocation(0);
				}
				ot1.setVisible(true);

		}

		public void closeRightPanel() {
				selectPane.remove(catalogedPanel);
				selectPane.setOneTouchExpandable(false);
		}

		public void closeRightPanel(OntologyTree ot) {
				instanceSplitPane.remove(ot);
				instanceSplitPane.setOneTouchExpandable(false);
		}

		public void setShowKeywords(boolean showKey) {
				showKeywords = showKey;
		}

		public JTree getInstanceTree() {
				return null;
		}
		public JTree getClassTree() {
				return (classTree);
		}
		private void setRootOntologyType(int r) {
				rootOntologyType = r;
		}

		private int getRootOntologyType() {
				return rootOntologyType;
		}
		private void disableRootAddBtn(OntologyTree ot) {
				// Do not allow the user to do a recursive add 
				// Remove the add root node button
				String rootNodeName = 
						OntologyMap.getRootNodeName(ot.getOntologyButtonPanel());
				Integer buttonIndex = 
						OntologyMap.getButtonIndex(rootNodeName);
				ot.disableButton(buttonIndex.intValue());
		}
		private void updateButtonString(Object obj) {
					if(! (isSB || isST))
					{
						if ( obj instanceof Class) {
								okButton.setText("New");
						}
						else {
								okButton.setText("Done");
						}
					}
		}

		private OntologyTree refreshOntologyTree(OntologyTree ot,
																						 String startClassName,
																						 Vector endingClassNames,
																						 OncFilter filter,
																						 int displayMode) {
				ot.setFilter(filter);
				ot.setPruningOn(false);
				if (OntologyMap.getButtonIndex(startClassName)!= null )
						ot.getOntologyButtonPanel().setRoot
								(OntologyMap.getButtonIndex(startClassName).intValue() );

				ot.setLeaves(endingClassNames);
			
				AbstractButton filterToggle = 
						ot.getButton(OntologyTree.FILTER_TOGGLE_BUTTON);
				if ( filter == null ) 
						filterToggle.setSelected(false);
				else 
						filterToggle.setSelected(true);
				// cataloged part of tree
				Hashtable h = ot.getTreeInstances(startClassName, 
																					endingClassNames,
																					(OncFilter)null,
																					TreeDisplayModePanel.ROOT);
				if ( endingClassNames != null && endingClassNames.size() > 0 ) {
						// Part of tree that is not keyword cataloged
						Hashtable h2 = ot.getTreeInstances
								((String)endingClassNames.firstElement(), 
								 new Vector(),
								 getNotFilter(),
								 TreeDisplayModePanel.ROOT);
						// Get the  " " keyword Unattached a Keyword and attach to these 
						// statementbundles that do not have any keywords specified
						Vector keywordVector = CollectionHelper.makeVector(unattachedToKeyword);
						for (Enumeration e = h2.keys() ; 
								 e.hasMoreElements() ;) {
								Object key = e.nextElement();
								Vector parents = (Vector)h.put(key, keywordVector );
						}				
				}
				ot.updateTree(h, startClassName);
				ot.refreshRootNode(startClassName);
				return ot;
		}
		// GOOD CHANGE

			private OntologyTree initOntologyTree(String treeName) {
				OntologyTree ot = new OntologyTree();
				ot.collapseController();
				ot.disableButton(OntologyMap.K);
				ot.disableButton(OntologyTree.DELETE_BUTTON);
				ot.disableButton(OntologyTree.COLLAPSE_BUTTON);
				ot.disableButton(OntologyTree.UNLINK_BUTTON);
				ot.disableButton(OntologyTree.FILTER_TOGGLE_BUTTON);

				ot.setLeaflessSelected(true);
				ot.disableButton(OntologyTree.SIBLINGS_TOGGLE_BUTTON);
				ot.setName(treeName);
				ot.setModal(true);
				return ot;
		}

		public void changed(TreePanelEvent evt) {
				// The user changed their selections update the
				// scroll list
				selectedObject = evt.getSource();
				// System.out.println("evt.getTree() " + evt.getTree()  + " " +
// 				 													 evt.getSource().getClass()
// 													 + selectedObject);
				updateButtonString(selectedObject);
				// determine what was just selected and 
				if ( evt.getSource() != null ) {
						if ( evt.getTree() == classTree ) {
								//System.out.println("classTree");
								String oName = 
										StringHelper.className(selectedObject.toString());
								showAllPanes();
								//System.out.println("oName " + oName);
								if (noShowClasses != null 
										 && noShowClasses.contains(evt.getSource())) {
										// For SBs being created from STs attaching an instnace 
										// is not allowed
										ot1.setVisible(false);
										ot1Panel.setVisible(false);
										closeRightPanel();
								}
								else {
										if ( showKeywords ) {
												// Build a tree with keywords and selected type
												Vector leaves = CollectionHelper.makeVector(oName);
												leaves.addElement(oName);
												refreshOntologyTree(ot1,
																			 OntologyMap.KEYWORD,
																			 leaves, 
																			 (OncFilter)null,
																			 TreeDisplayModePanel.ROOT);
		
										}
										else {
												// Build a tree with only selected types
												//System.out.println("update ot1 " + oName);
												if ( showInstances ) {
														refreshOntologyTree(ot1,
																								oName,
																								emptyVector,
																								(OncFilter)null,
																								TreeDisplayModePanel.NONE);
												}
												else {
														closeRightPanel();
												}
										}
								}
						}
						
						// unselect the items in the other list
						if ( evt.getTree() != ot1.getTree() ) {
								ot1.getTree().clearSelection();
								ot1.getTree().repaint();
						}
						if ( evt.getTree() != classTree ) {
								classTree.clearSelection();
								classTree.repaint();
						}
				}
				disableRootAddBtn(ot1);
		}
		//END GOOD CHANGE
		public OncFilter getFilter(String oName) {
				// Build a filter with the desired keyword as the only node
				OncFilter filter = null;
				// Create a filter
				filter = new OncFilter();	
				OncTreeNode rootNode = filter.getRootNode();
				OncTreeNode orNode = new OncTreeNode(TcapLogicalOperator.OR);
				OncTreeNode ontologyObjectNode = 
						new OncTreeNode(new OntologyObjectName(oName));
				orNode.add(ontologyObjectNode);
				rootNode.add(orNode);
				//System.out.println("filter " + filter);
				return filter;
		}
		public OncFilter getNotFilter() {
				// Build a filter with the desired keyword as the only node
				OncFilter filter = null;
				// Create a filter
				filter = new OncFilter();	
				OncTreeNode rootNode = filter.getRootNode();
				OncTreeNode orNode = new OncTreeNode(TcapLogicalOperator.NOT);
				OncTreeNode ontologyObjectNode = 
						new OncTreeNode(new OntologyObjectName(OntologyMap.KEYWORD));
				orNode.add(ontologyObjectNode);
				rootNode.add(orNode);
				return filter;
		}

		public GenericTree getSelectionTree() {
				return classTree;
		}

		public Object getSelectedValue(){
				return this.selectedObject;
		}
		
		public void clickOk() {
				okButton.doClick();
		}
		public void clickNew() {
				newButton.doClick();
		}
		public void clickCancel() {
				cancelButton.doClick();
		}
		private void addDoubleClickListener() {
				// Override what happens in double click
				// remove listener from the ontology tree 
				ot1.getTree().getSelectionListener().removeDoubleClickListener(ot1);
				// add the listener
				ot1.getTree().getSelectionListener().addDoubleClickListener(this);
				classTree.getSelectionListener().addDoubleClickListener(this);
				overrideEnterAction(ot1.getTree());
				overrideEnterAction(classTree);

		}
		private void overrideEnterAction(GenericTree tree) {
				tree.getInputMap(JComponent.WHEN_FOCUSED).put
													(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
													 "AddLinkEnter");
				tree.getActionMap().put("AddLinkEnter", 
																enterAction);	
		}
		public void doubleClicked(DoubleClickEvent evt){
					// If there is nothing selected then create a new 
						// object
						if ( selectedObject == null )
								clickNew();
						else
								clickOk();
						// close the dialog
						setVisible(false);
						/*
				int x = 0;
				int y = 0;
				if ( evt.getMouseEvent() != null ) { 
						x = evt.getMouseEvent().getX();
						y = evt.getMouseEvent().getY();
				}
				if ( evt.getSource() instanceof GenericTree ) {
						Object obj = ((GenericTree)evt.getSource()).getLastSelectedPathComponent();
						Object userObject = null;
						if ( obj instanceof DefaultMutableTreeNode ) 
								userObject = ((DefaultMutableTreeNode)obj).getUserObject();
						else
								return;
						if ( userObject instanceof Editable) {
								EditorPanel editorPanel =
										EditorFrame.showEditor((Editable)userObject, 
																					 null,
																					 x, y, 
																					 (GenericTree)evt.getSource());
								if ( userObject instanceof Persistible ) {
										((Persistible)userObject).addSaveListener(editorPanel);
								}
						}
						else {
								JOptionPane.showMessageDialog
										(null, 
										 "There is no editor for the selected object.");
						}
				}
						*/
		}

		class DoneAction extends AbstractAction {
				public DoneAction() {
						super("Done");
				}
				public void actionPerformed(ActionEvent e) {
						// close the dialog
						setVisible(false);
				}
		}
		class CancelAction extends AbstractAction {
				public CancelAction() {
						super("Cancel");
				}
				public void actionPerformed(ActionEvent e) {
						// close the dialog
						setVisible(false);
				}
		}

		class AddLinkEnterAction extends AbstractAction {
				public AddLinkEnterAction() {
						super("AddLinkEnter");
				}
				public void actionPerformed(ActionEvent e) {
						
						// If there is nothing selected then create a new 
						// object
						if ( selectedObject == null) 
								clickNew();
						else
								clickOk();
						// close the dialog
						setVisible(false);
				}
		}

		class ButtonActionListener implements ActionListener {
				public void actionPerformed(ActionEvent e) {
						// 		System.out.println("SELECTED OBJECT " + selectedObject
						// 															 + "BUTTON " + e.getSource() );
						
						if ( e.getSource() != null && e.getSource() == cancelButton )// {
								selectedObject = null;
						else if(e.getSource() == newButton && 
 										"Add a New SB".equals(newButton.getText())) {
 								selectedObject = StatementBundle.class;
 						}
						else if(e.getSource() == newButton && 
 										"Create SB from new ST".equals(newButton.getText())) {
 								selectedObject = StatementTemplate.class;
 						}
						else if ( e.getSource() == okButton ) {
								if ( selectedObject instanceof CodeBundle ) {
										CodeBundle cb = (CodeBundle)selectedObject;
										selectedObject = cb.clone();
										
								}
								else if ( selectedObject instanceof StatementBundle ) {
										StatementBundle sb = (StatementBundle)selectedObject;
										//if ( selectedLinkToObject instanceof Encoding)
										//		selectedObject = sb;
										//else
												selectedObject = sb.clone();												
								}
						}
						
						hide();
				}
		}
}
