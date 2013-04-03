package oncotcap.display.editor.persistibleeditorpanel;

import javax.swing.*;
import java.awt.Dimension;

import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.action.*;
import oncotcap.datalayer.persistible.parameter.*;

public class ModifyEnumEditor extends AbstractVariableEditor implements ModifyVariableEditor
{
	protected JTextArea modification = new JTextArea();
	protected JLabel modifier = new JLabel("=");
	protected JLabel varName = new JLabel();
	
	public ModifyEnumEditor()
	{
		init();
	}
	private ModifyEnumEditor(ModifyEnum mod)
	{
		init();
	}
	private void init()
	{
		modification.setMinimumSize(new Dimension(200,25));
		modification.setPreferredSize(new Dimension(300,25));
		add(varName);
		add(modifier);
		add(modification);
	}

	public void editAction(ModifyVariableAction modVar)
	{
		VariableModification mod = modVar.getModification();
		if(mod != null && mod instanceof ModifyEnum)
		{
			varName.setText(modVar.getName());
			modification.setText(((ModifyEnum) mod).getModification());
		}
	}
	public void save()
	{
		if(modifyAction != null)
		{
//			System.out.println("Modification Action Saved: " + modification.getText());
			((ModifyEnum) modifyAction.getModification()).setModification(modification.getText());
		}
		else
			System.out.println("Modification not saved due to null");
	}
	public VariableModification getVariableModification()
	{
		return(modifyAction.getModification());
	}
}