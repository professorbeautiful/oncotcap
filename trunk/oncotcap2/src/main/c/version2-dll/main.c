#include "build.h"
#include "defines.h"


#ifdef MPI
#include "mpi.h"
#else 
#include <stdlib.h>
#endif

#include "classes.h"
#include  "cellp.h"
#include "tox.h"

#define NEW_PAGE(file) fprintf(file,"\f");

extern double get_value(),ranmarm(int),rand01(int);
extern int Memory,setInitialCellCounts(),fill_para();
extern int ReadModelFile(),nSavedDoses[Ndrug+1];
extern char *EventString();
extern void ranrmarin(),reset_events(),release_mem();
extern void get_pgf(), cellp();
extern int MaxGroup,MaxDrop,OrganMetOpt,fillgap;
extern int (*OrganFunc)(int,unsigned int,double,int);
extern int OrganMet(int,unsigned int,double,int),OrganMetGroup(int,unsigned int,double,int);
void UnixPlotFnc(),UnixEventFnc();
FILE *Eventfp,*Plotfp;

#ifdef REALUNIX
#include  <sys/time.h>
#else 
#include <time.h>
#endif
#ifndef  DLL
time_t stime;
#endif
double delta_t;
int  nrepetitions;
time_t saitime;

int main(argc, argv)
int argc;
char *argv[];
{  
#ifdef MPI  
int  myid, numprocs, who, namelen, tag=50,itype;
char processor_name[MPI_MAX_PROCESSOR_NAME];
MPI_Status stat;
FILE *outfile;
char sumName[30], fname[30];
int nproc;
#endif

#ifdef TESTMPI
int rep, repetition=100000,if_goodness_test=0,mymargin=2,
	bootrep=10000,resample=1000;
static int Seed1, Seed2;
extern int PrintGap, numTest;
extern char outName[30];
int numFile=9;
int test_time;
char rmcommand[35];
FILE *sp;
#endif
extern int ApplyRule();
char ModelFile[30],name[15];
void initsim();

  	tau=0.0;
	nrepetitions = repetition;
	strcpy(name,"default");
	#ifdef REALUNIX
		   strcpy(WorkingDir,"");
	#else
		   strcpy(WorkingDir,"c:\\temp\\");
	#endif
/*
#ifndef MPI
    switch (argc ) {
      case 5:
	resample=atoi(argv[4]);
      case 4:
	bootrep=atoi(argv[3]);
      case 3:
       repetition=atoi(argv[2]);
      case  2:
       if_goodness_test=atoi(argv[1]);
       break;
      default:
         fprintf(stderr,"usage:main [iftest [repetition],[bootrep],[resample size]]\n");
   }
#endif
*/
    INFINITESIMAL = 1e-12;
	INFINITY = 1e12;
	nconds = 0;
	nenvlist = 0;

	PlotData = UnixPlotFnc;
	PostEvent = UnixEventFnc;

/* for get run time */

#ifdef TEST 
	numTest = numFile;
	if ( nrepetitions == 1 ) {
		if (( Eventfp = fopen("eventdata.out","w")) == NULL )
		{
			fprintf(eout,"Error opening eventdata.out\n");
			return 1;
		}
	
		if (( Plotfp = fopen("plotdata.out","w")) == NULL )
		{
			fprintf(eout,"Error opening plotdata.out\n");
			return 1;
		}
	}


//	srand( (unsigned)time( NULL ) );
	srand(13451);
	ranSeed1 = (int ) (rand()* 31328*INV_RAND_MAX) ;
	ranSeed2 = (int ) (rand()* 30081*INV_RAND_MAX) ;
	ranrmarin(ranSeed1,ranSeed2,SEEDRAND);

	strcpy(outName,"justtest");

	if ( if_goodness_test ) {
	     numTest=numFile;

    /* construct input file nmae  */
    if ( numTest<=9 ) { 
		sprintf(ModelFile,"%s%s0%1d.model",WorkingDir,name,numTest);
	}
    else {
		sprintf(ModelFile,"%s%s%2d.model",WorkingDir,name,numTest);
	}

	if (!ReadModelFile(ModelFile,PGF)){
		printf("\nread data error\n");
		return 0;
	}

	if (numTest <= 9 )
		sprintf(outName,"%s0%1d.%d.%d.0%d.out",outName,numTest,repetition,(int)EndT,(int)(delta_t*1000));
	else 
		sprintf(outName,"%s%2d.%d.%d.0%d.out",outName,numTest,repetition,(int)EndT,(int)(delta_t*1000));


	initsim(PGF,1);
	fill_para(PGF);
#ifndef REALUNIX	     
	 get_pgf(delta_t,repetition,mymargin,bootrep,resample);   
#endif
	 release_mem(PGF);
	 free(CNcopy);
 }
 else {

/*	numFile = atoi(argv[1]); */

	for ( numTest=numFile; numTest<=numFile; numTest++ ) {

		/* construct input file nmae  */
		if ( numTest<=9 ) { 
			sprintf(ModelFile,"%s%s0%1d.model",WorkingDir,name,numTest);
		}
		else {
			sprintf(ModelFile,"%s%s%2d.model",WorkingDir,name,numTest);
		}

		if (!ReadModelFile(ModelFile,SIM)){
			printf("\nread data error\n");
			return 0;
		}

	    if ( repetition > 1  ) {
			if (numTest <= 9 )
				sprintf(outName,"%s0%1d.%d.%d.0%d.out",outName,numTest,repetition,(int)EndT,(int)(delta_t*1000));
			else 
				sprintf(outName,"%s%2d.%d.%d.0%d.out",outName,numTest,repetition,(int)EndT,(int)(delta_t*1000));
    		
			sprintf(rmcommand,"rm -f %s",outName);   
			system(rmcommand);
		}

	            /* Start to run simulation */
		  PrintGap=(int)((EndT-StartT)/(delta_t*120));
		  rep = 1;
		 
		  if ((	sp = fopen("OncoTcap.dat","w")) == NULL ) {
				return 0;
		  }

		  while ( rep <= repetition ) {
			  if ( (rep%50)==0 ) {
			    fprintf(stdout,"r=%d ",rep);
			    fflush(stdout);
			  }
			    initsim(SIM,rep);
				Memory=True;
#ifndef DLL
			if ( nrepetitions == 1 )
				printf("Program begins at %d\n",time(&saitime));
#endif		
			cellp(delta_t);
			if(GuaranteeReset == False)
			{
			//	fprintf(sp,"%d %d %d %u\n",rep,ranSeed1,ranSeed2,ranSeed3);
				rep++;
			} 
#ifndef DLL			
			if ( nrepetitions == 1 )
				printf("program ends at %d\n", time(&stime)-saitime);
#endif		
			if ( Memory== False  ) {
				printf("Run out of memory space!!!\n");
				return 1;
			}
		 }
		  	free(CNcopy);
	    }
	fclose(sp);
	if ( numFile<=9 )
	   printf("\nAll done. Your output data is saved in file %s01 -- %s%1d.\n",outName,outName,numFile);
	else 
	   printf("\nAll done. Your output data is saved in file %s01 -- %s%2d.\n",outName,outName,numFile);
	}
#endif

	
#ifdef MPI
   
    MPI_Init(&argc,&argv);
/* Determine how many processes there are.*/
    MPI_Comm_size(MPI_COMM_WORLD,&numprocs);
/* enroll in mpi , find my task id*/
    MPI_Comm_rank(MPI_COMM_WORLD,&myid);
    MPI_Get_processor_name(processor_name,&namelen);
/*
   if (argc>=2)
     { 
       numFile=(int)atof(argv[1]);
     }
*/
   numTest=numFile;
/*  PE0 is master to send data to slaves , use SPMD mode to implement
    master-slave mode on  T3E_MPI   */
    if ( myid == 0 )
   { 

    fprintf(stdout,"This is PE %d\n",myid);
    fflush(stdout);
    strcpy(sumName,"justsum");
    /*	construct  the ouput file nmae  */
     if ( numTest<=9 )
	 sprintf(fname,"%s0%1d.out",sumName,numTest);
     else 
	 sprintf(fname,"%s%2d.out",sumName,numTest);

    fprintf(eout,"Here is file name:%s\n",fname);
    if (( outfile = fopen(fname,"w")) == NULL ) 
      { 
       fprintf(eout,"Error opening %s\n",fname); 
       MPI_Finalize();
       return 1; 
       } 
    else {
     /* Wait for results from slaves, and there are ( num_proc -1) children. */
       for( nproc=1 ; nproc < numprocs ; nproc++ ){
       for ( rep=1; rep<= repetition; rep ++) {
       MPI_Recv( &active_ntypes,1, MPI_INT, nproc, tag, MPI_COMM_WORLD, &stat );
       CN = (double * )calloc((active_ntypes+1),(sizeof(double)));
     
	 MPI_Recv(CN, active_ntypes+1, MPI_DOUBLE, nproc,tag,MPI_COMM_WORLD,&stat );
	 fprintf(outfile,"%-5d ",(nproc-1)*repetition+rep);
	 for ( itype=1; itype <= active_ntypes; itype++) {
	   fprintf(outfile,"%8.6e ",CN[itype]);
	 }
	 fprintf(outfile,"\n");
	 /*      fprintf(stdout,"I have got the data from PE %d \n", who);
		 fflush(stdout);
		 */  
      free(CN);
      }
      } /* end of for */
       fclose(outfile);
       fprintf(eout,"master (active_ntypes=%d)\n",active_ntypes);
       fflush(eout);
     }  /*  end if outfile  */
  	} /* end if ( myid==0 ) */
	else   /*** child processes ***/
	{
		fprintf(stdout,"This is child  PE %d (rep=%d)\n",myid,repetition);
		fflush(stdout);
		sprintf(outName,"justtest%d.",myid);
		if ( nrepetitions == 1 ) {
			if (( Eventfp = fopen("eventdata.out","w")) == NULL )
			{
				fprintf(eout,"Error opening eventdata.out\n");
				return 1;
			}
		
			if (( Plotfp = fopen("plotdata.out","w")) == NULL )
			{
				fprintf(eout,"Error opening plotdata.out\n");
				return 1;
			}
		}

		/* construct input file nmae  */
		if ( numTest<=9 ) { 
			sprintf(ModelFile,"%s%s0%1d.model",WorkingDir,name,numTest);
		}
		else {
			sprintf(ModelFile,"%s%s%2d.model",WorkingDir,name,numTest);
		}

		if ( !ReadModelFile(ModelFile,SIM)) {
			fprintf(stdout,"Error opening data file.\n"); 
			fflush(stdout);
			MPI_Finalize();
			return 1; 
		}

		 /* Start to run simulation */
		PrintGap=(int)((EndT-StartT)/(delta_t*100));
		Memory=True;
		fprintf(eout,"PE %d starts to run simulation.\n",myid);
		fflush(eout);

	
	  	srand((10000*myid)%( (unsigned)time( NULL ) ));
		ranSeed1 = (int ) (rand()* 31328*INV_RAND_MAX) ;
		ranSeed2 = (int ) (rand()* 30081*INV_RAND_MAX) ;
		ranrmarin(ranSeed1,ranSeed2,SEEDRAND);

		rep = 1;
		while ( rep<= repetition ) {
		    initsim(SIM,rep);
	
		    cellp(delta_t);
			if(GuaranteeReset == False)
			{
				rep++;
				if ( Memory == False )
				fprintf(eout,"Run out of memory space!!!\n");
                
				/* Send result to master */
				MPI_Send( &active_ntypes,1, MPI_INT, 0 ,tag,MPI_COMM_WORLD);
				MPI_Send( CN, active_ntypes+1, MPI_DOUBLE, 0,tag,MPI_COMM_WORLD);
				free(CN);
				active_ntypes=0;
			} 
		
			if ( nrepetitions == 1 ) {
				fclose(Eventfp);
				fclose(Plotfp);
			}
		}
	   } /* child process */
#endif
 /* Program finished. Exit MPI before stopping */
#ifdef MPI
     MPI_Finalize();
     free(CNcopy);
#endif
#ifdef TEST
	 if ( nrepetitions == 1 ) {
		fclose(Eventfp);
		fclose(Plotfp); 
	 }
#endif
     return 0;
}

