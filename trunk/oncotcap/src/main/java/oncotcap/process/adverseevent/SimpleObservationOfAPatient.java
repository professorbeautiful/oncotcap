package oncotcap.process.adverseevent;

import oncotcap.sim.schedule.Schedulable;

public class SimpleObservationOfAPatient implements ObservationOfAPatient, Schedulable
{
	private AETarget target;
	private double scheduleOffset;

	
	public SimpleObservationOfAPatient(AETarget target)
	{
		this.target = target;
	}

	public Object getObservation()
	{
		return(new Double(target.getFunctionStatus()));
	}

	public void update()
	{
		
	}
	public double getScheduleOffset()
	{
		return(this.scheduleOffset);
	}
	public void setScheduleOffset(double offset)
	{
		this.scheduleOffset = offset;
	}
}