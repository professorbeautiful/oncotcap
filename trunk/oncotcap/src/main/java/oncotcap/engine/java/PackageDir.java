package oncotcap.engine.java;

import java.io.*;
import oncotcap.*;
import oncotcap.util.*;

public class PackageDir
{
	public String packagePath=null;
	public String packageName=null;
	private static final int tempDirLimit = 1000;
	private static int packageNum = 1;
	public String packageBaseName=null;

	/* Use this constructor to run an existing model externally to OncBrowser.
	 * So far, it is not used (april 2006 - rd). 
	 */
	public PackageDir(String dirName) {
		File pd = new File(dirName);
		try	{
			if(pd.exists() && pd.isDirectory()){
					File [] files = pd.listFiles();
					for (int i = 0; i<files.length; i++)
						System.out.println("PackageDir: " + 
								files[i]);
				}
				packageName = dirName;
				packagePath = new String(Oncotcap.getTempPath() + packageName);
		
			packagePath = packagePath.trim();
			if (!packagePath.endsWith(File.separator))
				packagePath = packagePath.concat(File.separator);
		}
		catch(SecurityException  e){Logger.log("Error can not create temporary directory");}
		
	}
	
	public PackageDir() throws TempDirFullException
	{
		this(true);
	}
	public PackageDir(boolean numbered) throws TempDirFullException
	{

		//this should be rewritten to take into account that multiple
		//copies of the program are running and trying to create these
		//directories on the same machine.  Possibly, a file that can
		//be locked that contains the next incremental name, if the
		//file is locked, spin on it for 10 seconds or so.
		File pd; 
		if(numbered) {
			pd = new File(nextName());
			int trys = 1;
			while(pd.exists() && trys <= tempDirLimit)
			{
				pd = new File(nextName());
				trys++;
			}
			if(trys > tempDirLimit)
				throw(new TempDirFullException());
		}
		else {
			pd = new File(standardName());
		}
		try
		{
			if(pd.exists()) {
				if(pd.isDirectory()){
					File [] files = pd.listFiles();
					for (int i = 0; i<files.length; i++)
						files[i].delete();
				}
			}
			else if (!pd.mkdir()) {
					Logger.log("PackageDir(): error can not create temporary directory  " + pd);
					packageName = null;
					packagePath = null;
			}
			packagePath = packagePath.trim();
			if (!packagePath.endsWith(File.separator))
				packagePath = packagePath.concat(File.separator);
		}
		catch(SecurityException  e){Logger.log("Error can not create temporary directory");}
	}

	private String nextName()
	{
		packageName = new String("tcapmodel" + packageNum++);
		packagePath = new String(Oncotcap.getTempPath() + packageName);
		return (packagePath);
	}
	private String standardName()
	{
		packageName = new String("tcapmodel");
		
		for (Object i : System.getProperties().keySet()){
			if(i != null){
				//System.out.println(i + " = " + System.getProperties().get(i));
			}
		}
		//  The Eclipse "environment" tab doesn't seem to add variables as advertised.
		System.setProperty("tcapmodelEclipse", "C:\\u\\My Documents\\eclipse31\\workspace\\oncotcap4\\src\\");
		
		if (System.getProperty("tcapmodelEclipse") != null)
			packageBaseName = new String(System.getProperty("tcapmodelEclipse"));
		else 
			packageBaseName = new String(Oncotcap.getTempPath());
		packagePath = packageBaseName + packageName;
		return (packagePath);
	}  
	public String getSourcePath(){
		return packageBaseName;
	}
	public String getDestPath(){
		return packageBaseName;
	}
	public String toString()
	{
		return(new String(packagePath));
	}
	
	public  String getCompileClassPath()
	{
		String compileClassPath = StringHelper.removeQuotesOS("\".") + File.pathSeparator +
								 Oncotcap.getInstallDir() + "bin" + File.pathSeparator +
								 Oncotcap.getProgramDir() + File.pathSeparator + 
								 Oncotcap.getInstallDir() + "lib" + File.separator + "tools.jar" + File.pathSeparator +
								 Oncotcap.getInstallDir() + "lib" + File.separator + "protege.jar" + File.pathSeparator +
								 FileHelper.sansTrailingSeparator(Oncotcap.getTempPath());
		 //packageBaseName;

		String xtraPath = Oncotcap.getExtraCompilerClassPath().trim();
		if(xtraPath != null && ! xtraPath.equals(""))
		{
			if(! xtraPath.startsWith(File.pathSeparator))
				compileClassPath = compileClassPath + File.pathSeparator;

			compileClassPath = compileClassPath + xtraPath;
		}
			
		String strPluginDir = Oncotcap.getInstallDir() + "plugins";
		if(FileHelper.fileExists(strPluginDir))
		{
			strPluginDir = strPluginDir + File.separator;
			
			File pluginDir = new File(strPluginDir);
			{
			   if(pluginDir.isDirectory())
			   {
			   		File [] jars = pluginDir.listFiles();
			   		if(jars != null)
			   		{
			   			for(int n = 0; n < jars.length; n++)
			   			{
			   				if(jars[n].getName().endsWith(".jar"))
			   					compileClassPath = compileClassPath + File.pathSeparator + strPluginDir + jars[n].getName();
			   			}
			   		}
			   }
			}
		}
		compileClassPath = compileClassPath + StringHelper.removeQuotesOS("\"");
		return(compileClassPath);
	}
	private String objectPath(String objName)
	{
		String fp;
		fp = new String(this.packagePath);
		fp = fp.concat(objName);
		return(fp);
	}
	public String objectSourcePath(String objName)
	{
		String fp = objectPath(objName);
		fp = fp.concat(".java");
		return(fp);
	}

}