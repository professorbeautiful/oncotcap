#include "build.h"

#define SORT

#include "Pheno.h"
#include <malloc.h>
#include <string.h>

#ifndef TESTMPI
#include <windows.h>
#include <crtdbg.h>
#else 
#include <stdlib.h>
#endif
#include <ctype.h>
#include "defines.h"
extern double base_kill[MAXDRUGS];
extern struct gomppara *GompPr;

extern void fillclasslevel(),copy_environment(),fill_env(),ApplyThisRule();
extern int get_ntypes(),is_match_level(),is_match_env();
extern int migindex,muindex,coindex,blindex,gompindex;
extern double EndT, *CNcopy;
extern void construct_type_name();

size_t lookup_size,gomppara_size;

int is_newcelltype(unsigned int icelltype) {

	int iindex, binsearch();

#ifdef SORT
	/* for sorted part, use binary serach */
	change_index =1;
	if ( (active_ntypes >0) && (binsearch(icelltype,pre_ntypes,&iindex))) {
		change_index = iindex;
	    return false;
	}
	else  {  /* for non sorted part */
		for ( iindex =pre_ntypes+1; iindex <= active_ntypes; iindex ++ ) {
			if ( LookUp[iindex].LookUpId == icelltype ) {
				change_index = iindex;
				return false;
			}
		}
	}
#else  /* for debug */
	for ( iindex =1; iindex <= active_ntypes; iindex ++ ) {
		if ( LookUp[iindex].LookUpId == icelltype ) {
			change_index = iindex;
			return false;
		}
	}
#endif

	active_ntypes++;
	change_index = active_ntypes;
	LookUp[change_index].LookUpId = icelltype;
	CN[change_index]=0.0;

	return true;
}

void GetTabelSize(icelltype)
int icelltype;
{
int iRule;
void CountThisRule();

 LookUp[change_index].ParaPr->NumOfMu=0;
 LookUp[change_index].ParaPr->NumOfCo=0;
 LookUp[change_index].ParaPr->NumOfBl=0;
 
 /*for ( iRule =0; iRule < (TotalSingleRules - Kinetic_Changes); iRule++ ) {*/
   for ( iRule =0; iRule < TotalRules; iRule++ ) {
			if ( is_match_level(icelltype,iRule ) ) {
 				CountThisRule(iRule);
			}
		}
  }

void setup_para()
{
	int idx;

	nowenv = 0;

	for ( idx = 1; idx <= ndrugs; idx++ )
         drugkill(idx,change_index,nowenv) = base_kill[idx];

	for ( idx = 0; idx < LookUp[change_index].ParaPr->NumOfMu; idx++ )
		mutrate(change_index,idx,nowenv)=0.0;

	gama(change_index,nowenv) = 0.00;
	LookUp[change_index].mark = 0; /* cancer cell */
}

unsigned int getcelltype(int iindex, int jindex,int env,int paraKey)
{
	int icelltype;

	switch (paraKey ) {
	case MUTATION:
		icelltype=MuLookUp(iindex,jindex,env);
		break;
	case G0T:
		icelltype=CoLookUp(iindex,jindex,env);
		break;
	case BLEBT:
		icelltype=BlLookUp(iindex,jindex,env);
		break;
	case MIG:
		icelltype=MigLookUp(iindex,jindex,env);
		break;
	}
			
	return icelltype;
}

