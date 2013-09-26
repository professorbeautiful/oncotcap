#include "build.h"

#include "defines.h"
void InitDoseFactor();

		/**@ buildsched.p **/
void /*PROCEDURE*/ buildsched()   /*COMPILED*/
	/** purpose: to build sched and nsched out of doselist **/
	/** called by buildkill **/
/*VAR*/{
  integer idrug, idose;

	nsched = 0;
	for (idrug = ENVINDEX; idrug  <= ndrugs; idrug ++)
		for (idose = 1; idose  <= ndoses [idrug]; idose ++)  {
			nsched = nsched + 1;
			if ( nsched <= MAXTIMES)   {
				sched [nsched] . d = idrug;
				sched [nsched] . t = doselist [idrug][idose];
				sched [nsched] . df = dosefactorlist [idrug][idose];
				sched [nsched] . TreatmentIdx = TreatmentIdxList[idrug][idose];
				if (TreatmentIdxList[idrug][idose] > 2)
					MsgBoxWarning("OVER2");
			}
		}
	if ( nsched > MAXTIMES)   {
		fprintf (eout,"CAUTION: total # timepoints must be less than %4d",
			 MAXTIMES+1);
		fprintf (eout, "nsched is too big (buildsched): %d", nsched);
		nsched = MAXTIMES;
	}
/*	InitDoseFactor();  */
} 
