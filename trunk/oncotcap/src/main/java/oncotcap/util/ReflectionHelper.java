package oncotcap.util;

import java.util.*;
import java.util.jar.*;
import java.util.zip.*;
import java.net.*;
import java.io.*;
import java.lang.reflect.*; 
import java.util.regex.*;

import oncotcap.datalayer.persistible.StatementBundle;
import oncotcap.datalayer.persistible.VariableInstance;

public class ReflectionHelper
{
	private static boolean warned1 = false;
	private static boolean warned2 = false;
	private static Hashtable packages = new Hashtable();
	
	public static void main(String [] args)
	{
		System.out.println(nameAndPackageForClass(Vector.class));
/*		Collection clss = classesInPackage("oncotcap.util");
		Collection clss = classesInPackage("horst.webwindow");
		Iterator it = clss.iterator();
		while(it.hasNext())
		{
			System.out.println(it.next());
		}
*/
	}

	/**
	 ** classesInPackage returns a list (Collection) of class names
	 ** (file system paths included) in a the given package.  The
	 ** current ClassPath is searched for all occurrences of the package
	 ** and all top level classes are returned as Strings.  No inner
	 ** classes are returned.
	 **/
	public static Collection classesInPackage(String packageName)
	{
		if(packages.contains(packageName))
			return((Vector)packages.get(packageName));
		else
		{
			ClassLoader cl = ClassLoader.getSystemClassLoader();
			if(! (cl instanceof URLClassLoader))
			{
				if( ! warned1)
				{
					Logger.log("SYSTEM WARNING: System class loader is not a URLClassLoader.\nUnable to find installed modelling APIs");
					warned1 = true;
				}
				return(null);
			}
			ClassCollection classes = new ClassCollection();
			classes.setBasePackage(packageName);
			URL [] urls = ((URLClassLoader) cl).getURLs();
			for(int i = 0; i < urls.length; i++)
			{
				if(addClasses(urls[i], packageName, classes))
					break;  //only add the first found occurrence of the package
			}
			packages.put(packageName, classes);
			return(classes);
		}
	}
	private static boolean memberOf(ZipEntry entry, String packageName)
	{
		String slashedPckgName = StringHelper.substitute(packageName, ".", "/") + "/";
		if(entry.getName().startsWith(slashedPckgName))
			if(entry.getName().indexOf('$') < 0 &&
				entry.getName().endsWith(".class") &&
  			   entry.getName().substring(slashedPckgName.length()).indexOf('/') < 0)
					return(true);
		return(false);
	}
	private static boolean addClasses(URL packagePath, String packageName, Collection classList)
	{
		String path = packagePath.toString();
		int listSize = classList.size();
		if(path.startsWith("file:"))
		{
			if(path.endsWith(".jar"))
			{
				JarFile jf;
				try{
					jf = new JarFile(packagePath.getPath());
					Enumeration ents = jf.entries();
					while(ents.hasMoreElements())
					{
						ZipEntry ze = (ZipEntry) ents.nextElement();
						if(memberOf(ze, packageName))
							classList.add(className(ze.getName()));
					}
				}
				catch(IOException e){}
			}
			else if(path.endsWith("/"))
			{
				File classDir = new File(packagePath.getPath());
					addClasses(classDir, packageName, classList);
			}
		}
		else if( ! warned2)
		{
			Logger.log("SYSTEM Warning: Network connected modelling APIs are not supported in this version.\nUnable to find all installed modelling APIs");
			warned2 = true;
		}
		if(listSize < classList.size())
			return(true);
		else
			return(false);
	}

	private static void addClasses(File packageDir, String packageName, Collection classList)
	{
		int i;
		OncFileFilter classFilter = new OncFileFilter();
		classFilter.setDir(false);
		classFilter.endsWith(".class");
		OncFileFilter dirFilter = new OncFileFilter();
		dirFilter.setDir(true);
		
		if(packageName.trim().equals(""))
		{
			String strAdd;
			File [] classes = packageDir.listFiles(classFilter);
			for(i = 0; i < classes.length; i++)
				if((strAdd = classes[i].toString()).indexOf('$') < 0)
					classList.add(className(strAdd));
		}
		else
		{
			String fpd = getFirstPackageDirName(packageName);
			dirFilter.beginsWith(fpd);
			File [] classes = packageDir.listFiles(dirFilter);
			if(classes != null && classes.length > 0)
			{
				addClasses(classes[0], removeFirstPackageDirName(packageName), classList);
			}
		}
	}
	private static String getFirstPackageDirName(String packageName)
	{
		int dotPos = packageName.indexOf('.');
		if(dotPos < 0)
			return(packageName);
		else
			return(packageName.substring(0, dotPos));
	}
	private static String removeFirstPackageDirName(String packageName)
	{
		int dotPos = packageName.indexOf('.');
		if(dotPos < 0)
			return("");
		else
			return(packageName.substring(dotPos + 1));
	}
	public static String className(String classWithPath)
	{
		int slashIndex;
		String strT = FileHelper.sansExtension(classWithPath);
		
		if((slashIndex = strT.lastIndexOf('\\')) >= 0)
			return(strT.substring(slashIndex + 1));
		else if((slashIndex = strT.lastIndexOf('/')) >= 0)
			return(strT.substring(slashIndex + 1));
		else
			return(strT);
	}
	static class ClassCollection extends Vector
	{
		String basePackage = null;
		public void setBasePackage(String basePackage)
		{
			this.basePackage = basePackage;
		}
		public boolean add(Object o)
		{
			if(basePackage != null)
				return(super.add(basePackage + "." + o.toString()));
			else
				return(super.add(o.toString()));
		}
	}

