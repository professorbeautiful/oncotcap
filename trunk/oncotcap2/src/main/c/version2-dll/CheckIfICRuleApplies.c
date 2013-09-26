#include "build.h"
#ifdef DLL
#include <windows.h>
#endif
#include "defines.h"
#include "classes.h"
#include "cellp.h"
#include "InducedConv.h"


boolean CheckIfICRuleApplies(agentIndex,dose)
int agentIndex;
double dose;
{
	/*//this code is called from checkfortreatment.  
	//So it assumes that a treatment exists at t */

	int i, icellindex,icelltype,dcellindex,dcelltype;
	int WasARuleApplied;
	double nCellCount, nChanged;
	int envnowl;
	double convProb;

	/*init*/
	WasARuleApplied = FALSE;
	envnowl = getenvir(t);


	for (i=1;i<=nICrules;i++)
	{
		/*// check if the agent applies is the agent in the rule
		// check if the environ is the same as in the rule */
		if ((icRules[i].agentIndex == agentIndex) && ((icRules[i].envIndex == envnowl) || (icRules[i].envIndex < 0)) )
		{
			/*for all active cell types*/
			for (icellindex = 1; icellindex <=active_ntypes;icellindex++)
			{
				if (DoesICRuleAppliesToCelltype(i,icellindex) == TRUE)
				{
					/*//this means the celltype has completely satified the rule
					//and we should apply the rule */
					icelltype = LookUp[icellindex].LookUpId;
					/*get the index of the destination cell type*/
					dcelltype = get_dest_gap(icRules[i].Source.classIndex,icRules[i].Source.levelIndex,icRules[i].Dest.levelIndex) + icelltype;
					dcellindex = getcellindex(dcelltype);
					if( dcellindex <= 0 )
					{
						/* the dcelltype does not exist in the Just in Time Lookup */
						create_para_struct(dcelltype,SIM);
						/* the index of the cell type created is */
						dcellindex = change_index; 
					}
					WasARuleApplied = TRUE;
					/*get the cellcount of this celltype*/
					nCellCount = CN[icellindex];
					/* get the number of cells that have converted*/
					convProb = antilogit(logit(icRules[i].Prob) + log(dose) *0.75);
					nChanged = gbinomi(nCellCount,convProb);
					CN[dcellindex] +=nChanged;
					CN[icellindex] -= nChanged;

				}/*if*/
			}/*for*/
		}/*if*/
	}/*for*/
	return(WasARuleApplied);
}


boolean DoesICRuleAppliesToCelltype(i,icellindex)
int i,icellindex;
{
	int icelltype;
	int iclass,iclassindex,ans,celltypeSatisfiesifCond;

	/*init*/
	celltypeSatisfiesifCond = TRUE;
	icelltype = LookUp[icellindex].LookUpId;
						
	iclass = 0;
	/*check the class and levels in the if part of the class*/
	while (iclass < icRules[i].nClassAndLevels)
	{
		iclassindex = icRules[i].ifClass[iclass].classIndex;
		/*ans gets the level in the class iclass for icelltype*/
		ans = getcelllevelbyclass(icelltype,iclassindex);
		if (ans >= 0 )
		{
			/*check if this is the level in the rule*/
			if (icRules[i].ifClass[iclass].levelIndex != ans)
			{
				return(FALSE);
			}
		}
		iclass = iclass + 1;
	}

	/*//if the celltype gets here that means it has satisfied all the class:level
	//compares in the if part of the rule.
	//now check if the source class and level match */
	ans = getcelllevelbyclass(icelltype,icRules[i].Source.classIndex);
	if (ans >= 0 )
	{
	/*check if this is the level in the Source part of the rule*/
		if (icRules[i].Source.levelIndex == ans)
		{	
			/*this means the celltype has completely satified the rule*/
			return(TRUE);
		}
		else
			return(FALSE);
	}
	else
			return(FALSE);
}

		



















