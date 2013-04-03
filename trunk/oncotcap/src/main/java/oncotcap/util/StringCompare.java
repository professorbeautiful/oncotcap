package oncotcap.util;

import java.util.*;

class StringCompare implements Comparator
{
	public int compare(Object obj1, Object obj2)
	{
	       return(((String) obj1).compareTo((String)obj2));
	}

	public boolean equals(Object obj1, Object obj2)
	{
		return( ((String) obj1).equalsIgnoreCase((String) obj2) );
	}
}
