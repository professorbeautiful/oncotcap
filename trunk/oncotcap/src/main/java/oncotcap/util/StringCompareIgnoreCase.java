package oncotcap.util;

import java.util.*;

public class StringCompareIgnoreCase implements Comparator
{
	public int compare(Object obj1, Object obj2)
	{
		   return(obj1.toString().toUpperCase().compareTo(obj2.toString().toUpperCase()));
	}

	public boolean equals(Object obj1, Object obj2)
	{
		return(obj1.toString().equalsIgnoreCase(obj2.toString()));
	}
}