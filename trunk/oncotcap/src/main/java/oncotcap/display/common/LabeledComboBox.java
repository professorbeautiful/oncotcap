package oncotcap.display.common;

import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class LabeledComboBox extends JPanel
{
	JLabel title;
	JComboBox comboBox;
	public LabeledComboBox()
	{
		comboBox = new JComboBox();
		init("");
	}
	public LabeledComboBox(String title)
	{
		comboBox = new JComboBox();
		init(title);
	}
	public LabeledComboBox(String title, ComboBoxModel aModel)
	{
		comboBox = new JComboBox(aModel);
		init(title);
	}
	public LabeledComboBox(String title, Object[] items)
	{
		comboBox = new JComboBox(items);
		init(title);
	}
	public LabeledComboBox(String title, Vector items)
	{
		comboBox = new JComboBox(items);
		init(title);
	}

	private void init(String strTitle)
	{
		title = new JLabel(strTitle);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		title.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		comboBox.setAlignmentX(JComboBox.LEFT_ALIGNMENT);
		add(title);
		add(comboBox);
	}
	public void setLabel(String label)
	{
		title.setText(label);
	}
	public void addActionListener(ActionListener listener)
	{
		comboBox.addActionListener(listener);
	}
	public void addItemListener(ItemListener listener)
	{
		comboBox.addItemListener(listener);
	}
	public ActionListener[] getActionListeners()
	{
		return(comboBox.getActionListeners());
	}
	public void removeActionListener(ActionListener listener)
	{
		comboBox.removeActionListener(listener);
	}
	public Object getSelectedItem()
	{
		return(comboBox.getSelectedItem());
	}
	public void setSelectedItem(Object item)
	{
		if(item != null)
			comboBox.setSelectedItem(item);
		else if(getItemCount() > 0)
			comboBox.setSelectedIndex(0);
	}
	public void setSelectedIndex(int index)
	{
		comboBox.setSelectedIndex(index);
	}
	public void addItem(Object item)
	{
		comboBox.addItem(item);
	}
	public void insertItemAt(Object item, int index)
	{
		comboBox.insertItemAt(item, index);
	}
	public int getItemCount()
	{
		return(comboBox.getItemCount());
	}
	public void setRenderer(ListCellRenderer renderer)
	{
		comboBox.setRenderer(renderer);
	}
	public void removeAllItems()
	{
		comboBox.removeAllItems();
	}
	public void removeItem(Object item)
	{
		comboBox.removeItem(item);
	}
	public void addItems(Collection items)
	{
		if(items != null)
		{
			Iterator it = items.iterator();
			while(it.hasNext())
				comboBox.addItem(it.next());
		}
	}
	public void clear()
	{
		setLabel("");
		DefaultComboBoxModel model = (DefaultComboBoxModel) comboBox.getModel();
		model.removeAllElements();
	}
}