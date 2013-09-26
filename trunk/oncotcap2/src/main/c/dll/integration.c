/* All codes in this file are from <<Numerical Recipes in C>> */
#include <math.h>
#include <stdlib.h>
#include <stdio.h>

#define EPS 1.0e-6
#define JMAX 20
#define JMAXP (JMAX+1)
#define K 5
#define NRANSI
#define NR_END 1
#define FREE_ARG char*

extern FILE *eout;
extern double kern();
double *vector(long nl, long nh);
void free_vector(double *v, long nl, long nh);

double *vector(long nl, long nh)
/* allocate a double vector with subscript range v[nl..nh] */
{
	double *v;

	v=(double *)malloc((size_t) ((nh-nl+1+NR_END)*sizeof(double)));
	if (!v) fprintf(eout,"in [vector]:allocation failure in vector()");
	return v-nl+NR_END;
}

void free_vector(double *v, long nl, long nh)
/* free a double vector allocated with vector() */
{
	free((FREE_ARG) (v+nl-NR_END));
}

void polint(double xa[], double ya[], int n, double x, double *y, double *dy)
{
	int i,m,ns=1;
	double den,dif,dift,ho,hp,w;
	double *c,*d;

	dif=fabs(x-xa[1]);
	c=vector(1,n);
	d=vector(1,n);
	for (i=1;i<=n;i++) {
		if ( (dift=fabs(x-xa[i])) < dif) {
			ns=i;
			dif=dift;
		}
		c[i]=ya[i];
		d[i]=ya[i];
	}
	*y=ya[ns--];
	for (m=1;m<n;m++) {
		for (i=1;i<=n-m;i++) {
			ho=xa[i]-x;
			hp=xa[i+m]-x;
			w=c[i+1]-d[i];
			if ( (den=ho-hp) == 0.0) fprintf(eout,"in [polint]:Error in routine polint");
			den=w/den;
			d[i]=hp*den;
			c[i]=ho*den;
		}
		*y += (*dy=(2*ns < (n-m) ? c[ns+1] : d[ns--]));
	}
	free_vector(d,1,n);
	free_vector(c,1,n);
}
#undef NRANSI


double trapzd(double a, double b, int n,double g,double psit)
{
	double x,tnm,sum,del;
	static double s;
	int it,j;

	if (n == 1) {
		return (s=0.5*(b-a)*(kern(a,g,psit)+kern(b,g,psit)));
	} else {
		for (it=1,j=1;j<n-1;j++) it <<= 1;
		tnm=it;
		del=(b-a)/tnm;
		x=a+0.5*del;
		for (sum=0.0,j=1;j<=it;j++,x+=del) sum += kern(x,g,psit);
		s=0.5*(s+(b-a)*sum/tnm);
		return s;
	}
}

/* this program is from <<Numerical Recipes in C>>,	p140
   qromb() ===> pgfint()
   This pgfint() will replace old pgfint(), see pgfint.c ( 2/5/99 )
*/
double pgfint(double a, double b,double g,double psit)
{
	void polint(double xa[], double ya[], int n, double x, double *y, double *dy);
	double trapzd(double a, double b, int n,double g,double psit);
	double ss,dss;
	double s[JMAXP+1],h[JMAXP+1];
	int j;

	h[1]=1.0;
	for (j=1;j<=JMAX;j++) {
		s[j]=trapzd(a,b,j,g,psit);
		if (j >= K) {
			polint(&h[j-K],&s[j-K],K,0.0,&ss,&dss);
			if (fabs(dss) < EPS*fabs(ss)) 
				return ss;
		}
		s[j+1]=s[j];
		h[j+1]=0.25*h[j];
	}
	fprintf(eout,"in [pgfint]:Too many steps in routine qromb\n");
	return 0.0;
}
#undef EPS
#undef JMAX
#undef JMAXP
#undef K
/* (C) Copr. 1986-92 Numerical Recipes Software *)%. */
