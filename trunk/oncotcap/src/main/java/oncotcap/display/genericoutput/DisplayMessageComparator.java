package oncotcap.display.genericoutput;


import java.util.*;

import oncotcap.display.common.DisplayMessage;

public class DisplayMessageComparator implements Comparator
{
	public int compare(Object obj1, Object obj2)
	{
		if (((DisplayMessage) obj1).getTime() > ((DisplayMessage) obj2).getTime())
			return(1);
		else if (((DisplayMessage) obj1).getTime() < ((DisplayMessage) obj2).getTime())
			return(-1);
		else
			return(0);
	}

	public boolean equals(Object obj1, Object obj2)
	{
		return( ((DisplayMessage) obj1).getTime() == ((DisplayMessage) obj2).getTime() );
	}
}
