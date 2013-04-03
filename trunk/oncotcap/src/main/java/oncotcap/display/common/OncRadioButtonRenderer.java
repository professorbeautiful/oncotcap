package oncotcap.display.common;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;

public class OncRadioButtonRenderer implements TableCellRenderer { 
		public Component getTableCellRendererComponent(JTable table, 
																									 Object value,
																									 boolean isSelected, 
																									 boolean hasFocus, 
																									 int row, 
																									 int column) { 
				if (value==null || !(value instanceof Component)) 
						return null;
				return (Component)value;
		}
} 
