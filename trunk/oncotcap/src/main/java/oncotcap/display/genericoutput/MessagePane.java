package oncotcap.display.genericoutput;

import java.awt.*;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;

import oncotcap.display.common.DisplayMessage;
import oncotcap.display.common.DisplayMessageHelper;
import oncotcap.display.common.DisplayMessageListener;
import oncotcap.process.OncProcess;
import oncotcap.util.FontHelper;
import oncotcap.util.StringHelper;

public class MessagePane extends JPanel implements DisplayMessageListener
{
	public MessagePane(OncProcess process)
	{
		init(process);
	}
	private JTable messageTable;
	private MessageTableModel model;
	private void init(OncProcess process)
	{
		setLayout(new BorderLayout());
		model = new MessageTableModel();
		
		messageTable = new JTable(model);
		messageTable.getColumnModel().getColumn(0).setPreferredWidth(50);
		messageTable.getColumnModel().getColumn(1).setPreferredWidth(50);
		messageTable.getColumnModel().getColumn(2).setPreferredWidth(500);
		messageTable.setDefaultRenderer(Object.class, new MessageCellRenderer());
		JScrollPane scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); //messageTable, + JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getViewport().setLayout(new BorderLayout());
		scrollPane.getViewport().add(messageTable, BorderLayout.CENTER);
		add(scrollPane, BorderLayout.CENTER);
		DisplayMessageHelper.addDisplayMessageListener(process, this);
	}
	
	public void handleMessage(DisplayMessage message)
	{
		model.addMessage(message);
		messageTable.getColumnModel().getColumn(0).setPreferredWidth(50);
		messageTable.getColumnModel().getColumn(1).setPreferredWidth(50);
		messageTable.getColumnModel().getColumn(2).setPreferredWidth(500);
	}

	class MessageCellRenderer extends JLabel implements TableCellRenderer {
		public java.awt.Component getTableCellRendererComponent(JTable table, Object value,	boolean isSelected,	boolean hasFocus, int row,	int column) 
		{
			if(value != null)
			{
				if(value instanceof DisplayMessage)
				{
					DisplayMessage message = (DisplayMessage) value;
					this.setFont(message.getFont());
					setForeground(message.getForeground());
					if(column == 0)
						setText(StringHelper.convertTime(message.getTime()));
					else if(column == 1)
						setText(message.getName());
					else if(column == 2)
						setText(message.getMessage());
				}
				else
				{
					setFont(FontHelper.defaultFont);
					setText(value.toString());
				}
			}


			return this;
		}
	}
}
