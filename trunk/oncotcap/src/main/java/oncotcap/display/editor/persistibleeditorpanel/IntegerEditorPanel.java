package oncotcap.display.editor.persistibleeditorpanel;

import oncotcap.datalayer.persistible.parameter.TcapInteger;
import oncotcap.util.StringHelper;

public class IntegerEditorPanel extends SingleValueEditorPanel
{
	private TcapInteger integerValue;
	
	public IntegerEditorPanel()
	{
		super();
		init();
	}
	private void init()
	{
		setLabel("Enter an integer value:");
	}
	public void edit(Object obj)
	{
		if(obj instanceof Integer)
			edit((Integer) obj);
		else if(obj instanceof TcapInteger)
			edit((TcapInteger) obj);
	}
	public void edit(TcapInteger val)
	{
		integerValue = val;
		setText(val.toString());
	}
	public void edit(Integer val)
	{
		if(val != null)
		{
			integerValue = new TcapInteger(val, true);
			setText(val.toString());
		}
	}
	public void save()
	{
		if(StringHelper.isNumeric(getText()))
			integerValue.setValue(getText());
	}
	public Object getValue()
	{
		return(integerValue);
	}
}