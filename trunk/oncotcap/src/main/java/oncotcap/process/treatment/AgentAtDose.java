package oncotcap.process.treatment;

import oncotcap.sim.schedule.MasterScheduler;

public class AgentAtDose extends Administrable {
	Agent agent;
	Dose dose;
	AgentDistributor distributor;

	/** Implementation of Administrable is currently based on
	 ** boosting a concentration variable, then removing it one
	 ** tick later
	 **/
	public static void main(String[] args){
	//	double concentration;
	//	Agent agent = new Agent("testAgent", new Dose(16.0));
	//	AgentDistributor simpleAgentDistributor = new SimpleAgentDistributor(null, agent); 
	//	AgentAtDose me = new AgentAtDose(agent, simpleAgentDistributor);
	//	MasterScheduler.installTrigger(2.0,simpleAgentDistributor,"distribute");
	//	MasterScheduler.execute();
	}
	/**
	public AgentAtDose(AgentAtDose agentAtDose, AbstractPatient abstractPatient, double time) {
		super(agentAtDose.agent.name + "@" + agentAtDose.dose.dose + " " + agentAtDose.dose.units);
		this.agent = agentAtDose.agent;
		this.dose = agentAtDose.dose;
		this.distributor = agentAtDose.distributor;
		this.patient = agentAtDose.patient;
		this.absoluteTime = agentAtDose.absoluteTime;
	}
**/
	public AgentAtDose(Agent agent, AgentDistributor distributor){
		this(agent, agent.initialDose, distributor);
	}

	public Object clone()
	{
		return(new AgentAtDose(agent, (Dose) dose.clone(), distributor));
	}

	public AgentAtDose(Agent agent, Dose dose, AgentDistributor distributor){
		super(agent.name + "@" + dose.dose + " " + dose.units);
		this.agent = agent;
		this.dose = dose;
		this.distributor = distributor;
	}
	public Agent getAgent(){
		return(agent);
	}
	public Dose getDose(){
		return(dose);
	}
	public void setDose(Dose dose){
		this.dose = dose;
	}
	public void administer(AbstractPatient patient, double absoluteTime){
		Object [] arglist = { null, null };
		arglist[0] = patient;
		arglist[1] = new Double(absoluteTime);
		patient.getMasterScheduler().installTrigger(absoluteTime, distributor, "distribute", arglist);
	}
	public void administer(AbstractPatient patient){
		distributor.distribute(patient, dose);
	}

	public Dose reduceDose(double percentReduction)
	{
		double newDose = dose.get() * (1.0 - percentReduction/100.0);
//		Logger.log("Dose reduced to " + newDose);
		dose.set(newDose);
		return(dose);
	}

	public Dose increaseDose(double percentIncrease)
	{
		if (dose.get() < 0.0000000001)
		{
			dose.set(agent.initialDose.get());
//			Logger.log("Dose reset to " + agent.initialDose.get());
		}
		else
		{
			double newDose = dose.get() / (percentIncrease/100.0);
//			Logger.log("Dose increased to " + newDose);
			dose.set(newDose);
		}
		return(dose);
	}

}
