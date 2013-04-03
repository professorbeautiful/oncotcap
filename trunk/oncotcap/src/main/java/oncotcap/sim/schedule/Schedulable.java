/**
 *	Interface that, when implimented, allows an object to use the
 *	MasterScheduler to schedule triggers of the objects methods.
 */
package oncotcap.sim.schedule;

public interface Schedulable
{
	/**
	 *  Method returns the schedule offset that the object is
	 *  assigned.  The schedule offset is set to the current global
	 *  time when the object first schedules a trigger event.
	 */
	public double getScheduleOffset();

	/**
	 *	Method to set the schedule offset for this object.  This method
	 *	is provide for use by the MasterScheduler only.  This method
	 *	should never be used to change the schedule offset directly.
	 *	The schedule offset is set to the current global time when the
	 *	object first schedules a trigger event.
	 */
	public void setScheduleOffset(double offset);

	/**
	 *	Default method that is triggered by the MasterScheduler if no
	 *	other method name is provided at trigger installation time.
	 */
	//public void update();
}