package oncotcap.datalayer;

public interface SaveListener
{
	public void objectSaved(SaveEvent e);
	public void objectDeleted(SaveEvent e);
}
