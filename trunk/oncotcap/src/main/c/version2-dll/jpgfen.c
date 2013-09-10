#include "build.h"

#include "defines.h"
#include <math.h>

		/**@ jpgfen.p **/
real /*FUNCTION*/ jpgfen(spar)
cellarray(spar);  /*COMPILED*/
/*	cellarray(spar); */
/*VAR*/{
	 real prod;
	 integer iprod;

	prod = ONE;
	for (iprod = 1; iprod  <= (int)ntypes; iprod ++)
	        if (tiny (spar [iprod]) and (cellstart [iprod] >ZERO)) 
	                prod = ZERO;
	        else if (not (tiny (spar [iprod])))
	                prod = prod *
					 expo( ln (spar [iprod]) * cellstart [iprod]);

	return(prod);
}

double pgf_lognormal(spar,mu,tau)
double spar,mu,tau;
{
	double ret=1.0,preret,x,f_x,delta_x;
	  
   	x = -6;
	ret=ZERO;
	preret=1.0;
	delta_x =0.001;

	while ( ( x < 6 ) || (fabs(ret - preret) > 1.0e-10)) {
		preret=ret;
		x += delta_x;
		f_x=expo(- (x*x/2))/sqrt(2*PI);
		ret += delta_x * expo(log(spar)*expo(mu+tau*x)) * f_x;
	}
   
	return ret;
  
}

double jpgfen_rand(spar)
cellarray(spar);
{
	double jpgf,ssum;
	int iprod;

	ssum = ZERO;

	if ( InitTotal > ZERO ) {
	for (iprod = 1; iprod  <= (int)ntypes; iprod ++) {
		if (( cellstart[iprod]/InitTotal) > ZERO ) {
			ssum += spar [iprod]*cellstart[iprod]/InitTotal;
		}
	}
	}

	if ( ssum == ZERO )
		jpgf = ZERO;
	else
		jpgf = pgf_lognormal (ssum,theta,tau);


	return(jpgf);

}

