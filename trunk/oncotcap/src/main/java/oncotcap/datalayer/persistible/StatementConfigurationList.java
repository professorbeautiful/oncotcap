package oncotcap.datalayer.persistible;

import java.util.*;
import oncotcap.util.*;

public class StatementConfigurationList
{
	private Hashtable configurations = new Hashtable();
	private Hashtable statements = new Hashtable();
	private SortedList sortedStatements = new SortedList(new StatementBundleCompareBySortKey());
	
	public void addConfigurations(Collection statements)
	{
		Iterator it = statements.iterator();
		while(it.hasNext())
		{
			Object obj = it.next();
			if(obj instanceof StatementBundleConfiguration)
			{
				addConfiguration((StatementBundleConfiguration) obj);
			}
		}
	}

	public boolean contains(StatementBundleConfiguration config)
	{
		return(configurations.containsKey(config));
	}
	public void addConfiguration(StatementBundleConfiguration config)
	{
		if(! configurations.containsKey(config))
			configurations.put(config, new Vector());
	}

	public Hashtable getStatementHashtable()
	{
		return(statements);
	}
	public SortedList getSortedStatements()
	{
		return(sortedStatements);
	}
	public StatementBundle addStatement(StatementBundleConfiguration config)
	{
		StatementBundle sb1 = config.getStatementBundle();
		
		StatementBundle sb = (StatementBundle) sb1.clone(false);		
		statements.put(sb, config);
		sortedStatements.add(sb);
		if(! configurations.containsKey(config))
			addConfiguration(config);

		Vector stments = (Vector) configurations.get(config);
		stments.add(sb);
		return(sb);
	}
	public void removeStatement(StatementBundle sb)
	{
		StatementBundleConfiguration config = (StatementBundleConfiguration) statements.get(sb);
		Vector stments = (Vector) configurations.get(config);
		statements.remove(sb);
		stments.remove(sb);
		sortedStatements.remove(sb);
	}
	public Vector getStatements(StatementBundleConfiguration config)
	{
		if(!configurations.containsKey(config));
		addConfiguration(config);
		return((Vector) configurations.get(config));
	}

	public Enumeration getStatements()
	{
		return(statements.keys());
	}
	public StatementBundle getFirstStatement(StatementBundleConfiguration config)
	{
		if(!configurations.containsKey(config))
			return(null);

		Vector statmnts = (Vector) configurations.get(config);
		if(statmnts.size() > 0)
			return((StatementBundle) statmnts.get(0));
		else
			return(null);
	}
	public boolean isComplete(StatementBundleConfiguration config)
	{
		if(configurations.containsKey(config))
		{
			Vector statements = (Vector) configurations.get(config);
			Iterator it = statements.iterator();
			int count = 0;
			while(it.hasNext())
			{
				count++;
				if( ! ((StatementBundle) it.next()).isComplete())
					return(false);
			}
			if(count > 0)
				return(true);
			else
				return(false);
		}
		else
			return(false);
	}
	public StatementBundleConfiguration getConfiguration(StatementBundle statement)
	{
		if(!statements.containsKey(statement))
			return(null);
		else
			return((StatementBundleConfiguration) statements.get(statement));
	}
}