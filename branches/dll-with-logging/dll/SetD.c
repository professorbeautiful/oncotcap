/* SetD - Sets a Double BlackBoard variable, given a pointer to an argument list
 *  Input: args, pointer to an argument list that contains
 *         args[0]: a pointer to a BlackBoard key
 *		   args[1]: a pointer to a value to set
 */

#include <stdlib.h>
#include <memory.h>
#include "bboard.h"
#include <string.h>

void SetD(void *args[2])
{
	double *tdbl;
	tdbl = args[1];
	SetDA((char *) args[0], *tdbl);
}

/* SetDA - Sets an Integer BlackBoard variable
 * Inputs: Key: a pointer to a BlackBoard key
 *         Val: Value to set
 *
 */

void SetDA(char *key, double val)
{
	BBEType *bbe;
	bbe = Lookup(key);
	if (bbe == NULL)
	{
		bbe = AddNewBBe(key);
		bbe->type = BBDOUBLE;
	    bbe->value = malloc(sizeof(double));
	}
	memcpy(bbe->value, &val, sizeof(double));
}