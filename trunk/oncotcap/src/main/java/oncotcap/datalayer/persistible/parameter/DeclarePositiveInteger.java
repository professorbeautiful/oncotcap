package oncotcap.datalayer.persistible.parameter;

import oncotcap.datalayer.Persistible;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.editor.persistibleeditorpanel.EditorPanel;
import oncotcap.display.editor.persistibleeditorpanel.PositiveIntegerEditor;
import oncotcap.display.common.*;
import oncotcap.engine.ValueMapPath;

public class DeclarePositiveInteger extends AbstractDroppable implements DeclareVariable
{
	private DeclareVariableHelper dVHelper = new DeclareVariableHelper();
	
	public DeclarePositiveInteger(){}
	public DeclarePositiveInteger(boolean saveToDataSource)
	{
		if(!saveToDataSource)
			setPersistibleState(Persistible.DO_NOT_SAVE);
	}
	public DeclarePositiveInteger(oncotcap.util.GUID guid){
		super(guid);
	}
	public Object clone()
	{
		DeclarePositiveInteger newPI = new DeclarePositiveInteger();
		if(getValue() != null)
			newPI.setValue(new String(getValue()));
		return(newPI);
	}
/*	public Object cloneSubstitute(StatementBundle sb)
	{
		DeclarePositiveInteger dp = new DeclarePositiveInteger(false);
		dp.setName(sb.substitute(getName()));
		dp.setInitialValue(sb.substitute(getInitialValue()));
		dp.setValue(sb.substitute(getValue()));
		return(dp);
	}*/
	public void setValue(int val)
	{
		setValue(Integer.toString(val));
	}
	public void setValue(String val)
	{
		if(val != null)
			dVHelper.setValue(new String(val));
		else
			dVHelper.setValue(null);
		update();
	}
	public String getValue()
	{
		return(dVHelper.getValue());
	}
	public boolean check()
	{
		return(true);
	}
	public String getStringValue()
	{
		return(dVHelper.getValue());
	}
	public VariableType getType()
	{
		return(VariableType.POSITIVE_INTEGER);
	}
	public EditorPanel getEditorPanel()
	{
		return(new PositiveIntegerEditor());
	}
	public EditorPanel getEditorPanelWithInstance()
	{
		EditorPanel ep = new PositiveIntegerEditor();
		ep.edit(this);
		return(ep);
	}
/*	public void writeDeclarationSection(Writer writer, StatementBundle sb) throws IOException
	{
		String varName;
		if(sb == null)
			varName = StringHelper.javaName(getName());
		else
			varName = StringHelper.javaName(sb.substitute(getName()));
		
		String initVal = getInitialValue();
		if(initVal == null)
			initVal = "";
		else
			initVal = initVal.trim();
		
		writer.write("\tint " + varName +	((! initVal.equals("")) ? " = " + initVal : "") + ";\n");
	}*/
	public int equalDeclaration(DeclareVariable obj, ValueMapPath compPath, ValueMapPath myPath)
	{
		return(DeclareVariableHelper.equalDeclarations(this, myPath, obj, compPath));
	}
	public void setStatementBundleUsingMe(StatementBundle sb)
	{
		dVHelper.setStatementBundleUsingMe(sb);
	}
	public StatementBundle getStatementBundleUsingMe()
	{
		return(dVHelper.getStatementBundleUsingMe());
	}	
	//public void writeMethodSection(Writer writer, StatementBundle sb) throws IOException {}
	public void setName(String name)
	{
		dVHelper.setName(name);
	}
	public String getName()
	{
		return(dVHelper.getName());
	}
	public String getInitialValue()
	{
		return(dVHelper.getInitialValue());
	}
	public void setInitialValue(String val)
	{
		dVHelper.setInitialValue(val);
	}
	public String getDisplayString()
	{
		return(dVHelper.getDisplayString());
	}
	// THis is necessary for dependencytable to work. - not sure if
	// this is where i should put it though or if this is not
	// subclassing the proper thing
	
	public String toString() {
		return getName();
	}
}
