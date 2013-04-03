package oncotcap.sim.random;

public interface OncRandom
{
	public int nextInt();
	public long nextLong();
	public void setSeed(long seed);
	public double nextDouble();
	public double nextGaussian();
	public boolean nextBoolean();
}