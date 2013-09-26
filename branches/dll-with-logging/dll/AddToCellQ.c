/* add_to_CNsave
 *
 * This procedure adds the current time (global t) and cell counts (global CN) to the 
 * CNsave array
 *
 * CNsave is implemented as a circular queue, with length MAXQLENGTH.
 * iCellQHead is the index of the first piece of data in the queue
 * iCellQTail is the index of the next free space in the queue
 *
 * The event object hCellQwriteLock is used to make sure two writes don't overlap
 * this should never happen (yet . . .)
 *
 * wes 6/20/97
 */


#include <stdlib.h>
#include "build.h"
#ifdef DLL
#include <windows.h>
#endif
#include "defines.h"
#include "classes.h"
#include "cellp.h"

void AddToCellQ()
{
	int itype,ilevel;
/*//	DWORD dwWaitResult;
//	FILE *sp;
//	char outfilename[255];

//	sprintf(outfilename,"%s\\cell.out",WorkingDir);
//	sp = fopen(outfilename,"a");


    
    //if the Q is already being written to, wait til it's done
 //   dwWaitResult = WaitForSingleObject(hCellQWriteLock, INFINITE);
//	switch (dwWaitResult)
//	{
//		case WAIT_OBJECT_0:

			//save the time to CNsave */
			CNsave[0].time = t;
/*			fprintf(sp,"t=%e",t); */

			/* save the cellcounts to CNsave, don't increment iCellQTail until all cell types 
			//are written */
			if (CNsave[0].cellcount != NULL)
			{
				free(CNsave[0].cellcount);
				CNsave[0].cellcount = NULL;
			}
			switch (PlotOption)
			{
				case PLOTTOTALS:
					CNsave[0].cellcount = (double *) calloc (1, sizeof(double));
					CNsave[0].cellcount[0] = AllCells();
					CNsave[0].ntypes = 1;
				break;
				case PLOTALLCELLS:
					{
						CNsave[0].ntypes = active_ntypes;
						CNsave[0].cellcount = (double *) calloc (active_ntypes, sizeof(double));
						for ( itype = 0; itype < active_ntypes; itype++ )
							CNsave[0].cellcount[itype] = CN[itype + 1];
					}
				break;
				default:
					if (PlotOption >= 0 )
					{
						CNsave[0].ntypes =	 Class[PlotOption].no_levels;
						CNsave[0].cellcount = (double *) calloc (Class[PlotOption].no_levels, sizeof(double));
						for (ilevel = 0;ilevel < Class[PlotOption].no_levels; ilevel ++)
						{	
							CNsave[0].cellcount[ilevel] = sumcellbylevel(PlotOption,ilevel);

						}
					}
				break;
			}
/*//			for ( itype = 0; itype < active_ntypes; itype++ )
//					fprintf(sp,"\t%e",CN[itype+1]);
//			fprintf(sp,"\n");
//			fclose(sp); */

			PlotData();

/*			//if CNsave is full, wait til it gets read and reset before incrementing
			//the tail
			//if (CircInc(iCellQTail) == iCellQHead)
			//	dwWaitResult = WaitForSingleObject(hCellQFinishedReset, INFINITE);

			//iCellQTail = CircInc(iCellQTail);
	//		break;

	//	default:
	//		MsgBoxWarning("Error waiting for cell queue to reset [Add_To_CNSave]");
	//}

	//release queue write lock
//	if (! SetEvent( hCellQWriteLock ))
//		MsgBoxWarning("Error removing cell count queue lock [Add_To_CNSave]");*/
}