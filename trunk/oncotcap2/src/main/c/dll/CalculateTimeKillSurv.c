#include "build.h"
#include <stdio.h>
#include "defines.h"

void CalculateTimeKillSurv()
{
	integer itime, itype, isched, ienv;
	struct drugandtime s; /*** DECLARATOR **/
/*	FILE *fp; */
	double jjj;
	nallts = 1;
/*	fp=fopen("c:\\temp\\timesurv2.out","w");*/

	for (itime = 0; itime  <= MAXTIMES; itime ++)  
	{
		timevec [itime] = ZERO;
		for ( ienv =0; ienv < nenvirons ; ienv ++ ) 
		{
			for (itype = 1; itype  <= active_ntypes; itype ++)  
			{
				timekill(itime,itype,ienv) = ZERO;
				timesurv (itime,itype,ienv) = ONE;
			}
		}
	}
	for (isched = 1; isched  <= nsched; isched ++) 
	{
		s=sched [isched] ;
		if ( s.t != timevec [nallts])  
		{
			nallts = nallts + 1;
			timevec [nallts] = s.t;
		}
		for (itype = 1; itype  <= active_ntypes; itype ++)  
		{
			for ( ienv =0; ienv < nenvirons ; ienv ++ ) 
			{
				timekill (nallts, itype,ienv) =  1 -
					(1 - timekill (nallts, itype,ienv))
					*expo (-drugkill (s.d,itype,ienv)* s.df  * 2.3025850929);
				timesurv (nallts,itype,ienv) = 
											  timesurv (nallts, itype,ienv) *
											  expo (-drugkill(s.d, itype,ienv) * s.df * 2.3025850929);
				jjj = timesurv (nallts, itype,ienv);
			}
/*//			fprintf(fp,"%d\t%d\t%d\t%lf\t%lf\n",nallts,isched,s.d,s.df,timesurv [nallts][ itype]);*/
		}
	}
/*//	fclose(fp);*/
}