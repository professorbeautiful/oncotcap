#include "build.h"

#include "defines.h"
		/**@ nlogs.p **/
 real /** FUNCTION **/	 nlogs (real cellnumber)   /*COMPILED*/
/*	real cellnumber ;                */
{
	if ( cellnumber > INFINITESIMAL) 
		return( ln (cellnumber ) / LN10);
	else return( - INFINITY);
}
