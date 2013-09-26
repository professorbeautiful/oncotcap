#include "build.h"
#ifdef DLL
#include <windows.h>
#endif
#include "defines.h"
#include "classes.h"
#include "cellp.h"
#include "tox.h"

void InitNextToxTime()
{
	int iagent,i;
	double nextTreatTime;

	/*  This code calculates the first treatment position so that the tox 
	    code is called */

	nextToxTime=EndT;
	for(iagent=1;iagent<=ndrugs;iagent++)/* Do for all drugs */
	{
		nextTreatTime = TreatmentExistsInt(iagent,0.0,EndT);
		if((nextTreatTime > 0.0) && (nextToxTime > nextTreatTime))
		{
			nextToxTime = nextTreatTime;
		}
	}

	for (i = 1; i <= nenvlist; i++)
	{
		if(envlist[i].e != 0)
			if (envlist[i].t < nextToxTime)
				nextToxTime = envlist[i].t;		
	}

		firstTreatmentTime = nextToxTime;

}