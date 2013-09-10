#include "build.h"
#include <stdlib.h>
#include "defines.h"

void ParseSchedule(buffer,i,id)
char buffer[];
int i,id;
{

   /*
    * A schedule can be in one of the following forms:
    *    q#x# #...   where # indicates an integer,
    *    # # #...    where # can be an integer or floating point,
    *    n           the letter "n".
    */
	
	int j;
	int interval, nts, start;
    char temp[100];

	  i++;
 
      while ( buffer[i] == ' ' )
        i++;
      j = 0;
      while ( ( buffer[i] >= '0' && buffer[i] <= '9' ) || buffer[i]=='+' || buffer[i]=='-' || toupper(buffer[i])=='E' || buffer[i] == '.' )
         temp[j++] = buffer[i++];
      temp[j] = '\0';
      interval = atoi( temp );

      while ( buffer[i] == ' ' || buffer[i] == 'x' || buffer[i] == 'X' )
         i++;
      j = 0;
      while ( buffer[i] >= '0' && buffer[i] <= '9' )
         temp[j++] = buffer[i++];
      temp[j] = '\0';
      nts = atoi( temp );

      while ( buffer[i] == ' ' || buffer[i] == ',' ||
              ( buffer[i] >= 'A' && buffer[i] <= 'z' ) )
         i++;
      j = 0;
      while ( ( buffer[i] >= '0' && buffer[i] <= '9' ) || buffer[i]=='+' || buffer[i]=='-' || toupper(buffer[i])=='E' || buffer[i] == '.' )
         temp[j++] = buffer[i++];
      temp[j] = '\0';
      start = atoi( temp );
      for ( i = 1; i <= nts; i++ )
         doselist[id][i] = start + ( i - 1 ) * interval;
      ndoses[id] = nts;
}