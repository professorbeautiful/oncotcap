package oncotcap.display.editor.persistibleeditorpanel; 

import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.action.*;
import oncotcap.datalayer.persistible.parameter.*;

public abstract class AbstractVariableEditor extends EditorPanel implements ActionEditor
{
	protected ModifyVariableAction modifyAction;
	private CodeBundleEditorPanel cbParent;
		
	protected AbstractVariableEditor()
	{
		setPreferredSize(null);
	}

	public abstract void editAction(ModifyVariableAction var);
	public final void edit(Object action)
	{
		if(action instanceof ModifyVariableAction)
			edit((ModifyVariableAction) action);
	}
	public final void edit(OncAction action)
	{
		if(action instanceof ModifyVariableAction)
			edit((ModifyVariableAction) action);
	}
	public final void edit(ModifyVariableAction action)
	{
		modifyAction = action;
		editAction(action);
	}
	public final Object getValue()
	{
		return(modifyAction);
	}
	public ActionType getType()
	{
		return(modifyAction.getType());
	}
	public static AbstractVariableEditor createEditor(DeclareVariable var)
	{
		AbstractVariableEditor edit = var.getType().getModifierEditorInstance();
		VariableModification mod = var.getType().getModifierStorageInstance();
		ModifyVariableAction modAction = new ModifyVariableAction();
		modAction.setVariable(var);
		edit.edit(modAction);
		return(edit);
	}
/*	public static AbstractVariableEditor createEditor(ModifyVariableAction modVarAction)
	{
		AbstractVariableEditor edit = modVarAction.getModification().getType().getModifierEditorInstance();
		edit.edit(modVarAction);
		return(edit);	
	}
*/
	public void save()
	{
			// FUTURE - put in proper save methods to be consistent
			
	}
	public CodeBundleEditorPanel getCBParent()
	{
		return(cbParent);	
	}
	public void setCBParent(CodeBundleEditorPanel par)
	{
		cbParent = par;
	}
}

