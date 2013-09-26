#include "build.h"
#ifdef DLL
#include <windows.h>
#endif
#include "defines.h"
#include "classes.h"
#include "cellp.h"
#include "tox.h"

void check_events()
{
	double TotalCellCount, CellCount, CombinedTumorSize;
	static double NextExamTime;
	int k, CRHappened, DiagHappened, ExamPoint, AllToSmall;
	static int JustDiagnosed;
	char chtemp[255];

	if ((Cured == False) && (Dead == False))
	{
		CombinedTumorSize = 0.0;
		/* previousCombinedTumorSize = 0.0; */
		TotalCellCount = AllCells();
		sprintf(chtemp, "%.4e\0", TotalCellCount);

		/*//Set a flag to designate if this is an examination point
        //Start exams at time of first treatment, don't examine if diagnosis hasn't yet occured.*/
		if (AfterFirstTreatment == True && t >= NextExamTime  && PrimaryDiagnosed == True)
		{
			ExamPoint = True;
			NextExamTime = NextExamTime + TumorExaminationInterval;
			t=t;
		}
		else
			ExamPoint = False;

		/*//if no location class was specified in the model, use total cell counts
		//for these events */
		if (LocationClass == -1)
		{
			if (t > firstTreatmentTime && t < (firstTreatmentTime + delta_t) && AfterFirstTreatment == False)
			{
				AfterFirstTreatment = True;
				CellCountAtFirstTreatment[0] = previousCellCount[0];
				previousCellCountAtEval[0] = previousCellCount[0];
				/*CombinedTumorSizeAtFirstTreatment = pow(previousCellCount[0], TumorCombinationFactor);*/
				previousCombinedTumorSize = pow(previousCellCount[0], TumorCombinationFactor);
				ExamPoint = True;
				NextExamTime = t + TumorExaminationInterval;
			/*	if (PrimaryDiagnosed == False)
				{
					MsgBoxWarning("The first treatment is before tumor diagnosis");
				} */
			}

		   /* check for a diagnosable tumor */
		   if ((previousCellCount[0] < PE_diagnosis_threshold) &&
			   (TotalCellCount >= PE_diagnosis_threshold) && (DiagnosedOnce[0] != True))
		   {
			   /* if the tumor was diagnosed once, this is a recurrence not a diagnosis */

			   AddToEventQ(t,DIAGNOSISEVENT,chtemp,".",".");
			   DiagnosedOnce[0] = True;
			   PrimaryDiagnosed = True;
			   DiagnosedNow = True;
		   }

           /*Check for CR, only check at examination points, when a CR hasn't occured*/
		   if (ExamPoint == True)
		   {
			   if ((previousCellCountAtEval[0] < LR_diagnosis_threshold) &&
				   (TotalCellCount >= LR_diagnosis_threshold) && (DiagnosedOnce[0] == True) &&
				   (DiagnosedNow == False))
			   {
				   AddToEventQ(t,RECURRENCEEVENT,chtemp,".",".");
				   DiagnosedNow = True;
			   }



			   if(CompleteResponse.checked == True && CompleteResponse.event == False)
			   {
				   if (TotalCellCount < LR_diagnosis_threshold)
				   {
					   CompleteResponse.event = True;
					   AddToEventQ(CompleteResponse.time,CREVENT,".",".",".");
					   previousCombinedTumorSize = CombinedTumorSize;
					   DiagnosedNow = False;
				   }
				   else
				   {
					   CompleteResponse.checked = False;
				   }
			   }

			   if(CompleteResponse.event == False)
			   {
				   if(previousCellCountAtEval[0] >= LR_diagnosis_threshold  &&
					  TotalCellCount < LR_diagnosis_threshold &&
					  CompleteResponse.checked == False)
				   {
					   CompleteResponse.checked = True;
					   CompleteResponse.time = t;
				   }
			   }

			   previousCellCountAtEval[0] = TotalCellCount;
		   }

		   if ( TotalCellCount > CellCountAtFirstTreatment[0])
		   {
			   ResponseHappened[0] = False;
		   }

		   /*//Calculate Combined tumor size.  This is used later to check for PR.
		   //Only do this at Examination points, if a complete or partial response hasn't
		   //already occured. (pow()'s are expensive) */
		   if (ExamPoint == True && 
			   CompleteResponse.event == False && 
			   PartialResponse.event == False)
		   {
			   if(DiagnosedNow == True)
			   {
				   CombinedTumorSize = pow(TotalCellCount, TumorCombinationFactor);
				   AllToSmall = False;
			   }
			   else
			   {
				   AllToSmall = True;
				   CombinedTumorSize = 0;
			   }
		   }
		}
		/*//if there is a location  class specified in the model, check for Diagnosis,
		//Recurrence, Complete Response, Response, and Metastasis */
		else
		{   
			CRHappened = True;
			DiagHappened = False;
			AllToSmall = True;

			for (k = 0; k < Class[LocationClass].no_levels; k++)
			{
				CellCount = sumcellbylevel(LocationClass, k);
				sprintf(chtemp, "%.4e\0", CellCount);

				if ((t >= firstTreatmentTime) && (t < (firstTreatmentTime +delta_t)) && (AfterFirstTreatment == False))
				{
					if (k == Class[LocationClass].no_levels - 1)
						AfterFirstTreatment = True;
					CellCountAtFirstTreatment[k+1] = previousCellCount[k+1];
					previousCellCountAtEval[k+1] = previousCellCount[k+1];
					/*CombinedTumorSizeAtFirstTreatment = CombinedTumorSizeAtFirstTreatment + pow(previousCellCount[k+1], TumorCombinationFactor);*/
					previousCombinedTumorSize = previousCombinedTumorSize + pow(previousCellCount[k+1], TumorCombinationFactor);
					ExamPoint = True;
					NextExamTime = firstTreatmentTime + TumorExaminationInterval;
				/*	if (PrimaryDiagnosed == False)
					{
						
						MsgBoxWarning("The first treatment is before tumor diagnosis");
					} */
				}
				if ((previousCellCount[k+1] < PE_diagnosis_threshold) &&
					(CellCount >= PE_diagnosis_threshold) && (PrimaryDiagnosed == False))
				{
					{

						AddToEventQ(t,DIAGNOSISEVENT,chtemp,Class[LocationClass].Level[k].level_name,".");
						DiagnosedOnce[k+1] = True;
						CurrentlyDiagnosed[k+1] = True;
						DiagnosedNow = True;
						PrimaryDiagnosed = True;
						PrimaryTumor = k + 1;
						JustDiagnosed = True;
					}
				}

				/*//if this isn't the primary site (first level by definition, yech)
				//and previously there were no cells and now there is, its a metastasis event
				//OK never mind the above crap!  If a tumor happens where there wasn't one before, call it a 
				//TumorStart Event.  -wes 3/5/98 */
				if (( /* (k > 0) && */ previousCellCount[k+1] < 0.5) && (CellCount >= 1.0) /* && ((k+1) != PrimaryTumor) */ )
				{
					/*if (PrimaryTumor == -1)
						PrimaryTumor = k+1;
					else  if (PrimaryTumor != (k+1)) */
						AddToEventQ(t,TUMORSTARTEVENT,chtemp,Class[LocationClass].Level[k].level_name,".");
				}		

/*				if ((previousCellCountAtEval[k+1] < PE_diagnosis_threshold) &&
					(CellCount >= PE_diagnosis_threshold) )
					 if((PrimaryDiagnosed == True) && (CurrentlyDiagnosed[k+1] == False) &&
					(PrimaryTumor != k+1))
				{
						AddToEventQ(t,METEVENT,chtemp,Class[LocationClass].Level[k].level_name,".");
						CurrentlyDiagnosed[k+1] = True;
				} */

				if (ExamPoint == True)
				{
					if ((TrialSim == True) &&
						(TrialInfo.MaxProgression >= 0) &&
						(TSSecondTreatmentHappened == True) &&
						(k+1 == PrimaryTumor) &&
						(OffTrial == False) &&
						(pow(CellCount,0.66666) >= pow((InitialPrimaryTumorSize * ( 1.0 + (TrialInfo.MaxProgression/100.0))),0.6666666)))
					{
							AddToEventQ(t,OFFTRIALEVENT,"Tumor Progression",".",".");
							OffTrial = True;
							/* delete remaining treatments now */
							RemoveTreatments();
					}



					if ((previousCellCountAtEval[k+1] < LR_diagnosis_threshold) &&
						(CellCount >= LR_diagnosis_threshold) && (PrimaryDiagnosed == True) /* && 
						(DiagnosedNow == False)*/ && ((JustDiagnosed == False) || (PrimaryTumor != k+1)))
					{
						if (DiagnosedOnce[k+1] == True)
						{ 
							AddToEventQ(t,RECURRENCEEVENT,chtemp,Class[LocationClass].Level[k].level_name,".");
						}
						else
						{
							AddToEventQ(t,RECURRENCEEVENT,chtemp,Class[LocationClass].Level[k].level_name,".");
							DiagnosedOnce[k+1] = True;
						} 
						CurrentlyDiagnosed[k+1] = True;
						DiagnosedNow = True;
					}

/*					if (previousCellCountAtEval[k+1] >= LR_diagnosis_threshold || CellCount >= LR_diagnosis_threshold)
					{
						CurrentlyDiagnosed[k+1] = True;
					}
					else
						CurrentlyDiagnosed[k+1] = False;*/
					if(previousCellCountAtEval[k+1] >= LR_diagnosis_threshold)
						AllToSmall = False;


					if(( !(CellCount < LR_diagnosis_threshold) ) && CurrentlyDiagnosed[k+1] == True)
					{
						if ((CellCount >= LR_diagnosis_threshold && CompleteResponse.checked == True) || 
							 CompleteResponse.checked == False)
						{
							      CRHappened = False;
						}
					}

					previousCellCountAtEval[k+1] = CellCount;
				}

				/*//Calculate combined tumor size.  This is used later to check for PR.
				//Only do this at Examination points, if a complete or partial response hasn't already occured*/
				if ( ExamPoint == True &&  
					 CompleteResponse.event == False && 
					 PartialResponse.event == False)
						if(CurrentlyDiagnosed[k+1] == True)
							CombinedTumorSize = CombinedTumorSize + pow(CellCount, TumorCombinationFactor);
				
				previousCellCount[k+1] = CellCount;
			}
			if(CompleteResponse.event == False && ExamPoint == True)
			{
				if(CompleteResponse.checked == True && CRHappened == True)
				{
					CompleteResponse.event = True;
					AddToEventQ(CompleteResponse.time,CREVENT,".",".",".");
					previousCombinedTumorSize = CombinedTumorSize;
					DiagnosedNow = False;
					JustDiagnosed = False;
				}
				else
				{
					CompleteResponse.checked = False;
				}

				if(CompleteResponse.event == False && 
				   CRHappened == True && 
				   CompleteResponse.checked == False  && 
				   AllToSmall == False)
				{
					CompleteResponse.checked = True;
					CompleteResponse.time = t;
				}
			}

		}

		
		/*//Check PR events
		//Only check on every examination interval, if a complete response hasn't already happened */
		if (ExamPoint == True)
		{
			if (CompleteResponse.event == False && 
			    PartialResponse.event == False)
			{
				if(PartialResponse.checked == True)
				{
					if (CombinedTumorSize <= PartialResponse.previousSize * response_threshold)
					{
						PartialResponse.event = True;
						previousCombinedTumorSize = CombinedTumorSize;
						AddToEventQ(PartialResponse.time,RESPONSEEVENT,".",".",".");
					}
					else
					{
						PartialResponse.checked = False;
					}
				}
				if(PartialResponse.checked == False &&
				   CombinedTumorSize <= previousCombinedTumorSize * response_threshold &&
				   previousCombinedTumorSize > 0 && AllToSmall == False)
				{
					PartialResponse.checked = True;
					PartialResponse.time = t;
					PartialResponse.previousSize = previousCombinedTumorSize;
				}		
			}
			if (CompleteResponse.event == True )
			{
				PartialResponse.checked = False;
				PartialResponse.event = False;
				CompleteResponse.checked = False;
				CompleteResponse.event = False;
			}
			if (PartialResponse.event == True)
			{
				PartialResponse.checked = False;
				PartialResponse.event = False;
			}

			if (CombinedTumorSize > previousCombinedTumorSize)
				previousCombinedTumorSize = CombinedTumorSize;
		}


		/*// Check for cure or death.
		// if cellcount is zero the patient is cured*/
		if (TotalCellCount < 0.5 && ExamPoint == True)
		{
			AddToEventQ(t,CUREEVENT,".",".",".");
			Cured = True;
			/*//check for a CR at this point.  If there was any diagnosable tumor before
			//its a CR.*/
			if (AllToSmall == False)
			{
				AddToEventQ(t,CREVENT,".",".",".");
				CompleteResponse.checked = False;
				PartialResponse.checked = False;
			}
			/* else if (PartialResponse.checked == True)
			{
				AddToEventQ(PartialResponse.time,RESPONSEEVENT,".",".",".");
				PartialResponse.checked = False;
			}*/
		}
		/* check for death */
		else if (TotalCellCount > death_threshold )
		{
			sprintf(chtemp,"%.4e\0",TotalCellCount);
			AddToEventQ(t,DEATHEVENT,chtemp,"Tumor",".");
			Dead = True;
			CompleteResponse.checked = False;
			PartialResponse.checked = False;
		}

		previousCellCount[0] = TotalCellCount;

	}

}