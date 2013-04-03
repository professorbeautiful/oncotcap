package oncotcap.util;

import java.io.*;

class OncFileFilter implements FileFilter
{
	private boolean directory = false;
	private String strEnd = null;
	private String strBegin = null;
	
	public boolean accept(File pathname)
	{
		if(pathname.isDirectory() != directory)
			return(false);
		if(strEnd != null)
			if(! pathname.getName().endsWith(strEnd))
				return(false);

		if(strBegin != null)
			if(! pathname.getName().startsWith(strBegin))
				return(false);
		
		return(true);
	}

	public void endsWith(String strEnd)
	{
		this.strEnd = strEnd;
	}
	public void setDir(boolean directory)
	{
		this.directory = directory;
	}
	public void beginsWith(String strBegin)
	{
		this.strBegin = strBegin;
	}
}