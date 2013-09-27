#include "build.h"
#include <time.h>
#define SORT

#ifndef UNIX
#include <windows.h>
#include <crtdbg.h> 
#endif
    
#ifdef TESTMPI
#define BASEMODEL
void printvalue();
#include <stdlib.h>
#endif
#include <stdio.h>    
#include "defines.h"
#include "classes.h"
#include "cellp.h"
#include "bboard.h"
#include "logger.h"
#include "tox.h"
#ifdef TEST
time_t saitime;
#endif
long Tblength;  
extern double  getcellcounts();
extern int minsearch,IsGompSumOverFlow();
int GetSumVELevel();
int Update_CN_NGomp();
int nentry;

#ifndef TESTMPI
extern 	double EXPORT PASCAL s2wrapper ( char *what, double val, int index)
{
	fprintf(logfile,"%s\tfunction:cellp.s2wrapper\twhat:%s\tval:%12f\tindex:%d\n", gettime(),what,val,index);
  	if (strcmp(what,"PeekCN") == 0)  
	{
 		return(getcellcounts(index,!VEGF));
	}
	if (strcmp(what,"PokePgfCN") == 0)  
	{
	
    		cellstart[index]=val;
		    /* index is the celltype, cellstart is for jointpgf() */
    		return(DZERO);
    }
    if (strcmp(what,"InitTox") == 0) 
    {
    	nToxTypes = 0;
    	InitToxTypes();
    	return(DZERO);
    }
#ifdef DLL    
    	MsgBoxError("Invalid Call to s2wrapper");
#endif
    	return ((double) -1); /*error*/
}
#endif
    
    /* This function returns cell population (CN) at the time t+delta_t
     * according to the cell population (CN) at time t.  CN is input and
     * output vector.
     */
    
    int NextPBin(int *penv)
    {
        double nChanged, nDied, nMitosed, nRemaining, nGone,
				nMigrationTo, MigrationPool,
     	    	nMutated, nMutatedTo, MutationPool, nConverted, nConvertedTo, ConversionPool,temp;
        int    iindex, jindex, AlteringEvent = false, anychanges;
        int    envnowl;
		unsigned int icelltype, getcelltype();
       	extern int get_dest_gap();
		extern void sort_lookuptable();
		void Gomp_UpdateTime();
       
		if (!ContinDrugRuleActive) /* Only check for an enviroment change if a BB rule isn't in effect */
			envnowl = getenvir( t );
		else
			envnowl = *penv;
		
		if (PendingEnvChange)
			CheckEnv( t, &envnowl );


		if ((KineticsModel == IsGompertz) && (t > UpdateTime)) {
			Gomp_UpdateTime(envnowl);
    		UpdateTime += UPDATEGAP*delta_t;
    	}

	   	anychanges = false;
    	pre_ntypes = active_ntypes;
   
	 for ( iindex= 1; iindex < MaxLookUp; iindex++ )
    	      nDescOfItype(iindex) = 0.0; /* set  matrix zero */
	 for ( iindex = 1; iindex <= pre_ntypes; iindex++ )
       {
          nChanged = nMutated = nDied = nMitosed = 0.0;
          if ( CN[iindex]>= 1.0 )
          {
             /* the number of cells that are mutated, died or devided */

            temp = EventProb(SomeEventProb,iindex,envnowl);
			nChanged= grand_b_n( CN[iindex],temp);
   

     		nRemaining = nChanged;
     		if ( nRemaining <= 0.0) goto NEXTCELLTYPE;
     
            anychanges = true;
            /* number of "itype" cells which died */
			temp = EventProb(GoneProb,iindex,envnowl);


            nGone = grand_b_n( nChanged,temp);

     		nDied = grand_b_n( nGone, CondProbDeath(iindex,envnowl));
     		
			nConverted = nGone - nDied;
     		ConversionPool = nConverted; 
			
			for ( jindex = 0; jindex < LookUp[iindex].ParaPr->NumOfCo; jindex++ )
     		{ 
    		   if ( ConversionPool <= 0.0)
     			 break;
    			 icelltype=getcelltype(iindex,jindex,envnowl,G0T);
    		   	 AlteringEvent = TRUE;
                 temp=CondCoTo(iindex,jindex,envnowl);
     			 nConvertedTo = grand_b_n( ConversionPool,temp);
     			 if ( nConvertedTo > 0.0 )
     			 {
					if ( (Class[0].class_type==1 ) && (labs((long) (LookUp[iindex].LookUpId-icelltype)))>= Tblength ) {
						/* A cell has converted from the ith to jth type */                        
     					if ((*OrganFunc)(iindex,icelltype,nConvertedTo,envnowl) ==False)
							return False;
					} 
					else { /* if not "organ" */
						if ( create_para_struct(icelltype,SIM) == False )
    						return False;
    					nDescOfItype(change_index) += nConvertedTo;
					}
    			
			   		if ( ConversionPool > nConvertedTo )
     					   ConversionPool = ConversionPool - nConvertedTo;
     				else
     					   ConversionPool = 0.0;
				}/* if nConverted > 0 */
     		} /* end of for loop */

			MigrationPool = ConversionPool;
			
			if ( MigrationPool > 0.0) {
				for ( jindex = 0; jindex < LookUp[iindex].ParaPr->NumOfMig; jindex++ )
     			{ 
    			  	 AlteringEvent = TRUE;
					 temp = CondMigTo(iindex,jindex,envnowl);
					 nMigrationTo = grand_b_n(	MigrationPool,temp);
					 if (nMigrationTo > 0.0 )
     				 {
						icelltype=getcelltype(iindex,jindex,envnowl,MIG);
    					if ( create_para_struct(icelltype,SIM)== False )
    						return False;
						nDescOfItype(change_index) += nMigrationTo;
     					
    					if ( MigrationPool > nMigrationTo )
     					   MigrationPool = MigrationPool - nMigrationTo;
     					else
     					   MigrationPool = 0.0;
						if ( MigrationPool  <= 0.0)
     						 break;
     			  }
     			}
			}
    		
    		nDescOfItype(iindex) -= nGone; 
     		nRemaining =  nChanged - nGone;
     		if ( nRemaining <= 0.0) goto NEXTCELLTYPE;
     
			temp = EventProb(MuProb,iindex,envnowl);
     		nMutated = grand_b_n( nRemaining, temp);
     		MutationPool = nMutated;                  /* number of cells mutated */
     		for ( jindex = 0; jindex < LookUp[iindex].ParaPr->NumOfMu; jindex++ )
     		{ 
    		   if ( MutationPool <= 0.0)
     			 break;
    			 icelltype=getcelltype(iindex,jindex,envnowl,MUTATION);
    			 AlteringEvent = TRUE;
    			 temp=CondMuTo(iindex,jindex,envnowl);
     			 nMutatedTo = grand_b_n( MutationPool,temp);
     			 if ( nMutatedTo > 0.0 )
     			 { /* A cell has mutated from the ith to jth type */   
					 if ( create_para_struct(icelltype,SIM) == False)
    						return False ;

    				 nDescOfItype(change_index) += nMutatedTo;
     								 
					 if ( MutationPool > nMutatedTo )
     					   MutationPool = MutationPool - nMutatedTo;
     				 else
     					   MutationPool = 0.0;
					 } /* if ( nMutatedTo > 0.0 ) */
     		}    /* end of for loop */     

     		nRemaining =  nChanged - nGone - nMutated;
     		if ( nRemaining <= 0.0) goto NEXTCELLTYPE;
    		
    		nDescOfItype(iindex) += nRemaining;  /* number of cells which divided */
    
     	  }
    	  NEXTCELLTYPE:  ; /* Skip to the next cell type.*/
      }
      if ( anychanges ){
     	  /* A change occurred in at least one cell (any type) */
     	  for ( iindex = 1 ; iindex <= active_ntypes; iindex++ ){
			if (Update_CN_NGomp(iindex,envnowl)== false )
				return false;
		}

		if ( (AlteringEvent == TRUE) && (verbos > 3) )
     		  eprint( "Note: at least one cell was altered (mutated or converted)\n" );
      }
  
#ifdef SORT   
	if ( pre_ntypes < active_ntypes)
		sort_lookuptable(pre_ntypes+1,active_ntypes,SIM);
#endif
   
      *penv=envnowl;
	  nowenv = envnowl;
      return True;
     }

 int Update_CN_NGomp(int iindex,int envnowl ){
 int  cn,ksumlevel;

    CN[iindex] += nDescOfItype(iindex);

		if ( KineticsModel == IsGompertz ) {
			 for ( cn=0; cn < Number_of_Gomp_Classes;cn++ ) {
				if ( LookUp[iindex].mark != NEG ) {
					/* Sum the cell counts for different sumlevels */
					ksumlevel =  SumGompLevel(cn,iindex);
					
					if ( (iindex > pre_ntypes ) && IsGompSumOverFlow(ksumlevel,cn))
						return False;
				
					NGomp(cn,ksumlevel) += nDescOfItype(iindex);
				}
			}
			/* micro class add VE and CA cells */
			cn = Number_of_Gomp_Classes;
			ksumlevel =  SumGompLevel(cn,iindex);
				
			if ( (iindex > pre_ntypes ) && IsGompSumOverFlow(ksumlevel,cn))
				return False;
		
			NGomp(cn,ksumlevel) += nDescOfItype(iindex);
		
		} /* if IsGompertz ==> sum by gompertz level */
		return true;
 }
    /*
     * Inputs:
     *    ntypes   --- the numbers of the type of cell
     *    delta_t  --- time step
     *    xi       --- mitosis rate
     *    gama     --- total culling rate
     *    mutrate  --- mutation rate
     *    T        --- simulation time interval
     * Output:
     *    CN       --- [ntypes]
     */
    
    /*********  Changes by sai 28 march 1996 *********
     *  
     *  Made cellp return the number of points that it wrote in
     *  the data file s2.out.  So it now returns nobj which is 
     *  a long.  A long in C is eq to an int in Java
     *
     **********  additions by sai 28 march 1996 *********/
    #ifdef DLL
    void cellp( LPVOID lpParam )
    #else 
    void cellp( double delta_t)
    #endif
    {
     int itype,iindex, cn,ismax=False;
     int cells_survived = 0;
     int PrintLoopVar=PrintGap;
     double bigEndT, jjj;
     int snow;
     double TotalCells;
	 int SendCellCountOneTime;
	 char stemp[255];
	 int ksumlevel;
	 int GiveTimeCount;
	 int envnowl = 0;
     extern void release_mem();
	 extern double rand01(int), ranmarm(int);
#ifdef TEST
     time_t stime;
#endif
    #ifndef DLL
       FILE *out;/* the pointer to s2.out, the interface file to Java */
    #endif
    
    #ifdef TESTMPI
   	extern int nrepetitions;
    #endif

	if (Cured == False)
	{
		SendCellCountOneTime = True;
	}
	else
	{
		SendCellCountOneTime = False;
	}

    	/*_ASSERT(0);*/
    
    /*  This func call inits the nexttoxtime variable*/
	Tblength = (long) (ntypes/Class[0].no_levels);
    InitNextToxTime();
	SetBBForPatient();		
    snow = 0;
  	if ((GiveTimeCount = 48 - (ntypes * 3)) < 0) GiveTimeCount = 0;
  
 
    /*		Changes: 12 September 1996
     *		while((timevec[itime] <= StartT) && (itime <= nallts)) itime++;
     *		<= changed to < .  This ensures that itime is no increased when timevec[itime] = startT
     */
         
         
    	/* added by Qinshou 10/25/96 for test suites */
    	
    #ifdef TEST
    	/* construct output file name  */
       
    	if ( nrepetitions == 1 ) {
			if (numTest <= 9 )
				sprintf(outName,"%s0%1d.%d.%d.0%d.out",outName,numTest,nrepetitions,(int)EndT,(int)(delta_t*1000));
			else 
				sprintf(outName,"%s%2d.%d.%d.0%d.out",outName,numTest,nrepetitions,(int)EndT,(int)(delta_t*1000));
    		out = fopen(outName,"w");
		}
    	else 
			out = fopen(outName,"a");

		if ( out==NULL )
    	{
    			printf("Error opening %s\n",outName);
  				Memory=False;
  				return;
    	}
       if ( nrepetitions ==1 )
			printvalue(out);
   #endif
    	  
      t=StartT;
      UpdateTime = 0.0;
      cellpitime = 2;
 	  nowenv = 0;

	if (KineticsModel == IsGompertz ) {

	 for ( iindex=1; iindex<=active_ntypes;iindex++) {
	
		 /* Number_of_Gomp_Classes includes gomp class for whole body */
		  for ( cn=0; cn <Number_of_Gomp_Classes; cn++ ) {
			if ( LookUp[iindex].mark != NEG ) {
				ksumlevel =  SumGompLevel(cn,iindex);
				if ( IsGompSumOverFlow(ksumlevel,cn)) {
					Memory = False;
					return;
				}
				NGomp(cn,ksumlevel) += CN[iindex]; /* GompRuleIndex=0 is for whole body */
			}
		  }

			/* micro class add VE and CA cells */
			cn = Number_of_Gomp_Classes;
			ksumlevel =  SumGompLevel(cn,iindex);
				
			if ( (iindex > pre_ntypes ) && IsGompSumOverFlow(ksumlevel,cn)){
				Memory = false;
				return;
			}

			NGomp(cn,ksumlevel) += CN[iindex];
   
		}
	}

       while((timevec[cellpitime] > t) && (timevec[cellpitime] < t + delta_t) && (cellpitime <= nallts)) cellpitime++;

          /* For each sample, the cell population is got at time EndT.    */
          bigEndT = EndT + delta_t / 2;
          while ( t < bigEndT )
          {   
 
  		  /*for speed sake, only update cell counts if patient isn't cured*/
    		  if (Cured != True)
    		  {
  				if (NextPBin(&envnowl) == False )
				{

  					release_mem(SIM);
  					Memory=False;
#ifdef DLL
  					MessageBox(NULL,"Sorry. The total number of cell types has exceeded the memory limits, Please reduce the number of levels or number of classes. Try again.","treat.dll",MB_ICONEXCLAMATION | MB_OK);
#else
					printf("Sorry. The total number of cell types has exceeded the memory limits\n");
#endif
					return;
  				}
    			if ((t >= timevec[cellpitime]  ) && ( timevec[cellpitime] < t+delta_t) && (cellpitime <= nallts))
    			{
  					for ( itype = 1; itype <= active_ntypes; itype++ )
    				{   
					    if ( timesurv(cellpitime,itype,envnowl) < 1.0 )
						{
							jjj = timesurv(cellpitime,itype,envnowl);
 							nDescOfItype(itype) = grand_b_n( CN[itype], timesurv(cellpitime,itype,envnowl) ) - CN[itype];
							if (Update_CN_NGomp(itype,envnowl)== false ){
  								release_mem(SIM);
  								Memory=False;
#ifdef DLL
  								MessageBox(NULL,"Sorry. The total number of cell types has exceeded the memory limits, Please reduce the number of levels or number of classes. Try again.","treat.dll",MB_ICONEXCLAMATION | MB_OK);
#else
								printf("Sorry. The total number of cell types has exceeded the memory limits\n");
#endif
								return ;
							}

							//should send treatment event right here
							
							//also, send plot update event
						}
					}					
					if (CheckForTreatment())
						PlotHandler();
					CheckForTox();					
    				cellpitime++;
    			 }
    			}
    
  		  /* check for any toxicity events */
/*#ifdef DLL*/
    	  CheckForTox();

		  /* call here for CheckBBRules */
		CheckBBRules();
		ResetBBEveryDeltaT();

		//do this in case the enviroment was changed by a rule.
		envnowl = nowenv;
//		CheckForTreatment();
		if ((HasGuaranteeTimeChecked == False) && (nconds >0))
		{
			CheckGuaranteeTime();
		}

		  if ( TrialSim  == False || TSFirstTreatmentHappened == True )
			  check_events();

/*#endif */
		  if (( t >= BeginPlotTime) && (t <= EndPlotTime))
		  {
 			  if ( PrintLoopVar ==  PrintGap) 
   			  {
    			  if (nrepetitions == 1)
    			  {
    		   			  if (Cured != True)
    					  {	
		/*#ifdef DLL */
    							//AddToCellQ();
							  PlotHandler();
		#ifdef DLL
    							if (SleepTime != 0)
    							{ 
    								if (SleepTime != -1)
    									Sleep(SleepTime);
    								else
    									while(SleepTime == -1)
    										Sleep(0);
    							}
		#endif    
		/*#else*/
		#ifdef TEST
		/*						fprintf(out,"%5.3e",t);*/
 							fprintf(out,"%5.3e [%d]",t, active_ntypes);
		#endif
  							for (itype = 1; itype <= active_ntypes; itype++ )
    							{
		#ifdef TEST
		/* 							fprintf(out," %5.2e",CN[itype]); */
 				fprintf(out," %5.2e (%d,%s)",CN[itype],LookUp[itype].LookUpId,cellname(itype));
				fflush(out);
		#endif
    							}
		#ifdef TEST
    						fprintf(out,"\n");
		#endif
		/*#endif*/
  						   }/* close cured */
						  if ((Cured == True) && (SendCellCountOneTime == True))
						  {
//							  AddToCellQ();
							  PlotHandler();
							  SendCellCountOneTime = False;
						  }

  					} /* close nrepetions */

				  PrintLoopVar = 1;
			  }/* printloopvar */
			  else 
					PrintLoopVar++;
		  }/*begin and end plot time */
/*#ifdef DLL*/
/*  		  if (nrepetitions == 1)
  		  {
  				if (snow++ == GiveTimeCount)
    			 {
    				 snow = 0;
    				 Sleep(0);
  				  }
  		   } */
  
		  
  		  /* check to see if patient has died, if so end simulation */

    		  if ((Dead == True) || (EndSim == True)) 
    		  {
	  			  release_mem(SIM);
    	  	      return;
    	      }
/* #endif */
#ifdef TEST
		if ( nrepetitions == 1 ) {
			  if (( t >= 5.0 && t < 5.0 + delta_t ) || 
				  ( t >= 10.0 && t < 10.0 + delta_t ) || 
				  ( t >= 15.0 && t < 15.0 + delta_t ) || 
				  ( t >= 20.0 && t < 20.0 + delta_t ) || 
				  ( t >= 25.0 && t < 25.0 + delta_t ) || 
				  ( t >= 30.0 && t < 30.0 + delta_t ) || 
				  ( t >= 31.0 && t < 31.0 + delta_t ) || 
				  ( t >= 32.0 && t < 32.0 + delta_t ) || 
				  ( t >= 33.0 && t < 33.0 + delta_t ) || 
				  ( t >= 34.0 && t < 34.0 + delta_t ) || 
				  ( t >= 35.0 && t < 35.0 + delta_t ) || 
				  ( t >= 36.0 && t < 36.0 + delta_t ) ||
				  ( t >= 37.0 && t < 37.0 + delta_t ) || 
				  ( t >= 38.0 && t < 38.0 + delta_t ) ||
				  ( t >= 39.0 && t < 39.0 + delta_t ) || 
				  ( t >= 40.0 && t < 40.0 + delta_t ) || 
				  ( t >= 41.0 && t < 41.0 + delta_t ) || 
				  ( t >= 42.0 && t < 42.0 + delta_t ) || 
				  ( t >= 43.0 && t < 43.0 + delta_t ) || 
				  ( t >= 44.0 && t < 44.0 + delta_t ) || 
				  ( t >= 45.0 && t < 45.0 + delta_t ))
			  {
				  printf("At time %lf cell types = %d systime %d\n",t,active_ntypes,time(&stime)-saitime);
			  }
		}
#endif

		
    		  t = t+delta_t;
 
     }
    
#ifdef TEST
  	if ((GuaranteeReset == false) && ( nrepetitions > 1 )) {
		if ( ntypes  > 10 ) {
  			 for (iindex =1;iindex<= active_ntypes;iindex++) 
  	 			  fprintf(out,"%e ",CN[iindex]);
  			 fprintf(out,"\n");
		}
		else { 
	/* for pgf test, get rearranged output including zero cell counts */
			 for (iindex =1;iindex<= active_ntypes;iindex++) {
				  itype = iindex-1;
				  do {
					  itype++;
					  if ((int)LookUp[iindex].LookUpId == itype )
  	 					 fprintf(out,"%e ",CN[itype]);
					  else  fprintf(out,"%e ",ZERO);
				 } while ((int)LookUp[iindex].LookUpId != itype);
			 }

			 for (iindex = LookUp[active_ntypes].LookUpId+1;iindex<=(int)ntypes ;iindex++) 
				 fprintf(out,"%e ",ZERO);
  			 fprintf(out,"\n");
		}
  	}
    	fclose(out);
#endif
/*#ifdef DLL*/
  	/* if patient didn't die and has cells less than diagnosis, then NED event */
		if (GuaranteeReset == False)
		{
			TotalCells = AllCells();
  			sprintf(stemp,"%e",TotalCells);
    		/* if(TotalCells < diagnosis_threshold)*/
			if (DiagnosedNow == False )
    		{
  			/*if patient didn't die and has cells less than diagnosis, 
  			// then an end of followup with NED event */
  			AddToEventQ((t-delta_t),EFUNEDEVENT,stemp,".",".");
  			}
			else
  			{
  			/* Patient didn't die, and had a diagnosable tumor
  			// so add an end of follow up event with tumor */
  			AddToEventQ((t - delta_t),EFUTUMEVENT,stemp,".",".");
			}
			
			/*//if a CR or PR is pending, but the simulation ended before it couldn't 
			//be checked after the TumorExamInterval is up, post it now.*/
			if (CompleteResponse.checked == True)
				AddToEventQ(CompleteResponse.time,CREVENT,".",".",".");
			else if (PartialResponse.checked == True)
				AddToEventQ(PartialResponse.time,RESPONSEEVENT,".",".",".");
		}
/*	//	else
	//		AddToEventQ((t - delta_t),NORESPONSEEVENT,".",".",".");*/

/*#endif */	
    	SimRunning = False;
		release_mem(SIM);
 return;
}
    
 int d_is_zero( inp)
    double inp;
 {
    	/* INFINITESIMAL = 1e-12;*/
    	if ((inp < 1e-12) && (inp > (-1.0 * 1e-12))) return 1;
    	else return 0;
 }
    
 void printvalue(outputfile)
    FILE *outputfile;
    {
         int i;
    
    	 fprintf(outputfile,"delta_t = %f \n", delta_t);
    	 fprintf(outputfile,"startT = %f \n", StartT);
    	 fprintf(outputfile,"endT = %f \n", EndT);
    	 fprintf(outputfile,"PrintGap=%d\n",PrintGap);
    	 fprintf(outputfile,"ntypes=%d\n\n",ntypes);
    	 
       fprintf(outputfile,"Time Point [active_ntypes] ");
    
  	 if ( ntypes <= 8 ) {
    		 for ( i=1; i<=(int) ntypes; i++) 
    			 fprintf(outputfile,"   Type %d  ",i);
    		 fprintf(outputfile,"\n");
    	 }
  	 else fprintf(outputfile,"Cell Counts ( celltype )\n");
  
  }
  
