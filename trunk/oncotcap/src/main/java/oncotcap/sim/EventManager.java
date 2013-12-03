package oncotcap.sim;

import java.lang.reflect.*;
import java.util.*;
import oncotcap.*;
import oncotcap.sim.schedule.*;
import oncotcap.engine.EventHandler;
import oncotcap.process.*;
import oncotcap.sim.*;
import oncotcap.util.StringHelper;
public class EventManager
{
	private Hashtable events = new Hashtable();
	private Class [] eventArgs = {Object.class, EventParameters.class};
	private Object [] nullEventParameters = {null, null};
	private Vector<EventHandler> allEventEventHandlers = new Vector<EventHandler>();
	private EventManager thisEventManager;
	private MasterScheduler masterScheduler;
	private Object [] args;
	
	public EventManager(MasterScheduler sched)
	{
		thisEventManager = this;
		masterScheduler = sched;
		args = (Object []) Array.newInstance(Object.class, 2);
	}
	
	public String registerEvent(Object object, String eventName)
	{
		Vector listenerVec;
		Method meth = null;
		String methodName = StringHelper.className(eventName);
		try{meth = object.getClass().getDeclaredMethod(methodName, eventArgs);}
		catch(NoSuchMethodException e){System.out.println("Warning: method " + methodName + " does not exist in class " + object.getClass()); return("");}
		ListenerInfo lInfo = new ListenerInfo(object, meth);
		if(events.containsKey(eventName))
		{
			listenerVec = ((EventInfo) events.get(eventName)).listeners;
		}
		else
		{
			EventInfo evInfo = new EventInfo();
			events.put(eventName, evInfo);
			listenerVec = evInfo.listeners;
		}
		listenerVec.add(lInfo);
		return(eventName);
	}
	public void removeEvent(Object object, String eventName)
	{
		if(events.containsKey(eventName))
		{
			Vector listeners = ((EventInfo) events.get(eventName)).listeners;
			Iterator it = listeners.iterator();
			while(it.hasNext())
			{
				ListenerInfo li = (ListenerInfo) it.next();
				if(li.listener == object)
				{
					listeners.remove(li);
					break;
				}
			}
			if(listeners.size() == 0)
				events.remove(eventName);
		}
	}
	public void registerForAllEvents(EventHandler handler)
	{
		if(! allEventEventHandlers.contains(handler))
			allEventEventHandlers.add(handler);
	}
	public void unRegisterForAllEvents(EventHandler handler)
	{
		if(allEventEventHandlers.contains(handler))
			allEventEventHandlers.remove(handler);
	}
	public EventParameters blankEventParameters = new EventParameters();

	public void fireEvent(Object caller, String eventName, EventParameters eventParameters)
	{		
		EventParameters evParams;
		if(eventParameters == null)
			evParams = blankEventParameters;
		else
			evParams = eventParameters;
		
		//notify handlers that want notification for all events
		for(EventHandler handler : allEventEventHandlers)
			handler.handleEvent(eventName, caller, evParams);
		
		if(events.containsKey(eventName))
		{
			Vector listenerVec = ((EventInfo) events.get(eventName)).listeners;
			Iterator it = listenerVec.iterator();
			while(it.hasNext())
			{
				ListenerInfo li = (ListenerInfo) it.next();
				args[0] = caller;
				args[1] = evParams;
//				oncotcap.simtest.Cancer_cellCollection coll = (oncotcap.simtest.Cancer_cellCollection) li.listener;
//				coll.collection_update((OncProcess) caller, evParams);
				try{li.listenerMethod.invoke(li.listener, args);}
				catch(IllegalAccessException e){System.out.println("Illegal access exception invoking " + eventName + " on " + li.listener);}
				catch(InvocationTargetException e){System.out.println("Invocation target exception invoking " + eventName + " on " + li.listener); e.printStackTrace();}
			}
		}
	}
	public void endRecurrentEvents(String eventName)
	{
		if(events.contains(eventName))
		{
			Vector schedVec = ((EventInfo) events.get(eventName)).schedulers;
			Iterator it = schedVec.iterator();
			while(it.hasNext())
			{
				EventScheduler sched = (EventScheduler) it.next();
				sched.endRecurrentEvent();
			}
		}
	}
	public void clear()
	{
		events.clear();
		allEventEventHandlers.clear();
	}
	public class EventScheduler
	{
		private Object caller;
		private String eventName;
		private EventParameters eventParameters;
		private Class [] fireArgs = {};
		private Object [] emptyArgs = {};
		private Method myFireMethod;
		private Scheduler scheduler;
		private String methodName = null;
		
		public EventScheduler(double time, Object caller, String eventName, EventParameters eventParameters)
		{
			this.caller = caller;
			this.eventName = eventName;
			this.eventParameters = eventParameters;
			try{myFireMethod = EventScheduler.class.getDeclaredMethod("fireEvent", fireArgs);}
			catch(NoSuchMethodException e){System.out.println("Warning: can not find fireEvent method in EventManager.EventScheduler"); myFireMethod = null;}
			masterScheduler.installTriggerAbsolute(time, this, myFireMethod, emptyArgs);
		}
		public EventScheduler(double time, Object caller, String eventName, EventParameters eventParameters, double recurGap)
		{
			this.caller = caller;
			this.eventName = eventName;
			this.eventParameters = eventParameters;
			this.methodName = methodName;
			Vector schedVec;
			scheduler = new Scheduler(this, "fireEvent", masterScheduler);
			scheduler.addRecurrentEvent(time, recurGap);
			if(events.containsKey(eventName))
			{
				schedVec = ((EventInfo) events.get(eventName)).schedulers;
			}
			else
			{
				EventInfo evInfo = new EventInfo();
				events.put(eventName, evInfo);
				schedVec = evInfo.schedulers;				
			}			
			schedVec.add(this);
		}
		public void endRecurrentEvent()
		{
			if(scheduler != null)
				scheduler.endRecurrentEvent();
		}
		public void fireEvent()
		{
			thisEventManager.fireEvent(caller, eventName, eventParameters);
		}
	}
}
class EventInfo
{
	Vector listeners = new Vector();
	Vector schedulers = new Vector();
}
class ListenerInfo
{
	Object listener;
	Method listenerMethod;
	Object [] args = null;
	
	ListenerInfo(Object listener, Method listenerMethod)
	{
		this(listener, listenerMethod, null);
	}
	ListenerInfo(Object listener, Method listenerMethod, Object [] arguments)
	{
		this.listener = listener;
		this.listenerMethod = listenerMethod;
		this.args = arguments;
	}
}
