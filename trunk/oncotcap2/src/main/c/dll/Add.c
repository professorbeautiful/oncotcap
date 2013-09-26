/* Add -Performs Addition on two BlackBoard variables, result is stored in a third 
 *      BlackBoard Variable.  Either argument can be an Int or a Double.  The result
 *      will be stored as Integer if both arguments are Int otherwise it will be
 *      stored as a Double.
 *
 *  Input: args, pointer to an argument list that contains
 *         args[0]: a pointer to a BlackBoard key that will receive the result
 *         args[1]: a pointer to a BlackBoard key that contains the first argument
 *         args[2]: a pointer to a BlackBoard key that contains the second argument
 */ 

#include <stdlib.h>
#include <memory.h>
#include <string.h>
#include "bboard.h"

void Add(void *args[3])
{
	AddA((char *) args[0], (char *) args[1], (char*) args[2]);
}

/* AddA -Performs Addition on two BlackBoard variables, result is stored in a third 
 *      BlackBoard Variable.  Either argument can be an Int or a Double.  The result
 *      will be stored as Integer if both arguments are Int otherwise it will be
 *      stored as a Double.
 *
 * Inputs: Key: a pointer to a BlackBoard key that will receive the result
 *         Val1: first argument
 *         Val2: second argument
 * 
 */
void AddA(char *key, char *val1, char *val2)
{
	BBEType *bbe, *bbe1, *bbe2;
	int *intVal1, *intVal2, intret;
	double *dblVal1, *dblVal2, dblret;
	char strWarn[255];

	bbe = Lookup(key); //get a pointer to the BB entry for the result
	if (bbe == NULL)   //if this entry doesn't already exist, create it
	{
		bbe = AddNewBBe(key);
		bbe->type = BBDOUBLE;                //assume that the result
		bbe->value = malloc(sizeof(double)); //is a double
	}                                        //we'll switch it to int later if needed . . .
	else
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
		strcat(strWarn, " not found during ADD function. [AddA]");
		MsgBoxWarning(strWarn);
		return;
	}


	bbe2 = Lookup(val2); //get a pointer to the second argument
	if (bbe2 == NULL)    //if it doesn't exist leave with a warning
	{
		strcpy(strWarn, "BlackBoard variable ");
		strcat(strWarn, val2);
		strcat(strWarn, " not found during ADD function. [AddA]");
		MsgBoxWarning(strWarn);
		return;
	}

	if ( ( bbe1->type == BBINT ) &&      /*if they're both int*/
		 ( bbe2->type == BBINT ) )
	{
		intVal1 = bbe1->value;
		intVal2 = bbe2->value;
		intret = *intVal1 + *intVal2;
		free(bbe->value);
		bbe->type = BBINT;
		bbe->value = malloc(sizeof(int));
		memcpy(bbe->value, &intret, sizeof(int));
	}
	else if( bbe1->type == BBDOUBLE &&        /* if the first arg is a double and the second is an int*/
		    (bbe2->type == BBINT) )
	{
		dblVal1 = bbe1->value;
		intVal2 = bbe2->value;
		dblret = *dblVal1 + ((double) *intVal2);
		memcpy(bbe->value, &dblret, sizeof(double));
	}
	else if( bbe2->type == BBDOUBLE &&        /* if the second arg is a double and the first is an int*/
		    (bbe1->type == BBINT) )
	{
		dblVal2 = bbe2->value;
		intVal1 = bbe1->value;
		dblret = *dblVal2 + ((double) *intVal1);
		memcpy(bbe->value, &dblret, sizeof(double));
	}
	else if ( bbe2->type == BBDOUBLE && bbe1->type == BBDOUBLE )  /* if both args are doubles */
	{
		dblVal1 = bbe1->value;
		dblVal2 = bbe2->value;
		dblret = *dblVal1 + *dblVal2;
		memcpy(bbe->value, &dblret, sizeof(double));
	}
	else
	{
		strcpy(strWarn, "Argument ");
		strcat(strWarn, val1);
		strcat(strWarn, " or argument ");
		strcat(strWarn, val2);
		strcat(strWarn, " is not of type Integer, or Double. [AddA]");
		MsgBoxWarning(strWarn);
	}
	return;
}