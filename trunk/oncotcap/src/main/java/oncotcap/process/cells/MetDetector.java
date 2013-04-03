package oncotcap.process.cells;

import oncotcap.util.*;
import oncotcap.process.treatment.AbstractPatient;
import java.util.Observable;

import oncotcap.sim.OncReporter;
import oncotcap.sim.schedule.*;

public class MetDetector extends TumorSizeDetector {

	private static final int NED = 1;
	private static final int NOT_NED = 2;
	
	OncEnum organ;
	private int state;
	public MetDetector(AbstractPatient patient, double threshhold, OncEnum organ) {
		super(patient);
		setThreshHold(threshhold);
		setCellType(organ);
		this.organ = organ;

		if (getCellCount() > threshhold)
			state = NOT_NED;
		else
			state = NED;
		
		scheduler = new Scheduler(this, "update", patient.getMasterScheduler());
		setScheduleOffset(0.0);
		scheduler.setScheduleOffset(0.0);
		scheduler.addRecurrentEvent(patient.getMasterScheduler().globalTime, 0.5);
		patient.getReporter().addObserver(this);

	}

	public void update() {
//		Logger.log("==========================MetDetector update1");
		if(getCellCount() > threshhold)
		{
//			Logger.log("==========================MetDetector update2");
			if ( state == NED)
			{
				Logger.log("Sending met event for " + organ + " time = " + patient.getMasterScheduler().globalTime);
				getPatient().getCTReporter().notifyObservers(OncEvent.MET,new MetInfo(patient, organ), patient.getMasterScheduler().globalTime);
				getPatient().getReporter().notifyObservers(OncEvent.MET,new MetInfo(patient, organ),patient.getMasterScheduler().globalTime);
				state = NOT_NED;

			}
		}
		else
		{
//			Logger.log("==========================MetDetector update3");
			state = NED;
		}

	}

	public void update(Observable obs, Object arg) {

	}
}