package oncotcap.util;

import java.util.*;

/**
 **  HashVector is a combination of Vector and Hashtable.  It allows
 **  multiple values to be associated with the same key.
 **/
public class HashVector
{
	private final static Vector emptyVector = new Vector();
	private Vector allValues = new Vector();
	private Hashtable values = new Hashtable();
	
	public void put(Object key, Object value)
	{
		Vector vals;
		if(values.containsKey(key))
		{
			vals = (Vector) values.get(key);
		}
		else
		{
			vals = new Vector();
			values.put(key, vals);
		}
		vals.add(value);
		allValues.add(value);
	}
	public boolean containsKey(Object key)
	{
		return(values.containsKey(key));
	}
	public boolean containsValue(Object val)
	{
		return(allValues.contains(val));
	}
	public Vector get(Object key)
	{
		if(values.containsKey(key))
			return((Vector) values.get(key));
		else
			return(emptyVector);
	}
	public Vector getAllValues()
	{
		return(allValues);
	}
	public int size()
	{
		return(allValues.size());
	}
	public int size(Object key)
	{
		if(!values.containsKey(key))
			return(0);
		else
			return(((Vector) values.get(key)).size());
	}
	public void removeKey(Object key)
	{
		if(values.containsKey(key))
		{
			Vector vals = (Vector) values.get(key);
			Iterator it = vals.iterator();
			while(it.hasNext())
			{
				allValues.remove(it.next());
			}
			values.remove(key);
		}
	}
}