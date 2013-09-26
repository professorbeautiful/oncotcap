#ifndef CONSTANTSDEFINED

#define CONSTANTSDEFINED

#define true          1
#define false         0
#define True          1
#define False         0
#define NEG			 -1

#define PGF 1
#define SIM 0
/* Randmarm setting */
#define TOXRAND  1
#define METRAND  2
#define CNRAND	 0
#define INITRAND 2
#define NONRAND  0
#define RAND  1
#define SEEDRAND 3
#define PROPGOMP -9
#define VEGF     -10

/* INV_RAND_MAX */
#define INV_RAND_MAX  1.0/RAND_MAX

#define	MAXDRUGS     10
/* changed by Qingshou (  -1 to 0 ) */
#define	ENVINDEX     0
#define EVAL          0
#define MAXTYPES      50
#define MAXCLASSES    20
#define MAXGOMPCLASSES  4
#define MAXLOOKUPS    100
#define MAX_CHAR      70
#define MAXTIMES     1000
#define INV_LN2       1.442696
#define MAXLOGPLATEAU 30
#define LN2           0.6931471805
#define LN10          2.3025850929
#define ZERO          0.0
#define ONE           1.0
#define NENVIRS       4
#define MAXPROBS     30
#define ENDOFSTRING   0
#define SMALLBUFFER 200
#define PI 3.141592653589 
#define EOLN '\n'
#define tcapCT 0
#define tcapDT 1
#define tcapDR 2

/*** LP constants --> ****/
#define MAXSPLUS2  32
#define MAXS       30
#define BINS       22
#define RWORK       1
#define IWORK       1
/*** <-- constants LP ****/

/*** plotting flags ****/
#define FIXD        1
#define FIXP        2
#define TIMEC       3
/*** conditioning flags --> ****/

#define MAXCONDS   20
#define LT         -1
#define GE          1
#define YES         1
#define NO          0
#define GTZERO      1
#define NOCOND     -1
#define EQZERO      0
#endif

/*#define ArchiveDir "e:\\users\\day\\treatsim.working\\archive"
#define WorkingDir "e:\\users\\day\\data.working"*/

/* New additions for toxicity */
#define MAXTOXTYPES 20
#define CUMTOX 1
#define ABSTOX 2
#define BYONEGRADE 1
#define FALLTOZERO 2
#define NEVERRESOLVES -1

#define MAXQLENGTH 500

/* constants and globals for event tracking */
/*
#define ONTRIALEVENT 1
#define OFFTRIALEVENT 2
#define BEGINSIMEVENT 3
#define TUMORSTARTEVENT 4
#define DIAGNOSISEVENT 5
#define TOXRESOLVEEVENT 6
#define CREVENT 7
#define RECURRENCEEVENT 8
#define TREATMENTEVENT 9
#define EFUTUMEVENT 10
#define TOXICITYEVENT 11
#define METEVENT 12
#define EFUNEDEVENT 13
#define NORESPONSEEVENT 14
#define DOSEMODIFICATIONEVENT 15
#define RESPONSEEVENT 16
#define CUREEVENT 17
#define STAGEONEFINISHEVENT 18
#define DEATHEVENT 19
*/

#define ONTRIALEVENT 1
#define BEGINSIMEVENT 2
#define TUMORSTARTEVENT 3
#define DIAGNOSISEVENT 4
#define CREVENT 5
#define RESPONSEEVENT 6
#define CUREEVENT 7
#define NORESPONSEEVENT 8
#define RECURRENCEEVENT 9
#define METEVENT 10
#define TOXRESOLVEEVENT 11
#define TREATMENTEVENT 12
#define TOXICITYEVENT 13
#define DOSEMODIFICATIONEVENT 14
#define OFFTRIALEVENT 15
#define EFUTUMEVENT 16
#define EFUNEDEVENT 17
#define STAGEONEFINISHEVENT 18
#define DEATHEVENT 19
#define RESETEVENT 20

/*Dose mod type */
#define NEXTDOSE 1
#define RESTOFCOURSE 2
#define ALLREMAININGCOURSES 3
#define UNTILTOXRESOLVES 4

#define CELLQ 1
#define EVENTQ 2

/* Patient Flags */
#define NEWPATIENT 1
#define PREVPATIENT 2

/* Plot flags */
#define PLOTNOPROPSELECTED  10 /*is same as plotting totals*/
#define PLOTALLPROPSELECTED  20 /*is same as plotting all cells*/
#define PLOTSOMEPROPSELECTED  30

/*Subset Flags */
#define SUBSETALLCELLSSELECTED 2 /*'includes all celltypes in the subset*/
#define SUBSETSOMECELLSSELECTED 3

/* should delete the following 3 flags after confirming that all the code now uses 
the new plot flags*/
#define PLOTTOTALS 1
#define PLOTALLCELLS 2
#define PLOTSOMECELLS 3

/*Inital Cell Count Flags */
#define INITFIXED 1
#define INITRANDOM 2

/*Treatment type*/
#define TCAPAGENT 1
#define TCAPCOMBO 2
#define TCAPCONTINDRUG 3
#define TCAPCOURSE 4

#define MAXREGIMENS 20


/* constants for variable types */
#define TCAPBOOLEAN  1
#define TCAPINTEGER  2
#define TCAPFLOATING  3
#define TCAPSTRING 4

/* constants for rulemanager context */
#define TCAPPATIENTMGR  1
#define TCAPTRIALMGR  2

/* constants for Actions */
#define TCAPSTATUSVAR  1
/*  patient mgmt actions */
#define TCAPStartNewCourse  2
#define TCAPModifyDose  3
/*  trial mgmt actions */
#define TCAPStartNextStage  2
#define TCAPTerminateTrial  3
#define TCAPMODIFYDOSETO  4
#define TCAPMODIFYDOSEBY  5

#define MAXTRTEGREATMENTS 50

#define USERAND 1
#define USERANDMAR 2
#define USEMEANONLY 3

#define MACRO 2