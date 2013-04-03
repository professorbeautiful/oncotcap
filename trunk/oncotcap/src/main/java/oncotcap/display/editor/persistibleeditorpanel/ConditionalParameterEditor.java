package oncotcap.display.editor.persistibleeditorpanel;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.dnd.*;
import java.util.*;
import java.lang.reflect.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.tree.*;
import javax.swing.border.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import java.awt.datatransfer.*;
import java.beans.PropertyVetoException;

import oncotcap.datalayer.*;
import oncotcap.util.*;
import oncotcap.display.common.*;
import oncotcap.display.browser.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.datalayer.autogenpersistible.*;


public class ConditionalParameterEditor extends EditorPanel
		implements ActionListener, 
				CanvasEditorChangeListener,
				TableModelListener {
		private final static String WINDOWS =
				"com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		// Declare static keyword tree models that are used in all cases where 
		// there are enum/category/keyword trees this allows keyword tree to 
		// be centrally maintained and cut down on duplication 
		// where the keyword tree has no leaves or has keywords only as leaves
		public static DefaultTreeModel keywordNestedTreeModel = null;
		static OncoTCapDataSource dataSource = null;
		ProbabilityTable probTable = null;
		private static final LineBorder lineBorder = 
				(LineBorder)BorderFactory.createLineBorder
				(OncBrowserConstants.MBColorDark);
		private static final LineBorder lightLineBorder = 
				(LineBorder)BorderFactory.createLineBorder
				(OncBrowserConstants.MBColorPale, 3);
		DroppableCanvas canvas = new DroppableCanvas();
		JTextField tableShorthandField = null;
		JTextField notationTextField = new JTextField(50);
		int numCombinations = 0;
		private String heading;
		Vector dependentElementTypes = new Vector();
		Vector levelLists = new Vector();
		EnumLevel[][] jointEvents = null;
		//EnumDefinition conditionalValueType = null;
		Persistible setType = null;
		ConditionalTableParameter condParameter = null;
		Vector probabilityTablePanels = new Vector();
		Hashtable probTableFrames = new Hashtable();
		OntologyTree ot = null;

		
		public  ConditionalParameterEditor() {
				initKeywordTree();
				initUI();
		}
		
		public void initUI(Object editObj) {
				//System.out.println("initui");
				initKeywordTree();

				condParameter = (ConditionalTableParameter)editObj;
				initUI();
		}

		private void initKeywordTree() {
				Vector endClasses = new Vector();
				endClasses.addElement(OntologyMap.KEYWORD);
				Hashtable keywords = 
						oncotcap.Oncotcap.getDataSource().getInstanceTree
						(OntologyMap.KEYWORD,
						 endClasses,
						 null,
						 TreeDisplayModePanel.NONE);
				GenericTree tree = new GenericTree(keywords, true);					
				keywordNestedTreeModel = (DefaultTreeModel)tree.getModel();		
		}

		private void initUI() {
//				getInputMap().setParent(OncBrowser.getDefaultInputMap());
//				getActionMap().setParent(OncBrowser.getDefaultActionMap());
				setLayout(new BorderLayout());
				setPreferredSize( new Dimension(850,750));
				
				// Panel is layed out as follows 
				// |     
				// |   canvas    | enum tree
				// |   
				// | 
				// |   table canvas where one or more tables reside
				// |
				JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
				JPanel enumPanel = new JPanel(new BorderLayout());
				OntologyTree ot = getKeywordTree();
				enumPanel.add(ot, BorderLayout.CENTER);
				enumPanel.setPreferredSize(new Dimension(350, 500));
				JScrollPane enumScrollPane = new JScrollPane();
				enumScrollPane.setViewportView(enumPanel);

				JPanel droppablePanel = new JPanel(new BorderLayout());
				droppablePanel.setPreferredSize(new Dimension(500, 500));
				// have to use two labels for come reason
				// putting <> in the html causes it not to show
				JLabel directions1 = 
						new JLabel("<Select a Keyword and Press F3 to create or add characteristics");
				JLabel directions1a = 
					new JLabel("to probability table.>");
				JLabel directions2 = 
						new JLabel("<Select a Keyword and Press F4 to add a condition");
				JLabel directions2a = new JLabel("to probability table(s)>");
				directions1.setFont(OncBrowserConstants.DIRECTIONS_FONT_SMALL);
				directions1.setForeground(Color.BLACK);
				directions2.setFont(OncBrowserConstants.DIRECTIONS_FONT_SMALL);
				directions2.setForeground(Color.BLACK);
				directions1a.setFont(OncBrowserConstants.DIRECTIONS_FONT_SMALL);
				directions1a.setForeground(Color.BLACK);
				directions2a.setFont(OncBrowserConstants.DIRECTIONS_FONT_SMALL);
				directions2a.setForeground(Color.BLACK);
				JPanel directions = new JPanel();
				directions.setLayout(new BoxLayout(directions, BoxLayout.Y_AXIS));
				directions1.setAlignmentX(Component.LEFT_ALIGNMENT);
				directions2.setAlignmentX(Component.LEFT_ALIGNMENT);
				directions1a.setAlignmentX(Component.LEFT_ALIGNMENT);
				directions2a.setAlignmentX(Component.LEFT_ALIGNMENT);
				directions.add(directions1);
				directions.add(directions1a);
				directions.add(directions2);
				directions.add(directions2a);

				droppablePanel.add(directions, BorderLayout.NORTH);
				canvas.setBorder(lightLineBorder);
				droppablePanel.add(canvas, BorderLayout.CENTER);
				JScrollPane scrollPane = new JScrollPane();
				scrollPane.setViewportView(droppablePanel);
				JPanel notationPanel = new JPanel();
				JLabel notationLabel = new JLabel("Set Notation");
				
				notationPanel.add(notationLabel);
				notationPanel.add(notationTextField);
				add(notationPanel, BorderLayout.NORTH);

				splitPane.setLeftComponent(scrollPane);
				splitPane.setRightComponent(enumScrollPane);
				splitPane.setDividerLocation(390);

				add(splitPane, BorderLayout.CENTER);

				// USe the parameter to fill in the UI
				fillInUI();
				revalidate();
				
		}

		private void fillInUI() {
				// If there isn't a parameter display empty display
				//System.out.println("fillInUI condParameter " + condParameter);
				if ( condParameter == null) 
						return;
				ConditionalDiscreteStateFunction cdsf = null;
				// Update the notation
				refreshNotation(condParameter);

				// Get the state matrix 
				//System.out.println("fillInUI stateMatrix " + stateMatrix);
				// Get the function
				if ( condParameter.getFunction() != null && 
						 condParameter.getFunction().size() > 0 ){
						Iterator fnIterator =  condParameter.getFunction().iterator();
						while ( fnIterator.hasNext() ) {
								//System.out.println("fnIterator " + cdsf);

								cdsf = (ConditionalDiscreteStateFunction)fnIterator.next();
								renderFunction(condParameter, cdsf);
								//addTable(fn);
						}
				}
				else {
						// cdsf = new ConditionalDiscreteStateFunction();
// 						condParameter.setFunction(cdsf);
						return;
				}
				revalidate();
				
		}

		public DroppableCanvas getCanvas() {
				return canvas;
		}
		private void renderFunction(ConditionalTableParameter condParameter,
																ConditionalDiscreteStateFunction cdsf) {
				StateMatrix stateMatrix = condParameter.getStateMatrix();
// 				if ( condParameter.getFunction()!= null && 
// 						 condParameter.getFunction().size() > 0 ) 
					// 	System.out.println("condParameter " +
// 															 condParameter.getGUID()
// 															 + "cdsf " + cdsf.getGUID()
// 															 + " condParameter.cdsf " + ((Persistible)condParameter.getFunction().firstElement()).getGUID());
				// If the joint events matrix has not been initialized  do it
				
				// For each possible outcome in the function create 
				// a table and place on the canvas
				Vector possibleOutcomes = cdsf.getOutcomeValues();
				Persistible condition = cdsf.getConditionValue();
				// Need a copy of the state matrix data do
				Vector sets  = stateMatrix.getDataAsVectorOfVectors();
				//System.out.println("sets " + sets);
				addOutcomes(sets, possibleOutcomes);
				//sets = addProbTableDefaultColumns(sets);
				Vector headings = stateMatrix.getMatrixHeading();
				// if ( !headings.contains("Default") )
// 						headings.add(0,"Default");
// 				if ( !headings.contains("Proportion") )
// 						headings.add("Proportion");
				BooleanExpression condPanelBoolean = null;
				if ( condition != null ) { 
						condPanelBoolean = new BooleanExpression(false);
						condPanelBoolean.setLeftHandSide(condParameter.getConditionValue());
						condPanelBoolean.setRightHandSide(condition);
				}
				//System.out.println("render headings " + headings +
				//									 " sets " + sets);
				ProbabilityTablePanel ptp = createProbabilityTable(cdsf,
																													 condPanelBoolean);
						//sets, 05.09
						//																			headings,
						//																			condPanelBoolean);
				ptp.getConditionalPanel().addCanvasEditorChangeListener(canvas);
				JInternalFrame jif = addTable(ptp);
			// 	System.out.println("createCDSF " + cdsf.getStateMatrix()
// 													 + " condition " + cdsf.getConditionValue()
// 													 + " prob table " + ptp.getTableModel());		
				probTableFrames.put(cdsf, jif);
				//System.out.println("probTableFrames " + probTableFrames.keys());
		}
		private void addOutcomes(Vector vectorOfVectors, Vector outcomes) {
				if ( vectorOfVectors == null || outcomes == null )
						return;
				
				for ( int i = 0; i < vectorOfVectors.size(); i++) {
						Vector vec = (Vector)vectorOfVectors.elementAt(i);
						if ( outcomes.size() > i) 
								vec.add(outcomes.elementAt(i));
				}
		}
// 		private ProbabilityTablePanel createProbabilityTable
// 				(StateMatrix stateMatrix,
// 				 Persistible condition) {
// 				// Get headings
// 				Vector headings = stateMatrix.getMatrixHeading();
// 				// convert the table data into a vector of vectors
// 				Vector data = stateMatrix.getDataAsVectorOfVectors();
// 				ProbabilityTablePanel pt = 
// 						createProbabilityTable(data, headings, condition);
// 				pt.addCanvasEditorChangeListener(canvas);
// 				ConditionalPanel condPanel = pt.getConditionalPanel();
// 				condPanel.addCanvasEditorChangeListener(canvas);
// 				return pt;
// 		}
// 		private ProbabilityTablePanel createProbabilityTable
// 				(Vector vectorOfVectorData,
// 				 Vector headings,
// 				 Persistible condition) {
// 				System.out.println("createProbabilityTable " + headings +
// 													 " condition " + condition
// 													 + condParameter.getStateMatrix());
				
// 				Vector sets = addProbTableDefaultColumns(vectorOfVectorData);
// 			// 	if ( condParameter instanceof ConditionalTableEventParameter )  
// // 						sets = initializeOutcomes(sets, EventDefinition.class);
// 				// System.out.println("createProbabilityTableaft add default "
// // 													 + condParameter.getStateMatrix());
// 				Vector headingData = new Vector(headings);
// 				if ( !headingData.contains("Default") )
// 						headingData.add(0,"Default");
// 				if ( !headingData.contains("Proportion") )
// 						headingData.add("Proportion");
// 				// 
// 			// 	System.out.println("createProbabilityTable aftr add default headings"
// // 													 + condParameter.getStateMatrix());
// 		// 		OncWritableTableModel tableModel = new OncWritableTableModel();
// // 				OncWritableTable table = new OncWritableTable(tableModel);
// // 				tableModel.setDataVector(sets,
// // 																 headingData);
// 				ProbabilityTablePanel tablePanel = 
// 						new ProbabilityTablePanel(tableModel);
// 				if ( condParameter != null ) 
// 						tablePanel.setTableType(condParameter.getClass());
// 				ConditionalPanel condPanel = tablePanel.getConditionalPanel();
// 				condPanel.setValue(condition);
// 				tablePanel.addCanvasEditorChangeListener(canvas);
// 				return tablePanel;
// 		}

		private ProbabilityTablePanel createProbabilityTable
				(ConditionalDiscreteStateFunction cdsf,
				 Persistible condition) {
			// 	System.out.println("createProbabilityTable " + cdsf.getHeadings() +
// 													 " condition " + condition
// 													 + cdsf.getStateMatrix());
				
				CDSFTableModel tableModel = new CDSFTableModel(cdsf);
				tableModel.addTableModelListener(this);
				if ( condParameter 
						 instanceof ConditionalTableEventParameter ) 
						tableModel.setLastColumnName("Events");
				ProbabilityTablePanel tablePanel = 
						new ProbabilityTablePanel(tableModel);
				if ( condParameter != null ) 
						tablePanel.setTableType(condParameter.getClass());
				ConditionalPanel condPanel = tablePanel.getConditionalPanel();
				condPanel.setValue(condition);
				tablePanel.addCanvasEditorChangeListener(canvas);
				tableModel.fireTableDataChanged(); 
				tableModel.fireTableStructureChanged();
				tablePanel.getTable().repaint();
				return tablePanel;
		}

		public void refreshNotation(ConditionalTableParameter condParam) {
				notationTextField.setText(condParam.toString());
				// System.out.println("refreshNotation " + 
// 													 condParam);
				//oncotcap.util.ForceStackTrace.showStackTrace();
				revalidate();
		}

		public void refreshNotation(DefaultTableModel sets, 
																Object conditionalSet) {
				//oncotcap.util.ForceStackTrace.showStackTrace();
				StringBuffer setNotation = new StringBuffer();
				for ( int i = 1; i <  sets.getColumnCount() - 1; i++) {
						if ( !sets.getColumnName(i).equals("Dependent") ) {
								setNotation.append(sets.getColumnName(i));
								if ( i+1 < sets.getColumnCount() - 1 )
										setNotation.append(" * ");
						}
				}
				if ( conditionalSet != null ) {
						setNotation.append(" | ");
						setNotation.append(conditionalSet.toString());
				}
				notationTextField.setText(setNotation.toString());
		}
		private OntologyTree getKeywordTree() {
				ot = new OntologyTree();
				ot.collapseController();
				ot.setFilterSelected(true);
				ot.setName("Subset Parameter Keyword Ontology Tree");
				ot.getOntologyButtonPanel().setRoot(OntologyMap.K);
				ot.getOntologyButtonPanel().setLeaves
						(CollectionHelper.makeVector(new Integer(OntologyMap.K)));
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
		public OntologyTree getOntologyTree() {
				return ot;
		}

		public JInternalFrame addTable(ProbabilityTablePanel tablePanel){
		

				probabilityTablePanels.add(tablePanel);
				tablePanel.setVisible(true);
				tablePanel.setSize(new Dimension(300,300));
				// Make the frame title
				String title = condParameter.toString();
				if ( condParameter.getConditionValue() != null ) {
						title = title +	"=" + getConditionValueString(tablePanel);
				}
				
				JInternalFrame jif = 
						new JInternalFrame(title,true,false,true,true);

				jif.add(tablePanel);
				//				System.out.println("jif panel " + 
				//									 (ProbabilityTablePanel)ComponentHelper.getFirstChildComponent(jif.getContentPane(), ProbabilityTablePanel.class));
				jif.setSize(new Dimension(300, 275));
				jif.setVisible(true);
				canvas.setBorder(lightLineBorder);
				canvas.add(jif);

				// Determine the location
				tileWindows();
				canvas.revalidate();
				return jif;
		}
		// adapted from java forum
   public void tileWindows()
   { 
			 JInternalFrame[] frames = canvas.getAllFrames();
 
      // count frames that aren't iconized
      int frameCount = 0;
      for (int i = 0; i < frames.length; i++) {  
					if (!frames[i].isIcon())
            frameCount++;
      }
 
      int cols = (int)Math.sqrt(frameCount);
      int rows = frameCount / cols;
      int extra = frameCount % cols;
      // number of columns with an extra row
 
      int width = canvas.getWidth() / cols;
      int height = canvas.getHeight() / rows;
      int r = 0;
      int c = 0;
	
      for (int i = 0; i < frames.length; i++) {
					if (!frames[i].isIcon()) {  
							try {  
									frames[i].setMaximum(false);
									// Don't stretch if they can fit on the canvas 
									// only shrink to fit
									if ( frames[i].getHeight() < height ) 
											height = frames[i].getHeight();
									if ( frames[i].getWidth() < width ) 
											width = frames[i].getWidth();
									// If somehow the size is 0 do not reshape
									if ( width > 0 || height > 0 ) {
											frames[i].setBounds(c * width,
																				r * height, width, height);
									}
									else {
											// use a default size
											width = 300;
											height = 300;
											frames[i].setBounds(c * width,
																					r * height, width, height);
									}
									r++;
									if (r == rows) {
											r = 0;
											c++;
											if (c == cols - extra) {
													// start adding an extra row
													rows++;
													height = canvas.getHeight() / rows;
											}
									}
							}
							catch(PropertyVetoException e) {
									System.out.println("PropertyVetoException");
							}
					}
      }
   }

	// 	public ProbabilityTablePanel getProbabilityTablePanel() {
// 				ProbabilityTable probTable = new ProbabilityTable();
// 				ProbabilityTablePanel probTablePanel = 
// 						new ProbabilityTablePanel(probTable);
// 				probTablePanel.addCanvasEditorChangeListener(canvas);
// 				ConditionalPanel condPanel = probTablePanel.getConditionalPanel();
// 				condPanel.addCanvasEditorChangeListener(canvas);

// 				return probTablePanel;
// 		}
	// 	public ProbabilityTablePanel getProbabilityTablePanel(Keyword keyword) {
// 				ProbabilityTable probTable = new ProbabilityTable();
// 				probTable.setHeading(keyword.toString());
// 				ProbabilityTablePanel probTablePanel = 
// 						new ProbabilityTablePanel(probTable);
// 				probTablePanel.addCanvasEditorChangeListener(canvas);
// 				ConditionalPanel condPanel = probTablePanel.getConditionalPanel();
// 				condPanel.addCanvasEditorChangeListener(canvas);

// 				return probTablePanel;
// 		}

		// TO DO!!!!!!!!!!!!!!!!! Make level list action
		public void actionPerformed(ActionEvent a) {
				if ( a.getSource() instanceof JButton ) {
						if ( ((JButton)a.getSource()).getText().equals("LL+") ) {
								//showLevelListEditor();
						}
				}
		}


		private Vector getTables(String tableNotation) {
				// Parse out the operators 
				// Legal operators for this field are | ( conditional prob)
				// + marginal probability
				// * joint probability
				// all other strings are assumed to be table variable place holders
				// example age * sex will translate to a single 
				// joint prob table with two columns "age" and "sex" where 
				// age and sex will need to be replaced by enums
				// just as A * B will result in the same thing
				// In future versions the enums will try to be mapped 
				// whenever possible - but level lists and onc process will
				// still need to be selected
				// A B will result in an syntax error because all variables will 
				// need to be separated by an operator
				return null;
		}



		// When an object on this canvas changes as the parent you must handle it
		// to make sure everything is consistent
		public void canvasObjectChanged(CanvasObjectChangeEvent evt ) {
				//System.out.println("canvasObjectChanged TABLE CANVAS - not used?");
		}
		
		public void addDependentSet(BooleanExpression aSet){

				// Dependent set corresponds to a column 
				EnumDefinition heading = null;
				if ( aSet.getLeftHandSide() instanceof EnumDefinition){
						// Not sure I like this design would like to separate 
						// from enum and other data type dependencies
						heading = 
								(EnumDefinition)aSet.getLeftHandSide();
				}
				else // show user an error message
						return; // this has to be set to add a column to table 

				// Get the default level ( set the default radio 
				// button on it) YUCK YUCK YUCK separate instead of levels use ELEMENT
				// ELEMENT TYPE instead of header or EnumDefinition
				EnumLevel level = (EnumLevel)aSet.getRightHandSide();
				// Get the selected level list
				if (level == null) 
						return;
				EnumLevelList levels = level.getLevelList();
				levelLists.addElement(levels);
				// Add the new column ( set ) type to use as a heading
				if ( dependentElementTypes.size() > 2 ) 
						dependentElementTypes.add(dependentElementTypes.size()-1,
																						 heading);
				else 
						dependentElementTypes.addElement(heading);
				// Now get the other dependent columns and start building the 
				// rows which we will call a joint event and the last cell in each 
				// row we will call an outcome
				// the rows will be all combinations of dependent variables
				// for now numeric enums are not allowable
				
				// Generate the cartesian product of all the enums 
				// and their level lists
				buildJointEvents();
		 }


		

		public Object[][] getTableValues() {
				 return jointEvents;
		}
		public Vector getTableHeadings() {
				 return dependentElementTypes;
		}
		// Take the cartesian product of the level list sets
		private void buildJointEvents() {
				// recurse through the dependent variables building rows
				// Pass in the list of level lists and the current position in the list
				// Get max number of levels in all the level lists
				// Get number of levelLists
				int numLevelLists = levelLists.size();
				Iterator i = levelLists.iterator();
				int maxNumLevels = 0;

				while ( i.hasNext() ) {
						EnumLevelList levelList = (EnumLevelList)i.next();
						if ( levelList.getNumberOfLevels() > maxNumLevels ) 
								maxNumLevels = levelList.getNumberOfLevels();
				}
				// Create a 2D array
				numCombinations = (int)Math.pow(maxNumLevels, numLevelLists);
				int[] dim = { numCombinations, numLevelLists};
				jointEvents = 
                 (EnumLevel[][]) Array.newInstance(EnumLevel.class, dim);
				// current row , current column
				buildJointEvents(levelLists, 0, 0, jointEvents);
				//printMatrix(jointEvents);

				// take the rows and reconfigure into columns -NO
		}

		private void printMatrix(EnumLevel [][] mat)
		{
				int rows = mat.length;
				if(rows > 0)
						{
								int cols = mat[0].length;
								int row, column;
								for(row = 0; row < rows; row++)
										{
												System.out.print("\t" + row);
												for(column = 0; column < cols; column++)
														System.out.print("\t" + mat[row][column]);
												System.out.print("\n");
										}
						}
		}
		private int buildJointEvents(Vector levelLists, 
																 int startRow,
																 int currentLevelListIndex, // current col
																 EnumLevel[][] jointEventRows) {
				//int currentLevelIndex,
				EnumLevelList levelList = 
						(EnumLevelList)levelLists.elementAt(currentLevelListIndex);
				SortedList levels = levelList.getLevels();
				Iterator i = levels.iterator();
				int currentRow = startRow;
				while (i.hasNext()) {
						EnumLevel level = (EnumLevel)i.next();
						jointEvents[currentRow][currentLevelListIndex] = 
								level;

						// If there are more LevelLists do them now
						if ( currentLevelListIndex < levelLists.size()-1) {
								currentRow = buildJointEvents(levelLists, 
																							currentRow,
																							currentLevelListIndex+1, //current col
																							jointEventRows);
						}
						else {
								currentRow++;
						}
						// If on the last level list
						// backfill empty values in the other columns in this 
						// row with values in previous row this should be pretty efficient
						if (currentLevelListIndex == levelLists.size()-1  
								&& currentRow < numCombinations ){
								for ( int currentCol=currentLevelListIndex-1; 
											currentCol > -1; currentCol--) {
										if ( jointEvents[currentRow][currentCol] == null ) {
												jointEvents[currentRow][currentCol] = 
														jointEvents[currentRow-1][currentCol];
										}
								}
						}
				}// while
				return currentRow;
		}

		// EditorPanel
		public  void edit(Object objectToEdit){
				//System.out.println("edit objectToEdit");
				// Put info in the fields
				if(objectToEdit instanceof ConditionalTableParameter)
						edit((ConditionalTableParameter) objectToEdit);
		}
		public void edit(ConditionalTableParameter param) {
				//System.out.println("edit param");
				this.condParameter = param;
				fillInUI(); //initData(param.getFunction());
		}
		public void initData(Vector condDiscreteStateFunction) {
				Vector sets = new Vector(); // vector of vectors
				// Convert Value matrix into a vector of vectors
				int i =0;
				StateMatrixRow stateMatrixRow = null;
				while (condDiscreteStateFunction != null && i < condDiscreteStateFunction.size()) {
						stateMatrixRow = (StateMatrixRow)condDiscreteStateFunction.elementAt(i);
						sets.add(i, (Vector)stateMatrixRow.getRowColumns());
						i++;
				}
				OncWritableTableModel model = 
						new OncWritableTableModel();
				model.setDataVector(sets, 
														dependentElementTypes);
		}
		public  void save(){
				// take info from fields and put in object and store in datasource
				// If nothing has been set yet ignore this
				//System.out.println("Saving " + condParameter);
				if ( jointEvents == null )
						return;
				//System.out.println("Saving " + condParameter);
				//condParameter = getFieldsFromUI();
				// make sure fields you want to save have save state > -2 (DO_NOT_SAVE)
				StateMatrix sm = condParameter.getStateMatrix();
				if ( sm != null ) {
						Vector smRows = sm.getStateMatrixRows();
						Iterator i = smRows.iterator();
						while ( i.hasNext() ) { // && headingRowsFound == false) {
								StateMatrixRow smr = (StateMatrixRow)i.next();
								smr.setPersistibleState(Persistible.DIRTY);
								Vector rowColumns = smr.getRowColumns();
								Iterator ii = rowColumns.iterator();
								while ( ii.hasNext() ) {
										Object obj = ii.next();
										if (obj instanceof EnumDefinition ) 
												((EnumDefinition)obj).setPersistibleState
														(Persistible.DIRTY);
								}
						}
				}
				if ( condParameter != null ) {
						//	System.out.println("Saving conditionalTableParameter " + 
						//									 condParameter);
						condParameter.setPersistibleState
														(Persistible.DIRTY);
						// Mark this as modified so that subordinate objects can get saved
						// since the top level rarely changes 
						if (condParameter.getConditionValue() != null
								&& condParameter.getConditionValue() instanceof EnumDefinition ){ 
												((EnumDefinition)condParameter.getConditionValue()).setPersistibleState
														(Persistible.DIRTY);
												((EnumDefinition)condParameter.getConditionValue()).update();
						}
						if ( condParameter.getFunction() != null) {
								for ( int l = 0; l < condParameter.getFunction().size(); l++) {
										((ConditionalDiscreteStateFunction)condParameter.getFunction().elementAt(l)).setPersistibleState(Persistible.DIRTY);
										// Update each fn
										ConditionalDiscreteStateFunction cdsf = 
												((ConditionalDiscreteStateFunction)condParameter.getFunction().elementAt(l));
										Vector outcomes = cdsf.getOutcomes();
										Iterator iii = outcomes.iterator();
										while ( iii.hasNext() ) {
												ConditionalOutcome outcome = 
														(ConditionalOutcome)iii.next();
												outcome.setPersistibleState(Persistible.DIRTY);
										// 		System.out.println("saving outcome " + outcome + 
// 																					 " " + outcome.getOutcome());
												outcome.update();
										}
										cdsf.update();
								}
						}
						if (condParameter.getStateMatrix() != null) {
								condParameter.getStateMatrix().update();
								Vector stateMatrixRows = 
										condParameter.getStateMatrix().getStateMatrixRows();
								if ( stateMatrixRows != null ) {
										for ( int i = 0; i < stateMatrixRows.size(); i++) {
												StateMatrixRow stateMatrixRow = 
														(StateMatrixRow)stateMatrixRows.elementAt(i);
												stateMatrixRow.update();
										}
								}
						}
						condParameter.setPersistibleState(Persistible.DIRTY);
						condParameter.update();

				}
				// Update the editor title
		}

		public ConditionalTableParameter getFieldsFromUI() {
				//System.out.println("GETFIELDS FROM UI");
				if ( condParameter == null ) {
						condParameter = new ConditionalTableParameter();
				}
				syncStateMatrix(condParameter);
				
				// Create a conditional function for each 
				//		table/conditional value 
				// Outcomes point to state matrix rows
				// conditional discrete state fiunction should
				// point to state matrix
				
				// If this parameter already has functions clean them out and 
				// reuse them 
				DefaultPersistibleList stateFunctions = 
						new DefaultPersistibleList();
				ConditionalDiscreteStateFunction condDiscreteStateFunction = null;
				// Go thru all the tables on the canvas
				// each table represents an instance of function
				JInternalFrame jif = null;
				ConditionalDiscreteStateFunction cdsf = null;
				ProbabilityTablePanel probTablePanel = null;
				ConditionalPanel condPanel = null;
				Persistible conditionalValue = null;
				for (Enumeration e=probTableFrames.keys(); 
						 e.hasMoreElements(); ) {
						cdsf = 
								(ConditionalDiscreteStateFunction)e.nextElement();
						//System.out.println("cdsf used to get prob table " + cdsf);
						// Get the variable
						jif = (JInternalFrame)probTableFrames.get(cdsf);
						System.out.println("jif " + jif.getTitle());
						probTablePanel = (ProbabilityTablePanel)ComponentHelper.getFirstChildComponent(jif.getContentPane(), JPanel.class);
						if ( probTablePanel == null ) {
								//System.out.println("Does not currently have a UI " + cdsf);
								
						}
						else {
								condPanel = probTablePanel.getConditionalPanel();
								//System.out.println("does prob panel have a good cdsf " + 
								//probTablePanel.getTableModel().getCDSF());
						}
						if ( condPanel != null ) {
								// Get the conditional value
								if ( condPanel.getValue() instanceof BooleanExpression ) 
										conditionalValue = 
												(Persistible)((BooleanExpression)condPanel.getValue()).getRightHandSide();
								else if ( condPanel.getValue() instanceof EnumLevel) 
										conditionalValue = (Persistible)condPanel.getValue();
						}
					// 	StateMatrix stateMatrix = condParameter.getStateMatrix();
// 						cdsf.setStateMatrix(stateMatrix);
// 						cdsf.setConditionValue(conditionalValue);
						//  	System.out.println("condDiscreteStateFunction " + 
						// 																 condDiscreteStateFunction
						// 																 + " conditionValue " 
						//  															  + conditionalValue);
						// Now get the outcomes - the last column in the table
					// 	System.out.println("Get fields from UI outcomes before" + cdsf.getOutcomes());
						// if ( probTablePanel != null ) {
// 								int numColumns = probTablePanel.getTableModel().getColumnCount();
// 								int numRows = probTablePanel.getTableModel().getRowCount();
// 								Vector outcomes  = null;
// 								if ( cdsf.getOutcomes() != null ) 
// 										outcomes = cdsf.getOutcomes();
// 								else 
// 										outcomes = new Vector();
// 								int j = 1;
// 								ConditionalOutcome outcome = null;
// 								ConditionalEventOutcome eventOutcome = null;
								
// 								DefaultPersistibleList stateMatrixRows = 
// 										stateMatrix.getStateMatrixRows();
// 								while ( stateMatrixRows != null 
// 												&& j <= stateMatrixRows.size()-1 ) {
// 										Object outObject =
// 												probTablePanel.getTableModel().getValueAt(j-1, numColumns-1);
// 										// Re use existing conditional outcomes
// 										if ( outcomes.size() > j-1 ) {
// 												System.out.println("existing outcomes " + outcomes);
// 												outcome = (ConditionalOutcome)outcomes.elementAt(j-1);
// 										}
// 										else {

// 												if ( condParameter 
// 														 instanceof ConditionalTableEventParameter ){
// 														System.out.println("maybe new event outcome " );
// 														if ( outObject != null ){
// 																System.out.println("new event outcome " +
// 																									 outObject);
// 																outcome = new ConditionalEventOutcome();
// 																((ConditionalEventOutcome)outcome).setOutcome(((EventDefinition)outObject));
// 														}
// 												}
// 												else {
// 														System.out.println("new cond out " );
// 														outcome = new ConditionalOutcome();
// 												}
// 										}
// 										if ( outcome != null ) {
// 												System.out.println("outcome is not null " + outObject);
// 												outcome.setOutcome(outObject);
// 												outcome.setConditionValue(conditionalValue);
// 												outcome.setStateMatrixRow(
// 																									(StateMatrixRow)stateMatrixRows.elementAt(j));
// 												outcomes.add(j-1, outcome);
// 										}
// 						System.out.println("Get fields from UI outcomes after" + cdsf.getOutcomes());
// 										j++;
// 								}
			 
						//cdsf.setOutcomes(outcomes);
						// System.out.println(" get fields from ui cdsf " 
// 															 + cdsf.toString());
						stateFunctions.add(cdsf);
				}				
				condParameter.setFunction(stateFunctions);
				if ( condParameter != null && 
						 setType instanceof EnumDefinition) {
						condParameter.setConditionValue((EnumDefinition)setType);
				}
				return condParameter;
		}
		
		private String getConditionValueString(ProbabilityTablePanel probTable) {
				ConditionalPanel condPanel = probTable.getConditionalPanel();
				Persistible conditionalValue = null;
				if ( condPanel != null ) {
						// Get the conditional value
						if ( condPanel.getValue() instanceof BooleanExpression ) 
								conditionalValue = 
										(Persistible)((BooleanExpression)condPanel.getValue()).getRightHandSide();
						else if ( condPanel.getValue() instanceof EnumLevel) 
								conditionalValue = (Persistible)condPanel.getValue();
						if (conditionalValue != null) 
								return conditionalValue.toString();
				}
				return null;
		}
		public  Object getValue(){ return condParameter; }
		public ConditionalDiscreteStateFunction 
		createCDSF(ConditionalTableParameter condParameter,
				Persistible conditionalValue) {
			ConditionalDiscreteStateFunction cdsf = 
				new ConditionalDiscreteStateFunction();
			cdsf.setFunctionParameter(condParameter);
			cdsf.setConditionValue(conditionalValue);
			System.out.println("createCDSF " + cdsf );
			// Initialize outcomes
			DefaultPersistibleList outcomes = 
				initializeOutcomes(condParameter.getStateMatrix(), cdsf);
			cdsf.setOutcomes(outcomes);
			
			return cdsf;
		}
		public DefaultPersistibleList initializeOutcomes(StateMatrix sm, 
				ConditionalDiscreteStateFunction cdsf) {
			//System.out.println("initializeOutcomes " + cdsf);
			Class cls;
			ConditionalOutcome outcome = null;
			DefaultPersistibleList outcomes = cdsf.getOutcomes();
			if ( outcomes == null ) {
				outcomes = new DefaultPersistibleList();
			}
			if (condParameter
					instanceof ConditionalTableEventParameter) {
				cls = ConditionalEventOutcome.class;
			}
			else 
				cls = ConditionalOutcome.class;
			try {
				for ( int i = 0; i < sm.getStateMatrixRows().size(); i++) {
					// Put empty outcome in heading  row to maybe be used to specify type of outcome
					if (((StateMatrixRow)sm.getStateMatrixRows().elementAt(i)).getIsHeading() )
						continue;
					if ( outcomes.size() < i || outcomes.elementAt(i-1) == null ) {
						outcome = (ConditionalOutcome)cls.newInstance();
						outcome.setStateMatrixRow((StateMatrixRow)sm.getStateMatrixRows().elementAt(i));
						outcome.setConditionValue(cdsf.getConditionValue());
						outcomes.add(outcome);
						System.out.println("Create a new outcome " + i + " " + outcome + " count "
								+ outcomes.size());
					}
					else {
						outcome = (ConditionalOutcome)outcomes.elementAt(i-1);
						outcome.setConditionValue(cdsf.getConditionValue());
						System.out.println("Update an existing outcome " + outcome  + " at " + (i-1));
					}
					
				}
			}
			catch(Exception e) {
				System.out.println("Error initializing outcomes" );
				e.printStackTrace();
			}
			//System.out.println("initializeOutcomes after " + cdsf);
			
			return outcomes;
		}
		public String toString() {
			return notationTextField.getText();
		}
		public static void main(String[] args) {
				try {
						UIManager.setLookAndFeel(WINDOWS);
				}
				catch (Exception ex) {
						System.out.println("Failed loading L&F: ");
				}
				UIManager.put( "Table.selectionBackground",new Color(228,225,220)); 
				UIManager.put( "Table.selectionForeground",Color.BLACK); 
				UIManager.put( "List.selectionBackground",new Color(228,225,220));
				UIManager.put( "List.selectionForeground",Color.BLACK); 
				oncotcap.Oncotcap.handleCommandLine(args);
				dataSource = oncotcap.Oncotcap.getDataSource();

				ConditionalParameterEditor p = new ConditionalParameterEditor();
				OncFrame f = new OncFrame(p);
 				f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				WindowListener windowListener = 
						new WindowAdapter() {
								public void windowClosing(WindowEvent e) {
										System.exit(0);
								}
						};
				f.addWindowListener(windowListener);
				f.getContentPane().add(p);
				f.setSize(500,500);
				f.setVisible(true);

		}



		//CLASS
		public class DroppableCanvas extends EditorDesktopPane 
				implements DropTargetListener,
									 CanvasEditorChangeListener {
				Vector usefulComponents = new Vector();
				public DroppableCanvas() {
						super();
						//setSize(new Dimension(600,630));
						setBackground(OncBrowserConstants.MBColorDark);
						new DropTarget(this, this);

				}
				
				public Component[] getProbabilityTablePanels() {
						Component[] componentArray = 
								(Component[])Array.newInstance(Component.class, 
																	probabilityTablePanels.size());
						componentArray = 
								(Component[])probabilityTablePanels.toArray(componentArray);
						return componentArray;
				}

				// When an object on this canvas changes as the parent you must 
				//handle it to make sure everything is consistent
				public void canvasObjectChanged(CanvasObjectChangeEvent evt ) {
						System.out.println("canvasObjectChanged CANVAS");
						BooleanExpression bool = null;
						EnumLevel level = null;
						EnumLevelList levelList = null;
						Vector sets = new Vector();
						if ( evt.getChangedObject() instanceof BooleanExpression ) {
								// Create a copy of the current cartesian product
								// one for each element in the 'condition' set 
								 bool = (BooleanExpression)evt.getChangedObject();
								 level = (EnumLevel)bool.getRightHandSide();
								 // Get the selected level list
								 if ( level != null )
										 levelList = level.getLevelList();

						}
						// If an object has been added
						if ( evt.getChangeEventType() == CanvasObjectChangeEvent.ADD ) {
								// Where was it added - what type of object was it added to 
								if ( evt.getSource() instanceof  ConditionalPanel ) {
										// System.out.println("adding to conditional panel " + 
// 																			 condParameter.getGUID());
										// Determiine the new state matrices and update the 
										// conditionalTableParameter
										// Remove any existing ui objects and 
										// render new ones -- -see if this is too time/memory
										// consuming
 
										// What element is this parameter conditioned upon
										if ( !(bool.getLeftHandSide() instanceof Persistible) ) {
												System.out.println("Error ConditionalParameterEditor.CanvasObjectChangeEvent() : Adding non Persistible to conditional panel is unsupported at this time.");
												return;
										}
												
										setType = bool.getLeftHandSide();
										if ( condParameter != null && 
												 setType instanceof EnumDefinition)
												condParameter.setConditionValue((EnumDefinition)setType);
										// Update functions ( 1 per condition ) 
										// Determine how many levels in the new condition
										// This is how many functions (-1) that need to be created
										if ( levelList == null ) {
												System.out.println("Warning: This command will be ignored. A level list must be provided."); // send this to the screen
												return; // Something isn't right
										}
										SortedList levels = levelList.getLevels();
										ConditionalDiscreteStateFunction cdsf = null;
										// Modify the existing function
										if ( condParameter.getFunction() != null && 
												 condParameter.getFunction().size() > 0 ){
												cdsf = 
													(ConditionalDiscreteStateFunction)condParameter.getFunction().firstElement();
												//	System.out.println("CDSF " + cdsf.getGUID());
												cdsf.setConditionValue((EnumLevel)levels.get(0));
												//refresh existing table
												BooleanExpression condPanelBoolean = null;
												if ( levels.get(0) != null ) { 
														condPanelBoolean = new BooleanExpression(false);
														condPanelBoolean.setLeftHandSide(condParameter.getConditionValue());
														condPanelBoolean.setRightHandSide((EnumLevel)levels.get(0));
												}	
												((ConditionalPanel)evt.getSource()).setValue(condPanelBoolean);
												((ConditionalPanel)evt.getSource()).revalidate();
												JInternalFrame jif =
														(JInternalFrame)probTableFrames.get(cdsf);
												// update title
												if ( jif != null ) {
														jif.setTitle(condParameter.toString() +	"=" 
																				 + (EnumLevel)levels.get(0) );
														jif.revalidate();
												}
										}
										//System.out.println("fnIterator " + condParameter.getFunction().size());
										// Create new functions
										for ( int i = 1; 
													i < levels.size(); i++) {
												cdsf = createCDSF(condParameter, (EnumLevel)levels.get(i)); 	
												condParameter.setFunction(cdsf);
												//System.out.println("fnIterator " + condParameter.getFunction().size());
												renderFunction(condParameter, cdsf);

										}
										// Refresh the notation
										refreshNotation(condParameter);
								}//conditionalpanel
								else if (evt.getSource() instanceof ProbabilityTablePanel ) {
										//System.out.println("adding to column to table " + 
										//condParameter);
										// Adding a column to the tables
										addDependentSet(bool);

										// Update the statematrix
										syncStateMatrix(condParameter);
										// Update all the CDSFs to match the new statematrix
										// this means all outcomes will be dumped they are 
										// no longer relevant
										if ( condParameter.getFunction() != null && 
												 condParameter.getFunction().size() > 0 ){
												Iterator fnIterator =  condParameter.getFunction().iterator();
												ConditionalDiscreteStateFunction cdsf = null;
												while ( fnIterator.hasNext() ) {
														cdsf = 
																(ConditionalDiscreteStateFunction)fnIterator.next();
														cdsf = updateCDSF(condParameter,
																			 cdsf);
														//System.out.println("Just updated " + cdsf);
												}
										}
 										// Refresh the notation
										refreshNotation(condParameter);
										revalidate();
								}
						}//add
				}



		private ConditionalDiscreteStateFunction 
				updateCDSF(ConditionalTableParameter condParameter,
									 ConditionalDiscreteStateFunction cdsf) {
				//System.out.println("updateCDSF " + cdsf.getGUID());
				JInternalFrame jif = (JInternalFrame)probTableFrames.get(cdsf);
				ProbabilityTablePanel probTablePanel = 
						(ProbabilityTablePanel)ComponentHelper.getFirstChildComponent(jif.getContentPane(), JPanel.class);
				// System.out.println("BEFORE updateCDSF " + cdsf.getGUID()
// 													 + " prob table " + probTablePanel.getTableModel().getCDSF().getGUID()
// 	 + " prob model" + probTablePanel.getTableModel());
				DefaultPersistibleList outcomes = 
						initializeOutcomes(condParameter.getStateMatrix(), cdsf);
			 	System.out.println("updated OUTCOMES " + cdsf.getGUID() + "  " 
													 +  cdsf.getStateMatrix());
				cdsf.setOutcomes(outcomes);
				
	// 			if ( cdsf.getConditionValue() != null ) {
// 						jif.setTitle(condParameter.toString() +	"=" 
// 												 + cdsf.getConditionValue() );
// 				}
// 				else {
// 						jif.setTitle(condParameter.toString());
// 				}
				// Refresh the table
				
				// System.out.println(">> cdsf " + cdsf.getGUID() + " " + cdsf + " prob table " + probTablePanel.getTableModel().getCDSF().getGUID()+ " prob model" + probTablePanel.getTableModel());
 				probTablePanel.getTableModel().fireTableStructureChanged();
				probTablePanel.getTable().repaint();

				probTablePanel.revalidate();
				return cdsf;
		}
        public void dragEnter(DropTargetDragEvent evt) {
            // Called when the user is dragging and enters this drop target.
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
								 System.out.println("Drop it on main Canvas panel " + t 
 																	 + " condParameter " + condParameter);
								if ( probTableFrames.size() > 0 ) {
										// If the main canvas is not blank do not allow 
										// dropping - only conditions and joint probabilities are 
										// allowed at that point
										// Tell this to the user?
									// 	System.out.println("Warning : Adding to main canvas after initial probability condition has been established is not allowed " );
										return;
								}
								if ( t.isDataFlavorSupported(Droppable.genericTreeNode) ) {
										evt.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
										Object transferableData = 
												t.getTransferData(Droppable.genericTreeNode);
										GenericTreeNode transferNode = 
												(GenericTreeNode)transferableData;
										boolean persistMode = false;
										if ( transferNode.getUserObject() instanceof Keyword) {
												addKeywordToMainCanvas(
																 (Keyword)transferNode.getUserObject());
										}
										evt.getDropTargetContext().dropComplete(true);
								}
								else if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
										evt.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
										String s = 
												(String)t.getTransferData(DataFlavor.stringFlavor);
										evt.getDropTargetContext().dropComplete(true);
								} else {
										evt.rejectDrop();
								}
						} catch (IOException e) {
								evt.rejectDrop();
						} catch (UnsupportedFlavorException e) {
								evt.rejectDrop();
						}
				}
		}
		
		public boolean addKeywordToMainCanvas(Keyword keyword) {
			BooleanExpression bool = null;
			// Determine if there are levels associated with keyword
			if ( keyword.getAssociatedLevelLists() != null 
					&& keyword.getAssociatedLevelLists().size() > 0 )
				 bool = 	
						BooleanExpression.dropKeywordCreateEnum
						(keyword, false);
			else {
				JOptionPane.showMessageDialog
				(null, 
				 "There are no level lists associated with this keyword. Please add a level list using the LL+ button.");
				return false;
			}
				// Determine the enum or backquoted reference to
				// and enum
				Object setType = 
						bool.getLeftHandSide();
				// Get the default level ( set the default radio 
				// button on it)
				EnumLevel level = (EnumLevel)bool.getRightHandSide();
				// Get the selected level list
				if ( level == null )
					return false;
				EnumLevelList levelList = level.getLevelList();

				// 	// System.out.println("Level List " 
				// 												// 											 + levelList);
				// 												ProbabilityTable probTable = new ProbabilityTable();
				// 												probTable.initProbTable(setType.toString(),
				// 																								levelList);
				addDependentSet(bool);
				syncStateMatrix(condParameter);
				ConditionalDiscreteStateFunction cdsf = 
						createCDSF(condParameter,
											 (EnumLevel)null);
				condParameter.setFunction(cdsf);	
				// System.out.println("condParameter " + condParameter +
				// 																					 " cdsf just added " + cdsf);
				renderFunction(condParameter, cdsf);
				
				refreshNotation(condParameter);
				return true;
		}
		private void syncStateMatrix(ConditionalTableParameter condParameter) {
				// Get the value matrix ( the body of all the displayed tables )
				// This is a 2D array
				StateMatrix stateMatrix = condParameter.getStateMatrix();
				if ( stateMatrix == null )
						stateMatrix = new StateMatrix();
				DefaultPersistibleList jointEventsVector = 
						oncotcap.util.CollectionHelper.arrayToDefaultPersistibleList(jointEvents);							
				int numDataRows = jointEventsVector.size();

				DefaultPersistibleList stateMatrixRows = 
						stateMatrix.getStateMatrixRows();
				if ( stateMatrixRows == null )
						stateMatrixRows = new DefaultPersistibleList();
//System.out.println("stateMatrixRows " + stateMatrixRows);

				// Reuse any rows that exist just reset values 
				StateMatrixRow stateMatrixRow = stateMatrix.getStateMatrixRowHeading();
				if ( stateMatrixRow == null ) {
						stateMatrixRow = new StateMatrixRow();
						stateMatrixRow.setIsHeading(true);
						stateMatrixRows.add(0, stateMatrixRow);
				}
				DefaultPersistibleList headingValues = 
						new DefaultPersistibleList();
				headingValues.addAll(dependentElementTypes);
				// Strip off the ends
// 				System.out.println("dependentElementTypes " + dependentElementTypes);
				int indx = -1;
				if ( (indx = headingValues.indexOf("Dependent")) > -1 ) {
						headingValues.remove(indx);
				}
				if ( (indx = headingValues.indexOf("Proportion")) > -1 ) {
						headingValues.remove(indx);
				}
// 				if ( headingValues.size() > 2 ) {
// 						headingValues.remove(headingValues.size()-1);
// 						headingValues.remove(0);
// 						headingValues.trimToSize();
// 				}
// 				System.out.println("headingValues " +  headingValues);
						
				stateMatrixRow.setRowColumns(headingValues);

				// If this is a modification of an existing state matrix 
				// remove any excess rows and reuse any existing rows 
				StateMatrixRow extraRow = null;

				if ( stateMatrixRows.size() > jointEventsVector.size()+1 ) {
						//System.out.println("stateMatrixRows " +  stateMatrixRows.size());						//System.out.println("jointEventsVector " 
						//+  jointEventsVector.size());
						for ( int k = jointEventsVector.size(); 
									k < stateMatrixRows.size(); k++ ) {
								extraRow = (StateMatrixRow)stateMatrixRows.elementAt(k);
								// System.out.println("stateMatrixRows ( delete ) " 
// 																	 + extraRow);
								extraRow.delete();
								stateMatrixRows.remove(k);
								stateMatrixRows.trimToSize();
						}
				}
				int i;
				// Fill up the existing rows 
				int jIndex = 0;
				for ( i = 0; i < stateMatrixRows.size(); i++) {
						stateMatrixRow = (StateMatrixRow)stateMatrixRows.elementAt(i);
					// 	System.out.println("num stateMatrixRows " + 
// 															 + stateMatrixRows.size() 
// 															 + " num data rows "
// 															 + numDataRows
// 															 + " stateMatrixRow A " + i 
// 															 + " " + stateMatrixRow);
						if (stateMatrixRow == null )
								continue;
						if ( stateMatrixRow.getIsHeading() == true)
								continue;
						stateMatrixRow.setRowColumns((DefaultPersistibleList)jointEventsVector.elementAt(jIndex));
						jIndex++;
						//System.out.println("stateMatrixRow " + stateMatrixRow);
						//stateMatrixRows.add(stateMatrixRow);
				}
				// Add new rows
				for ( i = stateMatrixRows.size(); i < jointEventsVector.size()+1; i++) {
						stateMatrixRow = new StateMatrixRow();
						stateMatrixRow.setRowColumns((DefaultPersistibleList)jointEventsVector.elementAt(i-1));
						
						stateMatrixRows.add(stateMatrixRow);
						// System.out.println("stateMatrixRow B " + (i) 
// 															 + " " + stateMatrixRow
// 															 + " STATE MATRIX ROWS " + stateMatrixRows);
				}
				stateMatrix = condParameter.getStateMatrix();
			// 	System.out.println("existing state matrix is " + 
// 													 stateMatrix);
				if ( stateMatrix == null ) {
						stateMatrix = new StateMatrix();
						stateMatrix.setStateMatrixRows(stateMatrixRows);
						condParameter.setStateMatrix(stateMatrix);
				}
// 				System.out.println("sync stateMatrix " + stateMatrix);
		}

		private Vector initializeOutcomes(Vector oldVec, Class cls) {
				Vector vec = new Vector(oldVec);
				// Add the eventradio button as the first element of every row
				Iterator i = vec.iterator();
				Object inst = null;
				while ( i.hasNext() ) {
						Vector row = (Vector)i.next();
						try {
								inst = cls.newInstance();
								row.add(inst);
						}
						catch(Exception e) {
								System.out.println("Error initializing outcomes" );
						}
				}
				return vec;
		}
		private Vector addProbTableDefaultColumns(Vector oldVec) {
				Vector vec = new Vector(oldVec);
				// Add the radio button as the first element of every row
				Iterator i = vec.iterator();
				while ( i.hasNext() ) {
						Vector row = (Vector)i.next();
						row.add(0, new JRadioButton());
				}
				
				return vec;
		}
		
