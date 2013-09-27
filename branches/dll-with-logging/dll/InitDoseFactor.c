#include <stdio.h>
#include "build.h"

#include "defines.h"
#include "logger.h"
#ifdef DLL
extern int EXPORT PASCAL InitDoseFactor()
#else
int InitDoseFactor()
#endif
{
	int i;
	fprintf(logfile,"%s\tfunction:InitDoseFactor.InitDoseFactor\n", gettime());	
   buildsched();
   sortsched();

	for(i=1;i<=nsched;i++)
	{
		sched[i].df = 1.0;
	}
	CalculateTimeKillSurv();
	return(0);
}
