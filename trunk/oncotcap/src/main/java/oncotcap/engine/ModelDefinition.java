package oncotcap.engine;

import oncotcap.Oncotcap;

import java.util.*;
import java.io.*;
import javax.swing.*;

import oncotcap.process.*;
import oncotcap.util.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.engine.java.PackageDir;
import oncotcap.sim.*;

public class ModelDefinition
{
	PackageDir packageDir;
	private Hashtable allProcesses = new Hashtable();
	private Hashtable allCollections = new Hashtable();
	private Hashtable<StatementBundle, StatementBundleConfiguration> statementBundles = new Hashtable<StatementBundle, StatementBundleConfiguration>();
	private Vector substitutedStatementBundles = new Vector();
	private StatementConfigurationList configurationList = new StatementConfigurationList();
	private ModelController controller;
	private String compileClassPath = null;
	private boolean compiled = false;
	
	public ModelDefinition()
	{
		init();
	}
	public ModelDefinition(ModelController cont)
	{
		controller = cont;
		//refresh the configuration in case any
		//new Statement Bundles have been added
		cont.refresh();
		addInvisibleBundles(cont);
		init();
	}
	public static void main(String [] args)
	{
		//wdr setting submodelgroup
//		46e659cf0000040c000000f78f166666

		//wdr biophys submodelgroup
//		888e669800000549000000f7676bb3d2
//		SubModelGroup smg = (SubModelGroup) Oncotcap.getDataSource().find(new GUID("46e659cf0000040c000000f78f166666"));

		//TEST WDR submodelgroup
//		SubModelGroup smg = (SubModelGroup) Oncotcap.getDataSource().find(new GUID("888e66a300000814000000f85fa4d3e6"));
//		OncModel om = new OncModel();
//		om.add(smg);
//		OncPreCompiler opc = new OncPreCompiler(om);
//		om.writeModel();
	}
	private void init()
	{

	}
/*	public void assembleModel()
	{
		try{packageDir = new PackageDir();}
		catch(TempDirFullException e){ Logger.log(e); }
		System.out.println("Writing model to: " + packageDir.packagePath);
		refresh();
		substituteStatementBundles();
		Iterator it = substitutedStatementBundles.iterator();
		while(it.hasNext())
		{
			StatementBundle sb = (StatementBundle) it.next();
			extractCodeSections(sb);
			addVariableDefinitions(sb.getVariableDefinitions());			
		}
		writeModel();
	}*/
	public StatementConfigurationList getConfigurations()
	{
		return(configurationList);
	}
	public void removeStatement(StatementBundle sb)
	{
		statementBundles.remove(sb);
		configurationList.removeStatement(sb);
	}
	public Hashtable getStatements()
	{
		return(configurationList.getStatementHashtable());
	}
	public SortedList getSortedStatements()
	{
		return(configurationList.getSortedStatements());
	}
	public ModelController getController()
	{
		return(controller);
	}
	public void setController(ModelController cont)
	{
		controller = cont;
	}
	public ProcessDeclaration getStarterProcess()
	{
		return(controller.getStarterProcess());
	}
/*	public void refresh()
	{
		allProcesses.clear();
		allCollections.clear();
		EventManager.clear();
		//statementBundles.clear();
		Iterator it = statementBundles.keySet().iterator();
		while(it.hasNext())
		{
			StatementBundle sb = (StatementBundle) it.next();
			addStatementBundle(sb, (StatementBundleConfiguration) statementBundles.get(sb));
		}
	}*/
	private void addInvisibleBundles(ModelController controller)
	{
		if(controller != null && controller.getConfiguration() != null && controller.getConfiguration().getConfigurations() != null)
		{
			Iterator it = controller.getConfiguration().getConfigurations().iterator();
			while(it.hasNext())
			{
				StatementBundleConfiguration sbc = (StatementBundleConfiguration) it.next();
				if(! sbc.isVisible() )
					addStatementBundle(sbc.getStatementBundle(), sbc);
			}
		}
	}
	public void addStatementBundle(StatementBundle sb, StatementBundleConfiguration sc)
	{
		statementBundles.put(sb, sc);
//		extractActions(sb);
	}
	
