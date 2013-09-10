#include "build.h"

#ifndef UNIX
#include <windows.h>
#include <crtdbg.h> 
#endif
#define BASEMODEL
#include "defines.h"
#include "classes.h"
#include "cellp.h"
#include "InducedConv.h"
#include "tox.h"
#include "rule.h"

extern int  nloops,cside_pdf(),	IfAnyPGP,minsearch;
extern double base_kill[MAXDRUGS],mincycletime;
extern char *unix_strlwr();
extern void fSread(),read_into_temp(),read_to_eol(),init_rule(),clear_base_model();
void ReadHetClass(),ReadAgents(),ReadCombos(),ReadCourses(),EvaluateRuleInModelfile();
void ReadICRules(), ReadGRRules(),ReadMgmtRules(),ReadMgmtVars(),ReadRegimens();
int getAgentIndex();

#define strcmpSAME 0
#define maxAgents 10
#define maxCombos 10
#define maxCourses 10
#define tcapYear 1
#define tcapMonth 2
#define tcapDay 3
#define tcapHour 4
#define tcapWeek 5

void CreateCurrPoints();

struct ComboAndCourseAgent
{
    int Index;
	int numTrtTime;
    double Dose;
	double TreatmentTime[29];
} ComboAndCourseAgent;

struct Combo{
	char Name[SMALLBUFFER];
	int numAgents;
	struct ComboAndCourseAgent cmbAgents[maxAgents];
} Combos[maxCombos];

struct Course{
	char Name[SMALLBUFFER];
	int numAgents;
	struct ComboAndCourseAgent courseAgents[maxAgents];
} Courses[maxCourses];

int nCombos,nCourses,AgentTypes[maxAgents];

