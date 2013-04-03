package oncotcap.process.cells;

import oncotcap.util.*;
import oncotcap.sim.OncReporter;
import oncotcap.sim.schedule.*;
import oncotcap.process.treatment.*;
import java.util.Observable;

public class ProgressionDetector extends TumorSizeDetector {

	private double percentIncrease;
	private boolean threshholdSet = true;
	private Regimen regimen;
	
	public ProgressionDetector(AbstractPatient patient, double percentIncrease, Regimen regimen) {
		super(patient);
		scheduler = new Scheduler(this, "update", patient.getMasterScheduler());
		this.percentIncrease = percentIncrease;
		this.regimen = regimen;
		setThreshhold();
		if (getCellCount() < 1e7)
			threshholdSet = false;
		patient.getReporter().addObserver(this);
		patient.addOncReporterObserver(this);
	}
	public void update() {
		if ( !patient.getOffTreatment() && threshholdSet && (Math.pow(getCellCount(), 0.66666666666666667) >= threshhold) ) {
			getPatient().getCTReporter().notifyObservers(OncEvent.OFFTREATMENT,"Due to progression of tumor", getPatient().getMasterScheduler().globalTime);
			getPatient().getReporter().notifyObservers(OncEvent.OFFTREATMENT,"Due to progression of tumor", getPatient().getMasterScheduler().globalTime);
			regimen.removeAllDoses();
			patient.setOffTreatment(true);
			getPatient().getReporter().notifyObservers(OncEvent.ENDOFTREATMENT, "", getPatient().getMasterScheduler().globalTime);
//			Util.exitSim();
//			patient.terminateOncProcess();
		}
	}

	private void setThreshhold()
	{
		setThreshHold(Math.pow(getCellCount(), 0.66666666666666667) * (1.0 + percentIncrease/100.0));
	}
	
	public void update(Observable obs, Object arg) {
		if(!threshholdSet && (obs instanceof OncReporterObservable))
		{
			if (  (((OncEvent) arg).getIntEventType() == OncEvent.TREATMENT) )
			{
				threshholdSet = true;
				setThreshhold();
				update();
			}
		}

	}
}