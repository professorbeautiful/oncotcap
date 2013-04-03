package oncotcap.util;

import java.util.Hashtable;

public class ObjectSerializer
{
	private static Hashtable<Object, Integer> serialnos = new Hashtable<Object, Integer>();
	private static int serialcount = 0;
	/**
	 ** getSerialNumber returns a unique serial number for each object.  Once
	 ** an object has an assigned serial number it will be returned every time
	 ** getSerialNumber is called for that object.
	 **/
	public static int getSerialNumber(Object o)
	{
		if(!serialnos.containsKey(o))
			serialnos.put(o, new Integer(++serialcount));
		
		return(serialnos.get(o).intValue());
	}
}
