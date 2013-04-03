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

import java.util.*;
import java.lang.reflect.*;
import javax.swing.*;
import javax.swing.table.*; 
import javax.swing.event.TableModelListener; 
import javax.swing.event.TableModelEvent; 
import oncotcap.util.*;
import oncotcap.datalayer.*;

public class OncScrollTableModel extends DefaultTableModel 
		implements TableModelListener,
							 SaveListener
{
		private JTable table = null;
		private Vector tableObjects = null;
		private Vector columnNames = null;
		private Vector columnClasses = new Vector();
		private Vector vecOfVectors  = null;  // vector representation of 
		private int numOfColumns = 0;
		private Object editObject = null;
		private OncScrollTable scrollTable = null;
		int selectedRow = -1;

		public OncScrollTableModel(Vector dataVector,
															 OncScrollTable scrollTable,
															 Object editObject) {
				tableObjects = dataVector;
				this.scrollTable = scrollTable;
				this.editObject = editObject;
				columnNames = new Vector();
				setColumnNames(dataVector);
				setData(dataVector);
				addTableModelListener(scrollTable);
				//setColumnClasses(dataVector);
		}

    public TableModel  getModel() {
        return this;
    }
		public void clear() {
				if ( vecOfVectors == null ) 
						vecOfVectors = new Vector();
				else
						vecOfVectors.clear();
				table.revalidate();
		}

		public void setData(Collection items) {
				if ( items == null ) {
						return;
				}
				Object obj = null;
				if ( vecOfVectors == null ) 
						vecOfVectors = new Vector();
				else {
						vecOfVectors.clear();
				}

				Iterator i = items.iterator();
				Vector vec = null;
				int rowCnt = 0;
				while ( i.hasNext()) {
						obj = i.next();
						if ( obj instanceof Persistible) 
								((Persistible)obj).addSaveListener(this);
						// Convert data into a vector of data
						vec = convertDataToVector(obj, rowCnt);
						vecOfVectors.addElement(vec);
						rowCnt++;
				}
				if ( vec != null )
						numOfColumns = vec.size();

		}

		public Vector getData() {
				return tableObjects;
		}
		public Vector convertDataToVector(Object objectToEdit, int row) {
				if ( !tableObjects.contains(objectToEdit) ) 
						tableObjects.addElement(objectToEdit);
				Vector vectorData = new Vector();
				// If the object is a auto gen persistible - go to work
				AutoGenEditable persistible = null;
				if ( objectToEdit instanceof AutoGenEditable ) {
						persistible = (AutoGenEditable)objectToEdit;
				}
				Hashtable getterMap = persistible.getGetterMap();
				if ( !(persistible instanceof Tableable) ){
 						return vectorData;
				}
				String columnNames[] = ((Tableable)persistible).getColumnsInOrder();
				for ( int i = 0; i < Array.getLength(columnNames); i++) {
						Method getter = (Method)getterMap.get(columnNames[i]);
						Object getReturn = 
								ReflectionHelper.invoke(getter, persistible, null);
						Class cls = editObject.getClass();
						if ( getReturn != null && getReturn.getClass().isAssignableFrom(cls) ) {
								//
						}
						else {
								if  ( row == 0 ) {  


										// capture column classes while processing 
										// 1st row of data
										columnClasses.addElement(Object.class);
								}
								vectorData.addElement(getReturn);
						}
				}
				return vectorData;
		}

		public void addData(Object obj) {
				Vector vec = null;
				if ( obj == null ) {
						return;
				}
				// If this is an empty table 
				if ( vecOfVectors == null ) 
						vecOfVectors = new Vector();
				int rowCnt = vecOfVectors.size();
				if ( obj instanceof Persistible) 
						((Persistible)obj).addSaveListener(this);
						// Convert data into a vector of data
						vec = convertDataToVector(obj, rowCnt);
						vecOfVectors.addElement(vec);

				if ( vec != null )
						numOfColumns = vec.size();

				// Make sure the column names are set if they aren't
				// the table will not show up
				if ( rowCnt == 0 ) 
						setColumnNames(obj);
		}

		public void setColumnNames(Vector data) {
				if ( data.size() == 0 ) {
						return;
				}
				Object obj = data.firstElement();
				setColumnNames(obj);
		}

		public void setColumnNames(Object obj) {
				if ( columnNames == null ) 
						columnNames = new Vector();
				else 
						columnNames.clear();
				if ( obj instanceof Tableable ) {
						Tableable tableable = (Tableable)obj;
						columnNames  = 
								CollectionHelper.arrayToVector
								(tableable.getColumnNamesInOrder());
				}
				else {
						for ( int j = 0; j < numOfColumns; j++) {
								columnNames.addElement(String.valueOf(j));
						}
				}
				table = scrollTable.getTable();
				for ( int  j = 0; j < numOfColumns; j++) {
						addColumn(columnNames.elementAt(j));
				}
		}

    public void  setModel(TableModel model) {
        addTableModelListener(this); 
    }

    // By default, Implement TableModel by forwarding all messages 
    // to the model. 

    public Object getValueAt(int aRow, int aColumn) {
				if ( vecOfVectors != null ) {
						Vector row = (Vector)vecOfVectors.elementAt(aRow);
						if ( aColumn < row.size())
							return row.elementAt(aColumn);
						else 
							return null;
				}
				else 
						return null;
    }
	
    public void setValueAt(Object aValue, int aRow, int aColumn) {
				if ( vecOfVectors != null ) {
						Vector row = (Vector)vecOfVectors.elementAt(aRow);
						row.setElementAt(aValue, aColumn);
				}
    }

    public int getRowCount() {
        return (vecOfVectors == null) ? 0 : vecOfVectors.size(); 
    }

    public int getColumnCount() {
        return numOfColumns;
    }
	
    public String getColumnName(int aColumn) {
        return (String)columnNames.elementAt(aColumn); 
    }

    public Class getColumnClass(int aColumn) {
        return (Class)columnClasses.elementAt(aColumn); 
    }
	
    public boolean isCellEditable(int row, int column) { 
         return false; 
    }

		public Vector getObjectsAt(int[] rows) {
				Vector objs = new Vector();
				for ( int i = 0; i < Array.getLength(rows); i++) {
						objs.addElement(tableObjects.elementAt(rows[i]));
				}
				return objs;
		}
		public Object getObjectAt(int row) {
						return tableObjects.elementAt(row);
		}

    public void tableChanged(TableModelEvent e) {
        fireTableChanged(e);
    }

		public void objectSaved(SaveEvent e) {
				if ( !tableObjects.contains(e.getSavedObject()) ) 
						addData(e.getSavedObject());
				setData(tableObjects);
				fireTableChanged(new TableModelEvent(this));
		}

		public void objectDeleted(SaveEvent e) {
				fireTableChanged(new TableModelEvent(this));
		}
		public void deleteSelected() {
				int [] rows = table.getSelectedRows();
				Object obj = null;
				for ( int i = Array.getLength(rows)-1; i >= 0; i--) {
						// Delete object
						obj = tableObjects.elementAt(i);
					 	if ( obj instanceof Persistible) 
 								((Persistible)obj).delete();
						removeRow(rows[i]);
				}
				
		}
		public void removeRow(int row) {
				vecOfVectors.remove(row);
				tableObjects.remove(row);
		}
		public String toString() {
				StringBuffer toString = new StringBuffer();
				if ( tableObjects != null) 
						toString.append(tableObjects.toString());
				toString.append(" , Vector of Vectord -->>");
				if ( vecOfVectors != null ) 
						toString.append(vecOfVectors.toString());
				toString.append(" , Column Names -->>");
				if ( columnNames != null ) 
						toString.append(columnNames.toString());
				return new String(toString);
				
		}
}