void compute_para(int iindex,int envnowl)
{

	Setup_Probability(iindex,envnowl);
	if (KineticsModel == IsGompertz )
		Setup_Gompertzian(iindex,envnowl);
	else 
		compute_probability(iindex,envnowl);
	
}

void compute_probability(itype,envnowl)
	int itype,envnowl;
{
 	double RateSum;

	if (KineticsModel == IsGompertz) {
		Update_Gompertzian(itype,envnowl);
	}

	  RateSum = mu(itype,envnowl) + xi(itype,envnowl) + gama(itype,envnowl);

	  /* pr of some event happening =1-exp(-rate * time) */
	  EventProb(SomeEventProb,itype,envnowl) = 1.0 - exp( -RateSum * delta_t );

	  /* conditional pr of death or conversion */
	  if ( RateSum > 0.0 ) {
			EventProb(GoneProb,itype,envnowl) = 
				gama(itype,envnowl) / RateSum;
	  }
	  else {
			EventProb(GoneProb,itype,envnowl)=0.0;
	  }
	  
	  /* pr of mutation given event but no death = pr of mutation /(1-pr of death) */
	  if ( (RateSum - gama(itype,envnowl) ) > 0.0 ) {
		EventProb(MuProb,itype,envnowl) =mu(itype,envnowl) / 
					( RateSum - gama(itype,envnowl) );
	  }
	  else {
		EventProb(MuProb,itype,envnowl)=0.0;
	  }
}

