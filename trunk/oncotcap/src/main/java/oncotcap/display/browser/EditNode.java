package oncotcap.display.browser;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class EditNode extends AbstractAction {

		GenericTree tree = null;
		Object source = null;
		static Dimension defaultSize = new Dimension(600,400);

		public EditNode() {
				super("Edit");
				
		}
		public void actionPerformed(ActionEvent e) {
				Object obj = null;
				JTable table  = null;
				JList list = null;
				if ( getSource() instanceof OncScrollList) {
						list = ((OncScrollList)getSource()).getList();
						// select item
						obj = list.getSelectedValue();
					// 	System.out.println("edit selected value " 
// 															 + ((OncScrollList)getSource()).getValue());
				}
				else if ( getSource() instanceof OncScrollTable) {
						int selectedRow = ((OncScrollTable)getSource()).getSelectedRow();
						obj = ((OncScrollTable)getSource()).getDataObject(selectedRow);
				}
				if ( obj instanceof Editable) {
						EditorFrame.showEditor((Editable)obj, defaultSize);
				}
				else if ( obj instanceof String ) {
						// Make a label
						String label = new String("Please enter string value");
						String inputValue = 
								JOptionPane.showInputDialog(label, obj); 
						if ( inputValue != null ) {
								DefaultListModel model = (DefaultListModel)list.getModel();
								model.set(list.getSelectedIndex(), inputValue);
								list.setModel(model);
								list.revalidate();
						}
						else 
								System.out.println("How did you get to add a string");
				}
		}

		public void setTree(GenericTree tree) {
				this.tree = tree;
		}
		public GenericTree getTree() {
				return this.tree;
		}
		public void setSource(Object source) {
				this.source = source;
		}
		public Object getSource() {
				return this.source;
		}

		public void setSourceParent(Object source) {
				this.source = source;
		}
		public Object getSourceParent() {
				return this.source;
		}

}
