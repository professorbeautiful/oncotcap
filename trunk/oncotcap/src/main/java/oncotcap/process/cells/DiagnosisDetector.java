package oncotcap.process.cells;

import oncotcap.sim.OncReporter;
import oncotcap.util.*;
import oncotcap.process.treatment.AbstractPatient;
import java.util.Observable;

/** RecurrenceDetector inherits Schedulable and also
 ** has mustBeReset = true.
 **/
public class DiagnosisDetector extends TumorSizeDetector {

	boolean diagnosisHappened = false;
	
	public DiagnosisDetector(AbstractPatient patient) {
		super(patient);
		//setMustBeReset(true);
		//reArm();
	}
	/**  Used by MasterScheduler.
	 **/
	public void update() {
		if ( (getCellCount() > threshhold) && !diagnosisHappened ) {
			getPatient().getCTReporter().notifyObservers(OncEvent.DIAGNOSIS,patient,patient.getMasterScheduler().globalTime);
			getPatient().getReporter().notifyObservers(OncEvent.DIAGNOSIS,patient,patient.getMasterScheduler().globalTime);
			notifyObservers(patient);
			diagnosisHappened = true;
		}

	}
	/**  Used by an Observable.
	 **/
	public void update(Observable obs, Object arg) {

	}
}