int allocate_mem()
{
	int nowenvl;

	for ( nowenvl=0; nowenvl < nenvirons;nowenvl++) {
		if ((LookUp[change_index].ParaPr->NumOfMu > 0 ) && ((LookUp[change_index].ParaPr->KinaPr+nowenvl)->MuPr==NULL)) {
			(LookUp[change_index].ParaPr->KinaPr+nowenvl)->MuPr=(struct mutation_para * )malloc((LookUp[change_index].ParaPr->NumOfMu)*(sizeof (struct mutation_para)));
			if ((LookUp[change_index].ParaPr->KinaPr+nowenvl)->MuPr == NULL )
				return False;
		}

		if ((LookUp[change_index].ParaPr->NumOfCo > 0 ) && ((LookUp[change_index].ParaPr->KinaPr+nowenvl)->CoPr==NULL)) {
			 (LookUp[change_index].ParaPr->KinaPr+nowenvl)->CoPr=(struct transition_para * )malloc(LookUp[change_index].ParaPr->NumOfCo*sizeof (struct transition_para ));
			if ((LookUp[change_index].ParaPr->KinaPr+nowenvl)->CoPr == NULL )
				return False;
		}

		if ((LookUp[change_index].ParaPr->NumOfBl > 0) && ((LookUp[change_index].ParaPr->KinaPr+nowenvl)->BlPr==NULL)) {
	 		 (LookUp[change_index].ParaPr->KinaPr+nowenvl)->BlPr=(struct transition_para * )malloc(LookUp[change_index].ParaPr->NumOfBl*sizeof (struct transition_para ));
			if ((LookUp[change_index].ParaPr->KinaPr+nowenvl)->BlPr == NULL )
				return False;
		}

		if ((LookUp[change_index].ParaPr->NumOfMig > 0) && ((LookUp[change_index].ParaPr->KinaPr+nowenvl)->MigPr==NULL)) {
	 		 (LookUp[change_index].ParaPr->KinaPr+nowenvl)->MigPr=(struct migration_para *)malloc(LookUp[change_index].ParaPr->NumOfMig*sizeof (struct migration_para ));
			if ((LookUp[change_index].ParaPr->KinaPr+nowenvl)->MigPr == NULL )
				return False;
		}
	}
	return True;
}

int IsOverFlow(int type)
{
int i;
struct lookup *templookup;
extern int IsGompOverFlow(),alloc_gompsum();

lookup_size = sizeof(struct lookup );
if ( LookUp == NULL) {
	if ( type == SIM ) 
		MaxLookUp=min(MAXLOOKUPS,(ntypes+2));
	else 
		MaxLookUp = ntypes+2;

	LookUp=(struct lookup *)calloc(MaxLookUp,lookup_size);
	CN = (double * )calloc(MaxLookUp,(sizeof(double)));
	if (( LookUp == NULL) || ( CN==NULL))
		return True;
	if ( ( type == SIM ) && (KineticsModel == IsGompertz ) ) {
		gomppara_size = sizeof(struct gomppara);
		if (IsGompOverFlow(false))
			return True;
		if ( alloc_gompsum()==false)
			return true;
	}

	else return False;
}
else {
	if ( (active_ntypes+1) >= MaxLookUp ) {
		MaxLookUp += MAXLOOKUPS;
	/*	LookUp=(struct lookup *)realloc(LookUp,MaxLookUp*(sizeof(struct lookup )));*/
		templookup=(struct lookup *)calloc(MaxLookUp,(sizeof(struct lookup )));
		if ( templookup== NULL ) 
			return true;

		memcpy(templookup,LookUp,(sizeof(struct lookup )*(MaxLookUp-MAXLOOKUPS)));
	   	free(LookUp);
		LookUp=templookup;

		CN=(double * )realloc(CN,MaxLookUp*(sizeof(double)));
		if ((CN == NULL ))
			return True;
	
		for ( i = MaxLookUp-MAXLOOKUPS;i < MaxLookUp;i++ ){
			LookUp[i].ParaPr=NULL;
			LookUp[i].nDescOfItype=0.0;
		}
		if ( (type == SIM ) &&(KineticsModel == IsGompertz ) ) {
			if (IsGompOverFlow(True))
				return True;
		}
		else return False;
	}
}

	return False;
}

