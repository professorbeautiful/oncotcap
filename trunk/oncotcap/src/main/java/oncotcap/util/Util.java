package oncotcap.util;

import java.lang.reflect.Array;
import java.awt.Color;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

import oncotcap.process.OncCollection;
import oncotcap.process.OncProcess;
import oncotcap.sim.schedule.MasterScheduler;

public class Util {
	private static javax.swing.JOptionPane jop = new javax.swing.JOptionPane("",javax.swing.JOptionPane.WARNING_MESSAGE);
	public static void main(String[] args)
	{
/*		if(getOS() == WINDOWSOS)
			System.out.println("IT IS WINDOWS!");
		else
			System.out.println("IT IS NOT WINDOWS!");
*/		
		System.getProperties().list(System.out);
//		System.out.println("*" + roundToString(-20.0,0) + "*");
//		System.out.println("*" + roundToString(20000.12345,1) + "*");
	}
	public static void fatal(String error) {
		Logger.log(error);
		exitProgram();
	}
	public static void exitProgram() { 
		Logger.log("All done!");
		System.exit(0); 
	}
	public static void log(Loggable me, Object s){
		if ( me.verbose() > 0)
			Logger.log("LOG: " + me.logName() + ": " + s);
	}
	public static void log(Loggable me, int verbose, Object s){
		if ( me.verbose() >= verbose)
			Logger.log("LOG: " + me.logName() + ": " + s);
	}
	public static void mark(String s){Logger.log("MARK " + s);}
	public static Object[] newArray(String className, int n){
		try {
			return((Object[])Array.newInstance(Class.forName(className), n));
		} catch(ClassNotFoundException e){
			Logger.log("Class " + className + " not found" + e);
			return(null);
		}
	}
	public static Color lighter(Color c, int howmuch){
		int newRed = 255 - (255-c.getRed())*howmuch/10;
		int newGreen = 255 - (255-c.getGreen())*howmuch/10;
		int newBlue = 255 - (255-c.getBlue())*howmuch/10;
		return (new Color(newRed, newGreen, newBlue));
	}

/*	public static Object findParent(Object child, Class parentType)
	{
		Object nextObj;
		OncProcess oobj;
		OncCollection ocol;
		
		if ( !(child instanceof OncProcess) && !(child instanceof OncCollection))
			return(null);

		if (parentType == null)
			return(null);

		nextObj = child;

		while(nextObj != null)
		{
			if(nextObj instanceof OncProcess)
			{
				oobj = (OncProcess) nextObj;
				if (oobj == null)
					return(null);
				
 				if ( oobj.oncParent.getClass().equals(parentType))
					return(oobj.oncParent);
				else
					nextObj = oobj.oncParent;
			}
			else if(nextObj instanceof OncCollection)
			{
				ocol = (OncCollection) nextObj;

				if (ocol == null)
					return(null);
				
				if (ocol.oncParent.getClass().equals(parentType))
					return(ocol.oncParent);
				else
					nextObj = ocol.oncParent;
			}
			else
				nextObj = null;
		}

		return(null);
		
	}
*/
	public static void warningMessage(String message)
	{
		JOptionPane.showMessageDialog(null,message,"WARNING",javax.swing.JOptionPane.WARNING_MESSAGE);
	}

	public static int yesNoQuestion(String question)
	{
		return(JOptionPane.showConfirmDialog(SwingUtil.getModeFrame(), question, "Information", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE));
	}
	public static ImageIcon getIcon(String iconPath) {
		ImageIcon icon = null;
		URL url = Util.class.getResource("../resource/image/" + iconPath);
		if (url != null) {
			icon = new ImageIcon(url);
			if (icon.getIconWidth() == -1) {
				Logger.log("ERROR:  failed to load icon " + iconPath);
			}
		}
		return icon;
	}

	public static void setFontSize( JComponent comp, int newSize) {
		comp.setFont(comp.getFont().deriveFont((float)newSize));
	}
	public static void setFontSize( JComponent comp, double newSize) {
		comp.setFont(comp.getFont().deriveFont((float)newSize));
	}
	public static void setFontSize( JComponent comp, float newSize) {
		comp.setFont(comp.getFont().deriveFont(newSize));
	}

	public static double roundToDouble(double d, int places){
		int i;
		if(d >= 0)
			i = (int) (d*Math.pow(10.0,places)+0.5);
		else
			i = (int) (d*Math.pow(10.0,places)-0.5);
		
		return (double)(i)/Math.pow(10.0,places);
	}
	public static String roundToString(double d, int places) {
		String s = "" + (roundToDouble(d, places));
//		if (d >= 0 && places > 0 && places+3 < s.length())
//			s = s.substring(0,places+3);
//		else if (d < 0 && places > 0 && places+4 < s.length())
//			s = s.substring(0,places+4);
		return ( s);
	}

	public static void printHTMLFile(String fileAndPath)
	{
		try{Runtime.getRuntime().exec(oncotcap.Oncotcap.getPrintCommand().concat(" ").concat(fileAndPath));
		}catch(java.io.IOException e){Logger.log("ERROR printing " + fileAndPath + "\n" + e);}

	}
	public static double log10(double x)
	{
		 if(x < 1e-69)
				x = 1e-69;
		 return(Math.log(x)/Math.log(10.0));
	}

	public static final int WINDOWSOS = 1;
	public static final int OTHEROS = 2;
	public static Integer osType = null;
	public static int getOS()
	{
		if(osType == null)
		{
			String os = System.getProperty("os.name","NOTWINDOWS").trim();
			if(os.length() >= 7 && os.substring(0,7).equalsIgnoreCase("windows"))
				osType = new Integer(WINDOWSOS);
			else
				osType = new Integer(OTHEROS);
		}
		return(osType.intValue());
	}
}