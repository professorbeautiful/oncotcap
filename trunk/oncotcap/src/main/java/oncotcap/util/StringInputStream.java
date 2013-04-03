package oncotcap.util;

import java.io.*;

public class StringInputStream extends InputStream
{
	StringReader reader;
	public StringInputStream(String data)
	{
		reader = new StringReader(data);
	}
	public int read() throws IOException
	{
		return(reader.read());
	}
}