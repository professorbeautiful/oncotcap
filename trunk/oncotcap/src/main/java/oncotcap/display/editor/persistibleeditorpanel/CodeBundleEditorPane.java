package oncotcap.display.editor.persistibleeditorpanel;

import javax.swing.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.action.*;

public class CodeBundleEditorPane extends JTextArea implements ActionEditor
{
	private CodeBundleEditorPanel cbParent;	

	public CodeBundleEditorPane()
	{
		init();
	}
	public CodeBundleEditorPane(oncotcap.datalayer.persistible.action.BlankAction action)
	{
		init();
	}
	private void init()
	{
		setTabSize(1);
	}
	public void edit(Object action){}
	public void edit(OncAction action){}
	public void save(){}
	public CodeBundleEditorPanel getCBParent()
	{
		return(cbParent);	
	}
	public void setCBParent(CodeBundleEditorPanel par)
	{
		cbParent = par;
	}
}