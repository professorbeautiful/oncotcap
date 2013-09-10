/*  LookupBBE - Lookup (find) a blackboard enrty.
 *      Input: Key, BlackBoard key;
 *     Output: pointer to the matching blackboard entry.
 *             null if no match is found
 */
#include <stdlib.h>
#include <string.h>
#include "bboard.h"

BBEType *Lookup(char *key)
{
	BBEType * bbe;

	bbe = gblBB.head;

	while((bbe != NULL) && (strcmp(bbe->key,key) != 0))
		bbe = bbe->next;

	return(bbe);

}
