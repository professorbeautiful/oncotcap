package oncotcap.process.cells;

import oncotcap.util.*;
import oncotcap.sim.OncReporter;
import oncotcap.sim.schedule.*;
import oncotcap.process.treatment.AbstractPatient;
import java.util.*;

public class ResponseDetector implements Observer
{
	
	private double scheduleOffset = -1;
	private CompleteResponseDetector completeResponseDetector;
	private PartialResponseDetector partialResponseDetector;
	private int prs = 0;
	private int crs = 0;
	private double firstResponseTime = -1.0;
	private static int rn = 0;
	private boolean didit = false;
	private AbstractPatient patient;
	
	public ResponseDetector(AbstractPatient patient, double threshhold,
							double checkInterval, double prResponseLevel)
	{
		rn++;
		this.patient = patient;
		patient.getReporter().addObserver(this);
		patient.myOncReporterObservers.add(this);
		//Necessary because ResponseDetector does NOT extend
		//TumorSizeDetector
		
		completeResponseDetector =  new CompleteResponseDetector(patient);
		completeResponseDetector.setMustBeReset(false);
		completeResponseDetector.setScheduleOffset(0.0);
		completeResponseDetector.setThreshHold(threshhold);
		completeResponseDetector.setScheduler(new Scheduler(completeResponseDetector, "update", patient.getMasterScheduler()));
		completeResponseDetector.scheduler.setScheduleOffset(0.0);
		completeResponseDetector.getScheduler().addRecurrentEvent(patient.getMasterScheduler().globalTime,checkInterval);
		completeResponseDetector.addObserver(this);
		
		partialResponseDetector =  new PartialResponseDetector(patient, prResponseLevel);
		partialResponseDetector.setMustBeReset(false);
		partialResponseDetector.setScheduleOffset(0.0);
		partialResponseDetector.setScheduler(new Scheduler(partialResponseDetector, "update", patient.getMasterScheduler()));
		partialResponseDetector.scheduler.setScheduleOffset(0.0);
		partialResponseDetector.getScheduler().addRecurrentEvent(patient.getMasterScheduler().globalTime,checkInterval);		
		partialResponseDetector.addObserver(this);

		
	}

	public void update(Observable obs, Object arg)
	{
		if(obs instanceof CompleteResponseDetector)
		{
			if(firstResponseTime < 0 || crs == 0)
				firstResponseTime = patient.getMasterScheduler().globalTime;
			crs++;


		}
		else if(obs instanceof PartialResponseDetector)
		{
			prs++;
			if(firstResponseTime < 0)
				firstResponseTime = patient.getMasterScheduler().globalTime;

		}
		else if(obs instanceof OncReporterObservable)
		{
			// CAREFUL___ is this too late??
			int eventType = ((OncEvent) arg).getIntEventType();
			if ( eventType == OncEvent.OFFTRIAL || eventType == OncEvent.DEATHDUETOTUMOR || eventType == OncEvent.DEATHDUETOTOXICITY)
			{
				if((crs > 0 || prs > 0) && ! didit)
				{
					didit = true;
					patient.getCTReporter().notifyObservers(new OncEvent(firstResponseTime, OncEvent.RESPONSE, ""));
					patient.getReporter().notifyObservers(new OncEvent(firstResponseTime, OncEvent.RESPONSE, ""));
				}
			}
		}
	}

	public double getScheduleOffset()
	{
		return(this.scheduleOffset);
	}
	public void setScheduleOffset(double offset)
	{
		this.scheduleOffset = offset;
	}

}