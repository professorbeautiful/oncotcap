/*******{@ procdecs.h }******/
 
/*******SWITCHING declarations: :g/FUNC/s/\(.*\);/\2 \1 \(.*\);******/
#include <string.h>
#include <math.h>
#include <ctype.h>

extern char *index();
extern char *ttyname();
extern FILE *fopen();
extern FILE *freopen();
extern real fabs();
extern real sqrt();
extern real log();
extern real exp();

extern  real /*FUNCTION*/ power();
 
extern  boolean /*FUNCTION*/ tiny();
 
extern  real /*FUNCTION*/ logten();
 
extern real /*FUNCTION*/ expo();

extern real /*FUNCTION*/ sgn();
 
extern void /*PROCEDURE*/ boolprompt();
 
extern void /*PROCEDURE*/ buildkill(int iindex, int env);
 
extern void /*PROCEDURE*/ buildsched();

extern void CheckEnv(double currtime, int *currenviroment);

extern  real /*FUNCTION*/ cellcount();
 
extern void /*PROCEDURE*/ doseresponse();
 
extern void /*PROCEDURE*/ evaluate();
 
extern void /*PROCEDURE*/ fixeddoses();
 
extern void /*PROCEDURE*/ fixedprobs();
 
extern integer /*FUNCTION*/ getenvir();

extern void /*PROCEDURE*/ getstring();
 
extern void /*PROCEDURE*/ help();
 
extern void /*PROCEDURE*/ inblebtime();

extern void /*PROCEDURE*/ incycletime();
 
extern void /*PROCEDURE*/ indoubletime();
 
extern void /*PROCEDURE*/ indrugs();
 
extern void /*PROCEDURE*/ inenv();
 
extern void /*PROCEDURE*/ infile();
 
extern void /*PROCEDURE*/ ing0time();
 
extern void /*PROCEDURE*/ ingrowth();
 
extern void /*PROCEDURE*/ inkill();
 
extern void /*PROCEDURE*/ inmask();
 
extern void /*PROCEDURE*/ inmutrate();
 
extern void /*PROCEDURE*/ inschedule();
 
extern void /*PROCEDURE*/ intprompt();
 
extern void /*PROCEDURE*/ intypes();
 
extern void /*PROCEDURE*/ inverbosity();
 
extern  real /*FUNCTION*/ jointpgf();
 
extern  real /*FUNCTION*/ jpgfcond();
 
extern  real /*FUNCTION*/ jpgfstep();
 
extern  real /*FUNCTION*/ jpgfen();
 
extern void /*PROCEDURE*/ jpgfit();

extern  real /*FUNCTION*/ nlogs();
 
extern  real /*FUNCTION*/ ncells();
 
extern void /*PROCEDURE*/ outfile();

extern void PlotHandler();

/****** DEC only -->  
extern void  pasrat(
var     ndrugs:integer;
var     ntypes:integer;
var     nallts:integer;
var     nt: integer;
var     timevec: timearray;
var     nenvirons: integer;
var     nowenv: integer;
var     doubletime: kinarray1;
var     cycletime: kinarray1;
var     birthrate: kinarray1;
var     deathrate: kinarray1;
var     growrate: kinarray1;
var     xi: kinarray1;
var     gamma: kinarray1;
var     mu: kinarray1;
var     mutrate: kinarray2;
var     blebtime: kinarray2;
var     blebrate: kinarray2;
var     g0time: kinarray2;
var     g0rate: kinarray2;
var     timekill: tkarray;
var     timesurv: tkarray;
var     cellstart: cellarray;
var     wvec: cellarray;
var     svec: cellarray;
var     smask: cellarray
);
extern  fortran();

extern void prsvec();
extern  fortran();
  <-- DEC only **********/

extern  real /*FUNCTION*/ pgf1();
 
extern  real /*FUNCTION*/ pgfxcm();
 
extern  real /*FUNCTION*/ pgfint();

extern  real /*FUNCTION*/ probeprob();
 
extern void /*PROCEDURE*/ probzeros();
 
extern void /*PROCEDURE*/ realprompt();
 
