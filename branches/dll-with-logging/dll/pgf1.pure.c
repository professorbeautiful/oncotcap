#include "build.h"
#ifndef UNIX
#include <windows.h>
#include <crtdbg.h> 
#endif

#include "defines.h"

	 char wmessage[255];
	 real rinc, sinc, rho, psit, range;
	 real lams, lamr, psi0;   /*COMPILED*/
	 real w, pgftemp, gratio, xrate, kappa, crate;
	 real answer, x1, x2, g;
     /***** This global variable stuff works- see 'globtest.c'*****/

real pgf1 ( stype,rtype, t, s)
int stype,rtype;
real t, s;
{

 /**pgf1**/
	if ( (verbos >= 6)) 
		fprintf (eout,"	pgf1: args are %6d %12d %lf %lf",
			 stype, rtype, t, s);
	xrate = xi(rtype,nowenv);
	crate = gama(rtype,nowenv);
	lamr = growrate(rtype,nowenv);
	/* change back to original version, QS 1/19/99 */
	lams = growrate(stype,nowenv);
/*	lams = growrate(stype,nowenv)*(1- mu(stype,nowenv));*/

	w = ONE - s;

	if ( tiny (t) or tiny (w)) 
/**B1**/		pgftemp = s;
	else if (tiny (xrate))   {
		rinc = expo (t* lamr);
		sinc = expo (t* lams);
		range = sinc - ONE;
		if ( tiny ( lams - lamr))   {
			if ( tiny (lams)) 
				pgftemp = s;
			else {
				if (fabs(range) < 1.00000000000001e-30) {
					sprintf(wmessage,"Division by zero in PGF engine at (B4).\nPlease send this small information and the file %s\\OncoTcap.log to Tcapbugs@pci.upmc.edu.",WorkingDir);
#ifdef DLL
					MessageBox(NULL,wmessage,"treat.dll",MB_ICONEXCLAMATION | MB_OK);
#else
					printf("%s",wmessage);
#endif
					exit(1);
				}
				else /**B4*/	pgftemp = ONE - w/range *lams *t *sinc;
				
			}
		}
		else if (tiny (lams)) {
			if ((fabs(crate) < 1.00000000000001e-30)|| (fabs(rinc) < 1.00000000000001e-30) ){
				sprintf(wmessage,"Division by zero in PGF engine at (B3).\nPlease send this small information and the file %s\\OncoTcap.log to Tcapbugs@pci.upmc.edu.",WorkingDir);
#ifdef DLL
				MessageBox(NULL,wmessage,"treat.dll",MB_ICONEXCLAMATION | MB_OK);
#else 
				printf("%s",wmessage);
#endif
				exit (1);
			}
			else /**B3**/ pgftemp = ONE - w/(t*crate)* (ONE-ONE/rinc);
			
		}
		else {
			if ((fabs(range) < 1.00000000000001e-30)|| (fabs(lams-lamr) < 1.00000000000001e-30) ){
				sprintf(wmessage,"Division by zero in PGF engine at (B2).\nPlease send this small information and the file %s\\OncoTcap.log to Tcapbugs@pci.upmc.edu.",WorkingDir);
#ifdef DLL
				MessageBox(NULL,wmessage,"treat.dll",MB_ICONEXCLAMATION | MB_OK);
#else 
				printf("%s",wmessage);
#endif
			    exit(1);
			}
			else 
				/**B2*/			pgftemp = ONE -
					w/range *lams/(lams-lamr)*
					(sinc - rinc );		
			
		}
	}
	else if (tiny ( lamr))   {
		if ( tiny ( lams)) 
		{
			if (fabs(xrate) < 1.00000000000001e-30){
				sprintf(wmessage,"Division by zero in PGF engine at (B6).\nPlease send this small information and the file %s\\OncoTcap.log to Tcapbugs@pci.upmc.edu.",WorkingDir);
#ifdef DLL
				MessageBox(NULL,wmessage,"treat.dll",MB_ICONEXCLAMATION | MB_OK);
#else 
				printf("%s",wmessage);
#endif
			    exit (1);
			}
			else 	/**B6**/	pgftemp = ONE - ln (ONE + xrate *t *w) /t/xrate;
			
		}
		else  {
			sinc = expo (t* lams);
			range = sinc - ONE;
			
			if (fabs(xrate) < 1.00000000000001e-30){
				sprintf(wmessage,"Division by zero in PGF engine at (B5).\nPlease send this small information and the file %s\\OncoTcap.log to Tcapbugs@pci.upmc.edu.",WorkingDir);
#ifdef DLL
				MessageBox(NULL,wmessage,"treat.dll",MB_ICONEXCLAMATION | MB_OK);
#else 
				printf("%s",wmessage);
#endif
				exit (1);
			}
			else /**B5**/ kappa = lams / (xrate *w );
			
		/** pure only --> **/
/*			pgftemp = ONE -  kappa * exp (-kappa) /range *w *
					pgfint ((double)ZERO, kappa, kappa+lams*t, (double)ZERO);*/
			pgftemp = ONE -  kappa * exp (-kappa) /range *w *
					pgfint ((double)ZERO, kappa, kappa+lams*t, (double)ZERO);
		/** <-- pure only **/
		/** hybrid only -->  
			aerr = 1.0e-9;
			rerr = aerr;
			x1 = kappa;
			x2 = kappa+lams*t;
			g = ZERO;
			pgfint (x1, x2, g, psit, aerr, rerr, ier, answer);
			pgftemp = ONE -  kappa * exp (-kappa) /range *w *
				answer;
			if ( (ier != 0)) 
				fprintf (eout,"	pgfint: ier == %d", ier);
		  <-- hybrid only **/
		}
	}
	else if (tiny ( (crate/xrate - s))) 
		pgftemp = s;
	else  {
		rinc = expo (t* lamr);
		sinc = expo (t* lams);
		range = sinc - ONE;
		rho = crate / xrate;
		psi0 = w/(rho - s) ;
		psit = psi0 *rinc;
		if ( (verbos >= 6)) 
			fprintf (eout,"	psi0, psit: %10f %10f", 
				psi0, psit); 
		if ( tiny (lamr - lams)) {
			if ((fabs(range) < 1.00000000000001e-30)|| (fabs(ONE-psit) < 1.00000000000001e-30)||(fabs(sinc-psit)<1.0e-30)){
				sprintf(wmessage,"Division by zero in PGF engine at (B8).\nPlease send this small information and the file %s\\OncoTcap.log to Tcapbugs@pci.upmc.edu.",WorkingDir);
#ifdef DLL
				MessageBox(NULL,wmessage,"treat.dll",MB_ICONEXCLAMATION | MB_OK);
#else 
				printf("%s",wmessage);
#endif
			    exit (1);
			}
			/** rtype && stype grow at same rate **/
/**B8**/	else		pgftemp = ONE - (rho-ONE)/range*psit *
				ln (fabs ( (sinc-psit)/(ONE-psit) ) );
		}
		else if (tiny ( lams)) {
			if ((fabs(lamr) < 1.00000000000001e-30)|| (fabs(ONE-psit) < 1.00000000000001e-30)||(fabs(ONE-psi0) < 1.00000000000001e-30)){
				sprintf(wmessage,"Division by zero in PGF engine at (B7).\nPlease send this small information and the file %s\\OncoTcap.log to Tcapbugs@pci.upmc.edu.",WorkingDir);
#ifdef DLL
				MessageBox(NULL,wmessage,"treat.dll",MB_ICONEXCLAMATION | MB_OK);
#else
				printf("%s",wmessage);
#endif
				exit (1);
			}
/**B7**/	else	pgftemp = ONE - (rho-ONE)/t/lamr *
			ln (fabs ( (ONE-psi0)/(ONE-psit) ) );
		}
		else  {
/**10**/
			pgftemp = eq10();
		};
	};
	/*****
		if ( ((pgftemp < (double)ZERO) or (pgftemp > (double)ONE)))   {
	*****/
	if (pgftemp < (double)ZERO - INFINITESIMAL) 
		fprintf (eout,"WARNING: pgftemp =  %e\n", pgftemp);
	if (pgftemp > (double) ONE + INFINITESIMAL)
		fprintf (eout,"WARNING: 1-pgftemp =  %e\n", 1-pgftemp);
	if (verbos >= 6)
		fprintf (eout,"		returns  %lf", pgftemp);
	if ( pgftemp < (double)ZERO)  pgftemp = ZERO;
	if ( pgftemp > (double)ONE)  pgftemp = ONE;
	return(pgftemp);
}

