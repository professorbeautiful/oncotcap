package oncotcap.display.browser;

import javax.swing.*;
import java.util.*;
import javax.swing.table.*;
import javax.swing.border.*;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Color;

public class OncModelTableModel extends DefaultTableModel {
		Vector editableColumns = null;
   
		public OncModelTableModel(Vector columnNames, int rowCount) {
				super(columnNames, rowCount);
		}
		public OncModelTableModel(Vector data,
														 Vector columnNames) {
				super(data, columnNames);
		}
		public Class getColumnClass(int columnIndex) {
				//return getValueAt(0, c).getClass();
				Object o = getValueAt(0, columnIndex);
				if (o == null) {
						return Object.class;
				} else {
						return o.getClass();
				}
		}

		public void setEditableColumns(Vector editableCols){
				editableColumns = editableCols;
		}
		public boolean isCellEditable(int rowIndex, int mColIndex) {
				if ( editableColumns != null )
            return editableColumns.contains(new Integer(mColIndex));
				else 
						return true;
		}

}
