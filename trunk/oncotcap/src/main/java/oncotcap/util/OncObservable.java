package oncotcap.util;

import oncotcap.sim.schedule.Schedulable;
import java.util.Observable;

public class OncObservable extends Observable implements Schedulable
{
	boolean mustBeReset = false;

	double offset;
	public void setScheduleOffset(double d) {
		offset = d;
	}
	public double getScheduleOffset() {
		return offset;
	}

	public OncObservable () {
		this(false);
	}

	public OncObservable (boolean mustBeReset) {
		this.mustBeReset = mustBeReset;
		//To get the usual behavior of Observable,
		// construct with OncObservable(true);
	}
	public void setMustBeReset(boolean b) {
		mustBeReset = b;
	}
	public void notifyObservers(Object arg)
	{
		if (! mustBeReset)
			super.setChanged();
		super.notifyObservers(arg);
	}
	public void clear()
	{
		mustBeReset = false;
		super.deleteObservers();
		super.clearChanged();
	}

}