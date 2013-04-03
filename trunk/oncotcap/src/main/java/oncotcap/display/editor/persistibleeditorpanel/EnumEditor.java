package oncotcap.display.editor.persistibleeditorpanel;

import javax.swing.*;
import java.awt.*;

import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.action.OncAction;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.display.common.LabeledTextBox;

public class EnumEditor extends VariableEditor
{
	
	private JTextArea inputField = new JTextArea();
	private JLabel varNameLabel = new JLabel();
	public EnumEditor()
	{
		init();
	}
	public EnumEditor(DeclareEnum var)
	{
		init();
		edit(var);
	}
	public EnumEditor(DeclareVariable var)
	{
		init();
		edit(var);
	}
	void init()
	{
		setLayout(new BorderLayout(10,10));
		//setMinimumSize(new Dimension(200, 45));
		//setMaximumSize(new Dimension(Short.MAX_VALUE, 45));
		//inputField.setPreferredSize(new Dimension(300, 40));
		Box hBox = Box.createHorizontalBox();
		hBox.add(varNameLabel);
		inputField.setLineWrap(true);
		inputField.setWrapStyleWord(true);
		hBox.add(Box.createHorizontalGlue());
		JScrollPane inputSP = new JScrollPane(inputField, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED ,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		//hBox.add(inputField);
		add(hBox, BorderLayout.NORTH);
		add(Box.createHorizontalStrut(40), BorderLayout.WEST);
		add(inputSP, BorderLayout.CENTER);
	}
	public void edit(Object var)
	{
		if(var instanceof DeclareEnum)
		{
			edit((DeclareEnum) var);
		}
	}
	public void edit(OncAction var)
	{
		if(var != null && var instanceof DeclareEnum)
		{
			edit((DeclareEnum) var);
		}
	}
	public void edit(DeclareVariable var)
	{
		if(var != null && var instanceof DeclareEnum)
			edit((DeclareEnum) var);
	}
	public void edit(DeclareEnum var)
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
		if(getVariable() == null)
			setVariable(new DeclareEnum());
		
		DeclareEnum var = (DeclareEnum) getVariable();
		if(editMode == EDITPARAM)
			var.setValue(inputField.getText());
		else
			var.setInitialValue(inputField.getText());
	}
}