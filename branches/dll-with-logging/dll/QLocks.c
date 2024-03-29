#include <windows.h>
#include <stdio.h>
#include "build.h"
#include "defines.h"
#include "classes.h"
#include "cellp.h"
#include "logger.h"

extern int EXPORT PASCAL LockQ( int QType )
{
	DWORD dwWaitResult;

	fprintf(logfile,"%s\tfunction:QLocks.LockQ\tQType:%d\n", gettime(),QType);
    switch (QType)
	{
		case CELLQ:
		{
			//if the Q is being reset or read from, wait til it's done
			dwWaitResult = WaitForSingleObject(hCellQRRLock, INFINITE);
			break;
		}
		case EVENTQ:
			//if the Q is being reset or read from, wait til it's done
			dwWaitResult = WaitForSingleObject(hEventQRRLock, INFINITE);
			break;
	}
	return(0);
}

extern int EXPORT PASCAL UnlockQ( int QType )
{
    fprintf(logfile,"%s\tfunction:QLocks.UnlockQ\tQType:%d\n", gettime(),QType);	
    switch (QType)
	{
		case CELLQ:
		{
			//release the lock for more resets or reads
			if (! SetEvent( hCellQRRLock ))
				MsgBoxWarning("Error releasing cell queue RR Locks. [UnlockQ]");
			break;
		}
		case EVENTQ:
			//release the lock for more resets or reads
			if (! SetEvent( hEventQRRLock ))
				MsgBoxWarning("Error releasing Event RR Locks. [UnlockQ]");
			break;
	}
	return(0);
}
