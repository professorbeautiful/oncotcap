package oncotcap.util;

import java.util.*;
import java.io.*;
import javax.swing.ListModel;
import javax.swing.event.*;
/**
 * This class provides a list that is always sorted.  As items are
 * added they are automatically inserted in sorted order based on the
 * Comparator supplied at during instantiation.  Items can be added and
 * removed from the list while it is being iterated over using the
 * native hasNext(), getNext() methods.
 **/
public class SortedList<E> extends ArrayList<E> implements ListModel
{
	private Comparator ocomp;
	private int first = -1;
 	private int nEntries = 0;
 	private int current = -1;
	public long addAtEnd = 0;
	public long totalAdds = 0;
	public boolean keepStats = false;
	public long maxSize = 0;
	private FileWriter out;
			
	public static void main(String[] args)
	{
/*		SortedList sl = new SortedList(new TriggerCompare());
		TriggerEntry t1 = new TriggerEntry(0.5, null, null);
		TriggerEntry t2 = new TriggerEntry(1.0, null, null);
		TriggerEntry t3 = new TriggerEntry(3.5, null, null);
		TriggerEntry t4 = new TriggerEntry(3.5, null, null);
		TriggerEntry t5 = new TriggerEntry(2.0, null, null);

		sl.add(t1);
		sl.add(t2);
		sl.add(t3);
		sl.add(t4);
		sl.add(t5);

        while(sl.hasNext())
             Logger.log(((TriggerEntry) sl.getNext()).triggerTime);*/

		SortedList s1 = new SortedList(new StringCompare());
		s1.add("B");
		s1.add("D");
		s1.add("F");
		s1.add("C");

		int i = s1.find("F");

		while(s1.hasNext())
			Logger.log((String) s1.getNext());

		Logger.log(new Integer(i));
	}


	public SortedList(Comparator comp, boolean turnStatsOn)
	{
		if (turnStatsOn)
		{
			statsOn();
		}
		ocomp = comp;
	}
	public SortedList(Comparator comp)
	{
		ocomp = comp;
	}

	public SortedList(Comparator comp, E [] objs)
	{
		int n;
		ocomp = comp;
		for(n = 0; n<objs.length; n++)
			add(objs[n]);
	}

	public void add(int i, E obj)
	{
		throw(new NoSuchMethodError("add(int, Object) method not usable in SortedList."));
	}
	public boolean addAll(int index, Collection c)
	{
		throw(new NoSuchMethodError("addAll(int, Collection) method not usable in SortedList."));
	}
	public boolean addAll(Collection c)
	{
		Iterator it = c.iterator();
		while(it.hasNext())
			addItem((E)it.next());
		contentsChanged();
		return(true);
	}
	public boolean add(E newEntry)
	{
		int addIdx = addItem(newEntry);
		intervalAdded(addIdx, addIdx);
		return(true);
	}
	private int addItem(E newEntry)
	{
		int idx0;
		totalAdds++;
		if ( size() == 0 )
		{
			super.add(newEntry);
			//writeStats(size());
			nEntries = 1;
			first = 0;
			return(0);
		}

		int insert = Collections.binarySearch(this, newEntry, ocomp);
		if (insert > size() || insert < -size()-1)
		{
		    Logger.log("ERROR: In SortedList- invalid insertion value");
		}
		if (insert == size())
		{
			super.add(newEntry);
			//writeStats(size());
			idx0 = size() - 1;
			addAtEnd++;
		}
		else if(insert == -size()-1)
		{
		        super.add(newEntry);
				//writeStats(size());
				idx0 = size() - 1;
				addAtEnd++;
		}
		else if (insert < 0)
		{
			idx0 = (-insert) - 1;			
			super.add(idx0, newEntry);
			//writeStats((-insert));
		}
		else
		{
			while( ( (insert + 1) < size()) && ( ocomp.compare(get(++insert),newEntry) == 0) ){}

			if (  ((insert + 1) == size()) && ocomp.compare(get(insert),newEntry) == 0)
			   insert++;

			if ((insert+1) <= size())
			{
				super.add(insert, newEntry);
				idx0 = insert;
				//writeStats(insert+1);
			}
			else
			{
				super.add(newEntry);
				//writeStats(size());
				idx0 = size() - 1;
				addAtEnd++;
			}
		}
		nEntries++;
		return(idx0);
	}
    public E getFirst()
    {
    	if(first >= 0)
     	{
    		current = first;
            return(super.get(0));
        }
        //TODO: else throw(NORECORDS)
        return(null);
    }
    public Object getNext()
    {
      if(hasNext())
      {
        current++;
        return(get(current));
      }
      else
        return(null);
//TODO: throw(NOMORERECORDS)
    }
    public boolean hasNext()
    {
      if ( (current /* + 1 */) < size()  && size() > 0)
        return(true);
      else
        return(false);

    }
	 public boolean remove(Object obj)
	 {
		 int objIdx = super.indexOf(obj);
		 if(objIdx >= 0)
		 {
			 remove(objIdx);
			 return(true);
		 }
		 else
			 return(false);
	 }
    public E remove(int index)
    {
		if (size() > 0)
		{
   			nEntries--;
            if (nEntries == 0) first = -1;
			if (current >= index) current--;
			intervalRemoved(index, index);
			return(super.remove(index));
		}
		else
			return(null);
            //TODO: else throw(ILLEGALOPERATIONONEMPTYARRAY)
    }
    public void resetIterator()
    {
      first=-1;
    }
    public void clear()
    {
            first = -1;
            nEntries = 0;
            current = -1;
            super.clear();
    }
	public int find(E obj)
	{
		return(Collections.binarySearch(this, obj, ocomp));
	}
	public boolean contains(Object obj)
	{
/*		for(Object cObj : this)
		{
			if(ocomp.compare(cObj, obj) == 0)
				return(true);
		}
		return(false); */
		return(super.contains(obj));
/*		int i = Collections.binarySearch(this, obj, ocomp);
		if( i >= 0 )
			return(true);
		else
			return(false); */
	}

