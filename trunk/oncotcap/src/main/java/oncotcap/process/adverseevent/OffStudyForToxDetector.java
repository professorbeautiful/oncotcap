package oncotcap.process.adverseevent;

import oncotcap.sim.OncReporter;
import oncotcap.util.*;
import oncotcap.process.treatment.*;

public class OffStudyForToxDetector implements java.util.Observer
{
	private int grade;
	private AbstractPatient patient;
	private Regimen regimen;
	
	public OffStudyForToxDetector (AbstractPatient patient, int grade, Regimen regimen)
	{
		patient.getReporter().addObserver(this);
		patient.myOncReporterObservers.add(this);
		this.grade = grade;
		this.patient = patient;
		this.regimen = regimen;
	}
	public void update(java.util.Observable obs, Object arg) {
		if ( !patient.getOffTreatment() && (arg instanceof ToxEvent))
		{
			int cTox = ((ToxEvent) arg).getToxGrade();
			//don't bother if it is grade 5
			if (cTox >= grade && cTox < 5)
			{
				patient.getCTReporter().notifyObservers(OncEvent.OFFTREATMENT,"Due to a toxicity of grade " + cTox, patient.getMasterScheduler().globalTime);
				patient.getReporter().notifyObservers(OncEvent.OFFTREATMENT,"Due to a toxicity of grade " + cTox, patient.getMasterScheduler().globalTime);
				patient.setOffTreatment(true);
				regimen.removeAllDoses();
				patient.getReporter().notifyObservers(OncEvent.ENDOFTREATMENT, "", patient.getMasterScheduler().globalTime);
				
				//Util.exitSim();
				//patient.terminateOncObject();
			}
		}
	}
}
