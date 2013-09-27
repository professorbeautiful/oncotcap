#include <stdlib.h>
#include <windows.h>
#include <stdio.h>
#include "build.h"
#include "defines.h"
#include "classes.h"
#include "cellp.h"
#include "logger.h"

extern int EXPORT PASCAL ResetCellP()
{
	long lngRetVal;
	fprintf(logfile,"%s\tfunction:ResetCellP.ResetCellP\n", gettime());	
	lngRetVal = InterlockedExchange(& ((long) SleepTime), ((long) 0) );
    EndSim = True;
	return(0);

}