int ReadModelFile( fname,type )
char *fname;
{
   int i,lcount,I_ignore,idx;
   double D_ignore,deltaT_SET,deltaT_PGF,CondTime;
   char S_ignore[SMALLBUFFER],tempstring[SMALLBUFFER*10];
   char vbuff[256],TimeUnit[20];
   float fltVsn;
   FILE *fp;
  
	if (( fp = fopen(fname,"r")) ==  NULL)
	{
		return (FALSE);
	}

	   
	fSread(fp,S_ignore); /* version */
  
	nICrules = 0;
	TotalRules=0;
	TotalSingleRules=0;
	Dead=false;
	nToxTypes = 0;
    nRules=0;

	clear_base_model( );

	/* SetEnvior */
	nenvlist = 1;
    envlist[1].t = 0.0;
    envlist[1].e = 0;

	/* InitDoseList */
	for(i=1;i<=maxAgents;i++)
	{
		ndoses[i]=0;
	}
	 
	fIread (fp, KineticsModel );    fdumpline ( fp );
	fIread (fp, KineticsUnchecked );   fdumpline ( fp );
	fDread (fp, Base_Doubling_Time );    fdumpline ( fp );
	fDread (fp, Base_Death_Time);  fdumpline ( fp );
	fDread (fp, Base_Cycling_Time );  fdumpline ( fp );

	mincycletime = Base_Cycling_Time;

	if ( KineticsModel == 1 ) { /* Gompertz model */
		fDread (fp, Base_Doubling_Time );    fdumpline ( fp );
		fDread (fp, Base_Death_Time );   fdumpline ( fp );
		fDread (fp, Base_Cycling_Time );  fdumpline ( fp );
	}
	else { /* ignore the data for exponetial model */
		fDread (fp, D_ignore );    fdumpline ( fp );
		fDread (fp, D_ignore );   fdumpline ( fp );
		fDread (fp, D_ignore);  fdumpline ( fp );
	}

	if ( KineticsModel == 1 ) { /* Gompertz model */
		fDread (fp, Base_GompPlateau ); fdumpline ( fp );
		fDread (fp, Base_GompSplit); fdumpline ( fp );
		GompRule[0][0].gp_or_inv.GP=Base_GompPlateau;
		if ( Base_GompSplit==0.0 )
				Base_GompSplit=1.0; 
		GompRule[0][0].GS=Base_GompSplit;
	}
	else {
		fSread(fp,S_ignore);
		fSread(fp,S_ignore);
	}

	/* ignore logkill & agent lists now */
	fIread (fp, lcount);  fdumpline ( fp );
	for ( i=1; i<=lcount; i++) {
		fSread(fp, S_ignore);
	}

	/* ignore treatment lists and type now */
	fIread (fp,lcount);fdumpline ( fp );
	for ( i=1; i <= lcount; i++) {
		fSread(fp,S_ignore);
		fSread(fp,S_ignore);
	}

	fIread (fp, Number_of_Classes );  fdumpline ( fp );
	for ( i=0; i<Number_of_Classes; i++) {
		ReadHetClass(fp,i);
	}

	/* read Agents */
	fSread (fp, tempstring);
	idx=0;
	read_into_temp(S_ignore,tempstring,&idx);
	if (strcmp(S_ignore,"<nAgents>")!=strcmpSAME)
#ifdef DLL
		MsgBoxWarning(" ReadModelFile:Bad nAgents");
#else 
		fprintf(eout," ReadModelFile:Bad nAgents");
#endif
	else {
		read_to_eol(S_ignore,tempstring,&idx);
		ndrugs = atoi(S_ignore);
	}

	for (i = 1; i <= ndrugs; i++)
	{
		 ReadAgents(fp,i);
	}

	/* read Combos */
	fSread (fp, tempstring);
	idx=0;
	read_into_temp(S_ignore,tempstring,&idx);
	if (strcmp(S_ignore,"<nCombos>")!=strcmpSAME)
#ifdef DLL
				MsgBoxWarning(" ReadModelFile:Bad nCombos");
#else 
				printf(" ReadModelFile:Bad nCombos");
#endif
	
	else {
		read_to_eol(S_ignore,tempstring,&idx);
		nCombos = atoi(S_ignore);
	}

	for (i = 1; i <= nCombos; i++)
	{
		 ReadCombos(fp,i);
	}
   /* read Courses */
	fSread (fp, tempstring);
	idx=0;
	read_into_temp(S_ignore,tempstring,&idx);
	if (strcmp(S_ignore,"<nCourses>")!=strcmpSAME)
#ifdef DLL
				MsgBoxWarning(" ReadModelFile:Bad nCourses");
#else
				printf("ReadModelFile:Bad nCourses");
#endif
	else {
		read_to_eol(S_ignore,tempstring,&idx);
		nCourses= atoi(S_ignore);
	}
	for (i = 1; i <= nCourses; i++)
	{
		 ReadCourses(fp,i);
	}

	fSread(fp,S_ignore); /* [END of TREATMENTS] */

	fDread(fp,InitMean); fdumpline ( fp );
	fDread(fp,CoVar); fdumpline ( fp );
	CoVar = 0.01*CoVar;
	fIread(fp,init_active_ntypes); fdumpline ( fp );
	CNcopy = (double * )calloc(init_active_ntypes+1,(sizeof(double)));
	initCellVector = (double *)calloc(init_active_ntypes+1,(sizeof(double)));

	for ( i=1; i<= init_active_ntypes;i++){
		fDread (fp,D_ignore); fdumpline ( fp );
		LookUpId[i]=i;
		initCellVector[i] = D_ignore*0.01;
	}

	fSread(fp,S_ignore);
	if (strcmp(S_ignore,"<Death Threshold>")==strcmpSAME) {
		fDread(fp,death_threshold);fdumpline ( fp );
		fSread(fp,S_ignore);
		fSread(fp,S_ignore);
	}

	if (strcmp(S_ignore,"<Diagnosis Threshold>")==strcmpSAME) {
		fDread(fp,PE_diagnosis_threshold);fdumpline ( fp );
		fSread(fp,S_ignore);
		fSread(fp,S_ignore);
	}

	if (strcmp(S_ignore,"<Response Threshold>")==strcmpSAME) {
		fDread(fp,response_threshold);fdumpline ( fp );
		fSread(fp,S_ignore);
		fSread(fp,S_ignore);
    }

	if (strcmp(S_ignore,"<LR Diagnosis Threshold>")==strcmpSAME) {
		fDread(fp,LR_diagnosis_threshold);fdumpline ( fp );
		fSread(fp,S_ignore);
		fSread(fp,S_ignore);
    }

	if (strcmp(S_ignore,"<PR Diagnosis Threshold>")==strcmpSAME) {
		fDread(fp,D_ignore);fdumpline ( fp );
		fSread(fp,S_ignore);
		fSread(fp,S_ignore);
    }

	if (strcmp(S_ignore,"<PlotPoints>")==strcmpSAME) {
		fIread(fp,I_ignore);fdumpline ( fp );
		fSread(fp,S_ignore);
		fSread(fp,S_ignore);
    }

	if (strcmp(S_ignore,"<General Rules>")==strcmpSAME) {
		fIread(fp,lcount);fdumpline ( fp );
		for ( i=1; i<= lcount; i++) {
			fSread(fp,S_ignore);
			fSread(fp,S_ignore);
			fSread(fp,S_ignore);
			fSread(fp,S_ignore);
			if (strcmp(S_ignore,"<Induction Rule>")==strcmpSAME) {
				ReadICRules(fp);
			}
			else {
				ReadGRRules(fp,S_ignore);
			}
		}
		fSread(fp,S_ignore); /* </General Rules> */
		fSread(fp,S_ignore);
	}

	EvaluateRuleInModelfile();

	if (strcmp(S_ignore,"<Tumor Type>")==strcmpSAME) {
		fIread(fp,I_ignore);fdumpline ( fp );
		fSread(fp,S_ignore);
		fSread(fp,S_ignore);
	}

	if (strcmp(S_ignore,"<Tumor Evaluation Interval>")==strcmpSAME) {
		fDread(fp,TumorExaminationInterval);fdumpline ( fp );
		fSread(fp,S_ignore);
		fSread(fp,S_ignore);
	}

   	if (strcmp(S_ignore,"<Tumor Evaluation Factor>")==strcmpSAME) {
		fDread(fp,TumorCombinationFactor);fdumpline ( fp );
		fSread(fp,S_ignore);
		fSread(fp,S_ignore);
	}
	else TumorCombinationFactor = 2.0/3.0;
  
  	if (strcmp(S_ignore,"<deltaT_SET>")==strcmpSAME) {
		fDread(fp,deltaT_SET);fdumpline ( fp );
		fSread(fp,S_ignore);
		fSread(fp,S_ignore);
	}

	if (strcmp(S_ignore,"<deltaT_PGF>")==strcmpSAME) {
		fDread(fp,deltaT_PGF);fdumpline ( fp );
		fSread(fp,S_ignore);
		fSread(fp,S_ignore);
	}

	if (strcmp(S_ignore,"<Conditional Time>")==strcmpSAME) {
		fDread(fp,CondTime);fdumpline ( fp );
		fSread(fp,S_ignore);
		fSread(fp,S_ignore);
	}

	if ( deltaT_SET > 0 ) delta_t = deltaT_SET * mincycletime;
	else delta_t= 0.007*mincycletime;

	if (strcmp(S_ignore,"<OptionRNG>")==strcmpSAME) {
 		fSread(fp,S_ignore);
		fSread(fp,S_ignore);
		fSread(fp,S_ignore);
	}

	if (strcmp(S_ignore,"<PtMgmtRules-SetFcns>")==strcmpSAME) {
		ReadMgmtRules(fp);
		fSread(fp,S_ignore);
	}
	if (strcmp(S_ignore,"<PtMgmtVars>")==strcmpSAME) {
		ReadMgmtVars(fp);
		fSread(fp,S_ignore); 
	}
	if (strcmp(S_ignore,"<NewKinetics>")==strcmpSAME) {
		fIread(fp,Base_Cycling_Switch);fdumpline ( fp );
		fIread(fp,Base_Death_Switch);fdumpline ( fp );
		if ( KineticsModel == 1 ) { /* Gompertz model */
			fIread (fp, Base_Cycling_Switch );   fdumpline ( fp );
			fIread (fp, Base_Death_Switch );  fdumpline ( fp );
		}
		else { /* ignore the data for exponetial model */
			fDread (fp, I_ignore );   fdumpline ( fp );
			fDread (fp, I_ignore);  fdumpline ( fp );
		}
		fSread(fp,S_ignore); /* </Newkinetics> */
		fSread(fp,TimeUnit); 
	}
	else strcpy(TimeUnit,S_ignore);

	fDread(fp,BeginPlotTime); /* BeginAxisTime */fdumpline ( fp );
	fDread(fp,EndPlotTime); /* EndAxisTime */fdumpline ( fp );
	fDread(fp,StartT); fdumpline ( fp );
	fDread(fp,EndT);fdumpline ( fp );
	fSread(fp,S_ignore); /* tGap */
	fSread(fp,S_ignore);  /* tTick */

	if ( CondTime > StartT )
		nconds = 1;
	else 
		nconds = 0;

	for ( i=1; i<=nconds; i++) {
		condition[i].t=CondTime;
	}
	/* evaluation points */
	if ( type == PGF ) {
	   CreateCurrPoints(tempstring,deltaT_PGF,CondTime);
	   ApplySchedule(tempstring,0);
	}

	InitToxTypes();
	ReadRegimens(fp,TimeUnit);
	return (true);
}

