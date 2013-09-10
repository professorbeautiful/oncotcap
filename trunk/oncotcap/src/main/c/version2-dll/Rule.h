#ifndef PASCAL
#define PASCAL __stdcall
#define EXPORT __declspec(dllexport)
#endif

#ifndef PFITYPED
typedef int (* PFI)(); 
#define PFITYPED
#endif

#ifndef MAXARGS
#define MAXARGS 10
#define MAXCLAUSES 10
#ifndef MAXRULES
#define MAXRULES 10
#endif
#endif

typedef struct ClauseTypeStruct
{
	PFI fptr;
	void *argList[MAXARGS];
} ClauseType;

typedef struct RuleTypeStruct
{
	int nIFClauses;
	int nACTIONClauses;
	ClauseType IFClause[MAXCLAUSES];
	ClauseType ACTIONClause[MAXCLAUSES];
}RuleType;

RuleType BBRules[MAXRULES];
int nRules;