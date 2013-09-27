#include <windows.h>
#include <stdio.h>
#include "build.h"
#include "defines.h"
#include "classes.h"
#include "cellp.h"
#include "logger.h"

extern int EXPORT PASCAL ResetEventQ( int numpop )
{
	fprintf(logfile,"%s\tfunction:ResetEventQ.ResetEventQ\tnumpop:%d\n", gettime(),numpop);
	if ((numpop > 0) && (numpop <= QLength(iEventQHead, iEventQTail)))
	{
		iEventQHead = AddQIndex(iEventQHead, numpop);

	}
	return(0);
}