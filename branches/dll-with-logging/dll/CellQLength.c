#include <windows.h>
#include "build.h"
#include "defines.h"
#include "classes.h"
#include "cellp.h"

extern int EXPORT PASCAL CellQLength( )
{
	DWORD dwWaitResult;
	int qlen;
    

	//if the Q is being reset or read from, wait til it's done
	dwWaitResult = WaitForSingleObject(hCellQRRLock, INFINITE);

	qlen = QLength(iCellQHead, iCellQTail);

	//release the lock for more resets or reads
	if (! SetEvent( hCellQRRLock ))
		MsgBoxWarning("Error releasing RR Locks. [CellQLength]");

	return(qlen);
}