package oncotcap.process.treatment;

import oncotcap.sim.schedule.*;
import java.util.*;

/** A Cycle consists of an array of AgentAtDoseAndTime objects.
 ** Future changes: it will be AdministrableAtDoseAndTime.
 **/
public class Cycle extends Administrable {

	public Cycle (String name){
		super(name);
	}
	public Cycle (Cycle cycle){
		this(cycle.name);
		Iterator iter = cycle.deliveries.iterator();
		while (iter.hasNext())
		{
			deliveries.add(iter.next());
		}
	}

	public Object clone()
	{
		Cycle cycleRet = new Cycle(name);
		Iterator iter = this.deliveries.iterator();
		while(iter.hasNext())
		{
			cycleRet.deliveries.add( ((AgentAtDoseAndTime)iter.next()).clone() );
		}
		return(cycleRet);
	}
	
	Vector deliveries = new Vector();

	public void administer(AbstractPatient patient)
	{
		administer(patient, patient.getMasterScheduler().globalTime);
	}

	public void administer(AbstractPatient patient, double time)
	{
		Iterator iter = deliveries.iterator();
		while (iter.hasNext())
		{
			((AgentAtDoseAndTime)iter.next()).administer(patient, time);
		}
	}

	public void addAgentAtDose (AgentAtDose agentAtDose, DayList daylist){
		Iterator iter = daylist.iterator();
		while (iter.hasNext()){
			Integer i = (Integer) iter.next();
			double relativeTime = new Double(i.intValue()).doubleValue();
			deliveries.add(new AgentAtDoseAndTime((AgentAtDose) agentAtDose.clone(),relativeTime));
//			Logger.log("Adding a dose to Cycle at relativeTime " + relativeTime);
		}
	}

	//returns the time of the last application
	public double getDuration()
	{
		AgentAtDoseAndTime adt;
		Iterator iter = deliveries.iterator();
		double max = 0;
		while (iter.hasNext()){
			adt = (AgentAtDoseAndTime) iter.next();
			if (adt.relativeTime.doubleValue() > max) max = adt.relativeTime.doubleValue();
		}
		return(max);
	}

	public void reduceAllDoses(double percentReduction)
	{
		Iterator iterCycle = deliveries.iterator();
		while (iterCycle.hasNext())
		{
			((AgentAtDoseAndTime)iterCycle.next()).reduceDose(percentReduction);
		}

	}

	public void increaseAllDoses(double percentIncrease)
	{
		Iterator iterCycle = deliveries.iterator();
		while (iterCycle.hasNext())
		{
			((AgentAtDoseAndTime)iterCycle.next()).increaseDose(percentIncrease);
		}

	}
}
