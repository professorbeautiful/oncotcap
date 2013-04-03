package oncotcap.util;

import java.util.Comparator;
import javax.swing.DefaultComboBoxModel;

public class SortedComboBoxModel extends DefaultComboBoxModel
{
	private Comparator comparator;
	public SortedComboBoxModel(Comparator comp)
	{
		comparator = comp;
	}
	
	public void insertElementAt(Object obj, int index)
	{
		throw(new OncotcapError("The method insertElement(Object, int) is not available for SortedComboBoxModel"));
	}
	
	public void addElement(Object elem)
	{
		boolean inserted = false;
		for(int i = 0; i < getSize(); i++)
		{
			if(comparator.compare(elem, getElementAt(i)) < 0)
			{
				super.insertElementAt(elem, i);
				inserted = true;
				break;
			}
		}
		if(!inserted)
			super.addElement(elem);
	}
}
