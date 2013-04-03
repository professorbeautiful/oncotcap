package oncotcap.display.editor.persistibleeditorpanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;

import java.util.*;
import java.io.*;
import java.lang.reflect.Array;
import oncotcap.util.*;
import oncotcap.datalayer.*;
import oncotcap.datalayer.Persistible;
import oncotcap.datalayer.autogenpersistible.*;

import oncotcap.display.browser.*;
import oncotcap.display.common.*;


import oncotcap.display.editor.EditorFrame;

import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.parameter.*;

import javax.swing.tree.*;

public class BooleanExpressionEditorPanel extends EditorPanel 
		implements ActionListener,
							 HyperTextMenuListener,
							 SaveListener {
		static private Vector operatorsList = TcapOperator.getBooleanExpressionOperators();
		static private Vector equalsOnly = TcapOperator.getAssignmentOperators();
		static private String blankLabel = "";
		static private ProcessDeclaration defaultOncProcess = null; // cancer cell
		private boolean operatorSet = false;
		private OncFilter filter;
		private boolean showOperator = true;

		JPanel exprPanel = new JPanel();

		JTextField numericLevel = null;
		JPanel numericPanel = null;
		HyperTextMenu oncProcessMenu = null;
		JTextField keywordLabel = null;
		HyperTextMenu operatorMenu = null;
		HyperTextMenu levelMenu = null;
		SortedList list = null;
		SortedList list2 = null;
		BooleanExpression expr = null;
		ProbabilityTable probabilityTable = null;
		public BooleanExpressionEditorPanel() {
				init();
		}

		public BooleanExpressionEditorPanel(BooleanExpression expr) {
				init();
				this.expr = expr;
				//edit(expr);
		}
		public BooleanExpressionEditorPanel(BooleanExpression expr, 
																				boolean showOperator) {
				this.showOperator = showOperator;
				init();
				this.expr = expr;
				//edit(expr);
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
				//System.out.println("keywordConstraint " + keywordConstraint);
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
// 				System.out.println("hyper text menu event " + e.getSource());
				if ( e.getSource().equals(levelMenu)) {	
						EnumLevelList eLevelList = null;
						if ( e.getSelectedItem() instanceof LevelAndList) {
								LevelAndList levelAndList = (LevelAndList)e.getSelectedItem();
								EnumLevel eLevel = null;
								eLevelList = levelAndList.getLevelList();
								eLevel = levelAndList.getLevel();
								if ( eLevelList != null ) 
										if ( eLevelList.isNumericList() )
												exprPanel.add(numericLevel);
						}
						// If the operator is 'has probability' then display 
						// a probability table
						if (  expr != null && expr.getOperator() != null
									&& "P".equals(expr.getOperator().toString()) ) {
								probabilityTable = new ProbabilityTable();
								// Collect the data to display in the table
								probabilityTable.initProbTable(keywordLabel.getText(), 
																				  eLevelList);
								
								Dimension defaultSize = null;
								if ( probabilityTable instanceof Editable) {
										EditorPanel editorPanel =
												EditorFrame.showEditor((Editable)probabilityTable, 
																							 defaultSize,
																							 300, 300, null);
										if ( probabilityTable instanceof Persistible ) {
												probabilityTable.addSaveListener(editorPanel);
												probabilityTable.addSaveListener(this);
										}
								}
								else {
										JOptionPane.showMessageDialog
												(null, 
												 "There is no editor for the selected object.");
								}

	
						}
				}
		}


		private void init() {
				setLayout(new FlowLayout());
				setPreferredSize( new Dimension(600,60));
				// expression panel
				JLabel processLabel = new JLabel("Process");
				keywordLabel = new JTextField(30);
				numericLevel = new JTextField(10);
				oncProcessMenu = new HyperTextMenu("Process");
				oncProcessMenu.addActionListener(this);
				operatorMenu = new HyperTextMenu("Operator");
				operatorMenu.addActionListener(this);
				levelMenu = new HyperTextMenu("Levels");
				levelMenu.addHyperTextMenuListener(this);

				exprPanel.add(oncProcessMenu);
				exprPanel.add(keywordLabel);
				if ( showOperator ) 
						exprPanel.add(operatorMenu);
				add(exprPanel); 

				// Make a panel with a field for text input for numeric levels
				numericPanel = new JPanel();
				numericPanel.add(numericLevel);
		}

		public  void edit(Object objectToEdit){
				//System.out.println("EDIT boolean expression>>> " + objectToEdit);
				edit((BooleanExpression)objectToEdit );
				setVisible(true);
		}
		
		public void edit(BooleanExpression expr) {
				this.expr = expr;
				if (levelMenu != null)
						levelMenu.addHyperTextMenuListener(expr);
				this.setSaveToDataSourceOnCreate((expr.getPersistibleState() == Persistible.DO_NOT_SAVE ? false : true));
				try { 
				if (expr == null) { 
						System.out.println("Null boolean expression");
				}
				if ( expr.getLeftHandSide() != null ) 
						keywordLabel.setText(expr.getLeftHandSide().toString());
				oncProcessMenu.setTheList
						(ProcessDeclaration.getAllProcessesCollection());
				if ( expr.getLeftHandSide() instanceof EnumDefinition) {
						EnumDefinition enoom = (EnumDefinition)expr.getLeftHandSide();
						if ( enoom.getProcess() != null )
								oncProcessMenu.setSelectedObject(enoom.getProcess());
						else if ( defaultOncProcess != null )
								oncProcessMenu.setSelectedObject(defaultOncProcess);
						if ( enoom.getName() != null )
								keywordLabel.setText(enoom.getName());
				}
				levelMenu.addHyperTextMenuListener(this);
				EnumLevel enumLevel  = null;
				if ( expr != null && expr.getRightHandSide() instanceof EnumLevel) {
						enumLevel = (EnumLevel)expr.getRightHandSide() ;
						//System.out.println("RIGHT HAND SIDE " + expr.getLeftHandSide());
						levelMenu.setSelectedObject(enumLevel);
				}
				try { 
				EnumLevelList ll = null;
				if ( expr != null && 
						 expr.getLeftHandSide() instanceof EnumDefinition) {
						Keyword keyword = 
								((EnumDefinition)expr.getLeftHandSide()).getKeyword();
						ll = 
								((EnumDefinition)expr.getLeftHandSide()).getLevelList();
						if ( ll != null ) {
								if ( ll.isNumericList() ){
										exprPanel.add(numericLevel);
										if ( enumLevel != null )
												numericLevel.setText(enumLevel.getName());
										exprPanel.add(new JLabel(ll.toString()));
								}
								else {
										list = ll.getLevels();
										levelMenu.setTheList(list);
										exprPanel.add(levelMenu);
							 	}
						}
						else { // no level list or level has been selected
								// show all available levels and level lists
								Vector levelLists = keyword.getAssociatedLevelLists();
		// 						System.out.println("Associated Level List " 
// 																	 + keyword +
// 																	 " --> " 
// 																	 +levelLists);
								// Compile a list of levels
								Iterator i = levelLists.iterator();
								while ( i.hasNext() ) {
										Vector levelsWithLists = new Vector();
										// Add each levels for each level
										EnumLevelList llist = (EnumLevelList)i.next();
										Iterator ii = llist.getLevelIterator();
										if (!ii.hasNext()  && llist.isNumericList()) {
												levelsWithLists.addElement
														(new LevelAndList(llist, 
																							new EnumLevel(llist.toString())));										}
										else {
												while (ii.hasNext()) {
														levelsWithLists.addElement
																(new LevelAndList(llist, (EnumLevel)ii.next()));
														
												}
										}
										levelMenu.addToTheList(levelsWithLists);
								}
								exprPanel.add(levelMenu);
						}
				}		

				//System.out.println("EXPRESSION TYPE " + expr.getExpressionType() );
				if (  expr != null && 
							expr.getExpressionType() == DeclareEnumPicker.BOOLEAN_EXPRESSION ) {
						// Remove the probabilty operator - it is only for assignments
						operatorMenu.setTheList(TcapOperator.getBooleanExpressionOperators());
				}
				else if (  expr != null && 
									 expr.getExpressionType() == DeclareEnumPicker.INSTANTIATION ) {
						operatorMenu.setTheList(TcapOperator.getEqualOnly());
						operatorMenu.setSelectedObject
								(TcapOperator.getEqualOnly().firstElement());
						operatorSet = true;
						if ( expr != null )
								expr.setOperator((TcapOperator)TcapOperator.getEqualOnly().firstElement());
				}
				else {
						operatorMenu.setTheList(TcapOperator.getAssignmentOperators());

				}
				if (  expr != null )
						operatorMenu.setSelectedObject(expr.getOperator());
				}catch ( Exception e) {
						e.printStackTrace();
				}
				
				// 	System.out.println("levelMenu " + levelMenu.getSelectedObject());
				// 	System.out.println("operatorMenu " 
				// + operatorMenu.getSelectedObject());
				// 	System.out.println("processMenu " 
				// + oncProcessMenu.getSelectedObject());
				levelMenu.setDefaultUIValue();
				if ( !operatorSet )
						operatorMenu.setDefaultUIValue();
				oncProcessMenu.setDefaultUIValue();
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
				if ( expr.getLeftHandSide() instanceof EnumDefinition ) {
						EnumDefinition enoom = (EnumDefinition)expr.getLeftHandSide();
						if (levelMenu.getParent() != null) { // if the level menu  on panel
								LevelAndList levelAndList = null;
								EnumLevel eLevel = null;
								EnumLevelList eLevelList = null;
										
								if ( levelMenu.getSelectedObject() instanceof LevelAndList) {
										levelAndList = 
												(LevelAndList)levelMenu.getSelectedObject(); 
										eLevelList = levelAndList.getLevelList();
										enoom.setLevelList(eLevelList);
										eLevel = levelAndList.getLevel();
										expr.setRightHandSide(eLevel) ;
								}
								else if ( levelMenu.getSelectedObject() instanceof EnumLevel) {
										eLevel = (EnumLevel)levelMenu.getSelectedObject();
										if ( (enoom.getLevelList() != null 
														 && enoom.getLevelList().isNumericList())  ) {
												eLevel.setName(numericLevel.getText());
												//eLevel.setLevelList(numericLevel.getText());
												expr.setRightHandSide(eLevel) ;
								
										}
										else if (  "P".equals(expr.getOperator().toString()) && 
															 probabilityTable != null) {
												expr.setRightHandSide(probabilityTable) ;
										}
										else {
												eLevel = (EnumLevel)levelMenu.getSelectedObject();
												expr.setRightHandSide(eLevel) ;
										}
								}
								expr.setRightHandSide(eLevel) ;
						}// if levelMenu.getParent() != null
						if (oncProcessMenu != null) {
								defaultOncProcess = 
										(ProcessDeclaration)oncProcessMenu.getSelectedObject();
								enoom.setProcess
										(defaultOncProcess);
						}
						enoom.setName(keywordLabel.getText());
				}
				if (operatorMenu != null) {
						expr.setOperator((TcapOperator)operatorMenu.getSelectedObject());
				}
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
				if ( (e.getSavedObject() instanceof ProbabilityTable) ) {
						if ( expr != null ) {
								expr.setRightHandSide((ProbabilityTable)e.getSavedObject());
						}
				}
		}

		public void objectDeleted(SaveEvent e) {
				// Do nothing the data source will refresh the tree
				//e.getSavedObject().removeSaveListener(this);
		}

} 

