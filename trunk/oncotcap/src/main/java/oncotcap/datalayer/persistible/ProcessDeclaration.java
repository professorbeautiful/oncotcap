package oncotcap.datalayer.persistible;

import java.util.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.action.ActionType;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.display.browser.TreeDisplayModePanel;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.engine.ValueMap;
import oncotcap.process.*;
import oncotcap.util.*;

import oncotcap.*;

public class ProcessDeclaration extends AbstractPersistible implements TreeViewable, Editable
{
	private DefaultPersistibleList methods = new DefaultPersistibleList();
	private String name;
	private Class superClass;
	private OncFilter connectedToMe;

	private static Hashtable allOncProcesses = null;

	Hashtable methodHashtable = new Hashtable();

	static{initAllOncProcesses();}

	public ProcessDeclaration(oncotcap.util.GUID guid){
		super(guid);	
		methods.addAll(SystemDefinedOncMethod.getAll());
		init(true);
	}

	public ProcessDeclaration() {
		this(true);
	}
	public ProcessDeclaration(boolean saveToDataSource)
	{
		if(!saveToDataSource)
			setPersistibleState(Persistible.DO_NOT_SAVE);
		methods.addAll(SystemDefinedOncMethod.getAll());
		init(saveToDataSource);
	}

	public ProcessDeclaration(Class cls, boolean saveToDataSource)
	{
		if(!saveToDataSource)
			setPersistibleState(Persistible.DO_NOT_SAVE);
		this.name = cls.toString();
		this.superClass = cls;
		if(saveToDataSource)
			addOncProcess(this);
		init(saveToDataSource);
	}

	public ProcessDeclaration(Class cls, String name, boolean saveToDataSource)
	{
		if(!saveToDataSource)
			setPersistibleState(Persistible.DO_NOT_SAVE);
		this.name = name;
		this.superClass = cls;
		if(saveToDataSource)
			addOncProcess(this);
		init(saveToDataSource);
	}
	ModelController mc;
	public static void main(String [] args)
	{
		OncoTCapDataSource dataSource = Oncotcap.getDataSource();
		//oncologist
		ProcessDeclaration pd = (ProcessDeclaration) dataSource.find(new GUID("888e66b800000440000000f7ba311dca"));
		Collection vars = pd.findVariables(true);
		System.out.println("****" + vars + "******");
		//patient
//		ProcessDeclaration pd = (ProcessDeclaration) dataSource.find(new GUID("888e66a30000001f000000f5bd96891b"));
		//cancer cell
//		ProcessDeclaration pd = (ProcessDeclaration) dataSource.find(new GUID("34c95c1d00000038000000f57f82c89d"));
//		pd.mc = (ModelController) dataSource.find(new GUID("888e66a3000009b8000000f97be45cab"));
//		OncFilter filter = new OncFilter();
//		filter.getRootNode().addChild(mc);
//		Iterator enums = pd.findVariables().iterator();
//		while(enums.hasNext())
//		{
//			Object obj = enums.next();
//			if(obj != null)
//			{
//				if(obj.toString() != null && ! (obj instanceof oncotcap.datalayer.persistible.action.AddVariableAction))
//					System.out.println(obj.toString());
//			}
//		}

/*		Vector vars = pd.findVariables();
		Iterator it = vars.iterator();
		while(it.hasNext())
			System.out.println(it.next()); */
	}
	private void init(boolean saveToDataSource)
	{
		connectedToMe = new OncFilter(saveToDataSource);
		connectedToMe.getRootNode().addChild(this, saveToDataSource);
	}
	public String getDisplayString()
	{
		return toString();
	}

	public EditorPanel getEditorPanel()
	{
		return new OncProcessEditorPanel();
	}

	public EditorPanel getEditorPanelWithInstance()
	{
			OncProcessEditorPanel opPanel = new OncProcessEditorPanel();
			opPanel.edit(this);
			return opPanel;
	}

	public Iterator getMethodList()
	{
		return (methods.iterator());
	}

	public Vector getMethods()
	{
		return (methods);
	}

	public String getName()
	{
		return (name);
	}

	public Class getProcessClass()
	{
		return (superClass);
	}

	public void setMethodList(Collection listOfMethods)
	{
			if ( methods == null)
			{
					methods = new DefaultPersistibleList();
			}
			methods.set(listOfMethods);
	} 
	public void setName(String name)
	{
		if (this.name != null)
		{
			allOncProcesses.remove(this.name);
		}
		this.name = name;
		addOncProcess(this);
	}

	public void setProcessClass(Class cls)
	{
		if (this.name != null)
		{
			allOncProcesses.remove(this.name);
		}
		this.superClass = cls;
		addOncProcess(this);
	}

	public void addMethod(MethodDeclaration meth)
	{
		if (!methods.contains(meth))
		{
			methods.add(meth);
		}
	}

	private static Vector enumDefClass = new Vector();
	static{ enumDefClass.add("EnumDefinition"); }

