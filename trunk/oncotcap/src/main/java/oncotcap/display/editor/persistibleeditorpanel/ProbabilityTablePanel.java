package oncotcap.display.editor.persistibleeditorpanel;

import java.awt.*;
import java.io.*;
import java.text.*;
import javax.print.attribute.*;
import java.awt.event.*;
import java.util.*;
import java.lang.reflect.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.tree.*;
import javax.swing.border.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;

import oncotcap.util.*;
import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.browser.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.datalayer.autogenpersistible.*;


public class ProbabilityTablePanel extends CanvasEditorPanel 
		implements ItemListener, TreeSelectionListener, DropTargetListener
																																	
{
		ProbabilityTable probTable = null;
		ConditionalPanel conditionalPanel = null;
		CDSFTableModel dataModel = null;
		JTable table = null;
		TablePopup tablePopup = new TablePopup();
		JPanel pickEnumPanel = null;
		ButtonGroup buttonGroup = null;
		private static int LEVEL = 1;
		private static int PROBABILITY = 2;
		private static int currentDefaultRow = -1;
		private static int percentageColumn = 2;
		private OncPopupMenu headerPopupMenu = new OncPopupMenu("Table Column Popup");
		private Class tableType;
		String [] strings = {"TEST", "CASE", "NUMBER 1"};
		public  ProbabilityTablePanel() {
				super();
				probTable = new ProbabilityTable();
		}

		public  ProbabilityTablePanel(CDSFTableModel tableModel) {
				super();
				//new DropTarget(this, this);
				dataModel = tableModel;
				table = new JTable(tableModel);
				table.setVisible(true);
				initUI();
				table.repaint();
				//oncotcap.util.ForceStackTrace.showStackTrace();
		}

		private void initUI() {
				new DropTarget(this, this);
				setLayout(new BorderLayout());
				//setPreferredSize( new Dimension(300,300));
				//dataModel = initProbabilityTableModel();
				table = new JTable(dataModel);
				//table.getTableHeader().setFont(new Font("Helvetica", 
				// Font.BOLD, 20));
				// Make a button group for the default row
				buttonGroup = new ButtonGroup();
				JTableHeader header = table.getTableHeader();
				header.addMouseListener(new ColumnHeaderListener());
				// Pack the second column of the table
				packRows(table, 4);
				packColumns(table, 0);
				
				
				table.addMouseListener(new TableListener());
				// Add header in NORTH slot
				JPanel tablePanel = new JPanel(new BorderLayout());
				tablePanel.add(header, BorderLayout.NORTH);
				tablePanel.add(table, BorderLayout.CENTER);
				// Add header in NORTH slot
				conditionalPanel = new ConditionalPanel(this);
				// conditionalPanel.add(new JLabel("Test condition A=1"));
				// 			conditionalPanel.setBackground(Color.green);
				add(conditionalPanel, BorderLayout.NORTH);
				add(tablePanel, BorderLayout.CENTER);
				initHeaderPopup();
			
				setRendersOnDefaultColumns();
		}
		private void initHeaderPopup(){
				// Add menu items to the popup
				// Allow delete columns
				JMenuItem mi;
				mi = new JMenuItem("Delete Column");
				//CopyPersistibleAction copyAction = new CopyPersistibleAction();
				//mi.setAction(copyAction);
				headerPopupMenu.add(mi);
				
				// Allow blank out columns ( Only available for last column)
				mi = new JMenuItem("Clear All Column Values");
				//mi.setActionCommand("");
				headerPopupMenu.add(mi);
				headerPopupMenu.setOpaque(true);
				headerPopupMenu.setLightWeightPopupEnabled(true);
				
		}
		public ConditionalPanel getConditionalPanel() {
				return conditionalPanel;
		}

		public CDSFTableModel getTableModel() {
				return dataModel;
		}
		public JTable getTable() {
				return table;
		}
		public void setTableType(Class cls) {
				tableType = cls;
				setRendersOnDefaultColumns();
		}
		public Class getTableType() {
				return tableType;
		}

		// from java almanac
    public void packColumns(JTable table, int margin) {
        for (int c=0; c<table.getColumnCount(); c++) {
            packColumn(table, c, 2);
        }
    }
    
    // Sets the preferred width of the visible column specified by vColIndex. The column
    // will be just wide enough to show the column head and the widest cell in the column.
    // margin pixels are added to the left and right
    // (resulting in an additional width of 2*margin pixels).
    public void packColumn(JTable table, int vColIndex, int margin) {
        TableModel model = table.getModel();
        DefaultTableColumnModel colModel = (DefaultTableColumnModel)table.getColumnModel();
        TableColumn col = colModel.getColumn(vColIndex);
        int width = 0;
    
        // Get width of column header
        TableCellRenderer renderer = col.getHeaderRenderer();
        if (renderer == null) {
            renderer = table.getTableHeader().getDefaultRenderer();
        }
        Component comp = renderer.getTableCellRendererComponent(
            table, col.getHeaderValue(), false, false, 0, 0);
        width = comp.getPreferredSize().width;
    
        // Get maximum width of column data
        for (int r=0; r<table.getRowCount(); r++) {
            renderer = table.getCellRenderer(r, vColIndex);
            comp = renderer.getTableCellRendererComponent(
                table, table.getValueAt(r, vColIndex), false, false, r, vColIndex);
            width = Math.max(width, comp.getPreferredSize().width);
        }
    
        // Add margin
        width += 2*margin;
    
        // Set the width
        col.setPreferredWidth(width);
    }
  // The height of each row is set to the preferred height of the
    // tallest cell in that row.
    public void packRows(JTable table, int margin) {
        packRows(table, 0, table.getRowCount(), margin);
    }
    
 // Returns the preferred height of a row.
    // The result is equal to the tallest cell in the row.
    public int getPreferredRowHeight(JTable table, int rowIndex, int margin) {
        // Get the current default height for all rows
        int height = table.getRowHeight();
    
        // Determine highest cell in the row
        for (int c=0; c<table.getColumnCount(); c++) {
            TableCellRenderer renderer = table.getCellRenderer(rowIndex, c);
            Component comp = table.prepareRenderer(renderer, rowIndex, c);
            int h = comp.getPreferredSize().height + 2*margin;
            height = Math.max(height, h);
        }
        return 2*height;
    }

    // For each row >= start and < end, the height of a
    // row is set to the preferred height of the tallest cell
    // in that row.
    public void packRows(JTable table, int start, int end, int margin) {
        for (int r=0; r<table.getRowCount(); r++) {
            // Get the preferred height
            int h = getPreferredRowHeight(table, r, margin);
    
            // Now set the row height using the preferred height
            if (table.getRowHeight(r) != h) {
                table.setRowHeight(r, 35);
            }
        }
    }

		public Object getValue(){ return null; }
		public  void edit(Object objectToEdit){
				edit((ProbabilityTable)objectToEdit );
				setVisible(true);
		}
		
		public void edit(ProbabilityTable probTable) {
				this.probTable = probTable;
				this.setSaveToDataSourceOnCreate(probTable.shouldSave());

				//invalidate();
				repaint();
		}
		
		
		public void save()
		{
			// 	ButtonModel selectedButtonModel = buttonGroup.getSelection();
// 				if ( dataModel != null ) {
// 						// Initialize the percentages & level columns
// 						Vector dataVector = dataModel.getDataVector();
// 						if ( probTable == null ) 
// 								probTable = new ProbabilityTable();
// 						Collection probabilities = new Vector(); 
//  						System.out.println("WHY CLEAR " );

// // 						probabilities.clear();
// 						if (dataVector != null ) {
// 								//System.out.println("dataVector " + dataVector);
// 								Iterator i = dataVector.iterator();
// 								while ( i.hasNext() ) {
// 										Vector row = (Vector)i.next();
// 										if ( row.size() >= 3 ) {
// 												EnumLevel level = (EnumLevel)row.elementAt(LEVEL);
// 												String levelProbability = (String)row.elementAt(PROBABILITY);
// 												if ( level != null && levelProbability != null ) {
													
// 														BooleanExpression probability = 
// 																new BooleanExpression();
// 														probability.setLeftHandSide(level);
// 														probability.setRightHandSide
// 																(new TcapString(levelProbability));
// 														probabilities.add(probability);
// 												}
// 										}
										
// 								}
// 								probTable.setProbabilities(probabilities);
// 								probTable.update();
// 						}
//				}	
		}



// 		private AbstractTableModel initProbabilityTableModel(){
// 				//System.out.println("initProbabilityTableModel");
// 				Vector columnNames = new Vector();
// 				Vector columnData = new Vector();
// 				AbstractTableModel model = new AbstractTableModel();
// 				if ( probTable != null ) {
// 						columnNames.addElement("Default");
// 						columnNames.addElement(probTable.getHeading());
// 						columnNames.addElement("Proportion");

// 						// Initialize the percentages & level columns
// 						if ( probTable.getProbabilities() != null ) {
// 								Iterator i = probTable.getProbabilities().iterator();
// 								while ( i.hasNext() ) {
// 										Vector row = new Vector();
// 										BooleanExpression probability = (BooleanExpression)i.next();
// 										//probability.setLeftHandSide(sortedLevels.get(i));
// 										row.addElement(new JRadioButton(""));
// 										row.addElement(probability.getLeftHandSide());
// 										row.addElement(probability.getRightHandSide());
// 										columnData.addElement(row);
// 								}
// 						}
// 						else {
// 								// dummy data 
// 								Vector row = new Vector();
// 								row.addElement(new JRadioButton(""));
// 								row.addElement(new String("ONE"));
// 								row.addElement(new String("two"));
// 								columnData.addElement(row);
// 						}
// 						model.setDataVector(columnData,columnNames);
// 						Vector editableColumns = new Vector();
// 						editableColumns.addElement(new Integer(0));
// 						editableColumns.addElement(new Integer(2));
// 						model.setEditableColumns(editableColumns);
// 				}
// 				return ( model ) ;
// 		}

		public void setDataModel(CDSFTableModel tableModel) {
				// This hapens every update
				this.dataModel = tableModel;
				table.setModel(tableModel);
				System.out.println("setDataModel " + dataModel.getColumnCount());
				// Make sure the last column is rendered using the JLabel and 
				// edited using the combobox
				setRendersOnDefaultColumns();
				//TableColumn table.getColumn("Proportion");
				
		}

		public void setTable(JTable table) {
				table = table;
		}

		// TreeSelectionListener
		public void valueChanged(TreeSelectionEvent e) {
				JTree tree = (JTree)e.getSource();
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)
						((JTree)e.getSource()).getLastSelectedPathComponent();
				if ( node.getUserObject() instanceof Keyword) {
						Keyword keyword = (Keyword)node.getUserObject();
						// show all available levels and level lists
						
						pickEnumPanel.add(getLevelLists(keyword), BorderLayout.EAST);
						pickEnumPanel.revalidate();
				}
				
		}

		public JList getLevelLists(Keyword keyword) {
					 Vector levelLists = keyword.getAssociatedLevelLists();
					 // CReate a list with the level lists in it
					 return new JList(levelLists);
		}
		public static void main(String[] args) {
				JFrame f = new JFrame();
				ProbabilityTablePanel p = new ProbabilityTablePanel();
				f.getContentPane().add(p);
				f.setSize(300,300);
				f.setVisible(true);
		}
		
		// Listen to the checkbox radio buttion for the default value row
		public void itemStateChanged(ItemEvent e) { 
				// When the user changes which row that should be the default right 
				// in the cell next to it  100 - Column Total
				// Blank out whichever row was the previous default row
				// if this is the item change to selected 
				if ( ((JRadioButton)e.getSource()).isSelected() ) {
					// 	if ( currentDefaultRow > -1)
// 								dataModel.setValueAt("", currentDefaultRow, percentageColumn);
// 						dataModel.setValueAt("100 - ColumnTotal", 
// 																 table.getEditingRow(), percentageColumn);
						currentDefaultRow = table.getEditingRow();
				}
				System.out.println("itemStateChanged probabilt defaultbutton pushed ");
		}
