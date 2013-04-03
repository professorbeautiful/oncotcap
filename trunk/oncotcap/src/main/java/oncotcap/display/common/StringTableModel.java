package oncotcap.display.common;

import javax.swing.table.*;
import java.util.*;

public class StringTableModel extends AbstractTableModel
{
	ArrayList list = new ArrayList();

	StringTableModel()
	{
		list = new ArrayList();
	}
	StringTableModel(Vector inList)
	{
		list = new ArrayList(inList);
	}
	public int getRowCount()
	{
		return(list.size());
	}
	public int getColumnCount()
	{
		return(1);
	}
	public void add(String strAdd)
	{
			list.add(strAdd);
			fireTableRowsInserted(list.size() -1, list.size() -1);
	}
	public void remove(int row)
	{
		if(row >= 0 && row < list.size())
		{
			list.remove(row);
			fireTableRowsDeleted(row, row);
		}
	}
	public Object getValueAt(int row, int col)
	{
		return((String) list.get(row));
	}
	public boolean contains(String testString)
	{
		return list.contains(testString);
	}
}