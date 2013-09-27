#include "build.h"
#ifndef UNIX
#include <windows.h>
#endif

/* In order to use the main,
    filename, variablename, type(c,i,d), value, x[,y,z]
*/

#include <stdio.h>
#ifdef DLL
#include <ole2.h>
#include "phaseii.h"
#endif
#include "defines.h"
#include "classes.h"
#include "cellp.h"
#include "tox.h"
#include "timecourse.h"
#include "InducedConv.h"
#include "regimen.h"
#include "bboard.h"
#include "PlotHandler.h"
#include "logger.h"
#ifndef TESTMPI
#include <crtdbg.h>
#define VBSTRING(A) SysAllocStringByteLen(A,strlen(A))
#endif

#ifndef TESTMPI
extern double EXPORT PASCAL  peekd(variablename,x,y,z)
char *variablename;
int x,y,z;
{
	fprintf(logfile,"%s\tfunction:vbc.peekd\tvariablename:%s\tx:%d\ty:%d\tz:%d\n", gettime(),variablename,x,y,z);
		    if(strcmp(variablename,"cycletime")==0) return cycletime(x,y);
			if(strcmp(variablename,"simtime")==0) 
				return t;
			if(strcmp(variablename,"deltaT")==0)
				return delta_t;
		    if(strcmp(variablename,"doubletime")==0) return doubletime(x,y);
		    if(strcmp(variablename,"birthrate")==0) return birthrate(x,y);
		    if(strcmp(variablename,"growrate")==0) return growrate(x,y);
		    if(strcmp(variablename,"mutrate")==0) return mutrate(x,y,z);
		    if(strcmp(variablename,"timevec")==0) return timevec[x];
		    if(strcmp(variablename,"drugkill")==0) return drugkill(x,y,z);
		    if(strcmp(variablename,"timekill")==0) return timekill(x,y,z);
		    if(strcmp(variablename,"timesurv")==0) return timesurv(x,y,z);
			if(strcmp(variablename,"celltime")==0) return t;
			if(strcmp(variablename,"timecourse.t")==0) return tcoursedata[x].t;
			if(strcmp(variablename,"timecourse.pr")==0) return tcoursedata[x].pr;
			if(strcmp(variablename,"timecourse.logs")==0) return tcoursedata[x].logs;
			if(strcmp(variablename,"timecourse.avgcells")==0) return tcoursedata[x].avgcells;
			if(strcmp(variablename,"timecourse.avggtzero")==0) return tcoursedata[x].avggtzero;
			if(strcmp(variablename,"cellcount")==0) 
			{
				//xx = AddQIndex(x, iCellQHead);
				return CNsave.cellcount[x];
			}
	     	if (strcmp(variablename,"eventtime") == 0) 
			{
				//xx = AddQIndex(x, iEventQHead);
				return(Event_List[0].time);
			}
			/* for phaseII design */
			if(strcmp(variablename,"r1")==0) return r1_Final;
			if(strcmp(variablename,"n1")==0) return n1_final;
			if(strcmp(variablename,"r")==0) return r_Final;
			if(strcmp(variablename,"n")==0) return n_final;
			if (strcmp(variablename,"dfatanytime")==0)
			{
				return(TrialInfo.Regimen.Treatments[1].dose);
			}
			return ((double)-99.0);
}