extern void /*PROCEDURE*/ schedprompt();
 
extern void /*PROCEDURE*/ setrates();
 
extern void /*PROCEDURE*/ sortsched();
 
extern void /*PROCEDURE*/ stringprompt();
 
extern void /*PROCEDURE*/ timecourse();
 
extern void /*PROCEDURE*/ typeconds();
 
extern void /*PROCEDURE*/ typemask();
 
extern void /*PROCEDURE*/ typestring();
 
extern void /*PROCEDURE*/ typevec();

extern void /*PROCEDURE*/ initialize();
 
 
/******** LP declarations -->  
 
extern void getacum();
 
extern void cumweights();
 
extern void setb();
 
extern void cumul();
 
extern void coeffs();
 
      extern void zx3lp(
var     a: MBarray;
var     ia: integer;
var     b: Marray;
var     c: Barray;
var     n: integer;
var     m1: integer;
var     m2: integer;
var     s: real;
var     psol: Barray;
var     dsol: Marray;
var     rw: Rarray;
var     iw: Iarray;
var     ier: integer
); 
 
  <-- LP declarations *******/
 

extern  boolean /*FUNCTION*/ qendofline();
 
extern  char /*FUNCTION*/ peek();

extern void /*PROCEDURE*/ getfname();

extern real /**FUNCTION**/ kern ();

extern real /**FUNCTION**/   eq10();

void incondition (/* FILE *p */);

#ifdef TESTMPI
double  ApplyRules();
int ApplySchedule();
real  Do_jointpgf();
#else
extern int EXPORT PASCAL DoPgf( );
extern double EXPORT PASCAL ApplyRules(char *phenofile);
extern int EXPORT PASCAL ApplySchedule(char *schedparam, int i);
extern  real EXPORT PASCAL Do_jointpgf();

extern  real EXPORT PASCAL peekd(char *strung, int ind1, int ind2, int ind3);

extern int EXPORT PASCAL ResetEnvir();
extern int EXPORT PASCAL  SetEnvir( char *name, double begin, double end);
extern int EXPORT PASCAL IncrementIFClause( int ruleidx );
extern int EXPORT PASCAL IncrementActionClause ( int ruleidx );
extern int EXPORT PASCAL IncrementRule();
extern int EXPORT PASCAL ClearRules();
extern int EXPORT PASCAL SendArgsI(int ruleIndex, int clauseIndex, int argIndex, int clauseType, int value);
extern int EXPORT PASCAL SendArgsB(int ruleIndex, int clauseIndex, int argIndex, int clauseType, int value);
extern int EXPORT PASCAL SendArgsD(int ruleIndex, int clauseIndex, int argIndex, int clauseType, double value);
extern int EXPORT PASCAL SendArgsC(int ruleIndex, int clauseIndex, int argIndex, int clauseType, char *value);
extern int EXPORT PASCAL SendFunction(int ruleIndex, int clauseIndex, int clauseType, int funcConst);
extern int EXPORT PASCAL printrulestofile();
extern double EXPORT PASCAL sumcellbylevel();

#ifndef MSGBOXWARNING
void MsgBoxWarning();
#define MSGBOXWARNING
#endif
void MsgBoxError();
void SetHomeDir();
void SetVersion();
#endif

int getcellindices();
int getlocationclass();
double AllCells();
int cside_pdf(int drugindex,double time,double fraction,int treatIdx);
void evaluate_schedule();
void RemoveTreatments();

int getcelllevelbyclass ();
int get_dest_gap();
int getcellindex();

boolean CheckIfICRuleApplies();
boolean DoesICRuleAppliesToCelltype();

#ifndef DRUGANDTIMEDEFINED
struct drugandtime { int d; double t; double df; int TreatmentIdx; } dnt;
#define DRUGANDTIMEDEFINED
#endif

struct drugandtime TreatmentExists();
double TreatmentExistsInt();

double logit();
double antilogit();

void ParseSchedule();
int ApplyRegimenIntoSchedule();
void CalculateTimeKillSurv();

double lognorm(double mean, double cv);
void CheckGuaranteeTime();
void release_mem();