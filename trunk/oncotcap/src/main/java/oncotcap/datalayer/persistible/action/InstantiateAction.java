package oncotcap.datalayer.persistible.action;

import java.util.*;

import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.engine.*;
import oncotcap.util.*;

public class InstantiateAction extends DefaultOncAction 
{
	public ProcessDeclaration oncProcess;
	private DefaultPersistibleList variableInitializations = new DefaultPersistibleList();
	private String enumInitializations;
	private String numNewProcesses = null;
	private Boolean instantiateAggregate = true;

	private static int nNewProcesses = 0;
	
	private InstantiateAction selfreference;
	public InstantiateAction(oncotcap.util.GUID guid){
		super(guid);
		selfreference = this;
	}
	public InstantiateAction() {selfreference = this; }

	public InstantiateAction(ProcessDeclaration proc)
	{
		oncProcess = proc;
		selfreference = this;
	}

	public String getName() {
		if ( oncProcess != null )
			return oncProcess.getName();
		else
			return getDisplayString();
	}
	public String getDisplayString()
	{
		return "Instantiate Action ";
	}

	public EditorPanel getEditorPanel()
	{
		return new InstantiateActionEditor();
	}
	public EditorPanel getEditorPanelWithInstance()
	{
		return new InstantiateActionEditor(this);
	}
	public Collection getVariableInitializations()
	{
		return(variableInitializations);
	}
	public void setVariableInitializations(Collection inits)
	{
		variableInitializations.set(inits);
	}
	public void addVariableInitialization(DeclareVariable initVar)
	{
		variableInitializations.add(initVar);
	}
	public void clearVariableInitializations()
	{
		variableInitializations.clear();
	}
	public void setEnumInitializations(String inits)
	{
		enumInitializations = inits;
		//update();
	}
	public String getEnumInitializations()
	{
		return (enumInitializations);
	}

	public ProcessDeclaration getInitializationProcessDeclaration()
	{
		return (oncProcess);
	}

	public void setProcessDeclaration(Object process)
	{
		if (process != null && process instanceof ProcessDeclaration)
		{
			oncProcess = (ProcessDeclaration)process;
		}
		//update();
	}
	public boolean check()
	{
		return (true);
	}