void CreateCurrPoints(CurrEvalPoints,deltaT_PGF,ConditionalTime)
char CurrEvalPoints[];
double deltaT_PGF,ConditionalTime;
{
int inserted = false;
double time;

	sprintf(CurrEvalPoints," ");
	for (time = deltaT_PGF * mincycletime; time <=EndT; time +=
		deltaT_PGF * mincycletime) {
		if ( (ConditionalTime > StartT)&&( ConditionalTime <= time)&&(inserted = false)){
			inserted = true;
			sprintf(CurrEvalPoints,"%s%5.3lf ",CurrEvalPoints, ConditionalTime);
		}
		sprintf(CurrEvalPoints,"%s%5.3lf ",CurrEvalPoints,time);
	}
	sprintf(CurrEvalPoints,"%s%5.3lf ",CurrEvalPoints,EndT);
}
void EvaluateRuleInModelfile(){
	int i,cn,iRule,iICrule;
	extern int get_ntypes();
	extern void cal_pre_post_prod(),construct_type_name(),display_summary( ),fill_env();
	extern void sort_rules(),evaluate_store_rule(),fill_clevel();
	ntypes=get_ntypes();

    if ( ntypes >= MAXTYPES ) {
        fprintf(eout,"MAXTYPES is not large enough.\n");
	}

	cal_pre_post_prod();

	for ( i= 1; i <=min((int)ntypes,MAXTYPES);i++ )
		construct_type_name(i,typename[i]);
	  
	cn = -1;
	macro_block_length =1;
	num_macro_nodes = 1;
	if ( Class[0].class_type == 2 ) cn=0;
	else if ((Number_of_Classes > 1 ) && (Class[1].class_type == 2 )) cn =1;
	 
	if ( cn >=0 ) macro_block_length = Class[cn].post_product;
	if ( cn > 0 ) 
		  num_macro_nodes = Class[cn].no_levels*Class[cn-1].no_levels;
	else num_macro_nodes = Class[0].no_levels;
	  
	  /* go through all the rules for changing kinetic parameters for particular
	   * treatments and set up the needed environments
	   */
    
	for ( iRule= 0 ; iRule < TotalRules; iRule++ )
		evaluate_store_rule( iRule );
		   
		/* sort rule by type first */
	if ( TotalSingleRules > 0 ) 
		sort_rules(Rules,0,TotalSingleRules-1);

	/* after reading all rules, convert the level name of propgomp rule
	   to level index */
	if ( IfAnyPGP ) fill_clevel();

	fill_env();
	nowenv = 0;

	minsearch=min(3,Number_of_Classes);

#ifndef MPI
	display_summary( );
#endif

	for (iICrule=1; iICrule <= nICrules;iICrule++){
		for (i= 0;i<nenvirons;i++)
			{
			   if (strcmp(icRules[nICrules].envName,Environments[i])==strcmpSAME)
			   {
				   icRules[nICrules].envIndex = i;
			   }
			}	   
	}

}