/**10**/
 real /**FUNCTION**/   eq10() 
 {
    real temp=0.0;
	x1 = (double)ONE;
	x2 = sinc;
	/** pure only --> **/
	gratio = lamr / lams;
	answer = pgfint (x1, x2, gratio, psit);
	/** <-- pure only **/

	/** hybrid only -->  
		 {
			aerr = 1.0e-10;
			rerr = 1.0e-9;
			pgfint (x1, x2, gratio, psit, aerr, rerr, ier, answer);
			if ( (ier > 0))  pgfwarn (ier);
		};
	  <-- hybrid only **/
	if (fabs(range) < 1.00000000000001e-30){
			sprintf(wmessage,"Division by zero in PGF engine at (B10).\nPlease send this small information and the file %s\\OncoTcap.log to Tcapbugs@pci.upmc.edu.",WorkingDir);
#ifdef DLL
			MessageBox(NULL,wmessage,"treat.dll",MB_ICONEXCLAMATION | MB_OK);
#else
			printf("%s",wmessage);
#endif
			exit (1);
	}
	else temp = ONE - (ONE-rho)/range*psit * answer;
	if ( verbos >= 3)  
		fprintf (eout,"eq10:  args %12f %12f %12f ,  returns  %e"
			 , x1, x2, gratio,temp);
	return(temp);
}






