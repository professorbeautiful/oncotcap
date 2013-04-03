package oncotcap.util;

import oncotcap.process.treatment.AbstractPatient;
import oncotcap.sim.schedule.MasterScheduler;

public class ToxResolveEvent extends OncEvent
{

	AbstractPatient thisPatient;
	String thisToxicityType;
	int thisToxGrade;

	public ToxResolveEvent(AbstractPatient patient, String toxicityType, int toxGrade) {
		super(patient.getMasterScheduler().globalTime, OncEvent.TOXRESOLVE, null);
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
