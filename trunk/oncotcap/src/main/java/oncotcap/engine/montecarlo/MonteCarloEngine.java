package oncotcap.engine.montecarlo;


import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;

import javax.swing.*;

import oncotcap.Oncotcap;
import oncotcap.datalayer.persistible.MethodDeclaration;
import oncotcap.datalayer.persistible.ProcessDeclaration;
import oncotcap.datalayer.persistible.action.AddVariableAction;
import oncotcap.datalayer.persistible.action.InstantiateAction;
import oncotcap.engine.*;
import oncotcap.engine.java.*;
import oncotcap.sim.schedule.MasterScheduler;
import oncotcap.util.*;
import oncotcap.datalayer.persistible.parameter.DeclareVariable;
import oncotcap.datalayer.persistible.CodeBundle;

public class MonteCarloEngine extends AbstractSimulationEngine
{
	public MonteCarloEngine(OncModel model)
	{
		super(model);
	}
	public boolean validModel(OncModel model){return(true);}
	public RunnableModel assemble()
	{
		if(getModel() != null)
		{
			PackageDir packageDir = writeModel();
			// TODO: Is the following section obsolete??
			for(ProcessDefinition proc : getModel().getProcesses())
			{
				boolean debugging = true;
				if(debugging) Logger.log("MonteCarloEngine.assemble: "
						+ " procDef is "  + proc.getName());
				if(proc.getName().equals("Cancer_cell"))
				{
					for(ClassSection sect : proc.getMethods())
					{
						if(sect.getName().equals("update"))
							((DefaultClassSection) sect).sort();
					}
				}
			}
			if(packageDir != null)
			{
				ProcessDeclaration starterProcess = getModel().getController().getStarterProcess();
				if(!compile(packageDir, starterProcess))
					return(null);
				else
					return(new MonteCarloRunnableModel(packageDir, starterProcess, getModel().getController()));
			}
		}

		return(null);
	}
	private void addContainerProcessVariables() {
		/** Purpose:  to create new codeproviders to allow access to variables up the instantiation chain...
		 *   but only if there's not already a variable by that name, please  (TODO)!
		 */
		/** TODO: Organize the processes in a tree, and walk down the tree to be sure variables
		 * propagate all the way.  e.g.
		int nProcesses = getModel().getProcesses().size();
		int dim[]={nProcesses,nProcesses};
		ProcessDefinition [] processes = (ProcessDefinition []) getModel().getProcesses().toArray();
		 * then loop through filling out the matrix, 
		 * then form the tree from the matrix (as we do with variables)
		 * then walk the tree.
		 */
		java.util.Vector instantiates = new java.util.Vector();
		System.out.println("=====ADDING upstreamed variables");
		for( ProcessDefinition proc : getModel().getProcesses()) {
			Collection<ClassSection> sections = proc.getMethods();
			for(ClassSection sect : sections)
			{
				if(sect.getName().equals("init")) {
					// Gather all the variables.
					java.util.Vector<DeclareVariable> variables = new java.util.Vector<DeclareVariable>();
					for(java.util.Iterator iAvIter =  sect.getInstructionsAndValues().listIterator();
						iAvIter.hasNext(); ) {
						InstructionAndValues iAv = (InstructionAndValues) iAvIter.next(); 
						Instruction instruction = iAv.getInstruction() ;
						if(instruction instanceof AddVariableAction){ 
							DeclareVariable variable = ((AddVariableAction)instruction).getVariable() ;
							variables.add(variable);
							if(proc.isCollection()){
								System.out.println("Located a collection AddVariableAction");
							}
						}
					}
					//Find any instantiated objects 
					for(java.util.Iterator iAvIter =  sect.getInstructionsAndValues().listIterator();
						iAvIter.hasNext(); ) {
						InstructionAndValues iAv = (InstructionAndValues) iAvIter.next(); 
						Instruction instruction = iAv.getInstruction() ;
						if(instruction instanceof InstantiateAction){
							ProcessDeclaration instantiatedProcess = ((InstantiateAction)instruction).oncProcess ;
							// Create corresponding variables
							// Check with Bill - create a CodeBundle??
							CodeBundle cb = new CodeBundle();
							cb.setProcessDeclaration(instantiatedProcess );
							cb.setMethodDeclaration( new MethodDeclaration ("init"));
							for(Object o : variables.toArray()){
								// add a new AddVariableAction
								DeclareVariable var = (DeclareVariable )o;
								AddVariableAction action = new AddVariableAction();
								String initialValue = "((" + proc.getName() + ")getContainerInstance("
										+ proc.getName() + ")." + var.getName();
								System.out.println(proc.getName() + ": " + var.getName() + " = " + initialValue);
								DeclareVariable newvar = (DeclareVariable) var.clone();
								newvar.setName(var.getName());
								newvar.setInitialValue(initialValue);
								action.setVariable((DeclareVariable)var);
								/*  TODO:  complete this.
								*/
								cb.addAction(action);
							}
							System.out.println(cb);
							/*  TODO:  complete this; put the stuff into the Model.
							//The following two approaches don't work.
							//sect.getStatementBundle().addCodeBundle(cb);  
							//getModel().addCodeBundle(cb);  Method doesnt exist.
							*/
						}	
					}
				}
			}
		}
		System.out.println("===========");
	}
	private PackageDir writeModel()
	{
		PackageDir packageDir = null;
		
		/* First, write the java files to the "standard" (eclipse) location.
		 */
		System.out.println("user.home=" + System.getProperty("user.home"));
		if(System.getProperty("user.home").contains("day")){  //what a kluge! but ok for now.
			try{packageDir = new PackageDir(false);}    /// standard location tcapmodel.
			catch(TempDirFullException e){Logger.log("ERROR: The system temporary directory is full"); return(null);}
			for( ProcessDefinition proc : getModel().getProcesses())
			{
				boolean debugging = true;
				if(debugging) System.out.println("MonteCarloEngine.writeModel(): "
						+ "  procDef is " + proc.getName());
				JavaClassWriter.writeClass(proc, packageDir);
			}
			 /* If this is needed for debugging, it will be compiled "by hand" in Eclipse.
			  * But first, we make sure
			  * that previously compiled class files are removed.
			  */
			String compiledClassFolderName = packageDir.packagePath.replaceFirst("\\\\src\\\\","\\\\bin\\\\");
			File compiledClassFolder = new File(compiledClassFolderName);
			if(compiledClassFolder.exists()) {
				if(compiledClassFolder.isDirectory()){
					File [] files = compiledClassFolder.listFiles();
					for (int i = 0; i<files.length; i++)
						files[i].delete();
				}
			}
		}

		//  Now, write to the TEMP place where the model will be compiled and run.
		try{packageDir = new PackageDir(true);}
		catch(TempDirFullException e){Logger.log("ERROR: The system temporary directory is full"); return(null);}
		addContainerProcessVariables();
		for( ProcessDefinition proc : getModel().getProcesses())
		{
			JavaClassWriter.writeClass(proc, packageDir);
		}
		return(packageDir);
	}
	private boolean compile(PackageDir packageDir, ProcessDeclaration starterProcess)
	{
		if(starterProcess == null || starterProcess.getName() == null || starterProcess.getName().trim().equals(""))
		{
			Logger.log("WARNING: Cannot compile.  No start process.");
			return(false);
		}
		
		String [] args = {" ", " ", " ", " ", " ", " ", " "};
		String tempPath = FileHelper.sansTrailingSeparator(Oncotcap.getTempPath());
//		String classPath = System.getProperty("java.class.path",".");
		String classPath = packageDir.getCompileClassPath();
		String startingPath = packageDir.objectSourcePath(StringHelper.javaName(starterProcess.getName().trim()));
		args[0] = "-classpath";
		args[1] = classPath;
		args[2] = "-sourcepath";
		args[3] = tempPath;
		args[4] =  "-d";
		args[5] = tempPath;
		args[6] = startingPath;
		Logger.log("COMPILING:\n" + args[0] + "\n" + args[1] + "\n" + args[2] + "\n" + args[3] + "\n" + args[4] + "\n" + args[5] + "\n" + args[6]);
		JOptionPane compilerMessagePane = new JOptionPane("    Compiling... please wait.",
			JOptionPane.INFORMATION_MESSAGE);
		compilerMessagePane.setVisible(true);

//		java.io.PrintStream err = new java.io.PrintStream(System.err);
//		err = System.err;  //unitl we figure out how to capture the err.
			// Anyway, it's not needed now.

//       The following try block implements these two lines of code
//       with reflection.  This is needed because the tools.jar file
//       isn't  guaranteed to be in the path.
//
			  com.sun.tools.javac.Main mycompiler = new com.sun.tools.javac.Main();
			  StringWriter output = new StringWriter();
			  int retval = com.sun.tools.javac.Main.compile(args, new PrintWriter(output));
			  if(retval != 0)
			  {
//				  OncMessageBox.showError("The model failed to compile.", "Monte Carlo compilation.");
				  OncTextMessageBox.showErrorMessage("Compile Error", output.toString());
				  return(false);
			  }
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
		return(true);
	}

}
