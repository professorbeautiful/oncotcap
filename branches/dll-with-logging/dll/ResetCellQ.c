#include <windows.h>
#include <stdio.h>
#include "build.h"
#include "defines.h"
#include "classes.h"
#include "cellp.h"
#include "logger.h"

extern int EXPORT PASCAL ResetCellQ( int numpop )
{
	fprintf(logfile,"%s\tfunction:ResetCellQ.ResetCellQ\n", gettime());
//	int i,j;
    
//	if ((numpop > 0) && (numpop <= QLength(iCellQHead, iCellQTail)))
//	{
		// free the cell count vector allocation
	//	for(i=0; i< numpop; i++)
	//	{
	//	   j = AddQIndex(i, iCellQHead);
		   free(CNsave.cellcount);
		   CNsave.cellcount = NULL;

	//	}
		// reset the Q head
//		iCellQHead = AddQIndex(iCellQHead, numpop);

		//in case the the Q was full, let the write process know we're finished

//	}
	return(0);
}