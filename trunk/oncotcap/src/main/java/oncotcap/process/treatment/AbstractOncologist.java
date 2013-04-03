package oncotcap.process.treatment;

import oncotcap.*;
import oncotcap.util.*;
import oncotcap.sim.OncReporter;
import oncotcap.sim.schedule.*;
//import oncotcap.noviceUI.NoviceUIMain;
import oncotcap.process.*;
import oncotcap.process.cells.*;
import oncotcap.display.GraphComp;
import java.util.Observable;
import java.util.Observer;
import oncotcap.process.adverseevent.*;

abstract public class AbstractOncologist extends OncProcess {
	/* This will contain much needed for Oncologist that is shared by
	 * Scenarios A and B.  Putting code here will make the simulation
	 * start faster.
	 */
	
	//AdverseEventDetector adverseEventDetector;

	
	protected String myAgentName;
	protected Agent myAgent;
	public  AgentDistributor myAgentDistributor;
	protected AgentAtDose myAgentAtDose;
	protected Cycle thisCycle;
	public Regimen thisRegimen;

	protected Agent surgery;
	public AgentDistributor surgeryDistributor;
	protected AgentAtDose surgeryAtDose;

	protected ResponseDetector responseDetector;
	protected DiagnosisDetector diagnosisDetector;
	protected RecurrenceDetector recurrenceDetector;
	protected ProgressionDetector progressDetector;
	
	protected int nResponses;

	public int nToxDeaths = 0;
	public boolean trialOver = false;
	

	//Set from Protege.

	protected String listOfDayNumbers=null;
	protected int courseDuration=0;
	protected int numberOfCourses=0;

	public AbstractOncologist(){}
	public AbstractOncologist(OncParent parent)
	{
		super(parent);
	}

	public AbstractOncologist(OncParent parent, boolean now)
	{
		super(parent, now);
	}

	
	public void init() {
		super.init();
//		if (NoviceUIMain.windowStyle.equals("external")) { 
/*			oncotcap.display.PhaseIIOutput ctout = new oncotcap.display.PhaseIIOutput();
			ctout.clear();
//			ctout.setParentWindow(NoviceUIMain.mainWindow);
			ctout.setVisible(true);
*/	
//		}
	}

	
	protected void addAgentDistributor(AbstractPatient patient) {
		final AbstractPatient thisPatient = patient;
		double halfLife = 0.5;
		double multiplier = 1.0;
		double undetectable = 1e-5;
		double decayDeltaT = 0.05;

		myAgentDistributor = new OneCompartmentAgentDistributor(
			patient, 
			myAgent,
			halfLife, multiplier, undetectable, decayDeltaT);

		myAgentAtDose = new AgentAtDose(myAgent, myAgentDistributor);

		thisCycle = new Cycle("Cycle of " + myAgent.name);
		try{
			thisCycle.addAgentAtDose(myAgentAtDose, new DayList(listOfDayNumbers));
		} catch (java.lang.Throwable e) {
		}

		thisRegimen = new Regimen("Regimen for " + myAgent.name);
		thisRegimen.addCycles(thisCycle, courseDuration, numberOfCourses);

		
		new AgentStarter(patient);
		
		new StarterWhenAgentGiven(patient, myAgent) {	
			protected void takeAction() {
//				Logger.log("Agent detected! Calling setupResponseDetector");
				setupResponseDetector(this.thisPatient);
				//We do this here instead of in AgentStarter because in
				//some problems the treatment may not start right away.
				//This way we guarantee that the Agent is actually
				//given.
			}
		};
	}


