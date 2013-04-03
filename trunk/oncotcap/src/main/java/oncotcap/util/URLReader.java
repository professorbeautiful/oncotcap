package oncotcap.util;

import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;

public class URLReader extends InputStreamReader
{
	public URLReader(URL url) throws IOException
	{
		super(url.openStream());
		Logger.log("URL opened");
	}
	public URLReader(String url) throws MalformedURLException, IOException
	{
		super(new URL(url).openStream());
	}

/*	public int read() throws IOException
	{
		int i = super.read();
		Logger.logn((char) i);
		return(i);
	}
	public int read(char[] cbuf) throws IOException
	{
		int i = super.read(cbuf);
		Logger.logn(cbuf);
		return(i);
	}
	public int read(char[] cbuf, int off, int len) throws IOException
	{
		int i = super.read(cbuf, off, len);
		Logger.logn(cbuf, off, len);
		return(i);
	}
*/
}