void Setup_Probability(iindex,envnowl)
	int iindex,envnowl;
{
   int jindex;
   double temp_mutrate,temp_mutsum,temp_convrate,temp_migrate,temp_gamasum,temp;
    
  	  temp_mutsum = 0.0;
	  for ( jindex = LookUp[iindex].ParaPr->NumOfMu-1; jindex >= 0 ; jindex-- ) {
			temp_mutrate = mutrate(iindex,jindex,envnowl);
			temp_mutsum += temp_mutrate;
			/* partial sum of the mutation */
			if ( temp_mutsum > 0.0 ) {
              temp=temp_mutrate / temp_mutsum;
              CondMuTo(iindex,jindex,envnowl)=temp;
            }
			else
			  CondMuTo(iindex,jindex,envnowl)=0.0;
		 }
		 	  
	  temp_migrate = migrate(iindex,envnowl);

	  temp_gamasum = 0.0;
	  
	  for ( jindex = LookUp[iindex].ParaPr->NumOfMig-1; jindex >= 0 ; jindex-- ) {
			temp_gamasum += temp_migrate;
			if ( temp_gamasum > 0.0 )
			   CondMigTo(iindex,jindex,envnowl)=temp_migrate / temp_gamasum;
			else
			   CondMigTo(iindex,jindex,envnowl)=0.0;
	 }
		
	  temp_gamasum = temp_migrate;

	  for ( jindex = LookUp[iindex].ParaPr->NumOfCo-1; jindex >= 0 ; jindex-- ) {
			temp_convrate = convrate(iindex,jindex,envnowl);
			temp_gamasum += temp_convrate;
			if ( temp_gamasum > 0.0 )
			   CondCoTo(iindex,jindex,envnowl)=temp_convrate / temp_gamasum;
			else
			   CondCoTo(iindex,jindex,envnowl)=0.0;
		  }
	
	temp_gamasum += deathrate(iindex,envnowl);
	if (temp_gamasum > 0.0 )
		  CondProbDeath(iindex,envnowl) = deathrate(iindex,envnowl) / temp_gamasum ;
	else 
		  CondProbDeath(iindex,envnowl) = 0.0;
}

