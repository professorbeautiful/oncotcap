package oncotcap.sim.schedule;

abstract public class AbstractSchedulable implements Schedulable {
	private double scheduleOffset = -1;
	public double getScheduleOffset()
	{
		return(this.scheduleOffset);
	}
	public void setScheduleOffset(double offset)
	{
		this.scheduleOffset = offset;
	}
	//abstract public void update();
}
