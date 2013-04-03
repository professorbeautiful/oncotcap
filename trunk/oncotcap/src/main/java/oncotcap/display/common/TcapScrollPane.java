package oncotcap.display.common;

import javax.swing.JScrollPane;
import java.awt.Component;

public class TcapScrollPane extends JScrollPane
{

	public TcapScrollPane()
	{
		super();
	}
	public TcapScrollPane(Component view)
	{
		super(view);
	}
	public TcapScrollPane(Component view, int vsbPolicy, int hsbPolicy)
	{
		super(view, vsbPolicy, hsbPolicy);
	}
	public TcapScrollPane(int vsbPolicy, int hsbPolicy)
	{
		super(vsbPolicy, hsbPolicy);
	}

	public void scrollToBottom()
	{
		verticalScrollBar.setValue(verticalScrollBar.getMaximum());
	}

	public int getHorizontalScrollBarWidth()
	{
		return(horizontalScrollBar.getWidth());
	}

	public int getVerticalScrollBarWidth()
	{
		return(verticalScrollBar.getWidth());
	}

	public int getVerticalUnitIncrement(int direction)
	{
		return(verticalScrollBar.getUnitIncrement(direction));
	}
	public int getVerticalBlockIncrement(int direction)
	{
		return(verticalScrollBar.getBlockIncrement(direction));
	}
	public int getVerticalMinimum()
	{
		return(verticalScrollBar.getMinimum());
	}
	public int getVerticalMaximum()
	{
		return(verticalScrollBar.getMaximum());
	}
	public int getVerticalValue()
	{
		return(verticalScrollBar.getValue());
	}
	public void setVerticalUnitIncrement(int inc)
	{
		verticalScrollBar.setUnitIncrement(inc);
	}
	public void setVerticalBlockIncrement(int block)
	{
		verticalScrollBar.setBlockIncrement(block);
	}
	public void setVerticalMinimum(int min)
	{
		verticalScrollBar.setMinimum(min);
	}
	public void setVerticalMaximum(int max)
	{
		verticalScrollBar.setMaximum(max);
	}
	public void setVerticalValue(int val)
	{
		verticalScrollBar.setValue(val);
	}
}
