package oncotcap.datalayer.persistible.parameter;

import oncotcap.datalayer.persistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.engine.ValueMapPath;

public class DeclarePositive extends AbstractIndividualParameter implements DeclareVariable
{
	private DeclareVariableHelper dVHelper = new DeclareVariableHelper();
	
	public DeclarePositive(oncotcap.util.GUID guid){
		super(guid);
	}
	public DeclarePositive()
	{
		this(true);
	}
	public DeclarePositive(boolean saveToDataSource)
	{
		super(saveToDataSource);
	}
	private DeclarePositive(DeclarePositive pos, boolean saveToDataSource)
	{
		super(saveToDataSource);
		if(pos.getValue() != null)
			dVHelper.setValue(new String(pos.getValue()));
		
	}
	public void setValue(String val)
	{
		dVHelper.setValue(val);
	}
	public void setValue(float val)
	{
		setValue(Float.toString(val));
	}
	public void setValue(Float val)
	{
		setValue(val.toString());
	}
	public String getValue()
	{
		return(dVHelper.getValue());
	}
	public void setDisplayValue(Object val)
	{
		if(val instanceof String)
			setValue((String) val);
		else if(val instanceof Float)
			setValue((Float) val);
	}
	public String getDisplayValue()
	{
		return(getValue());
	}
	public String getCodeValue()
	{
		return(getValue());
	}
	public boolean check()
	{
		return(true);
	}
	public String getStringValue()
	{
		return(getValue());
	}
	public VariableType getType()
	{
		return(VariableType.POSITIVE);
	}
	public EditorPanel getEditorPanel()
	{
		return(new ParameterEditor());
	}
	public EditorPanel getEditorPanelWithInstance()
	{
		EditorPanel ep = new ParameterEditor();
		ep.edit(this);
		return(ep);
	}
	public EditorPanel getParameterEditorPanel()
	{
		return(new PositiveEditor());
	}
	public EditorPanel getParameterEditorPanelWithInstance()
	{
		EditorPanel ep = new PositiveEditor();
		ep.edit(this);
		return(ep);
	}

	/*public Object clone()
	{
		return(clone(true));	
	}*/
	public Object clone()
	{
		return(super.clone());
	}
	public Parameter clone(boolean saveToDataSource)
	{
		DeclarePositive rVal = new DeclarePositive(this, saveToDataSource);
		rVal.setOriginalSibling(getOriginalSibling());
		return(rVal);
	}
/*	public Object cloneSubstitute(StatementBundle sb)
	{
		DeclarePositive dp = new DeclarePositive(false);
		dp.setName(sb.substitute(getName()));
		dp.setInitialValue(sb.substitute(getInitialValue()));
		dp.setValue(sb.substitute(getValue()));
		dp.displayName = sb.substitute(displayName);
		return(dp);
	}*/
	public ParameterType getParameterType()
	{
		return(ParameterType.POSITIVE);
	}
	public void initSingleParams()
	{
		setDefaultName("Positive Value");
		setSingleParameterID("Positive.Float");
		singleParameterList.add(this);
	}
/*	public void writeDeclarationSection(Writer w, StatementBundle sb) throws IOException
	{
		String varName;
		if(sb != null)
			varName = StringHelper.javaName(sb.substitute(getName()));
		else
			varName = StringHelper.javaName(getName());
		String initVal = getInitialValue();
		w.write("\tdouble " + varName +	(((initVal != null) && (! initVal.trim().equals(""))) ? " = " + initVal : "") + ";\n");

	}
	public void writeMethodSection(Writer writer, StatementBundle sb) throws IOException
	{
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
	
	public String toString()
	{
		return(name);
	}

	public String getDisplayString()
	{
		return(dVHelper.getDisplayString());
	}
	public void setInitialValue(String initVal)
	{
			dVHelper.setInitialValue(initVal);
			update();
	}
	public String getInitialValue()
	{
		return(dVHelper.getInitialValue());
	}
}