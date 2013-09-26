#include "build.h"

#include "Pheno.h"
#define DEBUG1

#ifndef TESTMPI
#include <windows.h>
#include <crtdbg.h>
#else 
#include <string.h>
#endif
#include <ctype.h>
#include "defines.h"
#include "InducedConv.h"

char *parseKeys[]=
{ "","KM", "KC", "DT", "TU", "CT", "GP", "GS", "K", "C", "L", 
	 "R" , "DR", "MR", "BT", "CO", "KR", "CY","GR","MA","PGP",
	 "NEG","PO", "DE", "CS", "DS"};

#define parseKeyKM 1
#define parseKeyKC 2
#define parseKeyDT 3
#define parseKeyTU 4
#define parseKeyCT 5
#define parseKeyGP 6
#define parseKeyGS 7
#define parseKeyK 8
#define parseKeyC 9
#define parseKeyL 10
#define parseKeyR 11
#define parseKeyDR 12  /* No longer used.*/
#define parseKeyMR 13
#define parseKeyBT 14
#define parseKeyCO 15
#define parseKeyKR 16
#define parseKeyCY 17
#define parseKeyGR 18
#define parseKeyMA 19
#define parseKeyPG 20
#define parseKeyNEG 21
#define parseKeyPO 22
#define parseKeyDE 23
#define parseKeyCS 24
#define parseKeyDS 25
#define NparseKeys 25

#define strcmpSAME 0
#define NODESTCLASS -1

void fillclasslevel(),sort_rules(),fill_env();
unsigned int get_ntypes();
int parse_general_rule(),is_match_level(),is_match_env(),
	getcelllevelbyclass();
void parse_class_level_other(),read_to_eol();

int migindex,muindex,coindex,blindex,gompindex;
extern double mincycletime;
extern int GetGompRuleIndex();
double base_kill[MAXDRUGS],Base_Conversion_Time;
int minsearch,IsSetPara,IfAnyPGP;
char *chopword();
char *getword();

char *read_rule_file( filenme )
 char *filenme;
{
   char *text;
   char strMsg[256];
   struct stat statb;
   float fltVsn;
   size_t length, j;
   int i;
   char VsnText[512];
   FILE *fp;

      if ( ! *filenme )
      {
         fprintf( eout,"\nWARNING (read_rule_file): No file name supplied.\n" );
      }
      else
      {
         if ( stat( filenme, &statb ) == -1 ||
              ( statb.st_mode & S_IFMT ) != S_IFREG ||
              ! ( fp = fopen( filenme, "r" ) ) )
         {
            perror( filenme );
            fprintf(eout,"Can't read %s.\n", filenme );
			strcpy(strMsg, "Cant't read ");
			strcat(strMsg, filenme);
#ifndef TESTMPI
            MsgBoxWarning(strMsg);
#else
			printf(strMsg);
#endif
         }
         else
         {
            length = (size_t) statb.st_size;
            if ( ! ( text = malloc(  length + 100 ) ) )
               fprintf(eout, "\nWARNING (read_rule_file): XtMalloc(%ld) failed for %s.\n",
                       length, filenme );
            else
            {
               j = fread( text, (size_t) sizeof( char ), length, fp );
               text[j] = '\0';
			   for(i = 0; (i < 510 && text[i] != '\n'); i++)
				   VsnText[i] = text[i];
			   VsnText[i] = '\0';

			   fltVsn = (float) atof(getword(chopword(chopword(VsnText))));
/*			   if (fltVsn > (float) atof(getword(chopword(chopword(strTCAPVERSION)))) || fltVsn < 1.2)
//			   if (memcmp(strTCAPVERSION,text, strlen(strTCAPVERSION)) != 0)
			   {
				   strcpy(strMsg, filenme);
				   strcat(strMsg," incorrect version number");
#ifndef TESTMPI
				   MsgBoxWarning(strMsg);
#else
				   printf(strMsg);
#endif
			   } */

            }
         }
         fclose( fp );
      }
      return text;
}


int is_assignment ( s )
   char *s;
{
   char *p;

   p = s;
   while ( *p != '\0' && *p != '=' )
      *p++;
   if ( *p == '=' )
      return ( 1 );
   else
      return ( 0 );
}

void clear_base_model( )
{
   int i;
  
     /* delete all the drug's log kill rates  */
  
   /* delete all the old drug names */
   for ( i = 0; i <= ndrugs; i++ )
      strcpy( drugname[i], "" );
   ndrugs = 0;

   /* delete all the mutation rates, etc. */

    for ( i = 1; i <= MAXTYPES; i++ ){
		strcpy(typename [i], "");}
   
   NumGompRules=0;
   Kinetic_Changes=0;

   Base_GompPlateau=0.0;
   Base_GompSplit=0.0;

   for ( i=0; i< MAXNGOMP; i++) {
		GompRule[i][0].cn=-1;
		GompRule[i][0].level=-1;
		GompRule[i][0].gp_or_inv.GP=Base_GompPlateau;
		GompRule[i][0].GS=1.0;
		GompRule[i][0].nclevel=0;
	  }

     /* delete all the cell types    */
   ntypes = 0;
   active_ntypes=0;
   init_active_ntypes=0;
   IsSetPara=False;
   IfAnyPGP=false;
   Number_of_Classes = 0;
   Number_of_Gomp_Classes = 0;
   Base_Conversion_Time=0;
}


void display_summary ( )
{

   int  i, j, k;

   fprintf( eout,"CLASS NAME\t\tLEVEL NAME    RULE NAME" );
   for ( i = 0; i < Number_of_Classes; i++ )
   {
      fprintf( eout,"\n\n%-14s", Class[i].class_name );
#ifdef DLL
	  if ( Class[i].class_type == 2 ) {
		fprintf(eout, "%-14s", Class[i].Level[0].level_name );
		fprintf( eout,"%-14s", Class[i].Level[0].Rule[0].rule_name );
	  }
	  else {
		  for ( j = 0; j < Class[i].no_levels; j++ )
		  {
			 if ( j == 0 )
				fprintf(eout, "%-14s", Class[i].Level[j].level_name );
			 else
				fprintf( eout,"\n%14s%-14s", " ", Class[i].Level[j].level_name );
			 for ( k = 0; k < Class[i].Level[j].no_rules; k++ )
			 {
				if ( k == 0 )
				   fprintf( eout,"%-14s", Class[i].Level[j].Rule[k].rule_name );
				else
				   fprintf(eout, "\n%28s%-14s", " ",
							Class[i].Level[j].Rule[k].rule_name );
			 }
		  }
	  }
#endif
   }
 }

void get_macro_levelname(int cn,int lnum,char *name)
{
	int mz,my,x,y,z,temp;

	mz=Class[cn].mz;
	my=Class[cn].my;

	if (( mz> 0 ) && ( my > 0 )) {
		x=lnum/(my*mz);
		temp = lnum % (my*mz);
		y=temp / mz;
		z= temp % mz;
	
		sprintf(name,"%s%d%d%d",Class[cn].Level[0].level_name,x,y,z);
	}
	else strcpy(name,"");
}

void construct_type_name (unsigned int icelltype,char *typename )
{
 int cn, lnum;
 char   s[SMALLBUFFER],temp[SMALLBUFFER];
 /*String (tempname);*/
 char tempname[SMALLBUFFER];
 
  
   strcpy(tempname,"");
   for ( cn = 0; cn < Number_of_Classes; cn++ ) {
	   if ( Class[cn].class_type > 0 ) {
		lnum=getcelllevelbyclass(icelltype,cn);
	
		if ( cn == 0 ) {
			if ( Class[cn].class_type != 2 )
				strcpy( tempname, Class[0].Level[lnum].level_name );
			else  {
				get_macro_levelname(cn,lnum,s);
				if ( strcmp(s,"")==strcmpSAME )
					 strcpy( tempname, Class[0].Level[lnum].level_name );
				else strcpy( tempname,s);
			}
		}
		else {
			if ( Class[cn].class_type != 2 )
				sprintf( s, "/%s", Class[cn].Level[lnum].level_name );
			else {
				get_macro_levelname(cn,lnum,temp);
				if ( strcmp(temp,"")==strcmpSAME )
					 sprintf( s, "/%s", Class[cn].Level[lnum].level_name );
				else sprintf( s, "/%s",temp);
			}
			strcat( tempname, s );
		}
	   }
   }

   strcpy(typename,tempname);
}