extern int  EXPORT PASCAL poked(variablename,value,a,b,c)
char *variablename;
double value,a,b,c;
{
	int x,y,z;
	x=(int) a;
	y=(int) b;
	z=(int) c;
	fprintf(logfile,"%s\tfunction:vbc.poked\tvariablename:%s\tvalue:%12f\ta:%12f\tb:%12f\tc:%g\n", gettime(),variablename,value,x,y,z);

   if (strcmp(variablename,"doubletime")==0)
   {
      doubletime(x,y)=value;
	  return(0);
	  }
   else if (strcmp(variablename,"cycletime")==0)
   {
      cycletime(x,y)=value;
	  return(0);														        
	  }
  else if (strcmp(variablename,"birthrate")==0)
  {
      birthrate(x,y)=value;
	  return(0);
  }
   else if (strcmp(variablename,"deathrate")==0)
   {
     deathrate(x,y)=value;
	 return(0);
   }
   else if (strcmp(variablename,"growrate")==0)
   {
     growrate(x,y)=value;
	 return(0);
   }
   else if (strcmp(variablename,"mutrate")==0)
   {
	 mutrate(x,y,z) =value; /*mutrate[x][y][z]=value;*/
	 return(0);
   }
   else if (strcmp(variablename,"toxprob")==0)
   {
	   Agents[x].toxicity[y].toxProbs[z] = value;
	   return(0);
   }
   else if (strcmp(variablename,"toxrestime")==0)
   {
	   Agents[x].toxicity[y].toxResTime = value;      
	   return(0);
   }
   else if (strcmp(variablename,"deltaT")==0)
   {
	   delta_t = value;
	   return(0);
   }
   else if (strcmp(variablename,"condtime")==0)
   {
	   condition[x].t = value;
	   return(0);
   }
      else if (strcmp(variablename,"InitTotal")==0)
   {
	   InitMean = value; 	   
	   return(0);
   }
   else if (strcmp(variablename,"CV")==0)
   {
	   CoVar = value;
	   return(0);
   }
   else if (strcmp(variablename,"initCellVector")==0)
   {
	   initCellVector[x] = value;
	   LookUpId[x] = (unsigned int) b;
	   return(0);
   }   
   else if (strcmp(variablename,"cttttime")==0)
   {
	   TrialInfo.Regimen.Treatments[x].time = value;
	   return(0);
   }
   else if (strcmp(variablename,"ctttdose")==0)
   {
	   TrialInfo.Regimen.Treatments[x].dose = value;
	   return(0);
   }
   else if (strcmp(variablename,"ctttoxreduction")==0)
   {
	   TrialInfo.ToxCriteria[x].Reduction = value;
	   return(0);
   }
   else if (strcmp(variablename,"ctmaxprogression")==0)
   {
	   TrialInfo.MaxProgression = value;
	   return(0);
   }
   else if (strcmp(variablename,"doselist")==0)
   {
	   if (value < 0)
	   {
		   ndoses[x] = 0;
	   }
	   else
	   {
		   ndoses[x] = ndoses[x] +1;
		   doselist[x][ndoses[x]] = value;
		   TreatmentIdxList[x][ndoses[x]] = y;
		   if (y > 2)
			   MsgBoxWarning("OVER2");
	   }
	   return(0);
   }
   else if (strcmp(variablename,"ICprob")==0)
   {
	   icRules[nICrules].Prob = value;
	   return(0);
   }
   else if (strcmp(variablename,"treatapptime")==0)
   {
	   Regimens[x].Treatments[y].Time = value;
	   return(0);
   }
   else if (strcmp(variablename,"treatappdose")==0)
   {
	   Regimens[x].Treatments[y].Dose = value;
	   return(0);
   }
   else if (strcmp(variablename,"treatappfulldose")==0)
   {
	   Regimens[x].Treatments[y].FullDose = value;
	   return(0);
   }
   else if (strcmp(variablename,"regstarttime")==0)
   {
	   Regimens[x].startTime[y] = value;
	   return(0);
   }
   else if (strcmp(variablename,"cmbagentdose")==0)
   {
	   Combos[x].cmbAgents[y].Dose = value;
	   return(0);
   }
   else if (strcmp(variablename,"cmbagentapptimes")==0)
   {
	   Combos[x].cmbAgents[y].AppTimes[z] = value;
	   return(0);
   }
   else if (strcmp(variablename,"courseagentdose")==0)
   {
	   Courses[x].courseAgents[y].Dose = value;
	   return(0);
   }
   else if (strcmp(variablename,"courseagentapptimes")==0)
   {
	   Courses[x].courseAgents[y].AppTimes[z] = value;
	   return(0);
   }
   else if (strcmp(variablename,"statusvarinitval")==0)
   {
	   UserAddedVars[x].Value = value;
	   return(0);
   }

   else return(1);
}

void CalculateTimeKillSurv();

extern int  EXPORT PASCAL pokedosefactor(drugindex,time,fraction,treatidx)
int drugindex;
double time,fraction;
double treatidx;
{
	fprintf(logfile,"%s\tfunction:vbc.pokedosefactor\tdrugindex:%d\ttime:%12f\tfraction:%12f\ttreatidx:%12f\n",gettime(),drugindex,time,fraction,treatidx);
	return(cside_pdf(drugindex,time,fraction,(int)treatidx));
}
#endif

