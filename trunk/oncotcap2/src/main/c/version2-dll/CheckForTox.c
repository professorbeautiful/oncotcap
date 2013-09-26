#include "build.h"
#ifdef DLL
#include <windows.h>
#endif
#include "defines.h"
#include "classes.h"
#include "cellp.h"
#include "tox.h"


/*	Filename	:Toxicity.c 
	Author		: Sai
	Date		:06/19/97
	Comments	:Ported from VB
*/

void CheckForTox()
{
	double prob,probsum,ThisTox,ThisProb,dose,TotalCells;
	int itox,jtox,igrade,iagent;
	char temp[255];
	double nextTreatTime,nextToxResTime,dtemp;
	double antilogit(), logit();


	if ((t > firstTreatmentTime) && (RandInit == False))
	{
		/* srand(ranSeed3); */
		/* QSH confirms this is OK */
		ranrmarin(ranSeed1,ranSeed3%30081,TOXRAND);
		RandInit = True;
	}
	
	nextTreatTime = EndT;
	nextToxResTime = EndT;

	if (t < nextToxTime)
	{
		return;
	}
	/* First allow all toxicity to resolve */

	for(jtox=1;jtox<=nToxTypes;jtox++)
	{
		switch((int)ToxTypes[jtox].toxResType) 
		{
		case NEVERRESOLVES:
			break;		
		case BYONEGRADE:
			if ((ToxTypes[jtox].currGrade > 0) && ((ToxTypes[jtox].lastToxTime + ToxTypes[jtox].toxResTime) < t+delta_t/2))
			{
				ToxTypes[jtox].currGrade--;
				ToxTypes[jtox].lastToxTime = t;
				if(ToxTypes[jtox].currGrade <0)
				{
					ToxTypes[jtox].currGrade=0;
				}
				sprintf(temp,"%d",ToxTypes[jtox].currGrade);
				AddToEventQ(t,TOXRESOLVEEVENT,".",ToxTypes[jtox].name,temp);
			}
			break;
		case FALLTOZERO:
			if((ToxTypes[jtox].currGrade > 0) && (( ToxTypes[jtox].lastToxTime + ToxTypes[jtox].toxResTime) < t+delta_t/2  ))
			{
				ToxTypes[jtox].currGrade=0;
				ToxTypes[jtox].lastToxTime = t;
				sprintf(temp,"%d",ToxTypes[jtox].currGrade);
				AddToEventQ(t,TOXRESOLVEEVENT,".",ToxTypes[jtox].name,temp);
			}
			break;

		}
	}

	/* Now generate the toxicities */
	for(iagent=1;iagent<=ndrugs;iagent++)
	{
		dose = (TreatmentExists(iagent,t)).df;
		for(itox=1;itox<=Agents[iagent].numToxTypes;itox++)
		{
			jtox=Agents[iagent].toxicity[itox].toxTypeIndex;			
			if(( dose > 0.0) && (ToxTypes[jtox].lastToxTime <= t))
			{
				prob = ranmarm(TOXRAND);
				probsum=0;
				igrade = 6;
				while((prob >= probsum) && (igrade > 0))
				{
					igrade--;
/*  changed by r.d. march 8,1998 */
					ThisProb=Agents[iagent].toxicity[itox].toxProbs[igrade];
					if (ThisProb > 0.0){
						if (ThisProb  == 1.0) {
							ThisTox = 1.0;
						}
						else{
							ThisTox= antilogit(logit(ThisProb)
							+ log(dose)/0.75);
						}
					/*// Any sigma between 1/2 and 1 is reasonable.
					// for now, fixed at 0.75. */
						probsum = probsum + ThisTox;					
					}
				}
				if(igrade >0)
				{
					if(Agents[iagent].toxicity[itox].toxAppType == CUMTOX)
					{
						ToxTypes[jtox].currGrade = ToxTypes[jtox].currGrade + igrade;
						if (ToxTypes[jtox].currGrade > 5)
						{
							ToxTypes[jtox].currGrade = 5;
						}						 
					}
					else
					{
						if (ToxTypes[jtox].currGrade < igrade)
						{
							ToxTypes[jtox].currGrade = igrade;
						}
					}
					sprintf(temp,"%d",ToxTypes[jtox].currGrade);
					AddToEventQ(t,TOXICITYEVENT,Agents[iagent].name,Agents[iagent].toxicity[itox].name,temp);
					ToxTypes[jtox].lastToxTime =t;
					if(ToxTypes[jtox].currGrade >=5)
					{
						Dead = True;
						TotalCells = AllCells();
						AddToEventQ(t,DEATHEVENT," ",Agents[iagent].name,"5");
						sprintf(temp,"%e",TotalCells);
						/* This code was commented to be consistent
						there is no end event after a death due to tox  or tumor
						if(TotalCells < diagnosis_threshold)
						{

							AddToEventQ((t),EFUNEDEVENT,temp,".",".");
						}
						else
						{
							AddToEventQ((t),EFUTUMEVENT,temp,".",".");
						}*/
						return;
					}
				}
			}
			if (nextToxResTime > ( ToxTypes[jtox].lastToxTime + ToxTypes[jtox].toxResTime))
			{
				if ( (ToxTypes[jtox].lastToxTime + ToxTypes[jtox].toxResTime) > t)
				{
					nextToxResTime = ToxTypes[jtox].lastToxTime + ToxTypes[jtox].toxResTime;
				}
			}
		}
		dtemp = TreatmentExistsInt(iagent,t,EndT);
		if (dtemp < nextTreatTime)
		{
			if (dtemp > t) 
			{
				nextTreatTime = dtemp;
			}
		}
	}
	if ((nextToxResTime > nextTreatTime) && nextTreatTime > 0.0 )
	{
		nextToxTime = nextTreatTime;
	}
	else
	{
		nextToxTime = nextToxResTime;
	}
	
}


double logit( double p)
{
	return ( log(p/(1-p)));
}

double antilogit( double x)
{	
	return ( 1 - 1/(1+exp(x)));
}


				














		