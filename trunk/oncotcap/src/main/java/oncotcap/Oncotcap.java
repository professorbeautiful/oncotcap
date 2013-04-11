package oncotcap;

import java.io.*;
import java.util.*;
import oncotcap.util.*;
import javax.swing.JFrame;
import oncotcap.datalayer.*;

public class Oncotcap
{
	private static final String defaultModel = "Brufomycin Scenario";
	private static boolean singleDataSourceMode = true;

	private static String startingModel = null;
	private static String tempPath = null;
	private static String classPath = null;
	private static String installDir = null;
	private static String programDir = null;
	private static String resourcePath = null;
	private static String installRoot = null;
	private static OncProperties properties = null;
	private static String javaExe = null;
	private static JFrame mainFrame = null;
	private static String printCmd = null;
	private static String outputDir = null;
	private static String userHomePath = null;
	private static boolean gbinomiOn = true;
	private static OncoTCapDataSource tcapDataSource = null;
	private static String dataSourcePath = null;
	private static String starterProcess = null;
	private static String outputScreen = null;
	private static boolean debugMode = false;
	private static String extraClassPath = "";
	private static Date buildDate = new Date("2/26/2008");
	private static String tempCompileDir = null;
	
	public static int modelTimeUnits = OncTime.MONTHS;
	/**	These are the packages that are searched for Abstract level
	 *	OncObjects.
	 */
	public static final String[] tcapPackages = {"oncotcap","oncotcap.process.cells","oncotcap.process.treatment"};

	//do this to set up all properties

	public static void main(String [] args)
	{
//		System.getProperties().list(System.out);
		//System.out.println(System.getProperty("java.version"));
		//Logger.log(javax.swing.UIManager.getLookAndFeel());
		//Oncotcap.initSim();
		//handleCommandLine(args);
		//System.out.println("INSTALL DIR: " + installDir + "\nOUTPUT DIR: " + outputDir);
		//System.out.println(getUserHomePath());
		//System.out.println(getInstallDir());
		oncotcap.display.browser.OncBrowser.main(args);
	}

	static{
		readProperties();
	}
	public Oncotcap()
	{
		initSim();
	}
	private static void initSim()
	{
/*		if(!setClassPaths())
			Util.exitSim();

		if(!setTempPath())
			Util.exitSim();
			
		if(!checkPaths())
			Util.exitSim();
	*/	
		Logger.log(tempPath);

	}
	public static void setGbinomiOn(boolean setting)
	{
		gbinomiOn = setting;
	}
	public static boolean getGbinomiOn()
	{
		return(gbinomiOn);
	}
	public static void setMainFrame(JFrame frm)
	{
		mainFrame = frm;
	}
	public static JFrame getMainFrame()
	{
		return(mainFrame);
	}
	public static void handleCommandLine(String [] args)
	{
		System.out.println("In handle command line");
		
		int n;
		for(n = 0; n < args.length; n++)
		{
			System.out.println("ARG:" + n + " " + args[n]);
			if(args[n].toLowerCase().startsWith("-i"))
			{
				setInstallDir(getNextArg(args, n));
				System.out.println("Found -i, installDir set to " + installDir);
			}
			else if(args[n].toLowerCase().startsWith("-m"))
			{
				startingModel = getNextArg(args,n);
			}
			else if(args[n].toLowerCase().startsWith("-r"))
			{
				properties = new OncProperties(getNextArg(args,n));	
			}
			else if(args[n].toLowerCase().startsWith("-p"))
			{
				setProgramDir(getNextArg(args,n));	
			}
			else if(args[n].toLowerCase().startsWith("-os"))
			{
				setOutputScreen(getNextArg(args,n));
			}
			else if(args[n].toLowerCase().startsWith("-o"))
			{
				setOutputDir(getNextArg(args,n));
			}
			else if(args[n].toLowerCase().startsWith("-tp"))
			{
				setTempPath(getNextArg(args,n));
			}
			else if(args[n].toLowerCase().startsWith("-ccp"))
			{
				setExtraCompilerClassPath(getNextArg(args,n));
			}
			else if(args[n].toLowerCase().startsWith("-debug"))
			{
				setDebugMode(true);
			}
			else if(args[n].toLowerCase().startsWith("-d"))
			{
				setDataSource(getNextArg(args,n));
			}
			else if(args[n].toLowerCase().startsWith("-sp"))
			{
				setStarterProcess(getNextArg(args,n));
			}
			else if(args[n].toLowerCase().startsWith("-s"))
			{
				Logger.suppressConsoleLogging();
			}
		}
	}

