#include "rule.h"

int EXPORT PASCAL IncrementActionClause(int ruleidx)
{

	return(BBRules[ruleidx].nACTIONClauses++);

}