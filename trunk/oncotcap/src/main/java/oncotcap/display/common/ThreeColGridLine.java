package oncotcap.display.common;

import java.awt.*;
import javax.swing.*;
import oncotcap.util.*;

public class ThreeColGridLine extends JPanel implements ListCellRenderer
{
	private JLabel col1 = new JLabel();
	private JLabel col2 = new JLabel();
	private JLabel col3 = new JLabel();

	private int width = 300;
	private int fixedColWidth = 50;
	private int height = 30;
	
	public ThreeColGridLine()
	{
		init("","","");
	}
	public ThreeColGridLine(String col1, String col2, String col3)
	{
		init(col1,col2,col3);
	}
	public ThreeColGridLine(OncEvent e)
	{
		init(Double.toString(e.getTime()),e.getEventType(),e.toString());
	}
	public ThreeColGridLine(int width, int height, int fixedColWidth)
	{
		this.width = width;
		this.height = height;
		this.fixedColWidth = fixedColWidth;
		init("","","");
	}
	private void init(String col1, String col2, String col3)
	{
		setLayout(null);
		this.col1.setText(col1.concat(" "));
		this.col2.setText(col2.concat(" "));
		this.col3.setText(" " + col3);
		this.col1.setHorizontalAlignment(SwingConstants.RIGHT);
		this.col2.setHorizontalAlignment(SwingConstants.RIGHT);
		this.col3.setHorizontalAlignment(SwingConstants.LEFT);
		add(this.col1);
		add(this.col2);
		add(this.col3);
		setSize();
		this.col1.setBorder(BorderFactory.createLineBorder(Color.black));
		this.col2.setBorder(BorderFactory.createLineBorder(Color.black));
		this.col3.setBorder(BorderFactory.createLineBorder(Color.black));
	}
	public void setCol1Text(String txt)
	{
		col1.setText(txt + " ");
	}
	public void setCol2Text(String txt)
	{
		col2.setText(txt + " ");
	}
	public void setCol3Text(String txt)
	{
		col3.setText(" " + txt);
	}
	private void setSize()
	{
		col1.setSize(fixedColWidth + 1, height);
		col2.setSize(fixedColWidth + 1, height);
		col3.setSize(width - 2 * fixedColWidth, height);
		col1.setLocation(0,0);
		col2.setLocation(fixedColWidth, 0);
		col3.setLocation(fixedColWidth * 2, 0);
		super.setSize(width, height);
	}

	public void setSize(int width, int height)
	{
		this.width = width;
		this.height = height;
		setSize();
	}

	public void setFixedColWidth(int width)
	{
		fixedColWidth = width;
		setSize();
	}

	public Component getListCellRendererComponent(	JList list,
													Object value,
													int index,
													boolean isSelected,
													boolean cellHasFocus  )
	{
		OncEvent oe = (OncEvent) value;

		setCol1Text(Double.toString(oe.getTime()));
		setCol2Text(oe.getEventType());
		setCol3Text(oe.toString());
		if (isSelected)
		{
			col1.setBackground(list.getSelectionBackground());
			col1.setForeground(list.getSelectionForeground());
			col2.setBackground(list.getSelectionBackground());
			col2.setForeground(list.getSelectionForeground());
			col3.setBackground(list.getSelectionBackground());
			col3.setForeground(list.getSelectionForeground());
		}
		else
		{
			col1.setBackground(list.getBackground());
			col1.setForeground(list.getForeground());
			col2.setBackground(list.getBackground());
			col2.setForeground(list.getForeground());
			col3.setBackground(list.getBackground());
			col3.setForeground(list.getForeground());
		}
		col1.setEnabled(list.isEnabled());
		col2.setEnabled(list.isEnabled());
		col3.setEnabled(list.isEnabled());
		col1.setFont(list.getFont());
		col2.setFont(list.getFont());
		col3.setFont(list.getFont());
		return this;
	}

	public Dimension getPreferredSize()
	{
		return(new Dimension(width,height));
	}

}