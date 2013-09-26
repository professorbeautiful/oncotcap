#include "build.h"

#include "defines.h"
		/**@ sortsched.p **/
#define div /   /*COMPILED*/
void /*PROCEDURE*/ sortsched()
	/** called by buildkill **/
/*VAR*/{

	  integer gap, i, j, jg;
	 real ttemp;
	 drug dtemp;
	 double dftemp;

	gap = nsched div 2;
	while ((gap > 0))  {
		for (i = gap+1; i  <= nsched; i ++)  {
			j = i - gap;
			while ((j > 0))  {
				jg = j + gap;
				if ( (sched[j].t <= sched[jg].t)) 
					j = 0;
				else  {
					ttemp = sched[j].t;
					sched[j].t = sched[jg].t;
					sched[jg].t = ttemp;
					dtemp = sched[j].d;
					sched[j].d = sched[jg].d;
					sched[jg].d = dtemp;
					dftemp = sched[j].df;
					sched[j].df = sched[jg].df;
					sched[jg].df = dftemp;
					dtemp = sched[j].TreatmentIdx;
					sched[j].TreatmentIdx = sched[jg].TreatmentIdx;
					sched[jg].TreatmentIdx = dtemp;
				}
				j = j - gap;
			}
		}
		gap = gap div 2;
	}
}
