package oncotcap.display.common;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import oncotcap.util.*;

public class HyperLabel extends JLabel implements MouseListener
{
	private Color txtColor = null;
	private Icon mouseOverIcon = null;
	private Icon normalIcon = null;
	protected HyperListener currentHyperListener = null;

	public HyperLabel()
	{
		super();
		init();
	}
	public HyperLabel(String text)
	{
		super(text);
		init();
	}
	public HyperLabel(Icon image)
	{
		super(image);
		normalIcon = image;
		init();
	}
	public HyperLabel(String text, int horizontalAlignment)
	{
		super(text, horizontalAlignment);
		init();
	}
	public HyperLabel(String text, Icon image, int horizontalAlignment)
	{
		super(text, image, horizontalAlignment);
		init();
	}
	protected void resetColor()
	{
		if(txtColor != null)
			setForeground(txtColor);
	}
	protected void setHyperFeature(boolean feature)
	{
		if(feature && ! isListenerInstalled())
			addMouseListener(this);

		if(!feature && isListenerInstalled())
			removeMouseListener(this);
	}
	public void setIcon(Icon icon)
	{
		super.setIcon(icon);
		normalIcon = icon;
	}
	public void setMouseOverIcon(Icon icon)
	{
		mouseOverIcon = icon;
	}
	private boolean isListenerInstalled()
	{
		MouseListener [] listeners = getMouseListeners();
		for(int n = 0; n < listeners.length; n++)
			if(listeners[n].equals(this))
				return(true);

		return(false);
	}
	private void init()
	{
		addMouseListener(this);
		setForeground(TcapColor.blue);
	}

	public void mouseClicked(MouseEvent e){}
	public void mousePressed(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	public void mouseEntered(MouseEvent e)
	{
		if(txtColor == null)
			txtColor = getForeground();
		setForeground(TcapColor.red);
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		super.setIcon(mouseOverIcon);
			
	}
	public void mouseExited(MouseEvent e)
	{
		resetColor();
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		super.setIcon(normalIcon);
	}
	public void addHyperListener(HyperListener listener)
	{
		currentHyperListener = listener;
		addMouseListener(listener);
	}
	public void removeHyperListener(HyperListener listener)
	{
		removeMouseListener(listener);
	}
}