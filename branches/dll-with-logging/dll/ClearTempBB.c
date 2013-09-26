/* ClearTempBB - Clears all temp entries from the BlackBoard.
 *               Temp entries are defined as any entry starting with ~T
 */

#include <stdlib.h>
#include <malloc.h>
#include "bboard.h"

void ClearTempBB()
{
	BBEType * bbe;
    BBEType * bbenext, *bbeprev;

	bbe = gblBB.head;
	while (bbe != NULL)
	{
		bbenext = bbe->next;
		bbeprev = bbe->prev;
		if(bbe->key[0] == '~' && bbe->key[1] == 'T')
		{
			if (bbenext != NULL)
				bbenext->prev = bbe->prev;
			else
				gblBB.tail = bbeprev;

			if (bbeprev != NULL)
				bbeprev->next = bbe->next;
			else
				gblBB.head = bbe->next;

			free(bbe->key);
			free(bbe->value);
			free(bbe);
		}
		bbe = bbenext;
	}
}
