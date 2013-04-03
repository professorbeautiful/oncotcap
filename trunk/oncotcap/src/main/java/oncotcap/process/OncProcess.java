package oncotcap.process;

import java.lang.reflect.*;
import java.util.*;
import java.awt.Color;
import java.awt.Font;
import java.io.*;
import java.net.URL;

import oncotcap.sim.schedule.*;
import oncotcap.util.*;
import oncotcap.sim.random.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.DisplayMessageHelper;

public abstract class OncProcess extends AbstractOncParent implements Schedulable, OncParent,
Countable
{
	private double scheduleOffset = Double.NEGATIVE_INFINITY;
	//private Long serialNum = null;
	private Long seed = null;
	private OncRandom rng = null;
	protected int count;
	
	protected HashVector processes = new HashVector();
	
	public abstract Object clone();
	public abstract Object clone(oncotcap.util.OncEnum changeLevel);
	public abstract Object clone(oncotcap.util.OncEnum [] changeLevels);
	//public abstract oncotcap.util.OncEnum [] getIdEnums();
	//public abstract void changeProp(oncotcap.util.OncEnum e);
	public abstract OncCollection newCollectionInstance();
		
	public Vector myOncReporterObservers = new Vector();
	
	protected OncIDEnum [] myIdEnums = null;
	protected OncEnum [] allEnums = null;
	
	public OncIDEnum [] getIdEnums()
	{
//		OncEnum [] tempEnums;
//		int i = 0;
//		if(myIdEnums == null)
//		{	
		if(myIdEnums == null || (myIdEnums.length > 0 && myIdEnums[0] == null))
			myIdEnums = (OncIDEnum []) getEnums(OncIDEnum.class);
//						for(OncEnum en : tempEnums)
//				myIdEnums[i] = (OncIDEnum) tempEnums[i++];
//		}
		return(myIdEnums);
	}
	private OncEnum [] getEnums(Class enumClass)
	{
		OncEnum [] enums;
		Class myClass = this.getClass();
		Field [] fields = myClass.getFields();
		Vector<OncEnum> tempEnums = new Vector<OncEnum>();
		for(int i = 0; i < fields.length; i++)
		{	
			if(ReflectionHelper.isSuper(fields[i].getType(), enumClass))
			{
				try{tempEnums.add((OncEnum) fields[i].get(this));}
				catch(IllegalAccessException e){System.out.println("Illegal Access Exception on " + fields[i] + " in " + this.getClass());}
			}
		}
		
		enums = (OncEnum []) Array.newInstance(enumClass, tempEnums.size());
		int count = 0;
		for(OncEnum enu : tempEnums)
		{
			if(enu != null)
				enums[count++] = enu;
		}
		return(enums);
	}
	public OncRandom getRNG()
	{
		if(rng == null)
		{
			if(usesSingleRNG())
				rng = getParent().getRNG();
			else
				rng = newRNG(getSeed());
		}
		return(rng);
	}
	public OncEnum [] getAllEnums()
	{
		if(allEnums == null)
			allEnums = getEnums(OncEnum.class);
		return(allEnums);
	}
	public void init()
	{

	}
	
	public OncProcess()
	{
//		processId = ++count;
		//MasterScheduler.statsOn();
		setCreationTime(getMasterScheduler().globalTime);
		getMasterScheduler().installTrigger(MasterScheduler.NOW,this,"init");
//		this(null, false);
	}

	public OncProcess(OncParent p) {
		this(p, false);
	}
	
	public OncProcess(OncParent p, boolean now)
	{
//	  oncParent = p;
		setParent(p);
		setCreationTime(getMasterScheduler().globalTime);
		p.addChild(this);
//	  if (now)
//		init();
//	  else

		getMasterScheduler().installTrigger(MasterScheduler.NOW, this, "init");
	}

//	public double getRelativeTime()
//	{
//		return(MasterScheduler.globalTime - timeAtCreation);
//	}
	
	public OncCollection getCollection()
	{
		if(getParent() != null)
			return(getParent().getCollection(this));
		else
			return(null);
	}
	public double delta_t;
	public double previous_t = 0;
	public double getDelta_t()
	{
		return delta_t;
	}
	public void update()
	{
		delta_t = getMasterScheduler().globalTime - previous_t;
		previous_t = getMasterScheduler().globalTime;
//		updateVars();
//		updateActions();
	}

	public boolean equals(Object obj)
	{		
		if(this == obj) return(true);
		
	    if(!(obj instanceof OncProcess))
	             return(false);

		if(! obj.getClass().equals(getClass()))
			return(false);
		
		OncIDEnum  [] idEnums = getIdEnums();

	    OncProcess oobj = (OncProcess) obj;

		OncIDEnum [] compIdEnums = oobj.getIdEnums();
		
		if(idEnums == null)
			return(false);
	    else if(idEnums.length != compIdEnums.length)
	      return(false);

	    int n;
	    for(n=0;n<idEnums.length;n++)
	    {
		  OncIDEnum thisEnum = idEnums[n];
		  OncIDEnum oEnum = compIdEnums[n];
	      if( ! thisEnum.equals(oEnum) )
			  return(false);
	    }
	    return(true);
	}

	public String getIDLevels()
	{
		return(levelsToString(getIdEnums()));
	}
	public String getAllLevels()
	{
		return(levelsToString(getAllEnums()));
	}
	private String levelsToString(OncEnum [] enums)
	{
		String rVal = "";
		if(enums != null && enums.length >= 1 && enums[0] != null)
		{
			if(enums[0] != null)
			{
				for(int n = 0; n < enums.length - 1; n++)
					rVal = rVal + enums[n].toString() + "/";
	
				rVal = rVal + enums[enums.length - 1].toString();
			}
		}
		
		return(rVal);		
	}
	public boolean containsProp(OncEnum value)
	{
		OncEnum  [] tempEnums = getAllEnums();
		int n;
		if(tempEnums == null)
			return(false);
		for(n=0;n<tempEnums.length;n++)
		{
			OncEnum en =  tempEnums[n];
			if (en.getClass().equals(value.getClass()))
				if(value == en)
				   return(true);
		}
		return(false);
	}
	public void changeProp(OncEnum [] props)
	{
		for(int n = 0; n < props.length;  n++)
			changeProp(props[n]);
	}
	public boolean equals(Object obj, OncIDEnum newval)
	{
		OncIDEnum  [] tempEnums = getIdEnums();
		if(!(obj instanceof OncProcess))
	             return(false);

	    OncProcess oobj = (OncProcess) obj;

		if(tempEnums == null)
			return(false);
	    else if(tempEnums.length != oobj.getIdEnums().length)
	      return(false);

	    int n;
		for(n=0;n<tempEnums.length;n++)
		{
			OncIDEnum thisEnum = tempEnums[n];
			OncIDEnum oEnum = oobj.getIdEnums()[n];

			if( thisEnum.getClass().equals(newval.getClass()) )
			{
                if( ! (thisEnum == newval))
					return(false);
			}
			else if( ! (thisEnum == oEnum) )
				return(false);
		}
		return(true);
	}
	public boolean equals(OncProcess process, OncIDEnum [] props)
	{
		OncIDEnum [] tempEnums = getIdEnums();
		for(int n = 0; n < tempEnums.length; n++)
		{
			boolean found = false;
			for(int m = 0; m < props.length; m++)
				if(tempEnums[n].getClass().equals(props[m].getClass()))
				{
					found = true;
					if(! tempEnums[n].equals(props[m]))
						return(false);
				}
			if(! found)
				if(!process.containsProp(tempEnums[n]))
					return(false);
		}
		return(true);
	}
	public void installTrigger(double time)
	{
		try
		{
			Method m1 = this.getClass().getMethod("update",  (Class[])null);
			getMasterScheduler().installTrigger(time, this, m1);
		}
		catch(NoSuchMethodException e){Logger.log ("ERROR: No update method [installTrigger]");}
	}

	public void installTrigger(double time, String methodName)
	{
		try{
			Method m1 = this.getClass().getMethod(methodName,  (Class[])null);
			getMasterScheduler().installTrigger(time, this, m1);
		}
		catch(NoSuchMethodException e){Logger.log ("ERROR: No Such Method " + methodName + " [installTrigger]");}

	}

	public double getScheduleOffset()
	{
		return(this.scheduleOffset);
	}

	public void setScheduleOffset(double offset)
	{
		this.scheduleOffset = offset;
	}

/*	public void setSerialNum(long sn)
	{
		serialNum = new Long(sn);
	}
*/		
	public String toString()
	{
		oncotcap.util.OncEnum  [] idEnums = getAllEnums();
		if(idEnums == null)
			return(super.toString());
		else if(idEnums.length == 0)
			return(super.toString());
		else if(idEnums[0] == null)
			return(super.toString());
		else
		{
			int n;
			String myName = new String("");
			for(n=0;n<idEnums.length;n++)
			{
			  myName = myName + "[" + idEnums[n] + "] ";
			}
			return(myName);
	  }
	}

	public void addOncReporterObserver(Observer ob) {
		myOncReporterObservers.add(ob);
	}

	public void setSeed(long seed)
	{
		this.seed = new Long(seed);
	}
	
	public long getSeed()
	{
		if(seed != null)
		{
			return(seed.longValue());
		}
		else
		{
			if(getIdEnums().length == 0)
			{
				try
				{
					seed = new Long(Hash.nextHash(getParent().getSeed(), getParent().getSerial(this)));
					return(seed.longValue());
				}
				catch(ObjectNotFoundException e)
				{
					Logger.log("ERROR: Object " + this + " not found in " + getParent() + " [OncProcess.getSeed]");
					stopSimulation();
					return(-1);
				}
			}
			else
			{
				seed = new Long(Hash.nextHash(getSimulationSeed(), idsToLong()));
				seed = Hash.nextHash(seed.longValue(), Hash.hash(this.getClass().getSimpleName()));
				return(seed.longValue());
			}
		}
	}
	
	private long idsToLong()
	{
		oncotcap.util.OncEnum [] idEnums = getIdEnums();
		if(idEnums.length <= 0)
		{
			Logger.log("ERROR: Cannot HASH ID variables");
			return(0);
		}
		else
			return(Hash.hash(idEnums));
	}

	private static Hashtable objs = new Hashtable();
	private static Vector objsClassVector;
	private static Class oncObj = null;
	static
	{
		try{oncObj = Class.forName("oncotcap.process.OncProcess");}
		catch(ClassNotFoundException e){}

	}
	public static Class getOncProcessClass()
	{
		return(oncObj);
	}
	public static Hashtable getObjectTable()
	{
		if(objs.size() <= 0)
		{
			ProcessClassDefinition ONC_OBJECT = addClass("oncotcap.process.OncProcess", "oncotcap.process.OncCollection");
			ProcessClassDefinition CANCER_CELL = addClass("oncotcap.process.cells.AbstractCancerCell", "oncotcap.process.cells.CancerCellCollection");
			ProcessClassDefinition CELL = addClass("oncotcap.process.cells.AbstractCell", "oncotcap.process.OncCollection");
			ProcessClassDefinition ONCOLOGIST = addClass("oncotcap.process.treatment.AbstractOncologist", "oncotcap.process.OncCollection");
			ProcessClassDefinition PATIENT = addClass("oncotcap.process.treatment.AbstractPatient", "oncotcap.process.OncCollection");
			ProcessClassDefinition CELL_DEATH = addClass("oncotcap.process.cells.AbstractCellDeathEvent", "oncotcap.process.OncCollection");
			ProcessClassDefinition MUTATION_XY = addClass("oncotcap.process.cells.MutationStandardXY", "oncotcap.process.OncCollection");
			ProcessClassDefinition MUTATION_YZ = addClass("oncotcap.process.cells.MutationStandardYZ", "oncotcap.process.OncCollection");
			ProcessClassDefinition CONVERSION = addClass("oncotcap.process.cells.Conversion", "oncotcap.process.OncCollection");
			ProcessClassDefinition MITOSIS = addClass("oncotcap.process.cells.AbstractMitosisEvent", "oncotcap.process.OncCollection");
			ProcessClassDefinition AGENT_DISTRIBUTOR = addClass("oncotcap.process.treatment.AbstractAgentDistributor", "oncotcap.process.OncCollection");
			ProcessClassDefinition CLINICAL_TRIAL = addClass("oncotcap.process.clinicaltrial.AbstractClinicalTrial", "oncotcap.process.OncCollection");
			
			// Shouldn't children be added inside their respective process?
			ONC_OBJECT.addChild(ONC_OBJECT);
			ONC_OBJECT.addChild(PATIENT);
			
			CANCER_CELL.addChild(CELL_DEATH);
			CANCER_CELL.addChild(MUTATION_XY);
			CANCER_CELL.addChild(MUTATION_YZ);
			CANCER_CELL.addChild(CONVERSION);
			CANCER_CELL.addChild(MITOSIS);
			CANCER_CELL.addChild(ONC_OBJECT);
			CANCER_CELL.addChild(AGENT_DISTRIBUTOR);
			
			CELL.addChild(CELL_DEATH);
			CELL.addChild(MUTATION_XY);
			CELL.addChild(MUTATION_YZ);
			CELL.addChild(CONVERSION);
			CELL.addChild(MITOSIS);
			CELL.addChild(ONC_OBJECT);
			CELL.addChild(AGENT_DISTRIBUTOR);
			
			ONCOLOGIST.addChild(PATIENT);
			ONCOLOGIST.addChild(ONC_OBJECT);

			PATIENT.addChild(CANCER_CELL);
			PATIENT.addChild(CELL);
			PATIENT.addChild(ONC_OBJECT);
			PATIENT.addChild(AGENT_DISTRIBUTOR);
			
			CELL_DEATH.addChild(ONC_OBJECT);

			MUTATION_XY.addChild(ONC_OBJECT);

			MUTATION_YZ.addChild(ONC_OBJECT);

			CONVERSION.addChild(ONC_OBJECT);
			
			MITOSIS.addChild(ONC_OBJECT);

			CLINICAL_TRIAL.addChild(PATIENT);
			CLINICAL_TRIAL.addChild(ONC_OBJECT);
		}
		
		return(objs);
	}

	public static Vector getObjectList()
	{
		if(objsClassVector == null)
		{
			objsClassVector = new Vector(getObjectTable().keySet());	
		}
		return(objsClassVector);
	}
	private static ProcessClassDefinition addClass(String className, String collectionClassName)
	{
		ProcessClassDefinition pcd = new ProcessClassDefinition(className, collectionClassName);
		objs.put(pcd.cls, pcd);
		return(pcd);
	}
	public Class getCollectionClass()
	{
		String collClassName = getClass().getName() + "Collection";
		return(ReflectionHelper.classForName(collClassName));
	}
	public static String getCollectionSubClassName(String processClassName)
	{
		return(ReflectionHelper.nameForClass(getCollectionClass(ReflectionHelper.classForName(processClassName))));
	}
	public static Class getCollectionClass(Class processClass)
	{
		if(getObjectTable().containsKey(processClass))
		{
			ProcessClassDefinition pcd = (ProcessClassDefinition) getObjectTable().get(processClass);
			return(pcd.getCollectionClass());
		}
		else
			return(OncCollection.class);
	}
	public static boolean isChildProcessType(Class parent, Class child)
	{
		if(getObjectTable().containsKey(parent))
		{
			ProcessClassDefinition pcd = (ProcessClassDefinition) getObjectTable().get(parent);
			return(pcd.isChild(child));
		}
		else
			return(false);
	}
	private static class ProcessClassDefinition
	{
		Class cls;
		Class collectionCls;
		Hashtable children = new Hashtable();
		ProcessClassDefinition(String className, String collectionClassName)
		{
			cls = ReflectionHelper.classForName(className);
			collectionCls = ReflectionHelper.classForName(collectionClassName);
		}
		void addChild(ProcessClassDefinition pcd)
		{
			children.put(pcd.cls, pcd);
		}
		boolean isChild(ProcessClassDefinition pcd)
		{
			return(children.containsKey(pcd.cls));
		}
		boolean isChild(Class child)
		{
			return(children.containsKey(child));
		}
		Class getCollectionClass()
		{
			return(collectionCls);
		}
	}

	public static Vector getInitializationVars(Class subClass)
	{
		Vector vars = new Vector();

		if(subClass == null || !(ReflectionHelper.isSuper(subClass, oncObj)))
			return(vars);

		URL props = subClass.getResource(StringHelper.className(subClass.toString()) + ".prop");
		if(props != null)
		{
			vars.addAll(readInitVars(props));
		}
		return(vars);
	}
	private static Vector readInitVars(URL objectProps)
	{
		Vector rVec = new Vector();
		String line, varType, varName, word;
		boolean isID = false;
		try
		{
			FileInputStream in = new FileInputStream(StringHelper.substitute(StringHelper.substitute(objectProps.getFile(), "file:/", ""),"%20"," "));
			while(! FileHelper.eof(in))
			{
				line = FileHelper.readLine(in);
				word = StringHelper.getWord(line);
				if(word.equalsIgnoreCase("initvar") || word.equalsIgnoreCase("initidenum"))
				{
					if(word.equalsIgnoreCase("initidenum"))
						isID = true;
					
					line = StringHelper.chopWord(line);
					varType = StringHelper.getWord(line);
					line = StringHelper.chopWord(line);
					varName = StringHelper.getWord(line);
					boolean array = false;
					if(StringHelper.removeWhiteSpace(varName).equals("[]"))
					{
						array = true;
						line = StringHelper.chopWord(line);
						varName = StringHelper.getWord(line);
					}
					if(! StringHelper.blank(varType) && ! StringHelper.blank(varName))
					{
						VariableDefinition vd = new VariableDefinition(varName, varType, array);
						vd.setIsID(isID);
						vd.setIsFromPropFile(true);
						rVec.add(vd);
					}
				}
			}
		}
		catch(FileNotFoundException e){return(rVec);}
		catch(IOException e){return(rVec);}
		return(rVec);
	}

	public Object getContainerInstance(Class childClass)
	{
		return(getContainerInstance(this, childClass));
	}
	private Object getContainerInstance(OncParent parent, Class childClass)
	{
		if(childClass.isInstance(parent))
			return(parent);
		else if(parent instanceof OncProcess)
		{
			OncProcess parentAsProc = (OncProcess) parent;
			if(childClass.equals(parentAsProc.getCollectionClass()))
			{
				return(parentAsProc.getParent().getCollection(parentAsProc));
			}
			else
			{
				OncParent nextParent = parentAsProc.getParent();
				return(getContainerInstance(nextParent, childClass));
			}
		}
		else if(parent instanceof TopOncParent)
		{
			return(null);
		}
		else
			return(null);
	}
	public void changeProp(OncEnum value)
	{
		Field [] fields = this.getClass().getFields();
		for(int n = 0; n < fields.length; n++)
		{
			if(value.getClass().equals(fields[n].getType()))
			{
				try{fields[n].set(this, value);}
				catch(IllegalAccessException e){System.out.println("Illegal Access Exception trying to set: " + fields[n].getType() + " in " + this.getClass());}
			}
		}
		if(value instanceof OncEnum)
		{
			if(allEnums == null || (allEnums.length > 1 && allEnums[0] == null)) getAllEnums();
			for(int i = 0; i < allEnums.length; i++)
			{
				if(allEnums[i].getClass().equals(value.getClass()))
					allEnums[i] = (OncEnum) value;
			}
		}
		if(value instanceof OncIDEnum)
		{
			if(myIdEnums == null || (myIdEnums.length > 0 && myIdEnums[0] == null)) getIdEnums();
			for(int i = 0; i < myIdEnums.length; i++)
			{
				if(myIdEnums[i].getClass().equals(value.getClass()))
					myIdEnums[i] = (OncIDEnum) value;
			}
		}
	}
	/**
	 * Checks to see if a given OncProcess is contained somewhere as a parent or child anywhere above or
	 * below this process.
	 * 
	 * @param process An OncProcess to search for.
	 * @return
	 */
	public boolean inProcessTree(OncProcess process)
	{
		return(process == this || isAbove(process) || isBelow(process));
	}
	
	public boolean isAbove(OncProcess process)
	{
		if(getParent() == null)
			return(false);
		else if(getParent() == process)
			return(true);
		else
			return(getParent().isAbove(process));
	}
	
	private boolean isBelow(OncProcess process)
	{
		for(OncCollection coll : ((Collection<OncCollection>) getCollections()))
		{
			if(coll.getProcesses().contains(process))
				return(true);
			else
			{
				for(OncProcess proc : (Collection<OncProcess>) coll.getProcesses())
					if(proc.isBelow(process))
						return(true);
			}
		}
		return(false);
	}
	/**
	 * Checks to see if a given OncCollection is contained somewere in any parent
	 * or child anywhere above or below this process;
	 * 
	 * @param collection A collection to search for.
	 * @return
	 */
	public boolean inProcessTree(OncCollection collection)
	{
		return(true);
	}
	public boolean isBelow(OncCollection collection)
	{
		for(OncCollection coll : ((Collection<OncCollection>)getCollections()))
		{			
			if(coll.isBelow(collection))
					return(true);
		}
		return(false);
	}
	/**
	 * stopSimulation stops the simulation that this process and all processes above (parents)
	 * and below (collections or processes) is running in.
	 *
	 */
	public void stopSimulation()
	{
		getReporter().clear();
		getCTReporter().clear();
		getMasterScheduler().resetScheduler();
		getEventManager().clear();
		Logger.log("Simulation ended.");
	}
/*	private boolean isChildCollection(OncCollection collection)
	{
		for(OncCollection coll : (Collection<OncCollection>) getCollections())
		{
			if(coll.equals(collection))
				return(true);
			else
			{
				for()
			}
		}
	} */
	/* (non-Javadoc)
	 * @see oncotcap.process.Countable#decrementCount(int)
	 */
	public void decrementCount(int decrementBy) {
		this.count -= decrementBy;
		}
	/* (non-Javadoc)
	 * @see oncotcap.process.Countable#getCount()
	 */
	public int getCount() {
		return count;
	}
	/* (non-Javadoc)
	 * @see oncotcap.process.Countable#incrementCount(int)
	 */
	public void incrementCount(int incrementBy) {
		this.count += incrementBy;
		
	}
	/* (non-Javadoc)
	 * @see oncotcap.process.Countable#setCount(int)
	 */
	public void setCount(int count) {
		this.count = count;
	}
	public Vector<OncProcess> getMatchingOncProcesses(Class oncProcessClass, 
			OncEnum[] characteristics){
			Vector<OncProcess> matchingProcesses = new Vector<OncProcess>();
			Collection allProcesses = processes.get(oncProcessClass);
			for ( Object obj : allProcesses ){
				if (((OncProcess)obj).equals(characteristics) ){
					matchingProcesses.add((OncProcess)obj);
				}
			}
			return matchingProcesses;
	}
	
	public void sendMessage(String messageName, String message)
	{
		sendMessage(messageName, message, null, null);
	}
	public void sendMessage(String messageName, String message, Font font, Color foreground)
	{
		DisplayMessageHelper.sendMessage(this, messageName, message, font, foreground);
	}
}

