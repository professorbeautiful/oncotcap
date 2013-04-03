package oncotcap.display.common;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;

public class OncRadioButtonEditor extends DefaultCellEditor
		implements ItemListener { 
		public JRadioButton button;
		public ItemListener buttonListener;

		public OncRadioButtonEditor(JCheckBox checkBox, ItemListener itemListener) { 
				super(checkBox);
				buttonListener = itemListener;
		}

		public Component getTableCellEditorComponent(JTable table, 
																								 Object value,  
																								 boolean isSelected, 
																								 int row, 
																								 int column) { 
				if (value==null) 
						return null;
				button = (JRadioButton)value;
				button.removeItemListener(buttonListener);
				button.addItemListener(buttonListener);
				//button.addItemListener(this);
				return (Component)value;
		}

		public Object getCellEditorValue() { 
				button.removeItemListener(this);
				return button;
		}
		
		public void itemStateChanged(ItemEvent e) { 
				System.out.println("what is the deal " );
				super.fireEditingStopped();
		}
}
