#include "build.h"
#include "defines.h"
#include "tox.h"
#include "regimen.h"
#include "BBoard.h" 

/*	Filename	:ApplyRegimen.c 
	Author		:Sai
	Date		:08/31/98
	Comments	:This code will apply a regimen into the exisiting schedule
                 regimenIndex is the index in the Regimens Structure
*/

int ApplyRegimenIntoSchedule(void *args[1])
{
	int *regIdx,retVal;

	regIdx = args[0];

	retVal = ApplyRegimenIntoScheduleA(*regIdx);
	return(retVal);
}


int ApplyRegimenIntoScheduleA(int regimenIndex)
{
	/*Insert the contents of the regimen structure into ndoses and doselist
	/The run buildsched to rebuild the sched array
	/then run sortsched to sort it
	/then run buildkill to build the timersurv and timekill arrays
	*/
	int treatIndex,agentIndex,idrug;
	double treatTime,treatDose;
	int i,j,k;


	if (Regimens[regimenIndex].numTreatmentApplications > 0 ) 
	// this means the information in the regimen is not in the string 
	{
		// First put the times in
		for(i=0; i < Regimens[regimenIndex].numTreatmentApplications;i++)
		{
			// get the index and the time of application
			treatIndex = Regimens[regimenIndex].Treatments[i].TreatIndex;
			treatTime = Regimens[regimenIndex].startTime[0] + Regimens[regimenIndex].Treatments[i].Time;
			treatDose = Regimens[regimenIndex].Treatments[i].Dose;

			switch (Regimens[regimenIndex].Treatments[i].TreatType) {
			//switch based on treatment type
				case TCAPAGENT:
					ndoses[treatIndex] +=1;
					doselist[treatIndex][ndoses[treatIndex]] = treatTime;
					dosefactorlist[treatIndex][ndoses[treatIndex ]] = treatDose;
					break;
				case TCAPCOMBO:
					for (j=0;j<Combos[treatIndex].numAgents;j++)
					{
						agentIndex = Combos[treatIndex].cmbAgents[j].Index;
						ndoses[agentIndex] +=1;
						doselist[agentIndex][ndoses[agentIndex]] = treatTime;
						treatDose = treatDose * Combos[treatIndex].cmbAgents[j].Dose;
                        dosefactorlist[agentIndex][ndoses[agentIndex]] = treatDose;
					}
					break;
				case TCAPCOURSE:
					for (j=0;j<Courses[treatIndex].numAgents;j++)
					{
						agentIndex = Courses[treatIndex].courseAgents[j].Index;
						for (k=0;k<Courses[treatIndex].courseAgents[j].numApps;k++)
						{
							treatTime = treatTime + Courses[treatIndex].courseAgents[j].AppTimes[k];
							ndoses[agentIndex] +=1;
							doselist[agentIndex][ndoses[agentIndex]] = treatTime;
							treatDose = treatDose * Courses[treatIndex].courseAgents[j].Dose;
							dosefactorlist[agentIndex][ndoses[agentIndex]] = treatDose;
						}
					}
					break;
			}

		}		
	}
	else if(Regimens[regimenIndex].numTreats > 0 ) 
	{
		for(idrug = 1 ; idrug <= Regimens[regimenIndex].numTreats; idrug++)
		{
			agentIndex = Regimens[regimenIndex].agentIndex[idrug];
			for ( i = 1; i <= Regimens[regimenIndex].numRepetitions[idrug]; i++ )
			{
				ndoses[agentIndex] = ndoses[agentIndex] + 1;
				doselist[agentIndex][ndoses[agentIndex]] = Regimens[regimenIndex].startTime[idrug] + ( i - 1 ) * Regimens[regimenIndex].Interval[idrug];
				dosefactorlist[agentIndex][ndoses[agentIndex]] = Regimens[regimenIndex].treatDose[idrug];
				TreatmentIdxList[agentIndex][ndoses[agentIndex]] = Regimens[regimenIndex].treatIndex[idrug];
				if (TreatmentIdxList[agentIndex][ndoses[agentIndex]] > 2)
					MsgBoxWarning("OVER2");
			}
		}
	}
	else
	{
		ParseSchedule(Regimens[regimenIndex].TextDef,1,Regimens[regimenIndex].treatIndex);
	}
	//next run buildsched
	buildsched();
	sortsched();
	CalculateTimeKillSurv();
	InitNextToxTime();
	return(0);
}
	









			
		

