#include "build.h"

#ifdef DLL
#include <windows.h>
#include <crtdbg.h> 
#endif
#include "grand.h"
#include "Const.h"
#ifdef REALUNIX
#define max(A,B)  ( ( (A)<(B) ) ? B : A )
#define min(A,B)  ( ( (A)>(B) ) ? B : A )
#endif
#define ABS(A)    ( (A) >= 0.0  ? (A) : -(A) )

extern double poidev();

double rand01(int dummy) 
{
	double temp;
	temp = rand()*INV_RAND_MAX;
	return temp;
}

void ranrmarin(ij,kl,whichrand)
int  ij,kl,whichrand;
{
		void exit();
		void eprint();
		int i, j, k, l, ii, jj, m;
        double  s, t;
    
		test[whichrand] = FALSE;

        if (ij<0 || ij>31328 || kl<0 || kl>30081) {
#ifdef DLL
				MessageBox(NULL,"The first random number seed must have a value between 0 and 31328","treat.dll",MB_ICONEXCLAMATION | MB_OK);
#else 
                eprint("The first random number seed must have a value between 0 \
and 31328.");
                eprint("The second seed must have a value between 0 and 30081.");
#endif
				exit(1);
        }
        i = (ij/177)%177 + 2;
        j = ij%177 + 2;
        k = (kl/169)%178 + 1;
        l = kl%169;
        for (ii=1; ii<=97; ii++) {
                s = 0.0;
                t = 0.5;
                for (jj=1; jj<=24; jj++) {
                        m = (((i*j)%179)*k) % 179;
                        i = j;
                        j = k;
                        k = m;
                        l = (53*l + 1) % 169;
                        if ((l*m)%64 >= 32) s += t;
                        t *= 0.5;
                }
                u[ii][whichrand] = s;
        }

        cx[whichrand] = 362436.0 / 16777216.0;
        cd[whichrand] = 7654321.0 / 16777216.0;
        cm[whichrand]= 16777213.0 / 16777216.0;

        i97[whichrand] = 97;
        j97[whichrand] = 33;

        test[whichrand] = TRUE;

}


double  ranmarm0(int whichrand)
{
        double  uni;
        if (test==FALSE) {
                eprint("Call the init routine rmarin() before calling ranmar().");
                exit(2);
        }
                uni = u[i97[whichrand]][whichrand] - u[j97[whichrand]][whichrand];
                if (uni < 0.0) uni += 1.0;
                u[i97[whichrand]][whichrand] = uni;
                i97[whichrand]--;
                if (i97[whichrand]==0) i97[whichrand] = 97;
                j97[whichrand]--;
                if (j97[whichrand]==0) j97[whichrand] = 97;
                cx[whichrand] -= cd[whichrand];
                if (cx[whichrand]<0.0) cx[whichrand] += cm[whichrand];
                uni -= cx[whichrand];
                if (uni<0.0) uni += 1.0;
                if ((uni==0.0)&& (j97[whichrand]>= 0)) uni = u[j97[whichrand]][whichrand]*pow(2.0,-24.0);
                if (uni==0.0) uni = pow(2.0,-48.0);
                return(uni);
}

