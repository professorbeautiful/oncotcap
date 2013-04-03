package oncotcap.datalayer.persistible;

import oncotcap.*;
import java.util.*;
import oncotcap.datalayer.*;
import oncotcap.util.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.engine.ClassSectionDeclaration;
import oncotcap.datalayer.persistible.TreeViewable;

public class EventDeclaration  extends AbstractPersistible implements TreeViewable, ClassSectionDeclaration
{
	private static Hashtable allOncEvents = null;
	public String name;
	public Boolean processEvent = new Boolean(false);
	static{initAllEvents();}
	public EventDeclaration(oncotcap.util.GUID guid){
		super(guid);
	}
	public EventDeclaration() {

		//addOncEvent(this);//I think this line will screw the
				    //dataacess stuff up...
	}
	public EventDeclaration(String name)
	{
		this(name, false);
	}
	public EventDeclaration(String name, boolean processEvent)
	{
		this.name = name;
		this.processEvent = new Boolean(processEvent);
		addOncEvent(this);
	}
	private static void initAllEvents()
	{
		if(allOncEvents == null)
		{
			allOncEvents = new Hashtable();
			Class clsOncEvent = ReflectionHelper.classForName("oncotcap.datalayer.persistible.EventDeclaration");
			OncoTCapDataSource dataSource = Oncotcap.getDataSource();
			Collection processes = dataSource.find(clsOncEvent);
			Iterator it = processes.iterator();
			while(it.hasNext())
			{
				EventDeclaration op = (EventDeclaration) it.next();
				addOncEvent(op);
			}
			processes = dataSource.find(SystemDefinedEventDeclaration.class);
			it = processes.iterator();
			while(it.hasNext())
			{
				EventDeclaration op = (EventDeclaration) it.next();
				addOncEvent(op);
			}

		}
	}
			
	private static void addOncEvent(EventDeclaration oncEvent)
	{
		if(oncEvent != null && oncEvent.name != null)
			allOncEvents.put(oncEvent.name, oncEvent);
	}
	public String getName()
	{
		return(name);
	}
	public void setName(String name)
	{
		if(this.name != null)
			allOncEvents.remove(this.name);
		this.name = name;
		addOncEvent(this);
	}
	public String toString()
	{
		return(getName());
	}
	public static Object [] getAllEvents()
	{
		return(allOncEvents.values().toArray());
	}
	public static boolean exists(String event)
	{
		if(event != null)
			return(allOncEvents.containsKey(event));
		else
			return(false);
	}
	public static boolean exists(EventDeclaration event)
	{
		if(event != null)
			return(exists(event.getName()));
		else
			return(false);
	}
	public boolean isProcessEvent()
	{
		if(processEvent == null)
			return(false);
		else
			return(processEvent.booleanValue());
	}

	public String getDisplayString()
	{
		return toString();
	}

	public EditorPanel getEditorPanel()
	{
		return new OncProcessEditorPanel();
	}

	public EditorPanel getEditorPanelWithInstance()
	{
			//OncEventEditorPanel opPanel = new OncEventEditorPanel();
			//opPanel.edit(this);
			return null;
	}
	public boolean equals(Object obj)
	{
		if( ! (obj instanceof EventDeclaration))
			return(false);
		else
		{
			EventDeclaration comp = (EventDeclaration) obj;
			return(comp.name.equalsIgnoreCase(name));
		}
	}

	public int hashCode()
	{
		return(name.toUpperCase().hashCode());
	}
}
