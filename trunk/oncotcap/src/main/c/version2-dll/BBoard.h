#ifndef PFITYPED
typedef int (* PFI)(); 
#define PFITYPED
#endif

typedef  struct BlackBoardEntry {
	char *key;
	void *value;
	unsigned char type;
	void *next;
	void *prev;
} BBEType;

typedef struct  BlackBoard 
{
	BBEType *head;
	BBEType *tail;
} BBType;


BBType gblBB;

#define BBNULL "~NULL"
#define TEMPNAMEPREFIX "~T"

/* definitions of BlackBoard variable types */
#define BBINT 2
#define BBBOOL 1
#define BBDOUBLE 3

/* definitions of constants used in GetFunctionPtr
   these must match the same named constants in moduleMgmtRule.bas */
#define FRSETI          1
#define FRSETB          2
#define FRSETD          3
#define FRADDTOREG      4
#define FRADD           5
#define FRAND           6
#define FREQ            7
#define FRLT            8
#define FRGT            9
#define FRLTE          10
#define FRGTE          11
#define FRNOW          12
#define FRRANDLOGNORM  13
#define FRNE           14
#define FRAPPLYREG     15
#define FRCLEARREG     16
#define FRSET          17
#define FRAPPLYCONTINDRUG    18
#define FRREMOVECONTINDRUG   19

int CheckOrExecClause();
int CheckRule();
int CheckBBRules();


/* Function definitions of BlackBoard functions */
BBEType *AddNewBBe( char * key);
void ClearBBE(char *key);
void ClearBlackBoard();
void ClearTempBB();
BBEType *Lookup(char *key);
void SetI(void *args[2]);
void SetIA(char * key, int val);
void SetB(void *args[2]);
void SetBA(char * key, int val);
void SetD(void *args[2]);
void SetDA(char * key, double val);
void Set(void *args[2]);
void SetA(char * key, char * sourcekey);
void Now(void *args);
void NowA(char * key);
void RandLogNorm(void *args);
double RandLogNormA(char *resultkey, char *meankey, char *cvkey);
void Add(void *args);
void AddA(char *key, char *val1, char *val2);
int And(void *args);
int AndA(char *key, char *val1, char *val2);
int Eq(void *args);
int Comp(char *key, char *val1, char *val2, char *CompType);
int Lt(void *args);
int Gt(void *args);
int Lte(void *args);
int Gte(void *args);
int Ne(void *args);
int AddtoRegimen(void *args);
int AddtoRegimenA(int ntreat,int treatIndex,int agentIndex,int numTimes,double interval,double startTime, double dose);
int ApplyRegimenIntoScheduleA();
int ApplyRegimenIntoSchedule();
void ClearBBRegimen();
void ApplyContinDrug(void *args);
void ApplyContinDrugA(char *drug/*, char *apptime */);
void RemoveContinDrug();

PFI GetFunctionPtr(int fConst);

void ResetBBEveryDeltaT();
void SetBBForPatient();
void SetBBFromEvents(double etime,int event,char det1[255],char det2[255], char det3[255]);


/* #ifdef INMAIN
int TRUEVAL = 1;
int FALSEVAL = 0;
#else */
int TRUEVAL;
int FALSEVAL;
/* #endif */

#ifndef MSGBOXWARNING
int MsgBoxWarning(char mesg[]);
#define MSGBOXWARNING
#endif

#define BBIFCOND 1
#define BBACTIONCOND 2

#ifndef TRUE
#define TRUE 1
#define FALSE 0
#endif

struct UserAddedVar {
	char varName[255];
	int varType;
	double Value;
} UserAddedVars[50];

int nUserAddedVars;\

//globals used for ApplyContinDrug/CheckEnv
int ContinDrugRuleActive;
int EnvToChangeTo;
double TimeToChangeEnv;
int PendingEnvChange;