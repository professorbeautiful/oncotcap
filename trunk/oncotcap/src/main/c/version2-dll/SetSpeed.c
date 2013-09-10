#include <windows.h>
#include "build.h"
#include "defines.h"
#include "classes.h"
#include "cellp.h"

extern int EXPORT PASCAL SetSpeed( int newtime )
{
	long lngRetVal;

		lngRetVal = InterlockedExchange(& ((long) SleepTime), ((long) newtime) );

	return(0);
}