#include <windows.h>
#include <stdio.h>
#include "build.h"
#include "defines.h"
#include "classes.h"
#include "cellp.h"
#include "logger.h"

extern int EXPORT PASCAL EventQLength( )
{
	DWORD dwWaitResult;
	int qlen;
    
	fprintf(logfile,"%s\tfunction:EventQLength:EventQLength\n", gettime());
	//if the Q is being reset or read from, wait til it's done
	dwWaitResult = WaitForSingleObject(hEventQRRLock, INFINITE);

	qlen = QLength(iEventQHead, iEventQTail);

	//release the lock for more resets or reads
	if (! SetEvent( hEventQRRLock ))
		MsgBoxWarning("Error releasing RR Locks. [EventQLength]");

	return(qlen);
}