package oncotcap.datalayer.persistible.parameter;

import oncotcap.datalayer.Persistible;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.editor.persistibleeditorpanel.EditorPanel;
import oncotcap.display.editor.persistibleeditorpanel.SwitchableEditor;
import oncotcap.display.common.*;
import oncotcap.engine.ValueMapPath;

public class DeclareSwitchable extends AbstractDroppable implements DeclareVariable
{
	public boolean initialState;
	public Boolean state = null;
	private boolean setOnce = false;

	private DeclareVariableHelper dVHelper = new DeclareVariableHelper();
	public DeclareSwitchable(){}
	public DeclareSwitchable(oncotcap.util.GUID guid){
		super(guid);
	}
	public DeclareSwitchable(boolean saveToDataSource)
	{
		if(!saveToDataSource)
			setPersistibleState(Persistible.DO_NOT_SAVE);
	}
	public Object clone()
	{
		DeclareSwitchable newSw = new DeclareSwitchable();
		newSw.initialState = initialState;
		if(state != null)
			newSw.state = new Boolean(state.booleanValue());
		newSw.setOnce = setOnce;
		return(newSw);
	}
/*	public Object cloneSubstitute(StatementBundle sb)
	{
		DeclareSwitchable ds = new DeclareSwitchable(false);
		ds.initialState = initialState;
		ds.setName(sb.substitute(getName()));
		ds.setInitialValue(sb.substitute(getInitialValue()));
		return(ds);
	}*/
	public boolean check()
	{
		if(state != null)
			return(true);
		else
			return(false);
	}
	public String getStringValue()
	{
		if(state == null)
			return("");
		else if(state.booleanValue())
			return("On");
		else
			return("Off");
	}
	public String getValue()
	{
		return("");
	}
	public String getDisplayValue()
	{
		return(getStringValue());
	}
	public void setState(boolean state)
	{
		if(this.state == null)
		{
			  this.state = new Boolean(state);
		}
		else if( (!setOnce) && ! (this.state.booleanValue() && state) )
		{
			this.state = new Boolean(state);
			setOnce = true;
		}
	}
	public boolean getState()
	{
		if(state != null)
			return(state.booleanValue());
		else
			return(false);
	}
	public boolean getInitialState()
	{
		return(initialState);
	}
	public void setInitialState(boolean state)
	{
		initialState = state;
		update();
	}
	public VariableType getType()
	{
		return(VariableType.SWITCHABLE);
	}
	public EditorPanel getEditorPanel()
	{
		return(new SwitchableEditor());
	}
	public EditorPanel getEditorPanelWithInstance()
	{
		EditorPanel ep = new SwitchableEditor();
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
		writer.write("\tboolean " + varName + " = " + getState() + ";\n");
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
/*	public void writeMethodSection(Writer write, StatementBundle sb) throws IOException
	{

	}*/
	public String getName()
	{
		return(dVHelper.getName());
	}
	public void setName(String name)
	{
		dVHelper.setName(name);
	}
	public void setInitialValue(String val)
	{
		if(val != null && (val.trim().equalsIgnoreCase("on") || val.trim().equalsIgnoreCase("true")))
			setState(true);
		else
			setState(false);
	}
	public String getInitialValue()
	{
		if(getState())
			return("On");
		else
			return("Off");
	}
	
	public String getDisplayString()
	{
		return(dVHelper.getDisplayString());
	}

	// THis is necessary for dependencytable to work. - not sure if
	// this is where	 i should put it though or if this is not
	// subclassing the proper thing
	public String toString() {
		return getName();
	}
}
