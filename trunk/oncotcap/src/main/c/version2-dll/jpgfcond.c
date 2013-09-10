#include "build.h"

#include "defines.h"
		/**@ jpgfcond.p **/
 real /**FUNCTION**/  jpgfcond (spar,itime,icond)
cellarray(spar);
int itime,icond;   /*COMPILED*/
	/* cellarray(spar); integer itime, icond;        */
/*VAR*/{

	    integer itype;
	 cellarray(sarg);
	 real bottom, top;
	 struct COND COND;/*declarator for COND pointer*/
  

	COND=condition [icond];
	
	for (itype = 1; itype  <= (int)ntypes; itype ++)
		if ( (COND.qtypes [itype] == YES)) 
			sarg [itype] = 1-(1/COND.size);
		else
			sarg [itype] = ONE;

	bottom = jointpgf (sarg, itime, icond-1);
	for (itype = 1; itype  <= (int)ntypes; itype ++)
		sarg [itype] = sarg [itype] * spar [itype];
	top = jointpgf (sarg, itime, icond-1);
	if ( (COND.dir == LT)) 
		return( top/bottom);
	else if (COND.dir == GE)   {
		bottom = ONE - bottom;
		top = jointpgf (spar, itime, icond-1) - top;
		return( top/bottom);
	}
	else fprintf (eout,"jpgfcond: COND.dir error\n");
	return(-1.0);
}
