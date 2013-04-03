package oncotcap.display.common;

import oncotcap.util.*;

public class OncEventTableModel extends javax.swing.table.AbstractTableModel
{
	private SortedList data = new SortedList(new EventTimeCompare());
	private double timeOnTrial = 0.0;
	
	public int getColumnCount() { return(3); };

	public int getRowCount()
	{
		return( data.size() );
	}

	public Object getValueAt(int row, int col)
	{
		switch(col)
		{
			case 0:
			{
				return(StringHelper.convertTime(((OncEvent) data.get(row)).getTime() - timeOnTrial));
			}
			case 1:
			{
				return(((OncEvent) data.get(row)).getEventType());
			}
			case 2:
			{
				return(((OncEvent) data.get(row)).toString());
			}
			default:
				Logger.log("Invalid column number in Table Model");

			return(null);

		}
	}
	

	public void add(OncEvent event)
	{
		data.add(event);
	}

	public void clear()
	{
		data.clear();
		timeOnTrial = 0;
	}

	public void setTimeOnTrial(double time)
	{
		timeOnTrial = time;
	}

	public double getTimeOnTrial()
	{
		return(timeOnTrial);
	}
}