void construct_cell_type_names ( )
{
   int    P, Done, level_index[MAXCLASSES+1];
   int    C1;
   char   s[SMALLBUFFER];

   for ( C1 = 0; C1 < Number_of_Classes; C1++ )
      level_index[C1] = 0;

   P = 0;
   while ( level_index[0] < Class[0].no_levels )
   {
	  if ( Class[0].class_type != 2 )
		 strcpy( typename[P+1], Class[0].Level[level_index[0]].level_name );
      else {
		 sprintf( s,"%s%d",Class[0].Level[level_index[0]].level_name,level_index[0]);
		 strcpy( typename[P+1], s);
	  }
	  for ( C1 = 1; C1 < Number_of_Classes; C1++ )
      {
		  if (Class[C1].class_type != 2 )
			 sprintf( s, "/%s", Class[C1].Level[level_index[C1]].level_name );
          else sprintf(s, "/%s%d", Class[C1].Level[level_index[0]].level_name,level_index[C1] );
		  strcat( typename[P+1], s );
      }
      Done = 0;
      C1 = Number_of_Classes - 1;
      level_index[C1] = level_index[C1] + 1;
      while ( Done == 0 && C1 > 0 )
      {
         if ( level_index[C1] < Class[C1].no_levels )
            Done = 1;
         else
         {
            level_index[C1] = 0;
            if ( C1 > 0 )
               level_index[C1-1] = level_index[C1-1] + 1;
         }
         C1--;
      }
	  P++;
	  if ( P >= MAXTYPES) 
		 return;
  }
   return;
}


void initialize_growth_rates ( )
{
   
/* This is rewritten by r.Day on Feb.23, 1997.
//   Turnover is used now instead of Death_Rate.*/
#ifndef TESTMPI
   fprintf(eout, "Base Cycling Time = %f\n", Base_Cycling_Time );
   fprintf(eout, "Base Cycling Time Switch = %f\n", Base_Cycling_Switch );
   fprintf(eout, "Base Doubling Time = %f\n", Base_Doubling_Time );
   fprintf(eout, "Base Death Time = %f\n", Base_Death_Time );
   fprintf(eout, "Base Death Time Switch= %f\n", Base_Death_Switch );
#endif
/* check for consistency*/ 
   /*
   if ( fabs(Base_Doubling_Time /( Base_Cycling_Time * (1+Base_Turnover))
	   - 1.0 ) > 1e-10)
		MsgBoxWarning(" Kinetics inconsistency?");
  */

	nowenv = 0;
   
	cycletime(change_index,nowenv) = Base_Cycling_Time;
	cycleswitch(change_index,nowenv) = Base_Cycling_Switch;
	vnodeath(change_index,nowenv) = false;
	doubletime(change_index,nowenv) = Base_Doubling_Time;
	deathtime(change_index,nowenv) = Base_Death_Time;
	deathswitch(change_index,nowenv) = Base_Death_Switch;
}


int pickcelltypes ( ic, il, p)
   int ic, il, *p;
{
   int *p_orig, post_product, pre_product, 
       iclass, yes_index, igroup, iconsec;
   int ret1, getcellindices();

   p_orig = ++p;  /*Index from 1.*/

   post_product = 1;
   for ( iclass = ic+1; iclass < Number_of_Classes; iclass++ )
      post_product *= Class[iclass].no_levels;

   pre_product = ntypes / post_product/Class[ic].no_levels;

   yes_index = post_product * il + 1;
   for ( igroup = 1;  igroup <= pre_product; igroup++)
   {
      *(p) = yes_index;
      for ( iconsec = 1; iconsec <= post_product; iconsec++ )
         *(++p) = ++yes_index;
      yes_index += post_product * ( Class[ic].no_levels - 1 );
   }
   ret1 = (int) ( p - p_orig ) ;
   /*
	ret2 = getcellindices (ic,il );
			sprintf(strMsg, 
				"Cell Indices %d %d  ic=%d il=%d", 
				ret1, ret2, ic, il);
			for (i=0; i<ret1; i++){
				sprintf(strtemp,", %d=%d",
					p_orig[i],cellindices[i+1]); 
				strcat(strMsg,strtemp);
			}
            MsgBoxWarning(strMsg);
  */
	return ( ret1);
}


int get_dest_gap( cn, si, di)
   int cn, si, di;
{
   return(( di - si ) * Class[cn].post_product);
}

void find_mincyctime(thisKey,value,opr)
int thisKey;
double value;
char opr;
{
	double Changed_Value=0,Base_Value=0;

	switch (thisKey){
		case parseKeyCT:
			Base_Value = Base_Cycling_Time;
			break;
		case parseKeyDE:
			Base_Value = Base_Death_Time;
			break;
		case parseKeyCO:
			Base_Value = Base_Conversion_Time;
			break;
		default:;
	}

	switch ( (int) opr )
    {
		case (int) '*':
			Changed_Value = Base_Value *value;
			break;
		case (int) '/':
			Changed_Value = Base_Value / value;
			break;
		case (int) '+':
			Changed_Value = Base_Value + value;
			break;
		case (int) '-':
			Changed_Value = Base_Value - value;
			break;
		case (int) '=':
			Changed_Value = value;
			break;
		default:;
	}

	if ( Changed_Value > 0 )
		if ( mincycletime > Changed_Value)
				mincycletime = Changed_Value;
}

void change_kinarray_switch (paraKey,value,ienv)
int ienv,paraKey;
int value;
{

	int *p;
	void change_value();

	p=&(LookUp[change_index].ParaPr->KinaPr+ienv)->paraswitch[paraKey];

	*p = value;

}

void change_kinarray_value_1 (paraKey,opr,value,ienv)
int ienv,paraKey;
double value;
char opr;
{
	double *p;
	void change_value();

	p=&(LookUp[change_index].ParaPr->KinaPr+ienv)->para[paraKey];

	change_value(p, opr, value);

/*	if ( (paraKey == TU)||( paraKey==CT)){
		doubleMinusCycletime(change_index,ienv) = turnover(change_index,ienv)*cycletime(change_index,ienv);
	}*/
}

/*  12/20/97 QS
There is no destination in the rule ==> all corresponding destionation;
e.g., R mr * 0 in the VE rule
Change value for all exiting rules
*/
void change_kinarray_value_all( paraKey, opr,value,ienv)
double value;
int paraKey,ienv;
char opr;
{
	int iindex,NumOfChange=0;
	void change_value();
	double *p;

	switch (paraKey ) {
		case MUTATION:
			NumOfChange = LookUp[change_index].ParaPr->NumOfMu;
			break;
		case G0T:
			NumOfChange = LookUp[change_index].ParaPr->NumOfCo;
			break;
		case BLEBT:
			NumOfChange = LookUp[change_index].ParaPr->NumOfBl;
			break;
	}

	for ( iindex=0; iindex < NumOfChange; iindex++){
		switch (paraKey ) {
			case MUTATION:
				p=&(((LookUp[change_index].ParaPr->KinaPr+ienv)->MuPr+iindex)->rate);
				break;
			case G0T:
				p=&(((LookUp[change_index].ParaPr->KinaPr+ienv)->CoPr+iindex)->time);
				break;
			case BLEBT:
				p=&(((LookUp[change_index].ParaPr->KinaPr+ienv)->BlPr+iindex)->time);
				break;
		}

		change_value(p, opr, value);
	}
}

void change_kinarray_value_2 ( paraKey,icelltype,
							  opr,value,ienv)
