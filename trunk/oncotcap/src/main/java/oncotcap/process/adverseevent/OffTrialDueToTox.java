package oncotcap.process.adverseevent;

import oncotcap.process.treatment.*;
import oncotcap.sim.OncReporter;
import oncotcap.util.*;

public class OffTrialDueToTox extends ToxMonitor {
	
	public OffTrialDueToTox(AbstractPatient patient, String toxicityType, int toxGrade){
		super (patient, toxicityType, toxGrade);
	}
	
	void actionToTake() {
		getPatient().getCTReporter().notifyObservers(OncEvent.OFFTRIAL, "Due to high toxicity", getPatient().getMasterScheduler().globalTime);
		getPatient().getReporter().notifyObservers(OncEvent.OFFTRIAL, "Due to high toxicity", getPatient().getMasterScheduler().globalTime);
		thisPatient.stopSimulation();
	}

	
}
	
	

