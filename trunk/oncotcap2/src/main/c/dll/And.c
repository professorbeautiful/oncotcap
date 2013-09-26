/* And - Performs AND on two BlackBoard variables, result is stored in a third 
 *       BlackBoard Variable.  The argumnents can be any combination of Integers
 *       Booleans, and Doubles.
 *
 *  Input: args, pointer to an argument list that contains
 *         args[0]: a pointer to a BlackBoard key that will receive the result
 *         args[1]: a pointer to a BlackBoard key that contains the first argument
 *         args[2]: a pointer to a BlackBoard key that contains the second argument
 * Output: Boolean, the result of the operation
 */ 

#include <stdlib.h>
#include <memory.h>
#include <string.h>
#include "bboard.h"

int And(void *args[3])
{
	return(AndA((char *) args[0], (char *) args[1], (char*) args[2]));
}

/* AndA - Sets a Boolean BlackBoard variable with the result of ANDing val1 and val2
 *        The argumnents can be any combination of Integers, Booleans, and Doubles.
 *
 * Inputs: Key: a pointer to a BlackBoard key that will receive the result
 *         Val1: first argument to AND
 *         Val2: second argument to AND
 * 
 * Output: The boolean result of the operation
 */

int AndA(char *key, char *val1, char *val2)
{
	BBEType *bbe, *bbe1, *bbe2;
	int *intVal1, *intVal2;
	double *dblVal1, *dblVal2;
	char strWarn[255];

	bbe = Lookup(key); //get a pointer to the BB entry for the result
	if (bbe == NULL)   //if this entry doesn't already exist, create it
	{
		bbe = AddNewBBe(key);
		bbe->type = BBBOOL;
	    bbe->value = malloc(sizeof(int));
	}
	else if (bbe->type != BBBOOL) //if the pointer to the result does exist
	{                              //but the variable isn't a boolean, leave with a warning
		strcpy(strWarn, "BlackBoard variable ");
		strcat(strWarn, key);
		strcat(strWarn, " Not of type Boolean. [AndA]");
		MsgBoxWarning(strWarn);
		return(FALSEVAL);
	}

	memcpy(bbe->value, & (FALSEVAL), sizeof(int)); //default result to false 
	                                               //in case we don't find one
	                                               //of the arguments on the blackboard

	bbe1 = Lookup(val1); //get a pointer to the first argument
	if (bbe1 == NULL)    //if it doesn't exist leave with a warning
	{
		strcpy(strWarn, "BlackBoard variable ");
		strcat(strWarn, val1);
		strcat(strWarn, " not found during AND function. [AndA]");
		MsgBoxWarning(strWarn);
		return(FALSEVAL);
	}


	bbe2 = Lookup(val2); //get a pointer to the second argument
	if (bbe2 == NULL)    //if it doesn't exist leave with a warning
	{
		strcpy(strWarn, "BlackBoard variable ");
		strcat(strWarn, val2);
		strcat(strWarn, " not found during AND function. [AndA]");
		MsgBoxWarning(strWarn);
		return(FALSEVAL);
	}

	if ( ( bbe1->type == BBINT || bbe1->type == BBBOOL ) &&      /*if they're both int or bool*/
		 ( bbe2->type == BBINT || bbe2->type == BBBOOL ) )
	{
		intVal1 = bbe1->value;
		intVal2 = bbe2->value;
    	if (*intVal1 && *intVal2)
		{
			memcpy(bbe->value, & (TRUEVAL), sizeof(int));
			return(TRUEVAL);
		}
	}
	else if( bbe1->type == BBDOUBLE &&        /* if the first arg is a double and the second is an int or bool */
		    (bbe2->type == BBINT || bbe2->type == BBBOOL) )
	{
		dblVal1 = bbe1->value;
		intVal2 = bbe2->value;
		if (*dblVal1 && ((double) *intVal2))
		{
			memcpy(bbe->value, & (TRUEVAL), sizeof(int));
			return(TRUEVAL);
		}
	}
	else if( bbe2->type == BBDOUBLE &&        /* if the second arg is a double and the first is an int or bool */
		    (bbe1->type == BBINT || bbe1->type == BBBOOL) )
	{
		dblVal2 = bbe2->value;
		intVal1 = bbe1->value;
		if (*dblVal2 && ((double) *intVal1))
		{
			memcpy(bbe->value, & (TRUEVAL), sizeof(int));
			return(TRUEVAL);
		}
	}
	else if ( bbe2->type == BBDOUBLE && bbe1->type == BBDOUBLE )  /* if both args are doubles */
	{
		dblVal1 = bbe1->value;
		dblVal2 = bbe2->value;
		if (*dblVal1 && *dblVal2)
		{
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
		strcat(strWarn, " is not of type Boolean, Integer, or Double. [AndA]");
		MsgBoxWarning(strWarn);
	}
	return(FALSEVAL);
}