double value;
int paraKey,ienv;
unsigned int icelltype;
char opr;
{
	void change_value();
	double *p;
	int cell_index=-1,iindex,found=false;

	switch (paraKey ) {
		case MUTATION:
			if ( opr =='=')	{
				muindex++;
				MuLookUp(change_index,muindex,ienv)=icelltype;
				cell_index=muindex;
			}
			else {
				for ( iindex=0; (iindex <= muindex)&&(found==false); iindex++){
					/* find oppropriate destination index in the list */
					if (MuLookUp(change_index,iindex,ienv)==icelltype) {
						cell_index=iindex;
						found=true;
					}
				}
			}
			if ( cell_index >= 0)
				p=&(((LookUp[change_index].ParaPr->KinaPr+ienv)->MuPr+cell_index)->rate);
			
			break;
		case G0T:
			if ( opr =='=')	{
				coindex++;
				CoLookUp(change_index,coindex,ienv)=icelltype;
				cell_index=coindex;
			}
			else {
				for ( iindex=0; (iindex <= coindex)&&(found==false); iindex++){
					if (CoLookUp(change_index,iindex,ienv)==icelltype) {
						cell_index=iindex;
						found=true;
					}
				}
			}
			if ( cell_index >= 0)
				p=&(((LookUp[change_index].ParaPr->KinaPr+ienv)->CoPr+cell_index)->time);
			
			break;
		case BLEBT:
			if ( opr =='=')	{
				blindex++;
				BlLookUp(change_index,blindex,ienv)=icelltype;
				cell_index=blindex;
			}
			else {
				for ( iindex=0; (iindex <= blindex)&&(found==false); iindex++){
					if (BlLookUp(change_index,iindex,ienv)==icelltype) {
						cell_index=iindex;
						found=true;
					}
				}
			}
			if ( cell_index >= 0)
				p=&(((LookUp[change_index].ParaPr->KinaPr+ienv)->BlPr+cell_index)->time);
			
			break;
	}

	if ( cell_index >=0)
		change_value(p, opr, value);
}

void change_value(p, opr, value)
double *p; 
char opr;
double value;
{
	if (p==NULL) return;
	switch ( (int) opr )
    {
       case (int) '*':
          *p  *= value;
          break;
       case (int) '/':
          *p  /= value;
          break;
       case (int) '+':
          *p  += value;
          break;
       case (int) '-':
          *p  -= value;
          break;
       case (int) '=':
          *p  = value;
          break;
       default:
#ifndef TESTMPI
		   MsgBoxWarning (" Operator not recognized");
#else
		   printf(" Operator not recognized\n");
#endif
          *p  = value;
          break;
    }
}

void change_kill_rates(d_drug,
		       opr, value,ienv)
char d_drug[], opr;
double value;
int ienv;
{
	int di, getdrugindex();
	double *p;

	if ( strcmp(d_drug,"") == strcmpSAME){
	   /* No drug name. Apply rule to all drugs (?)*/
	  for ( di = 1; di <= ndrugs; di++ )
		  	change_kill_rates( drugname[di],opr,value,ienv);
	  return;
	}
	
    di=getdrugindex(d_drug);
	
/*	if ( (di=getdrugindex(d_drug)) < 0 )
//	   MsgBoxWarning ("Drug not found");*/
	if ( di > 0 ) {
		p = &(drugkill(di,change_index,ienv));
		change_value(p, opr, value);
	}
}


int getdrugindex (thisDrug)	
char *thisDrug;
{
  /* Find out the index into the array which matches the drug acting upon
   * the cell type.
   */
  int di;

  di = 1;
#ifdef REALUNIX
  while ( di <= ndrugs && ( strcasecmp( thisDrug, drugname[di] ) != strcmpSAME ) )
#else
  while ( di <= ndrugs && ( _stricmp( thisDrug, drugname[di] ) != strcmpSAME ) )
#endif
     di++;
  if ( (di >= 1) && (di <= ndrugs ))
   return (di);
  else {
    /* MsgBoxWarning( "Drug name not found" );*/
  /*   fprintf(stdout,"Drug name not found");*/
	 return (-1);
  }
}


void change_gomp_param( thisKey, j, opr, value,ienv)
int thisKey, j, ienv;
char opr;
double value;
{
	double *pGompValue;

	switch (thisKey) {
	case parseKeyGP:
		pGompValue = &(GompRule[j][ienv].gp_or_inv.GP);
		break;
	case parseKeyGS:
		pGompValue = &(GompRule[j][ienv].GS);
		break;
	default:
#ifndef TESTMPI
		MsgBoxWarning("Bad Gompertz parseKey");
#else 
		printf("Bad Gompertz parseKey\n");
#endif
	}
   switch ( (int) opr ){
         case (int) '*':
            *pGompValue *= value;
            break;
         case (int) '/':
            *pGompValue /= value;
            break;
         case (int) '+':
            *pGompValue += value;
            break;
         case (int) '-':
            *pGompValue -= value;
			break;
		 case (int) '=':
		 default:
			*pGompValue = value;
      }
}
/* --------------------------------------------------------------------
 * Function:  evaluate_store_rule
 *            Parse a rule in the form "VAR (PHENOTYPE) OPERATOR VALUE."
 * --------------------------------------------------------------------
 */
void evaluate_store_rule (iRule)
   int iRule;
{
   char  temp[SMALLBUFFER];
   int i,nlevel;
   int is_operator();
   int thisKey,lookupKey();
   int iGompRule;
   struct action  *storeRule;
   struct LevelCond *storeLC;
   void CheckIfThereIsClass();

   storeRule = &(Rules[iRule].action);
   storeLC = &(Rules[iRule].LevelCond[0]);
   
   i = 0;
   strcpy( temp, read_word(storeRule->rule_name , &i ) );
   skip_blanks( storeRule->rule_name, &i );

   thisKey = lookupKey( temp);

   storeRule->ruleKey=thisKey;

	if (thisKey < 0)  {
#ifndef TESTMPI
		MsgBoxWarning( "Key not found");
#else
		printf("Key not found\n");
#endif
	}

			
   if ((is_assignment(storeRule->rule_name ))||(Class[storeLC->iclass].class_type==2) )
		storeRule->type=ASSIGNRULE;
   else storeRule->type=NOTASSIGNRULE;

   /* read the optional destination phenotype or drugname.
    */

   strcpy( storeRule->dest_name, "" );

   if ( (thisKey != parseKeyMA) && ( thisKey != parseKeyPO)) {
	    if ( not is_operator( storeRule->rule_name[i] ) )
		{
			if ((thisKey != parseKeyDS) && (thisKey != parseKeyCS)) {
				 strcpy( storeRule->dest_name,
						 read_word( storeRule->rule_name, &i ) );
				 skip_blanks( storeRule->rule_name, &i );
			
				 /*     added by Qingshou, 10/26/98      */
				 CheckIfThereIsClass(storeRule->dest_name,iRule);
			}
	   }
   }
   else return;

   /* Here, Rules[iRule].EnvirCond[0].envir_name keeps destination name.
	  Then, Rules[iRule].EnvirCond[0].ienv can be easily copied by comparing
	  the envir_name in function copy_env_gr().
   */

   if ( iRule < TotalSingleRules )
		strcpy(Rules[iRule].EnvirCond[0].envir_name, storeRule->dest_name);
                 
   /* read the operator
    */
   if ((thisKey != parseKeyDS) && (thisKey != parseKeyCS)) {
		storeRule->opr = storeRule->rule_name[i];
	    i++;
   }
  
   skip_blanks( storeRule->rule_name, &i );
   if ( ! is_operator(storeRule->opr)) {
	   if (  ! (thisKey== parseKeyNEG )&& !(thisKey== parseKeyDS)&& !(thisKey== parseKeyCS)) {
#ifndef TESTMPI
	   MsgBoxWarning("Bad operator");
#else
	   printf("Bad operator\n");
#endif
	   }
   }
   /* extract the floating point value
    */
   strcpy( temp, read_word( storeRule->rule_name, &i ) );
   storeRule->value = atof( temp );
  
/* Now make the changes.*/
   switch ( thisKey ){
        case parseKeyDR:
        case parseKeyCT:
			/*		//In the future, cytostatic rules should
			//  be able to put cells into G0.
			//This will be done with the "invisible"
			//  hetclasses,adding a new level "G0".*/
		case parseKeyDT:
        case parseKeyDE:
			 	find_mincyctime(thisKey,storeRule->value,storeRule->opr);
				if (strcmp(storeRule->dest_name, "")  != strcmpSAME ){
					storeRule->type=CYTOSTATICRULE;
					Kinetic_Changes ++;
				}
			 break;
		case parseKeyCO:
			if ( storeRule->opr =='='){
				if  (( Base_Conversion_Time == 0) || ( storeRule->value < Base_Conversion_Time ))
					Base_Conversion_Time = storeRule->value;
			}

			find_mincyctime(thisKey,storeRule->value,storeRule->opr);
			break;
		case parseKeyDS:
		case parseKeyCS:
		case parseKeyKR:
		case parseKeyMR:
		case parseKeyBT:
		case parseKeyNEG:
		case parseKeyPO:
			 break;
		case parseKeyGP:
		case parseKeyGS:
		  	 nlevel=0;
		   	 iGompRule = GetGompRuleIndex(Rules[iRule].LevelCond[nlevel].iclass,
									Rules[iRule].LevelCond[nlevel].ilevel);	
			 if ( iGompRule < 1 ) {
				if (GompRule[NumGompRules][0].cn !=Rules[iRule].LevelCond[nlevel].iclass )
						Number_of_Gomp_Classes++;
				NumGompRules++;
				GompRule[NumGompRules][0].cn=Rules[iRule].LevelCond[nlevel].iclass;
				GompRule[NumGompRules][0].level=Rules[iRule].LevelCond[nlevel].ilevel;
				KineticsModel=1;
			 }
			 if ( iRule < TotalSingleRules ) 
				 change_gomp_param(thisKey, NumGompRules, storeRule->opr, storeRule->value,0 );
				 				
			break;
		case parseKeyPG:
			/* proportinal gompertz rule */
			nlevel=0;
			if (GompRule[NumGompRules][0].cn !=Rules[iRule].LevelCond[nlevel].iclass )
				Number_of_Gomp_Classes++;
			NumGompRules++;
			GompRule[NumGompRules][0].cn=Rules[iRule].LevelCond[nlevel].iclass;
			GompRule[NumGompRules][0].level=Rules[iRule].LevelCond[nlevel].ilevel;
			KineticsModel=1;
			GompRule[NumGompRules][0].gp_or_inv.GP = PROPGOMP;
			GompRule[NumGompRules][0].gpmin = storeRule->value;
			IfAnyPGP=true;
			strcpy( temp, read_word( storeRule->rule_name, &i ) );
			 /* get class and level of the controler */
			read_to_eol(temp,storeRule->rule_name,&i);
			parse_class_level_other(temp);
		    break;
		default:
#ifndef TESTMPI
			MsgBoxWarning("Unknown Rule Key");
#else 
		    fprintf(stdout,"Unknown Rule Key(%d)\n",thisKey);
#endif
	}
}

