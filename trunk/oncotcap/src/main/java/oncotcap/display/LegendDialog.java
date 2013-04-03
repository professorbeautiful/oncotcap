package oncotcap.display;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JRootPane;

import oncotcap.util.ScreenHelper;

public class LegendDialog extends JDialog implements LegendPanelListener
{
	private JFrame owner;
	private Container contentPane;
	private LegendPanel displayedLegend = null;
	
	public LegendDialog(JFrame owner) 
	{
		super(owner, false);
		this.owner = owner;
		contentPane = getRootPane().getContentPane();
		contentPane.setLayout(new BorderLayout());
		getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
		setUndecorated(false);
	}
	
	public void showLegend(LegendPanel panel)
	{
		if(displayedLegend != null)
		{
			contentPane.remove(displayedLegend);
			displayedLegend.removedFromDialog(this);
			displayedLegend.removeLegendPanelListener(this);
		}
		displayedLegend = panel;
		if(panel != null)
		{
			contentPane.add(displayedLegend, BorderLayout.CENTER);
			displayedLegend.addedToDialog(this);
			displayedLegend.addLegendPanelListener(this);
		}
	}
	
	public void setLocation()
	{
		Dimension screenDimensions = ScreenHelper.getScreenDim();
		Point ownerLocation = owner.getLocation();
		int ownerY = (int) ownerLocation.getY();
		int ownerHeight = owner.getHeight();
		//if there is room at the top of the screen put the legend there
		if(ownerY > getPreferredHeight())
			setLocation(new Point(0, ownerY - getPreferredHeight()));
		//otherwise put it below the owner or overlap the bottom of the owner
		//if there isn't enough room at the bottom of the screen 
		else
			setLocation(new Point(0, (int) Math.min(ownerY + ownerHeight, getScreenHeight() - getPreferredHeight())));
	}
	public void setSize()
	{
		this.setSize(getPreferredWidth(),getPreferredHeight());
	}
	private int getPreferredWidth()
	{
		if(displayedLegend != null)
		{
			if(displayedLegend.getWindowState() == LegendPanel.INVISIBLE)
				return(0);
			return(Math.min((int)displayedLegend.getPreferredSize().getWidth(), getScreenWidth()));
		}
		else
			return(0);
	}
	private int getPreferredHeight()
	{
		if(displayedLegend != null)
		{
			if(displayedLegend.getWindowState() == LegendPanel.INVISIBLE)
				return(0);
			return(Math.min((int) displayedLegend.getPreferredSize().getHeight(), getScreenHeight()));
		}
		else
			return(0);
	}
	private int getScreenWidth()
	{
		return((int) ScreenHelper.getScreenDim().getWidth());
	}
	private int getScreenHeight()
	{
		return((int) ScreenHelper.getScreenDim().getWidth());
	}
	public void setVisible(boolean visible)
	{
		if(visible && displayedLegend == null)
			super.setVisible(false);
		else if(visible && displayedLegend.getWindowState() == LegendPanel.INVISIBLE)
			super.setVisible(false);
		else if(visible)
		{
			setSize();
			super.setVisible(true);
		}
		else
			super.setVisible(false);
	}
	
	public void legendPanelResized(LegendPanel panel)
	{
		if(displayedLegend.equals(panel))
		{
			setSize();
			setVisible(true);
		}
	}
}
