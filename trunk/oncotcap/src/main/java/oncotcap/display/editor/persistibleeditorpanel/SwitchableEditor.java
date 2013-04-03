package oncotcap.display.editor.persistibleeditorpanel;

import javax.swing.*;
import java.awt.*;

import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.action.OncAction;
import oncotcap.datalayer.persistible.parameter.*;

public class SwitchableEditor extends VariableEditor
{
	private JCheckBox chkBox = new JCheckBox("On");
	public SwitchableEditor()
	{
		init();
	}
	public SwitchableEditor(DeclareSwitchable var)
	{
		init();
		edit(var);
	}
	public SwitchableEditor(DeclareVariable var)
	{
		init();
		edit(var);
	}
	void init()
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setAlignmentX(JComponent.LEFT_ALIGNMENT);
		chkBox.setMaximumSize(new Dimension(Short.MAX_VALUE, 20));
		add(chkBox);
	}
	public void edit(Object var)
	{
		if(var instanceof DeclareSwitchable)
		{
			edit((DeclareSwitchable) var);
		}
	}
	public void edit(OncAction var)
	{
		if(var != null && var instanceof DeclareSwitchable)
		{
			edit((DeclareSwitchable) var);
		}
	}
	public void edit(DeclareVariable var)
	{
		if(var != null && var instanceof DeclareSwitchable)
			edit((DeclareSwitchable) var);
	}
	public void edit(DeclareSwitchable var)
	{
		setVariable(var);
		chkBox.setSelected(var.getInitialState());
	}

	public void save()
	{
		DeclareSwitchable var = (DeclareSwitchable) getVariable();
		var.setInitialState(chkBox.isSelected());
	}
}