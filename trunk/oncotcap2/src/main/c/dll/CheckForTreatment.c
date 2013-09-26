#include "build.h"
#ifdef DLL
#include <windows.h>
#endif
#include "defines.h"
#include "classes.h"
#include "cellp.h"
#include "tox.h"


int CheckForTreatment()
{
	double trDose,CellCount;
	char tmpstr[255],tmpstr2[255],tmpstr3[255],chtemp[255];
	int i;
	int found;
	struct drugandtime localsched;

	boolean retval;

		trDose = (TreatmentExists(-1,t)).df ;
		if (trDose> 0.0)
		{
			retval = true;
			found = false;
			i = 1;
			while((i<= Ndrug) &&(found == false))
			{
				localsched = TreatmentExists(i,t);
				if (localsched.df > 0.0)
				{	
					CellCount = AllCells();
					sprintf(chtemp, "%e\0", CellCount);
					sprintf(tmpstr2,"%lf\0",localsched.df);
					strcpy(tmpstr3,Agents[i].name);
//					tmpstr3[5] = (char) '\0';
					sprintf(tmpstr,"%d\0",localsched.TreatmentIdx);
					AddToEventQ(t,TREATMENTEVENT,tmpstr3,tmpstr2,tmpstr);
					found = true;
					retval = CheckIfICRuleApplies(i,trDose);
				}
				i++;
			}
			if (TrialSim == True)
			{
				if (TSFirstTreatmentHappened == False)
					TSFirstTreatmentHappened = True;
				else
				{
					if(TSSecondTreatmentHappened == False)
					{
						TSSecondTreatmentHappened = True;
						InitialPrimaryTumorSize = previousCellCount[PrimaryTumor];
					}
					TreatmentCount++;
					if ((TrialInfo.MaxCourses > 0) &&
						(((TreatmentCount) % TrialInfo.Regimen.numTreatments) == 0) &&
						(((TreatmentCount) / TrialInfo.Regimen.numTreatments) == TrialInfo.MaxCourses) &&
						(OffTrial == False)
					   )
					{
						AddToEventQ(t,OFFTRIALEVENT,"Maximum Courses Received",".",".");
						OffTrial = True;
					}
				}
			}
		}
		else
			retval = false;

	return(retval);
}