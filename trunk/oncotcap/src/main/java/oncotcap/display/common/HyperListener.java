package oncotcap.display.common;

import java.awt.event.*;

/**
 ** HyperListener is a convinience class provided to catch mouse
 ** clicks for HyperLabels.
 **
 ** A mouseClicked(MouseEvent e) method needs to be provided.
 **/
public abstract class HyperListener implements MouseListener
{
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {mouseActivated(e);}

	public abstract void mouseActivated(MouseEvent e);
} //end class ClickToAddParameter
