package oncotcap.process.adverseevent;

import java.util.Observer;
import java.util.Observable;

import oncotcap.sim.OncReporter;
import oncotcap.util.*;
import oncotcap.process.treatment.*;

abstract public class ToxResolveMonitor implements Observer{

	AbstractPatient thisPatient;
	String thisToxicityType;

	public ToxResolveMonitor(AbstractPatient patient, String toxicityType) {
		thisPatient = patient;
		thisToxicityType = new String(toxicityType);
		patient.getReporter().addObserver(this);
		patient.addOncReporterObserver(this);
	}


	abstract void resolveActionToTake();

	public void update(Observable obs, Object arg)
	{
		if ( arg instanceof ToxResolveEvent)
		{
			ToxResolveEvent toxEvent = (ToxResolveEvent)arg;
			if(toxEvent.getToxicityType().equalsIgnoreCase(thisToxicityType) && toxEvent.getToxGrade() == 0)
			{
				resolveActionToTake();
			}
		}
	}
}


