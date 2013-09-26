#include "build.h"

#ifndef UNIX
#include <windows.h>
#include <crtdbg.h> 
#endif
#include "defines.h"

int IsSetPara;
void setInitialCellCounts();
#ifdef TESTMPI 
real Do_jointpgf(ioption)
int ioption;
#else 
extern real EXPORT PASCAL Do_jointpgf(ioption)
int ioption;
#endif
{
	int i, nowenvl;			   
	real rets;


	setInitialCellCounts(ioption,NEWPATIENT,PGF,0);
	theta=LogMean;
	tau=LogStd;

	for (i= 1; i <= (int)ntypes; i++) { 

		for (nowenvl = 0; nowenvl < nenvirons; nowenvl++)
		{
			buildkill(i,nowenvl);
			setrates (i,nowenvl);
			smask[i] = 0;
			svec[i] = 0.0;
		}
	}

	rets = jointpgf(svec, nt, nconds);
	return rets;
}

real /*FUNCTION*/ jointpgf(spar,itime,icond)
cellarray(spar);
int itime,icond; /*COMPILED*/
/*	cellarray(spar);*/
/*	integer  itime, icond; */
/*VAR*/{
	 real jtemp;
extern double jpgfen_rand();


	if ( (verbos >= 3))   {
		fprintf (eout,"	Args to jointpgf:  spar == ");
		typevec (spar);
		fprintf (eout,"Jointpgf: itime = %4d	icond = %4d\n",
		 itime,icond);
	}

/*	 printf("time=%d,cond=%d,s1=%11.9lf,s2=%11.9lf\n",itime,icond,spar[1],spar[2]);*/
	 if ( (itime == 0)){
		if (tau > 0.0 )
			jtemp = jpgfen_rand (spar);
		else
			jtemp = jpgfen (spar);
		 }
     else  {
		while ((icond > 0) and (timevec [itime] < condition [icond].t))
			icond = icond - 1;
		if ( (icond == 0) or (timevec [itime] > condition [icond].t)){ 
			jtemp = jpgfstep (spar, &itime, icond);
        }
		else 
			jtemp = jpgfcond (spar, itime, icond);
	}
	if ( (verbos >= 2))   {
		fprintf (eout,"Jointpgf returns  %g", jtemp);
		fprintf (eout,"  for itime = %2d  icond = %2d\n",
			itime, icond);
	}
	return(jtemp);
}

#ifndef UNIX
extern int EXPORT PASCAL InitPgf()
{
	extern int fill_para();

	if ( ntypes > MAXTYPES ) {
		MessageBox(NULL,"Sorry. The total number of cell types has exceeded the limits, Please reduce the number of levels or number of classes. Try again.","treat.dll",MB_ICONEXCLAMATION | MB_OK);
	    return (False );
	}
    
	IsSetPara = false;
	return (fill_para(PGF));
}

extern void  EXPORT PASCAL FinishPgf()
{
	extern void release_mem();
	release_mem(PGF);
	free(initCellVector);
}
#endif