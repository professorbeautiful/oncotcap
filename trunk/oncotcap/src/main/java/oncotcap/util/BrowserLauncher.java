package oncotcap.util;
import java.io.*;
import java.net.URL;

import oncotcap.Oncotcap;
public class BrowserLauncher
{
	public static void launch(String url)
	{
		String turl;
		char data[] = new char[5120];
		Logger.log(url.trim().substring(0,3));
		if(url.trim().substring(0,7).equalsIgnoreCase("tcap://"))
		{
			turl = url.trim().substring(7);
			turl = "file:///" + oncotcap.Oncotcap.getInstallDir() + "TcapData" + File.separator + "resource_files" + File.separator + turl.trim();
			try{
				OncMessageBox.showMessageDialog(oncotcap.Oncotcap.getMainFrame(), new java.net.URL(turl), "", javax.swing.JOptionPane.INFORMATION_MESSAGE);}
			catch(java.net.MalformedURLException e){Logger.log("WARNING: Malformed URL.  Cannot open " + turl);}
		}
		else
		{
			if(url.trim().substring(0,8).equalsIgnoreCase("file:///"))
			{
				turl = url.trim().substring(8);
				turl = "file:///" + oncotcap.Oncotcap.getInstallDir() + "TcapData" + File.separator + "resource_files" + File.separator + turl.trim();
			}
			else if(url.trim().substring(0,7).equalsIgnoreCase("http://"))
			{
				turl = url.trim();
			}
			else if(url.trim().substring(0,11).equalsIgnoreCase("resource://"))
			{
				turl = StringHelper.substitute(url.trim().substring(11), "/", "\\");
			}
			else
			{
				turl = "file:///" + oncotcap.Oncotcap.getInstallDir() + "TcapData" + File.separator + "resource_files" + File.separator + url.trim();
			}
			try
			{
				BrowserLauncher2.openURL(StringHelper.substitute(turl, "%20", " "));
				
			}
			catch (java.net.MalformedURLException ex) {Logger.log("Error displaying browser, MalformedURL " + turl + "\n" + ex);}
			catch(java.io.IOException ex){ Logger.log("Error displaying browser\n" + ex); }
		}
	}
}