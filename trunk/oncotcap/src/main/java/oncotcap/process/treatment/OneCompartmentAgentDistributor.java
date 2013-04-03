package oncotcap.process.treatment;

import oncotcap.*;
import oncotcap.sim.OncReporter;
import oncotcap.sim.schedule.MasterScheduler;
import oncotcap.sim.schedule.Scheduler;
import oncotcap.util.*;

public class OneCompartmentAgentDistributor extends AgentDistributor implements Reporter{

	public double concentration = 0.0;
	public AbstractPatient patient = null;
	protected double multiplier = 1.0;   
	protected Scheduler scheduler = null;
	protected double undetectable = 1e-5;
	protected Dose dose;
	protected boolean reductionStarted = false;
	private double lastUpdateTime = -1.0;
	private double currentTime = -1.0;
	protected double halfLife;
	protected OneCompartmentAgentDistributor sink = null;
	
	protected static final double log2 = Math.log(2.0);  //yes, this is natural log.
	
   public OneCompartmentAgentDistributor(AbstractPatient patient, Agent agent, double halfLife){
		super(patient, agent);
		this.halfLife = halfLife;
		setScheduleOffset(0.0);
		scheduler = new Scheduler(this, "reduceConcentration", patient.getMasterScheduler());
		scheduler.setScheduleOffset(0.0);
		Logger.log("Constructing OneCompartmentAgentDistributor for Patient # "
						   + patient.thisptnum + "  agent " + agent.name +
						  "  t=" + patient.getMasterScheduler().globalTime);
	}
	
	public OneCompartmentAgentDistributor(AbstractPatient patient, Agent agent,
										  double halfLife, double multiplier){
		this(patient, agent, halfLife);
		this.multiplier =  multiplier;
	}

	public OneCompartmentAgentDistributor(AbstractPatient patient, Agent agent,
										  double halfLife, double multiplier,
										 double undetectable, double deltaT){
		this(patient, agent, halfLife, multiplier);
		this.undetectable =  undetectable;
		decayDeltaT = deltaT;
	}
	public double getHalfLife () {
		return (halfLife);
	}
	
	public OneCompartmentAgentDistributor getSink() {
		return sink;
	}

	public void setSink(OneCompartmentAgentDistributor sink) {
		// the sink is incremented by the decrement of this in update.
		this.sink = sink;
		if (sink.scheduler.triggerEntries.size() == 0)
		{
			double begin = patient.getMasterScheduler().globalTime + decayDeltaT;
			sink.scheduler.addRecurrentEvent(begin+decayDeltaT, decayDeltaT);
		}
	}
	
	public double getConcentration()
	{
		return(concentration);
	}

	public void distribute(AbstractPatient patient, Dose dose){
		this.dose = dose;
		double doseInMgPerM2 = dose.dose;  // Default: mg/m2
		// This presumes that BSA is the correct normalizer, and that
		// it has been set in the patient, and that the multiplier is
		// relative to "mg/m2".
		try {
			double bsa = Double.parseDouble((String)patient.getProp("BSA"));
			if (dose.units.unit.equals("mg/m2"))
				doseInMgPerM2 = dose.dose;
			else if (dose.units.unit.equals("mg")) {
				Logger.log (  "bsa = " + bsa);
				doseInMgPerM2 = dose.dose / bsa;
			}
			else if (dose.units.unit.equals("ug/m2"))
				doseInMgPerM2 = 1000 * dose.dose;
			else if (dose.units.unit.equals("ug"))
				doseInMgPerM2 = 1000 * dose.dose / bsa;
		}
		catch (NullPointerException e) {
			Logger.log("OncCompartmentAgentDistributor: can't compute BSA- null pointer");
		}
							
		concentration = concentration + multiplier * doseInMgPerM2;
		this.patient = patient;
		this.dose = dose;
//		scheduler.removeAll();
		startReduction();
		
		//Logger.log(MasterScheduler.globalTime +
		//				  " DISTRIBUTOR: concentration=" + concentration
		//				  + "time  = " + MasterScheduler.globalTime);
		patient.getCTReporter().notifyObservers(OncEvent.TREATMENT, toString(), patient.getMasterScheduler().globalTime);
		patient.getReporter().notifyObservers(OncEvent.TREATMENT, this, patient.getMasterScheduler().globalTime);
	}

	protected void startReduction()
	{
		if (!reductionStarted)
		{
			reductionStarted = true;
			double begin = patient.getMasterScheduler().globalTime + decayDeltaT;
			scheduler.addRecurrentEvent(begin, decayDeltaT);
		}
	}

	protected void stopReduction()
	{
		if(reductionStarted)
		{
			scheduler.endRecurrentEvent();
			reductionStarted = false;
		}
	}
	
	public String toString() {
/*		String agentname;
		if (agent == null)
			agentname = "???";
		else
			agentname = agent.name;
*/					
		double d = dose.get();
		return new String(super.toString()
						  + "  dose = "
						  + dose.get()) ;
	}
	
	public void reduceConcentration(){
		reduceConcentration(halfLife, sink);
		//By default, just reduce using the built-in halfLife field.
		// Override if there are modifications via organ function,
		//   or multiple mechanisms.
		notifyEverybody();
	}
	public void notifyEverybody() {
		//Not used in Version 4.
	}
	public void reduceConcentration(double halfLife, OneCompartmentAgentDistributor sink){
		//Use this if there are more
		//patient.changeProp(agent.name + "Concentration", "0");
		if(currentTime < 0.0)
		{
			currentTime = patient.getMasterScheduler().globalTime;
			return;
		}

		lastUpdateTime = currentTime;
		currentTime = patient.getMasterScheduler().globalTime;
		if (concentration < undetectable)
		{
			concentration = 0.0;
			stopReduction();
			return;
		}
		//  The elimination/metabolism should be proportional to
		//  Mass^0.75 ,  or BSA^(.75/.67).
		double decrement = concentration * (1-Math.exp((-getTimeSinceLastUpdate()/halfLife)*log2));
		//Logger.logn(MasterScheduler.globalTime + " Concentration of " + super.toString() + " was " + concentration);
		concentration = concentration - decrement;
		if (sink != null) {
			sink.concentration = sink.concentration + decrement;
		}
	}
	public double getTimeSinceLastUpdate()
	{
		if(lastUpdateTime < 0.0) return(-1.0);
		return(patient.getMasterScheduler().globalTime - lastUpdateTime);
	}
	public void update (){
		reportConcentration();
	}
	public Object [] report(){
		Object [] report = { new Double(patient.getMasterScheduler().globalTime), agent,
							new Double(concentration) };
		return(report);
	}
	public void reportConcentration(){
//		Logger.log("At " + MasterScheduler.globalTime
	//					   + "  concentration is " + concentration);
	}
	public void setDeltaT(double t) {
		decayDeltaT = t;
	}
	public void finalize() throws java.lang.Throwable {
		//Not used in version 4.
	}
}