package oncotcap.util;

import oncotcap.Oncotcap;

public class TempDirFullException extends Exception
{

	public TempDirFullException(){}

	public String toString()
	{
		return(new String("The directory " + Oncotcap.getTempPath() + " contains a maximum number of directories."));
	}

}