package oncotcap.display.browser;

import javax.swing.*;
import javax.swing.table.*;

import java.awt.Dimension;
import java.awt.event.*;
import java.awt.Color;
import java.applet.*;
import java.awt.*;
import java.io.*;

import java.util.*;
import java.text.ParseException;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import oncotcap.display.common.*;
import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.action.OncAction;
import oncotcap.datalayer.persistible.StatementBundle;
import oncotcap.datalayer.persistible.StatementTemplate;
import oncotcap.datalayer.persistible.CodeBundle;

public class OncModelTable extends JTable {
		public static LineBorder lineBorder1 = 
				new LineBorder(OncBrowserConstants.EColorDarkest, 2);
		public static LineBorder lineBorder3 = 
				new LineBorder(Color.gray,2);
		public static LineBorder lineBorder2 = 
				new LineBorder(OncBrowserConstants.KAColorDarkest,2);
		public static LineBorder lineBorder4 = 
				new LineBorder(OncBrowserConstants.MBColorDarkest,2);
		public static LineBorder lineBorder5 = 
				new LineBorder(OncBrowserConstants.CBColorDarkest,2);
		public static EmptyBorder emptyBorder = 
				new EmptyBorder(new Insets(0,0,0,0));
		int numColumns = 0;
		public OncModelTable(TableSorter dataModel) {
				super(dataModel);
				init();
		}
    public OncModelTable(OncModelTableModel dataModel) {
				super(dataModel);
				init();
		}
		public void init() {
				setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
				setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				//Ask to be notified of selection changes.
				ListSelectionModel rowSM = getSelectionModel();
				OncScrollListListener selectionListener = new OncScrollListListener();
				rowSM.addListSelectionListener(selectionListener);
				addMouseListener(selectionListener);
				setRowHeight(20);
        // Install a mouse listener in the TableHeader as the sorter UI.
        //dataModel.addMouseListenerToHeaderInTable(this);

				OncModelTableCellRenderer cellRenderer =
						new OncModelTableCellRenderer();
				setDefaultRenderer(StatementBundle.class, cellRenderer);
				setDefaultRenderer(CodeBundle.class, cellRenderer);
				setDefaultRenderer(StatementTemplate.class, cellRenderer);
				setDefaultRenderer(OncAction.class, cellRenderer);
				setDefaultRenderer(JButton.class, cellRenderer);
				setDefaultRenderer(String.class, cellRenderer);
				setDefaultRenderer(Object.class, cellRenderer);
				numColumns = getColumnCount();
    }

		public void setColumnWidths() {
				if (getColumnCount() > 7 ) {
						getColumnModel().getColumn(0).setPreferredWidth(20);
						getColumnModel().getColumn(1).setPreferredWidth(175);
						getColumnModel().getColumn(2).setPreferredWidth(100);
						getColumnModel().getColumn(3).setPreferredWidth(175);
						getColumnModel().getColumn(4).setPreferredWidth(75);
						getColumnModel().getColumn(5).setPreferredWidth(100);
						getColumnModel().getColumn(6).setPreferredWidth(30);
						getColumnModel().getColumn(7).setPreferredWidth(30);
						getColumnModel().getColumn(8).setPreferredWidth(30);
				}
		}

		public void tableChanged(TableModelEvent e) {
				super.tableChanged(e);
		}
    public static void main(String[] args) {
        //new OncModelTable();
    }
		// This table displays a tool tip text based on the string
    // representation of the cell value
	// 	public Component prepareRenderer(TableCellRenderer renderer,
// 																		 int rowIndex, int vColIndex) {
// 				Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
// 				if (c instanceof JComponent) {
// 						JComponent jc = (JComponent)c;
// 						jc.setToolTipText((String)getValueAt(rowIndex, vColIndex));
// 				}
// 				return c;
// 		}
		class OncModelTableCellRenderer extends JLabel 
				implements TableCellRenderer {
				JButton nextButton = new JButton("Next");

				public Component getTableCellRendererComponent(JTable table,
																								Object value,
																								boolean isSelected,
																								boolean hasFocus,
																								int row,
																								int column) {
						// If the value is a persistible item use the Display String
						String s = "";
						// Get the currently selected cell
						// if the value in this cell is the same as the selected
						// cell make the foreground green
						int r = table.getSelectedRow();
						int c = table.getSelectedColumn();
						setIcon(null);
						if (value != null ) {
								//System.out.println(" render " + value.getClass());
								if ( value instanceof AbstractPersistible ) {
										s = ((AbstractPersistible)value).toDisplayString();
										setIcon(((AbstractPersistible)value).getIcon());
								}
								else if (value instanceof Class) {
										// Strip the package name 
										s = "New " + oncotcap.util.StringHelper.className(value.toString());
								}
								else if ( value instanceof JButton ){
										return (JButton)value;
								}
								else {
										s = value.toString();
								}
						}
						setText(s);
						setBorder(new LineBorder(new Color(219,215,215)));
						if (isSelected) {
								setBackground(table.getSelectionBackground());
								setForeground(table.getSelectionForeground());
						}
						else {
								setBackground(table.getBackground());
								setForeground(table.getForeground());
						}
						setEnabled(table.isEnabled());
						setCellSelectionEnabled(true);
						setFont(table.getFont());
						setOpaque(true);
						// if ( value instanceof DependencyUserObject ) {
// 								Color color = ((DependencyUserObject)value).getHighlightColor();
// 								setForeground(color);
// 						}
						if ( value instanceof AMethod ) {
								setForeground(OncBrowserConstants.EColorDarkest);
						}
						else if ( value instanceof Variable ) {
								setForeground(OncBrowserConstants.KAColorDarkest);

						}
						else if (value instanceof AnEvent ) {
								setForeground(OncBrowserConstants.MBColorDarkest);
						}
						else if ( value instanceof AClass ) {
								setForeground(OncBrowserConstants.CBColorDarkest);
						}
						

						Object selectedObj = null;
						
						if ( c != 0
								 && column != 0
								 && r > -1 && c > -1 && (row != r || column != c)) {
								selectedObj = 
										table.getModel().getValueAt(r, c);
								setBorder(getRelationshipBorder(value, selectedObj));
													//setBackground(getRelationshipColor(value, selectedObj));
						}

						
						if ( value != null && value.toString() != null ) 
								setToolTipText(value.toString());
						if ( row == r ) {
								if ( column == c ) {
										setBorder(lineBorder3);
								}
								setBackground(table.getSelectionBackground()); //Color.gray);
						}
						return this;
				}
		}
		Color getRelationshipColor(Object currentObject, Object selectedObject) {
				if ( selectedObject == null || currentObject == null) 
						return Color.WHITE;
				
				if ( currentObject.equals(selectedObject) ) 
						return OncBrowserConstants.MBColorPale;
				if ( selectedObject instanceof DependencyNoun 
						 && currentObject instanceof DependencyNoun ) {
						// THey are This and that columns
						if ( selectedObject instanceof AnEvent ) { 
								if ( currentObject instanceof AMethod  && 
										 ((AMethod)currentObject).getObjectName().equals(((DependencyNoun)selectedObject).getObjectName()) )
										return OncBrowserConstants.KCColorPale;
								if ( currentObject instanceof AnEvent && 
										 ((AnEvent)currentObject).getObjectName().equals(((DependencyNoun)selectedObject).getObjectName()) )
										return OncBrowserConstants.KCColorPale;
						}
						if ( selectedObject instanceof AClass ) { 
								if ( currentObject instanceof AMethod  && 
										 ((AMethod)currentObject).getClassName().equals(((DependencyNoun)selectedObject).getClassName())
										 && ((AMethod)currentObject).getObjectName().equals("init") )
										return OncBrowserConstants.KCColorPale;
								if ( currentObject instanceof AClass  && 
										 ((AClass)currentObject).getClassName().equals(((DependencyNoun)selectedObject).getClassName())
										 && ((AClass)currentObject).getObjectName().equals("init") )
										return OncBrowserConstants.KCColorPale;
						}
				}
				return Color.WHITE;
			// 	if ( value instanceof Variable)
// 						System.out.println("What is value " + ((Variable)value).getClassName());
		}

