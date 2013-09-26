#define BASEMODEL
#include "defines.h"
#include "classes.h"
#include "cellp.h"
#include "bboard.h"

void RemoveContinDrug()
{
	nowenv = 0;
	PendingEnvChange = FALSE;
	ContinDrugRuleActive = FALSE;
}