int create_para_struct(int icelltype,int type) 
{
int iRule,iEnv,nowenvl,ret;
extern void buildkill(),compute_para(),setrates(),initialize_growth_rates(),alloc_gomp();
char times[SMALLBUFFER];

if ( IsOverFlow(type) )
	return False;

if ( is_newcelltype(icelltype) ) {
	muindex=-1;coindex=-1;blindex=-1;migindex=-1;
	for ( nowenvl=0; nowenvl< nenvirons; nowenvl++ ) {
	if ( LookUp[change_index].ParaPr == NULL ) {
		LookUp[change_index].ParaPr=(struct para_struct * )calloc(1,sizeof ( struct para_struct));
		if (LookUp[change_index].ParaPr == NULL )
			return False;
		LookUp[change_index].ParaPr->KinaPr=(struct kina_para * )calloc(nenvirons,sizeof ( struct kina_para));
		if ( LookUp[change_index].ParaPr->KinaPr == NULL )
			return False;

		GetTabelSize(icelltype);
		
		if (allocate_mem() == False )
			return False;

		setup_para();
		initialize_growth_rates();
				
		if ((KineticsModel == IsGompertz ) &&( type==SIM))
			alloc_gomp();

		for ( iRule =0; iRule < (TotalSingleRules - Kinetic_Changes); iRule++ ) {
				if ( is_match_level(icelltype,iRule ) ) {
					ApplyThisRule(iRule,icelltype,nowenvl);
				}
			}

	}

	/* First copy the environment */

	if ( nowenvl > 0 )
		copy_environment(nowenvl);
	   	
	for ( iRule =(TotalSingleRules - Kinetic_Changes); iRule < (TotalRules); iRule++ ) {
		iEnv=Rules[iRule].EnvirCond[0].ienv;
		if ( iEnv == nowenvl) {
			if ( is_match_level(icelltype,iRule ) ) {
 				if (is_match_env(Rules[iRule].EnvirCond[0].ienv,iRule)) {
				/*	if ( iEnv > 0 )
						copy_environment(iEnv); */
					ApplyThisRule(iRule,icelltype,iEnv);
				}
			}
		}
	}
   
	if ( LookUp[change_index].mark != NEG )
		construct_type_name(icelltype,cellname(change_index));
	else  strcmp(cellname(change_index),"VE cell");
	
	buildkill(change_index,nowenvl);
    setrates (change_index,nowenvl);
	compute_para(change_index,nowenvl);

	}
}
	return True;
}

int fill_para(int type)
{
int itype;
extern int LookUpId[MAXLOOKUPS];
extern void compute_para();

if (IsOverFlow(type))
	return false;

if ( type == PGF) {
	for (itype=1; itype<=(int)ntypes; itype++)
		create_para_struct(itype,type);
}
else {
	for (itype=1; itype<=init_active_ntypes; itype++){
		create_para_struct(LookUpId[itype],type);
		CN[itype]=CNcopy[itype];
	}
}
return True;
}

void release_mem(int type)
{
int i,env;

for (i=1; i<=active_ntypes; i++ ) {
	for ( env=0; env < nenvirons; env++) {
		if (( LookUp[i].ParaPr->KinaPr+env)->MuPr!= NULL){
			free((LookUp[i].ParaPr->KinaPr+env)->MuPr);
			(LookUp[i].ParaPr->KinaPr+env)->MuPr =NULL;
		}
		if (( LookUp[i].ParaPr->KinaPr+env)->CoPr!= NULL){
			free((LookUp[i].ParaPr->KinaPr+env)->CoPr);
			(LookUp[i].ParaPr->KinaPr+env)->CoPr = NULL;
		}
		if (( LookUp[i].ParaPr->KinaPr+env)->BlPr != NULL ){
			free((LookUp[i].ParaPr->KinaPr+env)->BlPr);
			(LookUp[i].ParaPr->KinaPr+env)->BlPr = NULL;
		}

		if (( LookUp[i].ParaPr->KinaPr+env)->MigPr != NULL ){
			free((LookUp[i].ParaPr->KinaPr+env)->MigPr);
			(LookUp[i].ParaPr->KinaPr+env)->MigPr = NULL;
		}
		
	}
	
	if ( LookUp[i].ParaPr->KinaPr !=NULL ){
		free(LookUp[i].ParaPr->KinaPr);
		LookUp[i].ParaPr->KinaPr = NULL;
	}
	if ( LookUp[i].ParaPr!= NULL ){
		free(LookUp[i].ParaPr);
		LookUp[i].ParaPr = NULL;
	}
	if ((type==SIM) && (KineticsModel == IsGompertz ) ){
		if ( GompPr[i].para != NULL ){
			free(GompPr[i].para);
			GompPr[i].para = NULL;
		}
		if ( GompPr[i].level != NULL ){
			free(GompPr[i].level);
			GompPr[i].level = NULL;
		}
	}
}

if ( LookUp !=NULL){
	free(LookUp);
	LookUp=NULL;
}
pre_ntypes = 0;

if ((type == SIM)&&(KineticsModel == IsGompertz ) ) {
	if ( GompPr != NULL ) {
		free(GompPr);
		GompPr=NULL;
	}
	for (i=0; i<= Number_of_Gomp_Classes;i++) {
		if ( GompSum[i].NGompPr != NULL )
			free(GompSum[i].NGompPr);
	}
	if ( GompSum != NULL ){
		free(GompSum);
		GompSum=NULL;
	}
}

#ifndef MPI
	active_ntypes=0;
	if ( CN != NULL ){
		free(CN);
		CN = NULL;
	}
#endif
}

