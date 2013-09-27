#include <stdlib.h>
#include <windows.h>
#include <stdio.h>
#include "build.h"
#include "defines.h"
#include "classes.h"
#include "cellp.h"
#include "msim.h"
#include "logger.h"

extern int EXPORT PASCAL ResetMultiCellP()
{
	long lngRetVal;
	fprintf(logfile,"%s\tfunction:ResetMultiCellP.ResetMultiCellP\n", gettime());
	lngRetVal = InterlockedExchange(& ((long) SleepTime), ((long) 0) );
	EndSim = True;
	MEndSim = True;
	return(0);
}
