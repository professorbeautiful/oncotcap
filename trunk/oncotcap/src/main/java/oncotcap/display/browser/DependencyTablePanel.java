
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

import java.awt.datatransfer.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.beans.*;
import java.util.regex.*;
import java.net.URL;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.text.TextAction;

import java.lang.reflect.Array;
import javax.swing.tree.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import oncotcap.datalayer.persistible.CodeBundle;
import oncotcap.Oncotcap;

import oncotcap.engine.VariableDependency;
import oncotcap.display.common.*;
import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.datalayer.persistible.action.*;
import oncotcap.datalayer.persistible.parameter.*;

import oncotcap.display.editor.EditorFrame;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.display.common.ListDialog;
import oncotcap.display.browser.*;
import oncotcap.util.*;
import java.awt.dnd.*;


/**
 * @author    morris
 * @created   
 */
public class DependencyTablePanel extends JPanel implements OncFrameable {
    private static String ROOT = String.valueOf(-1);
		private String title  =null;
		private int nextCounter = 0;
		JScrollPane scrollPane = null;
		// DEPENDENCY TABLE
		OncModelTableModel oncModelTableModel = null;		
		OncModelTable oncModelTable = null;
		TableSorter  sorter = null;
		Hashtable varDependencies = new Hashtable();
		Vector varDependencies2 = new Vector();
		Vector  allClasses = new Vector();
		Vector syntaxErrors = new Vector();
		Hashtable varToDepStrings = new Hashtable();
		Vector preprocessedCodeBundles = new Vector();
		public Vector allPreprocessedActions = new Vector();
		public Hashtable allVariables = new Hashtable();
		Vector variables = new Vector();
		Vector dependencies = new Vector();
		Vector columnHeadings = new Vector();
		NextButton nextButton = null;
		ModelController mc = null;
		static int methodCnt = 0;
		static final int PROCESS_VARIABLE = 1;
		static final int PROCESS_METHOD = 2;
		static final int ENUM = 3;
		static final int ADD_VARIABLE = 1;
		static final int INSTANTIATE = 2; 
		static final int TRIGGER_EVENT = 3;
		static final int MODIFY_VARIABLE = 4 ;
		static final int SCHEDULE_EVENT = 5;
		static final int MODIFY_SCHEDULE = 6;
		static final int INIT_VARIABLE = 7;
		static final int ADD_CODE = 8;
		static final int SELECTOR = 0;
		static final int THIS = 1;
		static final int RELATIONSHIP = 2;
		static final int THAT = 3;
		public static Pattern quotedVar = 
				Pattern.compile("`.*?`", Pattern.DOTALL);
		public static Pattern commentedCode = 
				Pattern.compile("/\\*.*\\*/", Pattern.DOTALL);
		public static Pattern isEqualTo = 
				Pattern.compile("\\=\\=.*?\\)", Pattern.DOTALL);
		public static Pattern backQuote = 
				Pattern.compile("`", Pattern.DOTALL);
		public static Pattern getContainerInstance = Pattern.compile("\\(\\(.*?\\) getContainerInstance\\(.*\\.class\\)\\)", Pattern.DOTALL);

		//(((.*?) getContainerInstance(.*?.class))


		private OncPopupMenu pathPopupMenu = new OncPopupMenu("Table Column Popup");		
		// DOT FILE STUFF 
		StringBuilder dotFileRep = new StringBuilder();
		UniqueVector dotFileRelationships = new UniqueVector();
		UniqueVector nodeVector = new UniqueVector();
		Hashtable processNodeMap = new Hashtable();
		Color [] diagramLightColors = {OncBrowserConstants.EColorVeryPale, 
																	 OncBrowserConstants.MBColorVeryPale, 
																	 OncBrowserConstants.MBColorPale,
																	 OncBrowserConstants.KCColorPale, 
																	 OncBrowserConstants.CBColorPale };
		Color [] diagramDarkColors = {OncBrowserConstants.MBColorDark, 
																	OncBrowserConstants.KColorDarkest, 
																	OncBrowserConstants.EColorDarkest,
																	OncBrowserConstants.KAColorDarkest, 
																	OncBrowserConstants.MBColorDarkest, 
																	OncBrowserConstants.CBColorDarkest};
		int colorCounter = 0;
		/** Constructor for the OncBrowser object */
		public DependencyTablePanel() {
				setLayout(new BorderLayout());
				setSize(new Dimension(860,600));
				//setIconImage(OncoTcapIcons.getDefault().getImage());
				init();
		}
		
		private void init() {
				setInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT,
										OncBrowser.getDefaultInputMap());
				setInputMap(JComponent.WHEN_FOCUSED,
										OncBrowser.getDefaultInputMap());
				setActionMap(OncBrowser.getDefaultActionMap());
				Vector vecOfVectors = new Vector();
				Vector vec = new Vector();
				columnHeadings.addElement("*");
				columnHeadings.addElement("This");
				columnHeadings.addElement("Influences");
				columnHeadings.addElement("That");
				columnHeadings.addElement("If Clause");
				columnHeadings.addElement("Action");
				columnHeadings.addElement("CB");
				columnHeadings.addElement("ST");
				columnHeadings.addElement("SB");
				columnHeadings.addElement("Next");
				
				oncModelTableModel = 
						new OncModelTableModel(columnHeadings,0);
				//oncModelTable = new OncModelTable(oncModelTableModel);
				
				sorter = new TableSorter(oncModelTableModel);
        oncModelTable = new OncModelTable(sorter);
				oncModelTable.addMouseListener(new ColumnListener());
			// 	SelectionListener listener = new SelectionListener(oncModelTable);
// 				oncModelTable.getSelectionModel().addListSelectionListener(listener);
// 				oncModelTable.getColumnModel().getSelectionModel()
// 						.addListSelectionListener(listener);

				nextButton = new NextButton("Next");
        sorter.addMouseListenerToHeaderInTable(oncModelTable);
				
				scrollPane = new JScrollPane(oncModelTable);
				add(scrollPane, BorderLayout.CENTER);

				initColumnPopup();
				