	public static void readProperties()
	{
		
		if (properties == null)
		{
			properties = new OncProperties();
			String propFile = SystemUtil.getEnv("TCAPPROP");
			if(propFile != null)
			{
				if (FileHelper.fileExists(propFile))
				{
					Logger.log("Using property file: " + propFile);
					properties = new OncProperties(propFile);
				}
				else
				{
					Logger.log("Warning: Property file " + propFile + " specified in TCAPPROP environment variable, but doesn't exist.");
					properties = new OncProperties();
				}
			}
		}

		setInstallDir(properties.getInstallDir());
		if(installDir != null)
		{
			if(programDir == null)
			{
				if((programDir = properties.getProgramDir()) == null)
				{
					setProgramDir(getInstallDir());
				}				
			}
			if(tcapDataSource == null)
			{
				if(properties == null || properties.getDataSourcePath() == null)
				{
					setDataSource(getInstallDir() + File.separator + "TcapData" + File.separator + "oncotcap.pprj");
				}
				else
					setDataSource(properties.getDataSourcePath());
			}
			if(javaExe == null)
			{
				if((javaExe = properties.getJavaExe()) == null)
				{
					javaExe = FileHelper.ensureTrailingSeparator(System.getProperty("java.home")) + "bin" + File.separator + "java";
	//				javaExe = getInstallDir() + "jre" + File.separator + "bin" + File.separator + "java";
	//				javaExe = getInstallDir() + "runSim.bat";
	//				javaExe = getInstallRoot() + "runSim.bat";
				}
			}
		}
		if(startingModel == null)
			startingModel = defaultModel;

	}
	private static String noQuotes(String inStr)
	{
		String outStr = new String(inStr.trim());
		if(outStr.startsWith("\""))
		   outStr = outStr.substring(1);
		if(outStr.endsWith("\""))
		   outStr = outStr.substring(0, outStr.length() - 1);

		return(outStr);
	}
	private static String getNextArg(String [] args, int currentIndex)
	{
//		if(args[currentIndex].length() > 2)
//			return(noQuotes(args[currentIndex].substring(2,args[currentIndex].length())));
		if (currentIndex + 1 == args.length)
			return("");
		else
			return(noQuotes(args[currentIndex + 1]));
	}
	public static void setInstallDir(String dir)
	{
		if(dir != null)
		{
			if(!dir.trim().endsWith(File.separator))
				installDir = dir.trim() + File.separator;
			else
				installDir = dir.trim();
		}
	}
	private static String calculatedInstallDir = null;
	public static String getInstallDir()
	{
		if(installDir == null)
		{
			if(calculatedInstallDir == null)
			{
				String cwd = System.getProperty("user.dir");
				String iDir = cwd;
				File id = new File(iDir);
				if(id.exists())
				{
					calculatedInstallDir = iDir + File.separator;
				}
				else
					calculatedInstallDir = "X";
			}
			if(calculatedInstallDir.equals("X"))
			{
				Logger.log("\nERROR: installation directory not found\nProvide a -i command line switch or a properties file in environment variable TCAPPROP\n");
				System.exit(0);
			}
			else
				return(calculatedInstallDir);
		}
		return(installDir);
	}
	public static OncoTCapDataSource getDataSource()
	{
		if(tcapDataSource == null)
		{
			if(dataSourcePath == null)
				dataSourcePath = getInstallDir() + "TcapData" + File.separator + "oncotcap.pprj";
			setDataSource(new ProtegeDataSource(dataSourcePath));
		}
		if ( tcapDataSource == null ) {
			System.out.println("Unable to open OncoTCap project");
			System.exit(0);
		}	
		return(tcapDataSource);
	}
	public static void setDataSource(String path)
	{
		if(path != null)
		{
			String tPath = path.trim();
			if(! tPath.equals(""))
			{
				if( ! (tPath.startsWith("\\") || tPath.startsWith("/") || tPath.indexOf(':') == 2))
				{
					tPath = getInstallDir() + "TcapData" + File.separator + tPath;
				}
				Logger.log("Opening: " + tPath);
				dataSourcePath = tPath;
			}
		}
		
	}
	public static void setDataSource(OncoTCapDataSource dSource)
	{
		tcapDataSource = dSource;
		DataSourceStatus.setDataSource(dSource);
	}
	public static String getInstallRoot()
	{
		if (installRoot == null)
		{
			int slash;
			if((slash = installDir.indexOf(File.separator)) >= 0)
				installRoot = installDir.substring(0,slash+1);
		}
		return(installRoot);
	}
	public static void setProgramDir(String dir)
	{
		if(dir.trim().endsWith(".jar"))
			programDir = dir.trim();
		else if(!dir.trim().endsWith(File.separator))
			programDir = dir.trim() + File.separator;
		else
			programDir = dir;
	}
	public static String getProgramDir()
	{
		if(programDir == null)
			return("." + File.separator);
		else
			return(programDir);
	}
	public static void setOutputDir(String dir)
	{
		if(!dir.trim().endsWith(File.separator))
			outputDir = dir.trim() + File.separator;
		else
			outputDir = dir;
	}
	public static String getExtraCompilerClassPath()
	{
		return(extraClassPath);
	}
	public static String getOutputDir()
	{
		if(outputDir == null)
			return("." + File.separator);
		else
			return(outputDir);
	}
	public static String getJavaExe()
	{
		return(javaExe);
	}
	public static String getResourcePath()
	{
		if(resourcePath == null)
			resourcePath = getInstallDir() + "TcapData" + File.separator + "resourse_files" + File.separator;
		return(resourcePath);
	}
	public static String getStartingModel()
	{
		if(startingModel == null)
			if((startingModel = properties.getInstallDir()) == null)
				return(defaultModel);	

		return(startingModel);
	}
	public static void setExtraCompilerClassPath(String path)
	{
		extraClassPath = path;
	}

