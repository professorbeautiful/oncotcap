package oncotcap.process.treatment;

import oncotcap.sim.schedule.Schedulable;
import oncotcap.util.OncEnum;
import oncotcap.process.OncCollection;
import oncotcap.process.OncProcess;

public class AgentDistributor extends OncProcess 
   implements Schedulable{
//  this class may not be needed at all!
	public OncCollection newCollectionInstance(){
		return(new AgentDistributorCollection(getMasterScheduler(), getEventManager()));
	}
	public AgentDistributor clone(){
		AgentDistributor dist = new AgentDistributor(patient,agent);
		dist.patient = patient;
		dist.agent = agent;
		return dist;
	}
	public AgentDistributor clone(OncEnum value){
		AgentDistributor dist = clone();
		dist.changeProp(value);
		return dist;
	}
	public AgentDistributor clone(OncEnum [] values){
		AgentDistributor dist = clone();
		for (OncEnum value : values)
			dist.changeProp(value);
		return dist;
	}
	protected AbstractPatient patient = null;
	protected Agent agent;
	protected static long id = 0;
	protected String description = new String();
	protected double decayDeltaT = 0.01;
	//TODO: decayDeltaT should be an OncTime.
	protected double concentration = 0.0;
	
	public void setDecayDeltaT(double value){
		decayDeltaT = value;
	}
	public double getDecayDeltaT(){
		return decayDeltaT ;
	}
	public AgentDistributor(AbstractPatient patient, Agent agent){
		this.patient = patient;
		this.agent = agent;
		id++;
	}
/*	 
 * override distribute()  for example if this is a source compartment with
 * destination compartments. 
*/	public void distribute( AbstractPatient patient, Dose dose){
		concentration += dose.get();
		double residenceTime = patient.getMasterScheduler().globalTime + decayDeltaT;
		patient.getMasterScheduler().installTrigger(residenceTime,(Schedulable)this, "clearCompartment");
	}
	public void clearCompartment(){
		concentration = 0;
	}
	public Agent getAgent() {
		return agent;
	}
	public double getConcentration(){
		return concentration;
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