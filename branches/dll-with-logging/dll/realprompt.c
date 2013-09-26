#include "build.h"

#include "defines.h"
		/**@ realprompt.p **/
void /*PROCEDURE*/ realprompt(real *x)
/*	real *x; */
{
	fprintf (eout," :  %f  ==>", *x );
	if ( (not qendofline) ) Dread(*x);
	dumpline;
}
