#include <stdlib.h>
#include "build.h"
#ifdef DLL
#include <windows.h>
#else 
#include <string.h>
#endif
#include "defines.h"
#include "classes.h"
#include "cellp.h"
#include "tox.h"

void InterpretEvents(double etime,int event,char det1[255],char det2[255], char det3[255])
{
	void AddToDoseModList( int lasttreat, double reduct );
	void ApplyTrialSched();
	int GetCurrCTTreatment();
	int ToxLevel;
	int nToxMods,n,k,j,i,found;	
	int TreatmentsLeftInCourse;
	int CurrentTreatment, LastTreatment, finished, FirstType, ad;
	double ReductionList[5],trDose,CellCount;
	char chtemp[255],chtemp2[255],tmpstr[255],tmpstr2[255],tmpstr3[255];

	int idxToxMod[255],tempToxMod;

	if (event == DIAGNOSISEVENT) 
	{
		ApplyTrialSched();
		AddToEventQ(t,ONTRIALEVENT,".",".",".");
	} 
	
	if (event == TOXRESOLVEEVENT)
	{ 
		CurrentTreatment = GetCurrCTTreatment();
		i = 1;
		while(i<=nToxResolutions)
		{
#ifdef REALUNIX 
			if(strcasecmp(ToxResolutions[i].name, det2)==0 && strcmp("0", det3) == 0)
#else
			if(stricmp(ToxResolutions[i].name, det2)==0 && strcmp("0", det3) == 0)
#endif
			{
				/*Remove this Dose Modification from the list*/
				for(k=ToxResolutions[i].DoseModIndex; k<nDoseMods; k++)
				{
					//need to check the ToxResolutions array to see if we've changed any of the indexes stored 
					//in .DoseModIndex
					for (j=0; j<nToxResolutions; j++)
							if ( ToxResolutions[j].DoseModIndex = k + 1 ) ToxResolutions[j].DoseModIndex = k;

					DoseMods[k].Reduction = DoseMods[k+1].Reduction;
					DoseMods[k].LastTreatment = DoseMods[k+1].LastTreatment;
				}
				nDoseMods--;



				/*//if it's the current treatment, start at the next one, otherwise
				//start with this one.*/
				ad = 0;
				if (CurrentTreatment != 10000)
					if ((t + delta_t >= CTTreatment[CurrentTreatment].Time) && (t - delta_t <= CTTreatment[CurrentTreatment].Time))
						ad = 1;


				for(j=CurrentTreatment+ad; j <=numCTTreatments; j++)
				{
					/*CTTreatment[j].Dose = CTTreatment[j].Dose / (1 - ToxResolutions[i].reduction/100.0);*/
					CTTreatment[j].Dose = CTTreatment[j].FullDose;
					for(k=1; k<=nDoseMods; k++)
					{
						if(DoseMods[k].LastTreatment >= j || DoseMods[k].LastTreatment == -1)	
							CTTreatment[j].Dose = CTTreatment[j].Dose * (1 - DoseMods[k].Reduction/100.0);
					}
					cside_pdf(CTTreatment[j].AgentIndex,
							  CTTreatment[j].Time,
							  CTTreatment[j].Dose,CTTreatment[j].AgentIndex);
				}
		
				for (j=i; j<nToxResolutions; j++)
				{
					strcpy(ToxResolutions[j].name,ToxResolutions[j+1].name);
					ToxResolutions[j].reduction = ToxResolutions[j+1].reduction;
					ToxResolutions[j].DoseModIndex = ToxResolutions[j+1].DoseModIndex;
				}
				nToxResolutions--;
			}
			else
				i++;
		}
	}

	if ((event == TOXICITYEVENT) && ((ToxLevel = atoi(det3)) < 5))
	{
/*		ToxLevel = atoi(det3);*/
		if ((TrialInfo.OffStudyGrade > 0) && 
			(ToxLevel >= TrialInfo.OffStudyGrade) && 
			(OffTrial == False))
		{
			AddToEventQ(t,OFFTRIALEVENT, "High Toxicity", ".",".");
			trDose = (TreatmentExists(-1,t)).df ;
			if (trDose> 0.0)
			{
				found = false;
				i = 1;
				while((i<= Ndrug) &&(found == false))
				{
					if ((TreatmentExists(i,t)).df > 0.0)
					{
						CellCount = AllCells();
						sprintf(chtemp, "%e\0", CellCount);
						sprintf(tmpstr2,"%lf\0",trDose);
						strcpy(tmpstr3,Agents[i].name);
						tmpstr3[5] = (char) '\0';
						sprintf(tmpstr,"%d\0",i);
						AddToEventQ(t,TREATMENTEVENT,tmpstr3,tmpstr2,tmpstr);
						found = true;
					}
					i++;
				}
			}
			RemoveTreatments();
			OffTrial = True;
		}

		/*loop for Dose reduction due to tox goes here*/
		nToxMods = 0;
		if(OffTrial == False)
		{
			for(n=1;n<=TrialInfo.numToxModifications;n++)
			{
#ifdef REALUNIX
				if((ToxLevel >= TrialInfo.ToxCriteria[n].ReductionGrade) &&
				   ((strcasecmp(Event_List[0].detail2,TrialInfo.ToxCriteria[n].ToxType) == 0) ||
				    (strcasecmp("Any",TrialInfo.ToxCriteria[n].ToxType) == 0)))
#else 
				if((ToxLevel >= TrialInfo.ToxCriteria[n].ReductionGrade) &&
				   ((strcmpi(Event_List[0].detail2,TrialInfo.ToxCriteria[n].ToxType) == 0) ||
				    (strcmpi("Any",TrialInfo.ToxCriteria[n].ToxType) == 0)))
#endif
				{
					nToxMods++;
					idxToxMod[nToxMods] = n;
				}
			}   
		} 

	
		if(nToxMods > 0)
		{
			/* sort by reduction amount */
			for(n=1;n < nToxMods; n++)
				for(j=n+1; j <= nToxMods; j++)
					if(TrialInfo.ToxCriteria[idxToxMod[j]].Reduction > TrialInfo.ToxCriteria[idxToxMod[n]].Reduction)
					{
						tempToxMod = idxToxMod[j];
						idxToxMod[j] = idxToxMod[n];
						idxToxMod[n] = tempToxMod;
					}
			/* sort by ReductionType (grouped by reduction amount) */
			for(n=1; n < nToxMods; n++)
				for(j=n+1; j <= nToxMods; j++)
					if((TrialInfo.ToxCriteria[idxToxMod[j]].Reduction == TrialInfo.ToxCriteria[idxToxMod[n]].Reduction) &&
					   (TrialInfo.ToxCriteria[idxToxMod[j]].ReductionType < TrialInfo.ToxCriteria[idxToxMod[n]].ReductionType))
					{
						tempToxMod = idxToxMod[j];
						idxToxMod[j] = idxToxMod[n];
						idxToxMod[n] = tempToxMod;
					}

			/* Make a list of Reductions in order of precedence (Highest - Lowest, NEXTDOSE-RESTOFCOURSE-ALLREMAININGTREATMENTS */
			for(n=1;n<=4;n++)
				ReductionList[n] = 0.0;

			FirstType = TrialInfo.ToxCriteria[idxToxMod[1]].ReductionType;
			for(n=FirstType; n <= 4; n++)
			{
				finished = False;
				for(j=1;(j<=nToxMods && finished == False);j++)
				{
					if(TrialInfo.ToxCriteria[idxToxMod[j]].ReductionType == n)
					{
						ReductionList[n] = TrialInfo.ToxCriteria[idxToxMod[j]].Reduction;
						finished = True;
					}
				}	
			}

			/* Apply Reductions */
			/* CurrentTreatment = TreatmentCount; */  /* yuk only the last treatment is supported here */
			                                    /* dosemod rules should be distinguishable by treatment type */
			CurrentTreatment = GetCurrCTTreatment();


			for (n=1;n<=4;n++)
			{
				if(ReductionList[n] > 0.00000001)
				{
					LastTreatment = 0;
					switch ( n ) 
					{
					case ALLREMAININGCOURSES : 
						LastTreatment = numCTTreatments;
						AddToDoseModList(LastTreatment, ReductionList[n]);
					break; 
					case RESTOFCOURSE : 
						TreatmentsLeftInCourse = (TrialInfo.Regimen.numTreatments - 
				((CurrentTreatment) % TrialInfo.Regimen.numTreatments)) % TrialInfo.Regimen.numTreatments;
						LastTreatment = CurrentTreatment + TreatmentsLeftInCourse;
						AddToDoseModList(LastTreatment, ReductionList[n]);
					break; 
					case NEXTDOSE : 
						LastTreatment = CurrentTreatment + 1;
						AddToDoseModList(LastTreatment, ReductionList[n]);
					break;
					case UNTILTOXRESOLVES :
						LastTreatment = numCTTreatments;
						nToxResolutions++;
						strcpy(ToxResolutions[nToxResolutions].name , det2);
						ToxResolutions[nToxResolutions].reduction = ReductionList[n];
						AddToDoseModList(-1, ReductionList[n]);
						ToxResolutions[nToxResolutions].DoseModIndex = nDoseMods;
					break;
					}
					for(j=CurrentTreatment + 1; j<=LastTreatment && j <=numCTTreatments; j++)
					{
						CTTreatment[j].Dose = CTTreatment[j].Dose * (1 - ReductionList[n]/100.0);
						cside_pdf(CTTreatment[j].AgentIndex,
							      CTTreatment[j].Time,
								  CTTreatment[j].Dose,CTTreatment[j].AgentIndex);
					}
					buildsched();
					sortsched();
					/*CurrentTreatment = LastTreatment + 1;*/
					sprintf(chtemp2, "%d\0", n);
					sprintf(chtemp, "%f\0", ReductionList[n]);
					AddToEventQ(t,DOSEMODIFICATIONEVENT, Event_List[0].detail2, chtemp, chtemp2);
				}
			}
		}
	}
}

void AddToDoseModList( int lasttreat, double reduct )
{
	nDoseMods++;
	DoseMods[nDoseMods].Reduction = reduct;
	DoseMods[nDoseMods].LastTreatment = lasttreat;
}

int GetCurrCTTreatment()
{
	int i,found;

	found = 10000;
	i = 1;
	while (i <=numCTTreatments && found == 10000)
	{
		if(t - delta_t < CTTreatment[i].Time)
			found = i;
		else
			i++;
	}
	return found;
}