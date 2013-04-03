package oncotcap.util;


public class OperatorHelper
{

	public static boolean XOR(boolean a, boolean b)
	{
		return( ( (!a) && b) || (a && (!b)) );
	}
}
