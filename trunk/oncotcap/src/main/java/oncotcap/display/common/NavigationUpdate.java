package oncotcap.display.common;

public class NavigationUpdate
{
	private int value;

	public NavigationUpdate(int value)
	{
		this.value = value;
	}

	public void setValue(int value)
	{
		this.value = value;
	}

	public int getValue()
	{
		return(value);
	}
}