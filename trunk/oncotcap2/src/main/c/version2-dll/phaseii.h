#define 	P1_TYPE	    	1
#define 	P2_TYPE	    	0
#define		INIT_VAL_FINAL	1000

#ifndef TRUE
#define		TRUE	    	1
#define		FALSE	    	0
#endif

/* array size definations */
#define 	MAX_SAMPLE_SIZE	    210
#define 	MAX_B_P1_SIZE	    MAX_SAMPLE_SIZE
#define 	MAX_B_P2_SIZE	    MAX_SAMPLE_SIZE
#define 	MAX_b_P1_SIZE	    MAX_SAMPLE_SIZE
#define 	MAX_b_P2_SIZE	    MAX_SAMPLE_SIZE


#define PASCAL __stdcall
#define EXPORT __declspec(dllexport)

double  compute_FUN();
double  comp_cumu_binomial();
double get_z_factor();
int compute_nmin();
extern int EXPORT PASCAL phaseii_simon(double alpha,double beta,double p1,double p2,int nmax);

/* data storage variables */
double AB_array_P1[MAX_B_P1_SIZE+1][MAX_B_P1_SIZE+1];
double AB_array_P2[MAX_B_P2_SIZE+1][MAX_B_P2_SIZE+1];
double b_array_P1[MAX_B_P1_SIZE+1][MAX_b_P1_SIZE+1];
double b_array_P2[MAX_B_P2_SIZE+1][MAX_b_P2_SIZE+1];

/* output variables */
double PET_final,Val_Final;
int  n1_final,n_final,r1_Final,r_Final;