void getlookupid(int x, int y, int z, int cn, int si,unsigned int jcelltype,int ienv)
{
	int di;
	unsigned int icelltype;

		migindex++;
		di=x*Class[cn].my*Class[cn].mz+y*Class[cn].mz+z;
		icelltype = macro_block_length*(di-si) + jcelltype;
		MigLookUp(change_index,migindex,ienv)=icelltype;
}

void build_migid(int irule,unsigned int jcelltype,int ienv)
{
	int cn,si,x,y,z,mx,my,mz,temp;

	cn=Rules[irule].LevelCond[0].iclass;
	si=getcelllevelbyclass(jcelltype,cn);
	
	mx=Class[cn].mx;
	my=Class[cn].my;
	mz=Class[cn].mz;

	if ( ( mx>0 ) && ( my>0 ) && (mz > 0 ) ) {
	    x=si/(my*mz);
		temp = si % (my*mz);
		y=temp / mz;
		z= temp % mz;
		
		if (  x < (mx-1 )) {
			getlookupid((x+1),y,z,cn,si,jcelltype,ienv);
		}
		if ( y < (my-1)) {
			getlookupid(x,(y+1),z,cn,si,jcelltype,ienv);
		}
		if ( z < (mz-1) ) {
			getlookupid(x,y,(z+1),cn,si,jcelltype,ienv);
		}
		if ( x > 0) {
			getlookupid((x-1),y,z,cn,si,jcelltype,ienv);
		}
		if ( y > 0) {
			getlookupid(x,(y-1),z,cn,si,jcelltype,ienv);
		}
		if ( z >0 ) {
			getlookupid(x,y,(z-1),cn,si,jcelltype,ienv);
		}
	}

/*if (migindex > LookUp[change_index].ParaPr->NumOfMet )
		printf("\n");*/
		
		migtime(change_index,ienv)=Rules[irule].action.value;
}

void cal_pre_post_prod() 
{
 int post_product,iclass;

  
   Class[Number_of_Classes -1].post_product=1;
   post_product = 1;

   for ( iclass = Number_of_Classes -1 ; iclass >0; iclass-- ){
      post_product *= Class[iclass].no_levels;
	  Class[iclass-1].post_product = post_product; /* number of nodes not including this node */
	  Class[iclass].pre_product = post_product;  /* number of nodes including this node */
   }

	Class[0].pre_product=Class[0].post_product*Class[0].no_levels;

 }

/* --------------------------------------------------------------------
 * Function:  ApplyThisRule
 *            Parse a rule in the form "VAR (PHENOTYPE) OPERATOR VALUE."
 * --------------------------------------------------------------------
 */
void ApplyThisRule ( irule,icelltype,ienv)
	int irule,ienv;
	unsigned int icelltype;
{

int di, dest_gap, get_dest_gap(),cn,lnum,ruleindex=0,jlevel;
int thisKey,paraKey;
double value;
char opr, dest[SMALLBUFFER];

thisKey = Rules[irule].action.ruleKey;
opr =  Rules[irule].action.opr;
value =  Rules[irule].action.value;
strcpy ( dest, Rules[irule].action.dest_name);

/*  First, set the array to change.*/
   	switch ( thisKey ){
        case parseKeyDT:
        case parseKeyTU:
			/* paraKey=DTMINUSCT; 
			paraKey=TU;*/
		case parseKeyDE:
			paraKey=DE; break;
		case parseKeyDS:
			paraKey=DS; break;
		case parseKeyCS:
			paraKey=CS;	break;
		case parseKeyCT:
			paraKey=CT; break;
        case parseKeyBT:
			paraKey = BLEBT; break;
	    case parseKeyMR:
			paraKey = MUTATION;
			break;
        case parseKeyCO:
			paraKey=G0T; break;
        case parseKeyKR:
        case parseKeyGP:
        case parseKeyGS:
		case parseKeyPG:
		case parseKeyMA:
		case parseKeyNEG:
		case parseKeyPO:
			break;
		default:
#ifndef TESTMPI
			MsgBoxWarning("Rule Key not matched");
#else 
			printf("Rule Key not matched\n");
#endif
	}

/* Now make the changes.*/

   switch ( thisKey ){
        case parseKeyDR:
        case parseKeyCT:
	    case parseKeyDT:
        case parseKeyDE:
				change_kinarray_value_1 (paraKey,opr,value,ienv);
				break;
		case parseKeyDS:
		case parseKeyCS:
				change_kinarray_switch (paraKey,(int)value,ienv);
				break;
		case parseKeyMR:
		case parseKeyBT:
		case parseKeyCO:
			if ( Rules[irule].action.DestClassID != NODESTCLASS) {
				cn = Rules[irule].action.DestClassID;
				lnum = getcelllevelbyclass(icelltype,cn);
			}
			else {
				/* not general rule */
				lnum = Rules[irule].LevelCond[0].ilevel;
				cn = Rules[irule].LevelCond[0].iclass;
			}

			for ( di = 0
				; ( di < Class[cn].no_levels ) &&
					( strcmp( Class[cn].Level[di].level_name, dest ) != 0 )
				;	di++ )
			; /*//Find destination levelname.*/
			if (di >= Class[cn].no_levels ) {
				if (  strcmp(dest, "") == strcmpSAME || strcmp(dest, "Dyn._Het.") == strcmpSAME) {
					change_kinarray_value_all(paraKey,opr,value,ienv);
				}
				else {
#ifndef TESTMPI
				MsgBoxWarning("destination levelname not found");
#else 
				printf("destination levelname not found\n");
#endif
				}
			}
			else {
				dest_gap = get_dest_gap (cn, lnum, di);
			
				icelltype += dest_gap;
				change_kinarray_value_2 (
						paraKey,icelltype,
						opr,value,ienv);
			}
			break;
		case parseKeyMA:
			build_migid(irule,icelltype,ienv);
			break;
		case parseKeyKR:
			change_kill_rates(dest,opr,value,ienv);
			break;
		case parseKeyNEG:
			/* add flag to lookup array */
				LookUp[change_index].mark= NEG;
				break;
		case parseKeyGP:
		case parseKeyGS:
			cn = Rules[irule].LevelCond[0].iclass;
			lnum = Rules[irule].LevelCond[0].ilevel;
			for ( jlevel=1;jlevel < Rules[irule].nLevelConds;jlevel++) {
				/* find maximum of class ID within location Het.class */
				if ( ((Rules[irule].LevelCond[jlevel].iclass > cn ) && 
					( Class[Rules[irule].LevelCond[jlevel].iclass].class_type <=3)) ||
					( Class[Rules[irule].LevelCond[jlevel].iclass].class_type >3 )){
					cn = Rules[irule].LevelCond[jlevel].iclass;
					lnum = Rules[irule].LevelCond[0].ilevel;
				}
		
			}
			
			if ((Class[cn].class_type <= 3) || (Rules[irule].nLevelConds ==1) ) { 
				/* gompertz rule only for location Het. or single other class */
				ruleindex = GetGompRuleIndex(cn,lnum);
				change_gomp_param(thisKey,ruleindex,opr,value,ienv );
			}
			else {
#ifndef TESTMPI
			MsgBoxWarning("Gompertz rule is applied an invalid class.");
#else 
		    fprintf(stdout,"Gompertz rule is applied invalid an class(%d)\n");
#endif
			}
			break;
		case parseKeyPG:
		case parseKeyPO:
		    break;
		default:
#ifndef TESTMPI
			MsgBoxWarning("Unknown Rule Key");
#else 
		    fprintf(stdout,"Unknown Rule Key(%d)\n",thisKey);
#endif

	}
}
/* End of ApplyThisRule */

