#include "build.h"

#include "defines.h"
void fgetstring(p, line)
	FILE *p;
	char *line;
{

	integer i;

	i = 0;
	while ((i <= MAX_CHAR) and not fqendofline ( p ) )  {
		line[i] = getc( p );
		i = i + 1;
	};
	if ( not fqendofline ( p ) ) 
//		fprintf (stderr, "getstring WARNING: line not fully read\n");
		fprintf (eout, "getstring WARNING: line not fully read\n");		
	line [i] = chr (ENDOFSTRING);
	fdumpline ( p );
}



void getstring(line)
	char *line;
{

	integer i;

	i = 0;
	while ((i <= MAX_CHAR) and not qendofline)  {
		line[i] = getc( stdin );
		i = i + 1;
	};
	if ( not qendofline ) 
//		fprintf (stderr, "getstring WARNING: line not fully read\n");
		fprintf (eout, "getstring WARNING: line not fully read\n");		
	line [i] = chr (ENDOFSTRING);
	dumpline;
   /********************
	qendofline? 
		fprintf (eout,"getstring: peek=-EOLN-\n")
	:
		fprintf (eout,"getstring: peek=-%c-\n", peek)
	;
   ********************/
}