int cside_pdf(drugindex,time,fraction,treatidx)
int drugindex;
double time,fraction;
int treatidx;
{
	int idose;
	double dround(double num, double places);
	double schedcomp, timecomp;
	timecomp = dround(time,6.0);
	for (idose = 1;idose <= ndoses[drugindex];idose++)
	{
		schedcomp = dround(doselist[drugindex][idose],6.0);
		if (schedcomp >= timecomp - 1e-9 && schedcomp <= timecomp + 1e-9)
		{
			dosefactorlist[drugindex][idose] = fraction;
			if (treatidx > 2)
			{

				MsgBoxWarning("treatidx > 2");
			}
			TreatmentIdxList[drugindex][idose] = treatidx;

		}
	}
/*	buildsched();
	sortsched(); */
	CalculateTimeKillSurv();
/*
*/
	/*for (isched = 1;isched <= nsched;isched++)
	{
		schedcomp = dround(sched[isched].t,6.0);
		if ((sched[isched].d == drugindex) && (schedcomp >= timecomp - 1e-9 && schedcomp <= timecomp + 1e-9))
		{
			sched[isched].df = fraction;
			CalculateTimeKillSurv();
			return(1);
		}
	}*/
	return(0);
}
#ifndef TESTMPI
extern int  EXPORT PASCAL CTChangeAllDoses (drugindex,fraction)
int drugindex;
double fraction;
{
	int isched;
	int found;
	
	found = 0;

	for (isched = 1;isched <= nsched;isched++)
	{
		if (sched[isched].d == drugindex) 
		{
			sched[isched].df = fraction;
			found = 1;
		}
	}
	CalculateTimeKillSurv();

	for (isched = 1; isched <= TrialInfo.Regimen.numTreatments; isched ++)
	{
		if ( TrialInfo.Regimen.Treatments[isched].TreatIndex == drugindex)
		{
			TrialInfo.Regimen.Treatments[isched].dose = fraction;

		}
	}
	return(found);
}


extern double  EXPORT PASCAL peekdfatanytime(double drugind)
{
	int isched; 
	int drugindex;
	fprintf(logfile,"%s\tfunction:vbc.peekdfatanytime\tdrugind:%12f\n", gettime(),drugind);
	drugindex = (int) drugind;
	for (isched = 1;isched <= TrialInfo.Regimen.numTreatments;isched++)
	{
		if (TrialInfo.Regimen.Treatments[isched].TreatIndex == drugindex)
		{
			return(TrialInfo.Regimen.Treatments[isched].dose);
		}
	}
	return(0.0);
}

extern double  EXPORT PASCAL peekdosefactor(drugindex,time)
int drugindex;
double time;
{
	int isched;
    fprintf(logfile,"%s\tfunction:vbc.peekdosefactor\tdrugindex:%12f\ttime:%d\n", gettime(),drugindex,time);
	for (isched = 1;isched <= nsched;isched++)
	{
		if ((sched[isched].d == drugindex) && (sched[isched].t == time)) return(sched[isched].df);
	}
	return(0.0);
}


extern int EXPORT PASCAL  peeki(variablename,x,y,z)
char *variablename;
int x,y,z;
{
	fprintf(logfile,"%s\tfunction:vbc.peeki\tvariablename:%s\tx:%d\ty:%d\tz:%d\n", gettime(),variablename,x,y,z);
	    if(strcmp(variablename,"ntypes")==0) return (ntypes);
	    if(strcmp(variablename,"Number_of_Classes")==0) return (Number_of_Classes);
	    if(strcmp(variablename,"no_levels")==0) return (Class[x].no_levels);
		if(strcmp(variablename,"nallts")==0) return (nallts);
		if(strcmp(variablename,"nevents") == 0)  
			return(nEvents);
		if(strcmp(variablename,"eventtype") == 0)  
			return(Event_List[0].event);
/*		if(strcmp(variablename,"head") == 0)
			return( iCellQHead );						
		if(strcmp(variablename,"ntypesnow")==0) return (CNsave.ntypes);*/
		if(strcmp(variablename,"ntcoursedata")==0) return (ntcoursedata);
		if(strcmp(variablename,"ranseed1")==0) return ranSeed1;
		if(strcmp(variablename,"ranseed2")==0) return ranSeed2;
		if(strcmp(variablename,"ranseed3")==0) return ranSeed3;
		if(strcmp(variablename,"numplotsubset")==0) return (numPlotSubset);
		if(strcmp(variablename,"numplotproperty")==0) return (numPlotProperty);
		if(strcmp(variablename,"plotsubsetclass")==0) return (PlotSubset[x].classIdx);
		if(strcmp(variablename,"plotsubsetlevel")==0) return (PlotSubset[x].levelIdx);
		if(strcmp(variablename,"plotproperty")==0) return PlotProperty[x];
		if(strcmp(variablename,"numplotlines")==0) return numPlotLines;
		if(strcmp(variablename,"subsetoption")==0) return SubsetOption;
		if(strcmp(variablename,"plotoption")==0) return PlotOption;
		return (-99);
}

