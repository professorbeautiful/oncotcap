package oncotcap.process;

import oncotcap.sim.EventManager;
import oncotcap.sim.OncReporter;
import oncotcap.sim.random.OncRandom;
import oncotcap.sim.schedule.MasterScheduler;
import oncotcap.util.*;

public class TopOncParent extends AbstractOncParent implements OncParent
{
	MasterScheduler masterScheduler = new MasterScheduler();
	EventManager eventManager = new EventManager(masterScheduler);
	OncReporter reporter = new OncReporter();
	OncReporter cTReporter = new OncReporter();
	private OncRandom rng = null;
	private boolean useSingleRNG = true;
	private Long simSeed = null;
	
	//because this is a "TopOncParent" always return a serial of one.
	public int getSerial(OncProcess o)
	{
		return(1);
	}
	//again, because this is a "TopOncParent" always return the global time
	public double getLocalTime()
	{
		return(getMasterScheduler().globalTime);
	}
	public boolean isTimeKeptLocally()
	{
		return(true);
	}
	public void setTimeKeptLocally(boolean localTime)
	{
		if(! localTime)
			throw(new ImmutableError("ERROR: Trying to set local time option to false.\nTopOncParent must keep time locally"));
	}
	public MasterScheduler getMasterScheduler()
	{
		return(masterScheduler);
	}
	public EventManager getEventManager()
	{
		return(eventManager);
	}
	public OncReporter getReporter()
	{
		return(reporter);
	}
	public OncReporter getCTReporter()
	{
		return(cTReporter);
	}

	public OncRandom getRNG()
	{
		if(rng == null)
			rng = newRNG(getSimulationSeed());
		return(rng);
	}
	
	public boolean usesSingleRNG()
	{
		return(useSingleRNG);
	}
	
	public void setSingleRNG(boolean useSingleRng)
	{
		this.useSingleRNG = useSingleRng;
	}
	
	public void setSimulationSeed(long seed)
	{
		simSeed = new Long(seed);
	}
	public long getSimulationSeed()
	{
		if(simSeed == null)
			simSeed = new Long(new java.util.Random().nextLong());
		return(simSeed.longValue());
	}
	public long getSeed()
	{
		return(getSimulationSeed());
	}
}