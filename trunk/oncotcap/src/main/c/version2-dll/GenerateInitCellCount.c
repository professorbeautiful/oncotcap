#include <stdlib.h>
#include "build.h"
#ifdef DLL
#include <windows.h>
#endif
#include <math.h>


extern double lognorm();

double GenerateInitCellCount(double total,double cv)
{
	double t1,t2,tau,mu,rettotal;
	extern double LogMean, LogStd;

	rettotal = 0.0;
	t1= (cv*cv) + 1;
	tau = log(t1);
	t1 = tau * 0.5;
	t2 = log(total);
	mu = t2 - t1;
	LogMean = mu;
	LogStd = tau;
	rettotal = lognorm(mu,tau);
	if (rettotal < 1.0 )
		return (1.0);
	else
		return(floor(rettotal));

}


