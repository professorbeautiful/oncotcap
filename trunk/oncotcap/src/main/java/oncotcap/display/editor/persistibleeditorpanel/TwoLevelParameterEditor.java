package oncotcap.display.editor.persistibleeditorpanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;

import java.util.*;
import java.io.*;
import java.lang.reflect.Array;
import oncotcap.util.*;
import oncotcap.datalayer.*;
import oncotcap.datalayer.autogenpersistible.*;

import oncotcap.display.browser.*;
import oncotcap.display.common.*;


import oncotcap.display.editor.EditorFrame;

import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.parameter.*;

import javax.swing.tree.*;

public class TwoLevelParameterEditor extends EditorPanel 
		implements ActionListener,
							 HyperTextMenuListener,
							 SaveListener,
							 SingleClickListener {
		static private Vector operatorsList = TcapOperator.getBooleanExpressionOperators();
		static private Vector equalsOnly = TcapOperator.getAssignmentOperators();
		static private String blankLabel = "";
		static private ProcessDeclaration defaultOncProcess = null; // cancer cell
		private boolean operatorSet = false;
		private OncFilter filter;
		private boolean showOperator = true;
		private EnumDefinition enumDefinition = null;
		private EnumLevelList eLevelList = null;

		private boolean levelDisplayed = false;

		JPanel exprPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel topPanel = new JPanel(new BorderLayout());

		JTextField numericLevel = null;
		JPanel numericPanel = null;
		HyperTextMenu oncProcessMenu = null;
		DroppableTextField keywordLabel = null;
		HyperTextMenu operatorMenu = null;
		HyperTextMenu fromLevelMenu = null;
		HyperTextMenu toLevelMenu = null;
		SortedList list = null;
		SortedList list2 = null;
		TwoLevelParameter param = null;
		ProbabilityTable probabilityTable = null;
		OntologyTree ontologyTree = null;
		Keyword keywordConstraint = null;

		public TwoLevelParameterEditor() {
				init();
		}

		public TwoLevelParameterEditor(TwoLevelParameter param) {
				init();
				this.param = param;
				//edit(param);
		}
		public TwoLevelParameterEditor(TwoLevelParameter param, 
																				boolean showOperator) {
				this.showOperator = showOperator;
				init();
				this.param = param;
				//edit(param);
		}
		

		private JPanel getLevelListPanel() {
				JPanel llPanel = new JPanel(new BorderLayout());
				JButton newLLButton = new JButton("New Level List");
				//llPanel.add(new HyperTextMenu(), BorderLayout.NORTH);
				return llPanel;
		}

		public OncFilter getFilter(Keyword keywordConstraint) {
				// Build a filter with the desired keyword as the only node
				OncFilter filter = null;
				if ( keywordConstraint != null ) {
						// Create a filter
						filter = new OncFilter(false);	
						OncTreeNode rootNode = filter.getRootNode();
						OncTreeNode orNode = new OncTreeNode(TcapLogicalOperator.OR);
					  OncTreeNode keywordNode = new OncTreeNode(keywordConstraint);
						orNode.add(keywordNode);
						rootNode.add(orNode);
				}
				return filter;
		}

		public void mouseClicked(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {mouseActivated(e);}
		
		public void mouseActivated(MouseEvent e){}
		public void actionPerformed(ActionEvent a) {}

		public void hyperTextMenuChanged(HyperTextMenuEvent e) {
				//System.out.println("hyper text menu event " + e.getSource());
				if ( e.getSource().equals(fromLevelMenu)) {	
						if ( e.getSelectedItem() instanceof LevelAndList) {
								LevelAndList levelAndList = (LevelAndList)e.getSelectedItem();
								EnumLevel eLevel = null;
								eLevelList = levelAndList.getLevelList();
								eLevel = levelAndList.getLevel();
								if ( eLevelList != null ) 
										if ( eLevelList.isNumericList() )
												exprPanel.add(numericLevel);

								// Reset the to menu so only the selected level list 
								// is available
								//toLevelMenu.clearList();
								Vector levelsWithLists = new Vector();
								Iterator i = eLevelList.getLevelIterator();
								if (!i.hasNext()  && eLevelList.isNumericList()) {
										levelsWithLists.addElement
												(new LevelAndList(eLevelList, 
																					new EnumLevel(eLevelList.toString())));
								}
								else {
										while (i.hasNext()) {
												levelsWithLists.addElement
														(new LevelAndList(eLevelList, (EnumLevel)i.next()));
												
										}
								}
								toLevelMenu.setTheList(levelsWithLists);
						}
				}
		}
		private void init() {
				setLayout(new BorderLayout());
				setPreferredSize( new Dimension(600,600));
				// expression panel
				JLabel processLabel = new JLabel("Process");
				keywordLabel = new DroppableTextField(30);
				numericLevel = new JTextField(10);
				oncProcessMenu = new HyperTextMenu("Process");
				oncProcessMenu.addActionListener(this);
				operatorMenu = new HyperTextMenu("Operator");
				operatorMenu.addActionListener(this);
				fromLevelMenu = new HyperTextMenu("From Level");
				fromLevelMenu.addHyperTextMenuListener(this);
				toLevelMenu = new HyperTextMenu("To Level");
				toLevelMenu.addHyperTextMenuListener(this);

				exprPanel.add(oncProcessMenu);
				exprPanel.add(keywordLabel);
				// if ( showOperator ) 
// 						exprPanel.add(operatorMenu);
				JLabel directions1 = 
						new JLabel("<Single click the keyword. Then set \"From\" and \"To\" levels>");
				directions1.setFont(OncBrowserConstants.DIRECTIONS_FONT_SMALL_ITALIC);
				directions1.setForeground(OncBrowserConstants.MBColorDark);
				topPanel.add(directions1, BorderLayout.NORTH);
				topPanel.add(exprPanel, BorderLayout.CENTER);
				add(topPanel, BorderLayout.NORTH); 

				ontologyTree = getKeywordTree();
			// 	ontologyTree.addPopup(UndefinedBooleanExpression.class, 
// 															new BooleanExpressionPopup());
				JPanel otPanel = new JPanel(new BorderLayout());
				JLabel levelLabel = new JLabel("Available characteristics");
				//levelLabel.setFont(OncBrowserConstants.DIRECTIONS_FONT_SMALL_ITALIC);
				//levelLabel.setForeground(Color.WHITE);
				levelLabel.setFont(new Font("Dialog", Font.BOLD, 12));
				otPanel.add(levelLabel,
										BorderLayout.NORTH);
				otPanel.add(ontologyTree, BorderLayout.CENTER);
				add(otPanel, BorderLayout.CENTER );

				// Make a panel with a field for text input for numeric levels
				numericPanel = new JPanel();
				numericPanel.add(numericLevel);
				ontologyTree.getGenericTreeSelectionListener().addSingleClickListener(this);
		}

		protected OntologyTree getKeywordTree() {
				OntologyTree ot = new OntologyTree();
				ot.collapseController();
				ot.setFilterSelected(true);
				ot.setName("Two Level Parameter Keyword Ontology Tree");
				ot.getOntologyButtonPanel().setRoot(OntologyMap.K);
				ot.getOntologyButtonPanel().setLeaves
						(CollectionHelper.makeVector(new Integer(OntologyMap.K)));
				if ( param != null ) {
 						ot.setFilter(getFilter(param.getConstraintKeyword()));
 						param.getBaseParameter().addSaveListener(this);
 				}
				JToolBar toolBar  = ot.getToolBar();
				AddLevelListAction addLevelListAction = 
						new AddLevelListAction("LL+");
				OncBrowserButton addLevelListBtn = new OncBrowserButton(ot.getTree(), 
																																addLevelListAction);
				JButton showLevelListBtn = new JButton("LLs"); //temporary
				addLevelListBtn.addActionListener(this);
				toolBar.add(addLevelListBtn);
				OncBrowser.addOntologyTree(ot);

				return ot;
		}	


		public  void edit(Object objectToEdit){
				edit((TwoLevelParameter)objectToEdit );
				setVisible(true);
		}
		
		public void edit(TwoLevelParameter param) {
				this.param = param;
				// Reset the keyword tree using the keyword restraint
				if ( keywordConstraint != param.getConstraintKeyword() ) {
						ontologyTree.setFilter(getFilter(param.getConstraintKeyword()));
						keywordConstraint = param.getConstraintKeyword();
						ontologyTree.refresh();
				}
				if (fromLevelMenu != null)
						fromLevelMenu.addHyperTextMenuListener(param);
				this.setSaveToDataSourceOnCreate((param.getPersistibleState() == Persistible.DO_NOT_SAVE ? false : true));
				try { 
						if (param == null) { 
								System.out.println("Null two level parameter");
								return;
						}
						oncProcessMenu.setTheList
								(ProcessDeclaration.getAllProcessesCollection());
						if ( param.getVariableDefinitions() != null 
								 && param.getVariableDefinitions().size() > 0 
								 && param.getVariableDefinitions().firstElement() 
								 instanceof EnumDefinition) {
								enumDefinition = 
										(EnumDefinition)param.getVariableDefinitions().firstElement();
								if ( enumDefinition.getProcess() != null )
										oncProcessMenu.setSelectedObject(enumDefinition.getProcess());
								else if ( defaultOncProcess != null )
										oncProcessMenu.setSelectedObject(defaultOncProcess);
								else 
										oncProcessMenu.setDefaultUIValue();

								if ( enumDefinition.getName() != null )
										keywordLabel.setText(enumDefinition.getName());

								if ( enumDefinition.getLevelList() != null ) {
										updateMenu(fromLevelMenu,  enumDefinition.getLevelList());
										updateMenu(toLevelMenu,  enumDefinition.getLevelList());
								}
						}
						fromLevelMenu.addHyperTextMenuListener(this);
						toLevelMenu.addHyperTextMenuListener(this);
						EnumLevel enumLevel  = null;
						if ( param.getStartLevel() instanceof EnumLevel) {
								fromLevelMenu.setSelectedObject((EnumLevel)param.getStartLevel());
								
						}
						else {
								fromLevelMenu.setDefaultUIValue();
						}
						if ( param.getEndLevel() instanceof EnumLevel) {
								toLevelMenu.setSelectedObject((EnumLevel)param.getEndLevel());
						}
						else {
								toLevelMenu.setDefaultUIValue();
						}
						if (levelDisplayed == false ) {
								exprPanel.add(new JLabel("From"));
								exprPanel.add(fromLevelMenu);
								exprPanel.add(new JLabel("To"));
								exprPanel.add(toLevelMenu);
								levelDisplayed = true;
						}
						
				}
				catch (Exception ee) {
						ee.printStackTrace();
				}
				invalidate();
				repaint();
				setVisible(true);

		}
		
		
		public void save()
		{
				syncEditor();
		}

		public void syncEditor() {
				 System.out.println("syncEditor " + enumDefinition);

				// Put UI values into corresponding datalayer fields
				if (oncProcessMenu != null && enumDefinition != null) {
						// Set the process on the enum definition
						enumDefinition.setProcess
								((ProcessDeclaration)oncProcessMenu.getSelectedObject());
						enumDefinition.setName(keywordLabel.getText());
				// 		System.out.println("Setting enum definition " + enumDefinition
// 															 + " and level list " + eLevelList);
						enumDefinition.setLevelList(eLevelList);
						DefaultPersistibleList varDefns = 
								new DefaultPersistibleList();
						varDefns.add(enumDefinition);
						param.setVariableDefinitions(varDefns);
				}
				if (fromLevelMenu != null 
						&& fromLevelMenu.getSelectedObject() instanceof LevelAndList) {
						param.setStartLevel
								(((LevelAndList)fromLevelMenu.getSelectedObject()).getLevel());
				}

				if (toLevelMenu != null
						&& toLevelMenu.getSelectedObject() instanceof LevelAndList) {
						param.setEndLevel
								(((LevelAndList)toLevelMenu.getSelectedObject()).getLevel());
				}
				//System.out.println("All set " + param);
				param.update();
		}

		public Object getValue()
		{
				return(null);
		}
		public ProcessDeclaration getOncProcess() {
				return defaultOncProcess;
		}
		protected OncFilter makeLevelListFilter(Hashtable keywords) {
				// Create a filter
				filter = new OncFilter(this.getSaveToDataSourceOnCreate());	
				OncTreeNode rootNode = filter.getRootNode();
				OncTreeNode orNode = new OncTreeNode(TcapLogicalOperator.OR);
				OncTreeNode keywordNode;
				for (Enumeration e = keywords.keys();
						 e.hasMoreElements(); ) {
						Object obj = e.nextElement();
						if ( obj instanceof Keyword )
								orNode.add(new OncTreeNode(obj));
				}
				rootNode.add(orNode);
				return filter;
				
		}

		public void objectSaved(SaveEvent e) {
				// If keyword changed then refresh tree
				if ( keywordConstraint != param.getConstraintKeyword() ) {
						ontologyTree.setFilter(getFilter(param.getConstraintKeyword()));
						keywordConstraint = param.getConstraintKeyword();
				}
				ontologyTree.refresh();
		}

		public void objectDeleted(SaveEvent e) {
				// Do nothing the data source will refresh the tree
				//e.getSavedObject().removeSaveListener(this);
		}
		private EnumDefinition createEnumDefinition(GenericTreeNode transferNode) {				boolean persistMode = false;
				if ( transferNode.getUserObject() instanceof Keyword) {
						Keyword key = (Keyword)transferNode.getUserObject();
						enumDefinition = 	
								Keyword.dropKeywordCreateEnum
								(enumDefinition, key, true);
				}
				return enumDefinition;
		}

		public void singleClicked(SingleClickEvent evt){
				System.out.println("singleClicked" );
				if ( evt.getSource() instanceof JTree ) {
						Object obj = 
								((JTree)evt.getSource()).getLastSelectedPathComponent();
						if ( obj instanceof GenericTreeNode ) {
								 enumDefinition = 
										createEnumDefinition((GenericTreeNode)obj);
								if ( enumDefinition == null ) 
										System.out.println("Unable to create enum definition. " 
																			 + obj);
								else
										updateExpressionPanel(enumDefinition);
						}
						else
								return;
				}

		}
		private void updateExpressionPanel(EnumDefinition enumDefinition) {
				keywordLabel.setText(enumDefinition.getKeyword().toString());
				if ( param != null ) {
						DefaultPersistibleList varDefns = 
								new DefaultPersistibleList();
						varDefns.add(enumDefinition);
						param.setVariableDefinitions(varDefns);
				}
				// Now get possible level list and display the 
				// from & to level menus
				fromLevelMenu.clear();
				toLevelMenu.clear();
				Vector levelLists = enumDefinition.getKeyword().getAssociatedLevelLists();
				Iterator i = levelLists.iterator();
				while ( i.hasNext() ) {
						Vector levelsWithLists = new Vector();
						// Add each levels for each level
						EnumLevelList llist = (EnumLevelList)i.next();
						Iterator ii = llist.getLevelIterator();
						if (!ii.hasNext()  && llist.isNumericList()) {
								levelsWithLists.addElement
										(new LevelAndList(llist, 
																			new EnumLevel(llist.toString())));
						}
						else {
								while (ii.hasNext()) {
										levelsWithLists.addElement
												(new LevelAndList(llist, (EnumLevel)ii.next()));
										
								}
						}
						fromLevelMenu.addToTheList(levelsWithLists);
						toLevelMenu.addToTheList(levelsWithLists);
				}
				if (levelDisplayed == false ) {
						exprPanel.add(new JLabel("From"));
						exprPanel.add(fromLevelMenu);
						exprPanel.add(new JLabel("To"));
						exprPanel.add(toLevelMenu);
						levelDisplayed = true;
				}
				exprPanel.revalidate();
		}


		public class DroppableTextField extends JTextField 
				implements DropTargetListener {

				public DroppableTextField (int s) {
						super(s);
						new DropTarget(this, this);
				}
				public void dragEnter(DropTargetDragEvent evt) {
            // Called when the user is dragging and enters this drop target
        }
        public void dragOver(DropTargetDragEvent evt) {
            // Called when the user is dragging and moves over this drop target.
        }
        public void dragExit(DropTargetEvent evt) {
            // Called when the user is dragging and leaves this drop target.
        }
        public void dropActionChanged(DropTargetDragEvent evt) {
            // Called when the user changes the drag action between copy or move.
        }
				public void drop(DropTargetDropEvent evt) {
						try {
								Transferable t = evt.getTransferable();
								if ( t.isDataFlavorSupported(Droppable.genericTreeNode) ) {
										evt.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
										Object transferableData = 
												t.getTransferData(Droppable.genericTreeNode);
										GenericTreeNode transferNode = 
												(GenericTreeNode)transferableData;
										boolean persistMode = false;
										if ( transferNode.getUserObject() instanceof Keyword) {
												Keyword key = (Keyword)transferNode.getUserObject();
												enumDefinition = 	
														Keyword.dropKeywordCreateEnum
														(key, true);
												setText(key.toString());
												DefaultPersistibleList varDefns = 
														new DefaultPersistibleList();
												varDefns.add(enumDefinition);
												param.setVariableDefinitions(varDefns);
												// Now get possible level list and display the 
												// from & to level menus
												fromLevelMenu.clear();
												toLevelMenu.clear();
												Vector levelLists = key.getAssociatedLevelLists();
												Iterator i = levelLists.iterator();
												while ( i.hasNext() ) {
														Vector levelsWithLists = new Vector();
														// Add each levels for each level
														EnumLevelList llist = (EnumLevelList)i.next();
														Iterator ii = llist.getLevelIterator();
														if (!ii.hasNext()  && llist.isNumericList()) {
																levelsWithLists.addElement
																		(new LevelAndList(llist, 
																											new EnumLevel(llist.toString())));
														}
														else {
																while (ii.hasNext()) {
																		levelsWithLists.addElement
																				(new LevelAndList(llist, (EnumLevel)ii.next()));
																		
																}
														}
														fromLevelMenu.addToTheList(levelsWithLists);
														toLevelMenu.addToTheList(levelsWithLists);
												}
												//fromLevelMenu.setTheList(levelLists);
												if (levelDisplayed == false ) {
														exprPanel.add(new JLabel("From"));
														exprPanel.add(fromLevelMenu);
														exprPanel.add(new JLabel("To"));
														exprPanel.add(toLevelMenu);
														levelDisplayed = true;
												}
												exprPanel.revalidate();
												// Also add a reset level list button in case user
												// makes a mistake
												// on the hyperlist text menu can each grouping 
												// be made a different rows
										}
								}
						} catch (Exception e) {
								evt.rejectDrop();
						} 
						//catch (UnsupportedFlavorException e) {
						// 								evt.rejectDrop();
						// 						}
				}
		}
		private void updateMenu(HyperTextMenu menu, EnumLevelList llist ) {
				System.out.println("updateMenu " + menu);
				// Add each levels for each level
				Iterator ii = llist.getLevelIterator();
				Vector levelsWithLists = new Vector();

				if (!ii.hasNext()  && llist.isNumericList()) {
						levelsWithLists.addElement
								(new LevelAndList(llist, 
																	new EnumLevel(llist.toString())));
				}
				else {
						while (ii.hasNext()) {
								levelsWithLists.addElement
										(new LevelAndList(llist, (EnumLevel)ii.next()));
								
						}
				}
				menu.addToTheList(levelsWithLists);
		}

} 

