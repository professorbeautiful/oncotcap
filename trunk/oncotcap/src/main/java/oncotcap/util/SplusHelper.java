package oncotcap.util;

import java.lang.reflect.*;

/**  to DO:   get rid of hard-coded Splus home
 **  **/

class SplusHelper {
	static java.lang.reflect.Method methodEval, methodGetOutput;
	static Class splusDataResultClass = null;
	static Class splusUserAppClass = null;
	static final Class getOutputArgType[] = {};
	static final Object getOutputArgs[] = {};
	static {
		Class evalArgType[] = {String.class, 
			boolean.class, boolean.class, 
			boolean.class, boolean.class, boolean.class};
		try {
		methodEval = 
			splusUserAppClass.getMethod("eval", evalArgType);
		methodGetOutput = 
			splusDataResultClass.getMethod(
			"getOutput", getOutputArgType);
		}
		catch (Exception e){
			System.out.println(e);
			e.printStackTrace();
		}
		findSplusClasses();
	}

	public double sPlusResultDouble(String expression) {
		return(Double.parseDouble(sPlusResultString(expression)));
	}
	public String sPlusResultString(String expression) {
		System.out.println("Sending expression " + expression);
		Object evalArgs[] = {expression + "\n",
			Boolean.TRUE, Boolean.FALSE, 
			Boolean.FALSE, Boolean.FALSE, Boolean.FALSE};
		Object result = null;
		Object result2 = null;
		try {
			result = methodEval.invoke(null, evalArgs);
			result2 = methodGetOutput.invoke(result, getOutputArgs);
		} catch (IllegalAccessException e) {
			System.out.println(e);
		}
		catch (InvocationTargetException e) {
			System.out.println(e);
		}
		System.out.println("Printing result " + result);
		//com.insightful.splus.SplusUserApp.eval(
		//com.insightful.splus.SplusDataResult result =
		System.out.println("Result from S-PLUS:");
		System.out.println(result2);
		return((String)result2);
	}
	
	static void findSplusClasses() {
		try { 
			java.net.URL url = new java.net.URL(
				"file:///C:/Program Files/Insightful/splus62/java/jre/lib/ext/Splus.jar") ;
			java.net.URL splusjars []
					= {url};
			java.net.URLClassLoader cl = 
				new java.net.URLClassLoader(splusjars); 
			splusDataResultClass = cl.loadClass(
				"com.insightful.splus.SplusDataResult");
			splusUserAppClass = cl.loadClass(
				"com.insightful.splus.SplusUserApp");
		//cl.resolveClass(com.insightful.splus.SplusUserApp.class);
			System.out.println("resolveClass called");
		//clas = defineClass( name, raw, 0, raw.length );
		}
		catch (java.net.MalformedURLException e) 
				{  System.out.println(e); }
		catch (java.lang.ClassNotFoundException e)
				{  System.out.println(e); }
	}
}