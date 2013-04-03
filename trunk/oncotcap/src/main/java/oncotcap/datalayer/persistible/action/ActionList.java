package oncotcap.datalayer.persistible.action;

import java.util.*;

import oncotcap.engine.*;
import oncotcap.util.CollectionHelper;
import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.ProcessDeclaration;

public class ActionList extends Vector<OncAction> implements PersistibleList, InstructionProvider
{
	ProcessDeclaration processDeclaration = null;
	public boolean add(OncAction action){
		if (processDeclaration == null)
			processDeclaration = action.getProcessDeclaration();
		else if (processDeclaration != action.getProcessDeclaration())
			System.err.println("ActionList.add:  a different process is set");
		return super.add(action);
	}
	public ProcessDeclaration getProcessDeclaration(){
		return processDeclaration;
	}
	public Iterator<OncAction> getIterator()
	{
		return(iterator());
	}
	public void set(Collection listItems)
	{
		clear();
		addAll(listItems);
	}
	public int getSize()
	{
		return(size());
	}
	public Collection<OncAction> getActions()
	{
		return((Collection<OncAction>) this);
	}
	public Collection<InstructionProvider> getAdditionalProviders(){
//		return (Collection<InstructionProvider>) CollectionHelper.emptyCollection;
		return(new Vector<InstructionProvider>());
	// I'd like to do away with all getAdditionalProviders calls.
	}
	public Collection<Instruction> getInstructions(){
		Collection<Instruction> coll = new Vector<Instruction> ();
		for (OncAction act : this){
				coll.addAll(act.getInstructions());
		}
		return(coll);
	}
	public Collection<ProcessDeclaration> getReferencedProcesses(){
		//return (Vector<ProcessDeclaration>)CollectionHelper.makeVector((Collection<ProcessDeclaration>)processDeclaration); 
		Vector<ProcessDeclaration> v = new Vector<ProcessDeclaration>();
		v.add(processDeclaration);
		return (v); 
	}
}
