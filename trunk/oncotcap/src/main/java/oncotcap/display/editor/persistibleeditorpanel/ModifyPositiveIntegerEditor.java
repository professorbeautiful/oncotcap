package oncotcap.display.editor.persistibleeditorpanel;

import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.datalayer.persistible.action.*;

public class ModifyPositiveIntegerEditor extends ModifyPositiveEditor implements ModifyVariableEditor
{
	public ModifyPositiveIntegerEditor()
	{
		init();
	}

	private void init()
	{
		modifier.setText("+=");
	}

	public void editAction(ModifyVariableAction obj)
	{
		VariableModification mod = obj.getModification();
		if(mod != null && mod instanceof ModifyPositiveInteger)
		{
			varName.setText(obj.getName());
			modification.setText(((ModifyPositiveInteger) mod).getModification());
		}
	}

	public void save()
	{
		((ModifyPositiveInteger) modifyAction.getModification()).setModification(modification.getText());
	}
	public VariableModification getVariableModification()
	{
		return(modifyAction.getModification());
	}
}