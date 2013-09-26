#include <stdio.h>
#define not !
#define or ||
#define and &&
#define integer int
#define real double
#define boolean int
#define true 1
#define false 0
#ifndef TRUE
#define TRUE true
#define FALSE false
#endif
#define ln log
#define trunc  (int)floor

/*max,min are in <stdlib>*/
#ifndef max
#define max(a,b)    (((a) > (b)) ? (a) : (b))
#define min(a,b)    (((a) < (b)) ? (a) : (b))
#endif   
   
/*#define TCAPVERSION "Version = 1.2 SixHet\n"
//#define strTCAPVERSION "Version = 1.2 SixHet"

//#define TCAPVERSION "Version = 1.1 \n"
//#define strTCAPVERSION "Version = 1.1"*/
#define strDLLVERSION "1.3"


#define PASCAL __stdcall
#define EXPORT __declspec(dllexport)

#include "procdecs.h"


#define repeat do
#define until(A) while (not (A))
#define peek  (ungetc(getc(stdin),stdin))
#define fpeek(A)  (ungetc(getc(A),A))
#define qendofline ((peek=='\n') ? true:false)
#define eof ((peek==EOF) ? true:false)
/**** IN <stdio.h> #define feof(A) ((peek(A)==EOF) ? true:false)****/
#define fqendofline(A) ((fpeek(A)=='\n') ? true:false)
#define dumpline while ((getc(stdin)) != '\n')
#define fdumpline(A) while ((getc(A)) != '\n')
#define reset(A,B)	A=fopen(B,"r")

/***FAILS #define Iread(A)	scanf("%d",templine);A=atoi(templine)**/
#define Iread(A)	scanf("%d",&A)
#define Dread(A)	scanf("%lf",&A)
#define fIread(F,A)	fscanf(F,"%d",&A)
#define fDread(F,A)	fscanf(F,"%lf",&A)

#define chr(A)	((char)(A))
#define ord(A)	((int)(A))
#define SPACE ' '
#define mod %
#define COMMA ','
	/***@ constants.i ***/
#include "Const.h"
/*** <-- conditioning flags ****/


	/**@ commons.i **/
/**** variable type definitions**/
typedef int drug;
#define Ndrug (MAXDRUGS+4)
typedef int celltype ;
#define Ncelltype (1+MAXTYPES)
typedef int dosenumber;
#define Ndosenumber  (1+MAXTIMES)
typedef int timenumber;
#define Ntimenumber  (1+MAXTIMES)

#ifndef DRUGANDTIMEDEFINED
struct drugandtime { int d; double t; double df; int TreatmentIdx; } dnt;
#define DRUGANDTIMEDEFINED
#endif

/* 
   #define ARRAYFULL
   #ifdef ARRAYFULL 
*/
#define String(A) char A[1+MAX_CHAR+1]
#define Fname(A) char A[1+MAX_CHAR+1]
#define cellarray(A) real A[1+MAXTYPES+1] 
#define timearray(A) real A[Ntimenumber+1] 
/*
#define kinarray1(A) real (A)[Ncelltype+1][ NENVIRS]
#define kinarray2(A) real (A)[Ncelltype+1][Ncelltype+1][ NENVIRS]
#define dkarray(A) real A[Ndrug+1][Ncelltype+1]
#define tkarray(A) real A[Ntimenumber+1][Ncelltype+1]
*/
   /** LP types -- dont delete **/ 
#define MBarray(A) real A [1+MAXSPLUS2+1][ 1+BINS+1] 
#define Marray(A) real A [1+MAXSPLUS2+1] 
#define Barray(A) real A [1+BINS+1] 
#define Rarray(A) real A [1+RWORK+1] 
#define Iarray(A) integer A [1+IWORK+1] 
/* #endif */
/*VAR*/
/*//  The following allocates space in main() for globals.
//    -BUT NOT IN WINDOWS!!!  this is a Unix trick.
//#ifdef MAIN
//#define extern 
//#endif*/

real INFINITESIMAL;
real INFINITY;
char ch;
char templine[50+1];
FILE *fsched;
Fname(stdinname);
FILE *infil;
Fname(infilname);
FILE *ofil;
Fname(ofilname);
Fname(ttynam);
String(command);
FILE *celpltfile; /** output file  for timecourse data **/
FILE *eout;
#ifdef TESTMPI
#define eout stdout
#endif
integer MaxNameLength;
integer verbos;
integer ndrugs,  nallts, nt;
unsigned int ntypes;
integer nsched;
timearray(timevec);
integer nenvirons, nowenv;
integer nenvlist;
struct { int e; real t; } envlist [Ntimenumber+1];