				setSize(new Dimension(800,600));
				setVisible(true);
		}
		
		private void initColumnPopup(){
				// Add menu items to the popup
				// Allow erasing path number 
				JMenuItem mi;
				mi = new JMenuItem("Remove From Path");
				pathPopupMenu.add(mi);
				pathPopupMenu.setOpaque(true);
				pathPopupMenu.setLightWeightPopupEnabled(true);
		}

		public String getGraphicRepresentation(ModelController mc) {
				buildDependencyTable(mc);
				writeDotFile();
				return dotFileRep.toString();

		}
		public void buildDependencyTable(ModelController mc) {
				// Make sure all dependencies have been cleared
				dependencies.clear();
				allVariables.clear();

				this.mc = mc;
				formatTable();

				// Determine which statement bundles to include in the model
				DefaultTreeModel treeModel = gatherStatementBundles(mc);

				// prune the tree to remove all duplicate top level SB
				removeDuplicateSBs(treeModel);
				
				///TEST CODE
				//JFrame treePanel = new JFrame();
				//treePanel.getContentPane().add(tree);
				//treePanel.setVisible(true);

				// Pre process all the code bundles to resolve all back quotes 
				// and find all variables 
				// Get all code bundle nodes in the tree - a code bundle 
				// will probably exist more than once but only once in a unique path
				// of st, sbs 
				Vector codeBundleNodes = getNodesOfType(treeModel, CodeBundle.class);
				//System.out.println("NUMBER OF CBs " + codeBundleNodes.size());
				Iterator i = codeBundleNodes.iterator();
				while ( i.hasNext() ) {
						GenericTreeNode treeNode = (GenericTreeNode)i.next();
						// As each CB is preprocessed it will be added to a vector 
						// and so will each preprocessed action
						//System.out.println("PROCESS CB " + treeNode);
						preprocessCodeBundle(treeNode);
				}
				// Now go through the preprocessed actions and create dependencies
				processDependencies();

				//use the newly created dependencies to update the table
				oncModelTableModel.setDataVector(dependencies, columnHeadings);
				oncModelTable.setColumnWidths();
				System.out.println("All classes " + allClasses);

				//writeDotFile();
		}

		
		// Display a  table that lists all vartiables and variables that are 
		// dependent on it and that it depends on - just a summary table
		public void showVariableDependencyTable() {
				Vector varDepVecOfVec = new Vector();
				Hashtable rehashedDependencies = 
						HashtableHelper.invertHashtable(varDependencies);
				// Now make the vector of vector data set
				//Take all the variables then loop through each one and get
				// from the two dependency hashtables to 
				// fill in the columns
				Vector tableRow = null;
				Vector dependsOnVec = null;
				Vector dependedOnVec = null;
				for (Enumeration e = allVariables.elements() ; 
						 e.hasMoreElements() ;) {
						Object key = e.nextElement();
						tableRow = new Vector();
						tableRow.add(0, key);
						dependsOnVec = (Vector)varDependencies.get(key);
						tableRow.add(1, dependsOnVec);
						dependedOnVec = (Vector)rehashedDependencies.get(key);
						tableRow.add(2, dependedOnVec);
						varDepVecOfVec.add(tableRow);
				}
				Vector columnNames = new Vector();
				columnNames.add(0, "Variable");
				columnNames.add(1, "Depends On Variables");
				columnNames.add(2, "Depended On By Variables");
				columnNames.add(3, "Is Conditioned Upon");
		
				JTable tbl = new JTable(varDepVecOfVec, columnNames);
				JScrollPane scrollPane = new JScrollPane(tbl);
				JFrame f = new JFrame();
				f.getContentPane().add(scrollPane);
				f.setSize(new Dimension(500, 300));
				f.setVisible(true);

				//System.out.println("varDepVecOfVec " + varDepVecOfVec);
				

		}
		public void determineVariableDependentOrder() {
		// 		// Make an orderd list of variables - wait for WES changes
		}
		public Hashtable getVariableDependencies() {
				return varDependencies;
		}
		public Vector getVariablesDependentOn(Variable variable, 
																					Hashtable varDependencies) {
				return (Vector)varDependencies.get(variable);
		}

		public boolean isIndependentVariable(Variable variable, 
																				 Hashtable varDependencies) {
				if ( varDependencies.get(variable) == null )
						return true;
				else
						return false;
		}

		public boolean isVariableDependentOn(Variable thisVariable, 
																				 Variable thatVariable, 
																				 Hashtable varDependencies) {
				Vector dependentVariables = (Vector)varDependencies.get(thisVariable);
				if ( dependentVariables.contains(thatVariable) )
						return true;
				else
						return false;
		}

		public void processDependencies() {

				Iterator i = allPreprocessedActions.iterator();
				PreprocessedAction ppAction = null;
				while ( i.hasNext()) {
						ppAction = (PreprocessedAction)i.next();
						// Create dependency for action
						GenericTreeNode node = 
								ppAction.getPreprocessedCodeBundle().getTreeNode();
						StatementBundle sb = 
								(StatementBundle)GenericTreeNode.getClosest(StatementBundle.class, node);						
						StatementTemplate st = sb.getStatementTemplate();
						CodeBundle cb = ppAction.getPreprocessedCodeBundle().getCodeBundle();
						String ifClause = ppAction.getPreprocessedCodeBundle().getIfClause();
						OncAction action = ppAction.getOncAction();
						String influence = ppAction.getActionDisplayName();
						Object resolvedActor = getResolvedActor(ppAction.getPreprocessedCodeBundle());
						Object recipient = getObjectOfThisAction(action, 
																										 ppAction.getPreprocessedCodeBundle());
						createDependency( sb,
														  st,
														  cb,
														  action,
															ifClause,
														  influence,
														  resolvedActor,
														  recipient);
						// Create dependencies for variables
						createVariableDependencies(ppAction,sb,
																	 st,
																	 cb,
																	 action,
																	 ifClause);

				} // while actions exist
		}

		private Vector getConditionalDependencies(String ifClause,
																							PreprocessedCodeBundle ppCB) {
				Vector conditionalDependencies = new Vector();
				String className = ppCB.getProcessName();
				// Now create variable dependencies for if clause
				if ( ifClause != null && ifClause.trim().length() > 0) {
						ifClause = "if ( "  + ifClause + " ) {a = 1;}";
						//System.out.println("ifClause " + ifClause);
						String varString = null;
						try {
								oncotcap.util.DissectString dString = 
										new DissectString(ifClause);
								OncASTTreeModel astTreeModel = dString.dissectString();
								if ( astTreeModel == null ) {
										syntaxErrors.addElement(new SyntaxError(ppCB, ifClause, "Parse error"));
										return new Vector();
								}
								Hashtable condDep = 
										astTreeModel.getAllConditionalDependencies();
								Vector varDeps = new Vector(condDep.values());
								//System.out.println("varDeps " + varDeps);
								Vector varVec = (Vector)varDeps.firstElement();
								Iterator varIter = varVec.iterator();
								while ( varIter.hasNext() ) {
										varString = 
												((VariableDependency)varIter.next()).getRightVariableName();
										Variable var = new Variable(ppCB, varString);
										conditionalDependencies.add(var);
										// add variable to all variables
										if ( allVariables.get(var.getFullName()) == null) {
												allVariables.put(var.getFullName(), var);
										}
								}
						}catch(Exception ex) {
								System.out.println("dissect string failed" );
								ex.printStackTrace();
						}
				} // if ifClause
						return conditionalDependencies;

		}
		public void createVariableDependencies(PreprocessedAction ppAction,
																					 StatementBundle sb,
																					 StatementTemplate st,
																					 CodeBundle cb,
																					 OncAction action,
																					 String ifClause) {
				Variable variable  = null;
				Hashtable varInits = ppAction.getVariableHashtable();
				//System.out.println("varInits " + varInits + " from " + ppAction.getVariableHashtable());
				Vector conditionalDependencies = getConditionalDependencies(ifClause, ppAction.getPreprocessedCodeBundle());
				//System.out.println("conditionalDependencies " + conditionalDependencies);
				// Now add conditional dependencies 
				Iterator condDeps = conditionalDependencies.iterator();
				variable = ppAction.getVariable();
				if ( variable != null )
						while ( condDeps.hasNext() ) {
								if ( variable != null )
										createDependency( sb,
																			st,
																			cb,
																			action,
																			ifClause,
																			"Conditionally Modifies",
																			condDeps.next(),
																			variable);
						}
				for (Enumeration e=varInits.keys(); 
						 e.hasMoreElements(); ) {
						String varString = (String)e.nextElement();
						//System.out.println("process varInits " + varString);
						// Get the variable
						variable = (Variable)allVariables.get(varString);
						// If this is an instantiation action then create another 
						// action that shows the initialization of variables
						// ( from prop files ) for continuity sake
						String variableModificationRelationship = null;
						if ( ppAction.getActionType() == INSTANTIATE ) {
								Object actor = getObjectOfThisAction(ppAction.getOncAction(), 
																										 ppAction.getPreprocessedCodeBundle());
								createDependency( sb,
																	st,
																	cb,
																	action,
																	ifClause,
																	"Initializes",
																	actor,
																	variable);
								variableModificationRelationship = "Determines Initial Value";
								
						}
						// for now just process first value
						String valString = 
								(String)((Vector)varInits.get(varString)).firstElement();										// For each variable look at the value
						if ( isConstantValue(valString) ) {
								// if the value is a constant don't make a dependency
						}
						else {
								// if the value is a statement see if any strings match 
								// variables from variable list - IS THIS OK cause
								// the parse is separating the variables from the class
								getVariableMatches(variable, valString,
																	 sb,
																	 st,
																	 cb,
																	 action,
																	 ifClause, 
																	 variableModificationRelationship);
						}
				} // for


		}
		public void getVariableMatches(Variable variable,
																	 String value,
																	 StatementBundle sb,
																	 StatementTemplate st,
																	 CodeBundle cb,
																	 OncAction action,
																	 String ifClause,
																	 String relationship) {
				if ( relationship == null )
						relationship = "Modifies Variable";
				// look in the allVariables list and only create dependencies 
				// for values from the right hand side that are known variables
				// ( ignore things like math.log etc
				Iterator i = allVariables.values().iterator();
				Variable knownVar = null;
				value = fixGetContainerInstance(value);
				while ( i.hasNext() ) {
						knownVar = (Variable)i.next();
				// 		System.out.println("VARIABLE DEPENDENCY 2 " +
// 																					 variable +
// 																					 " value " + value
// 																					 + " knownVar " + knownVar);
						if ( value.indexOf(knownVar.getFullName()) > -1 ) {
								// The value contains this variable
								// add to dependencies
								// 	System.out.println("VARIABLE DEPENDENCY " +
								// 										 variable +
								// 										 " value " + value);
								createDependency( sb,
																	st,
																	cb,
																	action,
																	ifClause,
																	relationship,
																	knownVar,
																	variable );
								
						}
						// If the known variable process name matches the 
						// process of this action then strip the process name 
						// from known variable and see if the variable
						// shortname exists in the value ( finds local vars ) 
					
						else if (knownVar.getClassName() != null && 
										 knownVar.getClassName().equals
								 (StringHelper.javaName(cb.getProcessDeclaration().getName())) ){
								
								// Parse this value and compare to variables 
								String statement = null;
								try {
										if ( value == null || value.trim().length() == 0 )
												return;
										if ( value.indexOf("=") > -1 ) {
												statement = value.substring(0, value.length());
										}
										else 
												statement = value; // + ";";
										oncotcap.util.DissectString dString = 
												new DissectString(statement);
										OncASTTreeModel astTreeModel = dString.dissectString();
										Vector dependencies = astTreeModel.getAllVariables();
										Iterator dep = dependencies.iterator();
										while ( dep.hasNext() ) {
												String depVariable = (String)dep.next();
												depVariable = StringHelper.javaName(depVariable);
												if ( ((String)knownVar.getVariableName()).equals(depVariable) ) {
													// 	System.out.println("knownVar " + knownVar + 
// 																							 " " + knownVar.getVariableName());
														createDependency( sb,
																							st,
																							cb,
																							action,
																							ifClause,
																							relationship,
																							knownVar,
																							variable);	
												}

										}
								} catch (Exception ex) {
										System.out.println("error parsing statement: " 
																			 + statement);
								}
						}
				}
		}

		static public boolean isConstantValue(String str) {
				boolean isConstantValue = false;
				if ( str == null ) 
						return true;
				if ( str.startsWith("\"") && str.endsWith("\"") )
						return true;
				try {
						Integer.parseInt(str);
						return true;
				}
				catch ( NumberFormatException nfe) {
				}
				try {
						Float.parseFloat(str);
						return true;
				}
				catch ( NumberFormatException nfe) {
				}
				try {
						Double.parseDouble(str);
						return true;
				}
				catch ( NumberFormatException nfe) {
				}
				return isConstantValue;
		}
		public Object getResolvedActor(PreprocessedCodeBundle ppcb) {
							
				String methodName = "";
				String processName = "";
				processName = ppcb.getProcessName() ;
				methodName = ppcb.getMethodName();
				if ( methodName == null ) {
						methodName = ppcb.getEventName();
						System.out.println("PPCB : methodName " + ppcb 
															 + " -- " + ppcb.getMethodName());
						return new AnEvent(ppcb, methodName);
				}
				return new AMethod(processName, methodName);
		}
		public Object getActor(CodeBundle cb) {
				String methodName = "";
				String processName = "";
				if ( cb.getProcessDeclaration() != null )
						processName = cb.getProcessDeclaration().getName() + "." ;

				if ( cb.getMethodDeclaration() != null ) {
						methodName = cb.getMethodDeclaration().getName();
				}
				else if ( cb.getEventDeclaration() != null ) {
						// If this is a response to an event keep class 
						// receiving this event anonymous   
						// 01.20.05 no don't do this need a different 
						// way to do name matching instead
						methodName = cb.getEventDeclaration().getName();
				}

				return new AMethod(processName, methodName);
		}
		
		private void formatTable() {
				JPanel labelPanel = new JPanel(new BorderLayout());
				labelPanel.setBackground(OncBrowserConstants.modelBuildingColor);
				labelPanel.setForeground(Color.WHITE);
				title = " Model Dependencies for: " + mc.toString();
				JLabel titleLabel = new JLabel(title);
				titleLabel.setBackground(OncBrowserConstants.modelBuildingColor);
				titleLabel.setForeground(Color.WHITE);
				titleLabel.setFont(new Font("TimesNewRoman", Font.BOLD, 14));
				labelPanel.add(titleLabel, BorderLayout.CENTER);
				JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
				buttonPanel.add(new StarterProcessButton());
				//buttonPanel.add(new JButton("Track Flow"));
				buttonPanel.add(new ShowVariablesButton());
				buttonPanel.add(new UpButton());
				buttonPanel.add(new DownButton());
				buttonPanel.add(new ClearPathsButton());
				labelPanel.add(buttonPanel, BorderLayout.SOUTH);
				add(labelPanel, BorderLayout.NORTH); 
		}

		private void preprocessCodeBundle(GenericTreeNode cbTreeNode) {
						preprocessedCodeBundles.addElement(new PreprocessedCodeBundle(cbTreeNode, this));
		}

		private DefaultTreeModel gatherStatementBundles(ModelController mc) {
				// Using the list of statementBundles provided 
				// construct a tree model all the way down to the CB level
				// Each node should be a DependencyTreeNode which points to a SB, ST a
				// Code bundle and some code which is usually extracted 
				// from an action in a code bundle
				OncFilter filter = new OncFilter(false);	
				OncTreeNode rootNode = filter.getRootNode();
				OncTreeNode andNode = new OncTreeNode(TcapLogicalOperator.AND);
			 	OncTreeNode mcNode = new OncTreeNode(mc);
 				andNode.add(mcNode);
 				rootNode.add(andNode);
				Vector endClasses = new Vector();
				endClasses.addElement(OntologyMap.STATEMENT_TEMPLATE);
				endClasses.addElement(OntologyMap.STATEMENT_BUNDLE);
				endClasses.addElement(OntologyMap.CODE_BUNDLE);
				Hashtable sbs = oncotcap.Oncotcap.getDataSource().getInstanceTree
						(OntologyMap.MODEL_CONTROLLER,
						 endClasses,
						 filter,
						 TreeDisplayModePanel.ROOT);
				GenericTree tree = new GenericTree(sbs, true);
				return (DefaultTreeModel)tree.getModel();
		}

		private void removeDuplicateSBs(DefaultTreeModel treeModel) {
				Vector topLevelSBs = new Vector();
				Vector statementBundleNodes = 
						getNodesOfType(treeModel, StatementBundle.class);
				
				Iterator i = statementBundleNodes.iterator();
				while ( i.hasNext() ) {
						GenericTreeNode treeNode = (GenericTreeNode)i.next();
						// If this is a top level SB ( Parent is a CB) 
						// put in a list
						if ( treeNode.getParent() != null 
								 && ((DefaultMutableTreeNode)treeNode.getParent()).getUserObject().getClass
								 ().equals(Encoding.class) ) {
								if ( topLevelSBs.contains(treeNode.getUserObject()) ) {
										// This top level SB is already included
										// prune this tree path from the tree model
										treeNode.removeFromParent();
								}
								else {
										// add this Top level SB to list
										topLevelSBs.addElement(treeNode.getUserObject());
								}
						}
				}
		}

		// Get a list of tree nodes that have 'CLass' as their user object type
		static public Vector getNodesOfType(DefaultTreeModel treeModel,
																 Class cls) {
				Vector nodes = new Vector();
				for (Enumeration e=
								 ((DefaultMutableTreeNode)treeModel.getRoot()).depthFirstEnumeration(); 
						 e.hasMoreElements(); ) {
						GenericTreeNode n = (GenericTreeNode)e.nextElement();		
						if ( cls.isInstance(n.getUserObject()) ) {
								nodes.addElement(n);
						}
				}	
				return nodes;
		}

		private DependencyUserObject createDependency(StatementBundle sb,
																									StatementTemplate st,
																									CodeBundle cb,
																									OncAction action,
																									String ifClause,
																									Object influence,
																									Object resolvedActor,
																									Object recipient) {
			// 	System.out.println("resolvedActor " +
// 													 resolvedActor 
// 													 + " recipient " + recipient);
				DependencyUserObject dependency = new DependencyUserObject();
				dependency.setStatementBundle(sb);
				dependency.setStatementTemplate(st);
				dependency.setCodeBundle(cb);
				dependency.setAction(action);
				if ( ifClause != null ) {
						dependency.setIfStatement(ifClause);
				}
				if ( influence != null )
						dependency.setInfluence(influence);
				else {
						if ( action.getType().equals(ActionType.INSTANTIATE) )
								dependency.setInfluence("Instantiate");
						else {
								dependency.setInfluence(action.getType().toString());
			
						}
						
				}
				dependency.setActor(resolvedActor);
				// Get a resolved version of the variable/event/schedule that
				// this action is operating on
				dependency.setRecipient(recipient);
				dependencies.add(dependencyToVector(dependency));
				putVariableDependency(varDependencies, recipient, resolvedActor);
				return dependency;
		}

		public void putVariableDependency(Hashtable hTable, Object dependentVariable,
														Object variable) {
				// Only collect variable dependencies here
				if ( !(variable instanceof Variable) ||
						 !(dependentVariable instanceof Variable) ) 
						return;
				// Don't collect dependencies on ones self
				if ( dependentVariable == variable ) 
						return;
				
				Vector currentValues = (Vector)hTable.get(variable);
				if ( currentValues == null ) {
						currentValues = new Vector();
				}
				if (!currentValues.contains(dependentVariable)) {
						currentValues.addElement(dependentVariable);
						hTable.put(variable, currentValues);
				}
		}

		private Vector dependencyToVector(DependencyUserObject d){
				Vector vec = new Vector();
				vec.addElement("");
				vec.addElement(d.getActor());
				vec.addElement(d.getInfluence());
				vec.addElement(d.getRecipient());
				vec.addElement(d.getIfStatement());
				vec.addElement(d.getAction());
				vec.addElement(d.getCodeBundle());
				vec.addElement(d.getStatementTemplate());
				vec.addElement(d.getStatementBundle());
				vec.addElement(nextButton);
				return vec;
		}


		private Object getObjectOfThisAction(OncAction action, PreprocessedCodeBundle ppcb) {	
				// Determine what type action this is 
				// Determine if it is a variable / schedule / or event that is
				// affected by this action
				// Resolve this action  ( substitute any backquotes ) 
				//System.out.println("ACTION " + action.getClass());
				String objectOfAction = action.getName();
				AClass aCls = null;
				// If this is a variable then add the process name to the 
				// front of the variable
				if ( action instanceof AddVariableAction 
						 || action instanceof ModifyVariableAction 
						 || action instanceof InitVariableAction) {
						return new Variable(ppcb, objectOfAction);
				}
				else if ( action instanceof TriggerEventAction ) {
						TriggerEventAction trigger = (TriggerEventAction)action;
						if ( trigger.getMethod() != null ) {
								String processName = "UndefinedProcess";
								if ( trigger.getTriggeredProcessDeclaration() != null ) {
										processName = trigger.getTriggeredProcessDeclaration().getName();
								}
								return new AMethod(processName, trigger.getMethod().getName());
						}
						else if ( trigger.getEvent() != null ) 
								return new AnEvent(ppcb, trigger.getEvent().getName(), true);
						else
								return new DependencyNoun(ppcb, "ERROR");
				}
				else if ( action instanceof InstantiateAction ) {
						aCls = new AClass(action.getName());
						allClasses.add(aCls);
						return aCls;
				}
				else if ( action instanceof AddGenericCode ) 
						return "";
				aCls = new AClass(ppcb.getProcessName());
				allClasses.add(aCls);
				return aCls;
		}
		

		static public String fixGetContainerInstance(String unresolvedString) {
						StringBuffer resolvedString = new StringBuffer();
						int idx = 0;
						Pattern getContainerInstance = 
								Pattern.compile("\\(\\(.*?\\)\\s*?getContainerInstance\\(.*?\\.class\\)\\)", Pattern.DOTALL);
						Matcher match = getContainerInstance.matcher(unresolvedString);
						// If there are no back quotes pass the string back as is
						String value = null;
						if ( !match.find() ) {
								//System.out.println("Do I ever come here" + unresolvedString);
								return unresolvedString;
						}
						else {
								match.reset();
								// Patch the string back together fully
								// substituted 
								while(match.find()) {
									// 	System.out.println("HEY A MATCH " +  unresolvedString
// 																	 + " AT " + match.start());
										// Find the position of the first close paren
										String matchString = 
												unresolvedString.substring(match.start(), match.end());
										int closeParenIdx = matchString.indexOf(")");
									
										String className = matchString.substring(2,
																														 closeParenIdx);
										resolvedString.append(unresolvedString.substring(idx, 
																																		 match.start()));
										resolvedString.append(className);
										idx = match.end();
								}
						}
						// TO DO: remove getContainerInstance and extras here - make
						// it readable
						resolvedString.append(unresolvedString.substring(idx, 
																														 unresolvedString.length()));
						// System.out.println("UNRESOLVED STRING " + unresolvedString
//  															 + "RESOLVED STRING " + resolvedString);
						return resolvedString.toString();
				}	

		// OncFrameable
		public String getTitle(){
				return title;
		}
		public ImageIcon getImageIcon(){
				return null;
		}

		public int moveRow(int matchedRow, int selectedRow) {
				int maxRow = oncModelTable.getRowCount();

				// If the selected row is the last row
				// move the matched row right above it and then move the 
				// selected row above the match row
				if ( selectedRow+1 > maxRow ) {
						// At the bottom of the table
						int r = selectedRow-1;
						
						oncModelTableModel.moveRow(matchedRow, matchedRow, 
																			 selectedRow-1);
						
						oncModelTableModel.moveRow(selectedRow, selectedRow,
																			 selectedRow-1);
						return selectedRow - 1;
				}
				else {
						oncModelTableModel.moveRow(matchedRow, matchedRow, 
																			 selectedRow+1);
						return selectedRow;
				}
		}
		
		public void writeDotFile() {
				Hashtable clusters = new Hashtable(); // key=process 
				                                      // value=assembledProcess
				Object thisObj = null;
				Object thatObj = null;
				Object relationshipObj = null;
				Hashtable inTree = new Hashtable(); 
				// Write header 
				dotFileRep.append("digraph DemoStandardPhaseIDesign {\n"
													+"graph [\n"
													+"rankdir = \"LR\"\n"
													+"];\n"
													+"node [\n"
													+"fontsize = \"12\"\n"
													+"shape = \"ellipse\"\n"
													+"];\n"
													+"edge [\n"
													+"];\n");

				
				// Loop through all the thiss and all the thats
				DefaultMutableTreeNode parentTreeRootNode = 
						new DefaultMutableTreeNode(mc.toString());
				for ( int row = 0; row < oncModelTableModel.getRowCount(); 
							row++) {
						thisObj = oncModelTableModel.getValueAt(row, THIS);
						thatObj = oncModelTableModel.getValueAt(row, THAT);
						relationshipObj = oncModelTableModel.getValueAt(row, RELATIONSHIP);
						// If the relationship is Instantiate put the this and that 
						// in a tree setParent(node)

						DefaultMutableTreeNode parentProcess = null;
						DefaultMutableTreeNode newProcess = null;
						// Keep a vector of all the processes in the tree already
						// cheaper than cycling tree getting user object etc
						if ( relationshipObj.equals("Instantiates") ) {
								if ( !inTree.containsKey(((DependencyNoun)thisObj).getClassName().toString()) ) {
										parentProcess = 
												new DefaultMutableTreeNode(((DependencyNoun)thisObj).getClassName());
										System.out.println("INSTANTIATES  Process 1 " + 
																			 parentProcess  + " to " 
																			 + parentTreeRootNode);
										parentTreeRootNode.add(parentProcess);
										inTree.put(((DependencyNoun)thisObj).getClassName().toString(), parentProcess);
								}
								else {
										// find the existing node in the tree
										parentProcess = 
												(DefaultMutableTreeNode)inTree.get(((DependencyNoun)thisObj).getClassName().toString());								
										//parentTreeRootNode.add(parentProcess);
										// System.out.println("INSTANTIATES  Process 2" + 
// 																			 newProcess  + " to " + parentProcess);


								}
								newProcess = 
										(DefaultMutableTreeNode)inTree.get(((DependencyNoun)thatObj).getClassName().toString());
								if ( newProcess == null ){
										System.out.println("new new process " + inTree 
																			 + " : " + thatObj);
										newProcess = 
												new DefaultMutableTreeNode(((DependencyNoun)thatObj).getClassName());
								}
								System.out.println("INSTANTIATES  Process 3" + 
																	newProcess  + " to " + parentProcess);
								parentProcess.add(newProcess);
						}
						else {
								// Add graphically interesting elements to Vector 
								// that will later be written to dot file
								DotFileRelationship relationship = 
										new DotFileRelationship(thisObj,
																						relationshipObj,
																						thatObj);
								// Make sure this and/or that are in the 
								// list of nodes
								if (  !relationshipObj.equals("Instantiates") 
											&& !relationshipObj.equals("Schedules")
											&& !relationshipObj.equals("Triggers")
											&& !relationshipObj.equals("Conditionally Modifies")) {
										dotFileRelationships.add(relationship);
								}
								
								if ( nodeVector.add(thisObj) 
										 && thisObj instanceof DependencyNoun
										 && ((DependencyNoun)thisObj).getClassName() != null ) {
										HashtableHelper.addVectorValue(processNodeMap,
																									 ((DependencyNoun)thisObj).getClassName().toString(), 
																									 thisObj);
								}
								if ( nodeVector.add(thatObj) 
										 && thatObj instanceof DependencyNoun
										 && ((DependencyNoun)thatObj).getClassName() != null ) {
										HashtableHelper.addVectorValue(processNodeMap,
																									 ((DependencyNoun)thatObj).getClassName().toString(), 
																									 thatObj);
								}
						}

						// Get all the classes/ oncprocesses
						// Determine the order tat the classes will be processed
						// Now write all the linkage info to a unique vector so a statement
						// is written more than once
						// for (Enumeration e = parentTreeRootNode.depthFirstEnumeration() ; 
						// 								 e.hasMoreElements() ;) {
						// 								Object key = e.nextElement();
						// 								System.out.println(" path " + key);
						// 						}
						//System.out.println("dot file vector " + 	dotFileRelationships);
						//System.out.println("node vector " + nodeVector);
				}
				System.out.println(" processNodeMap " + processNodeMap);
				buildCluster(parentTreeRootNode, 0);
				writeRelationships(dotFileRelationships);
				dotFileRep.append("}\n");
		}
		
		public void writeRelationships(Vector dotFileRelationships) {
				int thisID = 0;
				int thatID = 0;
				for ( int j = 0; j < dotFileRelationships.size(); j++ ) {
						DotFileRelationship relationship = 
								(DotFileRelationship)dotFileRelationships.elementAt(j);
						if ( relationship.thisObj instanceof DependencyNoun
								 && relationship.thatObj instanceof DependencyNoun) {
										DependencyNoun thisObj = 
												(DependencyNoun)relationship.thisObj;
										DependencyNoun thatObj = 
												(DependencyNoun)relationship.thatObj;
										if ( !relationship.relationshipObj.equals("Initializes") ) {
												dotFileRep.append(	"\"" 
																						+ thisObj
																						//+ getObjectID(thisObj)
																						+ "\""
																						+ "->"
																						+ "\"" 
																						+ thatObj
																						//+ getObjectID(thatObj)
																						+ "\"\n" );
										}
						}
				}
		}

		private int getObjectID(DependencyNoun noun) {
				int id = -1;
				PreprocessedCodeBundle ppcb = 
						noun.getPreprocessedCodeBundle();
				StatementBundle sb = null;
				
				if (  ppcb != null && ppcb.getTreeNode() != null ){
						sb = (StatementBundle)GenericTreeNode.getClosest
								(StatementBundle.class, ppcb.getTreeNode());
				}
				if ( sb != null ) 
						id = sb.hashCode();
				return id;
		}
		public int  buildCluster(DefaultMutableTreeNode node, int counter) {
				
				if ( counter >= diagramLightColors.length) {
						if ( colorCounter < diagramLightColors.length-1 ) 
								colorCounter++;
						else 
								colorCounter = 0;
				}
				else 
						colorCounter = counter;
				System.out.println("color Counter " + colorCounter);
				dotFileRep.append("subgraph cluster_" + String.valueOf(counter) +
													 "{\n"
													 + "style=filled;\n"
													 + "fillcolor=\"#" 
													 + Integer.toHexString(diagramLightColors[colorCounter].getRed()) 
													 + Integer.toHexString(diagramLightColors[colorCounter].getGreen()) 
													 + Integer.toHexString(diagramLightColors[colorCounter].getBlue()) 
													 + "\"\n"
													 + "shape=doublecircle\n"
													 + "color=black\n"
													 + "label = \"" + node.toString() + "\"");
				//get any nodes associated with this cluster
				Vector vec = 
						(Vector)processNodeMap.get(node.getUserObject().toString());
				if ( vec != null ){
						for ( int j = 0; j < vec.size(); j++ ) {
								if ( vec.elementAt(j) instanceof DependencyNoun) {
										DependencyNoun noun = (DependencyNoun)vec.elementAt(j);
										if ( noun.getObjectName() != null ) 
												dotFileRep.append(	"\"" 
																						+ noun
																						//+ getObjectID(noun)
																						+ "\""
																						+ "[\n"
																						+ "label="
																						+ "\"" 
																						+ noun.getObjectName()
																						+ "\"" 
																						+ getObjectDescription(noun)
																						+ "];\n");
								}
						}
				}
				
				//System.out.println("vec " + vec);
				if ( node.getChildCount() > 0 ) {
						for ( int i = 0; i < node.getChildCount(); i++) {
								counter++;
								counter = buildCluster((DefaultMutableTreeNode)node.getChildAt(i), counter);
						}
				}
				dotFileRep.append("}");
				return counter;
		}

		private String getObjectDescription(DependencyNoun noun) {
				if ( noun instanceof AMethod ) {
						return "\nshape=\"diamond\"\ncolor=\"#815454\"\nstyle=filled";
				}
				if ( noun instanceof AnEvent ) {
						return "\nshape=\"parallelogram\"\ncolor=\"#815454\"\nstyle=filled";
				}
				System.out.println("noun flavor " + noun.getClass());

				return "";
						
		}
		