	public static void setStarterProcess(String process)
	{
		starterProcess = process;
	}
	public static String getStarterProcess()
	{
		return(starterProcess);
	}

	public static String getOutputScreen()
	{
		return(outputScreen);
	}
	public static void setOutputScreen(String screen)
	{
		outputScreen = screen;
	}
	public static void setDebugMode(boolean mode)
	{
		debugMode = mode;
	}
	public static boolean getDebugMode()
	{
		return(debugMode);
	}
	private static boolean setClassPaths()
	{
		classPath = System.getProperty("java.class.path",".");
		Logger.log("classpath set to " + classPath);
		return(true);
	}
	private static boolean checkPaths()
	{
			//check for valid paths
		if (classPath == null)
		{
			Logger.log("Error: Classpath is null");
			return(false);
		}
		if(tempPath == null)
		{
			Logger.log("Error: Temporary file path is null");
			return(false);
		}
		if (classPath.trim().length() == 0)
		{
			Logger.log("Error: Invalid classpath.  Zero length");
			return(false);
		}
		if (tempPath.trim().length() == 0)
		{
			Logger.log("Error: Invalid temporary file path.  Zero length");
			return(false);
		}

		File f = new File(tempPath);
		if(!f.isDirectory())
		{
			Logger.log("Error: Temporary file path is not a valid directory");
			return(false);
		}

		return(true);
	}
	public static boolean setTempPath(String dir)
	{
		if(dir != null && !dir.trim().equals(""))
		{
			tempPath = dir;
			System.err.print("Temp directory set via command line to ");
		}
		else
		{
			tempPath = System.getProperty("java.io.tmpdir");
			System.err.print("Temp directory set with java.io.tmpdir to ");
		}
		
		tempPath = tempPath.trim();

		if(!tempPath.endsWith(File.separator))
			tempPath = tempPath.concat(File.separator);
	
		tempPath = tempPath.concat("tcap").concat(File.separator);

		System.err.println(tempPath);
		try
		{
			File tf = new File(tempPath);
			if (!tf.exists())
				tf.mkdir();
		}
		catch(Exception e){ Logger.log("Error creating temporary directory: " + tempPath + "\n" + e); return(false);}
		
		return(true);
		
	}

	public static String getTempPath()
	{
		boolean setPath;
		if (tempPath == null)
		{
			setPath = setTempPath(null);
			if(! setPath )
			{
				return("");
			}
		}
		return(tempPath);
	}

	public static String getClassPath()
	{
		if(classPath == null)
		{
			if(! setClassPaths() )
				return("");
		}
		return(classPath);
	}
	public static String getPrintCommand()
	{
		if(printCmd == null)
			printCmd = getInstallRoot() + "htmlprint.exe";
		return(printCmd);
	}
	public static String getUserHomePath()
	{
		if(userHomePath == null)
			userHomePath = FileHelper.ensureTrailingSeparator(System.getProperty("user.home"));
		return(userHomePath);

	}
	public static int getMajorVersion()
	{
		return(4);
	}
	public static int getMinorVersion()
	{
		return(0);
	}
	public static int getBuild()
	{
		return(13);
	}
	public static Date getBuildDate()
	{
		return(buildDate);
	}
  public static boolean getDataSourceMode() {
			return singleDataSourceMode;
  }
	public static void setDataSourceMode(boolean singleDS) {
			singleDataSourceMode = singleDS;
	}
}