void ReadHetClass(FILE *fp,int C){
	char S_ignore[SMALLBUFFER],tempstring[SMALLBUFFER],temp[SMALLBUFFER];
	int idx,thisKey,L=0,R=0;
	int doneread = false;
	extern void expand_macro_rule(),read_nomacro_rule();
	extern int lookupKey();
	#define parseKeyC 9
	#define parseKeyL 10
	#define parseKeyR 11

	fSread(fp,S_ignore); /* version */
	fSread(fp,tempstring);
	idx=0;
	read_into_temp(S_ignore,tempstring,&idx);
	if (strcmp(S_ignore,"<HETBOXNUM>")!=strcmpSAME)
#ifdef DLL
		MsgBoxWarning(" [ReadHetClass],sholud be <HETBOXNUM.");
#else 
		printf(" [ReadHetClass],sholud be <HETBOXNUM.");
#endif
	else read_to_eol(S_ignore,tempstring,&idx);

	Class[C].class_type = atoi(S_ignore); /* class_type */

	fSread(fp,tempstring);
	if (strcmp(tempstring,"</HETFILE>")==strcmpSAME) doneread=true;
	while (!doneread) {
		idx=0;
		read_into_temp(temp,tempstring,&idx);
		thisKey = lookupKey( temp);
		switch ( thisKey ){
		    case parseKeyC:
				read_to_eol(Class[C].class_name,tempstring,&idx);
                Class[C].no_levels = 0;
			    L = 0;
				break;
			case parseKeyL:
				read_to_eol(Class[C].Level[L].level_name,tempstring,&idx);
				Class[C].Level[L].no_rules = 0;
				Class[C].no_levels = ++L;
				R = 0;
				break;
			case parseKeyR:
				if ( Class[C].class_type == 2 )
					expand_macro_rule(tempstring,&idx,C+1,&L,&R);
				else 
					read_nomacro_rule(tempstring,&idx,C+1,L,&R);
				break;
		}
		fSread(fp,tempstring);
		if (strcmp(tempstring,"</HETFILE>")==strcmpSAME) doneread=true;
	}
}

