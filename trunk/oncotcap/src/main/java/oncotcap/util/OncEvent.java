package oncotcap.util;

public class OncEvent
{
	private double time;
	private String eventType = null;
	private Object arg;
	private int intEventType = 0;

	public static final int NEWPATIENT = 1;
	public static final int BEGINSIM = 2;
	public static final int TUMORSTART = 3;
	public static final int DIAGNOSIS = 4;
	public static final int CR = 5;
	public static final int COMPLETERESPONSE = 5;
	public static final int PR = 6;
	public static final int PARTIALRESPONSE = 6;
	public static final int CURE = 7;
	public static final int NORESPONSE = 8;
	public static final int RECURRENCE = 9;
	public static final int MET = 10;
	public static final int TOXRESOLVE = 11;
	public static final int TREATMENT = 12;
	public static final int TOXICITY = 13;
	public static final int DOSEMODIFICATION = 14;
	public static final int OFFTRIAL = 15;
	public static final int EFUTUM = 16;
	public static final int EFUNED = 17;
	public static final int STAGEONEFINISH = 18;
	public static final int DEATHDUETOTUMOR = 19;
	public static final int DEATHDUETOTOXICITY = 20;
	public static final int SIMFINISHED = 21;
	public static final int TRIALRESULTS = 22;
	public static final int TRIALDESIGN = 23;
	public static final int CELLCOUNT = 24;
	public static final int PATIENTGRAPHCREATED = 25;
	public static final int RESPONSE = 26;
	public static final int PROGRESSION = 27;
	public static final int ONTRIAL = 28;
	public static final int ENDOFTREATMENT = 29;
	public static final int OFFTREATMENT = 30;
	
	//any number not defined here is handled as a user defined event
											
	public OncEvent(double time, String eventType, Object arg)
	{
		this.time = time;
		this.eventType = eventType;
		this.arg = arg;
	}

	public OncEvent(double time, int eventType, Object arg)
	{
		this.time = time;
		this.intEventType = eventType;
		this.eventType = eventToString(eventType);
		this.arg = arg;
	}

	public double getTime()
	{
		return(time);
	}

	public String getEventType()
	{
		return(eventType);
	}
	public int getIntEventType()
	{
		return(intEventType);
	}
	public Object getArgument()
	{
		return(arg);
	}

	public void setTime(double time)
	{
		this.time = time;
	}

	public void setEventType(String eventType)
	{
		this.eventType = eventType;
	}

	public void setEventType(int eventType)
	{
		intEventType = eventType;
		this.eventType = eventToString(eventType);
	}

	public void setArgument(Object arg)
	{
		this.arg = arg;
	}

	public String toString()
	{
		return(arg.toString());
	}
	public static String eventToString(int eventType)
	{
		switch(eventType)
		{
			case NEWPATIENT:
				return("New Patient");
			case ONTRIAL:
				return("On Trial");
			case BEGINSIM:
				return("Begin Simulation");
			case TUMORSTART:
				return("Tumor Started");
			case DIAGNOSIS:
				return("Diagnosis");
			case CR:
				return("Complete Response");
			case RESPONSE:
				return("Response");
			case PR:
				return("Partial Response");
			case CURE:
				return("Cure");
			case NORESPONSE:
				return("No Response");
			case RECURRENCE:
				return("Recurrence");
			case MET:
				return("Metastasis");
			case TOXRESOLVE:
				return("Toxicity Resolved");
			case TREATMENT:
				return("Treatment");
			case TOXICITY:
				return("Toxicity");
			case DOSEMODIFICATION:
				return("Dose Modification");
			case OFFTRIAL:
				return("Off Trial");
			case EFUTUM:
				return("End Follow Up Tumor Present");
			case EFUNED:
				return("End Follow Up No Evidence of Disease");
			case STAGEONEFINISH:
				return("Stage One Finished");
			case DEATHDUETOTUMOR:
				return("Death due to tumor");
			case DEATHDUETOTOXICITY:
				return("Death due to toxicity");
			case SIMFINISHED:
				return("Simulation finished");
			case TRIALRESULTS:
				return("Clinical trial results");
			case TRIALDESIGN:
				return("Clinical trial design");
			case CELLCOUNT:
				return("Cell counts");
			case PATIENTGRAPHCREATED:
				return("Patient Graph");
			case PROGRESSION:
				return("Tumor Progression");
			case ENDOFTREATMENT:
				return("End of Treatment");
			case OFFTREATMENT:
				return("Patient off treatment");
			default:
				return("User Defined");
		}
	}
}