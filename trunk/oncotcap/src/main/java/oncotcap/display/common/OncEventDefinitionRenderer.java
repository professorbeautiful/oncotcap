package oncotcap.display.common;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class OncEventDefinitionRenderer extends JComboBox
		implements TableCellRenderer { 
		public OncEventDefinitionRenderer(Object[] items) {
				super(items);
		}
		
		public Component getTableCellRendererComponent(JTable table, 
																									 Object value,
																									 boolean isSelected, 
																									 boolean hasFocus, 
																									 int row, 
																									 int column) { 
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

				
// 				if ( value == null ) {
// 						System.out.println("value is null ");
// 						return this;
// 				}
// 				else if ( value instanceof EventDefinition 
// 									&& ((EventDefinition)value).getName() != null) {
// 						setText(((EventDefinition)value).getName());
// 						return this;
// 				}
// 				return this;
		}
} 
