#include "build.h"
#ifdef DLL
#include <windows.h>
#endif
#include "build.h"
#include "defines.h"
#include "classes.h"
#include "cellp.h"
#include "tox.h"

extern int fill_para();
double gnorm0();
double norm();

void reset_events()
{
/*	int iQ; */
	reset_sim();
//	iCellQHead = 0;
//	iCellQTail = 0;
//	iEventQHead = 0;
//	iEventQTail = 0;
//    CNsave.cellcount = NULL;
}

void reset_sim()
{
	int i;
	double retval;

	for(i=1;i<= ndrugs;i++)
	{
		ndoses[i] = nSavedDoses[i];
	}

	Dead = False;
	Cured = False;
	EndSim = False;
	LocationClass = getlocationclass();

	retval=gnorm0(TRUE);
	retval = norm(TRUE);

	for (i = 0; i <= MAXLEVELS; i++)
	{
		previousCellCount[i] = 0;
		previousCellCountAtEval[i] = 0;
		ResponseHappened[i] = 0;
		CellCountAtFirstTreatment[i] = 0;
		DiagnosedOnce[i] = False;
		CurrentlyDiagnosed[i] = False;
	}
	for (i=1; i<=nToxTypes; i++)
	{
		ToxTypes[i].currGrade = 0;
		ToxTypes[i].lastToxTime = 0.0;
	}

	PartialResponse.event = False;
	PartialResponse.checked = False;
	CompleteResponse.event = False;
	CompleteResponse.checked = False;
    CombinedTumorSizeAtFirstTreatment = 0.0;
	previousCombinedTumorSize = 0.0;
	AfterFirstTreatment = False;
	PrimaryDiagnosed = False;
	PrimaryTumor = -1;
	DiagnosedNow = False;
	if ((TrialSim == True)  && (TSSecondTreatmentHappened== True))
	{
		evaluate_schedule("n",TrialInfo.Regimen.Treatments[1].TreatIndex);
	}
	TSFirstTreatmentHappened = False;
	TSSecondTreatmentHappened= False;
	numCTTreatments = 0;
	TreatmentCount = 0;
	OffTrial = False;
	HasGuaranteeTimeChecked = False;
	GuaranteeReset = False;

}
