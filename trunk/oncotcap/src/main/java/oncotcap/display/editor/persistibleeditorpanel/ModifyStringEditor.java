package oncotcap.display.editor.persistibleeditorpanel;

import javax.swing.*;
import java.awt.*;

import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.datalayer.persistible.action.*;

public class ModifyStringEditor extends AbstractVariableEditor implements ModifyVariableEditor
{
	protected JTextArea modification = new JTextArea();
	protected JLabel modifier = new JLabel("=");
	protected JLabel varName = new JLabel();
	
	public ModifyStringEditor()
	{
		init();
	}
	private ModifyStringEditor(ModifyString mod)
	{
		init();
	}
	private void init()
	{
		setLayout(new BorderLayout(10, 10));
		Box varBox = Box.createHorizontalBox();
		varBox.add(varName);
		varBox.add(Box.createHorizontalStrut(5));
		varBox.add(modifier);
		modification.setLineWrap(true);
		modification.setWrapStyleWord(true);
		JScrollPane modificationSP = new JScrollPane(modification, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED ,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(varBox, BorderLayout.NORTH);
		add(Box.createHorizontalStrut(40), BorderLayout.WEST);
		add(modificationSP, BorderLayout.CENTER);
	}

	public void editAction(ModifyVariableAction modVar)
	{
		VariableModification mod = modVar.getModification();
		
		if(mod != null && mod instanceof ModifyString)
		{
			varName.setText(modVar.getName());
			modification.setText(((ModifyString)mod).getModification());
		}
	}
	public void save()
	{
		((ModifyString) modifyAction.getModification()).setModification(modification.getText());
	}
	public VariableModification getVariableModification()
	{
		return(modifyAction.getModification());
	}
}