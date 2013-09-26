#include "build.h"

#include "defines.h"
		/**@ jpgfstep.p **/


real /*FUNCTION*/ jpgfstep (sold,Ptime,icond)
cellarray(sold);
int *Ptime,icond;    /*COMPILED*/
/*	cellarray(sold);  integer *Ptime, icond; */
/*VAR*/{
  cellarray(snew);
	nowenv=getenvir ( timevec [*Ptime] /* *Ptime */ );
	jpgfit (sold, snew, *Ptime, nowenv);
	if ( (verbos >= 5))   {
		fprintf (eout,"	jpgfstep:  before");
		typevec (sold);
		fprintf (eout,"	jpgfstep:  after");
		typevec (snew);
		fprintf (eout,"	jpgfstep:  itime = %4d	icond = %4d\n",
			*Ptime, icond);
	}
	*Ptime = *Ptime - 1;
	return(jointpgf (snew, *Ptime, icond));
}
