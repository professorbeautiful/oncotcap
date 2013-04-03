package oncotcap.sim.schedule;

import java.util.*;

class TriggerCompare implements Comparator
{
	public int compare(Object obj1, Object obj2)
	{
		if (((TriggerEntry) obj1).triggerTime < ((TriggerEntry) obj2).triggerTime)
			return(-1);
		else if (((TriggerEntry) obj1).triggerTime > ((TriggerEntry) obj2).triggerTime)
			return(1);
		else
			return(0);
	}

	public boolean equals(Object obj1, Object obj2)
	{
		return( ((TriggerEntry) obj1).triggerTime == ((TriggerEntry) obj2).triggerTime );
	}
}