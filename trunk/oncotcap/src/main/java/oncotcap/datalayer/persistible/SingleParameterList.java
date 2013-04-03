package oncotcap.datalayer.persistible;

import java.util.*;
import oncotcap.util.GUID;
import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.parameter.SingleParameter;

public class SingleParameterList
{
	private Vector<SingleParameter> list = new Vector<SingleParameter>();
	private Vector saveListeners = new Vector();
	
	public SingleParameterList(){}
	public void add(SingleParameter sp)
	{
		if(! list.contains(sp))
		{
			list.add(sp);
		} 
		
	}
	public void addAll(Collection coll)
	{
		Iterator it = coll.iterator();
		while(it.hasNext())
		{
			Object spt = it.next();
			if(spt instanceof SingleParameter)
				add((SingleParameter) spt);
		}
	}
	public void remove(SingleParameter sp)
	{
		if(contains(sp))
		{
			list.remove(sp);
		}
	}
	public void replace(SingleParameter paramToReplace, SingleParameter sp)
	{
		int idx = -1;
		if(contains(paramToReplace))
		{
			idx = list.indexOf(paramToReplace);
			list.remove(paramToReplace);
		}

		if(idx >= 0 && idx <= list.size())
			list.add(idx, sp);
		else
			list.add(sp);
	}
	public SingleParameter get(int index)
	{
		if(index >= getSize())
			return(null);
		else
		{
			return((SingleParameter) list.get(index));
		}
	}
	public Iterator<SingleParameter> getIterator()
	{
		return(list.iterator());
	}
	public Collection<SingleParameter> getValues()
	{
		return(list);
	}
	public boolean contains(SingleParameter sp)
	{
		return(list.contains(sp));
	}
	public SingleParameter getByID(String singleParameterID)
	{
		SingleParameter rVal = null;
		Iterator it = list.iterator();
		while(it.hasNext())
		{
			SingleParameter sp = (SingleParameter) it.next();
			if(sp.getID().equals(singleParameterID))
				rVal = sp;
		}
		return(rVal);
	}
/*	public SingleParameter getByID(oncotcap.util.GUID guid)
	{
		return(getByID(guid.toString()));
	}*/
	public SingleParameter getSingleParameter(String singleParameterID)
	{
		SingleParameter sp;
		SingleParameter rVal = null;
		Iterator it = list.iterator();
		while(it.hasNext())
		{
			sp = (SingleParameter) it.next();
			if (sp.getSingleParameterID().equals(singleParameterID))
				rVal = sp;
		}
		return(rVal);
	}
	public SingleParameter getByDefaultName(String name)
	{
		SingleParameter singleParam;
		SingleParameter rVal = null;
		Iterator it = list.iterator();
		while(it.hasNext())
		{
			singleParam = (SingleParameter) it.next();
			if(singleParam.getDefaultName().equalsIgnoreCase(name))
				rVal = singleParam;
		}
		return(rVal);
	}
	public SingleParameter getByIndex(int index)
	{
		SingleParameter singleParam;
		SingleParameter rVal = null;
		Iterator it = list.iterator();
		int idx = 0;
		while(it.hasNext())
		{
			singleParam = (SingleParameter) it.next();
			if(index == idx++)
				rVal = singleParam;
		}
		return(rVal);
	}
	public int indexOf(SingleParameter sp)
	{
		return(list.indexOf(sp));
	}
	public int getSize()
	{
		return(list.size());
	}
	public void clear()
	{
		list.clear();
	}
	public SingleParameter getFirst()
	{
		SingleParameter rVal = null;
		Iterator it = list.iterator();
		if(it.hasNext())
			rVal = (SingleParameter) it.next();
		
		return(rVal);
	}
	public void removeSingleParameter(String property)
	{
		SingleParameter removeMe = null;
		Iterator it = list.iterator();
		while(it.hasNext())
		{
			SingleParameter sp = (SingleParameter) it.next();
			if(sp.toString().equalsIgnoreCase(property))
			{
				removeMe = sp;
			}
		}
		if(removeMe != null)
			list.remove(removeMe);
	}
	public void removeParameterWithID(String id)
	{
		Iterator it = list.iterator();
		SingleParameter paramToRemove = null;
		while(it.hasNext())
		{
			SingleParameter sp = (SingleParameter) it.next();
			if(sp.getSingleParameterID().equals(id))
			{
				paramToRemove = sp;
			}
		}
		if(paramToRemove != null)
			list.remove(paramToRemove);
	}
	public boolean contains(String singleParameterID)
	{
		return(getByID(singleParameterID) != null);
	}

	public void addSaveListener(SaveListener listener)
	{
		if(! saveListeners.contains(listener))
		{
			saveListeners.add(listener);
			SingleParameter sp;
			Iterator it = getIterator();
			while(it.hasNext())
			{
				sp = (SingleParameter) it.next();
				sp.addSaveListener(listener);
			}
		}
	}
	public void removeSaveListener(SaveListener listener)
	{
		SingleParameter sp;
		if(saveListeners.contains(listener))
		{
			saveListeners.removeElement(listener);
		}
		Iterator it = getIterator();
		while(it.hasNext())
		{
			sp = (SingleParameter) it.next();
			sp.removeSaveListener(listener);
		}
	}
		public String toString() {
				if ( list != null ) 
						return list.toString();
				else
						return "null";
		}
}
