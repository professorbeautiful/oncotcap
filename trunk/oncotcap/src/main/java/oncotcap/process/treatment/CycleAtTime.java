package oncotcap.process.treatment;

import java.util.*;
import oncotcap.sim.schedule.*;

public class CycleAtTime extends Cycle {
	Double relativeTime;
	TriggerEntry installedTrigger = null;
	
	public CycleAtTime(Cycle cycle, double relativeTime){
		super(cycle);  //make a clone.
//		Logger.log("Createing a CycleAtTime");
		this.relativeTime = new Double(relativeTime/30.0);
	}
	public void setScheduleOffset(Double relativeTime){
		this.relativeTime = new Double(relativeTime.doubleValue());
	}
	public double getScheduleOffset(){
		return(relativeTime.doubleValue());
	}
	
	public void administer(AbstractPatient patient, double absoluteTime){
		Object [] arglist = {patient};
		Class [] classlist = {AbstractPatient.class};
		
/*		Logger.log("CycleAtTime installing trigger at "
						   + "relativeTime=" + relativeTime
						   + "absoluteTime=" + (absoluteTime +
						   relativeTime.doubleValue()));*/
		
		installedTrigger = patient.getMasterScheduler().installTrigger (
								absoluteTime,
								this,
								"superAdminister", arglist, classlist
	   );
	}
	public void superAdminister(AbstractPatient patient)
	{
		//Logger.log("CYCLEATTIME Superadminister at time " + MasterScheduler.globalTime);
		super.administer(patient);
	}

	public void removeAllDoses()
	{
		if (installedTrigger != null)
			installedTrigger.removeTrigger();
		
		Iterator iterCycle = deliveries.iterator();
		while (iterCycle.hasNext())
		{
			((AgentAtDoseAndTime)iterCycle.next()).removeDose();
		}

	}
}