#include "build.h"

#include "defines.h"
		/**@ probzeros.p **/
void /*PROCEDURE*/ probzeros()   /*COMPILED*/
/*VAR*/{

	real p;
	   integer itype, jtype;

	fprintf (eout," PROB (ZERO cells)\n");
	fprintf (eout," At time    %2.4f\n", timevec [nt]);
	if ( ntypes > 1)   {
		for (itype = 1; itype  <= (int)ntypes; itype ++)  {
			wvec [itype] = ONE;
			svec [itype] = ZERO;
		}
                fprintf (eout,"[probzeros] nt1 %d",nt);
		p = jointpgf (svec, nt, nconds);
		fprintf (eout,"      pr {total = 0}    %12f\n", p);
	}
	for (itype = 1; itype  <= (int)ntypes; itype ++)  {
		for (jtype = 1; jtype  <= (int)ntypes; jtype ++)  {
			wvec [jtype] = ZERO;
			svec [jtype] = ONE;
		}
		wvec [itype] = ONE;
		svec [itype] = ZERO;
                fprintf (eout,"[probzeros] nt2 %d",nt);
		p = jointpgf (svec, nt, nconds);
		fprintf (eout,"pr {# of type  ");
		typestring ( cellname (itype));
		fprintf (eout," = 0}  %12f\n", p);
	}
}
