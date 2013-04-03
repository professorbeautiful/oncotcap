package oncotcap.process.treatment;

import oncotcap.process.*;
import oncotcap.sim.schedule.Schedulable;
import oncotcap.*;

abstract public class AbstractAgentDistributor extends OncProcess
   implements Schedulable{

	protected AbstractPatient patient = null;
	protected Agent agent;
	protected static long id = 0;
	protected String description = new String();
	protected double decayDeltaT;

	public AbstractAgentDistributor(OncParent parent)
	{
		super(parent);
	}
/*	public AbstractAgentDistributor(AbstractPatient patient, Agent agent){
		this.patient = patient;
		this.agent = agent;
		id++;
	} */
//	abstract public void distribute(AbstractPatient patient, Dose dose);

	public Agent getAgent() {
		return agent;
	}
//	abstract public double getConcentration();

	protected double scheduleOffset = 0;
	public double getScheduleOffset()
	{
		return(scheduleOffset);
	}
	public void setScheduleOffset(double offset)
	{
		this.scheduleOffset = offset;
	}

	public boolean equals(Object o)
	{
		if(o instanceof AgentDistributor)
		{
			//todo: check this out... id is static, so this will always return true
			if(id == AgentDistributor.id)
				return(true);
			else
				return(false);
		}
		else
			return(false);
	}
	public void setDescription(String desc)
	{
		description = desc;
	}
	public String getDescription()
	{
		return description;
	}

	public String toString()
	{
		return( ((agent==null)?  ": " : agent.name)
				+ ((description==null)?  "" : description));
	}

	public AbstractPatient getPatient() {
		return patient;
	}

	public double getDeltaT()
	{
		return(decayDeltaT);
	}
}