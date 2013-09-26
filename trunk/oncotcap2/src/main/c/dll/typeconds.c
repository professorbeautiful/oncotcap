#include "build.h"

#include "defines.h"
		/**@ typeconds.p **/
#define CONDlocal condition [icond]
void /*PROCEDURE*/ typeconds()   /*COMPILED*/
/*VAR*/{
  integer icond, itype;
  boolean qtypedyet;

	for (icond = 1; icond  <= nconds; icond ++) {
		fprintf (eout, "	Conditioning on:\n");
		qtypedyet=NO;
		fprintf (eout, "            Subpop'n:  ");
		for (itype = 1; itype  <= active_ntypes; itype ++)
			if ( CONDlocal.qtypes [itype] == YES)   {
				if (qtypedyet==YES) fprintf (eout, " + ");
				typestring (cellname (itype));
				qtypedyet=YES;
			}
		fprintf (eout, "\n            ");
		if ( (CONDlocal.dir == LT)) 
			fprintf (eout, "<");
		else    fprintf (eout, ">=");
		fprintf (eout, " %g   at time %2.2f\n",  CONDlocal.size, CONDlocal.t);
	}
}
