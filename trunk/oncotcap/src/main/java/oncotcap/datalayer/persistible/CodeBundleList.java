package oncotcap.datalayer.persistible;

import java.util.*;
import oncotcap.datalayer.*;

public class CodeBundleList implements PersistibleList
{
	private Vector<CodeBundle> list = new Vector<CodeBundle>();
	
	public void add(CodeBundle cb)
	{
		list.add(cb);
	}
	public Iterator getIterator()
	{
		return(list.iterator());
	}
	public void clear()
	{
		list.clear();
	}

	public void set(Collection listItems)
	{
		list = new Vector<CodeBundle>(listItems);
	}
	public Collection<CodeBundle> getList()
	{
		return(list);
	}
}
