package oncotcap.process.cells;

import oncotcap.sim.OncReporter;
import oncotcap.util.*;
import oncotcap.process.treatment.AbstractPatient;
import java.util.Observable;

/** RecurrenceDetector inherits Schedulable and also
 ** has mustBeReset = true.
 **/
public class RecurrenceDetector extends TumorSizeDetector {
	boolean patientIsNowInCompleteResponse = false;
	
	public RecurrenceDetector(AbstractPatient patient) {
		super(patient);
		patient.getReporter().addObserver(this);
	}
	/**  Used by MasterScheduler.
	 **/
	public void update() {

		if (getBroadcast()==true
			  && getCellCount() > threshhold
			  /*&& patientIsNowInCompleteResponse*/) {
			getPatient().getCTReporter().notifyObservers(OncEvent.RECURRENCE,patient,patient.getMasterScheduler().globalTime);
			getPatient().getReporter().notifyObservers(OncEvent.RECURRENCE,patient,patient.getMasterScheduler().globalTime);
			notifyObservers(patient);
			patientIsNowInCompleteResponse = false;
				// This will disArm this detector. 
		}
	}
	/**  Used by an Observable.
	 **/
	public void update(Observable obs, Object arg)
	{
		if ( ((OncEvent) arg).getIntEventType() == OncEvent.CR)
		{
			patientIsNowInCompleteResponse = true;
		}
	}
}