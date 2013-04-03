package oncotcap.display.editor.persistibleeditorpanel;

import java.awt.*;
import javax.swing.*;
import oncotcap.datalayer.Persistible;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.action.*;
import oncotcap.display.common.VariableListener;

public class InitVariableEditor extends EditorPanel implements ActionEditor
{
	InitVariableAction  variable;
	EditorPanel editor;
	private CodeBundleEditorPanel cbParent;
	private VariableListener variableListener = new MVVariableListener();
	private JLabel varName = new JLabel();
	private JPanel varEditPane = new JPanel();
	public InitVariableEditor()
	{
		init();
	}
	private void init()
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		Box labelBox = Box.createHorizontalBox();
		labelBox.add(new JLabel("Initialize Variable: "));
		labelBox.add(varName);
		labelBox.add(Box.createHorizontalGlue());
		add(labelBox);
		add(Box.createVerticalStrut(10));
		varEditPane.setLayout(new BorderLayout());
		add(varEditPane);
		add(Box.createVerticalGlue());
	}
	public Object getValue()
	{
		return(variable);
	}
	public void edit(Object obj)
	{
		if(obj instanceof InitVariableAction)
			edit((InitVariableAction) obj);
	}
	public void edit(OncAction act)
	{
		if(act instanceof InitVariableAction)
			edit((InitVariableAction) act);
	}
	public void edit(InitVariableAction var)
	{
		variable = var;
		if(editor != null)
			varEditPane.remove((EditorPanel) editor);
		var.addVariableListener(variableListener);
		editor = var.getVariableEditor();
		varName.setText(var.getName());
		varName.repaint();
		if(editor != null)
		{
			editor.edit(var.getNewInitialization());
			varEditPane.add((EditorPanel) editor, BorderLayout.CENTER);
			varEditPane.revalidate();
			varEditPane.repaint();
		}
	}
	public void save()
	{
		if(editor != null)
			editor.save();
	}
	public CodeBundleEditorPanel getCBParent()
	{
		return(cbParent);	
	}
	public void setCBParent(CodeBundleEditorPanel par)
	{
		cbParent = par;
	}

	private class MVVariableListener implements VariableListener
	{
		public void variableChanged(Object var)
		{
			edit(var);
		}
	}
}
 