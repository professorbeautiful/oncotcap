#include "build.h"
#ifdef DLL
#include <windows.h>
#endif
#include "defines.h"
#include "classes.h"
#include "cellp.h"
#include "InducedConv.h"

int getcelllevelbyclass ();
int get_dest_gap();
int getcellindex();
int getenvir();
struct drugandtime TreatmentExists();

/*	Filename	: CheckIfICRuleAppliesPGF.c 
	Author		: Sai
	Date		: 07/28/98
	Comments	: Applies the Induction Rule to the PGF engine.  This rule
	              is applied when a treatment is given
*/
boolean CheckIfICRuleAppliesPGF(sold,itime,nowenvl,iagent,dose)
cellarray(sold);
int itime,nowenvl,iagent;
double dose;
{

	int i, icellindex,icelltype,dcellindex,dcelltype;
	double convProb;
	boolean WasARuleApplied;

	WasARuleApplied = FALSE;
	
	/*then do for all ic rules*/
	for (i=1;i<=nICrules;i++)
	{
		/*Modify Prob based on dose*/
		convProb = antilogit(logit(icRules[i].Prob) + log(dose)*0.75);
		/*check if the agent applies is the agent in the rule*/
		/* check if the environ is the same as in the rule*/
		if ((icRules[i].agentIndex == iagent) 
			&& ((icRules[i].envIndex == nowenvl) || (icRules[i].envIndex < 0)))
		{
			/*for all active cell types*/
			for (icellindex = 1; icellindex <=active_ntypes;icellindex++)
			{
				if (DoesICRuleAppliesToCelltype(i,icellindex) == TRUE)
				{
					/*//this means the celltype has completely satified the rule
					//and we should apply the rule*/
					icelltype = LookUp[icellindex].LookUpId;
					dcelltype = get_dest_gap(icRules[i].Source.classIndex,icRules[i].Source.levelIndex,icRules[i].Dest.levelIndex) + icelltype;
					dcellindex = getcellindex(dcelltype);

					sold[icellindex] = convProb*sold[dcellindex]
								+ (1-convProb)*sold[icellindex]; 

					WasARuleApplied = TRUE;
				}
			}
		}
	}
	return(WasARuleApplied);
}

