package oncotcap.display.editor.autogeneditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class JournalPanel extends DefaultEditorPanel
{
	private OncTextField journalHomeLink = null;
	private OncScrollList fullSourceProvider = null;
	private OncTextField abbreviation = null;
	private OncTextField name = null;
	private OncTextField jURLabbrev = null;
	private OncScrollableTextArea sourceLocatorMethod = null;


	public  JournalPanel() {
		super();
		editObj = new Journal();
		initUI();
		fillUiHashtable();
	}


	public  JournalPanel(Journal editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (Journal)editObj;
	initUI();
	}
	private void initUI() {
		sourceLocatorMethod = new OncScrollableTextArea(editObj, "SourceLocatorMethod", true);
		jURLabbrev = new OncTextField(editObj, "JURLabbrev", true);
		name = new OncTextField(editObj, "Name", true);
		abbreviation = new OncTextField(editObj, "Abbreviation", true);
		fullSourceProvider = new OncScrollList(FullSourceProvider.class, editObj, "FullSourceProvider", true,true, false);
		journalHomeLink = new OncTextField(editObj, "JournalHomeLink", true);
sourceLocatorMethod.setBounds(0,240,250,280);
jURLabbrev.setBounds(150,120,100,60);
name.setBounds(0,0,250,60);
abbreviation.setBounds(0,120,150,60);
fullSourceProvider.setBounds(0,520,250,60);
journalHomeLink.setBounds(0,60,250,60);
sourceLocatorMethod.setVisible(true);
jURLabbrev.setVisible(true);
name.setVisible(true);
abbreviation.setVisible(true);
fullSourceProvider.setVisible(true);
journalHomeLink.setVisible(true);
		add(sourceLocatorMethod);
		add(jURLabbrev);
		add(name);
		add(abbreviation);
		add(fullSourceProvider);
		add(journalHomeLink);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(sourceLocatorMethod, "sourceLocatorMethod");
		uiHashtable.put(jURLabbrev, "jURLabbrev");
		uiHashtable.put(name, "name");
		uiHashtable.put(abbreviation, "abbreviation");
		uiHashtable.put(fullSourceProvider, "fullSourceProvider");
		uiHashtable.put(journalHomeLink, "journalHomeLink");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		JournalPanel p = new JournalPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
