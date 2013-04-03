package oncotcap.display.editor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import oncotcap.util.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;

public class CodeBundleEditor extends JDialog
{
	private CodeBundle codeBundle;
	private CodeBundleEditorPanel editorPanel;
	private JButton doneButton;
	private JButton cancelButton;
	
	private static CodeBundleEditor defaultEditor = null;
	
	private CodeBundleEditor()
	{
		super(SwingUtil.getModeFrame(), true);
	}
	private CodeBundleEditor(CodeBundle cb)
	{
		super(SwingUtil.getModeFrame(), true);
		init();
		setupEditor(cb);
		
	}
	public static void main(String [] args)
	{
		edit(new CodeBundle());
	}
	private void init()
	{
		setSize(300,300);
		Container cp = getContentPane();
		cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));
		editorPanel = new CodeBundleEditorPanel();
		doneButton = new JButton("Done");
		doneButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){ saveAndQuit(); }});
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){ codeBundle = null; setVisible(false); }});
		Box buttonBox = Box.createHorizontalBox();
		buttonBox.add(doneButton);
		buttonBox.add(Box.createHorizontalStrut(5));
		buttonBox.add(cancelButton);
		
		cp.add(editorPanel);
		cp.add(Box.createVerticalStrut(5));
		cp.add(buttonBox);
	}
	private void setupEditor(CodeBundle cb)
	{
		codeBundle = cb;
		editorPanel.edit(cb);
	}
	public static CodeBundle edit(CodeBundle cb)
	{
		if(defaultEditor == null)
			defaultEditor = new CodeBundleEditor(cb);
		else
			defaultEditor.setupEditor(cb);

		defaultEditor.setVisible(true);
		return(defaultEditor.codeBundle);
	}
	private void save()
	{
		editorPanel.save();
		codeBundle = editorPanel.getCodeBundle();
	}
	private void saveAndQuit()
	{
		save();
		setVisible(false);
	}
}