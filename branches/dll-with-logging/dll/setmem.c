#include "build.h"

#include "defines.h"
/***********************************************
#define MAIN
main() {
  char c;
	while ( (c=getc(stdin))!=EOF)
		fprintf (eout,"-%c- in abc? results %d\n", c,setmem("abc",c));
}
***********************************************/
#define MAIN


int setmem (s,c)
  char * s; char c;
{
	char ctemp[2];
	memcpy(ctemp,&c,1);
	ctemp[1] = '\0';
	if ((c != (char)0) && (int)strcspn(s,ctemp)) return(true);
	else return (false);
}
