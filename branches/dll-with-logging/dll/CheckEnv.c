#define BASEMODEL
#include "defines.h"
#include "classes.h"
#include "cellp.h"
#include "bboard.h"

void CheckEnv(double currtime, int *currenv)
{

	/*check to see if an enviroment change set in ApplyContinDrug
	  is ready to be activated */
	if (PendingEnvChange && TimeToChangeEnv >= currtime)
	{
		*currenv = EnvToChangeTo;
		nowenv = EnvToChangeTo;
		PendingEnvChange = FALSE;
		ContinDrugRuleActive = TRUE;
	}
	
}