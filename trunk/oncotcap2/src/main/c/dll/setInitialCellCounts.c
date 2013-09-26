#include "build.h"
#ifdef DLL
#include <windows.h>
#else 
#include <stdlib.h>
#endif
#include <stdio.h>
#include "defines.h"
#include "classes.h"
#include "cellp.h"
#include "tox.h"

extern double GenerateInitCellCount();
extern int IsSetPara;
/*void ranrmarin();*/
extern double (*RandFunc)(int);

void setInitialCellCounts(int option,int patientoption,int simtype,int PatientNum)
{
	/* unsigned int i; */
	int seed1,seed2;
	void distribute_cell();

/*	FILE *sp;*/
	if (simtype == SIM)
	{
		if ((patientoption == NEWPATIENT) || (PatientNum>0 ))
		{
			if (option == INITRANDOM)
			{
				/*RandFunc = ranmarm;
				sp = fopen("c:\\temp\\initseeds.out","a");
				seed1 = (int)(rand()* 31328*INV_RAND_MAX) ;
				seed2 = (int)(rand()* 30081*INV_RAND_MAX) ; */


			/* QSH said this was ok to do rather than generate a new seed 02/05/99*/
				ranrmarin(ranSeed3%31328,ranSeed2,INITRAND);
				InitTotal = (unsigned int) GenerateInitCellCount(InitMean,CoVar);	
		/*		fprintf(sp,"Total = %lf Mean = %lf, CoVar = %lf\n",InitTotal,InitMean,CoVar);
				fclose(sp);*/
			}
			else InitTotal = InitMean;
		}
	   /*for(i =1; i<= (unsigned int)init_active_ntypes;i++)
		{
			create_para_struct(LookUpId[i],SIM);

			CN[i] = initCellVector[i] * InitTotal;
		}*/
		distribute_cell(SIM);
	}							   
	else if (simtype == PGF)
	{
		RandFunc = rand01;
		if (IsSetPara == False ) {
			srand((unsigned)time(NULL));
			IsSetPara = True;
			if (option == INITRANDOM)
			{
				seed1 = (int)(rand()* 31328*INV_RAND_MAX) ;
				seed2 = (int)(rand()* 30081*INV_RAND_MAX) ;
				ranrmarin(seed1,seed2,INITRAND);
				InitTotal = (unsigned int) GenerateInitCellCount(InitMean,CoVar);	
			}
			else  { 
				InitTotal = InitMean;
				LogMean=log(InitMean);
				LogStd=0.0;
			}

			distribute_cell(PGF);
		}
	}
}

/* added by Qingshou, 10/31/97  */
void distribute_cell(int Type) {
	int i,n;
	double prob_sum=1.0,nRemaining,prob;

	 if ( Type == SIM ) n = init_active_ntypes;
	 else n=ntypes;

	 nRemaining=InitTotal;

	 for ( i = 1; i <= n; i++ ) {
		
		/* partial sum of the probability */
	
		if ( Type == SIM ) {
				if ( prob_sum > 0.0 ) {
					 prob = initCellVector[i]/ prob_sum;
				}
				else  prob=0.0;
				
				prob_sum -= initCellVector[i];
	
				 create_para_struct(LookUpId[i],SIM);
				 if ( prob > 0.0  ) {
					 CN[i] = grand_b_n( nRemaining, prob);
					 nRemaining = nRemaining - CN[i];
				 }
				 else CN[i] = 0.0;
		}
		 else {
			cellstart[i] = initCellVector[i]*InitTotal;
		}
	}
}