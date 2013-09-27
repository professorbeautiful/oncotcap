#include "build.h"

#define BASEMODEL

#include "defines.h"
#include <malloc.h>
#include <stdio.h>
#include "logger.h"

double mincycletime;

char *read_rule_file( );
int parse_base_model( );
void initdosefactor();
#ifdef TEST
extern int ReadModelFile();
#endif
#ifdef TESTMPI
double ApplyRules(phenofile)
char *phenofile;
#else 
extern  double EXPORT PASCAL ApplyRules(char *phenofile)
#endif
{
  char *t;
  int ret;
  int IsUnix = false;
  /* nenvirons = 1; */
  /* nenvlist = 1; */

  nowenv = 0;
  fprintf(logfile,"%s\tfunction:Applyrul.ApplyRules\tphenofile:%s\n", gettime(),phenofile);
  if ( IsUnix == false ) {
	  t = read_rule_file( phenofile );
 
	  ret=parse_base_model(t);
	  free(t);
  }
#ifdef TEST
  else {
	  ret = ReadModelFile ( phenofile );
  }
#endif
  if ( ret == 0 )
	  return ( -1.0);
 
/* s2wrapper("PokeDelT", 0.007*mincycletime); Cant see EXPORT PASCAL!*/
  return(mincycletime);
}


