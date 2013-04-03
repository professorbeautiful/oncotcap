package oncotcap.display.editor.persistibleeditorpanel;

import java.awt.Dimension;

import oncotcap.datalayer.persistible.parameter.TcapFloat;
import oncotcap.util.StringHelper;

public class FloatEditorPanel extends SingleValueEditorPanel
{
	private TcapFloat floatValue;
	public FloatEditorPanel()
	{
		super();
		init();
	}
	private void init()
	{
		setLabel("Enter a float value:");
		setPreferredSize(new Dimension(200,50));
	}
	public void edit(Object obj)
	{
		if(obj instanceof TcapFloat)
			edit((TcapFloat) obj);
		else if(obj instanceof Float)
			edit((Float) obj);
	}
	public void edit(Float val)
	{
		edit(new TcapFloat(val, true));
	}
	public void edit(TcapFloat val)
	{
		floatValue = val;
		if(val != null)
			setText(val.getValue());
	}
	public void save()
	{
		floatValue.setValue(getText());
	}
	public Object getValue()
	{
		return(floatValue);
	}
}