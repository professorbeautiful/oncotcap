package oncotcap.util;

import java.util.*;

public class IntegerCompare implements Comparator
{
	public int compare(Object obj1, Object obj2)
	{
	       return(((Integer) obj1).compareTo((Integer)obj2));
	}

	public boolean equals(Object obj1, Object obj2)
	{
		return(((Integer) obj1).equals(obj2) );
	}
}
