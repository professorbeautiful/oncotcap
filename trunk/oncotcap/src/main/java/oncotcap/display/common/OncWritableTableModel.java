package oncotcap.display.common;

import javax.swing.*;
import java.util.*;
import javax.swing.table.*;
import javax.swing.border.*;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Color;

public class OncWritableTableModel extends DefaultTableModel {
		Vector editableColumns = null;

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

		public Object clone() {
				try {
						OncWritableTableModel clonedModel = new OncWritableTableModel();
						Vector newDataVector = (Vector)((Vector)getDataVector()).clone();
						Vector newColumnNames = new Vector(columnIdentifiers);
						clonedModel.setDataVector(newDataVector, newColumnNames);
						return clonedModel;
				}
				catch (Exception e) {
						throw new InternalError(e.toString());
				}
		}
		
}
