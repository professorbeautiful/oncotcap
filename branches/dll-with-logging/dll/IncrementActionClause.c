#include<stdio.h>
#include "rule.h"
#include "logger.h"

int EXPORT PASCAL IncrementActionClause(int ruleidx)
{
	fprintf(logfile,"%s\tfunction:IncrementActionClause.IncrementActionClause\truleidx:%d\n", gettime(), ruleidx);	
	return(BBRules[ruleidx].nACTIONClauses++);

}