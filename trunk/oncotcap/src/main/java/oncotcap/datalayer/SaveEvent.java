package oncotcap.datalayer;

public class SaveEvent
{
	Object savedObject;
	public SaveEvent(Object p)
	{
		savedObject = p;
	}
	public Object getSavedObject()
	{
		return(savedObject);
	}
		public String toString(){
				return savedObject.toString();
		}
}
