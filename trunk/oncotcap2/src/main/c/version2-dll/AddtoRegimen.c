#include <stdlib.h>
#include <windows.h>
#include <memory.h>
#include "build.h"
#include "defines.h"
#include "classes.h"
#include "cellp.h"
#include "regimen.h"
#include "BBoard.h"


int AddtoRegimen(void *args[8])
{
	int *ntreat;
	int *treatIndex;
	int *agentIndex;
	int *numTimes;
	double *interval;
	double *dose;
	
	double startTime; 
	int retval;

	ntreat = args[0];
	treatIndex = args[1];
	agentIndex = args[2];
	interval = args[4];
	numTimes = args[3];
	dose = args[5];
	
	startTime = t;

	retval = AddtoRegimenA(*ntreat,*treatIndex, *agentIndex,*numTimes,*interval,startTime,*dose);
	return(retval);
}





int AddtoRegimenA(int ntreat,
				  int treatIndex,
				  int agentIndex,
   			      int numTimes, 
				  double interval, 
				  double startTime, 
				  double dose)
{
//	int i,j;

	Regimens[0].startTime[ntreat] = startTime;
	Regimens[0].numRepetitions[ntreat] =  numTimes;
	Regimens[0].treatDose[ntreat] = dose;
	Regimens[0].Interval[ntreat] = interval;
	Regimens[0].treatIndex[ntreat] = treatIndex;
	Regimens[0].agentIndex[ntreat]= agentIndex;
	Regimens[0].numTreats = ntreat;

/* this code sets the starttime of treats with same treatindex the same
	for(i = 1 ; i<= ntreat; i++)
	{
		for (j= i+1;j<=ntreat;j++)
		{
			if (Regimens[0].treatIndex[i] == Regimens[0].treatIndex[j])
			{
				Regimens[0].startTime[j] = Regimens[0].startTime[i];
			}
		}
	}
*/
	return(0);
}