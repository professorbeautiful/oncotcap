#include "build.h"
#include "defines.h"

extern eprint( );
extern  int  nloops;
extern double StartT;

void outfile( filenme )
   char *filenme;
{
   int itype, itime, jtype, is, ibin, ival, idrug, idose, ienv;
   int iprob, i;
   char str[100];
   char header[100];
   double tempvalue;
 

   strcpy (header,"This is a test .par file");
   if ( ( ofil = fopen( filenme, "w" ) ) == NULL )
   {
      sprintf( "ERROR (outfile):  Can't open \"%s\" for writing.\n\n",
               filenme );
      eprint( str );
   }
   else
   {
	  fprintf( ofil, TCAPVERSION);
      fprintf( ofil, "%s\n", header );
      fprintf( ofil, "%d\n", verbos );
      fprintf( ofil, "%d\n", ndrugs );
      fprintf( ofil, "%d\n", ntypes );
      fprintf( ofil, "%d\n", nallts );
      fprintf( ofil, "%d\n", nt );
      fprintf( ofil, "%d\n", nsched );
      fprintf( ofil, "%d\n", nenvirons );
      fprintf( ofil, "%d\n", nenvlist );
      for ( ienv = 1; ienv <= nenvlist; ienv++ )
      {
         fprintf( ofil, "%d\n", envlist[ienv].e );
         fprintf( ofil, "%f\n", envlist[ienv].t );
      }
      for (itype = 1; itype <= active_ntypes; itype++ )
      {
         fprintf( ofil, "%f\n", cellstart[itype] );
         i = 0;
         while ( i <= MAX_CHAR &&
                 ! ( cellname(itype)[i] == chr( ENDOFSTRING ) ) )
         {
            fprintf( ofil, "%c", cellname(itype)[i] );
            i = i + 1;
         }
         fprintf( ofil, "%c\n", chr( ENDOFSTRING ) );
         fprintf( ofil, "\n" );
         fprintf( ofil, "%f\n", svec[itype] );
         fprintf( ofil, "%f\n", smask[itype] );
         for ( ienv = 0; ienv < nenvirons; ienv++ )
         {
           fprintf( ofil, "dt=%f\n", doubletime(itype,ienv) );
			fprintf( ofil, "%f\n", xi(itype,ienv));
			fprintf( ofil, "%f\n", mu(itype,ienv) );
			fprintf( ofil, "%f\n", mutsum(itype,ienv) );
			fprintf( ofil, "%f\n", gama(itype,ienv) );
			fprintf( ofil, "%f\n", deathswitch(itype,ienv) );
			fprintf( ofil, "%f\n", deathtime(itype,ienv));
            fprintf( ofil, "%f\n", cycletime(itype,ienv) );
            fprintf( ofil, "%f\n", birthrate(itype,ienv) );
            fprintf( ofil, "%f\n", deathrate(itype,ienv) );
            fprintf( ofil, "gr=%f\n", growrate(itype,ienv) );
            for ( jtype = 0; jtype <LookUp[itype].ParaPr->NumOfMu; jtype++ )
            {
			   tempvalue =mutrate(itype,jtype,ienv) ;
               fprintf( ofil, "mutrate=%f\n", tempvalue);
			}
	      for ( jtype = 0; jtype < LookUp[itype].ParaPr->NumOfCo; jtype++ )
            {
			   tempvalue = convrate(itype,jtype,ienv) ;
               fprintf( ofil, "%f\n", tempvalue);
			   /*tempvalue = GET_BLEBTIME(itype,jtype,ienv) ;
               fprintf( ofil, "%f\n", tempvalue);*/
            }
		  }
         for ( idrug = 1; idrug <= ndrugs; idrug++ )
            fprintf( ofil, "%f\n", drugkill(idrug,itype,0) );
         for ( idose = 1; idose <= nallts; idose++ )
            fprintf( ofil, "%f\n", timekill(idose,itype,0) );
      }


      for ( itime = 1; itime <= nsched; itime++ )
      {
         fprintf( ofil, "%f\n", sched[itime].t );
         fprintf( ofil, "%d\n", sched[itime].d );
      }

      for ( itime = 1; itime <= nallts; itime++ )
         fprintf( ofil, "%f\n", timevec[itime] );

      for ( idrug = ENVINDEX; idrug <= ndrugs; idrug++ )
      {
         i = 0;
         while ( i <= MAX_CHAR &&
                 ! ( drugname[idrug][i] == chr( ENDOFSTRING ) ) )
         {
            fprintf( ofil, "%c", drugname[idrug][i] );
            i = i + 1;
         }
         fprintf( ofil, "%c\n", chr( ENDOFSTRING ) );
         fprintf( ofil, "%d\n", ndoses[idrug] );
         for ( idose = 1; idose <= ndoses[idrug]; idose++ )
            fprintf( ofil, "%f\n", doselist[idrug][idose] );
      }

      fprintf( ofil, "%f\n", deltakill );
      fprintf( ofil, "%f\n", topkill );
      fprintf( ofil, "%d\n", nprobs );

      for ( iprob = 1; iprob <= nprobs; iprob++ )
         fprintf( ofil, "%f\n", probtest[iprob] );

      fprintf( ofil, "%s\n", qaxis );
      fprintf( ofil, "%d\n", qsamedoub );
      fprintf( ofil, "%d\n", qsamecyclet );
      fprintf( ofil, "%d\n", qnodeath );
      fprintf( ofil, "%d\n", nbins );
      fprintf( ofil, "%d\n", ns );
      fprintf( ofil, "%d\n", nv );

      for ( is = 1; is <= ns; is++ )
         fprintf( ofil, "%f\n", slist[is] );

      for ( ival = 1; ival <= nv; ival++ )
         fprintf( ofil, "%f\n", val[ival] );

      for ( ibin = 1; ibin <= nbins + 1; ibin++ )
         fprintf( ofil, "%f\n", n[ibin] );

      for ( ibin = 1; ibin <= nbins; ibin++ )
         fprintf( ofil, "%f\n", bound[ibin] );

      for ( ibin = 1; ibin <= nbins; ibin++ )
         fprintf( ofil, "%f\n", deltan[ibin] );
		  
	   /* added by Qingshou. 10/21/96  */
	    /* fprintf (ofil,"%d\n", nloops); 
	 	 fprintf (ofil,"%lf\n", StartT); 
		 for ( itype=1; itype<=ntypes; itype++)
         	 fprintf (ofil,"%lf\n", CN[itype]); */

	 fprintf (ofil,"\nGomp\n");
	 for ( itype=0; itype<=NumGompRules; itype++) {
		  fprintf (ofil,"%d\n",itype);
		  fprintf (ofil,"%d\n",GompRule[itype][0].cn);
		  fprintf (ofil,"%d\n",GompRule[itype][0].level);
		  if ( GompRule[itype][0].gp_or_inv.inv_log_gp > 0 )
			  GompRule[itype][0].gp_or_inv.GP=exp(1/GompRule[itype][0].gp_or_inv.inv_log_gp);
		  else GompRule[itype][0].gp_or_inv.GP= GompRule[itype][0].gp_or_inv.inv_log_gp;
		  fprintf (ofil,"%lf\n",GompRule[itype][0].gp_or_inv.GP);
		  fprintf (ofil,"%lf\n",GompRule[itype][0].GS);
	 }
	  fprintf( ofil, "%f\n", err );

      fclose( ofil );
}
}




