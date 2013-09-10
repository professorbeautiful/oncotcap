#include <stdlib.h>
#include <windows.h>
#include "build.h"
#include "defines.h"
#include "classes.h"
#include "cellp.h"
#include "msim.h"

extern int EXPORT PASCAL ResetMultiCellP()
{
	long lngRetVal;

	lngRetVal = InterlockedExchange(& ((long) SleepTime), ((long) 0) );
	EndSim = True;
	MEndSim = True;
	return(0);
}
