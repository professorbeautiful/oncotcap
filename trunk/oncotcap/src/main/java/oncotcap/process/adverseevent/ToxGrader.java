package oncotcap.process.adverseevent;

import oncotcap.process.treatment.AbstractPatient;
import oncotcap.util.*;
import oncotcap.sim.OncReporter;
import oncotcap.sim.schedule.*;

public class ToxGrader implements Schedulable
{
	private double scheduleOffset = 0;
	private ObservationOfAPatient target;
	private String toxicityType;
	private int currentGrade = 0;
	private AbstractPatient patient;
	public Scheduler scheduler; 
	double gradeThresholds[] = {80.0, 60.0, 40.0, 20.0, 0.01};

	public static void main(String [] args)
	{
		final String a = args[0];
/*		ToxGrader tg = new ToxGrader("Test Toxicity", new ObservationOfAPatient(){
			public Object getObservation(){ Logger.log("In TG");
											return(new Double(a));}
		});

		Logger.log(tg.getGrade()); */
	}
	public ToxGrader(String name, AbstractPatient patient, ObservationOfAPatient target)
	{
		this.toxicityType = name;
		this.target = target;
		this.patient = patient;
		scheduler = new Scheduler(this, "checkGrade", patient.getMasterScheduler());
	} 
	
	//over ride this function to provide grade level variablity
	double statusUpdate(Object status)
	{
		if(status instanceof Double)
			return( ((Double )status).doubleValue());
		else
			return(1.0);
	}

	public void checkGrade()
	{
		int i = getGrade();
	}
	public int getGrade()
	{
		int grade;
		grade = setGrade();
		// Report only significant changes (speeds up display)-- but
		// causes problems.  RD sept 18
		// NO, OK I think- just don't run from the icon!
		if(currentGrade != grade)
		{
			if (currentGrade < grade) // && grade >= 3)
			{
				patient.getCTReporter().notifyObservers(new ToxEvent(patient, toxicityType, grade));
				patient.getReporter().notifyObservers(new ToxEvent(patient, toxicityType, grade));
			}
			else
//				if (currentGrade > grade && currentGrade >= 3)
			{
				patient.getCTReporter().notifyObservers(new ToxResolveEvent(patient, toxicityType, grade));
				patient.getReporter().notifyObservers(new ToxResolveEvent(patient, toxicityType, grade));
			}
			currentGrade = grade;
		}
		return(currentGrade);
	}
	private int setGrade()
	{
		int i;
		for(i = 0; i < 5; i++)
			if( statusUpdate(target.getObservation()) >= gradeThresholds[i])
				return(i);

		return(5);
	}

	public String getName()
	{
		return(toxicityType);
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