double ranmarm(int whichrand)
{
	double u1,u2;
#define LOWBOUND 0.0001
#define UPBOUND  0.9999

	u1=ranmarm0(whichrand);
	if ( (u1>UPBOUND) || (u1<LOWBOUND)){
		u2=ranmarm0(whichrand);
		if (u1<LOWBOUND){
			u1=LOWBOUND*u2;
		}
		else if ( u1>UPBOUND){
				u1=(1-LOWBOUND*u2);
		}
	}

	return u1;
}
double gbinomi(n, pp)
double n, pp;
{
#include <float.h>

static double  psave = -1.0;
static double nsave = -1;
static long ignbin,i,ix,ix1,k,m,mp,T1;
static double al,alv,amaxp,c,f,f1,f2,ffm,fm,g,p,p1,p2,p3,p4,q,qn,r,u,v,w,w2,x,x1,
    x2,xl,xll,xlr,xm,xnp,xnpq,xr,ynorm,z,z2;
unsigned int wfpStat;

    if(pp != psave) goto S10;
    if(n != nsave) goto S20;
    if(xnp < 30.0) goto S150;
    goto S30;
S10:
/*
*****SETUP, PERFORM ONLY WHEN PARAMETERS CHANGE
*/
    psave = pp;
    p = min(psave,1.0-psave);
    q = 1.0-p;
S20:
    xnp = n*p;
    nsave = n;
    if(xnp < 30.0) goto S140;
    ffm = xnp+p;
    m = (long)ffm;
    fm = m;
    xnpq = xnp*q;
    p1 = 2.195*sqrt(xnpq)-4.6*q+0.5;
    xm = fm+0.5;
    xl = xm-p1;
    xr = xm+p1;
    c = 0.134+20.5/(15.3+fm);
    al = (ffm-xl)/(ffm-xl*p);
    xll = al*(1.0+0.5*al);
    al = (xr-ffm)/(xr*q);
    xlr = al*(1.0+0.5*al);
    p2 = p1*(1.0+c+c);
    p3 = p2+c/xll;
    p4 = p3+c/xlr;
S30:
/*
*****GENERATE VARIATE
*/
    u = (*RandFunc)(CNRAND)*p4;
    v = (*RandFunc)(CNRAND);
/*
     TRIANGULAR REGION
*/
    if(u > p1) goto S40;
    ix = (long)(xm-p1*v+u);
    goto S170;
S40:
/*
     PARALLELOGRAM REGION
*/
    if(u > p2) goto S50;
    x = xl+(u-p1)/c;
    v = v*c+1.0-ABS(xm-x)/p1;
    if(v > 1.0 || v <= 0.0) goto S30;
    ix = (long)x;
    goto S70;

S50:
/*
     LEFT TAIL
*/
    if(u > p3) goto S60;
    ix = (long)(xl+log(v)/xll);

	/* This line added to clear Floating point status register
	   when v = 0 */
#ifdef DLL
    wfpStat = _clearfp();
#endif

    if(ix < 0) goto S30;
    v *= ((u-p2)*xll);
    goto S70;
S60:
/*
     RIGHT TAIL
*/
    ix = (long)(xr-log(v)/xlr);
	
	/* This line added to clear Floating point status register
	   when v = 0 */
#ifdef DLL
    wfpStat = _clearfp();
#endif

	if(ix > n) goto S30;
    v *= ((u-p3)*xlr);
S70:
/*
*****DETERMINE APPROPRIATE WAY TO PERFORM ACCEPT/REJECT TEST
*/
    k = ABS(ix-m);
    if(k > 20 && k < xnpq/2-1) goto S130;
/*
     EXPLICIT EVALUATION
*/
    f = 1.0;
    r = p/q;
    g = (n+1)*r;
    T1 = m-ix;
    if(T1 < 0) goto S80;
    else if(T1 == 0) goto S120;
    else  goto S100;
S80:
    mp = m+1;
    for(i=mp; i<=ix; i++) f *= (g/i-r);
    goto S120;
S100:
    ix1 = ix+1;
    for(i=ix1; i<=m; i++) f /= (g/i-r);
S120:
    if(v <= f) goto S170;
    goto S30;

S130:
/*
     SQUEEZING USING UPPER AND LOWER BOUNDS ON ALOG(F(X))
*/
    amaxp = k/xnpq*((k*(k/3.0+0.625)+0.1666666666666)/xnpq+0.5);
    ynorm = -(k*k/(2.0*xnpq));
    alv = log(v);

	/* This line added to clear Floating point status register
	   when v = 0 */
#ifdef DLL
    wfpStat = _clearfp();
#endif

    if(alv < ynorm-amaxp) goto S170;
    if(alv > ynorm+amaxp) goto S30;
/*
     STIRLING'S FORMULA TO MACHINE ACCURACY FOR
     THE FINAL ACCEPTANCE/REJECTION TEST
*/
    x1 = ix+1.0;
    f1 = fm+1.0;
    z = n+1.0-fm;
    w = n-ix+1.0;
    z2 = z*z;
    x2 = x1*x1;
    f2 = f1*f1;
    w2 = w*w;
    if(alv <= xm*log(f1/x1)+(n-m+0.5)*log(z/w)+(ix-m)*log(w*p/(x1*q))+((13860.0-
      (462.0-(132.0-(99.0-140.0/f2)/f2)/f2)/f2)/f1+(13860.0-(462.0-
      (132.0-(99.0-140.0/z2)/z2)/z2)/z2)/z+(13860.0-(462.0-(132.0-
      (99.0-140.0/x2)/x2)/x2)/x2)/x1+(13860.0-(462.0-(132.0-(99.0
      -140.0/w2)/w2)/w2)/w2)/w)/166320.0) goto S170;
    goto S30;
S140:
/*
     INVERSE CDF LOGIC FOR MEAN LESS THAN 30
*/
    qn = pow(q,n);
    r = p/q;
    g = r*(n+1);
S150:
    ix = 0;
    f = qn;
    u = (*RandFunc)(CNRAND);
S160:
    if(u < f) goto S170;
    if(ix > 110) goto S150;
    u -= f;
    ix += 1;
    f *= (g/ix-r);
    goto S160;
S170:
    if(psave > 0.5) ix = (long)(n-ix);
    ignbin = ix;
    return (double)(ignbin);
}

