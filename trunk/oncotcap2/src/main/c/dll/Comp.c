#include <stdlib.h>
#include <memory.h>
#include <string.h>
#include "bboard.h"

/* Comp - Sets a Boolean BlackBoard variable with the result of 
 *        the comparison test specified by CompType on val1 and val2
 *        val1 and val2 can any combination of Double or Integer
 *        
 * Inputs: Key: a pointer to a BlackBoard key that will receive the result
 *         Val1: first argument
 *         Val2: second argument
 *         CompType: the comparison type.  Valid values are ==, <, <=, >, >=, !=
 * 
 * Output: The boolean result of the operation
 */

int Comp(char *key, char *val1, char *val2, char *CompType)
{
	int CompI(int v1, int v2, char *typ);
	int CompD(double v1, double v2, char *typ);
	BBEType *bbe, *bbe1, *bbe2;
	int *intVal1, *intVal2;
	double *dblVal1, *dblVal2;
	char strWarn[255];

	if (strcmp(key,BBNULL) != 0)  /*only do this if not "saving" to NULL*/
	{
		bbe = Lookup(key); /*get a pointer to the BB entry for the result*/
		if (bbe == NULL)   /*if this entry doesn't already exist, create it*/
		{
			bbe = AddNewBBe(key);
			bbe->type = BBBOOL;
			bbe->value = malloc(sizeof(int));
		}
		else if (bbe->type != BBBOOL) /*if the pointer to the result does exist*/
		{                              /*but the variable isn't a boolean, leave with a warning*/
			strcpy(strWarn, "BlackBoard variable ");
			strcat(strWarn, key);
			strcat(strWarn, " Not of type Boolean. [Comp]");
#ifdef DLL
			MsgBoxWarning(strWarn);
#endif
			return(FALSEVAL);
		}
		memcpy(bbe->value, & (FALSEVAL), sizeof(int)); /*default result to false */
	}			                                           /*in case we don't find one*/
				                                       /*of the arguments on the blackboard*/

	bbe1 = Lookup(val1); /*get a pointer to the first argument*/
	if (bbe1 == NULL)    /*if it doesn't exist leave with a warning*/
	{
		strcpy(strWarn, "BlackBoard variable ");
		strcat(strWarn, val1);
		strcat(strWarn, " not found while trying to perform a comparison. [Comp]");
#ifdef DLL
		MsgBoxWarning(strWarn);
#endif
		return(FALSEVAL);
	}


	bbe2 = Lookup(val2); /*get a pointer to the second argument*/
	if (bbe2 == NULL)    /*if it doesn't exist leave with a warning*/
	{
		strcpy(strWarn, "BlackBoard variable ");
		strcat(strWarn, val2);
		strcat(strWarn, " not found while trying to perform a comparison. [Comp]");
#ifdef DLL
		MsgBoxWarning(strWarn);
#endif
		return(FALSEVAL);
	}

	if ( ( bbe1->type == BBINT || bbe1->type == BBBOOL) &&      /*if they're both int*/
		 ( bbe2->type == BBINT || bbe2->type == BBBOOL) )
	{
		intVal1 = bbe1->value;
		intVal2 = bbe2->value;
    	if (CompI(*intVal1,*intVal2,CompType))
		{
			if (strcmp(key,BBNULL) != 0)  /*only do this if not "saving" to NULL*/
				memcpy(bbe->value, & (TRUEVAL), sizeof(int));

			return(TRUEVAL);
		}
	}
	else if( bbe1->type == BBDOUBLE &&        /* if the first arg is a double and the second is an int*/
		    (bbe2->type == BBINT || bbe2->type == BBBOOL) )
	{
		dblVal1 = bbe1->value;
		intVal2 = bbe2->value;
		if (CompD(*dblVal1,((double) *intVal2),CompType))
		{
			if (strcmp(key,BBNULL) != 0)  /*only do this if not "saving" to NULL*/
				memcpy(bbe->value, & (TRUEVAL), sizeof(int));
			return(TRUEVAL);
		}
	}
	else if( bbe2->type == BBDOUBLE &&        /* if the second arg is a double and the first is an int*/
		    (bbe1->type == BBINT || bbe1->type == BBBOOL) )
	{
		dblVal2 = bbe2->value;
		intVal1 = bbe1->value;
		if (CompD(((double) *intVal1), *dblVal2, CompType ))
		{
			if (strcmp(key,BBNULL) != 0)  /*only do this if not "saving" to NULL*/
				memcpy(bbe->value, & (TRUEVAL), sizeof(int));
			return(TRUEVAL);
		}
	}
	else if ( bbe2->type == BBDOUBLE && bbe1->type == BBDOUBLE )  /* if both args are doubles */
	{
		dblVal1 = bbe1->value;
		dblVal2 = bbe2->value;
		if (CompD(*dblVal1, *dblVal2,CompType))
		{
			if (strcmp(key,BBNULL) != 0)  /*only do this if not "saving" to NULL*/
				memcpy(bbe->value, & (TRUEVAL), sizeof(int));
			return(TRUEVAL);
		}

	}
	else
	{
		strcpy(strWarn, "Argument ");
		strcat(strWarn, val1);
		strcat(strWarn, " or argument ");
		strcat(strWarn, val2);
		strcat(strWarn, " is not of type Integer, or Double. [EqA]");
#ifdef DLL
		MsgBoxWarning(strWarn);
#endif
	}
	return(FALSEVAL);
}

int CompI(int val1, int val2, char *CompType)
{

	int rv;
	char strWarn[255];

	rv = 0;
	if (strcmp(CompType,"==") == 0)
		rv = (val1 == val2);
	else if (strcmp(CompType,"<") == 0)
		rv = (val1 < val2);
	else if (strcmp(CompType,"<=") == 0)
		rv = (val1 <= val2);
	else if (strcmp(CompType,">") == 0)
		rv = (val1 > val2);
	else if (strcmp(CompType,">=") == 0)
		rv = (val1 >= val2);
	else if (strcmp(CompType,"!=") == 0)
		rv = (val1 != val2);
	else
	{
		strcpy(strWarn,"Undefined comparison type ");
		strcat(strWarn, CompType);
		strcat(strWarn, " [CompI]");
#ifdef DLL
		MsgBoxWarning(strWarn);
#endif
	}
	return(rv);
}

int CompD(double val1, double val2, char *CompType)
{

	int rv;
	char strWarn[255];

	rv = 0;
	if (strcmp(CompType,"==") == 0)
		rv = (val1 == val2);
	else if (strcmp(CompType,"<") == 0)
		rv = (val1 < val2);
	else if (strcmp(CompType,"<=") == 0)
		rv = (val1 <= val2);
	else if (strcmp(CompType,">") == 0)
		rv = (val1 > val2);
	else if (strcmp(CompType,">=") == 0)
		rv = (val1 >= val2);
	else if (strcmp(CompType,"!=") == 0)
		rv = (val1 != val2);
	else
	{
		strcpy(strWarn,"Undefined comparison type ");
		strcat(strWarn, CompType);
		strcat(strWarn, " [CompI]");
#ifdef DLL
		MsgBoxWarning(strWarn);
#endif
	}
	return(rv);
}
