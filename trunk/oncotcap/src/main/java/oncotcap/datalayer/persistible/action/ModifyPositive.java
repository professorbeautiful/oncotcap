package oncotcap.datalayer.persistible.action;

import java.io.*;

import oncotcap.util.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.engine.VariableDependency;

public class ModifyPositive extends AbstractPersistible implements VariableModification
{
	public String modification;
	public ModifyPositive() {}
	public ModifyPositive(oncotcap.util.GUID guid) {
		super(guid);
	}
	public ModifyPositive(boolean saveToDataSource)
	{
		if(!saveToDataSource)
			setPersistibleState(Persistible.DO_NOT_SAVE);
	}
	public void setModification(String mod)
	{
		modification = mod;
	}
	public String getModification()
	{
		return(modification);
	}
	public ModifyVariableEditor getEditor()
	{
		return(new ModifyPositiveEditor());
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
		}
		w.write("\t\t" + varName + " *= " + modification + ";");
	}
	public Object cloneSubstitute(StatementBundle sb)
	{
		ModifyPositive mp = new ModifyPositive(false);
		mp.modification = sb.substitute(modification);
		return(mp);
	}*/
	public int getOperatorType()
	{
		return(VariableDependency.STAR_ASSIGN);
	}
}