double gnorm0(int flag)
{
    static int iset=0;
    static double gset;
    double fac, rsq, v1, v2;     

	if (flag == true)
	{
		iset = 0;
		gset = 0.0;
		return(0);
	}

    if ( iset == 0) {
        do {
         v1=2.0*(*RandFunc)(CNRAND)-1.0;
         v2=2.0*(*RandFunc)(CNRAND)-1.0;
         rsq=v1*v1+v2*v2;
       } while (rsq >=1.0 || rsq == 0.0);
    fac=sqrt(-2.0*log(rsq)/rsq);
    gset=v1*fac;
    iset=1;
    return v2*fac;
      } else {
       iset=0;
       return gset;
     }
  }


double gnorm( uu, xigama )
    double uu, xigama;
{  
    double temp;
    temp = floor( gnorm0(FALSE)*sqrt(xigama) + uu );
    return temp;
}

/* added by QS, 2/3/99 for small prob with benoni distribution */
double sqrtp_uniform(pp)
double pp;
{
	double sqrtp,u1,u2;

	sqrtp = sqrt(pp);
	u1 = (*RandFunc)(CNRAND);
	u2 = (*RandFunc)(CNRAND);
	if ( ( u1 < sqrtp ) && ( u2 < sqrtp))
		return ( 1.0 );
	else return ( 0.0 );

}

double grand_b_n( nn , pp)
	double pp ,nn;
{
	extern int IsMeanOnly;
    double uu, xigama, temp ;
    uu= nn*pp;

	if ( IsMeanOnly == USEMEANONLY ) {
		return uu;
	}

    if ( nn < BINOM_MAX ){
		/* added by QS, 2/3/99 for very small prob */
		if ( pp == 0.0 )
			temp = 0.0;
		else if (( pp <= 1.0e-5 ) && ( nn == 1 ))
			temp = sqrtp_uniform(pp);
		else 
            temp = gbinomi( nn, pp );
	}
	else {
		if ( uu < CUTOFF ) {
			temp=poidev(uu);
		}
		 else
		 {
			 if (pp > (1.0 - 1e-12))
				 xigama = 0.0;
			 else
				 xigama = uu * ( 1.0 - pp);

			temp  = gnorm( uu, xigama );
			if ( temp > nn ) temp  = min ( nn, temp );
			else if ( temp < 0.0 ) temp  = max( temp, 0.0 );
		 }
	}
   return temp;
}
