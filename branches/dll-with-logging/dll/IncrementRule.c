#include <stdio.h>
#include "rule.h"
#include "logger.h"

int EXPORT PASCAL IncrementRule()
{
	fprintf(logfile,"%s\tfunction:IncrementRule.IncrementRule\n", gettime());	
	return(nRules++);
}