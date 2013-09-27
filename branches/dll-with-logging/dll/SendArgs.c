#include <string.h>
#include <malloc.h>
#include <stdio.h>
#include "Rule.h"
#include "BBoard.h"
#include "logger.h"

/***************************************************************************
* Name     :SendArgs.c
* Author   :Sai
* Date     :09/14/98
* Purpose  :This functions in this code are called by the VB function SendArgs. 
*           They set the argList array in a IF or ACTION clause of BBRules struct
*           They are type specific
*****************************************************************************/

int EXPORT PASCAL SendArgsI(int ruleIndex,
						   int clauseIndex, 
						   int argIndex,
						   int clauseType, 
						   int value
						   )
{
	int *tptr;

	fprintf(logfile,"%s\tfunction:SendArgs.SendArgsI\truleIndex:%d\tclauseIndex:%d\targIndex:%d\tclauseType:%d\tvalue:%d\n", gettime(),ruleIndex,clauseIndex,argIndex,clauseType,value);
	tptr =  malloc(sizeof(int));
	*tptr = value;

	switch(clauseType)
	{
	case BBIFCOND:
		BBRules[ruleIndex].IFClause[clauseIndex].argList[argIndex] = tptr;
		break;

	case BBACTIONCOND:
		BBRules[ruleIndex].ACTIONClause[clauseIndex].argList[argIndex] = tptr;
		break;
	}
	return(0);
}

int EXPORT PASCAL SendArgsB(int ruleIndex,
						   int clauseIndex, 
						   int argIndex,
						   int clauseType, 
						   int value
						   )
{
	int *tptr;
	fprintf(logfile,"%s\tfunction:SendArgs.SendArgsB\truleIndex:%d\tclauseIndex:%d\targIndex:%d\tclauseType:%d\tvalue:%d\n", gettime(),ruleIndex,clauseIndex,argIndex,clauseType,value);
	tptr =  malloc(sizeof(int));
	*tptr = value;

	switch(clauseType)
	{
	case BBIFCOND:
		BBRules[ruleIndex].IFClause[clauseIndex].argList[argIndex] = tptr;
		break;

	case BBACTIONCOND:
		BBRules[ruleIndex].ACTIONClause[clauseIndex].argList[argIndex] = tptr;
		break;
	}
	return(0);
}

int EXPORT PASCAL SendArgsD(int ruleIndex,
						   int clauseIndex, 
						   int argIndex,
						   int clauseType, 
						   double value
						   )
{
	double *tptr;

	tptr  =  malloc(sizeof(double));
	*tptr = value;

	fprintf(logfile,"%s\tfunction:SendArgs.SendArgsD\truleIndex:%d\tclauseIndex:%d\targIndex:%d\tclauseType:%d\tvalue:%12f\n", gettime(),ruleIndex,clauseIndex,argIndex,clauseType,value);	
	switch(clauseType)
	{
	case BBIFCOND:
		BBRules[ruleIndex].IFClause[clauseIndex].argList[argIndex] = tptr;
		break;

	case BBACTIONCOND:
		BBRules[ruleIndex].ACTIONClause[clauseIndex].argList[argIndex] = tptr;
		break;
	}
	return(0);
}


int EXPORT PASCAL SendArgsC(int ruleIndex,
						   int clauseIndex, 
						   int argIndex,
						   int clauseType, 
						   char *value
						   )
{
	char *tptr;
	fprintf(logfile,"%s\tfunction:SendArgs.SendArgsC\truleIndex:%d\tclauseIndex:%d\targIndex:%d\tclauseType:%d\tvalue:%s\n", gettime(),ruleIndex,clauseIndex,argIndex,clauseType,value);
	tptr  =  malloc((strlen(value) + 1) * sizeof(char));
	strcpy(tptr,value);

	switch(clauseType)
	{
	case BBIFCOND:
		BBRules[ruleIndex].IFClause[clauseIndex].argList[argIndex] = tptr;
		break;

	case BBACTIONCOND:
		BBRules[ruleIndex].ACTIONClause[clauseIndex].argList[argIndex] = tptr;
		break;
	}
	return(0);
}