		Border getRelationshipBorder(Object currentObject, Object selectedObject) {
				if ( selectedObject == null || currentObject == null) 
						return emptyBorder;
				
				if ( currentObject.equals(selectedObject) ) 
						return lineBorder1; //OncBrowserConstants.MBColorPale;
				if ( selectedObject instanceof DependencyNoun 
						 && currentObject instanceof DependencyNoun ) {
						// THey are This and that columns
						if ( selectedObject instanceof AnEvent ) { 
								if ( currentObject instanceof AMethod  && 
										 ((AMethod)currentObject).getObjectName().equals(((DependencyNoun)selectedObject).getObjectName()) )
										return lineBorder2; //OncBrowserConstants.KCColorPale;
								if ( currentObject instanceof AnEvent && 
										 ((AnEvent)currentObject).getObjectName().equals(((DependencyNoun)selectedObject).getObjectName()) )
										return lineBorder2; //OncBrowserConstants.KCColorPale;
						}
						if ( selectedObject instanceof AClass ) { 
								if ( currentObject instanceof AMethod  && 
										 ((AMethod)currentObject).getClassName().equals(((DependencyNoun)selectedObject).getClassName())
										 && ((AMethod)currentObject).getObjectName().equals("init") )
										return lineBorder2; //OncBrowserConstants.KCColorPale;
								if ( currentObject instanceof AClass  && 
										 ((AClass)currentObject).getClassName().equals(((DependencyNoun)selectedObject).getClassName())
										 && ((AClass)currentObject).getObjectName().equals("init") )
										return lineBorder2; //OncBrowserConstants.KCColorPale;
						}
				}
				return emptyBorder; //Color.WHITE;
		}
}
/*// This table displays a tool tip text based on the string
    // representation of the cell value
    JTable table = new JTable() {
        public Component prepareRenderer(TableCellRenderer renderer,
                                         int rowIndex, int vColIndex) {
            Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
            if (c instanceof JComponent) {
                JComponent jc = (JComponent)c;
                jc.setToolTipText((String)getValueAt(rowIndex, vColIndex));
            }
            return c;
        }
    };
*/
/*								// Since sometimes this is a Class and sometimes an EVent
								// Search for substrings when looking at a column other
								// than the column of the selected item - this will allow 
								// us to highlight all classes that listen to a particular
								// event and show all methods that come from a selected 
								// class that has just been instantiated
								if ( value != null ) {
										String valueString = value.toString();
										if ( selectedObj != null ) {
												if ( valueString.indexOf(".") > -1 ) {
														if ( selectedObj.equals(valueString) ) {
																//setForeground(Color.RED);
														setBackground(Color.YELLOW);
														}
												}
												// No class / method / event separator
												else {
														if ( valueString.indexOf(selectedObj.toString()) > -1 )
																setBackground(Color.ORANGE);
												}
*/

// setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
// 			if (!isSelected && table.isCellEditable(row, column)) {
// 				Color col;
// 				col = UIManager.getColor("Table.focusCellForeground");
// 				if (col != null) {
// 					super.setForeground(col);
// 				}
// 				col = UIManager.getColor("Table.focusCellBackground");
// 				if (col != null) {
// 					super.setBackground(col);
// 				}
// 			}
// 		} else {
// 			setBorder(new EmptyBorder(1, 1, 1, 1));
// 		}