	protected void addToxicityTarget(AbstractPatient patient,
									 String name,
									 AgentDistributor agentDistributor,
									 double updateGap,
									 OncTime resolutionTime,
									 double referenceFunctionStatus,
									 double referenceConcentrationForToxicity
									)
	{
		SimpleToxTarget tox = new SimpleToxTarget(agentDistributor,
								  updateGap,
								  resolutionTime,
								  referenceFunctionStatus,
								  referenceConcentrationForToxicity);

		ToxGrader tg = new ToxGrader(name, patient, tox);
		tg.scheduler.addRecurrentEvent(getMasterScheduler().globalTime, 0.2);

		//everything above here is needed to generate toxicity events
		//below just plots the function status of the tox target
		javax.swing.JFrame toxGraphFrame 
				= new javax.swing.JFrame("tox fcn status ");
		toxGraphFrame.setSize(800,300);
		GraphComp toxGraph = new oncotcap.display.GraphComp();
		toxGraph.setAxisIsLog10("y", false);
		toxGraph.setAxisBounds("x", 93, 100, 1);
		toxGraph.setAxisBounds("y", 0, 1, 0.1);
		toxGraph.setAxisName("x", "Months");
		toxGraph.setAxisName("y", "functionStatus");
		toxGraphFrame.getContentPane().add(toxGraph);
		toxGraphFrame.setVisible(false);

		Object [] arglist = {tox, toxGraph, name};
		Class [] classlist = {SimpleToxTarget.class, GraphComp.class, String.class};


		Scheduler toxScheduler = new Scheduler(this, "plotMyTox", arglist, classlist, getMasterScheduler());
		toxScheduler.addRecurrentEvent(0, 0.1);
		getCTReporter().notifyObservers(new OncEvent(getMasterScheduler().globalTime,OncEvent.PATIENTGRAPHCREATED, toxGraphFrame));
 		getReporter().notifyObservers(new OncEvent(getMasterScheduler().globalTime,OncEvent.PATIENTGRAPHCREATED, toxGraphFrame));



	}

	public void plotMyTox(SimpleToxTarget tox, GraphComp toxGraph, String name) {
		toxGraph.addData(getMasterScheduler().globalTime, tox.getFunctionStatus(), name);
		toxGraph.repaint();
	}
/*
	public void addSurgeryDistributor(AbstractPatient patient) {
		final AbstractPatient thisPatient = patient;

		surgeryDistributor = new SimpleAgentDistributor( patient, surgery);
		surgeryAtDose = new AgentAtDose(surgery, new Dose(1.0,new DoseUnit("operation")), surgeryDistributor);

		setupDiagnosisDetector(patient);

		new SurgeryStarter(patient);
		
		new StarterWhenAgentGiven(patient, surgery) {	
			protected void takeAction() {
//				Logger.log("\nSurgery detected! Calling setupRecurrenceDetector/n");
				Object [] arglist = {this.thisPatient};
				Class [] classlist = {AbstractPatient.class};
				MasterScheduler.installTrigger(MasterScheduler.globalTime + this.thisPatient.cancerCellCollection.getDeltaT(),
										   (AbstractOncologist)this.thisPatient.getOncologist(),
										   "setupRecurrenceDetector",
										   arglist, classlist);
				//We do this here instead of in AgentStarter because in
				//some problems the treatment may not start right away.
				//This way we guarantee that the Agent is actually
				//given.
			}
		};
	}
*/
	//this is overridden by Oncologist when needed
	public void addOffStudyForToxChecker(AbstractPatient patient){}

	public void addOffStudyForToxChecker(AbstractPatient patient, int grade, Regimen regimen)
	{
		OffStudyForToxDetector offStudyForTox = new OffStudyForToxDetector(patient, grade, regimen);
	}
	
	//this is overridden by Oncologist when needed
	public void addProgressionDetector(AbstractPatient patient) {}
	
