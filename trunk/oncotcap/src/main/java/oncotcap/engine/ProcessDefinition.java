package oncotcap.engine;

import java.util.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.process.OncProcess;
import oncotcap.util.ReflectionHelper;
import oncotcap.util.StringHelper;

public class ProcessDefinition
{
	private DeclarationSection declarationSection = new DeclarationSection();
	private Hashtable<String, ClassSection> methods = new Hashtable<String, ClassSection>();
	private Hashtable<String, ClassSection> events = new Hashtable<String, ClassSection>();
	private ProcessDeclaration declaration;
	private boolean isCollection = false;
	
	public ProcessDefinition(ProcessDeclaration decl)
	{
		this(decl, false);
	}
	public ProcessDefinition(ProcessDeclaration decl, boolean isCollection)
	{
		declaration = decl;
		this.isCollection = isCollection;
	}
	public OncMethod getMethod(MethodDeclaration methodDecl, ValueMapPath path)
	{
		String methodName = path.substituteJavaName(methodDecl.getName());
		if(methods.containsKey(methodName))
			return((OncMethod) methods.get(methodName));
		else
		{
			OncMethod meth = new OncMethod(methodDecl, methodName);
			methods.put(methodName, meth);
			return(meth);
		}
	}
	
	public OncEvent getEvent(EventDeclaration eventDecl, ValueMapPath path)
	{
		String eventName = path.substituteJavaName(eventDecl.getName());
		if(events.containsKey(eventName))
			return((OncEvent) events.get(eventName));
		else
		{
			OncEvent ev = new OncEvent(eventDecl, eventName);
			events.put(eventName, ev);
			return(ev);
		}
	}
	public void addProvider(InstructionAndValues prov)
	{
		ClassSectionDeclaration sect = prov.getInstruction().getSectionDeclaration();
		if(sect == null || sect instanceof ClassSectionDeclaration.DeclarationSection)
			declarationSection.addInstruction(prov);
		else if(sect instanceof EventDeclaration)
			getEvent((EventDeclaration) sect, prov.getValues()).addInstruction(prov);
		else if(sect instanceof MethodDeclaration)
			getMethod((MethodDeclaration) sect, prov.getValues()).addInstruction(prov);
	}
	public ClassSection getDeclarationSection()
	{
		return(declarationSection);
	}
	public Collection<ClassSection> getMethods()
	{
		return(methods.values());
	}
	public Collection<ClassSection> getEvents()
	{
		return(events.values());
	}
	public String getName()
	{
		if(! isCollection)
		{
			if(declaration == null || declaration.getName() == null || declaration.getName().trim().equals(""))
				return("UNAMED_PROCESS");
			else
				return(StringHelper.javaName(declaration.getName()));
		}
		else
		{
			if(declaration == null || declaration.getName() == null || declaration.getName().trim().equals(""))
				return("UNAMED_COLLECTION_PROCESS");
			else
				return(StringHelper.javaName(declaration.getName() + "Collection"));
		}
	}
	public String getType()
	{
		if(declaration != null && declaration.getProcessClass() != null)
		{
			if(isCollection)
				return(ReflectionHelper.nameForClass(OncProcess.getCollectionClass(declaration.getProcessClass())) + "<" + declaration.getProcessClass().getSimpleName() + ">");
			else
				return(ReflectionHelper.nameForClass(declaration.getProcessClass()));
		}
		else
			return("");
	}
	public boolean isCollection()
	{
		return(isCollection);
	}
}