//		Group the radio buttons.
//	    ButtonGroup group = new ButtonGroup();
//	    group.add(birdButton);

		private Vector addProbTableDefaultColumns(Object[][] tableValues) {
				Vector vec = oncotcap.util.CollectionHelper.arrayToVector(tableValues);
				// Add the radio button as the first element of every row
				Iterator i = vec.iterator();
				while ( i.hasNext() ) {
						Vector row = (Vector)i.next();
						row.add(0, new JRadioButton());
				}
				return vec;
		}
		
		public void tableChanged(TableModelEvent e){
			revalidate();
			repaint();
		}

}


/*
										
										// NULL POINTER
										probTable.addDependentSet(bool);
										// Create data set that has an additional column for the
										// default row button and the outcome column
										Vector elements = 
											 CollectionHelper.arrayToVector(probTable.getTableValues());
										dataModel.setDataVector(elements,
																						probTable.getTableHeadings());
										// make a little method out of this and for the outcomes 
										// field
										JRadioButton[] jbuttons = 
												(JRadioButton[]) Array.newInstance
												(JRadioButton.class,
												 elements.size());
										// Make a button group for the default row
										buttonGroup = new ButtonGroup();
										for (int j = 0; j < elements.size(); j++) {
												jbuttons[j] = new JRadioButton();	
												buttonGroup.add(jbuttons[j]);
										}
										dataModel.addColumn("Default",jbuttons);
										oncTable.revalidate();

 */
