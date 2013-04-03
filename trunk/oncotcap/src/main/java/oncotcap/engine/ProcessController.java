package oncotcap.engine;

import java.util.*;
import java.awt.Window;
import java.lang.reflect.*;
import javax.swing.JFrame;
import oncotcap.Oncotcap;
import oncotcap.util.*;
import oncotcap.process.OncParent;
import oncotcap.process.OncProcess;
import oncotcap.process.TopOncParent;
import oncotcap.sim.schedule.MasterScheduler;

public class ProcessController implements Runnable
{

	private OncProcess process = null;
	private String starterProcess;
	private String outputScreen;
	private MasterScheduler masterScheduler = null;
	private Vector<Window> shownScreens = new Vector<Window>();
	private OncParent topParent;
	
	public static Class [] procAndControllerClasses = {OncProcess.class, ProcessController.class};
	public static Class [] procClass = {OncProcess.class};
	public static Class [] parentClass = {OncParent.class};

	public static void main(String [] args)
	{
		//String [] defaultArgs = { "-sp", "oncotcap.simtest.Patient", "-os", "oncotcap.display.modelcontroller.cellkineticsdemo.SinglePatient"};
		String [] defaultArgs = { "-sp", "oncotcap.simtest.Patient", "-os", "oncotcap.display.genericoutput.GenericOutput"};
		if(args.length == 0)
			Oncotcap.handleCommandLine(defaultArgs);
		else
			Oncotcap.handleCommandLine(args);
		ProcessController pr = new ProcessController(new TopOncParent(), Oncotcap.getStarterProcess(), Oncotcap.getOutputScreen());
		pr.launch();
	}
	
	public ProcessController(OncParent topParent, String starterProcess, String outputScreen)
	{
		this.starterProcess = starterProcess;
		this.outputScreen = outputScreen;
		this.topParent = topParent;
	}
	public void launch()
	{
		Thread t = new Thread(this);
		t.start();
	}
	public void run()
	{
		Constructor construct = null;
		Object [] procArray = null;
		try
		{

			OncClassLoader cl = new OncClassLoader(Thread.currentThread().getContextClassLoader());
			Thread.currentThread().setContextClassLoader(cl);
			Class procClass = cl.loadClass(starterProcess);

//			Class procClass = ReflectionHelper.classForName(starterProcess);
			if(procClass != null)
			{
				construct = procClass.getConstructor(parentClass);
				
				masterScheduler = topParent.getMasterScheduler();
				procArray = (Object[]) Array.newInstance(Object.class, 1);
				procArray[0] = topParent;
				Object procObj = construct.newInstance(procArray);
				if(procObj instanceof OncProcess)
					process = (OncProcess) procObj;
				else
					Logger.log("ERROR: Starter process must be of type OncProcess.");
			}
			else
			{
				Logger.log("ERROR: Cannot find class for starter process:" + starterProcess);	
			}
			
		}
		catch(InstantiationException e){Logger.log("ERROR: Cannot instantiate " + starterProcess + "\n" ); e.printStackTrace();}
		catch(IllegalAccessException e){Logger.log("Error: Cannot instantiate " + starterProcess + " due to an illegal access.");}
		catch(NoSuchMethodException e){Logger.log("ERROR: Cannot instantiate " + starterProcess + ".  Constructor with a single OncParent must be provided.");}
		catch(InvocationTargetException e){Logger.log("ERROR: Cannot instantiate " + starterProcess + " due to an invocation error.");
			e.getCause().printStackTrace();
		}
		catch(ClassNotFoundException e){Logger.log("ERROR: Cannot find class " + starterProcess);}
		
		if(process != null)
		{
//			Iterator it = outputScreens.iterator();
//			while(it.hasNext())
//			{
//				Object nextObj = it.next();
				if(outputScreen != null)
				{
					try
					{
						Class screenClass = ReflectionHelper.classForName(outputScreen);
						if(screenClass != null)
						{
							try
							{
								construct = construct = screenClass.getConstructor(procAndControllerClasses);
								procArray = (Object []) Array.newInstance(Object.class, 2);
								procArray[0] = process;
								procArray[1] = this;
							}
							catch(NoSuchMethodException e1)
							{
								try
								{
									construct = screenClass.getConstructor(procClass);
									procArray = (Object []) Array.newInstance(Object.class, 1);
									procArray[0] = process;
								}
								catch(NoSuchMethodException e){Logger.log("ERROR: Cannot instantiate " + outputScreen + ".  A constructor that takes an OncProcess must be provided."); return;}
							}
							finally
							{
								Object screen = construct.newInstance(procArray);
								if(screen instanceof Window)
								{
									((Window) screen).setSize(800,600);
									((Window) screen).setVisible(true);
									shownScreens.add((Window)screen);
								}
							}
						}
					}
					catch(InstantiationException e){Logger.log("ERROR: Cannot instantiate " + outputScreen);}
					catch(IllegalAccessException e){Logger.log("ERROR: Cannot instantiate " + outputScreen + " due to an illegal access.");}
					
					catch(InvocationTargetException e){Logger.log("ERROR: cannot instantiate " + outputScreen + " due to an invocation error."); e.getTargetException().printStackTrace();}
				}
				else
					Logger.log("Invalid output screen specification, no output screen specified.");
//			}
			if(masterScheduler != null)
				masterScheduler.execute();
		}
	}
	public void stop()
	{
		if(masterScheduler != null)
			masterScheduler.resetScheduler();
	}
	public void pause()
	{
		if(masterScheduler != null && ! masterScheduler.isPaused())
			masterScheduler.togglePause();
	}
	public void resume()
	{
		if(masterScheduler != null)
		{
			if( masterScheduler.isPaused())
				masterScheduler.togglePause();
			else if(! masterScheduler.isRunning())
			{
				destroy();
				masterScheduler = null;
				process = null;
				launch();
			}
		}
	}
	public void destroy()
	{
		if(process != null)
			process.stopSimulation();
		
		for(Window screen : shownScreens)
			screen.dispose();
	}
	public void addShownScreen(Window frame)
	{
		if(!shownScreens.contains(frame))
			shownScreens.add(frame);
	}
}