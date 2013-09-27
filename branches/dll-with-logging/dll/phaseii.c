/* This is modified version of phaseii.simon 
   by Qingshou, 2/20/98
   using funcion call phaseii_simno() 
   only the results for optimum design are kept */

#include <windows.h>
#include <math.h>
#include <stdlib.h>
#include <stdio.h>
#include "phaseii.h"
#include "logger.h"

/* 
function phaseii_simon() to create results of phaseII design  
input: alpha -- type I error
	   beta  -- type II error
	   P1 -- "bad" response rate
	   P2 -- "good" response rate
	   nmax -- patient upper limit (maximum sample size )
output r1_final, n1_final, r_final, n_final
*/

extern int EXPORT PASCAL phaseii_simon(double alpha, double beta,double p1,double p2,int nmax)
{
    int i,n,nmin,r,start_next_n2;	/* loop control variables */
    int	n1_low,r_Old,r1,r2,n1,n2;
     
    int R_max[MAX_B_P1_SIZE];
    double Fun_val,Val;
	char disp_str[80];

	fprintf(logfile,"%s\tfunction:phaseii.phaseii_simon\talpha:%12f\tbeta:%12f\tp1:%12f\tp2:%12f\tnmax:%d\n", gettime(),alpha, beta, p1, p2, nmax);
	if ( nmax <= 0.0 ) nmax=MAX_SAMPLE_SIZE/2; /* default is 105 */
	else if ( nmax >= MAX_SAMPLE_SIZE ) nmax = MAX_SAMPLE_SIZE-1;
	
	if ( alpha <0.0 || alpha > 0.3 ) {
		MessageBox(NULL,"Value for alpha between 0 and 0.3 only.","treat.dll",MB_ICONEXCLAMATION | MB_OK);
		return FALSE;
	}
	if  ( beta < 0.0 || beta > 0.3 ) {
		MessageBox(NULL,"Value for beta between 0 and 0.3 only.","treat.dll",MB_ICONEXCLAMATION | MB_OK);
		return FALSE;
	}
	if ( p1 < 0.0 || p1 > 1.0 ) {
		MessageBox(NULL,"Value for 'poor' response rate between 0 and 1 only.","treat.dll",MB_ICONEXCLAMATION | MB_OK);
		return FALSE;
	}
	if ( p2 < 0.0 || p2 > 1.0 ) {
		MessageBox(NULL,"Value for 'good' response rate between 0 and 1 only.","treat.dll",MB_ICONEXCLAMATION | MB_OK);
		return FALSE;
	}
	if ( p2 <= p1 ) {
		MessageBox(NULL,"Value for 'good' response rate should be greater than 'poor' response rate.","treat.dll",MB_ICONEXCLAMATION | MB_OK);
		return FALSE;
	}

   /* initialize Val_Final, r1_Final and n1_final */
    Val_Final  = (double)INIT_VAL_FINAL;
    n1_final = nmax;
	r1_Final = 0;

    /* set R_max array to -1 in the begining */
    for (i=0; i < MAX_B_P1_SIZE; i++)
	R_max[i] = -1;

    /* done with reding input values now create tables */

    for (n=1; n <= nmax; n++)
    {
	  /****************************************************
	   calculate values of B(0,n,p1) = b(0,n,p1) = (1-p1)^n
	   calculate values of B(0,n,p2) = b(0,n,p2) = (1-p2)^n
	   *****************************************************/
	   AB_array_P1[0][n] = b_array_P1[0][n] = (double)pow((double)(1.0 - p1),
						      (double)n);
	   AB_array_P2[0][n] = b_array_P2[0][n] = (double)pow((double)(1.0 - p2),
						      (double)n);
	   for(r=1; r <= n; r++)
	   {
	      b_array_P1[r][n] = comp_cumu_binomial(r,n,p1,P1_TYPE);
	      b_array_P2[r][n] = comp_cumu_binomial(r,n,p2,P2_TYPE);

	      AB_array_P1[r][n] = AB_array_P1[r-1][n] + b_array_P1[r][n];
	      AB_array_P2[r][n] = AB_array_P2[r-1][n] + b_array_P2[r][n];
	   }  /* end of loop over r */
    } /* end of loop over n */

    /* done with building arrays now process them */

    /* find n1_low from the array AB_array_P2 */
    n1_low = 0;
    for ( n=1; n <= nmax; n++)
    {
	 if ( AB_array_P2[0][n] <= beta)
	 {
	     n1_low = n;
	     break;
	 }
    } /* end of for n < nmax */


    /* intialize value of r_Old */
    r_Old = nmax;

    /* load the R_max array with values */
    for(n=nmax; n >= n1_low; --n)
    {
	for(r=r_Old; r >= 0; --r)
	{
	     if (AB_array_P2[r][n] <= beta)
	     {
		 r_Old = r;
		 R_max[n] = r_Old;
		 break;
	     }
	     else
		R_max[n] = -1;
	} /* end of for r=r_Old */
    } /* end of for n > n1_low */

    /* compute nmin */
    nmin = compute_nmin(alpha,beta,p1,p2);

    /* do the final computations now */

    for (n1=n1_low; n1 <= nmax-1; ++n1)   /* loop 6 */
    {
	if(R_max[n1] < 0)
	   continue;

	for (n2=max(1,nmin-n1); n2 <= nmax-n1; ++n2)  /* loop 7 */
	{
	    start_next_n2 = TRUE;

	    /* loop 8 */
	    for(r1=R_max[n1]; r1 >= 0 && start_next_n2; --r1)
	    {
		/* loop 9 */
		for(r2=R_max[n1+n2]; r2 >= 0 && start_next_n2; --r2)
		{
		       if ((Fun_val = compute_FUN(r1,r2,n1,n2,P1_TYPE)) <
				   (1-alpha))
		       break;

		    if((Fun_val = compute_FUN(r1,r2,n1,n2,P2_TYPE)) > beta)
			    continue;

		    Val = (n1+n2) - (n2*AB_array_P1[r1][n1]);

		    if(Val < Val_Final)
		    {
		      Val_Final = Val;
		      r1_Final	= r1;
		      r_Final	= r2;
		      n1_final	= n1;
		      n_final	= n1+n2;
		      start_next_n2 = FALSE;
		    } /* end of if Val < Val_Final */

		}  /* end of for r2 > 0 loop */
	     } /* end of for r1 > 0 loop */
	} /* end of n2 <= nmax-n1 loop */
    } /* end of n1 <= nmax-1 loop */

    /* compute PET_final and PET_max */
    PET_final = AB_array_P1[r1_Final][n1_final];

	if ( Val_Final  == INIT_VAL_FINAL ) {
		sprintf(disp_str,"%s%d%s","Sorry. For the previous input values, the sample size will be larger than ",nmax,".");
		MessageBox(NULL,disp_str,"treat.dll",MB_ICONEXCLAMATION | MB_OK);
		return FALSE;
	}
	else return TRUE;
}

