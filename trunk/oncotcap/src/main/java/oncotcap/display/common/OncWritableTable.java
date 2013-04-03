package oncotcap.display.common;

import javax.swing.*;
import javax.swing.table.*;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Color;

public class OncWritableTable extends JTable {

    public OncWritableTable(OncWritableTableModel dataModel) {
				super(dataModel);
				setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        // Set a tooltip for the header of the colors column.
				// TableCellRenderer headerRenderer = colorColumn.getHeaderRenderer();
				// if (headerRenderer instanceof DefaultTableCellRenderer)
				// 		((DefaultTableCellRenderer)headerRenderer).setToolTipText("Hi Mom!");
				
				//setDefaultRenderer(Boolean.class, new ColorTableCellRenderer());
    }
		
    public static void main(String[] args) {
        //new OncWritableTable();
    }
}
