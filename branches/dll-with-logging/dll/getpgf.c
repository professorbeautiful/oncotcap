/* This is new version modified at 5/21/97, for pc and MPI version */
#include "build.h"

#include     "defines.h"
#include     <stdlib.h>

#define NUMPOINTS 8
#define MAXNUMS 35
#define MAXREPEAT 5000
#define BOOTSIZE 5000

extern double jointpgf( );
extern double  ranmarm();
extern double EndT,error;
extern int nloops;
extern char outName[30];

void resample();
double data[MAXREPEAT+1][10], bdata[BOOTSIZE+1][10];
void get_pgf(delta_t,nrep,mymargin,brep,bresize)
double delta_t;
int nrep,mymargin,brep,bresize;
{
 int     irep,ptype, qtype, itype, jtype, mtype,b,
         numpoints[NUMPOINTS], num_s=10,reject=0,Ok;
 double  sm[MAXNUMS],sc[NUMPOINTS][NUMPOINTS][MAXNUMS],
         count1=2,count2=1.0e+7,
         sum_pgfc[NUMPOINTS],sum_pgfm,est_pgfm, pgf_meancpq,
		 pgf_meanmpq,pgf_meanc[NUMPOINTS][MAXNUMS],pgf_meanm[MAXNUMS],
         delta_n, log_n,log_n_next, temp_n,temp_pgfm,
		 temp_pgfc,ssvec[MAXTYPES+2],
         a[5],k[NUMPOINTS],k_bak[NUMPOINTS],
         covpqm,covpqc, cov_Tcomb, Tcomb, temp_Tcomb,z;
 FILE *fpw,*fpwc,*fpwm,*resultfp;
 /* fpw,fpwc,fpwm ====> outfile (pgffile), covfile, covmfile for mrgine */
 char pgfdname[60],covdname[60],covmname[60],basename[60],resultname[60];;

/* initialize k and other things. 
   kk will decide the proportion of type 1 compared with type 2 cell
   it decides the quntiles which is interesting test range   
   when k[i]=0.001 ===>(ka=0.001 and kb=0.01),
   some  s is negative       */
 
 a[1]=a[4]=1.0; a[2]=a[3]=-1.0;
 numpoints[1]=1; numpoints[2]=4;

 k_bak[1]=0.0001; k_bak[2]=0.002;
 sprintf( basename,".%d.%d.0%d.%d",(int)(EndT),nrep,(int)(delta_t*1000),mymargin);

 for ( itype=1; itype<=(int)ntypes; itype++)
     ssvec[itype]=1.0;
 /* read data from file */
 if  ( (fpw=fopen(outName,"r"))==NULL){
     printf("Can't open the data file for reading\n");
     printf("You should have simulation data first\n");
     exit(0);
 }

 for (irep=1; irep<= nrep; irep++){
     for (jtype=1; jtype<=(int) ntypes; jtype++) {
	     fscanf(fpw,"%lf ",&data[irep][jtype]);
     }
 }
 fclose(fpw);

sprintf(resultname,"result%s",basename);
if  ( (resultfp=fopen(resultname,"w"))==NULL){
     printf("Can't open the file for writting\n");
     printf("You should have simulation data first\n");
     exit(0);
 }

fprintf(resultfp,"delta_t = %f \n", delta_t);
fprintf(resultfp,"nloops = %d\n", nloops);
fprintf(resultfp,"endT = %f \n", EndT);
fprintf(resultfp,"datafile=%s\n",outName);
fprintf(resultfp,"ntypes=%d\n",ntypes);
fprintf(resultfp,"error=%lf\n", error);
fprintf(resultfp,"k1 = %f, k2=%f\n", k_bak[1],k_bak[2]);
fprintf(resultfp,"brep = %d, bresize=%d\n", brep,bresize);


/* construct s vector, according to interval that you have chosen*/  
 temp_n=log10(count1);
 delta_n=(log10(count2)-temp_n)/num_s;

/* Now, construct ss vector, ss(i,j,k) ==> ith cell type (e.g., 1==> x, 2==>y,
 kth element of S vector  and jth part of four parts   */
	 
 for ( jtype=1; jtype <=num_s; jtype++) {
     log_n=temp_n+delta_n*((double)(jtype-1));
/* see formula (3) of the draft      */
     if ( mymargin == 2 ) {
		 log_n_next=temp_n+delta_n*((double)(jtype));
		 k[1]=k_bak[1];
		 k[2]=k_bak[2];
		 Ok = false;
		 do {
			 sc[2][2][jtype]=1.0-(1.0/(k[2]*pow(10.0,log_n)));
			 if (sc[2][2][jtype] > 0.0 ) {
				 sc[2][4][jtype]=1.0-(1.0/(k[1]*pow(10.0,log_n)));
				 if (sc[2][4][jtype] > 0.0 ) {
					sc[2][1][jtype]=1.0-(1.0/(k[2]*pow(10.0,log_n_next)));
					if (sc[2][1][jtype] > 0.0 )  {
						sc[2][3][jtype]=1.0-(1.0/(k[1]*pow(10.0,log_n_next)));
					   if (sc[2][3][jtype] > 0.0 ) {
							 sm[jtype]=1.0-(1.0/pow(10.0,log_n_next));
							 sc[1][2][jtype]=sc[1][4][jtype]=1.0-(1.0/pow(10.0,log_n));
							 sc[1][1][jtype]=sc[1][3][jtype]=1.0-(1.0/pow(10.0,log_n_next));
							 Ok=true;
					   }
					}
				 }
			 }
			 if ( Ok == false ) {
				 k[1]=10*k[1];
				 if ( k[1] > 1.0 ) k[1]=1.0;
				 k[2]=10*k[2];
				 if ( k[2] > 1.0 ) k[2]=1.0;
			 }
		} while ( Ok == false);
	 }
     else
	 if ( mymargin == 1 ) {
		 sm[jtype]=1.0-(1.0/pow(10.0,log_n));
		 sc[1][1][jtype]=1.0-(1.0/pow(10.0,log_n));
     }
 }

 printf("finish to construct a s vector.\n");
 
 for (ptype=1; ptype<=num_s; ptype++) {

	 for (mtype=1; mtype <= (int)ntypes; mtype++ ){
		 if ( mtype == mymargin){
			 ssvec[mtype]=sm[ptype];
		 }
		 else {ssvec[mtype] = 1.0;}
     }
  
     /* get lambda(s1i,s2i), marginal stuff */
     pgf_meanm[ptype]=jointpgf(ssvec, nt, nconds);
     /* for conditional stuff */
     for (itype=1; itype<=numpoints[mymargin]; itype++) {
		 for (mtype=1; mtype<= (int) ntypes;mtype++){
			 if ( mtype <= mymargin )
				 ssvec[mtype]=sc[mtype][itype][ptype];
			 else  ssvec[mtype] = 1.0;
		 }

		 pgf_meanc[itype][ptype]=jointpgf(ssvec, nt, nconds );
     }
     fprintf(resultfp,"s1=%11.9lf s2=%11.9lf pgfm= %11.9lf pgfc=%11.9lf  \n ",ssvec[1],ssvec[2],pgf_meanm[ptype],pgf_meanc[1][ptype]);
 } /* end of ptype loop */

 
 sprintf( covdname,"s2_cov%s",basename);
 sprintf( covmname,"s2_covm%s",basename);

 if (((fpwc=fopen(covdname,"w"))==NULL)||((fpwm=fopen(covmname,"w"))==NULL)){
     printf("Can't open the data file for writing\n");
 }

 cov_Tcomb=0;

 /* get cov matrix */
 for (ptype=1; ptype<=num_s; ptype++) {
     for (qtype=1; qtype<=num_s; qtype++) {
		 
		 for (mtype=1; mtype<= (int)ntypes; mtype++) {
			 if ( mtype==mymargin)  {             
				ssvec[mtype]=sm[ptype]*sm[qtype];
			 }
			 else {ssvec[mtype]=1.0;}
		 }
		 /* get lambda(s1is1j,s2is2j) */
		 pgf_meanmpq =jointpgf(ssvec, nt, nconds );
		 
		 covpqm = pgf_meanmpq - pgf_meanm[ptype]*pgf_meanm[qtype] ;

		 fprintf(fpwm,"%11.7lf  ",covpqm);
		 
		 covpqc=0.0;

		 /* for conditional stuff */
		 for (itype=1; itype<=numpoints[mymargin]; itype++) {
			 for (jtype=1; jtype<=numpoints[mymargin]; jtype++){
				 for (mtype=1; mtype<=(int) ntypes; mtype++) {    
					 if ( mtype <= mymargin )
						 ssvec[mtype]=sc[mtype][itype][ptype]*sc[mtype][jtype][qtype];
					 else ssvec[mtype]=1.0;
				 }
				 /* get lambda(s_x,i,p*s_x,j,q;s_y,i,p*s_y,j,q) */
				 pgf_meancpq=jointpgf(ssvec, nt, nconds );
				 covpqc+=a[itype]*a[jtype]*(pgf_meancpq - pgf_meanc[itype][ptype]*pgf_meanc[jtype][qtype] );
			     /* for debug */
				 if (( mymargin==1 ) && (covpqc != covpqm ))
					fprintf(resultfp,"sm[%d]=%11.9lf,covm=%11.9lf,covc=%11.9lf pqm=%11.9lf,pqc=%11.9lf \n",
					qtype,sm[qtype],covpqm,covpqc,pgf_meanmpq,pgf_meancpq);
			 }  /* end jtype ... */
		 } /* end itype ... */

		 cov_Tcomb +=covpqc;
		 fprintf(fpwc,"%11.7lf  ",covpqc);
		
		 if ((qtype%10)==0) {
			 fprintf(fpwm,"\n");
			 fprintf(fpwc,"\n");
		 }
     }/*end qtype for loop*/
 } /*end ptype for loop*/

 fclose(fpwc);
 fclose(fpwm);
 printf("finish to calculate the covariance matrix.\n");

 sprintf( pgfdname,"s2_pgf%s",basename);
		              
 if ((fpw=fopen(pgfdname,"w"))==NULL){
	printf("Can't open the data file for writing\n");
 }

 for ( b=1; b<=brep; b++ ) {
     Tcomb=0;
     fprintf(resultfp,"\nrep=%d",b);

     /* resample the data set  */
     resample(bresize,nrep,mymargin);

     for (ptype=1; ptype<=num_s; ptype++) {	     
		 /* est_pgf from simulation model for cond  */
		 sum_pgfm=0.0; 
		 for (itype=1; itype<=numpoints[mymargin]; itype++){
			 sum_pgfc[itype]=0.0;
		 }

		 for (irep=1; irep<= bresize; irep++){
			 if  (sm[ptype]>0.0){
			 temp_pgfm=pow(sm[ptype], bdata[irep][mymargin]);
			 }

			 sum_pgfm+=temp_pgfm;

			 for (itype=1; itype<=numpoints[mymargin]; itype++) {
				 temp_pgfc=1.0;
				 for (mtype=1; mtype<= mymargin; mtype++) {
					 if  (sc[mtype][itype][ptype]>0.0) {
						temp_pgfc=temp_pgfc*pow(sc[mtype][itype][ptype], bdata[irep][mtype]);
					 }
				 }
				 sum_pgfc[itype]+=temp_pgfc;
			 }
		 } /* end of irep loop */
		 if	( sum_pgfm >= 0.0 ) {est_pgfm=sum_pgfm/bresize;}
   
		 temp_Tcomb=0.0;
		 for (itype=1; itype<=numpoints[mymargin]; itype++) {
			 if	( sum_pgfc[itype] >= 0.0 ) {
			 temp_Tcomb += a[itype]*(pgf_meanc[itype][ptype]-sum_pgfc[itype]/bresize);  
			/* printf("epgf[%d]=%11.9lf ",itype,sum_pgfc[itype]/bresize);*/
			 }
		 }
		 Tcomb+=temp_Tcomb;
		 /* pgf_meanpm is pgf for marginal 
			est_pgf is pgf from simulation     
			if mymargin==1, ss[1][1][]=s[], and 
			pgf_meanp=pgf_meanp,......... */

		 fprintf(fpw,"%-2d ", ptype);
		 for (mtype=1; mtype<=mymargin;mtype++)	{	 
			 for ( itype=1; itype<=numpoints[mymargin]; itype++){
			 fprintf(fpw,"%11.9lf ",sc[mtype][itype][ptype]);
	/*		 fprintf(fpw,"%11.9lf ",sum_pgfc[itype]/bresize);*/
			 }
		 }
		 fprintf(fpw,"%11.9lf  %11.9lf ",est_pgfm,pgf_meanm[ptype]);
		 if ( mymargin >= 2) {
			 fprintf(fpw,"%11.9lf\n",temp_Tcomb);
		 }
		 else fprintf(fpw,"\n");
     }/* end  of ptype  loop */
  	 
	 fprintf(resultfp,"\nTcomb=%12.9lf\n",Tcomb);
     fprintf(resultfp,"bresize*sigma2_Tcomb=%12.9lf,",cov_Tcomb);
	 z=(sqrt((double)bresize)*Tcomb/sqrt(cov_Tcomb));
	 fprintf(resultfp,"Z=%e\n",z);
	 if (( z<-1.96 ) || ( z>1.96 ))
		 reject++;
 }/* end of  brep loop */
 fclose(fpw);
 fprintf(resultfp,"\npower=%lf\n",((double)reject)/((double)brep)); 
 fclose(resultfp);
}

void  resample(bresize,nrep,mymargin)
int bresize,nrep,mymargin;
{
    int i,j, nrand;

    for ( i=1; i<=bresize; i++) {
		if ( bresize == nrep ){
			nrand=i;
		}
		else {
	    nrand=(int)( ranmarm(INITRAND)*nrep)+1;
		}
		for ( j=1; j<=mymargin; j++) {
			bdata[i][j]=data[nrand][j];
		}
    }
}





