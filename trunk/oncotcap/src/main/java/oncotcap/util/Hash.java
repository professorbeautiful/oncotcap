package oncotcap.util;

import java.lang.reflect.Array;

public class Hash
{
	static final char decToHex[] = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
	public static void main(String[] args)
	{
		int data[] = {0,0,0,0,0};
		int n;
		for(n=1; n<=4; n++)
			updateData(data, 0, 16, n);
	}

	private static void updateData(int data[], int col, int range, int maxcols)
	{
		int n;
		for(n = 0; n <= range - 1; n++)
		{
			data[col] = n;
			if(col == maxcols - 1)
				print(data, maxcols);
			else
				updateData(data, col + 1, range, maxcols);
		}
	}
	private static void print(int data[], int len)
	{
//		int n;
//		for(n=0; n<len; n++)
//			Logger.logn(new String(decToHex[data[n]]));
//		Logger.logn(new String("\t" + hash(data,len) + "\n"));
	}

	public static int hash(int [] data, int len)
	{
		int val;
		val = 17;
		int n;
		for(n=0; n<len; n++)
			val = 37 * val + data[n];
		return(val);
	}
	public static int hash(int [] data)
	{
		return(hash(data, data.length));
	}
	
	public static long hash(String data)
	{
		return(hash(data.getBytes()));
	}
	public static long hash(byte [] data)
	{
		long val;
		val = 17;
		int n;
		for(n=0; n<data.length; n++)
			val = 37 * val + ((long) data[n]);
		return(val);
	}
	public static long hash(long [] data, int len)
	{
		long val;
		val = 17;
		int n;
		for(n=0; n<len; n++)
			val = 37 * val + data[n];
		return(val);

	}

	public static long hash(long [] data)
	{
		return(hash(data, data.length));
	}
	
	public static long nextHash(long previousHash, long nextNumber)
	{
		return(37 * previousHash + nextNumber);
	}

	public static int nextHash(int previousHash, int nextNumber)
	{
		return(37 * previousHash + nextNumber);
	}
	public static long hash(oncotcap.util.OncEnum [] enums)
	{
		long val;
		val = 17;
		int n;
		for(n=0;n<enums.length;n++)
		{
			val = 37 * val + enums[n].getHashValue();
		}

		return(val);
	}
}