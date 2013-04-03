package oncotcap.datalayer.persistible;

import java.util.*;
import oncotcap.datalayer.*;

public class StatementList  implements PersistibleList
{
	private Vector<StatementBundle> statements = new Vector<StatementBundle>();

	public void add(StatementBundle sb)
	{
		statements.add(sb);
	}
//   public void add(StatementTemplate sb)
//	{
//		statements.add(sb);
//	}
	public Iterator<StatementBundle> getIterator()
	{
		return(statements.iterator());
	}
	public void clear()
	{
		statements.clear();
	}

	public void set(Collection listItems)
	{
		statements = new Vector<StatementBundle>(listItems);
	}
	public boolean contains(StatementBundle sb)
	{
		
		if(statements.contains(sb))
			return(true);
		
		for(StatementBundle testBundle : statements)
		{
			if(testBundle.contains(testBundle))
				return(true);
		}
		
		return(false);
	}
}
