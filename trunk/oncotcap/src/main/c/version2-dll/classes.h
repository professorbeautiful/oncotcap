#ifndef CONSTANTSDEFINED
#include "Const.h"
#endif

/*#define BASEMODEL  */

#define MAXCLASSES   20
#define MAXLEVELS    20
#define MAXRULES     30
#define ASSIGNRULE		1
#define NOTASSIGNRULE	2
#define CYTOSTATICRULE  3

#ifdef BASEMODEL


struct Rule
{
   int nLevelConds,nEnvirConds;

   struct LevelCond {
	 char   class_name[SMALLBUFFER];
	 char   level_name[SMALLBUFFER];
	 char   clopr[3];
	 int    iclass,ilevel;
   } LevelCond[MAXCLASSES];

   struct EnvirCond {
	 char envir_name[SMALLBUFFER];
	 int  ienv;
   } EnvirCond[NENVIRS];

   struct action {
	  char   rule_name[SMALLBUFFER];
	  int	 ruleKey;
	  char   opr;
	  int    DestClassID;
	  char   dest_name[SMALLBUFFER];
	  double value;
	  int    type;
   } action;
} Rules[MAXRULES];

struct Class_Type
{
   char   class_name[SMALLBUFFER];
   int    no_levels,pre_product,post_product;
   int    class_type;
   short int mx,my,mz;
   struct Level_Type
   {
      char   level_name[SMALLBUFFER];
      int    no_rules;
      struct Rule_Type
      {
         char   rule_name[SMALLBUFFER];
		 double value;
	} Rule[MAXRULES];
   } Level[MAXLEVELS];
} Class[MAXCLASSES];

int Number_of_Classes,Number_of_Gomp_Classes,num_macro_nodes,macro_block_length,TopDrug, BottomDrug,Kinetic_Changes,TotalRules, 
	TotalSingleRules,Base_Death_Switch,Base_Cycling_Switch;

double  Base_Cycling_Time, Base_Doubling_Time,
        Base_G0_Time, Base_Death_Time,Base_GompPlateau, Base_GompSplit;
#else

extern struct Class_Type
{
   char   class_name[SMALLBUFFER];
   int    no_levels,pre_product,post_product;
   int    class_type;
   short int mx,my,mz;
   struct Level_Type
   {
      char   level_name[SMALLBUFFER];
	  int    no_rules;
      struct Rule_Type
      {
          char   rule_name[SMALLBUFFER];
		  double value;
	 } Rule[MAXRULES];
   } Level[MAXLEVELS];
} Class[MAXCLASSES];

extern int Number_of_Classes,Number_of_Gomp_Classes,num_macro_nodes,macro_block_length,TopDrug, BottomDrug,Kinetic_Changes,TotalRules,
		   TotalSingleRules,Base_Death_Switch,Base_Cycling_Switch;

extern double Base_Cycling_Time,Base_Doubling_Time,
              Base_G0_Time, Base_Death_Time,Base_GompPlateau, Base_GompSplit;

#endif