	//this method is called recursively as the StatementBundle heirarchy
	//is traversed.  The ValueMap argument is cloned and any
	//new/different values are added each time.
/*	private void extractCodeSections(StatementBundle sb)
	{
		addCodeSections(sb.getCodeSections());
	}*/
/*	public void addCodeSections(Vector sections)
	{
		OncProcessClassWriter op;

		Iterator it = sections.iterator();
		while(it.hasNext())
		{
			Object obj = it.next();
			if(obj instanceof CodeSection)
			{
				CodeSection cs = (CodeSection) obj;
				//add the code section to the correct set of sections
				//(OncCollection processes or OncProcess processes)
				if(cs.getEventType() == CodeBundle.EVENT)
				{
					op = getProcessWriter(cs);
					if(op != null) op.addCodeSection(cs);
				}
				else
				{
				   if(cs.getMethodDeclaration() != null &&
				   	  (cs.getMethodDeclaration().getName().equals("collection update") ||
					   cs.getMethodDeclaration().getName().equals("collection init")
					  )
					 )
				   {
						op = getCollectionProcessWriter(cs);
						if(op != null) op.addCodeSection(cs);
				   }
				   else
				   {
					   op = getProcessWriter(cs);
					   if(op != null) op.addCodeSection(cs);
				   }
		
				}
				
				if(cs.getProcessReferences().size() > 0)
				{
					Vector refs = cs.getProcessReferences();
					Iterator it2 = refs.iterator();
					while(it2.hasNext())
					{
						ProcessDeclaration def = (ProcessDeclaration) it2.next();
						getProcessWriter(def);
					}
					
				}
			}
		}
	}*/
/*	private void addVariableDefinitions(Vector defs)
	{
		Iterator it = defs.iterator();
		OncProcessClassWriter op;
		while(it.hasNext())
		{
			op = null;
			Object obj = it.next();
			if(obj instanceof VariableDefinition)
			{
				String methName = null;
				VariableDefinition vd = (VariableDefinition) obj;
				ProcessDeclaration procDef = null;
				if((procDef = vd.getProcessDeclaration()) != null)
				{
					if(vd.getMethodDeclaration() != null &&
					   (methName = vd.getMethodDeclaration().getName()) != null &&
					    (methName.equals("collection update") || methName.equals("collection init")))
					{
						op = getCollectionProcessWriter(procDef);
					}
					else
					{
						op = getProcessWriter(procDef);
					}
					if(op != null)
						op.addVariableDefinition(vd);
				}		
			}
		}
	} */
/*	private void extractActions(StatementBundle sb)
	{
		ValueMap vm = (ValueMap) sb.getValueMap().clone();
		//loop through all SingleParameters contained in this StatementBundle
		//and extract any actions.  Create code bundles from these actions (for
		//now only CreateVariable actions.  Add the code bundle with call to
		//addCodeBundle(cb, vm)
		
		//TODO: finish me!
		
	}
*/


