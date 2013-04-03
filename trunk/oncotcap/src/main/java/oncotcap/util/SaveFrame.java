package oncotcap.util;

import java.io.*;
import java.awt.*;

//import Acme.JPM.Encoders.*;

public class SaveFrame
{
	public static void captureToGIF(javax.swing.JFrame frame, String fileName) throws IOException, AWTException
	{
		FileOutputStream file = new FileOutputStream(FileHelper.ensureExtension(fileName,"gif"));
//		GifEncoder gEncode = new GifEncoder((new Robot().createScreenCapture(frame.getBounds())), file);
//		gEncode.encode();
		file.close();
	}

//doesn't work for some reason... will have to use GIF for now.  GIF's
//are probably better for screen dumps anyway...
/*	public static void captureToJPG(javax.swing.JFrame frame, String fileName) throws IOException, AWTException
	{
		FileOutputStream file = new FileOutputStream(FileHelper.ensureExtension(fileName,"jpg"));
		JpegEncoder gEncode = new JpegEncoder((new Robot().createScreenCapture(frame.getBounds())), file);
		gEncode.encode();
		file.close();
	}

*/
}