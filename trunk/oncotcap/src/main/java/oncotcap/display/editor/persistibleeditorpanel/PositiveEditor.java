package oncotcap.display.editor.persistibleeditorpanel;

import javax.swing.*;
import java.awt.*;

import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.action.OncAction;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.display.common.LabeledTextBox;

public class PositiveEditor extends VariableEditor
{

	private JTextArea inputField = new JTextArea();
//	private LabeledTextBox inputField = new LabeledTextBox("Initial Value");
	private JLabel varNameLabel = new JLabel();
	public PositiveEditor()
	{
			//System.out.println("init posistiveEditor " );
			init();
	}
	public PositiveEditor(DeclarePositive var)
	{
		init();
		edit(var);
	}
	public PositiveEditor(DeclareVariable var)
	{
		init();
		edit(var);
	}
	void init()
	{
		setLayout(new BorderLayout(10, 10));
		Box varBox = Box.createHorizontalBox();
		varBox.add(varNameLabel);
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
		if(var instanceof DeclarePositive)
		{
			edit((DeclarePositive) var);
		}
	}
	public void edit(OncAction var)
	{
		if(var != null && var instanceof DeclarePositive)
		{
			edit((DeclarePositive) var);
		}
	}
	public void edit(DeclareVariable var)
	{
		if(var != null && var instanceof DeclarePositive)
			edit((DeclarePositive) var);
	}
	public void edit(DeclarePositive var)
	{
		setVariable(var);
		if(var != null)
		{
			varNameLabel.setText(var.getName() + " = ");
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
			//System.out.println("PositiveEditor save" + getVariable() );
		if(getVariable() == null)
			setVariable(new DeclarePositive());
		
		DeclarePositive var = (DeclarePositive) getVariable();
		if(editMode == EDITPARAM)
			var.setValue(inputField.getText());
		else
			var.setInitialValue(inputField.getText());
	}
}