void Setup_Gompertzian(itype,envnowl)
	int itype,envnowl;
{
    void SetGompRuleIndex(),inv_log_gp();

/*	For now, no modification for individual celltypes.
*   MsgBoxBreak("Setup_Gompertzian",1);
*/
	birthrate0(itype,envnowl) =  birthrate(itype,envnowl);
	xi0(itype,envnowl) =  xi(itype,envnowl);
	gama0(itype,envnowl) = gama(itype,envnowl);
	if  ( xi(itype,envnowl) > 0.0 ) {
		xi_gama1(itype,envnowl)=1 - gama(itype,envnowl)/xi(itype,envnowl);
	}
	else  xi_gama1(itype,envnowl)=0.0;
	if ( gama(itype,envnowl) > 0.0){
		xi_gama2(itype,envnowl)= xi(itype,envnowl)/gama(itype,envnowl)-1;
	}
	else xi_gama2(itype,envnowl)=0.0;

	
	SetGompRuleIndex(change_index,envnowl);
	inv_log_gp(envnowl);
	
}


void inv_log_gp(int envnowl)
{
int iruleindex;
   for ( iruleindex=0; iruleindex <= NumGompRules;iruleindex++) 
	    if (GompRule[iruleindex][envnowl].gp_or_inv.GP >= 2 )
			GompRule[iruleindex][envnowl].gp_or_inv.inv_log_gp = 1.0/log(GompRule[iruleindex][envnowl].gp_or_inv.GP);
	    else GompRule[iruleindex][envnowl].gp_or_inv.inv_log_gp = GompRule[iruleindex][envnowl].gp_or_inv.GP;

}

