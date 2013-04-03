package oncotcap.util;

public abstract class OncEnum
{
	private final String name;
	private Long hashValue = null;
	private Long value = null;
	
	public OncEnum()
	{
		this.name = "";
	}
	
	public OncEnum(String name)
	{
		this.name = name;
	}
	
	public abstract OncEnum [] getValues();
	
	public String toString()
	{
		return(name);
	}
	
	public long getHashValue()
	{
		if(hashValue == null)
		{
			hashValue = new Long(Hash.hash(name));
		}
		return(hashValue.longValue());
	}
	public long getValue()
	{
		if(value == null)
		{
			OncEnum [] enums = getValues();
			int n;
			for(n=0; n<enums.length; n++)
				enums[n].setValue(n+1);
		}		
		if(value == null)
		{
			Logger.log("ERROR: Enum value not correctly set");
			return(0);
		}
		else
			return(value.longValue());
	}
	
	protected void setValue(long val)
	{
		value = new Long(val);
	}
	
	public final boolean equals(Object o) { return(super.equals(o)); }
	public final int hashCode() { return(super.hashCode()); }
}