package oncotcap.display.genericoutput;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import oncotcap.display.common.DisplayMessage;
import oncotcap.util.SortedList;
import oncotcap.util.StringHelper;

public class MessageTableModel extends DefaultTableModel
{
	private static final long serialVersionUID = 3939292329l;
	private SortedList<DisplayMessage> messages = new SortedList<DisplayMessage>(new DisplayMessageComparator());
	public MessageTableModel()
	{
		addColumn("Time");
		addColumn("Message Name");
		addColumn("Message");
	}
	public void addMessage(DisplayMessage message)
	{
		if(message != null)
		{
			
			int nRows = getRowCount();
			setRowCount(nRows + 1);
			for(int n = 0 ; n <=2 ; n++)
				setValueAt(message, nRows, n);
			this.fireTableRowsInserted(nRows - 1, nRows);
		}
	}
	
}
