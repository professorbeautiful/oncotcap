package oncotcap.process;

import java.util.*;
import java.lang.reflect.*;

import oncotcap.sim.EventManager;
import oncotcap.sim.schedule.*;
import oncotcap.util.*;

public abstract class OncCollection<T extends OncProcess>
{
	protected Vector<T> objs = new Vector<T>();
	protected double deltaT = 0.007;
	public OncParent oncParent = null;
	private Vector<CollectionListener> collectionListeners = new Vector<CollectionListener>();
	private MasterScheduler masterScheduler;
	private EventManager eventManager;
	
	public OncCollection(MasterScheduler sched, EventManager eventManager)
	{
		masterScheduler = sched;
		this.eventManager = eventManager;
	}
	public void init(){};
	public void collection_init(){};
	public MasterScheduler getMasterScheduler()
	{
		return(masterScheduler);
	}
	public EventManager getEventManager()
	{
		return(eventManager);
	}
	public void add(T o)
	{
//		if(!objs.contains(o))
//		{
			objs.add(o);
			fireCollectionChanged();
	//	}
	}

	public Class getType()
	{
		if(objs.size() > 0)
			return(objs.get(0).getClass());
		else
			return(null);
	}
	public double getDeltaT() {
		return deltaT;
	}
	public void setDeltaT(double t) {
		deltaT = t;
		//This should be overridden by subclasses, to do any updating,
		//such as CellCollection kinetics.
	}
	public void update(){}
	public void collection_update()
	{
		update();
		Iterator objIterator = objs.iterator();
        ArrayList justThis = new ArrayList();
		while (objIterator.hasNext())
		{
            justThis.add(objIterator.next());
		}
        objIterator = justThis.iterator();
        while(objIterator.hasNext())
        	((OncProcess) objIterator.next()).update();
	}

	public Iterator iterator()
	{
		return(objs.iterator());
	}
	public Collection<T> getProcesses()
	{
		return(objs);
	}
	public int getIndexOf(OncProcess proc)
	{
		return(objs.indexOf(proc));
	}
	public void installTrigger(double time, String methodName, MasterScheduler masterScheduler)
	{
		try{
			Method m1 = this.getClass().getMethod(methodName,  (Class []) null);
			masterScheduler.installTrigger(time, this, m1, null);
		}
		catch(NoSuchMethodException e){Logger.log ("ERROR: No Such Method " + methodName + " [installTrigger]");}

	}
	public void addCollectionListener(CollectionListener listener)
	{
		if(!collectionListeners.contains(listener))
			collectionListeners.add(listener);
	}
	public void removeCollectionListener(CollectionListener listener)
	{
		if(collectionListeners.contains(listener))
			collectionListeners.remove(listener);
	}
	public void fireCollectionChanged()
	{
		for(CollectionListener listener : collectionListeners)
			listener.collectionChanged(this);
	}

	public boolean isBelow(OncCollection coll)
	{
		for(OncProcess p : this.getProcesses())
		{
			if(p.isBelow(coll))
				return(true);
		}
		
		return(false);
	}
/*	public void installTrigger(double start, double end, double increment)
	{
		double t;
		try{
			Method m1 = this.getClass().getMethod("update", null);
			for (t = start; t <= end; t  = t + increment)
				MasterScheduler.installTrigger(t, this, m1);

		}
		catch(NoSuchMethodException e){Logger.log ("No Such Method");}
	}

	public double getScheduleOffset()
	{
		return(scheduleOffset);
	}
	public void setScheduleOffset(double offset)
	{
		this.scheduleOffset = offset;
	}

	public int getSerial(OncProcess o) throws ObjectNotFoundException
	{
		if(objs.contains(o))
			return(objs.indexOf(o));
		else
			throw(new ObjectNotFoundException());
	}
	*/
}
