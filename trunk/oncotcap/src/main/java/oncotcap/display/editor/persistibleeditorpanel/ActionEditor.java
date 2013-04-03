package oncotcap.display.editor.persistibleeditorpanel;

import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.action.*;

public interface ActionEditor extends OncEditor
{
//	public ActionType getType();
//	public OncAction edit();
	public void edit(Object actionToEdit);
	public void edit(OncAction actionToEdit);
	public void save();
	public CodeBundleEditorPanel getCBParent();
	public void setCBParent(CodeBundleEditorPanel parent);
}