#define MAXPARA 11
#define CondPrDeath 0
#define DT 1
#define CT 2
#define DR 3
#define BR 4
#define DE 5
#define GAMA  6
#define GR 7
#define MS 8
#define XI 9
#define MU 10


#define CS 0
#define DS 1

#define MUTATION 0
#define G0T 1 
#define G0R 2
#define BLEBT 3
#define BLEBR 4
#define MUTO 5
#define COTO 6
#define MIG  7

struct lookup {
 double nDescOfItype;
 unsigned int LookUpId; /* lookupid ==> itype */
 int mark;
 struct para_struct *ParaPr;
 String(cellname);
 } lookup;

double *CN;

struct lookup *LookUp;

struct mutation_para {
 unsigned int LookUpId;
 double rate, CondProb;
 } mutation_para;

struct transition_para {
 unsigned int LookUpId;
 double rate,time,CondProb;
 } transition_para;

struct migration_para {
	unsigned int LookUpId;
	double CondProb;
} migration_para;

struct kina_para {
	double para[MAXPARA];
	int paraswitch[2];
	double migtime,migrate;
	boolean  vnodeath;
	struct migration_para *MigPr;
	struct mutation_para *MuPr;
	struct transition_para *CoPr,*BlPr;
	double EventProb[3],drugkill[Ndrug+1],timekill[Ntimenumber+1],timesurv[Ntimenumber+1];
} kina_para;

struct para_struct {
	short int NumOfMig,NumOfMu,NumOfCo,NumOfBl;
	struct kina_para *KinaPr;
} para_struct;

#define timekill(i,j,k) ((LookUp+j)->ParaPr->KinaPr+k)->timekill[i]
#define timesurv(i,j,k) ((LookUp+j)->ParaPr->KinaPr+k)->timesurv[i]
#define drugkill(i,j,k) ((LookUp+j)->ParaPr->KinaPr+k)->drugkill[i]
#define doubletime(i,j) ((LookUp+i)->ParaPr->KinaPr+j)->para[DT]
#define cycletime(i,j) ((LookUp+i)->ParaPr->KinaPr+j)->para[CT]
#define cycleswitch(i,j) ((LookUp+i)->ParaPr->KinaPr+j)->paraswitch[CS]
#define birthrate(i,j) ((LookUp+i)->ParaPr->KinaPr+j)->para[BR]
#define deathtime(i,j) ((LookUp+i)->ParaPr->KinaPr+j)->para[DE]
#define deathswitch(i,j) ((LookUp+i)->ParaPr->KinaPr+j)->paraswitch[DS]
#define deathrate(i,j) ((LookUp+i)->ParaPr->KinaPr+j)->para[DR]
#define growrate(i,j) ((LookUp+i)->ParaPr->KinaPr+j)->para[GR]
#define mutsum(i,j) ((LookUp+i)->ParaPr->KinaPr+j)->para[MS]
#define xi(i,j) ((LookUp+i)->ParaPr->KinaPr+j)->para[XI]
#define gama(i,j) ((LookUp+i)->ParaPr->KinaPr+j)->para[GAMA]
#define mu(i,j) ((LookUp+i)->ParaPr->KinaPr+j)->para[MU]
#define CondProbDeath(i,j) ((LookUp+i)->ParaPr->KinaPr+j)->para[CondPrDeath]
#define EventProb(i,j,k) ((LookUp+j)->ParaPr->KinaPr+k)->EventProb[i]
#define vnodeath(i,j) ((LookUp+i)->ParaPr->KinaPr+j)->vnodeath
#define nDescOfItype(i) (LookUp+i)->nDescOfItype
#define cellname(i) (LookUp+i)->cellname

