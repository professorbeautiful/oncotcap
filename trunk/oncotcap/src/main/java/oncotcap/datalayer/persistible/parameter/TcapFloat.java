package oncotcap.datalayer.persistible.parameter;

import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.datalayer.persistible.*;

public class TcapFloat extends AbstractIndividualParameter
{
	public String floatValue;
	
	public TcapFloat(oncotcap.util.GUID guid){
		super(guid);
	}
	
	public TcapFloat()
	{
		this(true);	
	}
	public TcapFloat(boolean saveToDataSource)
	{
		super(saveToDataSource);
	}
	public TcapFloat(float val, boolean saveToDataSource)
	{
		this(Float.toString(val), saveToDataSource);
	}
	public TcapFloat(Float val, boolean saveToDataSource)
	{
		this(Float.toString(val.floatValue()), saveToDataSource);
	}
	public TcapFloat(String val, boolean saveToDataSource)
	{
		super(saveToDataSource);
		floatValue = val;
		if(saveToDataSource)
			update();
	}
	public void initSingleParams()
	{
		setDefaultName("Float Value");
		setSingleParameterID("TcapFloat.Float");
		singleParameterList.add(this);
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
	public void setValue(float val)
	{
		setValue(Float.toString(val));
	}
	public void setValue(Float val)
	{
		if(val != null)
			setValue(val.toString());
	}
	public void setValue(String val)
	{
		if(val != null)
		{
			floatValue = new String(val);
		}
		else
		{
			floatValue = null;
		}
	}
	public String getValue()
	{
		if(floatValue != null)
			return(floatValue);
		else
			return("");
	}
	public String getCodeValue()
	{
		return(getValue());
	}
	public EditorPanel getEditorPanelWithInstance()
	{
		EditorPanel ep = new ParameterEditor((Parameter) this);
		return(ep);
	}

	public EditorPanel getParameterEditorPanelWithInstance()
	{
		EditorPanel ep = new FloatEditorPanel();
		ep.edit(this);
		return(ep);
	}
	public EditorPanel getEditorPanel()
	{
		return(new ParameterEditor());
	}
	public EditorPanel getParameterEditorPanel()
	{
		return(new FloatEditorPanel());
	}
	public String toString()
	{
		return((floatValue == null) ? "" : floatValue);
	}
	public Object clone()
	{
			return(clone(true));
	}
	public Parameter clone(boolean saveToDataSource)
	{
		TcapFloat rVal = new TcapFloat(getValue(), saveToDataSource); 
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
		return(ParameterType.FLOAT);
	}
/*	public void writeDeclarationSection(Writer write, StatementBundle sb) throws IOException
	{

	}

	public void writeMethodSection(Writer write, StatementBundle sb) throws IOException
	{

	}*/
	public boolean equalDeclaration(Object obj)
	{
		if(obj instanceof TcapFloat)
		{
			String compValue = ((TcapFloat) obj).getValue().trim();
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