double getcellcounts(unsigned int index,int flag)
{
int i;

for (i=1;i<=active_ntypes;i++) {
	if ( flag == VEGF) {
		if (LookUp[i].LookUpId == index) /* mark VE cell */
			return (CN[i]);
	}
	else {
		if ((LookUp[i].LookUpId == index) && (LookUp[i].mark!=NEG)) /* mark VE cell */
			return (CN[i]);
	}
}

return (0.0);
}

int getcellindex(unsigned int icelltype)
{
int i;

for (i=1;i<=active_ntypes;i++) {
	if (LookUp[i].LookUpId == icelltype)
		return (i);
}

return (0);
}

int binsearch(unsigned int icelltype, int n, int *found )
{
	int low, high, mid;

	low =1;
	high= n;


	while ( low <= high ) {
		mid = ( low+high)/2;
		if ( icelltype < LookUp[mid].LookUpId ) 
			high = mid - 1;
		else if ( icelltype > LookUp[mid].LookUpId )
			low = mid + 1;
		else {
			*found=mid;
			return true;
		}
	}
	
		*found = low;
		return false;
}

void insert_element( int replacewith, int insertinfo,int type)
{
	struct lookup temp;
	struct gomppara tempgomppr;
	double CNtemp;
	int i,j;

	memcpy(&temp,&LookUp[replacewith],lookup_size);
	CNtemp=CN[replacewith];
	if ( ( type == SIM ) && (KineticsModel == IsGompertz ) ) 
		memcpy(&tempgomppr,&GompPr[replacewith],gomppara_size);
/*	program is crashed for the code below
	memmove(&LookUp[replaced+1],&LookUp[replaced],(sizeof (struct lookup))*(inserted-replaced));
	or for unix below
	memcpy(&LookUp[replaced+1],&LookUp[replaced],(sizeof (struct lookup))*(inserted-replaced)); */

	for (i = replacewith; i > insertinfo ; i--)
	{
	    j = i - 1;
	    memcpy(&LookUp[i], &LookUp[j], lookup_size);
        CN[i] = CN[j];
		if ( ( type == SIM ) && (KineticsModel == IsGompertz ) ) 
			memcpy(&GompPr[i],&GompPr[j],gomppara_size);
	}
	
	memcpy(&LookUp[insertinfo],&temp,lookup_size);
	CN[insertinfo]=CNtemp;
	if ( ( type == SIM ) && (KineticsModel == IsGompertz ) ) 
		memcpy(&GompPr[insertinfo],&tempgomppr, gomppara_size);

}

int alloc_gompsum()
{
	int i;
	
	GompSum = (struct GompSumStruct *)calloc(Number_of_Gomp_Classes+1,(sizeof(struct GompSumStruct)));
	if ( GompSum == NULL )
		return False;

	for (i=0; i<= Number_of_Gomp_Classes;i++) {
		GompSum[i].NGompPr= (struct gompsum *)calloc(1,(sizeof(struct gompsum)));
		if ( GompSum[i].NGompPr == NULL )
			return False;
			GompSum[i].NGompPr[0].Ngomp=0.0;
			GompSum[i].NGompPr[0].LogNgomp=0.0;
			NSumLevel(i) = 1;
	}

	return True;
}

int IsGompSumOverFlow(int  ksumlevel,int cn )
{
	int i;
	if ( ksumlevel >= NSumLevel(cn)) {
			GompSum[cn].NGompPr= (struct gompsum *)realloc(GompSum[cn].NGompPr,(ksumlevel+1)*(sizeof(struct gompsum)));
			if ( GompSum[cn].NGompPr == NULL ) {
				return true;
			}
			
			for ( i=NSumLevel(cn);i <= ksumlevel; i ++) {
				GompSum[cn].NGompPr[i].Ngomp=0.0;
				GompSum[cn].NGompPr[i].LogNgomp=0.0;
			}
			NSumLevel(cn) = ksumlevel+1;
		}

return false;

}	
void sort_lookuptable(int left,int right,int type) 
{		  
	int i, replaceid=0;
	void insert_element();

	for ( i = left; i <= right; i++ ){
		binsearch(LookUp[i].LookUpId, i-1,&replaceid );
		if ( replaceid < i )
			insert_element(i,replaceid,type);
	}
}