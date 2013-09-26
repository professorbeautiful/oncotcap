#include "build.h"

#include "defines.h"
#ifdef DLL
extern int EXPORT PASCAL InitDoseFactor()
#else
int InitDoseFactor()
#endif
{
	int i;
   buildsched();
   sortsched();

	for(i=1;i<=nsched;i++)
	{
		sched[i].df = 1.0;
	}
	CalculateTimeKillSurv();
	return(0);
}
