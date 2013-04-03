package oncotcap.display.editor.persistibleeditorpanel;

import javax.swing.*;
import java.awt.*;

import oncotcap.datalayer.persistible.parameter.DeclareSwitchablePositive;
import oncotcap.datalayer.persistible.parameter.DeclareVariable;
import oncotcap.datalayer.persistible.action.OncAction;
import oncotcap.display.common.LabeledTextBox;

public class SwitchablePositiveEditor extends VariableEditor
{
	private JTextArea inputField = new JTextArea();
	private JCheckBox chkBox = new JCheckBox("On");
	public SwitchablePositiveEditor()
	{
		init();
	}
	public SwitchablePositiveEditor(DeclareSwitchablePositive var)
	{
		init();
		edit(var);
	}
	public SwitchablePositiveEditor(DeclareVariable var)
	{
		init();
		edit(var);
	}
	void init()
	{
/*		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		inputField.setPreferredSize(new Dimension(100, 40));
		inputField.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));
		setAlignmentX(JComponent.LEFT_ALIGNMENT);
		add(inputField);
		add(chkBox); */

		setLayout(new BorderLayout(10, 10));
		Box varBox = Box.createHorizontalBox();
		varBox.add(new JLabel("Initial Value"));
		varBox.add(Box.createHorizontalStrut(5));
		Box chkBoxBox = Box.createVerticalBox();
		chkBoxBox.add(chkBox);
		chkBoxBox.add(Box.createVerticalGlue());
		inputField.setLineWrap(true);
		inputField.setWrapStyleWord(true);
		JScrollPane inputSP = new JScrollPane(inputField, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED ,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(varBox, BorderLayout.NORTH);
		add(chkBoxBox, BorderLayout.WEST);
		add(inputSP, BorderLayout.CENTER);

	}
	public void edit(Object var)
	{
		if(var instanceof DeclareSwitchablePositive)
			edit((DeclareSwitchablePositive) var);
	}
	public void edit(OncAction var)
	{
		if(var != null && var instanceof DeclareSwitchablePositive)
		{
			edit((DeclareSwitchablePositive) var);
		}
	}
	public void edit(DeclareVariable var)
	{
		if(var != null && var instanceof DeclareSwitchablePositive)
			edit((DeclareSwitchablePositive) var);
	}
	public void edit(DeclareSwitchablePositive var)
	{
		setVariable(var);
		if(var != null)
		{
			if(editMode == EDITPARAM && var.getValue() != null)
			{
				inputField.setText(var.getValue());
				chkBox.setSelected(var.getState());
			}
			else if(var.getInitialValue() != null)
			{
				inputField.setText(var.getInitialValue());
				chkBox.setSelected(var.getInitialState());
			}
		}
	}
	public void save()
	{
		DeclareSwitchablePositive var = (DeclareSwitchablePositive) getVariable();
		if(editMode == EDITPARAM)
		{
			var.setValue(inputField.getText());
			var.setState(chkBox.isSelected());
		}
		else
		{
			var.setInitialValue(inputField.getText());
			var.setInitialState(chkBox.isSelected());
		}
	}
}