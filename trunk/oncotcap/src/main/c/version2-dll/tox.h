#include <string.h>

/* New additions for toxicity */

void CheckForTox();
void InitToxTypes();

typedef struct 
{
	char name[255];
	double toxProbs[6];
	int toxAppType;
	int toxResType;
	int toxTypeIndex; /* thepointer into the toxtypes array */
	double toxResTime;/* the time taken for the toxicity grade to fall by 1 grade */
} Tox;

struct Agent
{
	char name[255];
	int numToxTypes;
	Tox toxicity[MAXTOXTYPES];
} Agents[MAXDRUGS+1];

struct ToxType
{
	char name[255];
	double lastToxTime;
	int currGrade;
	int toxResType;
	double toxResTime;/* the time taken for the toxicity grade to fall by 1 grade */
} ToxTypes[MAXTOXTYPES+1];

#define MAXTOXRESOLUTIONS 255

struct ToxRes
{
	char name[255];
	double reduction;
	int DoseModIndex;
} ToxResolutions[MAXTOXRESOLUTIONS];

int nToxTypes;
int nToxResolutions;

#define MAXDOSEMODS 255

int nDoseMods;
struct DoseModification {
	double Reduction;
	int LastTreatment;
} DoseMods[MAXDOSEMODS];


#ifndef DRUGANDTIMEDEFINED
struct drugandtime { int d; double t; double df; int TreatmentIdx; } dnt;
#define DRUGANDTIMEDEFINED
#endif

struct drugandtime TreatmentExists();
double TreatmentExistsInt();
void InitNextToxTime();

double nextToxTime;
boolean RandInit;
