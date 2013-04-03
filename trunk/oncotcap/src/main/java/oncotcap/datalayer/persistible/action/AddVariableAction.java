package oncotcap.datalayer.persistible.action;

import java.util.*;

import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.engine.*;
import oncotcap.engine.java.JavaParser;
import oncotcap.util.*;

public class AddVariableAction extends DefaultOncAction implements VariableActionable
{
	public DeclareVariable variable;
	
	private AddVariableAction thisAction;
	public AddVariableAction(oncotcap.util.GUID guid){
		super(guid);
		thisAction = this;
	}
	public AddVariableAction() {thisAction = this; }
	public AddVariableAction(PrimitiveData storage) { thisAction = this; }

	public String getDisplayString()
	{
		return toString();
	}

	public EditorPanel getEditorPanel()
	{
		return new AddVariableEditor();
	}
	public EditorPanel getEditorPanelWithInstance()
	{
		return new AddVariableEditor(this);
	}

	public DeclareVariable getVariable()
	{
		return (variable);
	}

	public VariableType getVariableType()
	{
		if (variable == null)
		{
			return (null);
		}
		else
		{
			return (variable.getType());
		}
	}

	public void setVariable(Object var)
	{
		//TODO: Add an interface or superclass above declare variable and enums 
		//so these methods can be cleaned up
		System.out.println("DeclareVariable var: " + var);
		if ( var instanceof DeclareVariable)
			variable = (DeclareVariable)var;
		update();
	}
	public String getName() {
		if ( variable != null )
			return variable.getName();
		else
			return "unknown_variable";
	}
	public boolean check()
	{
		return (true);
	}

	public String toString()
	{
		String rVal = "Add ";
		String varName = "";
		String varType = "";
		DeclareVariable var = getVariable();
		if (var != null)
		{
			if (var.getName() != null)
			{
				varName = var.getName();
			}
			if (var.getType() != null)
			{
				varType = var.getType().toString();
			}
		}
		rVal = rVal + varType + " variable: " + varName;
		return (rVal);
	}
/*	public void writeDeclarationSection(Writer writer) throws IOException
	{
//		writer.write("//DECLARE_VARIABLE " + getVariable().getName() + "\n");
		StatementBundle sb = this.getCodeBundleContainingMe().getStatementTemplateContainingMe().getStatementBundleImplementingMe();
		getVariable().writeDeclarationSection(writer, sb);
//		writer.write("//END_DECLARE_VARIABLE" + "\n");
	}
	public void writeMethodSection(Writer writer) throws IOException
	{
		StatementBundle sb = this.getCodeBundleContainingMe().getStatementTemplateContainingMe().getStatementBundleImplementingMe();
		getVariable().writeMethodSection(writer, sb);
	}*/
	public void setStatementBundleUsingMe(StatementBundle sb)
	{
		super.setStatementBundleUsingMe(sb);
		variable.setStatementBundleUsingMe(sb);
	}
	private Vector<Instruction> instructions = null;
	public Collection<Instruction> getInstructions()
	{
		if(instructions == null)
		{
			instructions = new Vector<Instruction>();
			instructions.add(new DeclarationInstruction());
		}
		return(instructions);
	}
	public boolean equals(InstructionAndValues comp, ValueMapPath myPath)
	{
		Object obj = comp.getInstruction().getEnclosingInstructionProvider();
		if(obj instanceof AddVariableAction)
		{
			int rVal = variable.equalDeclaration(((AddVariableAction)obj).variable, comp.getValues(), myPath);
			if(rVal == DeclareVariable.DECLARATION_EQUAL)
				return(true);
			else if(rVal == DeclareVariable.DECLARATION_CONFLICTS)
			{
				//TODO: notify user here of conflicting declaration
				//      bring up both code bundles, and StatementBundles
				return(false);
			}
			else
				return(false);
		}
		else
			return(false);
	}
	public class DeclarationInstruction implements Instruction
	{
		public Collection<String> getSetVariables(ValueMapPath path)
		{
			Vector<String> vars = new Vector();
			vars.add(path.substituteJavaName(getName()));
			return(vars);
		}
		public Collection<String> getAllVariables(ValueMapPath path)
		{
			Vector<String> vars = new Vector<String>();
			vars.add(getName());
			Collection<String> moreVars = JavaParser.getAllVariables(path.substitute(variable.getInitialValue()));
			for(String var : moreVars)
				if(!vars.contains(var))
					vars.add(var);
			Collection<String> ifVars = getCodeBundleContainingMe().getIfClauseVariables(path);
			vars.addAll(ifVars);
			return(vars);
		}
		public Collection<VariableDependency> getVariableDependencies(ValueMapPath path)
		{
			Vector<VariableDependency> deps = new Vector<VariableDependency>();
			String leftVar = path.substituteJavaName(getName());
			Collection<String> rightVars = JavaParser.getAllVariables(path.substitute(variable.getInitialValue()));
			for(String var : rightVars)
				deps.add(new VariableDependency(leftVar, var, VariableDependency.ASSIGN));
			Collection<String> ifVars = getCodeBundleContainingMe().getIfClauseVariables(path);
			for(String var : ifVars)
				deps.add(new VariableDependency(leftVar, var, VariableDependency.LITERAL_if));
			return(deps);
		}
		public ClassSectionDeclaration getSectionDeclaration()
		{
			return(ClassSectionDeclaration.DECLARATION_SECTION);
		}
		public InstructionProvider getEnclosingInstructionProvider()
		{
			return(thisAction);
		}
	}
}
