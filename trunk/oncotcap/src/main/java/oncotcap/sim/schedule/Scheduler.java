/**
 * Helper class that provides the functionality to schedule and manage
 * events that are scheduled with the MasterScheduler for a particular
 * Object and Method.
 */
package oncotcap.sim.schedule;

import java.util.*;
import java.lang.reflect.Array;
import java.lang.reflect.Method;

import oncotcap.util.*;

public class Scheduler extends AbstractSchedulable
{
	Object process;
	String methodString;
	Method method;
	Method updateMethod;
	Object [] arglist;
	public Vector triggerEntries = new Vector();
	private TriggerEntry recurringTriggerEntry;
	double gap;
	private boolean recur = false;
	private MasterScheduler masterScheduler;
	
	/**
	 *	Constructs a <code>Scheduler</code> object that will be used to
	 *	schedule events for the given method in the given object.
	 *
	 *	@param object Object that contains the method that will be used
	 *	when triggering a scheduled event
	 *
	 *	@param methodString String, name of the method that is called at scheduled time points.
	 *	@param arglist	Object [], arguments to the method.
	 */
	
	public Scheduler(Schedulable process, String methodString, Object [] arglist,
		Class [] classlist, MasterScheduler sched)
	{
		masterScheduler = sched;
		this.process = process;
		this.methodString = methodString;
		Class [] tClassList = {};
		try {
			this.method = process.getClass().getMethod(methodString, classlist);
			this.updateMethod = getClass().getMethod("update", tClassList);
		}
		catch (NoSuchMethodException e){ 
			Logger.log("Scheduler aborted: Cannot find the method " + methodString
							  + " for class " + process.getClass().getName());
		}
		this.arglist = arglist;
	}
	public Scheduler(Object process, String methodName, MasterScheduler sched)
	{
		this.masterScheduler = sched;
		this.process = process;
		this.methodString = methodName;
		Class [] classlist = {};
		try {
			this.method = process.getClass().getMethod(methodString, classlist);
			this.updateMethod = getClass().getMethod("update", classlist);
		}
		catch (NoSuchMethodException e){ 
			Logger.log("Scheduler aborted: Cannot find the method " + methodString
							   + " for class " + process.getClass().getName());
		}
		this.arglist = null;
		
		
	}
	public static void main(String [] args)
	{

		//TestObj testObj = new TestObj();
		//testObj.myScheduler = new Scheduler(testObj, "update");
		//testObj.myScheduler.addRecurrentEvent(MasterScheduler.globalTime, 0.1);
		//MasterScheduler.execute();
	}
//	private static class TestObj
//	{
//		public Scheduler myScheduler;
//		public void update()
//		{
//			System.out.println(masterScheduler.globalTime);
//			if(masterScheduler.globalTime >= 3)
//				myScheduler.endRecurrentEvent();
//		}
//	}
	/**
	 *	Get a list of all times currently scheduled.
	 *
	 *	@return array containing all times currently scheduled.
	 */
	public double[] getEventTimes(){
		double [] times = (double[])Array.newInstance(double.class, triggerEntries.size());
		Iterator iter = triggerEntries.iterator();
		int i=0;
		while (iter.hasNext())
			times[i++] = ((TriggerEntry)iter.next()).triggerTime;
		return(times);
	}
			
	/**
	 *	Add an event at the given time to the MasterScheduler.
	 *
	 *	@param time Time that the event will be scheduled at.
	 */
	public void addEvent(double time){
		TriggerEntry triggerEntry = masterScheduler.installTrigger(time, process, method, arglist);
		if (triggerEntry == null)
			Logger.log("Scheduler addEvent error: null triggerEntry");
		else
			triggerEntries.add(triggerEntry);
	}
	private void addEventAbsolute(double time){
		TriggerEntry triggerEntry = masterScheduler.installTriggerAbsolute(time, process, method, arglist);
		if (triggerEntry == null)
			Logger.log("Scheduler addEvent error: null triggerEntry");
		else
			triggerEntries.add(triggerEntry);
	}
	
	/**
	 *  Add events at the given times to the MasterScheduler.
	 *
	 *  @param times Array of times to schedule events.
	 */
	public void addEvents(double[] times){
		for (int i=0; i<Array.getLength(times); i++)
			addEvent(times[i]);
	}