	//get the process that is connected to this CodeBundle.  If the
	//process doesn't exist create it.
/*	private OncProcessClassWriter getProcessWriter(CodeSection cs)
	{
		OncProcessClassWriter op;
		ProcessDeclaration pd = cs.getProcessDeclaration();
		return(getProcessWriter(pd));
	}
	//get the process that is connected to this ProcessDeclaration.  If the
	//process doesn't exist create it.
	private OncProcessClassWriter getProcessWriter(ProcessDeclaration pd)
	{
		OncProcessClassWriter op;
		if(pd != null)
		{
			if(!allProcesses.containsKey(pd))
			{
				op = new OncProcessClassWriter(pd);
				allProcesses.put(pd, op);
						//add an OncProcessClassWriter for a collection of the same type
				getCollectionProcessWriter(pd);
			}
			else
			{
				op = (OncProcessClassWriter) allProcesses.get(pd);
			}
			return(op);
		}
		else
			return(null);
		
	}
	//get the collection process for this code bundle.  If the process
	//doesn't exist yet, create it.
	private OncProcessClassWriter getCollectionProcessWriter(CodeSection cs)
	{
		ProcessDeclaration pd = cs.getProcessDeclaration();
		return(getCollectionProcessWriter(pd));
	}
	
	//get the collection process for this ProcessDeclaration.  If the process
	//doesn't exist yet, create it.
	private OncProcessClassWriter getCollectionProcessWriter(ProcessDeclaration pd)
	{

		OncProcessClassWriter collectionProcess;

		//if a process for this collection doesn't yet exist create one
		if(pd == null)
			return(null);
		else
		{
			if(!allCollections.containsKey(pd.getName() + "Collection"))
			{
				OncProcessClassWriter childProcess;
				ProcessDeclaration cpd = new ProcessDeclaration(getCollectionClass(pd), pd.getName() + "Collection", false);
				collectionProcess = new OncProcessClassWriter(cpd);
				collectionProcess.setIsCollection(true);
				allCollections.put(pd.getName() + "Collection", collectionProcess);

				//if an oncprocess for the child doesn't yet exist, create it
				//and add it to the processes vector
				if(! allProcesses.containsKey(pd))
				{
					childProcess = new OncProcessClassWriter(pd);
					allProcesses.put(pd, childProcess);
				}
				else
				{
					childProcess = (OncProcessClassWriter) allProcesses.get(pd);
				}
				childProcess.setCollectionProcess(collectionProcess);
				collectionProcess.setChildProcess(childProcess);
			}
			else
			{
				collectionProcess = (OncProcessClassWriter) allCollections.get(pd.getName() + "Collection");
			}
			return(collectionProcess);
		}
	}*/
	private Class getCollectionClass(ProcessDeclaration pd)
	{
		return(oncotcap.process.OncProcess.getCollectionClass(pd.getProcessClass()));
	}
	private String getClasspath()
	{
		String rVal = "." + File.pathSeparator +
						  FileHelper.sansTrailingSeparator(Oncotcap.getProgramDir()) + File.pathSeparator +
						  Oncotcap.getInstallDir() + "tools.jar" + File.pathSeparator +
						  Oncotcap.getInstallDir() + "protege.jar" + File.pathSeparator +
						  FileHelper.sansTrailingSeparator(Oncotcap.getInstallDir()) + File.pathSeparator +
						  FileHelper.sansTrailingSeparator(Oncotcap.getTempPath());

		String xtraPath = Oncotcap.getExtraCompilerClassPath().trim();
		if(xtraPath != null && ! xtraPath.equals(""))
		{
			if(! xtraPath.startsWith(File.pathSeparator))
				rVal = rVal + File.pathSeparator;

			rVal = rVal + xtraPath;
		}

		return(rVal);

	}
	private String getCommand(String packageName, String startingObject)
	{
		String rval;

		rval = StringHelper.addQuotesOS(Oncotcap.getJavaExe()) +
			   " -mx200M" +
			   " -classpath " + StringHelper.addQuotesOS(getClasspath()) + " " +
			   packageName + "." + startingObject +
			   " -i " + StringHelper.addQuotesOS(FileHelper.sansTrailingSeparator(Oncotcap.getInstallDir())) +
			   " -o " + StringHelper.addQuotesOS(Oncotcap.getTempPath() + packageName) +
				" -s " + 
			   " > " + Oncotcap.getTempPath() + packageName + File.separator + "sim.out";

		return(rval);
	}
	public Collection getProcessWriters()
	{
		return(allProcesses.values());
	}
	public Collection getCollections()
	{
		return(allCollections.values());
	}
	public PackageDir getPackageDir()
	{
		return(packageDir);
	}
/*	private void writeModel()
	{
		writeProcesses(allProcesses);
		writeProcesses(allCollections);
	}
	public void writeProcesses(Hashtable tbl)
	{
		Iterator it = tbl.values().iterator();
		while(it.hasNext())
			((OncProcessClassWriter) it.next()).writeProcess(packageDir);
	}
*/
	public Hashtable<StatementBundle, StatementBundleConfiguration> getStatementBundles()
	{
		return(statementBundles);
	}
	public boolean isComplete(StatementBundleConfiguration sbc)
	{
		return(configurationList.isComplete(sbc));
	}
	/*	public void add(SubModelGroup smg)
	{
		Iterator it = smg.getSubmodelsInGroup().getIterator();
		while(it.hasNext())
			add((SubModel) it.next());
	}
	public void add(SubModel sm)
	{
		Iterator it = sm.getEncodingsInMe().getIterator();
		while(it.hasNext())
			add((Encoding) it.next());
	}
	public void add(Encoding en)
	{
		Iterator it = en.getStatementBundlesImplementingMe().getIterator();
		while(it.hasNext())
			add((StatementBundle) it.next());

		System.out.println("============================================================");
		Iterator it3;
		it = allProcesses.values().iterator();
		while(it.hasNext())
		{
			OncProcessClassWriter proc = (OncProcessClassWriter) it.next();
			System.out.println("-PROCESS: " + proc.getName());
			Iterator it2 = proc.getMethods().iterator();
			while(it2.hasNext())
			{
				OncMethod meth = (OncMethod) it2.next();
				System.out.println("--METHOD: " + meth.getName());
				it3 = meth.getCodeBundles().iterator();
				while(it3.hasNext())
					((CodeBundleAndValueMap) it3.next()).print();
			}

			it2 = proc.getEvents().iterator();
			while(it2.hasNext())
			{
				OncEvent meth = (OncEvent) it2.next();
				System.out.println("--EVENT: " + meth.getName());
				it3 = meth.getCodeBundles().iterator();
				while(it3.hasNext())
					((CodeBundleAndValueMap) it3.next()).print();
			}

		}


	}

	public void add(StatementBundle sb)
	{
		statementBundles.add(sb);
		extractCodeBundles(sb, sb.getValueMap());
	}
	public Vector getStatementBundles()
	{
		return(statementBundles);
	}
*/
	private String objectPath(String objName)
	{
		String fp;
		fp = new String(getPackageDir().packagePath);
		fp = fp.concat(objName);
		return(fp);
	}
	private String objectSourcePath(String objName)
	{
		String fp = objectPath(objName);
		fp = fp.concat(".java");
		return(fp);
	}
	private String getCompileClassPath()
	{
		if (compileClassPath == null)
		{
			compileClassPath = StringHelper.removeQuotesOS("\".") + File.pathSeparator +
									 Oncotcap.getProgramDir() + File.pathSeparator + 
									 Oncotcap.getInstallDir() + "tools.jar" + File.pathSeparator +
									 Oncotcap.getInstallDir() + "protege.jar" + File.pathSeparator +
									 FileHelper.sansTrailingSeparator(Oncotcap.getTempPath());

			String xtraPath = Oncotcap.getExtraCompilerClassPath().trim();
			if(xtraPath != null && ! xtraPath.equals(""))
			{
				if(! xtraPath.startsWith(File.pathSeparator))
					compileClassPath = compileClassPath + File.pathSeparator;

				compileClassPath = compileClassPath + xtraPath;
			}
				
			String strPluginDir = Oncotcap.getInstallDir() + "plugins";
			if(FileHelper.fileExists(strPluginDir))
			{
				strPluginDir = strPluginDir + File.separator;
				
				File pluginDir = new File(strPluginDir);
				{
				   if(pluginDir.isDirectory())
				   {
				   		File [] jars = pluginDir.listFiles();
				   		if(jars != null)
				   		{
				   			for(int n = 0; n < jars.length; n++)
				   			{
				   				if(jars[n].getName().endsWith(".jar"))
				   					compileClassPath = compileClassPath + File.pathSeparator + strPluginDir + jars[n].getName();
				   			}
				   		}
				   }
				}
			}
			compileClassPath = compileClassPath + StringHelper.removeQuotesOS("\"");
		}
		return(compileClassPath);
	}

