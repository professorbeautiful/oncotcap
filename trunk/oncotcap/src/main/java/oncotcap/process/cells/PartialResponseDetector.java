package oncotcap.process.cells;

import oncotcap.sim.OncReporter;
import oncotcap.util.*;
import oncotcap.process.treatment.AbstractPatient;
import java.util.*;

public class PartialResponseDetector extends TumorSizeDetector implements Observer
{
	static final int NOT_NED = 0;
	static final int TENTATIVE = 1;
	static final int CONFIRMED = 2;
	static final int NED = 3;

	int state = NED;

	private double percentResponseLevel = 0.0;
	private boolean threshholdSet = false;
	
	public PartialResponseDetector(AbstractPatient patient, double responseLevel) {
		super(patient);
		percentResponseLevel = responseLevel;
		setThreshHold();

		patient.getReporter().addObserver(this);
	}

	public void setThreshHold()
	{
		if (getCellCount() > 1e7)
			threshholdSet = true;
		threshhold = Math.pow(getCellCount(), 0.666666666666667) * (1.0 - percentResponseLevel);
//		Logger.log("PR threshhold set to " + threshhold + "  Time = " + MasterScheduler.globalTime);
	}

	public void setResponseLevel(double rl)
	{
		percentResponseLevel = rl;
		setThreshHold();
	}

	public void update(Observable obs, Object arg) {
		if(obs instanceof OncReporterObservable)
		{
			if ( !threshholdSet && (((OncEvent) arg).getIntEventType() == OncEvent.TREATMENT) )
			{
				setThreshHold();
				threshholdSet = true;
//				Logger.log("Set to true");
				state = NOT_NED;
				update();
			}
		}
	}
	public void update() {
		double currentSize = Math.pow(getCellCount(), 0.666666666666667);

//		Logger.log("In pr check " + MasterScheduler.globalTime);
		if (threshholdSet)
		{
//			Logger.log("Checking for PR size is " + currentSize + " needs to be less than " + threshhold + "\nstate = " + state);
			if (currentSize < threshhold)
			{
					if (state == TENTATIVE)
					{
						state = NED;
						if(sendBroadcast)
						{
							getPatient().getCTReporter().notifyObservers(OncEvent.PR,"", getPatient().getMasterScheduler().globalTime);
							getPatient().getReporter().notifyObservers(OncEvent.PR,"", getPatient().getMasterScheduler().globalTime);
//							Logger.log("!!!!!!!!!!Generated PR");
						}
						notifyObservers(patient);
					}
					else
					{
						state = TENTATIVE;
					}
			}
		}
/*		if (state == NED && currentSize >= threshhold)
		{
			state = NOT_NED;
		} */

		

	}
}