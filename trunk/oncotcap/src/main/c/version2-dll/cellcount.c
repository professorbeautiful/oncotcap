#include "build.h"

#include "defines.h"
		/**@ cellcount.p **/   /*COMPILED*/
 real /** FUNCTION*/ cellcount (real *Pprobtoget,real *Pprobwegot) 
 /* real *Pprobtoget, *Pprobwegot; */

#define	TOLERANCE 0.03
#define	ITERLIMIT 100
#define	TOOSMALL 0.0001
/*VAR*/{

	 real botdose, topdose, newdose;
	 real botprob, topprob, newprob;
	 real zeroprob ;
	 real accuracy;
	 integer itercount;

	botdose = ZERO;
	botprob = probeprob (botdose);
	topdose = 6.0;
	topprob = probeprob (topdose);
	zeroprob = botprob;
	if ( (zeroprob >= *Pprobtoget))   {
		*Pprobwegot = zeroprob;
		return ( - ln (zeroprob));
	}
	else  {
		while ((topprob < *Pprobtoget))  {
				botdose = topdose;
			botprob = topprob;
			topdose = 2* topdose;
			topprob = probeprob (topdose);
		}
		itercount = 1;
		if ( (1-*Pprobtoget < *Pprobtoget)) 
			accuracy = (1-*Pprobtoget)*TOLERANCE;
		else accuracy = *Pprobtoget * TOLERANCE;
		if ( accuracy < TOOSMALL ) accuracy =TOOSMALL;
		repeat {
			itercount = 1 + itercount;
			newdose = botdose* (topprob - *Pprobtoget)
					+ topdose* (*Pprobtoget - botprob);
			newdose = newdose / (topprob - botprob);
			newprob = probeprob (newdose);
			if ( newprob >= *Pprobtoget)   {
				topprob = newprob;
				topdose = newdose;
			}
			else  {
				botprob = newprob;
				botdose = newdose;
			}
		}
		until (
			(fabs (newprob - *Pprobtoget) <accuracy)
		or
			(itercount > ITERLIMIT)
		);
		*Pprobwegot = newprob;
		return (  ncells (newdose));
	}
}
