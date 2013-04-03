package oncotcap.process;

import oncotcap.util.ImmutableHashtable;

public interface ParentListener
{
	public void collectionChanged(ImmutableHashtable<Class, OncCollection> collectionTable);
}
