#include "build.h"
#include <stdlib.h>
#include "defines.h"

extern double ranmarm();

double norm(int flag)
{
    static int iset;
    static double gset;
	double angl,dist,retval;

	if (flag == TRUE)
	{
		iset = 0;
		gset = 0.0;
		return(0.0);
	}

	if (iset == 0) 
	{
		angl = 2 * PI * (ranmarm(INITRAND)) ;
		dist = -log(ranmarm(INITRAND));
	    gset = dist * cos(angl);
		iset = 1;
		retval = dist * sin(angl);
	}
	else
	{
		iset = 0;
		retval = gset;
	}

	return(retval);
}
