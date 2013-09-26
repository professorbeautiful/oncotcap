#include "build.h"

#include "defines.h"
		/**@ ariths.p **/   /*COMPILED*/
/**  This file contains arithmetic auxiliary routines. They are:
  power, tiny, logten, expo, sign **/
 
 
real /*FUNCTION*/ power  ( x,n)
real x;
integer n;
/*	real x; integer n; */
/*VAR*/{

	   real temp;
	      integer k;

	temp = 1;
	for (k = 1; k  <= n; k ++)
		temp = temp * x;
	return(temp);
}

boolean /*FUNCTION*/ tiny (x)
real x;
	/* real x; */
{
	if ( (x < INFINITESIMAL) and (x > -INFINITESIMAL)) 
		return(true);
	else return(false);
}
real /*FUNCTION*/ logten (x)
real x;
	/* real x; */
{
	if ( tiny (x) ) return ( 0.0);
	else if (x > INFINITESIMAL) return ( ln (x) / LN10);
	else  {
		fprintf (eout,"      CAUTION: logten evaluated at x == %f", x);
		return ( 0.0);
	};
}


real /*FUNCTION*/ expo (x)
real x;
/*	real x;	 */
{
	if ( (x < -80) ) return( 0.0);
	else return( exp (x));
}

real /*FUNCTION*/ sgn (x)
real x;
/*	real x; */ /*CAREFUL- THIS WAS a "VAR" arg*/
{
	if ( (x==0.0) ) return( 0.0);
	else if (x<0.0) return( -1.0);
	else return( 1.0);
}