	public String toString()
	{
		String rVal = "Instantiate ";
		if (getInitializationProcessDeclaration() != null)
		{
			rVal = rVal + getInitializationProcessDeclaration();
		}
		return (rVal);
	}
/*	public void writeDeclarationSection(Writer w) throws IOException
	{ 
//		System.out.println("Instantiate Declaration Section");
	}
	public void writeMethodSection(Writer w) throws IOException
	{
		StatementBundle sb = this.getCodeBundleContainingMe().getStatementTemplateContainingMe().getStatementBundleImplementingMe();
		String strCode = "";

		String enumVars = "";
		if(enumInitializations != null)
			enumVars = enumInitializations.trim();


		//if this is a back-quoted reference (`ref`) to an instantiation
		//type SubsetPicker, assemble the Enums used to instantiate this
		//new process in an array so they can be passed to the
		//constructor.
		Vector instantiateEnumVars = new Vector();
		Iterator it;
		Object obj;
		Object leftSide;
		Object rightSide;
		if(enumVars.endsWith("`") && enumVars.startsWith("`"))
		{
			Parameter param = sb.getClonedParameter(enumVars.substring(1,enumVars.length()-1));
			if(param != null && param instanceof SubsetParameter)
			{
				SubsetParameter sp = (SubsetParameter) param;
				if(sp.getPickerType() == DeclareEnumPicker.INSTANTIATION)
				{					
					//these enums should be returned as a vector of type
					//BooleanExpression, where the left side of the
					//expession is an EnumDefinition and the right side is
					//an EnumLevel
					Vector enums = sp.getInstantiationEnums();
					
					it = enums.iterator();
					while(it.hasNext())
					{
						obj = it.next();
						if(obj instanceof BooleanExpression)
						{
							instantiateEnumVars.add(obj);
						}
					}
				}
			}
		}

		String enumArrayName = new String("enums" + (++nNewProcesses));
		strCode = strCode + "\t\tOncEnum [] " + enumArrayName + " = {";
		it = instantiateEnumVars.iterator();
		boolean firstVar = true;
		while(it.hasNext())
		{
			obj = it.next();
			leftSide = ((BooleanExpression) obj).getLeftHandSide();
			rightSide =((BooleanExpression) obj).getRightHandSide();
			if(leftSide instanceof EnumDefinition && rightSide instanceof EnumLevel)
			{
				EnumDefinition enumDef = (EnumDefinition) leftSide;
				EnumLevel level = (EnumLevel) rightSide;
				
				if(!firstVar)
					strCode = strCode + " ,";
				else
					firstVar = false;

				strCode = strCode + StringHelper.javaName(enumDef.getProcess().getName()) + "."
				                  + StringHelper.javaName(enumDef.getName()) + "."
				                  + StringHelper.javaName(level.getName());
			}
		}
		strCode = strCode + "};\n";

		String procName = "process" + nNewProcesses;
		strCode = strCode + "\t\t" + StringHelper.javaName(getProcessDeclaration().getName()) + " " + procName + " = new " + StringHelper.javaName(getProcessDeclaration().getName()) + "(this, " + enumArrayName + ");\n";
		strCode = strCode + "\t\tprocesses.put(" + StringHelper.javaName(getProcessDeclaration().getName()) + ".class, process" + nNewProcesses + ");\n\n";
		it = variableInitializations.iterator();
		while(it.hasNext())
		{
			obj = it.next();
			if(obj instanceof DeclareVariable)
			{
				DeclareVariable e = (DeclareVariable) obj;
				String initialValue = e.getInitialValue().trim();
				if(initialValue.endsWith("`") && initialValue.startsWith("`"))
				{
					Parameter param = sb.getClonedParameter(initialValue.substring(1,initialValue.length()-1));
					if(param != null && param instanceof SubsetParameter)
					{
						SubsetParameter sp = (SubsetParameter) param;
						if(sp.getPickerType() == DeclareEnumPicker.ASSIGNMENT)
						{
							String assignmentVarName = "enumAssignment" + nNewProcesses;					
							strCode = strCode + "\t\tOncEnum [] " + assignmentVarName + " = {" + sp.getAssignmentValues() +"};\n";
							initialValue = assignmentVarName;
						}
					}
				}
				strCode = strCode + "\t\t" + procName + "." + StringHelper.javaName(sb.substitute(e.getName())) + " = " + initialValue + ";\n";
			}
			else
				System.out.println("WARNING: Initialization of type " + obj.getClass() + " in InstantiateAction");
		}
		w.write(strCode);
	}


		//
		public Hashtable getVariableInitializationStatements() {
				Hashtable varInitStatements = new Hashtable();
				
				Iterator it = variableInitializations.iterator();
				Object obj = null;
				while(it.hasNext()) {
						obj = it.next();
						if(obj instanceof DeclareVariable)
								{
										DeclareVariable e = (DeclareVariable) obj;
										String varLongName = getProcessDeclaration().getName()
												+ "."
												+ e.getName() ;
										String varInitStatement = varLongName
												+ " = " 
									// 			+ getProcessDeclaration().getName()
// 												+ "."
												+ e.getInitialValue().trim();
										varInitStatements.put(varLongName, varInitStatement);
								}
						else
								System.out.println("WARNING: Initialization of type " + obj.getClass() + " in InstantiateAction");
				}
				return varInitStatements;
	}*/
	public Collection<ProcessDeclaration> getReferencedProcesses()
	{
		if(getInitializationProcessDeclaration() == null)
			return(new Vector<ProcessDeclaration>());
		else
		{
			Vector<ProcessDeclaration> rVec = new Vector<ProcessDeclaration>();
			rVec.add(getInitializationProcessDeclaration());
			return(rVec);
		}
	}
		private Vector<Instruction> instructions = null;
		
		public Collection<InstructionProvider> getAdditionalProviders()
		{
			return(new Vector<InstructionProvider>());
		}
		public Collection<Instruction> getInstructions()
		{
			if(instructions == null)
			{
				instructions = new Vector<Instruction>();
				instructions.add(new MethodInstruction());
				instructions.add(new DeclarationInstruction());
			}
			return(instructions);
		}
		
