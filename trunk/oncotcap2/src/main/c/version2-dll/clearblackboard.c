/* ClearBlackBoard - Clears all entries from the BlackBoard
 */

#include <stdlib.h>
#include <malloc.h>
#include "bboard.h"
#include <string.h>

void ClearBlackBoard()
{
	BBEType * bbe;
    BBEType * tbbe;

	bbe = gblBB.head;
	while (bbe != NULL)
	{
		tbbe = bbe->next;
		free(bbe->key);
		free(bbe->value);
		free(bbe);
		bbe = tbbe;
	}
	gblBB.head = NULL;
	gblBB.tail = NULL;

}