	  public boolean equals(Object obj)
	  {
	 int n;

			if(! (obj instanceof SortedList))
			  return(false);

			SortedList sl = (SortedList) obj;

			if (sl.nEntries != this.nEntries)
			  return(false);

			for(n=0; n < nEntries; n++)
			  if ( ! (this.get(n).equals(sl.get(n)) ))
				 return(false);

			return(true);
	  }

	public void statsOn()
	{
		try
		{
			out = new FileWriter("u:\\SortedListStats.txt", false);
			keepStats = true;
		}catch(IOException e){Logger.log("Error opening SortedList stat file \n" + e);}

	}
/* void writeStats(long placement)
	{
		if (!keepStats) return;
		if (size() > maxSize) maxSize = size();
		try
		{
			out.write( oncotcap.sim.schedule.MasterScheduler.globalTime + "\t" + placement + "\t" + size() + "\n");
		}catch(IOException e){Logger.log("Error writing SortedList stat file \n" + e);}
	}*/

	public void resort()
	{
		int i, j;
		boolean swapped = false;
		int arryLen = size() - 1;
		for(i = 0; i < arryLen; i++)
			for(j = i + 1; j <= arryLen; j++)
				if(ocomp.compare(super.get(i), super.get(j)) > 0)
				{
					swap(i, j);
					swapped = true;
				}

		if(swapped)
			contentsChanged();
	}
	private void swap(int idx1, int idx2)
	{
		int i1 = Math.min(idx1, idx2);
		int i2 = Math.max(idx1, idx2);
		E obj1 = super.get(i1);
		E obj2 = super.get(i2);
		super.remove(i2);
		super.remove(i1);
		super.add(i1, obj2);
		super.add(i2, obj1);
	}
	public Object getLast()
	{
		if(size() > 0)
			return(super.get(size() - 1));
		else
			return(null);
	}
	private Vector listDataListeners = new Vector();
	public void addListDataListener(ListDataListener l)
	{
		if(!listDataListeners.contains(l))
			listDataListeners.add(l);
	}
	public E getElementAt(int index) {return(get(index));}
	public int getSize() {return(size());}
	public void removeListDataListener(ListDataListener l)
	{
		listDataListeners.remove(l);
	}
	
	//Sent when the contents of the list has changed in a way that's too complex to characterize with the previous methods. 
	private void contentsChanged()
	{
		ListDataEvent e = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, size() - 1);
		Iterator it = listDataListeners.iterator();
		while(it.hasNext())
			((ListDataListener) it.next()).contentsChanged(e);
	}
	//	Sent after the indices in the index0,index1 interval have been inserted in the data model. 
	private void intervalAdded(int index0, int index1)
	{
		ListDataEvent e = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, index0, index1);
		Iterator it = listDataListeners.iterator();
		while(it.hasNext())
			((ListDataListener) it.next()).intervalAdded(e);
	}
	//Sent after the indices in the index0,index1 interval have been removed from the data model. 
	void intervalRemoved(int index0, int index1)
	{
		ListDataEvent e = new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, index0, index1);
		Iterator it = listDataListeners.iterator();
		while(it.hasNext())
			((ListDataListener) it.next()).intervalRemoved(e);
	}

}
