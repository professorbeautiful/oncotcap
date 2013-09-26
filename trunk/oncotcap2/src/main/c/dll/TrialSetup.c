#include <string.h>
#include "build.h"
#include "defines.h"

void TrialSetup()
{
	TrialSim = True;
	TSFirstTreatmentHappened = False;
	TSSecondTreatmentHappened= False;


	TrialInfo.CourseLength = 150;
	TrialInfo.Regimen.numTreatments = 2;
	TrialInfo.Regimen.Treatments[1].TreatIndex = 3;
	TrialInfo.Regimen.Treatments[1].time = 60.0;
	TrialInfo.Regimen.Treatments[1].dose = 1.0;
	TrialInfo.Regimen.Treatments[2].TreatIndex = 3;
	TrialInfo.Regimen.Treatments[2].time = 135;
	TrialInfo.Regimen.Treatments[2].dose = 0.5;
	TrialInfo.numToxModifications = 2;
	strcpy(TrialInfo.ToxCriteria[1].ToxType,"Cardiac");
	TrialInfo.ToxCriteria[1].ReductionGrade = 3;
	TrialInfo.ToxCriteria[1].Reduction = 75;
	TrialInfo.ToxCriteria[1].ReductionType = RESTOFCOURSE;
	strcpy(TrialInfo.ToxCriteria[2].ToxType,"Any");
	TrialInfo.ToxCriteria[2].ReductionGrade = 2;
	TrialInfo.ToxCriteria[2].Reduction = 25;
	TrialInfo.ToxCriteria[2].ReductionType = ALLREMAININGCOURSES;
	TrialInfo.MaxCourses = 6;
	TrialInfo.MaxProgression = 25;
	TrialInfo.ProgressionOnNewMets = False;
	TrialInfo.OffStudyGrade = 4;
/*
int TrialSim;			   //Boolean set to true when we run a Trial Simulation


struct TreatmentandTime {  //Structure copied from VB side
	char *Name;
	int Type;
	int TreatIndex;
	double time;
	double dose;
	int lineidx;
} TreatmentandTime;

struct CTReg {				//Structure copied from VB side (Regimen)
	char *Name;
	char *ShortName;
	struct TreatmentandTime Treatments[MAXTREATMENTS];
	int numTreatments;
} CTReg;

struct CTToxicityCriteria { //Structure to hold dose modification rules
	double Reduction;		//amount of reduction ( 0 - 1.0)
	double ReductionGrade;	//at what grade (>=) apply reduction 
	int ReductionType;		//Type of reduction - NEXT, RESTOFCOURSE, ALLREMAINING
	int ToxType;            //Toxicity type (toxicity index) -1 for "Any"
} CTToxicity;

struct CTPhase2Trial {			//Structure to hold all Trial Simulation parameters
	double CourseLength;		//How long til we repeat a course (in DAYS)
	struct CTReg Regimen;		//Regimen object of one course
	int numToxModifications;	//Number of Dose Modification Rules
	struct CTToxicityCriteria ToxCriteria[MAXTOXMODS];  //Structures to dose mod info (index starting at 1)
	int MaxCourses;				//Number of courses given til patient is taken off study
	int MaxProgression;			//% of increase in original tumor before patient is taken off study
	int ProgressionOnNewMets;	//boolean, patient taken off study or not for occurence of new mets
	int OffStudyGrade;			//Grade (>=) at which patient is taken off study
	int n1;						//number of patients run through stage 1
	int r1;						//stage one criteria for pass/fail
	int n;						//number of patients run through stage 2 (includes stage 1 patients)
	int r;						//stage two criteria for pass/fail
} CTPhase2Trial;

struct CTPhase2Trial TrialInfo;

  */

}