// 		 // Disable autoCreateColumnsFromModel
//     table.setAutoCreateColumnsFromModel(false);
    

    
   //  // Insert a new column at the end
//     insertColumn(table, "New Column", null, table.getColumnCount());
    
// 		public void showKeywordsWithLevels() {
// 				// Ask the user what enum and what level list
// 				pickEnumPanel = new JPanel(new BorderLayout());
// 				GenericTree keywordTree = 
// 						new GenericTree(TableCanvasPanel2.keywordNestedTreeModel, true);
// 				keywordTree.addTreeSelectionListener(this);
// 				//JPanel keywordTreePanel = new JPanel(new BorderLayout());

// 				JScrollPane scrollPane = new JScrollPane();
// 				scrollPane.setViewportView(keywordTree);
// 				pickEnumPanel.add(scrollPane, BorderLayout.CENTER);
// 				//keywordTreePanelkeywordTreePanel(, BorderLayout.EAST);
// 				JButton okButton = new JButton("OK");
// 				pickEnumPanel.add(okButton, BorderLayout.SOUTH);
// 				OncFrame f = new OncFrame(pickEnumPanel); 
// 			//"Please select Enum and Level List.");
// 				f.addFrameableComponent(pickEnumPanel);
// 				//insertColumn(table, "Default Label",
// 				f.setVisible(true);
										 
