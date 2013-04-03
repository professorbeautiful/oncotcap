/*
 * Created on Oct 31, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package oncotcap.process;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import oncotcap.util.*;
import oncotcap.sim.EventManager;
import oncotcap.sim.OncReporter;
import oncotcap.sim.random.OncRandom;
import oncotcap.sim.schedule.MasterScheduler;
/**
 * @author shirey
 *
 */
public abstract class AbstractOncParent implements OncParent {

	private Long seed = null;
	private Hashtable childCollections = new Hashtable();
	private ImmutableHashtable<Class, OncCollection> immutableChildCollections = null;
	private Hashtable collectionTypes = new Hashtable();
	private Vector<ParentListener> collectionListeners = new Vector<ParentListener>();
	private OncParent parent;
	private boolean timeKeptLocally = false;
	private double creationTime = 0.0;
	private Class rngClass;
	private static final Class [] rngArg = new Class [] {long.class};
	
	public void addChild(OncProcess obj)
	{
		OncCollection coll;

		if(!childCollections.containsKey(obj.getClass()))
		{
			addCollection(obj.getClass(), (coll = obj.newCollectionInstance()));
			coll.installTrigger(MasterScheduler.NOW, "collection_init", getMasterScheduler());
		}
		else
			coll = (OncCollection) childCollections.get(obj.getClass());

		coll.add(obj);	
	}
	public void setSeed(long seed)
	{
		this.seed = new Long(seed);
	}
	public int getSerial(OncProcess o)
	{
		return(getCollection(o).getIndexOf(o));
	}

	private void addCollection(Class objectType, OncCollection coll)
	{
		childCollections.put(objectType, coll);

		//keep an index that cross references all classes inherited by
		//the object that is held in this collection (includes interfaces)
		Class [] interfaces = objectType.getInterfaces();
		Vector supers = ReflectionHelper.getSupers(objectType);
		supers.add(objectType);
		for(int i = 0; i < interfaces.length; i++)
			addCollectionType(interfaces[i], objectType);

		Iterator it = supers.iterator();
		while(it.hasNext())
			addCollectionType((Class) it.next(), objectType);
		
		fireCollectionChanged();
	}
	//used only by addCollection to maintain the index created there
	private void addCollectionType(Class type, Class superType)
	{
		Vector v;
		if(!collectionTypes.containsKey(type))
		{
			v = new Vector();
			v.add(superType);
			collectionTypes.put(type, v);
		}
		else
		{
			v = (Vector) collectionTypes.get(type);
			if(! v.contains(superType))
				v.add(superType);
		}
	}
	public OncCollection getCollection(OncProcess type)
	{
		Class typeClass = type.getClass();
		if(childCollections.containsKey(typeClass))
			return((OncCollection) childCollections.get(typeClass));
		else
			return(null);
	}
	public Collection getCollections()
	{
		return(childCollections.values());
	}
	public ImmutableHashtable<Class, OncCollection> getCollectionsTable()
	{
		if(immutableChildCollections == null)
			immutableChildCollections = new ImmutableHashtable(childCollections);
		return(immutableChildCollections);
	}
	public Vector getCollections(Class type)
	{
		Vector rVec = new Vector();
		if(collectionTypes.containsKey(type))
		{
			Vector collKeys = (Vector) collectionTypes.get(type);
			Iterator it = collKeys.iterator();
			while(it.hasNext())
				rVec.add(childCollections.get(it.next()));
		}
		return(rVec);
	}
	
	public void addCollectionListener(ParentListener listener)
	{
		if(!collectionListeners.contains(listener))
			collectionListeners.add(listener);
	}
	public void removeCollectionListener(ParentListener listener)
	{
		if(collectionListeners.contains(listener))
			collectionListeners.remove(listener);
	}
	public void fireCollectionChanged()
	{
		ImmutableHashtable<Class, OncCollection> table = getCollectionsTable();
		for(ParentListener listener : collectionListeners)
			listener.collectionChanged(table);
	}
	public OncParent getParent()
	{
		return(parent);
	}
	public void setParent(OncParent parent)
	{
		this.parent = parent;
	}
	public MasterScheduler getMasterScheduler()
	{
		return(getParent().getMasterScheduler());
	}
	public EventManager getEventManager()
	{
		return(getParent().getEventManager());
	}
	public OncReporter getReporter()
	{
		return(getParent().getReporter());
	}
	public OncReporter getCTReporter()
	{
		return(getParent().getCTReporter());
	}
	public boolean isTimeKeptLocally()
	{
		return(timeKeptLocally);
	}
	public void setTimeKeptLocally(boolean localTime)
	{
		timeKeptLocally = localTime;
	}
	public double getLocalTime()
	{
		if(timeKeptLocally)
		{
			return(getMasterScheduler().globalTime - creationTime);
		}
		else
		{
			if(parent == null)
				throw(new OncotcapError("ERROR: Parent not set.  Time is not kept locally by this process and no parent is available to retrieve time from."));
			else
				return(parent.getLocalTime());
		}
	}
	public void setCreationTime(double creationTime)
	{
		this.creationTime = creationTime;
	}
	public boolean isAbove(OncProcess proc)
	{
		if(proc == parent)
			return(true);
		else
		{
			if(parent == null)
				return(false);
			else
				return(parent.isAbove(proc));
		}
	}
	public boolean isAbove(OncCollection collection)
	{
		for(OncCollection coll : ((Collection<OncCollection>) getCollections()))
		{
			if(coll == collection)
				return(true);
			else if(getParent() == null)
				return(false);
			else
				if(getParent().isAbove(collection))
					return(true);
		}
		return(false);
	}
	
	protected OncRandom newRNG(long seed)
	{
		OncRandom rval = null;
		if(rngClass == null)
		{
			try
			{
				rngClass = Class.forName("oncotcap.sim.random.DefaultRNG");
			}
			catch(ClassNotFoundException e)
			{
				Logger.log("Fatal Error: Can not find default RNG oncotcap.sim.random.DefaultRNG");
				System.exit(1);
			}
		}
		Object [] oa = new Object [] {new Long(seed)};
		try
		{
			rval = (OncRandom) rngClass.getConstructor(rngArg).newInstance(oa);
		}
		catch(NoSuchMethodException e)
		{
			Logger.log("Fatal Error: Can not instantiate " + rngClass.getName() + "(long seed).  No such method.\n" + e);
			System.exit(1);
		}
		catch(InstantiationException e)
		{
			Logger.log("Fatal Error: Can not instantiate " + rngClass.getName() + "(long seed)\n" + e);
			System.exit(1);
		}
		catch(IllegalAccessException e)
		{
			Logger.log("Fatal Error: Can not instantiate " + rngClass.getName() + "(long seed) Illegal access exception.\n" + e);
			System.exit(1);
		}
		catch(InvocationTargetException e)
		{
			Logger.log("Fatal Error: Can not instantiate " + rngClass.getName() + "(long seed) nvocation Target exception.\n" + e);
			System.exit(1);
		}


		return(rval);
	}
	
	/**
	 ** This is the simulation seed for the ENTIRE simulation.  For this object
	 ** it is also returned in the OncParent.getSeed() method.    
	 **/
	public long getSimulationSeed()
	{
		return(getParent().getSimulationSeed());
	}
	
	public boolean usesSingleRNG()
	{
		return(getParent().usesSingleRNG());
	}
	
}