		//this action is only used to trigger a comparison of any referenced
		//EnumDefinitions (referenced via a DeclareEnum entry with a backquote
		//pointer to a SubsetPicker) in the variableInitializations list.
		//Because no code is generated in JavaCodeProvider for this instruction
		//no variables or dependencies are generated.  -wes 07/22/2005
		public class DeclarationInstruction implements Instruction
		{
			public Collection<String> getAllVariables(ValueMapPath path)
			{
				return(new Vector<String>());
			}
			public Collection<String> getSetVariables(ValueMapPath path)
			{
				return(new Vector<String>());
			}
			public Collection<VariableDependency> getVariableDependencies(ValueMapPath path)
			{
				return(new Vector<VariableDependency>());
			}			
			public ClassSectionDeclaration getSectionDeclaration()
			{
				return(ClassSectionDeclaration.DECLARATION_SECTION);
			}
			public InstructionProvider getEnclosingInstructionProvider()
			{
				return(selfreference);
			}			
		}
		public class MethodInstruction implements Instruction
		{
			public Collection<String> getAllVariables(ValueMapPath path)
			{
				//TODO: add variables needed to set the values for variableInitializations. 
				Vector<String> vars = new Vector<String>();
				vars.addAll(selfreference.getIfClauseVariables(path));
		
				/*Collection<DeclareVariable> varsDecls = variableInitializations;
				for(DeclareVariable decl : varsDecls)
					vars.add(decl.getName());
				*/
				return(vars);
			}
			public Collection<String> getSetVariables(ValueMapPath path)
			{
				Vector<String> vars = new Vector<String>();
/*				Vector<DeclareVariable> varsDecls = variableInitializations;
				for(DeclareVariable decl : varsDecls)
				{
					vars.add(path.substituteJavaName(decl.getName()));
				}
*/			// There should not be any set variables for an instantiation.
				return(vars);
			}
			public Collection<VariableDependency> getVariableDependencies(ValueMapPath path)
			{
				Vector<VariableDependency> deps = new Vector<VariableDependency>();
				Iterator it = getVariableInitializations().iterator();
				while(it.hasNext())
					deps.addAll(getVariableDependencies((DeclareVariable)it.next(), path));
				
				return(deps);
			}
			private Collection<VariableDependency> getVariableDependencies(DeclareVariable declVar, ValueMapPath path)
			{
				Vector<VariableDependency> deps = new Vector<VariableDependency>();
				return(deps);
				// No Set Variables, so no dependencies.  4/27/2006- rd.
/*				String initString = declVar.getInitialValue();
				if(initString == null || initString.trim().equals(""))
					return new Vector<VariableDependency>();
				else
				{
					Vector<VariableDependency> deps = new Vector<VariableDependency>();
					Collection<String> vars = oncotcap.engine.java.JavaParser.getAllVariables(path.substitute(initString));
					for(String var:vars)
						deps.add(new VariableDependency(path.substituteJavaName(declVar.getName()), var, VariableDependency.ASSIGN));
					return(deps);
				}
*/			}

			public ClassSectionDeclaration getSectionDeclaration()
			{
				return(getCodeBundleContainingMe().getSectionDeclaration());
			}
			public InstructionProvider getEnclosingInstructionProvider()
			{
				return(selfreference);
			}
		}
		
		public boolean equals(InstantiateAction compAction, ValueMapPath compPath, ValueMapPath myPath)
		{
			if(! this.getCodeBundleContainingMe().equals(compAction.getCodeBundleContainingMe()))
				return(false);

			String compName = compPath.substituteJavaName(compAction.getName());
			String myName = myPath.substituteJavaName(getName());
			if(! compName.equals(myName))
				return(false);
			
			String compEnumInits = compPath.substitute(compAction.getEnumInitializations());
			String myEnumInits = myPath.substitute(this.getEnumInitializations());
			if(! compEnumInits.equals(myEnumInits))
				return(false);
			
			Collection compVars = compAction.getVariableInitializations();
			Collection myVars = this.getVariableInitializations();
			if(compVars.size() != myVars.size())
				return(false);
			Iterator it = compVars.iterator();
			while(it.hasNext())
			{
				DeclareVariable compVar = (DeclareVariable) it.next();
				Iterator it2 = myVars.iterator();
				boolean found = false;
				while(it2.hasNext())
				{
					DeclareVariable myVar = (DeclareVariable) it2.next();
					if(myVar.equalDeclaration(compVar, compPath, myPath) == DeclareVariable.DECLARATION_EQUAL)
					{
						found = true;
						break;
					}
				}
				if(!found)
					return(false);
			}
			return(true);
		}
		/**
		 * @return Returns the numNewProcesses.
		 */
		public String getNumNewProcesses() {
			return numNewProcesses;
		}
		/**
		 * @param numNewProcesses The numNewProcesses to set.
		 */
		public void setNumNewProcesses(String numNewProcesses) {
			this.numNewProcesses = numNewProcesses;
		}
		/**
		 * @return Returns the instantiateAggregate.
		 */
		public boolean getInstantiateAggregate() {
			return instantiateAggregate;
		}
		/**
		 * @param instantiateAggregate The instantiateAggregate to set.
		 */
		public void setInstantiateAggregate(boolean instantiateAggregate) {
			this.instantiateAggregate = instantiateAggregate;
		}
}
