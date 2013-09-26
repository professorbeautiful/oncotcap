/* ApplyContinDrug - Sets the enviroment based on a given continuous drug
 *
 *  Input: args, pointer to an argument list that contains
 *         args[1]: a pointer to a BlackBoard key that contains the first argument, drug name
 *         args[2]: a pointer to a BlackBoard key that contains the second argument, time to apply drug
 */ 
#include "build.h"
#include <stdio.h>
#include <memory.h>
#include <string.h>
#define BASEMODEL
#include "defines.h"
#include "classes.h"
#include "cellp.h"
#include "bboard.h"

//removed second argument temporarily, 8/3/99 wes

void ApplyContinDrug(void *args[1])
{
	ApplyContinDrugA((char *) args[0] /*, (char *) args[1] */);
}

/* ApplyContinDrug - Sets the enviromnt based on a given continuos drug
 *
 * Inputs: drug: a pointer to the drug name that is to be applied
 *         atime: pointer to a BB variable that holds the application time of the drug
 *
 */
void ApplyContinDrugA(char *drug /*, char *atime */)
{
//	BBEType *bbe1;
//	char strWarn[255];
	int i, found;
//	double ttemp;

//	bbe1 = Lookup(atime); //get a pointer to the first argument
//	if (bbe1 == NULL)     //if it doesn't exist leave with a warning
//	{
//		strcpy(strWarn, "BlackBoard variable ");
//		strcat(strWarn, atime);
//		strcat(strWarn, " not found during ApplyContinDrug function. [ApplyContinDrugA]");
//#ifdef DLL
//		MsgBoxWarning(strWarn);
//#endif
//		return;
//	}

//	if  ( bbe1->type == BBDOUBLE )       /*if the time argument is a Double*/
//	{
//		memcpy(&ttemp,bbe1->value,sizeof(double));

		/* if the time has already passed to apply this drug, igonore it */
//		if (ttemp < t + delta_t/2)
//		   return;

		/* set the enviroment based on the drug name
		   the function CheckEnv called from cellp will switch to
		   enviroment "EnvToChangeTo" when t = TimeToChangeEnv */
		found = FALSE;
		for ( i = TotalSingleRules - Kinetic_Changes; i < TotalSingleRules && !found; i++ )
			if (strcmp(Rules[i].action.dest_name, drug) == 0)
			{
				found = TRUE;
				EnvToChangeTo = Rules[i].EnvirCond[0].ienv;
//				TimeToChangeEnv = ttemp;
//				if (TimeToChangeEnv > t - delta_t/2 && TimeToChangeEnv < t + delta_t/2)
//				{
					//do it now
					nowenv = EnvToChangeTo;
					PendingEnvChange = FALSE;
					ContinDrugRuleActive = TRUE;
//				}
//				else
//				{
//					//do it later in CheckEnv
//					PendingEnvChange = TRUE;
//				}
			}
//	}
//	else
//	{
//		strcpy(strWarn, "Argument ");
//		strcat(strWarn, atime);
//		strcat(strWarn, " is not of type Double. [ApplyContinDrugA]");
//#ifdef DLL
//		MsgBoxWarning(strWarn);
//#endif
//	}
	return;
}