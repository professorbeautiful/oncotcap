#include "build.h"
#include "defines.h"

#ifndef TESTMPI
#include <crtdbg.h>
#endif

extern  int  nloops;
extern double StartT;
char *getword();
char *chopword();
void infile( p )
FILE *p;
  /* FILE *p;  */
{
   integer itype, itime, jtype, is, ibin, ival, idrug, idose, ienv;
   integer iprob, i, nowenvl;
   char charac;
   char vbuff[256],tempname[SMALLBUFFER];
   double tempvalue;
   float fltVsn;

   /*  
    *   while (not qendofline )  {
    *      charac=getc( stdin);
    *      fprintf (eout,"%c",charac);
    *  };
    */ 
  
   i = 0;
   while ( (not fqendofline( p )) and (i < 256) )
	   vbuff[i++] = (char) getc(p);

   vbuff[i] = '\0';
   
   fltVsn = (float) atof(getword(chopword(chopword(vbuff))));

   while (fqendofline(p)) 
	   i = getc(p);

   /* vbuff[strlen(strTCAPVERSION)] = '\0';  */
/*   if (fltVsn > (float) atof(getword(chopword(chopword(strTCAPVERSION)))) || fltVsn < 1.2){
#ifndef TESTMPI
	   MsgBoxError("Infile failed, wrong version input file");
#else
	   printf("Infile failed, wrong version input file");
#endif
   }*/

   while ( not fqendofline ( p ) )
    {
      charac = (char) getc ( p );
      printf ("%c", charac );
    } 
     printf("\n");
    
   /* Changed to fIread from Iread...
    */
   fIread (p, verbos);    fdumpline ( p );
   fIread (p, ndrugs);    fdumpline ( p );
   fIread (p, ntypes);    fdumpline ( p );
   fIread (p, nallts);    fdumpline ( p );
   fIread (p, nt);        fdumpline ( p );
   fIread (p, nsched);    fdumpline ( p );
   fIread (p, nenvirons); fdumpline ( p );
   fIread (p, nenvlist);  fdumpline ( p );

   for (ienv = 1; ienv <= nenvlist; ienv++)
   {
      fIread (p, envlist[ienv].e); fdumpline ( p );
      fDread (p, envlist[ienv].t); fdumpline ( p );
	}

   for (itype = 1; itype <= (int)ntypes; itype++)
   {
      fDread (p, cellstart[itype]);  fdumpline ( p );
      i = 0;
      while ((i <= MAX_CHAR) and not fqendofline ( p ))
      {
         tempname[i] = (char) getc( p );
         i = i + 1;
      }
      tempname[i] = chr (ENDOFSTRING);
      fdumpline ( p );
      fDread (p, svec [itype]);  fdumpline ( p );
      fDread (p, smask [itype]); fdumpline ( p );
      for (ienv = 0; ienv < nenvirons; ienv++)
	  {
		 fDread (p,tempvalue); fdumpline ( p );
		 /* doubletime (itype,ienv)=tempvalue; */
         fDread (p, tempvalue); fdumpline ( p );
		 /* cycletime (itype,ienv)=tempvalue; */
         fDread (p,tempvalue); fdumpline ( p );
		 /* birthrate (itype,ienv)=tempvalue; */
         fDread (p, tempvalue); fdumpline ( p );
		 /* deathrate (itype,ienv)=tempvalue; */
		 /*if ( (deathrate (itype,ienv) == ZERO)) 
            vnodeath (itype,ienv) = true;
         else
            vnodeath (itype,ienv) = false;*/
         fDread (p,tempvalue); fdumpline ( p );
		  /* growrate (itype,ienv)=tempvalue; */
         
         for (jtype = 1; jtype  <= (int)ntypes; jtype ++)
         {
            fDread (p, tempvalue); fdumpline ( p );
		/*	mutrate(itype,jtype,ienv)=tempvalue;*/
			fDread (p, tempvalue); fdumpline ( p );
		/*	convtime(itype,jtype,ienv)=tempvalue;  */
			fDread (p, tempvalue); fdumpline ( p );
		/*  blebtime(itype,jtype,ienv)=tempvalue;*/
         }
      }
      for (idrug = 1; idrug <= ndrugs; idrug++)
      {
         fDread (p,tempvalue );  fdumpline ( p );
		 /* drugkill (idrug,itype)=tempvalue; */
	  }
      for (idose = 1; idose <= nallts; idose++)
      {
         fDread (p,tempvalue ); fdumpline ( p );
		/* timekill (idose,itype)=tempvalue; */
	  }
   }
   for (itime = 1; itime <= nsched; itime++)
   {
      fDread (p, sched[itime].t); fdumpline ( p );
      fDread (p, sched[itime].d); fdumpline ( p );
	  sched[itime].df = 1.0;
   }
   for (itime = 1; itime <= nallts; itime++)
   {
      fDread (p, timevec [itime]); fdumpline ( p );
   }
   for (idrug = ENVINDEX; idrug <= ndrugs; idrug++)
   {
      i = 0;
      while ((i <= MAX_CHAR) and not fqendofline ( p ))
      {
         drugname[idrug][i] = (char) getc ( p );
         i = i + 1;
      }
      drugname[idrug][i] = chr (ENDOFSTRING);
      fdumpline ( p );
      fIread(p, ndoses [idrug]); fdumpline ( p );
      for (idose = 1; idose <= ndoses [idrug]; idose++)
      {
         fDread (p, doselist [idrug][idose]); fdumpline ( p );
      }
   }
   fDread (p, deltakill); fdumpline ( p );
   fDread (p, topkill); fdumpline ( p );
   fIread (p, nprobs); fdumpline ( p );
   for (iprob = 1; iprob <= nprobs; iprob++)
   {
      fDread (p, probtest [iprob]); fdumpline ( p );
   }
   i = 0;
   while ((i <= MAX_CHAR) and not fqendofline  ( p ))
   {
      qaxis[i] = (char) getc ( p );
      i = i + 1;
   }
   fdumpline ( p );

   fIread (p, qsamedoub);    fdumpline ( p );
   fIread (p, qsamecyclet);  fdumpline ( p );
   fIread (p, qnodeath);     fdumpline ( p );
 
   fIread (p, nbins); fdumpline ( p );
   fIread (p, ns); fdumpline ( p );
   fIread (p, nv); fdumpline ( p );
   for (is = 1; is <= ns; is++)
   {
      fDread (p, slist [is]); fdumpline ( p );
   }

   for (ival = 1; ival <= nv; ival++)
   {
      fDread (p, val [ival]); fdumpline ( p );
   }

   for (ibin = 1; ibin <= nbins+1; ibin++)
   {
      fDread (p, n [ibin]); fdumpline ( p );
   }

   for (ibin = 1; ibin  <= nbins; ibin ++)
   {
      fDread (p, bound [ibin]); fdumpline ( p );
   }

   for (ibin = 1; ibin <= nbins; ibin++)
   {
      fDread (p, deltan [ibin]); fdumpline ( p );
   }

   /* added by Qingshou, 10/22/96 */
  
   fIread (p, nloops);  fdumpline ( p );
   fDread (p,StartT);  fdumpline ( p );
 
   for ( itype=1; itype<=active_ntypes; itype++)
   {
      fDread (p,tempvalue); fdumpline ( p );
	/* LookUp[itype].CN=temovalue; */
   }   	 

   fDread (p, err); fdumpline ( p );

   for ( i=1; i<=active_ntypes;i++) {
	  for (nowenvl = 0; nowenvl < nenvirons; nowenvl++)
	  {
		setrates (i,nowenvl);
		buildkill(i,nowenvl);
	  }
   }
}