int getallmigrate(int irule)
{
	/* return a number of migration for celltype: LookUp[change_index].LookUpId  */
	int cn,lnum,x,y,z,mx,my,mz,temp,nmet=0;

	cn=Rules[irule].LevelCond[0].iclass;
	lnum=getcelllevelbyclass(LookUp[change_index].LookUpId,cn);
	
	/* get dimension (mx,my,mz) for the macro level */
	mx=Class[cn].mx;
	my=Class[cn].my;
	mz=Class[cn].mz;

	if ( ( mx>0 ) && ( my>0 ) && (mz > 0 ) ) {
		/* get the location (x,y,z) for this level */
			x= lnum/(my*mz) ;
			temp = lnum % (my*mz);
			y= temp / mz;
			z= temp % mz;
						
		if (  x < (mx-1 )) 	nmet++;
		if ( y < (my-1)) 	nmet++;
		if ( z < (mz-1) ) 	nmet++;
		if ( x > 0) 		nmet++;
		if ( y > 0) 		nmet++;
		if ( z > 0)			nmet++;
	}

	return nmet;
}

void CountThisRule (irule)
int irule;
{
int thisKey;

thisKey = Rules[irule].action.ruleKey;

if ( ( thisKey == parseKeyMA) || ( strcmp(Rules[irule].action.dest_name,"" )!=strcmpSAME )) {
   	switch ( thisKey ){
		case parseKeyMA:
			LookUp[change_index].ParaPr->NumOfMig = getallmigrate(irule);
			return;
        case parseKeyBT:
			LookUp[change_index].ParaPr->NumOfBl++; 
			return;
	    case parseKeyMR:
			if (strcmp(Rules[irule].action.dest_name, "Dyn._Het.") != strcmpSAME ){
				if (Rules[irule].action.opr == '='){
					LookUp[change_index].ParaPr->NumOfMu++;
				}
			}
			return;
        case parseKeyCO:
			if (Rules[irule].action.opr == '='){
				LookUp[change_index].ParaPr->NumOfCo++;
			}
			return;
       	/*	default:
			printf("Rule Key not counted\n");*/
	}
 }
}

void read_into_temp	( temp, rule_buff, p_idx)
	char *temp; char *rule_buff; int *p_idx;
{
	strcpy( temp, read_word( rule_buff, p_idx ));
#ifdef _DEBUG
/*	fprintf(eout, "read_into_temp: temp is %s\n", temp);*/
#endif
}

void read_to_eol( temp, rule_buff, p_idx)
	char *temp; char *rule_buff; int *p_idx;
{
	/*read_word skips white space first, & has an internal buffer*/
	strcpy( temp, read_line( rule_buff, p_idx ));
}

int lookupKey(s)
char *s;
{
	int i;
         
	for (i=1; i<=NparseKeys; i++) {
#ifdef REALUNIX
		if (!strcasecmp(s,parseKeys[i])) 
#else
        if (!_stricmp(s,parseKeys[i])) 
#endif
                 return(i);
	    }

	return(-1);
	  
}

void init_rule(int C, int L,int *R)
{
	strcpy(Class[C-1].Level[L-1].Rule[*R].rule_name,Rules[TotalSingleRules].action.rule_name);
	Rules[TotalSingleRules].nLevelConds=1;
	Rules[TotalSingleRules].EnvirCond[0].ienv=0;
	Rules[TotalSingleRules].nEnvirConds=1;
	strcpy(Rules[TotalSingleRules].LevelCond[0].class_name,Class[C-1].class_name);
	strcpy(Rules[TotalSingleRules].LevelCond[0].level_name,Class[C-1].Level[L-1].level_name);
	strcpy(Rules[TotalSingleRules].LevelCond[0].clopr,"");
	Rules[TotalSingleRules].LevelCond[0].iclass=C-1;
	Rules[TotalSingleRules].LevelCond[0].ilevel=L-1;
	Class[C-1].Level[L-1].no_rules = ++(*R);
	TotalSingleRules++;
	TotalRules++;
}

void read_nomacro_rule(char *rule_buff,int *idx,int C, int L,int *R)
{
	read_to_eol(Rules[TotalSingleRules].action.rule_name,rule_buff,idx);
   	init_rule(C,L,R);
}

void expand_macro_rule(char *rulebuff,int *idx,int C, int *L,int *R)
{
	char temp_rule[1024],keyC[10];
	int lnum,itemp=0,idx_bak;

	idx_bak=*idx;
	read_to_eol(temp_rule,rulebuff,idx);
	strcpy(keyC,read_word(temp_rule,&itemp));
	if ( strcmp(keyC,"ma")==strcmpSAME) {
		Class[C-1].mx=(short int)atoi(read_word(temp_rule,&itemp));
		Class[C-1].my=(short int)atoi(read_word(temp_rule,&itemp));
		Class[C-1].mz=(short int)atoi(read_word(temp_rule,&itemp));
		Rules[TotalSingleRules].action.value=atof(read_word(temp_rule,&itemp));
		strcpy(Rules[TotalSingleRules].action.rule_name,temp_rule);
		/* total levels for this macro class */
		lnum=Class[C-1].mx*Class[C-1].my*Class[C-1].mz;
		init_rule(C,*L,R);
		Class[C-1].no_levels += lnum-1;
		*L += lnum-1;
		Rules[TotalSingleRules-1].LevelCond[0].ilevel = lnum;
	}
	else {
		 *idx=idx_bak;
		 read_nomacro_rule(rulebuff,idx,C,*L,R);
	}
}

