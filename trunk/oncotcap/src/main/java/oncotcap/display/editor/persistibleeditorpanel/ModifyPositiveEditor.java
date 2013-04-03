package oncotcap.display.editor.persistibleeditorpanel;

import javax.swing.*;
import java.awt.*;

import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.action.*;
import oncotcap.datalayer.persistible.parameter.*;

public class ModifyPositiveEditor extends AbstractVariableEditor implements ModifyVariableEditor
{
	protected JTextArea modification = new JTextArea();
	protected JLabel modifier = new JLabel("*=");
	protected JLabel varName = new JLabel();
	
	public ModifyPositiveEditor()
	{
		init();
	}
	private ModifyPositiveEditor(ModifyPositive mod)
	{
		init();
	}
	private void init()
	{
		setLayout(new BorderLayout(10,10));
		//modification.setMinimumSize(new Dimension(300,200));
		//modification.setPreferredSize(new Dimension(400,200));
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
		if(mod != null && mod instanceof ModifyPositive)
		{
			varName.setText(modVar.getName());
			modification.setText(((ModifyPositive) mod).getModification());
		}
	}
	public void save()
	{
		if(modifyAction != null && modifyAction.getModification() != null && modifyAction.getModification() instanceof ModifyPositive)
		{
			VariableModification mod = modifyAction.getModification();
			((ModifyPositive) mod).setModification(modification.getText());
//			((ModifyPositive) modifyAction.getModification()).setModification(modification.getText());
	//		((ModifyPositive) modifyAction.getModification()).update();
		}
	}
	public VariableModification getVariableModification()
	{
		return(modifyAction.getModification());
	}
}