	public void addProgressionDetector(AbstractPatient patient, double percentIncrease, Regimen regimen)
	{
		progressDetector = new ProgressionDetector(patient, percentIncrease, regimen);
		progressDetector.setScheduleOffset(0.0);
		progressDetector.scheduler.setScheduleOffset(0.0);
		progressDetector.scheduler.addRecurrentEvent(getMasterScheduler().globalTime, 1.0);
	} 
	public void setupResponseDetector(AbstractPatient patient) {
		double threshholdForCR = 1e8;
		double checkInterval = 1.0;  //MUST be 1.0???
		double fractionForPR= 0.50;
		responseDetector = new ResponseDetector(patient,  threshholdForCR,  checkInterval,  fractionForPR);
		ResponseCounter responseCounter = new ResponseCounter(patient);
		getReporter().addObserver(responseCounter);
		patient.myOncReporterObservers.add(responseCounter);
	}
	
	public void setupRecurrenceDetector(AbstractPatient patient) {
		recurrenceDetector = new RecurrenceDetector(patient);
		double threshholdForRecurrence = 1e9;
		recurrenceDetector.setThreshHold(threshholdForRecurrence);
		recurrenceDetector.setScheduler(new Scheduler(recurrenceDetector, "update", getMasterScheduler()));
		double checkIntervalForRecurrence = 0.25;
		recurrenceDetector.setScheduleOffset(0.0);
		recurrenceDetector.getScheduler().setScheduleOffset(0.0);
		recurrenceDetector.scheduler.addRecurrentEvent(getMasterScheduler().globalTime,checkIntervalForRecurrence);
	}
	
	protected void setupDiagnosisDetector(AbstractPatient patient) {
		diagnosisDetector = new DiagnosisDetector(patient);
		diagnosisDetector.setThreshHold(1.0e9);
		diagnosisDetector.setScheduler(new Scheduler(diagnosisDetector, "update", getMasterScheduler()));
		double checkIntervalForDiagnosis = 0.25;
		diagnosisDetector.scheduler.addRecurrentEvent(getMasterScheduler().globalTime,checkIntervalForDiagnosis);
	}


protected class ResponseCounter extends ClinicalStarter {
	//To be done: This should be based on FINAL assessment.
	protected ResponseCounter (AbstractPatient patient) {
		super(patient);
	}
	public void update(Observable obs, Object arg) {
		if ( arg instanceof OncEvent){
			OncEvent oncEvent = (OncEvent)arg;
			if (oncEvent.getIntEventType()==OncEvent.RESPONSE)
			{
				nResponses++;
//				Logger.log("Caught response " + nResponses);
			}
		}
	}
}

protected class ToxDeathCounter implements Observer
{
	int maxToxDeaths;
	public ToxDeathCounter (int maxToxDeaths)
	{
		getReporter().addObserver(this);
		myOncReporterObservers.add(this);
		this.maxToxDeaths = maxToxDeaths;
	}
	public void update(Observable obs, Object arg) {
		if ( arg instanceof OncEvent){
			OncEvent oncEvent = (OncEvent)arg;
			if (oncEvent.getIntEventType()==OncEvent.DEATHDUETOTOXICITY)
			{
				nToxDeaths++;

				if (nToxDeaths >= maxToxDeaths)
					terminateTrial(  "<HTML><I>CONCLUSION</I>: " + myAgentName + " is " + "<B>unpromising</B>  early termination due to excessive deaths due to toxicity");

			}
		}
	}
}

public void terminateTrial(String outcome) {
	getCTReporter().notifyObservers(new OncEvent(0.0, OncEvent.TRIALRESULTS, outcome));
	getReporter().notifyObservers(new OncEvent(0.0, OncEvent.TRIALRESULTS, outcome));
	trialOver = true;
	stopSimulation();
}
protected class SurgeryStarter extends ClinicalStarter {
	protected SurgeryStarter (AbstractPatient patient) {
		super(patient);
	}
	public void update ( java.util.Observable oncReporter, Object args) {
		OncEvent oe = (OncEvent) args;
		if (getEnabled() == false)
			return;
		if (oe.getIntEventType() == OncEvent.DIAGNOSIS) {
			AbstractPatient patientDiagnosed = (AbstractPatient) oe.getArgument();
			surgeryAtDose.administer(patientDiagnosed);
			setEnabled(false);
			diagnosisDetector.setBroadcast(false);  
		}
	}
}

