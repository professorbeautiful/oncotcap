package oncotcap.process.adverseevent;

import java.util.Observer;
import java.util.Observable;

import oncotcap.sim.OncReporter;
import oncotcap.util.*;
import oncotcap.process.treatment.*;

abstract public class ToxMonitor implements Observer{

	public static final int EQUAL = 0;
	public static final int GREATERTHANOREQUAL = 1;
	AbstractPatient thisPatient;
	String thisToxicityType;
	int thisToxGrade;
	private int compareMode;

	public ToxMonitor(AbstractPatient patient, String toxicityType, int toxGrade) {
		this(patient, toxicityType, toxGrade, GREATERTHANOREQUAL);
	}

	public ToxMonitor(AbstractPatient patient, String toxicityType, int toxGrade, int compareMode) {
		thisPatient = patient;
		thisToxicityType = new String(toxicityType);
		thisToxGrade = toxGrade;
		patient.getReporter().addObserver(this);
		patient.addOncReporterObserver(this);
		this.compareMode = compareMode;
	}

	protected AbstractPatient getPatient()
	{
		return(thisPatient);
	}
	abstract void actionToTake();

	public void update(Observable obs, Object arg)
	{
		if ( arg instanceof ToxEvent)
		{
			ToxEvent toxEvent = (ToxEvent)arg;
			if (toxEvent.getToxicityType().equalsIgnoreCase(thisToxicityType) && toxEvent.getToxGrade() >= thisToxGrade)
			{
				actionToTake();
			}
		}
	}
}

