package oncotcap.util;

import java.util.*;
import oncotcap.datalayer.persistible.StatementBundle;
public class StatementBundleCompareBySortKey implements Comparator 
{
	public int compare(Object obj1, Object obj2)
	{
		if(obj1 instanceof StatementBundle && obj2 instanceof StatementBundle)
		{
			int key1 = ((StatementBundle) obj1).getSortKey();
			int key2 = ((StatementBundle) obj2).getSortKey();
			return(key1 - key2);
		}
		else
			return(0);
	}

	public boolean equals(Object obj1, Object obj2)
	{
		if(obj1 instanceof StatementBundle &&
		   obj2 instanceof StatementBundle &&
		   (((StatementBundle) obj1).getSortKey() == 
		    ((StatementBundle) obj2).getSortKey()))
		    	return(true);
		else
			return(false);
	}
}

