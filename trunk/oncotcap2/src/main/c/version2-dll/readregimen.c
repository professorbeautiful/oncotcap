#include "build.h"

/* QS 4/10/97, read regimen file for supercomputer
	ifdef TEST ===> for supercomputer or test
  */

#ifndef REALUNIX
#include <crtdbg.h>
#include <windows.h>
#endif

#ifdef TESTMPI
#include "defines.h"
#include "classes.h"
#include "cellp.h"
#include "tox.h"

extern  int nloops, LookUpId[];
extern double StartT,EndT,delta_t,*CNcopy;
extern int ApplySchedule(),SetEnvir(),CalculateTimeKillSurv(),InitDoseFactor();
void fSread();

int ReadRegimen(filename,reg_type)
char *filename;
int reg_type;
  /* FILE *p;  */
{
	char times[200];
	int iagents,nAgents,MaxAgents,itype,ret,isched,ksched,totalsched;
    double time,fraction,endtime;

	FILE *p;

	InitDoseFactor();

	if (( p = fopen(filename,"r")) == NULL )
			{
				printf("Error opening %s\n",filename);
				return 0;
			}
    
		if (init_active_ntypes == 0 ) {
			MaxLookUp=min((MAXLOOKUPS),(ntypes+2));
			CNcopy = (double * )calloc(MaxLookUp,(sizeof(double)));
		}
	init_active_ntypes =1;
	LookUpId[1]=1;
    fDread (p,CNcopy[1]); fdumpline ( p );
  
    fDread (p,delta_t);  fdumpline ( p );
    fDread (p,StartT);  fdumpline ( p );
    fDread (p,EndT);    fdumpline ( p );
    fIread (p, nloops);  fdumpline ( p );
    fIread (p,MaxAgents);  fdumpline ( p );
    fIread (p,nAgents);  fdumpline ( p );

	for ( itype=1; itype <= MaxAgents ; itype++ ) {
        fSread(p,times);
 		fIread (p,iagents);  fdumpline ( p );
		/* If it is not continous agent */
		if ( strcmp(times,"c"))
			ret=ApplySchedule(times,iagents);

			fIread (p,totalsched);  fdumpline ( p );

		for (ksched = 1;ksched <= totalsched; ksched++)
		{
			   fDread(p,time); fdumpline ( p );
			   fDread(p,fraction); fdumpline ( p );
			   if ( strcmp(times,"c") ) { /* it's not continous agent */
				   /* the code below is from pokedosefactor(iagents ,time, fraction );*/
					for (isched = 1;isched <= nsched;isched++)
					{
						if ((sched[isched].d == iagents) && (sched[isched].t == time))
						{
							sched[isched].df = fraction;
							CalculateTimeKillSurv();
						}
					}
			   }
			   else {  /* For continous agent */
					fDread(p,endtime); fdumpline ( p );
					fSread(p,times);
					ret=SetEnvir(times,time,endtime);
			   }
		}
    }  /* to MaxAgents  */

    fclose(p);

/* evaluation points, added 6/6/97 after the strange bugs*/
if ( reg_type == PGF ) {
   sprintf(times,"q1x%d 1", (int)EndT);
   ret=ApplySchedule(times,0);
}
	
   return (1);
}

void  fSread(p,times)
FILE *p;
char *times;
 {
	char ch;
	int i;
	
		ch = fgetc( p );
		 for( i=0; ch!='\n'; i++ )
		{
		 times[i] = (char)ch;
		 ch = fgetc( p );
		}

		/* Add null to end string */
		times[i] = '\0';
}
#ifndef TESTMPI
int ReadTox(filename)
char *filename;
{
	FILE *p;
	int nag, n, i, j;
	if (( p = fopen(filename,"r")) == NULL )
			{
				printf("Error opening %s\n",filename);
				return 0;
			}
  
	fscanf(p,"%d", &nag );
	for (n = 1; n <= nag; n++)
	{
		fscanf(p, "%s", Agents[n].name);
		fscanf(p, "%d", &Agents[n].numToxTypes);
		for (i = 1; i <= Agents[n].numToxTypes; i++)
		{
			fscanf(p, "%s", Agents[n].toxicity[i].name);
			fscanf(p, "%d", &Agents[n].toxicity[i].toxAppType);
			fscanf(p, "%d", &Agents[n].toxicity[i].toxResType);
			fscanf(p, "%d", &Agents[n].toxicity[i].toxTypeIndex);
			fscanf(p, "%lf", &Agents[n].toxicity[i].toxResTime);
			for (j = 1; j <= 5; j++)
			{
				fscanf(p, "%lf\n", &Agents[n].toxicity[i].toxProbs[j]);
			}
		}
	}

	fscanf(p, "%lf", &death_threshold);
	fscanf(p, "%lf", &diagnosis_threshold);
    fclose(p);

    nToxTypes = 0;
	InitToxTypes();
   return (1);
}

#endif
#endif
