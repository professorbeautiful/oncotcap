package oncotcap.display.editor;

import javax.swing.*;
import java.awt.event.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.util.*;

public class OncProcessEditor extends JDialog
{
	private OncProcessEditorPanel processPanel;
	private Box buttonBox;
	private Box mainBox;
	private JButton btnOK;
	private JButton btnCancel;
	private static OncProcessEditor editor = null;
	private boolean cancelled = false;
	
	private OncProcessEditor()
	{
		this(SwingUtil.getModeFrame(), "Edit a Process");
	}
	private OncProcessEditor(JFrame frame, String title)
	{
		super(frame, title, true);
		init();
	}
	public static void main(String [] args)
	{
		ProcessDeclaration op = OncProcessEditor.edit();
		System.out.println(op.getName() + " " + op.getProcessClass());
		ProcessDeclaration op2 = OncProcessEditor.edit(op);
		System.out.println(op2.getName() + " " + op2.getProcessClass());
	}
	private void init()
	{
		setSize(600,150);
		mainBox = Box.createVerticalBox();
		processPanel = new OncProcessEditorPanel();
		buttonBox = Box.createHorizontalBox();
		btnOK = new JButton("OK");
		ButtonListener buttonListen = new ButtonListener();
		btnOK.addActionListener(buttonListen);
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(buttonListen);
		buttonBox.add(btnOK);
		buttonBox.add(Box.createHorizontalStrut(10));
		buttonBox.add(btnCancel);
		mainBox.add(processPanel);
		mainBox.add(buttonBox);
		mainBox.add(Box.createVerticalStrut(10));
		getContentPane().add(mainBox);
	}
	public static ProcessDeclaration edit()
	{
		return(edit(new ProcessDeclaration()));
	}
	public static ProcessDeclaration edit(ProcessDeclaration op)
	{
		if(editor == null)
			editor = new OncProcessEditor();

		editor.cancelled = false;
		editor.processPanel.setOncProcess(op);
		editor.setVisible(true);
		if(editor.cancelled)
			return(null);
		else
			return(editor.getOncProcess());
	}
	private ProcessDeclaration getOncProcess()
	{
		return(processPanel.getOncProcess());
	}

	private class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			Object src = e.getSource();
			if(src instanceof JButton)
			{
				JButton button = (JButton) src;
				if(button.getText().equalsIgnoreCase("OK"))
				{
					setVisible(false);
				}
				else if(button.getText().equalsIgnoreCase("Cancel"))
				{
					cancelled = true;
					setVisible(false);
				}
			}
		}
	}// end of ButtonListener class
}// end of OncProcessEditor class
