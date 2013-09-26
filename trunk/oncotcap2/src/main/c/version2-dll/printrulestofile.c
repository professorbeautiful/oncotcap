#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "defines.h"
#include "rule.h"
#include "BBoard.h"

FILE *fp;
void printclause(ClauseType *clause);
int nargsoffcn( PFI fptr );
char * fcnname(PFI fptr, char * namebuff);
void printargs(PFI fptr,void * argList[]);
int EXPORT PASCAL printrulestofile()
{
	int i, n;
	char tchar[255], fname[255];

	strcpy (fname, WorkingDir);
	strcat (fname, "\\rules.out");

	if (( fp = fopen(fname,"w")) ==  NULL)
	{
//		MsgBoxError("d:\\dll\\rules.out failed open");
		return (FALSE);
	}

 
	for (i=0;i<nRules;i++)
	{

		fprintf(fp,"\n\nRule #%s\n", _itoa(i,tchar,10));
		fprintf(fp, "\tIf Clauses:\n");
		for (n=0; n<BBRules[i].nIFClauses;n++)
			printclause( & BBRules[i].IFClause[n]);
		fprintf(fp, "\n\tAction Clauses:\n");
		for (n=0; n<BBRules[i].nACTIONClauses;n++)
			printclause( & BBRules[i].ACTIONClause[n]);
	}
	fclose(fp);
	return(1);
}

void printclause( ClauseType *clause )
{
	char nbuff[255];

	fprintf(fp,"\t\t%s", fcnname(clause->fptr, nbuff));
	printargs(clause->fptr,clause->argList);

}

int nargsoffcn(PFI fptr)
{
	if (fptr == (PFI) &SetI)
		return(2);
	else if (fptr == (PFI) &SetB)
		return(2);
	else if (fptr == (PFI) &SetD)
		return(2);
	else if (fptr == (PFI) &Add)
		return(3);
	else if (fptr == &And)
		return(3);
	else if (fptr == &Eq)
		return(3);
	else if (fptr == &Lt)
		return(3);
	else if (fptr == &Gt)
		return(3);
	else if (fptr == &Lte)
		return(3);
	else if (fptr == &Gte)
		return(3);
	else if (fptr == (PFI) &Now)
		return(1);
	else if (fptr == (PFI) &RandLogNorm)
		return(3);
	else if (fptr == &Ne)
		return(3);
	else if (fptr == (PFI) &ApplyContinDrug)
		return(1);
	else if (fptr == (PFI) &RemoveContinDrug)
		return(0);
	else
		return(0);
}

char * fcnname(PFI fptr, char * namebuff)
{
	if (fptr == (PFI) &SetI)
		strcpy(namebuff,"SetI");
	else if (fptr == (PFI) &SetB)
		strcpy(namebuff,"SetB");
	else if (fptr == (PFI) &SetD)
		strcpy(namebuff,"SetD");
	else if (fptr == (PFI) &Set)
		strcpy(namebuff, "Set");
	else if (fptr == (PFI) &Add)
		strcpy(namebuff,"Add");
	else if (fptr == &And)
		strcpy(namebuff,"And");
	else if (fptr == &Eq)
		strcpy(namebuff,"Eq");
	else if (fptr == &Lt)
		strcpy(namebuff,"Lt");
	else if (fptr == &Gt)
		strcpy(namebuff,"Gt");
	else if (fptr == &Lte)
		strcpy(namebuff,"Lte");
	else if (fptr == &Gte)
		strcpy(namebuff,"Gte");
	else if (fptr == (PFI) &Now)
		strcpy(namebuff,"Now");
	else if (fptr == (PFI) &RandLogNorm)
		strcpy(namebuff,"RandLogNorm");
	else if (fptr == &Ne)
		strcpy(namebuff,"Ne");
	else if (fptr == (PFI) &ClearBBRegimen)
		strcpy(namebuff,"ClearBBRegimen");
	else if (fptr == &AddtoRegimen)
		strcpy(namebuff,"AddtoRegimen");
	else if (fptr == &ApplyRegimenIntoSchedule)
		strcpy(namebuff,"ApplyRegimenIntoSchedule");
	else if (fptr == (PFI) &ApplyContinDrug)
		strcpy(namebuff,"ApplyContinDrug");
	else if (fptr == (PFI) &RemoveContinDrug)
		strcpy(namebuff, "RemoveContinDrug");

	return(namebuff);
}

