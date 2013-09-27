#include <windows.h>
#include <stdio.h>
#include "build.h"
#include "defines.h"
#include "classes.h"
#include "cellp.h"
#include "logger.h"

extern int EXPORT PASCAL SetSpeed( int newtime )
{
	long lngRetVal;

	fprintf(logfile,"%s\tfunction:SetSpeed.SetSpeed\tnewtime:%d\n", gettime(),newtime);
		lngRetVal = InterlockedExchange(& ((long) SleepTime), ((long) newtime) );

	return(0);
}