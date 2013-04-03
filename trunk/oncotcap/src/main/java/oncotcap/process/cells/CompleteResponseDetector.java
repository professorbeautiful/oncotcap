package oncotcap.process.cells;


import oncotcap.sim.OncReporter;
import oncotcap.util.*;
import oncotcap.process.treatment.AbstractPatient;
import java.util.Observable;

public class CompleteResponseDetector extends TumorSizeDetector {

	static final int NOT_NED = 0;
	static final int TENTATIVE = 1;
	static final int CONFIRMED = 2;
	static final int NED = 3;

	int state = NED;
	
//	double confirmInterval = 1.0;   //months, of course.
									// Not used yet. for now, just
									// successive observations.

	public CompleteResponseDetector(AbstractPatient patient) {
		super(patient);
		if (getCellCount() > threshhold)
			state = NED;
		else
			state = NOT_NED;
	}
	public void setThreshHold (double threshhold) {
		this.threshhold = threshhold;
		if (getCellCount() >= threshhold)
			state = NED;
		else
			state = NOT_NED;
	}

	public void update(Observable obs, Object arg) {

	}
	public void update() {
		if (getCellCount() >= threshhold)
			state = NOT_NED;
		
		if (getCellCount() < threshhold)
			if(state == NOT_NED  || state == TENTATIVE)
			{
				if (state == TENTATIVE)
				{
					state = NED;
					if(sendBroadcast)
					{
						getPatient().getCTReporter().notifyObservers(OncEvent.CR,"", patient.getMasterScheduler().globalTime);
						getPatient().getReporter().notifyObservers(OncEvent.CR,"", patient.getMasterScheduler().globalTime);
					}
					notifyObservers(patient);
				}
				else
					state = TENTATIVE;
			}

	}
}