package oncotcap.sim.random;
import java.util.*;
import java.lang.reflect.Array;
import oncotcap.util.*;

public class Gbinomi //extends RNGfamily
{

	static double psave = -1.0;
	static double nsave = -1;
	static long ignbin,i,ix,ix1,k,m,mp,T1;
	static double al,alv,amaxp,c,f,f1,f2,ffm,fm,g,p,p1,p2,p3,p4,q,qn,r,u,v,w,w2,x,x1,
	x2,xl,xll,xlr,xm,xnp,xnpq,xr,ynorm,z,z2;

	/*	unsigned int wfpStat; */

/*	public static void main(String[] args){
		int nreps = 1;
		Ranmar ranmar=null;
		if ( Arrays.asList(args).size() < 2)
			Logger.log("Usage:   Gbinomi n pp   -or-  Gbinomi n pp nreps");
		else
		{
			RanStream ranStream = new RanStream();
			try{
				ranmar = new Ranmar(123,456);
			}catch(RanmarSeedException e) {}
			if ( Arrays.asList(args).size() ==3)
				nreps = new Integer(args[2]).intValue();
			for(int i=0; i<nreps; i++)
				Logger.log(get(new Double(args[0]).doubleValue(),new Double(args[1]).doubleValue(),ranmar));
		}
	}
*/
	/**
	 * get a multinomial distribution of n items given an array (pp) of probabilities. 
	 */
	public static double [] getMulti(double n, double [] pp, OncRandom random)
	{
		int i;
		double runningCount = n;
		double pSum = 0;
		double [] rVal = (double []) Array.newInstance(double.class, pp.length);
		
		//TODO: Sort the probabilities highest to lowest to minimize the 
		//      number of calls to gbinomi.  the old index of each probability
		//      will need to be kept to place the values on the return array
		//      in the correct position.
		
		//sum the probabilities and initialize the return array
		for(i = 0; i < pp.length; i++)
		{
			pSum += pp[i];
			rVal[i] = 0.0;
		}
		
		if(pSum > 1 + (MathHelper.INFINITESIMAL * pp.length) || pSum < 1 - (MathHelper.INFINITESIMAL * pp.length))
			Logger.log("WARNING: Multinomial called without total probability of one. [Gbinomi.getMulti]");
		
		//loop through all but the last probability, call gbinomi
		for(i = 0; (i < (pp.length - 1) && runningCount > 0); i++)
		{
			rVal[i] = get(runningCount, pp[i]/pSum, random);
			runningCount -= rVal[i];
			pSum -= pp[i];
		}
		
		//if there is anything left assign it to the last value in the distribution
		if(runningCount > 0)
			rVal[pp.length - 1] = runningCount;
		
		return(rVal);
	}
/*	public static double get(double n, double pp, RanStream ranStream){
		// We should try/catch a "ranStream not initialized" error.
		return(get(n,pp,ranStream.getRanmar()));
	}*/
	public static double get(double n, double pp, OncRandom random){

		if (! oncotcap.Oncotcap.getGbinomiOn())
			return(n * pp);

		/******SETUP, PERFORM ONLY WHEN PARAMETERS CHANGE*/
		//if(pp != psave)
		{
			psave = pp;
			p = Math.min(psave,1.0-psave);
			q = 1.0-p;
		}
		//if(n != nsave || pp != psave)
		{
			xnp = n*p;
			nsave = n;

		/*
			 INVERSE CDF LOGIC FOR MEAN LESS THAN 30
		*/
			if(xnp < 30.0)
			{
				qn = Math.pow(q,n);
				r = p/q;
				g = r*(n+1);
			}
			else
			{
				ffm = xnp+p;
				m = (long)ffm;
				fm = m;
				xnpq = xnp*q;
				p1 = 2.195 * Math.sqrt(xnpq)-4.6*q+0.5;
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
			ix = 0;
			f = qn;
			u = random.nextDouble();
			while(true)
			{
				if(u < f) return (double) ( (psave > 0.5) ? (long) (n-ix) : ix );
				if(ix > 110)
				{
					ix = 0;
					f = qn;
					u = random.nextDouble();
				}
				else
				{
					u -= f;
					ix += 1;
					f *= (g/ix-r);
				}
			}
		}




S30:	while(true)
		{
/*
*****GENERATE VARIATE
*/
			u = random.nextDouble()*p4;
			v = random.nextDouble();
/*
     TRIANGULAR REGION
*/
			if(!(u > p1))
			{
				ix = (long)(xm-p1*v+u);
				return (double) ( (psave > 0.5) ? (long) (n-ix) : ix );
			}
/*
     PARALLELOGRAM REGION
*/
			if(!(u > p2))
			{
				x = xl+(u-p1)/c;
				v = v*c+1.0-Math.abs(xm-x)/p1;
				if(v > 1.0 || v <= 0.0) continue S30;
				ix = (long)x;
			}
/*
     LEFT TAIL
*/
			else if(!(u > p3))
			{
				ix = (long)(xl+Math.log(v)/xll);

		/* This line added to clear Floating point status register
		   when v = 0 */
/* #ifdef DLL
				wfpStat = _clearfp();
#endif */

				if(ix < 0) continue S30;
				v *= ((u-p2)*xll);
			}
/*
     RIGHT TAIL
*/
			else
			{
				ix = (long)(xr-Math.log(v)/xlr);

		/* This line added to clear Floating point status register
		   when v = 0 */
/* #ifdef DLL
				wfpStat = _clearfp();
#endif */

				if(ix > n) continue S30;
				v *= ((u-p3)*xlr);
			}
/*
*****DETERMINE APPROPRIATE WAY TO PERFORM ACCEPT/REJECT TEST
*/
			k = Math.abs(ix-m);
	/*
		 SQUEEZING USING UPPER AND LOWER BOUNDS ON ALOG(F(X))
	*/
			if(k > 20 && k < xnpq/2-1)
			{
				amaxp = k/xnpq*((k*(k/3.0+0.625)+0.1666666666666)/xnpq+0.5);
				ynorm = -(k*k/(2.0*xnpq));
				alv = Math.log(v);

	/* This line added to clear Floating point status register
	   when v = 0 */
/* #ifdef DLL
				wfpStat = _clearfp();
#endif */

				if(alv < ynorm-amaxp) return (double) ( (psave > 0.5) ? (long) (n-ix) : ix );
				if(alv > ynorm+amaxp) continue S30;
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
				if(alv <= xm*Math.log(f1/x1)+(n-m+0.5)*Math.log(z/w)+(ix-m)*Math.log(w*p/(x1*q))+((13860.0-
					(462.0-(132.0-(99.0-140.0/f2)/f2)/f2)/f2)/f1+(13860.0-(462.0-
					(132.0-(99.0-140.0/z2)/z2)/z2)/z2)/z+(13860.0-(462.0-(132.0-
					(99.0-140.0/x2)/x2)/x2)/x2)/x1+(13860.0-(462.0-(132.0-(99.0
					-140.0/w2)/w2)/w2)/w2)/w)/166320.0) return (double) ( (psave > 0.5) ? (long) (n-ix) : ix );
			}
	/*
		 EXPLICIT EVALUATION
	*/
			else
			{
				f = 1.0;
				r = p/q;
				g = (n+1)*r;
				T1 = m-ix;
				if(T1 < 0)
				{
					mp = m+1;
					for(i=mp; i<=ix; i++) f *= (g/i-r);
				}
				else if(!(T1 == 0))
				{
					ix1 = ix+1;
					for(i=ix1; i<=m; i++) f /= (g/i-r);
				}
				if(v <= f) 	return (double) ( (psave > 0.5) ? (long) (n-ix) : ix );
			}
		}

/*		return (double) ( (psave > 0.5) ? (long) (n-ix) : ix ); */
	}
}
