#include <stdio.h>
#include "rule.h"
#include "logger.h"

int EXPORT PASCAL IncrementIFClause(int ruleidx)
{
	fprintf(logfile,"%s\tfunction:IncrementIFClause.IncrementIFClause\truleidx:%d\n", gettime(), ruleidx);	
	return(BBRules[ruleidx].nIFClauses++);

}