int GetGompcn(int cn,int lnum)
{
	int k,gompcn=0,firstcn=true,envnowl=0;

	for ( k=1; k<= NumGompRules; k++ ){
		 if (GompRule[k][envnowl].cn != GompRule[k-1][envnowl].cn ){ /* if cn of this rule is different from that of the last rule */
			gompcn++;
		 }
		 if ((GompRule[k][envnowl].cn==cn ) && ((GompRule[k][envnowl].level == lnum)||((GompRule[k][envnowl].level >= lnum)&&(cn==1))) )  {
			return gompcn;
		 }
   }
   return 0;
}
 
/***************
SetGompRuleIndex()
input:
	iindex -- an index to LookUp table;
function:
	set GompRuleIndex and SumGomplevel for iindex over Gompertz class
	set LookUp[].mark, -1 ==> normal cell
					    0 ==> cancer cell
******************/

void SetGompRuleIndex(int iindex)
{
   int cn, gomp_cn,si;
   int GetGompRuleIndex(), GetSumGompLevel();
/* minsearch = min ( 3, Number_of_Classes)			*/
   for ( cn=0; cn < minsearch; cn++ ) {
		si = getcelllevelbyclass(LookUp[iindex].LookUpId,cn);
		gomp_cn = GetGompcn(cn,si); /* get class index with gomp rule */
		GompRuleIndex(gomp_cn,iindex) = GetGompRuleIndex(cn,si); /* get GompRuleIndex from GompRule */
		SumGompLevel(gomp_cn,iindex) = GetSumGompLevel(cn,si,iindex);
   }
 }

