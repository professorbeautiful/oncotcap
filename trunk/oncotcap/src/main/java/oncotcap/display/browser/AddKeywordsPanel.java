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
import oncotcap.display.editor.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.util.*;
import java.awt.dnd.*;

public class AddKeywordsPanel extends JDialog
		implements TreePanelListener {
		Hashtable emptyHashtable = new Hashtable();
		Vector emptyVector = new Vector();
		OncScrollList keywordList = null;
		private static OntologyTree ot1 = null;
		Vector keywords = null;
		Object selectedObject = null;
		Vector noShowClasses = null;
		boolean showKeywords = false;
		JSplitPane selectPane  = null;
		boolean cancelled = false;
		private JButton okButton = new JButton(new DoneAction());
		private JButton cancelButton = new JButton(new CancelAction());
		private int rootOntologyType = -1;
		Editable editedObject = null;
		public AddKeywordsPanel( Editable editedObject) {

				super((JFrame)null, "Would you like to add the following keywords", 
							true);  
		// Get all the keywords that the user would like to auto add
				// Determine if the toString contains any keywords
				this.editedObject = editedObject;
				keywords = 
						Keyword.extractKeywordFromText(editedObject.toString());
				init();
		}

		public  Object getValue() {
				if ( !cancelled ) 
						return selectedObject;
				else 
						return null;
		}

		
		private void init() {
				getContentPane().setLayout(new BorderLayout());
				JPanel mainPanel = new JPanel(new BorderLayout());
				JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

				ButtonActionListener actionListener = new ButtonActionListener();
				okButton.addActionListener(actionListener);
				cancelButton.addActionListener(actionListener);
				buttonPanel.add(okButton);
				buttonPanel.add(cancelButton);
				JPanel keywordListPanel = new JPanel(new BorderLayout());
				JScrollPane keywordListScrollPane = new JScrollPane();
				// Add any global add keywords
				keywordList = new OncScrollList(keywords, 
																				false, 
																				false,
																				RemoveNode.REMOVE_FROM_DISPLAY);
				keywordListPanel.add(keywordList, BorderLayout.CENTER);
				ot1 = initOntologyTree("Keyword Catalog");
				refreshOntologyTree(ot1,
														"Keyword",
														CollectionHelper.makeVector("Keyword"),
														null,
														TreeDisplayModePanel.ROOT);

				selectPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
				selectPane.setTopComponent(keywordListPanel);
				//selectPane.setBottomComponent(instancePanel);
				selectPane.setBottomComponent(ot1);
				selectPane.setDividerLocation(200);
				selectPane.setOneTouchExpandable(true);
				selectPane.setDividerSize(10);
				mainPanel.add(selectPane, BorderLayout.CENTER);
				getContentPane().add(buttonPanel, BorderLayout.SOUTH);
				getContentPane().add(selectPane, BorderLayout.CENTER);
				ot1.getTree().addTreePanelListener(this);
				//keywordList.addTreePanelListener(this);

		}

		public void addLinkPanelListener(TreePanelListener listener) {
				ot1.getTree().addTreePanelListener(listener);
				//keywordListTree.addTreePanelListener(listener);
		}

		public void selectRowLikeUser(int row) {
				ot1.getTree().clearSelection();
				//keywordListTree.selectRowLikeUser(row);
		}

		public void setNoShowClasses(Vector noShow) {
				noShowClasses = noShow;
		}

		public void showAllPanes() {
				selectPane.setOneTouchExpandable(true);
				selectPane.setDividerLocation(200);
				ot1.setVisible(true);
		}

		public void closeRightPanel() {
				selectPane.setOneTouchExpandable(false);
		}

		public void closeRightPanel(OntologyTree ot) {
		}

		public void setShowKeywords(boolean showKey) {
				showKeywords = showKey;
		}

		public JTree getInstanceTree() {
				return null;
		}
		public JTree getClassTree() {
				return null;
		}
		private void setRootOntologyType(int r) {
				rootOntologyType = r;
		}

		private int getRootOntologyType() {
				return rootOntologyType;
		}

		private OntologyTree refreshOntologyTree(OntologyTree ot,
																						 String startClassName,
																						 Vector endingClassNames,
																						 OncFilter filter,
																						 int displayMode) {
				ot.setFilter(filter);
				ot.setPruningOn(false);
				AbstractButton filterToggle = ot.getButton(OntologyTree.FILTER_TOGGLE_BUTTON);
				if ( filter == null ) 
						filterToggle.setSelected(false);
				else 
						filterToggle.setSelected(true);
				ot.updateTree(startClassName,
											endingClassNames,
											(OncFilter)filter,
											displayMode);
				return ot;
		}

		private OntologyTree initOntologyTree(String treeName) {
				OntologyTree ot = new OntologyTree();
				ot.collapseController();
				ot.disableButton(OntologyMap.K);
				ot.disableButton(OntologyTree.DELETE_BUTTON);
				ot.disableButton(OntologyTree.COLLAPSE_BUTTON);
				ot.disableButton(OntologyTree.UNLINK_BUTTON);
				ot.disableButton(OntologyTree.FILTER_TOGGLE_BUTTON);
				ot.disableButton(OntologyTree.SIBLINGS_TOGGLE_BUTTON);
				ot.setName(treeName);
				return ot;
		}

		public void changed(TreePanelEvent evt) {
				/*			// The user changed their selections update the
				// scroll list
				selectedObject = evt.getSource();
				// System.out.println("evt.getTree() " + evt.getTree()  + " " +
// 				 													 evt.getSource().getClass()
// 													 + selectedObject);
				updateButtonString(selectedObject);
				// determine what was just selected and 
				if ( evt.getSource() != null ) {
						if ( evt.getTree() == keywordListTree ) {
								showAllPanes();
								String oName = StringHelper.className(selectedObject.toString());
								//System.out.println("oName " + oName);
								if (noShowClasses != null 
										 && noShowClasses.contains(evt.getSource())) {
										// For SBs being created from STs attaching an instnace 
										// is not allowed
										ot1.setVisible(false);
										ot2.setVisible(false);
										closeRightPanel();
								}
								else {
										if ( showKeywords ) {
												// Build a tree with keywords and selected type
												Vector leaves = CollectionHelper.makeVector(oName);
												leaves.addElement(OntologyMap.KEYWORD);
												refreshOntologyTree(ot1,
																			 OntologyMap.KEYWORD,
																			 leaves, 
																			 (OncFilter)null,
																			 TreeDisplayModePanel.ROOT);
												refreshOntologyTree(ot2,
																						oName, 
																						emptyVector,
																						getNotFilter(),
																						TreeDisplayModePanel.ROOT);
										}
										else {
												// Build a tree with only selected types
												System.out.println("update ot1 " + oName);
												refreshOntologyTree(ot1,
																						oName,
																						emptyVector,
																						(OncFilter)null,
																						TreeDisplayModePanel.NONE);
												closeRightPanel(ot2);
										}
								}
						}
						
						// unselect the items in the other list
						if ( evt.getTree() != ot1.getTree() ) {
								ot1.getTree().clearSelection();
								ot1.getTree().repaint();
						}
						if ( evt.getTree() != ot2.getTree() ) {
								ot2.getTree().clearSelection();
								ot2.getTree().repaint();
						}
						if ( evt.getTree() != keywordListTree ) {
								classTree.clearSelection();
								keywordListTree.repaint();
						}
				}
				*/				
		}

		private void updateButtonString(Object obj) {
						if ( obj instanceof Class) {
								okButton.setText("New");
						}
						else {
								okButton.setText("Done");
						}
		}

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
				System.out.println("filter " + filter);
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
				return null;
		}

		public Object getSelectedValue(){
				return this.selectedObject;
		}
		class DoneAction extends AbstractAction {
				public DoneAction() {
						super("Done");
				}
				public void actionPerformed(ActionEvent e) {
						// close the dialog
						//add any keywords from the list to the edited object
						System.out.println(" editedobject "  + editedObject);
						if ( editedObject instanceof Persistible ) {
								Collection data = keywordList.getData();
								Iterator i = data.iterator();
								// take them out the list
								while ( i.hasNext() ) {
										Persistible obj = (Persistible)i.next();
										System.out.println("linking " + obj + " to "
																			 + editedObject);
										((Persistible)editedObject).link(obj);
										obj.link((Persistible)editedObject);
								}
						}
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

		class ButtonActionListener implements ActionListener {
				public void actionPerformed(ActionEvent e) {
						if ( e.getSource() != null && e.getSource() == cancelButton ) {
								selectedObject = null;
						}
						hide();
				}
		}

}