	/**
	 *	Add an event that will recur every <i>gap</i> time u nits,
	 *	beginning at <i>begin</i>.
	 *
	 *	@param begin Time to begin scheduling events.
	 *
	 *	@param gap interval between events
	 */
	public void addRecurrentEvent(double begin, double gap){
		this.gap = gap;
		double sTime = begin;
		if(begin == MasterScheduler.NOW )
			sTime = masterScheduler.globalTime;
		addEventAbsolute(sTime);
		recur = true;
		recurringTriggerEntry = masterScheduler.installTriggerAbsolute(sTime+gap, this, updateMethod);
			// this will call update after a delay of gap.
	}

	public void addRecurrentEvent(double gap)
	{
		addRecurrentEvent(0, gap);
	}
	public void setGap(double newGap)
	{
		gap = newGap;
	}
	public void endRecurrentEvent()
	{
		recur = false;
		if (recurringTriggerEntry != null)
			masterScheduler.removeTrigger(recurringTriggerEntry);
		recurringTriggerEntry = null;
	}
	public void update(){
		if(recur)
			addRecurrentEvent(masterScheduler.globalTime, gap);
	}
		
	/**
	 *	Add events starting at begin, ending at end at an interval of
	 *	gap.
	 *
	 *	@param begin Time to begin scheduling events.
	 *
	 *	@param end Time to end scheduling events.
	 *
	 *	@param gap interval between events
	 */
	public void addEvents(double begin, double end, double gap){
		for (double time = begin; time<=end; time = time+gap) {
			addEvent(time);
			//Logger.log("Adding event by interval " + begin + " "
			//				   +  end + " " + gap + " " + time);
		}
	}
	
	/**
	 *	Add n events starting at begin at an interval of gap.
	 *
	 *	@param begin Time to begin scheduling events
	 *
	 *	@param gap interval between events
	 *
	 *	@param n number of instances to schedule
	 */
	public void addEvents(double begin, double gap, int n){}

	/**
	 *	Remove the event scheduled at the given time.
	 */
	public void remove(double[] time){}

	/**
	 *	Delay the specified scheduled time by the amount specified by
	 *	delay.
	 */
	public void delay(double oldTime, double delay){}

	/**
	 *	Delay all scheduled events after the given time by the amount
	 *	specified by delay.
	 */
	public void delayAfter(double time, double delay){}

	/**
	 *	Delay all scheduled events before the given time by the amount
	 *	specified by delay.
	 */
	public void delayBefore(double time, double delay){}

	/**
	 *	Reschedule the event at the specified time to a new time.
	 */
	public void reschedule(double oldTime, double newTime){}


	public boolean removeEvent(double time)
	{
		Iterator iter = triggerEntries.iterator();
		int i=0;
		int nremoved = 0;
		while (iter.hasNext()) {
			TriggerEntry trig = (TriggerEntry)iter.next();
			if (time == trig.triggerTime) {
				masterScheduler.removeTrigger(trig);
				nremoved++;
			}
		}
		if (nremoved > 0)
			return(true);
		else
			return(false);
	}

	public int removeAll(){
		endRecurrentEvent();
		Iterator iter = triggerEntries.iterator();
		int i=0;
		int nremoved = 0;
		while (iter.hasNext()) {
			TriggerEntry trig = (TriggerEntry)iter.next();
			masterScheduler.removeTrigger(trig);
			nremoved++;
		}
		return (nremoved);
	}

	/**
	 *	Remove all events scheduled at or after the given time
	 */
	public int removeAfter(double time){
		Iterator iter = triggerEntries.iterator();
		int i=0;
		int nremoved = 0;
		while (iter.hasNext()) {
			TriggerEntry trig = (TriggerEntry)iter.next();
			if (time <= trig.triggerTime) {
				masterScheduler.removeTrigger(trig);
				nremoved++;
			}
		}
		return (nremoved);
	}

	/**
	 *	Remove all events scheduled at or before teh given time
	 */
	public int removeBefore(double time){
		Iterator iter = triggerEntries.iterator();
		int i=0;
		int nremoved = 0;
		while (iter.hasNext()) {
			TriggerEntry trig = (TriggerEntry)iter.next();
			if (time >= trig.triggerTime) {
				masterScheduler.removeTrigger(trig);
				nremoved++;
			}
		}
		return (nremoved);
	}
}