void Update_Gompertzian(itype,envnowl)
	int itype,envnowl;
{
	int j,ksumlevel,cn;
	double Rj,Qj;

	if ( ( LogNGomp(0,0)> 0.0) && ((GompRule[0][envnowl].gp_or_inv.inv_log_gp == PROPGOMP) || ((GompRule[0][envnowl].gp_or_inv.inv_log_gp> 0 ) && ( GompRule[0][envnowl].gp_or_inv.inv_log_gp <= INV_LN2)))){  /*  GompRule[0].GP=0 ==> no GompRule for whole body */
		 Rj = max(0.0,(1.0-xi_gama1(itype,envnowl) * LogNGomp(0,0)* GompRule[0][envnowl].GS));
		 Qj = max(0.0,(1.0+xi_gama2(itype,envnowl) * LogNGomp(0,0)*(1-GompRule[0][envnowl].GS)));
	}
	else {
		Rj=1.0;
		Qj=1.0;
	}

	for ( cn=0; cn <= Number_of_Gomp_Classes; cn++ ) {
	  
	   j= GompRuleIndex(cn,itype);
	   ksumlevel= SumGompLevel(cn,itype);
	   if ( ((j > 0) && ( LogNGomp(cn,ksumlevel) > 0.0)) && ( (GompRule[j][envnowl].gp_or_inv.inv_log_gp == PROPGOMP )|| (( GompRule[j][envnowl].gp_or_inv.inv_log_gp > 0 ) && ( GompRule[j][envnowl].gp_or_inv.inv_log_gp <= INV_LN2)))){ /* not for whole body */
		if ((deathrate(itype,envnowl)> 0.0 ) && ( GompRule[j][envnowl].GS < 1.0 )) {
			 Rj *= 1.0 - xi_gama1(itype,envnowl) * LogNGomp(cn,ksumlevel)* GompRule[j][envnowl].GS;
			 Qj *= 1.0 + xi_gama2(itype,envnowl) * LogNGomp(cn,ksumlevel)*(1-GompRule[j][envnowl].GS);
		}
		else {
			 Rj *= 1.0 - xi_gama1(itype,envnowl) * LogNGomp(cn,ksumlevel);
			 Qj *= 1.0;
		}
		}
	 }
   
	/*  CHECK ALL THIS!!!  kinetics were redefined 1-18-99 by QS and RD */
	birthrate(itype,envnowl) = birthrate0(itype,envnowl) * Rj;
	xi(itype,envnowl) = xi0(itype,envnowl) * Rj;
	gama(itype,envnowl) = gama0(itype,envnowl)* Qj;
	if ( xi(itype,envnowl) < 0.0 ) {
		/* Force the death rate > 0.0 and take a larger value */
		gama(itype,envnowl) = max(gama(itype,envnowl),(- xi(itype,envnowl)));
		xi(itype,envnowl)=0.0;
	}
	
	mu(itype,envnowl)= birthrate(itype,envnowl) * mutsum(itype,envnowl);
} 

/************
GetGompRuleIndex()
input:
	cn -- Class index
	lnum --level index within this class cn
output: 
	an index to GompRule
function:
	get index for a specified class and level,and GompRuleIndex=0 
	is for whole body 
**************/
int GetGompRuleIndex(cn,lnum)
int cn,lnum;
{
   int k,ienv=0;
  
   for ( k=1; k<= NumGompRules; k++ ){
		 if ( (GompRule[k][ienv].cn==cn) && ((GompRule[k][ienv].level == lnum)||((GompRule[k][ienv].level >= lnum)&&(cn==1))))  {
		 	  return ( k );
	     }
  }
   return ( 0 );
}

int is_match_control_level(int j, int iindex)
{
	int C,L,k,ienv=0;

	for (k=0;k < GompRule[j][ienv].nclevel;k++) {
		C = GompRule[j][ienv].cc[k].cclass;
		L= getcelllevelbyclass(iindex,C);
		if ( L == -1 ) return false;
		if  (L!= GompRule[j][ienv].cl[k].clevel)
			return false;
	}

	return true;
}