	private static Vector variableClasses = new Vector();
	static{	variableClasses.add("TreatmentSchedule");
				variableClasses.add("DeclareSwichable");
				variableClasses.add("DeclareSwitchablePositive");
				variableClasses.add("DeclarePositive");
				variableClasses.add("DeclarePositiveInteger");
				variableClasses.add("TcapString"); }

	public Enumeration findEnums()
	{

//		Hashtable vars = Oncotcap.getDataSource().getInstanceTree("EnumDefinition", enumDefClass, connectedToMe, 0);
		Hashtable vars = Oncotcap.getDataSource().getInstanceTree("AddAVariable", variableClasses, connectedToMe, 0);
		return(vars.keys());
	}
	public Collection findVariables(boolean includeEnums)
	{
		return(findVariables(null, includeEnums));
	}
	private Vector<String> varNames = new Vector<String>();
	public Collection findVariables(OncFilter filter, boolean includeEnums)
	{
		OncFilter insFilter;
		if(filter == null)
		{
			insFilter = connectedToMe;
		}
		else
		{
			insFilter = connectedToMe.and(filter);
		}
		SortedList<VariableInstance> vars = new SortedList<VariableInstance>(new ProcessAndVariableNameComparator());
		varNames.clear();
		addUniquelyNamedVars(vars, Oncotcap.getDataSource().getInstances("DeclareVariable", insFilter, "ProcessDeclarationActions"));
		addUniquelyNamedVars(vars, Oncotcap.getDataSource().getInstances("VariableDefinition", insFilter, "ProcessDeclarationActions"));
		if(includeEnums)
			addUniquelyNamedVars(vars, Oncotcap.getDataSource().getInstances("EnumDefinition", insFilter));
		addUniquelyNamedVars(vars, OncProcess.getInitializationVars(superClass));
		return(vars);
	}
	
	/** Find variables for process that are in a particular user defined scope
	 * The user will specify scope as CB, ST, SB, E, SM, SG, M
	 * This method will determine which instances of objects of the selected type are connected to the 
	 * current Code Bundle and find all variables in each of those objects and display them
	 *  
	 * @param includeEnums
	 * @return
	 */
	public Collection findVariablesByScope(Class scopeObject, 
			CodeBundle cb, boolean includeEnums) {
		Collection<CodeBundle> relatedCBs = new Vector<CodeBundle>();
		// From the KB get the tree of objects consisting of Root 
		// node = sourceObject and leafNode=CodeBundles
		// where the filter contains the currently selected cb
		Hashtable relatedObjectsInstances = Oncotcap.getDataSource().getInstanceTree(scopeObject.getSimpleName(), 
				CollectionHelper.makeVector("CodeBundle"),OncFilter.makeTransientFilter(cb), TreeDisplayModePanel.ROOT);
		// Get only the code bundles
		Collection codeBundles = Oncotcap.getDataSource().getOnly(relatedObjectsInstances, "CodeBundle");
		
		// Keep all code bundles that have ProcessDeclarations equal to 'this'
		for ( Object currentCB : codeBundles){
			if ( currentCB instanceof CodeBundle ) {
				if (((CodeBundle)currentCB).getProcessDeclaration() == this){
					relatedCBs.add((CodeBundle)currentCB);
				}
			}
		}
		// Find all initialized and added variables
		Collection variables = new Vector();
		for ( CodeBundle currentCB : relatedCBs){
			variables = CollectionHelper.or(variables, currentCB.getVariablesByType(ActionType.ADD_VARIABLE));
			variables = CollectionHelper.or(variables, currentCB.getVariablesByType(ActionType.INIT_VARIABLE));
		}
		//
		variables = CollectionHelper.or(variables,OncProcess.getInitializationVars(superClass));
		return(variables);
	}

	private SortedList addUniquelyNamedVars(SortedList<VariableInstance> varsByName, Collection<VariableInstance> varsToAdd)
	{
		Iterator<VariableInstance>  it = varsToAdd.iterator();
		while(it.hasNext())
		{
			VariableInstance var = it.next();
			if(! varNames.contains(var.getName()))
			{
				varNames.add(var.getName());
				varsByName.add(var);
			}
		}
		return(varsByName);
	}
	
	/*	public Vector findVariables()
	{
		Vector vars = new Vector(CodeBundle.findVariables(this));

		if(superClass != null)
		{
			vars.addAll(OncProcess.getInitializationVars(superClass));
		}
		else
			System.out.println("Super class is NULL!!!!");
		return (vars);
	}*/
//	public Vector allAttachedVariables()
//	{
//		Vector rVec = new Vector();
//		rVec.addAll(findNonEnumVariables());
//		rVec.addAll(findEnums());
//		
//	}
/*	public Vector findNonEnumVariables()
	{
		Vector rVec = new Vector(CodeBundle.findVariables(this, VariableType.ENUM, false));
		if(superClass != null)
		{
			Vector superVars = OncProcess.getInitializationVars(superClass);
			if(superVars != null)
			{
				Iterator it = superVars.iterator();
				while(it.hasNext())
				{
					Object obj = it.next();
					if(obj instanceof VariableDefinition)
					{
//						if(!((VariableDefinition) obj).getTypeClass().equals(Enum.class))
						rVec.add(obj);
					}
				}
			}
		}
		return (rVec);
	}
*/
	public String toString()
	{
		return (getName());
	}

