package oncotcap.datalayer.persistible.parameter;

import javax.swing.ImageIcon;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.engine.ValueMapPath;

public class TcapString extends AbstractIndividualParameter implements DeclareVariable
{
	private DeclareVariableHelper dVHelper = new DeclareVariableHelper();
	private ImageIcon icon = null;
	public TcapString(oncotcap.util.GUID guid){
		super(guid);
	}
	public TcapString()
	{
		this(true);
	}
	public TcapString(boolean saveToDataSource)
	{
		super(saveToDataSource);
	}
	public TcapString(String val)
	{
		this(val, true);
	}
	public TcapString(String val, boolean saveToDataSource) //, boolean saveToDataSource)
	{
		super(saveToDataSource);
		setValue(val);
	}
	public void setValue(String val)
	{
		dVHelper.setValue(val);
		update();
	}
	public String getValue()
	{
		return(dVHelper.getValue());
	}
	public String getDisplayValue()
	{
		return(getValue());
	}
	protected void setDefaultName(String defaultDisplayName)
	{
		this.defaultDisplayName = defaultDisplayName;
		if(displayName == null) displayName = defaultDisplayName;
	}
	public void setDisplayValue(Object val)
	{
		if(val instanceof String)
			setValue((String) val);
	}
	public String getCodeValue()
	{
		//return(StringHelper.javaName(getValue()));
		return(getValue());
	}
	public EditorPanel getEditorPanelWithInstance()
	{
		EditorPanel ep = new ParameterEditor((Parameter) this);
		return(ep);
	}
	public EditorPanel getEditorPanel()
	{
		return(new ParameterEditor());
	}
	public EditorPanel getParameterEditorPanelWithInstance()
	{
		EditorPanel ep = new StringEditorPanel();
		ep.edit(this);
		return(ep);
	}
	public EditorPanel getParameterEditorPanel()
	{
		return(new StringEditorPanel());
	}

	public String toString()
	{
		if(getName() == null || getName().trim().equals(""))
			return(getValue());
		else
			return(getName());
	}
	public Object clone()
	{
		return(clone(true));
	}
	public Parameter clone(boolean saveToDataSource)
	{
		TcapString rVal = new TcapString(getValue(), saveToDataSource);
		rVal.setOriginalSibling(getOriginalSibling());
		return(rVal);
	}
/*	public Object cloneSubstitute(StatementBundle sb)
	{
		TcapString ts = new TcapString(false);
		ts.setValue(sb.substitute(getValue()));
		ts.setName(sb.substitute(getName()));
		ts.setInitialValue(sb.substitute(getInitialValue()));
		ts.displayName = sb.substitute(displayName);
		return(ts);
	}*/
	public boolean check()
	{
		return(true);
	}
	public String getStringValue()
	{
		return(toString());
	}
	public VariableType getType()
	{
		return(VariableType.STRING);	
	}
	public ParameterType getParameterType()
	{
		return(ParameterType.STRING);
	}
	public void initSingleParams()
	{
		setDefaultName("String Value");
		setSingleParameterID("TcapString.String");
		singleParameterList.add(this);
	}
	public String getDisplayName()
	{
		return(displayName);
	}
	public void setDisplayName(String name)
	{
		displayName = name;
	}
/*	public void writeDeclarationSection(Writer write, StatementBundle sb) throws IOException
	{

	}

	public void writeMethodSection(Writer write, StatementBundle sb) throws IOException
	{

	}*/
	public ImageIcon getIcon() {
		return icon;
	}

 	public  void setIcon(ImageIcon icon) {
			this.icon = icon;
 	}
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
	public void setName(String name)
	{
		dVHelper.setName(name);
	}
	public String getName()
	{
		return(dVHelper.getName());
	}
	public String getDisplayString()
	{
		return(dVHelper.getDisplayString());
	}
	public String getInitialValue()
	{
		return(dVHelper.getInitialValue());
	}
	public void setInitialValue(String val)
	{
		dVHelper.setInitialValue(val);
		update();
	}
}