/* function to compute nmin */
int compute_nmin(alpha,beta,p1,p2)
double alpha;
double beta;
double p1;
double p2;
{   int nmin;
    double z_alpha,z_beta;
    double pow( );

    z_alpha = get_z_factor(alpha);
    z_beta  = get_z_factor(beta);

    nmin = (int)pow (
		      (
			 z_alpha * pow(p1*(1-p1),0.5) +
			 z_beta * pow(p2*(1-p2),0.5)
		      ) / (p2 - p1),2.0
		    );
    return(nmin-5);
} /* end of compute_nmin() */

double get_z_factor(probability)
double probability;
{
    if(probability <= (double)0.05)
       return((double)1.645);
    else if(probability <= (double)0.1)
	    return((double)1.28);
	 else if(probability <= (double)0.15)
		 return((double)1.03);
	      else if(probability <= (double)0.2)
		      return((double)0.84);
		   else if(probability <= (double)0.25)
			   return((double)0.67);
			else if(probability <= (double)0.3)
				return((double)0.52);
} /* end of get_z_factor() */

double compute_FUN(r1,r2,n1,n2,array_to_use)
int r1;
int r2;
int n1;
int n2;
int array_to_use;
{
      double FUN_value,sum,product;
      int x,limit;

      /* compute limit for summation */
      FUN_value = (double)0.0;
      limit = min(n1,r2);

      if(array_to_use == P1_TYPE)
      {
	  sum = (double)0.0;
	  if(limit > 0)
	  {
	     for (x=r1+1; x <= limit; x++)
	     {
		product = b_array_P1[x][n1] * AB_array_P1[r2-x][n2];
		sum += product;
	     }
	  }
	  FUN_value = AB_array_P1[r1][n1] + sum;
      }
      else if (array_to_use == P2_TYPE)
	   {
	       sum = (double)0.0;
	       for (x=r1+1; x <= limit; x++)
	       {
		  product = b_array_P2[x][n1] * AB_array_P2[r2-x][n2];
		  sum += product;
	       }
	       FUN_value = AB_array_P2[r1][n1] + sum;
	   }
      return(FUN_value);
} /* end of compute_FUN() */

double comp_cumu_binomial(r,n,p,array_to_use)
int r;
int n;
double p;
int array_to_use;
{
    double value1,value2;

    /* compute binomial probability */
	if ( p >= 1.0 )  { 
		return (1.0);
	}
	else {
		value1 = p/(1-p);
		value2 = ((double)(n-r+1)/(double)r);

		if (array_to_use == P1_TYPE)
		   return(value1 * value2 * b_array_P1[r-1][n]);
		else if (array_to_use == P2_TYPE)
			return(value1 * value2 * b_array_P2[r-1][n]);
	}
} /* end of comp_cumu_binomial() */