	/** Starts a detector upon receipt of a treatment event with a
	 ** particular agent.  Should this really be an Agent?   We don't want it to be
	 ** Administrable, which is a <I>particular</I> administration.
	 ** It has to be an abstraction, which Agent is.
	 **
	 ** A DetectorStarter will automatically disable itself.
	 ** If you want to turn it back on, use setEnabled(true).
	 **/

protected abstract class StarterWhenAgentGiven extends ClinicalStarter {

	Agent starterAgent = null;
	
	protected StarterWhenAgentGiven(AbstractPatient starterPatient, Agent agent) {
		super(starterPatient);
//		Logger.log("Constructing StarterWhenAgentGiven for agent " + agent.name + " class=" + getClass().getName());
		this.starterAgent = agent;
	}

	public void update ( java.util.Observable oncReporter, Object args)
	{
		OncEvent oe = (OncEvent) args;
		if (getEnabled() == false) {
			return;
		}
		if (oe.getIntEventType() == OncEvent.TREATMENT) {
			//TreatmentEvent te = (TreatmentEvent)oe;
			//AbstractPatient patientTreated = te.getPatient();
			AbstractPatient patientTreated = ((AgentDistributor)oe.getArgument()).patient;
			if (patientTreated != this.thisPatient)
				Util.fatal ("StarterWhenAgentGiven:  wrong patient!!!"
									 + this.thisPatient + patientTreated);
			Agent agentDelivered = ((AgentDistributor)oe.getArgument()).agent;
			if (starterAgent == null || starterAgent == agentDelivered) {
				takeAction();
			}
		}
	}

	protected abstract void takeAction();
}

/** This can be used with diagnosisDetector and RecurrenceDetector and
 ** PartialREsponseDetector and CompleteResponseDetector.
 ** It cannot be used with REsponseDetector, which is not
 ** HasScheduler-- It listens to OncReporter.
 **/
protected class StarterOfHasScheduler extends StarterWhenAgentGiven {
	HasScheduler detectorToStart;
	double detectorInterval;

