package oncotcap.sim;

public class ArgumentNotFoundException extends Exception
{
	String argName;
	
	public ArgumentNotFoundException(String argName)
	{
		this.argName = argName;
	}

	public String toString()
	{
		return("Argument: " + argName + " not found.");
	}

}