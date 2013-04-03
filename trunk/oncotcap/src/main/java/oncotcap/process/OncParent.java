package oncotcap.process;

import java.util.*;

import oncotcap.sim.EventManager;
import oncotcap.sim.OncReporter;
import oncotcap.sim.random.OncRandom;
import oncotcap.sim.schedule.MasterScheduler;

public interface OncParent
{
	public long getSeed();
	public int getSerial(OncProcess o) throws oncotcap.util.ObjectNotFoundException;
	public void addChild(OncProcess o);
	public OncCollection getCollection(OncProcess o);
	public Collection getCollections();
	public Vector getCollections(Class type);
	public double getLocalTime();
	public OncParent getParent();
	public void setTimeKeptLocally(boolean localTime);
	public boolean isTimeKeptLocally();
	public boolean isAbove(OncProcess process);
	public boolean isAbove(OncCollection collection);
	public MasterScheduler getMasterScheduler();
	public EventManager getEventManager();
	public OncReporter getReporter();
	public OncReporter getCTReporter();
	public OncRandom getRNG();
	public boolean usesSingleRNG();
	public long getSimulationSeed();
}