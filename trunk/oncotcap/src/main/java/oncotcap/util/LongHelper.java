package oncotcap.util;

public class LongHelper
{
	public static void main(String [] args)
	{
		System.out.println(LongHelper.hexToLong("0000000000000000"));
		System.out.println(LongHelper.hexToLong("8000000000000000"));
		System.out.println(LongHelper.hexToLong("7fffffffffffffff"));
		System.out.println(LongHelper.hexToLong("ffffffffffffffff"));
		System.out.println(LongHelper.hexToLong("fffffffffffffffe"));
	}
	public static long hexToLong(String hexValue)
	{
		String value = new String(hexValue);
		if(value.length() == 16)
		{
			int firstDigit = Integer.parseInt(value.substring(0,1), 16);
			if(firstDigit >= 8)  //negative value
			{
				firstDigit = firstDigit - 8;
				value = new String(firstDigit + value.substring(1));
				return(hexToLong(value) + Long.MIN_VALUE); //subtract 2^64 from the absolute value
			}
		}
		return(Long.parseLong(value,16));
	}
}