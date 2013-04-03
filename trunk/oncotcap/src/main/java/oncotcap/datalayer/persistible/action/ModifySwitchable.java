package oncotcap.datalayer.persistible.action;

import java.io.*;

import oncotcap.util.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.engine.VariableDependency;

public class ModifySwitchable extends AbstractPersistible implements VariableModification
{
	public boolean switchState = false;
	public ModifySwitchable() {}
	public ModifySwitchable(oncotcap.util.GUID guid) {
		super(guid);
	}
	public ModifySwitchable(boolean saveToDataSource)
	{
		if(!saveToDataSource)
			setPersistibleState(Persistible.DO_NOT_SAVE);
	}

	public void setSwitch(boolean state)
	{
		switchState = state;
	}
	public boolean getSwitch()
	{
		return(switchState);
	}
	public String getModification()
	{
		return(null);
	}
	
	public ModifyVariableEditor getEditor()
	{
		return(new ModifySwitchableEditor());
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
				
			w.write("\t\t" + varName + " = " + switchState + ";");
		}
	}
	public Object cloneSubstitute(StatementBundle sb)
	{
		ModifySwitchable ms = new ModifySwitchable(false);
		ms.switchState = switchState;
		return(ms);
	}*/
	public int getOperatorType()
	{
		return(VariableDependency.ASSIGN);
	}
}