#define mutrate(i,j,k) (((LookUp+i)->ParaPr->KinaPr+k)->MuPr+j)->rate
#define convrate(i,j,k) (((LookUp+i)->ParaPr->KinaPr+k)->CoPr+j)->rate
#define convtime(i,j,k) (((LookUp+i)->ParaPr->KinaPr+k)->CoPr+j)->time
#define migrate(i,k) ((LookUp+i)->ParaPr->KinaPr+k)->migrate
#define migtime(i,k) ((LookUp+i)->ParaPr->KinaPr+k)->migtime
#define blebrate(i,j,k) (((LookUp+i)->ParaPr->KinaPr+k)->BlPr+j)->rate
#define blebtime(i,j,k) (((LookUp+i)->ParaPr->KinaPr+k)->BlPr+j)->time
#define CondMuTo(i,j,k) (((LookUp+i)->ParaPr->KinaPr+k)->MuPr+j)->CondProb
#define CondCoTo(i,j,k) (((LookUp+i)->ParaPr->KinaPr+k)->CoPr+j)->CondProb
#define CondMigTo(i,j,k) (((LookUp+i)->ParaPr->KinaPr+k)->MigPr+j)->CondProb
#define MuLookUp(i,j,k) ((LookUp[i].ParaPr->KinaPr+k)->MuPr+j)->LookUpId
#define CoLookUp(i,j,k) ((LookUp[i].ParaPr->KinaPr+k)->CoPr+j)->LookUpId
#define BlLookUp(i,j,k) ((LookUp[i].ParaPr->KinaPr+k)->BlPr+j)->LookUpId
#define MigLookUp(i,j,k) ((LookUp[i].ParaPr->KinaPr+k)->MigPr+j)->LookUpId

int change_index,pre_ntypes,active_ntypes,init_active_ntypes,MaxLookUp;
struct drugandtime sched[Ndosenumber+1] ;

#ifdef MAIN
struct drugandtime blanksched = {0, 0.0, 0.0, 0};
#else
struct drugandtime blanksched;
#endif

real doselist[Ndrug+1][Ndosenumber+1];
integer ndoses[Ndrug+1];

#ifndef MPI
cellarray(wvec);
real wvalue;
  /** wvec= 1 - svec **/
		/** argument to jointpgf;= expectation  of (1-w)^R **/
   /* array to hold values from jointpgf call */
   /* added 31 December 1992 */
cellarray(jpgfvec);
#endif
cellarray(svec);
cellarray(smask);       /** margin mask for svec **/
cellarray(cellstart);

real deltakill, topkill, botkill; 
 		 /** govern log-kill scale in FIXEDDOSES **/
integer nprobs;
real probtest[1+MAXPROBS+1];
 		/** govern prob scale in FIXEDPROBS **/

String(qaxis);  /** tells DOSERESPONSE which way to graph it **/
boolean qsamedoub; /** have all celltypes the same doubletime? **/
boolean qsamecyclet; /** have all celltypes the same cycletime? **/
boolean qnodeath; /** do doubling timevec= cycleing timevec? **/

#define LTorGE int

struct COND {
       	real t;
       	cellarray(qtypes);
       	real size;
       	LTorGE dir;
	} condition[1+MAXCONDS+1];

integer nconds;

double theta,tau;

String(drugname[Ndrug+1] );
/*String(typename[Ncelltype+1] );*/
char typename[Ncelltype+1][SMALLBUFFER];
real aerr, rerr;  /**used in the pgfint hybrid**/

/** LP commons -- but don't delete (par files use them)**/
		/**  (a,ia,b,c,nvars,m1,m2,maxval,psol,dsol,rw,is,ier) **/
MBarray(a);
integer ia;
Marray(b);
Barray(c);
integer nvars;
integer m1;
integer m2;
real maxval;
Barray(psol);
Marray(dsol);
Rarray(rw);
Iarray(is);
integer ier;
 
/** variables used by cumul, getacum, coeffs, cumweights **/
integer nbins, ns, nv;
Marray(slist);
Barray(val);
Barray(n);
Barray(bound);
Barray(deltan);
real err;


/**end of variable declarations**/
/** beginning of PROCEDURE declarations **/


int  ncellindices, *cellindices;
/*//double dosefactor[Ndrug+1];*/



/*  Used for Gompertz modifications:*/
int KineticsModel; 
int KineticsUnchecked; 
int NumGompRules; 
 
#define IsExponential 0 
#define IsGompertz 1 
 
#define MAXNGOMP 30 
 
/* 
* GompRule[] keeps class, level, plateau, and split for each Gompertzs Rule;
  It is sorted by Classes ( created in the function evaluate_rule() );
* The size of GompRule[] is specified by a variable NumGompRules; 
* The index of the GompRule[] ( from 1 to NumGompRules ) distinguishes one
  level from other levels ( GompRuleIndex );
* GompRule[0] will keep the Base_GompPlateau and Base_Split for whole body, which
  is assined the value in the function Setup_Gompertzian();
* If the Class and the Level are known for a cell type, the GompRuleIndex for that class
  can be got by the funation GetGompRuleIndex();
*/

union CCLASS {
	   int cclass; /* for prop gomp rule */
	   char cname[SMALLBUFFER];
} CCLASS;


union CLEVEL{
	   int clevel; /* for prop gomp rule */
	   char cname[SMALLBUFFER];
} CLEVEL;

