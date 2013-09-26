#include "defines.h"
#include "regimen.h"
#include "BBoard.h"

void ClearBBRegimen()
{
	int i;


	for (i=0;i<=MAXDRUGS;i++)
	{
		Regimens[0].startTime[i] = 0.0;
		Regimens[0].numRepetitions[i] =  0;
		Regimens[0].treatDose[i] = 0.0;
		Regimens[0].Interval[i] = 0.0;
		Regimens[0].treatIndex[i] = 0;
		Regimens[0].agentIndex[i] = 0;
		Regimens[0].numTreats = 0;
	}
}
		