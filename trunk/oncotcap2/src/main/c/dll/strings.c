#include "build.h"

#include <stdio.h>
#include <string.h>
#include "defines.h"
void skip_blanks();
void skip_white();
char *read_line();

int is_a_substring ( substring, string )
   char substring[];
   char string[];
{
   int
      position, p1, p2;


   if ( strlen ( substring ) > 0 )
   {
      p1 = 0;
      p2 = 0;
      while ( p1 < (int) strlen ( string ) && p2 < (int) strlen ( substring ) )
      {
         while ( p1 < (int) strlen ( string) && substring[0] != string[p1] )
            p1++;
         if ( p1 < (int) strlen ( string ) )
         {
            position = p1;
            for ( p2 = 0; p1 < (int) strlen ( string ) && p2 < (int) strlen ( substring ) &&
                          substring[p2] == string[p1]; p2++ )
               p1++;
         }
      }
      if ( p2 < (int) strlen ( substring ) )
         return ( -1 );
      else
         return ( position );
   }
   else
      return ( 0 );
}



char *read_line( buffer, i )
   char buffer[];
   int *i;
{
   int j;
   static char temp[200];

   skip_white( buffer, i );
   j = 0;
   while ( (*i < (int) strlen(buffer)) && (buffer[*i] != '\n') )
   {
      temp[j] = buffer[*i];
      j = j + 1;
      *i = *i + 1;
   }
   temp[j] = '\0';
   return ( temp );
}
char *read_word( buffer, i )
   char buffer[];
   int *i;
{
   int j, is_white();
   static char temp[200];
   void skip_white();

   skip_white( buffer, i );
   j = 0;
   while ( (*i < (int) strlen(buffer)) && (!is_white(buffer[*i])) )
   {
      temp[j] = buffer[*i];
      j = j + 1;
      *i = *i + 1;
   }
   temp[j] = '\0';
   return ( temp );
}


 void eprint( command )
   char *command;
{
   fprintf(eout,"Error !!!!! %s \n",command);
}

void wprint( command )
   char *command;
{
   fprintf(eout,"Warning !!!!! %s \n",command);
}

void skip_blanks( buffer, i )
   char buffer[];
   int *i;
{
   while ( *i < (int) strlen( buffer ) && buffer[*i] == ' ' ) 
      *i = *i + 1;
}

int is_white( c )
   char c;
{
	return ( c == ' ' || c == '\n' || c == '\t');
}

void skip_white( buffer, i )
   char buffer[];
   int *i;
{
   while ( (*i < (int) strlen(buffer)) && is_white(buffer[*i])) 
      *i = *i + 1;
}


char *chopword(char line[])
{
int firstchar;
static char rets[512];

strcpy(rets,line);

if (rets[0] == '\0') return rets;
firstchar = (int) rets[0];


while (firstchar == 32 && firstchar != 0)
{
   strcpy(rets,&rets[1]);
   firstchar = (int) rets[0];
}  

while (firstchar != 32 && firstchar != 0)
{
   strcpy(rets,&rets[1]);
   firstchar = (int) rets[0];
}

while (firstchar == 32 && firstchar != 0)
{
   strcpy(rets,&rets[1]);
   firstchar = (int) rets[0];
}

return rets;
}
 

char *getword(char *line)
{
int firstchar,i;
static char rets[512];

strcpy(rets,line);

if (rets[0] == '\0') return rets;
firstchar = (int) rets[0];


while (firstchar == 32 && firstchar != 0)
{
   strcpy(rets,&rets[1]);
   firstchar = (int) rets[0];
}  

i = 0;
firstchar = rets[0];
while ((int) firstchar != 32 && (int) firstchar != 0)
{
   i++;
   firstchar = rets[i];
}
rets[i] = '\0';

return rets;
}

void  fSread(p,times)
FILE *p;
char *times;
 {
	char ch;
	int i;
	
		ch = fgetc( p );
		 for( i=0; ch!='\n'; i++ )
		{
		 times[i] = (char)ch;
		 ch = fgetc( p );
		}

		/* Add null to end string */
		times[i] = '\0';
}