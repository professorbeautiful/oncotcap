package oncotcap.sim;

public class ConversionException extends Exception
{
	String argName;
	
	public ConversionException(String argName)
	{
		this.argName = argName;
	}

	public String toString()
	{
		return("Argument: " + argName + " cannot be converted to the specified format.");
	}

}