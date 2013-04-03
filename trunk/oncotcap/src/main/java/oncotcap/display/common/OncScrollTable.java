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
package oncotcap.display.common;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.beans.*;
import java.io.*;

import java.lang.reflect.*;
import java.net.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import javax.accessibility.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.colorchooser.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;

import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.text.*;
import javax.swing.tree.*;
import javax.swing.table.*;


import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.CodeBundle;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.util.ReflectionHelper;
import oncotcap.display.browser.OncBrowser;

public class OncScrollTable extends OncUiObject
		implements	 DropTargetListener,
								 DragGestureListener,
								 TableModelListener,
								 ListSelectionListener {
		int selectedRow = -1;
		Vector sortedItems = null;
		Vector tableObjects = new Vector();
		Collection visibleColumns = null;
		boolean allowsMultiples = true;
		boolean sortItems = true;
		JTable table = null;
		OncScrollTableModel tableModel = null;
		String [] testList = {"Item 1", "Item 2", "Item 3"};
		boolean showButtons = false;
		boolean showLabel = false;
		String label = "Default Label";		
		String fieldName = "fieldName";
		JScrollPane scrollPane = null;
		OncScrollListListener listener = new OncScrollListListener();
		Object editObject = null;
		Class listType = null;
		Collection linkTos = null;
		OncScrollTableButtonPanel buttonPanel = null;
		RemoveSelectedFromList removeSelected = 
				new RemoveSelectedFromList("Remove From List");
		Toolkit kit = Toolkit.getDefaultToolkit();
		final Clipboard clipboard =
				kit.getSystemClipboard();
		int call = 0;

		// set up comparator
		oncotcap.display.browser.BrowserNodeComparator comparator = 
				new oncotcap.display.browser.BrowserNodeComparator();

		public OncScrollTable(boolean showButtons, 
												 boolean showLabel) {
				this.showButtons = showButtons;
				this.showLabel = showLabel;
				init();
		}

		public OncScrollTable(Class listType, 
												 boolean showButtons, 
												 boolean showLabel) {
				this.showButtons = showButtons;
				this.showLabel = showLabel;
				this.listType = listType;
				init();
		}

		public OncScrollTable(Class listType, 
												 boolean showButtons, 
												 boolean showLabel,
												 boolean allowsMultiples) {
				this.showButtons = showButtons;
				this.showLabel = showLabel;
				this.listType = listType;
				this.allowsMultiples = allowsMultiples;
				init();
		}

		public OncScrollTable(Collection items, boolean showButtons, boolean showLabel) {
				this.showButtons = showButtons;
				this.showLabel = showLabel;
				init();
				setData(items);
		}


		public OncScrollTable(Collection items, 
												 String label,
												 boolean showButtons, 
												 boolean showLabel) {
				this.showButtons = showButtons;
				this.showLabel = showLabel;
				this.label = label;
				init();
				setData(items);
		}

		public OncScrollTable(Class listType, 
												 Object editObj, 
												 String label,
												 String fieldName,
												 boolean showButtons, 
												 boolean showLabel) {
				this.showButtons = showButtons;
				this.showLabel = showLabel;
				this.label = label;
				this.fieldName = fieldName;
				this.listType = listType;
				this.editObject = editObj;
				init();
		}

		public OncScrollTable(Class listType, 
												 Object editObj, 
												 String label,
												 boolean showButtons, 
												 boolean showLabel,
												 boolean allowsMultiples) {
				this.showButtons = showButtons;
				this.showLabel = showLabel;
				this.label = label;
				this.listType = listType;
				this.editObject = editObj;
				this.allowsMultiples = allowsMultiples;
				init();
		}


		public OncScrollTable(Object editObj, 
												 String label,
												 boolean showButtons, 
												 boolean showLabel) {
				this.showButtons = showButtons;
				this.showLabel = showLabel;
				this.label = label;
				this.editObject = editObj;
				init();
		}

		private void init() {
				try {
						setInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT,
												OncBrowser.getDefaultInputMap());
						setInputMap(JComponent.WHEN_FOCUSED,
												OncBrowser.getDefaultInputMap());
						setActionMap(OncBrowser.getDefaultActionMap());
						JPanel topPanel = new JPanel();
						GridBagLayout g = new GridBagLayout();
						setLayout(g);
						GridBagConstraints c3 = new GridBagConstraints();
						//g.columnWeights = new double[]{0.0f, 1.0f};
						c3.fill = GridBagConstraints.BOTH;
						c3.anchor = GridBagConstraints.SOUTHEAST;
						c3.insets = new Insets(0,0,0,0); //t,l,b,r
						c3.ipadx = 0;
						c3.ipady = 0;
						c3.weightx = 1;

						JLabel l = new JLabel(label);
						l.setFont(new Font("Helvetica", Font.BOLD, 11));
						if ( showLabel ) {
								add(l, c3);
						}
						if ( showButtons ) {
								buttonPanel = 
										new OncScrollTableButtonPanel(this);
								c3.weightx = 0;
								c3.anchor = GridBagConstraints.SOUTHEAST;
								c3.gridwidth = GridBagConstraints.REMAINDER;
								add(buttonPanel,c3);
						}
						// 				if ( showButtons || showLabel ) 
						// 						add(topPanel, BorderLayout.NORTH);
						Method getter = null;
						if ( editObject != null &&  fieldName != null && 
								 editObject instanceof oncotcap.datalayer.AutoGenPersistibleWithKeywords) {
								// Fill the table with data from the editObject
								System.out.println("fieldName " + fieldName);
								getter = 
										((oncotcap.datalayer.AutoGenPersistibleWithKeywords)editObject).getGetMethod(fieldName);
								Object returnObject = null;										

								if ( getter != null) {
										returnObject = ReflectionHelper.invoke(getter,
																													 editObject,
																													 null);
								}
								if ( returnObject instanceof Collection ) {
										tableModel = new OncScrollTableModel
												(new Vector((Collection)returnObject), 
												 this,
												 editObject);
										table = new JTable(tableModel);	
								}
								else { // empty table
										table = new JTable();
										//setData(new Vector());
								}
						}
						else { // empty table
								table = new JTable();
								setData(new Vector());
						}
						
						table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
						table.setSelectionBackground(new Color(175,177,159));
						table.setSelectionForeground(Color.black);
						
						
						if ( allowsMultiples ) {
								scrollPane = new JScrollPane(table);
						}
						else {
								scrollPane = new JScrollPane(table);
								scrollPane.setVerticalScrollBarPolicy
										(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
								scrollPane.setHorizontalScrollBarPolicy
										(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
						}
						table.addMouseListener(listener);
						ListSelectionModel rowSM = table.getSelectionModel();
						rowSM.addListSelectionListener(this);
						//table.setCellRenderer(new OncListCellRenderer());
						new DropTarget(table, 
													 DnDConstants.ACTION_COPY_OR_MOVE,
													 this);
						c3.weightx = 1;
						c3.weighty = 1;
						c3.gridwidth = GridBagConstraints.REMAINDER;
						c3.anchor = GridBagConstraints.NORTHWEST;
						add(scrollPane, c3);
				} catch (Exception e) {
						e.printStackTrace();
				}
				
		}

		public void removeListener() {
				table.removeMouseListener(listener);
				//table.removeListSelectionListener(listener);
				
		}
		public void addListener(EventListener listener ) {
				table.addMouseListener((MouseListener)listener);
				//list.addListSelectionListener((ListSelectionListener)listener);
				
		}
		public Vector sort(Collection collection) {
				Object[] objs = collection.toArray();
			 Arrays.sort(objs, comparator);
			 return oncotcap.util.CollectionHelper.arrayToVector(objs);
		}
	  public void sortItems(boolean sortItems) {
				this.sortItems = sortItems;
		}	
		public void setColumns(Collection listOfVisibleColumns) {
				visibleColumns = listOfVisibleColumns;
		}

		public void setData(Collection items) {
			// 	System.out.println("setData " + items +
// 													 " table model " + tableModel);
				//	oncotcap.util.ForceStackTrace.showStackTrace();

				int [] selectedRows = null;
				Vector selectedItems = null;
				if ( tableModel == null ) {
						if ( items.size() > 0) {
								tableModel = new OncScrollTableModel(new Vector(items), 
																										 this, editObject);
								table.setModel(tableModel);
								selectedItems = new Vector();
						}
				}
				else {
						// Capture what is selected so they can be reset later
						selectedRows = table.getSelectedRows();
						selectedItems = tableModel.getObjectsAt(selectedRows);
				}
				//Make sure the column names are refreshed
				//tableModel.setColumnNames(new Vector(items));
				table.setModel(tableModel);

				//resetSelected(selectedItems);
				table.revalidate();
		}

		public int getSelectedRow(){
				return selectedRow;
		}
		// ListSelectionListener
		public void valueChanged(ListSelectionEvent e) {
				// List changed
				selectedRow = table.getSelectedRow();
		}

		private void resetSelected(Vector selectedItems){
				if ( selectedItems == null ) 
						return;
				Iterator i = selectedItems.iterator();
				while ( i.hasNext()) {
						table.setRowSelectionInterval(((Integer)i.next()).intValue(),
																		((Integer)i.next()).intValue());
				}
		}


		public void setLinkTos(Collection linkTos) {
				// 	System.out.println("setLinkTos: " + linkTos + " in table " + getList()
				// 													 + " for list type " + getListType());
				this.linkTos = linkTos;
		}

		public Collection getLinkTos() {
				return linkTos;
		}

		public Object getLinkTo() {
				if (linkTos != null )
						return (linkTos.toArray())[0];
				else 
						return null;
		}
		public Object getData(int row) {
				//	System.out.println("getData " + row);
				if ( tableObjects.size() > row)
						return tableObjects.elementAt(row);
				return null;
		}
		
		public Object getDataObject(int row) {
				if ( tableModel != null ) 
						return tableModel.getObjectAt(row); 
				return null;
		}
		public Collection getData() {
				if ( tableModel != null ) {
						//System.out.println("getData " + tableModel.getData());
						return tableModel.getData();
				}
				else 
						return new Vector();
		}

		public Vector getSelectedItems() {
				int[] selectedRows = table.getSelectedRows();
				Vector selectedValues = new Vector();
				for ( int i = 0; i < Array.getLength(selectedRows); i++) {
						selectedValues.addElement(getData(i));
				}	
				return selectedValues;
		}

		public Object getSelectedItem() {
				int selectedRow = table.getSelectedRow();
				return getData(selectedRow);
		}

		public void deleteSelected() {
				((OncScrollTableModel)getTableModel()).deleteSelected();
				
		}
		public Class getListType() {
				return listType;
		}
		
		public boolean allowsMultiples() {
				return this.allowsMultiples;
		}


		public String getLabel() {
				return label;
		}

		public Object getValue(){
				if ( allowsMultiples ) 
						return getData();
				else {
						// Get the single element from the vector and return that
						return getData(0);
				}
		}

		public void setValue(Object obj){
				if ( obj instanceof Collection ) 
						setData((Collection)obj);
				else {
						Vector v = new Vector();
						v.addElement(obj);
						setData(v);
				}
		}		
		public void addValue(Object obj){
				// Get all the existing values 
	
				Vector v = new Vector(getData());				
				if ( !v.contains(obj) ){
					// 	if ( obj instanceof Persistible) 
// 								((Persistible)obj).addSaveListener(this);
						v.addElement(obj);
				}
				setData(v);
		}		

		public void removeValue(Object obj){
				// Get all the existing values 
				Vector v = new Vector(getData());				
				if ( v.contains(obj) ) {
						v.remove(obj);
				}
				setData(v);
		}		

		public OncScrollTableModel getTableModel(){
				return (OncScrollTableModel)table.getModel();
		}		

		public Object getEditedObject() {
				return editObject;
		}

		public JTable getTable(){
				return table;
		}	
		public void clear() {
				TableModel tableModel = table.getModel();
				if ( tableModel instanceof OncScrollTableModel) 
						((OncScrollTableModel)tableModel).clear();
				table.setModel(tableModel);
		}
		private void clearSelection() {
				table.clearSelection();
		}

    // Find a string in  tree
    public int findString(String searchWord) {
				int currentPosition = 0; //getTable().getMaxSelectionIndex(); 
				if ( currentPosition < 0 ) 
						currentPosition = -1;
        return findString(currentPosition+1, searchWord);
    }

		public int findString(int position, 
													String searchWord) {
				// Loop through the table and return the position
				// of the table row that contains the search word
	// 			TableModel model = getTableModel();
// 				for ( int i = position; i < model.getRowCount(); i++) {
// 						String tableWord = model.getElementAt(i).toString();
// 							if ( tableWord!= null && 
// 								 tableWord.toLowerCase().indexOf
// 									 (searchWord.toLowerCase()) > -1 ) {
// 								// match found
// 								getTable().setSelectedIndex(i);
// 								return i;
// 						}
// 				}
// 				clearSelection();
				return -1;
		}

		public String toString() {
				if (label != null ) 
						return label;
				return getClass().toString();
		}

		public static void main(String[] args) {
				JFrame f = new JFrame();
				Collection codeBundles =
						oncotcap.Oncotcap.getDataSource().find(CodeBundle.class);
				OncScrollTable p = new OncScrollTable(codeBundles, true, true);
				f.getContentPane().add(p);
				f.addWindowListener
						(	new WindowAdapter() {
										public void windowClosing(WindowEvent e) {
												System.exit(0);
										}
								});
				f.setSize(500,500);
				f.setVisible(true);
		}

		public void refresh() {
				revalidate();
				table.repaint();
		}

		public void tableChanged(TableModelEvent e) {
		}

		// Drop target
		public void dragEnter(DropTargetDragEvent dtde) {}
		public void dragExit(DropTargetEvent dte) {}
		public void dragOver(DropTargetDragEvent dtde) {}
		public void drop(DropTargetDropEvent dtde) {
				Point pt = dtde.getLocation();
				try {
						Transferable t = clipboard.getContents(clipboard);
						DataFlavor stringFlavor = DataFlavor.stringFlavor;
						Transferable tr = dtde.getTransferable();
						// 	System.out.println("Drop " 
						// 															 + t + " -- " + tr);
						DataFlavor flavors[] = t.getTransferDataFlavors();

						if(tr.isDataFlavorSupported(Droppable.droppableData))
								{
										Object droppedData = 
												(Droppable) tr.getTransferData(Droppable.droppableData);
						// 				System.out.println("dropped " + droppedData + " - " 
// 																			 + droppedData.getClass());	
										addValue(droppedData);

								}

				} catch(Exception ufe) {
						ufe.printStackTrace();
				}
				
		}
		public void dropActionChanged(DropTargetDragEvent dtde) {}
		public void dragGestureRecognized(DragGestureEvent dge) {
 				//System.out.println("Drag Gesture: " + dge);
		}

		class RemoveSelectedFromList extends AbstractAction {
				public RemoveSelectedFromList(String actionName) {
						super(actionName);
				}
				public void actionPerformed(ActionEvent e) {
				// 		// What is selected
// 						Object [] selectedItems = table.getSelectedValues();
// 						// take them out the table
// 						for ( int i = 0; i < Array.getLength(selectedItems); i++) {
// 								removeValue(selectedItems[i]);
// 						}
				}
		}
}
