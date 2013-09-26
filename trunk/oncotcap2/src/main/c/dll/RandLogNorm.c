/* RandLogNorm -Performs a random log normal calculation on two BlackBoard variables, result is stored in a third 
 *              BlackBoard Variable.  The result is stored as a Double.
 *
 *  Input: args, pointer to an argument list that contains
 *         args[0]: a pointer to a BlackBoard key that will receive the result
 *         args[1]: a pointer to a BlackBoard key that contains the first argument
 *         args[2]: a pointer to a BlackBoard key that contains the second argument
 */ 

#include <stdlib.h>
#include <memory.h>
#include <stdio.h>
#include <string.h>
#include "build.h"
#include "bboard.h"

void RandLogNorm(void *args[3])
{
	double rval;
	rval = RandLogNormA((char *) args[0], (char *) args[1], (char*) args[2]);
}

/* RandLogNorm -Performs a random log normal calculation on two BlackBoard variables, result is stored in a third 
 *              BlackBoard Variable.  The result is stored as a Double.
 *
 * Inputs: Key: a pointer to a BlackBoard key that will receive the result
 *         Val1: first argument
 *         Val2: second argument
 */
double RandLogNormA(char *key, char *val1, char *val2)
{
	double lognorm();
	BBEType *bbe, *bbe1, *bbe2;
	double *dblVal1, *dblVal2, dblret;
	char strWarn[255];

	bbe = Lookup(key); //get a pointer to the BB entry for the result
	dblret = 0.0;
	if (bbe == NULL)   //if this entry doesn't already exist, create it
	{
		bbe = AddNewBBe(key);
		bbe->type = BBDOUBLE;               
		bbe->value = malloc(sizeof(double));
	}                                       
	else if(bbe->type != BBDOUBLE)  //if it's not a double, convert it to one
	{
		free(bbe->value);
		bbe->type = BBDOUBLE;
		bbe->value = malloc(sizeof(double));
	}
	bbe1 = Lookup(val1); //get a pointer to the first argument
	if (bbe1 == NULL)    //if it doesn't exist leave with a warning
	{
		strcpy(strWarn, "BlackBoard variable ");
		strcat(strWarn, val1);
		strcat(strWarn, " not found during RandLogNorm function. [RandLogNormA]");
#ifdef DLL
		MsgBoxWarning(strWarn);
#endif
		return(dblret);
	}


	bbe2 = Lookup(val2); //get a pointer to the second argument
	if (bbe2 == NULL)    //if it doesn't exist leave with a warning
	{
		strcpy(strWarn, "BlackBoard variable ");
		strcat(strWarn, val2);
		strcat(strWarn, " not found during RandLogNorm function. [RandLogNormA]");
#ifdef DLL
		MsgBoxWarning(strWarn);
#endif
		return(dblret);
	}

	if ( ( bbe1->type == BBDOUBLE ) &&      /*if they're both Doubles*/
		 ( bbe2->type == BBDOUBLE ) )
	{
		dblVal1 = bbe1->value;
		dblVal2 = bbe2->value;
		dblret = lognorm(*dblVal1, *dblVal2); //(mean, cv)
		memcpy(bbe->value, &dblret, sizeof(double));
	}
	else
	{
		strcpy(strWarn, "Argument ");
		strcat(strWarn, val1);
		strcat(strWarn, " or argument ");
		strcat(strWarn, val2);
		strcat(strWarn, " is not of type Double. [RandLogNormA]");
#ifdef DLL
		MsgBoxWarning(strWarn);
#endif
	}
	return(dblret);
}