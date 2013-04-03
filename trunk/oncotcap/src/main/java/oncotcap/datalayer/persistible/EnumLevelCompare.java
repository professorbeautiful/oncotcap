package oncotcap.datalayer.persistible;

import java.util.*;

class EnumLevelCompare implements Comparator
{
    EnumLevelCompare(){}
	public int compare(Object obj1, Object obj2)
	{
		//return(((EnumLevel) obj1).getName().compareTo(((EnumLevel) obj2).getName()));
		 return((int)(((EnumLevel) obj1).getListIndex() - ((EnumLevel) obj2).getListIndex()));
	}

	public boolean equals(Object obj1, Object obj2)
	{
		return(((EnumLevel) obj1).getListIndex() == ((EnumLevel) obj2).getListIndex());
	}
}