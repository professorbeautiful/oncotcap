package oncotcap.display.editor.persistibleeditorpanel;

import javax.swing.*;
import java.awt.*;

import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.action.OncAction;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.display.common.LabeledTextBox;

public class PositiveIntegerEditor extends VariableEditor
{
	private JTextArea inputField = new JTextArea();

	public PositiveIntegerEditor()
	{
		init();
	}
	public PositiveIntegerEditor(DeclarePositiveInteger var)
	{
		init();
		edit(var);
	}
	public PositiveIntegerEditor(DeclareVariable var)
	{
		init();
		edit(var);
	}
	void init()
	{
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
		if(var instanceof DeclarePositiveInteger)
		{
			edit((DeclarePositiveInteger) var);
		}
	}
	public void edit(OncAction var)
	{
		if(var != null && var instanceof DeclarePositiveInteger)
		{
			edit((DeclarePositiveInteger) var);
		}
	}
	public void edit(DeclareVariable var)
	{
		if(var != null && var instanceof DeclarePositiveInteger)
			edit((DeclarePositiveInteger) var);
	}
	public void edit(DeclarePositiveInteger var)
	{
		setVariable(var);
		inputField.setText(var.getInitialValue());
	}
	public void save()
	{
		DeclarePositiveInteger var = (DeclarePositiveInteger) getVariable();
		var.setInitialValue(inputField.getText());
	}
}