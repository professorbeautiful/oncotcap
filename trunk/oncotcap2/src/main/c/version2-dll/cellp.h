#include "build.h"
#include <time.h>

#define CircInc(A)  ((A == (MAXQLENGTH - 1)) ? 0 : (A + 1))
#define QLength(H, T)  (( H <= T ) ? (T - H) : (T + MAXQLENGTH - H))
    void AddToCellQ();
	void AddToEventQ();
	void InterpretEvents();
	unsigned long SleepTime;

	struct CNsaveType
	{
		double time;
		double *cellcount;
	} CNsave;

//	int iCellQHead, iCellQTail, iEventQHead, iEventQTail;
	unsigned long  IDThreadCellP;

struct Event_Type
{
   double time;
   int    event;
   char   detail1[255];
   char   detail2[255];
   char   detail3[255];
} Event_List[MAXQLENGTH];

struct Response
{
	double   cellcount;
	double   time;
	double   previousSize;
	int      event;
	int      checked;
} Response;

struct Response PartialResponse;
struct Response CompleteResponse;

double CombinedTumorSizeAtFirstTreatment;
int AfterFirstTreatment;
int PrimaryDiagnosed;
int PrimaryTumor;
int IsMeanOnly;
double TumorExaminationInterval;
double TumorCombinationFactor;
#ifndef PFITYPED
typedef void (* PFI)();
#define PFITYPED
#endif
PFI PlotData;
PFI PostEvent;



int LocationClass;
	double previousCellCount[MAXLEVELS+1];
	double previousCellCountAtEval[MAXLEVELS+1];
	double LR_diagnosis_threshold;
	double PE_diagnosis_threshold;
	double death_threshold;
	double response_threshold;
	double previousCombinedTumorSize;
	int DiagnosedNow;
	int DiagnosedOnce[MAXLEVELS+1];
	int CurrentlyDiagnosed[MAXLEVELS+1];
	int Dead;
	int Cured;
	int SimRunning;
	int EndSim;
void   compute_probability(),compute_para();
extern int create_para_struct(),getcelllevelbyclass();

extern double gnorm0( );
extern double gnorm( );
extern double gbinomi( );
extern void   eprint( );
extern double grand_b_n( );
extern double ranmarm(int),rand01(int);
extern void   ranrmarin();
#ifdef REALUNIX
#ifdef TEST
extern int time();
#endif
#endif

double dtemp;
int nloops;
int PrintGap;
int numTest;

char outName[30];

int loopcellp();
int d_is_zero();






#define dabs(A) (((A)>(0.0)) ? A : (-1*A))

void  cellp();
int nEvents;
#define MAXEVENTS 50

extern char buff[];

/* Changed by Roger on May 15, starting with ~huang/Xtreatsim/Xtr/cellp.c".
 */

#define DZERO (double)0
	
void compute_probability();
void Update_Gompertzian();
void Setup_Gompertzian(), Setup_Probability();
void check_events();
void reset_events();
int CheckForTreatment();
void reset_sim();
int AddQIndex();

#define GompRuleIndex(i,j) (GompPr+j)->level[i].gompruleindex
#define SumGompLevel(i,j) (GompPr+j)->level[i].sumgomplevel
#define birthrate0(i,j) ((GompPr+i)->para+j)->birthrate0
#define xi0(i,j) ((GompPr+i)->para+j)->xi0
#define gama0(i,j) ((GompPr+i)->para+j)->gama0
#define xi_gama1(i,j) ((GompPr+i)->para+j)->xi_gama1
#define xi_gama2(i,j) ((GompPr+i)->para+j)->xi_gama2

#define NGomp(i,j) GompSum[i].NGompPr[j].Ngomp
#define LogNGomp(i,j) GompSum[i].NGompPr[j].LogNgomp

#define SomeEventProb 0
#define GoneProb 1
#define MuProb  2

#define MAXGROUP 5
#define MAXDROP  0

struct gomppara *GompPr;

double StartT;
double EndT; 
int verbose;
double t,UpdateTime,error;
double delta_t;
int nrepetitions;

int MaxGroup,MaxDrop,OrganMetOpt,fillgap;
int (*OrganFunc)(int,unsigned int,double,int);
int running,Memory;
int LookUpId[MAXLOOKUPS];

FILE * cellp_eout;

double *CNcopy;

double firstTreatmentTime;
double CellCountAtFirstTreatment[MAXLEVELS+1];
int ResponseHappened[MAXLEVELS+1];
unsigned int ranSeed3;
int ranSeed1,ranSeed2;
    
#define UPDATEGAP 20
#ifndef RAND_MAX
#define RAND_MAX 2147483647
#endif

int nSavedDoses[Ndrug+1];
double BeginPlotTime,EndPlotTime;

int UserSetSeeds;
int HasGuaranteeTimeChecked;

int cellpitime;