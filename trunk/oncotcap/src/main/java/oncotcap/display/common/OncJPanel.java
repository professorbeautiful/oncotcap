package oncotcap.display.common;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;

/**
 *	  Subclass of JPanel.  Overrides the addFocusListener, adds the
 *	  FocusListener to all Components of the panel.
 **/
public class OncJPanel extends JPanel
{
	public void addFocusListener(FocusListener l)
	{
		super.addFocusListener(l);
		addFocusListenerToAllSubComponents(l);
	}
	private void addFocusListenerToAllSubComponents(FocusListener l)
	{
		addFocusListenerToAllSubComponents(this, l);
	}
	private static void addFocusListenerToAllSubComponents(Container cont, FocusListener l)
	{
		java.awt.Component [] comps = cont.getComponents();
		for(int n = 0; n<comps.length; n++)
		{
			comps[n].addFocusListener(l);
			if(comps[n] instanceof Container)
				addFocusListenerToAllSubComponents((Container)comps[n], l);
		}		
	}
	public Component add(Component comp)
	{
		addExistingFocusListeners(comp);
		return(super.add(comp));
	}
	
	public Component add(Component comp, int index)
	{
		addExistingFocusListeners(comp);
		return(super.add(comp, index));
	}
	public void add(Component comp, Object constraints)
	{
		addExistingFocusListeners(comp);
		super.add(comp, constraints);
	}
	public void add(Component comp, Object constraints, int index)
	{
		addExistingFocusListeners(comp);
		super.add(comp, constraints, index);
	}
	public Component add(String name, Component comp)
	{
		addExistingFocusListeners(comp);
		return(super.add(name, comp));

	}
	private void addExistingFocusListeners(Component comp)
	{
		FocusListener [] listeners = getFocusListeners();
		for(int n=0; n<listeners.length; n++)
			comp.addFocusListener(listeners[n]);
	}
}