package oncotcap.util;

/**
 ** This class provides system specific helper methods.
 **/
public class SystemUtil
{
	private static boolean envNotAvailable = false;
	
	static
	{
		try{System.loadLibrary("getenv4.0");}
		catch(SecurityException e){envNotAvailable = true;}
		catch(UnsatisfiedLinkError e){envNotAvailable = true;}
		catch(Throwable e){envNotAvailable = true;}
	}
	
	private static native String getenv(String var);

	/**
	 ** This method returns the value of an environment variable.  JNI
	 ** is used to get the value of the environment variable.  See
	 ** the file getenv.c for more information.
	 **
	 ** @param var A string value representing the name of the
	 **            environment variable to be read.
	 **
	 ** @return The current value of the environment variable.  Returns null if
	 **         the variable doesn't exist or if the getenv JNI library
	 **         isn't installed.
	 **/
	public static String getEnv(String var)
	{
		String rVal;
		
		if(envNotAvailable)
		{
			Logger.log("\nWARNING: Cannot access environment variable: " + var + ".\n" + "Place the getenv native library in the library search path.\n");
			return(null);
		}
		else
		{
			try{rVal = getenv(var);}
			catch(Throwable e)
			{
				Logger.log("\nWARNING: Cannot access environment variable: " + var + ".\n" + "getenv.dll may be wrong version.\n");
				envNotAvailable = true;
				return(null);
			}
			return(rVal);
		}
	}

		public static String getSystemProperty(String property) {
        String value;
        try {
            value = System.getProperty(property);
        } catch (SecurityException e) {
            value = null;
        }
        return value;
    }

	public static void main(String [] args)
	{
		checkVersion();
	}
	
	public static boolean checkVersion()
	{
		String ver = System.getProperty("java.version");
		if(! ver.startsWith("1.7"))
		{
			OncMessageBox.showError("Java 1.7 (7) required.\nPlease reinstall and choose a java version 1.5 virtual machine.", "Incorrect Version");
			return(false);
		}
		return(true);
	}
}
