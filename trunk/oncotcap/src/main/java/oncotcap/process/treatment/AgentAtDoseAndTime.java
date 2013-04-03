package oncotcap.process.treatment;

import oncotcap.sim.schedule.*;
import oncotcap.util.*;

public class AgentAtDoseAndTime extends AgentAtDose {
	
	TriggerEntry installedDose = null;
	
	Double relativeTime;
	public AgentAtDoseAndTime(AgentAtDose agentAtDose, double relativeTime){
		super(agentAtDose.agent, agentAtDose.dose, agentAtDose.distributor);
		this.relativeTime = new Double(relativeTime/30.0);
	}
	public AgentAtDoseAndTime(Agent agent, Dose dose, AgentDistributor dist, double relativeTime){
		super(agent, dose, dist);
		this.relativeTime = new Double(relativeTime/30.0);
	}
	public void setScheduleOffset(Double relativeTime){
		this.relativeTime = new Double(relativeTime.doubleValue());
	}
	public double getScheduleOffset(){
		return(relativeTime.doubleValue());
	}
	public void administer(AbstractPatient patient){
		Object [] arglist = {patient};
		Class [] classlist = {AbstractPatient.class};
		
/*		Logger.log("AgentAtDoseAndTime installing trigger at "
						   + "relativeTime=" + relativeTime
						   + "absoluteTime=" + absoluteTime); */
		
		installedDose = patient.getMasterScheduler().installTrigger (
				patient.getMasterScheduler().globalTime,
				(AgentAtDose)this,
				"administer", arglist, classlist
	   );
	}

	public void removeDose()
	{
		if (installedDose != null)
		{
			Logger.log("removing dose at " + installedDose.triggerTime);
			if (installedDose.removeTrigger())
				Logger.log("removed from MasterScheduler");
		}
	}
	public Object clone()
	{
		return(new AgentAtDoseAndTime(agent, (Dose) dose.clone(), distributor, relativeTime.doubleValue() * 30.0));
	}
}