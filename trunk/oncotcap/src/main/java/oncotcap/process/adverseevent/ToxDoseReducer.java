package oncotcap.process.adverseevent;

import oncotcap.process.treatment.*;
import oncotcap.util.*;

public class ToxDoseReducer extends ToxMonitor {

	double thisToxPctReduction;
	String thisReductionDuration;
	Regimen thisRegimen;
	private double totalPctReductionLevel = 100.0;
	
	public ToxDoseReducer(AbstractPatient patient, String toxicityType,
						  int toxGrade, double toxPctReduction, String reductionDuration,
						 Regimen regimen){
		super (patient, toxicityType,
			   toxGrade);
		thisReductionDuration = reductionDuration;
		thisToxPctReduction = toxPctReduction;
		thisRegimen = regimen;
	}
	
	void actionToTake() {
		if (thisReductionDuration.equals(" until toxicity resolves."))
			reduceUntilToxicityResolves();
		else if (thisReductionDuration.equals(" for all remaining courses."))
		{
			getPatient().getCTReporter().notifyObservers(OncEvent.DOSEMODIFICATION, "", getPatient().getMasterScheduler().globalTime);
			thisRegimen.reduceAllDoses(thisToxPctReduction);
		}
/*		else if (thisReductionDuration.equals(" until the next course."))
			reduceUntilNextCourse(); */
		else
		{
			Logger.log("WARNING: unrecognized plan for dosage reduction in ToxDoseReducer.actionToTake():"
							   + thisReductionDuration);
		}
	}
	void reduceUntilToxicityResolves(){
		getPatient().getCTReporter().notifyObservers(OncEvent.DOSEMODIFICATION, "", getPatient().getMasterScheduler().globalTime);
		thisRegimen.reduceAllDoses(thisToxPctReduction);
		totalPctReductionLevel = totalPctReductionLevel * (1.0 - thisToxPctReduction/100.0);
		ToxResolveMonitor tmResolve = new ToxResolveMonitor(thisPatient, thisToxicityType){
			void resolveActionToTake()
			{
				thisRegimen.increaseAllDoses(totalPctReductionLevel);
				totalPctReductionLevel = 100.0;
			}
		};
		
	}
}