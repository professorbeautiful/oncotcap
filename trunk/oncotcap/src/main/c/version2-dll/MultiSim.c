#include <windows.h>
#include "build.h"
#include "defines.h"
#include "classes.h"
#include "cellp.h"
#include "tox.h"
#include "msim.h"

int AddEvent();
void setInitialCellCounts();

void MultiSim( int RNGOption)
{
	int i;
	FILE *sp;
	int rngSeed1,rngSeed2;
	char seedfilename[255];

	srand( (unsigned)time( NULL ) );
	rngSeed1 = (int ) (rand()* 31328*INV_RAND_MAX) ;
	rngSeed2 = (int ) (rand()* 30081*INV_RAND_MAX) ;
	ranrmarin(rngSeed1,rngSeed2,SEEDRAND);

	sprintf(seedfilename,"%s\\OncoTcap.log",WorkingDir);
	if ((	sp = fopen(seedfilename,"a")) == NULL ) {
		MessageBox(NULL,"There is a problem to open file.","treat.dll",MB_ICONEXCLAMATION | MB_OK);
		return;
	}
	fprintf(sp,"\nMultisim\n");
	i=0;
	while((MEndSim == False) && (i < repetitions))

	{

		ranSeed1 = (int ) (ranmarm(SEEDRAND)* 31328) ;
		ranSeed2 = (int ) (ranmarm(SEEDRAND)* 30081) ;
		ranSeed3 = (unsigned int ) (ranmarm(SEEDRAND)*4294967295); /* 2^32 -1*/
		reset_sim();
		IsMeanOnly = USERAND;
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
		else /* for setInitialCellCounts() */
		{
			ranrmarin(ranSeed1,ranSeed2,CNRAND);
			RandFunc = ranmarm;
		}

		ranrmarin(ranSeed1,ranSeed2,METRAND);
		RandInit = False;
		AddToEventQ(0.0,BEGINSIMEVENT,".",".",".");
		setInitialCellCounts(InitialOption,NEWPATIENT,SIM,i);
		if (RNGOption == USEMEANONLY)
			IsMeanOnly = USEMEANONLY;

		cellp();
		PlotData();
		if(GuaranteeReset == False)
		{
			fprintf(sp,"sim=%d,seed1=%d,seed2=%d,seed3=%d,Total=%lf\n",i,ranSeed1,ranSeed2,ranSeed3,InitTotal);
			SeedList[0][i] = ranSeed1;
			SeedList[1][i] = ranSeed2;
			SeedList[2][i] = ranSeed3;
			i++;
		}
	}

	fclose(sp);
	free(initCellVector);
}


