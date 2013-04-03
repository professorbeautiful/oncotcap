package oncotcap.process;

public interface CollectionListener
{
	public void collectionChanged(OncCollection<? extends OncProcess> changedCollection);
}
