#include "build.h"

#ifdef DLL
#include <windows.h>
#include <crtdbg.h>
#endif

#include "defines.h"

#define Zerod0 0.0   /*COMPILED*/

void setrates (iindex,nowenvl) 
int iindex,nowenvl;
 /*  integer nowenvl; */
{
   integer jindex;
   real hazConv, hazMig, hazDeath, hazMitosis, blebsum/*, br, dr, gr*/;

   if ( iindex > 0 )
   {
	   /*doubletime(iindex,nowenvl) = 
		   cycletime(iindex,nowenvl)
		   + doubleMinusCycletime(iindex,nowenvl);*/
	   /* New spec!!  changes are applied to DT-CT.*/
/*	   if (cycletime(iindex,nowenvl) < Zerod0){
// ERROR!!!!! 
		   doubletime(iindex,nowenvl) = cycletime(iindex,nowenvl);
		   cycletime(iindex,nowenvl) = Zerod0;
	   }
*/
	   mutsum(iindex,nowenvl) = Zerod0;
       blebsum = Zerod0;
       hazConv = Zerod0;
	   hazMig = Zerod0;
     /* convtime [iindex][iindex][nowenvl] = Zerod0;*/
	   for (jindex = 0; jindex  < LookUp[iindex].ParaPr->NumOfCo; jindex ++)
       {
         if (convtime(iindex,jindex,nowenvl)> Zerod0 )
         {
           /* convrate[iindex][jindex][nowenvl] = LN2 / convtime[iindex][jindex][nowenvl];*/
			convrate(iindex,jindex,nowenvl)=LN2 /convtime(iindex,jindex,nowenvl);
            hazConv = hazConv + convrate(iindex,jindex,nowenvl);
         }
		 else convrate(iindex,jindex,nowenvl)=0.0;
	   }

	   if (migtime(iindex,nowenvl)> Zerod0 )
       {
        	migrate(iindex,nowenvl)=LN2 /migtime(iindex,nowenvl);
            hazMig = hazMig + migrate(iindex,nowenvl);
       }
	   else
	   {
		   migrate(iindex,nowenvl)=0.0;
	   }

	  for (jindex = 0; jindex  < LookUp[iindex].ParaPr->NumOfMu; jindex ++)
      {
         mutsum(iindex,nowenvl)= mutsum(iindex,nowenvl) + mutrate(iindex,jindex,nowenvl);
      }
	/* added by QS 5/25/99 */
	  if ( cycleswitch (iindex,nowenvl) == true )
         hazMitosis = LN2 / cycletime (iindex,nowenvl);
      else  hazMitosis = Zerod0; 
/*
      if ( cycletime (iindex,nowenvl) > Zerod0 )
         hazMitosis = LN2 / cycletime (iindex,nowenvl);
      else if (cycletime (iindex,nowenvl)== Zerod0) // represents infinity 
         hazMitosis = Zerod0; 
      else fprintf (eout,"CYCLETIME is negative?? %3d %f %3d\n",
                   iindex, cycletime (iindex,nowenvl),nowenvl);*/
	
/*      if ( (vnodeath (iindex,nowenvl) == true))
      {
         hazDeath = Zerod0;
		 gr = br - g0sum;
         if ( (gr == Zerod0)) 
            doubletime (iindex,nowenvl) = Zerod0;
         else
            doubletime (iindex,nowenvl) = LN2/ gr;
      }
      else
      {
         if ( doubletime (iindex,nowenvl) > Zerod0) 
            gr = LN2 / doubletime (iindex,nowenvl);
		 else if ( doubletime (iindex,nowenvl) < Zerod0) 
            gr = LN2 / doubletime (iindex,nowenvl); 
         else
            gr = Zerod0;
		 hazdeath = br - gr; /* = LN2(1/CT - 1/DT) = LN2*Turnover/DT  

	}
*/
	  /* added by QS 5/25/99 */
	  if (deathswitch(iindex,nowenvl) == true)
		 hazDeath = LN2 / deathtime(iindex,nowenvl);
	  else 
		 hazDeath = Zerod0; 
	  
	 /* if (doubletime(iindex,nowenvl) != 0.0)
		 hazDeath = LN2 * turnover(iindex,nowenvl) / doubletime(iindex,nowenvl);*/

      birthrate (iindex,nowenvl) = hazMitosis;
      deathrate (iindex,nowenvl) = hazDeath;

      xi (iindex,nowenvl) = hazMitosis*(1-mutsum(iindex,nowenvl));
      gama (iindex,nowenvl) = hazDeath + hazConv + hazMig;
      mu (iindex,nowenvl)= blebsum + mutsum(iindex,nowenvl) * hazMitosis;
      growrate (iindex,nowenvl) = xi (iindex,nowenvl) - gama (iindex,nowenvl);

     if ( (hazDeath < Zerod0) or (verbos > 4)) 
      {
         if ( hazDeath < Zerod0) 
            fprintf (eout,"      WARNING: negative death rate\n");
         fprintf (eout," setrates: hazMitosis [%3d]== %f  (%3d)\n", iindex, hazMitosis, nowenvl);
         fprintf (eout," setrates: growrate [%3d]== %f  (%3d)\n", 
				iindex, growrate(iindex, nowenvl), nowenvl);
         fprintf (eout," setrates: deathrate [%3d]== %f  (%3d)\n", iindex, hazDeath, nowenvl);
         fprintf (eout,"      hazConv   %f",  hazConv);
		 fprintf (eout,"      hazMig   %f",  hazMig);
         fprintf (eout,"      mutsum(iindex,nowenvl)  %f",  mutsum(iindex,nowenvl));
      /*   fprintf (eout,"      blebsum %f",  blebsum);*/
     }

   }
   	 fflush(eout);
}
