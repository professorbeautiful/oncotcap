package oncotcap.util;

import java.io.*;
import java.util.*;

public class Logger 
{
	private static long referenceTime = 0L;
	private static boolean logToConsole = true;
	private static boolean logToFile = false;
	private static String logFileName = null;
	private static File logFile = null;
	private static FileWriter fw = null;
	private static boolean cannotWriteWarn = false;
	private static boolean suppressConsoleLogging = false;
	
	public static void resetReferenceTime(){
		referenceTime = System.currentTimeMillis();
	}
	public static void logTheTime(String s) {
		logTheTime();
		log(s);
	}
	public static void logTheTime() {
		long delta = System.currentTimeMillis() - referenceTime;
		logn(delta + ": ");
	}
	public static void log(Object message)
	{
		logn(message + "\n");
	}
	public static void log(String message)
	{
		logn(message + "\n");
	}
	public static void log(int message)
	{
		logn((new Integer(message).toString()) + "\n");
	}
	public static void log(double message)
	{
		logn((new Double(message).toString()) + "\n");
	}
	public static void log(float message)
	{
		logn((new Float(message).toString()) + "\n");
	}
	public static void log(boolean message)
	{
		logn((new Boolean(message).toString()) + "\n");
	}
	public static void log(long message)
	{
		logn((new Long(message).toString()) + "\n");
	}
	public static void log(char message)
	{
		char [] ch = new char[1];
		ch[0] = message;
		logn((new String(ch)) + "\n");
	}
	public static void log(char [] message)
	{
		logn(new String(message) + "\n");
	}
	public static void log(char[] message, int off, int len)
	{
		logn(new String(message, off, len) + "\n");
	}
	public static void log(char[] message, int pos)
	{
		logn(new String(message, 0, pos) + "\n");
	}
	public static void logn(char message)
	{
		char [] ch = new char[1];
		ch[0] = message;
		logn(new String(ch));
	}
	public static void logn(char [] message)
	{
		logn(new String(message));
	}
	public static void logn(char[] message, int off, int len)
	{
		logn(new String(message, off, len));
	}
	public static void logn(char[] message, int pos)
	{
		logn(new String(message, 0, pos));
	}
	public static void logn(Object message)
	{
		String wMessage;
		
		if(message == null)
			wMessage = "null";
		else
			wMessage = message.toString();

		if(logToConsole)
			System.out.print(wMessage);
		if(logToFile)
		{
			try{fw.write(wMessage);}
			catch(IOException e){if (!cannotWriteWarn){OncMessageBox.showMessageDialog(null,
				"ERROR: Cannot write to log file " + logFileName + " " + e,
				"Log file error",
				OncMessageBox.ERROR_MESSAGE);
				cannotWriteWarn = true;}}
		}
	}

	public static void setConsoleLogging(boolean logit)
	{
		if(! suppressConsoleLogging)
			logToConsole = logit;
	}
	public static void suppressConsoleLogging()
	{
		logToConsole = false;
		suppressConsoleLogging = true;
	}
	public static void setLogFile(String filename)
	{
		if(fw != null)
		{
			if(!closeLogFile())
			{
				return;
			}
		}
		try{logFile = new File(filename);}
		catch(NullPointerException e){log("Warning: Cannot open logfile " + filename);}
		if(logFile != null)
		{
			try{fw = new FileWriter(logFile);}
			catch(IOException e){log("ERROR: Unable to open log file " + filename + " for writing." + e);}
			logToFile = true;
			logFileName = filename;
			log(new Date(System.currentTimeMillis()));
		}
	}
	public static boolean closeLogFile()
	{
		if(fw != null)
		{
			try{fw.close();}
			catch(IOException e){log("ERROR: Cannot close log file " + logFileName + " " + e); return(false); }
		}
		logFile = null;
		fw = null;
		return(true);
	}
}
