#include "rule.h"
#include "bboard.h"

/***************************************************************************
* Name     :CheckRule.c
* Author   :Sai
* Date     :09/14/98
* Purpose  :The function that checks the Rule with index ruleIndex
*****************************************************************************/

int CheckRule(int ruleIndex)
{
	int clauseIndex;
	int retVal;
	
	retVal = FALSE;
	for(clauseIndex=0;clauseIndex<BBRules[ruleIndex].nIFClauses;clauseIndex++)
	{
		retVal = CheckOrExecClause(ruleIndex, clauseIndex,BBIFCOND);
	}
	if(retVal)
	{
		for(clauseIndex=0;clauseIndex<BBRules[ruleIndex].nACTIONClauses;clauseIndex++)
		{
			retVal=CheckOrExecClause(ruleIndex,clauseIndex,BBACTIONCOND);
		}
	}
	return(retVal);
}