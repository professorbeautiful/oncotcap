#include "build.h"

#include "defines.h"
		/**@ typemask.p **/
void /*PROCEDURE*/ typemask()   /*COMPILED*/
/*VAR*/{
  integer itype;

	fprintf (eout,"	For cells of these celltype(s): ");
	for (itype = 1; itype  <= active_ntypes; itype ++)
		if ( (smask [itype] == 0))   {
			typestring ( cellname (itype));
			fprintf (eout,", ");
		}
	fprintf(eout,"\n");
	typeconds();
}
