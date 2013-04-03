package oncotcap.display.editor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import oncotcap.util.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;

public class StatementBundleEditor extends JDialog
{
	private StatementBundle statementBundle;
	private StatementBundleEditorPanel editorPanel;
	private JButton doneButton;
	private JButton cancelButton;
	
	private static StatementBundleEditor defaultEditor = null;
	
	private StatementBundleEditor()
	{
		super(SwingUtil.getModeFrame(), true);
	}
	private StatementBundleEditor(StatementBundle sb)
	{
		super(SwingUtil.getModeFrame(), true);
		init();
		setupEditor(sb);
		
	}
	public static void main(String [] args)
	{
		edit(new StatementBundle());
	}
	private void init()
	{
		setSize(300,300);
		Container cp = getContentPane();
		cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));
		editorPanel = new StatementBundleEditorPanel();
		doneButton = new JButton("Done");
		doneButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){ saveAndQuit(); }});
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){ statementBundle = null; setVisible(false); }});
		Box buttonBox = Box.createHorizontalBox();
		buttonBox.add(doneButton);
		buttonBox.add(Box.createHorizontalStrut(5));
		buttonBox.add(cancelButton);
		
		cp.add(editorPanel);
		cp.add(Box.createVerticalStrut(5));
		cp.add(buttonBox);
	}
	private void setupEditor(StatementBundle sb)
	{
		statementBundle = sb;
		editorPanel.edit(sb);
	}
	public static StatementBundle edit(StatementTemplate st)
	{
		StatementBundle sb = new StatementBundle(st);
		return(edit(sb));
	}
	public static StatementBundle edit(StatementBundle sb)
	{
		if(defaultEditor == null)
			defaultEditor = new StatementBundleEditor(sb);
		else
			defaultEditor.setupEditor(sb);

		defaultEditor.setVisible(true);
		return(defaultEditor.statementBundle);
	}
	private void save()
	{
		editorPanel.save();
		statementBundle = editorPanel.getStatementBundle();
	}
	private void saveAndQuit()
	{
		save();
		setVisible(false);
	}
}