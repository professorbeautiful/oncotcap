#include "build.h"
#include <windows.h>
#include "defines.h"
#include "timecourse.h"
		/**@ timecourse.p **/
void /*PROCEDURE*/ timecourse()   /*COMPILED*/

#define VBSTRING(A) SysAllocStringByteLen(A,strlen(A))

#define ENEG1  0.3678794411
/*VAR*/{

	 integer minidose, idose ;
	 int nc;
	 char * ptrTC;
	 static real  mintime=0; 
	 real problevel, probwegot, zeroprob, lognumber, cellnumber;

	ptrTC = strTimecourse;
#define STRPRINT0(A) nc = sprintf(ptrTC,A); ptrTC = ptrTC + nc;
#define STRPRINT1(A,B) nc = sprintf(ptrTC,A,B); ptrTC = ptrTC + nc;
#define STRPRINTEOL nc = sprintf(ptrTC,"%c%c",(char) 13, (char) 10); ptrTC = ptrTC + nc;
	problevel = ENEG1;


/** next 6 lines new as of dec 4 1985;  cutsout early eval times **/
	fprintf (eout," Minimum evaluation time: ");
/*	realprompt (&mintime);*/
	mintime=1;
	minidose = 1;
	while ((doselist [EVAL][minidose] < mintime)
	    && (minidose <= ndoses [EVAL]))
		minidose = minidose+1;
	
	fprintf (eout," TIMECOURSE\n");
	typemask();
	fprintf (eout,"      Time");
	STRPRINT0("      Time");
	fprintf (eout,"	Pr (no cells)");
	STRPRINT0("	Pr (no cells)");
	fprintf (eout,"     # logs");
	STRPRINT0("     # logs");
	fprintf (eout,"        Avg # cells");
	STRPRINT0("        Avg # cells");
	fprintf (eout,"     Avg if > 0\n");
	STRPRINT0("     Avg if > 0");
	STRPRINTEOL;
	for (idose = minidose; idose  <= ndoses [EVAL]; idose ++)  {
		nt = nallts;
		while ((doselist [EVAL][idose] < timevec [nt]) && (nt>=0))
			nt = nt-1;
		if (ntcoursedata < MAXTCOURSE)
			ntcoursedata++;

		fprintf (eout," %10.2f", timevec [nt] );
		STRPRINT1(" %10.2f", timevec [nt]);
		tcoursedata[ntcoursedata].t = timevec[nt];
		zeroprob = probeprob (ZERO);
		fprintf (eout," %15.6f", zeroprob );
		STRPRINT1(" %15.6f", zeroprob);
		tcoursedata[ntcoursedata].pr = zeroprob;
		if ( (zeroprob > 1e-7) and (zeroprob < ONE - 1e-7))   {
			problevel = ENEG1;
			cellnumber = cellcount (&problevel, &probwegot);
			lognumber = nlogs (cellnumber);
			fprintf (eout," %15e", lognumber);
			STRPRINT1(" %15e", lognumber);
			tcoursedata[ntcoursedata].logs = lognumber;
			fprintf (eout," %15e", cellnumber);
			STRPRINT1(" %15e", cellnumber);
			tcoursedata[ntcoursedata].avgcells = cellnumber;
			if ( (fabs (1- probwegot/problevel) > 0.01)
			and (verbos >= 2)
			and (lognumber > ZERO))   {
				fprintf(eout,"\n");
				STRPRINTEOL;
				fprintf 
			 (eout,"			      			");
				STRPRINT0("			      			");
				fprintf 
			 (eout,"(actual prob achieved %10f)", probwegot);
				STRPRINT1("(actual prob achieved %10f)", probwegot);
			}
		}
		if ( (zeroprob > 1e-4) and (zeroprob < ONE - 1e-4))   {
			problevel = ENEG1 *(1-zeroprob) + zeroprob;
			cellnumber = cellcount (&problevel, &probwegot);
			fprintf (eout," %15e", cellnumber);
			STRPRINT1(" %15e", cellnumber);
			if ( (fabs (1- probwegot/problevel) > 0.01)
			and (verbos >= 2)
			and (lognumber > ZERO))   {
				fprintf(eout,"\n");
				STRPRINTEOL;
				fprintf 
			 (eout,"			      			");
				STRPRINT0("			      			");
				fprintf 
			 (eout,"(actual prob achieved %10f)", probwegot);
				STRPRINT1("(actual prob achieved %10f)", probwegot);
			}
		} else
			fprintf (eout,"   -1" );
				STRPRINT0("   -1");
		fprintf(eout,"\n");
		STRPRINTEOL;
	}
}

void setInitialCellCounts();

extern BSTR EXPORT PASCAL Do_Timecourse(ioption)
int ioption;
{
	int i, nowenvl;			   

	setInitialCellCounts(ioption,NEWPATIENT,PGF,0);
	theta=LogMean;
	tau=LogStd;

	for (i= 1; i <= (int)ntypes; i++) 
	{ 
		for (nowenvl = 0; nowenvl < nenvirons; nowenvl++)
		{
			buildkill(i,nowenvl);
			setrates (i,nowenvl);
			smask[i] = 0;
			svec[i] = 0.0;
		}
	}

	timecourse();

	return VBSTRING(strTimecourse);
}
