package oncotcap.sim.schedule;

import java.util.*;
import java.lang.reflect.*;
import oncotcap.util.*;
import oncotcap.sim.EventManager;

import java.io.*;

/**	The class <code>MasterScheduler</code> contains methods for
 *	registering a call back event based on a fictitious clock.
 *	This clock does not run in real time, rather it compiles a list of
 *	times that objects would like to be notified at and successively
 *	calls the objects.
 */
public class MasterScheduler
{
	public static final double NOW = Double.NEGATIVE_INFINITY;
	private SortedList triggerQ = new SortedList(new TriggerCompare(), false);
	private Object tTrig;
	public double globalTime = 0;
	public static final Object [] emptyList = { };
	public boolean paused = false;
	public boolean running = false;
	private FileWriter out;
	private boolean writeStat = false;
	private OncTime.TimeUnit timeUnit = OncTime.MONTH;
	private Vector<MasterSchedulerListener> listeners = new Vector();
	
	public OncTime.TimeUnit getTimeUnit()
	{
		return(timeUnit);
	}
	//installTrigger
	/** Adds an entry in the list of objects to be triggered.  It adds
	 *	the method in the list relative to the current
	 *	<code>globalTime</code>, that is the objects return call will
	 *	happen at time <code>globalTime + time</code>
	 *	@param	time	the relative time for an object to be called
	 *	back
	 *	@param	caller	a reference to the object that wants to be
	 *	notified
	 *	@param	method	a reference to the method that the calling
	 *	object would like to be notified.  This method must be of the
	 *	form <code>void Method()</code>
	 */
	public TriggerEntry installTrigger(double time, Schedulable caller, Method method)
	{
		return(installTrigger (time, caller, method, emptyList));
	}
	public TriggerEntry installTrigger(double time, Schedulable caller)
	{
		return(installTrigger (time, caller, "update", emptyList));
	}
	public TriggerEntry installTrigger(double time, Object caller, Method method, Object[] arglist)
	{
		double triggerTime;
		TriggerEntry triggerEntry = null;
		if(time == Double.NEGATIVE_INFINITY)
			triggerTime = globalTime;
		else if(! (caller instanceof Schedulable))
			triggerTime = time + globalTime;
		else if(((Schedulable)caller).getScheduleOffset() == Double.NEGATIVE_INFINITY)
			triggerTime = time + globalTime;
		else
		{
			if (((Schedulable)caller).getScheduleOffset() == -1) ((Schedulable)caller).setScheduleOffset(globalTime);
			triggerTime = time + ((Schedulable)caller).getScheduleOffset();
		}
		return (installTriggerAbsolute(triggerTime, caller, method, arglist));
		// This should be an exception.
	}

	public TriggerEntry installTrigger(double time, Object caller, String methodString)
	{
	      return(installTrigger(time, caller, methodString, emptyList));
	}

	public TriggerEntry installTrigger(double time, Object caller, String methodString, Object[] arglist)
	{
		// This method will fail if any arg is a subclass of the
		// signature class.
		Class [] classlist;
		classlist = (Class []) Array.newInstance(Class.class, arglist.length);
		for (int i= 0; i<Array.getLength(arglist); i++)
			classlist[i] = arglist[i].getClass();
		return  installTrigger(time, caller,
			methodString, arglist, classlist);
	}
	public TriggerEntry installTrigger(double time, Object caller,
						  String methodString, Object[] arglist, Class [] classlist)
	{
		try
		{
			Method method = caller.getClass().getMethod(methodString, classlist);
			return(installTrigger(time,caller,method,arglist));
		}
		catch(NoSuchMethodException e){
			Logger.log ("No Such Method " + methodString + " in " + caller);
			for (int j=0; j < Array.getLength(classlist); j++){
				Logger.logn("  with arg[" + j + "] of class " + classlist[j]
								  + " = " + arglist[j]);
				Logger.log("");
			}
			return(null);
		}
	}
	public TriggerEntry installTriggerAbsolute(double time, Object caller, String methodString)
	{
		Class [] classlist = {};
		try
		{
			Method method = caller.getClass().getMethod(methodString, classlist);
			return(installTriggerAbsolute(time,caller,method));
		}
		catch(NoSuchMethodException e){
			Logger.log ("No Such Method " + methodString + " in " + caller);
			for (int j=0; j < Array.getLength(classlist); j++){
				Logger.logn("  with arg[" + j + "] of class " + classlist[j]);
				Logger.log("");
			}
			return(null);
		}
	}
	public TriggerEntry installTriggerAbsolute(double triggerTime, Object caller, Method method, Object[] arglist)
	{
		TriggerEntry triggerEntry = null;
		if (triggerTime >= globalTime)
		{
			writeStats(triggerTime, method.getDeclaringClass() + "." + method.getName());
			triggerEntry = new TriggerEntry(triggerTime, caller, method, arglist, this);
			boolean ret = triggerQ.add(triggerEntry);
		}
		else
			Logger.log("Error- installTrigger- installing a time in the past, "
							  + triggerTime + " < " + globalTime
							  + " for " + method + " in " + caller + ".");
		return (triggerEntry);
		// This should be an exception.
	}
	public TriggerEntry installTriggerAbsolute(double time, Object caller, Method method)
	{
		return(installTriggerAbsolute (time, caller, method, emptyList));
	}
	public boolean removeTrigger(TriggerEntry trig) {
		int found = triggerQ.find(trig);
		if (found >= 0) {
			triggerQ.remove(found);
			return true;
		}
		else
			return false;
	}

