/* This is new version modified at 9/23/98, from getpgf.c   
   s values are based on the quantiles for specific regions
*/
#include "build.h"

#include     "defines.h"
#include     <stdlib.h>

#define NUMPOINTS 8
#define MAXNUMS 35
#define MAXREPEAT 50000
#define BOOTSIZE 5000

#define Nx 1
#define MINIMUM        6
#define UPPER_QUANTILE 3
#define MID 4
#define LOWER_QUANTILE 5
#define MAXIMUM        2
#define LOWQ  0.25
#define UPQ 0.75

extern double jointpgf( );
extern double  ranmarm();
extern double EndT,error;
extern int nloops;
extern char outName[30];

void resample(),q_sort(),make_noise();
double data[MAXREPEAT+1][10], bdata[BOOTSIZE+1][10];
void get_pgf(delta_t,nrep,mymargin,brep,bresize)
double delta_t;
int nrep,mymargin,brep,bresize;
{
 int     irep,ptype, qtype, itype, jtype, mtype,b,sort=false,noise_region=0,
		 test_region=0,lowerbound,upperbound,
         numpoints[NUMPOINTS], num_s=10,reject=0,noise=false;
 double  sm[MAXNUMS],sc[NUMPOINTS][NUMPOINTS][MAXNUMS],
         sum_pgfc[NUMPOINTS],sum_pgfm,est_pgfm, pgf_meancpq,
		 pgf_meanmpq,pgf_meanc[NUMPOINTS][MAXNUMS],pgf_meanm[MAXNUMS],
         nxy[MAXNUMS][7],margin_data,delta_n,temp_n,temp_pgfm,
		 temp_pgfc,ssvec[MAXTYPES+2],
         a[5],
         covpqm,covpqc, cov_Tcomb, Tcomb, temp_Tcomb,z;
 FILE *fpw,*fpwc,*fpwm,*resultfp;
 /* fpw,fpwc,fpwm ====> outfile (pgffile), covfile, covmfile for mrgine */
 char pgfdname[60],covdname[60],covmname[60],basename[60],resultname[60];;
 
 a[1]=a[4]=1.0; a[2]=a[3]=-1.0;
 numpoints[1]=1; numpoints[2]=4;

 sprintf( basename,".%d.%d.0%d.%d",(int)(EndT),nrep,(int)(delta_t*1000),mymargin);

 switch (test_region) {
	 case 1:
		 lowerbound=MINIMUM;
		 upperbound=LOWER_QUANTILE;
		 break;
	 case 2:
		 lowerbound=LOWER_QUANTILE;
		 upperbound=UPPER_QUANTILE;
	 break;
	 case 3:
		 lowerbound=UPPER_QUANTILE;
		 upperbound=MAXIMUM;
	 break;
	 case 0:
		 lowerbound=MINIMUM;
		 upperbound=MAXIMUM;
 }

 for ( itype=1; itype<=(int)ntypes; itype++)
     ssvec[itype]=1.0;
 /* read simulation data from file */
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

 /* read quantile data from file */
 if  ( (fpw=fopen("d:\\newtest\\quantile.dat","r"))==NULL){
     printf("Can't open the file for reading\n");
     printf("You should have summary data first\n");
     exit(0);
 }

 for (irep=1; irep<=num_s +3 ; irep++){ 
	 /* first num_s +1 points construct num_s boxes;
		last-1 row for type y cell's marginal 
		last row for type x cell
	 */
     for (jtype=1; jtype<= MINIMUM; jtype++) { 
	 /* first col for Nx, last three cols for Ny (100%,95%,50%,5%,0% quantile) */
	     fscanf(fpw,"%lf ",&nxy[irep][jtype]);
     }
 }
 fclose(fpw);


 if ( sort == true ) {
	 q_sort(1,nrep,1);

	 /* write sorted data to file */
	 
	 if  ( (fpw=fopen(outName,"w"))==NULL){
		 printf("Can't open the data file for reading\n");
		 exit(0);
	 }

	 for (irep=1; irep<= nrep; irep++){
		 for (jtype=1; jtype<=(int) ntypes; jtype++) {
			 fprintf(fpw,"%e ",data[irep][jtype]);
		 }

		 fprintf(fpw,"\n");
	 }
	 fclose(fpw);
	 printf("\nYou have got sorted data based on type %d cell.\n",mymargin);
	 exit(0);
 }

if (( noise == true)  && (mymargin == 2 )){
	make_noise(noise_region,nrep,mymargin);
}

/* write noised data to file */
	 if  ( (fpw=fopen("noise.dat","w"))==NULL){
		 printf("Can't open the data file for reading\n");
		 exit(0);
	 }

	 for (irep=1; irep<= nrep; irep++){
		 for (jtype=1; jtype<=(int) ntypes; jtype++) {
			 fprintf(fpw,"%e ",data[irep][jtype]);
		 }

		 fprintf(fpw,"\n");
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
fprintf(resultfp,"brep = %d, bresize=%d\n", brep,bresize);


/* Now, construct ss vector, ss(i,j,k) ==> ith cell type (e.g., 1==> x, 2==>y,
 kth element of S vector  and jth part of four parts   */

 if ( mymargin==1 ){
	 /*//temp_n=log(10^7);
	 //delta_n=(log(10^11)-temp_n)/num_s;*/
	 temp_n=log10(nxy[num_s+3][lowerbound]);
	 delta_n=(log10(nxy[num_s+3][upperbound])-temp_n)/num_s;
 }
 else {
	 /* for marginal test */
	temp_n=log10(nxy[num_s+2][lowerbound]);
	delta_n=(log10(nxy[num_s+2][upperbound])-temp_n)/num_s;
 }

 for ( jtype=1; jtype <=num_s; jtype++) {
    /* see formula (3) of the draft      */
    if ( mymargin == 2 ) {
		 sc[2][2][jtype]=1.0-(1.0/nxy[jtype][upperbound]);
		 sc[2][4][jtype]=1.0-(1.0/nxy[jtype][lowerbound]);
		 sc[2][1][jtype]=1.0-(1.0/nxy[jtype+1][upperbound]);
		 sc[2][3][jtype]=1.0-(1.0/nxy[jtype+1][lowerbound]);
	 
		 sc[1][2][jtype]=sc[1][4][jtype]=1.0-(1.0/nxy[jtype][Nx]);
		 sc[1][1][jtype]=sc[1][3][jtype]=1.0-(1.0/nxy[jtype+1][Nx]);
		 
		 margin_data = temp_n+(jtype-1)*delta_n;
		 sm[jtype]=1.0-(1.0/pow(10.0,margin_data));
	 }
     else
	 if ( mymargin == 1 ) {
		 margin_data = temp_n+(jtype-1)*delta_n;

		 sm[jtype]=1.0-(1.0/pow(10.0,margin_data));
		 sc[1][1][jtype]=sm[jtype];
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
	 fprintf(resultfp,"s1=%11.9lf s2=%11.9lf pgfm= %11.9lf",ssvec[1],ssvec[2],pgf_meanm[ptype]);
     /* for conditional stuff */
     for (itype=1; itype<=numpoints[mymargin]; itype++) {
		 for (mtype=1; mtype<= (int) ntypes;mtype++){
			 if ( mtype <= mymargin )
				 ssvec[mtype]=sc[mtype][itype][ptype];
			 else  ssvec[mtype] = 1.0;
		 }

		 pgf_meanc[itype][ptype]=jointpgf(ssvec, nt, nconds );
     }
     fprintf(resultfp,"  s1=%11.9lf s2=%11.9lf pgfc=%11.9lf  \n ",ssvec[1],ssvec[2],pgf_meanc[1][ptype]);
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

		 fprintf(fpwm,"%13.9lf  ",covpqm);
		 
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
		 fprintf(fpwc,"%13.9lf  ",covpqc);
		
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
				 if ( mymargin ==1 ) fprintf(fpw,"%13.11lf ",sm[ptype]);
				 else fprintf(fpw,"%13.11lf ",sc[mtype][itype][ptype]);
	/*		 fprintf(fpw,"%11.9lf ",sum_pgfc[itype]/bresize);*/
			 }
		 }
		 if (brep <=1000 && mymargin==2 ) {
			 fprintf(fpw,"%13.11lf ",sm[ptype]);
		 }
		 fprintf(fpw,"%13.11lf  %13.11lf ",est_pgfm,pgf_meanm[ptype]);
		 if ( mymargin >= 2) {
			 fprintf(fpw,"%13.11lf\n",temp_Tcomb);
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

void q_sort(int Left, int Right,int margin) 
{
int Last,i;
void qswap();

	if (Left >= Right)  return;
   
    qswap (Left, (Left + Right) / 2);
    Last = Left;
    for ( i = Left + 1; i<= Right;i++ ) {
        if ( data[i][margin] < data[Left][margin] ) {
            Last = Last + 1;
            qswap(Last, i);
		}
    }
    
    qswap(Left, Last);
    q_sort(Left,(Last - 1),margin);
    q_sort((Last + 1),Right,margin);
 
}

void qswap(int i, int j )
 {
	double temp;
	int itype;

    for ( itype =1; itype <= (int)ntypes; itype ++ ) {
		temp = data[i][itype];
		data[i][itype] = data[j][itype];
		data[j][itype] = temp;
	}
 }

void make_noise(int region,int rep,int margin)
{
	int block,irep, lowerbound, upperbound;
	long bound, noise;
#ifndef RAND_MAX
#define RAND_MAX 2147483647
#endif

	srand(34567);

	if ( region==1) {
		/* lower quantile */
		block = (int)(LOWQ*rep);
		lowerbound = 1;
		upperbound = (long)block;
		bound =  (long) (0.0003*data[upperbound][1]);
	}
	else {
		if ( region ==3 ) {
			/* upper quantile */
			block = (int)(UPQ*rep);
			lowerbound = (long)(block+1);
			upperbound = rep;
			bound =  (long) (0.003*data[lowerbound][1]);
		}
		else {
			if ( region ==2 ) {
				/* middle region */
				lowerbound = (int)(LOWQ*rep)+1;
				upperbound = (int)(UPQ*rep);
				block = (int)(0.5*rep);
				bound =  (long) (0.0005*data[block][1]);
			}
			else {
				/* whole range */
				lowerbound = 1;
				upperbound = (int)(rep);
				block = (int)(0.5*rep);
				bound =  (long) (0.00015*data[block][1]);
			}
		}
	}

	
	for ( irep = lowerbound; irep <= upperbound;irep++) {
		noise = (long) (( ((double)rand())/RAND_MAX)*bound);
		data[irep][margin] += noise;
	}

}