/**************
GetSumGompLevel()
input:
	cn -- Class index
	lnum -- level index
	iindex -- index to LookUp table
output: index to indicate which level cell counts should be added
function: If current class(cn) and level(lnum) has a gomp rule,
    the level index is got, and this index can be used as an index for 
	sumarize. For example, for 2x2x2 ( two organs, two macro envirments, and 
	two miroenvirments) model,there is only a gomprule for micro class (cn=2,
	lnum = 0 ), there are four gomp sum levels (this
	simple gomprule will control all micro level nested within organ
	and macro). If there are 2 gompertz rules for micro class, there
	are eight sum levels for this model.

	organ                              o
    macro                      o               o
	micro                  o       o       o       o
	ntypes=8             o   o   o   o   o   o   o   o
	sumlevels=4(cn=2)      1       2       3       4
***************/

int GetSumGompLevel(cn,lnum,iindex)
int cn,lnum,iindex;
{
  int k,found=False,k_last,k_this,ienv=0;
  
  for ( k=1; k<= NumGompRules; k++ ){
		 if ( (GompRule[k][ienv].cn==cn) && ((GompRule[k][ienv].level == lnum)||((GompRule[k][ienv].level >= lnum)&&(cn==1))))  {
		 	 found = True;
			 break;
		 }
   }

  if ( found ){
	  if ( cn > 0 ) {		  
		  k_last=getcelllevelbyclass(LookUp[iindex].LookUpId,0);
		  k_this=getcelllevelbyclass(LookUp[iindex].LookUpId,1);
		  k=k_last*Class[1].no_levels+k_this;
	  
		  if ( cn == 2 ) k=k*Class[cn].no_levels+lnum; 
		  return (k+1);  /* cn = 1 */
	 }
	  else  return(k); /* cn = 0 */
  }
  else return (0);
   
}

extern double EXPORT PASCAL  sumcellbylevel ( cn, si )
   int cn, si;
{

	int i, j;
	double sum;
	sum = 0;

	fprintf(logfile,"%s\tfunction:cellp.sumcellbylevel\tcn:%d\tsi:%d\\n", gettime(),cn,si);
	cellindices=(int *)malloc((ntypes/Class[cn].no_levels+1)*sizeof(int));
	if ( cellindices == NULL ) {
		Memory = False;
		return (0.0);
	}
	i = getcellindices( cn, si );
	for (j = 1; j <= i; j++)
		sum += getcellcounts(cellindices[j],!VEGF);

	free(cellindices);
	return sum;
}

double sumcellbycontrolgroup ( jrule)
   int jrule;
{

	int intersection_i, i, j;
	int  *intersection_cellindices;
	double sum;
	int C,L,k,ienv=0;
	int intersection_indices();

	sum = 0;
	intersection_cellindices=(int *)malloc((ntypes/GompRule[jrule][ienv].nclevel+1)*sizeof(int));

	nentry=0;

	for (k=0;k < GompRule[jrule][ienv].nclevel;k++) {
		C = GompRule[jrule][ienv].cc[k].cclass;
		L = GompRule[jrule][ienv].cl[k].clevel;
		if  (L !=-1) {
			cellindices=(int *)malloc((ntypes/GompRule[jrule][ienv].nclevel+1)*sizeof(int));
			if ( cellindices == NULL ) {
				Memory = False;
				return (0.0);
			}

			i = getcellindices( C, L );
			intersection_i=intersectionindices(intersection_cellindices,intersection_i,i);
			
			free(cellindices);
		}
		else intersection_i=0;

	}

	for (j = 1; j <= intersection_i; j++)
		sum += getcellcounts(intersection_cellindices[j],VEGF);

	free(intersection_cellindices);
	return sum;
}

int intersectionindices(int *intersectionindx,int intersection_i,int length){
int i,j,ret,count=1;
int found = true;
	
	if ( nentry==0){
		for ( i=1; i<=length;i++) {
			intersectionindx[i]=cellindices[i];
		}
	    ret=length;
	}
	else {
		for ( i=1; i<=intersection_i; i++) {
			for ( j=1; j<=length,found=false;j++) {
				if ( intersectionindx[i]==cellindices[j]){
					count++;
					found=true;
				}
			}
			if ( found == false ) {
				intersectionindx[count]=intersectionindx[i];
			}
		}
		ret = count;
	}

	nentry++;
	return ret;
}

double AllCells ()
{
	int i;
	double sum;
	sum = 0;
	for (i = 1; i <= active_ntypes; i++) {
		if (LookUp[i].mark!=NEG ) sum += CN[i];
	}
	return (sum);
}

int IsGompOverFlow(int update)
{
struct gomppara *tempgomppr;

if ( update!=True) {  /* first allocate */
	GompPr=(struct gomppara *)calloc(MaxLookUp,(sizeof(struct gomppara )));
	if (GompPr == NULL )
		return True;
}
else {
	tempgomppr=(struct gomppara*)calloc(MaxLookUp,(sizeof(struct gomppara )));
	if (tempgomppr== NULL )
		return True;
	memcpy(tempgomppr,GompPr,(sizeof(struct gomppara )*(MaxLookUp-MAXLOOKUPS)));
	free(GompPr);
	GompPr=tempgomppr; 
}
	return False;
}

int alloc_gomp()
{
	if ( GompPr[change_index].para == NULL ) {
		GompPr[change_index].para = (struct temppara *)calloc(nenvirons,(sizeof(struct temppara)));
		if ( GompPr[change_index].para == NULL )
			return False;
	}

	if ( GompPr[change_index].level == NULL ) {
		GompPr[change_index].level = (struct gomp_level *)calloc(Number_of_Gomp_Classes+1,(sizeof(struct gomp_level)));
		if ( GompPr[change_index].level == NULL )
			return False;
	}

   	return True;

}

