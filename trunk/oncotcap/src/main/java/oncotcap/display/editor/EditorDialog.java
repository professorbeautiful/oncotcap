package oncotcap.display.editor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.util.*;

public class EditorDialog extends JDialog
{
	private Container contentPane;
	private EditorPanel editor;
	private EditorPanel previousEditor = null;
	private JButton doneButton;
	private JButton cancelButton;
	private Editable returnVal;
	
	private Editable editedObject;
	
	public EditorDialog()
	{
		super(SwingUtil.getModeFrame(), true);
		init();
	}
	public EditorDialog(Editable obj)
	{
		super(SwingUtil.getModeFrame(), true);
		init();
		edit(obj);
	}

	private void init()
	{
		setSize(300,300);
		contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		doneButton = new JButton("Done");
		doneButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){ saveAndQuit(); }});
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){ editedObject = null; setVisible(false); }});
		addWindowListener(new MyWindowListener());	
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(doneButton);
		buttonPanel.add(cancelButton);		
		contentPane.add(buttonPanel, BorderLayout.SOUTH);

	}

	public void edit(Editable obj)
	{
		if(previousEditor != null)
			contentPane.remove(previousEditor);
		editedObject = obj;
		editor = obj.getEditorPanel();
		setTitle(StringHelper.className(obj.getClass().toString()));
		// 	JScrollPane scrollPane = new JScrollPane();
		// 		scrollPane.setViewportView(editor);
		contentPane.add(editor, BorderLayout.CENTER);
		editor.edit(obj);
		editor.repaint();
		previousEditor = editor;
	}

	public static Editable showEditor(Editable obj)
	{
		return(showEditor(obj, null));
	}
	public static Editable showEditor(Editable obj, Dimension size)
	{
		EditorDialog diag = new EditorDialog(obj);
		if(size != null)
			diag.setSize(size);
		else if(diag.editor.getPreferredSize().getWidth() > 0 && diag.editor.getPreferredSize().getHeight() > 0)
			diag.setSize(diag.editor.getPreferredSize());
		
		diag.setVisible(true);
		return(diag.editedObject);
	}
	private void save()
	{
		editor.save();
		editedObject.update();
	}
	private void saveAndQuit()
	{
		save();
		setVisible(false);
	}
	private class MyWindowListener implements WindowListener
	{
		public void windowActivated(WindowEvent e) {}
		public void windowClosed(WindowEvent e)
		{
			editedObject = null;
		}
		public void windowClosing(WindowEvent e)
		{
			editedObject = null;
		}
		public void windowDeactivated(WindowEvent e) {}
		public void windowDeiconified(WindowEvent e) {}
		public void windowIconified(WindowEvent e) {}
		public void windowOpened(WindowEvent e) {}
	}
}
