package oncotcap.display.editor.persistibleeditorpanel;

import oncotcap.datalayer.persistible.action.VariableModification;

public interface ModifyVariableEditor extends ActionEditor
{
	public VariableModification getVariableModification();
}