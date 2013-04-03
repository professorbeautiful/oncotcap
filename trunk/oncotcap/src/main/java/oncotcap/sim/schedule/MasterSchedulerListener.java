package oncotcap.sim.schedule;

public interface MasterSchedulerListener
{
	public void schedulerStarted();
	public void schedulerPaused();
	public void schedulerReset();
}
