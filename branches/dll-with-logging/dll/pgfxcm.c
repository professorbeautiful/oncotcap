#include "build.h"

#include "defines.h"
real /**FUNCTION**/ pgfxcm ( xarg, carg, marg, xrate,
		   crate, mrate,tdiff) 
real xarg, carg,marg, xrate, crate, mrate, tdiff;  /*COMPILED*/
/*	 real xarg, carg, marg, xrate, crate, mrate, tdiff; */
/*VAR*/{

	 real ptemp, nu, exparg, d, b, r1, r2, eta;

	if ( (verbos  >= 7))   {
		fprintf (eout,"	pgfxcm: args ");
		fprintf (eout,"%15f %15f %15f ", xarg, carg, marg);
		fprintf (eout,"		rates");
		fprintf (eout,"%15f %15f %15f ", xrate, crate, mrate);
	}
	if ( (xarg < ZERO))   {
		if ( (verbos > 0)) 
			fprintf
			   (eout," WARNING : xarg < 0 in pgfxcm %f", xarg);
		xarg = ZERO;
	}
	if ( (xarg > ONE))   {
		if ( (verbos > 0)) 
			fprintf
			   (eout," WARNING : 1-xarg < 0 in pgfxcm %f", xarg);
		xarg = ONE;
	}
	if ( (carg < ZERO))   {
		if ( (verbos > 0)) 
			fprintf
			   (eout," WARNING : carg < 0 in pgfxcm %f", carg);
		carg = ZERO;
	}
	if ( (carg > ONE))   {
		if ( (verbos > 0)) 
			fprintf
			   (eout," WARNING : 1-carg < 0 in pgfxcm %f", carg);
		carg = ONE;
	}
	if ( (marg < ZERO))   {
		if ( (verbos > 0)) 
			fprintf
			   (eout," WARNING : marg < 0 in pgfxcm %f", marg);
		marg = ZERO;
	}
	if ( (marg > ONE))   {
		if ( (verbos > 0)) 
			fprintf
			   (eout," WARNING : 1-marg < 0 in pgfxcm %f", marg);
		marg = ONE;
	}
	if ( tiny(tdiff))  ptemp = xarg			/**A1**/;
	else if (tiny (xrate))   {
		b = crate + (1 - marg) *mrate;
		if ( (b == ZERO))  ptemp = xarg		/**A1**/;
		else  {
			r2 = carg * crate / b;
								/**A3**/
			ptemp = ONE - ((ONE-xarg) * expo (-b * tdiff)
				+ (ONE-r2) * (1 - expo (-b * tdiff)));
		}
	}
	else  {
		b = crate + (1 - marg) *mrate;
		nu = 1 + b/xrate;
		d = sqrt (nu*nu - 4*crate*carg/xrate);
		r1 = (nu + d)/2;
		r2 = (nu - d)/2;
		exparg =  d*xrate *tdiff;
		if ( (exparg > 80.0))   {
			fprintf (eout," WARNING: exparg > 80 in pgfxcm\n");
			ptemp = r2;				/**A1**/
		}
		else if ((tiny (r2 - xarg))) 
			ptemp = r2;				/**A1**/
		else  {
			eta = (r1-xarg)/(r2-xarg) * expo (exparg);
			if ( tiny (ONE-eta))   {
				if ( (xarg == ONE)) 
				   ptemp = ONE;
				else
								/**A2**/
				   ptemp = 1 - 1/(xrate*tdiff + 1/(1-xarg) );
			}
			else
								/**8**/
				ptemp = (r1 - r2*eta)/(1 - eta);
		}
	}
	if ( (verbos  >= 7))   {
		fprintf (eout,"		nu  == %15f",nu);
		fprintf (eout,"      d     == %15f",d);
		fprintf (eout,"		r1  == %15f",r1 );
		fprintf (eout,"      r2    == %15f",r2   );
		fprintf (eout,"		eta == %15f",eta);
		fprintf (eout,"      ptemp == %15f",ptemp);
	}
	return(ptemp);
}
