/* tempname - Generates a temporary key name for use on the BlackBoard.
 *   input:  TempKeyID, used to serialize the name
 */

#include <stdlib.h>
#include <string.h>
#include "bboard.h"

char * tempname(int TempKeyID)
{
	char tkey[255];
	char tkey2[255];
	char *rkey;
	strcpy (tkey, TEMPNAMEPREFIX);
	strcat (tkey,_itoa(TempKeyID,tkey2,10));

	rkey = malloc((strlen(tkey) + 1) * sizeof(char));
	strcpy (rkey,tkey);

	return(rkey);
}