void printargs(PFI fptr, void *arglist[])
{

	int *intt;
	double *dblt;
	int *ntreat;
	int *treatIndex;
	int *numTimes;
	int *AgIndex;
	double *interval;
	double *dose;
	int t1;
	double t2;
	

	char tchar[255];

	if (fptr == (PFI) &SetI)
	{
		intt = (int *) arglist[1];
		fprintf(fp,"([%s], %d)\n",arglist[0], *intt);
	}
	else if (fptr == (PFI) &SetB)
	{
		intt = (int *) arglist[1];
		if (*intt == 0)
			strcpy(tchar,"False");
		else
			strcpy(tchar,"True");
		fprintf(fp,"([%s], [%s])\n",arglist[0], tchar);
	}
	else if (fptr == (PFI) &SetD)
	{
		dblt = (double *) arglist[1];
		fprintf(fp,"([%s], %lf)\n",arglist[0], *dblt);
	}
	else if (fptr == (PFI) &Set)
		fprintf(fp, "([%s], [%s])\n",arglist[0], arglist[1]);
	else if (fptr == (PFI) &Add)
		fprintf(fp,"([%s], [%s], [%s])\n",arglist[0], arglist[1], arglist[2]);
	else if (fptr == &And)
		fprintf(fp,"([%s], [%s], [%s])\n",arglist[0], arglist[1], arglist[2]);
	else if (fptr == &Eq)
		fprintf(fp,"([%s], [%s], [%s])\n",arglist[0], arglist[1], arglist[2]);
	else if (fptr == &Lt)
		fprintf(fp,"([%s], [%s], [%s])\n",arglist[0], arglist[1], arglist[2]);
	else if (fptr == &Gt)
		fprintf(fp,"([%s], [%s], [%s])\n",arglist[0], arglist[1], arglist[2]);
	else if (fptr == &Lte)
		fprintf(fp,"([%s], [%s], [%s])\n",arglist[0], arglist[1], arglist[2]);
	else if (fptr == &Gte)
		fprintf(fp,"([%s], [%s], [%s])\n",arglist[0], arglist[1], arglist[2]);
	else if (fptr == (PFI) &Now)
		fprintf(fp,"([%s])\n",arglist[0]);
	else if (fptr == (PFI) &RandLogNorm)
		fprintf(fp,"([%s], [%s], [%s])\n",arglist[0], arglist[1], arglist[2]);
	else if (fptr == &Ne)
		fprintf(fp,"([%s], [%s], [%s])\n",arglist[0], arglist[1], arglist[2]);
	else if (fptr == (PFI) &ClearBBRegimen)
		fprintf(fp,"()\n");
	else if (fptr == &AddtoRegimen)
	{
		ntreat = arglist[0];
		treatIndex = arglist[1];
		AgIndex = arglist[2];
		interval = arglist[4];
		numTimes = arglist[3];
		dose = arglist[5];
		t1 = *numTimes;
		t2 = *interval;
		
		fprintf(fp,"(%d, %d, %d, %lf, %d, %lf )\n",*ntreat, *treatIndex,*AgIndex,*interval,*numTimes,*dose);
	}
	else if (fptr == (PFI) &ApplyRegimenIntoSchedule)
	{
		intt = arglist[0];
		fprintf(fp,"(%d)\n",*intt);
	}
	else if (fptr == (PFI) &ApplyContinDrug)
		fprintf(fp, "([%s])\n",arglist[0]); 
	return;
}
