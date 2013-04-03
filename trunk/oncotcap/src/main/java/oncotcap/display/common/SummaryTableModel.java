package oncotcap.display.common;

import java.util.*;

class SummaryTableModel extends javax.swing.table.AbstractTableModel
{
	private ArrayList columns = new ArrayList();
	
	public int getRowCount()
	{
		return( 1 );
	}
	public int getColumnCount()
	{
		return( columns.size() );
	}

	public Object getValueAt(int row, int col)
	{
		if (row != 0)
			return(null);

		if (col >= columns.size())
			return(new Integer(0));

		Integer ir = (Integer) (columns.get(col));
		return( ir );
	}

	int addColumn()
	{
		columns.add(new Integer(0));
		return(columns.size() - 1);
	}
	void incrementColumn(int col)
	{
		int val = ((Integer) (columns.get(col))).intValue();
		val++;
		Object ret = columns.set(col, new Integer(val));
	}

	void reset()
	{
		int n;
		Object ret;
		for(n=0; n<columns.size(); n++)
			ret = columns.set(n, new Integer(0));
	}

	void clear()
	{
		columns.clear();
	}
}
