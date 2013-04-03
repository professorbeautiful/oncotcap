package oncotcap.process.cells;

import oncotcap.util.*;
import oncotcap.sim.OncReporter;
import oncotcap.sim.schedule.*;
import oncotcap.process.treatment.*;

public class DeathDueToCancerCellsDetector extends TumorSizeDetector {

	
	public DeathDueToCancerCellsDetector(AbstractPatient patient, double threshhold, double checkInterval) {
		super(patient);
		setScheduleOffset(0.0);
		scheduler = new Scheduler(this, "update", patient.getMasterScheduler());
		scheduler.setScheduleOffset(0.0);
		setThreshHold(threshhold);
		scheduler.addRecurrentEvent(patient.getMasterScheduler().globalTime, checkInterval);
	}
	public DeathDueToCancerCellsDetector(AbstractPatient patient) {
		this(patient, 1e12, 1.0);
	}
	
	public void update() {
		if ( getCellCount() >= threshhold)  {
			patient.getCTReporter().notifyObservers(OncEvent.DEATHDUETOTUMOR,"Tumor size " + new java.text.DecimalFormat("0.###E0").format(getCellCount()) + " cells",patient.getMasterScheduler().globalTime);
			patient.getReporter().notifyObservers(OncEvent.DEATHDUETOTUMOR,"Tumor size " + getCellCount(),patient.getMasterScheduler().globalTime);
			patient.stopSimulation();
		}
	}

	
//	public void update(Observable obs, Object arg) {}
}