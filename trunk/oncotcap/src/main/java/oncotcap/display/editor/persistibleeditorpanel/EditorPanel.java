package oncotcap.display.editor.persistibleeditorpanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import oncotcap.display.browser.OncBrowser;
import oncotcap.datalayer.Persistible;
import oncotcap.datalayer.SaveListener;
import oncotcap.datalayer.SaveEvent;
import oncotcap.display.editor.*;

public abstract class EditorPanel extends JPanel implements SaveListener
{
	private EPFocusListener focusListener = new EPFocusListener();
	private EPContainerListener containerListener = new EPContainerListener();
	private boolean saveToDataSource = true;
	private boolean focusSavingEnabled = false;
	private boolean containerListeningEnabled = false;
	private EditorFrame myFrame = null;
	
	protected EditorPanel()
	{
		addFocusListener(this);
		setPreferredSize(new Dimension(300,300));
		setInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT,
			    OncBrowser.getDefaultInputMap());
		setInputMap(JComponent.WHEN_FOCUSED,
			    OncBrowser.getDefaultInputMap());
		setActionMap(OncBrowser.getDefaultActionMap());

	}
	public abstract void edit(Object objectToEdit);
	public abstract void save();
	public abstract Object getValue();

	public void setSaveToDataSourceOnCreate(boolean saveToDataSource)
	{
		this.saveToDataSource = saveToDataSource;
	}
	public boolean getSaveToDataSourceOnCreate()
	{
		return(saveToDataSource);
	}
	public void saveObject()
	{
			//			System.out.println("saveObject " + this);
		if(focusSavingEnabled)
		{
			save();
		}
	}
	public void setFocusSavingEnabled(boolean focusSave)
	{
		if(focusSave)
			addFocusListener(this);

		focusSavingEnabled = focusSave;
	}
	private void addFocusListener(Component c)
	{
		if(c instanceof Container)
		{
			Component [] comps = ((Container) c).getComponents();
			for(int n = 0; n < comps.length; n++)
				addFocusListener(comps[n]);
			if ( containerListeningEnabled ) 
					addContainerListener((Container) c);
		}			
		if(! containsFocusListener(c))
			c.addFocusListener(focusListener);
	}
	private void removeFocusListener(Component c)
	{
		if(c instanceof Container)
		{
			Component [] comps = ((Container) c).getComponents();
			for(int n = 0; n < comps.length; n++)
				removeFocusListener(comps[n]);
			if ( containerListeningEnabled ) 
					removeContainerListener((Container) c);
		}
		if(containsFocusListener(c))
			c.removeFocusListener(focusListener);
	}
	private boolean containsFocusListener(Component c)
	{
		FocusListener [] listeners = c.getFocusListeners();
		for(int n = 0; n < listeners.length; n++)
			if(listeners[n].equals(focusListener))
				return(true);

		return(false);
	}
	private void addContainerListener(Container c)
	{
		if(! containsContainerListener(c))
			c.addContainerListener(containerListener);
	}
	private void removeContainerListener(Container c)
	{
		if(containsContainerListener(c))
			c.removeContainerListener(containerListener);
	}
	private boolean containsContainerListener(Container c)
	{
		ContainerListener [] listeners = c.getContainerListeners();
		for(int n = 0; n < listeners.length; n++)
			if(listeners[n].equals(containerListener))
				return(true);

		return(false);
	}
	public void setMyFrame(EditorFrame frame)
	{
		myFrame = frame;
	}
	public EditorFrame getMyFrame()
	{
		return(myFrame);
	}

		// SaveListener
		public void objectSaved(SaveEvent e) {
				// DO NOTHING 
				// Refresh the panel with the new persistible info
				// 	not necessary anymore - i don't think
				//if(e.getSavedObject() instanceof Editable)
				// 						edit((Editable)e.getSavedObject() );		
		}

		public void objectDeleted(SaveEvent e) {
				// TODO: Some how this needs to clean out the frame and 
				// close up or the delete function should bring this to forefront
				// and confirm that the user wants to delete it
		}

	private class EPFocusListener implements FocusListener
	{
		public void focusGained(FocusEvent e){}
		public void focusLost(FocusEvent e)
		{
				saveObject();
		}
	}
	private class EPContainerListener implements ContainerListener
	{
		public void componentAdded(ContainerEvent e)
		{
				//System.out.println("componentAdded " + e);
				//oncotcap.util.ForceStackTrace.showStackTrace();
			addFocusListener(e.getChild());
		}
		public void componentRemoved(ContainerEvent e)
		{
				//System.out.println("componentRemoved " + e);

			saveObject(); 
			removeFocusListener(e.getChild());
			//oncotcap.display.browser.OncBrowser.repaintIt(); 07.29.04 potential fix for 
			// double line in tree problem but this may be too heavy handed.
		}
	}
}
