package oncotcap.display.editor.autogeneditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class RuleTriggerEventPanel extends DefaultEditorPanel
{
	private OncTextField description = null;
	private OncTextField name = null;
	private OncTextField sentenceText = null;


	public  RuleTriggerEventPanel() {
		super();
		editObj = new RuleTriggerEvent();
		initUI();
		fillUiHashtable();
	}


	public  RuleTriggerEventPanel(RuleTriggerEvent editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (RuleTriggerEvent)editObj;
	initUI();
	}
	private void initUI() {
		sentenceText = new OncTextField(editObj, "SentenceText", true);
		name = new OncTextField(editObj, "Name", true);
		description = new OncTextField(editObj, "Description", true);
sentenceText.setBounds(0,120,250,60);
name.setBounds(0,60,250,60);
description.setBounds(0,0,250,60);
sentenceText.setVisible(true);
name.setVisible(true);
description.setVisible(true);
		add(sentenceText);
		add(name);
		add(description);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(sentenceText, "sentenceText");
		uiHashtable.put(name, "name");
		uiHashtable.put(description, "description");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		RuleTriggerEventPanel p = new RuleTriggerEventPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