extern int  EXPORT PASCAL pokei(variablename,val,a,b,c)
char *variablename;
double val;
double a,b,c; 
{
	int value;
	int x,y,z;
	fprintf(logfile,"%s\tfunction:vbc.pokei\tvariablename:%s\tval:%12f\ta:%12f\tb:%12f\tc:%12f\n",gettime(),variablename,val,a,b,c);
	x=(int) a;
	y=(int) b;
	z=(int)c;
	value = (int) val;
    
	if (strcmp(variablename,"is")==0)
	{
      is[x]=value;
	  return(0);
	}
	else if (strcmp(variablename,"toxapptype") == 0)
	{
	   Agents[x].toxicity[y].toxAppType = value;
	   return(0);
	}
	else if (strcmp(variablename,"toxrestype") == 0)
	{
 	   Agents[x].toxicity[y].toxResType = value;
	   return(0);
	}
	else if (strcmp(variablename,"toxtypeindex") == 0)
	{
	   Agents[x].toxicity[y].toxTypeIndex = value;
	   return(0);
	}
	else if (strcmp(variablename,"numtoxttypes") == 0)
	{
	   Agents[x].numToxTypes = value;
	   return(0);
	}
	else if (strcmp(variablename,"init_active_ntypes") == 0)
	{
		  init_active_ntypes = value;
//		  if (initCellVector != NULL)
//				free(initCellVector);
		  initCellVector = (double *)calloc(init_active_ntypes+1,(sizeof(double)));
		  return(0);
	}
	else if (strcmp(variablename, "printgap") == 0)
	{
		PrintGap = value;
		return(0);
	}
	else if (strcmp(variablename,"ctcourselength")==0)
   {
	   TrialInfo.CourseLength = value;
	   return(0);
   }
   else if (strcmp(variablename,"ctnumtoxmodifications")==0)
   {
	   TrialInfo.numToxModifications = value;
	   return(0);
   }
	else if (strcmp(variablename,"ctmaxcourses")==0)
   {
	   TrialInfo.MaxCourses = value;
	   return(0);
   }   
	else if (strcmp(variablename,"ctprogressiononnewmets")==0)
   {
	   TrialInfo.ProgressionOnNewMets = value;
	   return(0);
   }
	else if (strcmp(variablename,"ctoffstudygrade")==0)
   {
	   TrialInfo.OffStudyGrade = value;
	   return(0);
   }
	else if (strcmp(variablename,"ctn1")==0)
   {
	   TrialInfo.n1 = value;
	   return(0);
   }
	else if (strcmp(variablename,"ctr1")==0)
   {
	   TrialInfo.r1 = value;
	   return(0);
   }
	else if (strcmp(variablename,"ctn")==0)
   {
	   TrialInfo.n = value; 
	   return(0);
   }
	else if (strcmp(variablename,"ctr")==0)
   {
	   TrialInfo.r = value;
	   return(0);
   }	
	else if (strcmp(variablename,"ctnumtreatments")==0)
   {
	   TrialInfo.Regimen.numTreatments = value;
	   return(0);
   }
	else if (strcmp(variablename,"cttttype")==0)
   {
	   TrialInfo.Regimen.Treatments[x].Type = value;
	   return(0);
   }
	else if (strcmp(variablename,"cttttreatindex")==0)
   {
	   TrialInfo.Regimen.Treatments[x].TreatIndex = value;
	   return(0);
   }
	else if (strcmp(variablename,"ctttoxreductiongrade")==0)
   {
	   TrialInfo.ToxCriteria[x].ReductionGrade = value;
	   return(0);
   }
	else if (strcmp(variablename,"ctttoxreductiontype")==0)
   {
	   TrialInfo.ToxCriteria[x].ReductionType = value;
	   return(0);
   }
	else if (strcmp(variablename,"trialsim")==0)
   {
	   TrialSim = value;
	   return(0);
   }
	else if (strcmp(variablename,"ICifclassindex")==0)
	{
		icRules[nICrules].ifClass[x].classIndex =value;	   
		return(0);
	}
	else if (strcmp(variablename,"ICiflevelindex")==0)
	{
		icRules[nICrules].ifClass[x].levelIndex =value;	   
		return(0);
	}
	else if (strcmp(variablename,"ICsourceclassindex")==0)
	{
		icRules[nICrules].Source.classIndex =value;	   
		icRules[nICrules].Dest.classIndex =value;	   
		return(0);
	}
	else if (strcmp(variablename,"ICsourcelevelindex")==0)
	{
		icRules[nICrules].Source.levelIndex =value;	   
		return(0);
	}
	else if (strcmp(variablename,"ICdestlevelindex")==0)
	{
		icRules[nICrules].Dest.levelIndex =value;	   
		return(0);
	}
	else if (strcmp(variablename,"ICagentindex")==0)
	{
		icRules[nICrules].agentIndex =value;	   
		return(0);
	}
	else if (strcmp(variablename,"ICincnicrules")==0)
	{
		nICrules=nICrules+1;
		return(0);
	}
	else if (strcmp(variablename,"ICinitnicrules")==0)
	{
		nICrules=0;
		return(0);
	}
	else if (strcmp(variablename,"ICnclassandlevels")==0)
	{
		icRules[nICrules].nClassAndLevels = value;
		return(0);
	}
	else if (strcmp(variablename,"treattreatindex")==0)
	{
		Regimens[x].Treatments[y].TreatIndex = value;
		return(0);
	}
	else if (strcmp(variablename,"treattreattype")==0)
	{
		Regimens[x].Treatments[y].TreatType = value;
		return(0);
	}
	else if (strcmp(variablename,"treatcourseindex")==0)
	{
		Regimens[x].Treatments[y].CourseIndex = value;
		return(0);
	}
	else if (strcmp(variablename,"regtreatindex")==0)
	{
		Regimens[x].treatIndex[x] = value;
		return(0);
	}
	else if (strcmp(variablename,"regnumtreatapps")==0)
	{
		Regimens[x].numTreatmentApplications = value;
		return(0);
	}
	else if (strcmp(variablename,"combonumagents")==0)
	{
		Combos[x].numAgents = value;
		return(0);
	}
	else if (strcmp(variablename,"coursenumagents")==0)
	{
		Courses[x].numAgents = value;
		return(0);
	}
	else if (strcmp(variablename,"courseagentindex")==0)
	{
		Courses[x].AgentIndex[y] = value;
		return(0);
	}
	else if (strcmp(variablename,"cmbagentsindex")==0)
	{
		Combos[x].cmbAgents[y].Index = value;
		return(0);
	}
	else if (strcmp(variablename,"cmbagentsnumapps")==0)
	{
		Combos[x].cmbAgents[y].numApps = value;
		return(0);
	}
	else if (strcmp(variablename,"courseagentsindex")==0)
	{
		Courses[x].courseAgents[y].Index = value;
		return(0);
	}
	else if (strcmp(variablename,"courseagentsnumapps")==0)
	{
		Courses[x].courseAgents[y].numApps = value;
		return(0);
	}
	else if (strcmp(variablename,"statusvartype")==0)
	{
		UserAddedVars[x].varType = value;
		return(0);
	}
	else if (strcmp(variablename,"nuserstatusvars")==0)
	{
		nUserAddedVars = value;
		return(0);
	}
	else if (strcmp(variablename,"usersetseeds")==0)
	{
		UserSetSeeds = value;
		return(0);
	}
   else if (strcmp(variablename,"ranseed1")==0)
   {
	   ranSeed1 = value;
	   return(0);
   }
   else if (strcmp(variablename,"ranseed2")==0)
   {
	   ranSeed2 = value;
	   return(0);
   }
   else if (strcmp(variablename,"ranseed3")==0)
   {
	   ranSeed3 = value;
	   return(0);
   }
    else if (strcmp(variablename,"nconds")==0)
   {
	   nconds = value;
	   return(0);
   }
   else if (strcmp(variablename,"numplotsubset")==0)
   {
	   numPlotSubset = value;
	   return(0);
   }
   else if (strcmp(variablename,"numplotproperty")==0)
   {
	   numPlotProperty = value;
	   return(0);
   }
   else if (strcmp(variablename,"plotsubsetclass")==0)
   {
	   if (numPlotSubset < (unsigned int)x)
	   {
		   numPlotSubset = (unsigned int)x;
	   }
	   PlotSubset[x].classIdx = value;
	   return(0);
   }
   else if (strcmp(variablename,"plotsubsetlevel")==0)
   {
	   PlotSubset[x].levelIdx = value;
	   return(0);
   }
   else if (strcmp(variablename,"plotproperty")==0)
   {
	   if ( numPlotProperty < (unsigned int)x) 
	   {
		   numPlotProperty = (unsigned int) x;
	   }
	   PlotProperty[x] = value;
	   return(0);
   }
   else if (strcmp(variablename,"plotoption")==0)
   {
	   PlotOption = value;
	   return(0);
   }
   else if (strcmp(variablename,"subsetoption")==0)
   {
	   SubsetOption = value;
	   return(0);
   }
   else if (strcmp(variablename,"cellpitime")==0)
   {
	   cellpitime = value;
	   return(0);
   }
   else return(1);
}

