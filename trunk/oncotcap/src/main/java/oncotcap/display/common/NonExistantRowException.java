package oncotcap.display.common;

public class NonExistantRowException extends Exception
{
	private String err;

	public NonExistantRowException()
	{
		err = new String("The referenced row does not exist");
	}
	public NonExistantRowException(String err)
	{
		this.err = err;
	}

	public String toString()
	{
		return(err);
	}

}