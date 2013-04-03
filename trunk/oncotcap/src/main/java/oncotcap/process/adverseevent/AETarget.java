package oncotcap.process.adverseevent;

import java.util.*;
import oncotcap.util.*;
import oncotcap.sim.OncReporter;
import oncotcap.sim.schedule.*;
import oncotcap.process.treatment.*;

/**
 *	Adverse event target class.  This class watches agent
 *	concentrations and generates a status that can be converted to a
 *	toxicity grade.
 */
public abstract class AETarget implements Observer, Schedulable, ObservationOfAPatient
{
	AgentDistributor agentDist;
	double gap;
	private double scheduleOffset = 0.0;
	Scheduler sched =  null;
	boolean started = false;
	double fStatus;
	double maxFunctionStatus = 100;
	boolean treatmentEnded = false;
	private boolean firstcaught = false;
	
	AETarget(AgentDistributor agentDist, double updateGap)
	{
		this.agentDist = agentDist;
		this.gap = updateGap;
		agentDist.getPatient().getReporter().addObserver(this);
		agentDist.getPatient().addOncReporterObserver(this);
	}

	abstract public double functionStatus();

	public void start()
	{
		if (sched == null)
		{
			sched = new Scheduler(this,"update", agentDist.getPatient().getMasterScheduler());
			sched.setScheduleOffset(0.0);
		}
		
		sched.addRecurrentEvent(agentDist.getPatient().getMasterScheduler().globalTime, gap);
		started = true;
//		Logger.log("AETarget started");
	}
/*	public void update()
	{
//		fStatus = functionStatus();
	}*/

	public double getFunctionStatus()
	{
		return(fStatus);
	}

	public void setFunctionStatus(double fStatus)
	{
		this.fStatus = fStatus;
	}

	public Object getObservation()
	{
		return(new Double(fStatus));
	}


	//start when the first treatment is given
	public void update(Observable o, Object arg)
	{
		if (arg instanceof OncEvent)
		{
			OncEvent oe = (OncEvent) arg;
			if(!started && (oe.getIntEventType() == OncEvent.TREATMENT))
			{
				if (firstcaught)
				{
					treatmentEnded = false;
					start();
					firstcaught = false;
				}
				else
					firstcaught = true;
			}
			else if(started && (oe.getIntEventType() == OncEvent.ENDOFTREATMENT))
			{
				treatmentEnded = true;
			}
		} 
	}

	public void stop()
	{
		if (sched != null)
		{
			sched.endRecurrentEvent();
			started = false;
//			Logger.log("AETarget stopped");
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
	public void setMaxFunctionStatus(double maxval)
	{
		maxFunctionStatus = maxval;
	}


	public void setGap(double updateGap)
	{
		gap = updateGap;
		if (sched != null)
			sched.setGap(updateGap);
	}
}