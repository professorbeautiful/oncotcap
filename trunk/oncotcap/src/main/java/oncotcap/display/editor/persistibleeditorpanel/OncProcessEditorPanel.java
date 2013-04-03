package oncotcap.display.editor.persistibleeditorpanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.LabeledComboBox;
import oncotcap.display.common.LabeledTextBox;

public class OncProcessEditorPanel extends EditorPanel
{
	ProcessDeclaration oncProcess;
	LabeledTextBox nameBox;
	LabeledComboBox superClassCombo;
	Box mainBox;
	
	public OncProcessEditorPanel()
	{
		this(new ProcessDeclaration());
	}
	public OncProcessEditorPanel(ProcessDeclaration oncProcess)
	{
		this.oncProcess = oncProcess;
		init();
	}

	public static void main(String [] args)
	{
		JFrame jf = new JFrame();
		jf.setSize(500,100);
		ProcessDeclaration op = new ProcessDeclaration();
		op.setName("BLECH");
		op.setProcessClass(oncotcap.util.ReflectionHelper.classForName("oncotcap.process.cells.AbstractCell"));
		jf.getContentPane().add(new OncProcessEditorPanel(op));
		jf.setVisible(true);
	}
	private void init()
	{
		setPreferredSize(new Dimension(600,100));
		mainBox = Box.createHorizontalBox();
		nameBox = new LabeledTextBox("Name");
		nameBox.setMaximumSize(new Dimension(Short.MAX_VALUE,40));
		nameBox.setMinimumSize(new Dimension(100, 40));
		nameBox.setPreferredSize(new Dimension(200, 40));
		superClassCombo = new LabeledComboBox("Super Class", oncotcap.process.OncProcess.getObjectList());
		if(oncProcess != null)
		{
			setOncProcess(oncProcess);
		}
		else
		{
			superClassCombo.setSelectedIndex(0);
			nameBox.setText("");
		}
		
		mainBox.add(nameBox);
		mainBox.add(superClassCombo);
		add(mainBox);
		addFocusListener(new EditorFocusListener());
	}
	public void save()
	{
		if(oncProcess == null)
			oncProcess = new ProcessDeclaration();

		Object superClass = superClassCombo.getSelectedItem();
		if(superClass instanceof Class)
		{
			oncProcess.setProcessClass((Class) superClass);
		}
		oncProcess.setName(nameBox.getText());
	}
	
	public void setOncProcess(ProcessDeclaration oncProcess)
	{
		this.oncProcess = oncProcess;
		if(oncProcess.getProcessClass() != null)
			superClassCombo.setSelectedItem(oncProcess.getProcessClass());
		else
			superClassCombo.setSelectedIndex(0);

		if(oncProcess.getName() != null)
			nameBox.setText(oncProcess.getName());
		else
			nameBox.setText("");
	}
	public ProcessDeclaration getOncProcess()
	{
		save();
		return(oncProcess);
	}
	public Object getValue()
	{
		return(oncProcess);
	}
	public void edit(Object obj) {
		if(obj instanceof ProcessDeclaration)
			edit((ProcessDeclaration) obj);
	}
	public void edit(ProcessDeclaration op)
	{
		save();
		setOncProcess(op);
	}
	class EditorFocusListener implements FocusListener
	{
		public void focusGained(FocusEvent e){}
		public void focusLost(FocusEvent e)
		{
			save();
		}
	}

}
