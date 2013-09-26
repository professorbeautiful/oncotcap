#include "build.h"

#include "defines.h"
		/**@ typevec.p **/
void /*PROCEDURE*/ typevec (vec)   /*COMPILED*/
cellarray(vec);
/*	 cellarray(vec);   */
/*VAR*/{                                         
  integer itype;

	if (ntypes <= 0 || ntypes >= 100 ) {
		fprintf (eout,"typevec:ERROR: ntypes = %d\n", ntypes);
		return;
	}
	for (itype = 1; itype  <= (int) ntypes; itype ++)  {
		if ( (itype mod 4 == 1))   {
			fprintf(eout,"\n");
			fprintf (eout,"	");
		}
		fprintf (eout,"(%2d) %7.8f", itype, vec [itype]);
	}
	fprintf(eout,"\n");
}