	//execute
	/**
	 *	Executes all entries in the list of methods to be triggered.
	 *	Methods are called in ascending order based on thier trigger
	 *	time.  Events can be added or removed from the queue while
	 *	execution is taking place.  When all events have been
	 *	triggered the MasterScheduler object is <code>reset</code>.
	 */
	public void execute() //throws IllegalAccessException, InvocationTargetException
	{
		TriggerEntry trig = null;
		if(running)
		{
			Logger.log("WARNING: MasterScheduler executed while it was already running.  The previous run will be stopped.");
			resetScheduler();
		}

		if(triggerQ.hasNext())
		{
			running = true;
			fireStarted();
			while(triggerQ.hasNext())
			{
				try
				{
					if(paused)
					{
						firePaused();
						while(paused)
						{
							Thread.currentThread().sleep(100);
						}
						if(running)
							fireStarted();
						else
							return;
					}
					trig = (TriggerEntry) triggerQ.getNext();
					globalTime = trig.triggerTime;
					trig.method.invoke(trig.target,trig.arglist);
					tTrig = triggerQ.remove(0);
					tTrig = null;
				}
				catch(InterruptedException e)
				{
					resetScheduler();
					Logger.log("Simulation Interrupted."); 
					return;
				}
				catch(IllegalArgumentException e){
					e.printStackTrace();
					Logger.log( "See stacktrace above: " + e.toString());
					if(trig != null)
					{
						Logger.log("Caller: " + trig.target.toString());
						Logger.log("Method: " + trig.method.toString());
						Logger.log( "Arglist: " + trig.arglist.toString()
										  + " length="  + Array.getLength(trig.arglist));
						for (int j=0; j < Array.getLength(trig.arglist); j++){
							Logger.logn("  with arg[" + j + "] of class "
											  + trig.method.getParameterTypes()[j]
											  + " = " + trig.arglist[j]);
							Logger.log ("");
						}
					}
				}
				catch(IllegalAccessException e){ Logger.log( e); }
				catch(InvocationTargetException e)
				{
					Logger.log( e);
					Logger.log("Caller: " + trig.target.toString());
					Logger.log("Method: " + trig.method.toString());
					if(trig.arglist != null)
					{
						Logger.log( "Arglist: " + trig.arglist.toString() + " length="  + Array.getLength(trig.arglist));
					
						for (int j=0; j < Array.getLength(trig.arglist); j++){
							Logger.logn("  with arg[" + j + "] of class "
											  + trig.method.getParameterTypes()[j]
											  + " = " + trig.arglist[j]);
							Logger.log("");
						}
					}
					e.getCause().printStackTrace();
				}
			}
			if(! triggerQ.hasNext())
				resetScheduler();
		}
			
	}

	//reset
	/**
	 *	Resets the MasterScheduler object.  Global time is reset to
	 *	zero and a trigger events are cleared from the queue.
	 */
	public synchronized void resetScheduler()
	{
		Thread t = Thread.currentThread();
		if(! t.isInterrupted())
			t.interrupt();
		globalTime = 0;
		triggerQ.clear();
		running = false;
		paused = false;
		fireReset();
	}

	/**
	 *	Pause/Unpause the MasterScheduler.  Will halt the execution of the
	 *	schedule queue if MasterScheduler is currently in the executing
	 *	in the execute function.  If currently paused it will cause the
	 *	MasterScheduler to start executing again.
	 */
	public synchronized void togglePause()
	{
		
			paused = ! paused;
	}

	public void printTotals()
	{
		Logger.log("Total trigger adds = " + triggerQ.totalAdds + ".  " + triggerQ.addAtEnd + " were at the end of the list.\nMaximum queue size was " + triggerQ.maxSize);
		try{
			out.close();
		}catch(IOException e){Logger.log("Error closing SortedList2 stat file \n" + e);}

	}

	public void statsOn()
	{
//		syncronized (writeStat) {
			if (!writeStat){
				writeStat = true;
				try
				{
					out = new FileWriter("u:\\SortedListStats2.txt", false);
				}catch(IOException e){Logger.log("Error opening SortedList2 stat file \n" + e);}
			}
//		}
	}
	public void writeStats(double insertTime, String name)
	{
		if(writeStat)
		{
			try
			{
				out.write((insertTime - globalTime) + "\t" + name + "\n");
			}catch(IOException e){Logger.log("Error writing SortedList2 stat file \n" + e);}
		}
	}
	public void printQueue() {
		Iterator it = triggerQ.iterator();
		for (; it.hasNext(); ) {
			TriggerEntry triggerEntry = (TriggerEntry) it.next();
			Logger.log("triggerQ " + triggerEntry.triggerTime
							   + " "  + triggerEntry.target
							   + " "  + triggerEntry.method
							  );
		}
	}
	
	public void addMasterSchedulerListener(MasterSchedulerListener listener)
	{
		if(! listeners.contains(listener))
			listeners.add(listener);
	}
	public void removeMasterSchedulerListener(MasterSchedulerListener listener)
	{
		if(listeners.contains(listener))
			listeners.remove(listener);
	}
	private void firePaused()
	{
		for(MasterSchedulerListener listener : listeners)
			listener.schedulerPaused();
	}
	private void fireReset()
	{
		running = false;
		paused = false;
		for(MasterSchedulerListener listener : listeners)
			listener.schedulerReset();
	}
	private void fireStarted()
	{
		running = true;
		for(MasterSchedulerListener listener : listeners)
			listener.schedulerStarted();
	}
	public boolean isRunning()
	{
		return(running);
	}
	public boolean isPaused()
	{
		return(paused);
	}
}

