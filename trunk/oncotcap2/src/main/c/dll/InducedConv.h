/*  Rule type declaration */


struct ClassAndLevel {
	int classIndex;
	int levelIndex;
};



struct icRuleType {
/* if part */
	/* for classes and levels */
	int nClassAndLevels;
	struct ClassAndLevel ifClass[MAXCLASSES];
	/* for continuous agents */
	int envIndex;
	char envName[255];
	/* for cytotoxic agents */
	int agentIndex;
/* then part */
	struct ClassAndLevel Source;
	struct ClassAndLevel Dest;
	double Prob;
} icRules[MAXCLASSES];

int nICrules;


char Environments[NENVIRS][255];