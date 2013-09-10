struct TreatmentApplication{
	double Time;
	int TreatIndex;
	int TreatType;
	int CourseIndex;
	int CourseNum;
	double Dose;
	double FullDose;
} TreatmentApplication;

struct Regimen {
	char Name[255];
	char TextDef[255];
	double Interval[MAXDRUGS];
	int numRepetitions[MAXDRUGS];
	int numTreats;
	struct TreatmentApplication Treatments[MAXTRTEGREATMENTS];
	int agentIndex[MAXDRUGS];
	int treatIndex[MAXDRUGS];
	double treatDose[MAXDRUGS];
	double startTime[MAXDRUGS];
	int numTreatmentApplications;
} Regimens[MAXREGIMENS];

struct ComboAndCourseAgent {
	char Name[255];
	int Index;
	int numApps;
	double Dose;
	double AppTimes[MAXTREATMENTS];
} ComboAndCourseAgent;

struct Combo {
    char Name[255];
    int numAgents;
    struct ComboAndCourseAgent cmbAgents[MAXDRUGS];
} Combos[MAXDRUGS];

struct Course {
	char Name[255];
	int numAgents;
	struct ComboAndCourseAgent courseAgents[MAXDRUGS];
    int AgentIndex[MAXDRUGS];
} Courses[MAXDRUGS];

