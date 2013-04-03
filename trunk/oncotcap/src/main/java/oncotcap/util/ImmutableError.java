package oncotcap.util;

public class ImmutableError extends Error
{
	private static final String  immutableMessage = new String("This object is read only.");
	public ImmutableError()
	{
		super(immutableMessage);
	}
	public ImmutableError(String message)
	{
		super(message);
	}
}
