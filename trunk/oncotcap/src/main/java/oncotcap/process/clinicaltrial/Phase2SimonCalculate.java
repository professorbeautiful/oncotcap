package oncotcap.process.clinicaltrial;

import oncotcap.util.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.persistibleeditorpanel.*;

public class Phase2SimonCalculate {
	 /*
	function phaseii_simon() to create results of phaseII design
	input: Alpha -- type I error
		 beta  -- type II error
		 PrResp0 -- "bad" response rate
		 PrResp1 -- "good" response rate
		 nmax -- patient upper limit (maximum sample size )
	output r1_final, n1_final, r_final, n_final
	*/
	final int PrResp0_TYPE= 1;
	final int PrResp1_TYPE= 0;
	final int MAX_SAMPLE_SIZE=210;
	public final int INIT_VAL_FINAL=1000;
	double[][]  AB_array_PrResp0 = new double [MAX_SAMPLE_SIZE+1][MAX_SAMPLE_SIZE+1];
	double[][]  AB_array_PrResp1 = new double [MAX_SAMPLE_SIZE+1][MAX_SAMPLE_SIZE+1];
	double[][]  b_array_PrResp0 = new double [MAX_SAMPLE_SIZE+1][MAX_SAMPLE_SIZE+1];
	double[][]  b_array_PrResp1 = new double [MAX_SAMPLE_SIZE+1][MAX_SAMPLE_SIZE+1];
	public double PET_final, Val_Final;
	public int r1_Final,r_Final,n1_final, n_final;

	Phase2SimonEditorPanel parent;

