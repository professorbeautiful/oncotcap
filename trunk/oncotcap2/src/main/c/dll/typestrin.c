#include "build.h"

#include "defines.h"
		/**@ typestring.p **/
void /*PROCEDURE*/ typestring(line)   /*COMPILED*/
char *line;
/*	char *line;   */
/*VAR*/{
 /* int i; */

	fprintf (eout,"%s", line);


  /*******************
#define PADLEN 20
   char pad[PADLEN+2];

	pad[0]=(char)NULL;
	for (i=0; i <= (min(minlength-strlen(line),PADLEN) ) ; i++) 
		pad[i]=' ';
	pad[i]=(char)NULL;
	fprintf (eout,"%s%s", line, pad);

   integer i;

	i = 1;
	while ((i <= MAXCHAR) and not (line [i] == chr(ENDOFSTRING) ))  {
		fprintf(eout,"%c",line[i]);
		i = i + 1;
	};
	while (i++<minlength) fprintf(eout," ");
  *******************/
}
