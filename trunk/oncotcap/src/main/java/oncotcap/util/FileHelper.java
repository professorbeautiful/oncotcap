package oncotcap.util;

import java.io.*;

public class FileHelper
{
	public static File openFileForWrite(String fileName)
	{
		return(openFileForWrite(fileName,false));
	}
	public static File openFileForWrite(String fileName, boolean verbose)
	{
		File outputFile = new File(fileName);

		if(outputFile.exists())
			if(!outputFile.delete())
			{
				if (verbose) Logger.log("ERROR: Unable to delete " + fileName);
				return(null);
			}

		try
		{
			outputFile.createNewFile();
		}
		catch(IOException e){if (verbose) Logger.log("ERROR: Unable to create file " + fileName + ". IOException"); return(null);}
		catch(SecurityException e){if (verbose) Logger.log("ERROR: Unable to create file " + fileName + ". Access denied"); return(null);}

		if(!outputFile.canWrite())
		{
			if (verbose) Logger.log("ERROR: Unable to write to file " + fileName + ".");
			return(null);
		}

		return(outputFile);
	}

	public static boolean fileExists(String fileName)
	{
		if(fileName != null)
		{
			File f = new File(fileName);
			return(f.exists());
		}
		else
			return(false);
	}

	public static String ensureTrailingSeparator(String pathName)
	{
		if(pathName.trim().endsWith(File.separator))
			return(pathName);
		else
			return(pathName.trim() + File.separator);
	}
	public static String sansTrailingSeparator(String pathName)
	{
		if(pathName.trim().endsWith(File.separator))
			return(pathName.trim().substring(0,pathName.trim().length() - 1 ));
		else
			return(pathName);

	}
	public static String ensureExtension(String fileName, String extension)
	{
		if(! (fileName.toUpperCase().endsWith("." + extension.toUpperCase())))
			fileName = fileName + "." + extension;

		return(fileName);
	}
	public static String sansExtension(String fileName)
	{
		String fn = new String(fileName);
		int dotPos = fn.lastIndexOf('.');
		if(dotPos > 0)
			return(fn.substring(0, dotPos));
		else
			return(fn);
	}
	public static String getDirectory(String pathAndFile)
	{
		String rVal = new String(pathAndFile);
		int forwardSlashPos = rVal.lastIndexOf("/");
		int backSlashPos = rVal.lastIndexOf("\\");
		int slashPos = Math.max(forwardSlashPos, backSlashPos);
		if(slashPos > 0)
			return(rVal.substring(0, slashPos));
		else
			return("./");
	}
	public static String readLine(FileInputStream in) throws IOException
	{
		int c;
		String ret = new String();
		while( in.available() > 0 && ((c = in.read()) != '\n' || c == 13))
		{
			ret = ret + String.valueOf((char) c);
		}
		return(ret);	
	}
	public static boolean eof(FileInputStream in) throws IOException
	{
		if (in.available() > 0 )
			return(false);
		else
			return(true);	
	}
	public static String readFile(FileInputStream in) throws IOException
	{
		String ret = new String();
		while(in.available() > 0)
			ret = ret + String.valueOf((char) in.read());

		return(ret);
	}
}