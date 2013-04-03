package oncotcap.display.editor.persistibleeditorpanel;

import javax.swing.*;

import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.action.*;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.datalayer.*;

public class ModifySwitchableEditor extends AbstractVariableEditor implements ModifyVariableEditor
{
	private JCheckBox switchBox = new JCheckBox();
	private JLabel modifier = new JLabel();
	private JLabel varName = new JLabel();
	
	public ModifySwitchableEditor()
	{
		init();
	}
	private void init()
	{
		add(varName);
		add(modifier);
		add(switchBox);
	}

/*	public void editAction(ModifyVariableAction var)
	{
		DeclareVariable origVar = var.getVariable();
		VariableModification mod = var.getModification();
		if(mod != null && mod instanceof ModifySwitchable)
			editAction((ModifySwitchable) mod, origVar);
	} */
	public void editAction(ModifyVariableAction modVar)
	{
		
		VariableModification mod = modVar.getModification();
		Persistible origVar = modVar.getVariable();

		if(mod != null && mod instanceof ModifySwitchable && origVar != null && origVar instanceof DeclareSwitchable)
		{
			DeclareSwitchable var = (DeclareSwitchable) origVar;
			varName.setText("Turn " + var.getName() + " ");
			if(var.getInitialState())
				modifier.setText("OFF");
			else
				modifier.setText("ON");

			switchBox.setSelected(((ModifySwitchable) mod).getSwitch());
		}
		else
			System.out.println("WARNING: trying to edit " + ((mod != null) ? mod.getClass().getName() : "null") + " with ModifySwitchableEditor");
	}
	public void save()
	{
		((ModifySwitchable) modifyAction.getModification()).setSwitch(switchBox.isSelected());
	}
	public VariableModification getVariableModification()
	{
		return(modifyAction.getModification());
	}
}