extern BSTR EXPORT PASCAL peekc(char *variablename,int x, int y, int z)
{
	fprintf(logfile,"%s\tfunction:vbc.peekc\tvariablename:%s\tx:%d\ty:%d\tz:%d\n", gettime(),variablename,x,y,z);
	    if(strcmp(variablename,"class_name")==0) return VBSTRING(Class[x].class_name);
	    if(strcmp(variablename,"level_name")==0) return VBSTRING(Class[x].Level[y].level_name);
		if(strcmp(variablename,"plotlinename")==0) return VBSTRING(PlotLineName[x]);
				if(strcmp(variablename,"typename")==0) {
			if ( x<=MAXTYPES) return VBSTRING(typename[x]);
			else return VBSTRING(" ");
		}
		if(strcmp(variablename,"cellname")==0) return VBSTRING(cellname(x));
		if(strcmp(variablename,"eventdetail1")==0) 
			return VBSTRING(Event_List[0].detail1);
		if(strcmp(variablename,"eventdetail2")==0) 
			return VBSTRING(Event_List[0].detail2);
		if(strcmp(variablename,"eventdetail3")==0) 
			return VBSTRING(Event_List[0].detail3);
		return(VBSTRING("Error"));
}

extern int  EXPORT PASCAL pokec(variablename,value,a,b,c)
char *variablename;
char *value;
double a,b,c; 
{
	int x,y,z;
	int j;
	fprintf(logfile,"%s\tfunction:vbc.pokec\tvariablename:%s\tvalue:%s\ta:%12f\tb:%12fc:%12f\n",gettime(),variablename,value,a,b,c);
	x=(int) a;
	y=(int) b;
	z=(int) c;
   if (strcmp(variablename,"drugname")==0)
   {
      strcpy(drugname[x],value);
	  return(0);
   }
   else if (strcmp(variablename,"typename")==0)
   {
		if ( x <= MAXTYPES ) 
			strcpy(typename[x],value);
		else return (1);
	  return(0);
   }
     else if (strcmp(variablename,"cellname")==0)
   {
      strcpy(cellname(x),value);
	  return(0);
   }
   else if (strcmp(variablename,"agentname")==0)
   {
	   strcpy(Agents[x].name,value);
	   return(0);
   }
   else if (strcmp(variablename,"toxtypename")==0)
   {
	   strcpy(Agents[x].toxicity[y].name,value);
	   return(0);
   }
   else if (strcmp(variablename,"ctagentname")==0)
   {
	   strcpy(TrialInfo.Regimen.Name,value);
	   return(0);
   }
   else if (strcmp(variablename,"ctagentshortname")==0)
   {
	   strcpy(TrialInfo.Regimen.ShortName,value);
	   return(0);
   }
   else if (strcmp(variablename,"ctttname")==0)
   {
	   strcpy(TrialInfo.Regimen.Treatments[x].Name,value);
	   return(0);
   }
   else if (strcmp(variablename,"ctttoxtoxtype")==0)
   {
	   strcpy(TrialInfo.ToxCriteria[x].ToxType,value);
	   return(0);
   }
   else if (strcmp(variablename,"ICdrugenvname")==0)
   {
	   strcpy(icRules[nICrules].envName,value);	
	   if (strcmp(icRules[nICrules].envName,"NODRUG") == 0)
	   {
		   icRules[nICrules].envIndex = -1;
	   }
	   else
	   {
		   for (j= 0;j<nenvirons;j++)
		   {
			   if (strcmp(icRules[nICrules].envName,Environments[j])==0)
			   {
				   icRules[nICrules].envIndex = j;
			   }
		   }	   
	   }
	   return(0);
   }
   else if (strcmp(variablename,"regname")==0)
   {
	   strcpy(Regimens[x].Name,value);
	   return(0);
   }
   else if (strcmp(variablename,"regtextdef")==0)
   {
	   strcpy(Regimens[x].TextDef,value);
	   return(0);
   }
   else if (strcmp(variablename,"cmbagentname")==0)
   {
	   strcpy(Combos[x].cmbAgents[y].Name,value);
	   return(0);
   }
   else if (strcmp(variablename,"courseagentname")==0)
   {
	   strcpy(Courses[x].courseAgents[y].Name,value);
	   return(0);
   }
   else if (strcmp(variablename,"statusvarname")==0)
   {
	   strcpy(UserAddedVars[x].varName,value);
	   return(0);
   }
   else return(1);
}

