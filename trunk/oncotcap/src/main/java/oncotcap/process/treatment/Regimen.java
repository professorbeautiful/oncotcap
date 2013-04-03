package oncotcap.process.treatment;

import oncotcap.util.*;
import oncotcap.sim.OncReporter;
import oncotcap.sim.schedule.*;
import java.util.*;

/** A Cycle consists of an array of AgentAtDoseAndTime objects.
 ** Future changes: it will be AdministrableAtDoseAndTime.
 **/
public class Regimen extends Cycle {
	//Just like cycle, except you can add a cycle!
	private double regDuration;
	TriggerEntry installedTrigger = null;
	
	public Regimen (String name){
		super(name);
	}

	Vector deliveries = new Vector();

	public void administer(AbstractPatient patient){
		administer(patient, patient.getMasterScheduler().globalTime);
	}
	public void administer(AbstractPatient patient, double absoluteTime){
		Iterator iter = deliveries.iterator();
		Object admin = null;
		while (iter.hasNext()) {
			admin = iter.next();
			if (admin instanceof AgentAtDoseAndTime)
				((AgentAtDoseAndTime)admin).administer(patient, absoluteTime);
			else if (admin instanceof Cycle)
				((Cycle)admin).administer(patient, absoluteTime);
		}
		if (admin != null)
		{
			if (admin instanceof Cycle)
			{
				double lastAdmin = regDuration + absoluteTime + .001;
				installedTrigger = patient.getMasterScheduler().installTrigger(lastAdmin, new SendEndTreatment(patient.getMasterScheduler(), patient.getReporter()));			
			}
		}

	}

	public class SendEndTreatment extends AbstractSchedulable
	{
		MasterScheduler masterScheduler;
		OncReporter reporter;
		public SendEndTreatment(MasterScheduler scheduler, OncReporter reporter)
		{
			masterScheduler = scheduler;
			this.reporter = reporter;
			setScheduleOffset(0.0);
		}
		public void update()
		{
			reporter.notifyObservers(OncEvent.ENDOFTREATMENT, "", masterScheduler.globalTime);
		}
	}

	public void addCycles (Cycle cycle, double lengthOfCycle, int nCycles){
		for (int i=0; i<nCycles; i++) 
			deliveries.add(new CycleAtTime((Cycle) cycle.clone(), lengthOfCycle*i));

		regDuration = lengthOfCycle/30.0 * (nCycles - 1) + cycle.getDuration();
	}


	public void reduceAllDoses(double percentReduction)
	{
		Iterator iter = deliveries.iterator();
		while (iter.hasNext()) {
			Cycle cycle = (Cycle) iter.next();
			cycle.reduceAllDoses(percentReduction);
		}
	}

	public void increaseAllDoses(double percentIncrease)
	{
		Iterator iter = deliveries.iterator();
		while (iter.hasNext()) {
			CycleAtTime cycle = (CycleAtTime) iter.next();
			cycle.increaseAllDoses(percentIncrease);
		}

	}

	public void removeAllDoses()
	{
		if (installedTrigger != null)
			installedTrigger.removeTrigger();
		
		Iterator iter = deliveries.iterator();
		while(iter.hasNext())
		{
			CycleAtTime cycle = (CycleAtTime) iter.next();
			cycle.removeAllDoses();
		}
		
	}

}
