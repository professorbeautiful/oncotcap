package oncotcap.util;

import java.net.*;
import java.io.Serializable;
import java.beans.*;
import java.util.*;


import oncotcap.sim.random.MersenneTwister;

/**
 * class GUID
 * 
 * GUID is used to generate Globally Unique Identifiers.  It does this
 * by combining a session id with the computer's IP address and the
 * current time.  In the event that the computer doesn't have an IP
 * address, or the address is a localhost or local network address a
 * random number is substituted.
 * 
 **/
public class GUID extends Object implements Serializable
{
	private static final long serialVersionUID = 298347203423l;
	private static int counter = 0;
	private static MersenneTwister mt = new MersenneTwister();
	private static Integer baseIP = null;
	private long serializer;
	private long time;
	private String strID;
	
		static {
				try {
						// Make the props property transient
						BeanInfo info = Introspector.getBeanInfo(GUID.class);
						PropertyDescriptor[] propertyDescriptors = 
								info.getPropertyDescriptors();
						for (int i = 0; i < propertyDescriptors.length; ++i) {
								PropertyDescriptor pd = propertyDescriptors[i];
								String writeMethod = null;
								if ( pd.getWriteMethod() != null ) 
										writeMethod = pd.getWriteMethod().getName();
								System.out.println("Property " + pd.getName()
																	 + " setMethod " 
																	 + writeMethod
																	 + " getMethod " 
																	 + pd.getReadMethod().getName());
								for (Enumeration e = pd.attributeNames() ; e.hasMoreElements() ;) {
										System.out.println("Attribute " + e.nextElement());
								}

							// 	if (!pd.getName().equals("GUID")) {
// 										pd.setValue("transient", Boolean.TRUE);
// 								}
						}
				} catch (IntrospectionException e) {
						System.out.println("Introspection exception");
				}
		}

	public static void main(String [] args)
	{
		GUID g = pvt_nextGUID();
		System.out.println(g.equals(g.toString()));
		System.out.println(g.equals(GUID.fromString(g.toString()) ));
		System.out.println(nextGUID());
	}
	public GUID(long serializer, long time)
	{
		this.serializer = serializer;
		this.time = time;
	}
	public GUID()
	{
		this(pvt_nextGUID());
	}
	public GUID(String hexGUID)
	{
		this(fromString(hexGUID));
	}
	private GUID(GUID guid)
	{
		this.serializer = guid.getSerializer();
		this.time = guid.getTime();
	}
	public long getTime()
	{
		return(time);
	}
	public long getSerializer()
	{
		return(serializer);
	}
	/**
	 ** nextGUID() generates a GUID.
	 **/
	public static String nextGUID()
	{
		return(new GUID().toString());
	}
	
	private static GUID pvt_nextGUID()
	{
		long ips;
		if(baseIP == null)
		{
			try
			{
				//get ip address
				InetAddress id = InetAddress.getLocalHost();
				byte[] ip = id.getAddress();

				//store ip address in an integer
				baseIP = new Integer(NetHelper.ip2Int(ip));
			}
			//on a Win2K computer with no network installed a HostNotFound
			//exception is thrown on the first call to getLocalHost(), on
			//subsequent calls a null pointer exception is thrown.  If
			//this happens, use a randomly generated int.
			catch(Exception e)
			{
				baseIP = new Integer(0);
			}
		}

		int tip = baseIP.intValue();

		//if the ip address is 0, localhost or in any reserved
		//local network range substitute a random integer.
		if(tip == 0)
			tip = mt.nextInt();
		else if(NetHelper.isLocalHost(tip) ||  NetHelper.isLocalNet(tip))
			tip = Hash.nextHash(mt.nextInt(), tip);

		//put ip (or random number in the high bytes of a long and add a
		//session specific serializer.
		long lip = ((((long)tip) << 32) & 0xffffffff00000000L) + counter++;

		long currentTime = System.currentTimeMillis();
		
		GUID guid = new GUID(lip, currentTime);
		
		return(guid);
	}

	public String toString()
	{
		//return a string with the id (ip + serializer) and current time
		if(strID == null)
			strID = new String(StringHelper.padToLength(Long.toHexString(serializer),'0',16) + StringHelper.padToLength(Long.toHexString(time),'0',16));
		return(strID);
	}
	public static GUID fromString(String strGUID)
	{
		if(strGUID.length() != 32)
			return(null);
		else
		{
			long serializer = LongHelper.hexToLong(strGUID.substring(0,16));
			long time = LongHelper.hexToLong(strGUID.substring(16, 32));
			return(new GUID(serializer, time));
		}
	}
	public boolean equals(Object obj)
	{
		if(obj instanceof GUID)
		{
			GUID guid = (GUID) obj;
			return(equals(guid.getSerializer(), guid.getTime()));
		}
		else if(obj instanceof String)
		{
			String strCmp = (String) obj;
			if(strCmp.length() != 32)
				return(false);
			else
			{
				boolean serial = strCmp.substring(0,16).equalsIgnoreCase(StringHelper.padToLength(Long.toHexString(serializer),'0',16));
				boolean cmpTime= strCmp.substring(16,32).equalsIgnoreCase(StringHelper.padToLength(Long.toHexString(time),'0',16));
				if(serial && cmpTime)
					return(true);
				else
					return(false);
			}
		}
		else
			return(false);
	}
	private boolean equals(long serializer, long time)
	{
		if(serializer == this.serializer && time == this.time)
			return(true);
		else
			return(false);
	}
	public int hashCode()
	{
		return((int) Hash.nextHash(serializer, time));
	}
		public void setStrId(String str) {
				
		}
		public String getStrId() {
				return toString();
		}
}
