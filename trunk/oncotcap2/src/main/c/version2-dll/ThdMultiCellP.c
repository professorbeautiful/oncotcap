#include "build.h"
#include <windows.h>
#include <malloc.h>
#include "defines.h"
#include "classes.h"
#include "cellp.h"
#include "msim.h"
#include "tox.h"

extern int EXPORT PASCAL ThreadedMultiCellP(
									   double delT,
									   double simStart,
									   double simEnd,
									   double LRDiagThresh,
									   double PEDiagThresh,
									   double DeathThresh,
									   double RespThresh,
									   int pgap,
									   int reps,
									   int OrganMetOption,
									   int InitOption,
									   double TumorExamInterval,
									   double TumorCombFactor,
									   int RNGOption,
									   PFI PlottingFnc,
									   PFI EventFnc)
{

//	int answer;
	int i;
	extern int OrganMet(int,unsigned int,double,int),OrganMetGroup(int,unsigned int,double,int);


	MEndSim = False;
	EndSim = False;

	//do some initialization stuff
	delta_t = delT;
	StartT = simStart;
	EndT = simEnd;
	LR_diagnosis_threshold = LRDiagThresh;
	PE_diagnosis_threshold = PEDiagThresh;
	death_threshold = DeathThresh;
    repetitions = reps;
	//PrintGap = pgap;
	response_threshold = RespThresh;
	InitialOption = InitOption;
//	PlotOption = PLOTTOTALS;
	TumorExaminationInterval = TumorExamInterval;
	TumorCombinationFactor = TumorCombFactor;
    PlotData = PlottingFnc;
	PostEvent = EventFnc;
	
	OrganMetOpt = OrganMetOption;

	if ( OrganMetOpt == NONRAND ) {
		OrganFunc = OrganMet;
	//	RandFunc = ranmarm;
	//ranrmarin(ranSeed1, ranSeed2,CNRAND);
	 }
	 else {
		  OrganFunc = OrganMetGroup;
		  MaxGroup=MAXGROUP,MaxDrop=MAXDROP;
		  fillgap = Class[1].no_levels/MaxGroup;
	 }


//	RandFunc =rand01;
//	 RandFunc = ranmarm;


	for (i=0;i<3;i++)
	{
		SeedList[i] = (int *) calloc(reps,sizeof(int));
	}

	for(i=1;i<= ndrugs;i++)
	{
		nSavedDoses[i] = ndoses[i];
	}


	nrepetitions = 2;
	reset_events();

	MultiSim(RNGOption);
	return(0);
}