	public void  calculate(Phase2SimonEditorPanel parent)
	{
		this.parent = parent;
		int i,n,nmin,r; /* loop control variables */
		int n1_low,r_Old,r1,r2,n1,n2;
		int[] R_max=new int[MAX_SAMPLE_SIZE];
		boolean start_next_n2;
		double Fun_val,Val;

		parent.SetWaitInfo();

		/* set R_max array to -1 in the begining */
		for (i=0; i < MAX_SAMPLE_SIZE; i++)
			R_max[i] = -1;

		/* initialize Val_Final, r1_Final and n1_final */
		r1_Final = 0;r_Final=0;n1_final = parent.nmax;n_final=0;
		Val_Final  = (double)INIT_VAL_FINAL;

		/* done with reding input values now create tables */

		for (n=1; n <= parent.nmax; n++)
		{
		  /****************************************************
			calculate values of B(0,n,PrResp0) = b(0,n,PrResp0) = (1-PrResp0)^n
			calculate values of B(0,n,PrResp1) = b(0,n,PrResp1) = (1-PrResp1)^n
		   *****************************************************/
			AB_array_PrResp0[0][n] = b_array_PrResp0[0][n] = (double)Math.pow((double)(1.0 - parent.PrResp0),
				(double)n);
			AB_array_PrResp1[0][n] = b_array_PrResp1[0][n] = (double)Math.pow((double)(1.0 - parent.PrResp1),
				(double)n);
			for(r=1; r <= n; r++)
			{
				b_array_PrResp0[r][n] = comp_cumu_binomial(r,n,parent.PrResp0,PrResp0_TYPE);
				b_array_PrResp1[r][n] = comp_cumu_binomial(r,n,parent.PrResp1,PrResp1_TYPE);

				AB_array_PrResp0[r][n] = AB_array_PrResp0[r-1][n] + b_array_PrResp0[r][n];
				AB_array_PrResp1[r][n] = AB_array_PrResp1[r-1][n] + b_array_PrResp1[r][n];
			}  /* end of loop over r */
		} /* end of loop over n */

		/* done with building arrays now process them */

		/* find n1_low from the array AB_array_PrResp1 */
		n1_low = 0;
		for ( n=1; n <= parent.nmax; n++)
		{
			if ( AB_array_PrResp1[0][n] <= parent.Beta)
			{
				n1_low = n;
				break;
			}
		} /* end of for n < nmax */

		/* intialize value of r_Old */
		r_Old = parent.nmax;

		/* load the R_max array with values */
		for(n=parent.nmax; n >= n1_low; --n)
		{
			for(r=r_Old; r >= 0; --r)
			{
				if (AB_array_PrResp1[r][n] <= parent.Beta)
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
		nmin = compute_nmin();

		/* do the final computations now */

		for (n1=n1_low; n1 <= parent.nmax-1; ++n1)   /* loop 6 */
		{
			if(R_max[n1] < 0)
				continue;

			for (n2=Math.max(1,nmin-n1); n2 <= parent.nmax-n1; ++n2)  /* loop 7 */
			{
				start_next_n2 = true;

			  /* loop 8 */
				for(r1=R_max[n1]; r1 >= 0 && start_next_n2; --r1)
				{
			 /* loop 9 */
					for(r2=R_max[n1+n2]; r2 >= 0 && start_next_n2; --r2)
					{
						if ((Fun_val = compute_FUN(r1,r2,n1,n2,PrResp0_TYPE)) <
							  (1-parent.Alpha))
							break;

						if((Fun_val = compute_FUN(r1,r2,n1,n2,PrResp1_TYPE)) > parent.Beta)
							continue;

						Val = (n1+n2) - (n2*AB_array_PrResp0[r1][n1]);

						if(Val < Val_Final)
						{
							Val_Final = Val;
							r1_Final = r1;
							r_Final = r2;
							n1_final = n1;
							n_final = n1+n2;
							start_next_n2 = false;
						} /* end of if Val < Val_Final */

					}  /* end of for r2 > 0 loop */
				} /* end of for r1 > 0 loop */
			}   /* end of n2 <= nmax-n1 loop */
		} /* end of n1 <= nmax-1 loop */

		/* compute PET_final and PET_max */
		PET_final = AB_array_PrResp0[r1_Final][n1_final];

	}

	/* function to compute nmin */
	int compute_nmin()
	{   int nmin;
	double z_Alpha,z_beta;

	z_Alpha = get_z_factor(parent.Alpha);
	z_beta  = get_z_factor(parent.Beta);

	nmin = (int)Math.pow (
	  (
	   z_Alpha * Math.pow(parent.PrResp0 *(1-parent.PrResp0),0.5)
	   +z_beta * Math.pow(parent.PrResp1*(1-parent.PrResp1),0.5)
	  ) / (parent.PrResp1 - parent.PrResp0),2.0   );
	return(nmin-5);
	} /* end of compute_nmin() */

	double get_z_factor(double probability)
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
		/* should not get here */
		return (2.0);
	} /* end of get_z_factor() */

	double compute_FUN(int r1,int r2,int n1,int n2,int array_to_use)
	{
		double FUN_value,sum,product;
		int x,limit;

		  /* compute limit for summation */
		FUN_value = (double)0.0;
		limit = Math.min(n1,r2);

		if(array_to_use == PrResp0_TYPE)
		{
			sum = (double)0.0;
			if(limit > 0)
			{
				for (x=r1+1; x <= limit; x++)
				{
					product = b_array_PrResp0[x][n1] * AB_array_PrResp0[r2-x][n2];
					sum += product;
				}
			}
			FUN_value = AB_array_PrResp0[r1][n1] + sum;
		}
		else if (array_to_use == PrResp1_TYPE)
		{
			sum = (double)0.0;
			for (x=r1+1; x <= limit; x++)
			{
				product = b_array_PrResp1[x][n1] * AB_array_PrResp1[r2-x][n2];
				sum += product;
			}
			FUN_value = AB_array_PrResp1[r1][n1] + sum;
		}
		return(FUN_value);
	} /* end of compute_FUN() */

	double comp_cumu_binomial(int r,int n,double p,int array_to_use)
	{
		double value1,value2;

		/* compute binomial probability */
		if ( p >= 1.0 )  {
			return (1.0);
		}
		else {
			value1 = p/(1-p);
			value2 = ((double)(n-r+1)/(double)r);

			if (array_to_use == PrResp0_TYPE)
				return(value1 * value2 * b_array_PrResp0[r-1][n]);
			else if (array_to_use == PrResp1_TYPE)
				return(value1 * value2 * b_array_PrResp1[r-1][n]);
		}
		/* should not get heare */
		Logger.log("ERROR- can't get here");
		return(0.0);
	} /* end of comp_cumu_binomial() */
}