int parse_base_model(text) /* widget, client_data, call_data )*/
char *text;
{
   char rule_buff[2048], temp[200], temp2[100], cthisKey[20];
   int C, L, R, iRule,cn;
   int i,idx, length, line, thisKey;
   extern int atoi();
   extern double atof();
   void fill_clevel();

#ifdef MPI
    strcpy (ofilname, "parse_base_model.out");
#else
	strcpy (ofilname, WorkingDir);
	strcat (ofilname, "\\parse_base_model.out");
#endif
	/*if (( parse_eout = fopen(ofilname,"w")) ==  NULL)
	//	MessageBox(NULL,"ERROR: opening parse_base_model.out",
	//		"ERROR", MB_ICONEXCLAMATION | MB_OK);
	//else
	//	MessageBox(NULL,"success: opening c:\\temp\\parse_base_model.out",
	//		"SUCCESS", MB_ICONEXCLAMATION | MB_OK);
    */
      
	clear_base_model( );
       
	strncpy( rule_buff, text, sizeof( rule_buff ) );
      Number_of_Classes = 0;
      C = 0;
      L = 0;
      R = 0;
	  TotalRules=0;
	  TotalSingleRules=0;
      idx = 0;
      line = 1;
      length = strlen( rule_buff );
	  read_to_eol(temp,rule_buff,&idx); /* to read the version line in default.phenotypes*/

      while ( idx < length ) {
		while (rule_buff[idx] == '#')
		{ 
			read_to_eol(temp,rule_buff,&idx);
		}
		read_into_temp(temp,rule_buff,&idx);
		thisKey = lookupKey( temp);
		strcat(temp, "=>");

               	/*itoa(thisKey, cthisKey, 10);*/ 

        sprintf(cthisKey, "%d", thisKey);
   		strcat(temp, cthisKey);
		/* This messagebox will cause cnvasplot to lose focus
		// On regaining focus, canvasplot will do initialCellForm,
		//  which leads back here!
		//MessageBox(NULL,temp,"thisKey", MB_ICONEXCLAMATION | MB_OK);
		*/
		  switch ( thisKey ){
            case parseKeyDT:
				read_into_temp(temp,rule_buff,&idx);
                Base_Doubling_Time = atof( temp );
                 /*if ( parameter[tcapCT] == 1 && parameter[tcapDR] == 1 )
                 //    fprintf( parse_eout,"Doubling time ignored.\n" );
                //else 
                //     skip_blanks( rule_buff, &idx );
		//read_into_temp(temp,rule_buff,&idx));
                //     Base_Death_Rate = atof( temp );*/
            break;
			case parseKeyDE:
                read_into_temp(temp,rule_buff,&idx);
				Base_Death_Time = atof( temp );
				break;
			case parseKeyDS:
                read_into_temp(temp,rule_buff,&idx);
				Base_Death_Switch = atoi( temp );
				break;
            case parseKeyCT:
                read_into_temp(temp,rule_buff,&idx);
				Base_Cycling_Time = atof( temp );
				break;
            case parseKeyCS:
                read_into_temp(temp,rule_buff,&idx);
				Base_Cycling_Switch = atoi( temp );
				break;
            /*case parseKeyTU:
				read_into_temp(temp,rule_buff,&idx);
                Base_Turnover = atof( temp );
				break; */
	  /*Gompertz*/
            case parseKeyGP:
				read_into_temp(temp,rule_buff,&idx);
				Base_GompPlateau = atof(temp);
				GompRule[0][0].gp_or_inv.GP=Base_GompPlateau;
				if ( Base_GompSplit==0.0 )
					Base_GompSplit=1.0; 
				/*MessageBox(NULL,"Setting Base_GompPlateau",temp,MB_ICONEXCLAMATION | MB_OK);*/
				break;
            case parseKeyGS:
				read_into_temp(temp,rule_buff,&idx);
				Base_GompSplit = atof(temp);
				GompRule[0][0].GS=Base_GompSplit;
				break;
            case parseKeyKM:
				read_into_temp(temp,rule_buff,&idx);
				KineticsModel = atoi(temp);
				break;
            case parseKeyKC:
				read_into_temp(temp,rule_buff,&idx);
				KineticsUnchecked = atoi(temp);
				break;
            case parseKeyK:
				read_into_temp(temp,rule_buff,&idx);
				i = 1;
    
    
#ifdef TESTMPI
				while ( (i <= ndrugs) && (strcmp( temp, drugname[i] ) != 0 ))
#else 
while ( (i <= ndrugs) && (_stricmp( temp, drugname[i] ) != 0 ))
#endif
			i++;
				if ( i > ndrugs )
					  strcpy( drugname[++ndrugs], temp );
				/*// Prevents duplication of drugnames
				//  but allows a change in logkill.*/
				read_into_temp(temp,rule_buff,&idx);
				base_kill[i] =  atof( temp );
            break;
            case parseKeyC:
				read_to_eol(Class[C].class_name,rule_buff,&idx);
                Class[C].no_levels = 0;
				Class[C].class_type = 0;
                C++;
                L = 0;
            break;
			case parseKeyCY:
				read_into_temp(temp,rule_buff,&idx);
				if (C > 0)
					Class[C-1].class_type = atoi(temp); /* class_type */
				break;
			case parseKeyL:
				read_to_eol(Class[C-1].Level[L].level_name,rule_buff,&idx);
				Class[C-1].Level[L].no_rules = 0;
				Class[C-1].no_levels = ++L;
				R = 0;
            break;
            case parseKeyR:
				if ( Class[C-1].class_type == 2 )
					expand_macro_rule(rule_buff,&idx,C,&L,&R);
				else 
					read_nomacro_rule(rule_buff,&idx,C,L,&R);
            break;
			case parseKeyGR:
				Number_of_Classes = C;
				/* this is general rule in the last part of the file */
				read_to_eol(temp,rule_buff,&idx);
				if ((parse_general_rule(temp,TotalRules)))
				TotalRules++;
			break;
            default:
               eprint( "WARNING (parse_base_model): " ); 
	       if(rule_buff[idx] != '\n') read_to_eol(temp2,rule_buff,&idx);
               fprintf(eout, "unknown key word %s and %s on line %d [parse_base_model]\n",
                        temp, temp2, line );
 		}/* end of switch*/
		if (rule_buff[idx] == '\n') idx++;
		line++;
	  }/*end of while*/
	  
	  Number_of_Classes = C;
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
	mincycletime =min(Base_Cycling_Time,Base_Death_Time);

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
return(TRUE);
}

void fill_clevel() 
{
int k,j,L,C,found,ienv=0;
char classname[30], levelname[30],strMsg[256];

	for ( k=1; k<= NumGompRules; k++ ) {
		for ( j=0; j< GompRule[k][ienv].nclevel; j++) {
			found = false;
			for ( C=0; C< Number_of_Classes; C++ ) {
				for ( L=0; L< Class[C].no_levels; L++) {
					strcpy(classname,Class[C].class_name);
					strcpy(levelname,Class[C].Level[L].level_name);
					if ((!(strcmp(classname,GompRule[k][ienv].cc[j].cname))) && 
						 (!(strcmp(levelname,GompRule[k][ienv].cl[j].cname))))
					{
							GompRule[k][ienv].cl[j].clevel = L;
							GompRule[k][ienv].cc[j].cclass = C;
							found = true;
							break;
					}
			}
			if ( found ) break;
		}
		if ( found == false ){
			sprintf(strMsg, "One of the VE rule will be ignored.");
#ifndef TESTMPI
			MsgBoxWarning(strMsg);
#else
			printf(strMsg);
#endif
			GompRule[k][ienv].cc[j].cclass = -1;
			GompRule[k][ienv].cl[j].clevel = -1;
		}
	}
}

}

void fill_env()
{
	int i,n,found;
	void fill_env_gr();

    nenvirons = 1;
	 for ( i = TotalSingleRules - Kinetic_Changes; i < TotalSingleRules; i++ )
/*	 for ( i = 0 ; i < TotalSingleRules; i++ ) this line is for test */
	 {
	  /* Questions for Bill:
		1. shouldnt it be "n <= 1" below?
		2. shouldnt you set kinetic_rule[i].environment to
		      Kinetic_Rules[n].environment if it is found?

		    YES!  Qingshou and Bill (6/18/98) 
	  */
		 found = 0;
		 for ( n = i - 1; n >= 0; n-- ){
				if (strcmp(Rules[i].action.dest_name,
					Rules[n].action.dest_name) == strcmpSAME)
				{
				   found = 1;
				   Rules[i].EnvirCond[0].ienv = Rules[n].EnvirCond[0].ienv;
				}
		 }
		 if (found == 0){
				 nenvirons = nenvirons + 1;
				 Rules[i].EnvirCond[0].ienv = nenvirons - 1;
/* newcode added by sai 7/21/98 */
				 strcpy(Environments[nenvirons-1],Rules[i].action.dest_name); 				 
				 
		 }
	 }

	 if ( TotalRules > TotalSingleRules )
		fill_env_gr();
}

void fill_env_gr()
{
	int irule,jrule,ienv;
	int found;
	/* char env_name[20]; Here, 20 is not large enough. If length(drug name) is over 
	   20, the program will crash
	*/
	char env_name[SMALLBUFFER]; 

	for ( irule = TotalSingleRules; irule < TotalRules; irule ++ ) {
		for ( ienv = 0; ienv < Rules[irule].nEnvirConds; ienv ++ ) {
			found=false;
			for ( jrule = 0; jrule < TotalSingleRules; jrule ++ ) {
				strcpy(env_name,Rules[jrule].EnvirCond[0].envir_name);
				if (strcmp( Rules[irule].EnvirCond[ienv].envir_name,env_name) == strcmpSAME) 
				{
					Rules[irule].EnvirCond[0].ienv = Rules[jrule].EnvirCond[0].ienv;
					found=true;
				}
			}
			if (found == false ) {
				Rules[irule].EnvirCond[0].ienv =-1; /* there is no matched env */
			}
		}
	}
}

