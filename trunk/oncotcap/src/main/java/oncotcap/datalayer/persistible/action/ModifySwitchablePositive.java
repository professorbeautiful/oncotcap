package oncotcap.datalayer.persistible.action;

import java.io.*;

import oncotcap.util.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.engine.VariableDependency;

public class ModifySwitchablePositive extends AbstractPersistible implements VariableModification
{
	public boolean switchState = false;
	public String modification;
	public ModifySwitchablePositive() {}
	public ModifySwitchablePositive(oncotcap.util.GUID guid) {
		super(guid);
	}
	public ModifySwitchablePositive(boolean saveToDataSource)
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
		return(new ModifySwitchablePositiveEditor());
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
				
			w.write("\t\t" + varName + ".setValue(" + varName + ".getValue() *= " + modification + ");");
			w.write("\t\t" + varName + ".setState(" + switchState + ")");
		}
	}
	public Object cloneSubstitute(StatementBundle sb)
	{
		ModifySwitchablePositive msp = new ModifySwitchablePositive(false);
		msp.switchState = switchState;
		msp.modification = sb.substitute(modification);
		return(msp);
	}*/
	public int getOperatorType()
	{
		return(VariableDependency.PLUS_ASSIGN);
	}
}