void initsim(int sim_type,int rep){
int i,initoption,RNGOption=USERANDMAR;
char times[SMALLBUFFER];

 	for(i=1;i<= ndrugs;i++)
	{
		nSavedDoses[i] = ndoses[i];
	}

	ranSeed1 = (int)(ranmarm(SEEDRAND)* 31328) ;
	ranSeed2 = (int)(ranmarm(SEEDRAND)* 30081) ;
	ranSeed3 = (unsigned int ) (ranmarm(SEEDRAND)*4294967295);
/*
	ranSeed1 = 22185;
	ranSeed2 = 9227; 
	ranSeed3=513; 
*/
	if (RNGOption == USERAND) 
	{
		srand(ranSeed3);
		RandFunc = rand01;
	}
	else if (RNGOption == USERANDMAR)
	{
		ranrmarin(ranSeed1,ranSeed2,CNRAND);
		RandFunc = ranmarm;
	}
	ranrmarin(ranSeed1,ranSeed2,METRAND);

	if (CoVar > 0 ) initoption = INITRANDOM;
	else initoption = INITFIXED;

	reset_events();
	setInitialCellCounts(initoption,NEWPATIENT,sim_type,rep);

	OrganMetOpt=NONRAND;
	if ( OrganMetOpt == NONRAND ) {
		OrganFunc = OrganMet;
	 }
	 else {
		OrganFunc = OrganMetGroup;
		MaxDrop=MAXDROP; MaxGroup=MAXGROUP;
		fillgap = 3;
	 }

	RandInit = False;

	PlotOption = PLOTALLCELLS ;
}

void UnixPlotFnc(){
	int i;

	fprintf(Plotfp,"%5.3e ",CNsave[0].time);
	for (i=0; i < (int)CNsave[0].ntypes; i++ )
		fprintf(Plotfp,"%5.3e ",CNsave[0].cellcount[i]);
	fprintf(Plotfp,"\n");
}

void UnixEventFnc(){
	if ( nrepetitions == 1 ) {
		fprintf(Eventfp,"%5.3e ",Event_List[0].time);
		fprintf(Eventfp,"%18s ",EventString(Event_List[0].event));
		fprintf(Eventfp,"%16s ",Event_List[0].detail1);
		fprintf(Eventfp,"%16s ",Event_List[0].detail2);
		fprintf(Eventfp,"%16s\n",Event_List[0].detail3);
	}
}

