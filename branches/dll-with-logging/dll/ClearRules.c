#include "rule.h"
#include "BBoard.h"

int EXPORT PASCAL ClearRules()
{
	int n;

	for (n=0;n<=nRules;n++)
	{
		BBRules[n].nIFClauses = 0;
		BBRules[n].nACTIONClauses = 0;
	}

	nRules = 0;

	//initialize globals used for ApplyContinDrug/CheckEnv functions
	ContinDrugRuleActive = FALSE;
	PendingEnvChange = FALSE;

	return(0);
}