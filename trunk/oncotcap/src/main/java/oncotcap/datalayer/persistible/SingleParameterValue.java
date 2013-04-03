package oncotcap.datalayer.persistible;

public class SingleParameterValue
{
	private String displayString = null;
	private String codeString = null;

	public String getDisplayString()
	{
		return(displayString);
	}
	public String getCodeString()
	{
		if(codeString == null)
			return(displayString);
		else
			return(codeString);
	}
	public void setDisplayString(String val)
	{
		displayString = val;
	}
	public void setCodeString(String val)
	{
		codeString = val;
	}

	public String toString()
	{
		return(displayString);
	}
}