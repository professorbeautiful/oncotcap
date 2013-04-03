package oncotcap.util;

import java.util.*;

public class EventTimeCompare implements Comparator
{
	public int compare(Object obj1, Object obj2)
	{
		if (((OncEvent) obj1).getTime() > ((OncEvent) obj2).getTime())
			return(1);
		else if (((OncEvent) obj1).getTime() < ((OncEvent) obj2).getTime())
			return(-1);
		else
			return(0);
	}

	public boolean equals(Object obj1, Object obj2)
	{
		return( ((OncEvent) obj1).getTime() == ((OncEvent) obj2).getTime() );
	}
}