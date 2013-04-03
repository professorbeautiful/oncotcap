package oncotcap.display.common;

import javax.swing.*;
import javax.swing.table.*;
import java.util.*;
import java.awt.*;

public class SummaryTable extends JPanel
{

	private OncTable table;
	private SummaryTableModel data = new SummaryTableModel();
	private int nColumns = 0;
	private int width=200;
	private int height;
	private ArrayList colTypes = new ArrayList();
	private JScrollPane scrollPane;
	private boolean followTableWidth = false;
	private int cols = -1;

	public static void main(String [] args)
	{
		JFrame jf = new JFrame();
		jf.setSize(300,300);
		SummaryTable st = new SummaryTable(jf);
		jf.getContentPane().add(st);
		HeaderInfo hInfo = new HeaderInfo(" Test Information ", "");
		st.addColumn("Test Column", 120, 1, hInfo);
		st.addColumn("Test column 2", 60, 1, hInfo);
		jf.setVisible(true);
	}
	public SummaryTable(Frame parentFrame)
	{
		setLocation(0,0);
		table = new OncTable(data, parentFrame);
		table.getTableHeader().setReorderingAllowed(false);
//		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
//		table.sizeColumnsToFit(false);
		scrollPane = new JScrollPane(table);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		add(scrollPane);
		setVisible(true);
	}

	public void addColumn(String title, int width, int eventType, HeaderInfo info)
	{
		TableColumn cm = new TableColumn();
		cm.setMinWidth(width);
		cm.setMaxWidth(width);
		cm.setPreferredWidth(width);
		cm.setHeaderValue(title);
		cm.setCellRenderer(new SummaryCellRenderer());
		cols++;
		cm.setModelIndex(cols);
		table.addColumn(cm);
		table.addHeaderInfo(cm, info);
		int colnum = data.addColumn();
		colTypes.add(new Integer(eventType));
		setSize();
	}

	public void addEvent(oncotcap.util.OncEvent ev)
	{
		int i;
		if ( (i = colTypes.indexOf(new Integer(ev.getIntEventType()))) >= 0 )
			data.incrementColumn(i);
		
		revalidateAndRepaint();
	}
	
	public void clear()
	{
		data.clear();
		colTypes.clear();
		revalidateAndRepaint();
	}
	public void reset()
	{
		data.reset();
		revalidateAndRepaint();
	}
	public void revalidateAndRepaint()
	{
		table.revalidate();
		table.repaint();
		scrollPane.revalidate();
		scrollPane.repaint();
	}

	public void setWidth(int width)
	{
		this.width = width;
		setSize();
	}

	public void setSize(int width, int height)
	{
		//height is ignored here, it is always 2 rows high
		this.width = width;
		setSize();
	}

	private void setSize()
	{
		TableColumnModel tcm = table.getColumnModel();
		height = table.getRowHeight() + (int) table.getTableHeader().getPreferredSize().getHeight() + 7;
		int tableWidth = 0;
		int n;
		for (n=0;n<table.getColumnCount();n++)
			tableWidth = tableWidth + tcm.getColumn(n).getWidth();
		
		table.setSize(tableWidth,height);
		table.setPreferredSize(new Dimension(tableWidth, height));

		if (followTableWidth)
		{
			scrollPane.setSize(tableWidth+3,height);
			scrollPane.setPreferredSize(new Dimension(tableWidth+3,height));
			super.setSize(tableWidth+5,height);
			setPreferredSize(new Dimension(tableWidth+5,height));
		}
		else
		{
			//if (tableWidth > width)	height = height + 18;
			scrollPane.setSize(Math.min(width,tableWidth+3),height);
			scrollPane.setPreferredSize(new Dimension(Math.min(width,tableWidth+3),height));
			super.setSize(width,height);
			setPreferredSize(new Dimension(width,height));
		}
		revalidateAndRepaint();
	}

	public void setFollowTableWidth(boolean follow)
	{
		followTableWidth = follow;
		setSize();
	}

	class SummaryCellRenderer extends JLabel implements TableCellRenderer
	{
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean cellHasFocus, int row, int column)
		{
			String s = value.toString();
			setText(s);
			if (isSelected)
			{
				setBackground(table.getSelectionBackground());
				setForeground(table.getSelectionForeground());
			}
			else
			{
				setBackground(table.getBackground());
				setForeground(table.getForeground());
			}
			setEnabled(table.isEnabled());
			setFont(table.getFont());
			setHorizontalAlignment(SwingConstants.CENTER);
			setVerticalAlignment(SwingConstants.CENTER);
			return this;
		}
	}
}