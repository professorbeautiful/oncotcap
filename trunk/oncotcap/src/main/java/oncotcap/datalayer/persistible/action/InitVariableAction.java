package oncotcap.datalayer.persistible.action;

import java.util.*;

import oncotcap.display.common.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.engine.ClassSectionDeclaration;
import oncotcap.engine.Instruction;
import oncotcap.engine.InstructionProvider;
import oncotcap.engine.ValueMapPath;
import oncotcap.engine.VariableDependency;
import oncotcap.engine.java.JavaParser;
import oncotcap.util.*;

public class InitVariableAction extends DefaultOncAction 
implements Editable, TreeViewable, VariableOperation, VariableActionable
{

	public Persistible variable ;
	private DeclareVariable newInit;

	private Vector variableListeners = new Vector();
	private VariableListener chooserListener;
	private InitVariableAction selfreference;
	
	public InitVariableAction(oncotcap.util.GUID guid){
		super(guid);
		selfreference = this;
		init();
	}

	public InitVariableAction() {

		init();
		selfreference = this;
	}

	private void init()
	{
		chooserListener = new VariableListener(){
			public void variableChanged(Object variable)
			{
				if(variable instanceof DeclareVariable)
					setVariable((DeclareVariable) variable);
				else if(variable instanceof VariableDefinition)
					setVariable((VariableDefinition) variable);
			}
		};
	}
	public String getDisplayString()
	{
		return toString();
	}

	public EditorPanel getEditorPanelWithInstance()
	{
		if(newInit != null)
		{
			EditorPanel ed = (EditorPanel) getEditor();
			ed.edit(newInit);
			return(ed);
		}
		else
			return(null);
	}

	public EditorPanel getEditorPanel()
	{
		return getEditorPanelWithInstance();
	}

	public Persistible getVariable()
	{
		return (variable);
	}

	public void setVariable(Object var)
	{
		if (var != null && (var instanceof DeclareVariable || var instanceof VariableDefinition))
		{
			variable = (Persistible) var;
			if(var instanceof DeclareVariable)
			{
				DeclareVariable dv = (DeclareVariable) ((DeclareVariable) var).clone();
				dv.setInitialValue("");
				setNewInitialization(dv);
			}
			else if(var instanceof EnumDefinition)
			{
				EnumDefinition ed = (EnumDefinition) var;
				DeclareEnum de = new DeclareEnum();
				de.setName(ed.getName());
				de.setDisplayName(ed.getName());
				setNewInitialization(de);
			}
			else if(var instanceof VariableDefinition)
				setNewInitialization(VariableType.getDeclarationInstance(((VariableDefinition) var).getType()));
		}
	}

	public DeclareVariable getNewInitialization()
	{
		return(newInit);
	}
	public void setNewInitialization(DeclareVariable newVar)
	{
		if(newVar != null)
		{
			this.newInit = newVar;
			if(newInit instanceof DeclareEnum)
			{
				((DeclareEnum) newInit).setIsNonID(true);
			}
			fireVariableChanged();
		}
	}
	public boolean check()
	{
		return (true);
	}

	public String getName()
	{
		if(variable != null)
		{
			if(variable instanceof DeclareVariable)
				return(((DeclareVariable) variable).getName());
			else if(variable instanceof VariableDefinition)
				return(((VariableDefinition) variable).getName());
			else
				return("");
		}
		else
			return("");
	}
	public String toString()
	{
		String rVal = "Initialize ";
		if (getName() != null)
		{
			rVal = rVal + " " + getName() ;
		}
		return (rVal);
	}
	public String toString(ValueMapPath path)
	{
		String rVal = "Initialize ";
		if (getName() != null)
		{
			rVal = rVal + " " + getName() + "f(";
		}
		for(VariableDependency vd : getVariableDependencies(path))
			rVal = rVal + vd.getRightVariableName() + " ";
		return (rVal + ")");
	}

/*	public void writeDeclarationSection(Writer write) throws IOException
	{

	}
	public void writeMethodSection(Writer write) throws IOException
	{
		if(newInit != null && newInit.getInitialValue() != null && !newInit.getInitialValue().trim().equals("") && ! getName().trim().equals(""))
			write.write( "\t\t" + getName() + " = " + newInit.getInitialValue().toString() + ";\n");
	}*/
	public ActionEditor getEditor()
	{
		return(new InitVariableEditor());
	}
	public EditorPanel getVariableEditor()
	{
		if(newInit != null)
		{
			return(((DeclareVariable) newInit).getType().getEditor());
		}
		else
			return(null);
	}
	public VariableListener getChooserListener()
	{
		return(chooserListener);
	}

	public void addVariableListener(VariableListener listener)
	{
		if(! variableListeners.contains(listener))
			variableListeners.add(listener);
	}
	public void removeVariableListener(VariableListener listener)
	{
		if(variableListeners.contains(listener))
			variableListeners.remove(listener);
	}
	public void fireVariableChanged()
	{
		Iterator it = variableListeners.iterator();
		while(it.hasNext())
			((VariableListener) it.next()).variableChanged(this);
	}
	public Collection<InstructionProvider> getAdditionalProviders()
	{
		if(newInit instanceof DeclareEnum)
		{
			Vector<InstructionProvider> vars = new Vector<InstructionProvider>();
			EnumDefinition def = new EnumDefinition(((DeclareEnum) newInit).getName(), false);
			def.setIsNonID(true);
			vars.add(def);
			return(vars);
		}
		else if(newInit instanceof EnumDefinition)
		{
			Vector<InstructionProvider> vars = new Vector<InstructionProvider>();
			EnumDefinition def = (EnumDefinition) newInit;
			def.setIsNonID(true);
			vars.add(def);
			return(vars);			
		}
		else
			return(new Vector<InstructionProvider>());
	}
	private Vector<Instruction> instructions = null;
	public Collection<Instruction> getInstructions()
	{
		//if(instructions == null)
		{
			instructions = new Vector<Instruction>();
			instructions.add(new MethodInstruction());
		}
		return(instructions);
	}
	public class MethodInstruction implements Instruction
	{
		public Collection<String> getSetVariables(ValueMapPath path)
		{
			Collection<String> vars;
			vars = getVarNamesFromReferencedParameter(path);
			if(vars == null)
			{
				vars = new Vector<String>();
				vars.add(getName());
			}
			return(vars);
				
		}
		public Collection<String> getAllVariables(ValueMapPath path)
		{
			Collection<String> vars;
			vars = getVarNamesFromReferencedParameter(path);
			if(vars == null)
			{
				vars = new Vector<String>();
				vars.add(getName());
				vars.addAll(JavaParser.getAllVariables(newInit.getInitialValue()));
			}
			vars.addAll(getIfClauseVariables(path));
			return(vars);
		}
		private Collection<String> getVarNamesFromReferencedParameter(ValueMapPath path)
		{
			Vector<String> vars = null;
			if(newInit instanceof DeclareEnum && newInit.getName().equals(CodeBundleEditorPanel.upstreamParameterPlaceholderName))
			{
				vars = new Vector<String>();
				if(StringHelper.isBackquoteRef(newInit.getInitialValue()))
				{
					Parameter param = path.getParameter(StringHelper.removeQuotesFromReference(newInit.getInitialValue()));
					if(param instanceof SubsetParameter)
					{
						vars.addAll(getVarNamesFromSubset((SubsetParameter)param));
					}
				}
			}
			return(vars);
		}
		private Collection<String> getVarNamesFromSubset(SubsetParameter param)
		{
			Vector<String> varNames = new Vector<String>();
			Iterator it = param.getAssignmentEnums().iterator();
			while(it.hasNext())
			{
				BooleanExpression be = (BooleanExpression) it.next();
				varNames.add("_" + ((EnumDefinition)be.getLeftHandSide()).getName());
			}
			return(varNames);
		}
		public Collection<VariableDependency> getVariableDependencies(ValueMapPath path)
		{
			Vector<VariableDependency> depends = new Vector<VariableDependency>();
			if(newInit instanceof DeclareEnum && newInit.getName().equals(CodeBundleEditorPanel.upstreamParameterPlaceholderName))
			{
				// How are Enum dependencies handled, then?
			}
			else
			{
				Collection<String> allVars = JavaParser.getAllVariables(newInit.getInitialValue());
				for(String var: allVars)
				{
					depends.add(new VariableDependency(getName(),var, VariableDependency.ASSIGN));
				}
			}
			String leftVar = path.substituteJavaName(getName());
			for(String var : selfreference.getIfClauseVariables(path))
				depends.add(new VariableDependency(leftVar, var, VariableDependency.LITERAL_if));
			return(depends);
		}
		public ClassSectionDeclaration getSectionDeclaration()
		{
			return(getCodeBundleContainingMe().getSectionDeclaration());
		}
		public InstructionProvider getEnclosingInstructionProvider()
		{
			return(selfreference);
		}
	}
}
