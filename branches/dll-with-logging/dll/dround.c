#include <math.h>

double truncate(double xx)
{
	double intptr,j;
	j = modf(xx, &intptr );
	return(intptr);
}

double dround(double dd, double place)
{
	double mulpl;
	mulpl = pow(10.0,place);
	return( truncate(dd * mulpl + 0.5) / mulpl );
}

