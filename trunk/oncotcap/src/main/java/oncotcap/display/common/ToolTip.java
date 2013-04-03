package oncotcap.display.common;

import java.awt.*;
import javax.swing.*;

public class ToolTip extends Window
{

	public static void main(String [] args)
	{
		JFrame jf = new JFrame();
		jf.setSize(500,500);
		jf.setLocation(100,100);
		ToolTip tip = new ToolTip(jf,"BLECH");
		tip.setLocation(100,100);
		jf.setVisible(true);
		tip.setVisible(true);
	}

	private JLabel txtTip = new JLabel();
	private Frame parentFrame;
	public ToolTip(Frame parent)
	{
		super(parent);
		this.parentFrame = parent;
		init();
	}
	public ToolTip(Frame parentFrame, String tip)
	{
		super(parentFrame);
		this.parentFrame = parentFrame;
		init();
		setTip(tip);
	}
	private void init()
	{
		setBackground(Color.magenta);
		txtTip.setBackground(Color.magenta);
		txtTip.setForeground(Color.white);
		add(txtTip);
	}
	public void setTip(String tip)
	{
		txtTip.setText(tip);
		txtTip.setSize(txtTip.getPreferredSize());
		setSize(txtTip.getPreferredSize());
	}	
}