#include "build.h"
#ifndef UNIX
#include <windows.h>
#include <crtdbg.h> 
#endif

#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>


#ifndef TESTMPI
#include <io.h>
#endif
#include "defines.h"
#include "logger.h"

void evaluate_schedule();

#ifdef TESTMPI
int ApplySchedule( schedparam, id )
char *schedparam;
 int id ;
#else 
extern int EXPORT PASCAL  ApplySchedule( char *schedparam, int id )
#endif
{
   fprintf(logfile,"%s\tfunction:evalsched.ApplySchedule\tschedparam:%s\tid:%d\n", gettime(),schedparam,id);	
   evaluate_schedule(schedparam, id);
   buildsched();
   if ( nsched < MAXTIMES ){
		sortsched();
		return true;
   }
   else {
		MessageBox(NULL,"Sorry. The total # timepoints have exceeded the maximum, Please increase 'Delta T' multiplier for PGF engine. Try again.","treat.dll",MB_ICONEXCLAMATION | MB_OK);
	    return false;
   }
}

void evaluate_schedule( buffer, id)
char buffer[] ;
int id;
{
   int i,j;
   
   /* int interval, nts, start; */
   char temp[100];
      
   i = 0;

   /*
    * Skip past blank spaces.
    */
   while ( buffer[i] == ' ' )
      i++;

   if ( buffer[i] == 'q' || buffer[i] == 'Q' )
   {
      ParseSchedule(buffer,i,id);
   }
   else if ( buffer[i] == 'n' || buffer[i] == 'N' )
      ndoses[id] = 0;
   else
   {
      i = 0;
      ndoses[id] = 0;
      while ( i < (int) strlen( buffer ) )
      {
         while ( buffer[i] == ' ' || buffer[i] == '\t' )
            i++;
         j = 0;
         while ( ( buffer[i] >= '0' && buffer[i] <= '9' ) || buffer[i]=='+' || buffer[i]=='-' || toupper(buffer[i])=='E' || buffer[i] == '.' )
            temp[j++] = buffer[i++];
         temp[j] = '\0';
         ndoses[id] = ndoses[id] + 1;
         doselist[id][ndoses[id]] = (double) atof( temp );
         while ( i < (int) strlen( buffer ) && buffer[i] == ' ' )
            i++;
      }
   }
}
