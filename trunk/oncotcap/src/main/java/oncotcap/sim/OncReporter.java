package oncotcap.sim;

import java.util.Observer;
import oncotcap.util.*;

public class OncReporter
{
	private OncReporterObservable obs = new OncReporterObservable();
	
	public void addObserver(Observer o)
	{
		obs.addObserver(o);
	}

/*	public static void notifyObservers(int eventType, String description)
	{
		OncEvent oe = new OncEvent(MasterScheduler.globalTime, eventType, description);
		obs.notifyObservers(oe);
	} */

	public void notifyObservers(int eventType, Object arg, double time)
	{
		OncEvent oe = new OncEvent(time, eventType, arg);
		obs.notifyObservers(oe);
	}
	
	public void notifyObservers(OncEvent e)
	{
		obs.notifyObservers(e);
	}

	public void deleteObserver(Observer o)
	{
		obs.deleteObserver(o);
	}
	public void clear()
	{
		obs.clear();
	}
}