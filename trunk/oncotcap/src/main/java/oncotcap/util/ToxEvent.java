package oncotcap.util;

import oncotcap.process.treatment.AbstractPatient;
import oncotcap.sim.schedule.MasterScheduler;

public class ToxEvent extends OncEvent
{

	AbstractPatient thisPatient;
	String thisToxicityType;
	int thisToxGrade;

	public ToxEvent(AbstractPatient patient, String toxicityType, int toxGrade) {
		super(patient.getMasterScheduler().globalTime, OncEvent.TOXICITY, null);
		thisPatient = patient;
		thisToxicityType = new String(toxicityType);
		thisToxGrade = toxGrade;
	}

	public String getToxicityType() {
		return thisToxicityType;
	}
	public int getToxGrade() {
		return thisToxGrade;
	}

	public String toString()
	{
		return(new String(thisToxicityType + " Grade " + thisToxGrade));
	}

	public Object getArgument()
	{
		return(toString());
	}

}
