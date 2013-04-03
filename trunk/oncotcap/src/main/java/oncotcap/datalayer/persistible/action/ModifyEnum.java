package oncotcap.datalayer.persistible.action;

import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.datalayer.*;
import oncotcap.engine.VariableDependency;

public class ModifyEnum extends AbstractPersistible implements VariableModification
{
	public String modification;
	public ModifyEnum() {}
	public ModifyEnum(oncotcap.util.GUID guid) {
		super(guid);
	}
	public ModifyEnum(boolean saveToDataSource)
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
		return(new ModifyEnumEditor());
	}

/*	public void writeMethodSection(Writer w, Object origVar, StatementBundle sb) throws IOException
	{

	}*/
/*	public Object cloneSubstitute(StatementBundle sb)
	{
		ModifyEnum me = new ModifyEnum(false);
		me.modification = sb.substitute(modification);
		return(me);
	}*/
	public int getOperatorType()
	{
		return(VariableDependency.ASSIGN);
	}
}