int getAgentIndex(char *agentname) {
	int idx;

	for ( idx=1; idx<=ndrugs; idx++){
       if (!(strcmp(drugname[idx],agentname)))
		 {
				return idx;
		 }
	  }

	return (-1);
}

int getAgentComboCourseTypeAndIndex(char name[],int *index) {
	int idx;
	char tempname[SMALLBUFFER];

	for ( idx=1; idx<=nCombos; idx++){
       	 strcpy(tempname,Combos[idx].Name);
			 if (!(strcmp(tempname,name)))
			 {
				*index=idx;
			 	return 2;
			 }
	  }
	for ( idx=1; idx<=nCourses; idx++){
       	 strcpy(tempname,Courses[idx].Name);
			 if (!(strcmp(tempname,name)))
			 {
				*index=idx;
			 	return 4;
			 }
	  }

	for ( idx=1; idx<=ndrugs; idx++){
     	if (!(strcmp(drugname[idx],name)))
		 {
			*index=idx;
			return AgentTypes[idx]; /* can be continuous agent */
		 }
	  }
	return (-1);
}

int GetClassIndex(char *classnameIN){
	 int C;
	 char classname[SMALLBUFFER];
	 
	 for ( C = 0; C < Number_of_Classes; C++ ){
		  strcpy(classname,Class[C].class_name);
 			 if (!(strcmp(classname,classnameIN)))
			 {
				 	return C;
			 }
	  }

	return (-1);
}

int GetLevelIndex(int classindex,char *levelnameIN){
  int L;
  char levelname[SMALLBUFFER];
 
	  for ( L = 0; L < Class[classindex].no_levels; L++ ) {
		 strcpy(levelname,Class[classindex].Level[L].level_name);
			 if (!(strcmp(levelname,levelnameIN)))
			 {
					 return (L);
			 }
	   }
	  return (-1);
}

void ReadICRules(FILE *fp){
int i;
char S_ignore[SMALLBUFFER],classname[SMALLBUFFER], levelname[SMALLBUFFER],agentname[SMALLBUFFER];

	nICrules=nICrules+1;

	fSread(fp,icRules[nICrules].envName);
	if (strcmp(icRules[nICrules].envName,"NODRUG") == strcmpSAME)
	{
	   icRules[nICrules].envIndex = -1;
	}
	
	fIread(fp,icRules[nICrules].nClassAndLevels);fdumpline ( fp );

	/* if parts */
	for ( i=0; i< icRules[nICrules].nClassAndLevels;i++) {
		fSread(fp,classname);
		fSread(fp,levelname);
		icRules[nICrules].ifClass[i].classIndex = GetClassIndex(classname);
		icRules[nICrules].ifClass[i].levelIndex = GetLevelIndex(icRules[nICrules].ifClass[i].classIndex,levelname);
	}

	/* agent name */
	fSread(fp,agentname);
	icRules[nICrules].agentIndex = getAgentIndex(agentname);

	/* source parts */
	fSread(fp,classname);
	fSread(fp,levelname);
	icRules[nICrules].Source.classIndex =GetClassIndex(classname);   
	icRules[nICrules].Dest.classIndex =icRules[nICrules].Source.classIndex;	   
	icRules[nICrules].Source.levelIndex =GetLevelIndex(icRules[nICrules].Source.classIndex,levelname);

	/* destination parts */
	fSread(fp,classname);
	fSread(fp,levelname);
	icRules[nICrules].Dest.levelIndex = GetLevelIndex(icRules[nICrules].Source.classIndex,levelname);

	fDread(fp,icRules[nICrules].Prob);fdumpline ( fp );
	fSread(fp,S_ignore);
}

