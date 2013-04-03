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
import oncotcap.engine.VariableDeclaration;
import oncotcap.engine.VariableDependency;
import oncotcap.util.CollectionHelper;
import oncotcap.engine.java.*;

public class ModifyVariableAction extends DefaultOncAction implements Editable, TreeViewable, VariableOperation
{

	//these are public so the dataacess stuff can set them...
	public Persistible variable;
	public VariableModification mod;

	private Vector variableListeners = new Vector();
	private VariableListener chooserListener;
	
	private ModifyVariableAction selfreference;
	
	public ModifyVariableAction(oncotcap.util.GUID guid){
		super(guid);
		selfreference = this;
		init();
	}
	
	public ModifyVariableAction() {
		selfreference = this;
		init();
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
		if(mod != null)
		{
			EditorPanel ed = (EditorPanel) getEditor();
			ed.edit(mod);
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
				setModification(((DeclareVariable) var).getType().getModifierStorageInstance());
			}
			else if(var instanceof VariableDefinition)
			{
				setModification(VariableType.getModificationInstance(((VariableDefinition) var).getType()));
			}
		}
		System.out.println("setVariable");
		update();
	}

	public VariableModification getModification()
	{
		return(mod);
	}
	public void setModification(VariableModification mod)
	{
		this.mod = mod;
		fireVariableChanged();
//		System.out.println("setModification");
		update();
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
		String rVal = "Modify ";
		String varName = "";
		String varType = "";
		if (getName() != null)
		{
			varName = getName();
		}
		rVal = rVal + " variable: " + varName;
		return (rVal);
	}

/*		public void writeDeclarationSection(Writer write) throws IOException
		{
			
		}
		public void writeMethodSection(Writer write) throws IOException
		{
			if(variable != null && mod != null)
			{
			//	if(variable instanceof DeclareVariable)
			//	{
					StatementBundle sb = this.getCodeBundleContainingMe().getStatementTemplateContainingMe().getStatementBundleImplementingMe();
					mod.writeMethodSection(write, variable, sb);
			//	}
			}		
				
		}*/
		public ActionEditor getEditor()
		{
			return(new ModifyVariableActionEditor());
		}
		public ModifyVariableEditor getVariableEditor()
		{
			if(mod != null)
				return(mod.getEditor());
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
		public Collection<String> getSetVariables()
		{
			Vector<String> vars = new Vector<String>();
			vars.add(getName());
			return(vars);
		}
		public Collection<VariableDeclaration> getVariableDeclarations()
		{
			return new Vector<VariableDeclaration>();
		}
		public Collection<VariableDependency> getVariableDependencies()
		{
			Vector<VariableDependency> depends = new Vector<VariableDependency>();
			Collection<String> allVars = JavaParser.getAllVariables(mod.getModification());
			for(String var: allVars)
			{
				depends.add(new VariableDependency(getName(),var, mod.getOperatorType()));
			}
			return new Vector<VariableDependency>();
		}
		private Vector<Instruction> instructions = null;
		public Collection<Instruction> getInstructions()
		{
			if(instructions == null)
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
				Vector<String> vars = new Vector<String>();
				vars.add(getName());
				return(vars);
			}
			public Collection<String> getAllVariables(ValueMapPath path)
			{
				return(getSetVariables(path));
			}
			public Collection<VariableDependency> getVariableDependencies(ValueMapPath path)
			{
				if(mod == null)
					return new Vector<VariableDependency>();
				
				Vector<VariableDependency> depends = new Vector<VariableDependency>();
				Collection<String> allVars = JavaParser.getAllVariables(mod.getModification());
				for(String var: allVars)
				{
					depends.add(new VariableDependency(getName(),var, mod.getOperatorType()));
				}
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
