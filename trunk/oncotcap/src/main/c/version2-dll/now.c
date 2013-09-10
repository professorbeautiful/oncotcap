/* Now - Sets a  BlackBoard variable with the current simulation time
 *  Input: args, pointer to an argument list that contains
 *         args[0]: a pointer to a BlackBoard key that will recieve the time
 *                  as a Double
 */

#include <stdlib.h>
#include <windows.h>
#include <memory.h>
#include "build.h"
#include "defines.h"
#include "classes.h"
#include "cellp.h"
#include "bboard.h"

void Now(void *args[1])
{
	NowA((char *) args[0]);
}

/* NowA - Sets an BlackBoard variable with the current simulation time
 * Inputs: Key: a pointer to a BlackBoard key
 *
 */

void NowA(char *key)
{
	BBEType *bbe;
	bbe = Lookup(key);
	if (bbe == NULL)
	{
		bbe = AddNewBBe(key);
		bbe->type = BBDOUBLE;
	    bbe->value = malloc(sizeof(double));
	}
	memcpy(bbe->value, &t, sizeof(double));
}