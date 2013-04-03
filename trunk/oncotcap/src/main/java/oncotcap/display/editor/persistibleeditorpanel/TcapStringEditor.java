package oncotcap.display.editor.persistibleeditorpanel;

import javax.swing.*;
import java.awt.*;

import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.action.OncAction;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.display.common.LabeledTextBox;

public class TcapStringEditor extends VariableEditor
{
	
	private JTextArea inputField = new JTextArea();
	public TcapStringEditor()
	{
		init();
	}
	public TcapStringEditor(TcapString var)
	{
		init();
		edit(var);
	}
	public TcapStringEditor(DeclareVariable var)
	{
		init();
		edit(var);
	}
	void init()
	{
/*		setMinimumSize(new Dimension(200, 45));
		setMaximumSize(new Dimension(Short.MAX_VALUE, 45));
		inputField.setPreferredSize(new Dimension(100, 40));
		Box hBox = Box.createHorizontalBox();
		hBox.add(varNameLabel);
		hBox.add(inputField);
		add(hBox); */
		
		setLayout(new BorderLayout(10, 10));
		Box varBox = Box.createHorizontalBox();
		varBox.add(new JLabel("Initial Value"));
		varBox.add(Box.createHorizontalStrut(5));
		inputField.setLineWrap(true);
		inputField.setWrapStyleWord(true);
		JScrollPane inputSP = new JScrollPane(inputField, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED ,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(varBox, BorderLayout.NORTH);
		add(Box.createHorizontalStrut(40), BorderLayout.WEST);
		add(inputSP, BorderLayout.CENTER);

	}
	public void edit(Object var)
	{
		if(var instanceof TcapString)
		{
			edit((TcapString) var);
		}
	}
	public void edit(OncAction var)
	{
		if(var != null && var instanceof TcapString)
		{
			edit((TcapString) var);
		}
	}
	public void edit(DeclareVariable var)
	{
		if(var != null && var instanceof TcapString)
			edit((TcapString) var);
	}
	public void edit(TcapString var)
	{
		setVariable(var);
		if(var != null)
		{
			if(editMode == EDITPARAM && var.getValue() != null)
				inputField.setText(var.getValue());
			else if(var.getInitialValue() != null)
				inputField.setText(var.getInitialValue());
		}
	}
	public String getInitVal()
	{
		return(inputField.getText());
	}
	public void save()
	{
		if(getVariable() == null)
			setVariable(new TcapString());
		
		TcapString var = (TcapString) getVariable();
		if(editMode == EDITPARAM)
			var.setValue(inputField.getText());
		else
			var.setInitialValue(inputField.getText());
	}
}