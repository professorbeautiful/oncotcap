/*
 * @(#)TableMap.java	1.9 01/12/03
 *
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/** 
 * In a chain of data manipulators some behaviour is common. TableMap
 * provides most of this behavour and can be subclassed by filters
 * that only need to override a handful of specific methods. TableMap 
 * implements TableModel by routing all requests to its model, and
 * TableModelListener by routing all events to its listeners. Inserting 
 * a TableMap which has not been subclassed into a chain of table filters 
 * should have no effect.
 *
 * @version 1.9 12/03/01
 * @author Philip Milne */
package oncotcap.display.browser;

import javax.swing.table.*; 
import javax.swing.event.TableModelListener; 
import javax.swing.event.TableModelEvent; 

public class TableMap extends AbstractTableModel implements TableModelListener
{
    protected TableModel model; 

    public TableModel  getModel() {
        return model;
    }

    public void  setModel(TableModel model) {
        this.model = model; 
        model.addTableModelListener(this); 
    }

    // By default, Implement TableModel by forwarding all messages 
    // to the model. 

    public Object getValueAt(int aRow, int aColumn) {
        return model.getValueAt(aRow, aColumn); 
    }
	
    public void setValueAt(Object aValue, int aRow, int aColumn) {
        model.setValueAt(aValue, aRow, aColumn); 
    }

    public int getRowCount() {
        return (model == null) ? 0 : model.getRowCount(); 
    }

    public int getColumnCount() {
        return (model == null) ? 0 : model.getColumnCount(); 
    }
	
    public String getColumnName(int aColumn) {
        return model.getColumnName(aColumn); 
    }

    public Class getColumnClass(int aColumn) {
        return model.getColumnClass(aColumn); 
    }
	
    public boolean isCellEditable(int row, int column) { 
         return model.isCellEditable(row, column); 
    }
//
// Implementation of the TableModelListener interface, 
//

    // By default forward all events to all the listeners. 
    public void tableChanged(TableModelEvent e) {
				//System.out.println("tableChanged" + e.getSource());
				// 		oncModelTable.getColumnModel().getColumn(0).setPreferredWidth(200);
// 				oncModelTable.getColumnModel().getColumn(1).setPreferredWidth(100);
// 				oncModelTable.getColumnModel().getColumn(2).setPreferredWidth(200);
// 				oncModelTable.getColumnModel().getColumn(3).setPreferredWidth(100);
// 				oncModelTable.getColumnModel().getColumn(4).setPreferredWidth(30);
// 				oncModelTable.getColumnModel().getColumn(5).setPreferredWidth(30);
// 				oncModelTable.getColumnModel().getColumn(6).setPreferredWidth(30);

        fireTableChanged(e);
    }
}
