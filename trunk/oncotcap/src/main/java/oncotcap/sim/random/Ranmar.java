package oncotcap.sim.random;

import oncotcap.util.*;

public class Ranmar {

	double[] u=new double[98];
	double cx,cd,cm;
	int i97, j97;
	int ij, kl;

	static int ijUpperBound=31328;
	static int klUpperBound=30081;
	private final static double maxInt = (double) Integer.MAX_VALUE;

	public static void main(String[] args) {
		/*double v;
		int i;
		try {
			Ranmar rMaster = new Ranmar(123,457);
			for (i = 0; i<1000000; i++)
			{
				v = rMaster.getValue();
				if( (i%10000) == 0)
					Logger.log(new Integer(i));
			}

		}
		catch(RanmarSeedException e){
			Logger.log("Bad rMaster" + e.errstring);
		}*/
		new Ranmar(2341234525234l);
	}
	public Ranmar()
	{
		try{setSeed(System.currentTimeMillis());}
		catch(RanmarSeedException e){System.err.println("Error setting RANMAR seed."); System.exit(1);}
	}
	
	public Ranmar(int ij,int kl) throws RanmarSeedException {
		init(ij,kl);
	}
	public Ranmar(long seed)
	{
		setSeed(seed);
	}
	
	public void setSeed(long seed)
	{
		int s1 = (int) ((((double)((int) abs(seed) & Integer.MAX_VALUE))/maxInt) * ijUpperBound);
		int s2 = (int) ((((double)((int) abs(seed) & Integer.MAX_VALUE))/maxInt) * klUpperBound);
		init(s1,s2);
	}
	private long abs(long val)
	{
		if(val == Long.MIN_VALUE)
			return(Long.MAX_VALUE);
		if(val < 0)
			return(-val);
		else
			return(val);
	}
	public void init(int ij,int kl) {
		int i, j, k, l, ii, jj, m;
		double  s, t;

		this.ij = ij;
		this.kl = kl;
		if (ij<0 || ij > ijUpperBound)
		{
			String errorMessage = "Error initializing Ranmar, bad first seed: "  + ij
					  + ". The first random number seed must have a value between 0 and " + ijUpperBound + ".";
			Logger.log(errorMessage);
			throw new RanmarSeedException(errorMessage);
		}
		if (kl<0 || kl> klUpperBound) {
			String errorMessage = "Error initializing Ranmar, bad second seed:" + kl
											  + ". The second random number seed must have a value between 0 and " + klUpperBound + ".";
			Logger.log(errorMessage);
			throw new RanmarSeedException(errorMessage);
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
			u[ii] = s;
		}
		cx = 362436.0 / 16777216.0;
		cd = 7654321.0 / 16777216.0;
		cm= 16777213.0 / 16777216.0;

		i97 = 97;
		j97 = 33;
	}

	private double  ranmarm0() {
		double  uni;

		uni = u[i97] - u[j97];
		if (uni < 0.0) uni += 1.0;
		u[i97] = uni;
		i97--;
		if (i97==0) i97 = 97;
		j97--;
		if (j97==0) j97 = 97;
		cx -= cd;
		if (cx<0.0) cx += cm;
		uni -= cx;
		if (uni<0.0) uni += 1.0;
		if ((uni==0.0)&& (j97>= 0))
			uni = u[j97]*Math.pow(2.0,-24.0);
		if (uni==0.0) uni = Math.pow(2.0,-48.0);
		return(uni);
	}

	public double nextDouble()
	{
		double u1,u2;
		final double LOWBOUND = 0.0001;
		final double UPBOUND  = 0.9999;

		u1=ranmarm0();
		if ( (u1>UPBOUND) || (u1<LOWBOUND)){
			u2=ranmarm0();
			if (u1<LOWBOUND){
				u1=LOWBOUND*u2;
			}
			else if ( u1>UPBOUND){
				u1=(1-LOWBOUND*u2);
			}
		}
		return(u1);
	}
}
