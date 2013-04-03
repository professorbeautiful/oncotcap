package oncotcap.display.editor.persistibleeditorpanel;

import javax.swing.*;
import java.awt.*;

import oncotcap.datalayer.persistible.action.*;
import oncotcap.datalayer.persistible.parameter.VariableType;
import oncotcap.display.common.LabeledTextBox;

public class AddVariableEditor extends EditorPanel implements ActionEditor
{
	LabeledTextBox name;

	AddVariableAction actionToEdit;
//	JPanel initVal_EnumProps = new JPanel();
	JPanel editorPanel = new JPanel();
	private CodeBundleEditorPanel cbParent;
	
	private static final String blankLabel = "";
	
	public static void main(String [] args)
	{
		JFrame jf = new JFrame();
		jf.setSize(400,300);
		jf.getContentPane().add(new AddVariableEditor());
		jf.setVisible(true);
	}
	public AddVariableEditor()
	{
		init();
	}
	public AddVariableEditor(AddVariableAction action)
	{
		init();
		edit(action);
	}

	private void init()
	{
	/*	setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		initVal_EnumProps.setLayout(new BoxLayout(initVal_EnumProps, BoxLayout.X_AXIS));
		editorPanel.setLayout(new BoxLayout(editorPanel, BoxLayout.Y_AXIS));
		editorPanel.add(Box.createVerticalStrut(5));
		editorPanel.add(new JLabel("Initial Value"));
		initVal_EnumProps.add(editorPanel);
		name = new LabeledTextBox("Variable Name");
		name.setPreferredSize(new Dimension(100,40));
		name.setMinimumSize(new Dimension(100, 40));
		name.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));
		name.setAlignmentY(Component.TOP_ALIGNMENT);
		JPanel labelPanel = new JPanel();
		labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.X_AXIS));
		labelPanel.add(name);
		add(labelPanel);
		add(initVal_EnumProps);
		add(Box.createHorizontalStrut(5));
		*/

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		Box labelBox = Box.createHorizontalBox();
		name = new LabeledTextBox("Variable Name");
		name.setPreferredSize(new Dimension(100,40));
		name.setMinimumSize(new Dimension(100, 40));
		name.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));
//		name.setAlignmentY(Component.TOP_ALIGNMENT);
		labelBox.add(name);
		labelBox.add(Box.createHorizontalGlue());
//		Box ivLabelBox = Box.createHorizontalBox();
//		ivLabelBox.add(new JLabel("Initial Value"));
//		ivLabelBox.add(Box.createHorizontalGlue());
		editorPanel.setLayout(new BorderLayout());
		add(labelBox);
//		add(ivLabelBox);
		add(Box.createVerticalStrut(10));
		add(editorPanel);
	}

	public void edit(Object action)
	{
		if(action instanceof AddVariableAction)
			edit((AddVariableAction) action);
	}
	public void edit(OncAction action)
	{
		if(action instanceof AddVariableAction)
			edit((AddVariableAction) action);
	}
	public void edit(AddVariableAction addVarAction)
	{
		actionToEdit = addVarAction;
		editorPanel.removeAll();
		if(addVarAction.getVariable() != null)
		{
//			if(addVarAction.getVariableType() == VariableType.ENUM)
//				name.setLabel("Parameter Display Name");
//			else
//				name.setLabel("Variable Name");
			name.setText(addVarAction.getVariable().getName());
			variableEditor = addVarAction.getVariableType().getEditor();
			variableEditor.edit(addVarAction.getVariable());
			editorPanel.add(variableEditor, BorderLayout.CENTER);
			variableEditor.revalidate();
			variableEditor.repaint();
		}
	}
	public Object getValue()
	{
		return(actionToEdit);
	}
	public void save()
	{
		if(variableEditor != null)
		{
			variableEditor.save();
			variableEditor.getVariable().setName(name.getText());
			variableEditor.getVariable().update();
//			actionToEdit.setVariable(variableEditor.getVariable());
		}
	}
	public ActionType getType()
	{
		return(ActionType.ADD_VARIABLE);
	}
	public CodeBundleEditorPanel getCBParent()
	{
		return(cbParent);	
	}
	public void setCBParent(CodeBundleEditorPanel par)
	{
		cbParent = par;
	}
	private VariableType previousType = null;
	private VariableEditor previousVariableEditor = null;
	private VariableEditor variableEditor;
	private boolean firstTime = true;
}

