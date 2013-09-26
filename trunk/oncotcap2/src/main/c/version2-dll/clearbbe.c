/* ClearBBE - Clear one entry from the BlackBoard
 *     Input: Key, BlackBoard key to clear
 */

#include <stdlib.h>
#include <malloc.h>
#include "bboard.h"

void ClearBBE(char *key)
{
	BBEType *bbe, *bbeprev, *bbenext;
	bbe = Lookup(key);
	if (bbe != NULL)
	{

		if (bbe->prev != NULL) //there are more entries before this one
		{
			bbeprev = (BBEType *) bbe->prev;
			bbeprev->next = bbe->next;
		}
		else if (bbe->next == NULL && bbe->prev == NULL) //this is the only entry in the list
		{
			gblBB.head = NULL;
			gblBB.tail = NULL;
		}
		else  //this is the first entry in the list
		{
			bbenext = (BBEType *) bbe->next;
			bbenext->prev = NULL;
			gblBB.head = bbenext;
		}
		free(bbe->key);
		free(bbe->value);
		free(bbe);
	}
}
