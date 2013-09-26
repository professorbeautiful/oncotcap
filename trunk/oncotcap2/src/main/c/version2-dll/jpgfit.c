#include "build.h"
#include "defines.h"


void CheckIfICRuleAppliesPGF();


		/**@ jpgfit.p **/
void /*PROCEDURE*/ jpgfit (sold,snew,itime,nowenvl)   /*COMPILED*/
cellarray(sold);
cellarray(snew);
int itime,nowenvl;
   /*COMPILED*/
/*	cellarray(sold);
	cellarray(snew);
	integer itime, nowenv; */
/*VAR*/{

     integer itype, rtype, stype ;
     real tdiff;
	 real culltop, cullbot, cullarg;
	 real muttop, mutbot, mutarg;
	 real cr, mr, br, blebr;
	 real xrate, crate, mrate;
	 real w;
	 int iagent;
	 double Dose;

	for (itype = 1; itype  <= (int) ntypes; itype ++)  {
		w = ONE - sold [itype];
		w = timesurv(itime,itype,nowenvl) * w;
		sold [itype] = ONE - w;
		/**** s = 1- (1-s)t ***/
	}

    /* do for all drugs*/
	for (iagent=1;iagent<Ndrug;iagent++)
	{
		/*if a treatment exists for this drug at this time*/
		Dose = (TreatmentExists(iagent,timevec[itime])).df;
		if ( Dose > 0.0) 
		{
			CheckIfICRuleAppliesPGF(sold,itime,nowenvl,iagent,Dose);
		}
	}


/* psudo code by rd for new conversion rule 7/7/98 -wes */
/*	for (idrug = 1; ...
		if ( drug[idrug] is administered now){  //loop over all cytotoxic agents
			UnpackCytotoxicConversionRules();
			for (ConversionRulesNow...  //loop over conversion rules
				// Given drug, dose, class, source, dest, prob
				ModifyProbByDose
				for (stype = 1; stype  <= (int)ntypes; stype ++)  {
					if ( levelMatchesSource)
					for (rtype = 1; rtype  <= (int)ntypes; rtype ++)  {
						if ( levelMatchesDest)
							sold[stype] = sold[stype]-(sold[rtype]-sold[stype]*(1-convprob)); */
/*                                               ^     ^                     ^
                                                 |     |                     |
                      I think this should be rtype     |                     | 
													   |                     |
								and	I think there should be parenthesis around this expression - wes */


	
    tdiff = timevec [itime] - timevec [itime - 1];

	for (stype = 1; stype  <= (int)ntypes; stype ++)  {
		cullbot = gama(stype,nowenvl);
		mutbot = mu(stype,nowenvl);
		culltop = deathrate(stype,nowenvl);
		muttop = ZERO;
		br = birthrate (stype,nowenvl);
		for (rtype = 0; rtype  < LookUp[stype].ParaPr->NumOfMu; rtype ++)  {
			mr = mutrate(stype,rtype,nowenvl);
			if ( (mr !=ZERO))   {
				muttop = muttop + mr * br *
			    pgf1 (
			    stype, MuLookUp(stype,rtype,nowenvl), tdiff, sold [MuLookUp(stype,rtype,nowenvl)]);
				if ( (verbos >= 6)) 
					fprintf (eout,"	muttop, mutbot: %f, %f",
					muttop, mutbot);
			}
		}
		for (rtype = 0; rtype  < LookUp[stype].ParaPr->NumOfBl; rtype ++)  {
			blebr = blebrate(stype,rtype,nowenvl);
			if ( (blebr !=ZERO))   {
				muttop = muttop + blebr *
				    pgf1 (
				    stype, BlLookUp(stype,rtype,nowenvl), tdiff, sold[BlLookUp(stype,rtype,nowenvl)]);
				if ( (verbos >= 6)) 
					fprintf (eout,"	muttop, mutbot: %f, %f ",
					muttop, mutbot);
			}
		}
		for (rtype = 0; rtype  < LookUp[stype].ParaPr->NumOfCo; rtype ++)  {
			cr = convrate(stype,rtype,nowenvl);
			if ( (cr !=ZERO))   {
				culltop = culltop + cr *
			    pgf1 (
			    stype,CoLookUp(stype,rtype,nowenvl),tdiff, sold[CoLookUp(stype,rtype,nowenvl)]);
				if ( (verbos >= 6)) 
					fprintf (eout,"	culltop, cullbot: %f, %f",
					culltop, cullbot);
			}
		}
		xrate = xi (stype,nowenvl) ;
		crate = gama (stype,nowenvl) ;
		mrate = mu (stype,nowenvl) ;
		if ( (cullbot !=ZERO)) 
			cullarg = culltop/cullbot;
		else    cullarg = ZERO;
		if ( (mutbot !=ZERO)) 
			mutarg = muttop/mutbot;
		else    mutarg = ZERO;

		snew [stype] = pgfxcm (
			sold [stype],cullarg,mutarg,
			xrate, crate, mrate, tdiff);
	}
}
