void sort_rules(struct Rule Rules[], int left,int right)
{
	int i, last;
	void swap();

	if ( left >= right ) 
		return;

/*	swap(Rules, left, (left+right)/2); */
	last = left;
	for ( i = left +1 ; i <= right; i++ )
		if ( Rules[i].action.type < Rules[left].action.type ) 
			swap( Rules,++last,i);

	swap(Rules,left,last);
	sort_rules(Rules,left,last-1);
	sort_rules(Rules,last+1,right);
}

void swap( struct Rule Rules[], int i, int j )
{
	struct Rule temp;

	memcpy(&temp,&Rules[i],sizeof ( struct Rule ));
	memcpy(&Rules[i],&Rules[j],sizeof ( struct Rule ));
	memcpy(&Rules[j],&temp,sizeof ( struct Rule ));
}
/************************************************************
/*  Rules[iRule].nLevelConds ==0 == > all classes and leveles
*************************************************************/
int is_match_level(int itype, int iRule)
{
	int iLevel,C,L;

	for ( iLevel = 0; iLevel < Rules[iRule].nLevelConds; iLevel++) {
		C = Rules[iRule].LevelCond[iLevel].iclass;
		L = getcelllevelbyclass(itype,C);
		if ( L==-1 )
			return FALSE;
		else {
			if ( iRule >= TotalSingleRules ) {
				if ( strcmp(Rules[iRule].LevelCond[iLevel].clopr,"=")==strcmpSAME) {
					if (Rules[iRule].LevelCond[iLevel].ilevel != L )
						return FALSE;
				}
				else if ( strcmp(Rules[iRule].LevelCond[iLevel].clopr,"<=")==strcmpSAME) {
					if (Rules[iRule].LevelCond[iLevel].ilevel < L )
						return FALSE;
				}
				else if ( strcmp(Rules[iRule].LevelCond[iLevel].clopr,">=")==strcmpSAME) {
					if (Rules[iRule].LevelCond[iLevel].ilevel > L )
						return FALSE;
				}
			}
			else {
				if ( Class[Rules[iRule].LevelCond[0].iclass].class_type == 2 ){
					/* For macro env, Rules[iRule].LevelCond[iLevel].ilevel=Class[].no_levels */
					if ( Rules[iRule].LevelCond[iLevel].ilevel <= L  )
						return FALSE;
				}
				else {
					if (Rules[iRule].LevelCond[iLevel].ilevel != L )
						return FALSE;
				}
			}
		}
	}

	return TRUE;

}
/****************************************************
*  Rules[iRule].nEnvirConds ==0 == > all envirments *
*****************************************************/
int is_match_env(int env,int iRule)
{
	int ienv;

	for ( ienv = 0; ienv< Rules[iRule].nEnvirConds; ienv++) {
		if ( Rules[iRule].EnvirCond[ienv].ienv != env ) 
			return FALSE;
	}

	return TRUE;

}


void flush_line( buffer, i )
   char buffer[];
   int *i;
{
   while ( ((size_t) *i) < strlen( buffer ) && buffer[*i] != '\n' )
      *i = *i + 1;
   *i = *i + 1;
}

/*extern int EXPORT PASCAL getcellindices (  cn, si )*/
int getcellindices (  cn, si )
   int cn, si;
{
   unsigned int i,j;
   int block_length, block_gap;

   /*block_length = 1;
   for ( k = cn + 1; k < Number_of_Classes; k++ )
      block_length = block_length * Class[k].no_levels;
   block_gap = block_length * Class[cn].no_levels;
   */
   block_length = Class[cn].post_product;
   block_gap = Class[cn].pre_product;

   ncellindices = 0;

   for ( i = ( si * block_length ); i < ntypes; i = i + block_gap )
      for ( j = i; j < ( i + block_length ); j++ ) {
		 	cellindices [++ncellindices] = j + 1;
	 }
   return ncellindices;
}

int getcelllevelbyclass ( icelltype, cn)
unsigned int icelltype;
int cn;
{
   int ilevel=-1,temp;
  
   if ( ( cn>= 0 ) && ( icelltype > 0 )) { 
	   if ( Class[cn].pre_product > 0 )
		  temp = ( icelltype - 1 ) % Class[cn].pre_product;
	   else return -1;

	   if ( Class[cn].post_product > 0 )
		 ilevel = temp/ Class[cn].post_product;
	   else return -1;
   }
   return ilevel;
}

int getlocationclass()
{
   int i;
   for ( i = 0; i < Number_of_Classes; i++)
	   if (Class[i].class_type == 1)
	      return(i);
   return(-1);
}

void copy_environment ( toenv)
int toenv;
{
   int i, j;
 
   i=change_index;
  
   doubletime(i,toenv) = doubletime(i,0);
   cycletime(i,toenv) = cycletime(i,0);
   cycleswitch(i,toenv) = cycleswitch(i,0);
   birthrate(i,toenv) = birthrate(i,0);
   deathtime(i,toenv) = deathtime(i,0);
   deathswitch(i,toenv) = deathswitch(i,0);
   growrate(i,toenv) = growrate(i,0);
   xi(i,toenv) = xi(i,0);
   gama(i,toenv) = gama(i,0);
   mu(i,toenv) = mu(i,0);
   vnodeath(i,toenv) = vnodeath(i,0);

   for ( j = 0; j<= Ndrug;j++)
		drugkill(j,i,toenv) = drugkill(j,i,0);

   for ( j = 0; j<= Ntimenumber;j++) {
	   timekill(j,i,toenv) = timekill(j,i,0);
	   timesurv(j,i,toenv) = timesurv(j,i,0);
   }

   for(j = 0; j < LookUp[i].ParaPr->NumOfMu; j++)
   {
	   mutrate(i,j,toenv)= mutrate(i,j,0);
	   MuLookUp(i,j,toenv)=MuLookUp(i,j,0);
   }
	for(j = 0; j < LookUp[i].ParaPr->NumOfBl; j++)
   {
	   blebtime(i,j,toenv) = blebtime(i,j,0);
	   blebrate(i,j,toenv) = blebrate(i,j,0);
	   BlLookUp(i,j,toenv) = BlLookUp(i,j,0);
	}
	 for(j = 0; j < LookUp[i].ParaPr->NumOfCo; j++)
   {
	   convtime(i,j,toenv) = convtime(i,j,0);
	   convrate(i,j,toenv) = convrate(i,j,0);
	   CoLookUp(i,j,toenv)= CoLookUp(i,j,0);
   }
	 for(j = 0; j < LookUp[i].ParaPr->NumOfMig; j++)
   {
	   MigLookUp(i,j,toenv)= MigLookUp(i,j,0);
   }
   migtime(i,toenv) = migtime(i,0);
   migrate(i,toenv) = migrate(i,0);

   if ( i == 1 ) {
	   /* copy GompRule */
		for ( j=0; j <= NumGompRules; j++) {
			GompRule[j][toenv].multiplier = GompRule[j][0].multiplier;
			GompRule[j][toenv].gpmin = GompRule[j][0].gpmin;
			GompRule[j][toenv].gpmax = GompRule[j][0].gpmax;
			GompRule[j][toenv].GS = GompRule[j][0].GS;
			GompRule[j][toenv].gp_or_inv.GP= GompRule[j][0].gp_or_inv.GP;
		}
   }
 }

#ifndef TESTMPI
extern int EXPORT PASCAL ResetEnvir()
{
	nenvlist = 1;
    envlist[1].t = 0.0;
    envlist[1].e = 0;
	return 1;

}
#endif
void sort_envlist()
{
	int sorted, ienvlist, etemp;
	double ttemp;

	sorted= FALSE;

	while (sorted == FALSE) {
		sorted = TRUE;
		for (ienvlist = 1; ienvlist < nenvlist; ienvlist++){
			if ( envlist[ienvlist].t > envlist[ienvlist+1].t) {
				sorted = FALSE;
				etemp = envlist[ienvlist].e;
				ttemp = envlist[ienvlist].t;
				envlist[ienvlist].e = envlist[ienvlist+1].e;
				envlist[ienvlist].t = envlist[ienvlist+1].t;
				envlist[ienvlist+1].e = etemp;
				envlist[ienvlist+1].t = ttemp;
			}
		}
	}
}

