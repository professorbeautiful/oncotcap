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

    if(pp != psave)
	{
		psave = pp;
		p = min(psave,1.0-psave);
		q = 1.0-p;
	}

	if(n != nsave)
	{
		xnp = n*p;
		nsave = n;
		if(xnp < 30.0)
		{
			qn = pow(q,n);
			r = p/q;
			g = r*(n+1);
		}
		else
		{

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
		}
	}
    if(xnp < 30.0)
	{
S150:
		ix = 0;
		f = qn;
		u = (*RandFunc)(CNRAND);
		while !(u < f)
		{
			if(ix > 110) goto S150;  /* change goto to continue for java */
			u -= f;
			ix += 1;
			f *= (g/ix-r);
		}
		return (double)  (psave > 0.5) ? (long)(n-ix) : ix;
	}

	

S30:
/*
*****GENERATE VARIATE
*/
	while(1)
	{
		u = (*RandFunc)(CNRAND)*p4;
		v = (*RandFunc)(CNRAND);
	/*
		 TRIANGULAR REGION
	*/
		if !(u > p1)
		{
			ix = (long)(xm-p1*v+u);
			return (double)  (psave > 0.5) ? (long)(n-ix) : ix;
		}

	/*
		 PARALLELOGRAM REGION
	*/
		if!(u > p2)
		{
			x = xl+(u-p1)/c;
			v = v*c+1.0-ABS(xm-x)/p1;
			if(v > 1.0 || v <= 0.0) goto S30;
			ix = (long)x;
		}

	/*
		 LEFT TAIL
	*/
		else if!(u > p3)
		{
			ix = (long)(xl+log(v)/xll);

			/* This line added to clear Floating point status register
			   when v = 0 */
		#ifdef DLL
			wfpStat = _clearfp();
		#endif

			if(ix < 0) goto S30;
			v *= ((u-p2)*xll);
			return (double)  (psave > 0.5) ? (long)(n-ix) : ix;
		}
	/*
		 RIGHT TAIL
	*/
		else
		{
			ix = (long)(xr-log(v)/xlr);

			/* This line added to clear Floating point status register
			   when v = 0 */
	#ifdef DLL
			wfpStat = _clearfp();
	#endif

			if(ix > n) goto S30;
			v *= ((u-p3)*xlr);
		}
	/*
	*****DETERMINE APPROPRIATE WAY TO PERFORM ACCEPT/REJECT TEST
	*/
		k = ABS(ix-m);
		if!(k > 20 && k < xnpq/2-1)
		{
		/*
			 EXPLICIT EVALUATION
		*/
			f = 1.0;
			r = p/q;
			g = (n+1)*r;
			T1 = m-ix;
			if!(T1 < 0)
			{
				mp = m+1;
				for(i=mp; i<=ix; i++) f *= (g/i-r);
			}
			else if!(T1 == 0)
			{
				if(v <= f) 	return (double)  (psave > 0.5) ? (long)(n-ix) : ix;
				goto S30;
			}
			else
			{
				ix1 = ix+1;
				for(i=ix1; i<=m; i++) f /= (g/i-r);
			}

			if(v <= f) 	return (double)  (psave > 0.5) ? (long)(n-ix) : ix;
			goto S30;
		}
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

		if(alv < ynorm-amaxp) 	return (double)  (psave > 0.5) ? (long)(n-ix) : ix;
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
		  -140.0/w2)/w2)/w2)/w2)/w)/166320.0) 	return (double)  (psave > 0.5) ? (long)(n-ix) : ix;

	}
	
	return (double)  (psave > 0.5) ? (long)(n-ix) : ix;
}