int OrganMetGroup(int iindex, unsigned int icelltype,double nConvertedTo,int envnowl)
{
	int	nmeted,oldmacrolevel,newmacrolevel,neworganlevel,oldorganlevel,nConvertedToMac;
	unsigned int tempId, getcelltype();

		if ( ( Number_of_Classes > 1 ) && (Class[1].class_type == 2 )) {
			oldmacrolevel=getcelllevelbyclass(LookUp[iindex].LookUpId,1);
			oldorganlevel=getcelllevelbyclass(LookUp[iindex].LookUpId,0);
			neworganlevel=getcelllevelbyclass(icelltype,0);
			tempId=(neworganlevel-oldorganlevel)*Tblength+LookUp[iindex].LookUpId;
			if ( nConvertedTo  <= MaxDrop ) {
				for ( nmeted=1;nmeted<=	nConvertedTo; nmeted++) {
					newmacrolevel=(int)(ranmarm(METRAND)*Class[1].no_levels);
					
					icelltype=tempId+macro_block_length*(newmacrolevel-oldmacrolevel);
					
					if ( create_para_struct(icelltype,SIM) == False)
    				return False;
				
    				nDescOfItype(change_index) += 1;
				} /* end of for loop */
			}
			else { /* nConverted > MaxDrop ) */
				newmacrolevel=(int)(ranmarm(METRAND)*Class[1].no_levels);
				nConvertedToMac = (int) (nConvertedTo/MaxGroup);
				if ( nConvertedToMac > 0 ) {
					for ( nmeted=1;nmeted<=	MaxGroup; nmeted++) {
						newmacrolevel = ( newmacrolevel+fillgap) % Class[1].no_levels;
						icelltype=tempId+macro_block_length*(newmacrolevel-oldmacrolevel);
							if ( create_para_struct(icelltype,SIM) == False)
    					return False;
					
    					nDescOfItype(change_index) += nConvertedToMac;
					} /* end of for loop */
				}
				nConvertedToMac = ((int)nConvertedTo)%MaxGroup;
				for ( nmeted=1;nmeted<=	nConvertedToMac; nmeted++) {
					newmacrolevel = nmeted % Class[1].no_levels;
					icelltype=tempId+macro_block_length*(newmacrolevel-oldmacrolevel);
					if ( create_para_struct(icelltype,SIM) == False)
    					return False;
				
    				nDescOfItype(change_index) += 1;
				} /* end of for loop */
			} /* end of "nConverted > MaxDrop " */
			} /* if "macro" */
			else {
				if ( create_para_struct(icelltype,SIM) == False)
    					return False ;
					
    			nDescOfItype(change_index) += nConvertedTo;
			} /* if not "macro" */
			
		return True;

}

int OrganMet (int iindex,unsigned int icelltype, double  nConvertedTo, int envnowl)
{
		if ( create_para_struct(icelltype,SIM) == False )
    		return False;

    	nDescOfItype(change_index) += nConvertedTo;

		return True;
	
}

/* for each UpdateTime, this function is called to save computation time */
void Gomp_UpdateTime(int envnowl) {
	int iindex, cn, ksumlevel;
	double temp_gp,ratio;/* Nvegf/Nvegf_max  */
	double Pmax;
	double sum_bygroup;
	extern int getcellindex();

	for ( iindex=1;iindex <= active_ntypes;iindex++) {
		for ( cn=0; cn <= Number_of_Gomp_Classes;cn++ ) {
			ksumlevel =  SumGompLevel(cn,iindex);
			temp_gp=GompRule[GompRuleIndex(cn,iindex)][envnowl].gpmin;
			if ( NGomp(cn,ksumlevel)> 1.0 ) {
				if (GompRule[GompRuleIndex(cn,iindex)][envnowl].gp_or_inv.inv_log_gp == PROPGOMP ) {
					/* Gompertz GP is controlled by another population.
					 Information of control group is keeped in GompRule.cl.clevel
					 and GP = PROPGOMP instead of normal GP. The sum of cell counts is 
					 multiplied by a constant ( GompRule.multiplier ) which is
					 read when calling ApplyRule().*/
					
					sum_bygroup=sumcellbycontrolgroup(GompRuleIndex(cn,iindex));
					if ( LookUp[iindex].mark !=NEG ) { /* cancer cell*/
						/* GP = GPmin + Constant*N_VE_region  */
						/*temp_gp += GompRule[GompRuleIndex(cn,iindex)][envnowl].multiplier*NGomp(cn,jsumlevel);*/
						temp_gp += GompRule[GompRuleIndex(cn,iindex)][envnowl].multiplier*sum_bygroup;
						
					}
					else {/* VE cell */
						Pmax = GompRule[GompRuleIndex(cn,iindex)][envnowl].gpmax;
						if ( Pmax <= 0 ) Pmax = temp_gp*GompRule[GompRuleIndex(cn,iindex)][envnowl].multiplier;
						ratio = GompRule[GompRuleIndex(cn,iindex)][envnowl].multiplier*sum_bygroup/Pmax;
					/*	ratio = GompRule[GompRuleIndex(cn,iindex)][envnowl].multiplier*NVegf[jsumlevel]/Pmax;*/
						if ( ratio > 1.0 ) ratio = 1.0;
						
						/* log(GP) = log(Pmin) + log(Pmax/Pmin)*min(1,Nvegf/Nvegf_max)  */
						temp_gp = temp_gp*pow(Pmax/temp_gp,ratio);
					}
				
					if ( temp_gp > 1.0 ) 
						LogNGomp(cn,ksumlevel) = log(NGomp(cn,ksumlevel))/log (temp_gp);
					else 
						LogNGomp(cn,ksumlevel) = MAXLOGPLATEAU;
				}
				else  /* general Gompertz rule */
					LogNGomp(cn,ksumlevel) = GompRule[GompRuleIndex(cn,iindex)][envnowl].gp_or_inv.inv_log_gp*log(NGomp(cn,ksumlevel));
			}
		}
    	compute_probability(iindex,envnowl);
	}
}
