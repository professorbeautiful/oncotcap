package oncotcap.sim.random;

import java.util.Random;

public class DefaultRNG extends Random implements OncRandom
{
	public DefaultRNG(long seed)
	{
		super(seed);
	}
}