void printvaluec(variablename,x)
char *variablename;
int x; 
{
   if (strcmp(variablename,"drugname")==0)
     printf("drugname=%s\n",drugname[x]);
   else if (strcmp(variablename,"typename")==0) {
	   if ( x <= MAXTYPES )
		 printf("typename=%s\n",typename[x]);
	}
   else if (strcmp(variablename,"cellname")==0)
      printf("typename=%s\n",cellname(x));
}

char *gettime()
{
	char *rval;
	char *pos;
	time ( &rawtime );
	timeinfo = localtime ( &rawtime );
	rval = asctime(timeinfo);
	if ((pos=strchr(rval, '\n')) != NULL)
		*pos = '\0';
	return(rval);	
}

/* void main(argc,argv)
int argc ;
char *argv[];
{
   int     ioutvalue,iinvalue,x,y,z=1;
   double  doutvalue,dinvalue;
   char     vname[100],type[2], cinvalue[50], *coutvalue;

   if ( argc>=2 )
      strcpy(vname,argv[1]);

   if ( argc>=3 )
      strcpy(type,argv[2]);

   if  (strcmp(type,"d")==0)   {
   dinvalue=atof(argv[3]);
   x=atoi(argv[4]);
   y=atoi(argv[5]);
   poked(vname,dinvalue,x,y,z);
   printf("After poked,");
   printvalued(vname,x,y,z); 
   doutvalue=peekd(vname,x,y,z);
   printf("After peekd,%s[%d]=%lf\n",vname,x,doutvalue);
    }
   else if (strcmp(type,"c")==0) { 
   strcpy(cinvalue,argv[3]);
   x=atof(argv[4]);
   pokec(vname,cinvalue,x);
   printf("After pokec,");
   printvaluec(vname,x); 
   coutvalue=peekc(vname,x);
   printf("After pokec,%s[%d]=%s\n",vname,x,coutvalue);
    }
   else if (strcmp(type,"i")==0) { 
   iinvalue=atoi(argv[3]);
   x=atoi(argv[4]);
   pokei(vname,iinvalue,x);
   printf("After pokei,");
   printvaluei(vname,x); 
   ioutvalue=peeki(vname,x);
   printf("After peeki,%s[%d]=%i\n",vname,x,ioutvalue);
    }

  
} */


#endif
