package oncotcap.datalayer.persistible.parameter;


public class SystemDefinedTcapString extends TcapString
{

	public SystemDefinedTcapString(oncotcap.util.GUID guid){
		super(guid);
	}
	public SystemDefinedTcapString(){}
	public SystemDefinedTcapString(boolean saveToDataSource)
	{
		super(saveToDataSource);
	}
	public SystemDefinedTcapString(String val, boolean saveToDataSource)
	{
		super(saveToDataSource);
		setValue(val);
	}
}