void ReadGRRules(FILE *fp,char *envname){
int i,nenvi=0;
char classname[SMALLBUFFER], levelname[SMALLBUFFER],GRabbrev[10],S_ignore[SMALLBUFFER];
char opr[5],mark[20];

	Rules[TotalRules].EnvirCond[nenvi].ienv=0;

	strcpy(Rules[TotalRules].EnvirCond[nenvi].envir_name,envname);
	if (strcmp(Rules[TotalRules].EnvirCond[nenvi].envir_name,"") == strcmpSAME)
		Rules[TotalRules].nEnvirConds =0;
	else Rules[TotalRules].nEnvirConds =1;

	fIread(fp,Rules[TotalRules].nLevelConds);fdumpline ( fp );

	/* if parts */
	for ( i=0; i< Rules[TotalRules].nLevelConds;i++) {
		strcpy(Rules[TotalRules].LevelCond[i].clopr,"="); /* can be <=, >= */
		fSread(fp,classname);
		fSread(fp,levelname);
		Rules[TotalRules].LevelCond[i].iclass = GetClassIndex(classname);
		Rules[TotalRules].LevelCond[i].ilevel= GetLevelIndex(Rules[TotalRules].LevelCond[i].iclass,levelname);
	}

	/* GR abbrev */
	fSread(fp,GRabbrev);
	fSread(fp,mark);
	if (strcmp(mark,"<mutgrule>") == strcmpSAME){
		/* destination parts */
		fSread(fp,classname);
		fSread(fp,levelname);
		fSread(fp,S_ignore); /* </mutgrule> */
		fSread(fp,opr);
		if (strcmp(classname,"") != strcmpSAME){
			sprintf(Rules[TotalRules].action.rule_name,"%s [%s]%s %s",GRabbrev,classname,levelname,opr);
		}
		else {
			sprintf(Rules[TotalRules].action.rule_name,"%s %s",GRabbrev,opr);
		}
	}
	else 
		sprintf(Rules[TotalRules].action.rule_name,"%s %s",GRabbrev,mark);

	fSread(fp,levelname); /* source level name, it is redundant for mr rule */
	fSread(fp,S_ignore);

	sprintf(Rules[TotalRules].action.rule_name,"%s %s",Rules[TotalRules].action.rule_name,S_ignore);

	TotalRules++;

}

void DumpJunk(FILE *fp,char outname[SMALLBUFFER]) {
char S_ignore[SMALLBUFFER],tempstring[SMALLBUFFER];
int idx;

	fSread(fp,tempstring);
	idx=0;
	read_into_temp(S_ignore,tempstring,&idx);

	if (strcmp(S_ignore,"Version") == strcmpSAME) fSread(fp,tempstring);
    if (tempstring[0]== '[') fSread(fp,tempstring);
   
	strcpy(outname,tempstring);
}
void CreateDummyClassLevel(int iAgent,char *rulename){
int R=0;

	sprintf(Class[Number_of_Classes].class_name,"Continuous_Agent%1d",iAgent);
	Class[Number_of_Classes].no_levels = 1;
	sprintf(Class[Number_of_Classes].Level[0].level_name,"Dummy_Level");
	strcpy(Rules[TotalSingleRules].action.rule_name,rulename);
	Number_of_Classes++;
	init_rule(Number_of_Classes,1,&R);
}

void ReadAgents(FILE *fp,int iAgent){
	char S_ignore[SMALLBUFFER],tempstring[SMALLBUFFER];
	int idx,itox,a;

	DumpJunk(fp,S_ignore);
	strcpy(drugname[iAgent],S_ignore);
	
	fDread(fp,base_kill[iAgent]); fdumpline ( fp );
	fIread(fp,AgentTypes[iAgent]); fdumpline ( fp );
	fSread(fp,S_ignore);  /* rule */

	if (strcmp(S_ignore,"") != strcmpSAME){
		/* This is continuous agent with rule */
		CreateDummyClassLevel(iAgent,S_ignore);
	}
	/* Toxicity information */
	fSread(fp,tempstring);
	idx=0;
	read_into_temp(S_ignore,tempstring,&idx);
	if (strcmp(S_ignore,"<NumToxTypes>") == strcmpSAME){
		read_to_eol(S_ignore,tempstring,&idx);
		Agents[iAgent].numToxTypes =atoi(S_ignore);
	}
	else Agents[iAgent].numToxTypes = 1;

	for (itox=1; itox <= Agents[iAgent].numToxTypes; itox++) {
		fSread(fp,Agents[iAgent].toxicity[itox].name);
		Agents[iAgent].toxicity[itox].toxTypeIndex = -1;
		for ( a=1;a<=5; a++) {
			fDread(fp,Agents[iAgent].toxicity[itox].toxProbs[a]); fdumpline ( fp );
			Agents[iAgent].toxicity[itox].toxProbs[a]=Agents[iAgent].toxicity[itox].toxProbs[a]*0.01;
		}
		/* toxModel on VB side */
		fIread(fp,Agents[iAgent].toxicity[itox].toxAppType);fdumpline ( fp );
		Agents[iAgent].toxicity[itox].toxAppType++; /* C side uses 1 & 2 for CUM & ABS */
		fIread(fp,Agents[iAgent].toxicity[itox].toxResType);fdumpline ( fp );
		fDread(fp,Agents[iAgent].toxicity[itox].toxResTime); fdumpline ( fp );
	}
	fSread(fp,S_ignore);/* stdDose */
	fSread(fp,S_ignore);/* Unit */
}

