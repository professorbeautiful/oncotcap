#include "build.h"
#include <math.h>
#include "defines.h"
#include "Const.h"

/*extern double gnorm0();*/
extern double norm();
double lognorm(double logmean,double logstd)
{
	double temp1,retval;
	/*temp1 = gnorm0(INITRAND) * logstd + logmean;*/
	temp1 = norm(FALSE) * logstd + logmean;
	retval = exp(temp1);
	return(retval);
}

