#include "build.h"
#include <windows.h>
#include <math.h>
#include <stdio.h>
#include "defines.h"
#include "classes.h"
#include "cellp.h"
#include "msim.h"
#include "tox.h"


extern int EXPORT PASCAL ThreadedCellP(int PatientMode,
									   double delT,
									   double simStart,
									   double simEnd,
									   int PGap,
									   double LRDiagThresh,
									   double PEDiagThresh,
									   double DeathThresh,
									   double RespThresh,
									   int sleepy,								  
									   int PatientNum,
									   int OrganMetOption,
									   int InitOption,
									   double TumorExamInterval,
									   double TumorCombFactor,
									   double BeginAxisTime,
									   double EndAxisTime,
									   int RNGOption,
									   PFI PlottingFnc,
									   PFI EventFnc)
{

//	DWORD dwWaitResult;
//	int answer;
	extern int OrganMet(int,unsigned int,double,int),OrganMetGroup(int,unsigned int,double,int);
	extern void setInitialCellCounts();
	void SingleCellp();
//	extern double (*RandFunc)(int);
	FILE *sp;
	char seedfilename[255];
	int i;


	// if MSim is running, ask and kill it
	//do some initialization stuff
	delta_t = delT;
	StartT = simStart;
	EndT = simEnd;
	//PrintGap = 1;
	//PrintGap = PGap;
	LR_diagnosis_threshold = LRDiagThresh;
	PE_diagnosis_threshold = PEDiagThresh;
	death_threshold = DeathThresh;
    SleepTime = (DWORD) sleepy;
//	PlotOption = option;
	EndSim = False;
	response_threshold = RespThresh;
	TumorExaminationInterval = TumorExamInterval;
	TumorCombinationFactor = TumorCombFactor;
	BeginPlotTime = BeginAxisTime;
	EndPlotTime = EndAxisTime;
    PlotData = PlottingFnc;
	PostEvent = EventFnc;


	nrepetitions = 1;
	sprintf(seedfilename,"%s\\OncoTcap.log",WorkingDir);
	if ((	sp = fopen(seedfilename,"a")) == NULL ) {
		MessageBox(NULL,"There is a problem to open file.","treat.dll",MB_ICONEXCLAMATION | MB_OK);
		return (1);
	}
	if ( UserSetSeeds == false)
	{
		if (PatientNum == 0) 
		{
			if ((PatientMode == NEWPATIENT) || ((ranSeed1 == 0) && (ranSeed2 == 0)))
			{		
				srand( (unsigned)time( NULL ) );
				ranSeed1 = (int)(rand()* 31328*INV_RAND_MAX) ;
				ranSeed2 = (int)(rand()* 30081*INV_RAND_MAX) ;
				ranSeed3 = (unsigned int) (rand()* 31328*INV_RAND_MAX);
			}
		}
		else
		{
			ranSeed1 = SeedList[0][PatientNum-1];
			ranSeed2 = SeedList[1][PatientNum-1];
			ranSeed3 = (unsigned int ) SeedList[2][PatientNum-1];
		}
	}

	/* ranSeed1 = 22185;
	ranSeed2 = 9227; 
	ranSeed3=513; */

	IsMeanOnly = USERAND;
	if (RNGOption == USERAND) 
	{
		srand(ranSeed3);
		RandFunc = rand01;
	}
	else if (RNGOption == USERANDMAR)
	{
		ranrmarin(ranSeed1,ranSeed2,CNRAND);
		RandFunc = ranmarm;
	}
	else /* for setInitialCellCounts() */
	{
		ranrmarin(ranSeed1,ranSeed2,CNRAND);
		RandFunc = ranmarm;
	}

	for(i=1;i<= ndrugs;i++)
	{
		nSavedDoses[i] = ndoses[i];
	}

//	reset_events();
	setInitialCellCounts(InitOption,PatientMode,SIM,PatientNum);

	if (RNGOption == USEMEANONLY)
		IsMeanOnly = USEMEANONLY;

	/* used only for testing 
	 ranSeed1 = 6757;
	   ranSeed2 = 7886; 	 */

	OrganMetOpt = OrganMetOption;
	if ( OrganMetOpt == NONRAND ) {
		OrganFunc = OrganMet;
	 }
	 else {
		  OrganFunc = OrganMetGroup;
		  MaxGroup=MAXGROUP,MaxDrop=MAXDROP;
		  fillgap = Class[1].no_levels/MaxGroup;
	 }
	fprintf(sp,"\nseed1=%d,seed2=%d,seed3=%d,Total=%lf\n",ranSeed1,ranSeed2,ranSeed3,InitTotal);
	fclose(sp);
	//RandFunc = ranmarm;
	ranrmarin(ranSeed1,ranSeed2,METRAND);
	RandInit = False;

	SingleCellp();
	free(initCellVector);
	reset_events();
	return(0);
}