void ReadAgentDuplicate(FILE *fp){
	char S_ignore[SMALLBUFFER],tempstring[SMALLBUFFER];
	int idx,itox,a,lcount;

	fSread(fp,S_ignore); /* base kill */
	fSread(fp,S_ignore); /* agent type for VB side  */
	fSread(fp,S_ignore);  /* rule */

	/* Toxicity information */
	fSread(fp,tempstring);
	idx=0;
	read_into_temp(S_ignore,tempstring,&idx);
	if (strcmp(S_ignore,"<NumToxTypes>") == strcmpSAME){
		read_to_eol(S_ignore,tempstring,&idx);
		lcount =atoi(S_ignore);
	}
	else lcount = 1;

	for (itox=1; itox <= lcount; itox++) {
		fSread(fp,S_ignore);
		for ( a=1;a<=5; a++) {
			fSread(fp,S_ignore);
		}
		/* toxModel on VB side */
		fSread(fp,S_ignore);
		fSread(fp,S_ignore);
		fSread(fp,S_ignore);
	}
	fSread(fp,S_ignore);/* stdDose */
	fSread(fp,S_ignore);/* Unit */
}

void ReadCombos(FILE *fp,int i){
char S_ignore[SMALLBUFFER];
int iAgent;

	DumpJunk(fp,S_ignore);/* Combo name */
	strcpy(Combos[i].Name,S_ignore);
	fIread(fp,Combos[i].numAgents); fdumpline ( fp );
	for ( iAgent=1; iAgent<=Combos[i].numAgents;iAgent++){
		DumpJunk(fp,S_ignore);/* Agent name */
		Combos[i].cmbAgents[iAgent].Index=getAgentIndex(S_ignore);
		ReadAgentDuplicate(fp); 
		fSread(fp,S_ignore); /* Agent Dose */
		if (strcmp(S_ignore,"<AgentDose>") == strcmpSAME){
			fDread(fp,Combos[i].cmbAgents[iAgent].Dose);fdumpline ( fp );
			fSread(fp,S_ignore);
		}
	}
}

void ReadCourses(FILE *fp,int i){
char S_ignore[SMALLBUFFER];
int iAgent,jtime;

	DumpJunk(fp,S_ignore); /* Course name */
	strcpy(Courses[i].Name,S_ignore);
	fIread(fp,Courses[i].numAgents); fdumpline ( fp );
	for ( iAgent=1; iAgent<=Courses[i].numAgents;iAgent++){
		DumpJunk(fp,S_ignore);/* Agent name */
		Courses[i].courseAgents[iAgent].Index=getAgentIndex(S_ignore);
		ReadAgentDuplicate(fp); 
		fSread(fp,S_ignore); /* CourseTreatmentApplications*/
		if (strcmp(S_ignore,"<CourseTreatmentApplications>") == strcmpSAME){
			/* treatment time*/
			jtime=1;
			fSread(fp,S_ignore);
			while (strcmp(S_ignore,"</CourseTreatmentApplications>") != strcmpSAME){
				Courses[i].courseAgents[iAgent].TreatmentTime[jtime]=atof(S_ignore);
				fSread(fp,S_ignore);
				jtime++;
			}
			Courses[i].courseAgents[iAgent].numTrtTime=jtime-1;
			fSread(fp,S_ignore);
			if (strcmp(S_ignore,"<CourseTreatmentAppDoses>") == strcmpSAME){
				fDread(fp,Courses[i].courseAgents[iAgent].Dose);fdumpline ( fp );
				fSread(fp,S_ignore); /* </CourseTreatmentAppDoses> */
			}
		}
	}
}

/* convert tcapDay to corresponding timeunit */
double timeConversion(int UnitId) {
double timeConversionTable;

	switch (UnitId){
	case tcapYear:timeConversionTable = 1;
					break; 
	case tcapMonth:timeConversionTable = 1.0 / 30.0;
					break; 
	case tcapDay:timeConversionTable = 1;
					break; 
	case tcapHour:timeConversionTable = 24;
					break; 
	case tcapWeek:timeConversionTable = 1.0/7.0;
					break;
	default:timeConversionTable =1;
	}

	return timeConversionTable;

}

