package oncotcap.display.editor.persistibleeditorpanel;

import javax.swing.*;
import java.awt.*;
import oncotcap.datalayer.persistible.parameter.TcapString;

public class StringEditorPanel extends SingleValueEditorPanel
{
	private TcapString stringValue;
	
	public StringEditorPanel()
	{
		super();
		init();
	}
	private JTextArea inputField = new JTextArea();

	private void init()
	{
		remove(valueBox);

		setLayout(new BorderLayout(10, 10));
		Box varBox = Box.createHorizontalBox();
		varBox.add(new JLabel("Enter a string value"));
		varBox.add(Box.createHorizontalStrut(5));
		inputField.setLineWrap(true);
		inputField.setWrapStyleWord(true);
		JScrollPane inputSP = new JScrollPane(inputField, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED ,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(varBox, BorderLayout.NORTH);
		add(Box.createHorizontalStrut(40), BorderLayout.WEST);
		add(inputSP, BorderLayout.CENTER);	
	}
	public void edit(Object obj)
	{
		if(obj instanceof TcapString)
			edit((TcapString) obj);
		else if(obj instanceof String)
			edit((String) obj);
	}
	public void edit(TcapString val)
	{
		stringValue = val;
		setText(val.toString());
	}
	public void edit(String val)
	{
		edit(new TcapString(val));
	}
	public void save()
	{
		stringValue.setValue(getText());
	}
	public Object getValue()
	{
		return(stringValue);
	}
	protected void setText(String text)
	{
		inputField.setText(text);
	}
	protected String getText()
	{
		return(inputField.getText());
	}
}