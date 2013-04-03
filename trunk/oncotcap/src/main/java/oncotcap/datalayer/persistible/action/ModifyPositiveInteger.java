package oncotcap.datalayer.persistible.action;

import java.io.*;

import oncotcap.util.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.datalayer.Persistible;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.engine.VariableDependency;

public class ModifyPositiveInteger extends ModifyPositive
{
	public ModifyPositiveInteger() {}
	public ModifyPositiveInteger(oncotcap.util.GUID guid) {
		super(guid);
	}
	public ModifyPositiveInteger(boolean saveToDataSource)
	{
		if(!saveToDataSource)
			setPersistibleState(Persistible.DO_NOT_SAVE);
	}
	public ModifyVariableEditor getEditor()
	{
		return(new ModifyPositiveIntegerEditor());
	}

/*	public void writeMethodSection(Writer w, Object origVar, StatementBundle sb) throws IOException
	{
		String varName = null;
		if(origVar instanceof DeclareVariable)
			varName = ((DeclareVariable) origVar).getName();
		else if(origVar instanceof VariableDefinition)
			varName = ((VariableDefinition) origVar).getName();
		if(varName != null)
		{
			if(sb == null)
				varName = StringHelper.javaName(varName);
			else
				varName = StringHelper.javaName(sb.substitute(varName));
			w.write("\t\t" + varName + " += " + modification + ";");
		}
	}
	public Object cloneSubstitute(StatementBundle sb)
	{
		ModifyPositiveInteger mp = new ModifyPositiveInteger(false);
		mp.modification = sb.substitute(modification);
		return(mp);
	}*/
	public int getOperatorType()
	{
		return(VariableDependency.PLUS_ASSIGN);
	}
}