union GPORINV { 
	   double GP;
	   double inv_log_gp;
} GPORINV;
	      /* store a inverse of log(GP)*/

struct GompRule 
{   
   double GS;
   union GPORINV gp_or_inv;
   int cn,level,nclevel;
   union CCLASS cc[2];
   union CLEVEL cl[2];
   double multiplier,gpmin,gpmax;
}  GompRule[MAXNGOMP][NENVIRS]; 

struct gompsum
{
	double Ngomp,LogNgomp;
} gompsum;

struct GompSumStruct
{
	int nsumlevel;
	struct gompsum *NGompPr;
} *GompSum;

struct temppara {
	double birthrate0,xi0,gama0,xi_gama1,xi_gama2;
} temppara;

struct gomp_level {
	int gompruleindex,sumgomplevel;
} gomp_level;

struct gomppara{
	struct temppara *para;
	struct gomp_level *level;
    /* maps each celltype to the location level index.*/
} gomppara;

#define NSumLevel(i) GompSum[i].nsumlevel

double (*RandFunc)(int);

char ArchiveDir[255];
char WorkingDir[255];

char TCAPVERSION[255];
char strTCAPVERSION[255];

double *initCellVector;
double InitMean,InitTotal,CoVar;
double LogMean, LogStd;

/* Stuff for Phase II trial simulation */
#define MAXTREATMENTS 28   /* max number of treatments in this course */
#define MAXTOXMODS 10      /* max number of Dose modification rules */

int TrialSim;			   /* Boolean set to true when we run a Trial Simulation */


struct TreatmentandTime {  /* Structure copied from VB side */
	char Name[255];
	int Type;
	int TreatIndex;
	double time;
	double dose;
	int lineidx;
} TreatmentandTime;

struct CTReg {				/* Structure copied from VB side (Regimen) */
	char Name[255];
	char ShortName[255];
	struct TreatmentandTime Treatments[MAXTREATMENTS];
	int numTreatments;
} CTReg;

struct CTToxicityCriteria { /* Structure to hold dose modification rules */
	double Reduction;		/* amount of reduction ( 0 - 1.0) */
	int ReductionGrade;	/* at what grade (>=) apply reduction  */
	int ReductionType;		/* Type of reduction - NEXT, RESTOFCOURSE, ALLREMAINING */
	char ToxType[255];      /* Toxicity type */
} CTToxicity;

struct CTPhase2Trial {			/* Structure to hold all Trial Simulation parameters */
	double CourseLength;		/* How long til we repeat a course (in DAYS) */
	struct CTReg Regimen;		/* Regimen object of one course */
	int numToxModifications;	/* Number of Dose Modification Rules */
	struct CTToxicityCriteria ToxCriteria[MAXTOXMODS];  /*Structures to dose mod info (index starting at 1) */
	int MaxCourses;				/* Number of courses given til patient is taken off study */
	double MaxProgression;			/* % of increase in original tumor before patient is taken off study */
	int ProgressionOnNewMets;	/* boolean, patient taken off study or not for occurence of new mets */
	int OffStudyGrade;			/* Grade (>=) at which patient is taken off study */
	int n1;						/* number of patients run through stage 1 */
	int r1;						/* stage one criteria for pass/fail */
	int n;						/* number of patients run through stage 2 (includes stage 1 patients) */
	int r;						/* stage two criteria for pass/fail */
} CTPhase2Trial;

struct CTPhase2Trial TrialInfo;

int TSFirstTreatmentHappened;
int TSSecondTreatmentHappened;

struct CTTreatments {
	double Time;
	int AgentIndex;
	int CourseNum;
	double Dose;
	double FullDose;
} CTCourses;

struct CTTreatments CTTreatment[512];
int numCTTreatments;
int TreatmentCount;
double InitialPrimaryTumorSize;
int OffTrial;

/*  These are the VB equivalents of the above 
Type TreatmentandTime
    Name As String
    Type As Integer '1 - Single agent, also radiation and surgury (for now)
                    '2 - Combination of agents
                    '3 - Continuous agents
    typeindex As Integer
    time As Double
    dose As Double
    endTime As Double
    lineidx As Integer
End Type

Public Type Regimen
    Name As String
    ShortName As String
    Treatments(maxTreatments) As TreatmentandTime
    numTreatments As Integer
End Type */

/* new arrays to store the dose fractions and VB-side treatment index
analagous to doselist that stores dosetimes */

double dosefactorlist[Ndrug+1][Ndosenumber+1];
int TreatmentIdxList[Ndrug+1][Ndosenumber+1];

int GuaranteeReset;