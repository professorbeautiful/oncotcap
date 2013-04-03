package oncotcap.sim.schedule;

import java.lang.reflect.*;

public class TriggerEntry
{
	public double triggerTime;
	Object target;
	Method method;
	Object[] arglist = {};
	private MasterScheduler schedulerContainingMe;
	
	TriggerEntry(double time, Object target, Method meth, MasterScheduler sched)
	{
		schedulerContainingMe = sched;
		triggerTime = time;
		this.target = target;
		method = meth;
	}
	TriggerEntry(double time, Object target, Method meth, Object[] arglist, MasterScheduler sched)
	{
		schedulerContainingMe = sched;
		triggerTime = time;
		this.target = target;
		method = meth;
		if(arglist != null)
			this.arglist = arglist;
	}
	public MasterScheduler getSchedulerContainingMe()
	{
		return(schedulerContainingMe);
	}
	public boolean removeTrigger()
	{
		return(schedulerContainingMe.removeTrigger(this));
	}
}