// 		}
		public void insertColumn() {
		}
		public void insertColumn(Keyword keyword, EnumLevelList levelList) {
				addColumn(table, keyword.toString(),
									levelList.getLevels().toArray());
				table.revalidate();
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
						//System.out.println("Drop it ProbabilityTablePanel" + t );
						if ( t.isDataFlavorSupported(Droppable.genericTreeNode) ) {
								evt.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
								Object transferableData = 
										t.getTransferData(Droppable.genericTreeNode);
								GenericTreeNode transferNode = 
										(GenericTreeNode)transferableData;
								if ( transferNode.getUserObject() instanceof Keyword) {
										// Make this an action !!!!!!
										BooleanExpression bool = 	
												BooleanExpression.dropKeywordCreateEnum
												((Keyword)transferNode.getUserObject(), false);		
										// Let interested parties know that 
										// a boolean expression was dropped
										fireCanvasObjectChanged(new CanvasObjectChangeEvent
																						(CanvasObjectChangeEvent.ADD,
																						 bool,
																						 this));											}
								evt.getDropTargetContext().dropComplete(true);
						}
						else if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
								evt.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
								String s = 
										(String)t.getTransferData(DataFlavor.stringFlavor);
								evt.getDropTargetContext().dropComplete(true);
								//process(s);
						} else {
								evt.rejectDrop();
						}
				} catch (IOException e) {
						evt.rejectDrop();
				} catch (UnsupportedFlavorException e) {
						evt.rejectDrop();
				}
		}
		private Vector addProbTableDefaultColumns(Object[][] tableValues) {
				Vector vec = oncotcap.util.CollectionHelper.arrayToVector(tableValues);
				System.out.println("HOw often do we add probtabledefaultcolumns");
				// Add the radio button as the first element of every row
				Iterator i = vec.iterator();
				while ( i.hasNext() ) {
						Vector row = (Vector)i.next();
						row.add(0, new JRadioButton());
				}
				return vec;
		}
		private void setRendersOnDefaultColumns() {
				// set render & editor
				table.setDefaultRenderer(EventDeclaration.class,
							new OncEventDefinitionRenderer(EventDeclaration.getAllEvents()));
				table.setDefaultEditor(EventDeclaration.class, 
															 new OncEventDefinitionEditor());
		}

    // Creates a new column at position vColIndex
    public void insertColumn(JTable table, Object headerLabel,
                             Object[] values, int vColIndex) {
        addColumn(table, headerLabel, values);
        table.moveColumn(table.getColumnCount()-1, vColIndex);
		}
    // This method adds a new column to table without reconstructing
    // all the other columns.
    public void addColumn(JTable table, Object headerLabel,
                                Object[] values) {
        DefaultTableModel model = (DefaultTableModel)table.getModel();
				TableColumn col = new TableColumn(model.getColumnCount());
    
        // Ensure that auto-create is off
				table.setAutoCreateColumnsFromModel(false); 
        if (table.getAutoCreateColumnsFromModel()) {
            throw new IllegalStateException();
        }
        col.setHeaderValue(headerLabel);
        table.addColumn(col);
        model.addColumn(headerLabel.toString(), values);
				int colPos = model.getColumnCount()-1;
				table.moveColumn(colPos-1,colPos);
				// try {
// 						table.print(JTable.PrintMode.NORMAL,
// 													 new MessageFormat(""),
// 													 new MessageFormat(""),
// 													 false,
// 													 new HashPrintRequestAttributeSet(),
// 													 false);
// 				}catch ( Exception ex) {
// 				}
    }

		public void showTablePopup(int column, MouseEvent evt) {
				tablePopup.setColumn(column);
				tablePopup.show(table, evt.getX(), evt.getY() );
		}

		public class TablePopup extends OncPopupMenu {
				int col = 0;
				public TablePopup() {
						JMenuItem mi;
						mi = new JMenuItem();
						InsertTableColumnAction insertAction = 
								new InsertTableColumnAction("Add Joint Probability");
						mi.setAction(insertAction);
						add(mi);
						mi = new JMenuItem();
						AddConditionalAction addConditionalAction = 
								new AddConditionalAction("Add Conditional Probability");
						mi.setAction(addConditionalAction);
						add(mi);
						setOpaque(true);
						setLightWeightPopupEnabled(true);
				}
				public void setColumn(int column) {
						col = column;
				}
		}




		public class InsertTableColumnAction extends AbstractAction {
				public InsertTableColumnAction(String name) {
						super(name);
				}
				public void actionPerformed(ActionEvent ae) {
						//System.out.println("INsertTableColumnACtion " + ae);
						insertColumn();
				}
		}


		public class AddConditionalAction extends AbstractAction {
				public AddConditionalAction(String name) {
						super(name);
				}
				public void actionPerformed(ActionEvent ae) {
						//System.out.println("AddConditionalAction " + ae);
						insertColumn();
				}
		}
    public class TableListener extends MouseAdapter {
				public void mouseClicked(MouseEvent evt) {
						if ( evt.isPopupTrigger() 
								 || evt.getButton() == MouseEvent.BUTTON3) {
								//System.out.println("Mouse clicked on table " );
								// Determine where the mouse is on the table 
								int col = table.columnAtPoint(evt.getPoint());
								showTablePopup(col, evt);
						}
				}
		}
    
    public class ColumnHeaderListener extends MouseAdapter {
        public void mouseClicked(MouseEvent evt) {
						//System.out.println("Mouse clicked on header " );
            JTable table = ((JTableHeader)evt.getSource()).getTable();
            TableColumnModel colModel = table.getColumnModel();
    
            // The index of the column whose header was clicked
            int vColIndex = colModel.getColumnIndexAtX(evt.getX());
            int mColIndex = table.convertColumnIndexToModel(vColIndex);
    
            // Return if not clicked on any column header
            if (vColIndex == -1) {
                return;
            }
						
						if ( evt.isPopupTrigger() || evt.getButton() == MouseEvent.BUTTON3) {
								headerPopupMenu.show(table, evt.getX(), evt.getY());
						}
            // Determine if mouse was clicked between column heads
            Rectangle headerRect = table.getTableHeader().getHeaderRect(vColIndex);
            if (vColIndex == 0) {
                headerRect.width -= 3;    // Hard-coded constant
            } else {
                headerRect.grow(-3, 0);   // Hard-coded constant
            }
            if (!headerRect.contains(evt.getX(), evt.getY())) {
                // Mouse was clicked between column heads
                // vColIndex is the column head closest to the click
								// vLeftColIndex is the column head to the left of the click
                int vLeftColIndex = vColIndex;
                if (evt.getX() < headerRect.x) {
                    vLeftColIndex--;
                }
            }
        }
    }



		public class MyComboBoxRenderer extends JComboBox implements TableCellRenderer {
        public MyComboBoxRenderer(String[] items) {
            super(items);
        }
				
        public Component getTableCellRendererComponent(JTable table, Object value,
																											 boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                super.setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }
						
            // Select the current value
            setSelectedItem(value);
            return this;
        }
    }
    
    public class MyComboBoxEditor extends DefaultCellEditor {
        public MyComboBoxEditor(String[] items) {
            super(new JComboBox(items));
        }
				
		}
}