int Unit2Int(char *unit){
char *tunit;
int Unit2Int;

#ifndef UNIX
		tunit=_strlwr(unit);
#else 
		tunit=unix_strlwr(unit);
#endif

	if (strcmp(tunit,"year") == strcmpSAME)
		 Unit2Int = tcapYear;
	else if (strcmp(tunit,"month") == strcmpSAME)
		 Unit2Int = tcapMonth;
	else if (strcmp(tunit,"day") == strcmpSAME)
		 Unit2Int = tcapDay;
	else if (strcmp(tunit,"hour") == strcmpSAME)
		 Unit2Int = tcapHour;
	else if (strcmp(tunit,"week") == strcmpSAME)
		 Unit2Int = tcapWeek;
	else 
		 Unit2Int = 0;

return Unit2Int;

}

void ReadRegimens(FILE *fp, char *TimeUnit) {
char S_ignore[SMALLBUFFER],trtName[SMALLBUFFER];
int nTreatments,iTrt,trtIndex,trtType,k,ret,didx,iCourseTime,trtidx=0;
double trtDose,trtTime,trtEndtime,CourseAppTime;
extern void buildsched(),sortsched();


	fSread(fp,S_ignore); /* version */
	fIread(fp,nTreatments);fdumpline ( fp );

	/* InitDoseFactor() */

	for(k=1;k<=nsched;k++)
	{
		sched[iTrt].df = 1.0;
	}

	fSread(fp,S_ignore); /* nmae: aaabbbbbbbb*/
	for ( iTrt=1; iTrt<= nTreatments;iTrt++) {
		fSread(fp,trtName);
		fDread(fp,trtTime);fdumpline ( fp );
		fDread(fp,trtDose);fdumpline ( fp );
		trtType=getAgentComboCourseTypeAndIndex(trtName,&trtIndex);
		fSread(fp,S_ignore);
		if (strcmp(S_ignore,"<ComboAndCourseDoses>") == strcmpSAME){
			fSread(fp,S_ignore);
			k=1;
			while (strcmp(S_ignore,"</ComboAndCourseDoses>") != strcmpSAME){
				trtDose=atof(S_ignore);
				/* ApplyRegimen part on VB side */
				if ( trtType == 4) {/* Course */
					didx = Courses[trtIndex].courseAgents[k].Index;
					for (iCourseTime=1;iCourseTime<=Courses[trtIndex].courseAgents[k].numTrtTime;iCourseTime++){
						ndoses[didx] = ndoses[didx] +1;
						CourseAppTime = trtTime+(Courses[trtIndex].courseAgents[k].TreatmentTime[iCourseTime]-1)*timeConversion(Unit2Int(TimeUnit));
						doselist[didx][ndoses[didx]] = CourseAppTime;
						ret=cside_pdf(didx,CourseAppTime,trtDose,trtidx);
					}
				}
				else {
					didx = Combos[trtIndex].cmbAgents[k].Index;
					ndoses[didx] = ndoses[didx] +1;
					doselist[didx][ndoses[didx]] = trtTime;
					ret=cside_pdf(didx,trtTime,trtDose,trtidx);
				}
				fSread(fp,S_ignore);
				k++;
			}
			fDread(fp,trtEndtime);fdumpline ( fp );
		}
		else { /* Agent */
			trtEndtime=atof(S_ignore);
			if ( trtType == 1 ) {
				didx = trtIndex;
				ndoses[didx] = ndoses[didx] +1;
				doselist[didx][ndoses[didx]] = trtTime;
				ret=cside_pdf(didx,trtTime,trtDose,trtidx);
			}
			else { /* continuous agent */
				ret=SetEnvir(trtName,trtTime,trtEndtime);
			}
		}
	}
	buildsched();
	sortsched();
}

void ReadMgmtRules(FILE *fp){
char S_ignore[SMALLBUFFER];
int nMgmtRules;
int n;
/* SendBBRules on VB side */
	fIread(fp,nRules);fdumpline ( fp );

/* ClearRules first */
	for (n=0;n<=nRules;n++)
	{
		BBRules[n].nIFClauses = 0;
		BBRules[n].nACTIONClauses = 0;
	}

	fSread(fp,S_ignore); /* read ending </PtMgmtRules-SetFcns> */
}

void ReadMgmtVars(FILE *fp){
char S_ignore[SMALLBUFFER];
int nUserStatusVars;
/* PokeStatusVars on VB side */
	fIread(fp,nUserStatusVars);fdumpline ( fp );
	fSread(fp, S_ignore); /* read ending </PtMgmtVars> */
}