	public void compile()
	{
		if(getStarterProcess() == null || getStarterProcess().getName() == null || getStarterProcess().getName().trim().equals(""))
		{
			Logger.log("WARNING: Cannot compile.  No start process.");
			return;
		}
		
		String [] args = {" ", " ", " ", " ", " ", " ", " "};
		String tempPath = FileHelper.sansTrailingSeparator(Oncotcap.getTempPath());
//		String classPath = System.getProperty("java.class.path",".");
		String classPath = getCompileClassPath();
		String startingPath = objectSourcePath(StringHelper.javaName(getStarterProcess().getName().trim()));
		args[0] = "-classpath";
		args[1] = classPath;
		args[2] = "-sourcepath";
		args[3] = tempPath;
		args[4] =  "-d";
		args[5] = tempPath;
		args[6] = startingPath;
		Logger.log("COMPILING:\n" + args[0] + "\n" + args[1] + "\n" + args[2] + "\n" + args[3] + "\n" + args[4] + "\n" + args[5] + "\n" + args[6]);
		JOptionPane compilerMessagePane = new JOptionPane("Compiling... please wait.",
			JOptionPane.INFORMATION_MESSAGE);
		compilerMessagePane.setVisible(true);

		java.io.PrintStream err = new java.io.PrintStream(System.err);
		err = System.err;  //unitl we figure out how to capture the err.
			// Anyway, it's not needed now.

//       The following try block implements these two lines of code
//       with reflection.  This is needed because the tools.jar file
//       isn't  guaranteed to be in the path.
//
			  com.sun.tools.javac.Main mycompiler = new com.sun.tools.javac.Main();
			  int retval = com.sun.tools.javac.Main.compile(args);
//		sun.tools.javac.Main mycompiler = new sun.tools.javac.Main(); //err, null);
//		boolean retval = mycompiler.compile(args);
/*			try
			{
				OncClassLoader cl = new OncClassLoader(Thread.currentThread().getContextClassLoader());
				Thread.currentThread().setContextClassLoader(cl);
				Class clsCompiler = cl.loadClass("sun.tools.javac.Main");
				Class [] params = { err.getClass(), classPath.getClass()};
				Constructor [] constrs = clsCompiler.getDeclaredConstructors();
				Object [] instParams = {err, null};
				Object compiler = constrs[0].newInstance(instParams);
				Class [] cmplMethParams = {args.getClass()};
				Method compileMeth = clsCompiler.getMethod("compile", cmplMethParams);
				Object [] cmplParams = {args};
				Object retval = compileMeth.invoke(compiler, cmplParams);

			}
			catch(ClassNotFoundException e){System.err.println("Class not found. [OncPreCompiler]\n" + e);}
			catch(NoSuchMethodException e){System.err.println("No such method. [OncPreCompiler]\n"); e.printStackTrace();}
			catch(InstantiationException e){System.err.println("Instantiation Exception. [OncPreCompiler]\n" + e);}
			catch(IllegalAccessException e){System.err.println("Illegal Access Exception. [OncPreCompiler]\n" + e);}
			catch(InvocationTargetException e){System.err.println("Invocation target Exception. [OncPreCompiler]\n"); e.printStackTrace();}
*/			
			//the following two lines need to be used to make the
			//deprecated message go away when compiling with 1.4.  This
			//does not work with 1.3, however.
//			com.sun.tools.javac.Main mycompiler = new com.sun.tools.javac.Main();
//			int retval = mycompiler.compile(args);
		compiled = true;
	}

/*	public void run()
	{
		if(!compiled)
		{
			Logger.log("Model must be compiled before running.");
			return;
		}

		ProcessController runner = new ProcessController(getPackageDir().packageName + "." + StringHelper.javaName(controller.getStarterProcess().getName()), controller.getOutputScreens());
		runner.launch();
	}
	private void substituteStatementBundles()
	{
		StatementBundle sb;
		substitutedStatementBundles.clear();
		Iterator it = statementBundles.keySet().iterator();
		while(it.hasNext())
		{
			sb = (StatementBundle) it.next();
			substitutedStatementBundles.add(sb.cloneSubstitute());
		}
	}*/
}

