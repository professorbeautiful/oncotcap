/* AddNewBBe - Add a new Black Board entry to the end of the list
 *     Input: key, BlackBoard Key
 */

#include <stdlib.h>
#include <malloc.h>
#include <string.h>
#include "bboard.h"

BBEType *AddNewBBe( char * key)
{
	BBEType *bbe;

	bbe = malloc(sizeof(BBEType));

	/* if this is not the first entry */
	if (gblBB.tail != NULL)
		gblBB.tail->next = bbe;
	else
	{
		gblBB.head = bbe;
	}

	bbe->key = malloc((strlen(key)+1) * sizeof(char));
	strcpy(bbe->key, key);
	bbe->next = NULL;
    bbe->prev = gblBB.tail;
	gblBB.tail = bbe;
	return(bbe);
}
