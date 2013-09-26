#include <stdlib.h>
#include <windows.h>
#include "build.h"
#include "defines.h"
#include "classes.h"
#include "cellp.h"

extern int EXPORT PASCAL ResetCellP()
{
	long lngRetVal;
	
	lngRetVal = InterlockedExchange(& ((long) SleepTime), ((long) 0) );
    EndSim = True;
	return(0);

}
