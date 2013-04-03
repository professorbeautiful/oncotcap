package oncotcap.util;

public class NetHelper
{
	public final static int LOCALHOST = 0x7f000001;       //127.0.0.1
	public final static int LOCALNET_A_LOW = 0x0A000000;  //10.0.0.0
	public final static int LOCALNET_A_HIGH = 0x0AFFFFFF; //10.255.255.255
	public final static int LOCALNET_B_LOW = 0xAC100000;  //172.16.0.0
	public final static int LOCALNET_B_HIGH = 0xAC1FFFFF; //172.31.255.255
	public final static int LOCALNET_C_LOW = 0xC0A80000;  //192.168.0.0
	public final static int LOCALNET_C_HIGH = 0xC0A8FFFF; //192.186.255.255

	public static void main(String [] args)
	{
		System.out.println(Integer.toHexString(LOCALNET_B_LOW));
	}
	public static boolean isLocalHost(byte [] ip)
	{
		return(isLocalHost(ip2Int(ip)));
	}
	public static boolean isLocalHost(int ip)
	{
		if(ip == LOCALHOST) return(true);
		else return(false);
	}
	public static boolean isLocalNet(byte [] ip)
	{
		return(isLocalNet(ip2Int(ip)));
	}
	public static boolean isLocalNet(int ip)
	{
		if(ip >= LOCALNET_A_LOW && ip <= LOCALNET_A_LOW)       //class a
			return(true);
		else if(ip >= LOCALNET_B_LOW && ip <= LOCALNET_B_HIGH) //class b
			return(true);
		else if(ip >= LOCALNET_C_LOW && ip <= LOCALNET_C_HIGH) //class c
			return(true);
		else
			return(false);
	}
	public static int ip2Int(byte [] ip)
	{
		if(ip.length != 4)
			return(0);
		else
		{
			int rVal = ((int)ip[0]) << 24 | 
						  ((((int)ip[1]) << 16) & 0x00ff0000) +
						  ((((int)ip[2]) << 8) & 0x0000ff00) +
						  (((int)ip[3]) & 0x000000ff);
			return(rVal);
		}
	}
}