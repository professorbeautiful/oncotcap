#include "build.h"
#ifdef DLL
#include <windows.h>
#endif
#include "defines.h"
#include "classes.h"
#include "cellp.h"
#include "tox.h"


/*	Filename	:TreatmentExists.c 
	Author		: Sai
	Date		:06/23/97
	Comments	:Checks if a treatment exist at checkTime for agent index and returns 1.0 or 0.0
				 Should return dose in future.

    Modifications
	------------------------------------------------------------------------------
	Date   Programmer Description
	------------------------------------------------------------------------------
	270298 Shirey     Added ability to search for any application of a treatment
	                  by giving a negative number for agentindex

    220998 Shirey     Made function return whole drugandtime struct instead of 
	                  just the dose.

*/


struct drugandtime TreatmentExists(int agentindex, double checkTime)
{

	int i;


	for(i=1;i<=nsched;i++)
	{
		if(((sched[i].d == agentindex) || (agentindex < 0 && sched[i].d > 0.0)) && 
			(sched[i].t <= checkTime) && 
			(checkTime < (sched[i].t + delta_t)))
		{
			return(sched[i]);
		}
	}
return(blanksched);
}