#include "build.h"

#include "defines.h"
		/**@ probeprob.p **/
real /**FUNCTION**/ probeprob (real probelog)   /*COMPILED*/
/*	real probelog; */
/*VAR*/{

	 real survprob, prob;
	 integer itype;

	survprob = expo (-probelog * LN10);
	for (itype = 1; itype  <= (int) ntypes; itype ++)
		if ( (smask [itype] == ZERO))   {
			wvec [itype] = survprob;
			svec [itype] = ONE - survprob;
		}
		else  {
			wvec [itype] = ZERO;
			svec [itype] = ONE;
		}
	prob = jointpgf (svec, nt, nconds);
	if ( verbos >= 4) 
	fprintf (eout,"      probeprob: if you add %6f logs of kill, prob(0) = %10f"
		, probelog, prob);
	return(prob);
}




