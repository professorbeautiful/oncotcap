#include "rule.h"

int EXPORT PASCAL IncrementIFClause(int ruleidx)
{

	return(BBRules[ruleidx].nIFClauses++);

}