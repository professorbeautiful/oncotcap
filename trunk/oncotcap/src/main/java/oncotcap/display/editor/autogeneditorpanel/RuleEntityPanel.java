package oncotcap.display.editor.autogeneditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class RuleEntityPanel extends DefaultEditorPanel
{
	private OncTextField sentenceText = null;


	public  RuleEntityPanel() {
		super();
		editObj = new RuleEntity();
		initUI();
		fillUiHashtable();
	}


	public  RuleEntityPanel(RuleEntity editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (RuleEntity)editObj;
	initUI();
	}
	private void initUI() {
		sentenceText = new OncTextField(editObj, "SentenceText", true);
sentenceText.setBounds(0,0,250,60);
sentenceText.setVisible(true);
		add(sentenceText);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(sentenceText, "sentenceText");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		RuleEntityPanel p = new RuleEntityPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
