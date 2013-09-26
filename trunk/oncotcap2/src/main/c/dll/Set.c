/* SetI - Sets an Integer BlackBoard variable, given a pointer to an argument list
 *  Input: args, pointer to an argument list that contains
 *         args[0]: a pointer to a BlackBoard key
 *		   args[1]: a pointer to a value to set
 */

#include <stdlib.h>
#include <memory.h>
#include "bboard.h"
#include <string.h>

void Set(void *args[2])
{
	SetA((char *) args[0], (char *) args[1]);
}

/* SetA - Sets a BlackBoard variable to the value of another BlackBoard variable
 * Inputs: Key: a pointer to a BlackBoard key
 *         ValKey: Value to set
 *
 */

void SetA(char *key, char *valkey)
{
	BBEType *bbe, *bbesource;
	size_t sourcesize;
	char strWarn[255];

	bbesource = Lookup(valkey);
	if (bbesource == NULL)
	{
		strcpy(strWarn, "BlackBoard variable ");
		strcat(strWarn, valkey);
		strcat(strWarn, " not found during SET function. [SetA]");
		MsgBoxWarning(strWarn);
		return;
	}
	
	if (bbesource->type == BBDOUBLE)
		sourcesize = sizeof(double);
	else
		sourcesize = sizeof(int);

	bbe = Lookup(key);
	if (bbe == NULL)
	{
		bbe = AddNewBBe(key);
		bbe->type = bbesource->type;
	    bbe->value = malloc(sourcesize);
	}
	memcpy(bbe->value, bbesource->value, sourcesize);
}