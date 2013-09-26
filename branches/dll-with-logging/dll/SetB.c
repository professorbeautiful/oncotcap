/* SetB - Sets a Boolean BlackBoard variable, given a pointer to an argument list
 *  Input: args, pointer to an argument list that contains
 *         args[0]: a pointer to a BlackBoard key
 *		   args[1]: a pointer to a value to set
 */

#include <stdlib.h>
#include <memory.h>
#include "bboard.h"
#include <string.h>

void SetB(void *args[2])
{
	int *tint;
	tint = args[1];
	SetBA((char *) args[0], *tint);
}

/* SetBA - Sets a Boolean BlackBoard variable
 * Inputs: Key: a pointer to a BlackBoard key
 *         Val: Value to set
 *
 */

void SetBA(char *key, int val)
{
	BBEType *bbe;
	bbe = Lookup(key);
	if (bbe == NULL)
	{
		bbe = AddNewBBe(key);
		bbe->type = BBBOOL;
	    bbe->value = malloc(sizeof(int));
	}
	memcpy(bbe->value, &val, sizeof(int));
}