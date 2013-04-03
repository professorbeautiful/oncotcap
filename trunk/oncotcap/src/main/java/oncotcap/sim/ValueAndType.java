package oncotcap.sim;


public class ValueAndType
{

	private Object value;
	private Class type;
	public ValueAndType(Object value, Class type)
	{
		this.value = value;
		this.type = type;
	}
	public Object getValue()
	{
		return(value);
	}
	public Class getType()
	{
		return(type);
	}
}