// 		public Vector getMatchingRows(Object searchObj, int column) {
// 				// Find all rows that match the given object in the the specified 
// 				// column
// 				Vector matchingRows = new Vector();
// 				Object obj = oncModelTableModel.getValueAt(i, column);
// 				Vector matchingRow = new Vector();
// 				if ( obj != null && obj.equals(searchObj) ) {
// 						// get all columns if the value matches
						
// 						matchingRows.add(matchingRow);
// 				}
// 				return null;
// 		}

		// BUTTONS
		class NextButton extends JButton implements ActionListener {
				int maxRow = 0;
				public NextButton(String name) {
						super(name);
						addActionListener(this);
				}																						 
				public void actionPerformed(ActionEvent ae) {
						// Determine where the button is
						int r = oncModelTable.getSelectedRow();
						int c = oncModelTable.getSelectedColumn();

						// Now go through all the rows "This" column and find
						// cells that match the "That" column in this buttons row
						DependencyNoun thatObject = (DependencyNoun)oncModelTable.getValueAt(r, THAT);
						Object relationship = oncModelTable.getValueAt(r, RELATIONSHIP);
						System.out.println("Selected That object " + thatObject
															 + " " + thatObject.getClassName());
						System.out.println("relationship " + relationship );
						maxRow = oncModelTable.getRowCount();
						// Now move all matched rows immediately beneath this 
						// buttons row. 
						int i = r+1; //works
						Vector moveRow = new Vector();
						DependencyNoun thisObject = null;
						boolean matches = false;
						while ( i < maxRow ) {
								thisObject = (DependencyNoun)oncModelTable.getValueAt(i, THIS);
								System.out.println("This Value " + thisObject.getClassName());
								if ( thatObject != null &&
										 thisObject != null ){
										if ( thisObject.equals(thatObject) )
												matches = true;
										if ( "Instantiates".equals(relationship)
												 &&
												 thisObject.getClassName().equals(thatObject.getClassName())) {
												// Bring all methods of that class to top
												matches = true;
										}

										if ( matches ) {
														moveRow.addElement(new Integer(i));
														System.out.println("This Value matches " + thisObject);
										}
								}
								matches = false;
								i++;
						}
						//System.out.println("Move rows " + moveRow);
						i = 0;
						while (  i < moveRow.size() && moveRow.elementAt(i) != null ) {
								
								int selectedRow = 
										moveRow(((Integer)moveRow.elementAt(i)).intValue(), r);
								//System.out.println("Selected row new position " 
								// + selectedRow);
								i++;
						}
						if ( oncModelTable.getValueAt(r, SELECTOR) == null 
								 || oncModelTable.getValueAt(r, SELECTOR).equals("") ) {
								nextCounter++;
								oncModelTable.setValueAt(nextCounter, r, SELECTOR);
						}
				}

		}

		class StarterProcessButton extends JButton implements ActionListener {
				public StarterProcessButton() {
						super("Starter Process");
						addActionListener(this);
				}
				public void actionPerformed(ActionEvent ae) {
						
						//Get the started process and bring all init "This" columns for 
						// the starter process to the top
						ProcessDeclaration starterProcess = mc.getStarterProcess();
						String starterPt = starterProcess.getName() + ".init";
						int maxRow = oncModelTable.getRowCount();
						int i = 0;
						Object thisObject = null;
						while ( i < maxRow ) {
								thisObject = oncModelTable.getValueAt(i, THIS);
								// move to the to
								System.out.println("find starter moving " + i 
																	 + " to 0 where i is "
																	 + thisObject );
								if ( thisObject instanceof DependencyNoun
										&& starterPt.equals(((DependencyNoun)thisObject).getFullName()) ) {
										System.out.println("find starter moving " + i 
																		+ " to 0 where i is "
																		+ thisObject );
										
										oncModelTableModel.moveRow(i, i, 0);
								} 
								i++;
						}
				}
		} // starter process

		class ShowVariablesButton extends JButton implements ActionListener {
				public ShowVariablesButton() {
						super("Show Variables");
						addActionListener(this);
				}
				public void actionPerformed(ActionEvent ae) {
						// Display all the variables in a list
						showVariableDependencyTable();
				}
		} // show variables

		class UpButton extends JButton implements ActionListener {
				public UpButton() {
						super("Up");
						addActionListener(this);
				}
				public void actionPerformed(ActionEvent ae) {
						// Determine where the button is
						int r = oncModelTable.getSelectedRow();
						// Move row up 1
						if ( r > 0 ) {
								oncModelTableModel.moveRow(r,r, r-1);
								oncModelTable.setRowSelectionInterval(r-1, r-1);

	// 							oncModelTable.changeSelection(r-1,
// 																SELECTOR,
// 																false,
// 																false);
						}
				}
		} // up

		class DownButton extends JButton implements ActionListener {
				public DownButton() {
						super("Down");
						addActionListener(this);
				}
				public void actionPerformed(ActionEvent ae) {
						// Move row down 1
						int r = oncModelTable.getSelectedRow();
						if ( r < oncModelTable.getRowCount() ) {
								oncModelTableModel.moveRow(r, r, r+1);
								oncModelTable.setRowSelectionInterval(r+1, r+1);
						}
				}
		} // down 

		class ClearPathsButton extends JButton implements ActionListener {
				public ClearPathsButton() {
						super("Clear Paths");
						addActionListener(this);
				}
				public void actionPerformed(ActionEvent ae) {
						// Erase all the numbers in the 1st column and reset counter
						nextCounter = 0;
						for ( int r = 0; r < oncModelTable.getRowCount(); r++) {
								oncModelTable.setValueAt("", r, SELECTOR);
						}
				}
		} // clear paths

