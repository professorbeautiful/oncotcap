package oncotcap.display.editor.persistibleeditorpanel;

import oncotcap.datalayer.persistible.parameter.DeclareVariable;

public abstract class VariableEditor extends EditorPanel implements ActionEditor
{
	public static final int INITVAL = 1;
	public static final int EDITPARAM = 2;
	protected int editMode = INITVAL;

	DeclareVariable variable;
	private CodeBundleEditorPanel cbParent;

	abstract void init();
	abstract void edit(DeclareVariable var);
	public void setVariable(DeclareVariable var)
	{
		this.variable = var;
	}
	public void setEditMode(int mode)
	{
		editMode = mode;
		if(variable != null)
			edit(variable);
	}
  	public DeclareVariable getVariable()
	{
		return(variable);
	}
	public Object getValue()
	{
		return(getVariable());
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