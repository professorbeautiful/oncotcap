package oncotcap.sim.random;

/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 */
 
import oncotcap.util.*;

public class Mrg32k3a {

	public static final	int START_STREAM = 0;
	public static final int START_SUBSTREAM = 1;
	public static final int NEXT_SUBSTREAM = 2;
	private static final double two63 = (double) 0x8000000000000000L - 1;
	private static final double two32 = (double) 0x100000000L - 1;
	private static final double two31 = (double) 0x80000000L - 1;
	private static final double norm   = 2.3283163396834613e-10;
	private static final double m1     = 4294949027.0;
	private static final double m1mult = m1/two32;
	private static final double m2     = 4294934327.0;
	private static final double a12    =  1154721.0;
	private static final double a14    =   1739991.0;
	private static final double a15n   =   1108499.0;
	private static final double a21    =   1776413.0;
	private static final double a23   =   865203.0;
	private static final double a25n   =  1641052.0;
	private static final double two17    =  131072.0;
	private static final double two53    =  9007199254740992.0;
	private static final double invtwo24 = 5.9604644775390625e-8;

   private static final double nextSeed[] = {12345,12345,12345,12345,12345,12345,12345,12345,12345,12345};
   // Default seed of the package and seed for the next stream to be created.

   private double Cg[] = new double[10];
   //Cg contains the current state
   
   private boolean anti;
   // This stream generates antithetic variates if and only if {\tt anti = 1}.

   private boolean prec53;
   // The precision of the output numbers is ``increased'' (see
   // {\tt increasedPrec}) if and only if {\tt prec53 = true}.

// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
// Private methods

   public static void main(String [] args)
   {
	   int n;
	   Mrg32k3a rng = new Mrg32k3a(9998);
	   for (n=1; n<=10; n++)
		   Logger.log(new Integer(rng.getInt(0,10)));
	   rng.setSeed(9998);
	   Logger.log("########################");
	   for (n=1; n<=10; n++)
		   Logger.log(new Integer(rng.getInt(0,100))); 
	   
   }
   private double U01 () {
        long   k;
        double p1, p2;
        /* Component 1 */
        p1 = a12 * Cg[3] - a15n * Cg[0];
		if (p1 > 0.) p1 -= a14 * m1;
        p1 += a14 * Cg[1];
		k = (long) (p1 / m1);
		p1 -= k * m1;
        if (p1 < 0.0) p1 += m1;
        Cg[0] = Cg[1];   Cg[1] = Cg[2];   Cg[2] = Cg[3]; Cg[3] = Cg[4]; Cg[4] = p1;
        /* Component 2 */
        p2 = a21 * Cg[4] - a25n * Cg[5];
		if (p2 > 0.0) p2 -= a23 * m2;
		p2 += a23 * Cg[7];
        k  = (long)(p2 / m2);
		p2 -= k * m2;
        if (p2 < 0.0) p2 += m2;
        Cg[5] = Cg[6];   Cg[6] = Cg[7];   Cg[7] = Cg[8]; Cg[8] = Cg[9]; Cg[9] = p2;
        /* Combination */
        return((p1 > p2) ? (p1 - p2) * norm : (p1 - p2 + m1) * norm);
   }

   private double U01d () {
        double u;
            u = U01() + (U01() - 1.0) * invtwo24;
            return (u < 0.0) ? u + 1.0 : u;
   }
 
   public Mrg32k3a()
   {
      anti = false;
      prec53 = true;
      for (int i = 0; i < 10; ++i)
		  Cg[i] = nextSeed[i];
   }

   public Mrg32k3a(long seed)
   {
	   this();
	   setSeed(seed);
   }

   public void setSeed(long seed)
   {
	   int arySeed [] = {0,0,12345,12345,12345,12345,12345,12345,12345,12345};
	   arySeed[0] = (int) (seed * m1mult);
	   arySeed[1] = (int) ((seed << 32) * m1mult);
	   setSeed(arySeed);
   }

   public void setSeed (int seed[])
   {
        int i;

        for (i = 0; i <= 4;  ++i) {
          if (seed[i] >= m1) {
             Logger.log("Error: Seed[%d] >= m1\n"+i);
            }
        }
        for (i = 5; i <= 9;  ++i) {
            if (seed[i] >= m2) {
               Logger.log("Error: Seed[%d] >= m2\n"+ i);
             }
        }
        for (i = 0; i <= 9;  ++i)
			Cg[i] = seed[i];
   }


   public double nextDouble()
   {
	   return(U01d());
   }
   public double getDouble()
   {
   	return(nextDouble());
   }
   public float nextFloat()
   {
	   return((float)U01());
   }
   
   public int nextInt()
   {
	   return( new Float((U01()* two31)).intValue());
   }

   public long nextLong()
   {
	   return((((long)nextInt()) << 32) + nextInt());	   
   }

   public int getInt (int i, int j)
   {
      return (i + (int)(U01() * (j - i + 1)));
   } 
} 

