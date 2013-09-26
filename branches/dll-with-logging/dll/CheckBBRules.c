#include "rule.h"
#include "bboard.h"

/***************************************************************************
* Name     :CheckBBRules.c
* Author   :Sai
* Date     :09/14/98
* Purpose  :The function that checks all the rules in the Rule structure
*****************************************************************************/

int CheckBBRules()
{
	int ruleIndex;
	int retVal;
	
	retVal = FALSE;
	for(ruleIndex=0;ruleIndex<nRules;ruleIndex++)
	{
		retVal = CheckRule(ruleIndex);
	}
	return(retVal);
}