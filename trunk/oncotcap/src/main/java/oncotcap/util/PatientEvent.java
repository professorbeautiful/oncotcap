package oncotcap.util;

import oncotcap.process.treatment.AbstractPatient;
import oncotcap.sim.schedule.MasterScheduler;

abstract public class PatientEvent extends OncEvent
{

	AbstractPatient thisPatient;

	public PatientEvent(AbstractPatient patient, int eventType, String eventString) {
		super(patient.getMasterScheduler().globalTime, eventType, eventString);
		thisPatient = patient;
	}

	AbstractPatient getPatient() {
		return thisPatient;
	}
}
