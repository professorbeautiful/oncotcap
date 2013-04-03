package oncotcap.util;


public class ForceStackTrace 
{
	
	public static void showStackTrace() {
			Integer forceStackTrace = null;
			try {
					System.out.println("Forced stackTrace" 
														 + forceStackTrace.getClass());	
			}catch(Exception ee) {
					System.out.println("Forced stackTrace >>>" );
					ee.printStackTrace();
			}		
	}
	public static void showStackTrace(Object  obj) {
			Integer forceStackTrace = null;
			try {
					System.out.println("Forced stackTrace" 
														 + forceStackTrace.getClass());	
			}catch(Exception ee) {
					System.out.println("Forced stackTrace >>>"  + obj);
					ee.printStackTrace();
			}		
	}

}