// 		class ClearPathsButton extends JButton implements ActionListener {
// 				public ClearPathsButton() {
// 						super("Clear Paths");
// 						addActionListener(this);
// 				}
// 				public void actionPerformed(ActionEvent ae) {
// 						// Get the path number for the currently selected row
// 						int selectedRow = oncModelTable.getSelectedRow();
// 						int selectedPathNumber = -1;
// 						Object obj =
// 								oncModelTable.getValueAt(\selected, SELECTOR);
// 						if ( obj == null || obj.equals("") ) 
// 								return;
// 						if ( obj instanceof Integer ) {
// 								selectedPathNumber = 
// 										((Integer)selectedPathNumber).toInt();
// 						}
// 						if ( selectedPathNumber < 0)
// 								return;

// 						for ( int r = 0; r < oncModelTable.getRowCount(); r++) {
// 								oncModelTable.getValueAt("", r, SELECTOR);
// 						}
// 				}
// 		} // clear paths



    public class ColumnListener extends MouseAdapter {
        public void mouseClicked(MouseEvent evt) {
						//System.out.println("Mouse clicked on " + evt.getSource());
						if ( evt.getSource() instanceof JTable ) {
								TableColumnModel colModel = 
										((JTable)evt.getSource()).getColumnModel();
								// what column was clicked
								int col = colModel.getColumnIndexAtX(evt.getX());
								int row = ((JTable)evt.getSource()).rowAtPoint(new Point(evt.getX(),
																															 evt.getY()));
								
                // Return if not clicked on any column header
								if (col != 0 || row < 0) {
										return;
								}
								if ( evt.isPopupTrigger() 
										 || evt.getButton() == MouseEvent.BUTTON3) {
										pathPopupMenu.show(	((JTable)evt.getSource()), 
																				evt.getX(), evt.getY());
								}	
						}
        }
    }



    
    public class SelectionListener implements ListSelectionListener {
        JTable table;
    
        // It is necessary to keep the table since it is not possible
        // to determine the table from the event's source
        SelectionListener(JTable table) {
            this.table = table;
        }
        public void valueChanged(ListSelectionEvent e) {
            // If cell selection is enabled, both row and 
						// column change events are fired
            if (e.getSource() == table.getSelectionModel()
								&& table.getRowSelectionAllowed()) {
                // Column selection changed
                int first = e.getFirstIndex();
                int last = e.getLastIndex();
            } else if (e.getSource() == table.getColumnModel().getSelectionModel()
											 && table.getColumnSelectionAllowed() ){
                // Row selection changed
                int first = e.getFirstIndex();
                int last = e.getLastIndex();
            }
						
            if (e.getValueIsAdjusting()) {
                // The mouse button has not yet been released
            }
        }
    }

		public class DotFileRelationship {
				public Object thisObj = null;
				public Object thatObj = null;
				public Object relationshipObj = null;
				public DotFileRelationship(Object obj1, Object obj2, Object obj3) {
						thisObj = obj1;
						thatObj = obj3;
						relationshipObj = obj2;
				}
				public String toString() {
						return thisObj.toString() + ", " + relationshipObj.toString() + ", " + thatObj.toString();
				}
		}

}


// table.changeSelection( row, column, false, false );


// To make the row visible, you can use

// table.scrollRectToVisible( table.getCellRect( row, column, true ) );

