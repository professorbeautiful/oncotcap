#include <stdio.h>
#include "Rule.h"
#include "BBoard.h"
#include "logger.h"

/***************************************************************************
* Name     :SendFunction.c
* Author   :Sai
* Date     :09/14/98
* Purpose  :This function is called by the VB function SendRules. 
*           Sends the funcConst which then sets the corresponding func ptr
*           in the BBRules structure
*****************************************************************************/


int EXPORT PASCAL SendFunction(int ruleIndex,
							   int clauseIndex,
							   int clauseType,
							   int funcConst)
{
	PFI fptr;
	fprintf(logfile,"%s\tfunction:SendFunction.SendFunction\truleIndex%d\tclauseIndex:%d\tclauseType:%d\tfuncConst:%d\n", gettime(),ruleIndex,clauseIndex,clauseType,funcConst);
	fptr = GetFunctionPtr(funcConst);

	switch(clauseType)
	{
	case BBIFCOND:
		BBRules[ruleIndex].IFClause[clauseIndex].fptr = fptr;
		break;
	case BBACTIONCOND:
		BBRules[ruleIndex].ACTIONClause[clauseIndex].fptr = fptr;
		break;
	}
	return(0);
}

