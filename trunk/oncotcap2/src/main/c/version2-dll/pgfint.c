#include "build.h"

#include "defines.h"
/* modified by QS 1/27/99
	old pgfint assumes x1<= x2
    old pgfint will be replaced by new pgfint see integration.c
*/
real /** FUNCTION**/ pgfintold(x1,x2,g, psit)   /*COMPILED*/
real x1,x2, g, psit;   /*COMPILED*/
        /* real x1, x2, g, psit; */
#define LIMIT 700
/*VAR*/{

	 real x,y, delta, pgfsum; /* , kern(); */
/**  { pgfint **/

	if ( x1< x2) {
		x = x1;
		y = x2;
	}
	else {
		x = x2;
		y = x1;
	}

	delta = (y - x)/LIMIT;
  	pgfsum = kern (x,g,psit) /2;
	
	repeat
	{
		x = x + delta;
		pgfsum = pgfsum + kern (x,g,psit);
	}
	until   (x+delta > y);
	
	pgfsum = (pgfsum + kern (y,g,psit)/2 ) * delta;
	if ( x1 > x2 ) pgfsum = -1*pgfsum;

	return(pgfsum);
}

/**@ kern **/
real /**FUNCTION**/ kern ( x, g,psit)
real x, g, psit;
/* real x,g,psit; */
 {
	if ( tiny (g)) 
		return(expo(-x) /x);
	else {
		if ( x > 0.0 )
			return(1 / ( psit - expo ( g * ln(x))));
		else /* added by QS 1/19/99 to protect x=0 */
			return(1/ psit);
	}
}