	protected StarterOfHasScheduler(AbstractPatient patient, Agent agent, HasScheduler detectorToStart, double detectorInterval) {
		super(patient, agent);
//		Logger.log("Constructing StarterOfHasScheduler for agent " + agent.name + " class=" + getClass().getName());
		this.detectorToStart = detectorToStart;
		this.detectorInterval = detectorInterval;
	}
	protected void takeAction() {
//		Logger.log("takeAction: scheduling a HasScheduler of class" + detectorToStart.getClass().getName());
		detectorToStart.setScheduler(new Scheduler((Schedulable)detectorToStart, "update", getMasterScheduler()));
		detectorToStart.getScheduler().setScheduleOffset(0.0);
		detectorToStart.getScheduler().addRecurrentEvent(getMasterScheduler().globalTime, detectorInterval);
		this.setEnabled(false);
	}
}

protected class AgentStarter extends ClinicalStarter {
	public AgentStarter (AbstractPatient patient) {
		super(patient);
		//agentStarterVector.add(this);
//		Logger.log("Constructing AgentStarter " + getClass().getName()
//			+ "   for patient " + patient.thisptnum			  );
	}
	public void update ( java.util.Observable oncReporter, Object args) {
		if (getEnabled() == false)
			return;
		OncEvent oe = (OncEvent) args;
		if (oe.getIntEventType() == OncEvent.RECURRENCE) {
			//RecurrenceEvent re = (RecurrenceEvent)oe;
			//AbstractPatient patientRecurred = re.getPatient();
			AbstractPatient patientRecurred = (AbstractPatient)oe.getArgument();
/*			if (patientRecurred != thisPatient)
				Util.fatal ("AgentStarter:  wrong patient!!!"
							+ thisPatient + patientRecurred);
*/
			
			String infoString = "";
			java.util.Hashtable properties = thisPatient.properties;
			java.util.Iterator enoom = properties.entrySet().iterator();
			while (enoom.hasNext()) {
			   java.util.Map.Entry map = (java.util.Map.Entry) enoom.next();
			   String key = (String)map.getKey();
			   String val = (String)map.getValue();
			   //val = val.substring(0,6);
			   infoString = infoString + " " + key + "=" +  val;
			}
			if (infoString != "")
				infoString = "  Baseline data: " + infoString;
			Logger.log(" AgentStarter " + this + " \n  reports ONTRIAL for patient" + thisPatient.thisptnum);
			getCTReporter().notifyObservers(
				new OncEvent(getMasterScheduler().globalTime, OncEvent.ONTRIAL,
							"Patient #" + thisPatient.thisptnum + infoString));
			// Aha, both "Patient on trial" printouts come from a
			// single call to this line.
			Logger.log("  ----   ");
			getReporter().notifyObservers(new OncEvent(getMasterScheduler().globalTime,
				"Patient is enrolled", "Patient #" + thisPatient.thisptnum + infoString ));

			thisRegimen.administer(thisPatient);
			this.setEnabled(false);
			recurrenceDetector.setBroadcast(false);
		}
	}
}
/**  Purpose: setup an Observer of OncReporter that will start something
 **  (e.g. a treatment, treatment regimen, lab test, or a
 **  TumorSizeDetector).
 **
 **  Ths class is abstract because update() is not defined. 
 **/
protected abstract class ClinicalStarter implements Observer {
	AbstractPatient thisPatient;
	/** CAREFUL- this class does not guarantee that the "enabled" flag
	 ** is respected.  That must be done in the update() method.
	 **/
	boolean enabled = true;
	/** Constructor
	 **/
	protected ClinicalStarter(AbstractPatient patient, boolean b) {
		this(patient);
		setEnabled(b);
	}
	protected ClinicalStarter(AbstractPatient patient) {
		getReporter().addObserver(this);
		thisPatient = patient;
		thisPatient.myOncReporterObservers.add(this);
	}
	protected void setEnabled(boolean b){
		enabled = b;
	}
	protected boolean getEnabled(){
		return(enabled);
	}
}

public boolean tooManyToxDeaths(int acceptableDeaths)
{
	if (nToxDeaths > acceptableDeaths)
	{
		return(true);
	}
	else
	{
		return(false);
	}
}




	public void addOffTrialDueToMet(AbstractPatient patient){}

	protected class OffTrialDueToMet implements Observer
	{
		int maxToxDeaths;
		AbstractPatient patient;
		Regimen localRegimen;
		
		public OffTrialDueToMet(AbstractPatient patient, Regimen r)
		{
			getReporter().addObserver(this);
			this.patient = patient;
			localRegimen = r;
			patient.addOncReporterObserver(this);
			
		}
		public void update(java.util.Observable obs, Object arg) {
			if ( !patient.getOffTreatment() && (arg instanceof OncEvent)){
				OncEvent oncEvent = (OncEvent)arg;
				if (oncEvent.getIntEventType()==OncEvent.MET)
				{
					localRegimen.removeAllDoses();
					patient.setOffTreatment(true);
					getCTReporter().notifyObservers(OncEvent.OFFTREATMENT,"due to metastasis", getMasterScheduler().globalTime);
					getReporter().notifyObservers(OncEvent.OFFTREATMENT,"due to metastasis", getMasterScheduler().globalTime);
					getReporter().notifyObservers(OncEvent.ENDOFTREATMENT, "", getMasterScheduler().globalTime);
					//patient.patientHistoryIsTerminated();
				}
			}
		}
	}
}
