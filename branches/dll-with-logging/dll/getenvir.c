#include "build.h"

#include "defines.h"
int getenvir(etime)
double etime;
{
   integer ienvlist;

/*	if ( nenvirons == 1)  return(0);
	else  { */
		ienvlist = nenvlist;
		while ((ienvlist > 0)
		and	( etime /* timevec [itime] */ <= envlist [ienvlist] .t))  {
			ienvlist = ienvlist -1;
		};
		return(envlist [ienvlist ] .e);
/*	}  */
} 
