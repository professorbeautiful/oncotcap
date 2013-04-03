package oncotcap.util;

import java.awt.Color;

public class TcapColor
{
	public static final void main(String [] args)
	{
		try
		{
			Color c = getColor("CCDG");
			System.out.println(c.getRed() + " " + c.getGreen() + " " + c.getBlue());
		}
		catch(NumberFormatException e){System.out.println("Format Exception");}
	}
	public static final Color lightBlue = new Color(186,241,244);
	public static final Color darkBrown = new Color(70,40,10);
	public static final Color lightBrown = new Color(200,191,182);
	public static final Color darkGreen = new Color(40,90,10);
	public static final Color lightGreen = new Color(148,173,133);
	public static final Color pastelBlue = new Color(156,158,206);
	public static final Color lightGray = new Color(206,207,206);
	public static final Color guiGray = new Color(204,204,204);
	public static final Color orange = new Color(255,109,11);

	//the following colors are directly from the HTML spec
	public static final Color black = new Color(0,0,0);
	public static final Color silver = new Color(192,192,192);
	public static final Color gray = new Color(128,128,128);
	public static final Color white = new Color(255,255,255);
	public static final Color maroon = new Color(128,0,0);
	public static final Color red = new Color(255,0,0);
	public static final Color purple = new Color(128,0,128);
	public static final Color fuchsia = new Color(255,0,255);
	public static final Color green = new Color(0,128,0);
	public static final Color lime = new Color(0,255,0);
	public static final Color olive = new Color(128,128,0);
	public static final Color yellow = new Color(255,255,0);
	public static final Color navy = new Color(0,0,128);
	public static final Color blue = new Color(0,0,255);
	public static final Color teal = new Color(0,128,128);
	public static final Color aqua = new Color(0,255,255);
	public static final Color mediumBlue = new Color(206,206,255);

	private static final String strBlack = "black";
	private static final String strSilver = "silver";
	private static final String strGray = "gray";
	private static final String strWhite = "white";
	private static final String strMaroon = "maroon";
	private static final String strRed = "red";
	private static final String strPurple = "purple";
	private static final String strFuchsia = "fuchsia";
	private static final String strGreen = "green";
	private static final String strLime = "lime";
	private static final String strOlive = "olive";
	private static final String strYellow = "yellow";
	private static final String strNavy = "navy";
	private static final String strBlue = "blue";
	private static final String strTeal = "teal";
	private static final String strAqua = "aqua";
	
	public static Color getColor(String color) throws NumberFormatException
	{
		String tcolor = new String(color.trim());
		if(tcolor.startsWith("#"))
			return(getHexColor(tcolor));
		else if(strBlack.equalsIgnoreCase(tcolor))
			return(black);
		else if(strSilver.equalsIgnoreCase(tcolor))
			return(silver);
		else if(strGray.equalsIgnoreCase(tcolor))
			return(gray);
		else if(strWhite.equalsIgnoreCase(tcolor))
			return(white);
		else if(strMaroon.equalsIgnoreCase(tcolor))
			return(maroon);
		else if(strRed.equalsIgnoreCase(tcolor))
			return(red);
		else if(strPurple.equalsIgnoreCase(tcolor))
			return(purple);
		else if(strFuchsia.equalsIgnoreCase(tcolor))
			return(fuchsia);
		else if(strGreen.equalsIgnoreCase(tcolor))
			return(green);
		else if(strLime.equalsIgnoreCase(tcolor))
			return(lime);
		else if(strOlive.equalsIgnoreCase(tcolor))
			return(olive);
		else if(strYellow.equalsIgnoreCase(tcolor))
			return(yellow);
		else if(strNavy.equalsIgnoreCase(tcolor))
			return(navy);
		else if(strBlue.equalsIgnoreCase(tcolor))
			return(blue);
		else if(strTeal.equalsIgnoreCase(tcolor))
			return(teal);
		else if(strAqua.equalsIgnoreCase(tcolor))
			return(aqua);
		else
			return(getHexColor(tcolor));
	}
	private static Color getHexColor(String tcolor) throws NumberFormatException
	{
		if(tcolor.startsWith("#"))
			tcolor = tcolor.substring(1);
		if(tcolor.length() > 6)
			throw(new NumberFormatException());
		if(tcolor.length() < 6)
		{
			int tlen = tcolor.length();
			for(int n = tlen + 1; n <=6; n++)
				tcolor = "0" + tcolor;
		}
		int intRed = Integer.parseInt(tcolor.substring(0,2),16);
		int intGreen = Integer.parseInt(tcolor.substring(2,4),16);
		int intBlue = Integer.parseInt(tcolor.substring(4,6),16);
		return(new Color(intRed, intGreen, intBlue));
	}
}