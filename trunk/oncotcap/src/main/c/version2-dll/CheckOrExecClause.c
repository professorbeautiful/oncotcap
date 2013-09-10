#include "rule.h"
#include "bboard.h"

/***************************************************************************
* Name     :CheckOrExecClause.c
* Author   :Sai
* Date     :09/14/98
* Purpose  :The function that checks or executes clause clauseindex 
*           in rule ruleindex
*****************************************************************************/

int CheckOrExecClause(int ruleIndex,
					  int clauseIndex,
					  int clauseType)
{
	PFI fptr;
	void *Args;
	int retVal;

	switch(clauseType)
	{
	case BBIFCOND:
		fptr = BBRules[ruleIndex].IFClause[clauseIndex].fptr;
		Args = BBRules[ruleIndex].IFClause[clauseIndex].argList;
		break;
	case BBACTIONCOND:
		fptr = BBRules[ruleIndex].ACTIONClause[clauseIndex].fptr;
		Args = BBRules[ruleIndex].ACTIONClause[clauseIndex].argList;
		break;
	}
	retVal = (* fptr)(Args);
	return(retVal);
}