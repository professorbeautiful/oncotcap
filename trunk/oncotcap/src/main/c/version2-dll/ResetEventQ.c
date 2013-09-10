#include <windows.h>
#include "build.h"
#include "defines.h"
#include "classes.h"
#include "cellp.h"

extern int EXPORT PASCAL ResetEventQ( int numpop )
{ 
	if ((numpop > 0) && (numpop <= QLength(iEventQHead, iEventQTail)))
	{
		iEventQHead = AddQIndex(iEventQHead, numpop);

	}
	return(0);
}