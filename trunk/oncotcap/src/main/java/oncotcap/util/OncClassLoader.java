package oncotcap.util;

import javax.swing.JOptionPane;
import java.net.*;

public class OncClassLoader extends URLClassLoader
{

	private static URL [] getJarPaths()
	{
		String [] rVal = {null, null, null, null};
		URL [] rURLVal = {null, null, null, null};
		try
		{
			
			rVal[0] = new String("file://" + oncotcap.Oncotcap.getProgramDir());
			rVal[1] = new String("file://" + oncotcap.Oncotcap.getInstallDir() + "oncotcap.jar");
			rVal[2] = new String("file://" + oncotcap.Oncotcap.getInstallDir() + "tools.jar");
			rVal[3] = new String("file://" + oncotcap.Oncotcap.getTempPath());
			rURLVal[0] = new URL(rVal[0]);
			rURLVal[1] = new URL(rVal[1]);
			rURLVal[2] = new URL(rVal[2]);
			rURLVal[3] = new URL(rVal[3]);
		}
		catch(MalformedURLException e){Logger.log("Malformed url while constructing OncClassLoader.");}
		return(rURLVal);
	}
	public OncClassLoader(ClassLoader parent)
	{
		super(getJarPaths(), parent);
//		super(getClass().getClassLoader());
	}
/*	public Class loadClass(String classname) throws ClassNotFoundException
	{
		if(!didOnce)
		{
			Package [] pkgs;
			pkgs = getPackages();
			for(int n = 0; n< pkgs.length; n++)
				System.out.println( pkgs[n] );
			didOnce = true;
		}
		return(super.loadClass(classname));
	}*/
	static boolean didOnce = false;
	public Object getObject(String classname)
	{
			Class cret = null;
			Object robj = null;
			try
			{
					cret = loadClass(classname);
					robj = cret.newInstance();
			}
			catch(java.lang.NoClassDefFoundError e)
			{
					warnUser("java.lang.NoClassDefFoundError in OncClassLoader: " + classname + " " + e);
					return(null);
			}
			catch(ClassNotFoundException e)
			{
				warnUser("Class Not Found Exception in OncClassLoader: " + classname + " " + e);
				return(null);
			}
			catch(IllegalAccessException e)
			{
					warnUser("Illegal Access Exception in OncClassLoader: " + classname + " " + e);
					return(null);
			}
			catch(InstantiationException e)
			{
					warnUser("Instantiation Exception in OncClassLoader: " + classname + " " + e);
					return(null);
			}

			return(robj);

	}
	void warnUser(String errormessage){
		Logger.log(errormessage);
			JOptionPane.showMessageDialog(
									  null,
									  "There was a compiler error!\n"
									  + errormessage,
									  "Compiler error",
									  JOptionPane.ERROR_MESSAGE);
	}
}