#ifdef TESTMPI
int SetEnvir( name, begin, ender)
 char *name;
 double begin,ender;
#else
extern int EXPORT PASCAL  SetEnvir( char *name, double begin, double ender)
#endif
{
  int i, rets, found, fidx;
  nenvlist = nenvlist + 1;
  envlist[nenvlist].t = begin;
  found = 0;
  for ( i = TotalSingleRules - Kinetic_Changes; i < TotalSingleRules; i++ )
         if (strcmp(Rules[i].action.dest_name,name) == 0)
		 {
            found = 1;
			fidx = i;
         }
  
  if (found == 1)
  {
	  envlist[nenvlist].e = Rules[fidx].EnvirCond[0].ienv;
	  nenvlist = nenvlist + 1;
	  envlist[nenvlist].t = ender;
	  envlist[nenvlist].e = 0;
	  rets = 1;
  }
  else
  {
	  eprint("Name not found, environment list not updated [SetEnvir]");
	  nenvlist = nenvlist - 1;
      rets = 0;
  }
  sort_envlist();
  return (rets);
}


int is_operator(c)
	char c;
{
	switch (c)
	{
	case '=':
	case '*':
	case'/':
	    return(TRUE);
		break;
	default:
		return(FALSE);
		break;
	}
}

unsigned int get_ntypes()
{
 unsigned int P = 1;
 int C;

   for ( C=0; C< Number_of_Classes;C++ )
   {
     P*=Class[C].no_levels;
	} 
   return( P);
}

/* added by QS, 12/19/97 
Syntax for PG ( proportinal gompertz rule ):
ggp = gpmin + ( [classname] = levelname and [classname]=levelname ) * prop
e.g.,  gp = 1e6 + ( [huang2]=CA  and [VEGFprod]=On ) * 10 
*/

void parse_class_level_other(char rulebuf[])
{
	int nlevel=0,iterm,ienv=0;
	char *classname,*p,*brace,temp[20];

	p=rulebuf;

	iterm=0;
	strcpy(temp,read_word(p,&iterm));
	GompRule[NumGompRules][ienv].gpmax = atof(temp);
	do {
		iterm=0;
		classname=(char*)strchr(p,'[')+1;
		p =(char*)strstr(p,"=")+1;
		strcpy(GompRule[NumGompRules][ienv].cl[nlevel].cname,read_word(p,&iterm));
		brace=(char*)strchr(classname,']');
		*brace=0;
		strcpy(GompRule[NumGompRules][ienv].cc[nlevel].cname,classname);
		nlevel++;
	} while (p && (char*)strstr(p,"and"));

	p =(char*)strstr(p,"*")+1;

	iterm=0;
	strcpy(temp,read_word(p,&iterm));
	GompRule[NumGompRules][ienv].multiplier = atof(temp);
	GompRule[NumGompRules][ienv].nclevel=nlevel;
}

/* added by QS, 6/10/97 
Syntax for GR:
GR CLASS[classname]LEVEL opr levelname AND ENV num THEN rulename
*/

int parse_general_rule(char rulebuf[], int TotalRules )
{
	int nlevel=0,nenvi=0,irule=0;
	char *classname,tempbuf[1024],*brace,*p;
	struct action  *storeRule;
	struct LevelCond *storeLC;
	void extend_rule();
	

	storeRule = &(Rules[TotalRules].action);
	storeLC = &(Rules[TotalRules].LevelCond[nlevel]);

	storeLC->iclass = -1;
	storeLC->ilevel = -1;

	strcpy(tempbuf,rulebuf);

	p=tempbuf;

	do {
		Rules[TotalRules].EnvirCond[nenvi].ienv=0;
		/* get envir_name for this gr */
		if ( (p=(char*)strstr(p,"Env"))) {
			p =(char*)strstr(p,"=")+1;
			irule=0;
			strcpy(Rules[TotalRules].EnvirCond[nenvi].envir_name,read_word(p,&irule));
			nenvi++;
		}
		else strcpy(Rules[TotalRules].EnvirCond[nenvi].envir_name,"");
	} while (p && (char*)strstr(p,"And"));

	Rules[TotalRules].nEnvirConds=nenvi;/* nEnvirConds = 0 ==> all envi */

	p=tempbuf;

	do {
		/* get classname and levelname for this gr */
		if ( (p=(char*)strstr(p,"Class"))) {
			storeLC = &(Rules[TotalRules].LevelCond[nlevel]);
			if (!(classname=(char*)strchr(p,'[')+1))
				return 0;
			p = (char*)strstr(classname,"Level")+5;
			irule=0;
			strcpy(storeLC->clopr,read_word(p,&irule));
			strcpy(storeLC->level_name,read_word(p,&irule));
			brace=(char*)strchr(classname,']');
			*brace=0;
			strcpy(storeLC->class_name,classname);
			p=brace+1;
			/* fill class and level by name */
			fillclasslevel(nlevel,TotalRules);
			nlevel++;
		}
	} while (p && (char*)strstr(p,"And"));

	Rules[TotalRules].nLevelConds=nlevel;

	irule=0;
	
	if ((brace=(char*)strstr(rulebuf,"then"))||
		(brace=(char*)strstr(rulebuf,"THEN"))||
		(brace=(char*)strstr(rulebuf,"Then"))){
		/* extract rule name for all corresponding class, level, and
		   envir ( drug )   */
			read_to_eol(storeRule->rule_name,brace+4,&irule);
			return 1;
	}
	else {
		strcpy(storeRule->rule_name,"");
		return 0;
	}
}
/**********************************************************
*   fill class and level by classname and levelname of gr *
***********************************************************/
void fillclasslevel(int nlevel,int irule)
{
	  int C, L;
	  char classname[SMALLBUFFER], levelname[SMALLBUFFER];
	  struct LevelCond *storeLC;
	
	  storeLC = &(Rules[irule].LevelCond[nlevel]);

	  storeLC->iclass = -1;
	  storeLC->ilevel = -1;
	  for ( C = 0; C < Number_of_Classes; C++ ){
         for ( L = 0; L < Class[C].no_levels; L++ ) {
			 strcpy(classname,Class[C].class_name);
			 strcpy(levelname,Class[C].Level[L].level_name);

			 if ((!(strcmp(classname,storeLC->class_name))) && 
				 (!(strcmp(levelname,storeLC->level_name))))
			 {
				 	 storeLC->iclass=C;
					 storeLC->ilevel=L;
					 return ;
			 }
		 }
	   }
}

char *unix_strlwr(char *instr)
{
	char *p;
	int i;

	p=instr;

	for ( i=0; i<(int)strlen(instr);i++) {
		p[i]=tolower(instr[i]);
	}

	return p;

}

char *unix_strupr(char *instr)
{
	char *p;
	int i;

	p=instr;

	for ( i=0; i<(int)strlen(instr);i++) {
		p[i]=toupper(instr[i]);
	}

	return p;

}
void CheckIfThereIsClass(char *rulebuf,int rule_index)
{
	char *classname,tempbuf[SMALLBUFFER],*brace,*p;
	int id;
	void fillclassID();

	strcpy(tempbuf,rulebuf);

	p=tempbuf;

	Rules[rule_index].action.DestClassID=-1;

    if ( (p=(char*)strstr(p,"["))) {
		if ((classname=(char*)strchr(p,'[')+1)){
			id=0;
			p = (char*)strstr(classname,"]")+1;
			strcpy(Rules[rule_index].action.dest_name,read_word(p,&id));
			brace=(char*)strchr(classname,']');
			*brace=0;
			fillclassID(classname,rule_index);
		}
	
	}
}
	
void fillclassID(char *classnameIN,int rule_index)
{
	  int C;
	  char classname[SMALLBUFFER];
	 
	  Rules[rule_index].action.DestClassID = NODESTCLASS;

	  for ( C = 0; C < Number_of_Classes; C++ ){
        	 strcpy(classname,Class[C].class_name);
			 if (!(strcmp(classname,classnameIN)))
			 {
				 	 Rules[rule_index].action.DestClassID=C;
					 return ;
			 }
	  }
}
