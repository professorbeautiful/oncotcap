package oncotcap.engine;

import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.action.*;
import oncotcap.util.Logger;
import oncotcap.util.OncASTTreeModel;
import oncotcap.util.StringHelper;

import java.util.*;

import oncotcap.datalayer.persistible.action.*;

public class InstructionAndValues
{
	private Instruction instruction;
	private ValueMapPath  path;
	boolean iShallLog = false;
	
	public InstructionAndValues(Instruction instruction, ValueMapPath path)
	{
		this.instruction = instruction;
		this.path = path;
	}
	public Vector<InstructionAndValues> splitByIfClauses(){
		Vector<InstructionAndValues> v = new Vector<InstructionAndValues> ();
		if(instruction.getEnclosingInstructionProvider() instanceof CodeBundle){
			CodeBundle cb = (CodeBundle) instruction.getEnclosingInstructionProvider();
			if( StringHelper.notEmpty(cb.ifClause)) {
				for(Object action : cb.getActionList()){
					CodeBundle cbCloned = cb.clone(false);
					Vector<OncAction> coll = new Vector<OncAction>();
					coll.add((OncAction) action);
					cbCloned.setActionList(coll);
					Instruction singleInstruction = cbCloned.getInstructions().iterator().next();
					InstructionAndValues iav = new InstructionAndValues(singleInstruction, path);
					v.add(iav);
				}
				return v;
			}
		}
		return null;
	}
	public Instruction getInstruction()
	{
		return(instruction);
	}
	public ValueMapPath getValues()
	{
		return(path);
	}
	
	public boolean equals(Object obj)
	{
		if(obj instanceof InstructionAndValues)
		{
			InstructionAndValues iav = (InstructionAndValues) obj;
			if(instruction instanceof EnumDefinition.DeclarationInstruction  && 
				iav.getInstruction() instanceof EnumDefinition.DeclarationInstruction)
			{
				InstructionProvider ip = iav.getInstruction().getEnclosingInstructionProvider();
				EnumDefinition ed = (EnumDefinition) ip;
				return(ed.equals(this, iav.getValues()));
				//return(false);
			}
			else if(instruction instanceof AddVariableAction.DeclarationInstruction  &&
					iav.getInstruction() instanceof AddVariableAction.DeclarationInstruction)
			{
				AddVariableAction ava = (AddVariableAction) iav.getInstruction().getEnclosingInstructionProvider();
				return(ava.equals(this, iav.getValues()));
				//return(false);
			}
			else if(instruction instanceof TriggerEventAction.TriggerDeclarationInstruction
					  && iav.getInstruction() instanceof TriggerEventAction.TriggerDeclarationInstruction)
			{
				TriggerEventAction.TriggerDeclarationInstruction trigInst = (TriggerEventAction.TriggerDeclarationInstruction) instruction;
				return(trigInst.equals(this, iav.getValues()));
			}
		//	else if(instruction instanceof InstantiateAction.MethodInstruction
		//			 && iav.getInstruction() instanceof InstantiateAction.MethodInstruction)
		//	{
		//		InstantiateAction iAct = (InstantiateAction) iav.getInstruction().getEnclosingInstructionProvider();
		//		return(iAct.equals((InstantiateAction) this.instruction.getEnclosingInstructionProvider(), this.getValues(), iav.getValues()));
		//	}
			else
				return(false);
		}
		else
			return(false);
	}
	public int hashCode()
	{
		return(instruction.hashCode());
	}
	public String toString() {
		InstructionProvider ip = instruction.getEnclosingInstructionProvider();
		return(path.substitute(ip.toString().replace('\n', ' ')));
	}
	Vector<String> allVars = null;
	public Collection<String> getAllVariables()
	{
		// Only do once - calculate the first time accessed
/*		System.out.println("InstructionAndValues.getAllVariables: instruction is "
				+ path.substitute(instruction.toString()));
*/
	//	if ( allVars == null ) {
			allVars = new Vector<String>();
			for(String var : instruction.getAllVariables(path)) {
				String substitutedPath = path.substitute(var);
				Collection<String> varsFirstPass = oncotcap.engine.java.JavaParser.getAllVariables(substitutedPath);
				allVars.addAll(varsFirstPass);
			}
			printAllVars();
		//}
/*		else if (iShallLog)
			Logger.logTheTime("InstructionAndValues.getAllVariables: already done.");
		System.out.print("InstructionAndValues: allVariables = ");
		for(Object var: allVars)
			System.out.print(" " + var);
		System.out.println();
*/		return(allVars);
	}
	public void printAllVars(){
		String s = "";
		Iterator it = allVars.iterator();
		while(it.hasNext()) s = s + " " + it.next().toString();
		System.out.println(s);	
	}

	Vector<VariableDependency> deps = null;
	public Collection<VariableDependency> getVariableDependencies()
	{
		// Only do once - calculate the first time accessed
		if ( deps == null ) {
			deps = new Vector<VariableDependency>();
			Collection<VariableDependency> vds = instruction.getVariableDependencies(path);
			for(VariableDependency vd : vds)
				deps.add(new VariableDependency(path.substitute(vd.leftVariableName), path.substitute(vd.rightVariableName), vd.getOperatorType()));
		}
/*		else if (iShallLog)
			Logger.logTheTime("InstructionAndValues.getVariableDependencies: already done.");*/
		return(deps);
	}

	Vector<String> setVars = null;
	public Collection<String> getSetVariables()
	{
		// Only do once - calculate the first time accessed
		if ( setVars == null ) {
			setVars = new Vector<String>();
			for(String var : instruction.getSetVariables(path))
				setVars.add(path.substitute(var));
		}
/*		else if (iShallLog)
			Logger.logTheTime("InstructionAndValues.getSetVariables: already done.");*/
		return(setVars);
	}
	public String getSetVariable()
	{
		// I think we want to enforce: only one setVariable per instruction. -rd
		Collection<String> setVarsLocal = getSetVariables(); 
		if ( setVarsLocal == null ) 
			return(null);
		if(setVarsLocal.size() > 1)
			System.err.println("InstructionAndValues: Cannot have more than one variable set in a single Instruction.");
		for(String var : instruction.getSetVariables(path))
			return(path.substitute(var));  // return first one.
		return(null);  //should be unreachable.
	}
}