	public static Object[] getAllProcesses()
	{
		Object [] procs = allOncProcesses.values().toArray();
		Object t;
		for(int i = 0; i < procs.length - 1; i++)
			for(int j = i + 1; j < procs.length; j++)
				if(procs[i].toString().compareToIgnoreCase(procs[j].toString()) > 0)
				{
					t = procs[i];
					procs[i] = procs[j];
					procs[j] = t;
				}		
		return (procs);
	}
	public static Collection getAllProcessesCollection()
	{
		return(allOncProcesses.values());
	}
	public static Collection getChildrenProcesses(ProcessDeclaration parentProc)
	{
		ProcessDeclaration pd;
		SortedList children = new SortedList(new ProcessAndVariableNameComparator());
		Iterator it = allOncProcesses.values().iterator();
		while(it.hasNext())
		{
			if(isChildType(parentProc, (pd = (ProcessDeclaration) it.next())))
				children.add(pd);
		}
		return(children);
	}
	private static boolean isChildType(ProcessDeclaration parent, ProcessDeclaration child)
	{
		return(OncProcess.isChildProcessType(parent.getProcessClass(), child.getProcessClass()));
	}
	private static void addOncProcess(ProcessDeclaration oncProc)
	{
		if (oncProc != null && oncProc.name != null)
		{
			allOncProcesses.put(oncProc.name, oncProc);
		}
	}

	private static void initAllOncProcesses()
	{
		if (allOncProcesses == null)
		{
			allOncProcesses = new Hashtable();
			Class clsOncProcess = ReflectionHelper.classForName("oncotcap.datalayer.persistible.ProcessDeclaration");
			OncoTCapDataSource dataSource = Oncotcap.getDataSource();
			Collection processes = dataSource.find(clsOncProcess);
			Iterator it = processes.iterator();
			while (it.hasNext())
			{
				ProcessDeclaration op = (ProcessDeclaration)it.next();
				addOncProcess(op);
			}
		}
	}

	/** Generate the code string that would be associated with 
			this oncprocess
	*/
	public String getCode(ValueMap vm){ return(""); }
	
	
	public boolean equals(Object obj)
	{
		if(!(obj instanceof ProcessDeclaration))
			return(false);
		else
		{
			ProcessDeclaration comp = (ProcessDeclaration) obj;
			if ( comp != null && comp.superClass != null 
					 && comp.name != null ) 
					return( (comp.name.equalsIgnoreCase(name) 
									 && comp.superClass.equals(superClass)));
			else 
					return false;
		}
	}
	
	public int hashCode()
	{
		int c1 = name.toUpperCase().hashCode();
		int c2 = superClass.hashCode();
		return(Hash.nextHash(c1, c2));
	}
	
	private static class ProcessAndVariableNameComparator implements java.util.Comparator
	{
		public int compare(Object obj1, Object obj2)
		{
			String name1 = getVarName(obj1);
			String name2 = getVarName(obj2);
			
			
			if(isFromPropFile(obj1) && isFromPropFile(obj2))
				return(name1.compareToIgnoreCase(name2));
			else if(isFromPropFile(obj1))
				return(-1);
			else if(isFromPropFile(obj2))
				return(1);
			else
				return(name1.compareToIgnoreCase(name2));
		}
	
		public boolean equals(Object obj1, Object obj2)
		{
			String name1 = getVarName(obj1);
			String name2 = getVarName(obj2);
			if(isFromPropFile(obj1) && isFromPropFile(obj2))
				return(name1.equalsIgnoreCase(name2));
			else if(isFromPropFile(obj1) || isFromPropFile(obj2))
				return(false);
			else
				return(name1.equalsIgnoreCase(name2));
		}
		private boolean isFromPropFile(Object varObj)
		{
			if(varObj instanceof VariableDefinition)
				return(((VariableDefinition) varObj).isFromPropFile());
			else
				return(false);
		}
		private String getVarName(Object varObj)
		{
			if(varObj instanceof DeclareVariable)
			{
				DeclareVariable dvVar = (DeclareVariable) varObj;
				return(dvVar.getName().trim());
			}
			else if(varObj instanceof VariableDefinition)
			{
				if(varObj instanceof EnumDefinition)
					return(varObj.toString().trim());
				else
				{
					VariableDefinition vdVar = (VariableDefinition) varObj;
					return(vdVar.getName().trim());
				}
			}
			else if(varObj instanceof ProcessDeclaration)
			{
				ProcessDeclaration proc = (ProcessDeclaration) varObj;
				return(proc.getName().trim());
			}
			else
				return("");
		}
	}
}