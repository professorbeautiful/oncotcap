
package oncotcap.engine;

import java.util.*;

import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.action.*;

public class OncModel
{
	private ModelController controller;
	private Hashtable<StatementBundle, ValueMapPath> statementBundles = new Hashtable<StatementBundle, ValueMapPath>();
	private Hashtable<ProcessDeclaration, ProcessDefinition> processes = new Hashtable<ProcessDeclaration, ProcessDefinition>();
	private Hashtable<ProcessDeclaration, ProcessDefinition> collectionProcesses = new Hashtable<ProcessDeclaration, ProcessDefinition>();
	public OncModel(ModelDefinition def)
	{
		this(def, null, null);
	}
	public OncModel(ModelDefinition def, Long rngSeed, Boolean useSingleRNG)
	{
		this.controller = def.getController();
		Iterator<StatementBundle> it = def.getStatementBundles().keySet().iterator();
		while(it.hasNext())
			addStatementBundle(it.next());
	}
	public void addStatementBundle(StatementBundle sb)
	{
		if(! statementBundles.containsKey(sb))
		{
			addStatementBundle(sb, null);
		}
	}
	
	public ModelController getController()
	{
		return(controller);
	}
	//recursively called to add all SB's under a given statement bundle
	//and correctly set the ValueMapPath for each SB.
	private void addStatementBundle(StatementBundle sb, ValueMapPath vmp)
	{
		ValueMapPath mapPath;
		if(vmp == null)
			mapPath = new ValueMapPath();
		else
			mapPath = new ValueMapPath(vmp);
		
		mapPath.addToBeginning(sb);
		statementBundles.put(sb, mapPath);
		distributeInstructionProvidersToProcesses(sb, mapPath);
		Iterator<StatementBundle> it = sb.getStatementTemplate().getStatementBundles().getIterator();
		while(it.hasNext())
			addStatementBundle(it.next(), mapPath);
	}
	private void distributeInstructionProvidersToProcesses(StatementBundle sb, ValueMapPath mapPath)
	{
		Collection<InstructionProvider> providers = sb.getLocalInstructionProviders();
		for(InstructionProvider provider: providers)
		{
				distributeInstructionProvidersToProcesses(provider, mapPath);
		}
	}
	private void distributeInstructionProvidersToProcesses(InstructionProvider provider, ValueMapPath mapPath)
	{
		boolean debugging = true;
		if(debugging)
			System.err.println("OncModel.distributeInstructionProvidersToProcesses: ENTERING");
		ProcessDefinition proc;
		if(debugging) System.out.println("  ======\n  Provider==> " + provider);
		if (provider instanceof oncotcap.datalayer.persistible.parameter.TcapString){
			@SuppressWarnings("unused") int i=1;
		}
		Collection<Instruction> instructions = provider.getInstructions();
		for(Instruction inst: instructions)
		{
			if(debugging) System.out.println("    ..Instruction==> " + inst.getEnclosingInstructionProvider());
			ClassSectionDeclaration decl = inst.getSectionDeclaration();
			if(debugging) System.out.println("      ..SectionDeclaration==> " + inst.getEnclosingInstructionProvider());
			String sectionName = "";
			if(decl != null){
				if( ! (decl instanceof ClassSectionDeclaration.DeclarationSection))
						sectionName = inst.getSectionDeclaration().getName();
				else {
					ClassSectionDeclaration sec = null;
					CodeBundle cb = null;
					if(provider instanceof CodeBundle) {
						cb = (CodeBundle) provider;
					} else if(provider instanceof OncAction) {
						cb = ((OncAction) provider).getCodeBundleContainingMe();
					}
					if(cb != null)
						sec = cb.getSectionDeclaration();
					if(sec != null)
						sectionName = sec.getName();
				}
			}
			if(sectionName.equals("collection update") || sectionName.equals("collection init"))
				proc = getCollectionProcess(provider.getProcessDeclaration());
		//	else if	(sectionName.equals("") && (provider instanceof AddVariableAction) && ((AddVariableAction) provider).getCodeBundleContainingMe().method.displayName.contains("collection"))
		//		proc = getCollectionProcess(provider.getProcessDeclaration());
			else
				proc = getProcess(provider.getProcessDeclaration());
			if(proc != null)
				proc.addProvider(new InstructionAndValues(inst, mapPath));
			else
				System.err.println("OncModel.distributeInstructionProvidersToProcesses: "
						+ " null proc");
			//TODO:  handle error.
			System.out.println("        .. ProcDef==> " + proc.getName());
		}
		//create process definitions for any referenced (but Instructionless) processes
//		Collection ctemp1 = CollectionHelper.emptyCollection;
//		ctemp1.add(new Integer(1));
//		Collection ctemp2 = CollectionHelper.emptyCollection;
//		System.out.println(ctemp2.size());  //This proves that one cannot use emptyCollection.
		Collection<ProcessDeclaration> procDecCollection = provider.getReferencedProcesses();  
		for(Object procDec : procDecCollection){
			if ( ! (procDec instanceof ProcessDeclaration)) {
				System.err.println("ERROR: distributeInstructionProvidersToProcesses: "
						+ " procDec should be ProcessDeclaration, instead it is "
						+ procDec.getClass());
				@SuppressWarnings("unused") String temp = "Breakpoint";
			}
			else
				proc = getProcess((ProcessDeclaration)procDec);
		}
		
		//if this provider contains more providers process them now.
		// TODO: Eventually, get rid of getAdditionalProviders().
		for(InstructionProvider provider2: provider.getAdditionalProviders()){
			distributeInstructionProvidersToProcesses(provider2, mapPath);
		}
		if(debugging)
			System.err.println("OncModel.distributeInstructionProvidersToProcesses: LEAVING");
	}
	/**
	 * @param decl - a ProcessDeclaration 
	 * @return - a ProcessDefinition which (if necessary) has been created and added to the "processes" Hashtable.
	 */
	private ProcessDefinition getProcess(ProcessDeclaration decl)
	{
		if(decl == null)
			return(null);
		
		if(processes.containsKey(decl))
		{
			return(processes.get(decl));
		}
		else
		{
			ProcessDefinition proc = new ProcessDefinition(decl);
			processes.put(decl, proc);
			return(proc);
		}
	}
	private ProcessDefinition getCollectionProcess(ProcessDeclaration decl)
	{
		if(collectionProcesses.containsKey(decl))
		{
			return(collectionProcesses.get(decl));
		}
		else
		{
			ProcessDefinition proc = new ProcessDefinition(decl, true);
			collectionProcesses.put(decl, proc);
			return(proc);
		}
	}
	
	public Collection<ProcessDefinition> getProcesses()
	{
		Vector<ProcessDefinition> allProcesses = new Vector<ProcessDefinition>();
		
		//make sure there is an OncCollection process defined for every OncProcess
		Enumeration e = processes.keys();
		while(e.hasMoreElements())
		{
			ProcessDeclaration decl = (ProcessDeclaration) e.nextElement();
			if(! collectionProcesses.containsKey(decl))
				collectionProcesses.put(decl, new ProcessDefinition(decl, true));
		}

		//now do the reverse make sure there is an OncProcess process defined for every OncCollection
		e = collectionProcesses.keys();
		while(e.hasMoreElements())
		{
			ProcessDeclaration decl = (ProcessDeclaration) e.nextElement();
			if(! processes.containsKey(decl))
				processes.put(decl, new ProcessDefinition(decl, true));
		}

		
		allProcesses.addAll(processes.values());
		allProcesses.addAll(collectionProcesses.values());
						
		return(allProcesses);
	}
}
