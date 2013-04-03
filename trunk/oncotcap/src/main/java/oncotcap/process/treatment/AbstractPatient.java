package oncotcap.process.treatment;

import oncotcap.*;
import oncotcap.process.*;
import oncotcap.process.cells.*;
import oncotcap.util.*;
import oncotcap.sim.OncReporter;
import oncotcap.sim.schedule.*;
import java.util.*;

abstract public class AbstractPatient extends OncProcess {

	public Hashtable properties = new Hashtable();
	private static int ptnum = 0;
	public int thisptnum;
	Vector patients = new Vector();
	private boolean offTreatment = false;
//	private DeathDueToCancerCellsDetector deathDetector;
//	public CellCollection cancerCellCollection = new CellCollection(this);

	
/*	public AbstractPatient()
	{
		deathDetector = new DeathDueToCancerCellsDetector(this);
		oncotcap.display.CTReporter.notifyObservers(OncEvent.NEWPATIENT,"Patient #" + (++ptnum));
		OncReporter.notifyObservers(OncEvent.NEWPATIENT,"Patient #" + (ptnum));
		thisptnum = ptnum;
		patients.add(thisptnum,this);
	}
	*/
	public AbstractPatient()
	{
		this.setTimeKeptLocally(true);
	}
	public AbstractPatient(OncParent parent, boolean now)
	{
		super(parent,now);
		this.setTimeKeptLocally(true);
		if (now)
			throw new Error("AbstractPatient:  the constructor AbstractPatient(Object parent, boolean now) is not ready to be used!");
	}	
	public AbstractPatient(OncParent parent)
	{
		super(parent);
		this.setTimeKeptLocally(true);
//		deathDetector = new DeathDueToCancerCellsDetector(this);
		getCTReporter().notifyObservers(OncEvent.NEWPATIENT,"Patient #" + (++ptnum), getMasterScheduler().globalTime);
		getReporter().notifyObservers(OncEvent.NEWPATIENT,"Patient #" + (ptnum), getMasterScheduler().globalTime);
		thisptnum = ptnum;
	}


	public Vector getCellCollections()
	{
		return(getCollections(AbstractCell.class));
	}
	
	public void addToxicityTargets() {
	}
	
	public AbstractOncologist getOncologist() {
		return ((AbstractOncologist) getParent());
	}
//public ClinicalTrial getClinicalTrial() {
//	return getOncologist().getClinicalTrial();
//}
	public String toString()
	{
		return(new String("Patient " + thisptnum));
	}
	/** Use changeProp to set person-level variables.
	 **/
	public void changeProp(String propToChange, String value) {
		properties.put(propToChange,value);
	}
	public void setProp(String propToChange, String value) {
		properties.put(propToChange,value);
	}
	public String getProp(String propToChange) {
		return ((String)properties.get(propToChange));
	}

	public void patientDiesDueToTumor() {
		getCTReporter().notifyObservers(OncEvent.DEATHDUETOTUMOR, "", getMasterScheduler().globalTime);
		getReporter().notifyObservers(OncEvent.DEATHDUETOTUMOR, "", getMasterScheduler().globalTime);
		patientHistoryIsTerminated();
	}

	public void patientDiesDueToTumor(String where) {
		getCTReporter().notifyObservers(OncEvent.DEATHDUETOTUMOR, where, getMasterScheduler().globalTime);
		getReporter().notifyObservers(OncEvent.DEATHDUETOTUMOR, where, getMasterScheduler().globalTime);
		patientHistoryIsTerminated();
	}
	public void patientDiesDueToToxicity(String where) {
		getCTReporter().notifyObservers(OncEvent.DEATHDUETOTOXICITY, where, getMasterScheduler().globalTime);
		getReporter().notifyObservers(OncEvent.DEATHDUETOTOXICITY, where, getMasterScheduler().globalTime);
		patientHistoryIsTerminated();
	}
	public void patientDiesDueToToxicity() {
		getCTReporter().notifyObservers(OncEvent.DEATHDUETOTOXICITY, "", getMasterScheduler().globalTime);
		getReporter().notifyObservers(OncEvent.DEATHDUETOTOXICITY, "", getMasterScheduler().globalTime);
		patientHistoryIsTerminated();
	}

	public void patientIsCensored() {
		getReporter().notifyObservers(new OncEvent(getMasterScheduler().globalTime, "Patient is censored", "Patient #" + (thisptnum)));
		getCTReporter().notifyObservers(new OncEvent(getMasterScheduler().globalTime, "Patient is censored", "Patient #" + (thisptnum)));
		patientHistoryIsTerminated();
	}

	public void patientHistoryIsTerminated() {
		getCTReporter().notifyObservers(OncEvent.OFFTRIAL,"Patient #" + (thisptnum), getMasterScheduler().globalTime);
		getReporter().notifyObservers(OncEvent.OFFTRIAL,"Patient #" + (thisptnum), getMasterScheduler().globalTime);

		/*Iterator iter = myOncReporterObservers.iterator();
		while (iter.hasNext())
			OncReporter.deleteObserver((Observer)iter.next());
		*/
		stopSimulation();
	}
	public static void resetPatientCount() {
		ptnum = 0;
	}
	public void scheduleTumorScans() {
	}
	public void schedulePharmacokineticsSampling() {
	//dummy method, filled in by codebundle if chosen.
	}
	public void setOffTreatment(boolean offTreatmentStatus)
	{
		offTreatment = offTreatmentStatus;
	}

	public boolean getOffTreatment()
	{
		return(offTreatment);
	}

/*	public void resetDeltaTAtTreatmentEnd(double newDeltaT)
	{
		CheckForEndOfTreatment te = new CheckForEndOfTreatment(newDeltaT);
	}

	class CheckForEndOfTreatment implements Observer
	{
		double dt;
		CheckForEndOfTreatment(double dt)
		{
			OncReporter.addObserver(this);
			addOncReporterObserver(this);
			this.dt = dt;
		}

		public void update(Observable obs, Object arg)
		{
			if ( arg instanceof OncEvent )
			{
				OncEvent oe = (OncEvent) arg;
				if(oe.getIntEventType() == OncEvent.ENDOFTREATMENT)
				{
					Logger.log("Caught end of treatment event, setting delta T to "
									  + dt);
					cancerCellCollection.setDeltaT(dt);
				}
			}

		}

	}
*/
}