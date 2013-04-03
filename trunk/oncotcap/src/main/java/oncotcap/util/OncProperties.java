package oncotcap.util;

import java.io.*;
import java.lang.reflect.*;

public class OncProperties 
{
	public String installDir = null;
	public String programDir = null;
	public String javaExe = null;
	public String startingModel = null;
	public String logging = null;
	public String dataSource = null;
	
	public OncProperties()
	{

	}
	public OncProperties(String propertyFile)
	{
		parseFile(propertyFile);	
	}
	
	public static void main(String [] args)
	{
		OncProperties op = new OncProperties("C:\\test.props");
		Logger.log("Install Dir = " + op.getInstallDir());
		Logger.log("Program Dir = " + op.getProgramDir());	
	}
	
	private void parseFile(String fName)
	{
		String line;
		try
		{
			FileInputStream fRead = new FileInputStream(fName);
			while(! FileHelper.eof(fRead))
			{
				line = FileHelper.readLine(fRead);
				checkLine(line);
			}
		} 
		catch(FileNotFoundException e){Logger.log("Property file " + fName + " not found.");}
		catch(IOException e){Logger.log("IO Exception reading " + fName);}
	}
	private void checkLine(String line)
	{
		grabProperty(line, "installDir");
		grabProperty(line, "programDir");
		grabProperty(line, "javaExe");
		grabProperty(line, "startingModel");
		grabProperty(line, "logging");
		grabProperty(line, "dataSource");
	}
	private void grabProperty(String line, String propName)
	{
		try
		{
			String lineLower = line.toLowerCase().trim();
			if(lineLower.startsWith("[" + propName.toLowerCase().trim() + "]"))
			{
				Field prop = this.getClass().getField(propName);
				prop.set(this,line.trim().substring(propName.length() + 2).trim());
			}
		}
		catch(NoSuchFieldException e){Logger.log("WARNING: Trying to set property " + propName + " which does not exist in OncProperties.");}
		catch(IllegalAccessException e){Logger.log("WARNING: Can not access field " + propName + " in OncProperties.");}
	}
	
	public String getInstallDir()
	{
		return(installDir);
	}
	public String getProgramDir()
	{
		return(programDir);
	}
	public String getJavaExe()
	{
		return(javaExe);
	}
	public String getStartingModel()
	{
		return(startingModel);
	}
	public String getDataSourcePath()
	{
		return(dataSource);
	}
/*	public boolean getLogToFile()
	{
		if(logFile == null)
			return(false);
		else
			return(true);
	}
*/
}
