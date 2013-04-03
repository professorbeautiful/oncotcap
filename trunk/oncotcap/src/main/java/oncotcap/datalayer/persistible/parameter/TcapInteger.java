package oncotcap.datalayer.persistible.parameter;

import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.datalayer.persistible.*;

public class TcapInteger extends AbstractIndividualParameter
{
	public String integerValue;
	
	public TcapInteger(oncotcap.util.GUID guid){
		super(guid);
	}
	public TcapInteger()
	{
		this(true);
	}
	public TcapInteger(boolean saveToDataSource)
	{
		super(saveToDataSource);
	}
	public TcapInteger(int val, boolean saveToDataSource)
	{
		this(Integer.toString(val), saveToDataSource);
	}
	public TcapInteger(Integer val, boolean saveToDataSource)
	{
		this(val.toString(), saveToDataSource);
	}
	public TcapInteger(String val, boolean saveToDataSource)
	{
		super(saveToDataSource);
		setValue(val);
	}
	public void setValue(int val)
	{
		setValue(new Integer(val));
	}
	public void setValue(Integer val)
	{
		setValue(val.toString());
	}
	public void setValue(String val)
	{

		if(val != null)
		{
			integerValue = new String(val);
		}
		else
		{
			integerValue = null;
		}
		update();
	}
	public String getValue()
	{
		if(integerValue != null)
			return(integerValue);
		else
			return("");
	}
	public String getCodeValue()
	{
		return(getValue());
	}
	public String getDisplayValue()
	{
		return(getValue());
	}
	public void setDisplayValue(Object val)
	{
		if(val instanceof String)
			setValue((String) val);
		else if(val instanceof Integer)
			setValue((Integer) val);
	}

	public EditorPanel getParameterEditorPanelWithInstance()
	{
		EditorPanel ep = new IntegerEditorPanel();
		ep.edit(this);
		return(ep);
	}
	public EditorPanel getParameterEditorPanel()
	{
		return(new IntegerEditorPanel());
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

	public String toString()
	{
		return((integerValue == null) ? "" : integerValue);
	}
	public Object clone()
	{
		return(clone(true));
	}
	public Parameter clone(boolean saveToDataSource)
	{
		TcapInteger rVal = new TcapInteger(getValue(), saveToDataSource); 
		rVal.setOriginalSibling(getOriginalSibling());
			return(rVal);
	}
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
		return(null);	
	}
	public ParameterType getParameterType()
	{
		return(ParameterType.INTEGER);
	}
	public void initSingleParams()
	{
		setDefaultName("Integer Value");
		setSingleParameterID("TcapInteger.Integer");
		singleParameterList.add(this);
	}
/*	public void writeDeclarationSection(Writer write, StatementBundle sb) throws IOException
	{

	}

	public void writeMethodSection(Writer write, StatementBundle sb) throws IOException
	{

	}*/
	public boolean equalDeclaration(Object obj)
	{
		if(obj instanceof TcapInteger)
		{
			String compValue = ((TcapInteger) obj).getValue().trim();
			if(compValue != null && getValue() != null)
			{
				return(compValue.equals(getValue()));
			}
			else
				return(false);
		}
		else
			return(false);
	}
}
