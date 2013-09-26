#include "build.h"
#ifdef DLL
#include <windows.h>
#endif
#include "defines.h"
#include "classes.h"
#include "cellp.h"
#include "tox.h"
#ifndef INFINITE
#define INFINITE            0xFFFFFFFF  
#endif

/*	Filename	:TreatmentExistsInt.c 
	Author		: Sai
	Date		:07/21/97
	Comments	:Checks if a treatment exists for agentindex within time points fromTime
				 and toTime and returns the time of the first encountered treatment time
*/


double TreatmentExistsInt(int agentindex, double fromTime, double toTime)
{
	int i;
	double minval;

	minval = INFINITE;

	for(i=1;i<=ndoses[agentindex];i++)
	{
		if ((doselist[agentindex][i] > fromTime)  && (doselist[agentindex][i] < (toTime+delta_t/2)))
		{
			if (minval > doselist[agentindex][i])
			{
				minval = doselist[agentindex][i];
			}
		}
	}
if (minval == INFINITE)
   minval = -1;

return(minval);
}