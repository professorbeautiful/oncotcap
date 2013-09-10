#include "build.h"
#ifdef DLL
#include <windows.h>
#endif
#include <malloc.h>
#include <memory.h>
#include "defines.h"
#include "classes.h"
#include "cellp.h"
#include "tox.h"

void CalculateTimeKillSurv();
void InitToxTypes();

void ApplyTrialSched()
{
	int n,i,TimeCount[Ndrug+1],iindex,nowenvl;
	double j,dblDoses[MAXTREATMENTS][Ndrug+1], dblTimes[MAXTREATMENTS][Ndrug+1];
	char *times,stime[255];
    double DiscoveryTime, appTime,MC;
	double lognorm(double mean, double stddev);

	
	/*DiscoveryTime = lognorm(1.386294361,0.66666666);*/
	DiscoveryTime = lognorm(1.035,0.38); /* new value provided by qing shou on march 6th '98*/
/*//	AddToEventQ(t+DiscoveryTime,ONTRIALEVENT,".",".",".");
	//get info for a single course */
	if(TrialInfo.MaxCourses < 0)
		MC = ((EndT - t)/(TrialInfo.CourseLength/28.0));
	else
		MC = TrialInfo.MaxCourses;

	for (n=1; n<=Ndrug; n++)
	{
		TimeCount[n] = 0;
		for(i=1; i<=TrialInfo.Regimen.numTreatments; i++)
			if (TrialInfo.Regimen.Treatments[i].TreatIndex == n)
			{
				TimeCount[n]++;
				dblTimes[TimeCount[n]][n] = TrialInfo.Regimen.Treatments[i].time/28.0;
				dblDoses[TimeCount[n]][n] = TrialInfo.Regimen.Treatments[i].dose;
			/*	intAgentIndex[TimeCount[n]][n] = TrialInfo.Regimen.Treatments[i].TreatIndex;*/
			}
	}

	/*Clear all dose modifications*/
	nDoseMods = 0;
	
	/*make all courses*/
	for (n=1; n<=Ndrug; n++)
	{

		if (TimeCount[n] > 0)
		{
			times = malloc((unsigned int) (MC*TimeCount[n]*14));
			strcpy(times,"");
#ifdef DLL
			if(times == NULL)
				MsgBoxError("Out of Memory [ApplyTrialSched]");
#endif
			for(j=0.0; j<MC; j = j + 1.0)
				for(i=1; i<=TimeCount[n]; i++)
				{
					appTime = DiscoveryTime + t + dblTimes[i][n] + (j * (TrialInfo.CourseLength/28.0));
					numCTTreatments++;
					CTTreatment[numCTTreatments].Time = appTime;
					CTTreatment[numCTTreatments].AgentIndex = n;
					CTTreatment[numCTTreatments].CourseNum = (int) (j + 1);
					CTTreatment[numCTTreatments].Dose = dblDoses[i][n];
					CTTreatment[numCTTreatments].FullDose = dblDoses[i][n];					
					sprintf(stime,"%lf  \0", appTime);
					strcat(times, stime);
				}
				evaluate_schedule(times,n);
				free(times);
		}
	}

	for(n = 1; n<=numCTTreatments; n++)
	{
	#ifndef TESTMPI
			if (CTTreatment[n].AgentIndex > 2)
				MsgBoxWarning("CTTreatment > 2");

			cside_pdf(CTTreatment[n].AgentIndex, CTTreatment[n].Time, CTTreatment[n].Dose,CTTreatment[n].AgentIndex);
	#endif
	} 
	buildsched();
	sortsched();

	for ( iindex=1; iindex <=active_ntypes;iindex++) {	   
	   for (nowenvl = 0; nowenvl < nenvirons; nowenvl++)
	   {
		   buildkill(iindex,nowenvl);
		  setrates (iindex,nowenvl);
	   }
	}
/*	//apply doses

	//first makem all full strength
	for(i=1;i<=nsched;i++)
	{
		sched[i].df = 1.0;
	}

	//then apply them all

	//InitToxTypes();*/
	nextToxTime = DiscoveryTime + t;
}