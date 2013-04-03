package oncotcap.display.editor.persistibleeditorpanel;

import java.awt.*;
import javax.swing.*;
import oncotcap.datalayer.Persistible;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.action.*;
import oncotcap.display.common.*;


public class GenericCodeActionEditor extends EditorPanel implements ActionEditor {
	AddGenericCode  genericCode;
	private CodeBundleEditorPanel cbParent;
	private VariableListener variableListener = new MVVariableListener();
	private JLabel varName = new JLabel();
	private JCheckBox insideMethodCB = new JCheckBox();
	private OncScrollableTextArea codeTextArea = null;
	private JPanel topPanel = new JPanel(new GridBagLayout());

	public GenericCodeActionEditor()
	{
		init();
	}
	private void init()
	{
		setLayout(new BorderLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1.0;
		topPanel.add(new JLabel("Add generic code "), gbc);
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		gbc.weightx = 0.0;
		topPanel.add(new JLabel("Add code inside method? "), gbc);
		gbc.weightx = 1.0;
		topPanel.add(insideMethodCB, gbc);
		codeTextArea = 
				new OncScrollableTextArea(false, true);
    codeTextArea.setLabel("Enter generic code:");

		add(codeTextArea, BorderLayout.CENTER);
		add(topPanel, BorderLayout.NORTH);
	}
	public Object getValue()
	{
		return(genericCode);
	}
	public void edit(Object obj)
	{
		if(obj instanceof AddGenericCode)
			edit((AddGenericCode) obj);
	}
	public void edit(OncAction act)
	{
		if(act instanceof AddGenericCode)
			edit((AddGenericCode) act);
	}
	public void edit(AddGenericCode var)
	{
			genericCode = var;
			codeTextArea.setValue(genericCode.getGenericCode());
			insideMethodCB.setSelected(genericCode.getAddCodeInsideMethod());
	}
	public void save()
	{
			// Get values from ui objects and place in the persistible
			// and update persistible
			genericCode.setGenericCode((String)codeTextArea.getValue());
			genericCode.setAddCodeInsideMethod(insideMethodCB.isSelected());
			genericCode.update();	
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