	public static Class classForName(java.lang.String className)
	{
		Class rVal = null;
		try
		{
			rVal = Class.forName(className);
			return(rVal);
		}
		catch(ClassNotFoundException e)
		{
			if(className.equals("oncotcap.datalayer.autogenpersistible.Encoding"))
				return(oncotcap.datalayer.persistible.Encoding.class);
			else if(className.equals("oncotcap.datalayer.autogenpersistible.ModelController"))
				return(oncotcap.datalayer.persistible.ModelController.class);
			else if(className.equals("oncotcap.datalayer.autogenpersistible.SubModel"))
				return(oncotcap.datalayer.persistible.SubModel.class);
			else if(className.equals("oncotcap.datalayer.autogenpersistible.SubModelGroup"))
				return(oncotcap.datalayer.persistible.SubModelGroup.class);
			else
			{
				Logger.log("Fatal Error: Cannot find class " + className + ".");
				System.out.println(e);
				return(null);
			}
		}
		catch(NoClassDefFoundError e)
		{
			Logger.log("Fatal Error: No definition found for class " + className + ".");
			return(null);
		}
	}

	private static Pattern className = Pattern.compile("[A-Za-z0-9_$]*$");
	public static String nameForClass(Class cls)
	{
		String fullName = cls.getName();
		Matcher match = className.matcher(fullName);
		if(match.find())
			return(fullName.substring(match.start()));
		else
			return(fullName);
	}

	public static String nameAndPackageForClass(Class cls)
	{
		return(cls.getName());
	}
	public static boolean classExists(String nameAndPackage)
	{
		try
		{
			ClassLoader.getSystemClassLoader().loadClass(nameAndPackage);
			return(true);
		}
		catch(ClassNotFoundException e){return(false);}
	}
	 public static Object invoke(Method method, 
										  Object onObject, 
										  Object[] withArgs) {
		  Object returnValue = null;
		  try { 
				returnValue = method.invoke(onObject, withArgs);
		  }
			catch ( NullPointerException npe) {
					Logger.log("ReflectionHelpter.invoke: Null Pointer Exception occurred while executing " + 
										 method + "in " + onObject +
										 " with arguments " + withArgs);
					npe.getCause().printStackTrace();
			}
		  catch ( InvocationTargetException ite) {
				Logger.log("ReflectionHelpter.invoke: Invocation Exception occurred while executing " + 
							  method + " in " + onObject +
							  " with arguments " + withArgs);
				//ite.printStackTrace();
				ite.getCause().printStackTrace();
		  }
		  catch (IllegalAccessException iae) {
				//iae.printStackTrace();
		  }
			catch (IllegalArgumentException iarge) {
					Logger.log("ReflectionHelpter.invoke: Illegal Argument Exception occurred while executing " + 
										 method + " in " + onObject +
										 " with arguments " + withArgs[0] );
					if ( withArgs[0] != null ) 
							Logger.log(" of type " + withArgs[0].getClass());
					//iarge.printStackTrace();
		  }
		  return returnValue;
	 }
	 public static Vector getSupers(Class subClass)
	 {
		 Vector supers = new Vector();
		 Class superClass = subClass.getSuperclass();
		 while(superClass != null)
		 {
			supers.add(superClass);
			superClass = superClass.getSuperclass();
		 }

		 return(supers);
	 }

	 public static boolean isSuper(Class subClass, Class testSuper)
	 {
	 	Class superClass = subClass.getSuperclass();
	 	if(superClass == null)
	 		return(false);
	 	else if(superClass.equals(testSuper))
	 		return(true);
	 	else
	 		return(isSuper(superClass, testSuper));	
	 }

	 public static boolean hasNullConstructor(Class classToCheck)
	 {
		 Class [] nullArgs = {};
		 try
		 {
			 Constructor cons = classToCheck.getConstructor(nullArgs);
			 if(cons != null)
				 return(true);
			 else
				 return(false);
		 }
		 catch(NoSuchMethodException e){ return(false); }
		 catch(SecurityException e) { return(false); }
	 }
	 public static boolean methodExists(String methodName, Class cls, Class [] argTypes)
	 {
	 	try{ Method meth = cls.getMethod(methodName, argTypes); }
	 	catch(NoSuchMethodException e){return(false);}
	 	catch(SecurityException e){return(false);}
	 	
	 	return(true);
	 }
	 
	 public static Vector<String> getDeclaredEnums(Class c)
	 {
		Vector<String> enums = new Vector<String>();
		for(Field f : c.getFields())
		{
			Class varType = f.getType();
			if(OncEnum.class.isAssignableFrom(varType))
				enums.add(varType.getSimpleName());
		}
		return(enums);
	 }
/*	 private static Class [] sbClass = {StatementBundle.class};
	 public static Object cloneSubstitute(Object obj, StatementBundle sb)
	 {
	 	if(obj == null)
	 		return(null);
	 	
		Object newUserObj = obj;
		if(ReflectionHelper.methodExists("cloneSubstitute", obj.getClass(), sbClass))
		{
			Object [] sbArray = {sb};
			try{newUserObj = obj.getClass().getMethod("cloneSubstitute", sbClass).invoke(obj, sbArray);}
			catch(NoSuchMethodException e){Logger.log("WARNING: No such method exception while doing cloneSubstitute in ReflectionHelper.");}
			catch(IllegalAccessException e){Logger.log("WARNING: Illegal access exception while doing cloneSubstitute in ReflectionHelper.");}
			catch(InvocationTargetException e){Logger.log("WARNING: Invocation target exception while doing cloneSubstitute in ReflectionHelper.");}
		}
		else if(obj instanceof String)
			newUserObj = sb.substitute((String) obj);
		return(newUserObj);
	 }*/
}

