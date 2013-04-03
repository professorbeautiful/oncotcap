package oncotcap.display.common;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import oncotcap.datalayer.persistible.*;


public class OncEventDefinitionEditor extends DefaultCellEditor {
																							//implements ItemListener { 
		public JComboBox comboBox = new JComboBox(EventDeclaration.getAllEvents());
		public ItemListener buttonListener;
		
		public OncEventDefinitionEditor() { 
				super(new JComboBox(EventDeclaration.getAllEvents()));
				//super(comboBox);
				//buttonListener = itemListener;
		}
		
// 		public Component getTableCellEditorComponent(JTable table, 
// 																								 Object value,  
// 																								 boolean isSelected, 
// 																								 int row, 
// 																								 int column) { 
// // 				if ( value == null ) {
// // 						System.out.println("value is null in editor");
// // 						return new JComboBox(EventDefinition.getAllEvents());
// // 				}
// // 				else if ( (value instanceof EventDefinition ) 
// // 									&& (((EventDefinition)value).getName() != null) ) {
// // 						System.out.println("value is in editor"  + value + " " + value.getClass());

// // 						// return the combo box with the selected value displayed
// // 						JComboBox cBox = new JComboBox(EventDefinition.getAllEvents());
// // 						cBox.setSelectedItem(value);
// // 						return cBox;
// // 				}
// // 				System.out.println("value is in editor"  + value + " " + value.getClass());

// 				return new JComboBox(EventDefinition.getAllEvents());
// 		}

// // 		public Object getCellEditorValue() { 
// // 				//comboBox.removeItemListener(this);
// // 				System.out.println("what is the value" + comboBox.getSelectedItem());
// // 				return comboBox.getSelectedItem();
// // 		}
		
// // 		public void itemStateChanged(ItemEvent e) { 
// // 				System.out.println("what is the deal combobox " );
// // 				super.fireEditingStopped();
// // 		}
}
